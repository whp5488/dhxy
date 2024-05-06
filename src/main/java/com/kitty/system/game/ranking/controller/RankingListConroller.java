package com.kitty.system.game.ranking.controller;

import com.kitty.mina.annotation.RequestMapping;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.ranking.message.ReqCaNomRanking;
import com.kitty.system.game.ranking.message.ReqClassifyPetRanking;
import com.kitty.system.game.ranking.message.ReqComprehensiveStrengthRanking;
import com.kitty.system.game.ranking.message.ReqMaterNomRanking;
import com.kitty.system.game.ranking.message.ReqMaterTeamRanking;
import com.kitty.system.game.ranking.message.ReqMyRanking;
import com.kitty.system.game.ranking.message.ReqPractitionersRanking;
import com.kitty.system.game.ranking.message.ReqPropertyPetRanking;
import com.kitty.system.game.ranking.message.ReqQlkRanking;
import com.kitty.system.game.ranking.message.ReqWeaponsRanking;
import com.kitty.system.game.ranking.service.ComprehensiveStrengthRankingListService;
import com.kitty.system.game.ranking.service.DuelRankingService;
import com.kitty.system.game.ranking.service.PetRankingListService;
import com.kitty.system.game.ranking.service.PractitionersRankingListService;
import com.kitty.system.game.ranking.service.QlkRankingService;
import com.kitty.system.game.ranking.service.WeaponsRankingService;
import com.kitty.system.game.role.domain.Role;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class RankingListConroller {
  Logger logger = LoggerFactory.getLogger(RankingListConroller.class);
  
  @Autowired
  PetRankingListService petRankingListService;
  
  @Autowired
  ComprehensiveStrengthRankingListService comprehensiveStrengthRankingListService;
  
  @Autowired
  PractitionersRankingListService practitionersRankingListService;
  
  @Autowired
  WeaponsRankingService weaponsRankingService;
  
  @Autowired
  QlkRankingService qlkRankingService;
  
  @Autowired
  DuelRankingService duelRankingService;
  
  @RequestMapping
  public void petClassifyRankingList(IoSession session, ReqClassifyPetRanking reqClassifyPetRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    int petSerial = reqClassifyPetRanking.getPetSerial();
    if (petSerial < 0)
      return; 
    byte serverId = reqClassifyPetRanking.getServerId();
    if (serverId < 0)
      return; 
    this.petRankingListService.getClassifyPetRankingList(role, petSerial, serverId);
  }
  
  @RequestMapping
  public void getPetPropertyRankingList(IoSession session, ReqPropertyPetRanking reqPropertyPetRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    byte serverId = reqPropertyPetRanking.getServerId();
    if (serverId < 0)
      return; 
    byte type = reqPropertyPetRanking.getType();
    if (type < 0)
      return; 
    this.petRankingListService.getPetPropertyRankingList(role, type, serverId);
  }
  
  @RequestMapping
  public void getMyRankingList(IoSession session, ReqMyRanking reqMyRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return;
    byte type = reqMyRanking.getType();
    if (type < 0)
      return;
    byte serverId = reqMyRanking.getServerId();
    if (serverId < 0)
      return;
    int serial = reqMyRanking.getSerial();
    if (serial < 0)
      return;
    getMyRankingList(role, type, serverId, serial);
  }
  
  private void getMyRankingList(Role role, byte type, byte serverId, int serial) {
    int flag = type / 10;
    if (flag == 0)
      flag = type; 
    switch (flag) {
      case 0:
        this.petRankingListService.getMyClassifyRanking(role, serial, serverId);
        return;
      case 1:
        this.petRankingListService.getMyPropertyRanking(role, serial, serverId);
        return;
      case 2:
        this.comprehensiveStrengthRankingListService.getMyComprehensiveStrengthRanking(role, serial, serverId);
        return;
      case 3:
        this.practitionersRankingListService.getMyPractitionersRanking(role);
        return;
      case 4:
        this.weaponsRankingService.getMyWeaponsRanking(role, type, serial, serverId);
        return;
      case 5:
        this.qlkRankingService.getMyQlkRanking(role, serial, serverId);
        return;
    } 
    MessagePusher.pushMessage(role, (Message)new RespMsg("请求异常！"));
  }
  
  @RequestMapping
  public void getComprehensiveStrengthRankingList(IoSession session, ReqComprehensiveStrengthRanking reqComprehensiveStrengthRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    byte race = reqComprehensiveStrengthRanking.getRace();
    if (race < 0 && race > 5)
      return; 
    byte serverId = reqComprehensiveStrengthRanking.getServerId();
    if (serverId < 0)
      return; 
    this.comprehensiveStrengthRankingListService.getComprehensiveStrengthRankingList(role, race, serverId);
  }
  
  @RequestMapping
  public void getPractitionersRankingList(IoSession session, ReqPractitionersRanking reqPractitionersRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.practitionersRankingListService.getPractitionersRankingList(role);
  }
  
  @RequestMapping
  public void getWeaponsRankingList(IoSession session, ReqWeaponsRanking reqWeaponsRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    byte race = reqWeaponsRanking.getRace();
    if (race < 0)
      return; 
    byte equipType = reqWeaponsRanking.getEquipType();
    if (equipType < 0)
      return; 
    this.weaponsRankingService.getWeaponsRankingList(role, race, equipType);
  }
  
  @RequestMapping
  public void getQlkRinkingList(IoSession session, ReqQlkRanking reqQlkRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    short qlkType = reqQlkRanking.getQlkType();
    if (qlkType <= 0)
      return; 
    this.qlkRankingService.getQlkRinkingList(role, qlkType, (byte)0);
  }
  
  @RequestMapping
  public void getMaterNomRanking(IoSession session, ReqMaterNomRanking reqMaterNomRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.duelRankingService.getMaterNomRanking(session);
  }
  
  @RequestMapping
  public void getMaterTeamRanking(IoSession session, ReqMaterTeamRanking materTeamRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.duelRankingService.getMaterTeamRanking(session);
  }
  
  @RequestMapping
  public void getCaNomRanking(IoSession session, ReqCaNomRanking reqCaNomRanking) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role == null)
      return; 
    this.duelRankingService.getCaNomRanking(session, reqCaNomRanking);
  }
}
