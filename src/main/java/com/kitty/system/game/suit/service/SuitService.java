package com.kitty.system.game.suit.service;

import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.text.Convert;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.listener.event.DelEquipEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.suit.domain.Suit;
import com.kitty.system.game.suit.message.RespSuit;
import com.kitty.system.game.suit.message.RespSuitInfo;
import com.kitty.system.game.suit.message.vo.SuitEasy;
import com.kitty.system.game.vo.utils.Const;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SuitService {
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
  
  static String SWITCHOVER_TIPS = "切换成功，扣除银两#<IR{0}>#{1}，今日剩余免费切换次数：#<G>{2}#";
  
  public Map<Long, Suit> getSuit(long id) {
    Map<Long, Suit> suitMap = (Map<Long, Suit>)DataCache.SUIT_MAP.get(Long.valueOf(id));
    if (suitMap == null) {
      List<Suit> list = this.dao.query(Suit.class, (Condition)Cnd.where("r_id", "=", Long.valueOf(id)));
      suitMap = (Map<Long, Suit>)list.stream().collect(Collectors.toMap(Suit::getId, suit -> suit));
      DataCache.SUIT_MAP.put(Long.valueOf(id), suitMap);
      return suitMap;
    } 
    return suitMap;
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    int star = role.getExtendBox().getStar();
    if (star >= 8) {
      int suit = role.getExtendBox().getDayQuotaLimit().getInt("suit", 0);
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespSuitInfo((byte)1, suit));
    } 
  }
  
  public void firstOpenSuit(Role role) {
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    if (suitMap == null || suitMap.size() > 0)
      return; 
    Suit suit = addSuit(role.getId().longValue(), suitMap, (byte)1);
    List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
    for (int i = 0; i < wearEquips.size(); i++) {
      WearEquip wearEquip = wearEquips.get(i);
      suit.getEquip().add(wearEquip.getId());
    } 
    suit.save();
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespSuitInfo((byte)1, role.getExtendBox().getDayQuotaLimit().getInt("suit", 0)));
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("恭喜你开启套装方案"));
  }
  
  public void addSuit(Role role) {
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    if (suitMap == null)
      return; 
    if (suitMap.size() >= 12) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("套装已上限"));
      return;
    } 
    if (!this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, ((Integer)Const.SUIT_REPAIR.get(suitMap.size())).intValue(), "开启套装方案"))
      return; 
    addSuit(role.getId().longValue(), suitMap, (byte)0);
    suit(role);
  }
  
  public Suit addSuit(long id, Map<Long, Suit> suitMap, byte state) {
    if (suitMap == null || suitMap.size() >= 12)
      return null; 
    Suit suit = new Suit(this.worker.nextId(), id, "套装" + Convert.getChineseFromArabic(Integer.valueOf(suitMap.size() + 1)), state);
    suit.save();
    suitMap.put(suit.getId(), suit);
    return suit;
  }
  
  @EventHandler({EventType.CHANGE_EQUIP_WEAR})
  public void handleChangeEquipEvent(ChangeEquipEvent changeEquipEvent) {
    Role role = changeEquipEvent.getRole();
    List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    if (suitMap.size() <= 0)
      return; 
    Suit suit = suitMap.values().stream().filter(e -> (e.getState() == 1)).findFirst().orElse(null);
    suit.getEquip().clear();
    for (int i = 0; i < wearEquips.size(); i++) {
      WearEquip wearEquip = wearEquips.get(i);
      suit.getEquip().add(wearEquip.getId());
    } 
    suit.save();
  }
  
  @EventHandler({EventType.DEL_EQUIP})
  public void handleDelEquipEvent(DelEquipEvent delEquipEvent) {
    Role role = delEquipEvent.getRole();
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    for (Long key : suitMap.keySet()) {
      Suit suit = suitMap.get(key);
      if (suit.getEquip().contains(Long.valueOf(delEquipEvent.getId()))) {
        suit.getEquip().removeIf(e -> e.equals(Long.valueOf(delEquipEvent.getId())));
        suit.save();
      } 
    } 
  }
  
  public void switchover(Role role, long id) {
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    Suit suit = suitMap.get(Long.valueOf(id));
    if (suit == null || suit.getState() == 1)
      return; 
    List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
    if (this.bagService.residueBagPos(role, false) < wearEquips.size()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("物品栏空间不足"));
      return;
    } 
    int silver = 0;
    int num = role.getExtendBox().getDayQuotaLimit().getInt("suit", 0);
    if (num >= 5) {
      if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 100000L, "切换套装方案"))
        return; 
      silver = 100000;
    } 
    role.getExtendBox().getDayQuotaLimit().setv("suit", Integer.valueOf(num + 1));
    switchoverEquip(role, suit, wearEquips);
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespSuitInfo((byte)1, role.getExtendBox().getDayQuotaLimit().getInt("suit", 0)));
    int residue = 5 - num;
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespNotifyMiscEx(role.getId(), MessageFormat.format(SWITCHOVER_TIPS, new Object[] { Const.RESTYPE_SILVER, Integer.valueOf(silver), Integer.valueOf((residue <= 0) ? 0 : residue) }), true));
    List<Suit> list = (List<Suit>)suitMap.values().stream().filter(e -> (e.getState() == 1)).collect(Collectors.toList());
    for (Suit orElse : list) {
      orElse.setState((byte)0);
      orElse.save();
    } 
    suit.setState((byte)1);
    suit.save();
    suit(role);
  }
  
  public void switchoverEquip(Role role, Suit suit, List<WearEquip> wearEquips) {
    if (suit.getEquip().size() <= 0) {
      List<Long> list = new ArrayList<>(wearEquips.size());
      for (WearEquip equip : wearEquips)
        list.add(equip.getId()); 
      for (Long id : list)
        this.equipService.unEquip(role, id.longValue(), false); 
    } else {
      Map<Long, Long> map = (Map<Long, Long>)wearEquips.stream().collect(Collectors.toMap(WearEquip::getId, wearEquip -> wearEquip.getId()));
      List<Long> longs = new ArrayList<>(suit.getEquip());
      for (Long key : map.keySet()) {
        if (suit.getEquip().contains(key)) {
          longs.remove(key);
          continue;
        } 
        this.equipService.unEquip(role, key.longValue(), false);
      } 
      for (Long key : longs)
        this.equipService.wearEquip(role, key, false); 
    } 
    suit.setState((byte)1);
    suit.save();
  }
  
  public void suit(Role role) {
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    RespSuit respSuit = new RespSuit();
    List<Suit> collect = (List<Suit>)suitMap.values().stream().sorted(Comparator.comparing(Suit::getTime)).collect(Collectors.toList());
    for (Suit suit : collect)
      respSuit.getList().add(new SuitEasy(suit.getId(), suit.getName(), suit.getEquip(), suit.getState())); 
    MessagePusher.pushMessage(role.getId().longValue(), (Message)respSuit);
  }
  
  public void updateSuitName(Role role, long id, String name) {
    if (name == null || name.trim().length() <= 0) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("请输入名字"));
      return;
    } 
    if (name.trim().length() > 6) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("套装最多可以自定义不超过#<R>6个中文字符#"));
      return;
    } 
    Map<Long, Suit> suitMap = getSuit(role.getId().longValue());
    Suit suit = suitMap.get(Long.valueOf(id));
    if (suit == null)
      return; 
    suit.setName(name.trim());
    suit.save();
    suit(role);
  }
}
