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
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
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
    public void setAutoSkill(IoSession session, ReqAutoFightSetData reqAutoFightSetData) {
        FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
        Role role = SessionUtils.getRoleBySession(session);
        int id = reqAutoFightSetData.getId();
        Map<String, Object> autoSet = fightNewService.saveAutoFightSet(role, reqAutoFightSetData);
        Fight fight = fightNewService.getFightByRoleId((int)role.getRoleId());
        if (fight != null) {
            this.fightExecutorNewService.addFightTask(fight, () -> {
                FightObject fightObject = fight.queryFightObject(Integer.valueOf(id));
                if (fightObject != null) {
                    fightObject.setAutoFightType(reqAutoFightSetData.getType());
                    fightObject.setAutoSkillId(reqAutoFightSetData.getSkillId());
                    fightObject.setAction(reqAutoFightSetData.getAction());
                    fightObject.setType(reqAutoFightSetData.getType());
                    RespFightCommandInfo respFightCommandInfo = new RespFightCommandInfo();
                    if (fightObject.getRace() == RACE.RACE_LONG.getValue() && reqAutoFightSetData.getBorn() > 0) {
                        fightObject.setBorn(reqAutoFightSetData.getBorn());
                        respFightCommandInfo.setBorn(reqAutoFightSetData.getBorn());
                    }
                    respFightCommandInfo.setId(reqAutoFightSetData.getId());
                    respFightCommandInfo.setType(reqAutoFightSetData.getType());
                    respFightCommandInfo.setSkillId(reqAutoFightSetData.getSkillId());
                    try {
                        respFightCommandInfo.setBorn((int)autoSet.get("born"));
                    }
                    catch (Exception e) {
                        e.printStackTrace();
                    }
                    List<RespFightCommandInfo> fightCommandInfos = new ArrayList<>();
                    fightCommandInfos.add(respFightCommandInfo);
                    MessageAssistUtil.sendIsSuccess(role, (int)fightObject.getId(), (byte)5, fightCommandInfos);
                    if (fight.getRoundParam().getRound() == fight.getRound()) {
                        long time = System.currentTimeMillis() - fight.getRoundParam().getBeganTime();
                        if (time < 2500L) {
                            fightNewService.autoFightOne(fight, fightObject, 2500L - time);
                        }
                    }
                }
                return;
            });
        }
    }
    
    @RequestMapping
    public void changePosition(IoSession session, ReqMultiMoveTo reqMultiMoveTo) {
        try {
            this.movePosition(session, reqMultiMoveTo);
        }
        catch (Exception e) {
            this.logger.warn("移动出错=={}", Lang.getStackTrace(e));
        }
    }
    
    private void movePosition(IoSession session, ReqMultiMoveTo reqMultiMoveTo) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (!role.getPos().getMapId().equals(Integer.valueOf(reqMultiMoveTo.getMapId()))) {
            return;
        }
        if (SpringUtils.getFightNewService().isInFight(role)) {
            return;
        }
        this.roleService.recordPosition(role, reqMultiMoveTo);
        EventDispatcher.getInstance().fireEvent(new MoveEvent(EventType.MOVE, role));
        return;
    }
    
    @RequestMapping
    public void setFightMsg(IoSession session, ReqSetFightMsg reqSetFightMsg) {
        Role role = SessionUtils.getRoleBySession(session);
        List<NutMap> list = Json.fromJsonAsList(NutMap.class, reqSetFightMsg.getMsg());
        if (reqSetFightMsg.getId() == (int)role.getRoleId()) {
            role.save();
        }
        else {
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(reqSetFightMsg.getId()));
            if (pet != null) {
                pet.setMsgMap(list);
                role.save();
            }
        }
        RespFightMsgDlg respFightMsgDlg = new RespFightMsgDlg();
        respFightMsgDlg.setId(reqSetFightMsg.getId());
        respFightMsgDlg.setMsg(Json.toJson(list));
        MessagePusher.pushMessage(session, respFightMsgDlg);
    }
    
    public boolean isCompetePk(Role role, Role pkRole) {
        if ((int)role.getLevel() <= 0 || (int)pkRole.getLevel() <= 0) {
            return false;
        }
        if (!role.getPos().getMapId().equals(pkRole.getPos().getMapId())) {
            MessagePusher.pushMessage(role, new RespMsg("不在同一地图无法PK"));
            return false;
        }
        if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!((CaHandler)SpringUtils.getBean(CaHandler.class)).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!((WaterHandler)SpringUtils.getBean(WaterHandler.class)).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).isCompetePk(role, pkRole)) {
            return false;
        }
        if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isCompetePk(role, pkRole)) {
            return false;
        }
        if (role.getPos().getMapId().equals(Integer.valueOf(1201)) || pkRole.getPos().getMapId().equals(Integer.valueOf(1201))) {
            MessagePusher.pushMessage(role, new RespMsg("天牢重地，严禁斗殴#<E:18>#"));
            return false;
        }
        int theTime = (int)(System.currentTimeMillis() / 1000L);
        if (role.getPkInfoBox().getIncense() < theTime) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("夺命香的时间已经过期！不可PK"));
            return false;
        }
        return true;
    }
    
    @RequestMapping
    public void fightActionNew(IoSession session, ReqFightHandle reqFightHandle) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqFightHandle == null) {
            return;
        }
        Fight fight = this.fightNewService.getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            return;
        }
        FightObject attacker = fight.queryFightObject(Integer.valueOf(reqFightHandle.getAttackerId()));
        if (attacker == null) {
            return;
        }
        this.fightExecutorNewService.addFightTask(fight, () -> {
            try {
                if (reqFightHandle.getType() != 6 && reqFightHandle.getType() != 3 && reqFightHandle.getType() != 12) {
                    FightObject fightObject = fight.queryFightObject(Integer.valueOf(reqFightHandle.getAttackerId()));
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
                            fightObject.setType(reqFightHandle.getType());
                            fightObject.setTargetId(reqFightHandle.getTargetId());
                            if (!fightObject.isConfusion() || fightObject.getAutoFight() == 0) {
                                fightObject.setAutoFightType(reqFightHandle.getType());
                                fightObject.setAutoSkillId(reqFightHandle.getSkillId());
                                fightObject.setAutoTarget(reqFightHandle.getTargetId());
                            }
                        }
                        else {
                            fightObject.setBorn(1);
                            fightObject.setMorph(30041);
                            return;
                        }
                    }
                    RespFightCommandInfo respFightCommandInfo = new RespFightCommandInfo();
                    respFightCommandInfo.setId(reqFightHandle.getAttackerId());
                    respFightCommandInfo.setType(reqFightHandle.getType());
                    respFightCommandInfo.setSkillId(reqFightHandle.getSkillId());
                    MessagePusher.pushMessage(session, respFightCommandInfo);
                }
                Iterator<FightObject> iterator = fight.getAllFightObjects().iterator();
                while (iterator.hasNext()) {
                    FightObject memberObject = (FightObject)iterator.next();
                    if (memberObject.isPlayer() && !SpringUtils.getRoleService().isOnline((long)memberObject.getRole().getId())) {
                        memberObject.setAutoFight(1);
                        this.fightNewService.autoFightOne(fight, memberObject);
                    }
                    else {
                        continue;
                    }
                }
                this.fightNewService.doActionNew(reqFightHandle);
            }
            catch (Exception e) {
                this.logger.error("手动出手出错==={}", Lang.getStackTrace(e));
                reqFightHandle.setType(0);
                reqFightHandle.setTargetId(reqFightHandle.getAttackerId());
                reqFightHandle.setSkillId(0);
                this.fightNewService.doActionNew(reqFightHandle);
            }
            return;
        });
        return;
    }
    
    @RequestMapping
    public void fightNewPK(IoSession session, ReqFightNewPK reqFightNewPK) {
        Role role = SessionUtils.getRoleBySession(session);
        Role pkRole = this.roleService.getPlayerBy(reqFightNewPK.getRoleId());
        if (role == null || pkRole == null) {
            this.logger.error("角色获取失败无法PK");
            return;
        }
        Map<Integer, SysMapInfo> mapInfoMap = MapDataPool.mapInfoMap;
        SysMapInfo roleMapInfo = (SysMapInfo)mapInfoMap.get(role.getPos().getMapId());
        SysMapInfo pkRoleMapInfo = (SysMapInfo)mapInfoMap.get(pkRole.getPos().getMapId());
        if (roleMapInfo.getIsPkMap() == 0 || pkRoleMapInfo.getIsPkMap() == 0) {
            MessagePusher.pushMessage(role, new RespMsg("安全区不可以切磋"));
            return;
        }
        if (!this.isCompetePk(role, pkRole)) {
            return;
        }
        TeamService teamService = (TeamService)SpringUtils.getBean(TeamService.class);
        Team team = teamService.getTeam((int)role.getRoleId());
        Team pkTeam = teamService.getTeam((int)pkRole.getRoleId());
        if (team != null && pkTeam != null) {
            if (team.getId() == pkTeam.getId()) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("同一队伍内成员无法切磋。"));
                return;
            }
            Member member = (Member)pkTeam.getList().stream().filter(e -> e.getId().equals(pkRole.getId())).findFirst().orElse(null);
            if (member.isInTeam()) {
                this.fightNewService.enterTeamPK(team.getList(), pkTeam.getList(), role);
                return;
            }
        }
        Fight fightA = this.fightNewService.getFightByRoleId((int)role.getRoleId());
        Fight fightB = this.fightNewService.getFightByRoleId((int)pkRole.getRoleId());
        if (fightA != null) {
            RespMsg respMsg2 = new RespMsg();
            respMsg2.setMsg("你在战斗中，无法切磋。");
            MessagePusher.pushMessage(session, respMsg2);
            return;
        }
        if (fightB != null) {
            RespMsg respMsg2 = new RespMsg();
            respMsg2.setMsg("对方在战斗中，无法切磋。");
            MessagePusher.pushMessage(session, respMsg2);
            return;
        }
        Map<String, Integer> setMap = role.getExtendBox().getSetMap();
        Map<String, Integer> pkSetMap = pkRole.getExtendBox().getSetMap();
        if ((int)setMap.getOrDefault("refuse_fight", Integer.valueOf(0)) == 1) {
            RespMsg respMsg = new RespMsg();
            respMsg.setMsg("你的切磋开关没有打开。");
            MessagePusher.pushMessage(session, respMsg);
            return;
        }
        if ((int)pkSetMap.getOrDefault("refuse_fight", Integer.valueOf(0)) == 1) {
            RespMsg respMsg = new RespMsg();
            respMsg.setMsg("对方的切磋开关没有打开。");
            MessagePusher.pushMessage(session, respMsg);
            return;
        }
        if (team != null && pkTeam != null) {
            if (team.getId() == pkTeam.getId()) {
                MessagePusher.pushMessage(role, new RespMsg("同一队伍不可以切磋"));
                return;
            }
            List<Member> roleTeamList2 = this.fightNewService.excuteMemberLeaveTemp(role, team);
            List<Member> pkRoleTeamList2 = this.fightNewService.excuteMemberLeaveTemp(pkRole, pkTeam);
            this.fightNewService.enterTeamPK(roleTeamList2, pkRoleTeamList2, role);
        }
        else if (team != null) {
            ArrayList<Member> arrayList = new ArrayList<>();
            Member member2 = Member.valueOf(pkRole);
            arrayList.add(member2);
            List<Member> roleTeamList = this.fightNewService.excuteMemberLeaveTemp(role, team);
            this.fightNewService.enterTeamPK(roleTeamList, arrayList, role);
        }
        else if (pkTeam != null) {
            ArrayList<Member> arrayList = new ArrayList<>();
            Member member2 = Member.valueOf(role);
            arrayList.add(member2);
            List<Member> pkRoleTeamList = this.fightNewService.excuteMemberLeaveTemp(pkRole, pkTeam);
            this.fightNewService.enterTeamPK(arrayList, pkRoleTeamList, role);
        }
        else {
            ArrayList<Member> arrayListA = new ArrayList<>();
            Member memberA = Member.valueOf(role);
            arrayListA.add(memberA);
            ArrayList<Member> arrayListB = new ArrayList<>();
            Member memberB = Member.valueOf(pkRole);
            arrayListB.add(memberB);
            this.fightNewService.enterTeamPK(arrayListA, arrayListB, role);
        }
        return;
    }
    
    @RequestMapping
    public void returnAnimationPredictTime(IoSession session, ReqReturnAnimationPredictTime reqReturnAnimationPredictTime) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqReturnAnimationPredictTime == null) {
            return;
        }
        Fight fight = this.fightNewService.getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            return;
        }
        FightObject fightObject = fight.queryFightObject(role.getRoleId());
        if (fightObject == null) {
            return;
        }
        int predictTime = reqReturnAnimationPredictTime.getPredictTime();
        if (predictTime <= 0) {
            return;
        }
        RoundParam roundParam = fight.getRoundParam();
        if (fight.getRound() == roundParam.getRound() && reqReturnAnimationPredictTime.getRound() == fight.getRound() && roundParam.getUpdateAnimationPredictTime() <= 0L) {
            for (FightObject obj : fight.getAllFightObjects()) {
                if (!obj.isPlayer()) {
                    continue;
                }
                fight.roundFinishTask(obj, (long)predictTime);
            }
            roundParam.setUpdateAnimationPredictTime(System.currentTimeMillis());
            roundParam.setPredictTime(predictTime);
        }
        return;
    }
    
    @RequestMapping
    public void returnRoundEnd(IoSession session, ReqReturnRoundEnd reqReturnRoundEnd) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqReturnRoundEnd == null) {
            return;
        }
        String recordIdStr = reqReturnRoundEnd.getRecordId();
        if (recordIdStr != null && !recordIdStr.isEmpty()) {
            try {
                long recordId = Long.parseLong(recordIdStr);
                ((FightRecordService)SpringUtils.getBean(FightRecordService.class)).playerFightMsg((long)role.getId(), recordId, (short)(reqReturnRoundEnd.getRound() + 1));
            }
            catch (NumberFormatException e) {
                e.printStackTrace();
            }
            return;
        }
        Fight fight = this.fightNewService.getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            return;
        }
        if (fight.getRound() != reqReturnRoundEnd.getRound()) {
            return;
        }
        this.fightNewService.animationPlayEnd(fight, (int)role.getRoleId());
        return;
    }
    
    @RequestMapping
    public void setOffAutoFight(IoSession session, ReqSetOffAutoFight reqSetOffAutoFight) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null || reqSetOffAutoFight == null) {
            return;
        }
        Fight fight = this.fightNewService.getFightByRoleId((int)role.getRoleId());
        if (fight == null) {
            return;
        }
        byte autoFight = reqSetOffAutoFight.getAutoFight();
        if (autoFight != 0 && autoFight != 1) {
            return;
        }
        role.getExtendBox().setAutoFight(autoFight);
        this.fightExecutorNewService.addFightTask(fight, () -> this.roleService.openAutoFightNew(role, fight));
        return;
    }
    
    @RequestMapping
    public void getFightObjectAttrs(IoSession session, ReqFightObjectAttr reqFightObjectAttr) {
        int fightId = reqFightObjectAttr.getFightId();
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null && fightId <= 0) {
            return;
        }
        this.fightNewService.getFightObjectAttrs(role, fightId);
        return;
    }
}
