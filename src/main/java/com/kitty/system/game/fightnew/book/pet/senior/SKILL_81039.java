
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
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

public class SKILL_81039 extends SkillProcessor
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
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81039);
        final int time = 3;
        double increment = 50.0;
        final Effect effect = new Effect();
        if (!target.isFrozen()) {
            final List<Integer> clearList = PetSkillEffectorUtil.clearWuXin(target);
            if (clearList != null) {
                effect.getOrCreateList().add(new FiedValue("targetClearStatus", (byte)7, clearList));
            }
            final int add = (int)AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31411, target);
            if (add > 0) {
                increment += add;
            }
            ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2063, PRO_DICT_TYPE.WX_M.getDist(), time, increment);
            effect.setType(2063);
            effect.setTime(time);
            skillRecord.setT(target.getId());
        }
        return effect;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
    
    @Override
    public boolean excuteSkillCool(final Fight fight, final FightObject effector, final Object... args) {
        final int skillCoolTime = 5;
        effector.getSkillCoolMap().setv(String.valueOf(81039), (Object)skillCoolTime);
        return true;
    }
}
