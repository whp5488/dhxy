
package com.kitty.system.game.fightnew.book.pet.senior;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_81005 extends SkillProcessor
{
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        if (effector.isFallDown()) {
            return null;
        }
        final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 81005);
        final double addK = 0.01;
        final double maxK = 0.1;
        final double k_hl = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()));
        final double k_hs = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()));
        final double k_fy = effector.getPropertyMap().getDouble(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()));
        final boolean isAddHl = PetSkillEffectorUtil.addAttrObj(fight, effector, addK, maxK, PRO_DICT_TYPE.K_HL.getDist());
        if (isAddHl) {
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HL.getDist()), (Object)(k_hl + addK));
        }
        final boolean isAddHs = PetSkillEffectorUtil.addAttrObj(fight, effector, addK, maxK, PRO_DICT_TYPE.K_HS.getDist());
        if (isAddHs) {
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_HS.getDist()), (Object)(k_hs + addK));
            skillRecord.setT(effector.getId());
        }
        final boolean isAddFy = PetSkillEffectorUtil.addAttrObj(fight, effector, addK, maxK, PRO_DICT_TYPE.K_FY.getDist());
        if (isAddFy) {
            effector.getPropertyMap().setv(String.valueOf(PRO_DICT_TYPE.K_FY.getDist()), (Object)(k_fy + addK));
            skillRecord.setT(effector.getId());
        }
        if (!isAddHl || !isAddHs || !isAddFy) {
            return null;
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
