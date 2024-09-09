package com.kitty.system.game.suit.service;

import com.kitty.system.game.suit.message.vo.SuitEasy;
import java.util.Comparator;
import com.kitty.system.game.suit.message.RespSuit;
import java.util.ArrayList;
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
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.suit.message.RespSuitInfo;
import com.kitty.listener.event.LoginEvent;
import java.util.List;
import java.util.stream.Collectors;
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
    
    public SuitService() {
    }
    
    public Map<Long, Suit> getSuit(long id) {
        Map<Long, Suit> suitMap = (Map)DataCache.SUIT_MAP.get(Long.valueOf(id));
        if (suitMap == null) {
            List<Suit> list = this.dao.query(Suit.class, Cnd.where("r_id", "=", Long.valueOf(id)));
            suitMap = (Map)list.stream().collect(Collectors.toMap(Suit::getId, Suit -> Suit));
            DataCache.SUIT_MAP.put(Long.valueOf(id), suitMap);
            return suitMap;
        }
        return suitMap;
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        int star = role.getExtendBox().getStar();
        if (star >= 8) {
            int suit = role.getExtendBox().getDayQuotaLimit().getInt("suit", 0);
            MessagePusher.pushMessage((long)role.getId(), new RespSuitInfo((byte)1, suit));
        }
    }
    
    public void firstOpenSuit(Role role) {
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        if (suitMap == null || suitMap.size() > 0) {
            return;
        }
        Suit suit = this.addSuit((long)role.getId(), suitMap, (byte)1);
        List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            WearEquip wearEquip = (WearEquip)wearEquips.get(i);
            suit.getEquip().add(wearEquip.getId());
        }
        suit.save();
        MessagePusher.pushMessage((long)role.getId(), new RespSuitInfo((byte)1, role.getExtendBox().getDayQuotaLimit().getInt("suit", 0)));
        MessagePusher.pushMessage((long)role.getId(), new RespMsg("恭喜你开启套装方案"));
        return;
    }
    
    public void addSuit(Role role) {
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        if (suitMap == null) {
            return;
        }
        if (suitMap.size() >= 12) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("套装已上限"));
            return;
        }
        if (!this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, (long)(int)Const.SUIT_REPAIR.get(suitMap.size()), "开启套装方案")) {
            return;
        }
        this.addSuit((long)role.getId(), suitMap, (byte)0);
        this.suit(role);
        return;
    }
    
    public Suit addSuit(long id, Map<Long, Suit> suitMap, byte state) {
        if (suitMap == null || suitMap.size() >= 12) {
            return null;
        }
        Suit suit = new Suit(this.worker.nextId(), id, "套装" + Convert.getChineseFromArabic(Integer.valueOf(suitMap.size() + 1)), state);
        suit.save();
        suitMap.put(suit.getId(), suit);
        return suit;
    }
    
    @EventHandler({ EventType.CHANGE_EQUIP_WEAR })
    public void handleChangeEquipEvent(ChangeEquipEvent changeEquipEvent) {
        Role role = changeEquipEvent.getRole();
        List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        if (suitMap.size() <= 0) {
            return;
        }
        Suit suit = (Suit)suitMap.values().stream().filter(e -> e.getState() == 1).findFirst().orElse(null);
        suit.getEquip().clear();
        for (int i = 0; i < wearEquips.size(); ++i) {
            WearEquip wearEquip = (WearEquip)wearEquips.get(i);
            suit.getEquip().add(wearEquip.getId());
        }
        suit.save();
        return;
    }
    
    @EventHandler({ EventType.DEL_EQUIP })
    public void handleDelEquipEvent(DelEquipEvent delEquipEvent) {
        Role role = delEquipEvent.getRole();
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        for (Long key : suitMap.keySet()) {
            Suit suit = (Suit)suitMap.get(key);
            if (suit.getEquip().contains(Long.valueOf(delEquipEvent.getId()))) {
                suit.getEquip().removeIf(e -> e.equals(Long.valueOf(delEquipEvent.getId())));
                suit.save();
            }
            else {
                continue;
            }
        }
    }
    
    public void switchover(Role role, long id) {
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        Suit suit = (Suit)suitMap.get(Long.valueOf(id));
        if (suit == null || suit.getState() == 1) {
            return;
        }
        List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
        if (this.bagService.residueBagPos(role, false) < wearEquips.size()) {
            MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
            return;
        }
        int silver = 0;
        int num = role.getExtendBox().getDayQuotaLimit().getInt("suit", 0);
        if (num >= 5) {
            if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 100000L, "切换套装方案")) {
                return;
            }
            silver = 100000;
        }
        role.getExtendBox().getDayQuotaLimit().setv("suit", Integer.valueOf(num + 1));
        this.switchoverEquip(role, suit, wearEquips);
        MessagePusher.pushMessage((long)role.getId(), new RespSuitInfo((byte)1, role.getExtendBox().getDayQuotaLimit().getInt("suit", 0)));
        int residue = 5 - num;
        MessagePusher.pushMessage((long)role.getId(), new RespNotifyMiscEx(role.getId(), MessageFormat.format(SuitService.SWITCHOVER_TIPS, Const.RESTYPE_SILVER, Integer.valueOf(silver), Integer.valueOf((residue <= 0) ? 0 : residue)), true));
        List<Suit> list = (List)suitMap.values().stream().filter(e -> e.getState() == 1).collect(Collectors.toList());
        for (Suit orElse : list) {
            orElse.setState((byte)0);
            orElse.save();
        }
        suit.setState((byte)1);
        suit.save();
        this.suit(role);
        return;
    }
    
    public void switchoverEquip(Role role, Suit suit, List<WearEquip> wearEquips) {
        if (suit.getEquip().size() <= 0) {
            List<Long> list = new ArrayList(wearEquips.size());
            for (WearEquip equip : wearEquips) {
                list.add(equip.getId());
            }
            for (Long id : list) {
                this.equipService.unEquip(role, (long)id, false);
            }
        }
        else {
            Map<Long, Long> map = (Map)wearEquips.stream().collect(Collectors.toMap(WearEquip::getId, WearEquip -> WearEquip.getId()));
            List<Long> longs = new ArrayList(suit.getEquip());
            for (Long key : map.keySet()) {
                if (suit.getEquip().contains(key)) {
                    longs.remove(key);
                }
                else {
                    this.equipService.unEquip(role, (long)key, false);
                }
            }
            for (Long key : longs) {
                this.equipService.wearEquip(role, key, false);
            }
        }
        suit.setState((byte)1);
        suit.save();
    }
    
    public void suit(Role role) {
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        RespSuit respSuit = new RespSuit();
        List<Suit> collect = (List)suitMap.values().stream().sorted(Comparator.comparing(Suit::getTime)).collect(Collectors.toList());
        for (Suit suit : collect) {
            respSuit.getList().add(new SuitEasy(suit.getId(), suit.getName(), suit.getEquip(), suit.getState()));
        }
        MessagePusher.pushMessage((long)role.getId(), respSuit);
    }
    
    public void updateSuitName(Role role, long id, String name) {
        if (name == null || name.trim().length() <= 0) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("请输入名字"));
            return;
        }
        if (name.trim().length() > 6) {
            MessagePusher.pushMessage((long)role.getId(), new RespMsg("套装最多可以自定义不超过#<R>6个中文字符#"));
            return;
        }
        Map<Long, Suit> suitMap = this.getSuit((long)role.getId());
        Suit suit = (Suit)suitMap.get(Long.valueOf(id));
        if (suit == null) {
            return;
        }
        suit.setName(name.trim());
        suit.save();
        this.suit(role);
        return;
    }
    
    static {
        SuitService.SWITCHOVER_TIPS = "切换成功，扣除银两#<IR{0}>#{1}，今日剩余免费切换次数：#<G>{2}#";
    }
}
