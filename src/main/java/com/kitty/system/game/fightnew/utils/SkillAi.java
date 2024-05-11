
package com.kitty.system.game.fightnew.utils;

import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import java.util.function.Function;
import java.util.Comparator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.enums.war.itempro.def.PET_SKILL_TERM_DICT;
import java.util.Map;
import com.kitty.common.utils.MapObjectUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.FightObject;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class SkillAi
{
    public static Integer[] obstacle;
    
    public static FightObject certainStatusOrPro(final Fight fight, final List<Integer> status, final boolean flag, final int type) {
        final List<FightObject> objects = new ArrayList<FightObject>();
        final List<FightObject> list = unit(fight, flag);
        if (status.size() > 0) {
            for (final FightObject fightObject : list) {
                for (final Integer k : status) {
                    if (fightObject.getStatusMap().getInt(String.valueOf(k)) > 0) {
                        objects.add(fightObject);
                    }
                }
            }
        }
        if (objects.size() <= 0) {
            objects.addAll(list);
        }
        final List<FightObject> fightObjects = certainProMax(objects, type);
        if (fightObjects.size() > 0) {
            return fightObjects.get(0);
        }
        if (list == null || list.size() <= 0) {
            return null;
        }
        return list.get(0);
    }
    
    public static List<FightObject> unit(final Fight fight, final boolean flag) {
        final List<FightObject> list = new ArrayList<FightObject>();
        if (flag) {
            list.addAll(fight.getListA());
        }
        else {
            list.addAll(fight.getListB());
        }
        return list;
    }
    
    public static List<FightObject> certainProMax(final List<FightObject> list, final int type) {
        final Map<Integer, Double> pos2Actors = new HashMap<Integer, Double>();
        if (type == 1) {
            for (int i = 0; i < list.size(); ++i) {
                final FightObject fightObject = list.get(i);
                final double percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getCurrLife() / fightObject.getMaxLife());
                pos2Actors.put(i, percentage);
            }
        }
        else if (type == 2) {
            for (int i = 0; i < list.size(); ++i) {
                final FightObject fightObject = list.get(i);
                final double percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getCurrMana() / fightObject.getMaxMana());
                pos2Actors.put(i, percentage);
            }
        }
        else if (type == 3) {
            for (int i = 0; i < list.size(); ++i) {
                final FightObject fightObject = list.get(i);
                final double percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getPhyPower() + fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RISE_ATTACK.getDist()) * fightObject.getPhyPower());
                pos2Actors.put(i, percentage);
            }
        }
        else {
            for (int i = 0; i < list.size(); ++i) {
                final FightObject fightObject = list.get(i);
                final double percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + fightObject.getSpeed());
                pos2Actors.put(i, percentage);
            }
        }
        final List<FightObject> objects = new ArrayList<FightObject>();
        final Map<Integer, Double> map = MapObjectUtil.sortByValue(pos2Actors, true);
        for (final Integer key : map.keySet()) {
            objects.add(list.get(key));
        }
        return objects;
    }
    
    public static FightObject effectProBass(final Fight fight, final FightObject effector, final Integer type, final List<String> terms, final boolean isDie) {
        double down = -1.0;
        FightObject unit = null;
        final List<FightObject> list = unit(fight, effector.isASide());
        for (final FightObject fightObject : list) {
            if (!fightObject.getId().equals(effector.getId())) {
                if (!isDie && terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()) && fightObject.isFallDown()) {
                    continue;
                }
                double percentage = 0.0;
                if (type == 1) {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getCurrLife() / fightObject.getMaxLife());
                }
                else if (type == 2) {
                    if (fightObject.getMaxMana() <= 0) {
                        continue;
                    }
                    percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getCurrMana() / fightObject.getMaxMana());
                }
                else {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getSpeed());
                }
                if (down == -1.0) {
                    down = percentage;
                    unit = fightObject;
                }
                else {
                    if (percentage >= down) {
                        continue;
                    }
                    down = percentage;
                    unit = fightObject;
                }
            }
        }
        return unit;
    }
    
    public static FightObject effectProBass(final Fight fight, final FightObject effector, final Integer type, final List<String> terms) {
        double down = -1.0;
        FightObject unit = null;
        final List<FightObject> list = unit(fight, effector.isASide());
        for (final FightObject fightObject : list) {
            if (!fightObject.getId().equals(effector.getId())) {
                if (terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_PET_N.getValue()) && fightObject.getLogic() == LOGIC.LOGICTYPE_PET.getValue()) {
                    continue;
                }
                if (terms.contains(PET_SKILL_TERM_DICT.PRO_BASS_DOWN_N.getValue()) && fightObject.isFallDown()) {
                    continue;
                }
                double percentage = 0.0;
                if (type == 1) {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getCurrLife() / (fightObject.getMaxLife() + 0.0));
                }
                else if (type == 2) {
                    if (fightObject.getMaxMana() == 0) {
                        percentage = 1.0;
                    }
                    else {
                        percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getCurrMana() / (fightObject.getMaxMana() + 0.0));
                    }
                }
                else {
                    percentage = BigDecimalUtil.decimalScaleObjectDown(3, fightObject.getSpeed());
                }
                if (down == -1.0) {
                    down = percentage;
                    unit = fightObject;
                }
                else {
                    if (percentage >= down) {
                        continue;
                    }
                    down = percentage;
                    unit = fightObject;
                }
            }
        }
        return unit;
    }
    
    public static boolean canAffectOnPro(final double pro) {
        if (pro <= 0.0) {
            return false;
        }
        if (pro >= 1.0) {
            return true;
        }
        final int k = 1000000;
        final int rdNumber = (int)(Math.random() * k + 1.0);
        return rdNumber <= pro * k;
    }
    
    public static boolean checkDoubleHit(final FightObject effector) {
        final String combo = String.valueOf(PRO_DICT_TYPE.COMBO_RATIO.getDist());
        final double pro = BigDecimalUtil.decimalScaleObjectDown(8, effector.getPropertyMap().get((Object)combo));
        return canAffectOnPro(pro);
    }
    
    public static boolean checkPetIsSkill(final Fight fight, final boolean flag, final Integer skillId) {
        final List<FightObject> victim = unit(fight, flag);
        for (final FightObject object : victim) {
            final FightObject petObject = fight.queryFightObject(object.getFightPetId());
            if (petObject != null) {
                final RoleSkill skillBy = petObject.getSkillBy(skillId);
                if (skillBy != null) {
                    return true;
                }
                continue;
            }
        }
        return false;
    }
    
    public static FightObject effectProBassHight(final Fight fight, final FightObject effector, final Integer type) {
        FightObject unit = new FightObject();
        final List<FightObject> list = unit(fight, effector.isASide()).stream().filter(obj -> !obj.getId().equals(effector.getId()) && !obj.getId().equals(effector.getHostId()) && (obj.isPlayer() || obj.isPartner())).collect(Collectors.toList());
        if (list.size() <= 0) {
            return null;
        }
        if (type == 1) {
            unit = list.stream().max(Comparator.comparing(FightObject::getMaxLife)).get();
        }
        else {
            unit = list.stream().max(Comparator.comparing(FightObject::getMaxMana)).get();
        }
        return unit;
    }
    
    public static boolean checkMiss(final FightObject effector, final FightObject target) {
        final double hitPro = effector.getSelfAllTempPro(PRO_DICT_TYPE.HIT_RATIO.getDist());
        final double missPro = target.getSelfAllTempPro(PRO_DICT_TYPE.MISS_RATIO.getDist());
        double finalPro = hitPro - missPro;
        finalPro += 0.1;
        if (finalPro <= 0.5) {
            finalPro = 0.5;
        }
        return canAffectOnPro(finalPro);
    }
    
    public static boolean checkFury(final FightObject effector) {
        final double pro = effector.getSelfAllTempPro(PRO_DICT_TYPE.RAGE_RATIO.getDist());
        double pro_Ex = 0.0;
        if (effector.getStatusValue(PRO_DICT_TYPE.RAGE_RATIO.getDist()) > 0) {
            pro_Ex = effector.getStatusMap().getDouble(PRO_DICT_TYPE.RAGE_RATIO.getDist() + "value");
        }
        return canAffectOnPro(pro + pro_Ex);
    }
    
    static {
        SkillAi.obstacle = new Integer[] { SKILLATTR.SKILLATTR_ICE.getValue(), SKILLATTR.SKILLATTR_SLEEP.getValue(), SKILLATTR.SKILLATTR_CONFUSE.getValue(), SKILLATTR.SKILLATTR_POISON.getValue(), SKILLATTR.SKILLATTR_YIWANG.getValue() };
    }
}
