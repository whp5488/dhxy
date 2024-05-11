
package com.kitty.system.game.fightnew.book.pet.talent;

import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.utils.SkillCompute;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83020 extends SkillProcessor
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
        final List<FightObject> targetSide = fight.getTargetSide(effector.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (targetSide.size() < 2) {
            return targetSide.size();
        }
        return 2;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(83020);
        final Effect e = new Effect();
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83020);
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            final double hurt = BigDecimalUtil.decimalScaleObjectDown(0, 80 * level + effector.getMaxMana() / 100 * 6 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
            int harm = 0;
            if (!target.isFrozen()) {
                final SysPetSkill sysSkill = SkillDataPool.petSkillMap.get(83020);
                final ArrayList<FightObject> objects = fight.getSideAllFightObjects(effector.isASide());
                int die = 0;
                for (final FightObject object : objects) {
                    if (object.isFallDown()) {
                        ++die;
                    }
                }
                final double[] power = sysSkill.getPower();
                Integer proficient = skill.getProficient();
                if (proficient == null) {
                    proficient = 1;
                }
                final double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_GH.getDist());
                final double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                final double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_GH.getDist());
                final double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_GH.getDist());
                final double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                harm = SkillCompute.computeSkillDamageWildfire(effector, power, effector.getLevel(), proficient, kx, hkx, extra, weak, die, wx);
                harm += (int)hurt;
            }
            e.setType(100000);
            e.setHurt(harm);
            skillRecord.setT(target.getId());
        }
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
}
