package com.kitty.system.game.fightnew.book.pet.normal;

import java.util.List;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.ArrayList;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import java.util.Map;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_80003 extends SkillProcessor
{
    private int reduce;
    
    public SKILL_80003() {
        this.reduce = 0;
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 80003);
        Map<Integer, SysPetSkill> sysPetSkillMap = (Map)PetTriggerPassiveSkill.data_pet_skill_maps.get(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue());
        SysPetSkill petSkill = (SysPetSkill)sysPetSkillMap.get(Integer.valueOf(80003));
        if (petSkill != null) {
            SysPetSkill sysPetSkill = PetSkillEffectorUtil.initiativeQueryCover(petSkill, effector.getSkillIds(), sysPetSkillMap);
            if (sysPetSkill != null && 80003 != (int)sysPetSkill.getSerial()) {
                SkillProcessor processor = SkillProcessor.queryProcessor(sysPetSkill.getSerial());
                processor.changeVariate(Integer.valueOf(this.reduce));
                return processor.effect(fight, effector, target, args);
            }
        }
        int lastHurt = 0;
        double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(8), Double.valueOf((10.1 + 2.0 * ((double)effector.getRelive() * 0.4 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())) / 100.0));
        int hurt = (int)((double)this.reduce * percent);
        if (!target.isFrozen()) {
            lastHurt = PetSkillEffectorUtil.computeTMFGQMXLHurt(effector, target, hurt, 80003);
            double weak = effector.getSelfAllTempPro(PRO_DICT_TYPE.BURN_HURT.getDist()) - target.getSelfAllTempPro(PRO_DICT_TYPE.SH_DK.getDist());
            lastHurt = (int)((double)lastHurt + (double)lastHurt * weak);
            skillRecord.setT((long)(int)target.getId());
        }
        Effect effect = new Effect(100000, 0, lastHurt, -1.0, new ArrayList<>());
        return effect;
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
    
    @Override
    public int costLife(FightObject effector) {
        if (effector.getCurrLife() >= 10000) {
            int hp = BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf((double)effector.getCurrLife() * 0.95));
            return this.reduce = hp;
        }
        return 10000;
    }
    
    @Override
    public List<Integer> getTargetIds(Fight fight, FightObject effector) {
        List<Integer> list = PetTriggerPassiveSkill.getTMTargetIds(fight, effector, 80003);
        return list;
    }
}
