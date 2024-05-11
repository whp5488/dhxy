
package com.kitty.system.game.activity.handler.time;

import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.enums.item.ItemClassify;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.activity.model.product.ActivityType;
import com.kitty.listener.event.FightEndEvent;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.boss.model.BossFightParam;
import java.util.Collection;
import org.springframework.util.CollectionUtils;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.game.enter.Position;
import java.text.MessageFormat;
import com.kitty.common.core.SchedulerManager;
import com.kitty.system.game.map.model.GroupMapParam;
import java.util.HashSet;
import java.util.Set;
import com.kitty.system.game.vo.Npc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.boss.service.BossService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.RandomUtil;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.mina.cache.DataCache;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.init.domain.SysDataThreeScopeDemon;
import java.util.Random;
import java.util.List;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component
public class ThreeScopeHandler extends FightActivityHandler
{
    private static final int NEED_LEVEL = 33;
    private static final int SHOW_MINUTE = 15;
    private static final String CONTENT = "确定/threats_scope|取消/离开";
    private static final String IN_FIGHT = "我要观战/guard|取消/离开";
    private static final String TEAM_LEVEL_NOT_ENOUGH = "{0}等级竟然不足33级#<E:76>#，还是练练再来吧#<E:109>#";
    private static final String TIPS = "  确定要进入与{0}的战斗吗？\n（推荐等级:{1})";
    private static final String TIPS_NOT = "  我正在战斗中，等下再来收拾你#<E:32>#";
    private static final String TEAM_COUNT_NOT_ENOUGH = "你的队伍不够强大，请组够至少三名大侠再来挑战吧！";
    private static final String NOTICE = "#<B>{0}#在挖宝时触发远古封印,三界妖王的元神冲天而出,降临于#<r:181,g:115,b:52>{1}#,天地为之震动。";
    private static final String NOTICE_SYS = "三界妖王的元神冲天而出,降临于#<r:181,g:115,b:52>{0}#,天地为之震动。";
    private static final int MIN_TEAM_COUNT = 3;
    private static final int REWARD_MAX_MORE_LEVEL = 35;
    
    @Override
    protected int getFightType(final Role role) {
        return FIGHT_TYPE.THREE_SCOPE.getValue();
    }
    
    public boolean flushConditionByLevel(final Role role, int num) {
        final List<Integer> list = this.calculateFloat(role.getLevel(), 33);
        final int scene = this.create(role, list, true);
        if (scene > 0 && --num > 0) {
            final List<Integer> newList = this.scope(scene, list);
            for (int i = 0; i < num; ++i) {
                this.create(role, newList, false);
            }
        }
        return false;
    }
    
    public int create(final Role role, final List<Integer> list, final boolean is) {
        final Random random = new Random();
        final int index = random.nextInt(list.size());
        final int grade = list.get(index);
        final List<SysDataThreeScopeDemon> collect = TaskDataPool.threeScopeDemonList.stream().filter(e -> e.getGrade() == grade).collect(Collectors.toList());
        if (collect != null && collect.size() > 0) {
            final SysDataThreeScopeDemon object = collect.get(random.nextInt(collect.size()));
            long count = DataCache.THREE_SCOPE_SET.values().stream().filter(e -> e.getLevel() == object.getGrade()).count();
            count = object.getNum() - count;
            if (count > 0L) {
                final boolean load = this.load(object, role, is);
                if (load) {
                    return object.getScene();
                }
            }
        }
        return -1;
    }
    
    public void flushAfterServerStart() {
        final ArrayList<Integer> objects = new ArrayList<Integer>();
        final List<SysDataThreeScopeDemon> list = TaskDataPool.threeScopeDemonList;
        for (final SysDataThreeScopeDemon object : list) {
            long count = DataCache.THREE_SCOPE_SET.values().stream().filter(e -> e.getLevel() == object.getGrade()).count();
            count = object.getNum() - count;
            if (count > 0L) {
                for (int i = 0; i < count; ++i) {
                    this.load(object, null, !objects.contains(object.getScene()));
                    objects.add(object.getScene());
                }
            }
        }
    }
    
