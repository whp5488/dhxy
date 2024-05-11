
package com.kitty.system.game.chat.service;

import com.kitty.system.game.chat.message.RespDanMuMessage;
import com.kitty.system.game.chat.message.RespTempFriendState;
import com.kitty.system.game.vo.Npc;
import java.util.ArrayList;
import com.kitty.system.game.chat.message.RespSystemChat;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.team.domain.Team;
import java.util.Date;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.enter.message.RespNotifyMisc;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.common.utils.FieldUtils;
import org.nutz.lang.Strings;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.AsktaoUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.admin.service.AdminService;
import com.kitty.logs.LoggerFunction;
import java.text.MessageFormat;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.game.chat.message.ReqChat;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.chat.message.ReqChatObjectDetail;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import org.nutz.lang.util.NutMap;
import com.kitty.common.utils.LimitedCacheMap;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.skill.service.SkillService;
import com.kitty.system.game.pet.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.equip.service.EquipService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class ChatService
{
    private Logger logger;
    @Autowired
    EquipService equipService;
    @Autowired
    PetService petService;
    @Autowired
    SkillService skillService;
    @Autowired
    ServerService serverService;
    @Autowired
    RoleService roleService;
    @Autowired
    TeamService teamService;
    private LimitedCacheMap<String, NutMap> chatCache;
    public boolean chatForbid;
    
    public ChatService() {
        this.logger = LoggerFactory.getLogger(ChatService.class);
        this.chatCache = new LimitedCacheMap<String, NutMap>(500, 900000L);
        this.chatForbid = true;
    }
    
    public void chatObjectDetail(final Role role, final ReqChatObjectDetail req) {
        final Role ownerRole = this.roleService.getOnlinePlayer(req.getOwnerRoleId());
        if (ownerRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("拥有者不在线，无法查看！"));
            return;
        }
        final String id = req.getId();
        if (id == null) {
            return;
        }
        switch (req.getType()) {
            case 0: {
                this.roleService.chatEquipDetail(ownerRole, role, Long.parseLong(id));
                break;
            }
            case 2: {
                this.petService.chatPetDetail(ownerRole, role, Integer.parseInt(id));
                break;
            }
            case 13: {
                this.logger.error("宠物配饰：{}", (Object)req.getType());
                break;
            }
            default: {
                this.logger.error("未解析查询类型：{}", (Object)req.getType());
                break;
            }
        }
    }
    
    public void chat(final IoSession session, final ReqChat chat) {
        chat.setMsg(CheckCharUtils.filterEmoji(chat.getMsg()));
        if (Const.CHART_LIMIT.contains(chat.getType())) {
            MessagePusher.pushMessage(session, new RespMsg("该频道暂不支持发言"));
            return;
        }
        final Object object = session.getAttribute((Object)"chatSpeed");
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if (role.getLockTime() > System.currentTimeMillis()) {
            return;
        }
        final Message message = DataGlobalFunc.operationalConstraint(role, Deploy.speak, "充值#<R>%d#才发言哦！");
        if (message != null) {
            if (role.getRelive() * 1000 + role.getLevel() < 80) {
                MessagePusher.pushMessage(role, new RespMsg("发言在#<R>80#级开启。"));
                return;
            }
            if (!Deploy.curfew) {
                MessagePusher.pushMessage(session, message);
                return;
            }
            final double hourMinute = TimeUtil.hour() + TimeUtil.minute() / 100.0;
            if (hourMinute > Deploy.curfewStartTime && hourMinute < Deploy.curfewEndTime) {
                final String[] curfewStartTimeSplit = String.valueOf(Deploy.curfewStartTime).split("\\.");
                final String[] curfewEndTimeSplit = String.valueOf(Deploy.curfewEndTime).split("\\.");
                final String format = MessageFormat.format("宵禁时间：#<R>{0}点{1}分≈{2}点{3}分#", curfewStartTimeSplit[0], curfewStartTimeSplit[1], curfewEndTimeSplit[0], curfewEndTimeSplit[1]);
                MessagePusher.pushMessage(session, new RespMsg(format));
                return;
            }
        }
        final Team team = this.teamService.getTeam(role.getRoleId());
        int time = 0;
        int worldNum = 0;
        int needMoney = 0;
        NutMap nutMap = NutMap.NEW();
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (object == null) {
            nutMap.setv("chatTime", (Object)time);
            session.setAttribute((Object)"chatSpeed", (Object)nutMap);
        }
        else {
            nutMap = (NutMap)object;
            time = nutMap.getInt("chatTime", 0);
            worldNum = nutMap.getInt("worldNum", 0);
        }
        if (currTime - time <= 5) {
            MessagePusher.pushMessage(session, new RespMsg("您说话太频繁了！"));
            return;
        }
        if (this.chatForbid && CheckCharUtils.checkQQNumber(role.getName())) {
            LoggerFunction.FORBID.getLogger().error("{} {}名字[{}]带有qq群号", new Object[] { role.getRoleId(), role.getName(), chat.getMsg() });
            SpringUtils.getBean(AdminService.class).blockPlayer(role, System.currentTimeMillis() + 43200000L, "聊天带有敏感词：" + chat.getMsg());
            return;
        }
        nutMap.setv("chatTime", (Object)currTime);
        if (chat.getType() == 2) {
            if (currTime - time <= 300) {
                if (worldNum > 10) {
                    needMoney = (worldNum - 10) * 20000;
                    if (needMoney < 0) {
                        return;
                    }
                    if (needMoney > 5000000) {
                        needMoney = 5000000;
                    }
                    if (role.getCoin() < needMoney) {
                        if (role.getCoin() < needMoney) {
                            return;
                        }
                        role.setCoin(role.getCoin() - needMoney);
                        MessagePusher.pushMessage(session, new RespNotifyMiscEx(role.getId(), "发言成功，扣除" + AsktaoUtil.getMoneyFormat(needMoney) + "贡币。", false));
                    }
                    this.roleService.refreshRoleCurrency(role);
                }
                ++worldNum;
            }
            else {
                worldNum = 1;
            }
            nutMap.setv("worldNum", (Object)worldNum);
            nutMap.setv("chatTime", (Object)currTime);
        }
        else if (chat.getType() == 4 && team == null) {
            return;
        }
        session.setAttribute((Object)"chatSpeed", (Object)nutMap);
        if (chat.getType() == 11) {
            this.sendDanMu(role, team, chat.getMsg());
            return;
        }
        final RespChat res = new RespChat();
        if (!Strings.isEmpty((CharSequence)chat.getToken())) {
            res.setToken(chat.getToken());
        }
        res.setMsg(chat.getMsg());
        res.setList(FieldUtils.getChatList(role));
        res.setRoleName(role.getName());
        res.setRoleId(role.getRoleId());
        res.setTime(currTime);
        if (chat.getType() == 1) {
            res.setType(1);
            SpringUtils.getBean(BroadcastService.class).sendLocalMessage(role, res);
        }
        else if (chat.getType() == 2) {
            if (role.getVip() > 0) {
                res.setType(2);
                MessagePusher.pushMessage(role, res);
                return;
            }
            if (role.getLevel() < 10) {
                MessagePusher.pushMessage(role, new RespMsg("世界频道在#<R>10#级开启。"));
                return;
            }
            res.setType(2);
            SenderUtils.pushChatMessage(res);
        }
        else if (chat.getType() == 20) {
            final Integer result = SpringUtils.getBean(BagService.class).consumeBagGoods(role, Integer.valueOf(chat.getPara()), 1, false, true, "喇叭消息");
            if (result != null) {
                return;
            }
            MessagePusher.pushMessage(session, new RespNotifyMisc("消耗了1个铃铛。"));
            res.setType(20);
            res.setPara(chat.getPara());
            SenderUtils.pushChatMessage(res);
        }
        else if (chat.getType() == 4) {
            if (team != null) {
                res.setRoleName(role.getName());
                res.setType(4);
                for (int i = 0; i < team.getList().size(); ++i) {
                    final Member member = team.getList().get(i);
                    final Role memberRole = this.roleService.getOnlinePlayer(member.getId());
                    MessagePusher.pushMessage(memberRole, res);
                }
            }
        }
        else if (chat.getType() == 17) {
            if (team != null) {
                res.setRoleName(role.getName());
                res.setType(17);
                MessagePusher.pushMessage(role, res);
                SenderUtils.pushTeamRecruitChatMessage(res);
            }
        }
        else if (chat.getType() == 5) {
            if (Strings.isEmpty((CharSequence)role.getName())) {
                return;
            }
            final PartyService partyService = SpringUtils.getPartyService();
            if (partyService.isChatDeny(role)) {
                final long chatDenyEndTime = partyService.getChatDenyEndTime(role) * 1000L;
                if (chatDenyEndTime > 0L && chatDenyEndTime > System.currentTimeMillis()) {
                    final String formatDate = TimeUtil.formatDate(new Date(chatDenyEndTime), "yyyy-MM-dd HH:mm:ss");
                    MessagePusher.notify2Player(role, 357, formatDate);
                    return;
                }
            }
            res.setType(5);
            res.setPartyName(role.getName());
            SenderUtils.pushPartyChatMessage(role.getFaction(), res);
        }
    }
    
    public void addChatCache(final String uuId, final NutMap itemMap) {
        this.chatCache.put(uuId, itemMap);
    }
    
    public void sendInfo(final Long id, final String msg) {
        final RespChat respChat = new RespChat();
        respChat.setType(10);
        respChat.setMsg(msg);
        MessagePusher.pushMessage(id, respChat);
    }
    
    public void sendSystem(final String msg, final String name) {
        final RespSystemChat systemChat = new RespSystemChat();
        systemChat.setName(name);
        systemChat.setType(7);
        systemChat.setMsg(msg);
        SenderUtils.pushChatMessage(systemChat);
    }
    
    public void sendAdnotice(final String msg) {
        final RespSystemChat systemChat = new RespSystemChat();
        systemChat.setMsg(msg);
        systemChat.setList(new ArrayList<FiedValue>());
        systemChat.setTime(new Long(System.currentTimeMillis() / 1000L).intValue());
        systemChat.setType(21);
        SenderUtils.pushChatMessage(systemChat);
    }
    
    public void privateChat(final Role from, final Role to, final String msg, final int time) {
        final RespChat res = new RespChat();
        res.setRoleId(from.getRoleId());
        res.setRoleName(from.getName());
        res.setFriendId(to.getRoleId());
        res.setMsg(msg);
        res.setTime(time);
        res.setType(3);
        res.setList(FieldUtils.getChatList(from));
        SenderUtils.pushChatMessage(res);
    }
    
    public void localChat(final Role from, final String msg) {
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        final RespChat res = new RespChat();
        res.setMsg("当前频道：" + msg);
        res.setList(FieldUtils.getChatList(from));
        res.setRoleName(from.getName());
        res.setRoleId(from.getRoleId());
        res.setTime(currTime);
        res.setType(1);
        SenderUtils.pushChatMessage(res);
    }
    
    public void getNpcRespChat(final Npc npc, final int type, final String content) {
        final RespChat respChat = new RespChat();
        respChat.setRoleId(npc.getId());
        respChat.setRoleName(npc.getName());
        respChat.setMsg(content);
        respChat.setList(FieldUtils.getChatList(npc));
        respChat.setType(type);
        SenderUtils.pushChatMessage(npc, respChat);
    }
    
    public void sendLaba(final String msg) {
        final RespChat res = new RespChat();
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        res.setMsg(msg);
        res.setTime(currTime);
        res.setType(19);
        SenderUtils.pushChatMessage(res);
    }
    
    public void reqTempFriendState(final Role role, final String gid) {
        final long uid = Long.parseLong(gid);
        final Role targetRole = this.roleService.getOnlinePlayer(uid);
        final RespTempFriendState respTempFriendState = new RespTempFriendState();
        respTempFriendState.setGid(gid);
        respTempFriendState.setOnline((byte)((targetRole == null) ? 2 : 1));
        MessagePusher.pushMessage(role, respTempFriendState);
    }
    
    public RespDanMuMessage sendDanMu(final Role role, final Team team, final String msg) {
        final RespDanMuMessage danMuMessage = new RespDanMuMessage();
        danMuMessage.setId(role.getRoleId());
        danMuMessage.setName(role.getName());
        danMuMessage.setMsg(msg);
        danMuMessage.setList(FieldUtils.getChatList(role));
        if (team != null) {
            danMuMessage.setChannel(4);
            for (int i = 0; i < team.getList().size(); ++i) {
                final Member member = team.getList().get(i);
                if (member.isInTeam()) {
                    MessagePusher.pushMessage(member.getId(), danMuMessage);
                }
            }
        }
        else {
            MessagePusher.pushMessage(role.getId(), danMuMessage);
        }
        return danMuMessage;
    }
}
