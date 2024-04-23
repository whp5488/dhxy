package com.kitty.system.game.fightnew.service;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.FACTION_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.enums.war.itempro.def.ZSXZ_PRO_DICT;
import com.kitty.system.enums.war.wardef.ATTRCAP;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.domain.vo.Field;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.pool.PartDataPool;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.nutz.lang.util.NutMap;

public class CountUtils {
  public static int countLife(Role role) {
    List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
    double life = countBasicLife(role, equips);
    double lifeCap = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()));
    double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) / 1000.0D;
    double cardPct = countCardPtc(role, PRO_DICT_TYPE.HP_PCT.getDist(), PRO_DICT_TYPE.HP_PCT.getType());
    life += lifeCap;
    Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
    double revise_hp = countCorrection(role, Integer.valueOf(ZSXZ_PRO_DICT.HP_PCT.getPro()), DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.HP_PCT.getPro()).intValue(), reiEnMap);
    NutMap runeMap = TianFuUtils.getWearInstantlyTakeEffect(role);
    double runeCap = 0.0D;
    if (runeMap != null)
      runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())); 
    NutMap acsryMap = AcsrySkillUtil.wearTakeEffect(role);
    double acsryCap = 0.0D;
    if (acsryMap != null)
      acsryCap = acsryMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), 0.0D); 
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(life * (1.0D + eqPct + acsryCap) * (1.0D + cardPct) * (1.0D + runeCap) * (1.0D + revise_hp)));
  }
  
  public static double countBasicLife(Role role, List<WearEquip> equips) {
    Double hp = Double.valueOf(0.0D);
    SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
    if (hero != null) {
      double gg = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.GG.getDist()));
      Integer lv = role.getLevel();
      hp = Double.valueOf(hero.getHp().intValue() + (role.getGg().intValue() + gg) * BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf(lv.intValue() + (2000.0D - lv.intValue() * 20.0D) / 100.0D)) * (1.0D + Double.valueOf(hero.getGGg()).doubleValue()));
    } 
    return hp.doubleValue();
  }
  
  public static int countMana(Role role) {
    List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
    double mana = countBasicMana(role, equips).doubleValue();
    double manaCap = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()));
    double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) / 1000.0D;
    double cardPct = countCardPtc(role, PRO_DICT_TYPE.MP_PCT.getDist(), PRO_DICT_TYPE.MP_PCT.getType());
    mana += manaCap;
    Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
    double revise_mp = countCorrection(role, Integer.valueOf(ZSXZ_PRO_DICT.MP_PCT.getPro()), DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.MP_PCT.getPro()).intValue(), reiEnMap);
    double runeCap = 0.0D;
    NutMap runeMap = TianFuUtils.getWearInstantlyTakeEffect(role);
    if (runeMap != null)
      runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())); 
    NutMap acsryMap = AcsrySkillUtil.wearTakeEffect(role);
    double acsryCap = 0.0D;
    if (acsryMap != null)
      acsryCap = acsryMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), 0.0D); 
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(mana * (1.0D + eqPct + acsryCap) * (1.0D + cardPct) * (1.0D + runeCap) * (1.0D + revise_mp)));
  }
  
  public static Double countBasicMana(Role role, List<WearEquip> equips) {
    Double mp = Double.valueOf(0.0D);
    SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
    if (hero != null) {
      double lx = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.LX.getDist()));
      Integer lv = role.getLevel();
      mp = Double.valueOf(hero.getMp().intValue() + (role.getLx().intValue() + lx) * BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf(lv.intValue() + (2000.0D - lv.intValue() * 20.0D) / 100.0D)) * (1.0D + Double.valueOf(hero.getGLx()).doubleValue()));
    } 
    return mp;
  }
  
  public static int countSpeed(Role role) {
    List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
    double speed = countBasicSpeed(role, equips);
    double speedPro = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
    double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist())) / 1000.0D;
    double cardPct = countCardPtc(role, PRO_DICT_TYPE.SPEED_PCT.getDist(), PRO_DICT_TYPE.SPEED_PCT.getType());
    speed += speedPro;
    Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
    double revise_sp = countCorrection(role, Integer.valueOf(ZSXZ_PRO_DICT.SPEED_PCT.getPro()), DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.SPEED_PCT.getPro()).intValue(), reiEnMap);
    double runeCap = 0.0D;
    NutMap runeMap = TianFuUtils.countFuPlayerBookGain(role);
    if (runeMap != null)
      runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), 0.0D); 
    int sp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(speed * (1.0D + eqPct) * (1.0D + cardPct) * (1.0D + runeCap) * (1.0D + revise_sp)));
    sp = hypersensitivity(sp);
    return sp;
  }
  
  public static double countBasicSpeed(Role role, List<WearEquip> equips) {
    Double speed = Double.valueOf(0.0D);
    SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
    if (hero != null) {
      double mj = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.MJ.getDist()));
      speed = Double.valueOf(hero.getSp().intValue() + (role.getMj().intValue() + mj) * (1.0D + Double.valueOf(hero.getGMj()).doubleValue()));
    } 
    return speed.doubleValue();
  }
  
  public static int countPhyPower(Role role) {
    List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
    double atk = countBasicPhyPower(role, equips);
    double powerAtk = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.ATTACK.getDist()));
    double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist())) / 1000.0D;
    double cardPct = countCardPtc(role, PRO_DICT_TYPE.ATTACK_PCT.getDist(), PRO_DICT_TYPE.ATTACK_PCT.getType());
    atk += powerAtk;
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(atk * (1.0D + eqPct) * (1.0D + cardPct)));
  }
  
  public static double countBasicPhyPower(Role role, List<WearEquip> equips) {
    Double phy = Double.valueOf(0.0D);
    SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
    if (hero != null) {
      double ll = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.LL.getDist()));
      Integer lv = role.getLevel();
      phy = Double.valueOf(hero.getAp().intValue() + (role.getLl().intValue() + ll) * (lv.intValue() + (2000.0D - lv.intValue() * 20.0D) / 100.0D) / 5.0D * (1.0D + Double.valueOf(hero.getGLl()).doubleValue()));
    } 
    return phy.doubleValue();
  }
  
  public static double countExtraPro(Role role, Integer pro, Long id) {
    double value = 0.0D;
    try {
      List<WearEquip> wearEquips = SpringUtils.getEquipService().selectWearEquip(role);
      for (int i = 0; i < wearEquips.size(); i++) {
        WearEquip wearEquip = wearEquips.get(i);
        if (wearEquip != null && wearEquip.getId() != null && 
          !wearEquip.getId().equals(id))
          value += countEquipPro(wearEquip, pro); 
      } 
    } catch (ConcurrentModificationException e) {
      LoggerUtils.error("装备属性 = {}", e);
    } 
    return value;
  }
  
  public static double countEquipPro(WearEquip wearEquip, Integer dist) {
    double base = 0.0D;
    Integer distPro = Integer.valueOf(dist.intValue() + PRO_DICT_SERIAL.EQUIP_PRO.getSerial());
    Integer distPsPro = Integer.valueOf(dist.intValue() + PRO_DICT_SERIAL.PS.getSerial());
    Integer distLh = Integer.valueOf(dist.intValue() + PRO_DICT_SERIAL.EQUIP_PRO_LH.getSerial());
    int bn = 0;
    Optional<FiedEquip> first = wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
    if (first.isPresent()) {
      FiedEquip fiedEquip = first.get();
      bn = fiedEquip.getValue().intValue();
    } 
    int dur = 0;
    Optional<FiedEquip> firstDur = wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6672))).findFirst();
    if (firstDur.isPresent()) {
      FiedEquip fiedEquip = firstDur.get();
      dur = fiedEquip.getValue().intValue();
    } 
    if (dur > 0) {
      List<Integer> gemList = ZB_PRO_BASE_DICT.toList();
      for (FiedEquip fiedEquip : wearEquip.getField()) {
        if (fiedEquip.getType().equals(distPro) || fiedEquip.getType().equals(distPsPro)) {
          if (bn > 0 && gemList.contains(dist)) {
            base += fiedEquip.getValue().doubleValue() + fiedEquip.getValue().intValue() * 0.03D * bn;
            continue;
          } 
          base += fiedEquip.getValue().doubleValue();
        } 
      } 
      for (FiedEquip fiedEquip : wearEquip.getArtifice()) {
        if (fiedEquip.getType().equals(distLh))
          base += fiedEquip.getValue().doubleValue(); 
      } 
    } 
    return base;
  }
  
  public static double countCardPtc(Role role, int key, int type) {
    if (key <= 0)
      return 0.0D; 
    int masquerade = role.getMasquerade();
    int cardExpireTime = role.getCardExpireTime();
    int currTime = (new Long(System.currentTimeMillis() / 1000L)).intValue();
    if (masquerade > 0 && cardExpireTime - 1 > currTime) {
      SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(masquerade));
      if (card == null)
        return 0.0D; 
      double five = card.getFiveAttr().getDouble(String.valueOf(key), 0.0D);
      if (five > 0.0D)
        return five; 
      int base = card.getBaseAttr().getInt(String.valueOf(key), 0);
      if (base == 0)
        return 0.0D; 
      if (type == PRO_TYPE.Pro_Value_NUM_TYPE.getValue())
        return base; 
      CardExercise cardExercise = ((CardService)SpringUtils.getBean(CardService.class)).getCardExercise(role);
      double wxIncrease = getWxIncrease(cardExercise, card);
      double anInt = base / 1000.0D * (1.0D + wxIncrease);
      return anInt;
    } 
    return 0.0D;
  }
  
  public static double getWxIncrease(CardExercise cardExercise, SysDataCard card) {
    double add = 0.0D;
    int goldGrade = cardExercise.getGoldGrade();
    add += (card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), 0) * goldGrade) * card.getFactor();
    int woodGrade = cardExercise.getWoodGrade();
    add += (card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), 0) * woodGrade) * card.getFactor();
    int waterGrade = cardExercise.getWaterGrade();
    add += (card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), 0) * waterGrade) * card.getFactor();
    int fireGrade = cardExercise.getFireGrade();
    add += (card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), 0) * fireGrade) * card.getFactor();
    int soilGrade = cardExercise.getSoilGrade();
    add += (card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), 0) * soilGrade) * card.getFactor();
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), Double.valueOf(add * 100.0D / 10000.0D / 100.0D));
  }
  
  public static double countCorrection(Role role, Integer dict, int type, Map<Integer, SysReiEn> reiEnMap) {
    List<Integer> toList = ZSXZ_PRO_DICT.toList();
    double revise_data = 0.0D;
    if (toList.contains(dict))
      for (int i = 0; i <= role.getRelive().intValue(); i++) {
        Integer serial = (Integer)role.getRest().getCorrection().get(Integer.valueOf(i));
        if (serial != null && serial.intValue() > 0) {
          SysReiEn reiEn = reiEnMap.get(serial);
          if (reiEn != null) {
            int rei = i;
            SysReiEn sysReiEn = reiEnMap.values().stream().filter(e -> (e.getRei() == rei && e.getKey() == reiEn.getRace() * 2 + reiEn.getSex() - 2)).findFirst().orElse(null);
            for (Field field : sysReiEn.getReiEn()) {
              if (field.getKey().equals(dict)) {
                if (type == PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()) {
                  revise_data += Double.parseDouble(BigDecimalUtil.divide(String.valueOf(field.getValue()), String.valueOf(100), 3));
                  continue;
                } 
                revise_data += field.getValue().doubleValue();
              } 
            } 
          } 
        } 
      }  
    return revise_data;
  }
  
  public static double countPartyPractice(Role role, Integer dict, Map<Integer, Double> map) {
    Double revise_data = Double.valueOf(0.0D);
    Double gain = map.get(dict);
    Integer count = (Integer)role.getRest().getParty().get(dict);
    if (count != null && gain != null)
      revise_data = Double.valueOf(count.intValue() * gain.doubleValue()); 
    return revise_data.doubleValue();
  }
  
  public static double countPetCloseDoubleHit(Pet pet) {
    long close = pet.getPetClose();
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(Math.min(0.01D * Math.pow(close, 0.3D) + 0.002D * Math.pow(close, 0.66D), 12.0D) * 10.0D / 1000.0D));
  }
  
  public static double countPetCloseFury(Pet pet) {
    long close = pet.getPetClose();
    return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(Math.min(0.0182D * Math.pow(close, 0.3D) + 0.01D * Math.pow(close, 0.5D), 8.0D) * 10.0D / 1000.0D));
  }
  
  public static int countPetLife(Pet pet, List<PetEquip> list) {
    int petLife = getPetLife(pet, list);
    double petSpecialLife = getPetSpecialLife(pet);
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(petLife + petSpecialLife));
  }
  
  public static double getPetBasicsLife(Pet pet) {
    PetBase petBase = pet.getPetBase();
    Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
    Integer hp_base = Integer.valueOf(countPetFlyStarter(pet.getFly().intValue(), pet.getFlyStarter(), petBase.getHp_base().intValue(), 1));
    return (hp_base.intValue() * pet.getPetLv().intValue()) * grow_up.doubleValue() * 0.7D + pet.getGg().intValue() * grow_up.doubleValue() * pet.getPetLv().intValue() + hp_base.intValue();
  }
  
  public static double getPetGrowUp(Pet pet, boolean flag) {
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    double baseGrowUp = pet.getPetBase().getGrow_up().intValue();
    if (flag && sysPet != null)
      baseGrowUp = sysPet.getGrowthRate()[(sysPet.getGrowthRate()).length - 1]; 
    Double growUp = Double.valueOf(baseGrowUp / 1000.0D + countRestGrowUp(pet));
    if (pet.getGrowthDan().intValue() > 0 && 
      sysPet != null)
      growUp = Double.valueOf(growUp.doubleValue() + sysPet.getGrowUpH()); 
    return growUp.doubleValue();
  }
  
  public static int getPetLife(Pet pet, List<PetEquip> list) {
    double life = getPetBasicsLife(pet);
    NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv().intValue(), pet.getRebirth().intValue(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
    double skillLifeCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()));
    double hosrseLifePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
    double equipLifeCap = gainPro(Integer.valueOf(PRO_DICT_TYPE.HP_CAP.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list);
    double eqPct = gainPro(Integer.valueOf(PRO_DICT_TYPE.HP_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list) / 1000.0D;
    life += skillLifeCap + equipLifeCap;
    double hp = life * (1.0D + hosrseLifePct) * (1.0D + eqPct);
    double hosrseLifePctLast = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_LAST.getValue());
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(hp + hp * hosrseLifePctLast));
  }
  
  public static double getPetSpecialLife(Pet pet) {
    double horsePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_CAP.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_ZYFY.getValue());
    return horsePct;
  }
  
  public static int countPetMana(Pet pet, List<PetEquip> list) {
    int mana = getPetMana(pet, list);
    double petSpecialMana = getPetSpecialMana(pet);
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(mana + petSpecialMana));
  }
  
  public static double getPetBasicsMana(Pet pet) {
    PetBase petBase = pet.getPetBase();
    Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
    Integer mp_base = Integer.valueOf(countPetFlyStarter(pet.getFly().intValue(), pet.getFlyStarter(), petBase.getMp_base().intValue(), 2));
    return (mp_base.intValue() * pet.getPetLv().intValue()) * grow_up.doubleValue() * 0.7D + pet.getLx().intValue() * grow_up.doubleValue() * pet.getPetLv().intValue() + mp_base.intValue();
  }
  
  public static int getPetMana(Pet pet, List<PetEquip> list) {
    double mana = getPetBasicsMana(pet);
    NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv().intValue(), pet.getRebirth().intValue(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
    double skillManaCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()));
    double horseManaPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.MP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
    double manaCap = gainPro(Integer.valueOf(PRO_DICT_TYPE.MP_CAP.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list);
    double eqPct = gainPro(Integer.valueOf(PRO_DICT_TYPE.MP_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list) / 1000.0D;
    mana += skillManaCap + manaCap;
    double mp = mana * (1.0D + horseManaPct) * (1.0D + eqPct);
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(mp));
  }
  
  public static double getPetSpecialMana(Pet pet) {
    double horsePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.MP_CAP.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_ZYFY.getValue());
    return horsePct;
  }
  
  public static int countPetPhyPower(Pet pet, List<PetEquip> list) {
    PetBase petBase = pet.getPetBase();
    Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
    Integer ap_base = Integer.valueOf(countPetFlyStarter(pet.getFly().intValue(), pet.getFlyStarter(), petBase.getAp_base().intValue(), 3));
    double phy = ((ap_base.intValue() * pet.getPetLv().intValue()) * grow_up.doubleValue() * 0.7D + pet.getLl().intValue() * grow_up.doubleValue() * pet.getPetLv().intValue()) / 5.0D + ap_base.intValue();
    NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv().intValue(), pet.getRebirth().intValue(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
    double skillAttackCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist()));
    int horseAttackCap = HorseSkillUtils.getHorseSkillProperty(pet, PRO_DICT_TYPE.ATTACK.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
    double horseAttackPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.ATTACK_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
    double atpCap = gainPro(Integer.valueOf(PRO_DICT_TYPE.ATTACK.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list);
    double atkEqPct = gainPro(Integer.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list) / 1000.0D;
    double attack = phy + skillAttackCap + horseAttackCap + atpCap;
    return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(attack * (1.0D + horseAttackPct) * (1.0D + atkEqPct)));
  }
  
  public static int countPetSpeed(Pet pet, boolean flag) {
    Double speed = Double.valueOf(0.0D);
    PetBase petBase = pet.getPetBase();
    Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
    int base = countPetFlyStarter(pet.getFly().intValue(), pet.getFlyStarter(), petBase.getSp_base().intValue(), 4);
    speed = Double.valueOf(base + pet.getMj().intValue() * grow_up.doubleValue());
    NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv().intValue(), pet.getRebirth().intValue(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
    double skillSpeed = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
    int horseAttackCap = HorseSkillUtils.getHorseSkillProperty(pet, PRO_DICT_TYPE.SPEED.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
    double horseSpPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.SPEED_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
    int awakenSpeedCap = AwakenSkillUtils.getAwakenSpeed(pet);
    int acsryPetSpCap = AcsryPetSkillUtil.getAttrSpeed(pet);
    double acsryPetSpPct = AcsryPetSkillUtil.getAttrSpeedPct(pet);
    speed = Double.valueOf(speed.doubleValue() + skillSpeed + awakenSpeedCap + horseAttackCap + acsryPetSpCap);
    int sp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(speed.doubleValue() * (1.0D + horseSpPct) * (1.0D + acsryPetSpPct)));
    if (flag)
      sp = hypersensitivity(sp); 
    return sp;
  }
  
  public static double countRestGrowUp(Pet pet) {
    double num = 0.0D;
    num += 0.1D * pet.getRebirth().intValue();
    if (pet.getBone().intValue() > 0) {
      SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(Integer.valueOf(75025));
      if (item != null)
        num += item.getValue().intValue() / 1000.0D * pet.getBone().intValue(); 
    } 
    NutMap tianFuMap = TianFuUtils.getWearInstantlyTakeEffect(pet);
    if (tianFuMap != null) {
      double tianFuProperty = tianFuMap.getDouble(String.valueOf(PRO_DICT_TYPE.CZL.getDist()), 0.0D);
      if (tianFuProperty != 0.0D)
        num += tianFuProperty; 
    } 
    num += Const.PET_FLY[pet.getFly().intValue()];
    return num;
  }
  
  public static NutMap countSkillBookGain(Addition addition) {
    NutMap map = new NutMap();
    for (RoleSkill skill : addition.getSkills()) {
      if (skill.getSkillId().intValue() <= 0)
        continue; 
      SkillProcessor processor = SkillProcessor.queryGainProcessor(skill.getSkillId());
      if (processor != null) {
        NutMap nutMap = processor.addition(addition, new Object[0]);
        if (nutMap != null)
          for (String key : nutMap.keySet())
            map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));  
      } 
    } 
    return map;
  }
  
  public static int countPetFlyStarter(int fly, int starter, int base, int type) {
    if (fly < 3 || starter == 0)
      return base; 
    if (type == 1 && starter == 1) {
      base += 60;
    } else if (type == 2 && starter == 2) {
      base += 60;
    } else if (type == 3 && starter == 3) {
      base += 60;
    } else if (type == 4 && starter == 4) {
      if (base < 0) {
        base -= 60;
      } else {
        base += 60;
      } 
    } 
    return base;
  }
  
  public static NutMap proFieldValues(Pet pet) {
    NutMap nutMap = new NutMap();
    SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
    if (sysPet != null) {
      Map<Integer, Double> correctionMap = ConvertDataPool.defPetCorrectionMap;
      Map<Integer, Integer> map = pet.getCorrection();
      for (Integer key : map.keySet()) {
        int serial = DataGlobalFunc.GetProDictType(key.intValue()).intValue();
        Integer num = map.get(key);
        if (key.intValue() != PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()) {
          double close_gain = 0.0D;
          if (PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro() == key.intValue()) {
            close_gain = countPetCloseDoubleHit(pet);
          } else if (PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro() == key.intValue()) {
            close_gain = countPetCloseFury(pet);
          } else if (PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro() == key.intValue()) {
            close_gain = 0.8D;
          } 
          Double gain = correctionMap.get(key);
          nutMap.setnx(String.valueOf(serial), Double.valueOf(close_gain + num.intValue() * ((gain == null) ? 0.0D : gain.doubleValue())));
          continue;
        } 
        nutMap.setnx(String.valueOf(serial), Integer.valueOf(num.intValue() + 3));
      } 
    } 
    nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), sysPet.getWxJin());
    nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), sysPet.getWxMu());
    nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), sysPet.getWxShui());
    nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), sysPet.getWxHuo());
    nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), sysPet.getWxTu());
    NutMap skillBookGainNutMap = countSkillBookGain(new Addition(pet.getPetLv().intValue(), pet.getRebirth().intValue(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), nutMap));
    for (String key : skillBookGainNutMap.keySet()) {
      if (key.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) || key
        .equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) || key
        .equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) || key
        .equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist())) || key.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist())))
        continue; 
      nutMap.setv(key, Double.valueOf(nutMap.getDouble(key) + skillBookGainNutMap.getDouble(key)));
    } 
    return nutMap;
  }
  
  public static void restProFieldValue(Role role, Pet pet, NutMap nutMap, Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
    NutMap additionAwaken = AwakenSkillUtils.addition(role, pet);
    if (additionAwaken != null && additionAwaken.size() > 0)
      for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)additionAwaken.entrySet())
        nutMap.setv(entry.getKey(), Double.valueOf(nutMap.getDouble(entry.getKey()) + additionAwaken.getDouble(entry.getKey())));  
    AcsryPetSkillUtil.addPetAcsry(role, pet, nutMap, acsryPetSkillMap);
  }
  
  public static NutMap countPro(Role role) {
    NutMap nutMap = new NutMap();
    Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
    Map<Integer, Double> faction = FACTION_PRO_DICT.toMap();
    Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
    NutMap tianFuMap = TianFuUtils.countFuPlayerBookGain(role);
    NutMap acsryMap = AcsrySkillUtil.initializeFightObject(role);
    List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
    for (Integer key : map.keySet()) {
      Integer type = map.get(key);
      double pro_value = cuntPro(equips, key, type.intValue());
      double growPro = costRaceGrowUp(role, key);
      double correction = countCorrection(role, Integer.valueOf(key.intValue() + PRO_DICT_SERIAL.HERO_RELIVE.getSerial()), type.intValue(), reiEnMap);
      Integer factionDist = Integer.valueOf(key.intValue() + PRO_DICT_SERIAL.HERO_FACTION.getSerial());
      double factionGain = countPartyPractice(role, factionDist, faction);
      double cardPro = countCardPtc(role, key.intValue(), type.intValue());
      double count = pro_value + growPro + correction + factionGain + cardPro;
      double tianFuProperty = TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(key), count);
      count += tianFuProperty;
      double acsryProperty = acsryMap.getDouble(String.valueOf(key), 0.0D);
      count += acsryProperty;
      nutMap.setv(key.toString(), Double.valueOf(count));
    } 
    return costProLimit(nutMap, role.getRace().intValue(), role.getRelive().intValue(), role.getRest().getParty(), tianFuMap, acsryMap);
  }
  
  public static double cuntPro(List<WearEquip> list, Integer dict, int type) {
    double pro = 0.0D;
    double gainPro = gainPro(list, dict);
    if (type == PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()) {
      pro = BigDecimalUtil.decimalFormat("#.###", Double.valueOf(gainPro / 1000.0D));
    } else {
      pro = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(3), Double.valueOf(gainPro));
    } 
    return pro;
  }
  
  public static double gainPro(Integer dist, List<PetEquip> list) {
    double pro = 0.0D;
    for (PetEquip equip : list) {
      FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(dist)).findFirst().orElse(null);
      if (fiedEquip != null)
        pro += fiedEquip.getValue().intValue(); 
    } 
    return pro;
  }
  
  public static double gainPro(List<WearEquip> list, Integer dist) {
    double pro = 0.0D;
    for (WearEquip wearEquip : list) {
      double count = countEquipPro(wearEquip, dist);
      pro += count;
    } 
    return pro;
  }
  
  public static double costRaceGrowUp(Role role, Integer dist) {
    double value = 0.0D;
    int race_ren_Lv = 0;
    int race_mo_Lv = 0;
    int race_xian_Lv = 0;
    int race_gui_Lv = 0;
    int race_long_Lv = 0;
    if (role.getRace().equals(Integer.valueOf(RACE.RACE_REN.getValue()))) {
      race_ren_Lv = (int)Math.min(role.getLevel().intValue(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
    } else if (role.getRace().equals(Integer.valueOf(RACE.RACE_MO.getValue()))) {
      race_mo_Lv = (int)Math.min(role.getLevel().intValue(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
    } else if (role.getRace().equals(Integer.valueOf(RACE.RACE_XIAN.getValue()))) {
      race_xian_Lv = (int)Math.min(role.getLevel().intValue(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
    } else if (role.getRace().equals(Integer.valueOf(RACE.RACE_GUI.getValue()))) {
      race_gui_Lv = (int)Math.min(role.getLevel().intValue(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
    } else if (role.getRace().equals(Integer.valueOf(RACE.RACE_LONG.getValue()))) {
      race_long_Lv = (int)Math.min(role.getLevel().intValue(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
    } 
    if (PRO_DICT_TYPE.K_FY.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_ren_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 8.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 8.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_long_Lv / 8.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_HL.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_ren_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 8.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 6.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_long_Lv / 6.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_HS.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_ren_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 8.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 6.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_long_Lv / 6.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_DU.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_ren_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 8.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 6.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_long_Lv / 6.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_H.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_xian_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 12.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((race_gui_Lv - 1 ^ 0xFFFFFFFF) / 8.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_S.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_xian_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 12.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((race_gui_Lv - 1 ^ 0xFFFFFFFF) / 8.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_F.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_xian_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 12.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((race_gui_Lv - 1 ^ 0xFFFFFFFF) / 8.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_L.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_xian_Lv / 4.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 12.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((race_gui_Lv - 1 ^ 0xFFFFFFFF) / 8.0D / 100.0D));
    } else if (PRO_DICT_TYPE.WL_XS.getDist() == dist.intValue()) {
      double variable = 6.0D;
      if (role.getRace().equals(Integer.valueOf(RACE.RACE_MO.getValue())))
        variable = 8.0D; 
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_mo_Lv / 12.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_long_Lv / variable / 100.0D));
    } else if (PRO_DICT_TYPE.K_GH.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 6.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_YW.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 6.0D / 100.0D)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_long_Lv / 6.0D / 100.0D));
    } else if (PRO_DICT_TYPE.K_SS.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 6.0D / 100.0D));
    } else if (PRO_DICT_TYPE.HIT_RATIO.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 12.0D / 100.0D)) + 0.8D;
    } else if (PRO_DICT_TYPE.MISS_RATIO.getDist() == dist.intValue()) {
      value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf(race_gui_Lv / 4.0D / 100.0D));
    } 
    return value;
  }
  
  public static NutMap costProLimit(NutMap nutMap, int race, int rei, Map<Integer, Integer> map, NutMap tMap, NutMap acsryMap) {
    String pmz_value = String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist());
    double pmz = nutMap.getDouble(pmz_value);
    if (pmz > 0.0D) {
      double d = Math.min(ATTRCAP.MAX_PACC_NUM.getValue(), Math.max(ATTRCAP.MIN_PACC_NUM.getValue(), pmz));
      nutMap.setv(pmz_value, Double.valueOf(d));
    } 
    String lsb_value = String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist());
    double lsb = nutMap.getDouble(lsb_value);
    if (lsb > 0.0D) {
      double d = Math.min(ATTRCAP.MAX_PSBL_NUM.getValue(), Math.max(ATTRCAP.MIN_PSBL_NUM.getValue(), lsb));
      nutMap.setv(lsb_value, Double.valueOf(d));
    } 
    String slsb_value = String.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
    double slsb = nutMap.getDouble(slsb_value);
    if (slsb > 0.0D) {
      double d = Math.min(ATTRCAP.MAX_PSBL_NUM.getValue(), Math.max(ATTRCAP.MIN_PSBL_NUM.getValue(), slsb));
      nutMap.setv(slsb_value, Double.valueOf(d));
    } 
    String lljt_value = String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist());
    double lljt = nutMap.getDouble(lljt_value);
    if (lljt > 0.0D) {
      double d = Math.min(ATTRCAP.MAX_PLJTIMES_NUM.getValue(), Math.max(ATTRCAP.MIN_PLJTIMES_NUM.getValue(), lljt));
      nutMap.setv(lljt_value, Double.valueOf(d));
    } 
    String lftl_value = String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist());
    double lftl = nutMap.getDouble(lftl_value);
    if (lftl > 0.0D) {
      double d = Math.min(ATTRCAP.MAX_FTLV_NUM.getValue(), Math.max(ATTRCAP.MIN_FTLV_NUM.getValue(), lftl));
      nutMap.setv(lftl_value, Double.valueOf(d));
    } 
    int gain_dist = FACTION_PRO_DICT.K_KZ_CAP.getPro();
    double gain = FACTION_PRO_DICT.K_KZ_CAP.getGain();
    double maxRes = ATTRCAP.MAX_RES_HERO_ELSE_VALUE.getValue();
    if (race > 0)
      if (race == RACE.RACE_REN.getValue()) {
        maxRes = ATTRCAP.MAX_RES_HERO_REN_VALUE.getValue();
      } else if (race == RACE.RACE_LONG.getValue()) {
        maxRes = ATTRCAP.MAX_RES_HERO_LONG_VALUE.getValue();
      }  
    String kfy = String.valueOf(PRO_DICT_TYPE.K_FY.getDist());
    double kfyValue = nutMap.getDouble(kfy);
    Integer count = map.get(Integer.valueOf(gain_dist));
    count = Integer.valueOf((count == null) ? 0 : count.intValue());
    double gainRes = count.intValue() * gain;
    double endValue = maxRes + gainRes;
    double tianFuRes = endValue * tMap.getDouble(PRO_DICT_TYPE.K_FY.getDist() + "_" + 'w', 0.0D);
    double acsryRes = endValue * acsryMap.getDouble(PRO_DICT_TYPE.K_FY.getDist() + "_" + 'w', 0.0D);
    endValue += tianFuRes;
    endValue += acsryRes;
    double min = Math.min(kfyValue, endValue);
    nutMap.setv(kfy, Double.valueOf(min));
    String khs = String.valueOf(PRO_DICT_TYPE.K_HS.getDist());
    double khsValue = nutMap.getDouble(khs);
    Integer integer1 = map.get(Integer.valueOf(gain_dist));
    integer1 = Integer.valueOf((integer1 == null) ? 0 : integer1.intValue());
    double d1 = integer1.intValue() * gain;
    double d2 = maxRes + d1;
    double d4 = d2 * tMap.getDouble(PRO_DICT_TYPE.K_HS.getDist() + "_" + 'w', 0.0D);
    double d7 = d2 * acsryMap.getDouble(PRO_DICT_TYPE.K_HS.getDist() + "_" + 'w', 0.0D);
    d2 += d4;
    d2 += d7;
    double d10 = Math.min(khsValue, d2);
    nutMap.setv(khs, Double.valueOf(d10));
    String khl = String.valueOf(PRO_DICT_TYPE.K_HL.getDist());
    double khlValue = nutMap.getDouble(khl);
    Integer integer2 = map.get(Integer.valueOf(gain_dist));
    integer2 = Integer.valueOf((integer2 == null) ? 0 : integer2.intValue());
    double d3 = integer2.intValue() * gain;
    double d5 = maxRes + d3;
    double d8 = d5 * tMap.getDouble(PRO_DICT_TYPE.K_HL.getDist() + "_" + 'w', 0.0D);
    double d11 = d5 * acsryMap.getDouble(PRO_DICT_TYPE.K_HL.getDist() + "_" + 'w', 0.0D);
    d5 += d8;
    d5 += d11;
    double d13 = Math.min(khlValue, d5);
    nutMap.setv(khl, Double.valueOf(d13));
    String kyw = String.valueOf(PRO_DICT_TYPE.K_YW.getDist());
    double kywValue = nutMap.getDouble(kyw);
    Integer integer3 = map.get(Integer.valueOf(gain_dist));
    integer3 = Integer.valueOf((integer3 == null) ? 0 : integer3.intValue());
    double d6 = integer3.intValue() * gain;
    double d9 = maxRes + d6;
    double d12 = d9 * tMap.getDouble(PRO_DICT_TYPE.K_YW.getDist() + "_" + 'w', 0.0D);
    double d14 = d9 * acsryMap.getDouble(PRO_DICT_TYPE.K_YW.getDist() + "_" + 'w', 0.0D);
    d9 += d12;
    d9 += d14;
    double d15 = Math.min(kywValue, d9);
    nutMap.setv(kyw, Double.valueOf(d15));
    double max = reiProTmAndQmMax(rei);
    String ktm = String.valueOf(PRO_DICT_TYPE.K_TM.getDist());
    double ktmValue = nutMap.getDouble(ktm);
    if (ktmValue > 0.0D) {
      double d = Math.min(ktmValue, max);
      nutMap.setv(ktm, Double.valueOf(d));
    } 
    String kqm = String.valueOf(PRO_DICT_TYPE.K_QM.getDist());
    double kqmValue = nutMap.getDouble(kqm);
    if (kqmValue > 0.0D) {
      double d = Math.min(kqmValue, max);
      nutMap.setv(kqm, Double.valueOf(d));
    } 
    double fkAndXlMax = reiProFkAndXlMax(rei);
    String kfk = String.valueOf(PRO_DICT_TYPE.K_FG.getDist());
    double kfkValue = nutMap.getDouble(kfk);
    if (kfkValue > 0.0D) {
      double d = Math.min(kfkValue, fkAndXlMax);
      nutMap.setv(kfk, Double.valueOf(d));
    } 
    String kxl = String.valueOf(PRO_DICT_TYPE.K_XL.getDist());
    double kxlValue = nutMap.getDouble(kxl);
    if (kxlValue > 0.0D) {
      double d = Math.min(kxlValue, fkAndXlMax);
      nutMap.setv(kxl, Double.valueOf(d));
    } 
    return nutMap;
  }
  
  public static double reiProTmAndQmMax(int rei) {
    double max = 0.25D;
    if (rei == 1) {
      max = 0.3D;
    } else if (rei == 2) {
      max = 0.35D;
    } else if (rei >= 3) {
      max = 0.4D;
    } 
    return max;
  }
  
  public static double reiProFkAndXlMax(int rei) {
    double max = 0.5D;
    if (rei == 1) {
      max = 0.6D;
    } else if (rei == 2) {
      max = 0.7D;
    } else if (rei >= 3) {
      max = 0.8D;
    } 
    return max;
  }
  
  public static Effect countHurt(Fight fight, FightObject attacker, FightObject target, double rdk, double oriparam) {
    PassiveSkillUtils.beforeCountHurt(fight, attacker, target);
    Effect effect = new Effect(1, 0);
    int damageHp = 0;
    damageHp += getPhyAttack(attacker);
    double phyRes = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
    double penetrate = attacker.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_RATIO.getDist());
    if (SkillAi.canAffectOnPro(penetrate)) {
      effect.setType(101);
      double sub_deep = attacker.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_EXTENT.getDist());
      phyRes -= sub_deep;
    } 
    if (oriparam > 1.0D)
      damageHp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(damageHp * oriparam)); 
    double weak = attacker.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
    damageHp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(damageHp * weak + damageHp - damageHp * phyRes));
    if (target.getType() == 1)
      damageHp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(damageHp * Math.pow(0.5D, 0.99D))); 
    if (rdk <= 0.0D)
      rdk = RandomUtil.round(Integer.valueOf(95), Integer.valueOf(105)) / 100.0D; 
    int hurt = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(damageHp * rdk));
    effect.setHit(rdk);
    effect.setHurt(Math.max(1, hurt));
    ImbueUtil.clearTemps(attacker.getImbueMap());
    return effect;
  }
  
  public static int getPhyAttack(FightObject attacker) {
    int damageHp = attacker.getPhyPower() + (int)(attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * attacker.getPhyPower());
    damageHp += damageHp * (int)attacker.getSelfAllTempPro(PRO_DICT_TYPE.ATTACK_ADDITION.getDist());
    return damageHp;
  }
  
  public static int countPartnerSpeed(Partner partner) {
    Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map<Short, SysPartnerProperty>)PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType());
    int level = partner.getPartnerLv() + partner.getRebirth() * 1000;
    SysPartnerProperty sysPartnerProperty = shortSysPartnerPropertyMap.get(Short.valueOf((short)level));
    if (sysPartnerProperty != null) {
      int speed = sysPartnerProperty.getSpeed();
      return speed;
    } 
    return 0;
  }
  
  public static int hypersensitivity(int baseSpeed) {
    if (baseSpeed != 0) {
      int absSpeed = Math.abs(baseSpeed);
      double limitMin = Math.min(300.0D / absSpeed, 0.3D);
      double limitMax = 0.0D;
      double random = RandomUtil.getRandomDouble(Double.valueOf(limitMax), Double.valueOf(limitMin));
      double randomNum = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(random));
      double doubleSpeed = baseSpeed * (1.0D - randomNum * absSpeed / absSpeed);
      int returnSpeed = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(doubleSpeed));
      return returnSpeed;
    } 
    return baseSpeed;
  }
}
