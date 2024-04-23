package com.kitty.system.game.bag.service;

import com.alibaba.fastjson.JSONObject;
import com.kitty.cache.RedisService;
import com.kitty.common.bean.Lottery;
import com.kitty.common.model.Pos;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.uuid.IdUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.DelEquipEvent;
import com.kitty.logs.LoggerUtils;
import com.kitty.mina.cache.BagCache;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.PetCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespCherish;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.activity.handler.time.SealDemonHandler;
import com.kitty.system.game.activity.handler.time.ThreeScopeHandler;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.system.game.bag.domain.Overflow;
import com.kitty.system.game.bag.domain.vo.Temporary;
import com.kitty.system.game.bag.helper.BagHandler;
import com.kitty.system.game.bag.helper.BagHelper;
import com.kitty.system.game.bag.message.ReqDrugUse;
import com.kitty.system.game.bag.message.ReqExchange;
import com.kitty.system.game.bag.message.ReqOperateItem;
import com.kitty.system.game.bag.message.RespDeleteItem;
import com.kitty.system.game.bag.message.RespFlyInfo;
import com.kitty.system.game.bag.message.RespFlyPetEqInfo;
import com.kitty.system.game.bag.message.RespIconCartoon;
import com.kitty.system.game.bag.message.RespLodeTemporary;
import com.kitty.system.game.bag.message.RespUseIncense;
import com.kitty.system.game.bag.message.vo.TemporaryVo;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.domain.PetEquipEasy;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Goods;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.role.service.SettingService;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.system.init.domain.vo.Prop;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.queue.BroadcastThread;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BagService {
  Logger logger = LoggerFactory.getLogger(BagService.class);
  
  @Autowired
  private Dao dao;
  
  @Autowired
  private IdWorker worker;
  
  @Autowired
  private RedisService redisService;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private CardService cardService;
  
  @Autowired
  private PetService petService;
  
  @Autowired
  private BroadcastThread broadcastThread;
  
  public Map<Long, BagGoods> getBagGoods(Role role) {
    Map<Long, BagGoods> bagGoodsMap = (Map<Long, BagGoods>)BagCache.BAG_GOODS_MAP.get(role.getId());
    if (bagGoodsMap == null) {
      List<BagGoods> list = this.dao.query(BagGoods.class, (Condition)Cnd.where("r_id", "=", role.getId()));
      Map<Long, BagGoods> map = (Map<Long, BagGoods>)list.stream().collect(Collectors.toMap(BagGoods::getId, bagGoods -> bagGoods));
      BagCache.BAG_GOODS_MAP.put(role.getId(), map);
      return map;
    } 
    return bagGoodsMap;
  }
  
  public Map<Long, PetEquip> getPetBagEquip(long id) {
    Map<Long, PetEquip> bagGoodsMap = (Map<Long, PetEquip>)PetCache.PET_EQUIP_MAP.get(Long.valueOf(id));
    if (bagGoodsMap == null) {
      List<PetEquip> list = this.dao.query(PetEquip.class, (Condition)Cnd.where("r_id", "=", Long.valueOf(id)));
      Map<Long, PetEquip> map = (Map<Long, PetEquip>)list.stream().collect(Collectors.toMap(PetEquip::getId, petEquip -> petEquip));
      PetCache.PET_EQUIP_MAP.put(Long.valueOf(id), map);
      return map;
    } 
    return bagGoodsMap;
  }
  
  public List<PetEquip> getPetWearEquip(long id, long pId) {
    Map<Long, PetEquip> petBagEquip = getPetBagEquip(id);
    if (petBagEquip == null || petBagEquip.size() <= 0)
      return null; 
    return (List<PetEquip>)petBagEquip.values().stream().filter(e -> (e.getPId() == pId)).collect(Collectors.toList());
  }
  
  public Map<Long, BagEquip> getBagEquip(Role role) {
    Map<Long, BagEquip> bagGoodsMap = (Map<Long, BagEquip>)BagCache.BAG_EQUIP_MAP.get(role.getId());
    if (bagGoodsMap == null) {
      List<BagEquip> list = this.dao.query(BagEquip.class, (Condition)Cnd.where("r_id", "=", role.getId()));
      list.forEach(equip -> equip.doAfterInit());
      Map<Long, BagEquip> map = (Map<Long, BagEquip>)list.stream().collect(Collectors.toMap(BagEquip::getId, bagEquip -> bagEquip));
      BagCache.BAG_EQUIP_MAP.put(role.getId(), map);
      return map;
    } 
    return bagGoodsMap;
  }
  
  public boolean checkPos(Role role, int serial, int num) {
    boolean flag = false;
    ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
    if (easyInfo != null && easyInfo.getCategory().intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
      if (easyInfo.getIsOpen().intValue() == 1) {
        int size = easyInfo.getExtra().size() + easyInfo.getCycle().intValue();
        if (size <= 0)
          return true; 
        for (int i = 0; i < size; i++) {
          int pos = getPos(role, false);
          if (pos <= -1) {
            flag = false;
            break;
          } 
          flag = true;
        } 
      } else {
        flag = checkCanMerge(role, DataGlobalFunc.GetItemDataByItemTypeId(Integer.valueOf(serial)), serial, num);
      } 
    } else {
      if (easyInfo != null && easyInfo.getIsOpen().intValue() == 1 && Const.currency.contains(easyInfo.getType()))
        return true; 
      flag = checkCanMerge(role, DataGlobalFunc.GetItemDataByItemTypeId(Integer.valueOf(serial)), serial, num);
    } 
    if (!flag)
      MessagePusher.pushMessage(role, (Message)new RespMsg("物品栏空间不足")); 
    return flag;
  }
  
  public boolean checkPos(Role role, List<Param> list) {
    for (Param param : list) {
      if (!ItemClassify.getValue().contains(param.getK())) {
        boolean pos = checkPos(role, param.getK().intValue(), param.getV().intValue());
        if (!pos)
          return false; 
      } 
    } 
    return true;
  }
  
  public boolean checkCanMerge(Role role, Map map, int serial, int num) {
    if (map == null)
      return checkOther(role, serial, num); 
    boolean flag = false;
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    Object obj = map.get(Integer.valueOf(serial));
    JSONObject json = JsonUtils.objectToJson(obj);
    Integer canMerge = json.getInteger("canMerge");
    if (canMerge == null)
      return false; 
    List<BagGoods> collect = (List<BagGoods>)bagGoodsMap.values().stream().filter(item -> (item.getSerial() == serial)).collect(Collectors.toList());
    if (collect.size() > 0)
      for (BagGoods goods : collect) {
        int residue = canMerge.intValue() - DataGlobalFunc.getBagGoodsNum(goods);
        if (residue <= 0)
          continue; 
        if (residue >= num) {
          flag = true;
          break;
        } 
        num = Math.abs(residue - num);
      }  
    if (!flag) {
      int pos = getPos(role, false);
      if (pos > -1)
        flag = true; 
    } 
    return flag;
  }
  
  public boolean checkOther(Role role, int serial, int num) {
    if (serial == 9997) {
      RoleTianFu roleTianFu = ((List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> (obj.getTianFuSerial() == serial && obj.getStorePos() == 0)).findFirst().orElse(null);
      if (roleTianFu != null) {
        if (roleTianFu.getCount() + num > 9999) {
          MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("天策符栏空间不足"));
          return false;
        } 
      } else if (num > 9999) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("天策符栏空间不足"));
        return false;
      } 
    } 
    return true;
  }
  
  public int getPos(IoSession session, boolean is) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return -1; 
    return getPos(role, is);
  }
  
  public int getPos(Role role, boolean is) {
    return getMinAndMaxPos(role, is).intValue();
  }
  
  private Integer getMinAndMaxPos(Role role, boolean is) {
    List<Integer> list = new ArrayList<>();
    if (is) {
      Map<Long, PetEquip> petEquipMap = (Map<Long, PetEquip>)PetCache.PET_EQUIP_MAP.get(role.getId());
      if (petEquipMap != null) {
        int pos = role.getExtendBox().getPetEquipPosition().intValue() * Const.BAG_POS.intValue() + Const.PET_EQUIP_BAG_POS_MIN.intValue();
        if (petEquipMap.values().stream().filter(e -> (e.getPId() <= 0L)).count() >= pos)
          return Integer.valueOf(-1); 
      } 
      return Integer.valueOf(1);
    } 
    Map<Long, BagEquip> bagEquipMap = getBagEquip(role);
    for (Long key : bagEquipMap.keySet()) {
      BagEquip equip = bagEquipMap.get(key);
      list.add(Integer.valueOf(equip.getPos()));
    } 
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    for (Long key : bagGoodsMap.keySet()) {
      BagGoods bagGoods = bagGoodsMap.get(key);
      list.add(Integer.valueOf(bagGoods.getPos()));
    } 
    return getVacancySize(role, list, is);
  }
  
  public int residuePetEquipBagPos(Role role) {
    int totalPos = 0;
    int used = 0;
    Map<Long, PetEquip> petEquipMap = (Map<Long, PetEquip>)PetCache.PET_EQUIP_MAP.get(role.getId());
    if (petEquipMap != null)
      used = petEquipMap.size(); 
    totalPos = role.getExtendBox().getPetEquipPosition().intValue() * Const.BAG_POS.intValue() + Const.PET_EQUIP_BAG_POS_MIN.intValue();
    return totalPos - used;
  }
  
  public int residueBagPos(Role role, boolean isStore) {
    int totalPos = 0;
    int used = 0;
    if (!isStore) {
      Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
      Map<Long, BagEquip> bagEquipMap = getBagEquip(role);
      totalPos = role.getExtendBox().getPosition().intValue() * Const.BAG_POS.intValue() + role.getRelive().intValue() * Const.BAG_POS.intValue() + Const.BAG_POS_MIN.intValue();
      used = bagGoodsMap.size() + bagEquipMap.size();
    } 
    return totalPos - used;
  }
  
  public Integer getVacancySize(Role role, List<Integer> list, boolean is) {
    List<Integer> objects = new ArrayList<>();
    Integer pos = Integer.valueOf(0);
    if (is) {
      pos = Integer.valueOf(role.getExtendBox().getPetEquipPosition().intValue() * Const.BAG_POS.intValue() + Const.PET_EQUIP_BAG_POS_MIN.intValue());
    } else {
      pos = Integer.valueOf(role.getExtendBox().getPosition().intValue() * Const.BAG_POS.intValue() + role.getRelive().intValue() * Const.BAG_POS.intValue() + Const.BAG_POS_MIN.intValue());
    } 
    if (list.size() < pos.intValue()) {
      for (int i = 1; i <= pos.intValue(); i++)
        objects.add(Integer.valueOf(i)); 
      List<Integer> vacancy = new ArrayList<>();
      for (Integer v : objects) {
        if (!list.contains(v))
          vacancy.add(v); 
      } 
      return (vacancy.size() <= 0) ? Integer.valueOf(-1) : Collections.<Integer>min(vacancy);
    } 
    return Integer.valueOf(-1);
  }
  
  public void addPetEquip(Role role, PetEquip equip, boolean flag, String cause) {
    equip.save();
    Map<Long, PetEquip> bagEquipMap = getPetBagEquip(role.getId().longValue());
    bagEquipMap.put(equip.getId(), equip);
    refreshPetEquip(role, equip);
    if (flag) {
      RespIconCartoon respIconCartoon = new RespIconCartoon();
      respIconCartoon.setSerial(equip.getSerial());
      respIconCartoon.setOnlyId(String.valueOf(equip.getId()));
      MessagePusher.pushMessage(role, (Message)respIconCartoon);
    } 
    RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)0, (byte)1, cause + "，装备入包 - " + JsonUtils.object2String(equip), role.getMarking());
  }
  
  public void addEquip(Role role, BagEquip equip, boolean flag, String cause) {
    equip.save();
    Map<Long, BagEquip> bagEquipMap = getBagEquip(role);
    bagEquipMap.put(equip.getId(), equip);
    refreshRoleEquip(role, equip, false);
    if (flag) {
      RespIconCartoon respIconCartoon = new RespIconCartoon();
      respIconCartoon.setSerial(equip.getType());
      respIconCartoon.setOnlyId(String.valueOf(equip.getId()));
      MessagePusher.pushMessage(role, (Message)respIconCartoon);
    } 
    RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)0, (byte)1, cause + "，装备入包 - " + JsonUtils.object2String(equip), role.getMarking());
  }
  
  public int addGoods(Role role, int serial, int num, int bd, boolean isCartoon, boolean isRecord, String cause) {
    if (num <= 0) {
      this.logger.error("添加道具到背包错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getRoleId(), role.getName(), Integer.valueOf(serial), Integer.valueOf(num), cause });
      return -1;
    } 
    if (num >= 5999)
      this.logger.error("添加道具到背包可能异常，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getRoleId(), role.getName(), Integer.valueOf(serial), Integer.valueOf(num), cause }); 
    int record = num;
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
    if (easyInfo != null && easyInfo.getIsOpen().intValue() == 1) {
      if (Const.currency.contains(easyInfo.getType())) {
        this.roleService.addCurrency(role, Integer.valueOf(easyInfo.getType().intValue() % 1000), Long.valueOf(easyInfo.getValue().longValue() * num), "添加道具到背包", true, false);
        role.save();
        return -1;
      } 
      if (easyInfo.getCategory().intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()) {
        for (int i = 0; i < num; i++) {
          boolean gift = openGift(role, Integer.valueOf(serial), false);
          if (!gift)
            break; 
        } 
        return -1;
      } 
      if (otherItem(role, serial, num, cause))
        return -1; 
      this.logger.error("角色{} 直接开启物品未处理：编号为 = {}, 数量 = {}", new Object[] { role.getName(), Integer.valueOf(serial), Integer.valueOf(num) });
    } else if (otherItem(role, serial, num, cause)) {
      return -1;
    } 
    Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(serial));
    if (type.intValue() >= ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue() && type.intValue() <= ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue())
      return -1; 
    Map map = DataGlobalFunc.GetItemDataByItemTypeId(Integer.valueOf(serial));
    if (map == null) {
      if (otherItem(role, serial, num, cause))
        return -1; 
      String format = MessageFormat.format("角色{} 未找到物品：编号为 = {}, 数量 = {}", new Object[] { role.getName(), Integer.valueOf(serial), Integer.valueOf(num) });
      this.logger.error(format);
      RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)1, format, role.getMarking());
      return -1;
    } 
    Object obj = map.get(Integer.valueOf(serial));
    JSONObject json = JsonUtils.objectToJson(obj);
    int canMerge = json.getInteger("canMerge").intValue();
    Integer original = getBagGoodsNum(role, Integer.valueOf(serial));
    List<BagGoods> collect = (List<BagGoods>)bagGoodsMap.values().stream().filter(e -> (e.getSerial() == serial)).collect(Collectors.toList());
    if (collect.size() > 0)
      for (BagGoods goods : collect) {
        int residue = canMerge - DataGlobalFunc.getBagGoodsNum(goods);
        if (residue <= 0)
          continue; 
        int count = 0;
        if (residue >= num) {
          saveBagGoods(role, serial, num, goods, count, bd, isCartoon);
          num = 0;
          break;
        } 
        num = Math.abs(residue - num);
        saveBagGoods(role, serial, residue, goods, num, bd, isCartoon);
      }  
    while (num > 0) {
      int add = 0;
      int mod = num - canMerge;
      if (mod > 0) {
        add = canMerge;
      } else {
        add = num;
      } 
      num = mod;
      addBagGoods(role, bagGoodsMap, serial, add, bd, isCartoon);
    } 
    if (cause == null)
      cause = ""; 
    RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)1, String.format("道具：%d，原有数量：%d，新增数量：%d，原因：%s", new Object[] { Integer.valueOf(serial), original, Integer.valueOf(record), cause }), role.getMarking());
    return num;
  }
  
  public boolean otherItem(Role role, int serial, int num, String cause) {
    SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(serial));
    if (card != null) {
      this.cardService.addCard(role, Integer.valueOf(card.getSerial()), Integer.valueOf(num), Integer.valueOf(0), true, true);
      return true;
    } 
    if (serial == 9997) {
      RoleTianFuService bean = (RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class);
      List<RoleTianFu> list = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId());
      RoleTianFu debris = list.stream().filter(obj -> (obj.getTianFuSerial() == serial && obj.getStorePos() == 0)).findFirst().orElse(null);
      if (debris != null) {
        if (debris.getCount() + num > 9999) {
          RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)1, String.format("天策格子不足放入临时背包-未处理（1），道具：%d，新增数量：%d，原因：%s", new Object[] { Integer.valueOf(serial), Integer.valueOf(num), cause }), role.getMarking());
          return true;
        } 
        debris.setCount((short)(debris.getCount() + num));
      } else {
        if (num > 9999) {
          RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)1, String.format("天策格子不足放入临时背包-未处理（2），道具：%d，新增数量：%d，原因：%s", new Object[] { Integer.valueOf(serial), Integer.valueOf(num), cause }), role.getMarking());
          return true;
        } 
        debris = new RoleTianFu();
        debris.setId(Long.valueOf(IdGenerator.getNextId()));
        debris.setRoleId(role.getId().longValue());
        debris.setTianFuSerial(9997);
        debris.setCreateTime(new Date());
        debris.setCount((short)num);
        ((List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId())).add(debris);
      } 
      debris.save();
      bean.refreshTianFuDebris(role);
      cartoon(role, Integer.valueOf(9997));
      RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)1, String.format("道具：%d，新增数量：%d，原因：%s", new Object[] { Integer.valueOf(serial), Integer.valueOf(num), cause }), role.getMarking());
      return true;
    } 
    return false;
  }
  
  public Integer consumeBagGoods(Role role, int serial, int num, boolean isRecord, boolean isSend, String cause) {
    if (num <= 0 && cause != null) {
      this.logger.error("消耗道具错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getName(), role.getRoleId(), Integer.valueOf(serial), Integer.valueOf(num), cause });
      return Integer.valueOf(10000000);
    } 
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    Integer sum = (Integer)bagGoodsMap.values().stream().filter(e -> (e.getSerial() == serial)).collect(Collectors.summingInt(e -> DataGlobalFunc.getBagGoodsNum(e)));
    if (sum.intValue() >= num) {
      List<BagGoods> collect = (List<BagGoods>)bagGoodsMap.values().stream().filter(e -> (e.getSerial() == serial)).collect(Collectors.toList());
      int count = 0;
      try {
        Integer original = getBagGoodsNum(role, Integer.valueOf(serial));
        for (BagGoods goods : collect) {
          int goodsNum = DataGlobalFunc.getBagGoodsNum(goods);
          Integer surplus = Integer.valueOf(goodsNum - num - count);
          if (surplus.intValue() <= 0) {
            goods.setRests(new NutMap());
            count += goodsNum;
            goods.setDelete(true);
            bagGoodsMap.remove(goods.getId());
            goods.save();
          } else {
            count += goodsNum - surplus.intValue();
            goods = consume(bagGoodsMap, goods, (surplus.intValue() > 0) ? (goodsNum - surplus.intValue()) : goodsNum);
          } 
          if (isSend)
            refreshRoleGoods(role, goods, false); 
          if (count >= num)
            break; 
        } 
        if (cause == null)
          cause = ""; 
        RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)2, String.format("道具：%d，原有数量：%d，减少数量：%d，原因：%s", new Object[] { Integer.valueOf(serial), original, Integer.valueOf(num), cause }), role.getMarking());
      } catch (ConcurrentModificationException e) {
        LoggerUtils.error("消耗道具下标异常", new Object[] { e.getMessage() });
      } 
      if (isRecord);
      return null;
    } 
    return Integer.valueOf(num - sum.intValue());
  }
  
  public Integer consumeBagGoodsByPos(Role role, int num, int pos, boolean isRecord, String cause) {
    if (num <= 0) {
      this.logger.error("根据位置消耗道具错误，角色编号 = {}，角色名称 = {}，道具编号 = {}， 道具数量 = {}，操作位置 = {}", new Object[] { role.getName(), role.getRoleId(), Integer.valueOf(pos), Integer.valueOf(num), cause });
      return Integer.valueOf(10000000);
    } 
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    BagGoods goods = bagGoodsMap.values().stream().filter(e -> (e.getPos() == pos)).findFirst().orElse(null);
    if (goods != null) {
      int count = DataGlobalFunc.getBagGoodsNum(goods);
      if (count < num)
        return Integer.valueOf(num - count); 
      Integer surplus = Integer.valueOf(count - num);
      if (surplus.intValue() <= 0) {
        goods.setRests(new NutMap());
        goods.setDelete(true);
        goods.save();
        bagGoodsMap.remove(goods.getId());
      } else {
        goods = consume(bagGoodsMap, goods, (surplus.intValue() > 0) ? num : count);
      } 
      refreshRoleGoods(role, goods, false);
      RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)2, String.format("根据位置消耗道具：%d，减少数量：%d，原因：%s", new Object[] { Integer.valueOf(goods.getSerial()), Integer.valueOf(num), cause }), role.getMarking());
      if (isRecord);
      return null;
    } 
    return Integer.valueOf(num);
  }
  
  public Integer getBagGoods(Role role, int serial, int num) {
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    Integer sum = (Integer)bagGoodsMap.values().stream().filter(goods -> (goods.getSerial() == serial)).collect(Collectors.summingInt(goods -> DataGlobalFunc.getBagGoodsNum(goods)));
    if (sum.intValue() >= num)
      return null; 
    return Integer.valueOf(num - sum.intValue());
  }
  
  public Integer getBagGoodsNum(Role role, Integer serial) {
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    Integer sum = (Integer)bagGoodsMap.values().stream().filter(goods -> (goods.getSerial() == serial.intValue())).collect(Collectors.summingInt(goods -> DataGlobalFunc.getBagGoodsNum(goods)));
    return Integer.valueOf((sum == null) ? 0 : sum.intValue());
  }
  
  public boolean saveBagGoods(Role role, int serial, int num, BagGoods goods, int surplus, int bd, boolean isCartoon) {
    String key = String.valueOf(bd);
    int count = goods.getRests().getInt(key);
    if (count > 0) {
      goods.getRests().setv(key, Integer.valueOf(count + num));
    } else {
      goods.getRests().setv(key, Integer.valueOf(num));
    } 
    refreshRoleGoods(role, goods, false);
    if (surplus >= 0 && isCartoon)
      cartoon(role, Integer.valueOf(serial)); 
    goods.save();
    return true;
  }
  
  public boolean addBagGoods(Role role, Map<Long, BagGoods> bagGoodsMap, int serial, int num, int bd, boolean isCartoon) {
    int pos = getPos(role, false);
    if (pos <= 0) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("物品栏空间不足,已经物品放置临时包裹"));
      saveTemporary(role, Integer.valueOf(serial), Integer.valueOf(1), Integer.valueOf(num), Integer.valueOf(bd));
      return false;
    } 
    NutMap nutMap = new NutMap();
    nutMap.setv(String.valueOf(bd), Integer.valueOf(num));
    BagGoods goods = new BagGoods(this.worker.nextId(), role.getId().longValue(), serial, pos, nutMap);
    if (Const.treasureMap.contains(Integer.valueOf(serial))) {
      NutMap specialMap = treasureMapHand();
      goods.setSpecial(specialMap);
    } 
    if (isCartoon)
      cartoon(role, Integer.valueOf(serial)); 
    refreshRoleGoods(role, goods, false);
    goods.save();
    bagGoodsMap.put(goods.getId(), goods);
    return true;
  }
  
  public NutMap treasureMapHand() {
    Random random = new Random();
    int index = random.nextInt(Const.treasureScene.size());
    Integer serial = Const.treasureScene.get(index);
    Pos pos = DataGlobalFunc.getTreasureMap(serial.intValue());
    NutMap map = new NutMap();
    map.setv("map", pos.getMapId());
    map.setv("x", pos.getX());
    map.setv("y", pos.getY());
    return map;
  }
  
  public void cartoon(Role role, Integer serial) {
    if (this.roleService.mouserCartoon(role, serial.toString(), 200L)) {
      RespIconCartoon respIconCartoon = new RespIconCartoon();
      respIconCartoon.setSerial(serial.intValue());
      MessagePusher.pushMessage(role, (Message)respIconCartoon);
    } 
  }
  
  public void refreshPetEquip(Role role, PetEquip equip) {
    RespFlyPetEqInfo respFlyPetEqInfo = new RespFlyPetEqInfo();
    respFlyPetEqInfo.setEquip(new PetEquipEasy(equip));
    MessagePusher.pushMessage(role, (Message)respFlyPetEqInfo);
  }
  
  public void refreshRoleEquip(Role role, BagEquip equip, boolean isStore) {
    RespFlyInfo respFlyInfo = new RespFlyInfo();
    respFlyInfo.setEquip(new EquipEasy(equip, role.getRoleId().intValue()));
    if (isStore)
      respFlyInfo.setPos((byte)1); 
    MessagePusher.pushMessage(role, (Message)respFlyInfo);
  }
  
  public void refreshRoleGoods(Role role, BagGoods goods, boolean isStore) {
    RespFlyInfo respFlyInfo = new RespFlyInfo();
    respFlyInfo.setType((byte)1);
    List<FiedValue> list = new ArrayList<>();
    NutMap rests = goods.getRests();
    try {
      for (String key : rests.keySet())
        list.add(new FiedValue(key, (byte)3, Integer.valueOf(rests.getInt(key)))); 
      NutMap special = goods.getSpecial();
      for (String key : special.keySet())
        list.add(new FiedValue(key, (byte)3, Integer.valueOf(special.getInt(key)))); 
    } catch (ConcurrentModificationException concurrentModificationException) {}
    respFlyInfo.setGoods(new Goods(String.valueOf(goods.getId()), Integer.valueOf(DataGlobalFunc.getBagGoodsNum(goods)), Integer.valueOf(goods.getSerial()), Integer.valueOf(goods.getPos()), list));
    if (isStore)
      respFlyInfo.setPos((byte)1); 
    MessagePusher.pushMessage(role, (Message)respFlyInfo);
  }
  
  public void removeItem(Role role, int pos, int type, String des) {
    if (type == 0) {
      Map<Long, BagEquip> bagEquipMap = getBagEquip(role);
      BagEquip bagEquip = bagEquipMap.values().stream().filter(e -> (e.getPos() == pos)).findFirst().orElse(null);
      if (bagEquip != null)
        deleteRoleEquip(role, bagEquip, des); 
    } else {
      deleteRoleItem(role, pos, des);
    } 
  }
  
  public void deleteRoleItem(Role role, int pos, String des) {
    Map<Long, BagGoods> bagGoods = getBagGoods(role);
    BagGoods goods = bagGoods.values().stream().filter(e -> (e.getPos() == pos)).findFirst().orElse(null);
    if (goods != null) {
      goods.setRests(new NutMap());
      goods.setDelete(true);
      goods.save();
      bagGoods.remove(goods.getId());
      deleteInform(role, (byte)0, pos);
      if (des != null)
        RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1, (byte)2, des + "，道具编号：" + goods.getSerial(), role.getMarking()); 
    } 
  }
  
  public boolean deleteRoleEquip(Role role, BagEquip equip, String cause) {
    Map<Long, BagEquip> bagEquipMap = getBagEquip(role);
    if (bagEquipMap.get(equip.getId()) != null) {
      bagEquipMap.remove(equip.getId());
      equip.setDelete(true);
      equip.save();
      deleteInform(role, (byte)0, equip.getPos());
      EventDispatcher.getInstance().fireEvent((BaseGameEvent)new DelEquipEvent(EventType.DEL_EQUIP, role, equip.getId().longValue()));
      if (cause != null)
        RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)0, (byte)2, cause + "，删除装备，" + JsonUtils.object2String(equip), role.getMarking()); 
      return true;
    } 
    return false;
  }
  
  public void deleteInform(Role role, byte type, int pos) {
    MessagePusher.pushMessage(role, (Message)new RespDeleteItem(type, pos));
  }
  
  public void saveTemporary(Role role, Integer serial, Integer type, Integer num, Integer bd) {
    String key = "temporary" + role.getId();
    Overflow overflow = (Overflow)this.redisService.get(key, Overflow.class);
    if (overflow == null)
      overflow = new Overflow(); 
    List<Temporary> list = overflow.getList();
    if (list.size() < 8) {
      list.add(new Temporary(IdUtils.simpleUUID(), serial, type, num, bd));
      overflow.setList(list);
      lodeTemporary(role, list);
      this.redisService.set(key, (Serializable)overflow, 0L);
    } else {
      String format = MessageFormat.format("临时背包流失 = 编号：{0}，物品类型：{1}，流失数量：{2}", new Object[] { serial, (type.intValue() == 0) ? "装备" : "道具", num });
      RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)2, format, role.getMarking());
    } 
  }
  
  public void getTemporary(Role role, String id) {
    String key = "temporary" + role.getId();
    Overflow overflow = (Overflow)this.redisService.get(key, Overflow.class);
    if (overflow == null)
      return; 
    List<Temporary> list = overflow.getList();
    if ("0".equals(id)) {
      for (int i = list.size() - 1; i >= 0; i--) {
        Temporary temporary = list.get(i);
        Overflow fetch = fetch(role, list, temporary.getId());
        list = fetch.getList();
        if (!fetch.isFlag())
          break; 
      } 
    } else {
      Overflow fetch = fetch(role, list, id);
      list = fetch.getList();
    } 
    if (list.size() > 0) {
      overflow.setList(list);
      lodeTemporary(role, list);
      this.redisService.set(key, (Serializable)overflow, 0L);
    } else {
      this.redisService.del(new Serializable[] { key });
      lodeTemporary(role, new ArrayList<>());
    } 
  }
  
  public Overflow fetch(Role role, List<Temporary> list, String id) {
    boolean flag = true;
    Overflow overflow = new Overflow();
    List<String> dels = new ArrayList<>();
    List<Temporary> collect = (List<Temporary>)list.stream().filter(e -> e.getId().equals(id)).collect(Collectors.toList());
    for (Temporary temporary : collect) {
      if (temporary.getType().intValue() == 0) {
        if (getPos(role, false) > -1) {
          dels.add(temporary.getId());
          ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(temporary.getSerial()), 0, false, "临时背包");
          continue;
        } 
        flag = false;
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你的物品栏已满"));
        break;
      } 
      if (checkPos(role, temporary.getSerial().intValue(), temporary.getNum().intValue())) {
        dels.add(temporary.getId());
        int num = addGoods(role, temporary.getSerial().intValue(), temporary.getNum().intValue(), temporary.getRests().intValue(), true, true, "临时背包取出");
        if (num > 0) {
          flag = false;
          list.add(new Temporary(IdUtils.fastUUID(), temporary.getSerial(), temporary.getType(), Integer.valueOf(num), temporary.getRests()));
          break;
        } 
        continue;
      } 
      flag = false;
    } 
    for (String del : dels)
      list.removeIf(e -> e.getId().equals(del)); 
    overflow.setFlag(flag);
    overflow.setList(list);
    return overflow;
  }
  
  public void lodeTemporary(Role role, List<Temporary> list) {
    List<TemporaryVo> objects = new ArrayList<>();
    for (Temporary temporary : list) {
      TemporaryVo temporaryVo = new TemporaryVo();
      temporaryVo.setId(temporary.getId());
      temporaryVo.setSerial(temporary.getSerial());
      temporaryVo.setNum(temporary.getNum());
      objects.add(temporaryVo);
    } 
    MessagePusher.pushMessage(role, (Message)new RespLodeTemporary(objects));
  }
  
  public void useItem(Role role, int pos, short num) {
    boolean clicker = this.roleService.mouserClicker(role, "use", 250L);
    if (!clicker)
      return; 
    try {
      Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
      BagGoods goods = bagGoodsMap.values().stream().filter(item -> (item.getPos() == pos)).findFirst().orElse(null);
      if (goods == null)
        return; 
      if (DataGlobalFunc.getBagGoodsNum(goods) < num) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("物品数量不足"));
        return;
      } 
      Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(goods.getSerial()));
      if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_GIFT.getValue()))) {
        for (int i = 0; i < num; i++) {
          boolean b = openGift(role, Integer.valueOf(goods.getSerial()), true);
          if (!b)
            break; 
        } 
      } else if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue()))) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(Integer.valueOf(goods.getSerial()));
        if (sysPet != null) {
          PetService bean = (PetService)SpringUtils.getBean(PetService.class);
          boolean flag = bean.canGetPet(role, sysPet, false);
          if (flag) {
            int bd = 0;
            if (goods.getRests().getInt("2", 0) > 0)
              bd = 2; 
            Pet pet = bean.addPet(role, sysPet, bd, "宠物碎片合成");
            bean.loadPet(role, pet);
            deleteRoleItem(role, pos, "宠物碎片合成");
          } 
        } 
      } else if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_STUFF.getValue()))) {
        ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(goods.getSerial()));
        if (CardDataPool.cardFiveExerciseItem.contains(Integer.valueOf(goods.getSerial()))) {
          if (DataGlobalFunc.needGradeJudge(1, 80, role.getRelive().intValue(), role.getLevel().intValue(), "") != null) {
            MessagePusher.pushMessage(role, (Message)new RespMsg("#<LG1>五行修炼#1转80级开启，请先提升等级"));
            return;
          } 
          CardExercise cardExercise = this.cardService.getCardExercise(role);
          Param wxInfo = this.cardService.getWxInfo(this.cardService.getCardExercise(role), info.getType().intValue());
          if (wxInfo.getK().intValue() >= CardDataPool.cardFiveExerciseMaxGrade) {
            MessagePusher.pushMessage(role, (Message)new RespMsg("你已经将该五行修炼至登峰造极"));
            return;
          } 
          Integer integer = consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-五行修炼");
          if (integer != null)
            return; 
          this.cardService.addExp(role, info.getValue().intValue(), info.getType().intValue(), cardExercise, wxInfo, "使用道具");
        } else {
          SysEffect sysEffect = (SysEffect)ItemDataPool.effectMap.get(info.getApplyObjectSerial());
          if (sysEffect != null) {
            Integer integer = consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-特效" + info.getName());
            if (integer != null)
              return; 
            SpringUtils.getEquipService().addEffect(role, sysEffect.getSerial(), 0);
            MessagePusher.pushMessage(role, (Message)new RespCherish(info.getSerial().intValue(), (short)1));
          } 
        } 
      } else if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue()))) {
        ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(goods.getSerial()));
        if (info != null) {
          if (info.getUseCntLimit().intValue() > 0) {
            int anInt = role.getExtendBox().getDayQuotaLimit().getInt(String.valueOf(info.getSerial()), 0);
            if (anInt + num > info.getUseCntLimit().intValue()) {
              int may = info.getUseCntLimit().intValue() - anInt;
              if (may <= 0) {
                MessagePusher.pushMessage(role, (Message)new RespMsg("今日使用" + info.getName() + "已上限，请明日再使用吧！"));
                return;
              } 
              num = (short)may;
            } 
            role.getExtendBox().getDayQuotaLimit().setv(String.valueOf(info.getSerial()), Integer.valueOf(anInt + num));
          } 
          if (info.getType().intValue() > 0 && Const.currency.contains(info.getType())) {
            this.roleService.addCurrency(role, Integer.valueOf(info.getType().intValue() % 1000), Long.valueOf(info.getValue().longValue() * num), "使用物品-货币", true, false);
            consumeBagGoods(role, goods.getSerial(), Integer.valueOf(num).intValue(), true, true, "使用物品-货币");
          } else if (info.getType().intValue() == ItemClassify.STATUS.getId()) {
            useStatus(role, info, num);
          } else if (info.getType().intValue() == ItemClassify.PLUG.getId()) {
            int current = (new Long(System.currentTimeMillis() / 1000L)).intValue();
            Setting setting = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
            int endTime = setting.getPlugEndTime();
            if (endTime <= 0 || endTime <= current)
              endTime = current; 
            setting.setPlugEndTime(endTime + info.getValue().intValue() * num);
            setting.save();
            consumeBagGoods(role, goods.getSerial(), Integer.valueOf(num).intValue(), true, true, "使用物品-小助手");
            MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg(String.format("小助手到期时间：#<R>%s#", new Object[] { TimeUtil.convertTime(setting.getPlugEndTime()) })));
          } else if (goods.getSerial() == 90013) {
            this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, Long.valueOf(info.getValue().longValue() * num), "使用物品-帮派书简", true, false);
            consumeBagGoods(role, goods.getSerial(), Integer.valueOf(num).intValue(), true, true, "使用物品-帮派书简");
          } else if (info.getType().intValue() == ItemClassify.HERO_EXP.getId()) {
            this.roleService.addExp(role, info.getValue().longValue() * num, "使用道具", true);
            consumeBagGoods(role, goods.getSerial(), Integer.valueOf(num).intValue(), true, true, "使用物品-人物经验");
          } else if (goods.getSerial() == 90009) {
            if (role.getMasquerade() > 0) {
              consumeBagGoods(role, goods.getSerial(), 1, true, true, "使用物品-还原丹");
              ((CardService)SpringUtils.getBean(CardService.class)).cardExpire(role);
            } 
          } else {
            this.logger.error("使用道具未处理：{}", Integer.valueOf(goods.getSerial()));
          } 
        } else {
          this.logger.error("未找到消耗的道具信息：{}", Integer.valueOf(goods.getSerial()));
        } 
      } else {
        MessagePusher.pushMessage(role, (Message)new RespMsg("使用物品未处理"));
      } 
    } catch (Exception e) {
      this.logger.error("使用物品异常---------------------------------------------------");
      e.printStackTrace();
    } 
  }
  
  public boolean openGift(Role role, Integer typeId, boolean isConsume) {
    SysGiftItem giftItem = (SysGiftItem)ItemDataPool.data_gift_item.get(typeId);
    List<Item> objects = new ArrayList<>();
    objects.addAll(giftItem.getExtra());
    boolean flag = checkPos(role, giftItem.getSerial().intValue(), 1);
    if (!flag)
      return false; 
    if (isConsume) {
      Integer bagGoods = consumeBagGoods(role, giftItem.getSerial().intValue(), 1, true, true, "开启礼包");
      if (bagGoods != null)
        return false; 
    } 
    if (flag && giftItem.getCycle().equals(Integer.valueOf(1))) {
      List<SysGiftItemOpen> collect = (List<SysGiftItemOpen>)ItemDataPool.data_gift_item_open.stream().filter(item -> item.getGiftSerial().equals(giftItem.getSerial())).collect(Collectors.toList());
      if (collect.size() <= 0) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("使用物品失败"));
        return false;
      } 
      List<Lottery> list = new ArrayList<>();
      collect.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", Double.valueOf(itemOpen.getRatio()))));
      List<Double> orignalRates = new ArrayList<>(list.size());
      for (Lottery lottery : list)
        orignalRates.add(lottery.getBili()); 
      for (int j = 0; j < giftItem.getCycle().intValue(); j++) {
        int index = LotteryUtils.lottery(orignalRates);
        Integer serial = ((Lottery)list.get(index)).getSerial();
        SysGiftItemOpen giftItemOpen = collect.stream().filter(item -> item.getId().equals(serial)).findFirst().orElse(null);
        if (giftItemOpen != null) {
          objects.add(new Item(giftItemOpen.getSerial(), giftItemOpen.getNum()));
        } else {
          MessagePusher.pushMessage(role, (Message)new RespMsg("使用物品错误"));
          return false;
        } 
      } 
    } else if (giftItem.getCycle().equals(Integer.valueOf(8866))) {
      if (giftItem.getParam() == null)
        return false; 
      Prop prop = (Prop)JsonUtils.string2Object(giftItem.getParam(), Prop.class);
      List<Param> list = new ArrayList<>();
      if (prop.getOnset() == prop.getOver()) {
        list.add(new Param(Integer.valueOf(prop.getSerial()), Integer.valueOf(prop.getOver())));
      } else {
        List<Integer> integers = Arrays.asList(new Integer[] { Integer.valueOf(70), Integer.valueOf(28), Integer.valueOf(2) });
        Double num = Double.valueOf(MakeAttribute.createRndRange(new double[] { prop.getOnset(), prop.getOver() }, integers));
        list.add(new Param(Integer.valueOf(prop.getSerial()), Integer.valueOf(num.intValue())));
      } 
      sendReward(role, list, "开启礼包：" + giftItem.getSerial(), 0);
    } 
    if (giftItem.getCoin().longValue() > 0L)
      this.roleService.addCurrency(role, Const.RESTYPE_COIN, giftItem.getCoin(), "开启礼包", true, false); 
    if (giftItem.getSilver().longValue() > 0L)
      this.roleService.addCurrency(role, Const.RESTYPE_SILVER, giftItem.getSilver(), "开启礼包", true, false); 
    if (giftItem.getGold().longValue() > 0L)
      this.roleService.addCurrency(role, Const.RESTYPE_GOLD, giftItem.getGold(), "开启礼包", true, false); 
    if (giftItem.getExp() > 0)
      openGiftExp(role, giftItem.getExp()); 
    for (Item item : objects) {
      if (item.getItem().intValue() > 0 && item.getNum().intValue() > 0)
        addGoods(role, item.getItem().intValue(), item.getNum().intValue(), giftItem.getBind().intValue(), true, true, "开启礼包"); 
    } 
    return true;
  }
  
  public void openGiftExp(Role role, int serial) {
    long petExp = 0L;
    long heroExp = 0L;
    Pet pet = null;
    if (serial == 3003) {
      heroExp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + (role.getLevel().intValue() + role.getRelive().intValue() * 15.0D) / 140.0D * 2.5D) / 30.0D * 3.0D)).longValue();
      Integer combatPet = role.getExtendBox().getCombatPet();
      if (combatPet.intValue() > 0) {
        pet = (Pet)this.petService.selectPet(role).get(combatPet);
        if (pet != null)
          heroExp = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf(1.6E7D * (1.0D + (pet.getPetLv().intValue() + pet.getRebirth().intValue() * 15.0D) / 140.0D * 2.5D) / 30.0D * 3.0D * 1.2D)).longValue(); 
      } 
    } 
    if (heroExp > 0L)
      this.roleService.addExp(role, heroExp, "", true); 
    if (pet != null && petExp > 0L)
      this.petService.addExp(role, pet, petExp, true); 
  }
  
  public void openGiftTips(Role role, Integer serial, Integer num, String tips) {
    Integer itemType = DataGlobalFunc.GetItemTypeByItemTypeId(serial);
    if (itemType.intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_OTHERITEM.getValue())
      if (DataGlobalFunc.getItemSubTypeByItemTypeId(serial).intValue() == 2) {
        int bookType = DataGlobalFunc.getPetSkillBookTypeByItemTypeId(serial).intValue();
        if (bookType == PETSKILLSUB.SUBTYPE_SUPREME.getValue());
      }  
  }
  
  public BagGoods consume(Map<Long, BagGoods> bagGoodsMap, BagGoods goods, int consume) {
    NutMap rests = goods.getRests();
    String key = String.valueOf(2);
    int num = rests.getInt(key, 0);
    if (num < consume) {
      rests.remove(key);
      consume -= num;
      List<Integer> list = new ArrayList<>();
      for (String s : rests.keySet())
        list.add(Integer.valueOf(s)); 
      List<Integer> collect = (List<Integer>)list.stream().sorted(Comparator.comparing(Integer::intValue)).collect(Collectors.toList());
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
      if (residue <= 0) {
        rests.remove(String.valueOf(key));
      } else {
        rests.setv(String.valueOf(key), Integer.valueOf(residue));
      } 
    } 
    goods.setRests(rests);
    if (DataGlobalFunc.getBagGoodsNum(goods) <= 0) {
      goods.setRests(new NutMap());
      goods.setDelete(true);
      bagGoodsMap.remove(goods.getId());
    } 
    goods.save();
    return goods;
  }
  
  public void useDrug(Role role, ReqDrugUse drugUse) {
    Integer goodsNum = getBagGoodsNum(role, Integer.valueOf(drugUse.getSerial()));
    if (goodsNum.intValue() <= 0)
      return; 
    SysDrug drug = (SysDrug)ItemDataPool.data_drug.get(Integer.valueOf(drugUse.getSerial()));
    if (drug != null) {
      if (drug.getShowClass().intValue() == 4 || drug.getUseType().intValue() == 2)
        return; 
      if (drug.getDrugAddHpStored().intValue() > 0) {
        consumeBagGoods(role, drug.getSerial().intValue(), 1, true, true, "使用药品-气血储备");
        role.setSaveLife(Long.valueOf(role.getSaveLife().longValue() + drug.getDrugAddHpStored().intValue()));
        MessagePusher.notify2Player(role, 104, new Object[] { drug.getName(), "气血", drug.getDrugAddHpStored() });
        this.roleService.refreshRoleStore(role);
        role.save();
        return;
      } 
      if (drug.getDrugAddMpStored().intValue() > 0) {
        consumeBagGoods(role, drug.getSerial().intValue(), 1, true, true, "使用药品-法力储备");
        role.setSaveMana(Long.valueOf(role.getSaveLife().longValue() + drug.getDrugAddMpStored().intValue()));
        MessagePusher.notify2Player(role, 104, new Object[] { drug.getName(), "法力", drug.getDrugAddMpStored() });
        this.roleService.refreshRoleStore(role);
        role.save();
        return;
      } 
      if (role.getRoleId().equals(Integer.valueOf(drugUse.getId()))) {
        if (drug.getShowClass().intValue() == 1) {
          if (role.getCurrLife().intValue() >= role.getMaxLife().intValue()) {
            MessagePusher.notify2Misc(role, false, 101, new Object[] { "你" });
          } else {
            int need = role.getMaxLife().intValue() - role.getCurrLife().intValue();
            double addHp = drug.getDrugAddHpValue().intValue() + drug.getDrugAddHpPercent().intValue() / 100.0D * role.getMaxLife().intValue();
            int num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp / need));
            if (goodsNum.intValue() > num) {
              role.setCurrLife(role.getMaxLife());
              consumeBagGoods(role, drug.getSerial().intValue(), num, true, true, "使用药品-恢复气血1");
              this.roleService.refreshRoleInfo(role, false);
            } else {
              role.setCurrLife(Integer.valueOf((int)(addHp * goodsNum.intValue())));
              consumeBagGoods(role, drug.getSerial().intValue(), goodsNum.intValue(), true, true, "使用药品-恢复气血2");
              this.roleService.refreshRoleInfo(role, false);
            } 
          } 
        } else if (drug.getShowClass().intValue() == 2) {
          if (role.getCurrLife().intValue() >= role.getMaxLife().intValue()) {
            MessagePusher.notify2Misc(role, false, 102, new Object[] { "你" });
          } else {
            int need = role.getMaxLife().intValue() - role.getCurrLife().intValue();
            double addMp = drug.getDrugAddMpValue().intValue() + drug.getDrugAddMpPercent().intValue() / 100.0D * role.getMaxMana().intValue();
            int num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp / need));
            if (goodsNum.intValue() > num) {
              role.setCurrMana(role.getMaxMana());
              consumeBagGoods(role, drug.getSerial().intValue(), num, true, true, "使用药品-恢复法力1");
              this.roleService.refreshRoleInfo(role, false);
            } else {
              role.setCurrMana(Integer.valueOf((int)(addMp * goodsNum.intValue())));
              consumeBagGoods(role, drug.getSerial().intValue(), goodsNum.intValue(), true, true, "使用药品-恢复法力2");
              this.roleService.refreshRoleInfo(role, false);
            } 
          } 
        } else if (drug.getShowClass().intValue() == 3) {
          if (role.getCurrLife().intValue() >= role.getMaxLife().intValue() && role.getCurrMana().intValue() >= role.getCurrMana().intValue()) {
            MessagePusher.notify2Misc(role, false, 103, new Object[] { "你" });
          } else {
            int needLife = role.getMaxLife().intValue() - role.getCurrLife().intValue();
            int needMana = role.getMaxLife().intValue() - role.getCurrLife().intValue();
            double addHp = drug.getDrugAddHpValue().intValue() + drug.getDrugAddHpPercent().intValue() / 100.0D * role.getMaxLife().intValue();
            double addMp = drug.getDrugAddMpValue().intValue() + drug.getDrugAddMpPercent().intValue() / 100.0D * role.getMaxMana().intValue();
            int num = 1;
            if (needLife > 0)
              num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp / needLife)); 
            if (needMana > 0) {
              int upInt = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp / needLife));
              if (num < upInt)
                num = upInt; 
            } 
            if (goodsNum.intValue() > num) {
              role.setCurrLife(role.getMaxLife());
              role.setCurrMana(role.getMaxMana());
              consumeBagGoods(role, drug.getSerial().intValue(), num, true, true, "使用药品-双加药1");
              this.roleService.refreshRoleInfo(role, false);
            } else {
              role.setCurrLife(Integer.valueOf((int)(addHp * goodsNum.intValue())));
              role.setCurrMana(Integer.valueOf((int)(addMp * goodsNum.intValue())));
              consumeBagGoods(role, drug.getSerial().intValue(), goodsNum.intValue(), true, true, "使用药品-双加药2");
              this.roleService.refreshRoleInfo(role, false);
            } 
          } 
        } 
      } else {
        Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(drugUse.getId()));
        if (pet != null) {
          PetService petService = SpringUtils.getPetService();
          if (drug.getShowClass().intValue() == 1) {
            if (role.getCurrLife().intValue() >= role.getMaxLife().intValue()) {
              MessagePusher.notify2Misc(role, false, 101, new Object[] { "#<LB1>" + pet.getPetName() + "#" });
            } else {
              int need = pet.getMaxLife().intValue() - pet.getCurrLife();
              double addHp = drug.getDrugAddHpValue().intValue() + drug.getDrugAddHpPercent().intValue() / 100.0D * pet.getMaxLife().intValue();
              int num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp / need));
              if (goodsNum.intValue() > num) {
                pet.setCurrLife(pet.getMaxLife().intValue());
                consumeBagGoods(role, drug.getSerial().intValue(), num, true, true, "宠物使用药品-血药1");
                petService.refreshPetInfo(role, pet);
              } else {
                pet.setCurrLife((int)(addHp * goodsNum.intValue()));
                consumeBagGoods(role, drug.getSerial().intValue(), goodsNum.intValue(), true, true, "宠物使用药品-血药2");
                petService.refreshPetInfo(role, pet);
              } 
            } 
          } else if (drug.getShowClass().intValue() == 2) {
            if (role.getCurrLife().intValue() >= role.getMaxLife().intValue()) {
              MessagePusher.notify2Misc(role, false, 102, new Object[] { "#<LB1>" + pet.getPetName() + "#" });
            } else {
              int need = role.getMaxLife().intValue() - role.getCurrLife().intValue();
              double addMp = drug.getDrugAddMpValue().intValue() + drug.getDrugAddMpPercent().intValue() / 100.0D * role.getMaxMana().intValue();
              int num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp / need));
              if (goodsNum.intValue() > num) {
                pet.setCurrMana(pet.getMaxMana());
                consumeBagGoods(role, drug.getSerial().intValue(), num, true, true, "宠物使用药品-蓝药1");
                petService.refreshPetInfo(role, pet);
              } else {
                pet.setCurrMana(Integer.valueOf((int)(addMp * goodsNum.intValue())));
                consumeBagGoods(role, drug.getSerial().intValue(), goodsNum.intValue(), true, true, "宠物使用药品-蓝药2");
                petService.refreshPetInfo(role, pet);
              } 
            } 
          } else if (drug.getShowClass().intValue() == 3) {
            if (role.getCurrLife().intValue() >= role.getMaxLife().intValue() && role.getCurrMana().intValue() >= role.getCurrMana().intValue()) {
              MessagePusher.notify2Misc(role, false, 103, new Object[] { "#<LB1>" + pet.getPetName() + "#" });
            } else {
              int needLife = pet.getMaxLife().intValue() - pet.getCurrLife();
              int needMana = pet.getMaxLife().intValue() - pet.getCurrLife();
              double addHp = drug.getDrugAddHpValue().intValue() + drug.getDrugAddHpPercent().intValue() / 100.0D * pet.getMaxLife().intValue();
              double addMp = drug.getDrugAddMpValue().intValue() + drug.getDrugAddMpPercent().intValue() / 100.0D * pet.getMaxMana().intValue();
              int num = 1;
              if (needLife > 0)
                num = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addHp / needLife)); 
              if (needMana > 0) {
                int upInt = BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf(addMp / needLife));
                if (num < upInt)
                  num = upInt; 
              } 
              if (goodsNum.intValue() > num) {
                pet.setCurrLife(pet.getMaxLife().intValue());
                pet.setCurrMana(pet.getMaxMana());
                consumeBagGoods(role, drug.getSerial().intValue(), num, true, true, "宠物使用药品-双加药1");
                petService.refreshPetInfo(role, pet);
              } else {
                pet.setCurrLife((int)(addHp * goodsNum.intValue()));
                pet.setCurrMana(Integer.valueOf((int)(addMp * goodsNum.intValue())));
                consumeBagGoods(role, drug.getSerial().intValue(), goodsNum.intValue(), true, true, "宠物使用药品-双加药2");
                petService.refreshPetInfo(role, pet);
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  public void useStatus(Role role, ItemEasyInfo info, short type) {
    if (type <= 0) {
      role.getExtendBox().getState().remove(info.getDict());
    } else {
      Integer integer = consumeBagGoods(role, info.getSerial().intValue(), 1, true, true, "使用-" + info.getName());
      if (integer != null)
        return; 
      int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
      role.getExtendBox().getState().setv(info.getDict(), Integer.valueOf(currTime + info.getValue().intValue()));
    } 
    this.roleService.stateRefresh(role);
    role.save();
    SpringUtils.getBroadcastService().sendUpdateAppear(role);
  }
  
  public void useIncense(Role role, int serial, int residueTimes) {
    if (serial == 90005) {
      if (residueTimes > 0) {
        Integer goods = consumeBagGoods(role, serial, 1, true, true, "使用-引妖香");
        if (goods != null)
          return; 
      } else {
        residueTimes = 0;
      } 
      role.getExpireTimeMap().put("incenseExpireTime", Integer.valueOf(residueTimes));
    } else if (serial == 90006) {
      Integer goods = consumeBagGoods(role, 90006, 1, true, true, "使用-百里香");
      if (goods != null)
        return; 
      residueTimes = 0;
      role.getExpireTimeMap().put("incenseExpireTime", Integer.valueOf(residueTimes));
    } else if (serial == 90007) {
      if (residueTimes > 0) {
        Integer goods = consumeBagGoods(role, serial, 1, true, true, "使用-夺命香");
        if (goods != null)
          return; 
      } 
      role.getPkInfoBox().setIncense(residueTimes);
    } 
    this.roleService.stateRefresh(role);
    role.save();
    MessagePusher.pushMessage(role, (Message)new RespUseIncense(Integer.valueOf(serial), Integer.valueOf(residueTimes)));
  }
  
  public void itemOperate(Role role, ReqOperateItem operateItem) {
    int type = operateItem.getType();
    boolean clicker = this.roleService.mouserClicker(role, "up_it_" + type, 250L);
    if (!clicker)
      return; 
    BagHandler handler = BagHelper.INSTANCE.getHandler(Integer.valueOf(type));
    if (handler != null) {
      String message = handler.acceptOperate(role, Integer.valueOf(operateItem.getPos()), Integer.valueOf(operateItem.getNum()), operateItem.getParam());
      if (!Strings.isEmpty(message) && message != null)
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), message, false)); 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("很抱歉，该功能暂未开放。"));
    } 
  }
  
  public boolean send(Role role, int serial, int type, int num, int pos, String cause, int bd) {
    if (type == 0) {
      ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(serial), 0, false, cause);
      if (pos >= 0)
        deleteRoleItem(role, pos, null); 
      return true;
    } 
    if (type == 1 || type == 2) {
      if (serial == ItemClassify.RESTYPE_SILVER.getId()) {
        this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf(num), cause, true, false);
      } else if (serial == ItemClassify.RESTYPE_COIN.getId()) {
        this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(num), cause, true, false);
      } else if (serial == ItemClassify.RES_TYPE_ACHIEVEMENT.getId()) {
        this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, Long.valueOf(num), cause, true, false);
      } else {
        addGoods(role, serial, num, bd, true, true, cause);
      } 
      if (pos >= 0)
        deleteRoleItem(role, pos, cause); 
      return true;
    } 
    return false;
  }
  
  public void exchange(Role role, ReqExchange reqExchange) {
    boolean clicker = this.roleService.mouserClicker(role, "exchange", 250L);
    if (!clicker)
      return; 
    SysDataExchange exchange = (SysDataExchange)WelfareDataPool.exchangeMap.get(Integer.valueOf(reqExchange.getSerial()));
    if (exchange == null)
      return; 
    boolean flag = false;
    try {
      flag = checkPos(role, exchange.getSerial(), exchange.getNum() * reqExchange.getNum());
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("兑换物品检查背包空位出错 物品编号 = {}，物品数量 = {}", Integer.valueOf(reqExchange.getSerial()), Short.valueOf(reqExchange.getNum()));
      return;
    } 
    if (!flag)
      return; 
    int needNum = reqExchange.getNum() * exchange.getNeedNum();
    Integer integer = consumeBagGoods(role, exchange.getNeedSerial(), needNum, false, true, "兑换物品");
    if (integer != null) {
      ItemEasyInfo info = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(exchange.getNeedSerial()));
      if (info != null)
        MessagePusher.pushMessage(role, (Message)new RespMsg(info.getName() + "缺少：#<R>" + integer + "#")); 
      return;
    } 
    if (exchange.getType() == 1)
      addGoods(role, exchange.getSerial(), exchange.getNum() * reqExchange.getNum(), 0, true, true, "兑换物品"); 
  }
  
  public boolean shelvesConsume(Role role, int pos, int consume) {
    int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
    Map<Long, BagGoods> bagGoodsMap = getBagGoods(role);
    BagGoods goods = bagGoodsMap.values().stream().filter(e -> (e.getPos() == pos)).findFirst().orElse(null);
    if (goods == null)
      return false; 
    NutMap rests = goods.getRests();
    int num = DataGlobalFunc.unrestricted(goods.getRests());
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
    goods.setRests(rests);
    if (DataGlobalFunc.getBagGoodsNum(goods) <= 0) {
      goods.setRests(new NutMap());
      goods.setDelete(true);
      bagGoodsMap.remove(goods.getId());
    } 
    goods.save();
    refreshRoleGoods(role, goods, false);
    return true;
  }
  
  public void send(Role role, int serial, byte type, int num, int pos, byte isNotice, String tips, int bd) {
    if (type == 0) {
      send(role, serial, type, num, pos, "挖宝", bd);
    } else if (type == 1 || type == 2) {
      boolean flag = send(role, serial, type, num, pos, "挖宝", bd);
      if (flag && isNotice == 1) {
        ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
        if (easyInfo != null && tips != null) {
          String msg = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", new Object[] { Integer.valueOf(serial), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(serial), Integer.valueOf(serial), easyInfo.getName() });
          this.broadcastThread.add2Queue(String.format(tips, new Object[] { role.getName(), msg, Integer.valueOf(num) }));
        } 
      } 
    } else if (type == 3) {
      this.cardService.addCard(role, Integer.valueOf(serial), Integer.valueOf(num), Integer.valueOf(0), true, true);
      deleteRoleItem(role, pos, "挖宝");
    } else if (type == 100) {
      ((SealDemonHandler)SpringUtils.getBean(SealDemonHandler.class)).flushConditionByLevel(role, num);
      deleteRoleItem(role, pos, "挖宝封妖");
    } else if (type == 101) {
      ((ThreeScopeHandler)SpringUtils.getBean(ThreeScopeHandler.class)).flushConditionByLevel(role, num);
      deleteRoleItem(role, pos, "挖宝妖王");
    } 
  }
  
  public List<BagGoods> getBagItem(Role role, int serial) {
    Map<Long, BagGoods> goodsMap = getBagGoods(role);
    return (List<BagGoods>)goodsMap.values().stream().filter(e -> (e.getSerial() == serial)).collect(Collectors.toList());
  }
  
  public void sendReward(Role role, List<Param> rewards, String cause, int grade) {
    for (Param reward : rewards) {
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTS.getId())) && reward.getV().equals(Integer.valueOf(0)) && reward.getT().equals(Integer.valueOf(0))) {
        List<Item> list = taskRewardDynamic(reward.getD());
        for (Item item : list)
          rewardItem(role, item.getItem().intValue(), item.getNum().intValue(), 0, cause); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.HERO_EXP.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addExp(role, reward.getV().longValue(), "", true); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.PET_EXP.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0) {
          Integer combatPet = role.getExtendBox().getCombatPet();
          if (combatPet.intValue() > 0) {
            Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
            this.petService.addExp(role, pet, reward.getV().longValue(), true);
          } 
        } 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTYPE_COIN.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RESTYPE_COIN, Long.valueOf(reward.getV().longValue()), "奖励派发1 - " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTYPE_SILVER.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf(reward.getV().longValue()), "奖励派发2 - " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_BRAC.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RESTYPE_BRAC, Long.valueOf(reward.getV().longValue()), "降魔积分 - " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_BOUND_GOLD.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RES_TYPE_BOUND_GOLD, Long.valueOf(reward.getV().longValue()), "奖励派发 3 " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_GOLD.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RESTYPE_GOLD, Long.valueOf(reward.getV().longValue()), "奖励派发 4 - " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_CHARGE_SCORE.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RES_TYPE_CHARGE_SCORE, Long.valueOf(reward.getV().longValue()), "奖励派发充值积分 - " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RESTYPE_TRIBUTE.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RESTYPE_TRIBUTE, Long.valueOf(reward.getV().longValue()), "奖励派发5 - " + cause, true, false); 
        continue;
      } 
      if (reward.getK().equals(Integer.valueOf(ItemClassify.RES_TYPE_ACHIEVEMENT.getId())) && reward.getT().equals(Integer.valueOf(0))) {
        boolean flag = true;
        if (reward.getR().intValue() > 0)
          flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D); 
        if (flag && 
          reward.getT().intValue() == 0)
          this.roleService.addCurrency(role, Const.RES_TYPE_ACHIEVEMENT, Long.valueOf(reward.getV().longValue()), "奖励派发5 - " + cause, true, false); 
        continue;
      } 
      if (reward.getT().byteValue() == 2 && DataGlobalFunc.GetItemTypeByItemTypeId(reward.getK()).equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue()))) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(reward.getK());
        if (sysPet != null) {
          boolean flag = this.petService.canGetPet(role, sysPet, false);
          if (flag) {
            Pet pet = this.petService.addPet(role, sysPet, 0, cause);
            this.petService.loadPet(role, pet);
            this.petService.petInfo(role, pet);
            if (role.getExtendBox().getCombatPet().intValue() <= 0) {
              role.getExtendBox().setCombatPet(pet.getPetId());
              role.save();
            } 
            continue;
          } 
          rewardItem(role, reward.getK().intValue(), reward.getV().intValue(), reward.getBd(), cause);
        } 
        continue;
      } 
      if (reward.getT().intValue() > 0) {
        if (reward.getR().intValue() <= 0) {
          rewardItem(role, reward.getK().intValue(), reward.getV().intValue(), reward.getBd(), cause);
          continue;
        } 
        if (reward.getR().intValue() > 0) {
          boolean flag = SkillAi.canAffectOnPro(reward.getR().intValue() / 1000.0D);
          if (flag)
            rewardItem(role, reward.getK().intValue(), reward.getV().intValue(), reward.getBd(), cause); 
        } 
      } 
    } 
  }
  
  public List<Item> taskRewardDynamic(String dist) {
    List<Item> list = new ArrayList<>();
    Map<Integer, Integer> map = new HashMap<>();
    List<SysDataTaskRewardDynamic> collect = (List<SysDataTaskRewardDynamic>)TaskDataPool.taskRewardDynamicList.stream().filter(e -> e.getDict().equals(dist)).collect(Collectors.toList());
    for (SysDataTaskRewardDynamic dynamic : collect) {
      double rate = ((Double)TaskDataPool.taskRewardDynamicRateList.get(Integer.valueOf(dynamic.getIdentify()))).doubleValue();
      boolean flag = SkillAi.canAffectOnPro(rate);
      if (flag) {
        Integer num = map.get(Integer.valueOf(dynamic.getSerial()));
        if (num == null)
          num = Integer.valueOf(0); 
        int numCalculate = ((Integer)TaskDataPool.taskRewardDynamicNumList.get(Integer.valueOf(dynamic.getIdentify()))).intValue();
        num = Integer.valueOf(num.intValue() + numCalculate);
        if (num.intValue() > 0) {
          map.put(Integer.valueOf(dynamic.getSerial()), num);
          list.add(new Item(Integer.valueOf(dynamic.getSerial()), num, Integer.valueOf(dynamic.getNotice())));
        } 
      } 
    } 
    return list;
  }
  
  public void rewardItem(Role role, int serial, int num, int bd, String cause) {
    try {
      Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(serial));
      if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())) || type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()))) {
        creationEquip(role, serial, "任务奖励");
      } else if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_PET.getValue()))) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(Integer.valueOf(serial));
        if (sysPet != null)
          addGoods(role, serial, num, bd, true, false, "任务奖励物品1：" + cause); 
      } else {
        ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(serial));
        if (itemEasyInfo != null) {
          if (itemEasyInfo.getType().equals(Integer.valueOf(1))) {
            if (itemEasyInfo.getValue().intValue() > 1000) {
              creationEquip(role, itemEasyInfo.getValue().intValue(), cause);
            } else if (itemEasyInfo.getValue().intValue() == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
              List<SysSinBieEq> bieEqs = (List<SysSinBieEq>)EquipDataPool.sinBieEq.values().stream().collect(Collectors.toList());
              List<SysSinBieEq> collect = (List<SysSinBieEq>)bieEqs.stream().filter(eq -> (eq.getPos() == itemEasyInfo.getCostType().intValue() && (eq.getOwnerRole().contains(Integer.valueOf(0)) || eq.getOwnerRole().contains(role.getSerial()) || eq.getOwnerRole().contains(role.getRace())) && (eq.getSex() == 0 || role.getSex().intValue() == eq.getSex()) && (eq.getRace() == 0 || role.getRace().intValue() == eq.getRace()) && DataGlobalFunc.getPj(eq.getSerial()) == itemEasyInfo.getItemPJ().intValue())).collect(Collectors.toList());
              int index = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1));
              creationEquip(role, ((SysSinBieEq)collect.get(index)).getSerial(), cause);
            } 
          } else {
            addGoods(role, serial, num, bd, true, false, "任务奖励物品2：" + cause);
          } 
        } else if (serial == 9997) {
          addGoods(role, serial, num, bd, true, false, "任务奖励天策府：" + cause);
        } 
      } 
    } catch (Exception e) {
      e.printStackTrace();
      LoggerUtils.error("任务奖励物品 = {}", e);
    } 
  }
  
  public void creationEquip(Role role, int serial, String cause) {
    EquipAlike equipData = DataGlobalFunc.getEquipData(Integer.valueOf(serial));
    if (equipData != null)
      ((EquipService)SpringUtils.getBean(EquipService.class)).upgradeEquip(role, 0, 11, String.valueOf(equipData.getSerial()), 0, false, cause); 
  }
}
