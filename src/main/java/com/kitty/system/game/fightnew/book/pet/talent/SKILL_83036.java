
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.game.fightnew.utils.PassiveSkillUtils;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83036 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        if (effector.isPet()) {
            this.setStatus(fight, effector);
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        int count = 1;
        final int status = this.haveStatus(effector);
        if (status > 0) {
            if (2082 == status || 2083 == status || 2084 == status) {
                count = 2;
            }
            else if (2085 == status || 2086 == status) {
                count = 3;
            }
        }
        return count;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(83036);
        final Effect e = new Effect();
        int harm = 0;
        if (skill != null && !target.isFrozen()) {
            final int hurt = this.getHurt(effector);
            double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_GH.getDist());
            final double wsk = PassiveSkillUtils.getWsk(effector);
            kx -= wsk;
            final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_GH.getDist());
            final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_GH.getDist());
            final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
            harm = SkillCompute.computePetSkillDamage(hurt, kx, hkx, extra, weak, wx);
        }
        e.setType(100000);
        e.setHurt(harm);
        return e;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        if (args != null && args.length >= 2) {
            final FightObject effector = (FightObject)args[0];
            if (effector != null) {
                return effector.getLevel() * 32;
            }
        }
        return 0;
    }
    
    @Override
    public void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (args != null && args.length > 0) {
            final int skillId = (int)args[0];
            if (83036 == skillId) {
                final int status = this.haveStatus(effector);
                if (status > 0) {
                    effector.removeStatus(status);
                    final FightObjectParam attackerParam = actionDetailParam.gainAttackerParam();
                    attackerParam.addClearStatus(status);
                }
            }
        }
    }
    
    private int haveStatus(final FightObject fightObject) {
        if (fightObject.getStatusValue(2082) > 0) {
            return 2082;
        }
        if (fightObject.getStatusValue(2083) > 0) {
            return 2083;
        }
        if (fightObject.getStatusValue(2084) > 0) {
            return 2084;
        }
        if (fightObject.getStatusValue(2085) > 0) {
            return 2085;
        }
        if (fightObject.getStatusValue(2086) > 0) {
            return 2086;
        }
        return 0;
    }
    
    private void setStatus(final Fight fight, final FightObject effector) {
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 43, 83036, effector.getId());
        final int[] addStatus = new int[2];
        int clearStatus = 0;
        final int time = 2;
        addStatus[1] = time;
        if (effector.getStatusValue(2082) > 0) {
            effector.setStatus(2083, time);
            addStatus[0] = 2083;
            effector.removeStatus(2082);
            clearStatus = 2082;
        }
        else if (effector.getStatusValue(2083) > 0) {
            effector.setStatus(2084, time);
            addStatus[0] = 2084;
            effector.removeStatus(2083);
            clearStatus = 2083;
        }
        else if (effector.getStatusValue(2084) > 0) {
            effector.setStatus(2085, time);
            addStatus[0] = 2085;
            effector.removeStatus(2084);
            clearStatus = 2084;
        }
        else if (effector.getStatusValue(2085) > 0) {
            effector.setStatus(2086, time);
            addStatus[0] = 2086;
            effector.removeStatus(2085);
            clearStatus = 2085;
        }
        else if (effector.getStatusValue(2086) > 0) {
            effector.setStatus(2086, time);
            addStatus[0] = 2086;
        }
        else {
            effector.setStatus(2082, time);
            addStatus[0] = 2082;
        }
        final FightObjectParam targetParam = ActionParamUtils.addStatusForTarget(effector, 2, addStatus[0], addStatus[1], 1);
        if (clearStatus > 0) {
            targetParam.addClearStatus(clearStatus);
        }
        actionDetailParam.getTargetParamList().add(targetParam);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
    }
    
    private int getHurt(final FightObject effector) {
        double coefficient = 0.0;
        final int status = this.haveStatus(effector);
        switch (status) {
            case 2082: {
                coefficient = 1.0;
                break;
            }
            case 2083: {
                coefficient = 1.6;
                break;
            }
            case 2084: {
                coefficient = 2.4;
                break;
            }
            case 2085: {
                coefficient = 3.2;
                break;
            }
            case 2086: {
                coefficient = 4.0;
                break;
            }
        }
        final int param1 = 80 * effector.getLevel();
        final double param2 = effector.getMaxMana() / 100.0 * 6.0 * (effector.getRelive() * 0.6 + 1.0);
        final double param3 = Math.pow(effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0 / (100 + effector.getRelive() * 20);
        return BigDecimalUtil.decimalScaleIntegerDown(0, (param1 + param2 * param3) * coefficient);
    }
}
