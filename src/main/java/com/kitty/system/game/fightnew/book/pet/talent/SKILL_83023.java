package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.FightRateUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.stream.Collectors;
import java.util.Comparator;
import java.util.List;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83023 extends SkillProcessor
{
    public SKILL_83023() {
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
    public void normalAttack(Fight fight, FightObject effector, FightObject target, ActionDetailParam actionDetailParam, Object... args) {
        if (args != null && args.length > 0) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83023);
            int skillSerial = 0;
            int count = 1;
            try {
                skillSerial = (int)args[0];
                if (args.length > 1) {
                    count = (int)args[1];
                }
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (target == null) {
                return;
            }
            if (count > 2) {
                return;
            }
            if (83023 != skillSerial) {
                return;
            }
            double residueLifeRatio = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(target.getCurrLife()), String.valueOf(target.getMaxLife())));
            if (residueLifeRatio >= 0.3) {
                return;
            }
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(80 * level) + (double)(effector.getMaxMana() / 100 * 6) * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
            List<FightObject> targets = (List)fight.getTargetSide((int)effector.getId()).stream().filter(fightObject -> !fightObject.isFallDown()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            if (targets.size() <= 0) {
                return;
            }
            FightObject targetNew = (FightObject)targets.get(0);
            Effect e2 = new Effect();
            int harm = PetSkillEffectorUtil.wuXingWaterCount(fight, effector, target, hurt);
            e2.setType(100000);
            e2.setHurt(harm);
            e2.setOtherType("add_to_target");
            RoleSkill skill = effector.getSkillBy(Integer.valueOf(83023));
            BattleUnit battleUnit = new BattleUnit(fight, effector, targetNew, skill);
            boolean stunt = FightRateUtil.calcMagicStuntRate(effector, skill);
            battleUnit.setStunt(stunt);
            battleUnit.setSideId(0);
            battleUnit.setDoubleHit(1);
            battleUnit.setEffect(e2);
            battleUnit.triggerMagic();
            ActionDetailParam actionDetailParamNew = battleUnit.getActionDetailParam();
            if (actionDetailParamNew != null && actionDetailParamNew.getTargetParamList().size() > 0) {
                actionDetailParam.getTargetParamList().addAll(actionDetailParamNew.getTargetParamList());
            }
            List<Integer> targetList = new ArrayList<>();
            targetList.add(targetNew.getId());
            ((FightNewService)SpringUtils.getBean(FightNewService.class)).checkOrderTarget(fight, targetList);
            if (effector.isFallDown() && !fight.getDeadPetIds().contains(effector.getId())) {
                fight.onActorDie(effector);
            }
            double addAmount = 0.1;
            ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), 1, addAmount);
            skillRecord.setT((long)(int)targetNew.getId());
            ++count;
            this.normalAttack(fight, effector, targetNew, actionDetailParam, skill.getSkillId(), Integer.valueOf(count));
        }
    }
    
    @Override
    public int targetCount(Fight fight, FightObject effector, Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(Fight fight, FightObject effector, FightObject target, Object... args) {
        RoleSkill skill = effector.getSkillBy(Integer.valueOf(83023));
        Effect e = new Effect();
        if (skill != null) {
            SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83023);
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            double hurt = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(0), Double.valueOf((double)(80 * level) + (double)(effector.getMaxMana() / 100 * 6) * ((double)effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(Integer.valueOf(effector.getLevel()), Integer.valueOf(effector.getRelive()), effector.getClose())));
            double addAmount = 0.1;
            int harm = PetSkillEffectorUtil.wuXingWaterCount(fight, effector, target, hurt);
            e.setType(100000);
            try {
                e.setHurt(harm);
            }
            catch (Exception e2) {
                e2.printStackTrace();
            }
            e.setOtherType("add_to_target");
            ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.S_KB_RATIO.getDist()), 1, addAmount);
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
        return level * 32;
    }
}
