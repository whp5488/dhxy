package com.kitty.system.game.card.service;

import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.listener.event.LoginEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.bag.message.RespIconCartoon;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.card.message.RespCardFlyInfo;
import com.kitty.system.game.card.message.RespFiveExercise;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.echo.handler.EchoHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysDataCardFiveExercise;
import com.kitty.system.init.domain.vo.Param;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CardService extends EchoHandler {
  Logger logger = LoggerFactory.getLogger(CardService.class);
  
  @Autowired
  private Dao dao;
  
  @Autowired
  private IdWorker worker;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private TeamService teamService;
  
  @Autowired
  private BroadcastService broadcastService;
  
  public CardExercise getCardExercise(Role role) {
    CardExercise cardExercise = (CardExercise)DataCache.CARD_EXERCISE_MAP.get(role.getId());
    if (cardExercise == null) {
      CardExercise fetch = (CardExercise)this.dao.fetch(CardExercise.class, (Condition)Cnd.where("r_id", "=", role.getId()));
      if (fetch == null) {
        fetch = new CardExercise(this.worker.nextId(), role.getId().longValue());
        fetch.save();
        this.logger.error("获取变身卡-五行修炼，数据为空角色编号：{}，角色SID：{}", role.getRoleId(), role.getSid());
      } 
      DataCache.CARD_EXERCISE_MAP.put(role.getId(), fetch);
      return fetch;
    } 
    return cardExercise;
  }
  
  public Map<Long, CardChange> getCard(Role role) {
    Map<Long, CardChange> cardChangeMap = (Map<Long, CardChange>)DataCache.CUTTING_FERRULE_MAP.get(role.getId());
    if (cardChangeMap == null) {
      List<CardChange> list = this.dao.query(CardChange.class, (Condition)Cnd.where("r_id", "=", role.getId()));
      Map<Long, CardChange> map = (Map<Long, CardChange>)list.stream().collect(Collectors.toMap(CardChange::getId, cardChange -> cardChange));
      DataCache.CUTTING_FERRULE_MAP.put(role.getId(), map);
      return map;
    } 
    return cardChangeMap;
  }
  
  @EventHandler({EventType.LOGIN})
  public void handleLoginEvent(LoginEvent loginEvent) {
    Role role = loginEvent.getRole();
    Map<Long, CardChange> cardMap = getCard(role);
    for (Long key : cardMap.keySet()) {
      CardChange cardChange = cardMap.get(key);
      refresh(role, cardChange);
    } 
  }
  
  public int addCard(Role role, Integer serial, Integer num, Integer bd, boolean isCartoon, boolean isTips) {
    SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(serial);
    if (card == null) {
      this.logger.error("角色{} 未找到变身卡：编号为 = {}, 数量 = {}", new Object[] { role.getName(), serial, num });
      return -1;
    } 
    if (isTips)
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), String.format("你获得了#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", new Object[] { Integer.valueOf(card.getSerial()), Integer.valueOf(card.getSerial()), Integer.valueOf(card.getSerial()), card.getName() }), true)); 
    Map<Long, CardChange> map = getCard(role);
    int canMerge = card.getCanMerge();
    List<CardChange> collect = (List<CardChange>)map.values().stream().filter(e -> (e.getSerial() == serial.intValue())).collect(Collectors.toList());
    for (CardChange cardChange : collect) {
      int residue = canMerge - DataGlobalFunc.getCardChangeNum(cardChange);
      if (residue <= 0)
        continue; 
      int count = 0;
      if (residue >= num.intValue()) {
        saveCard(role, serial, num, cardChange, count, bd.intValue(), isCartoon);
        num = Integer.valueOf(0);
        break;
      } 
      num = Integer.valueOf(Math.abs(residue - num.intValue()));
      saveCard(role, serial, Integer.valueOf(residue), cardChange, num.intValue(), bd.intValue(), isCartoon);
    } 
    while (num.intValue() > 0) {
      int add = 0;
      int mod = num.intValue() - canMerge;
      if (mod > 0) {
        add = canMerge;
      } else {
        add = num.intValue();
      } 
      num = Integer.valueOf(mod);
      CardChange cardChange = newAddCard(role, serial, Integer.valueOf(add), bd, isCartoon);
      map.put(cardChange.getId(), cardChange);
      cardChange.save();
    } 
    return num.intValue();
  }
  
  public CardChange newAddCard(Role role, Integer serial, Integer num, Integer bd, boolean isCartoon) {
    CardChange cardChange = new CardChange();
    cardChange.setId(this.worker.nextId());
    cardChange.setrId(role.getId().longValue());
    cardChange.setSerial(serial.intValue());
    cardChange.getRests().setv(String.valueOf(bd), num);
    if (isCartoon)
      cartoon(role, serial); 
    refresh(role, cardChange);
    return cardChange;
  }
  
  public boolean saveCard(Role role, Integer serial, Integer num, CardChange cardChange, int surplus, int bd, boolean isCartoon) {
    String key = String.valueOf(bd);
    int count = cardChange.getRests().getInt(key, 0);
    cardChange.getRests().setv(key, Integer.valueOf(num.intValue() + count));
    refresh(role, cardChange);
    if (surplus >= 0 && isCartoon)
      cartoon(role, serial); 
    cardChange.save();
    return true;
  }
  
  public void cartoon(Role role, Integer serial) {
    if (this.roleService.mouserCartoon(role, serial.toString(), 200L)) {
      RespIconCartoon respIconCartoon = new RespIconCartoon();
      respIconCartoon.setSerial(serial.intValue());
      MessagePusher.pushMessage(role, (Message)respIconCartoon);
    } 
  }
  
  public void refresh(Role role, CardChange cardChange) {
    RespCardFlyInfo cardFlyInfo = new RespCardFlyInfo(String.valueOf(cardChange.getId()), cardChange.getSerial(), JsonUtils.map2String((Map)cardChange.getRests()));
    MessagePusher.pushMessage(role, (Message)cardFlyInfo);
  }
  
  public boolean cardDetach(Role role, int serial, int num) {
    boolean flag = false;
    SysDataCard dataCard = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
    if (dataCard == null)
      return flag; 
    Map<Long, CardChange> cardMap = getCard(role);
    List<CardChange> collect = (List<CardChange>)cardMap.values().stream().filter(e -> (e.getSerial() == serial)).collect(Collectors.toList());
    for (CardChange card : collect) {
      if (num <= 0) {
        flag = true;
        break;
      } 
      int cardChangeNum = DataGlobalFunc.getCardChangeNum(card);
      if (cardChangeNum >= num) {
        consume(role, cardMap, card, num);
        flag = true;
        break;
      } 
      card.setRests(new NutMap());
      card.setDelete(true);
      cardMap.remove(card.getId());
      card.save();
      refresh(role, card);
      num -= cardChangeNum;
    } 
    return flag;
  }
  
  public void useCar(Role role, int serial, long id) {
    SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
    if (card == null) {
      this.logger.error("使用变身卡角色{} 未找到变身卡：编号为 = {}", role.getName(), Integer.valueOf(serial));
      return;
    } 
    int grade = card.getNeedOwnerGrade();
    String msg = DataGlobalFunc.needGradeJudge(grade / 1000, grade % 1000, role.getRelive().intValue(), role.getLevel().intValue(), "");
    if (msg != null) {
      MessagePusher.notify2Player(role, 900, new Object[] { msg, card.getName() });
      return;
    } 
    if (getCardExercise(role).getFiveGrade() < card.getNeedFiveGrade()) {
      MessagePusher.notify2Player(role, 901, new Object[] { Integer.valueOf(card.getNeedFiveGrade()), card.getName() });
      return;
    } 
    Map<Long, CardChange> map = getCard(role);
    CardChange cardChange = map.values().stream().filter(e -> (e.getSerial() == serial && e.getId().equals(Long.valueOf(id)))).findFirst().orElse(null);
    if (cardChange != null) {
      NutMap rests = cardChange.getRests();
      String key = String.valueOf(2);
      int num = rests.getInt(key);
      if (num > 0) {
        transfiguration(role, card.getSerial(), card.getExpire() * 60 + (new Long(System.currentTimeMillis() / 1000L)).intValue(), card.getName());
        useCarTrailing(role, map, cardChange, key, num);
        rests.setv(key, Integer.valueOf(num - 1));
        return;
      } 
      List<Integer> list = new ArrayList<>();
      for (String s : rests.keySet())
        list.add(Integer.valueOf(s)); 
      List<Integer> collect = (List<Integer>)list.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
      key = String.valueOf(collect.get(0));
      num = rests.getInt(key);
      transfiguration(role, card.getSerial(), card.getExpire() * 60 + (new Long(System.currentTimeMillis() / 1000L)).intValue(), card.getName());
      useCarTrailing(role, map, cardChange, key, num);
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("未获取到您要使用的变身卡"));
    } 
  }
  
  public void useCarTrailing(Role role, Map<Long, CardChange> map, CardChange card, String key, int num) {
    num--;
    if (num <= 0) {
      card.getRests().remove(key);
    } else {
      card.getRests().setv(key, Integer.valueOf(num));
    } 
    int count = DataGlobalFunc.getCardChangeNum(card);
    if (count <= 0) {
      card.setDelete(true);
      map.remove(card.getId());
    } 
    card.save();
    refresh(role, card);
  }
  
  public void transfiguration(Role role, int serial, int time, String name) {
    boolean flag = false;
    if (role.getMasquerade() > 0)
      flag = true; 
    role.setMasquerade(serial);
    role.setCardExpireTime(time);
    this.roleService.calculateBase(role);
    this.roleService.refreshRole(role);
    this.roleService.stateRefresh(role);
    this.broadcastService.sendUpdateAppear(role);
    this.teamService.updateTeamList(role);
    String msg = "";
    if (serial > 0 && name != null) {
      msg = "你使用了#<R>" + name + "#，变的更加强大了#<E:89>#";
      if (flag)
        msg = "你使用了#<R>" + name + "#，一阵烟雾过后获得了新的造型，变的更加强大了#<E:89>#"; 
    } else if (name != null) {
      msg = "#<R>" + name + "#失效了";
    } 
    if (!msg.isEmpty())
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg)); 
    role.save();
  }
  
  public void upgradeFiveGrade(Role role) {
    boolean clicker = this.roleService.mouserClicker(role, "up_car_five", 250L);
    if (!clicker)
      return; 
    CardExercise cardExercise = getCardExercise(role);
    if (cardExercise != null && upgradeFiveGradeCheck(role, cardExercise)) {
      int fiveGrade = cardExercise.getFiveGrade();
      SysDataCardFiveExercise exercise = (SysDataCardFiveExercise)CardDataPool.cardFiveExerciseMap.get(Integer.valueOf(cardExercise.getFiveGrade()));
      long wx = exercise.getWx().longValue();
      boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_COIN, wx);
      if (!flag)
        return; 
      this.roleService.deductCurrency(role, Const.RESTYPE_COIN, wx, "upgradeFiveGrade");
      cardExercise.setFiveGrade(fiveGrade + 1);
      cardExercise.save();
      List<FiedValue> objects = new ArrayList<>();
      objects.add(new FiedValue("grade", (byte)2, Integer.valueOf(cardExercise.getFiveGrade())));
      MessagePusher.pushMessage(role, (Message)new RespFiveExercise(objects));
      MessagePusher.pushMessage(role, (Message)new RespMsg("你消耗了#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(Long.valueOf(wx)) + "，五行等级提示到了" + cardExercise.getFiveGrade() + "级"));
      this.roleService.calculateBase(role);
    } 
  }
  
  public boolean upgradeFiveGradeCheck(Role role, CardExercise exercise) {
    if (exercise == null)
      return false; 
    boolean flag = true;
    int nextFiveGrade = exercise.getFiveGrade() + 1;
    if (exercise.getGoldGrade() < nextFiveGrade) {
      flag = false;
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先提升#<r:16,g:243,b:239>金#五行修炼等级"));
    } else if (exercise.getWoodGrade() < nextFiveGrade) {
      flag = false;
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先提升#<r:16,g:243,b:239>木#五行修炼等级"));
    } else if (exercise.getWaterGrade() < nextFiveGrade) {
      flag = false;
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先提升#<r:16,g:243,b:239>水#五行修炼等级"));
    } else if (exercise.getFireGrade() < nextFiveGrade) {
      flag = false;
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先提升#<r:16,g:243,b:239>火#五行修炼等级"));
    } else if (exercise.getFireGrade() < nextFiveGrade) {
      flag = false;
      MessagePusher.pushMessage(role, (Message)new RespMsg("请先提升#<r:16,g:243,b:239>土#五行修炼等级"));
    } 
    return flag;
  }
  
  public boolean addExp(Role role, int exp, int type, CardExercise cardExercise, Param param, String reason) {
    if (exp <= 0)
      return false; 
    int level = param.getK().intValue();
    int totalExp = (param.getV().intValue() + exp > Const.INT_LIMIT.intValue()) ? Const.INT_LIMIT.intValue() : (param.getV().intValue() + exp);
    int needExp = getNeedExpWxInfo((SysDataCardFiveExercise)CardDataPool.cardFiveExerciseMap.get(Integer.valueOf(level)), type);
    if (totalExp >= needExp) {
      while (totalExp >= needExp) {
        totalExp -= needExp;
        level++;
        updateCardExercise(cardExercise, type, new Param(Integer.valueOf(level), Integer.valueOf(totalExp)));
        informFiveExercise(role, cardExercise, type);
        if (level > CardDataPool.cardFiveExerciseMaxGrade)
          break; 
        SysDataCardFiveExercise exercise = (SysDataCardFiveExercise)CardDataPool.cardFiveExerciseMap.get(Integer.valueOf(level));
        if (exercise == null) {
          this.logger.error("五行修炼升级重新获取升级经验错误，当前等级为：{}", Integer.valueOf(level));
          break;
        } 
        needExp = getNeedExpWxInfo(exercise, type);
      } 
    } else {
      updateCardExercise(cardExercise, type, new Param(Integer.valueOf(level), Integer.valueOf(totalExp)));
      informFiveExercise(role, cardExercise, type);
    } 
    return true;
  }
  
  public int getNeedExpWxInfo(SysDataCardFiveExercise exercise, int type) {
    if (type == PRO_DICT_TYPE.WX_J.getDist())
      return exercise.getGold(); 
    if (type == PRO_DICT_TYPE.WX_M.getDist())
      return exercise.getWood(); 
    if (type == PRO_DICT_TYPE.WX_S.getDist())
      return exercise.getWater(); 
    if (type == PRO_DICT_TYPE.WX_H.getDist())
      return exercise.getFire(); 
    if (type == PRO_DICT_TYPE.WX_T.getDist())
      return exercise.getSoil(); 
    return -1;
  }
  
  public void informFiveExercise(Role role, CardExercise exercise, int type) {
    List<FiedValue> objects = new ArrayList<>();
    if (type == PRO_DICT_TYPE.WX_J.getDist()) {
      objects.add(new FiedValue("goldGrade", (byte)2, Integer.valueOf(exercise.getGoldGrade())));
      objects.add(new FiedValue("goldExp", (byte)3, Integer.valueOf(exercise.getGoldExp())));
    } else if (type == PRO_DICT_TYPE.WX_M.getDist()) {
      objects.add(new FiedValue("woodGrade", (byte)2, Integer.valueOf(exercise.getWoodGrade())));
      objects.add(new FiedValue("woodExp", (byte)3, Integer.valueOf(exercise.getWoodExp())));
    } else if (type == PRO_DICT_TYPE.WX_S.getDist()) {
      objects.add(new FiedValue("waterGrade", (byte)2, Integer.valueOf(exercise.getWaterGrade())));
      objects.add(new FiedValue("waterExp", (byte)3, Integer.valueOf(exercise.getWaterExp())));
    } else if (type == PRO_DICT_TYPE.WX_H.getDist()) {
      objects.add(new FiedValue("fireGrade", (byte)2, Integer.valueOf(exercise.getFireGrade())));
      objects.add(new FiedValue("fireExp", (byte)3, Integer.valueOf(exercise.getFireExp())));
    } else if (type == PRO_DICT_TYPE.WX_T.getDist()) {
      objects.add(new FiedValue("soilGrade", (byte)2, Integer.valueOf(exercise.getSoilGrade())));
      objects.add(new FiedValue("soilExp", (byte)3, Integer.valueOf(exercise.getSoilExp())));
    } 
    objects.add(new FiedValue("useCount", (byte)2, Integer.valueOf(exercise.getUseCount())));
    MessagePusher.pushMessage(role, (Message)new RespFiveExercise(objects));
  }
  
  public Param getWxInfo(CardExercise cardExercise, int type) {
    if (type == PRO_DICT_TYPE.WX_J.getDist())
      return new Param(Integer.valueOf(cardExercise.getGoldGrade()), Integer.valueOf(cardExercise.getGoldExp())); 
    if (type == PRO_DICT_TYPE.WX_M.getDist())
      return new Param(Integer.valueOf(cardExercise.getWoodGrade()), Integer.valueOf(cardExercise.getWoodExp())); 
    if (type == PRO_DICT_TYPE.WX_S.getDist())
      return new Param(Integer.valueOf(cardExercise.getWaterGrade()), Integer.valueOf(cardExercise.getWaterExp())); 
    if (type == PRO_DICT_TYPE.WX_H.getDist())
      return new Param(Integer.valueOf(cardExercise.getFireGrade()), Integer.valueOf(cardExercise.getFireExp())); 
    if (type == PRO_DICT_TYPE.WX_T.getDist())
      return new Param(Integer.valueOf(cardExercise.getSoilGrade()), Integer.valueOf(cardExercise.getSoilExp())); 
    return null;
  }
  
  public void updateCardExercise(CardExercise cardExercise, int type, Param param) {
    int grade = param.getK().intValue();
    int exp = param.getV().intValue();
    if (grade > CardDataPool.cardFiveExerciseMaxGrade) {
      grade = CardDataPool.cardFiveExerciseMaxGrade;
      exp = -1;
    } 
    if (type == PRO_DICT_TYPE.WX_J.getDist()) {
      cardExercise.setGoldGrade(grade);
      cardExercise.setGoldExp(exp);
    } else if (type == PRO_DICT_TYPE.WX_M.getDist()) {
      cardExercise.setWoodGrade(grade);
      cardExercise.setWoodExp(exp);
    } else if (type == PRO_DICT_TYPE.WX_S.getDist()) {
      cardExercise.setWaterGrade(grade);
      cardExercise.setWaterExp(exp);
    } else if (type == PRO_DICT_TYPE.WX_H.getDist()) {
      cardExercise.setFireGrade(grade);
      cardExercise.setFireExp(exp);
    } else if (type == PRO_DICT_TYPE.WX_T.getDist()) {
      cardExercise.setSoilGrade(grade);
      cardExercise.setSoilExp(exp);
    } 
    cardExercise.save();
  }
  
  public void checkCardExpire(Role role, int currTime) {
    int cardExpireTime = role.getCardExpireTime();
    if (cardExpireTime - 1 <= currTime)
      cardExpire(role); 
  }
  
  public void cardExpire(Role role) {
    String name = null;
    SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(role.getMasquerade()));
    if (card != null)
      name = card.getName(); 
    transfiguration(role, 0, 0, name);
  }
  
  public void reqFiveExercise(Role role, boolean flag) {
    if (flag)
      if (DataGlobalFunc.needGradeJudge(1, 80, role.getRelive().intValue(), role.getLevel().intValue(), "") != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("#<LG1>五行修炼#1转80级开启，请先提升等级"));
        return;
      }  
    CardExercise cardExercise = getCardExercise(role);
    MessagePusher.pushMessage(role, (Message)new RespFiveExercise(cardExercise));
  }
  
  public void consume(Role role, Map<Long, CardChange> carMap, CardChange card, int consume) {
    NutMap rests = card.getRests();
    String key = String.valueOf(2);
    int num = rests.getInt(key, 0);
    if (num < consume) {
      rests.remove(key);
      consume -= num;
      List<Integer> list = new ArrayList<>();
      for (String s : rests.keySet())
        list.add(Integer.valueOf(s)); 
      List<Integer> collect = (List<Integer>)list.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
      for (Integer key_ : collect) {
        int anInt = rests.getInt(String.valueOf(key_), 0);
        if (anInt > 0) {
          if (consume > anInt) {
            rests.remove(String.valueOf(key_));
            consume -= anInt;
            continue;
          } 
          int residue = anInt - consume;
          if (residue <= 0) {
            rests.remove(String.valueOf(key_));
            break;
          } 
          rests.setv(String.valueOf(key_), Integer.valueOf(residue));
          break;
        } 
      } 
    } else {
      int residue = num - consume;
      if (residue <= 0)
        rests.remove(String.valueOf(key)); 
    } 
    card.setRests(rests);
    if (DataGlobalFunc.getCardChangeNum(card) <= 0) {
      card.setRests(new NutMap());
      card.setDelete(true);
      carMap.remove(card.getId());
    } 
    refresh(role, card);
    card.save();
  }
  
  public boolean shelvesConsume(Role role, long id, int consume) {
    int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
    Map<Long, CardChange> cardMap = getCard(role);
    CardChange card = cardMap.get(Long.valueOf(id));
    NutMap rests = card.getRests();
    int num = DataGlobalFunc.unrestricted(rests);
    if (consume > num)
      return false; 
    List<Integer> list = new ArrayList<>();
    for (String s : rests.keySet()) {
      int key = Integer.valueOf(s).intValue();
      if (key != 2 && key < time)
        list.add(Integer.valueOf(key)); 
    } 
    list.add(Integer.valueOf(0));
    for (Integer key : list) {
      int anInt = rests.getInt(String.valueOf(key), 0);
      if (anInt > 0) {
        if (consume > anInt) {
          rests.remove(String.valueOf(key));
          consume -= anInt;
          continue;
        } 
        int residue = anInt - consume;
        if (residue <= 0) {
          rests.remove(String.valueOf(key));
          break;
        } 
        rests.setv(String.valueOf(key), Integer.valueOf(residue));
        break;
      } 
    } 
    card.setRests(rests);
    if (DataGlobalFunc.getCardChangeNum(card) <= 0) {
      card.setRests(new NutMap());
      card.setDelete(true);
      cardMap.remove(card.getId());
    } 
    card.save();
    refresh(role, card);
    return true;
  }
  
  public void handleEcho(HeartBeatEvent heartBeatEvent) {
    Role role = heartBeatEvent.getRole();
    if (role == null)
      return; 
    int currTime = (new Long(heartBeatEvent.getCurveHeartTime() / 1000L)).intValue();
    if (role.getMasquerade() > 0)
      checkCardExpire(role, currTime); 
  }
}
