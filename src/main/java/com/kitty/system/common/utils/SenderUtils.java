package com.kitty.system.common.utils;

import org.slf4j.LoggerFactory;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.chat.message.RespSystemChat;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.mina.message.Message;
import org.slf4j.Logger;

public class SenderUtils
{
    private static Logger logger;
    
    public SenderUtils() {
    }
    
    public static void brodCastMessage(Message message, int type) {
        if (type == LOGIC.LOGICTYPE_HERO.getValue()) {
            DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, message));
        }
    }
    
    public static void pushChatMessage(RespChat respChat) {
        DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, respChat));
    }
    
    public static void pushChatMessage(RespSystemChat systemChat) {
        DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, systemChat));
    }
    
    public static void pushChatMessage(Npc npc, RespChat respChat) {
        if (respChat.getType() == 1) {
            DataCache.ONLINE_ROLES.values().forEach(role -> {
                if (role.getPos().getMapId().equals(Integer.valueOf(npc.getMap()))) {
                    MessagePusher.pushMessage(role, respChat);
                }
                return;
            });
        }
        else if (respChat.getType() == 2) {
            pushChatMessage(respChat);
        }
    }
    
    public static void pushTeamRecruitChatMessage(RespChat respChat) {
        TeamService teamService = SpringUtils.getTeamService();
        DataCache.ONLINE_ROLES.values().forEach(onlineRole -> {
            Team team = teamService.getTeam((int)onlineRole.getRoleId());
            if (team == null) {
                MessagePusher.pushMessage(onlineRole, respChat);
            }
            return;
        });
    }
    
    public static void pushPartyChatMessage(Integer partyId, RespChat respChat) {
        DataCache.ONLINE_ROLES.values().forEach(onlineRole -> {
            if (!partyId.equals(onlineRole.getFaction())) {
                return;
            }
            MessagePusher.pushMessage(onlineRole, respChat);
            return;
        });
    }
    
    public static void sendAllOnlinePlayers(Message message) {
        DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, message));
    }
    
    static {
        SenderUtils.logger = LoggerFactory.getLogger(SenderUtils.class);
    }
}
