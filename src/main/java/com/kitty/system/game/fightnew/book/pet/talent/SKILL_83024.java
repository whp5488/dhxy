package com.kitty.system.game.fightnew.book.pet.talent;

import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import java.util.Iterator;
import com.kitty.system.game.fightRecord.domain.SkillRecord;
import java.util.List;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.book.pet.utils.PetSkillEffectorUtil;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import java.util.Random;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.fightnew.spectator.pool.FightParamPool;
import com.kitty.system.game.fightnew.book.vo.Effect;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.book.vo.Addition;
import com.kitty.system.game.fightnew.book.SkillProcessor;

public class SKILL_83024 extends SkillProcessor
{
    public SKILL_83024() {
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
        SkillRecord skillRecord = FightParamPool.addSkillRecord(fight, 7, (long)(int)effector.getId(), 83024);
        int num = effector.getSkillCoolMap().getInt("83024value");
        boolean isExcute = false;
        if (num == -1) {
            RoleSkill skill = new RoleSkill();
            skill.setType(Integer.valueOf(0));
            skill.setSkillId(Integer.valueOf(83024));
            BattleUnit battleUnit2 = new BattleUnit(fight, effector, target, skill);
            ActionDetailParam actionDetailParam2 = PetPassiveSkillUtil.passivePhysics(fight, effector, target, battleUnit2, null, 83024);
            RespFightShouting fightShouting = ((FightMessageNewService)SpringUtils.getBean(FightMessageNewService.class)).fightShouting(effector, "法攻");
            FightObjectParam attackerParam = actionDetailParam2.gainAttackerParam();
            ActionParamUtils.updateObjectInfo(effector, 7, 84003, 2, fightShouting, attackerParam);
            if (args.length > 1) {
                List<ActionDetailParam> actionDetailParams = (List)args[1];
                if (actionDetailParams == null) {
                    actionDetailParams = new ArrayList<>();
                }
                actionDetailParams.add(actionDetailParam2);
                FightObjectParam targetParam2 = actionDetailParam2.gainTargetParam((int)target.getId(), false);
                if (targetParam2 != null && targetParam2.isHit()) {
                    ++num;
                    effector.getSkillCoolMap().setv(String.valueOf(83024), Integer.valueOf(1));
                    effector.getSkillCoolMap().setv("83024value", Integer.valueOf(num));
                    this.effect(fight, effector, target, Boolean.valueOf(false), actionDetailParam2);
                }
            }
            skillRecord.setT((long)(int)target.getId());
            return null;
        }
        else {
            if (num == 0) {
                isExcute = true;
            }
            else if (num > 0 && num < 3) {
                double percent = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf((156.0 + ((double)effector.getRelive() * 0.5 + 1.0) * (Math.pow((double)effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0)) / 10.0 / 100.0));
                boolean flag = SkillAi.canAffectOnPro(percent);
                if (flag) {
                    isExcute = true;
                }
            }
            if (isExcute) {
                ActionDetailParam actionDetailParam = null;
                if (args.length > 1) {
                    actionDetailParam = (ActionDetailParam)args[1];
                }
                if (actionDetailParam == null) {
                    return null;
                }
                double hitRate = BigDecimalUtil.decimalScaleObjectDown(Integer.valueOf(3), Double.valueOf(((double)effector.getRelive() * 0.12 + 0.3) * (Math.pow((double)effector.getLevel(), 0.5) / 10.0 + Math.pow((double)effector.getClose(), 0.16666666666666666) * 10.0) / 10.0 / 100.0));
                ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.HIT_RATIO.getDist()), 1, hitRate);
                ImbueUtil.addRound(effector.getImbueMap(), Integer.valueOf(PRO_DICT_TYPE.CONQUER_EXTENT.getDist()), 1, hitRate);
                Random rd = new Random();
                boolean isHorizontal = rd.nextBoolean();
                FightObject newTarget = null;
                List<Integer> targetIds = effector.getSkillCoolMap().getList("83024value_targets", Integer.class);
                List<FightObject> targetSide = SkillTargetPicker.getFightObjects(fight, effector, true);
                if (targetIds != null && targetIds.size() > 0) {
                    List<FightObject> removeList = new ArrayList<>();
                    for (Integer targetId : targetIds) {
                        FightObject historyTarget = fight.queryFightObject(targetId);
                        if (historyTarget != null) {
                            removeList.add(historyTarget);
                        }
                        else {
                            continue;
                        }
                    }
                    if (removeList.size() > 0) {
                        for (FightObject fightObject : removeList) {
                            targetSide.remove(fightObject);
                        }
                    }
                }
                if (targetSide.size() > 0) {
                    newTarget = PetSkillEffectorUtil.horizontalLongitudinalTarget(isHorizontal, fight, effector, targetSide, target);
                    if (newTarget == null) {
                        newTarget = PetSkillEffectorUtil.horizontalLongitudinalTarget(!isHorizontal, fight, effector, targetSide, target);
                    }
                    if (newTarget == null) {
                        return null;
                    }
                    if (targetIds == null) {
                        targetIds = new ArrayList<>();
                    }
                    if (num == 0) {
                        targetIds.add(target.getId());
                    }
                    targetIds.add(newTarget.getId());
                    RoleSkill skillBy = effector.getSkillBy(Integer.valueOf(83024));
                    BattleUnit battleUnit = new BattleUnit(fight, effector, newTarget, skillBy);
                    ActionDetailParam actionDetailParamT = PetPassiveSkillUtil.passivePhysics(fight, effector, newTarget, battleUnit, null, 83024);
                    if (actionDetailParamT.getTargetParamList() != null) {
                        for (FightObjectParam targetParam : actionDetailParamT.getTargetParamList()) {
                            targetParam.addDamageType(105);
                        }
                    }
                    actionDetailParam.getTargetParamList().addAll(actionDetailParamT.getTargetParamList());
                    skillRecord.setT((long)(int)newTarget.getId());
                    ++num;
                    effector.getSkillCoolMap().setv(String.valueOf(83024), Integer.valueOf(1));
                    effector.getSkillCoolMap().setv("83024value", Integer.valueOf(num));
                    effector.getSkillCoolMap().setv("83024value_targets", targetIds);
                    this.effect(fight, effector, target, Boolean.valueOf(false), actionDetailParam);
                    return null;
                }
            }
            return null;
        }
    }
    
    @Override
    public int costMana(RoleSkill skill, Object... args) {
        return 0;
    }
}
