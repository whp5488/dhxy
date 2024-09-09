package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.Comparator;
import java.util.stream.Collectors;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import java.util.Map;
import com.kitty.common.utils.MapObjectUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.HashMap;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class SkillAi
{
    public static Integer[] obstacle;
    
    public SkillAi() {
    }
    
    public static FightObject certainStatusOrPro(Fight fight, List<Integer> status, boolean flag, int type) {
        List<FightObject> objects = new ArrayList<>();
        List<FightObject> list = unit(fight, flag);
        if (status.size() > 0) {
            for (FightObject fightObject : list) {
                for (Integer k : status) {
                    if (fightObject.getStatusMap().getInt(String.valueOf(k)) > 0) {
                        objects.add(fightObject);
                    }
                    else {
                        continue;
                    }
                }
            }
        }
        if (objects.size() <= 0) {
            objects.addAll(list);
        }
        List<FightObject> fightObjects = certainProMax(objects, type);
        if (fightObjects.size() > 0) {
            return (FightObject)fightObjects.get(0);
        }
        if (list == null || list.size() <= 0) {
            return null;
        }
        return (FightObject)list.get(0);
    }
    
    public static List<FightObject> unit(Fight fight, boolean flag) {
        List<FightObject> list = new ArrayList<>();
        if (flag) {
            list.addAll(fight.getListA());
            return list;
        }
        list.addAll(fight.getListB());
        return list;
    }
    
    public static List<FightObject> certainProMax(List<FightObject> list, int type) {
        Map<Integer, Double> pos2Actors = new HashMap();
        if (type == 1) {
            for (int i = 0; i < list.size(); ++i) {
                FightObject fightObject = (FightObject)list.get(i);
                double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrLife() / fightObject.getMaxLife()));
                pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
            }
        }
        else if (type == 2) {
            for (int i = 0; i < list.size(); ++i) {
                FightObject fightObject = (FightObject)list.get(i);
                double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrMana() / fightObject.getMaxMana()));
                pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
            }
        }
        else if (type == 3) {
            for (int i = 0; i < list.size(); ++i) {
                FightObject fightObject = (FightObject)list.get(i);
                double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((double)fightObject.getPhyPower() + fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * (double)fightObject.getPhyPower()));
                pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
            }
        }
        else {
            for (int i = 0; i < list.size(); ++i) {
                FightObject fightObject = (FightObject)list.get(i);
                double percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + (double)fightObject.getSpeed()));
                pos2Actors.put(Integer.valueOf(i), Double.valueOf(percentage));
            }
        }
        List<FightObject> objects = new ArrayList<>();
        Map<Integer, Double> map = MapObjectUtil.sortByValue(pos2Actors, true);
        for (Integer key : map.keySet()) {
            objects.add(list.get((int)key));
        }
        return objects;
    }
    
    public static FightObject effectProBass(Fight fight, FightObject effector, Integer type, List<String> terms, boolean isDie) {
        double down = -1.0;
        FightObject unit = null;
        List<FightObject> list = unit(fight, effector.isASide());
        for (FightObject fightObject : list) {
            if (!fightObject.getId().equals(effector.getId())) {
                if (!isDie && terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()) && fightObject.isFallDown()) {
                    continue;
                }
                double percentage = 0.0;
                if ((int)type == 1) {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrLife() / fightObject.getMaxLife()));
                }
                else if ((int)type == 2) {
                    if (fightObject.getMaxMana() <= 0) {
                        continue;
                    }
                    percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getCurrMana() / fightObject.getMaxMana()));
                }
                else {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getSpeed()));
                }
                if (down == -1.0) {
                    down = percentage;
                    unit = fightObject;
                }
                else if (percentage < down) {
                    down = percentage;
                    unit = fightObject;
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return unit;
    }
    
    public static FightObject effectProBass(Fight fight, FightObject effector, Integer type, List<String> terms) {
        double down = -1.0;
        FightObject unit = null;
        List<FightObject> list = unit(fight, effector.isASide());
        for (FightObject fightObject : list) {
            if (!fightObject.getId().equals(effector.getId())) {
                if (terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue()) && fightObject.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
                    continue;
                }
                if (terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()) && fightObject.isFallDown()) {
                    continue;
                }
                double percentage = 0.0;
                if ((int)type == 1) {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((double)fightObject.getCurrLife() / ((double)fightObject.getMaxLife() + 0.0)));
                }
                else if ((int)type == 2) {
                    if (fightObject.getMaxMana() == 0) {
                        percentage = 1.0;
                    }
                    else {
                        percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((double)fightObject.getCurrMana() / ((double)fightObject.getMaxMana() + 0.0)));
                    }
                }
                else {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Integer.valueOf(fightObject.getSpeed()));
                }
                if (down == -1.0) {
                    down = percentage;
                    unit = fightObject;
                }
                else if (percentage < down) {
                    down = percentage;
                    unit = fightObject;
                }
                else {
                    continue;
                }
            }
            else {
                continue;
            }
        }
        return unit;
    }
    
    public static boolean canAffectOnPro(double pro) {
        if (pro <= 0.0) {
            return false;
        }
        if (pro >= 1.0) {
            return true;
        }
        int k = 1000000;
        int rdNumber = (int)(Math.random() * (double)k + 1.0);
        return (double)rdNumber <= pro * (double)k;
    }
    
    public static boolean checkDoubleHit(FightObject effector) {
        String combo = String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist());
        double pro = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), effector.getPropertyMap().get(combo));
        return canAffectOnPro(pro);
    }
    
    public static boolean checkPetIsSkill(Fight fight, boolean flag, Integer skillId) {
        List<FightObject> victim = unit(fight, flag);
        for (FightObject object : victim) {
            FightObject petObject = fight.queryFightObject(Integer.valueOf(object.getFightPetId()));
            if (petObject != null) {
                RoleSkill skillBy = petObject.getSkillBy(skillId);
                if (skillBy != null) {
                    return true;
                }
                continue;
            }
            else {
                continue;
            }
        }
        return false;
    }
    
    public static FightObject effectProBassHight(Fight fight, FightObject effector, Integer type) {
        FightObject unit = new FightObject();
        List<FightObject> list = (List)unit(fight, effector.isASide()).stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.getId().equals(Integer.valueOf(effector.getHostId())) && (obj.isPlayer() || obj.isPartner())).collect(Collectors.toList());
        if (list.size() <= 0) {
            return null;
        }
        if ((int)type == 1) {
            unit = (FightObject)list.stream().max(Comparator.comparing(FightObject::getMaxLife)).get();
            return unit;
        }
        unit = (FightObject)list.stream().max(Comparator.comparing(FightObject::getMaxMana)).get();
        return unit;
    }
    
    public static boolean checkMiss(FightObject effector, FightObject target) {
        double hitPro = effector.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
        double missPro = target.getSelfAllTempPro(PRO_DICT_TYPE.MISS_RATIO.getDist());
        double finalPro = hitPro - missPro;
        finalPro += 0.1;
        if (finalPro <= 0.5) {
            finalPro = 0.5;
        }
        return canAffectOnPro(finalPro);
    }
    
    public static boolean checkFury(FightObject effector) {
        double pro = effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
        double pro_Ex = 0.0;
        if (effector.getStatusValue(PRO_DICT_TYPE.RAGE_RATIO.getDist()) > 0) {
            pro_Ex = effector.getStatusMap().getDouble(PRO_DICT_TYPE.RAGE_RATIO.getDist() + "value");
        }
        return canAffectOnPro(pro + pro_Ex);
    }
    
    static {
        SkillAi.obstacle = new Integer[] { Integer.valueOf(SKILLATTR.SKILLATTR_ICE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_SLEEP.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_CONFUSE.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_POISON.getValue()), Integer.valueOf(SKILLATTR.SKILLATTR_YIWANG.getValue()) };
    }
}
