package com.kitty.system.game.fightnew;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.warskill.skilldef.EFFECTTYPE;
import com.kitty.system.game.equip.domain.Effect;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.imbue.bean.Imbue;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.util.NutMap;

public class FightObject implements Comparable<FightObject> {
  protected Integer id;
  
  protected int serial;
  
  protected String name;
  
  protected int level;
  
  protected int relive;
  
  protected int pos;
  
  protected int race;
  
  protected int icon;
  
  protected String color;
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setLevel(int level) {
    this.level = level;
  }
  
  public void setRelive(int relive) {
    this.relive = relive;
  }
  
  public void setPos(int pos) {
    this.pos = pos;
  }
  
  public void setRace(int race) {
    this.race = race;
  }
  
  public void setIcon(int icon) {
    this.icon = icon;
  }
  
  public void setColor(String color) {
    this.color = color;
  }
  
  public void setColorMap(NutMap colorMap) {
    this.colorMap = colorMap;
  }
  
  public void setMaxLife(int maxLife) {
    this.maxLife = maxLife;
  }
  
  public void setWeaponId(int weaponId) {
    this.weaponId = weaponId;
  }
  
  public void setGem(int gem) {
    this.gem = gem;
  }
  
  public void setMaxMana(int maxMana) {
    this.maxMana = maxMana;
  }
  
  public void setSpeed(int speed) {
    this.speed = speed;
  }
  
  public void setPhyPower(int phyPower) {
    this.phyPower = phyPower;
  }
  
  public void setDoAction(boolean doAction) {
    this.doAction = doAction;
  }
  
  public void setHostId(int hostId) {
    this.hostId = hostId;
  }
  
  public void setFightPetId(int fightPetId) {
    this.fightPetId = fightPetId;
  }
  
  public void setLogic(int logic) {
    this.logic = logic;
  }
  
  public void setSkillIds(List<RoleSkill> skillIds) {
    this.skillIds = skillIds;
  }
  
  public void setTargetId(int targetId) {
    this.targetId = targetId;
  }
  
  public void setSkillId(int skillId) {
    this.skillId = skillId;
  }
  
  public void setAutoFight(int AutoFight) {
    this.AutoFight = AutoFight;
  }
  
  public void setBorn(int born) {
    this.born = born;
  }
  
  public void setMorph(int morph) {
    this.morph = morph;
  }
  
  public void setSkillCoolMap(NutMap skillCoolMap) {
    this.skillCoolMap = skillCoolMap;
  }
  
  public void setStatusMap(NutMap statusMap) {
    this.statusMap = statusMap;
  }
  
  public void setPropertyMap(NutMap propertyMap) {
    this.propertyMap = propertyMap;
  }
  
  public void setStatusSubMap(NutMap statusSubMap) {
    this.statusSubMap = statusSubMap;
  }
  
  public void setImbueMap(Map<Integer, Imbue> imbueMap) {
    this.imbueMap = imbueMap;
  }
  
  public void setClose(long close) {
    this.close = close;
  }
  
  public void setAction(int action) {
    this.action = action;
  }
  
  public void setASide(boolean isASide) {
    this.isASide = isASide;
  }
  
  public void setAutoFightType(int autoFightType) {
    this.autoFightType = autoFightType;
  }
  
  public void setAutoSkillId(int autoSkillId) {
    this.autoSkillId = autoSkillId;
  }
  
  public void setAutoTarget(int autoTarget) {
    this.autoTarget = autoTarget;
  }
  
  public void setComeRound(int comeRound) {
    this.comeRound = comeRound;
  }
  
  public void setDieRound(int dieRound) {
    this.dieRound = dieRound;
  }
  
  public void setReviveSize(int reviveSize) {
    this.reviveSize = reviveSize;
  }
  
  public void setFightMsg(List<NutMap> fightMsg) {
    this.fightMsg = fightMsg;
  }
  
  public void setYepFightPet(List<Integer> yepFightPet) {
    this.yepFightPet = yepFightPet;
  }
  
  public void setFightActionRecord(NutMap fightActionRecord) {
    this.fightActionRecord = fightActionRecord;
  }
  
  public void setBeAttackedMap(NutMap beAttackedMap) {
    this.beAttackedMap = beAttackedMap;
  }
  
  public void setOtherMap(NutMap otherMap) {
    this.otherMap = otherMap;
  }
  
  public void setIsCompleted(Map<Integer, Boolean> isCompleted) {
    this.isCompleted = isCompleted;
  }
  
