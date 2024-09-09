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
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83020 extends SkillProcessor
{
    public SKILL_83020() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        List<FightObject> targetSide = (List)fight.getTargetSide((int)effector.getId()).stream().filter(obj -> !obj.isFallDown()).collect(Collectors.toList());
        if (targetSide.size() < 2) {
            return targetSide.size();
        }
        return 2;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(83020));
        Effect e = new Effect();
        if (skill != null) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83020);
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(80 * level) + (double)(effector.getMaxMana() / 100 * 6) * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
            int harm = 0;
            if (!target.isFrozen()) {
                SysPetSkill sysSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(Integer.valueOf(83020));
                ArrayList<FightObject> objects = fight.getSideAllFightObjects(effector.isASide());
                int die = 0;
                for (FightObject object : objects) {
                    if (object.isFallDown()) {
                        ++die;
                    }
                    else {
                        continue;
                    }
                }
                double[] power = sysSkill.getPower();
                Integer proficient = skill.getProficient();
                if (proficient == null) {
                    proficient = Integer.valueOf(1);
                }
                double kx = target.getSelfAllTempPro(PRO_DICT_TYPE.K_GH.getDist());
                double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
                double hkx = effector.getSelfAllTempPro(PRO_DICT_TYPE.OMIT_K_GH.getDist());
                double extra = effector.getSelfAllTempPro(PRO_DICT_TYPE.RISE_GH.getDist());
                double wx = SkillFunc.getWuXingKeZhiXiuZheng(effector, target, fight.getType());
                harm = (int)SkillCompute.computeSkillDamageWildfire(effector, power, effector.getLevel(), (int)proficient, kx, hkx, extra, weak, die, wx);
                harm = (int)((double)harm + hurt);
            }
            e.setType(100000);
            e.setHurt(harm);
            skillRecord.setT((long)(int)target.getId());
            return e;
        }
        else {
            return e;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        if (args != null && args.length >= 2) {
            FightObject effector = (FightObject)args[0];
            if (effector != null) {
                return effector.getLevel() * 32;
            }
        }
        return 0;
    }
}
