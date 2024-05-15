
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.imbue.bean.ImbueParam;
import java.util.Collections;
import java.util.Iterator;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83040 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        if (!effector.isPet()) {
            return null;
        }
        final double percentAC = Math.round(8.0 + 0.2 * this.getParam1(effector)) / 100.0;
        final double percentBD = Math.round(3.2 + 0.08 * this.getParam1(effector)) / 100.0;
        final int round = 5 - (fight.getRound() - effector.getComeRound());
        if (round <= 0) {
            return null;
        }
        final List<FightObject> mySide = fight.getMySide(effector.getId());
        final List<FightObject> zlList = fight.getMySide(effector.getId()).stream().filter(obj -> obj.isPet() && obj.getSerial() == 21075).collect(Collectors.toList());
        if (zlList != null && zlList.size() > 0) {
            final Iterator<FightObject> iterator = zlList.iterator();
            while (iterator.hasNext()) {
                FightObject obj = iterator.next();
                if (!obj.getId().equals(effector.getId())) {
                    this.exclude(mySide, obj);
                }
            }
        }
        final Iterator<FightObject> iterator2 = mySide.iterator();
        while (iterator2.hasNext()) {
            FightObject obj = iterator2.next();
            if (obj.getId().equals(effector.getId())) {
                continue;
            }
            if (obj.getSpeed() > effector.getSpeed()) {
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist(), round, 83040 + effector.getId(), percentAC);
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), round, 83040 + effector.getId(), percentBD);
            }
            else {
                if (obj.getSpeed() >= effector.getSpeed()) {
                    continue;
                }
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), round, 83040 + effector.getId(), percentAC);
                ImbueUtil.addOrUpdSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist(), round, 83040 + effector.getId(), percentBD);
            }
        }
        return null;
    }
    
    @Override
    public void death(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        if (target.isFallDown() && target.isPet()) {
            final List<FightObject> mySide = fight.getMySide(target.getId());
            final boolean isDelete = this.exclude(mySide, target);
            if (isDelete) {
                final List<FightObject> zlList = fight.getMySide(target.getId()).stream().filter(obj -> obj.isPet() && obj.getSerial() == 21075 && !obj.getId().equals(target.getId())).collect(Collectors.toList());
                if (zlList != null && zlList.size() > 0) {
                    Collections.sort(zlList);
                    final FightObject firstZl = zlList.get(0);
                    this.passive(fight, firstZl);
                }
            }
        }
    }
    
    private boolean exclude(final List<FightObject> mySide, final FightObject target) {
        boolean isDelete = false;
        for (final FightObject obj : mySide) {
            if (obj.getId().equals(target.getId())) {
                continue;
            }
            final ImbueParam imbueParamBH = ImbueUtil.gainProForSkill(obj.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist(), 83040 + target.getId(), false);
            final ImbueParam imbueParamMHR = ImbueUtil.gainProForSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), 83040 + target.getId(), false);
            final ImbueParam imbueParamSD = ImbueUtil.gainProForSkill(obj.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), 83040 + target.getId(), false);
            final ImbueParam imbueParamMMR = ImbueUtil.gainProForSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist(), 83040 + target.getId(), false);
            if (imbueParamBH != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.BURN_HURT.getDist(), 83040 + target.getId());
                isDelete = true;
            }
            if (imbueParamMHR != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_HIT_RATIO.getDist(), 83040 + target.getId());
                isDelete = true;
            }
            if (imbueParamSD != null) {
                ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.SH_DK.getDist(), 83040 + target.getId());
                isDelete = true;
            }
            if (imbueParamMMR == null) {
                continue;
            }
            ImbueUtil.removeSkill(obj.getImbueMap(), PRO_DICT_TYPE.MAGIC_MISS_RATIO.getDist(), 83040 + target.getId());
            isDelete = true;
        }
        return isDelete;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    private double getParam1(final FightObject effector) {
        return (effector.getRelive() * 5 + 1) * (Math.pow(effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (100 + effector.getRelive() * 20));
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
