
package com.kitty.system.game.fightnew.service;

import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.wardef.ATTRCAP;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.enums.war.itempro.def.FACTION_PRO_DICT;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.game.fightnew.book.SkillProcessor;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.enums.war.itempro.def.HORSE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.init.domain.vo.Field;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import java.util.Iterator;
import java.util.Optional;
import com.kitty.system.enums.war.itempro.def.ZB_PRO_BASE_DICT;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import java.util.ConcurrentModificationException;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.init.domain.SysHero;
import org.nutz.lang.util.NutMap;
import com.kitty.system.init.domain.SysReiEn;
import java.util.Map;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.List;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.ZSXZ_PRO_DICT;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;

public class CountUtils
{
    public static int countLife(final Role role) {
        final List<WearEquip> equips = SpringUtils.getBean(EquipService.class).getEquipValid(role, false);
        double life = countBasicLife(role, equips);
        final double lifeCap = gainPro(equips, PRO_DICT_TYPE.HP_CAP.getDist());
        final double eqPct = gainPro(equips, PRO_DICT_TYPE.HP_PCT.getDist()) / 1000.0;
        final double cardPct = countCardPtc(role, PRO_DICT_TYPE.HP_PCT.getDist(), PRO_DICT_TYPE.HP_PCT.getType());
        life += lifeCap;
        final Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        final double revise_hp = countCorrection(role, ZSXZ_PRO_DICT.HP_PCT.getPro(), DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.HP_PCT.getPro()), reiEnMap);
        final NutMap runeMap = TianFuUtils.getWearInstantlyTakeEffect(role);
        double runeCap = 0.0;
        if (runeMap != null) {
            runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()));
        }
        final NutMap acsryMap = AcsrySkillUtil.wearTakeEffect(role);
        double acsryCap = 0.0;
        if (acsryMap != null) {
            acsryCap = acsryMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), 0.0);
        }
        return BigDecimalUtil.decimalScaleIntegerDown(0, life * (1.0 + eqPct + acsryCap) * (1.0 + cardPct) * (1.0 + runeCap) * (1.0 + revise_hp));
    }
    
    public static double countBasicLife(final Role role, final List<WearEquip> equips) {
        Double hp = 0.0;
        final SysHero hero = PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            final double gg = gainPro(equips, PRO_DICT_TYPE.GG.getDist());
            final Integer lv = role.getLevel();
            hp = hero.getHp() + (role.getGg() + gg) * BigDecimalUtil.decimalScaleObjectDown(0, lv + (2000.0 - lv * 20.0) / 100.0) * (1.0 + Double.valueOf(hero.getGGg()));
        }
        return hp;
    }
    
    public static int countMana(final Role role) {
        final List<WearEquip> equips = SpringUtils.getBean(EquipService.class).getEquipValid(role, false);
        double mana = countBasicMana(role, equips);
        final double manaCap = gainPro(equips, PRO_DICT_TYPE.MP_CAP.getDist());
        final double eqPct = gainPro(equips, PRO_DICT_TYPE.MP_PCT.getDist()) / 1000.0;
        final double cardPct = countCardPtc(role, PRO_DICT_TYPE.MP_PCT.getDist(), PRO_DICT_TYPE.MP_PCT.getType());
        mana += manaCap;
        final Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        final double revise_mp = countCorrection(role, ZSXZ_PRO_DICT.MP_PCT.getPro(), DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.MP_PCT.getPro()), reiEnMap);
        double runeCap = 0.0;
        final NutMap runeMap = TianFuUtils.getWearInstantlyTakeEffect(role);
        if (runeMap != null) {
            runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()));
        }
        final NutMap acsryMap = AcsrySkillUtil.wearTakeEffect(role);
        double acsryCap = 0.0;
        if (acsryMap != null) {
            acsryCap = acsryMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), 0.0);
        }
        return BigDecimalUtil.decimalScaleIntegerDown(0, mana * (1.0 + eqPct + acsryCap) * (1.0 + cardPct) * (1.0 + runeCap) * (1.0 + revise_mp));
    }
    
    public static Double countBasicMana(final Role role, final List<WearEquip> equips) {
        Double mp = 0.0;
        final SysHero hero = PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            final double lx = gainPro(equips, PRO_DICT_TYPE.LX.getDist());
            final Integer lv = role.getLevel();
            mp = hero.getMp() + (role.getLx() + lx) * BigDecimalUtil.decimalScaleObjectDown(0, lv + (2000.0 - lv * 20.0) / 100.0) * (1.0 + Double.valueOf(hero.getGLx()));
        }
        return mp;
    }
    
    public static int countSpeed(final Role role) {
        final List<WearEquip> equips = SpringUtils.getBean(EquipService.class).getEquipValid(role, false);
        double speed = countBasicSpeed(role, equips);
        final double speedPro = gainPro(equips, PRO_DICT_TYPE.SPEED.getDist());
        final double eqPct = gainPro(equips, PRO_DICT_TYPE.SPEED_PCT.getDist()) / 1000.0;
        final double cardPct = countCardPtc(role, PRO_DICT_TYPE.SPEED_PCT.getDist(), PRO_DICT_TYPE.SPEED_PCT.getType());
        speed += speedPro;
        final Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        final double revise_sp = countCorrection(role, ZSXZ_PRO_DICT.SPEED_PCT.getPro(), DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.SPEED_PCT.getPro()), reiEnMap);
        double runeCap = 0.0;
        final NutMap runeMap = TianFuUtils.countFuPlayerBookGain(role);
        if (runeMap != null) {
            runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), 0.0);
        }
        int sp = BigDecimalUtil.decimalScaleIntegerDown(0, speed * (1.0 + eqPct) * (1.0 + cardPct) * (1.0 + runeCap) * (1.0 + revise_sp));
        sp = hypersensitivity(sp);
        return sp;
    }
    
    public static double countBasicSpeed(final Role role, final List<WearEquip> equips) {
        Double speed = 0.0;
        final SysHero hero = PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            final double mj = gainPro(equips, PRO_DICT_TYPE.MJ.getDist());
            speed = hero.getSp() + (role.getMj() + mj) * (1.0 + Double.valueOf(hero.getGMj()));
        }
        return speed;
    }
    
    public static int countPhyPower(final Role role) {
        final List<WearEquip> equips = SpringUtils.getBean(EquipService.class).getEquipValid(role, false);
        double atk = countBasicPhyPower(role, equips);
        final double powerAtk = gainPro(equips, PRO_DICT_TYPE.ATTACK.getDist());
        final double eqPct = gainPro(equips, PRO_DICT_TYPE.ATTACK_PCT.getDist()) / 1000.0;
        final double cardPct = countCardPtc(role, PRO_DICT_TYPE.ATTACK_PCT.getDist(), PRO_DICT_TYPE.ATTACK_PCT.getType());
        atk += powerAtk;
        return BigDecimalUtil.decimalScaleIntegerDown(0, atk * (1.0 + eqPct) * (1.0 + cardPct));
    }
    
    public static double countBasicPhyPower(final Role role, final List<WearEquip> equips) {
        Double phy = 0.0;
        final SysHero hero = PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            final double ll = gainPro(equips, PRO_DICT_TYPE.LL.getDist());
            final Integer lv = role.getLevel();
            phy = hero.getAp() + (role.getLl() + ll) * (lv + (2000.0 - lv * 20.0) / 100.0) / 5.0 * (1.0 + Double.valueOf(hero.getGLl()));
        }
        return phy;
    }
    
    public static double countExtraPro(final Role role, final Integer pro, final Long id) {
        double value = 0.0;
        try {
            final List<WearEquip> wearEquips = SpringUtils.getEquipService().selectWearEquip(role);
            for (int i = 0; i < wearEquips.size(); ++i) {
                final WearEquip wearEquip = wearEquips.get(i);
                if (wearEquip != null && wearEquip.getId() != null && !wearEquip.getId().equals(id)) {
                    value += countEquipPro(wearEquip, pro);
                }
            }
        }
        catch (final ConcurrentModificationException e) {
            LoggerUtils.error("装备属性 = {}", e);
        }
        return value;
    }
    
    public static double countEquipPro(final WearEquip wearEquip, final Integer dist) {
        double base = 0.0;
        final Integer distPro = dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial();
        final Integer distPsPro = dist + PRO_DICT_SERIAL.PS.getSerial();
        final Integer distLh = dist + PRO_DICT_SERIAL.EQUIP_PRO_LH.getSerial();
        int bn = 0;
        final Optional<FiedEquip> first = wearEquip.getField().stream().filter(e -> e.getType().equals(6670)).findFirst();
        if (first.isPresent()) {
            final FiedEquip fiedEquip = first.get();
            bn = fiedEquip.getValue();
        }
        int dur = 0;
        final Optional<FiedEquip> firstDur = wearEquip.getField().stream().filter(e -> e.getType().equals(6672)).findFirst();
        if (firstDur.isPresent()) {
            final FiedEquip fiedEquip2 = firstDur.get();
            dur = fiedEquip2.getValue();
        }
        if (dur > 0) {
            final List<Integer> gemList = ZB_PRO_BASE_DICT.toList();
            for (final FiedEquip fiedEquip3 : wearEquip.getField()) {
                if (fiedEquip3.getType().equals(distPro) || fiedEquip3.getType().equals(distPsPro)) {
                    if (bn > 0 && gemList.contains(dist)) {
                        base += fiedEquip3.getValue() + fiedEquip3.getValue() * (0.03 * bn);
                    }
                    else {
                        base += fiedEquip3.getValue();
                    }
                }
            }
            for (final FiedEquip fiedEquip3 : wearEquip.getArtifice()) {
                if (fiedEquip3.getType().equals(distLh)) {
                    base += fiedEquip3.getValue();
                }
            }
        }
        return base;
    }
    
    public static double countCardPtc(final Role role, final int key, final int type) {
        if (key <= 0) {
            return 0.0;
        }
        final int masquerade = role.getMasquerade();
        final int cardExpireTime = role.getCardExpireTime();
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (masquerade <= 0 || cardExpireTime - 1 <= currTime) {
            return 0.0;
        }
        final SysDataCard card = CardDataPool.cardMap.get(masquerade);
        if (card == null) {
            return 0.0;
        }
        final double five = card.getFiveAttr().getDouble(String.valueOf(key), 0.0);
        if (five > 0.0) {
            return five;
        }
        final int base = card.getBaseAttr().getInt(String.valueOf(key), 0);
        if (base == 0) {
            return 0.0;
        }
        if (type == PRO_TYPE.Pro_Value_NUM_TYPE.getValue()) {
            return base;
        }
        final CardExercise cardExercise = SpringUtils.getBean(CardService.class).getCardExercise(role);
        final double wxIncrease = getWxIncrease(cardExercise, card);
        final double anInt = base / 1000.0 * (1.0 + wxIncrease);
        return anInt;
    }
    
    public static double getWxIncrease(final CardExercise cardExercise, final SysDataCard card) {
        double add = 0.0;
        final int goldGrade = cardExercise.getGoldGrade();
        add += card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), 0) * goldGrade * card.getFactor();
        final int woodGrade = cardExercise.getWoodGrade();
        add += card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), 0) * woodGrade * card.getFactor();
        final int waterGrade = cardExercise.getWaterGrade();
        add += card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), 0) * waterGrade * card.getFactor();
        final int fireGrade = cardExercise.getFireGrade();
        add += card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), 0) * fireGrade * card.getFactor();
        final int soilGrade = cardExercise.getSoilGrade();
        add += card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), 0) * soilGrade * card.getFactor();
        return BigDecimalUtil.decimalScaleObjectDown(8, add * 100.0 / 10000.0 / 100.0);
    }
    
    public static double countCorrection(final Role role, final Integer dict, final int type, final Map<Integer, SysReiEn> reiEnMap) {
        final List<Integer> toList = ZSXZ_PRO_DICT.toList();
        double revise_data = 0.0;
        if (toList.contains(dict)) {
            for (int i = 0; i <= role.getRelive(); ++i) {
                final Integer serial = role.getRest().getCorrection().get(i);
                if (serial != null && serial > 0) {
                    final SysReiEn reiEn = reiEnMap.get(serial);
                    if (reiEn != null) {
                        final int rei = i;
                        final SysReiEn sysReiEn = reiEnMap.values().stream().filter(e -> e.getRei() == rei && e.getKey() == reiEn.getRace() * 2 + reiEn.getSex() - 2).findFirst().orElse(null);
                        for (final Field field : sysReiEn.getReiEn()) {
                            if (field.getKey().equals(dict)) {
                                if (type == PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()) {
                                    revise_data += Double.parseDouble(BigDecimalUtil.divide(String.valueOf(field.getValue()), String.valueOf(100), 3));
                                }
                                else {
                                    revise_data += field.getValue();
                                }
                            }
                        }
                    }
                }
            }
        }
        return revise_data;
    }
    
    public static double countPartyPractice(final Role role, final Integer dict, final Map<Integer, Double> map) {
        Double revise_data = 0.0;
        final Double gain = map.get(dict);
        final Integer count = role.getRest().getParty().get(dict);
        if (count != null && gain != null) {
            revise_data = count * gain;
        }
        return revise_data;
    }
    
    public static double countPetCloseDoubleHit(final Pet pet) {
        final long close = pet.getPetClose();
        return BigDecimalUtil.decimalScaleObjectDown(2, Math.min(0.01 * Math.pow((double)close, 0.3) + 0.002 * Math.pow((double)close, 0.66), 12.0) * 10.0 / 1000.0);
    }
    
    public static double countPetCloseFury(final Pet pet) {
        final long close = pet.getPetClose();
        return BigDecimalUtil.decimalScaleObjectDown(2, Math.min(0.0182 * Math.pow((double)close, 0.3) + 0.01 * Math.pow((double)close, 0.5), 8.0) * 10.0 / 1000.0);
    }
    
    public static int countPetLife(final Pet pet, final List<PetEquip> list) {
        final int petLife = getPetLife(pet, list);
        final double petSpecialLife = getPetSpecialLife(pet);
        return BigDecimalUtil.decimalScaleIntegerDown(0, petLife + petSpecialLife);
    }
    
    public static double getPetBasicsLife(final Pet pet) {
        final PetBase petBase = pet.getPetBase();
        final Double grow_up = getPetGrowUp(pet, false);
        final Integer hp_base = countPetFlyStarter(pet.getFly(), pet.getFlyStarter(), petBase.getHp_base(), 1);
        return hp_base * pet.getPetLv() * grow_up * 0.7 + pet.getGg() * grow_up * pet.getPetLv() + hp_base;
    }
    
    public static double getPetGrowUp(final Pet pet, final boolean flag) {
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        double baseGrowUp = pet.getPetBase().getGrow_up();
        if (flag && sysPet != null) {
            baseGrowUp = sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1];
        }
        Double growUp = baseGrowUp / 1000.0 + countRestGrowUp(pet);
        if (pet.getGrowthDan() > 0 && sysPet != null) {
            growUp += sysPet.getGrowUpH();
        }
        return growUp;
    }
    
    public static int getPetLife(final Pet pet, final List<PetEquip> list) {
        double life = getPetBasicsLife(pet);
        final NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv(), pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        final double skillLifeCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()));
        final double hosrseLifePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        final double equipLifeCap = gainPro(PRO_DICT_TYPE.HP_CAP.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial(), list);
        final double eqPct = gainPro(PRO_DICT_TYPE.HP_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial(), list) / 1000.0;
        life += skillLifeCap + equipLifeCap;
        final double hp = life * (1.0 + hosrseLifePct) * (1.0 + eqPct);
        final double hosrseLifePctLast = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_LAST.getValue());
        return BigDecimalUtil.decimalScaleIntegerDown(0, hp + hp * hosrseLifePctLast);
    }
    
    public static double getPetSpecialLife(final Pet pet) {
        final double horsePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_CAP.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_ZYFY.getValue());
        return horsePct;
    }
    
    public static int countPetMana(final Pet pet, final List<PetEquip> list) {
        final int mana = getPetMana(pet, list);
        final double petSpecialMana = getPetSpecialMana(pet);
        return BigDecimalUtil.decimalScaleIntegerDown(0, mana + petSpecialMana);
    }
    
    public static double getPetBasicsMana(final Pet pet) {
        final PetBase petBase = pet.getPetBase();
        final Double grow_up = getPetGrowUp(pet, false);
        final Integer mp_base = countPetFlyStarter(pet.getFly(), pet.getFlyStarter(), petBase.getMp_base(), 2);
        return mp_base * pet.getPetLv() * grow_up * 0.7 + pet.getLx() * grow_up * pet.getPetLv() + mp_base;
    }
    
    public static int getPetMana(final Pet pet, final List<PetEquip> list) {
        double mana = getPetBasicsMana(pet);
        final NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv(), pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        final double skillManaCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()));
        final double horseManaPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.MP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        final double manaCap = gainPro(PRO_DICT_TYPE.MP_CAP.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial(), list);
        final double eqPct = gainPro(PRO_DICT_TYPE.MP_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial(), list) / 1000.0;
        mana += skillManaCap + manaCap;
        final double mp = mana * (1.0 + horseManaPct) * (1.0 + eqPct);
        return BigDecimalUtil.decimalScaleIntegerDown(0, mp);
    }
    
    public static double getPetSpecialMana(final Pet pet) {
        final double horsePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.MP_CAP.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_ZYFY.getValue());
        return horsePct;
    }
    
    public static int countPetPhyPower(final Pet pet, final List<PetEquip> list) {
        final PetBase petBase = pet.getPetBase();
        final Double grow_up = getPetGrowUp(pet, false);
        final Integer ap_base = countPetFlyStarter(pet.getFly(), pet.getFlyStarter(), petBase.getAp_base(), 3);
        final double phy = (ap_base * pet.getPetLv() * grow_up * 0.7 + pet.getLl() * grow_up * pet.getPetLv()) / 5.0 + ap_base;
        final NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv(), pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        final double skillAttackCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist()));
        final int horseAttackCap = HorseSkillUtils.getHorseSkillProperty(pet, PRO_DICT_TYPE.ATTACK.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        final double horseAttackPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.ATTACK_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        final double atpCap = gainPro(PRO_DICT_TYPE.ATTACK.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial(), list);
        final double atkEqPct = gainPro(PRO_DICT_TYPE.ATTACK_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial(), list) / 1000.0;
        final double attack = phy + skillAttackCap + horseAttackCap + atpCap;
        return BigDecimalUtil.decimalScaleIntegerDown(0, attack * (1.0 + horseAttackPct) * (1.0 + atkEqPct));
    }
    
    public static int countPetSpeed(final Pet pet, final boolean flag) {
        Double speed = 0.0;
        final PetBase petBase = pet.getPetBase();
        final Double grow_up = getPetGrowUp(pet, false);
        final int base = countPetFlyStarter(pet.getFly(), pet.getFlyStarter(), petBase.getSp_base(), 4);
        speed = base + pet.getMj() * grow_up;
        final NutMap nutMap = countSkillBookGain(new Addition(pet.getPetLv(), pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        final double skillSpeed = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
        final int horseAttackCap = HorseSkillUtils.getHorseSkillProperty(pet, PRO_DICT_TYPE.SPEED.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        final double horseSpPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.SPEED_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        final int awakenSpeedCap = AwakenSkillUtils.getAwakenSpeed(pet);
        final int acsryPetSpCap = AcsryPetSkillUtil.getAttrSpeed(pet);
        final double acsryPetSpPct = AcsryPetSkillUtil.getAttrSpeedPct(pet);
        speed += skillSpeed + awakenSpeedCap + horseAttackCap + acsryPetSpCap;
        int sp = BigDecimalUtil.decimalScaleIntegerDown(0, speed * (1.0 + horseSpPct) * (1.0 + acsryPetSpPct));
        if (flag) {
            sp = hypersensitivity(sp);
        }
        return sp;
    }
    
    public static double countRestGrowUp(final Pet pet) {
        double num = 0.0;
        num += 0.1 * pet.getRebirth();
        if (pet.getBone() > 0) {
            final SysDataStuff item = ItemDataPool.data_stuff.get(75025);
            if (item != null) {
                num += item.getValue() / 1000.0 * pet.getBone();
            }
        }
        final NutMap tianFuMap = TianFuUtils.getWearInstantlyTakeEffect(pet);
        if (tianFuMap != null) {
            final double tianFuProperty = tianFuMap.getDouble(String.valueOf(PRO_DICT_TYPE.CZL.getDist()), 0.0);
            if (tianFuProperty != 0.0) {
                num += tianFuProperty;
            }
        }
        num += Const.PET_FLY[pet.getFly()];
        return num;
    }
    
    public static NutMap countSkillBookGain(final Addition addition) {
        final NutMap map = new NutMap();
        for (final RoleSkill skill : addition.getSkills()) {
            if (skill.getSkillId() <= 0) {
                continue;
            }
            final SkillProcessor processor = SkillProcessor.queryGainProcessor(skill.getSkillId());
            if (processor == null) {
                continue;
            }
            final NutMap nutMap = processor.addition(addition);
            if (nutMap == null) {
                continue;
            }
            for (final String key : nutMap.keySet()) {
                map.setv(key, map.getDouble(key) + nutMap.getDouble(key));
            }
        }
        return map;
    }
    
    public static int countPetFlyStarter(final int fly, final int starter, int base, final int type) {
        if (fly < 3 || starter == 0) {
            return base;
        }
        if (type == 1 && starter == 1) {
            base += 60;
        }
        else if (type == 2 && starter == 2) {
            base += 60;
        }
        else if (type == 3 && starter == 3) {
            base += 60;
        }
        else if (type == 4 && starter == 4) {
            if (base < 0) {
                base -= 60;
            }
            else {
                base += 60;
            }
        }
        return base;
    }
    
    public static NutMap proFieldValues(final Pet pet) {
        final NutMap nutMap = new NutMap();
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        if (sysPet != null) {
            final Map<Integer, Double> correctionMap = ConvertDataPool.defPetCorrectionMap;
            final Map<Integer, Integer> map = pet.getCorrection();
            for (final Integer key : map.keySet()) {
                final int serial = DataGlobalFunc.GetProDictType(key);
                final Integer num = map.get(key);
                if (key != PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()) {
                    double close_gain = 0.0;
                    if (PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro() == key) {
                        close_gain = countPetCloseDoubleHit(pet);
                    }
                    else if (PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro() == key) {
                        close_gain = countPetCloseFury(pet);
                    }
                    else if (PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro() == key) {
                        close_gain = 0.8;
                    }
                    final Double gain = correctionMap.get(key);
                    nutMap.setnx(String.valueOf(serial), (Object)(close_gain + num * ((gain == null) ? 0.0 : gain)));
                }
                else {
                    nutMap.setnx(String.valueOf(serial), (Object)(num + 3));
                }
            }
        }
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), (Object)sysPet.getWxJin());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), (Object)sysPet.getWxMu());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), (Object)sysPet.getWxShui());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), (Object)sysPet.getWxHuo());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), (Object)sysPet.getWxTu());
        final NutMap skillBookGainNutMap = countSkillBookGain(new Addition(pet.getPetLv(), pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), nutMap));
        for (final String key2 : skillBookGainNutMap.keySet()) {
            if (!key2.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) && !key2.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) && !key2.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) && !key2.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) && !key2.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) && !key2.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) && !key2.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()))) {
                if (key2.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()))) {
                    continue;
                }
                nutMap.setv(key2, (Object)(nutMap.getDouble(key2) + skillBookGainNutMap.getDouble(key2)));
            }
        }
        return nutMap;
    }
    
    public static void restProFieldValue(final Role role, final Pet pet, final NutMap nutMap, final Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
        final NutMap additionAwaken = AwakenSkillUtils.addition(role, pet);
        if (additionAwaken != null && additionAwaken.size() > 0) {
            for (final Map.Entry<String, Object> entry : additionAwaken.entrySet()) {
                nutMap.setv((String)entry.getKey(), (Object)(nutMap.getDouble((String)entry.getKey()) + additionAwaken.getDouble((String)entry.getKey())));
            }
        }
        AcsryPetSkillUtil.addPetAcsry(role, pet, nutMap, acsryPetSkillMap);
    }
    
    public static NutMap countPro(final Role role) {
        final NutMap nutMap = new NutMap();
        final Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
        final Map<Integer, Double> faction = FACTION_PRO_DICT.toMap();
        final Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        final NutMap tianFuMap = TianFuUtils.countFuPlayerBookGain(role);
        final NutMap acsryMap = AcsrySkillUtil.initializeFightObject(role);
        final List<WearEquip> equips = SpringUtils.getBean(EquipService.class).getEquipValid(role, false);
        for (final Integer key : map.keySet()) {
            final Integer type = map.get(key);
            final double pro_value = cuntPro(equips, key, type);
            final double growPro = costRaceGrowUp(role, key);
            final double correction = countCorrection(role, key + PRO_DICT_SERIAL.HERO_RELIVE.getSerial(), type, reiEnMap);
            final Integer factionDist = key + PRO_DICT_SERIAL.HERO_FACTION.getSerial();
            final double factionGain = countPartyPractice(role, factionDist, faction);
            final double cardPro = countCardPtc(role, key, type);
            double count = pro_value + growPro + correction + factionGain + cardPro;
            final double tianFuProperty = TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(key), count);
            count += tianFuProperty;
            final double acsryProperty = acsryMap.getDouble(String.valueOf(key), 0.0);
            count += acsryProperty;
            nutMap.setv(key.toString(), (Object)count);
        }
        return costProLimit(nutMap, role.getRace(), role.getRelive(), role.getRest().getParty(), tianFuMap, acsryMap);
    }
    
    public static double cuntPro(final List<WearEquip> list, final Integer dict, final int type) {
        double pro = 0.0;
        final double gainPro = gainPro(list, dict);
        if (type == PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()) {
            pro = BigDecimalUtil.decimalFormat("#.###", gainPro / 1000.0);
        }
        else {
            pro = BigDecimalUtil.decimalScaleIntegerDown(3, gainPro);
        }
        return pro;
    }
    
    public static double gainPro(final Integer dist, final List<PetEquip> list) {
        double pro = 0.0;
        for (final PetEquip equip : list) {
            final FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(dist)).findFirst().orElse(null);
            if (fiedEquip != null) {
                pro += fiedEquip.getValue();
            }
        }
        return pro;
    }
    
    public static double gainPro(final List<WearEquip> list, final Integer dist) {
        double pro = 0.0;
        for (final WearEquip wearEquip : list) {
            final double count = countEquipPro(wearEquip, dist);
            pro += count;
        }
        return pro;
    }
    
    public static double costRaceGrowUp(final Role role, final Integer dist) {
        double value = 0.0;
        int race_ren_Lv = 0;
        int race_mo_Lv = 0;
        int race_xian_Lv = 0;
        int race_gui_Lv = 0;
        int race_long_Lv = 0;
        if (role.getRace().equals(RACE.RACE_REN.getValue())) {
            race_ren_Lv = (int)Math.min(role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(RACE.RACE_MO.getValue())) {
            race_mo_Lv = (int)Math.min(role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(RACE.RACE_XIAN.getValue())) {
            race_xian_Lv = (int)Math.min(role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(RACE.RACE_GUI.getValue())) {
            race_gui_Lv = (int)Math.min(role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(RACE.RACE_LONG.getValue())) {
            race_long_Lv = (int)Math.min(role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        if (PRO_DICT_TYPE.K_FY.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_ren_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 8.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 8.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_long_Lv / 8.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_HL.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_ren_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 8.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 6.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_long_Lv / 6.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_HS.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_ren_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 8.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 6.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_long_Lv / 6.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_DU.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_ren_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 8.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 6.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_long_Lv / 6.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_H.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_xian_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 12.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", ~(race_gui_Lv - 1) / 8.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_S.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_xian_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 12.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", ~(race_gui_Lv - 1) / 8.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_F.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_xian_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 12.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", ~(race_gui_Lv - 1) / 8.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_L.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_xian_Lv / 4.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 12.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", ~(race_gui_Lv - 1) / 8.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.WL_XS.getDist() == dist) {
            double variable = 6.0;
            if (role.getRace().equals(RACE.RACE_MO.getValue())) {
                variable = 8.0;
            }
            value = BigDecimalUtil.doubleFormat("#.###", race_mo_Lv / 12.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_long_Lv / variable / 100.0);
        }
        else if (PRO_DICT_TYPE.K_GH.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 6.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_YW.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 6.0 / 100.0) + BigDecimalUtil.doubleFormat("#.###", race_long_Lv / 6.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.K_SS.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 6.0 / 100.0);
        }
        else if (PRO_DICT_TYPE.HIT_RATIO.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 12.0 / 100.0) + 0.8;
        }
        else if (PRO_DICT_TYPE.MISS_RATIO.getDist() == dist) {
            value = BigDecimalUtil.doubleFormat("#.###", race_gui_Lv / 4.0 / 100.0);
        }
        return value;
    }
    
    public static NutMap costProLimit(final NutMap nutMap, final int race, final int rei, final Map<Integer, Integer> map, final NutMap tMap, final NutMap acsryMap) {
        final String pmz_value = String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist());
        final double pmz = nutMap.getDouble(pmz_value);
        if (pmz > 0.0) {
            final double min = Math.min(ATTRCAP.MAX_PACC_NUM.getValue(), Math.max(ATTRCAP.MIN_PACC_NUM.getValue(), pmz));
            nutMap.setv(pmz_value, (Object)min);
        }
        final String lsb_value = String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist());
        final double lsb = nutMap.getDouble(lsb_value);
        if (lsb > 0.0) {
            final double min2 = Math.min(ATTRCAP.MAX_PSBL_NUM.getValue(), Math.max(ATTRCAP.MIN_PSBL_NUM.getValue(), lsb));
            nutMap.setv(lsb_value, (Object)min2);
        }
        final String slsb_value = String.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
        final double slsb = nutMap.getDouble(slsb_value);
        if (slsb > 0.0) {
            final double min3 = Math.min(ATTRCAP.MAX_PSBL_NUM.getValue(), Math.max(ATTRCAP.MIN_PSBL_NUM.getValue(), slsb));
            nutMap.setv(slsb_value, (Object)min3);
        }
        final String lljt_value = String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist());
        final double lljt = nutMap.getDouble(lljt_value);
        if (lljt > 0.0) {
            final double min4 = Math.min(ATTRCAP.MAX_PLJTIMES_NUM.getValue(), Math.max(ATTRCAP.MIN_PLJTIMES_NUM.getValue(), lljt));
            nutMap.setv(lljt_value, (Object)min4);
        }
        final String lftl_value = String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist());
        final double lftl = nutMap.getDouble(lftl_value);
        if (lftl > 0.0) {
            final double min5 = Math.min(ATTRCAP.MAX_FTLV_NUM.getValue(), Math.max(ATTRCAP.MIN_FTLV_NUM.getValue(), lftl));
            nutMap.setv(lftl_value, (Object)min5);
        }
        final int gain_dist = FACTION_PRO_DICT.K_KZ_CAP.getPro();
        final double gain = FACTION_PRO_DICT.K_KZ_CAP.getGain();
        double maxRes = ATTRCAP.MAX_RES_HERO_ELSE_VALUE.getValue();
        if (race > 0) {
            if (race == RACE.RACE_REN.getValue()) {
                maxRes = ATTRCAP.MAX_RES_HERO_REN_VALUE.getValue();
            }
            else if (race == RACE.RACE_LONG.getValue()) {
                maxRes = ATTRCAP.MAX_RES_HERO_LONG_VALUE.getValue();
            }
        }
        final String kfy = String.valueOf(PRO_DICT_TYPE.K_FY.getDist());
        final double kfyValue = nutMap.getDouble(kfy);
        Integer count = map.get(gain_dist);
        count = ((count == null) ? 0 : count);
        final double gainRes = count * gain;
        double endValue = maxRes + gainRes;
        final double tianFuRes = endValue * tMap.getDouble(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, 0.0);
        final double acsryRes = endValue * acsryMap.getDouble(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, 0.0);
        endValue += tianFuRes;
        endValue += acsryRes;
        final double min6 = Math.min(kfyValue, endValue);
        nutMap.setv(kfy, (Object)min6);
        final String khs = String.valueOf(PRO_DICT_TYPE.K_HS.getDist());
        final double khsValue = nutMap.getDouble(khs);
        Integer count2 = map.get(gain_dist);
        count2 = ((count2 == null) ? 0 : count2);
        final double gainRes2 = count2 * gain;
        double endValue2 = maxRes + gainRes2;
        final double tianFuRes2 = endValue2 * tMap.getDouble(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, 0.0);
        final double acsryRes2 = endValue2 * acsryMap.getDouble(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, 0.0);
        endValue2 += tianFuRes2;
        endValue2 += acsryRes2;
        final double min7 = Math.min(khsValue, endValue2);
        nutMap.setv(khs, (Object)min7);
        final String khl = String.valueOf(PRO_DICT_TYPE.K_HL.getDist());
        final double khlValue = nutMap.getDouble(khl);
        Integer count3 = map.get(gain_dist);
        count3 = ((count3 == null) ? 0 : count3);
        final double gainRes3 = count3 * gain;
        double endValue3 = maxRes + gainRes3;
        final double tianFuRes3 = endValue3 * tMap.getDouble(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, 0.0);
        final double acsryRes3 = endValue3 * acsryMap.getDouble(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, 0.0);
        endValue3 += tianFuRes3;
        endValue3 += acsryRes3;
        final double min8 = Math.min(khlValue, endValue3);
        nutMap.setv(khl, (Object)min8);
        final String kyw = String.valueOf(PRO_DICT_TYPE.K_YW.getDist());
        final double kywValue = nutMap.getDouble(kyw);
        Integer count4 = map.get(gain_dist);
        count4 = ((count4 == null) ? 0 : count4);
        final double gainRes4 = count4 * gain;
        double endValue4 = maxRes + gainRes4;
        final double tianFuRes4 = endValue4 * tMap.getDouble(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, 0.0);
        final double acsryRes4 = endValue4 * acsryMap.getDouble(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, 0.0);
        endValue4 += tianFuRes4;
        endValue4 += acsryRes4;
        final double min9 = Math.min(kywValue, endValue4);
        nutMap.setv(kyw, (Object)min9);
        final double max = reiProTmAndQmMax(rei);
        final String ktm = String.valueOf(PRO_DICT_TYPE.K_TM.getDist());
        final double ktmValue = nutMap.getDouble(ktm);
        if (ktmValue > 0.0) {
            final double min10 = Math.min(ktmValue, max);
            nutMap.setv(ktm, (Object)min10);
        }
        final String kqm = String.valueOf(PRO_DICT_TYPE.K_QM.getDist());
        final double kqmValue = nutMap.getDouble(kqm);
        if (kqmValue > 0.0) {
            final double min11 = Math.min(kqmValue, max);
            nutMap.setv(kqm, (Object)min11);
        }
        final double fkAndXlMax = reiProFkAndXlMax(rei);
        final String kfk = String.valueOf(PRO_DICT_TYPE.K_FG.getDist());
        final double kfkValue = nutMap.getDouble(kfk);
        if (kfkValue > 0.0) {
            final double min12 = Math.min(kfkValue, fkAndXlMax);
            nutMap.setv(kfk, (Object)min12);
        }
        final String kxl = String.valueOf(PRO_DICT_TYPE.K_XL.getDist());
        final double kxlValue = nutMap.getDouble(kxl);
        if (kxlValue > 0.0) {
            final double min13 = Math.min(kxlValue, fkAndXlMax);
            nutMap.setv(kxl, (Object)min13);
        }
        return nutMap;
    }
    
    public static double reiProTmAndQmMax(final int rei) {
        double max = 0.25;
        if (rei == 1) {
            max = 0.3;
        }
        else if (rei == 2) {
            max = 0.35;
        }
        else if (rei >= 3) {
            max = 0.4;
        }
        return max;
    }
    
    public static double reiProFkAndXlMax(final int rei) {
        double max = 0.5;
        if (rei == 1) {
            max = 0.6;
        }
        else if (rei == 2) {
            max = 0.7;
        }
        else if (rei >= 3) {
            max = 0.8;
        }
        return max;
    }
    
    public static Effect countHurt(final Fight fight, final FightObject attacker, final FightObject target, double rdk, final double oriparam) {
        PassiveSkillUtils.beforeCountHurt(fight, attacker, target);
        final Effect effect = new Effect(1, 0);
        int damageHp = 0;
        damageHp += getPhyAttack(attacker);
        double phyRes = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
        final double penetrate = attacker.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_RATIO.getDist());
        if (SkillAi.canAffectOnPro(penetrate)) {
            effect.setType(101);
            final double sub_deep = attacker.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_EXTENT.getDist());
            phyRes -= sub_deep;
        }
        if (oriparam > 1.0) {
            damageHp = BigDecimalUtil.decimalScaleIntegerDown(0, damageHp * oriparam);
        }
        final double weak = attacker.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        damageHp = BigDecimalUtil.decimalScaleIntegerDown(0, damageHp * weak + (damageHp - damageHp * phyRes));
        if (target.getType() == 1) {
            damageHp = BigDecimalUtil.decimalScaleIntegerDown(0, damageHp * Math.pow(0.5, 0.99));
        }
        if (rdk <= 0.0) {
            rdk = RandomUtil.round(95, 105) / 100.0;
        }
        final int hurt = BigDecimalUtil.decimalScaleIntegerDown(0, damageHp * rdk);
        effect.setHit(rdk);
        effect.setHurt(Math.max(1, hurt));
        ImbueUtil.clearTemps(attacker.getImbueMap());
        return effect;
    }
    
    public static int getPhyAttack(final FightObject attacker) {
        int damageHp = attacker.getPhyPower() + (int)(attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * attacker.getPhyPower());
        damageHp += damageHp * (int)attacker.getSelfAllTempPro(PRO_DICT_TYPE.ATTACK_ADDITION.getDist());
        return damageHp;
    }
    
    public static int countPartnerSpeed(final Partner partner) {
        final Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType());
        final int level = partner.getPartnerLv() + partner.getRebirth() * 1000;
        final SysPartnerProperty sysPartnerProperty = shortSysPartnerPropertyMap.get((short)level);
        if (sysPartnerProperty != null) {
            final int speed = sysPartnerProperty.getSpeed();
            return speed;
        }
        return 0;
    }
    
    public static int hypersensitivity(final int baseSpeed) {
        if (baseSpeed != 0) {
            final int absSpeed = Math.abs(baseSpeed);
            final double limitMin = Math.min(300.0 / absSpeed, 0.3);
            final double limitMax = 0.0;
            final double random = RandomUtil.getRandomDouble(limitMax, limitMin);
            final double randomNum = BigDecimalUtil.decimalScaleObjectDown(3, random);
            final double doubleSpeed = baseSpeed * (1.0 - randomNum * absSpeed / absSpeed);
            final int returnSpeed = BigDecimalUtil.decimalScaleIntegerDown(0, doubleSpeed);
            return returnSpeed;
        }
        return baseSpeed;
    }
}