  public void setTempCache(Map<String, Object> tempCache) {
    this.tempCache = tempCache;
  }
  
  public void setRoundController(NutMap roundController) {
    this.roundController = roundController;
  }
  
  public void setRoundUseSkillMap(Map<Integer, List<Integer>> roundUseSkillMap) {
    this.roundUseSkillMap = roundUseSkillMap;
  }
  
  @ConstructorProperties({"id", "serial", "name", "level", "relive", "pos", "race", "icon", "color", "colorMap", "currLife", "maxLife", "weaponId", "gem", "currMana", "maxMana", "speed", "phyPower", "doAction", "hostId", "fightPetId", "logic", "skillIds", "targetId", "type", "skillId", "AutoFight", "born", "morph", "skillCoolMap", "statusMap", "propertyMap", "statusSubMap", "imbueMap", "close", "action", "isASide", "autoFightType", "autoSkillId", "autoTarget", "comeRound", "dieRound", "reviveSize", "fightMsg", "yepFightPet", "fightActionRecord", "beAttackedMap", "otherMap", "isCompleted", "tempCache", "roundController", "roundUseSkillMap"})
  public FightObject(Integer id, int serial, String name, int level, int relive, int pos, int race, int icon, String color, NutMap colorMap, int currLife, int maxLife, int weaponId, int gem, int currMana, int maxMana, int speed, int phyPower, boolean doAction, int hostId, int fightPetId, int logic, List<RoleSkill> skillIds, int targetId, int type, int skillId, int AutoFight, int born, int morph, NutMap skillCoolMap, NutMap statusMap, NutMap propertyMap, NutMap statusSubMap, Map<Integer, Imbue> imbueMap, long close, int action, boolean isASide, int autoFightType, int autoSkillId, int autoTarget, int comeRound, int dieRound, int reviveSize, List<NutMap> fightMsg, List<Integer> yepFightPet, NutMap fightActionRecord, NutMap beAttackedMap, NutMap otherMap, Map<Integer, Boolean> isCompleted, Map<String, Object> tempCache, NutMap roundController, Map<Integer, List<Integer>> roundUseSkillMap) {
    this.id = id;
    this.serial = serial;
    this.name = name;
    this.level = level;
    this.relive = relive;
    this.pos = pos;
    this.race = race;
    this.icon = icon;
    this.color = color;
    this.colorMap = colorMap;
    this.currLife = currLife;
    this.maxLife = maxLife;
    this.weaponId = weaponId;
    this.gem = gem;
    this.currMana = currMana;
    this.maxMana = maxMana;
    this.speed = speed;
    this.phyPower = phyPower;
    this.doAction = doAction;
    this.hostId = hostId;
    this.fightPetId = fightPetId;
    this.logic = logic;
    this.skillIds = skillIds;
    this.targetId = targetId;
    this.type = type;
    this.skillId = skillId;
    this.AutoFight = AutoFight;
    this.born = born;
    this.morph = morph;
    this.skillCoolMap = skillCoolMap;
    this.statusMap = statusMap;
    this.propertyMap = propertyMap;
    this.statusSubMap = statusSubMap;
    this.imbueMap = imbueMap;
    this.close = close;
    this.action = action;
    this.isASide = isASide;
    this.autoFightType = autoFightType;
    this.autoSkillId = autoSkillId;
    this.autoTarget = autoTarget;
    this.comeRound = comeRound;
    this.dieRound = dieRound;
    this.reviveSize = reviveSize;
    this.fightMsg = fightMsg;
    this.yepFightPet = yepFightPet;
    this.fightActionRecord = fightActionRecord;
    this.beAttackedMap = beAttackedMap;
    this.otherMap = otherMap;
    this.isCompleted = isCompleted;
    this.tempCache = tempCache;
    this.roundController = roundController;
    this.roundUseSkillMap = roundUseSkillMap;
  }
  
  public Integer getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  public int getLevel() {
    return this.level;
  }
  
  public int getRelive() {
    return this.relive;
  }
  
  public int getPos() {
    return this.pos;
  }
  
  public int getRace() {
    return this.race;
  }
  
  public int getIcon() {
    return this.icon;
  }
  
  public String getColor() {
    return this.color;
  }
  
  protected NutMap colorMap = new NutMap();
  
  protected int currLife;
  
  protected int maxLife;
  
  public NutMap getColorMap() {
    return this.colorMap;
  }
  
  public int getCurrLife() {
    return this.currLife;
  }
  
  public int getMaxLife() {
    return this.maxLife;
  }
  
