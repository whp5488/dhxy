package com.kitty.system.game.pet.service;

import com.google.common.base.Joiner;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.cache.PetCache;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.messages.old.RespFightMsgDlg;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.market.message.RespMarketPetCard;
import com.kitty.system.game.market.message.vo.HorseSkillCard;
import com.kitty.system.game.market.message.vo.PetEquipCard;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.domain.vo.PetCorrection;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.handler.FeedHelper;
import com.kitty.system.game.pet.handler.FeedPetHandler;
import com.kitty.system.game.pet.message.ReqPetCutAccColor;
import com.kitty.system.game.pet.message.ReqPetFly;
import com.kitty.system.game.pet.message.ReqPetFlyStarter;
import com.kitty.system.game.pet.message.ReqSetShapeTemp;
import com.kitty.system.game.pet.message.RespPetCorrection;
import com.kitty.system.game.pet.message.RespPetEffctInfo;
import com.kitty.system.game.pet.message.RespPetInfo;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.vo.Condition;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.vo.PetAccColor;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
//import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.json.Json;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PetService {
  private Logger logger = LoggerFactory.getLogger(getClass());
  
  @Autowired
  private Dao dao;
  
  @Autowired
  private IdWorker worker;
  
  @Autowired
  private HorseService horseService;
  
  @Autowired
  private RoleService roleService;
  
  @Autowired
  private BagService bagService;
  
  public void loadPet(Role role) {
    Map<Integer, Pet> map = selectPet(role);
    for (Integer key : map.keySet()) {
      Pet pet = map.get(key);
      loadPet(role, pet);
    } 
  }
  
  public void doWeeklyReset(Role role) {
    Map<Integer, Pet> map = selectPet(role);
    for (Integer key : map.keySet()) {
      Pet pet = map.get(key);
      if (pet.getDomesticate() > 0) {
        pet.setDomesticate((short)0);
        loadPet(role, pet);
        pet.save();
      } 
    } 
  }
  
  public void firstClaimPet(Role role, Integer type) {
    if (role.getExtendBox().getFkp() == 0 && Deploy.firstClaimPet.contains(type)) {
      SysPet sysPet = (SysPet)PetDataPool.petMap.get(type);
      if (sysPet != null) {
        boolean flag = canGetPet(role, sysPet, false);
        if (flag) {
          Pet pet = addPet(role, sysPet, 0, "上线领取");
          loadPet(role, pet);
          role.getExtendBox().setCombatPet(pet.getPetId());
          petInfo(role, pet);
          role.getExtendBox().setFkp((byte)1);
          role.save();
        } 
      } 
    } 
  }
  
  public boolean canGetPet(Role role, SysPet pet, boolean is) {
    boolean flag = true;
    String msg = "";
    Integer relive = role.getRelive();
    if (pet != null) {
      if (is && pet.getCanCompose() == 0) {
        flag = false;
        msg = "该宠物不可以合成";
      } 
      if (relive <= 0 && role.getLevel() < pet.getOpenLv()) {
        msg = String.format("需要等级%d", pet.getOpenLv());
        flag = false;
      } 
    } 
    SysRbAttr data = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
    if (data != null) {
      Integer limit = data.getPetNum() + role.getExtendBox().getBuyPetGrid();
      Map<Integer, Pet> map = selectPet(role);
      if (map != null && selectPet(role).size() >= limit) {
        msg = String.format("召唤兽超过上限%d个,无法获得", limit);
        int next_relive = relive + 1;
        SysRbAttr next_data = PartDataPool.rbAttrMap.get(next_relive + 1);
        if (next_data != null) {
          Integer num = next_data.getPetNum() + role.getExtendBox().getBuyPetGrid();
          msg = String.format("召唤兽超过当前上限%d个，%d转后上限增加至%d个", limit, next_relive, num);
        } 
        flag = false;
      } 
    } else {
      msg = "获取宠物信息失败！";
      flag = false;
    } 
    if (!flag)
      MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
    return flag;
  }
  
  public void onC2SGetPet(Integer serial, Role role) {
    SysPet sysPet = PetDataPool.petMap.get(serial);
    if (sysPet != null) {
      boolean flag = canGetPet(role, sysPet, true);
      if (!flag)
        return; 
      flag = this.roleService.checkCondition(role, sysPet.getNeedItem(), null);
      if (!flag) {
        MessagePusher.pushMessage(role, new RespNotifyMiscEx("合成材料不足"));
        return;
      } 
      flag = this.roleService.costCondition(role, new Condition(null, sysPet.getNeedItem()), "合成兑换", true, false);
      if (flag) {
        Pet pet = addPet(role, sysPet, 0, "合成");
        if (pet != null) {
          loadPet(role, pet);
          MessagePusher.pushMessage(role, new RespNotifyMiscEx("五行能量流转，你获得了新召唤兽" + String.format("#<O>%s#", new Object[] { sysPet.getName() })));
          if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
            MessagePusher.notify2Lb(role, 701, role.getName(), String.format("#<MM:%d,M:2,MP:%d,MT:%d,CP:%d,%s>[%s]#", new Object[] { pet.getPetId(), role.getRoleId(), pet.getPetType(), pet.getRebirth(), "O", pet.getPetName() }));
          } else {
            MessagePusher.notify2Lb(role, 700, role.getName(), String.format("#<MM:%d,M:2,MP:%d,MT:%d,CP:%d,%s>[%s]#", new Object[] { pet.getPetId(), role.getRoleId(), pet.getPetType(), pet.getRebirth(), "O", pet.getPetName() }));
          } 
        } else {
          MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获取宠物信息失败！", false));
        } 
      } 
    } else {
      MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获取宠物信息失败！", false));
    } 
  }
  
  public Pet addPet(Role role, SysPet sysPet, int bd, String cause) {
    Pet pet = initAsPet(sysPet);
    try {
      if (pet != null) {
        pet.setBindTime(bd);
        pet.setRoleId(role.getRoleId());
        PetBase petBase = createPetBase(sysPet, 1);
        if (petBase == null)
          this.logger.error("初始化宠物基础属性为空"); 
        List<PetEquip> list = new ArrayList<>();
        pet.setPetBase(petBase);
        int life = CountUtils.countPetLife(pet, list);
        pet.setCurrLife(life);
        pet.setMaxLife(life);
        int mana = CountUtils.countPetMana(pet, list);
        pet.setCurrMana(mana);
        pet.setMaxMana(mana);
        getArenaScore(role.getId(), pet);
        if (pet.getPetBase() == null)
          this.logger.error("设置完宠物宠物评分后宠物基础属性为空"); 
        selectPet(role).put(pet.getPetId(), pet);
        pet.save();
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)2, (byte)1, cause + "-增加宠物" + JsonUtils.object2String(pet), role.getMarking());
        return pet;
      } 
      return null;
    } catch (Exception e) {
      this.logger.error("增加宠物异常：玩家 = {}，添加宠物 = {}，宠物信息{}", role.getRoleId(), cause, pet);
      e.printStackTrace();
      return null;
    } 
  }
  
  public int getPetId() {
    return SpringUtils.getIdentityService().getNextId(IdentityType.PET);
  }
  
  public Pet initAsPet(SysPet sysPet) {
    if (sysPet != null) {
      Pet pet = new Pet();
      pet.setId(Long.valueOf(this.worker.nextId()));
      pet.setPetId(Integer.valueOf(getPetId()));
      pet.setCorrection(createCorrection());
      pet.setPetType(sysPet.getSerial());
      pet.setShape(sysPet.getShape());
      pet.setPetName(sysPet.getName());
      pet.setSkills(initSkills(sysPet));
      pet.setMsgMap(DataGlobalFunc.setMsg());
      pet.setCloseLimitMap(DataGlobalFunc.closeLimit());
      Map<String, Byte> map = new HashMap<>();
      map.put("color", Byte.valueOf((byte)0));
      map.put("is_use", Byte.valueOf((byte)1));
      List<Map<String, Byte>> list = new ArrayList<>();
      list.add(map);
      pet.setColorList(list);
      return pet;
    } 
    this.logger.error("合成宠物信息为空");
    return null;
  }
  
  public PetBase createPetBase(SysPet sysPet, Integer type) {
    PetBase petBase = new PetBase();
    int growUp = getGrowUp(sysPet, type);
    int hp = 0;
    int mp = 0;
    int ap = 0;
    int sp = 0;
    if (sysPet.getStaticInitVarScore().intValue() >= 90) {
      hp = sysPet.getHpMax().intValue();
      mp = sysPet.getMpMax().intValue();
      ap = sysPet.getApMax().intValue();
      sp = sysPet.getSpMax().intValue();
    } else {
      hp = RandomUtil.round(sysPet.getHpMin(), sysPet.getHpMax());
      mp = RandomUtil.round(sysPet.getMpMin(), sysPet.getMpMax());
      ap = RandomUtil.round(sysPet.getApMin(), sysPet.getApMax());
      sp = RandomUtil.round(sysPet.getSpMin(), sysPet.getSpMax());
    } 
    petBase.setHp_base(Integer.valueOf(hp));
    petBase.setMp_base(Integer.valueOf(mp));
    petBase.setAp_base(Integer.valueOf(ap));
    petBase.setSp_base(Integer.valueOf(sp));
    petBase.setGrow_up(Integer.valueOf(growUp));
    int grade = countPetGrade(sysPet, petBase);
    petBase.setGrade_base(Integer.valueOf(grade));
    return petBase;
  }
  
  public Map<Integer, Integer> createCorrection() {
    Map<Integer, Integer> map = new HashMap<>();
    List<Integer> list = PRACTICE_PET_PRO_DICT.toList();
    for (Integer v : list)
      map.put(v, Integer.valueOf(0)); 
    return map;
  }
  
  public void calculateBase(long id, Pet pet) {
    List<PetEquip> equips = getPetEffective(id, pet);
    int life = CountUtils.countPetLife(pet, equips);
    pet.setCurrLife(life);
    pet.setMaxLife(Integer.valueOf(life));
    int mana = CountUtils.countPetMana(pet, equips);
    pet.setCurrMana(Integer.valueOf(mana));
    pet.setMaxMana(Integer.valueOf(mana));
  }
  
  public List<PetEquip> getPetEffective(long id, Pet pet) {
    List<PetEquip> objects = new ArrayList<>();
    Map<Long, PetEquip> equipMap = this.bagService.getPetBagEquip(id);
    List<PetEquip> collect = (List<PetEquip>)equipMap.values().stream().filter(e -> pet.getId().equals(Long.valueOf(e.getPId()))).collect(Collectors.toList());
    for (PetEquip equip : collect) {
      SysPetAcsry sysPetAcsry = (SysPetAcsry)EquipDataPool.petAcsryMap.get(Integer.valueOf(equip.getSerial()));
      if (pet.getRebirth().intValue() * 1000 + pet.getPetLv().intValue() >= sysPetAcsry.getNeedRei() * 1000 + sysPetAcsry.getNeedGrade())
        objects.add(equip); 
    } 
    return objects;
  }
  
  public int getGrowUp(SysPet sysPet, Integer flag) {
    int[] growthRate = sysPet.getGrowthRate();
    int grow_up = growthRate[0];
    if (sysPet.getStaticInitVarScore().intValue() >= 90 && sysPet.getSerial().equals(Integer.valueOf(21053))) {
      grow_up = growthRate[growthRate.length - 1];
    } else {
      int index = (int)(Math.random() * growthRate.length);
      grow_up = growthRate[index];
    } 
    return grow_up;
  }
  
  public List<PetSkill> initSkills(SysPet pet) {
    List<Integer> list = new ArrayList<>();
    if (pet.getSkills().size() > 0)
      list.addAll(pet.getSkills()); 
    if (pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
      list.addAll(Arrays.asList(Const.NIUBI_PET_INIT_SKILLS));
    } else if (pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
      list.addAll(Arrays.asList(Const.TOP_PET_INIT_SKILLS));
    } else if (pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
      list.addAll(Arrays.asList(Const.VERY_PET_INIT_SKILLS));
    } else if (pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue()) {
      list.addAll(Arrays.asList(Const.PROTECT_PET_INIT_SKILLS));
    } else {
      list.addAll(Arrays.asList(Const.NORMAL_PET_INIT_SKILLS));
    } 
    list = list.subList(0, list.size() - pet.getSkills().size());
    if (pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue() || pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue())
      list.addAll(Arrays.asList(Const.NIUBI_PET_INIT_SPECIAL_SKILLS)); 
    List<PetSkill> petSkills = new ArrayList<>();
    for (Integer serial : list) {
      PetSkill petSkill = new PetSkill();
      petSkill.setSerial(serial.intValue());
      if (serial.intValue() > 0)
        petSkill.setType(PETSKILLSUB.SUBTYPE_BORN.getValue()); 
      if (serial.equals(Const.LOCK_SKILLPOS)) {
        petSkill.setSerial(-1);
        if (pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || pet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
          petSkill.setType(PETSKILLSUB.SUB_TYPE_SOUL.getValue());
        } else {
          petSkill.setType(PETSKILLSUB.SUBTYPE_SPECIAL.getValue());
        } 
      } 
      petSkills.add(petSkill);
    } 
    return petSkills;
  }
  
  public int countPetGrade(SysPet sysPet, PetBase base) {
    if (sysPet.getStaticInitVarScore().intValue() >= 90)
      return 3; 
    int[] weight = sysPet.getWeight();
    int hp_w = weight[0];
    int mp_w = weight[1];
    int ap_w = weight[2];
    int sp_w = weight[3];
    double scoreHp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(hp_w * (base.getHp_base().intValue() - sysPet.getHpMin().intValue() + hp_w)), String.valueOf(sysPet.getHpMax().intValue() - sysPet.getHpMin().intValue() + hp_w))).doubleValue();
    double scoreMp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(mp_w * (base.getMp_base().intValue() - sysPet.getMpMin().intValue() + mp_w)), String.valueOf(sysPet.getMpMax().intValue() - sysPet.getMpMin().intValue() + mp_w))).doubleValue();
    double scoreAp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(ap_w * (base.getAp_base().intValue() - sysPet.getApMin().intValue() + mp_w)), String.valueOf(sysPet.getApMax().intValue() - sysPet.getApMin().intValue() + ap_w))).doubleValue();
    double scoreSp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(sp_w * (base.getSp_base().intValue() - sysPet.getSpMin().intValue() + sp_w)), String.valueOf(sysPet.getSpMax().intValue() - sysPet.getSpMin().intValue() + sp_w))).doubleValue();
    double score = 100.0D;
    if (sysPet.getGrowthRate()[(sysPet.getGrowthRate()).length - 1] != Const.grand_t_range[0].doubleValue())
      score = 100.0D * Double.valueOf(BigDecimalUtil.divide(String.valueOf(base.getGrow_up().intValue() - sysPet.getGrowthRate()[0]), String.valueOf(sysPet.getGrowthRate()[(sysPet.getGrowthRate()).length - 1] - sysPet.getGrowthRate()[0]))).doubleValue(); 
    score = (int)((scoreHp + scoreMp + scoreAp + scoreSp + score) / (hp_w + mp_w + ap_w + sp_w + 100) * 100.0D);
    if (base.getGrow_up().intValue() != sysPet.getGrowthRate()[(sysPet.getGrowthRate()).length - 1])
      return countPetGrade(score); 
    if (score > 98.0D)
      return countPetGrade(score); 
    double one = (Math.abs(base.getHp_base().intValue()) + Math.abs(base.getMp_base().intValue()) + Math.abs(base.getAp_base().intValue()) + Math.abs(base.getSp_base().intValue()));
    double two = (Math.abs(sysPet.getHpMax().intValue()) + Math.abs(sysPet.getMpMax().intValue()) + Math.abs(sysPet.getApMax().intValue()) + Math.abs(sysPet.getSpMax().intValue()));
    if (one / two > 0.985D)
      return countPetGrade(99.0D); 
    return countPetGrade(score);
  }
  
  public int countPetGrade(double score) {
    if (score > 98.0D)
      return 4; 
    if (score > 86.0D)
      return 3; 
    if (score > 75.0D)
      return 2; 
    return 1;
  }
  
  public Integer getArenaScore(long id, Pet pet) {
    int score = 0;
    SysPet dataPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (dataPet != null) {
      score += getBaseScope(pet, dataPet);
      score += score * dataPet.getLevelType().intValue();
      List<PetSkill> skillsList = pet.getSkills();
      for (PetSkill petSkill : skillsList) {
        if (petSkill.getSerial() > 0) {
          if (petSkill.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
            score += Const.soul.intValue();
            continue;
          } 
          score += petCheckSills(petSkill.getSerial()).intValue();
        } 
      } 
      if (dataPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Normal.getValue()) {
        score += 0;
      } else if (dataPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue()) {
        score += 500;
      } else if (dataPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
        score += 1000;
      } else if (dataPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
        score += 1500;
      } else if (dataPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
        score += 2000;
      } 
    } 
    int petClose = (int)Math.min(pet.getPetClose() / 20000L, 1000L);
    score += petClose;
    score += (pet.getColorList().size() - 1) * 50;
    score += pet.getAccColor().size() * 50;
    score += pet.getAccColorTwo().size() * 50;
    score += pet.getBrandNew().size() * 200;
    List<PetEquip> collect = getPetEffective(id, pet);
    for (PetEquip equip : collect) {
      FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
      if (fiedEquip != null)
        score += fiedEquip.getValue().intValue(); 
    } 
    pet.setScore(Integer.valueOf(score));
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, collect));
    return Integer.valueOf(score);
  }
  
  public Integer petCheckSills(int skillId) {
    int logicType = DataGlobalFunc.GetObjType(Integer.valueOf(skillId)).intValue();
    if (logicType == LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue()) {
      Integer subType = SkillFunc.getPetSkillSubType(Integer.valueOf(skillId));
      if (subType.intValue() == PETSKILLSUB.SUBTYPE_NORMAL.getValue())
        return Const.normal; 
      if (subType.intValue() == PETSKILLSUB.SUBTYPE_SENIOR.getValue())
        return Const.senior; 
      if (subType.intValue() == PETSKILLSUB.SUBTYPE_SUPREME.getValue())
        return Const.supreme; 
      if (subType.intValue() == PETSKILLSUB.SUBTYPE_SPECIAL.getValue())
        return Const.special; 
      if (subType.intValue() == PETSKILLSUB.SUBTYPE_BORN.getValue())
        return Const.born; 
    } 
    return Integer.valueOf(0);
  }
  
  public int getBaseScope(Pet pet, SysPet dataPet) {
    int score = 0;
    double hp = pet.getPetBase().getHp_base().intValue() - dataPet.getHpMin().doubleValue();
    int hp_score = (dataPet.getHpMax().doubleValue() == dataPet.getHpMin().intValue()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(hp * 200.0D), Double.valueOf(dataPet.getHpMax().doubleValue() - dataPet.getHpMin().intValue())));
    score += Math.abs(hp_score);
    double mp = pet.getPetBase().getMp_base().intValue() - dataPet.getMpMin().doubleValue();
    int mp_score = (dataPet.getMpMax().doubleValue() == dataPet.getMpMin().intValue()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(mp * 200.0D), Double.valueOf(dataPet.getMpMax().doubleValue() - dataPet.getMpMin().intValue())));
    score += Math.abs(mp_score);
    double ap = pet.getPetBase().getAp_base().intValue() - dataPet.getApMin().doubleValue();
    int ap_score = (dataPet.getApMax().doubleValue() == dataPet.getApMin().intValue()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(ap * 200.0D), Double.valueOf(dataPet.getApMax().doubleValue() - dataPet.getApMin().intValue())));
    score += Math.abs(ap_score);
    double sp = pet.getPetBase().getSp_base().intValue() - dataPet.getSpMin().doubleValue();
    int sp_score = (dataPet.getSpMax().doubleValue() == dataPet.getSpMin().intValue()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(sp * 200.0D), Double.valueOf(dataPet.getSpMax().doubleValue() - dataPet.getSpMin().intValue())));
    score += Math.abs(sp_score);
    if (pet.getGrowthDan().intValue() >= 1)
      score += 500; 
    score += Arrays.binarySearch(dataPet.getGrowthRate(), pet.getPetBase().getGrow_up().intValue()) * 50;
    score += pet.getPetLv().intValue() * 4;
    score += pet.getRebirth().intValue() * 200;
    score += pet.getFly().intValue() * 70;
    score += pet.getBone().intValue() * 50;
    score += pet.getPetBase().getGrade_base().intValue() * 60;
    score += (pet.getCorrectionInfo().getLevel() - pet.getCorrectionInfo().getPoints()) * 10;
    return score;
  }
  
  public static Double calculateData(Double thed, Double t) {
    if (t.doubleValue() <= 0.0D)
      return Double.valueOf(0.0D); 
    return Double.valueOf(BigDecimalUtil.decimalScaleDouble(Integer.valueOf(0), Double.valueOf(thed.doubleValue() / t.doubleValue())));
  }
  
  public void loadPet(Role role, Pet pet) {
    petInfo(role, pet);
    RespPetEffctInfo effctInfo = new RespPetEffctInfo(role.getRoleId(), pet.getPetId(), pet.getSkills());
    MessagePusher.pushMessage(role, (Message)effctInfo);
    RespFightMsgDlg respFightMsgDlg = new RespFightMsgDlg();
    respFightMsgDlg.setId(pet.getPetId().intValue());
    respFightMsgDlg.setMsg(Json.toJson(pet.getMsgMap()));
    MessagePusher.pushMessage(role, (Message)respFightMsgDlg);
    sendPetCorrection(role, pet);
    sendPetPanels(role, pet);
  }
  
  public void petInfo(Role role, Pet pet) {
    RespPetInfo petInfo = new RespPetInfo();
    BeanUtils.copyBeanProp(petInfo, pet);
    if (role.getExtendBox().getCombatPet().equals(pet.getPetId()))
      petInfo.setCombat(Integer.valueOf(1)); 
    MessagePusher.pushMessage(role, (Message)petInfo);
  }
  
  public void sendPetPanels(Role role, Pet pet) {
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(pet.getPetId().intValue());
    List<FiedValue> list = getPetFieds(role.getId(), pet, true);
    list.add(new FiedValue("exp", (byte)4, String.valueOf(pet.getExp())));
    SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(pet.getRebirth().intValue() + 1));
    list.add(new FiedValue("nextExp", (byte)4, String.valueOf(FormulaHero.formula_petLvUpExp(sysRbattr.getLvLimit(), pet.getPetLv(), Integer.valueOf(pet.getPetLv().intValue() + 1), pet.getRebirth()))));
    int mount = 0;
    if (pet.getHorse() > 0L) {
      Horse horse = (Horse)this.horseService.getHorse(role).get(Long.valueOf(pet.getHorse()));
      if (horse != null)
        mount = horse.getSerial(); 
    } 
    list.add(new FiedValue("mount", (byte)3, Integer.valueOf(mount)));
    int domesticate = 30 - pet.getDomesticate();
    list.add(new FiedValue("domesticate", (byte)2, Integer.valueOf((domesticate <= 0) ? 0 : domesticate)));
    list.add(new FiedValue("acc", (byte)1, Byte.valueOf(isOrnament(role.getId().longValue(), (SysPet)PetDataPool.petMap.get(pet.getPetType()), pet))));
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdate);
  }
  
  public void sendPetCorrection(Role role, Pet pet) {
    RespPetCorrection respPetCorrection = new RespPetCorrection();
    respPetCorrection.setPetId(pet.getPetId());
    respPetCorrection.setExp(String.valueOf(pet.getCorrectionInfo().getExp()));
    respPetCorrection.setLevel(pet.getCorrectionInfo().getLevel());
    respPetCorrection.setPoints(pet.getCorrectionInfo().getPoints());
    respPetCorrection.setNutMap(JsonUtils.map2String(pet.getCorrection()));
    MessagePusher.pushMessage(role, (Message)respPetCorrection);
  }
  
  public void refreshPetClose(Role role, Pet pet, boolean once, int type) {
    if (type == FIGHT_TYPE.MONSTER.getValue()) {
      boolean flag = false;
      long close = pet.getPetClose();
      if (once) {
        flag = true;
        close -= 25L;
        close = (close < 0L) ? 0L : close;
      } else {
        flag = true;
        Integer num = (Integer)pet.getCloseLimitMap().get("close_fight");
        if (num != null && num.intValue() < 200000) {
          num = Integer.valueOf(num.intValue() + 5);
          if (num.intValue() > 200000) {
            close += (num.intValue() - 200000);
            num = Integer.valueOf(200000);
          } else {
            close += 5L;
          } 
          pet.getCloseLimitMap().put("close_fight", num);
        } 
      } 
      if (flag) {
        pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
        pet.save();
        if (role != null)
          refreshPetInfo(role, pet); 
      } 
    } 
  }
  
  public void refreshPetLevelClose(Pet pet) {
    long close = pet.getPetClose() + (pet.getPetLv().intValue() * 15);
    pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
  }
  
  public void addPetClose(Role role, long petId, long value, int serial, int tips) {
    Map<Integer, Pet> petMap = selectPet(role);
    Pet pet = petMap.values().stream().filter(e -> e.getId().equals(Long.valueOf(petId))).findFirst().orElse(null);
    if (pet != null) {
      long close = pet.getPetClose() + value;
      pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
      getArenaScore(role.getId().longValue(), pet);
      loadPet(role, pet);
      pet.save();
      if (serial == 702) {
        if (tips > 0)
          value = tips; 
        MessagePusher.notify2Misc(role, true, 702, new Object[] { pet.getPetName(), Long.valueOf(value) });
      } 
    } 
  }
  
  public void refreshPetInfo(Role role, Pet pet) {
    RespUpdate respUpdate = new RespUpdate();
    List<FiedValue> list = new ArrayList<>();
    respUpdate.setId(pet.getPetId().intValue());
    List<PetEquip> equips = getPetEffective(role.getId().longValue(), pet);
    list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countPetLife(pet, equips))));
    list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countPetMana(pet, equips))));
    list.add(new FiedValue("curr_life", (byte)3, Integer.valueOf(pet.getCurrLife())));
    list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
    list.add(new FiedValue("pet_close", (byte)4, String.valueOf(pet.getPetClose())));
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdate);
  }
  
  public void refineReplace(Role role, ReqSetShapeTemp reqSetShapeTemp) {
    Pet pet = selectPet(role).get(Integer.valueOf(reqSetShapeTemp.getId()));
    if (pet != null) {
      List<PetBase> petBases = (List<PetBase>)pet.getDarkBaseMap().get(Integer.valueOf(reqSetShapeTemp.getType()));
      if (reqSetShapeTemp.getIndex() >= petBases.size())
        return; 
      PetBase petBase = petBases.get(reqSetShapeTemp.getIndex());
      pet.setPetBase(petBase);
      calculateBase(role.getId().longValue(), pet);
      getArenaScore(role.getId().longValue(), pet);
      RespUpdate respUpdate = new RespUpdate();
      List<FiedValue> list = new ArrayList<>();
      respUpdate.setId(pet.getPetId().intValue());
      PetBase base = pet.getPetBase();
      list.add(new FiedValue("hp_base", (byte)3, base.getHp_base()));
      list.add(new FiedValue("mp_base", (byte)3, base.getMp_base()));
      list.add(new FiedValue("ap_base", (byte)3, base.getAp_base()));
      list.add(new FiedValue("sp_base", (byte)3, base.getSp_base()));
      list.add(new FiedValue("grow_up", (byte)3, base.getGrow_up()));
      list.add(new FiedValue("grade_base", (byte)3, base.getGrade_base()));
      list.add(new FiedValue("score", (byte)3, pet.getScore()));
      list.add(new FiedValue("fly", (byte)1, Byte.valueOf(pet.getFly().byteValue())));
      list.add(new FiedValue("starter", (byte)1, Byte.valueOf((byte)pet.getFlyStarter())));
      respUpdate.setList(list);
      MessagePusher.pushMessage(role, (Message)respUpdate);
      pet.getDarkBaseMap().put(Integer.valueOf(reqSetShapeTemp.getType()), new ArrayList());
      pet.save();
    } else {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "未获取到宠物信息", false));
    } 
  }
  
  public boolean addExp(Role role, Pet pet, long exp, boolean reason) {
    if (exp <= 0L)
      return false; 
    boolean flag = true;
    int level = pet.getPetLv().intValue();
    SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf(pet.getRebirth().intValue() + 1));
    if (sysRbattr != null) {
      Integer lvLimit = sysRbattr.getLvLimit();
      int roleGrade = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
      int petGrade = pet.getRebirth().intValue() * 1000 + pet.getPetLv().intValue();
      if (petGrade - roleGrade >= 10) {
        String msg = String.format("%s超过你的%d级%d级", new Object[] { pet.getPetName(), role.getLevel(), Integer.valueOf(10) });
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, false));
        return false;
      } 
      if (pet.getPetLv().intValue() >= lvLimit.intValue() && pet.getExp() >= DataGlobalFunc.storageExp(pet.getRebirth().intValue() * 1000 + pet.getPetLv().intValue(), LOGIC.LOGICTYPE_PET.getValue())) {
        String msg = String.format("%s召唤兽已到达%d转的上限%d级", new Object[] { pet.getPetName(), pet.getRebirth(), lvLimit });
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), msg, false));
        return false;
      } 
      String exp_tips = "#<G>" + exp + "#点经验";
      long storageExp = DataGlobalFunc.storageExp(pet.getRebirth().intValue() * 1000 + pet.getPetLv().intValue(), LOGIC.LOGICTYPE_PET.getValue());
      long totalExp = (pet.getExp() + exp > storageExp) ? storageExp : (pet.getExp() + exp);
      long needExp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Long.valueOf(FormulaHero.formula_petLvUpExp(lvLimit, pet.getPetLv(), Integer.valueOf(pet.getPetLv().intValue() + 1), pet.getRebirth())));
      if (totalExp >= needExp && pet.getPetLv().intValue() < lvLimit.intValue()) {
        while (totalExp >= needExp) {
          totalExp -= needExp;
          level++;
          pet.setExp(totalExp);
          storageExp = DataGlobalFunc.storageExp(pet.getRebirth().intValue() * 1000 + pet.getPetLv().intValue(), LOGIC.LOGICTYPE_PET.getValue());
          if (level > lvLimit.intValue() || pet.getRebirth().intValue() * 1000 + level - roleGrade > 10) {
            totalExp = (pet.getExp() + exp > storageExp) ? storageExp : (pet.getExp() + exp);
            pet.setExp(totalExp);
            flag = false;
          } 
          if (!flag) {
            if (pet.getExp() >= storageExp)
              MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你的召唤兽#<G>" + pet.getPetName() + "#等级已超过角色等级8级无法获得经验", true)); 
            updateExp(role, pet);
            break;
          } 
          pet.setPetLv(Integer.valueOf(level));
          petLevelChangeEvent(role, pet);
          needExp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Long.valueOf(FormulaHero.formula_petLvUpExp(lvLimit, pet.getPetLv(), Integer.valueOf(pet.getPetLv().intValue() + 1), pet.getRebirth())));
        } 
      } else {
        pet.setExp(totalExp);
        updateExp(role, pet);
      } 
      pet.save();
      role.save();
      if (reason)
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), "你的#<G>" + pet.getPetName() + "#获得了#<IR12>#" + exp_tips, true)); 
      return true;
    } 
    return false;
  }
  
  public void updateExp(Role role, Pet pet) {
    RespUpdate respUpdate = new RespUpdate();
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("exp", (byte)4, String.valueOf(pet.getExp())));
    respUpdate.setId(pet.getPetId().intValue());
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdate);
  }
  
  public void petLevelChangeEvent(Role role, Pet pet) {
    pet.setGg(Integer.valueOf(pet.getGg().intValue() + 1));
    pet.setLx(Integer.valueOf(pet.getLx().intValue() + 1));
    pet.setLl(Integer.valueOf(pet.getLl().intValue() + 1));
    pet.setMj(Integer.valueOf(pet.getMj().intValue() + 1));
    pet.setFpoint(Integer.valueOf(pet.getFpoint().intValue() + 4));
    refreshPetLevelClose(pet);
    calculateBase(role.getId().longValue(), pet);
    getArenaScore(role.getId().longValue(), pet);
    sendPetPanels(role, pet);
  }
  
  public Map<Integer, Pet> selectPet(Role role) {
    Map<Integer, Pet> map = (Map<Integer, Pet>)PetCache.PET_MAP.get(role.getId());
    if (map == null) {
      map = new HashMap<>();
      List<Pet> list = this.dao.query(Pet.class, Cnd.where("role_id", "=", role.getRoleId()));
      for (Pet pet : list) {
        pet.doAfterInit();
        map.put(pet.getPetId(), pet);
      } 
      PetCache.PET_MAP.put(role.getId(), map);
      return map;
    } 
    return map;
  }
  
  public void feedPet(Role role, Pet pet, int param, String type, int num, Object... args) {
    boolean clicker = this.roleService.mouserClicker(role, type, 250L);
    if (!clicker)
      return; 
    if (!SafeUtil.isUnlock(role))
      return; 
    FeedPetHandler handler = FeedHelper.INSTANCE.getHandler(type);
    if (handler != null) {
      String tips = handler.acceptFeed(pet, role, Integer.valueOf(param), type, num, args);
      if (!Strings.isEmpty(tips))
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx(role.getId(), tips, false)); 
    } else {
      MessagePusher.pushMessage(role, (Message)new RespMsg("很抱歉，该功能暂未开放。"));
    } 
  }
  
  public void assignCorrection(Role role, Pet pet, String nutMap) {
    Map<Integer, Integer> defMaxMap = (Map<Integer, Integer>)ConvertDataPool.defPetCorrectionMaxMap.get(Integer.valueOf(pet.getRebirth().intValue() + 1));
    Map<Integer, Integer> correctionMap = pet.getCorrection();
    PetCorrection correction = pet.getCorrectionInfo();
    NutMap map = (NutMap)Json.fromJson(NutMap.class, nutMap);
    List<Integer> list = PRACTICE_PET_PRO_DICT.toList();
    int count = 0;
    for (String key : map.keySet()) {
      Integer pro = Integer.valueOf(key);
      boolean contains = list.contains(pro);
      if (!contains) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("修炼属性中没有该修炼类型"));
        return;
      } 
      int num = map.getInt(key);
      if (num < 0)
        return; 
      if (num > 0) {
        if (num > ((Integer)defMaxMap.get(pro)).intValue()) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("修炼超过限值"));
          return;
        } 
        count += num - ((Integer)correctionMap.get(pro)).intValue();
      } 
    } 
    if (count > correction.getPoints()) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("剩余修炼点不足"));
      return;
    } 
    for (String key : map.keySet()) {
      int num = map.getInt(key);
      if (num > 0) {
        Integer pro = Integer.valueOf(key);
        correctionMap.put(pro, Integer.valueOf(num));
      } 
    } 
    int points = correction.getPoints() - count;
    correction.setPoints((points < 0) ? 0 : points);
    sendPetCorrection(role, pet);
    pet.save();
    role.save();
  }
  
  public void restAssignCorrection(Role role, Pet pet, byte type) {
    if (!checkRestExpend(role, pet, type))
      return; 
    pet.getCorrectionInfo().setPoints(pet.getCorrectionInfo().getLevel());
    pet.setCorrection(createCorrection());
    sendPetCorrection(role, pet);
    MessagePusher.pushMessage(role, (Message)new RespMsg("重置成功"));
    pet.save();
  }
  
  public void restAttribute(Role role, Pet pet, byte type) {
    Integer count = Integer.valueOf(FormulaHero.restorePorCount(pet.getRebirth().intValue(), pet.getPetLv().intValue(), LOGIC.LOGICTYPE_PET.getValue()));
    if (pet.getFpoint().equals(count)) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("当前召唤兽还没加过点，无需重置"));
      return;
    } 
    if (!checkRestExpend(role, pet, type))
      return; 
    pet.setGg(pet.getPetLv());
    pet.setLx(pet.getPetLv());
    pet.setLl(pet.getPetLv());
    pet.setMj(pet.getPetLv());
    pet.setFpoint(count);
    calculateBase(role.getId(), pet);
    sendPetPanels(role, pet);
    MessagePusher.pushMessage(role, new RespMsg("重置成功"));
    pet.save();
    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, getPetEffective(role.getId().longValue(), pet)));
  }
  
  public boolean checkRestExpend(Role role, Pet pet, byte type) {
    if (!SafeUtil.isUnlock(role))
      return false; 
    if (pet.getRebirth().intValue() > 0)
      if (type == 1) {
        boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 500000L, "重置宠物加点");
        if (!currency)
          return false; 
      } else {
        Integer goods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, 90020, 1, false, true, "重置宠物加点");
        if (goods != null) {
          MessagePusher.pushMessage(role, new RespMsg("背包没有万兽灵符"));
          return false;
        } 
      }  
    return true;
  }
  
  public void relive(Role role, Integer serial) {
    Pet pet = selectPet(role).get(serial);
    if (pet != null)
      if (role.getRelive() > pet.getRebirth()) {
        if (pet.getRebirth() < 3) {
          int needClose = Const.pet_relive[pet.getRebirth()];
          if (pet.getPetClose() < needClose) {
            MessagePusher.pushMessage(role, new RespMsg("亲密度不足"));
            return;
          } 
          pet.setPetClose(pet.getPetClose() - needClose);
          SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(pet.getRebirth() + 2);
          int count = FormulaHero.restorePorCount(pet.getRebirth() + 1, sysRbAttr.getInitLv(), LOGIC.LOGICTYPE_PET.getValue());
          pet.setGg(sysRbAttr.getInitLv());
          pet.setLx(sysRbAttr.getInitLv());
          pet.setLl(sysRbAttr.getInitLv());
          pet.setMj(sysRbAttr.getInitLv());
          pet.setFpoint(count);
          pet.setPetLv(sysRbAttr.getInitLv());
          pet.setRebirth(pet.getRebirth() + 1);
          calculateBase(role.getId(), pet);
          pet.save();
          MessagePusher.pushMessage(role, new RespHint(9, (short)1));
          loadPet(role, pet);
          MessagePusher.pushMessage(role, new RespMsg(String.format("#<ZWG>%s#转生成功", pet.getPetName())));
          EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, getPetEffective(role.getId().longValue(), pet)));
        } else {
          MessagePusher.pushMessage(role, new RespMsg("转生已上限"));
        } 
      } else if (pet.getRebirth() == 3) {
        MessagePusher.pushMessage(role, new RespMsg("转生已上限"));
      } else {
        MessagePusher.pushMessage(role, new RespMsg("请先将人物转生"));
      }  
  }
  
  public void petFly(Role role, ReqPetFly reqPetFly) {
    Pet pet = selectPet(role).get(reqPetFly.getPetId());
    if (pet == null)
      return; 
    if (pet.getFly() >= 3 || !DataGlobalFunc.checkPetFly(pet)) {
      MessagePusher.pushMessage(role, new RespHint(23, (short)0));
      MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升条件！"));
      return;
    } 
    int count = DataGlobalFunc.getPetFlyCount(pet.getRebirth(), pet.getPetLv());
    if (count <= 0 || pet.getFly() + 1 > count) {
      MessagePusher.pushMessage(role, new RespHint(23, (short)0));
      MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升条件！"));
      return;
    } 
    MessagePusher.pushMessage(role, new RespHint(23, (short)1));
    pet.setFly(count);
    calculateBase(role.getId(), pet);
    sendPetPanels(role, pet);
    pet.save();
    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, getPetEffective(role.getId().longValue(), pet)));
  }
  
  public void petFlyStarter(Role role, ReqPetFlyStarter reqPetFlyStarter) {
    if (reqPetFlyStarter.getStarter() < 1 || reqPetFlyStarter.getStarter() > 4)
      return; 
    Pet pet = selectPet(role).get(reqPetFlyStarter.getPetId());
    if (pet == null)
      return; 
    if (pet.getFly() < 2) {
      MessagePusher.pushMessage(role, new RespHint(24, (short)0));
      MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升调整！"));
      return;
    } 
    boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 5000000L);
    if (flag)
      if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 5000000L, "宠物飞升自定义初始值")) {
        MessagePusher.pushMessage(role, (Message)new RespHint(24, (short)0));
        return;
      }  
    int fly = pet.getFly() + 1 > 3 ? 3 : pet.getFly() + 1;
    MessagePusher.pushMessage(role, new RespHint(24, (short)1));
    pet.setFly(fly);
    pet.setFlyStarter(reqPetFlyStarter.getStarter());
    calculateBase(role.getId(), pet);
    sendPetPanels(role, pet);
    pet.save();
    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, getPetEffective(role.getId().longValue(), pet)));
  }
  
  public void chatPetDetail(Role ownerRole, Role role, int petId) {
    Pet pet = selectPet(ownerRole).get(petId);
    if (pet == null) {
      MessagePusher.pushMessage(role, new RespMsg("链接已过期~"));
      return;
    } 
    RespMarketPetCard card = new RespMarketPetCard();
    List<PetEquip> petEffective = getPetEffective(ownerRole.getId(), pet);
    card.setId(String.valueOf(pet.getPetId()));
    card.setRoleId(ownerRole.getRoleId());
    card.setSerial(pet.getPetType());
    card.setSkills(pet.getSkills());
    card.setStatus((byte)0);
    card.setEndTime(0);
    card.setFields(cardPetFied(ownerRole, pet, petEffective));
    List<FieldProp> fieldProps = petFieldPro(ownerRole, pet);
    if (fieldProps != null)
      card.setProList(fieldProps); 
    if (petEffective.size() > 0) {
      List<PetEquipCard> equips = new ArrayList<>();
      for (PetEquip equip : petEffective)
        equips.add(new PetEquipCard(equip.getId(), equip.getSerial(), equip.getProductOrder()));
      card.setEquips(equips);
    } 
    if (pet.getHorse() > 0L) {
      Map<Long, Horse> horseMap = this.horseService.getHorse(ownerRole);
      Horse horse = horseMap.get(pet.getHorse());
      if (horse != null) {
        List<HorseSkillCard> horseSkillCard = new ArrayList<>();
        String result = Joiner.on(",").withKeyValueSeparator(":").join(horse.getSkill());
        horseSkillCard.add(new HorseSkillCard(horse.getGrowUp(), horse.getLevel(), result));
        card.setHorseSkillCard(horseSkillCard);
      } 
    } 
    MessagePusher.pushMessage(role, (Message)card);
  }
  
  public List<FiedValue> cardPetFied(Role role, Pet pet, List<PetEquip> petEffective) {
    List<FiedValue> list = getPetFieds(role.getId(), pet, false);
    list.add(new FiedValue("name", (byte)4, pet.getPetName()));
    list.add(new FiedValue("correction", (byte)3, pet.getCorrectionInfo().getLevel()));
    list.add(new FiedValue("at", (byte)3, CountUtils.countPetPhyPower(pet, petEffective)));
    list.add(new FiedValue("sp", (byte)3, CountUtils.countPetSpeed(pet, false)));
    list.add(new FiedValue("curr_grow_up", (byte)3, (int) (CountUtils.getPetGrowUp(pet, false) * 1000.0D)));
    list.add(new FiedValue("max_grow_up", (byte)3, (int) (CountUtils.getPetGrowUp(pet, true) * 1000.0D)));
    if (petEffective != null && petEffective.size() > 0) {
      byte acc = isOrnament(role.getId().longValue(), (SysPet)PetDataPool.petMap.get(pet.getPetType()), pet);
      if (acc > 0)
        list.add(new FiedValue("acc", (byte)1, acc));
    } 
    return list;
  }
  
  public List<FieldProp> petFieldPro(Role role, Pet pet) {
    NutMap nutMap = CountUtils.proFieldValues(pet);
    if (nutMap != null) {
      CountUtils.restProFieldValue(role, pet, nutMap, new HashMap<>());
      HorseSkillUtils.countHorseSkillGain(nutMap, SkillFunc.initHorseSkill(role.getId(), pet.getHorse()));
      TianFuUtils.changePetPropery(role, pet, nutMap);
      Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
      List<Integer> wuXinDist = PRO_DICT_TYPE.getWuXinDist();
      List<FieldProp> proList = new ArrayList<>();
      for (String key : nutMap.keySet()) {
        Integer integer = Integer.valueOf(key);
        Integer type = map.get(integer);
        if (type != null) {
          if (type.equals(Integer.valueOf(PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()))) {
            proList.add(new FieldProp(integer, Integer.valueOf((int)(nutMap.getDouble(key) * 1000.0D))));
            continue;
          } 
          if (type.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
            if (wuXinDist.contains(integer)) {
              proList.add(new FieldProp(integer, Integer.valueOf((int)(nutMap.getDouble(key) * 100.0D))));
              continue;
            } 
            proList.add(new FieldProp(integer, Integer.valueOf(nutMap.getInt(key))));
          } 
        } 
      } 
      return proList;
    } 
    return null;
  }
  
  public List<FiedValue> getPetFieds(Long id, Pet pet, boolean flag) {
    List<FiedValue> list = new ArrayList<>();
    List<PetEquip> equips = new ArrayList<>();
    if (id != null)
      equips = getPetEffective(id.longValue(), pet); 
    list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countPetLife(pet, equips))));
    list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countPetMana(pet, equips))));
    list.add(new FiedValue("pet_close", (byte)4, String.valueOf(pet.getPetClose())));
    PetBase base = pet.getPetBase();
    list.add(new FiedValue("hp_base", (byte)3, base.getHp_base()));
    list.add(new FiedValue("mp_base", (byte)3, base.getMp_base()));
    list.add(new FiedValue("ap_base", (byte)3, base.getAp_base()));
    list.add(new FiedValue("sp_base", (byte)3, base.getSp_base()));
    list.add(new FiedValue("grow_up", (byte)3, base.getGrow_up()));
    list.add(new FiedValue("grade_base", (byte)3, base.getGrade_base()));
    list.add(new FiedValue("level", (byte)2, Short.valueOf(pet.getPetLv().shortValue())));
    list.add(new FiedValue("relive", (byte)1, Byte.valueOf(pet.getRebirth().byteValue())));
    list.add(new FiedValue("score", (byte)3, pet.getScore()));
    list.add(new FiedValue("bone", (byte)1, Byte.valueOf(pet.getBone().byteValue())));
    list.add(new FiedValue("growth_dan", (byte)1, Byte.valueOf(pet.getGrowthDan().byteValue())));
    short[] sendPetColor = getSendPetColor(pet.getColorList());
    if (sendPetColor != null) {
      list.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
      list.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
    } 
    short[] accColor = getSendPetAccColor(pet.getAccColor());
    if (accColor != null) {
      list.add(new FiedValue("accColor", (byte)2, Short.valueOf(accColor[0])));
      list.add(new FiedValue("accColorList", (byte)2, Short.valueOf(accColor[1])));
    } 
    short[] accColorTwo = getSendPetAccColor(pet.getAccColorTwo());
    if (accColorTwo != null) {
      list.add(new FiedValue("accColor2", (byte)2, Short.valueOf(accColorTwo[0])));
      list.add(new FiedValue("accColorList2", (byte)2, Short.valueOf(accColorTwo[1])));
    } 
    list.add(new FiedValue("fly", (byte)1, Byte.valueOf(pet.getFly().byteValue())));
    list.add(new FiedValue("awaken", (byte)2, Short.valueOf(pet.getAwaken())));
    petHf(pet, list);
    if (flag) {
      list.add(new FiedValue("gg", (byte)2, Short.valueOf(pet.getGg().shortValue())));
      list.add(new FiedValue("lx", (byte)2, Short.valueOf(pet.getLx().shortValue())));
      list.add(new FiedValue("ll", (byte)2, Short.valueOf(pet.getLl().shortValue())));
      list.add(new FiedValue("mj", (byte)2, Short.valueOf(pet.getMj().shortValue())));
      list.add(new FiedValue("fpoint", (byte)2, Short.valueOf(pet.getFpoint().shortValue())));
      list.add(new FiedValue("curr_life", (byte)3, Integer.valueOf(pet.getCurrLife())));
      list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
      list.add(new FiedValue("starter", (byte)1, Byte.valueOf((byte)pet.getFlyStarter())));
    } 
    return list;
  }
  
  public void petHf(Pet pet, List<FiedValue> list) {
    if (pet.getBrandNew().size() > 0) {
      String brandNew = Joiner.on(",").skipNulls().join(pet.getBrandNew().keySet());
      list.add(new FiedValue("list_hf", (byte)4, brandNew));
      short useHf = 0;
      for (Integer key : pet.getBrandNew().keySet()) {
        if (((Byte)pet.getBrandNew().get(key)).byteValue() == 1) {
          useHf = key.shortValue();
          break;
        } 
      } 
      list.add(new FiedValue("use_hf", (byte)2, Short.valueOf(useHf)));
    } 
  }
  
  public byte isOrnament(long id, SysPet sysPet, Pet pet) {
    byte flag = 0;
    if (sysPet == null)
      return flag; 
    Map<Long, PetEquip> equipMap = this.bagService.getPetBagEquip(id);
    if (equipMap.size() <= 0)
      return flag; 
    List<PetEquip> collect = (List<PetEquip>)equipMap.values().stream().filter(e -> (pet.getId().equals(Long.valueOf(e.getPId())) && e.getProductOrder() >= ACC_GRADE.PRICELESS.getValue())).collect(Collectors.toList());
    if (collect.size() <= 0)
      return flag; 
    if (sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() && collect.size() >= 1) {
      flag = 1;
    } else if (sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue() && collect.size() >= 2) {
      flag = 1;
    } else if ((sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) && 
      collect.size() >= 3) {
      flag = 1;
    } 
    return flag;
  }
  
  public short[] getSendPetColor(List<Map<String, Byte>> colorList) {
    short[] colors = new short[2];
    if (colorList != null && colorList.size() > 0) {
      byte currentColor = 0;
      StringBuffer sb = new StringBuffer("00000000");
      for (Map<String, Byte> map : colorList) {
        if (((Byte)map.get("is_use")).byteValue() == 1)
          currentColor = ((Byte)map.get("color")).byteValue(); 
        if (((Byte)map.get("color")).byteValue() == 0)
          continue; 
        sb.setCharAt(((Byte)map.get("color")).intValue() - 1, '1');
      } 
      String colorBanary = sb.reverse().toString();
      short returnColorList = Short.parseShort(colorBanary, 2);
      colors[0] = (short)currentColor;
      colors[1] = returnColorList;
      return colors;
    } 
    return null;
  }
  
  public static short[] getSendPetAccColor(Map<Byte, Byte> colorList) {
    short[] colors = new short[2];
    if (colorList != null && colorList.size() > 0) {
      byte currentColor = 0;
      StringBuffer sb = new StringBuffer("0000000000");
      for (Byte key : colorList.keySet()) {
        if (((Byte)colorList.get(key)).byteValue() == 1)
          currentColor = key.byteValue(); 
        sb.setCharAt(key.intValue() - 1, '1');
      } 
      String colorBanary = sb.reverse().toString();
      short returnColorList = Short.parseShort(colorBanary, 2);
      colors[0] = (short)currentColor;
      colors[1] = returnColorList;
      return colors;
    } 
    return null;
  }
  
  public void changeColor(Role role, int petId, byte colorSerial) {
    Pet pet = selectPet(role).get(Integer.valueOf(petId));
    if (pet == null)
      return; 
    List<Map<String, Byte>> colorList = pet.getColorList();
    Optional<Map<String, Byte>> colorOption = colorList.stream().filter(map -> (((Byte)map.get("color")).byteValue() == colorSerial)).findAny();
    if (!colorOption.isPresent())
      return; 
    int[] needYuanqizhuGB = getNeedYuanqizhuGB(pet, Byte.valueOf(colorSerial));
    int yuanqizhuNum = needYuanqizhuGB[0];
    int gongNum = needYuanqizhuGB[1];
    if (gongNum > 0) {
      boolean gongFlag = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, gongNum, "宠物变更颜色");
      if (!gongFlag)
        return; 
    } 
    Integer yqzGoods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, 75194, yuanqizhuNum, true, true, "宠物变更颜色");
    if (yqzGoods != null)
      return; 
    colorList.stream().forEach(map -> map.put("is_use", Byte.valueOf((byte)0)));
    Map<String, Byte> stringByteMap = colorOption.get();
    stringByteMap.put("is_use", Byte.valueOf((byte)1));
    pet.save();
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(petId);
    short[] sendPetColor = getSendPetColor(colorList);
    List<FiedValue> list = new ArrayList<>();
    if (sendPetColor != null) {
      list.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
      list.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
    } 
    respUpdate.setList(list);
    MessagePusher.pushMessage(role, (Message)respUpdate);
  }
  
  public void disassembleYuanqidan(Role role, Integer serial, Integer num) {
    if (num == null)
      num = Integer.valueOf(1); 
    BagService bean = (BagService)SpringUtils.getBean(BagService.class);
    Map<Integer, SysDataStuff> data_stuff = ItemDataPool.data_stuff;
    SysDataStuff sysDataStuff = data_stuff.get(serial);
    if (sysDataStuff == null)
      return; 
    if (sysDataStuff.getItemPJ().intValue() >= 5)
      return; 
    Integer goods = bean.consumeBagGoods(role, serial.intValue(), num.intValue(), true, true, "宠物变更颜色");
    if (goods != null)
      return; 
    Integer itemPJ = sysDataStuff.getItemPJ();
    Integer yqzNum = DICT_YUANQIDAN_PJ_ZHU.getNum(itemPJ);
    yqzNum = Integer.valueOf(yqzNum.intValue() * num.intValue());
    int i = bean.addGoods(role, 75194, yqzNum.intValue(), 0, true, false, "宠物变更颜色");
  }
  
  public void cutAccColor(Role role, ReqPetCutAccColor reqPetCutAccColor) {
    Pet pet = selectPet(role).get(reqPetCutAccColor.getId());
    if (pet == null)
      return; 
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet == null || sysPet.getAccColor() == null || sysPet.getAccColor().isEmpty())
      return; 
    Map<Byte, PetAccColor> petAccColorMap = (Map<Byte, PetAccColor>)PetDataPool.petAccColorMap.get(sysPet.getAccColor());
    if (petAccColorMap == null || petAccColorMap.get(Byte.valueOf(reqPetCutAccColor.getSerial())) == null)
      return; 
    if (reqPetCutAccColor.getPos() > 1 && sysPet.getLevelType().intValue() < PETLEVELTYPE.Pet_LevelType_GJSH.getValue())
      return; 
    if (!checkAccColor(role.getId().longValue(), pet, sysPet))
      return; 
    if (reqPetCutAccColor.getPos() == 1) {
      if (accColorConsume(role, (Byte)pet.getAccColor().get(Byte.valueOf(reqPetCutAccColor.getSerial())), petAccColorMap, reqPetCutAccColor.getSerial())) {
        for (Byte key : pet.getAccColor().keySet()) {
          if (((Byte)pet.getAccColor().get(key)).byteValue() == 1)
            pet.getAccColor().put(key, Byte.valueOf((byte)0)); 
        } 
        if (reqPetCutAccColor.getSerial() > 0)
          pet.getAccColor().put(Byte.valueOf(reqPetCutAccColor.getSerial()), Byte.valueOf((byte)1)); 
        pet.save();
        cutAccColor(role.getId().longValue(), pet, reqPetCutAccColor.getPos());
      } 
    } else if (reqPetCutAccColor.getPos() == 2) {
      if (accColorConsume(role, (Byte)pet.getAccColorTwo().get(Byte.valueOf(reqPetCutAccColor.getSerial())), petAccColorMap, reqPetCutAccColor.getSerial())) {
        for (Byte key : pet.getAccColorTwo().keySet()) {
          if (((Byte)pet.getAccColorTwo().get(key)).byteValue() == 1)
            pet.getAccColorTwo().put(key, Byte.valueOf((byte)0)); 
        } 
        if (reqPetCutAccColor.getSerial() > 0)
          pet.getAccColorTwo().put(Byte.valueOf(reqPetCutAccColor.getSerial()), Byte.valueOf((byte)1)); 
        pet.save();
        cutAccColor(role.getId().longValue(), pet, reqPetCutAccColor.getPos());
      } 
    } 
  }
  
  public void cutAccColor(long id, Pet pet, byte pos) {
    RespUpdate respUpdate = new RespUpdate();
    respUpdate.setId(pet.getPetId().intValue());
    List<FiedValue> list = new ArrayList<>();
    if (pos == 1) {
      short[] accColor = getSendPetAccColor(pet.getAccColor());
      if (accColor != null) {
        list.add(new FiedValue("accColor", (byte)2, Short.valueOf(accColor[0])));
        list.add(new FiedValue("accColorList", (byte)2, Short.valueOf(accColor[1])));
      } 
    } else if (pos == 2) {
      short[] accColorTwo = getSendPetAccColor(pet.getAccColorTwo());
      if (accColorTwo != null) {
        list.add(new FiedValue("accColor2", (byte)2, Short.valueOf(accColorTwo[0])));
        list.add(new FiedValue("accColorList2", (byte)2, Short.valueOf(accColorTwo[1])));
      } 
    } 
    respUpdate.setList(list);
    MessagePusher.pushMessage(id, (Message)respUpdate);
  }
  
  public boolean accColorConsume(Role role, Byte value, Map<Byte, PetAccColor> petAccColorMap, byte serial) {
    PetAccColor accColor = petAccColorMap.get(Byte.valueOf(serial));
    Map<Integer, Integer> map = accColor.getMat();
    if (value != null)
      map = ((PetAccColor)petAccColorMap.get(Byte.valueOf(serial))).getSwitchMat(); 
    for (Integer key : map.keySet()) {
      Integer amount = map.get(key);
      Integer num = this.bagService.getBagGoods(role, key.intValue(), amount.intValue());
      if (num != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("您的染料不足"));
        return false;
      } 
    } 
    if (accColor.getSilver() > 0 && 
      !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, accColor.getSilver(), "宠物饰品染色"))
      return false; 
    for (Integer key : map.keySet()) {
      Integer amount = map.get(key);
      Integer integer = this.bagService.consumeBagGoods(role, key.intValue(), amount.intValue(), true, true, "宠物饰品染色");
      if (integer != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("道具扣除失败"));
        return false;
      } 
    } 
    return true;
  }
  
  public boolean checkAccColor(long id, Pet pet, SysPet sysPet) {
    boolean flag = true;
    if (sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() && pet.getPetClose() < 1000000L) {
      flag = false;
    } else if (sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_SS.getValue() && pet.getPetClose() < 2000000L) {
      flag = false;
    } else if ((sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType().intValue() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) && 
      pet.getPetClose() < 10000000L) {
      flag = false;
    } 
    if (flag) {
      if (isOrnament(id, sysPet, pet) == 0) {
        flag = false;
        MessagePusher.pushMessage(id, (Message)new RespMsg("装备的无价佩饰数量不足"));
      } 
    } else {
      MessagePusher.pushMessage(id, (Message)new RespMsg("亲密度不足"));
    } 
    return flag;
  }
  
  enum DICT_YUANQIDAN_PJ_ZHU {
    PJ_ONE(Integer.valueOf(2), Integer.valueOf(1)),
    PJ_TWO(Integer.valueOf(3), Integer.valueOf(3)),
    PJ_THREE(Integer.valueOf(4), Integer.valueOf(25));
    
    private Integer pj;
    
    private Integer num;
    
    public Integer getPj() {
      return this.pj;
    }
    
    public Integer getNum() {
      return this.num;
    }
    
    DICT_YUANQIDAN_PJ_ZHU(Integer pj, Integer num) {
      this.pj = pj;
      this.num = num;
    }
    
    static Integer getNum(Integer pj) {
      Integer num = PJ_ONE.num;
      for (DICT_YUANQIDAN_PJ_ZHU concise : values()) {
        if (concise.getPj().equals(pj)) {
          num = concise.getNum();
          break;
        } 
      } 
      return num;
    }
  }
  
  private int[] getNeedYuanqizhuGB(Pet pet, Byte colorSerial) {
    int[] returnDatas = { 1, 0 };
    if (colorSerial.byteValue() == 0)
      return returnDatas; 
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    Integer levelType = sysPet.getLevelType();
    if (levelType == null)
      levelType = Integer.valueOf(1); 
    int yuanqizhuNum = 6;
    int gongNum = 100000;
    switch (levelType.intValue()) {
      case 2:
        yuanqizhuNum = 20;
        gongNum = 200000;
        break;
      case 3:
        yuanqizhuNum = 35;
        gongNum = 300000;
        break;
      case 4:
        yuanqizhuNum = 100;
        gongNum = 800000;
        break;
      case 5:
        yuanqizhuNum = 150;
        gongNum = 1000000;
        break;
    } 
    returnDatas[0] = yuanqizhuNum;
    returnDatas[1] = gongNum;
    return returnDatas;
  }
}
