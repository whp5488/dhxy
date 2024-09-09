package com.kitty.system.game.fightnew.service;

import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.enums.war.itempro.mind.MIND;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.enums.war.wardef.ATTRCAP;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.train.TrainService;
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
import com.kitty.system.game.mind.domain.Mind;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.List;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.ZSXZ_PRO_DICT;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.game.fightnew.book.mind.utils.MindSkillUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.mind.service.MindService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.domain.Role;

public class CountUtils
{
    public CountUtils() {
    }
    
    public static int countLife(Role role) {
        List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
        List<Mind> mindList = ((MindService)SpringUtils.getBean(MindService.class)).getMind((long)role.getId());
        double life = countBasicLife(role, equips);
        double lifeCap = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()));
        double mindCap = MindSkillUtil.countMindProp(role, mindList, PRO_DICT_TYPE.HP_CAP.getDist(), 0.0);
        double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) / 1000.0;
        double cardPct = countCardPtc(role, PRO_DICT_TYPE.HP_PCT.getDist(), PRO_DICT_TYPE.HP_PCT.getType());
        life = life + lifeCap + mindCap;
        Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        double revise_hp = countCorrection(role, Integer.valueOf(ZSXZ_PRO_DICT.HP_PCT.getPro()), (int)DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.HP_PCT.getPro()), reiEnMap);
        NutMap runeMap = TianFuUtils.getWearInstantlyTakeEffect(role);
        double runeCap = 0.0;
        if (runeMap != null) {
            runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()));
        }
        NutMap acsryMap = AcsrySkillUtil.wearTakeEffect(role);
        double acsryCap = 0.0;
        if (acsryMap != null) {
            acsryCap = acsryMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist()), 0.0);
        }
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(life * (1.0 + eqPct + acsryCap) * (1.0 + cardPct) * (1.0 + runeCap) * (1.0 + revise_hp)));
    }
    
    public static double countBasicLife(Role role, List<WearEquip> equips) {
        Double hp = Double.valueOf(0.0);
        SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            double gg = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.GG.getDist()));
            Integer lv = role.getLevel();
            hp = Double.valueOf((double)(int)hero.getHp() + ((double)(int)role.getGg() + gg) * BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(int)lv + (2000.0 - (double)(int)lv * 20.0) / 100.0)) * (1.0 + (double)Double.valueOf(hero.getGGg())));
        }
        return (double)hp;
    }
    
    public static int countMana(Role role) {
        List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
        List<Mind> mindList = ((MindService)SpringUtils.getBean(MindService.class)).getMind((long)role.getId());
        double mana = (double)countBasicMana(role, equips);
        double manaCap = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()));
        double mindCap = MindSkillUtil.countMindProp(role, mindList, PRO_DICT_TYPE.MP_CAP.getDist(), 0.0);
        double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) / 1000.0;
        double cardPct = countCardPtc(role, PRO_DICT_TYPE.MP_PCT.getDist(), PRO_DICT_TYPE.MP_PCT.getType());
        mana = mana + manaCap + mindCap;
        Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        double revise_mp = countCorrection(role, Integer.valueOf(ZSXZ_PRO_DICT.MP_PCT.getPro()), (int)DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.MP_PCT.getPro()), reiEnMap);
        double runeCap = 0.0;
        NutMap runeMap = TianFuUtils.getWearInstantlyTakeEffect(role);
        if (runeMap != null) {
            runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()));
        }
        NutMap acsryMap = AcsrySkillUtil.wearTakeEffect(role);
        double acsryCap = 0.0;
        if (acsryMap != null) {
            acsryCap = acsryMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist()), 0.0);
        }
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(mana * (1.0 + eqPct + acsryCap) * (1.0 + cardPct) * (1.0 + runeCap) * (1.0 + revise_mp)));
    }
    
    public static Double countBasicMana(Role role, List<WearEquip> equips) {
        Double mp = Double.valueOf(0.0);
        SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            double lx = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.LX.getDist()));
            Integer lv = role.getLevel();
            mp = Double.valueOf((double)(int)hero.getMp() + ((double)(int)role.getLx() + lx) * BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(int)lv + (2000.0 - (double)(int)lv * 20.0) / 100.0)) * (1.0 + (double)Double.valueOf(hero.getGLx())));
            return mp;
        }
        return mp;
    }
    
    public static int countSpeed(Role role) {
        List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
        double speed = countBasicSpeed(role, equips);
        double speedPro = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
        double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist())) / 1000.0;
        double cardPct = countCardPtc(role, PRO_DICT_TYPE.SPEED_PCT.getDist(), PRO_DICT_TYPE.SPEED_PCT.getType());
        speed += speedPro;
        Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        double revise_sp = countCorrection(role, Integer.valueOf(ZSXZ_PRO_DICT.SPEED_PCT.getPro()), (int)DataGlobalFunc.GetProType(ZSXZ_PRO_DICT.SPEED_PCT.getPro()), reiEnMap);
        double runeCap = 0.0;
        NutMap runeMap = TianFuUtils.countFuPlayerBookGain(role);
        if (runeMap != null) {
            runeCap = runeMap.getDouble(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()), 0.0);
        }
        int sp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(speed * (1.0 + eqPct) * (1.0 + cardPct) * (1.0 + runeCap) * (1.0 + revise_sp)));
        sp = hypersensitivity(sp);
        return sp;
    }
    
    public static double countBasicSpeed(Role role, List<WearEquip> equips) {
        Double speed = Double.valueOf(0.0);
        SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            double mj = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.MJ.getDist()));
            speed = Double.valueOf((double)(int)hero.getSp() + ((double)(int)role.getMj() + mj) * (1.0 + (double)Double.valueOf(hero.getGMj())));
        }
        return (double)speed;
    }
    
    public static int countPhyPower(Role role) {
        List<WearEquip> equips = ((EquipService)SpringUtils.getBean(EquipService.class)).getEquipValid(role, false);
        double atk = countBasicPhyPower(role, equips);
        double powerAtk = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.ATTACK.getDist()));
        double eqPct = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist())) / 1000.0;
        double cardPct = countCardPtc(role, PRO_DICT_TYPE.ATTACK_PCT.getDist(), PRO_DICT_TYPE.ATTACK_PCT.getType());
        atk += powerAtk;
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(atk * (1.0 + eqPct) * (1.0 + cardPct)));
    }
    
    public static double countBasicPhyPower(Role role, List<WearEquip> equips) {
        Double phy = Double.valueOf(0.0);
        SysHero hero = (SysHero)PartDataPool.heroMap.get(role.getSerial());
        if (hero != null) {
            double ll = gainPro(equips, Integer.valueOf(PRO_DICT_TYPE.LL.getDist()));
            Integer lv = role.getLevel();
            phy = Double.valueOf((double)(int)hero.getAp() + ((double)(int)role.getLl() + ll) * ((double)(int)lv + (2000.0 - (double)(int)lv * 20.0) / 100.0) / 5.0 * (1.0 + (double)Double.valueOf(hero.getGLl())));
        }
        return (double)phy;
    }
    
    public static double countExtraPro(Role role, Integer pro, Long id) {
        double value = 0.0;
        try {
            List<WearEquip> wearEquips = SpringUtils.getEquipService().selectWearEquip(role);
            for (int i = 0; i < wearEquips.size(); ++i) {
                WearEquip wearEquip = (WearEquip)wearEquips.get(i);
                if (wearEquip != null && wearEquip.getId() != null && !wearEquip.getId().equals(id)) {
                    value += countEquipPro(wearEquip, pro);
                }
            }
        }
        catch (ConcurrentModificationException e) {
            LoggerUtils.error("装备属性 = {}", e);
        }
        return value;
    }
    
    public static double countEquipPro(WearEquip wearEquip, Integer dist) {
        double base = 0.0;
        Integer distPro = Integer.valueOf((int)dist + PRO_DICT_SERIAL.EQUIP_PRO.getSerial());
        Integer distPsPro = Integer.valueOf((int)dist + PRO_DICT_SERIAL.PS.getSerial());
        Integer distLh = Integer.valueOf((int)dist + PRO_DICT_SERIAL.EQUIP_PRO_LH.getSerial());
        int bn = 0;
        Optional<FiedEquip> first = wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6670))).findFirst();
        if (first.isPresent()) {
            FiedEquip fiedEquip = (FiedEquip)first.get();
            bn = (int)fiedEquip.getValue();
        }
        int dur = 0;
        Optional<FiedEquip> firstDur = wearEquip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6672))).findFirst();
        if (firstDur.isPresent()) {
            FiedEquip fiedEquip2 = (FiedEquip)firstDur.get();
            dur = (int)fiedEquip2.getValue();
        }
        if (dur > 0) {
            List<Integer> gemList = ZB_PRO_BASE_DICT.toList();
            for (FiedEquip fiedEquip3 : wearEquip.getField()) {
                if (fiedEquip3.getType().equals(distPro) || fiedEquip3.getType().equals(distPsPro)) {
                    if (bn > 0 && gemList.contains(dist)) {
                        base += (double)fiedEquip3.getValue() + (double)(int)fiedEquip3.getValue() * (0.03 * (double)bn);
                    }
                    else {
                        base += (double)fiedEquip3.getValue();
                    }
                }
                else {
                    continue;
                }
            }
            for (FiedEquip fiedEquip3 : wearEquip.getArtifice()) {
                if (fiedEquip3.getType().equals(distLh)) {
                    base += (double)fiedEquip3.getValue();
                }
                else {
                    continue;
                }
            }
        }
        return base;
    }
    
    public static double countCardPtc(Role role, int key, int type) {
        if (key <= 0) {
            return 0.0;
        }
        int masquerade = role.getMasquerade();
        int cardExpireTime = role.getCardExpireTime();
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (masquerade > 0 && cardExpireTime - 1 > currTime) {
            SysDataCard card = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(masquerade));
            if (card == null) {
                return 0.0;
            }
            double five = card.getFiveAttr().getDouble(String.valueOf(key), 0.0);
            if (five > 0.0) {
                return five;
            }
            int base = card.getBaseAttr().getInt(String.valueOf(key), 0);
            if (base == 0) {
                return 0.0;
            }
            if (type == PRO_TYPE.Pro_Value_NUM_TYPE.getValue()) {
                return (double)base;
            }
            CardExercise cardExercise = ((CardService)SpringUtils.getBean(CardService.class)).getCardExercise(role);
            double wxIncrease = getWxIncrease(cardExercise, card);
            double anInt = (double)base / 1000.0 * (1.0 + wxIncrease);
            return anInt;
        }
        else {
            return 0.0;
        }
    }
    
    public static double getWxIncrease(CardExercise cardExercise, SysDataCard card) {
        double add = 0.0;
        int goldGrade = cardExercise.getGoldGrade();
        add += (double)(card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), 0) * goldGrade) * card.getFactor();
        int woodGrade = cardExercise.getWoodGrade();
        add += (double)(card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), 0) * woodGrade) * card.getFactor();
        int waterGrade = cardExercise.getWaterGrade();
        add += (double)(card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), 0) * waterGrade) * card.getFactor();
        int fireGrade = cardExercise.getFireGrade();
        add += (double)(card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), 0) * fireGrade) * card.getFactor();
        int soilGrade = cardExercise.getSoilGrade();
        add += (double)(card.getFiveAttr().getInt(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), 0) * soilGrade) * card.getFactor();
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), Double.valueOf(add * 100.0 / 10000.0 / 100.0));
    }
    
    public static double countCorrection(Role role, Integer dict, int type, Map<Integer, SysReiEn> reiEnMap) {
        List<Integer> toList = ZSXZ_PRO_DICT.toList();
        double revise_data = 0.0;
        if (toList.contains(dict)) {
            for (int i = 0; i <= (int)role.getRelive(); ++i) {
                Integer serial = (Integer)role.getRest().getCorrection().get(Integer.valueOf(i));
                if (serial != null && (int)serial > 0) {
                    SysReiEn reiEn = (SysReiEn)reiEnMap.get(serial);
                    if (reiEn != null) {
                        int rei = i;
                        SysReiEn sysReiEn = (SysReiEn)reiEnMap.values().stream().filter(e -> e.getRei() == rei && e.getKey() == reiEn.getRace() * 2 + reiEn.getSex() - 2).findFirst().orElse(null);
                        for (Field field : sysReiEn.getReiEn()) {
                            if (field.getKey().equals(dict)) {
                                if (type == PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()) {
                                    revise_data += Double.parseDouble(BigDecimalUtil.divide(String.valueOf(field.getValue()), String.valueOf(100), 3));
                                }
                                else {
                                    revise_data += (double)field.getValue();
                                }
                            }
                            else {
                                continue;
                            }
                        }
                    }
                }
            }
        }
        return revise_data;
    }
    
    public static double countPartyPractice(Role role, Integer dict, Map<Integer, Double> map) {
        Double revise_data = Double.valueOf(0.0);
        Double gain = (Double)map.get(dict);
        Integer count = (Integer)role.getRest().getParty().get(dict);
        if (count != null && gain != null) {
            revise_data = Double.valueOf((double)(int)count * (double)gain);
        }
        return (double)revise_data;
    }
    
    public static double countPetCloseDoubleHit(Pet pet) {
        long close = pet.getPetClose();
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(Math.min(0.01 * Math.pow((double)close, 0.3) + 0.002 * Math.pow((double)close, 0.66), 12.0) * 10.0 / 1000.0));
    }
    
    public static double countPetCloseFury(Pet pet) {
        long close = pet.getPetClose();
        return BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(2), Double.valueOf(Math.min(0.0182 * Math.pow((double)close, 0.3) + 0.01 * Math.pow((double)close, 0.5), 8.0) * 10.0 / 1000.0));
    }
    
    public static int countPetLife(Pet pet, List<PetEquip> list) {
        int petLife = getPetLife(pet, list);
        double petSpecialLife = getPetSpecialLife(pet);
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)petLife + petSpecialLife));
    }
    
    public static double getPetBasicsLife(Pet pet) {
        PetBase petBase = pet.getPetBase();
        Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
        Integer hp_base = Integer.valueOf(countPetFlyStarter((int)pet.getFly(), pet.getFlyStarter(), (int)petBase.getHp_base(), 1));
        return (double)((int)hp_base * (int)pet.getPetLv()) * (double)grow_up * 0.7 + (double)(int)pet.getGg() * (double)grow_up * (double)(int)pet.getPetLv() + (double)(int)hp_base;
    }
    
    public static double getPetGrowUp(Pet pet, boolean flag) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        double baseGrowUp = (double)(int)pet.getPetBase().getGrow_up();
        if (flag && sysPet != null) {
            baseGrowUp = (double)sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1];
        }
        Double growUp = Double.valueOf(baseGrowUp / 1000.0 + countRestGrowUp(pet));
        if ((int)pet.getGrowthDan() > 0 && sysPet != null) {
            growUp = Double.valueOf((double)growUp + sysPet.getGrowUpH());
        }
        return (double)growUp;
    }
    
    public static int getPetLife(Pet pet, List<PetEquip> list) {
        double life = getPetBasicsLife(pet);
        NutMap nutMap = countSkillBookGain(new Addition((int)pet.getPetLv(), (int)pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        double skillLifeCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist()));
        double hosrseLifePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.HP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        double equipLifeCap = gainPro(Integer.valueOf(PRO_DICT_TYPE.HP_CAP.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list);
        double eqPct = gainPro(Integer.valueOf(PRO_DICT_TYPE.HP_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list) / 1000.0;
        life += skillLifeCap + equipLifeCap;
        double hp = life * (1.0 + hosrseLifePct) * (1.0 + eqPct);
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
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)mana + petSpecialMana));
    }
    
    public static double getPetBasicsMana(Pet pet) {
        PetBase petBase = pet.getPetBase();
        Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
        Integer mp_base = Integer.valueOf(countPetFlyStarter((int)pet.getFly(), pet.getFlyStarter(), (int)petBase.getMp_base(), 2));
        return (double)((int)mp_base * (int)pet.getPetLv()) * (double)grow_up * 0.7 + (double)(int)pet.getLx() * (double)grow_up * (double)(int)pet.getPetLv() + (double)(int)mp_base;
    }
    
    public static int getPetMana(Pet pet, List<PetEquip> list) {
        double mana = getPetBasicsMana(pet);
        NutMap nutMap = countSkillBookGain(new Addition((int)pet.getPetLv(), (int)pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        double skillManaCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist()));
        double horseManaPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.MP_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        double manaCap = gainPro(Integer.valueOf(PRO_DICT_TYPE.MP_CAP.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list);
        double eqPct = gainPro(Integer.valueOf(PRO_DICT_TYPE.MP_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list) / 1000.0;
        mana += skillManaCap + manaCap;
        double mp = mana * (1.0 + horseManaPct) * (1.0 + eqPct);
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(mp));
    }
    
    public static double getPetSpecialMana(Pet pet) {
        double horsePct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.MP_CAP.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH_ZYFY.getValue());
        return horsePct;
    }
    
    public static int countPetPhyPower(Pet pet, List<PetEquip> list) {
        PetBase petBase = pet.getPetBase();
        Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
        Integer ap_base = Integer.valueOf(countPetFlyStarter((int)pet.getFly(), pet.getFlyStarter(), (int)petBase.getAp_base(), 3));
        double phy = ((double)((int)ap_base * (int)pet.getPetLv()) * (double)grow_up * 0.7 + (double)(int)pet.getLl() * (double)grow_up * (double)(int)pet.getPetLv()) / 5.0 + (double)(int)ap_base;
        NutMap nutMap = countSkillBookGain(new Addition((int)pet.getPetLv(), (int)pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        double skillAttackCap = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist()));
        int horseAttackCap = HorseSkillUtils.getHorseSkillProperty(pet, PRO_DICT_TYPE.ATTACK.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        double horseAttackPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.ATTACK_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        double atpCap = gainPro(Integer.valueOf(PRO_DICT_TYPE.ATTACK.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list);
        double atkEqPct = gainPro(Integer.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist() + PRO_DICT_SERIAL.PET_PS.getSerial()), list) / 1000.0;
        double attack = phy + skillAttackCap + (double)horseAttackCap + atpCap;
        return BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(attack * (1.0 + horseAttackPct) * (1.0 + atkEqPct)));
    }
    
    public static int countPetSpeed(Pet pet, boolean flag) {
        Double speed = Double.valueOf(0.0);
        PetBase petBase = pet.getPetBase();
        Double grow_up = Double.valueOf(getPetGrowUp(pet, false));
        int base = countPetFlyStarter((int)pet.getFly(), pet.getFlyStarter(), (int)petBase.getSp_base(), 4);
        speed = Double.valueOf((double)base + (double)(int)pet.getMj() * (double)grow_up);
        NutMap nutMap = countSkillBookGain(new Addition((int)pet.getPetLv(), (int)pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), proFieldValues(pet)));
        double skillSpeed = nutMap.getDouble(String.valueOf(PRO_DICT_TYPE.SPEED.getDist()));
        int horseAttackCap = HorseSkillUtils.getHorseSkillProperty(pet, PRO_DICT_TYPE.SPEED.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        double horseSpPct = HorseSkillUtils.getHorseSkillPropertyPct(pet, PRO_DICT_TYPE.SPEED_PCT.getDist(), HORSE_SKILL_TRIGGER_DICT.STUDY_FINISH.getValue());
        int awakenSpeedCap = AwakenSkillUtils.getAwakenSpeed(pet);
        int acsryPetSpCap = AcsryPetSkillUtil.getAttrSpeed(pet);
        double acsryPetSpPct = AcsryPetSkillUtil.getAttrSpeedPct(pet);
        speed = Double.valueOf((double)speed + (skillSpeed + (double)awakenSpeedCap + (double)horseAttackCap + (double)acsryPetSpCap));
        int sp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)speed * (1.0 + horseSpPct) * (1.0 + acsryPetSpPct)));
        if (flag) {
            sp = hypersensitivity(sp);
        }
        return sp;
    }
    
    public static double countRestGrowUp(Pet pet) {
        double num = 0.0;
        num += 0.1 * (double)(int)pet.getRebirth();
        if ((int)pet.getBone() > 0) {
            SysDataStuff item = (SysDataStuff)ItemDataPool.data_stuff.get(Integer.valueOf(75025));
            if (item != null) {
                num += (double)(int)item.getValue() / 1000.0 * (double)(int)pet.getBone();
            }
        }
        NutMap tianFuMap = TianFuUtils.getWearInstantlyTakeEffect(pet);
        if (tianFuMap != null) {
            double tianFuProperty = tianFuMap.getDouble(String.valueOf(PRO_DICT_TYPE.CZL.getDist()), 0.0);
            if (tianFuProperty != 0.0) {
                num += tianFuProperty;
            }
        }
        num += Const.PET_FLY[(int)pet.getFly()];
        return num;
    }
    
    public static NutMap countSkillBookGain(Addition addition) {
        NutMap map = new NutMap();
        for (RoleSkill skill : addition.getSkills()) {
            if ((int)skill.getSkillId() <= 0) {
                continue;
            }
            SkillProcessor processor = SkillProcessor.queryGainProcessor(skill.getSkillId());
            if (processor != null) {
                NutMap nutMap = processor.addition(addition, new Object[0]);
                if (nutMap != null) {
                    for (String key : nutMap.keySet()) {
                        map.setv(key, Double.valueOf(map.getDouble(key) + nutMap.getDouble(key)));
                    }
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return map;
    }
    
    public static int countPetFlyStarter(int fly, int starter, int base, int type) {
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
    
    public static NutMap proFieldValues(Pet pet) {
        NutMap nutMap = new NutMap();
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (sysPet != null) {
            Map<Integer, Double> correctionMap = ConvertDataPool.defPetCorrectionMap;
            Map<Integer, Integer> map = pet.getCorrection();
            for (Integer key : map.keySet()) {
                int serial = (int)DataGlobalFunc.GetProDictType((int)key);
                Integer num = (Integer)map.get(key);
                if ((int)key != PRACTICE_PET_PRO_DICT.COMBO_NUM.getPro()) {
                    double close_gain = 0.0;
                    if (PRACTICE_PET_PRO_DICT.COMBO_RATIO.getPro() == (int)key) {
                        close_gain = countPetCloseDoubleHit(pet);
                    }
                    else if (PRACTICE_PET_PRO_DICT.RAGE_RATIO.getPro() == (int)key) {
                        close_gain = countPetCloseFury(pet);
                    }
                    else if (PRACTICE_PET_PRO_DICT.HIT_RATIO.getPro() == (int)key) {
                        close_gain = 0.8;
                    }
                    Double gain = (Double)correctionMap.get(key);
                    nutMap.setnx(String.valueOf(serial), Double.valueOf(close_gain + (double)(int)num * ((gain == null) ? 0.0 : ((double)gain))));
                }
                else {
                    nutMap.setnx(String.valueOf(serial), Integer.valueOf((int)num + 3));
                }
            }
        }
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_J.getDist()), sysPet.getWxJin());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_M.getDist()), sysPet.getWxMu());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_S.getDist()), sysPet.getWxShui());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_H.getDist()), sysPet.getWxHuo());
        nutMap.setnx(String.valueOf(PRO_DICT_TYPE.WX_T.getDist()), sysPet.getWxTu());
        NutMap skillBookGainNutMap = countSkillBookGain(new Addition((int)pet.getPetLv(), (int)pet.getRebirth(), pet.getPetClose(), SkillFunc.initPetSkill(pet.getSkills()), nutMap));
        for (String key2 : skillBookGainNutMap.keySet()) {
            if (key2.equals(String.valueOf(PRO_DICT_TYPE.HP_CAP.getDist())) || key2.equals(String.valueOf(PRO_DICT_TYPE.MP_CAP.getDist())) || key2.equals(String.valueOf(PRO_DICT_TYPE.ATTACK.getDist())) || key2.equals(String.valueOf(PRO_DICT_TYPE.SPEED.getDist())) || key2.equals(String.valueOf(PRO_DICT_TYPE.HP_PCT.getDist())) || key2.equals(String.valueOf(PRO_DICT_TYPE.MP_PCT.getDist())) || key2.equals(String.valueOf(PRO_DICT_TYPE.ATTACK_PCT.getDist()))) {
                continue;
            }
            if (key2.equals(String.valueOf(PRO_DICT_TYPE.SPEED_PCT.getDist()))) {
                continue;
            }
            nutMap.setv(key2, Double.valueOf(nutMap.getDouble(key2) + skillBookGainNutMap.getDouble(key2)));
        }
        return nutMap;
    }
    
    public static void restProFieldValue(Role role, Pet pet, NutMap nutMap, Map<Integer, AcsryPetSkillParam> acsryPetSkillMap) {
        NutMap additionAwaken = AwakenSkillUtils.addition(role, pet);
        if (additionAwaken != null && additionAwaken.size() > 0) {
            for (Map.Entry<String, Object> entry : additionAwaken.entrySet()) {
                nutMap.setv((String)entry.getKey(), Double.valueOf(nutMap.getDouble((String)entry.getKey()) + additionAwaken.getDouble((String)entry.getKey())));
            }
        }
        AcsryPetSkillUtil.addPetAcsry(role, pet, nutMap, acsryPetSkillMap);
    }
    
    public static NutMap countPro(Role role) {
        NutMap nutMap = new NutMap();
        Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
        Map<Integer, Double> faction = FACTION_PRO_DICT.toMap();
        Map<Integer, SysReiEn> reiEnMap = PartDataPool.reiEnMap;
        NutMap tianFuMap = TianFuUtils.countFuPlayerBookGain(role);
        NutMap acsryMap = AcsrySkillUtil.initializeFightObject(role);
        List<WearEquip> equips = SpringUtils.getBean(EquipService.class).getEquipValid(role, false);
        NutMap searProMap = ((TrainService)SpringUtils.getBean(TrainService.class)).countSearEffectivityPro(role);
        List<Mind> mindList = ((MindService)SpringUtils.getBean(MindService.class)).getMind(role.getId());
        for (Integer key : map.keySet()) {
            Integer type = (Integer)map.get(key);
            double pro_value = cuntPro(equips, key, (int)type);
            double searPro = searProMap.getDouble(key.toString(), 0.0);
            double growPro = costRaceGrowUp(role, key);
            double correction = countCorrection(role, Integer.valueOf((int)key + PRO_DICT_SERIAL.HERO_RELIVE.getSerial()), (int)type, reiEnMap);
            Integer factionDist = Integer.valueOf((int)key + PRO_DICT_SERIAL.HERO_FACTION.getSerial());
            double factionGain = countPartyPractice(role, factionDist, faction);
            double cardPro = countCardPtc(role, (int)key, (int)type);
            double count = pro_value + searPro + growPro + correction + factionGain + cardPro;
            double tianFuProperty = TianFuUtils.getPropertyForMap(tianFuMap, String.valueOf(key), count);
            count += tianFuProperty;
            double acsryProperty = acsryMap.getDouble(String.valueOf(key), 0.0);
            count += acsryProperty;
            double mindProp = MindSkillUtil.countMindProp(role, mindList, (int)key, count);
            if (map.get(Integer.valueOf((int)key + PRO_DICT_SERIAL.MIND.getSerial())) != null) {
                mindProp += MindSkillUtil.countMindProp(role, mindList, (int)key + PRO_DICT_SERIAL.MIND.getSerial(), count);
            }
            nutMap.setv(key.toString(), Double.valueOf(count + mindProp));
        }
        NutMap mindProMap = MindSkillUtil.addition(role);
        nutMap.putAll(mindProMap);
        return costProLimit(role, nutMap, tianFuMap, acsryMap, searProMap, mindList);
    }
    
    public static double cuntPro(List<WearEquip> list, Integer dict, int type) {
        double pro = 0.0;
        double gainPro = gainPro(list, dict);
        if (type == PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()) {
            pro = BigDecimalUtil.decimalFormat("#.###", Double.valueOf(gainPro / 1000.0));
        }
        else {
            pro = (double)BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(3), Double.valueOf(gainPro));
        }
        return pro;
    }
    
    public static double gainPro(Integer dist, List<PetEquip> list) {
        double pro = 0.0;
        for (PetEquip equip : list) {
            FiedEquip fiedEquip = (FiedEquip)equip.getField().stream().filter(e -> e.getType().equals(dist)).findFirst().orElse(null);
            if (fiedEquip != null) {
                pro += (double)(int)fiedEquip.getValue();
            }
            else {
                continue;
            }
        }
        return pro;
    }
    
    public static double gainPro(List<WearEquip> list, Integer dist) {
        double pro = 0.0;
        for (WearEquip wearEquip : list) {
            double count = countEquipPro(wearEquip, dist);
            pro += count;
        }
        return pro;
    }
    
    public static double costRaceGrowUp(Role role, Integer dist) {
        double value = 0.0;
        int race_ren_Lv = 0;
        int race_mo_Lv = 0;
        int race_xian_Lv = 0;
        int race_gui_Lv = 0;
        int race_long_Lv = 0;
        if (role.getRace().equals(Integer.valueOf(RACE.RACE_REN.getValue()))) {
            race_ren_Lv = (int)Math.min((double)(int)role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(Integer.valueOf(RACE.RACE_MO.getValue()))) {
            race_mo_Lv = (int)Math.min((double)(int)role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(Integer.valueOf(RACE.RACE_XIAN.getValue()))) {
            race_xian_Lv = (int)Math.min((double)(int)role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(Integer.valueOf(RACE.RACE_GUI.getValue()))) {
            race_gui_Lv = (int)Math.min((double)(int)role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        else if (role.getRace().equals(Integer.valueOf(RACE.RACE_LONG.getValue()))) {
            race_long_Lv = (int)Math.min((double)(int)role.getLevel(), ATTRCAP.MAX_RACE_LV_LIMIT.getValue());
        }
        if (PRO_DICT_TYPE.K_FY.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_ren_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 8.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 8.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_long_Lv / 8.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_HL.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_ren_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 8.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 6.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_long_Lv / 6.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_HS.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_ren_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 8.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 6.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_long_Lv / 6.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_DU.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_ren_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 8.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 6.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_long_Lv / 6.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_H.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_xian_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 12.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)~(race_gui_Lv - 1) / 8.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_S.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_xian_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 12.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)~(race_gui_Lv - 1) / 8.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_F.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_xian_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 12.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)~(race_gui_Lv - 1) / 8.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_L.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_xian_Lv / 4.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 12.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)~(race_gui_Lv - 1) / 8.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.WL_XS.getDist() == (int)dist) {
            double variable = 6.0;
            if (role.getRace().equals(Integer.valueOf(RACE.RACE_MO.getValue()))) {
                variable = 8.0;
            }
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_mo_Lv / 12.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_long_Lv / variable / 100.0));
        }
        else if (PRO_DICT_TYPE.K_GH.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 6.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_YW.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 6.0 / 100.0)) + BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_long_Lv / 6.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.K_SS.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 6.0 / 100.0));
        }
        else if (PRO_DICT_TYPE.HIT_RATIO.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 12.0 / 100.0)) + 0.8;
        }
        else if (PRO_DICT_TYPE.MISS_RATIO.getDist() == (int)dist) {
            value = BigDecimalUtil.doubleFormat("#.###", Double.valueOf((double)race_gui_Lv / 4.0 / 100.0));
        }
        return value;
    }
    
    public static NutMap costProLimit(Role role, NutMap nutMap, NutMap tMap, NutMap acsryMap, NutMap searProMap, List<Mind> mindList) {
        int race = (int)role.getRace();
        int rei = (int)role.getRelive();
        Map<Integer, Integer> map = role.getRest().getParty();
        String pmz_value = String.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist());
        double pmz = nutMap.getDouble(pmz_value);
        if (pmz > 0.0) {
            double min = Math.min(ATTRCAP.MAX_PACC_NUM.getValue(), Math.max(ATTRCAP.MIN_PACC_NUM.getValue(), pmz));
            nutMap.setv(pmz_value, Double.valueOf(min));
        }
        String lsb_value = String.valueOf(PRO_DICT_TYPE.MISS_RATIO.getDist());
        double lsb = nutMap.getDouble(lsb_value);
        if (lsb > 0.0) {
            double min2 = Math.min(ATTRCAP.MAX_PSBL_NUM.getValue(), Math.max(ATTRCAP.MIN_PSBL_NUM.getValue(), lsb));
            nutMap.setv(lsb_value, Double.valueOf(min2));
        }
        String slsb_value = String.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist());
        double slsb = nutMap.getDouble(slsb_value);
        if (slsb > 0.0) {
            double min3 = Math.min(ATTRCAP.MAX_PSBL_NUM.getValue(), Math.max(ATTRCAP.MIN_PSBL_NUM.getValue(), slsb));
            nutMap.setv(slsb_value, Double.valueOf(min3));
        }
        String lljt_value = String.valueOf(PRO_DICT_TYPE.COMBO_NUM.getDist());
        double lljt = nutMap.getDouble(lljt_value);
        if (lljt > 0.0) {
            double min4 = Math.min(ATTRCAP.MAX_PLJTIMES_NUM.getValue(), Math.max(ATTRCAP.MIN_PLJTIMES_NUM.getValue(), lljt));
            nutMap.setv(lljt_value, Double.valueOf(min4));
        }
        String lftl_value = String.valueOf(PRO_DICT_TYPE.SHOCK_EXTENT.getDist());
        double lftl = nutMap.getDouble(lftl_value);
        if (lftl > 0.0) {
            double min5 = Math.min(ATTRCAP.MAX_FTLV_NUM.getValue(), Math.max(ATTRCAP.MIN_FTLV_NUM.getValue(), lftl));
            nutMap.setv(lftl_value, Double.valueOf(min5));
        }
        int gain_dist = FACTION_PRO_DICT.K_KZ_CAP.getPro();
        double gain = FACTION_PRO_DICT.K_KZ_CAP.getGain();
        double maxRes = ATTRCAP.MAX_RES_HERO_ELSE_VALUE.getValue();
        if (race > 0) {
            if (race == RACE.RACE_REN.getValue()) {
                maxRes = ATTRCAP.MAX_RES_HERO_REN_VALUE.getValue();
            }
            else if (race == RACE.RACE_LONG.getValue()) {
                maxRes = ATTRCAP.MAX_RES_HERO_LONG_VALUE.getValue();
            }
        }
        String kfy = String.valueOf(PRO_DICT_TYPE.K_FY.getDist());
        double kfyValue = nutMap.getDouble(kfy);
        Integer count = (Integer)map.get(Integer.valueOf(gain_dist));
        count = Integer.valueOf((count == null) ? 0 : ((int)count));
        double gainRes = (double)(int)count * gain;
        double endValue = maxRes + gainRes;
        double tianFuRes = endValue * tMap.getDouble(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, 0.0);
        endValue += tianFuRes;
        double acsryRes = endValue * acsryMap.getDouble(PRO_DICT_TYPE.K_FY.getDist() + "_" + 119, 0.0);
        endValue += acsryRes;
        endValue += searProMap.getDouble(String.valueOf(PRO_DICT_TYPE.K_FY_LMT.getDist()), 0.0);
        endValue += MindSkillUtil.countMindProp(role, mindList, PRO_DICT_TYPE.K_FY_LMT.getDist(), endValue);
        double min6 = Math.min(kfyValue, endValue);
        nutMap.setv(kfy, Double.valueOf(min6));
        String khs = String.valueOf(PRO_DICT_TYPE.K_HS.getDist());
        double khsValue = nutMap.getDouble(khs);
        Integer count2 = (Integer)map.get(Integer.valueOf(gain_dist));
        count2 = Integer.valueOf((count2 == null) ? 0 : ((int)count2));
        double gainRes2 = (double)(int)count2 * gain;
        double endValue2 = maxRes + gainRes2;
        double tianFuRes2 = endValue2 * tMap.getDouble(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, 0.0);
        endValue2 += tianFuRes2;
        double acsryRes2 = endValue2 * acsryMap.getDouble(PRO_DICT_TYPE.K_HS.getDist() + "_" + 119, 0.0);
        endValue2 += acsryRes2;
        endValue2 += searProMap.getDouble(String.valueOf(PRO_DICT_TYPE.K_HS_LMT.getDist()), 0.0);
        endValue2 += MindSkillUtil.countMindProp(role, mindList, PRO_DICT_TYPE.K_HS_LMT.getDist(), endValue2);
        double min7 = Math.min(khsValue, endValue2);
        nutMap.setv(khs, Double.valueOf(min7));
        String khl = String.valueOf(PRO_DICT_TYPE.K_HL.getDist());
        double khlValue = nutMap.getDouble(khl);
        Integer count3 = (Integer)map.get(Integer.valueOf(gain_dist));
        count3 = Integer.valueOf((count3 == null) ? 0 : ((int)count3));
        double gainRes3 = (double)(int)count3 * gain;
        double endValue3 = maxRes + gainRes3;
        double tianFuRes3 = endValue3 * tMap.getDouble(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, 0.0);
        endValue3 += tianFuRes3;
        double acsryRes3 = endValue3 * acsryMap.getDouble(PRO_DICT_TYPE.K_HL.getDist() + "_" + 119, 0.0);
        endValue3 += acsryRes3;
        endValue3 += searProMap.getDouble(String.valueOf(PRO_DICT_TYPE.K_HL_LMT.getDist()), 0.0);
        endValue3 += MindSkillUtil.countMindProp(role, mindList, PRO_DICT_TYPE.K_HL_LMT.getDist(), endValue3);
        double min8 = Math.min(khlValue, endValue3);
        nutMap.setv(khl, Double.valueOf(min8));
        String kyw = String.valueOf(PRO_DICT_TYPE.K_YW.getDist());
        double kywValue = nutMap.getDouble(kyw);
        Integer count4 = (Integer)map.get(Integer.valueOf(gain_dist));
        count4 = Integer.valueOf((count4 == null) ? 0 : ((int)count4));
        double gainRes4 = (double)(int)count4 * gain;
        double endValue4 = maxRes + gainRes4;
        double tianFuRes4 = endValue4 * tMap.getDouble(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, 0.0);
        endValue4 += tianFuRes4;
        double acsryRes4 = endValue4 * acsryMap.getDouble(PRO_DICT_TYPE.K_YW.getDist() + "_" + 119, 0.0);
        endValue4 += acsryRes4;
        endValue4 += searProMap.getDouble(String.valueOf(PRO_DICT_TYPE.K_YW_LMT.getDist()), 0.0);
        endValue4 += MindSkillUtil.countMindProp(role, mindList, PRO_DICT_TYPE.K_YW_LMT.getDist(), endValue4);
        double min9 = Math.min(kywValue, endValue4);
        nutMap.setv(kyw, Double.valueOf(min9));
        double max = reiProTmAndQmMax(rei);
        String ktm = String.valueOf(PRO_DICT_TYPE.K_TM.getDist());
        double ktmValue = nutMap.getDouble(ktm);
        if (ktmValue > 0.0) {
            double min10 = Math.min(ktmValue, max);
            nutMap.setv(ktm, Double.valueOf(min10));
        }
        String kqm = String.valueOf(PRO_DICT_TYPE.K_QM.getDist());
        double kqmValue = nutMap.getDouble(kqm);
        if (kqmValue > 0.0) {
            double min11 = Math.min(kqmValue, max);
            nutMap.setv(kqm, Double.valueOf(min11));
        }
        double fkAndXlMax = reiProFkAndXlMax(rei);
        String kfk = String.valueOf(PRO_DICT_TYPE.K_FG.getDist());
        double kfkValue = nutMap.getDouble(kfk);
        if (kfkValue > 0.0) {
            double min12 = Math.min(kfkValue, fkAndXlMax);
            nutMap.setv(kfk, Double.valueOf(min12));
        }
        String kxl = String.valueOf(PRO_DICT_TYPE.K_XL.getDist());
        double kxlValue = nutMap.getDouble(kxl);
        if (kxlValue > 0.0) {
            double min13 = Math.min(kxlValue, fkAndXlMax);
            nutMap.setv(kxl, Double.valueOf(min13));
            return nutMap;
        }
        return nutMap;
    }
    
    public static double reiProTmAndQmMax(int rei) {
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
    
    public static double reiProFkAndXlMax(int rei) {
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
    
    public static Effect countHurt(Fight fight, FightObject attacker, FightObject target, double rdk, double oriparam) {
        PassiveSkillUtils.beforeCountHurt(fight, attacker, target);
        Effect effect = new Effect(1, 0);
        int damageHp = 0;
        damageHp += getPhyAttack(attacker);
        double phyRes = target.getSelfAllTempPro(PRO_DICT_TYPE.WL_XS.getDist());
        phyRes += MindSkillUtil.getMindValue(target, MIND.XS_WL_XF.getDist());
        double penetrate = attacker.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_RATIO.getDist());
        if (SkillAi.canAffectOnPro(penetrate)) {
            effect.setType(101);
            double sub_deep = attacker.getSelfAllTempPro(PRO_DICT_TYPE.CONQUER_EXTENT.getDist());
            phyRes -= sub_deep;
        }
        if (oriparam > 1.0) {
            damageHp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)damageHp * oriparam));
        }
        double shdk = target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
        double shjs = attacker.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist());
        double weak = shjs - shdk;
        damageHp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)damageHp * weak + ((double)damageHp - (double)damageHp * phyRes)));
        if (target.getType() == 1) {
            damageHp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)damageHp * Math.pow(0.5, 0.99)));
        }
        if (rdk <= 0.0) {
            rdk = (double)RandomUtil.round(Integer.valueOf(95), Integer.valueOf(105)) / 100.0;
        }
        int hurt = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)damageHp * rdk));
        hurt -= (int)target.getSelfAllTempPro(MIND.XS_WL.getDist());
        if (hurt <= 0) {
            hurt = 1;
        }
        effect.setHit(rdk);
        effect.setHurt(Math.max(1, hurt));
        ImbueUtil.clearTemps(attacker.getImbueMap());
        return effect;
    }
    
    public static int getPhyAttack(FightObject attacker) {
        int damageHp = attacker.getPhyPower() + (int)(attacker.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * (double)attacker.getPhyPower());
        damageHp += damageHp * (int)attacker.getSelfAllTempPro(PRO_DICT_TYPE.ATTACK_ADDITION.getDist());
        return damageHp;
    }
    
    public static int countPartnerSpeed(Partner partner) {
        Map<Short, SysPartnerProperty> shortSysPartnerPropertyMap = (Map)PartnerDataPool.partnerPropertyMap.get(partner.getPartnerType());
        int level = partner.getPartnerLv() + partner.getRebirth() * 1000;
        SysPartnerProperty sysPartnerProperty = (SysPartnerProperty)shortSysPartnerPropertyMap.get(Short.valueOf((short)level));
        if (sysPartnerProperty != null) {
            int speed = sysPartnerProperty.getSpeed();
            return speed;
        }
        return 0;
    }
    
    public static int hypersensitivity(int baseSpeed) {
        if (baseSpeed != 0) {
            int absSpeed = Math.abs(baseSpeed);
            double limitMin = Math.min(300.0 / (double)absSpeed, 0.3);
            double limitMax = 0.0;
            double random = RandomUtil.getRandomDouble(Double.valueOf(limitMax), Double.valueOf(limitMin));
            double randomNum = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(random));
            double doubleSpeed = (double)baseSpeed * (1.0 - randomNum * (double)absSpeed / (double)absSpeed);
            int returnSpeed = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(doubleSpeed));
            return returnSpeed;
        }
        return baseSpeed;
    }
}
