package com.kitty.system.game.fightnew.facade;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.listener.event.MoveEvent;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.enums.war.wardef.RACE;
import com.kitty.system.game.activity.handler.timing.at.AtWillHandler;
import com.kitty.system.game.activity.handler.timing.ca.CaHandler;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.handler.timing.water.WaterHandler;
import com.kitty.system.game.enter.ReqMultiMoveTo;
import com.kitty.system.game.enter.message.ReqSetFightMsg;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.bean.RoundParam;
import com.kitty.system.game.fightnew.messages.ReqFightHandle;
import com.kitty.system.game.fightnew.messages.ReqFightNewPK;
import com.kitty.system.game.fightnew.messages.ReqFightObjectAttr;
import com.kitty.system.game.fightnew.messages.ReqReturnAnimationPredictTime;
import com.kitty.system.game.fightnew.messages.ReqReturnRoundEnd;
import com.kitty.system.game.fightnew.messages.ReqSetOffAutoFight;
import com.kitty.system.game.fightnew.messages.old.ReqAutoFightSetData;
import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import com.kitty.system.game.fightnew.messages.old.RespFightMsgDlg;
import com.kitty.system.game.fightnew.service.FightExecutorNewService;
import com.kitty.system.game.fightnew.service.FightNewService;
import com.kitty.system.game.fightnew.utils.MessageAssistUtil;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.init.domain.SysMapInfo;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.mina.core.session.IoSession;
import org.nutz.json.Json;
import org.nutz.lang.Lang;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class FightController {
  private final Logger logger = LoggerFunction.FIGHT.getLogger();
  
  @Autowired
  public FightExecutorNewService fightExecutorNewService;
  
  @Autowired
  FightNewService fightNewService;
  
  @Autowired
  public RoleService roleService;
  
  @Autowired
  public PetService petService;
  
  @RequestMapping
  public void setAutoSkill(IoSession session, ReqAutoFightSetData reqAutoFightSetData) {
    FightNewService fightNewService = (FightNewService)SpringUtils.getBean(FightNewService.class);
    Role role = SessionUtils.getRoleBySession(session);
    int id = reqAutoFightSetData.getId();
    Map<String, Object> autoSet = fightNewService.saveAutoFightSet(role, reqAutoFightSetData);
    Fight fight = fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight != null)
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
                respFightCommandInfo.setBorn(((Integer)autoSet.get("born")).intValue());
              } catch (Exception e) {
                e.printStackTrace();
              } 
              List<RespFightCommandInfo> fightCommandInfos = new ArrayList<>();
              fightCommandInfos.add(respFightCommandInfo);
              MessageAssistUtil.sendIsSuccess(role, fightObject.getId().intValue(), (byte)5, fightCommandInfos);
              if (fight.getRoundParam().getRound() == fight.getRound()) {
                long time = System.currentTimeMillis() - fight.getRoundParam().getBeganTime();
                if (time < 2500L)
                  fightNewService.autoFightOne(fight, fightObject, 2500L - time); 
              } 
            } 
          }); 
  }
  
  @RequestMapping
  public void changePosition(IoSession session, ReqMultiMoveTo reqMultiMoveTo) {
    try {
      movePosition(session, reqMultiMoveTo);
    } catch (Exception e) {
      this.logger.warn("移动出错=={}", Lang.getStackTrace(e));
    } 
  }
  
  private void movePosition(IoSession session, ReqMultiMoveTo reqMultiMoveTo) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    if (!role.getPos().getMapId().equals(Integer.valueOf(reqMultiMoveTo.getMapId())))
      return; 
    if (SpringUtils.getFightNewService().isInFight(role))
      return; 
    this.roleService.recordPosition(role, reqMultiMoveTo);
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new MoveEvent(EventType.MOVE, role));
  }
  
  @RequestMapping
  public void setFightMsg(IoSession session, ReqSetFightMsg reqSetFightMsg) {
    Role role = SessionUtils.getRoleBySession(session);
    List<NutMap> list = Json.fromJsonAsList(NutMap.class, reqSetFightMsg.getMsg());
    if (reqSetFightMsg.getId() == role.getRoleId().intValue()) {
      role.save();
    } else {
      Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(reqSetFightMsg.getId()));
      if (pet != null) {
        pet.setMsgMap(list);
        role.save();
      } 
    } 
    RespFightMsgDlg respFightMsgDlg = new RespFightMsgDlg();
    respFightMsgDlg.setId(reqSetFightMsg.getId());
    respFightMsgDlg.setMsg(Json.toJson(list));
    MessagePusher.pushMessage(session, (Message)respFightMsgDlg);
  }
  
  public boolean isCompetePk(Role role, Role pkRole) {
    if (role.getLevel().intValue() <= 0 || pkRole.getLevel().intValue() <= 0)
      return false; 
    if (!role.getPos().getMapId().equals(pkRole.getPos().getMapId())) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("不在同一地图无法PK"));
      return false;
    } 
    if (!((AtWillHandler)SpringUtils.getBean(AtWillHandler.class)).isCompetePk(role, pkRole))
      return false; 
    if (!((CaHandler)SpringUtils.getBean(CaHandler.class)).isCompetePk(role, pkRole))
      return false; 
    if (!((WaterHandler)SpringUtils.getBean(WaterHandler.class)).isCompetePk(role, pkRole))
      return false; 
    if (!((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).isCompetePk(role, pkRole))
      return false; 
    if (!((KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class)).isCompetePk(role, pkRole))
      return false; 
    if (role.getPos().getMapId().equals(Integer.valueOf(1201)) || pkRole.getPos().getMapId().equals(Integer.valueOf(1201))) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("天牢重地，严禁斗殴#<E:18>#"));
      return false;
    } 
    int theTime = (int)(System.currentTimeMillis() / 1000L);
    if (role.getPkInfoBox().getIncense() < theTime) {
      MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("夺命香的时间已经过期！不可PK"));
      return false;
    } 
    return true;
  }
  
  @RequestMapping
  public void fightActionNew(IoSession session, ReqFightHandle reqFightHandle) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null || reqFightHandle == null)
      return; 
    Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight == null)
      return; 
    FightObject attacker = fight.queryFightObject(Integer.valueOf(reqFightHandle.getAttackerId()));
    if (attacker == null)
      return; 
    this.fightExecutorNewService.addFightTask(fight, () -> {
          try {
            if (reqFightHandle.getType() != 6 && reqFightHandle.getType() != 3 && reqFightHandle.getType() != 12) {
              FightObject fightObject = fight.queryFightObject(Integer.valueOf(reqFightHandle.getAttackerId()));
              if (fightObject != null)
                if (reqFightHandle.getSkillId() != 30041) {
                  boolean flag = false;
                  byte type = 2;
                  if (fight.getRound() != reqFightHandle.getRound())
                    flag = true; 
                  if (attacker.isCompleted(fight.getRound(), false))
                    flag = true; 
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
                } else {
                  fightObject.setBorn(1);
                  fightObject.setMorph(30041);
                  return;
                }  
              RespFightCommandInfo respFightCommandInfo = new RespFightCommandInfo();
              respFightCommandInfo.setId(reqFightHandle.getAttackerId());
              respFightCommandInfo.setType(reqFightHandle.getType());
              respFightCommandInfo.setSkillId(reqFightHandle.getSkillId());
              MessagePusher.pushMessage(session, (Message)respFightCommandInfo);
            } 
            for (FightObject memberObject : fight.getAllFightObjects()) {
              if (memberObject.isPlayer() && !SpringUtils.getRoleService().isOnline(memberObject.getRole().getId().longValue())) {
                memberObject.setAutoFight(1);
                this.fightNewService.autoFightOne(fight, memberObject);
              } 
            } 
            this.fightNewService.doActionNew(reqFightHandle);
          } catch (Exception e) {
            this.logger.error("手动出手出错==={}", Lang.getStackTrace(e));
            reqFightHandle.setType(0);
            reqFightHandle.setTargetId(reqFightHandle.getAttackerId());
            reqFightHandle.setSkillId(0);
            this.fightNewService.doActionNew(reqFightHandle);
          } 
        });
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
    SysMapInfo roleMapInfo = mapInfoMap.get(role.getPos().getMapId());
    SysMapInfo pkRoleMapInfo = mapInfoMap.get(pkRole.getPos().getMapId());
    if (roleMapInfo.getIsPkMap() == 0 || pkRoleMapInfo.getIsPkMap() == 0) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("安全区不可以切磋"));
      return;
    } 
    if (!isCompetePk(role, pkRole))
      return; 
    TeamService teamService = (TeamService)SpringUtils.getBean(TeamService.class);
    Team team = teamService.getTeam(role.getRoleId().intValue());
    Team pkTeam = teamService.getTeam(pkRole.getRoleId().intValue());
    if (team != null && pkTeam != null) {
      if (team.getId() == pkTeam.getId()) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("同一队伍内成员无法切磋。"));
        return;
      } 
      Member member = pkTeam.getList().stream().filter(e -> e.getId().equals(pkRole.getId())).findFirst().orElse(null);
      if (member.isInTeam()) {
        this.fightNewService.enterTeamPK(team.getList(), pkTeam.getList(), role);
        return;
      } 
    } 
    Fight fightA = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    Fight fightB = this.fightNewService.getFightByRoleId(pkRole.getRoleId().intValue());
    if (fightA != null) {
      RespMsg respMsg = new RespMsg();
      respMsg.setMsg("你在战斗中，无法切磋。");
      MessagePusher.pushMessage(session, (Message)respMsg);
      return;
    } 
    if (fightB != null) {
      RespMsg respMsg = new RespMsg();
      respMsg.setMsg("对方在战斗中，无法切磋。");
      MessagePusher.pushMessage(session, (Message)respMsg);
      return;
    } 
    Map<String, Integer> setMap = role.getExtendBox().getSetMap();
    Map<String, Integer> pkSetMap = pkRole.getExtendBox().getSetMap();
    if (((Integer)setMap.getOrDefault("refuse_fight", Integer.valueOf(0))).intValue() == 1) {
      RespMsg respMsg = new RespMsg();
      respMsg.setMsg("你的切磋开关没有打开。");
      MessagePusher.pushMessage(session, (Message)respMsg);
      return;
    } 
    if (((Integer)pkSetMap.getOrDefault("refuse_fight", Integer.valueOf(0))).intValue() == 1) {
      RespMsg respMsg = new RespMsg();
      respMsg.setMsg("对方的切磋开关没有打开。");
      MessagePusher.pushMessage(session, (Message)respMsg);
      return;
    } 
    if (team != null && pkTeam != null) {
      if (team.getId() == pkTeam.getId()) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("同一队伍不可以切磋"));
        return;
      } 
      List<Member> roleTeamList = this.fightNewService.excuteMemberLeaveTemp(role, team);
      List<Member> pkRoleTeamList = this.fightNewService.excuteMemberLeaveTemp(pkRole, pkTeam);
      this.fightNewService.enterTeamPK(roleTeamList, pkRoleTeamList, role);
    } else if (team != null) {
      ArrayList<Member> arrayList = new ArrayList<>();
      Member member = Member.valueOf(pkRole);
      arrayList.add(member);
      List<Member> roleTeamList = this.fightNewService.excuteMemberLeaveTemp(role, team);
      this.fightNewService.enterTeamPK(roleTeamList, arrayList, role);
    } else if (pkTeam != null) {
      ArrayList<Member> arrayList = new ArrayList<>();
      Member member = Member.valueOf(role);
      arrayList.add(member);
      List<Member> pkRoleTeamList = this.fightNewService.excuteMemberLeaveTemp(pkRole, pkTeam);
      this.fightNewService.enterTeamPK(arrayList, pkRoleTeamList, role);
    } else {
      ArrayList<Member> arrayListA = new ArrayList<>();
      Member memberA = Member.valueOf(role);
      arrayListA.add(memberA);
      ArrayList<Member> arrayListB = new ArrayList<>();
      Member memberB = Member.valueOf(pkRole);
      arrayListB.add(memberB);
      this.fightNewService.enterTeamPK(arrayListA, arrayListB, role);
    } 
  }
  
  @RequestMapping
  public void returnAnimationPredictTime(IoSession session, ReqReturnAnimationPredictTime reqReturnAnimationPredictTime) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null || reqReturnAnimationPredictTime == null)
      return; 
    Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight == null)
      return; 
    FightObject fightObject = fight.queryFightObject(role.getRoleId());
    if (fightObject == null)
      return; 
    int predictTime = reqReturnAnimationPredictTime.getPredictTime();
    if (predictTime <= 0)
      return; 
    RoundParam roundParam = fight.getRoundParam();
    if (fight.getRound() == roundParam.getRound() && reqReturnAnimationPredictTime.getRound() == fight.getRound())
      if (roundParam.getUpdateAnimationPredictTime() <= 0L) {
        for (FightObject obj : fight.getAllFightObjects()) {
          if (!obj.isPlayer())
            continue; 
          fight.roundFinishTask(obj, predictTime);
        } 
        roundParam.setUpdateAnimationPredictTime(System.currentTimeMillis());
        roundParam.setPredictTime(predictTime);
      }  
  }
  
  @RequestMapping
  public void returnRoundEnd(IoSession session, ReqReturnRoundEnd reqReturnRoundEnd) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null || reqReturnRoundEnd == null)
      return; 
    String recordIdStr = reqReturnRoundEnd.getRecordId();
    if (recordIdStr != null && !recordIdStr.isEmpty()) {
      try {
        long recordId = Long.parseLong(recordIdStr);
        ((FightRecordService)SpringUtils.getBean(FightRecordService.class)).playerFightMsg(role.getId().longValue(), recordId, (short)(reqReturnRoundEnd.getRound() + 1));
      } catch (NumberFormatException e) {
        e.printStackTrace();
      } 
      return;
    } 
    Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight == null)
      return; 
    if (fight.getRound() != reqReturnRoundEnd.getRound())
      return; 
    this.fightNewService.animationPlayEnd(fight, role.getRoleId().intValue());
  }
  
  @RequestMapping
  public void setOffAutoFight(IoSession session, ReqSetOffAutoFight reqSetOffAutoFight) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null || reqSetOffAutoFight == null)
      return; 
    Fight fight = this.fightNewService.getFightByRoleId(role.getRoleId().intValue());
    if (fight == null)
      return; 
    byte autoFight = reqSetOffAutoFight.getAutoFight();
    if (autoFight != 0 && autoFight != 1)
      return; 
    role.getExtendBox().setAutoFight(autoFight);
    this.fightExecutorNewService.addFightTask(fight, () -> this.roleService.openAutoFightNew(role, fight));
  }
  
  @RequestMapping
  public void getFightObjectAttrs(IoSession session, ReqFightObjectAttr reqFightObjectAttr) {
    int fightId = reqFightObjectAttr.getFightId();
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null && fightId <= 0)
      return; 
    this.fightNewService.getFightObjectAttrs(role, fightId);
  }
}
