
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83028 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        final double percent = BigDecimalUtil.decimalScaleObjectDown(3, (0.1 + 6 * (effector.getRelive() * 1 + 1) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose())) / 100.0);
        final boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag && effector.getSerial() == 21063) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83028);
            final int round = 3;
            effector.getStatusMap().setv(String.valueOf(1011), (Object)round);
            effector.getStatusMap().setv("1011value", (Object)99);
            final ActionDetailParam actionDetailParam = ActionParamUtils.addStatusReturn(effector, effector, 7, 83028, 2, 1011, round);
            final boolean awakenFlag = AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 51508);
            final FightObject host = fight.queryFightObject(effector.getHostId());
            if (awakenFlag && host == null && !host.isFallDown()) {
                host.getStatusMap().setv(String.valueOf(1011), (Object)round);
                final FightObjectParam hostParam = new FightObjectParam();
                hostParam.setObjId(host.getId());
                hostParam.setOrder(actionDetailParam.getTargetParamList().size() + 1);
                hostParam.setMissed(2);
                hostParam.getStatusMap().put(1011, round);
                actionDetailParam.getTargetParamList().add(hostParam);
            }
            skillRecord.setT(effector.getId());
        }
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        return 0;
    }
}
