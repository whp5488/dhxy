package com.kitty.system.game.fightnew.bean;

import com.kitty.common.utils.PositionUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.pet.domain.vo.AssistParam;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.init.domain.SysDataCard;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PlayerFightObject extends FightObject {
  private static final Logger log = LoggerFactory.getLogger(PlayerFightObject.class);
  
  private static final NutMap publicAutoSet = NutMap.NEW();
  
  private Role role;
  
  private int index;
  
  private int customIndex;
  
  private List<AssistParam> assistParams;
  
  private DayInCeFight dayInCeFight;
  
  protected Map<Integer, Integer> abnormalRoundMap;
  
  private List<Integer> acsrySkillList;
  
  private NutMap acsryPropertyMap;
  
  private boolean isAnimationFinish;
  
  public Role getRole() {
    return this.role;
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public int getCustomIndex() {
    return this.customIndex;
  }
  
  public List<AssistParam> getAssistParams() {
    return this.assistParams;
  }
  
  public DayInCeFight getDayInCeFight() {
    return this.dayInCeFight;
  }
  
  public Map<Integer, Integer> getAbnormalRoundMap() {
    return this.abnormalRoundMap;
  }
  
  public void setAnimationFinish(boolean isAnimationFinish) {
    this.isAnimationFinish = isAnimationFinish;
  }
  
  public boolean isAnimationFinish() {
    return this.isAnimationFinish;
  }
  
  static {
    publicAutoSet.setv("type", 0);
    publicAutoSet.setv("action", 1);
    publicAutoSet.setv("skillId", 0);
    publicAutoSet.setv("born", 0);
  }
  
  public PlayerFightObject(Role role, int index, int customIndex, boolean isA) {
    this.assistParams = new ArrayList<>();
    this.dayInCeFight = new DayInCeFight();
    this.abnormalRoundMap = new HashMap<>();
    this.acsrySkillList = new ArrayList<>();
    this.acsryPropertyMap = NutMap.NEW();
    this.isAnimationFinish = false;
    this.index = index;
    this.customIndex = customIndex;
    this.role = role;
    Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
    if (autoSet == null || autoSet.isEmpty()) {
      autoSet = publicAutoSet;
    }

    setId(role.getRoleId());
    setHostId(role.getRoleId());
    setSerial(role.getSerial());
    setWeaponId(role.getWeaponIcon());
    setName(role.getName());
    setRelive(role.getRelive());
    setLevel(role.getLevel());
    setIcon(role.getIcon());
    setRace(role.getRace());
    setColor(role.getColor());
    setASide(isA);
    setLogic(LOGIC.LOGICTYPE_HERO.getValue());
    setPos(PositionUtils.positions.get(index));
    setMaxLife(role.getMaxLife());
    setMaxMana(role.getMaxMana());
    setCurrMana(role.getCurrMana());
    setCurrLife(role.getCurrLife());
    setPhyPower(CountUtils.countPhyPower(role));
    setSpeed(CountUtils.countSpeed(role));
    setFightMsg(role.getExtendBox().getMsgMap());
    setFightPetId(role.getExtendBox().getCombatPet());
    this.yepFightPet.add(role.getExtendBox().getCombatPet());
    this.role = role;
    setAutoFight(role.getExtendBox().getAutoFight());
    List<RoleSkill> collect = role.getSkills().values().stream().collect(Collectors.toList());
    setSkillIds(collect);

    int type = Integer.valueOf(String.valueOf(autoSet.get("type")));
    int skillId = Integer.valueOf(String.valueOf(autoSet.get("skillId")));
    int action = Integer.valueOf(String.valueOf(autoSet.get("action")));

    setType(type);
    setSkillId(skillId);
    setAutoFightType(type);
    setAutoSkillId(skillId);
    setAction(action);
    if (autoSet.get("target") != null && !"".equals(autoSet.get("target"))) {
      String target = String.valueOf(autoSet.get("target"));
      setTargetId(Integer.valueOf(target));
      setAutoTarget(Integer.valueOf(target));
    } 
    if (role.getRace() == RACE.RACE_LONG.getValue()) {
      getSkillCoolMap().setv(String.valueOf(30041), 4);
      Object born = autoSet.get("born");
      if (born != null) {
        setBorn((Integer) autoSet.get("born"));
      } else {
        setBorn(1);
      } 
    } 
    setPropertyMap(CountUtils.countPro(role));
    List<SuitParam> suits = role.getExtendBox().getSuits();
    if (suits.size() > 0) {
      List<Integer> petIds = suits.get(0).getPetIds();
      if (petIds != null && petIds.size() > 0)
        this.assistParams = petIds.stream().map(petId -> {
              AssistParam assist = new AssistParam();
              assist.setPetId(petId);
              return assist;
        }).collect(Collectors.toList());
    } 
    List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
    if (wearEquips.size() > 0) {
      int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
      setGem(num);
    } 
    int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
      SysDataCard dataCard = CardDataPool.cardMap.get(role.getMasquerade());
      if (dataCard != null && dataCard.getMorph() > 0)
        setIcon(dataCard.getMorph()); 
    } 
    this.dayInCeFight = getDayInCeFightValues();
    this.acsrySkillList = AcsrySkillUtil.getAcsrySkillList(role);
  }
  
  public RoleSkill getSkillBy(Integer skillId) {
    return getRole().getSkills().get(skillId);
  }
  
  public IoSession getSession() {
    return SessionUtils.getSessionBy(getRole().getId());
  }
  
  private DayInCeFight getDayInCeFightValues() {
    RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(this.role.getId());
    Map<Long, RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(this.role.getId()).stream().collect(Collectors.toMap(RoleTianFu::getId, roleTianFu -> roleTianFu));
    List<Long> tianFuSerials = TianFuUtils.getAllGridValues(roleDayInCe);
    List<TianFuObject> tianFuList = new ArrayList<>();
    for (Long id : tianFuSerials) {
      RoleTianFu roleTianFu = roleTianFus.get(id);
      if (roleTianFu == null || roleTianFu.getIsWear() != 1)
        continue; 
      TianFuObject tianFuObject = new TianFuObject();
      tianFuObject.setRoleTianFuId(roleTianFu.getId());
      tianFuObject.setCeSerial(roleTianFu.getTianFuSerial());
      tianFuObject.setLevel(roleTianFu.getLevel());
      tianFuObject.setProductOrder(roleTianFu.getProductOrder());
      if (roleTianFu.getTriggerType() == null) {
        SysTianFuSkill sysTianFuSkill = SkillDataPool.tianFuSkillMap.get(roleTianFu.getTianFuSerial());
        if (sysTianFuSkill != null)
          tianFuObject.setTriggerType(sysTianFuSkill.getOpportunity()); 
      } else {
        tianFuObject.setTriggerType(roleTianFu.getTriggerType());
      } 
      tianFuList.add(tianFuObject);
    } 
    DayInCeFight dayInCeFight = new DayInCeFight();
    dayInCeFight.setLevel(roleDayInCe.getLevel());
    dayInCeFight.setTianFuList(tianFuList);
    return dayInCeFight;
  }
  
  public void instalAbnormalRoundMap(Fight fight) {
    if (getStatusValue(1002) > 0) {
      getAbnormalRoundMap().put(fight.getRound(), 1002);
    } else if (getStatusValue(1003) > 0) {
      getAbnormalRoundMap().put(fight.getRound(), 1003);
    } else if (getStatusValue(1004) > 0) {
      getAbnormalRoundMap().put(fight.getRound(), 1004);
    } else if (getStatusValue(1006) > 0) {
      getAbnormalRoundMap().put(fight.getRound(), 1006);
    } 
  }
  
  public List<Integer> getAcsrySkillList() {
    return this.acsrySkillList;
  }
  
  public void setAcsrySkillList(List<Integer> acsrySkillList) {
    this.acsrySkillList = acsrySkillList;
  }
  
  public NutMap getAcsryPropertyMap() {
    return this.acsryPropertyMap;
  }
  
  public void setAcsryPropertyMap(NutMap acsryPropertyMap) {
    this.acsryPropertyMap = acsryPropertyMap;
  }
  
  public double getSelfAllTempPro(int dist) {
    double num = super.getSelfAllTempPro(dist);
    String distStr = String.valueOf(dist);
    int round = getAcsryPropertyMap().getInt(distStr, 0);
    if (round != 0) {
      double value = getAcsryPropertyMap().getDouble(distStr + "value", 0.0D);
      num += value;
      if (round == -1);
    } 
    return num;
  }
}
