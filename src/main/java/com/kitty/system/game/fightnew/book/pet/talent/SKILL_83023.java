
package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import java.util.Collection;
import com.kitty.system.game.fightnew.utils.FightRateUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.book.vo.Effect;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
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
    @Override
    public NutMap addition(final Addition addition, final Object... args) {
        return null;
    }
    
    @Override
    public NutMap passive(final Fight fight, final FightObject effector, final Object... args) {
        return null;
    }
    
    @Override
    public void normalAttack(final Fight fight, final FightObject effector, final FightObject target, final ActionDetailParam actionDetailParam, final Object... args) {
        if (args != null && args.length > 0) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83023);
            int skillSerial = 0;
            int count = 1;
            try {
                skillSerial = (int)args[0];
                if (args.length > 1) {
                    count = (int)args[1];
                }
            }
            catch (final NumberFormatException e) {
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
            final double residueLifeRatio = Double.parseDouble(BigDecimalUtil.divide(String.valueOf(target.getCurrLife()), String.valueOf(target.getMaxLife())));
            if (residueLifeRatio >= 0.3) {
                return;
            }
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            final double hurt = BigDecimalUtil.decimalScaleObjectDown(0, 80 * level + effector.getMaxMana() / 100 * 6 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
            final List<FightObject> targets = fight.getTargetSide(effector.getId()).stream().filter(fightObject -> !fightObject.isFallDown()).sorted(Comparator.comparing(FightObject::getSpeed).reversed()).collect(Collectors.toList());
            if (targets.size() <= 0) {
                return;
            }
            final FightObject targetNew = targets.get(0);
            final Effect e2 = new Effect();
            final int harm = PetSkillEffectorUtil.wuXingWaterCount(fight, effector, target, hurt);
            e2.setType(100000);
            e2.setHurt(harm);
            e2.setOtherType("add_to_target");
            final RoleSkill skill = effector.getSkillBy(83023);
            final BattleUnit battleUnit = new BattleUnit(fight, effector, targetNew, skill);
            final boolean stunt = FightRateUtil.calcMagicStuntRate(effector, skill);
            battleUnit.setStunt(stunt);
            battleUnit.setSideId(0);
            battleUnit.setDoubleHit(1);
            battleUnit.setEffect(e2);
            battleUnit.triggerMagic();
            final ActionDetailParam actionDetailParamNew = battleUnit.getActionDetailParam();
            if (actionDetailParamNew != null && actionDetailParamNew.getTargetParamList().size() > 0) {
                actionDetailParam.getTargetParamList().addAll(actionDetailParamNew.getTargetParamList());
            }
            final List<Integer> targetList = new ArrayList<Integer>();
            targetList.add(targetNew.getId());
            SpringUtils.getBean(FightNewService.class).checkOrderTarget(fight, targetList);
            if (effector.isFallDown() && !fight.getDeadPetIds().contains(effector.getId())) {
                fight.onActorDie(effector);
            }
            final double addAmount = 0.1;
            ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.S_KB_RATIO.getDist(), 1, addAmount);
            skillRecord.setT(targetNew.getId());
            ++count;
            this.normalAttack(fight, effector, targetNew, actionDetailParam, skill.getSkillId(), count);
        }
    }
    
    @Override
    public int targetCount(final Fight fight, final FightObject effector, final Object... args) {
        return 1;
    }
    
    @Override
    public Effect effect(final Fight fight, final FightObject effector, final FightObject target, final Object... args) {
        final RoleSkill skill = effector.getSkillBy(83023);
        final Effect e = new Effect();
        if (skill != null) {
            final SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, effector.getId(), 83023);
            int level = effector.getLevel();
            if (level <= 0) {
                level = 1;
            }
            final double hurt = BigDecimalUtil.decimalScaleObjectDown(0, 80 * level + effector.getMaxMana() / 100 * 6 * (effector.getRelive() * 0.6 + 1.0) * PetSkillEffectorUtil.getSuffix(effector.getLevel(), effector.getRelive(), effector.getClose()));
            final double addAmount = 0.1;
            final int harm = PetSkillEffectorUtil.wuXingWaterCount(fight, effector, target, hurt);
            e.setType(100000);
            try {
                e.setHurt(harm);
            }
            catch (final Exception e2) {
                e2.printStackTrace();
            }
            e.setOtherType("add_to_target");
            ImbueUtil.addRound(effector.getImbueMap(), PRO_DICT_TYPE.S_KB_RATIO.getDist(), 1, addAmount);
            skillRecord.setT(target.getId());
        }
        return e;
    }
    
    @Override
    public int costMana(final RoleSkill skill, final Object... args) {
        final int level = PetSkillEffectorUtil.getExcuteLevel(args);
        return level * 32;
    }
}
