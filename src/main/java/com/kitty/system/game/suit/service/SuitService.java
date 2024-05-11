
package com.kitty.system.game.suit.service;

import com.kitty.system.game.suit.message.vo.SuitEasy;
import java.util.Comparator;
import com.kitty.system.game.suit.message.RespSuit;
import java.util.Collection;
import java.util.ArrayList;
import java.util.stream.Collector;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.text.MessageFormat;
import java.util.Iterator;
import com.kitty.listener.event.DelEquipEvent;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.common.utils.text.Convert;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.suit.message.RespSuitInfo;
import com.kitty.listener.event.LoginEvent;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.suit.domain.Suit;
import java.util.Map;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class SuitService
{
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BagService bagService;
    @Autowired
    private EquipService equipService;
    static String SWITCHOVER_TIPS;
    
    public Map<Long, Suit> getSuit(final long id) {
        Map<Long, Suit> suitMap = DataCache.SUIT_MAP.get(id);
        if (suitMap == null) {
            final List<Suit> list = this.dao.query(Suit.class, Cnd.where("r_id", "=", (Object)id));
            suitMap = list.stream().collect(Collectors.toMap(Suit::getId, Suit -> Suit));
            DataCache.SUIT_MAP.put(id, suitMap);
            return suitMap;
        }
        return suitMap;
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        final int star = role.getExtendBox().getStar();
        if (star >= 8) {
            final int suit = role.getExtendBox().getDayQuotaLimit().getInt("suit", 0);
            MessagePusher.pushMessage(role.getId(), new RespSuitInfo((byte)1, suit));
        }
    }
    
    public void firstOpenSuit(final Role role) {
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        if (suitMap == null || suitMap.size() > 0) {
            return;
        }
        final Suit suit = this.addSuit(role.getId(), suitMap, (byte)1);
        final List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            final WearEquip wearEquip = wearEquips.get(i);
            suit.getEquip().add(wearEquip.getId());
        }
        suit.save();
        MessagePusher.pushMessage(role.getId(), new RespSuitInfo((byte)1, role.getExtendBox().getDayQuotaLimit().getInt("suit", 0)));
        MessagePusher.pushMessage(role.getId(), new RespMsg("恭喜你开启套装方案"));
    }
    
    public void addSuit(final Role role) {
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        if (suitMap == null) {
            return;
        }
        if (suitMap.size() >= 12) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("套装已上限"));
            return;
        }
        if (!this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, Const.SUIT_REPAIR.get(suitMap.size()), "开启套装方案")) {
            return;
        }
        this.addSuit(role.getId(), suitMap, (byte)0);
        this.suit(role);
    }
    
    public Suit addSuit(final long id, final Map<Long, Suit> suitMap, final byte state) {
        if (suitMap == null || suitMap.size() >= 12) {
            return null;
        }
        final Suit suit = new Suit(this.worker.nextId(), id, "套装" + Convert.getChineseFromArabic(suitMap.size() + 1), state);
        suit.save();
        suitMap.put(suit.getId(), suit);
        return suit;
    }
    
    @EventHandler({ EventType.CHANGE_EQUIP_WEAR })
    public void handleChangeEquipEvent(final ChangeEquipEvent changeEquipEvent) {
        final Role role = changeEquipEvent.getRole();
        final List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        if (suitMap.size() <= 0) {
            return;
        }
        final Suit suit = suitMap.values().stream().filter(e -> e.getState() == 1).findFirst().orElse(null);
        suit.getEquip().clear();
        for (int i = 0; i < wearEquips.size(); ++i) {
            final WearEquip wearEquip = wearEquips.get(i);
            suit.getEquip().add(wearEquip.getId());
        }
        suit.save();
    }
    
    @EventHandler({ EventType.DEL_EQUIP })
    public void handleDelEquipEvent(final DelEquipEvent delEquipEvent) {
        final Role role = delEquipEvent.getRole();
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        for (final Long key : suitMap.keySet()) {
            final Suit suit = suitMap.get(key);
            if (suit.getEquip().contains(delEquipEvent.getId())) {
                suit.getEquip().removeIf(e -> e.equals(delEquipEvent.getId()));
                suit.save();
            }
        }
    }
    
    public void switchover(final Role role, final long id) {
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        final Suit suit = suitMap.get(id);
        if (suit == null || suit.getState() == 1) {
            return;
        }
        final List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        if (this.bagService.residueBagPos(role, false) < wearEquips.size()) {
            MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
            return;
        }
        int silver = 0;
        final int num = role.getExtendBox().getDayQuotaLimit().getInt("suit", 0);
        if (num >= 5) {
            if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 100000L, "切换套装方案")) {
                return;
            }
            silver = 100000;
        }
        role.getExtendBox().getDayQuotaLimit().setv("suit", (Object)(num + 1));
        this.switchoverEquip(role, suit, wearEquips);
        MessagePusher.pushMessage(role.getId(), new RespSuitInfo((byte)1, role.getExtendBox().getDayQuotaLimit().getInt("suit", 0)));
        final int residue = 5 - num;
        MessagePusher.pushMessage(role.getId(), new RespNotifyMiscEx(role.getId(), MessageFormat.format(SuitService.SWITCHOVER_TIPS, Const.RESTYPE_SILVER, silver, (residue <= 0) ? 0 : residue), true));
        final List<Suit> list = suitMap.values().stream().filter(e -> e.getState() == 1).collect(Collectors.toList());
        for (final Suit orElse : list) {
            orElse.setState((byte)0);
            orElse.save();
        }
        suit.setState((byte)1);
        suit.save();
        this.suit(role);
    }
    
    public void switchoverEquip(final Role role, final Suit suit, final List<WearEquip> wearEquips) {
        if (suit.getEquip().size() <= 0) {
            final List<Long> list = new ArrayList<Long>(wearEquips.size());
            for (final WearEquip equip : wearEquips) {
                list.add(equip.getId());
            }
            for (final Long id : list) {
                this.equipService.unEquip(role, id, false);
            }
        }
        else {
            final Map<Long, Long> map = wearEquips.stream().collect(Collectors.toMap(WearEquip::getId, WearEquip -> WearEquip.getId()));
            final List<Long> longs = new ArrayList<Long>(suit.getEquip());
            for (final Long key : map.keySet()) {
                if (suit.getEquip().contains(key)) {
                    longs.remove(key);
                }
                else {
                    this.equipService.unEquip(role, key, false);
                }
            }
            for (final Long key : longs) {
                this.equipService.wearEquip(role, key, false);
            }
        }
        suit.setState((byte)1);
        suit.save();
    }
    
    public void suit(final Role role) {
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        final RespSuit respSuit = new RespSuit();
        final List<Suit> collect = suitMap.values().stream().sorted(Comparator.comparing(Suit::getTime)).collect(Collectors.toList());
        for (final Suit suit : collect) {
            respSuit.getList().add(new SuitEasy(suit.getId(), suit.getName(), suit.getEquip(), suit.getState()));
        }
        MessagePusher.pushMessage(role.getId(), respSuit);
    }
    
    public void updateSuitName(final Role role, final long id, final String name) {
        if (name == null || name.trim().length() <= 0) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("请输入名字"));
            return;
        }
        if (name.trim().length() > 6) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("套装最多可以自定义不超过#<R>6个中文字符#"));
            return;
        }
        final Map<Long, Suit> suitMap = this.getSuit(role.getId());
        final Suit suit = suitMap.get(id);
        if (suit == null) {
            return;
        }
        suit.setName(name.trim());
        suit.save();
        this.suit(role);
    }
    
    static {
        SuitService.SWITCHOVER_TIPS = "切换成功，扣除银两#<IR{0}>#{1}，今日剩余免费切换次数：#<G>{2}#";
    }
}
