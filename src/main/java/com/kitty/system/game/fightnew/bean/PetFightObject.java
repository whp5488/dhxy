package com.kitty.system.game.fightnew.bean;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.utils.FightUtils;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysPetSkill;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.apache.mina.core.session.IoSession;
import org.nutz.lang.util.NutMap;

public class PetFightObject extends FightObject {
  private static final NutMap publicAutoSet = NutMap.NEW();
  
  private FightObject master;
  
  private Pet pet;
  
  private Map<Integer, AcsryPetSkillParam> acsryPetSkillMap;
  
  private Map<Integer, HorseSkillParam> horseSkillMap;
  
  public Pet getPet() {
    return this.pet;
  }
  
  public Map<Integer, AcsryPetSkillParam> getAcsryPetSkillMap() {
    return this.acsryPetSkillMap;
  }
  
  public Map<Integer, HorseSkillParam> getHorseSkillMap() {
    return this.horseSkillMap;
  }
  
  public void setHorseSkillMap(Map<Integer, HorseSkillParam> horseSkillMap) {
    this.horseSkillMap = horseSkillMap;
  }
  
  static {
    publicAutoSet.setv("type", Integer.valueOf(1));
    publicAutoSet.setv("action", Integer.valueOf(1));
    publicAutoSet.setv("list", new ArrayList());
  }
  
  public PetFightObject(Pet pet, FightObject master) {
    NutMap nutMap = null;
    this.acsryPetSkillMap = new HashMap<>();
    this.horseSkillMap = new HashMap<>();
    this.master = master;
    Map<String, Object> autoSet = pet.getAutoFightSet();
    if (autoSet == null)
      nutMap = publicAutoSet; 
    setId(pet.getPetId());
    setIcon(pet.getShape().intValue());
    setName(pet.getPetName());
    setRelive(pet.getRebirth().intValue());
    setLevel(pet.getPetLv().intValue());
    setClose(pet.getPetClose());
    setPos(SkillFunc.getPetPosByMasterPos(Integer.valueOf(master.getPos())).intValue());
    setLogic(LOGIC.LOGICTYPE_PET.getValue());
    setSerial(pet.getPetType().intValue());
    setFightMsg(pet.getMsgMap());
    setSpeed(CountUtils.countPetSpeed(pet, true));
    setPhyPower(CountUtils.countPetPhyPower(pet, SpringUtils.getPetService().getPetEffective(getRole().getId().longValue(), pet)));
    setMaxLife(pet.getMaxLife().intValue());
    setMaxMana(pet.getMaxMana().intValue());
    setCurrMana(pet.getCurrMana().intValue());
    setCurrLife(pet.getCurrLife());
    this.pet = pet;
    setASide(master.isASide());
    setHostId(master.getId());
    setTargetId(pet.getPetId());
    if (nutMap.size() > 0) {
      int type = Integer.valueOf(String.valueOf(nutMap.get("type"))).intValue();
      int autoSkillId = Integer.valueOf(String.valueOf(nutMap.get("skillId"))).intValue();
      setType(type);
      setSkillId(autoSkillId);
      setAutoFightType(type);
      setAutoSkillId(autoSkillId);
      setAction(Integer.valueOf(nutMap.getOrDefault("action", Integer.valueOf(0)).toString()).intValue());
      if (nutMap.get("target") != null && !"".equals(nutMap.get("target"))) {
        String target = String.valueOf(nutMap.get("target"));
        setTargetId(Integer.valueOf(target).intValue());
        setAutoTarget(Integer.valueOf(target).intValue());
      } 
    } 
    setSkillIds(SkillFunc.initPetSkill(pet.getSkills()));
    setHorseSkillMap(SkillFunc.initHorseSkill(master.getRole().getId().longValue(), pet.getHorse()));
    setPropertyMap(CountUtils.proFieldValues(pet));
    CountUtils.restProFieldValue(getRole(), pet, getPropertyMap(), getAcsryPetSkillMap());
    HorseSkillUtils.countHorseSkillGain(getPropertyMap(), getHorseSkillMap());
    TianFuUtils.changePetPropery(getRole(), pet, getPropertyMap());
    color();
  }
  
