package com.kitty.system.game.fightnew.book.pet.normal;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.List;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.book.acsrypet.utils.AcsryPetSkillUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_80014 extends SkillProcessor
{
    public SKILL_80014() {
    }
    
    @Override
    public NutMap addition(Addition addition, Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(Fight fight, FightObject effector, Object... args) {
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 80014);
        SysPetSkill petSkill = (SysPetSkill)args[0];
        if (PetSkillEffectorUtil.checkCover(petSkill, fight, effector, null, false, args)) {
            return null;
        }
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((0.1 + 2.0 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        double pval = AcsryPetSkillUtil.getPval(80014, effector);
        if (pval > 0.0) {
            percent += pval;
        }
        boolean flag = SkillAi.canAffectOnPro(percent);
        if (flag) {
            if (!effector.isConfusion()) {
                return null;
            }
            effector.removeStatus(1002);
            List<Integer> list = new ArrayList<>();
            list.add(Integer.valueOf(1002));
            ActionParamUtils.clearStatus(fight, effector, effector, 7, 80014, 2, list);
            skillRecord.setT((long)(int)effector.getId());
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