  protected int weaponId = 0;
  
  public int getWeaponId() {
    return this.weaponId;
  }
  
  protected int gem = 0;
  
  protected int currMana;
  
  protected int maxMana;
  
  protected int speed;
  
  protected int phyPower;
  
  public int getGem() {
    return this.gem;
  }
  
  public int getCurrMana() {
    return this.currMana;
  }
  
  public int getMaxMana() {
    return this.maxMana;
  }
  
  public int getSpeed() {
    return this.speed;
  }
  
  public int getPhyPower() {
    return this.phyPower;
  }
  
  protected boolean doAction = false;
  
  protected int hostId;
  
  protected int fightPetId;
  
  protected int logic;
  
  protected List<RoleSkill> skillIds;
  
  public boolean isDoAction() {
    return this.doAction;
  }
  
  public int getHostId() {
    return this.hostId;
  }
  
  public int getFightPetId() {
    return this.fightPetId;
  }
  
  public int getLogic() {
    return this.logic;
  }
  
  public List<RoleSkill> getSkillIds() {
    return this.skillIds;
  }
  
  protected int targetId = 0;
  
  protected int type;
  
  protected int skillId;
  
  public int getTargetId() {
    return this.targetId;
  }
  
  public int getType() {
    return this.type;
  }
  
  public int getSkillId() {
    return this.skillId;
  }
  
  protected int AutoFight = 0;
  
  public int getAutoFight() {
    return this.AutoFight;
  }
  
  protected int born = 0;
  
  public int getBorn() {
    return this.born;
  }
  
  protected int morph = 0;
  
  public int getMorph() {
    return this.morph;
  }
  
  protected NutMap skillCoolMap = new NutMap();
  
  public NutMap getSkillCoolMap() {
    return this.skillCoolMap;
  }
  
  protected NutMap statusMap = new NutMap();
  
  protected NutMap propertyMap = new NutMap();
  
  public NutMap getPropertyMap() {
    return this.propertyMap;
  }
  
  protected NutMap statusSubMap = new NutMap();
  
  public NutMap getStatusSubMap() {
    return this.statusSubMap;
  }
  
  protected Map<Integer, Imbue> imbueMap = new HashMap<>();
  
  public Map<Integer, Imbue> getImbueMap() {
    return this.imbueMap;
  }
  
  protected long close = 0L;
  
  protected int action;
  
  public long getClose() {
    return this.close;
  }
  
  public int getAction() {
    return this.action;
  }
  
  protected boolean isASide = false;
  
  protected int autoFightType;
  
  protected int autoSkillId;
  
  protected int autoTarget;
  
  public boolean isASide() {
    return this.isASide;
  }
  
  public int getAutoFightType() {
    return this.autoFightType;
  }
  
  public int getAutoSkillId() {
    return this.autoSkillId;
  }
  
  public int getAutoTarget() {
    return this.autoTarget;
  }
  
  protected int comeRound = 1;
  
  public int getComeRound() {
    return this.comeRound;
  }
  
  protected int dieRound = 0;
  
  public int getDieRound() {
    return this.dieRound;
  }
  
  protected int reviveSize = 0;
  
  protected List<NutMap> fightMsg;
  
  public int getReviveSize() {
    return this.reviveSize;
  }
  
  public List<NutMap> getFightMsg() {
    return this.fightMsg;
  }
  
  protected List<Integer> yepFightPet = new ArrayList<>();
  
  public List<Integer> getYepFightPet() {
    return this.yepFightPet;
  }
  
  private NutMap fightActionRecord = NutMap.NEW();
  
  public NutMap getFightActionRecord() {
    return this.fightActionRecord;
  }
  
  private NutMap beAttackedMap = NutMap.NEW();
  
  public NutMap getBeAttackedMap() {
    return this.beAttackedMap;
  }
  
  private NutMap otherMap = NutMap.NEW();
  
  public NutMap getOtherMap() {
    return this.otherMap;
  }
  
  private Map<Integer, Boolean> isCompleted = new HashMap<>();
  
  protected Map<String, Object> tempCache;
  
  public Map<Integer, Boolean> getIsCompleted() {
    return this.isCompleted;
  }
  
  public Map<String, Object> getTempCache() {
    return this.tempCache;
  }
  
  private NutMap roundController = NutMap.NEW();
  
  public NutMap getRoundController() {
    return this.roundController;
  }
  
  private Map<Integer, List<Integer>> roundUseSkillMap = new HashMap<>();
  