  public void color() {
    if (this.pet.getColorList() != null && this.pet.getColorList().size() > 0) {
      byte currentColor = 0;
      for (Map<String, Byte> map : (Iterable<Map<String, Byte>>)this.pet.getColorList()) {
        if (((Byte)map.get("is_use")).byteValue() == 1) {
          currentColor = ((Byte)map.get("color")).byteValue();
          break;
        } 
      } 
      getColorMap().setv("currHut", Byte.valueOf(currentColor));
    } 
    byte acc = SpringUtils.getPetService().isOrnament(getRole().getId().longValue(), (SysPet)PetDataPool.petMap.get(this.pet.getPetType()), this.pet);
    getColorMap().setv("acc", Byte.valueOf(acc));
    if (this.pet.getAccColor() != null && this.pet.getAccColor().size() > 0)
      for (Byte key : this.pet.getAccColor().keySet()) {
        if (((Byte)this.pet.getAccColor().get(key)).byteValue() == 1) {
          getColorMap().setv("currAccHut", key);
          break;
        } 
      }  
    if (this.pet.getAccColorTwo() != null && this.pet.getAccColorTwo().size() > 0)
      for (Byte key : this.pet.getAccColorTwo().keySet()) {
        if (((Byte)this.pet.getAccColorTwo().get(key)).byteValue() == 1) {
          getColorMap().setv("currAccHut2", key);
          break;
        } 
      }  
    if (this.pet.getBrandNew() != null && this.pet.getBrandNew().size() > 0)
      for (Integer key : this.pet.getBrandNew().keySet()) {
        if (((Byte)this.pet.getBrandNew().get(key)).byteValue() == 1) {
          getColorMap().setv("hf", key);
          break;
        } 
      }  
  }
  
  public RoleSkill getSkillBy(Integer skillId) {
    if (String.valueOf(skillId).length() == 5) {
      Optional<PetSkill> anyPetSkill = this.pet.getSkills().stream().filter(skill -> (skill.getSerial() == skillId.intValue())).findAny();
      if (anyPetSkill.isPresent()) {
        SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
        if (petSkill == null)
          return null; 
        RoleSkill roleSkill = new RoleSkill();
        roleSkill.setSkillId(petSkill.getSerial());
        roleSkill.setType(petSkill.getAttr());
        return roleSkill;
      } 
    } else if (String.valueOf(skillId).length() == 6) {
      String hostSkillId = String.valueOf(skillId).substring(0, 5);
      Optional<PetSkill> anyPetSkill = this.pet.getSkills().stream().filter(skill -> (skill.getSerial() == Integer.parseInt(hostSkillId))).findAny();
      if (anyPetSkill.isPresent())
        return FightUtils.getPetRoleSkill(skillId.intValue()); 
    } 
    return null;
  }
  
  public Role getRole() {
    return this.master.getRole();
  }
  
  public int getHostId() {
    return this.master.getId().intValue();
  }
  
  public long getHostUid() {
    return this.master.getRole().getId().longValue();
  }
  
  public IoSession getSession() {
    return SessionUtils.getSessionBy(getHostUid());
  }
  
  public FightMember asFightMember() {
    FightMember fightMember = new FightMember();
    fightMember.setId(getId().intValue());
    fightMember.setHost(getHostId());
    fightMember.setIcon(getIcon());
    fightMember.setSerial(getSerial());
    fightMember.setPosition(getPos());
    fightMember.setList(asFieldValues());
    List<Integer> statusList = getStatusList();
    if (statusList != null)
      fightMember.setStatusList(statusList); 
    return fightMember;
  }
  
  public List<FiedValue> asFieldValues() {
    ArrayList<FiedValue> fiedValues = new ArrayList<>();
    fiedValues.add(new FiedValue("hostId", (byte)3, Integer.valueOf(getHostId())));
    fiedValues.add(new FiedValue("name", (byte)4, getName()));
    fiedValues.add(new FiedValue("maxLife", (byte)3, Integer.valueOf(getMaxLife())));
    fiedValues.add(new FiedValue("maxMana", (byte)3, Integer.valueOf(getMaxMana())));
    fiedValues.add(new FiedValue("currLife", (byte)3, Integer.valueOf(getCurrLife())));
    fiedValues.add(new FiedValue("currMana", (byte)3, Integer.valueOf(getCurrMana())));
    fiedValues.add(new FiedValue("rei", (byte)3, this.pet.getRebirth()));
    fiedValues.add(new FiedValue("lv", (byte)3, this.pet.getPetLv()));
    int currHut = this.colorMap.getInt("currHut", -1);
    if (currHut > 0)
      fiedValues.add(new FiedValue("currHut", (byte)2, Short.valueOf((short)currHut))); 
    int currAcc = this.colorMap.getInt("acc", 0);
    if (currAcc > 0)
      fiedValues.add(new FiedValue("acc", (byte)1, Byte.valueOf((byte)currAcc))); 
    int currAccHut = this.colorMap.getInt("currAccHut", -1);
    if (currAccHut > 0)
      fiedValues.add(new FiedValue("currAccHut", (byte)2, Short.valueOf((short)currAccHut))); 
    int currAccHut2 = this.colorMap.getInt("currAccHut2", -1);
    if (currAccHut2 > 0)
      fiedValues.add(new FiedValue("currAccHut2", (byte)2, Short.valueOf((short)currAccHut2))); 
    int hf = this.colorMap.getInt("hf", -1);
    if (hf > 0)
      fiedValues.add(new FiedValue("hf", (byte)2, Short.valueOf((short)hf))); 
    return fiedValues;
  }
  
  public String toString() {
    return "PetFightObject{id=" + this.id + ", name='" + this.name + '\'' + '}';
  }
}
