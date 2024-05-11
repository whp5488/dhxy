
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_830344 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        final List<FightObject> targetSide = fight.getTargetSide(effector.getId());
        return targetSide.size();
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(830344);
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 830344);
            final int time = 3;
            final Effect e = new Effect();
            if (!target.isFrozen()) {
                final List<Integer> clearList = PetSkillEffectorUtil.clearWuXin(target);
                if (clearList != null) {
                    e.getOrCreateList().add(new FiedValue("targetClearStatus", (byte)7, clearList));
                }
                final double persent = BigDecimalUtil.decimalScaleObjectDown(0, 50.0 + 4.6 * (effector.getRelive() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2080, PRO_DICT_TYPE.WX_H.getDist(), time, persent);
                e.setType(2080);
                e.setTime(time);
            }
            skillRecord.setT(target.getId());
            return e;
        }
        return null;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        final int skillCoolTime = 5;
        effector.getSkillCoolMap().setv(String.valueOf(83034), (Object)skillCoolTime);
        return true;
    }
}
