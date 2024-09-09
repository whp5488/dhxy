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
    public SKILL_830344() {
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
        List<FightObject> targetSide = fight.getTargetSide((int)effector.getId());
        return targetSide.size();
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(830344));
        if (skill != null) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 830344);
            int time = 3;
            Effect e = new Effect();
            if (!target.isFrozen()) {
                List<Integer> clearList = PetSkillEffectorUtil.clearWuXin(target);
                if (clearList != null) {
                    e.getOrCreateList().add(new FiedValue("targetClearStatus", (byte)7, clearList));
                }
                double persent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf(50.0 + 4.6 * ((double)effector.getRelive() * 0.5 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
                ImbueUtil.addOrUpdStatus(target.getImbueMap(), 2080, PRO_DICT_TYPE.WX_H.getDist(), time, persent);
                e.setType(2080);
                e.setTime(time);
            }
            skillRecord.setT((long)(int)target.getId());
            return e;
        }
        else {
            return null;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 21;
    }
    
    @Override
    public boolean excuteSkillCool(Fight fight, FightObject effector, Object... args) {
        int skillCoolTime = 5;
        effector.getSkillCoolMap().setv(String.valueOf(83034), Integer.valueOf(skillCoolTime));
        return true;
    }
}
