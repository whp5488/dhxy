package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83008 extends SkillProcessor
{
    public SKILL_83008() {
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
        int time = 3;
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(83008));
        Effect e = new Effect();
        if (skill != null) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83008);
            if (!target.isFrozen()) {
                double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 5.0 * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
                int speed = target.getSpeed();
                int result = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(speed), String.valueOf(percent)));
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1016, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, (double)(-result));
                e.setTime(time);
                e.setType(1016);
            }
            skillRecord.setT((long)(int)target.getId());
            return e;
        }
        else {
            return e;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 22;
    }
}
