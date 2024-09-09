package com.kitty.system.game.fightnew.book.tianfu.gattaca;

import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.bean.TianFuObject;
import com.kitty.system.game.fightnew.bean.DayInCeFight;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.enums.war.warskill.skilldef.SKILLATTR;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.tianfu.pojo.TianFuAddition;
import com.kitty.system.game.fightnew.book.tianfu.TianFuProcessor;

public class FU_324402 extends TianFuProcessor
{
    public FU_324402() {
    }
    
    @Override
    public NutMap addition(TianFuAddition tianFuAddition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill roleSkill = (RoleSkill)args[1];
        if (roleSkill == null || roleSkill.getSkillId() == null || (int)roleSkill.getSkillId() < 1) {
            return null;
        }
        SysSkill sysSkill = (SysSkill)SkillDataPool.skillMap.get(roleSkill.getSkillId());
        if (sysSkill == null) {
            return null;
        }
        if (4 != (int)sysSkill.getStep()) {
            return null;
        }
        if (SKILLATTR.SKILLATTR_THUNDER.getValue() != (int)roleSkill.getType()) {
            return null;
        }
        if (!effector.isPlayer()) {
            return null;
        }
        DayInCeFight dayInCeFight = ((PlayerFightObject)effector).getDayInCeFight();
        TianFuObject tianFuObject = TianFuUtils.getTianFuObject(dayInCeFight, 324402);
        if (tianFuObject == null) {
            return null;
        }
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 324402);
        int isPvp = fight.isPvp();
        if (0 == isPvp) {
            double percentb = 5.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int hurt = (int)args[0];
            skillRecord.setT((long)(int)target.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percentb);
        }
        if (1 == isPvp && fight.getRound() % 2 == 0) {
            double percenta = 10.0 * TianFuUtils.getTianFuCount(tianFuObject.getLevel(), dayInCeFight.getLevel()) / 100.0;
            int hurt = (int)args[0];
            skillRecord.setT((long)(int)target.getId());
            return TianFuUtils.getHurtReturnMap(hurt, percenta);
        }
        return null;
    }
}