  public Map<Integer, List<Integer>> getRoundUseSkillMap() {
    return this.roundUseSkillMap;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public Object getTempCacheValue(String key) {
    if (this.tempCache != null)
      return this.tempCache.get(key); 
    return null;
  }
  
  public RoleSkill getSkillBy(Integer skillId) {
    Optional<RoleSkill> optional = getSkillIds().stream().filter(skill -> skill.getSkillId().equals(skillId)).findFirst();
    if (optional.isPresent())
      return optional.get(); 
    return null;
  }
  
  public void setCurrLife(int currLife) {
    if (currLife > this.maxLife) {
      this.currLife = this.maxLife;
    } else {
      this.currLife = Math.max(currLife, 0);
    } 
  }
  
  public int setCurrMana(int currMana) {
    if (currMana > this.maxMana) {
      this.currMana = this.maxMana;
    } else {
      this.currMana = Math.max(currMana, 0);
    } 
    return this.currMana;
  }
  
  public int reduceMp(int mp) {
    this.currMana -= mp;
    if (this.currMana < 0)
      this.currMana = 0; 
    return this.currMana;
  }
  
  public int reduceHp(int hp) {
    this.currLife -= hp;
    if (this.currLife < 0)
      this.currLife = 0; 
    return this.currLife;
  }
  
  public boolean isFallDown() {
    return (this.currLife <= 0);
  }
  
  public boolean isCompleted(int round, boolean bo) {
    boolean boo = ((Boolean)this.isCompleted.getOrDefault(Integer.valueOf(round), Boolean.valueOf(false))).booleanValue();
    if (!boo && bo)
      this.isCompleted.put(Integer.valueOf(round), Boolean.valueOf(true)); 
    return boo;
  }
  
  public NutMap getStatusMap() {
    if (this.statusMap == null)
      setStatusMap(NutMap.NEW()); 
    return this.statusMap;
  }
  
  public void setStatus(int status, int value) {
    getStatusMap().setv(String.valueOf(status), Integer.valueOf(value));
  }
  
  public void removeStatus(int status) {
    getStatusMap().unset(String.valueOf(status));
    getStatusMap().unset(status + "value");
    getStatusMap().unset(status + "record");
    getStatusMap().unset(status + "odds");
    getStatusMap().unset(status + "perpetrator");
    getStatusMap().unset(status + "statusConstraint");
    removeStatusSub(status + "target");
  }
  
  public void removeSkillCool(String dist) {
    getSkillCoolMap().unset(dist);
    getSkillCoolMap().unset(dist + "value");
    getSkillCoolMap().unset(dist + "value_targets");
  }
  
  public void removeSkillCoolOther(String dist) {
    getSkillCoolMap().unset(dist + "value");
    getSkillCoolMap().unset(dist + "value_targets");
  }
  
  public List<SkillCool> getSkillCoolList() {
    List<SkillCool> list = new ArrayList<>();
    for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)getSkillCoolMap().entrySet()) {
      if (!((String)entry.getKey()).matches("^[0-9]+$"))
        continue; 
      SkillCool cool = new SkillCool();
      cool.setObjId(getId().intValue());
      cool.setSkillId(Integer.valueOf(entry.getKey()).intValue());
      cool.setRound(((Integer)entry.getValue()).intValue());
      list.add(cool);
    } 
    return list;
  }
  
  public void reduceSkillCool() {
    List<String> keys = new ArrayList<>();
    List<String> removeSkillCoolOther = new ArrayList<>();
    for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)getSkillCoolMap().entrySet()) {
      if (!(entry.getValue() instanceof Integer))
        continue; 
      int value = ((Integer)entry.getValue()).intValue();
      if (value <= 0)
        keys.add(entry.getKey()); 
      value--;
      this.skillCoolMap.setv(entry.getKey(), Integer.valueOf(value));
      if (value == 0)
        removeSkillCoolOther.add(entry.getKey()); 
    } 
    for (String key : keys)
      removeSkillCool(key); 
    for (String keyOther : removeSkillCoolOther)
      removeSkillCoolOther(keyOther); 
  }
  
  public void reduceImbue() {
    ImbueUtil.reduceImbueRounds(this.imbueMap);
  }
  
  public void removeStatusSub(String dict) {
    this.statusSubMap.unset(dict);
  }
  
  public int getStatusValue(int status) {
    String key = String.valueOf(status);
    return getStatusMap().getInt(key, 0);
  }
  
  public double getSelfAllTempPro(int dist) {
    String pro_dist = String.valueOf(dist);
    double sum = 0.0D;
    sum += this.propertyMap.getDouble(pro_dist);
    sum += ImbueUtil.getProValue(this.imbueMap, Integer.valueOf(dist));
    return sum;
  }
  
  public boolean isPet() {
    return this instanceof com.kitty.system.game.fightnew.bean.PetFightObject;
  }
  
  public boolean isPlayer() {
    return this instanceof com.kitty.system.game.fightnew.bean.PlayerFightObject;
  }
  
  public boolean isPartner() {
    return this instanceof com.kitty.system.game.fightnew.bean.PartnerFightObject;
  }
  
  public boolean isMonster() {
    if (this instanceof com.kitty.system.game.fightnew.bean.MonsterFightObject)
      return true; 
    return false;
  }
  
  public boolean isPetMirror() {
    return this instanceof com.kitty.system.game.fightnew.bean.PetMirrorFightObject;
  }
  
  public Boolean RoleIsDamgeImmunity() {
    return Boolean.valueOf((getStatusValue(EFFECTTYPE.EFFECTTYPE_TONGCHOUDIKAI.getValue()) >= 1 || getStatusValue(EFFECTTYPE.EFFECTTYPE_MONSTER_WUDI.getValue()) >= 1));
  }
  
  public boolean isFrozen() {
    return (getStatusValue(1003) >= 1);
  }
  
  public boolean isPoison() {
    return (getStatusValue(1005) >= 1);
  }
  
  public boolean isConfusion() {
    return (getStatusValue(1002) >= 1);
  }
  
  public boolean isSleep() {
    return (getStatusValue(1004) >= 1);
  }
  
  public boolean isForgotten() {
    return (getStatusValue(1006) >= 1);
  }
  
  public boolean inDeBuffStatus() {
    return (isFrozen() || isPoison() || isConfusion() || isSleep() || isForgotten());
  }
  
  public Role getRole() {
    return null;
  }
  
  public Pet getPet() {
    return null;
  }
  
  public void clearAssistBuff() {
    removeStatus(1010);
    removeStatus(1008);
    removeStatus(17);
    removeStatus(1009);
    removeStatus(1022);
  }
  
  public boolean hasAssistBuff() {
    return (getStatusValue(1010) > 0 || 
      getStatusValue(1008) > 0 || 
      getStatusValue(17) > 0 || 
      getStatusValue(1009) > 0 || 
      getStatusValue(1022) > 0);
  }
  
  public void clearObstacleBuff() {
    removeStatus(1005);
    removeStatus(1004);
    removeStatus(1006);
    removeStatus(1003);
    removeStatus(1002);
  }
  
  public IoSession getSession() {
    return null;
  }
  
  public void clearAnomalousStatus() {
    removeStatus(1002);
    removeStatus(1003);
    removeStatus(1004);
    removeStatus(1006);
    removeStatus(1019);
  }
  
  public void clearStatus(List<Integer> statusList) {
    if (statusList != null && statusList.size() > 0)
      for (Integer status : statusList)
        removeStatus(status.intValue());  
  }
  
  public List<Integer> getStatusList(List<Integer> sourceStatusList) {
    List<Integer> returnList = new ArrayList<>();
    if (sourceStatusList != null && sourceStatusList.size() > 0)
      for (Integer status : sourceStatusList) {
        if (getStatusValue(status.intValue()) > 0)
          returnList.add(status); 
      }  
    if (returnList.size() > 0)
      return returnList; 
    return null;
  }
  
  public long getHostUid() {
    return 0L;
  }
  
  public int compareTo(FightObject o) {
    int oSelf = (int)o.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + o.getSpeed();
    int self = (int)getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + getSpeed();
    return oSelf - self;
  }
  
  public FightMember asFightMember() {
    FightMember fightMember = new FightMember();
    fightMember.setId(getId().intValue());
    fightMember.setHost(getHostId());
    fightMember.setSerial((getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) ? 50000 : getSerial());
    fightMember.setPosition(getPos());
    fightMember.setIcon(getIcon());
    fightMember.setWeaponIcon(getWeaponId());
    fightMember.setList(asFieldValues());
    List<Integer> statusList = getStatusList();
    if (statusList != null)
      fightMember.setStatusList(statusList); 
    if (isPlayer()) {
      List<Effect> effects = ((EquipService)SpringUtils.getBean(EquipService.class)).selectEffect(getRole());
      if (effects != null && effects.size() > 0) {
        Effect wing = effects.stream().filter(e -> (e.getState() == 1 && e.getPart() == ADORN.WING.getValue())).findFirst().orElse(null);
        if (wing != null)
          fightMember.getLight_effect().add(Integer.valueOf(wing.getSerial())); 
      } 
    } 
    return fightMember;
  }
  
  public List<FiedValue> asFieldValues() {
    ArrayList<FiedValue> fiedValues = new ArrayList<>();
    fiedValues.add(new FiedValue("name", (byte)4, getName()));
    if (getGem() > 0)
      fiedValues.add(new FiedValue("gem", (byte)1, Integer.valueOf(getGem()))); 
    fiedValues.add(new FiedValue("lv", (byte)2, Integer.valueOf(getLevel())));
    fiedValues.add(new FiedValue("rei", (byte)3, Integer.valueOf(getRelive())));
    fiedValues.add(new FiedValue("maxLife", (byte)3, Integer.valueOf(getMaxLife())));
    fiedValues.add(new FiedValue("maxMana", (byte)3, Integer.valueOf(getMaxMana())));
    fiedValues.add(new FiedValue("currLife", (byte)3, Integer.valueOf(getCurrLife())));
    fiedValues.add(new FiedValue("currMana", (byte)3, Integer.valueOf(getCurrMana())));
    List<FiedValue> deyList = SpringUtils.getRoleService().deyList(getColor());
    if (deyList.size() > 0)
      fiedValues.addAll(deyList); 
    return fiedValues;
  }
  
  public double getSurplusLifePercent() {
    if (this.maxLife <= 0)
      return 0.0D; 
    String surplusLifePercentStr = BigDecimalUtil.divide(String.valueOf(getCurrLife()), String.valueOf(getMaxLife()));
    double surplusLifePercent = Double.parseDouble(surplusLifePercentStr);
    return surplusLifePercent;
  }
  
  public Object pushTempCache(String key, Object value) {
    if (this.tempCache == null)
      this.tempCache = new HashMap<>(4); 
    return this.tempCache.put(key, value);
  }
  
  public <T> T getTempCache(String key, Object dft) {
    if (this.tempCache == null)
      return (T)dft; 
    T result = (T)this.tempCache.get(key);
    return (result == null) ? (T)dft : result;
  }
  
  public double getSurplusManaPercent() {
    if (this.maxMana <= 0)
      return 0.0D; 
    String surplusManaPercentStr = BigDecimalUtil.divide(String.valueOf(getCurrMana()), String.valueOf(getMaxMana()));
    double surplusManaPercent = Double.parseDouble(surplusManaPercentStr);
    return surplusManaPercent;
  }
  
  public void recordAction(Fight fight) {
    getFightActionRecord().setv(String.valueOf(fight.getRound()), Integer.valueOf(getType()));
    getFightActionRecord().setv(fight.getRound() + "details", Integer.valueOf(getSkillId()));
  }
  
  public List<Integer> getStatusList() {
    if (getStatusMap().size() > 0) {
      List<Integer> statusList = new ArrayList<>();
      for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)getStatusMap().entrySet()) {
        if (!((String)entry.getKey()).matches("^[0-9]+$"))
          continue; 
        if (getStatusMap().getInt(entry.getKey(), 0) > 0) {
          statusList.add(Integer.valueOf(Integer.parseInt(entry.getKey())));
          statusList.add(Integer.valueOf(getStatusMap().getInt(entry.getKey(), 0)));
        } 
      } 
      if (statusList.size() > 0)
        return statusList; 
    } 
    return null;
  }
  
  public void addFallDownList(int targetId) {
    if (getTempCache() == null)
      this.tempCache = new HashMap<>(); 
    ArrayList<Integer> targetFallDownList = (ArrayList<Integer>)getTempCache().get("fallDownList");
    if (targetFallDownList == null)
      targetFallDownList = new ArrayList<>(); 
    targetFallDownList.add(Integer.valueOf(targetId));
  }
  
  public void createFallDownList() {
    if (this.tempCache == null)
      this.tempCache = new HashMap<>(); 
    getTempCache().put("fallDownList", new ArrayList());
  }
  
  public void addRoundUseSkill(int round, int skillId) {
    List<Integer> skillIds = this.roundUseSkillMap.get(Integer.valueOf(round));
    if (skillIds == null) {
      skillIds = new ArrayList<>();
      this.roundUseSkillMap.put(Integer.valueOf(round), skillIds);
    } 
    skillIds.add(Integer.valueOf(skillId));
  }
  
  public FightObject() {}
}
