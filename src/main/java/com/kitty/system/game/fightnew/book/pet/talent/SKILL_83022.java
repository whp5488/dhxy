package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.system.game.vo.utils.StatusConst;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.awaken.utils.AwakenSkillUtils;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83022 extends SkillProcessor
{
    public SKILL_83022() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83022);
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((60.0 + 1.2 * (double)(effector.getRelive() * 3 + 1) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        percent = Math.min(0.95, percent);
        percent += AwakenSkillUtils.skillIntensifyDouble(fight, effector, 31309, null);
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag && args.length > 0 && args[0] instanceof FightObject) {
            FightObject unit = (FightObject)args[0];
            List<Integer> clearStatusList = unit.getStatusList(StatusConst.getControlStatus());
            if (clearStatusList.size() > 0) {
                unit.clearStatus(clearStatusList);
                ActionParamUtils.clearStatus(fight, effector, unit, 7, 83022, 2, clearStatusList);
            }
            skillRecord.setT((long)(int)unit.getId());
        }
        return null;
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        return null;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
