
package com.kitty.system.game.fightnew.book.monster;

import com.kitty.system.game.fightnew.model.FightObjectParam;
import com.kitty.system.game.fightnew.messages.old.RespFightShouting;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.game.fightnew.book.pet.PetPassiveSkillUtil;
import com.kitty.system.game.fightnew.service.FightMessageNewService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.messages.vo.CombatAddObject;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import java.util.List;
import com.kitty.system.game.fightnew.model.RefreshPet;
import com.kitty.system.game.fightnew.model.ActionDetailParam;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.game.fightnew.utils.SkillAi;
import java.util.Collection;
import com.kitty.system.init.domain.vo.SkillParameter;
import java.util.ArrayList;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.Iterator;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;

public class MonsterSkillUtils
{
    public static final int[] petMonsterSerialsForStar18;
    
    public static void beforeAttack(final Fight fight, final FightObject effector) {
        if (effector.getSkillIds() != null && effector.getSkillIds().size() > 0) {
            for (final RoleSkill roleSkill : effector.getSkillIds()) {
                if (MonsterSkillConst.getBeforeAttack().contains(roleSkill.getSkillId())) {
                    final MonsterSkillProcessor monsterSkillProcessor = MonsterSkillProcessor.getMonsterSkill(roleSkill.getSkillId());
                    if (monsterSkillProcessor == null) {
                        continue;
                    }
                    monsterSkillProcessor.beforeAttack(fight, effector, new Object[0]);
                }
            }
        }
    }
    
    public static void randomMonsterPetSkill(final SysDataMonster petMonster) {
        final ArrayList<SkillParameter> skills = petMonster.getSkills();
        petMonster.setSkills(new ArrayList<SkillParameter>());
        petMonster.getSkills().addAll(skills);
        if (SkillAi.canAffectOnPro(0.5)) {
            addSkill(petMonster, 81031, "隐身");
        }
        if (SkillAi.canAffectOnPro(0.5)) {
            addSkill(petMonster, 82004, "子虚乌有");
        }
    }
    
    public static void addSkill(final SysDataMonster monster, final int skillSerial, final String skillName) {
        final SkillParameter skillParameter = new SkillParameter();
        skillParameter.setFightType((byte)0);
        skillParameter.setName(skillName);
        skillParameter.setPriority((byte)1);
        skillParameter.setSerial(skillSerial);
        monster.getSkills().add(skillParameter);
    }
    
    public static SysDataMonster getStar18SelectPet() {
        final int[] petMonsters = MonsterSkillUtils.petMonsterSerialsForStar18;
        final int round = RandomUtil.round(0, petMonsters.length - 1);
        final int petMonsterSerial = petMonsters[round];
        final SysDataMonster petMonster = FightDataPool.data_monster.get(petMonsterSerial);
        final SysDataMonster dataMonsterPet = new SysDataMonster();
        BeanUtils.copyBeanProp(dataMonsterPet, petMonster);
        return dataMonsterPet;
    }
    
    public static void star18PetAppear(final Fight fight, final FightObject effector, FightObject targetPet, final int petPos, final boolean isSelect) {
        targetPet.setPos(petPos);
        final ActionDetailParam actionDetailParam = new ActionDetailParam(effector, 3, 0, targetPet.getId());
        if (isSelect) {
            targetPet.setHostId(effector.getId());
            effector.setFightPetId(targetPet.getId());
            effector.setTargetId(targetPet.getId());
            final RefreshPet refreshPet = new RefreshPet();
            final ArrayList<RefreshPet> refreshPets = new ArrayList<RefreshPet>();
            refreshPets.add(refreshPet);
            refreshPet.setPetId(targetPet.getId());
            refreshPet.setType(1);
            actionDetailParam.setPetList(refreshPets);
        }
        else {
            targetPet.setHostId(0);
            targetPet.setCurrLife(targetPet.getMaxLife());
            targetPet.setTargetId(0);
            targetPet.setASide(false);
            final List<FightObject> monsterList = fight.getListB().stream().sorted(Comparator.comparing(FightObject::getId).reversed()).collect(Collectors.toList());
            final int monsterId = monsterList.get(0).getId() + 1;
            targetPet.setId(monsterId);
            if (targetPet.isPet()) {
                targetPet = new MonsterFightObject(targetPet, monsterId, targetPet.getSerial(), FIGHT_TYPE.MONSTER.getValue());
                targetPet.setCurrMana(0);
                targetPet.setMaxMana(0);
            }
        }
        fight.getListB().add(targetPet);
        final FightMember fightMember = targetPet.asFightMember();
        fight.getFightMemberB().add(fightMember);
        FightExecutorNewService.FIGHT_ROOMS.put(targetPet.getId(), fight.getId());
        final List<CombatAddObject> list = new ArrayList<CombatAddObject>();
        final CombatAddObject combatAddObject = SpringUtils.getBean(FightNewService.class).addObject2Fight(effector.getId(), targetPet, fight);
        list.add(combatAddObject);
        actionDetailParam.setCombatAddObjects(list);
        final RespFightShouting fightShouting = SpringUtils.getBean(FightMessageNewService.class).fightShouting(fight.queryFightObject(targetPet.getId()), "战斗出场");
        final FightObjectParam fightObjectParam = actionDetailParam.gainTargetParam(targetPet.getId(), true);
        fightObjectParam.setFightShouting(fightShouting);
        fight.getActionParam().getActionDetailParams().add(actionDetailParam);
        PetPassiveSkillUtil.otherEnterBattle(fight, targetPet.getId());
    }
    
    static {
        petMonsterSerialsForStar18 = new int[] { 318111, 318112, 318113, 318114, 318115, 318116, 318117, 318118, 318119, 318120, 318121 };
    }
}
