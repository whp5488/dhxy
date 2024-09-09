package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.Arrays;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83001 extends SkillProcessor
{
    public SKILL_83001() {
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
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83001);
        int round = 2;
        if (args.length > 0 && args[0] instanceof Integer) {
            double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 4.0 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
            percent += AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31304, target);
            ImbueUtil.addRound(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.RISE_SS.getDist()), round, -percent);
            ImbueUtil.addRound(target.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.SH_DK.getDist()), round, -percent);
            int hurt = (int)args[0];
            int addHurt = (int)((double)hurt * percent);
            Effect effect = new Effect(1012, round, hurt + addHurt, 0.0, Arrays.asList(new FiedValue[0]));
            effect.setTime(round);
            skillRecord.setT((long)(int)target.getId());
            return effect;
        }
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
