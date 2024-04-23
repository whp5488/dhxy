package com.kitty.system.game.tianfu.service;

import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.reflect.ReflectUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.ChangeRuneWearEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.enums.war.wardef.DAY_IN_CE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.tianfu.message.RespDayInCe;
import com.kitty.system.game.tianfu.message.RespReturnAchievement;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.vo.utils.Const;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleDayInCeService {
  @Autowired
  private Dao dao;
  
  public void getDayInCeLevel(Role role) {
    RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
    if (roleDayInCe != null) {
      short level = roleDayInCe.getLevel();
      RespDayInCe respDayInCe = new RespDayInCe(level);
      MessagePusher.pushMessage(role, (Message)respDayInCe);
    } 
  }
  
  public void getAchievement(Role role) {
    Long achievementScore = role.getExtendBox().getAchievementScore();
    FiedValue fiedValue = new FiedValue(String.valueOf(Const.RES_TYPE_ACHIEVEMENT), (byte)4, String.valueOf(achievementScore));
    List<FiedValue> list = new ArrayList<>();
    list.add(fiedValue);
    MessagePusher.pushMessage(role, (Message)new RespReturnAchievement(list));
  }
  
  public void addDayInCeLevel(Role role, Integer addLevel) {
    RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
    Map<Short, SysDayInCePractice> map = SkillDataPool.dayInCePracticeMap;
    SysDayInCePractice sysDayInCePractice = map.get(Short.valueOf((short)(roleDayInCe.getLevel() + addLevel.intValue())));
    if (sysDayInCePractice == null)
      return; 
    int need = role.getRelive().intValue() * 1000;
    if (need < sysDayInCePractice.getNeed()) {
      int relive = sysDayInCePractice.getNeed() / 1000;
      String msg = "你的#<R>角色转生#不足";
      if (relive > 0)
        msg = msg + "#<R>" + relive + "转#"; 
      MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
      return;
    } 
    int needAchievementScore = 0;
    int needCoin = 0;
    for (int i = roleDayInCe.getLevel() + 1; i <= roleDayInCe.getLevel() + addLevel.intValue(); i++) {
      sysDayInCePractice = map.get(Short.valueOf((short)i));
      if (sysDayInCePractice != null) {
        needAchievementScore += sysDayInCePractice.getAchievementScore();
        needCoin += sysDayInCePractice.getCoin();
      } 
    } 
    RoleService roleService = SpringUtils.getRoleService();
    if (!roleService.checkCurrency(role, Const.RESTYPE_COIN, needCoin))
      return; 
    if (!roleService.checkCurrency(role, Const.RES_TYPE_ACHIEVEMENT, needAchievementScore))
      return; 
    roleService.deductCurrency(role, Const.RESTYPE_COIN, needCoin, "天策符扣贡币");
    roleService.deductCurrency(role, Const.RES_TYPE_ACHIEVEMENT, needAchievementScore, "成就积分");
    roleDayInCe.setLevel((short)(roleDayInCe.getLevel() + addLevel.intValue()));
    for (Map.Entry<Byte, String> entry : (Iterable<Map.Entry<Byte, String>>)TianFuUtils.client2serverGridMap.entrySet()) {
      if (((Byte)entry.getKey()).byteValue() <= roleDayInCe.getLevel()) {
        String value = entry.getValue();
        if (((Long)ReflectUtils.invokeGetter(roleDayInCe, value)).longValue() == 0L)
          ReflectUtils.invokeSetter(roleDayInCe, value, Integer.valueOf(1)); 
      } 
    } 
    roleDayInCe.save();
    getAchievement(role);
    getDayInCeLevel(role);
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
  }
  
  public void tianFuWear(Role role, byte type, long tianFuId, int grid) {
    if (type == 1) {
      Optional<RoleTianFu> optional = ((List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> (obj.getId().longValue() == tianFuId)).findFirst();
      if (!optional.isPresent())
        return; 
      RoleTianFu roleTianFu = optional.get();
      RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
      String dayInCeGrid = (String)TianFuUtils.client2serverGridMap.get(Byte.valueOf((byte)grid));
      if (dayInCeGrid == null)
        return; 
      byte[] categoryLayer = getCategoryLayer(dayInCeGrid);
      byte categoryType = categoryLayer[0];
      byte layer = categoryLayer[1];
      if (roleTianFu.getCategory() != categoryType) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("类型不匹配"));
        return;
      } 
      if (grid > 3 && layer != -1 && layer != roleTianFu.getLayer()) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("此天策符不属于此层级"));
        return;
      } 
      long gridValue = ((Long)ReflectUtils.invokeGetter(roleDayInCe, dayInCeGrid)).longValue();
      if (gridValue == 0L)
        return; 
      List<RoleTianFuClient> list = new ArrayList<>();
      for (RoleTianFu rtf : DataCache.ROLE_TIAN_FU.get(role.getId())) {
        if (rtf.getId().equals(roleTianFu.getId()))
          continue; 
        if (rtf.getIsWear() == 1 && (rtf.getTianFuSerial() == roleTianFu.getTianFuSerial() || rtf.getId().longValue() == gridValue)) {
          rtf.setIsWear(0);
          rtf.save();
          ReflectUtils.invokeSetter(roleDayInCe, rtf.getDayInCeGrid(), Integer.valueOf(1));
          rtf.setDayInCeGrid(null);
          RoleTianFuClient roleTianFuClient1 = new RoleTianFuClient(rtf);
          list.add(roleTianFuClient1);
          TianFuUtils.excuteWearInstantlyTakeEffect(role, rtf);
        } 
      } 
      roleTianFu.setDayInCeGrid(dayInCeGrid);
      roleTianFu.setIsWear(1);
      ReflectUtils.invokeSetter(roleDayInCe, dayInCeGrid, roleTianFu.getId());
      roleTianFu.save();
      roleDayInCe.save();
      RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
      list.add(roleTianFuClient);
      TianFuUtils.excuteWearInstantlyTakeEffect(role, roleTianFu);
      MessagePusher.pushMessage(role, (Message)new RespTianFuList((byte)3, list));
      if (TianFuUtils.countFuPlayerBookGain(roleTianFu, true))
        EventDispatcher.getInstance().fireEvent((BaseGameEvent)new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role)); 
    } else if (type == 0) {
      List<RoleTianFu> noWears = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> (obj.getIsWear() == 0 && obj.getStorePos() == 0)).collect(Collectors.toList());
      if (noWears.size() >= 75) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("天策符背包已无空闲格子"));
        return;
      } 
      RoleTianFu oldRoleTianFu = ((List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> (obj.getId().longValue() == tianFuId)).findFirst().orElse(null);
      if (oldRoleTianFu == null)
        return; 
      RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
      String dayInCeGrid = (String)TianFuUtils.client2serverGridMap.get(Byte.valueOf((byte)grid));
      if (dayInCeGrid == null)
        return; 
      oldRoleTianFu.setDayInCeGrid(null);
      oldRoleTianFu.setIsWear(0);
      ReflectUtils.invokeSetter(roleDayInCe, dayInCeGrid, Integer.valueOf(1));
      oldRoleTianFu.save();
      roleDayInCe.save();
      TianFuUtils.excuteWearInstantlyTakeEffect(role, oldRoleTianFu);
      RoleTianFuClient roleTianFuClient = new RoleTianFuClient(oldRoleTianFu);
      MessagePusher.pushMessage(role, (Message)new RespTianFuList((byte)3, Arrays.asList(new RoleTianFuClient[] { roleTianFuClient })));
      if (TianFuUtils.countFuPlayerBookGain(oldRoleTianFu, false))
        EventDispatcher.getInstance().fireEvent((BaseGameEvent)new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role)); 
    } 
  }
  
  public void chageRace(Role role) {
    List<RoleTianFu> inappropriate = getInappropriate(role.getId().longValue(), role.getRace().intValue());
    if (inappropriate != null)
      for (RoleTianFu roleTianFu : inappropriate)
        tianFuWear(role, (byte)0, roleTianFu.getId().longValue(), ((Byte)TianFuUtils.server2clientGridMap.get(roleTianFu.getDayInCeGrid())).byteValue());  
  }
  
  public RoleDayInCe getDataForRoleId(Long roleId) {
    RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(roleId);
    if (roleDayInCe == null) {
      List<RoleDayInCe> roleDayInCes = this.dao.query(RoleDayInCe.class, (Condition)Cnd.where("role_id", "=", roleId));
      if (roleDayInCes.size() <= 0) {
        roleDayInCe = new RoleDayInCe();
        roleDayInCe.setId(Long.valueOf(IdGenerator.getNextId()));
        roleDayInCe.setRoleId(roleId.longValue());
        roleDayInCe.setLevel((short)0);
        roleDayInCe.save();
      } else {
        roleDayInCe = roleDayInCes.get(0);
      } 
      DataCache.ROLE_DAY_IN_CE.put(roleId, roleDayInCe);
    } 
    return roleDayInCe;
  }
  
  private byte[] getCategoryLayer(String dayInCeGrid) {
    String last = dayInCeGrid.substring(dayInCeGrid.length() - 2, dayInCeGrid.length() - 1);
    byte categoryType = -1;
    byte layer = -1;
    if (!last.matches("^[0-9]+$")) {
      switch (dayInCeGrid) {
        case "gattaca":
          categoryType = DAY_IN_CE.GATTACA.getValue();
          break;
        case "loadObj":
          categoryType = DAY_IN_CE.LOAD_OBJ.getValue();
          break;
        case "driveBeasts":
          categoryType = DAY_IN_CE.DRIVER_BEASTS.getValue();
          break;
      } 
    } else {
      String category = dayInCeGrid.substring(0, dayInCeGrid.length() - 2);
      switch (category) {
        case "gattaca":
          categoryType = DAY_IN_CE.GATTACA.getValue();
          break;
        case "loadObj":
          categoryType = DAY_IN_CE.LOAD_OBJ.getValue();
          break;
        case "driveBeasts":
          categoryType = DAY_IN_CE.DRIVER_BEASTS.getValue();
          break;
      } 
      layer = Byte.parseByte(last);
    } 
    return new byte[] { categoryType, layer };
  }
  
  public int checkDemount(Role role, int race) {
    Integer raceOld = role.getRace();
    if (race == raceOld.intValue())
      return 0; 
    List<RoleTianFu> inappropriate = getInappropriate(role.getId().longValue(), race);
    if (inappropriate != null) {
      int inappropriateCount = inappropriate.size();
      List<RoleTianFu> noWears = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> (obj.getIsWear() == 0 && obj.getStorePos() == 0)).collect(Collectors.toList());
      int num = 75 - noWears.size() - inappropriateCount;
      if (num < 0)
        return Math.abs(num); 
    } 
    return 0;
  }
  
  public List<RoleTianFu> getInappropriate(long roleId, int race) {
    List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(roleId);
    if (roleTianFus == null)
      return null; 
    Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
    List<RoleTianFu> returnList = new ArrayList<>();
    for (RoleTianFu roleTianFu : roleTianFus) {
      if (roleTianFu.getIsWear() != 1)
        continue; 
      SysTianFuSkill sysTianFuSkill = tianFuSkillMap.get(roleTianFu.getTianFuSerial());
      if (sysTianFuSkill != null && !sysTianFuSkill.getRace().contains((byte) race))
        returnList.add(roleTianFu); 
    } 
    if (returnList.size() > 0)
      return returnList; 
    return null;
  }
}
