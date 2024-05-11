
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83037 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        int status = 2087;
        if (fight.getRound() != 1) {
            status = this.getRandomStatus();
        }
        final int time = 2;
        effector.setStatus(status, time);
        if (2087 == status) {
            ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.H_KB_RATIO.getDist(), 1, 0.5);
        }
        final int oldStatus = this.getOldStatus(effector);
        if (oldStatus != status) {
            final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 43, 83037, effector.getId());
            final FightObjectParam targetParam = ActionParamUtils.addStatusForTarget(effector, 2, status, time, 1);
            if (oldStatus != 0) {
                effector.removeStatus(oldStatus);
                targetParam.addClearStatus(oldStatus);
            }
            actionDetailParam.getTargetParamList().add(targetParam);
            fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        }
        return null;
    }
    
    private int getRandomStatus() {
        final int[] statuss = { 2087, 2088, 2089 };
        final int random = RandomUtil.round(0, statuss.length - 1);
        return statuss[random];
    }
    
    private int getOldStatus(final FightObject fightObject) {
        if (fightObject.getStatusValue(2087) > 0) {
            return 2087;
        }
        if (fightObject.getStatusValue(2088) > 0) {
            return 2088;
        }
        if (fightObject.getStatusValue(2089) > 0) {
            return 2089;
        }
        return 0;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 2;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(83037);
        if (skill != null && !target.isFrozen()) {
            final double hurt = CountUtils.getPhyAttack(effector);
            final Effect e = new Effect();
            final int harm = PetSkillEffectorUtil.wuXingFireCount(fight, effector, target, hurt);
            e.setType(100000);
            e.setHurt(harm);
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
    
    @Override
    public List<Integer> getTargetIdsForTFQX(final Fight fight, final FightObject effector, final Object... args) {
        if (args != null && args.length > 0) {
            final int status = this.getStatus(effector);
            if (status > 0) {
                final int skillSerial = (int)args[0];
                if (status == 2088 && (skillSerial == 80007 || skillSerial == 81012)) {
                    return PetTriggerPassiveSkill.getTFQXTargetIds(fight, effector, 83037);
                }
                if (status == 2089 && (skillSerial == 80009 || skillSerial == 81013)) {
                    return PetTriggerPassiveSkill.getTFQXTargetIds(fight, effector, 83037);
                }
            }
        }
        return null;
    }
    
    private int getStatus(final FightObject fightObject) {
        if (fightObject.getStatusValue(2088) > 0) {
            return 2088;
        }
        if (fightObject.getStatusValue(2089) > 0) {
            return 2089;
        }
        return 0;
    }
}
