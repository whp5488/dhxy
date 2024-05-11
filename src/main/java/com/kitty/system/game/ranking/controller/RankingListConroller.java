
package com.kitty.system.game.ranking.controller;

import com.kitty.system.game.ranking.message.ReqCaNomRanking;
import com.kitty.system.game.ranking.message.ReqMaterTeamRanking;
import com.kitty.system.game.ranking.message.ReqMaterNomRanking;
import com.kitty.system.game.ranking.message.ReqQlkRanking;
import com.kitty.system.game.ranking.message.ReqWeaponsRanking;
import com.kitty.system.game.ranking.message.ReqPractitionersRanking;
import com.kitty.system.game.ranking.message.ReqComprehensiveStrengthRanking;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.ranking.message.ReqMyRanking;
import com.kitty.system.game.ranking.message.ReqPropertyPetRanking;
import com.kitty.mina.annotation.RequestMapping;
import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.ranking.message.ReqClassifyPetRanking;
import org.apache.mina.core.session.IoSession;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.ranking.service.DuelRankingService;
import com.kitty.system.game.ranking.service.QlkRankingService;
import com.kitty.system.game.ranking.service.WeaponsRankingService;
import com.kitty.system.game.ranking.service.PractitionersRankingListService;
import com.kitty.system.game.ranking.service.ComprehensiveStrengthRankingListService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.ranking.service.PetRankingListService;
import org.slf4j.Logger;
import org.springframework.stereotype.Controller;

@Controller
public class RankingListConroller
{
    Logger logger;
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
    
    public RankingListConroller() {
        this.logger = LoggerFactory.getLogger(RankingListConroller.class);
    }
    
    @RequestMapping
    public void petClassifyRankingList(final IoSession session, final ReqClassifyPetRanking reqClassifyPetRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final int petSerial = reqClassifyPetRanking.getPetSerial();
        if (petSerial < 0) {
            return;
        }
        final byte serverId = reqClassifyPetRanking.getServerId();
        if (serverId < 0) {
            return;
        }
        this.petRankingListService.getClassifyPetRankingList(role, petSerial, serverId);
    }
    
    @RequestMapping
    public void getPetPropertyRankingList(final IoSession session, final ReqPropertyPetRanking reqPropertyPetRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final byte serverId = reqPropertyPetRanking.getServerId();
        if (serverId < 0) {
            return;
        }
        final byte type = reqPropertyPetRanking.getType();
        if (type < 0) {
            return;
        }
        this.petRankingListService.getPetPropertyRankingList(role, type, serverId);
    }
    
    @RequestMapping
    public void getMyRankingList(final IoSession session, final ReqMyRanking reqMyRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final byte type = reqMyRanking.getType();
        if (type < 0) {
            return;
        }
        final byte serverId = reqMyRanking.getServerId();
        if (serverId < 0) {
            return;
        }
        final int serial = reqMyRanking.getSerial();
        if (serial < 0) {
            return;
        }
        this.getMyRankingList(role, type, serverId, serial);
    }
    
    private void getMyRankingList(final Role role, final byte type, final byte serverId, final int serial) {
        int flag = type / 10;
        if (flag == 0) {
            flag = type;
        }
        switch (flag) {
            case 0: {
                this.petRankingListService.getMyClassifyRanking(role, serial, serverId);
                break;
            }
            case 1: {
                this.petRankingListService.getMyPropertyRanking(role, serial, serverId);
                break;
            }
            case 2: {
                this.comprehensiveStrengthRankingListService.getMyComprehensiveStrengthRanking(role, serial, serverId);
                break;
            }
            case 3: {
                this.practitionersRankingListService.getMyPractitionersRanking(role);
                break;
            }
            case 4: {
                this.weaponsRankingService.getMyWeaponsRanking(role, type, serial, serverId);
                break;
            }
            case 5: {
                this.qlkRankingService.getMyQlkRanking(role, serial, serverId);
                break;
            }
            default: {
                MessagePusher.pushMessage(role, new RespMsg("请求异常！"));
                break;
            }
        }
    }
    
    @RequestMapping
    public void getComprehensiveStrengthRankingList(final IoSession session, final ReqComprehensiveStrengthRanking reqComprehensiveStrengthRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final byte race = reqComprehensiveStrengthRanking.getRace();
        if (race < 0 && race > 5) {
            return;
        }
        final byte serverId = reqComprehensiveStrengthRanking.getServerId();
        if (serverId < 0) {
            return;
        }
        this.comprehensiveStrengthRankingListService.getComprehensiveStrengthRankingList(role, race, serverId);
    }
    
    @RequestMapping
    public void getPractitionersRankingList(final IoSession session, final ReqPractitionersRanking reqPractitionersRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.practitionersRankingListService.getPractitionersRankingList(role);
    }
    
    @RequestMapping
    public void getWeaponsRankingList(final IoSession session, final ReqWeaponsRanking reqWeaponsRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final byte race = reqWeaponsRanking.getRace();
        if (race < 0) {
            return;
        }
        final byte equipType = reqWeaponsRanking.getEquipType();
        if (equipType < 0) {
            return;
        }
        this.weaponsRankingService.getWeaponsRankingList(role, race, equipType);
    }
    
    @RequestMapping
    public void getQlkRinkingList(final IoSession session, final ReqQlkRanking reqQlkRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final short qlkType = reqQlkRanking.getQlkType();
        if (qlkType <= 0) {
            return;
        }
        this.qlkRankingService.getQlkRinkingList(role, qlkType, (byte)0);
    }
    
    @RequestMapping
    public void getMaterNomRanking(final IoSession session, final ReqMaterNomRanking reqMaterNomRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.duelRankingService.getMaterNomRanking(session);
    }
    
    @RequestMapping
    public void getMaterTeamRanking(final IoSession session, final ReqMaterTeamRanking materTeamRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.duelRankingService.getMaterTeamRanking(session);
    }
    
    @RequestMapping
    public void getCaNomRanking(final IoSession session, final ReqCaNomRanking reqCaNomRanking) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        this.duelRankingService.getCaNomRanking(session, reqCaNomRanking);
    }
}