    public boolean load(final SysDataThreeScopeDemon object, final Role role, final boolean is) {
        final Position position = this.getRandomPosition(object.getScene());
        if (position == null) {
            return false;
        }
        final int index = RandomUtil.round(0, object.getWar().size() - 1);
        final Integer warSerial = object.getWar().get(index);
        final SysDataMonster monster = DataGlobalFunc.getMainMonster(warSerial);
        if (monster == null) {
            this.logger.error("加载妖王数据错误 = {}", (Object)object.getId());
            return false;
        }
        int icon = monster.getShape();
        if (monster.getShape() <= 1) {
            icon = DataGlobalFunc.getFormula(role, monster.getShape());
        }
        final int id = SpringUtils.getBean(BossService.class).getTempNpcId();
        final Npc npc = new Npc(id, 0, object.getScene(), position.getX(), position.getY(), icon, object.getName(), "", object.getId(), object.getGrade(), warSerial, 22);
        npc.setEndTime(npc.getCreateTime() + 900);
        DataCache.THREE_SCOPE_SET.put(npc.getId(), npc);
        final Set<Integer> set = DataCache.THREE_SCOPE_MAP_NPC.computeIfAbsent(Integer.valueOf(object.getScene()), k -> new HashSet());
        set.add(npc.getId());
        this.bossService.broadcastNpcShow(null, npc);
        SchedulerManager.getInstance().schedule(() -> this.clearBoosOfficerTimeOut(npc), 900000L);
        if (is) {
            if (role != null) {
                final SysMapInfo map = SpringUtils.getMapService().getMap(object.getScene());
                if (map != null) {
                    SpringUtils.getChatService().sendLaba(MessageFormat.format("#<B>{0}#在挖宝时触发远古封印,三界妖王的元神冲天而出,降临于#<r:181,g:115,b:52>{1}#,天地为之震动。", role.getName(), map.getName()));
                }
            }
            else {
                final SysMapInfo map = SpringUtils.getMapService().getMap(object.getScene());
                if (map != null) {
                    SpringUtils.getChatService().sendLaba(MessageFormat.format("三界妖王的元神冲天而出,降临于#<r:181,g:115,b:52>{0}#,天地为之震动。", map.getName()));
                }
            }
        }
        return true;
    }
    
    public String getNpcContent(final Role role, final Npc npc) {
        this.setContent(npc, "  确定要进入与{0}的战斗吗？\n（推荐等级:{1})");
        return "确定/threats_scope|取消/离开";
    }
    
