
package com.kitty.system.game.activity.handler.time;

import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.vo.ItemEasyInfo;
import java.util.Iterator;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.init.domain.SysDataMonster;
import java.util.List;
import com.kitty.system.boss.model.BossFightParam;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.Collection;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataStarOfficer;
import com.kitty.system.game.team.domain.Team;
import java.text.MessageFormat;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class MonsterNianHandler extends FightActivityHandler
{
    private static final int NEED_LEVEL = 60;
    private static final int SERIAL = 651004;
    private static final String CONTENT = "小小的年兽敢来撒野/threats_ns|我只是路过/离开";
    private static final String TEAM_LEVEL_NOT_ENOUGH = "{0}等级竟然不足60级#<E:76>#，还是练练再来吧#<E:109>#";
    private static final String TIPS = "现在好多人胆子真大，看到我居然不怕。";
    
    @Override
    protected int getFightType(final Role role) {
        return FIGHT_TYPE.NIAN.getValue();
    }
    
    public String getNpcContent(final Role role, final Npc npc) {
        this.setContent(npc, "现在好多人胆子真大，看到我居然不怕。");
        return "小小的年兽敢来撒野/threats_ns|我只是路过/离开";
    }
    
    public String getNpcContentNotFight(final Role role, final Npc npc) {
        final Team team = this.teamService.getTeam(role.getRoleId());
        String names = null;
        if (this.teamService.isInTeam(role)) {
            names = this.teamService.checkMember(team, memberRole -> !SpringUtils.getActivityService().isLevel(memberRole, 60));
        }
        else if (!SpringUtils.getActivityService().isLevel(role, 60)) {
            names = role.getName();
        }
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}等级竟然不足60级#<E:76>#，还是练练再来吧#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        if (names != null) {
            return names;
        }
        return null;
    }
    
    public void setContent(final Npc npc, final String talk) {
        final SysDataStarOfficer starOfficer = TaskDataPool.starOfficerList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (starOfficer != null) {
            npc.setTalk(MessageFormat.format(talk, starOfficer.getLevel(), starOfficer.getGrade(), starOfficer.getName()));
        }
    }
    
    public void doStartFight(final Role role, final Npc npc) {
        final SysWarRole warRole = FightDataPool.data_war_role.get(651004);
        if (warRole != null) {
            final List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
            if (CollectionUtils.isEmpty((Collection)monsters)) {
                return;
            }
            monsters.stream().forEach(monster -> {
                if (monster.getPos() == 10103 || monster.getPos() == 10003) {
                    monster.setName(npc.getName());
                }
                else {
                    monster.setName(DataGlobalFunc.dynamicName(monster.getName()));
                }
                monster.setMonsterType(FIGHT_TYPE.NIAN.getValue());
                return;
            });
            final BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.NIAN.getValue(), 0, npc.getId());
            this.bossService.startFightToBoss(role, bossFightParam);
        }
    }
    
    @Override
    protected Npc getBossNpc(final int npcId) {
        final SysNpcInfo sysNpcInfo = DataCache.ID_NPC.get(npcId);
        return new Npc(sysNpcInfo);
    }
    
    @Override
    public void doFightWinForSingle(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        super.doFightWinForSingle(role, fightEndEvent, npc);
        SpringUtils.getActivityService().addFinishCount(role, ActivityType.NIAN_OFFICER, 1);
    }
    
    @Override
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public void giveReward(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        if (this.teamService.getTeam(role.getRoleId()) != null && !this.teamService.isInTeam(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("离队无法获得奖励"));
            return;
        }
        if (SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.NIAN_OFFICER)) {
            this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), 2, ItemClassify.HERO_EXP), "年兽", true);
            final Integer combatPet = role.getExtendBox().getCombatPet();
            if (combatPet > 0) {
                final Pet pet = this.petService.selectPet(role).get(combatPet);
                if (pet != null) {
                    this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), 2, ItemClassify.PET_EXP), true);
                }
            }
            this.specialReward(role);
        }
    }
    
    public void specialReward(final Role role) {
        final BagService bagService = SpringUtils.getBean(BagService.class);
        final List<Item> list = bagService.taskRewardDynamic("ns");
        for (final Item item : list) {
            bagService.rewardItem(role, item.getItem(), item.getNum(), 0, "击杀年兽");
            if (item.getOthers() > 0) {
                final ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(item.getItem());
                if (itemEasyInfo == null) {
                    continue;
                }
                MessagePusher.notify2Lb(role, 212, role.getName(), String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", itemEasyInfo.getSerial(), 0, 0, itemEasyInfo.getSerial(), itemEasyInfo.getSerial(), itemEasyInfo.getName()), String.format("#<ZWG3>X%d#", item.getNum()));
            }
        }
    }
    
    private static long calculate(final int grade, final int rei, final int cnt, final ItemClassify type) {
        final double fold = ActivityType.NIAN_OFFICER.getFold();
        double exp = 0.0;
        if (type == ItemClassify.HERO_EXP) {
            exp = 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5;
        }
        else if (type == ItemClassify.PET_EXP) {
            exp = 1.6E7 * (1.0 + (grade + rei * 15.0) / 140.0 * 2.5) / 30.0 / 28.13 * (0.2 + 0.1 * cnt + Math.pow(cnt, 0.45)) * 1.5 * 1.2;
        }
        return BigDecimalUtil.decimalScaleDown(0, (fold > 1.0) ? (exp * fold) : exp).longValue();
    }
    
    @Override
    public boolean isSetInFight() {
        return false;
    }
    
    @Override
    protected void clearNpcAfterWin(final Role role, final FightEndEvent fightEndEvent, final Npc bossNpc) {
    }
    
    @Override
    protected void clearNpcTimeOut(final Npc npc) {
    }
}
