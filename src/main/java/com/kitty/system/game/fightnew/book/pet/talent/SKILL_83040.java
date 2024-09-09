package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import java.util.Collections;
import java.util.Iterator;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83040 extends SkillProcessor
{
    public SKILL_83040() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        if (!effector.isPet()) {
            return null;
        }
        double percentAC = (double)Math.round(8.0 + 0.2 * this.getParam1(effector)) / 100.0;
        double percentBD = (double)Math.round(3.2 + 0.08 * this.getParam1(effector)) / 100.0;
        int round = 5 - (fight.getRound() - effector.getComeRound());
        if (round <= 0) {
            return null;
        }
        List<FightObject> mySide = fight.getMySide((int)effector.getId());
        List<FightObject> zlList = (List)fight.getMySide((int)effector.getId()).stream().filter(obj -> obj.isPet() && obj.getSerial() == 21075).collect(Collectors.toList());
        if (zlList != null && zlList.size() > 0) {
            Iterator<FightObject> iterator = zlList.iterator();
            while (iterator.hasNext()) {
            	FightObject obj = (FightObject)iterator.next();
                if (!obj.getId().equals(effector.getId())) {
                    this.exclude(mySide, obj);
                }
                else {
                    continue;
                }
            }
        }
        Iterator<FightObject> iterator2 = mySide.iterator();
        while (iterator2.hasNext()) {
        	FightObject obj = (FightObject)iterator2.next();
            if (obj.getId().equals(effector.getId())) {
                continue;
            }
            if (obj.getSpeed() > effector.getSpeed()) {
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()), round, 83040 + (int)effector.getId(), percentAC);
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), round, 83040 + (int)effector.getId(), percentBD);
            }
            else if (obj.getSpeed() < effector.getSpeed()) {
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), round, 83040 + (int)effector.getId(), percentAC);
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist()), round, 83040 + (int)effector.getId(), percentBD);
            }
            else {
                continue;
            }
        }
        return null;
    }
    
    @Override
    public void death(Fight fight, FightObject effector, FightObject target, Object... args) {
        if (target.isFallDown() && target.isPet()) {
            List<FightObject> mySide = fight.getMySide((int)target.getId());
            boolean isDelete = this.exclude(mySide, target);
            if (isDelete) {
                List<FightObject> zlList = (List)fight.getMySide((int)target.getId()).stream().filter(obj -> obj.isPet() && obj.getSerial() == 21075 && !obj.getId().equals(target.getId())).collect(Collectors.toList());
                if (zlList != null && zlList.size() > 0) {
                    Collections.sort(zlList);
                    FightObject firstZl = (FightObject)zlList.get(0);
                    this.passive(fight, firstZl, new Object[0]);
                }
            }
        }
    }
    
    private boolean exclude(List<FightObject> mySide, FightObject target) {
        boolean isDelete = false;
        for (FightObject obj : mySide) {
            if (obj.getId().equals(target.getId())) {
                continue;
            }
            ImbueParam imbueParamBH = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.BURN_HURT.getDist()), 83040 + (int)target.getId(), false);
            ImbueParam imbueParamMHR = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist()), 83040 + (int)target.getId(), false);
            ImbueParam imbueParamSD = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), 83040 + (int)target.getId(), false);
            ImbueParam imbueParamMMR = ImbueUtil.gainProForSkill(obj.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist()), 83040 + (int)target.getId(), false);
            if (imbueParamBH != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist(), 83040 + (int)target.getId());
                isDelete = true;
            }
            if (imbueParamMHR != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), 83040 + (int)target.getId());
                isDelete = true;
            }
            if (imbueParamSD != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), 83040 + (int)target.getId());
                isDelete = true;
            }
            if (imbueParamMMR != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist(), 83040 + (int)target.getId());
                isDelete = true;
            }
            else {
                continue;
            }
        }
        return isDelete;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    private double getParam1(FightObject effector) {
        return (double)(effector.getRelive() * 5 + 1) * (Math.pow((double)effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (double)(100 + effector.getRelive() * 20));
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