    public String getNpcContentNotFight(final Role role, final Npc npc) {
        if (npc.isInFight()) {
            this.setContent(npc, "  我正在战斗中，等下再来收拾你#<E:32>#");
            this.sendNpcContent(role, npc, "我要观战/guard|取消/离开");
            return "我要观战/guard|取消/离开";
        }
        String names = null;
        final int teamCount = this.teamService.getTeamCount(role);
        if (teamCount < 3) {
            npc.setTalk("你的队伍不够强大，请组够至少三名大侠再来挑战吧！");
            return "";
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        if (this.teamService.isInTeam(role)) {
            names = this.teamService.checkMember(team, memberRole -> !SpringUtils.getActivityService().isLevel(memberRole, 33));
        }
        if (names != null) {
            npc.setTalk(MessageFormat.format("{0}等级竟然不足33级#<E:76>#，还是练练再来吧#<E:109>#", "#<LB1>" + names + "#"));
            return "";
        }
        return null;
    }
    
    public void setContent(final Npc npc, final String talk) {
        final SysDataThreeScopeDemon object = TaskDataPool.threeScopeDemonList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (object != null) {
            npc.setTalk(MessageFormat.format(talk, object.getName(), object.getGrade()));
        }
    }
    
    public void doStartFight(final Role role, final Npc npc) {
        final SysWarRole warRole = FightDataPool.data_war_role.get(npc.getWarSerial());
        if (warRole != null) {
            final List<SysDataMonster> monsters = this.monsterService.getMonsters(FightDataPool.data_monster, warRole);
            if (CollectionUtils.isEmpty((Collection)monsters)) {
                this.logger.error("妖王进入战斗错误：{}", (Object)npc.getLevel());
                return;
            }
            monsters.stream().forEach(monster -> {
                if (monster.getShape() <= 0) {
                    monster.setShape(npc.getIcon());
                }
                monster.setMonsterType(FIGHT_TYPE.THREE_SCOPE.getValue());
                return;
            });
            final BossFightParam bossFightParam = new BossFightParam(monsters, FIGHT_TYPE.THREE_SCOPE.getValue(), 0, npc.getId());
            final Fight fight = this.bossService.startFightToBoss(role, bossFightParam);
            if (fight == null) {
                this.setNotInFight(npc);
            }
        }
        else {
            this.logger.error("妖王进入战斗错误2，{}", (Object)npc.getId());
            this.setNotInFight(npc);
        }
    }
    
    @Override
    protected Npc getBossNpc(final int npcId) {
        return DataCache.THREE_SCOPE_SET.get(npcId);
    }
    
    @Override
    public void doFightWinForSingle(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        super.doFightWinForSingle(role, fightEndEvent, npc);
        try {
            SpringUtils.getActivityService().addFinishCount(role, ActivityType.SCOPE_OFFICER, 1);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    @Override
    protected void clearNpcAfterWin(final Role role, final FightEndEvent fightEndEvent, final Npc bossNpc) {
        this.clearStarOfficerAfterFightWin(bossNpc);
    }
    
    @Override
    public void doFightFail(final Role role, final FightEndEvent fightEndEvent) {
        super.doFightFail(role, fightEndEvent);
    }
    
    public void giveReward(final Role role, final FightEndEvent fightEndEvent, final Npc npc) {
        if (!this.isGiveReward(role, npc)) {
            return;
        }
        if (this.teamService.getTeam(role.getRoleId()) != null && !this.teamService.isInTeam(role)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("离队无法获得奖励"));
            return;
        }
        final SysDataThreeScopeDemon object = TaskDataPool.threeScopeDemonList.stream().filter(e -> e.getId() == npc.getNs()).findFirst().orElse(null);
        if (object != null && SpringUtils.getActivityService().isHaveRemainCount(role, ActivityType.SCOPE_OFFICER)) {
            this.roleService.addExp(role, calculate(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP), "", true);
            final Integer combatPet = role.getExtendBox().getCombatPet();
            if (combatPet > 0) {
                final Pet pet = this.petService.selectPet(role).get(combatPet);
                this.petService.addExp(role, pet, calculate(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP), true);
            }
            SpringUtils.getBean(BagService.class).sendReward(role, object.getAward(), "妖王", 0);
        }
    }
    
    private static long calculate(final int grade, final int rei, final ItemClassify type) {
        if (ItemClassify.HERO_EXP == type) {
            return BigDecimalUtil.decimalScaleDown(0, RandomUtil.round(95, 105) / 100 * (1.6E7 * (1.0 + grade / 140 * 2.5) / 15.0 / 5.0)).longValue();
        }
        if (ItemClassify.PET_EXP == type) {
            return BigDecimalUtil.decimalScaleDown(0, RandomUtil.round(95, 105) / 100 * 1.2 * (1.6E7 * (1.0 + grade / 140 * 2.5) / 15.0 / 5.0)).longValue();
        }
        return 0L;
    }
    
    private boolean isGiveReward(final Role role, final Npc npc) {
        if (npc == null) {
            return false;
        }
        if (role.getLevel() > npc.getLevel() + 35) {
            MessagePusher.pushMessage(role, new RespMsg("你当前等级大于三界妖王35级，无法获得奖励"));
            return false;
        }
        return true;
    }
    
    private void clearBoosOfficerTimeOut(final Npc npc) {
        if (npc == null) {
            return;
        }
        if (DataCache.THREE_SCOPE_SET.get(npc.getId()) == null) {
            return;
        }
        if (npc.isInFight()) {
            this.bossService.broadcastNpcHide(null, npc);
        }
        else {
            this.clearStarOfficer(npc);
            DataCache.THREE_SCOPE_SET.remove(npc.getId());
        }
    }
    
    private void clearStarOfficerAfterFightWin(final Npc npc) {
        this.clearStarOfficer(npc);
    }
    
    private void clearStarOfficer(final Npc npc) {
        if (npc == null) {
            return;
        }
        DataCache.THREE_SCOPE_SET.remove(npc.getId());
        final Set<Integer> set = DataCache.THREE_SCOPE_MAP_NPC.get(npc.getMap());
        if (set != null) {
            set.remove(npc.getId());
            this.removeUsedPosition(npc.getMap(), npc.getX(), npc.getY());
            this.bossService.broadcastNpcHide(null, npc);
        }
    }
    
    @Override
    protected void clearNpcTimeOut(final Npc npc) {
        this.clearBoosOfficerTimeOut(npc);
    }
}
