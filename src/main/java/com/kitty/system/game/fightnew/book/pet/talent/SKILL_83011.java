package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83011 extends SkillProcessor
{
    public SKILL_83011() {
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
        int count = 1;
        if (AwakenSkillUtils.skillIntensifyBoolean(fight, effector, 31410)) {
            ++count;
        }
        return count;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(83011));
        int time = 3;
        int speed = target.getSpeed();
        Effect e = new Effect();
        if (skill != null && speed > 0) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83011);
            if (!target.isFrozen()) {
                double percent = 0.15;
                int result = (int)Double.parseDouble(BigDecimalUtil.multiply(String.valueOf(speed), String.valueOf(percent)));
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 1010, PRO_DICT_TYPE.RISE_SPEED.getDist(), time, (double)result);
                e.setTime(time);
                e.setType(1010);
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
