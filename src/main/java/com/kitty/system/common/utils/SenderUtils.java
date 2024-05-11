
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
    
    public static void brodCastMessage(final Message message, final int type) {
        if (type == LOGIC.LOGICTYPE_HERO.getValue()) {
            DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, message));
        }
    }
    
    public static void pushChatMessage(final RespChat respChat) {
        DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, respChat));
    }
    
    public static void pushChatMessage(final RespSystemChat systemChat) {
        DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, systemChat));
    }
    
    public static void pushChatMessage(final Npc npc, final RespChat respChat) {
        if (respChat.getType() == 1) {
            DataCache.ONLINE_ROLES.values().forEach(role -> {
                if (role.getPos().getMapId().equals(npc.getMap())) {
                    MessagePusher.pushMessage(role, respChat);
                }
            });
        }
        else if (respChat.getType() == 2) {
            pushChatMessage(respChat);
        }
    }
    
    public static void pushTeamRecruitChatMessage(final RespChat respChat) {
        final TeamService teamService = SpringUtils.getTeamService();
        DataCache.ONLINE_ROLES.values().forEach(onlineRole -> {
            final Team team = teamService.getTeam(onlineRole.getRoleId());
            if (team == null) {
                MessagePusher.pushMessage(onlineRole, respChat);
            }
        });
    }
    
    public static void pushPartyChatMessage(final Integer partyId, final RespChat respChat) {
        DataCache.ONLINE_ROLES.values().forEach(onlineRole -> {
            if (!(!partyId.equals(onlineRole.getFaction()))) {
                MessagePusher.pushMessage(onlineRole, respChat);
            }
        });
    }
    
    public static void sendAllOnlinePlayers(final Message message) {
        DataCache.ONLINE_ROLES.values().forEach(role -> MessagePusher.pushMessage(role, message));
    }
    
    static {
        SenderUtils.logger = LoggerFactory.getLogger(SenderUtils.class);
    }
}
