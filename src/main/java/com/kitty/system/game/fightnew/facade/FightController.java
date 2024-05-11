
package com.kitty.system.game.fightnew.facade;

import com.kitty.system.game.fightnew.messages.ReqFightObjectAttr;
import com.kitty.system.game.fightnew.messages.ReqSetOffAutoFight;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import com.kitty.system.game.fightnew.messages.ReqReturnRoundEnd;
import com.kitty.system.game.fightnew.bean.RoundParam;
import com.kitty.system.game.fightnew.messages.ReqReturnAnimationPredictTime;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.fightnew.messages.ReqFightNewPK;
import java.util.Iterator;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.common.message.RespMsg;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.messages.old.RespFightMsgDlg;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.json.Json;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.enter.message.ReqSetFightMsg;
import com.kitty.listener.event.MoveEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import org.nutz.lang.Lang;
import com.kitty.system.game.enter.ReqMultiMoveTo;
import com.kitty.mina.annotation.RequestMapping;
import java.util.List;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.fightnew.utils.MessageAssistUtil;
import java.util.ArrayList;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.messages.old.ReqAutoFightSetData;
import org.apache.mina.core.session.IoSession;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.fightnew.service.FightNewService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class FightController
{
    private final Logger logger;
    @Autowired
    public FightExecutorNewService fightExecutorNewService;
    @Autowired
    FightNewService fightNewService;
    @Autowired
    public RoleService roleService;
    @Autowired
    public PetService petService;
    
    public FightController() {
        this.logger = LoggerFunction.FIGHT.getLogger();
    }
    
    @RequestMapping
    public void setAutoSkill(final IoSession session, final ReqAutoFightSetData reqAutoFightSetData) {
        final FightNewService fightNewService = SpringUtils.getBean(FightNewService.class);
        final Role role = SessionUtils.getRoleBySession(session);
        final int id = reqAutoFightSetData.getId();
        final Map<String, Object> autoSet = fightNewService.saveAutoFightSet(role, reqAutoFightSetData);
        final Fight fight = fightNewService.getFightByRoleId(role.getRoleId());
        if (fight != null) {
            this.fightExecutorNewService.addFightTask(fight, () -> {
                final FightObject fightObject = fight.queryFightObject(id);
                if (fightObject != null) {
                    fightObject.setAutoFightType(reqAutoFightSetData.getType());
                    fightObject.setAutoSkillId(reqAutoFightSetData.getSkillId());
                    fightObject.setAction(reqAutoFightSetData.getAction());
                    fightObject.setType(reqAutoFightSetData.getType());
                    final RespFightCommandInfo respFightCommandInfo = new RespFightCommandInfo();
                    if (fightObject.getRace() == RACE.RACE_LONG.getValue() && reqAutoFightSetData.getBorn() > 0) {
                        fightObject.setBorn(reqAutoFightSetData.getBorn());
                        respFightCommandInfo.setBorn(reqAutoFightSetData.getBorn());
                    }
                    respFightCommandInfo.setId(reqAutoFightSetData.getId());
                    respFightCommandInfo.setType(reqAutoFightSetData.getType());
                    respFightCommandInfo.setSkillId(reqAutoFightSetData.getSkillId());
                    try {
                        respFightCommandInfo.setBorn((Integer) autoSet.get("born"));
                    }
                    catch (final Exception e) {
                        e.printStackTrace();
                    }
                    final List<RespFightCommandInfo> fightCommandInfos = new ArrayList<RespFightCommandInfo>();
                    fightCommandInfos.add(respFightCommandInfo);
                    MessageAssistUtil.sendIsSuccess(role, fightObject.getId(), (byte)5, fightCommandInfos);
                    if (fight.getRoundParam().getRound() == fight.getRound()) {
                        final long time = System.currentTimeMillis() - fight.getRoundParam().getBeganTime();
                        if (time < 2500L) {
                            fightNewService.autoFightOne(fight, fightObject, 2500L - time);
                        }
                    }
                }
            });
        }
    }
    
    @RequestMapping
    public void changePosition(final IoSession session, final ReqMultiMoveTo reqMultiMoveTo) {
        try {
            this.movePosition(session, reqMultiMoveTo);
        }
        catch (final Exception e) {
            this.logger.warn("移动出错=={}", (Object)Lang.getStackTrace((Throwable)e));
        }
    }
    
    private void movePosition(final IoSession session, final ReqMultiMoveTo reqMultiMoveTo) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (!role.getPos().getMapId().equals(reqMultiMoveTo.getMapId())) {
            return;
        }
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return;
        }
        this.roleService.recordPosition(role, reqMultiMoveTo);
        EventDispatcher.getInstance().fireEvent(new MoveEvent(EventType.MOVE, role));
    }
    
    @RequestMapping
    public void setFightMsg(final IoSession session, final ReqSetFightMsg reqSetFightMsg) {
        final Role role = SessionUtils.getRoleBySession(session);
        final List<NutMap> list = Json.fromJsonAsList(NutMap.class, (CharSequence)reqSetFightMsg.getMsg());
        if (reqSetFightMsg.getId() == role.getRoleId()) {
            role.save();
        }
        else {
            final Pet pet = this.petService.selectPet(role).get(reqSetFightMsg.getId());
            if (pet != null) {
                pet.setMsgMap(list);
                role.save();
            }
        }
        final RespFightMsgDlg respFightMsgDlg = new RespFightMsgDlg();
        respFightMsgDlg.setId(reqSetFightMsg.getId());
        respFightMsgDlg.setMsg(Json.toJson((Object)list));
        MessagePusher.pushMessage(session, respFightMsgDlg);
    }
    
    public boolean isCompetePk(final Role role, final Role pkRole) {
        if (role.getLevel() <= 0 || pkRole.getLevel() <= 0) {
            return false;
        }
        if (!role.getPos().getMapId().equals(pkRole.getPos().getMapId())) {
            MessagePusher.pushMessage(role, new RespMsg("不在同一地图无法PK"));
            return false;
        }
        if (!SpringUtils.getBean(AtWillHandler.class).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!SpringUtils.getBean(CaHandler.class).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!SpringUtils.getBean(WaterHandler.class).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!SpringUtils.getBean(GangWarsHandler.class).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!SpringUtils.getBean(KejuTaskHandler.class).isCompetePk(role, pkRole)) {
            return false;
        }
        if (role.getPos().getMapId().equals(1201) || pkRole.getPos().getMapId().equals(1201)) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，严禁斗殴#<E:18>#"));
            return false;
        }
        final int theTime = (int)(System.currentTimeMillis() / 1000L);
        if (role.getPkInfoBox().getIncense() < theTime) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("夺命香的时间已经过期！不可PK"));
            return false;
        }
        return true;
    }
    
    @RequestMapping
    public void fightActionNew(final IoSession session, final ReqFightHandle reqFightHandle) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqFightHandle == null) {
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        if (fight == null) {
            return;
        }
        final FightObject attacker = fight.queryFightObject(reqFightHandle.getAttackerId());
        if (attacker == null) {
            return;
        }
        this.fightExecutorNewService.addFightTask(fight, () -> {
            try {
                if (reqFightHandle.getType() != 6 && reqFightHandle.getType() != 3 && reqFightHandle.getType() != 12) {
                    final FightObject fightObject = fight.queryFightObject(reqFightHandle.getAttackerId());
                    if (fightObject != null) {
                        if (reqFightHandle.getSkillId() != 30041) {
                            boolean flag = false;
                            byte type = 2;
                            if (fight.getRound() != reqFightHandle.getRound()) {
                                flag = true;
                            }
                            if (attacker.isCompleted(fight.getRound(), false)) {
                                flag = true;
                            }
                            if (attacker.isDoAction()) {
                                type = 8;
                                flag = true;
                            }
                            if (flag) {
                                MessageAssistUtil.sendIsSuccess(role, reqFightHandle.getAttackerId(), type, null);
                                return;
                            }
                            else {
                                fightObject.setType(reqFightHandle.getType());
                                fightObject.setTargetId(reqFightHandle.getTargetId());
                                if (!fightObject.isConfusion() || fightObject.getAutoFight() == 0) {
                                    fightObject.setAutoFightType(reqFightHandle.getType());
                                    fightObject.setAutoSkillId(reqFightHandle.getSkillId());
                                    fightObject.setAutoTarget(reqFightHandle.getTargetId());
                                }
                            }
                        }
                        else {
                            fightObject.setBorn(1);
                            fightObject.setMorph(30041);
                            return;
                        }
                    }
                    final RespFightCommandInfo respFightCommandInfo = new RespFightCommandInfo();
                    respFightCommandInfo.setId(reqFightHandle.getAttackerId());
                    respFightCommandInfo.setType(reqFightHandle.getType());
                    respFightCommandInfo.setSkillId(reqFightHandle.getSkillId());
                    MessagePusher.pushMessage(session, respFightCommandInfo);
                }
                for (FightObject memberObject : fight.getAllFightObjects()) {
                    if (memberObject.isPlayer() && !SpringUtils.getRoleService().isOnline(memberObject.getRole().getId())) {
                        memberObject.setAutoFight(1);
                        this.fightNewService.autoFightOne(fight, memberObject);
                    }
                }
                this.fightNewService.doActionNew(reqFightHandle);
            }
            catch (final Exception e) {
                this.logger.error("手动出手出错==={}", Lang.getStackTrace(e));
                reqFightHandle.setType(0);
                reqFightHandle.setTargetId(reqFightHandle.getAttackerId());
                reqFightHandle.setSkillId(0);
                this.fightNewService.doActionNew(reqFightHandle);
            }
        });
    }
    
    @RequestMapping
    public void fightNewPK(final IoSession session, final ReqFightNewPK reqFightNewPK) {
        final Role role = SessionUtils.getRoleBySession(session);
        final Role pkRole = this.roleService.getPlayerBy(reqFightNewPK.getRoleId());
        if (role == null || pkRole == null) {
            this.logger.error("角色获取失败无法PK");
            return;
        }
        final Map<Integer, SysMapInfo> mapInfoMap = MapDataPool.mapInfoMap;
        final SysMapInfo roleMapInfo = mapInfoMap.get(role.getPos().getMapId());
        final SysMapInfo pkRoleMapInfo = mapInfoMap.get(pkRole.getPos().getMapId());
        if (roleMapInfo.getIsPkMap() == 0 || pkRoleMapInfo.getIsPkMap() == 0) {
            MessagePusher.pushMessage(role, new RespMsg("安全区不可以切磋"));
            return;
        }
        if (!this.isCompetePk(role, pkRole)) {
            return;
        }
        final TeamService teamService = SpringUtils.getBean(TeamService.class);
        final Team team = teamService.getTeam(role.getRoleId());
        final Team pkTeam = teamService.getTeam(pkRole.getRoleId());
        if (team != null && pkTeam != null) {
            if (team.getId() == pkTeam.getId()) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("同一队伍内成员无法切磋。"));
                return;
            }
            final Member member = pkTeam.getList().stream().filter(e -> e.getId().equals(pkRole.getId())).findFirst().orElse(null);
            if (member.isInTeam()) {
                this.fightNewService.enterTeamPK(team.getList(), pkTeam.getList(), role);
                return;
            }
        }
        final Fight fightA = this.fightNewService.getFightByRoleId(role.getRoleId());
        final Fight fightB = this.fightNewService.getFightByRoleId(pkRole.getRoleId());
        if (fightA != null) {
            final RespMsg respMsg = new RespMsg();
            respMsg.setMsg("你在战斗中，无法切磋。");
            MessagePusher.pushMessage(session, respMsg);
            return;
        }
        if (fightB != null) {
            final RespMsg respMsg = new RespMsg();
            respMsg.setMsg("对方在战斗中，无法切磋。");
            MessagePusher.pushMessage(session, respMsg);
            return;
        }
        final Map<String, Integer> setMap = role.getExtendBox().getSetMap();
        final Map<String, Integer> pkSetMap = pkRole.getExtendBox().getSetMap();
        if (setMap.getOrDefault("refuse_fight", 0) == 1) {
            final RespMsg respMsg2 = new RespMsg();
            respMsg2.setMsg("你的切磋开关没有打开。");
            MessagePusher.pushMessage(session, respMsg2);
            return;
        }
        if (pkSetMap.getOrDefault("refuse_fight", 0) == 1) {
            final RespMsg respMsg2 = new RespMsg();
            respMsg2.setMsg("对方的切磋开关没有打开。");
            MessagePusher.pushMessage(session, respMsg2);
            return;
        }
        if (team != null && pkTeam != null) {
            if (team.getId() == pkTeam.getId()) {
                MessagePusher.pushMessage(role, new RespMsg("同一队伍不可以切磋"));
                return;
            }
            final List<Member> roleTeamList = this.fightNewService.excuteMemberLeaveTemp(role, team);
            final List<Member> pkRoleTeamList = this.fightNewService.excuteMemberLeaveTemp(pkRole, pkTeam);
            this.fightNewService.enterTeamPK(roleTeamList, pkRoleTeamList, role);
        }
        else if (team != null) {
            final ArrayList<Member> arrayList = new ArrayList<Member>();
            final Member member2 = Member.valueOf(pkRole);
            arrayList.add(member2);
            final List<Member> roleTeamList2 = this.fightNewService.excuteMemberLeaveTemp(role, team);
            this.fightNewService.enterTeamPK(roleTeamList2, arrayList, role);
        }
        else if (pkTeam != null) {
            final ArrayList<Member> arrayList = new ArrayList<Member>();
            final Member member2 = Member.valueOf(role);
            arrayList.add(member2);
            final List<Member> pkRoleTeamList2 = this.fightNewService.excuteMemberLeaveTemp(pkRole, pkTeam);
            this.fightNewService.enterTeamPK(arrayList, pkRoleTeamList2, role);
        }
        else {
            final ArrayList<Member> arrayListA = new ArrayList<Member>();
            final Member memberA = Member.valueOf(role);
            arrayListA.add(memberA);
            final ArrayList<Member> arrayListB = new ArrayList<Member>();
            final Member memberB = Member.valueOf(pkRole);
            arrayListB.add(memberB);
            this.fightNewService.enterTeamPK(arrayListA, arrayListB, role);
        }
    }
    
    @RequestMapping
    public void returnAnimationPredictTime(final IoSession session, final ReqReturnAnimationPredictTime reqReturnAnimationPredictTime) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqReturnAnimationPredictTime == null) {
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        if (fight == null) {
            return;
        }
        final FightObject fightObject = fight.queryFightObject(role.getRoleId());
        if (fightObject == null) {
            return;
        }
        final int predictTime = reqReturnAnimationPredictTime.getPredictTime();
        if (predictTime <= 0) {
            return;
        }
        final RoundParam roundParam = fight.getRoundParam();
        if (fight.getRound() == roundParam.getRound() && reqReturnAnimationPredictTime.getRound() == fight.getRound() && roundParam.getUpdateAnimationPredictTime() <= 0L) {
            for (final FightObject obj : fight.getAllFightObjects()) {
                if (!obj.isPlayer()) {
                    continue;
                }
                fight.roundFinishTask(obj, predictTime);
            }
            roundParam.setUpdateAnimationPredictTime(System.currentTimeMillis());
            roundParam.setPredictTime(predictTime);
        }
    }
    
    @RequestMapping
    public void returnRoundEnd(final IoSession session, final ReqReturnRoundEnd reqReturnRoundEnd) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqReturnRoundEnd == null) {
            return;
        }
        final String recordIdStr = reqReturnRoundEnd.getRecordId();
        if (recordIdStr != null && !recordIdStr.isEmpty()) {
            try {
                final long recordId = Long.parseLong(recordIdStr);
                SpringUtils.getBean(FightRecordService.class).playerFightMsg(role.getId(), recordId, (short)(reqReturnRoundEnd.getRound() + 1));
            }
            catch (final NumberFormatException e) {
                e.printStackTrace();
            }
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        if (fight == null) {
            return;
        }
        if (fight.getRound() != reqReturnRoundEnd.getRound()) {
            return;
        }
        this.fightNewService.animationPlayEnd(fight, role.getRoleId());
    }
    
    @RequestMapping
    public void setOffAutoFight(final IoSession session, final ReqSetOffAutoFight reqSetOffAutoFight) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqSetOffAutoFight == null) {
            return;
        }
        final Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId());
        if (fight == null) {
            return;
        }
        final byte autoFight = reqSetOffAutoFight.getAutoFight();
        if (autoFight != 0 && autoFight != 1) {
            return;
        }
        role.getExtendBox().setAutoFight(autoFight);
        this.fightExecutorNewService.addFightTask(fight, () -> this.roleService.openAutoFightNew(role, fight));
    }
    
    @RequestMapping
    public void getFightObjectAttrs(final IoSession session, final ReqFightObjectAttr reqFightObjectAttr) {
        final int fightId = reqFightObjectAttr.getFightId();
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null && fightId <= 0) {
            return;
        }
        this.fightNewService.getFightObjectAttrs(role, fightId);
    }
}
