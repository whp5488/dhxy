
package com.kitty.system.game.fightnew.book.monster.star18;

import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillUtils;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.fightnew.utils.FightUtils;
import com.kitty.system.game.fightnew.utils.SkillAi;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.model.BattleUnit;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.List;
import com.kitty.system.game.fightnew.utils.SkillTargetPicker;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.utils.ActionParamUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.book.monster.MonsterSkillProcessor;

public class MONSTERSKILL_30043 extends MonsterSkillProcessor
{
    @Override
    public void beforeAttack(final Fight fight, final FightObject effector, final Object... args) {
        if (fight.getSpecialMonsterType() == 1 && effector.getPos() == 10003 && effector.getName().equals("地飞星")) {
            if (effector.getSkillCoolMap().getInt(String.valueOf(30043)) > 0) {
                return;
            }
            final RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(effector, "掌握旷世奇功摄魂大法，真是孤芳自赏啊", 2000, 1000);
            ActionParamUtils.fightShouting(fight, effector, effector, 10, 10, 2, fightShouting);
            effector.getSkillCoolMap().setv(String.valueOf(30043), (Object)2);
            final int[] attackCounts = { 4, 5, 6 };
            final int round = RandomUtil.round(0, attackCounts.length - 1);
            for (int count = attackCounts[round], i = 0; i < count; ++i) {
                List<FightObject> targets = SkillTargetPicker.getFightObjects(fight, effector, true);
                if (targets != null && targets.size() > 0) {
                    targets = targets.stream().filter(obj -> obj.isPet()).collect(Collectors.toList());
                    if (targets != null && targets.size() > 0) {
                        final int index = RandomUtil.round(0, targets.size() - 1);
                        final FightObject targetPet = targets.get(index);
                        this.excuteSkill(fight, effector, targetPet);
                    }
                }
            }
        }
    }
    
    private void excuteSkill(final Fight fight, final FightObject effector, final FightObject targetPet) {
        RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(effector, "喝啊", 2000, 0);
        ActionParamUtils.fightShouting(fight, effector, effector, 10, 10, 2, fightShouting);
        final RoleSkill skillBy = effector.getSkillBy(30043);
        final BattleUnit battleUnit = new BattleUnit(fight, effector, targetPet, skillBy);
        battleUnit.setDoubleHit(7);
        final ActionDetailParam actionDetailParam = PetPassiveSkillUtil.passivePhysics(fight, effector, targetPet, battleUnit, null, 30043);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        if (!targetPet.isFallDown()) {
            final double percent = 0.6;
            if (SkillAi.canAffectOnPro(percent)) {
                fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(targetPet, "老大原来你在对面啊", 2000, 1000);
                ActionParamUtils.fightShouting(fight, targetPet, targetPet, 10, 10, 2, fightShouting);
                FightUtils.excutePetDisappear(fight, effector, targetPet);
                final int petPos = this.getPetPos(fight, effector);
                FightObject oldPet = null;
                for (final FightObject obj : fight.getMySide(effector.getId())) {
                    if (petPos == obj.getPos()) {
                        oldPet = obj;
                        break;
                    }
                }
                final List<FightObject> monsterList = fight.getListB().stream().sorted(Comparator.comparing(FightObject::getSerial).reversed()).collect(Collectors.toList());
                final int monsterSerial = monsterList.get(0).getSerial() + 1;
                targetPet.setSerial(monsterSerial);
                MonsterSkillUtils.star18PetAppear(fight, effector, targetPet, petPos, false);
                if (oldPet != null) {
                    fight.removeFightObject(oldPet);
                    PetPassiveSkillUtil.sendDisappearData(fight, oldPet);
                }
            }
        }
    }
    
    private int getPetPos(final Fight fight, final FightObject effector) {
        final List<Integer> poss = new ArrayList<Integer>() {
            {
                this.add(10102);
                this.add(10104);
            }
        };
        final List<Integer> possZ = new ArrayList<Integer>();
        final List<FightObject> mySide = fight.getMySide(effector.getId());
        for (int i = 0; i < poss.size(); ++i) {
            for (final FightObject obj : mySide) {
                if (obj.getPos() == poss.get(i)) {
                    possZ.add(poss.get(i));
                    break;
                }
            }
        }
        if (possZ.size() >= 2 || possZ.size() <= 0) {
            return 10102;
        }
        poss.remove(possZ.get(0));
        return poss.get(0);
    }
}
