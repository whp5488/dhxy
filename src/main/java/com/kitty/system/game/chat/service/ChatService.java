package com.kitty.system.game.chat.service;

import com.kitty.system.game.chat.message.RespDanMuMessage;
import com.kitty.system.game.chat.message.RespTempFriendState;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.chat.message.RespSystemChat;
import com.kitty.system.game.party.service.PartyService;
import com.kitty.system.game.team.domain.Team;
import com.kitty.mina.message.Message;
import com.kitty.system.admin.service.AdminService;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.enter.message.RespNotifyMisc;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.team.domain.Member;
import com.kitty.system.common.utils.SenderUtils;
import java.util.Date;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.utils.FieldUtils;
import org.nutz.lang.Strings;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.AsktaoUtil;
import java.text.MessageFormat;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.game.chat.message.ReqChat;
import org.apache.mina.core.session.IoSession;
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
        this.chatCache = new LimitedCacheMap(500, 900000L);
        this.chatForbid = true;
    }
    
    public void chatObjectDetail(Role role, ReqChatObjectDetail req) {
        Role ownerRole = this.roleService.getOnlinePlayer(req.getOwnerRoleId());
        if (ownerRole == null) {
            MessagePusher.pushMessage(role, new RespMsg("拥有者不在线，无法查看！"));
            return;
        }
        String id = req.getId();
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
                this.logger.error("宠物配饰：{}", Byte.valueOf(req.getType()));
                break;
            }
            default: {
                this.logger.error("未解析查询类型：{}", Byte.valueOf(req.getType()));
                break;
            }
        }
        return;
    }
    
    public void chat(IoSession session, ReqChat chat) {
        chat.setMsg(CheckCharUtils.filterEmoji(chat.getMsg()));
        if (Const.CHART_LIMIT.contains(Integer.valueOf(chat.getType()))) {
            MessagePusher.pushMessage(session, new RespMsg("该频道暂不支持发言"));
            return;
        }
        Object object = session.getAttribute("chatSpeed");
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        if ((long)role.getLockTime() > System.currentTimeMillis()) {
            return;
        }
        Message message = DataGlobalFunc.operationalConstraint(role, Deploy.speak, "充值#<R>%d#才发言哦！");
        if (message != null) {
            if ((int)role.getRelive() * 1000 + (int)role.getLevel() < 80) {
                MessagePusher.pushMessage(role, new RespMsg("发言在#<R>80#级开启。"));
                return;
            }
            if (Deploy.curfew) {
                double hourMinute = (double)TimeUtil.hour() + (double)TimeUtil.minute() / 100.0;
                if (hourMinute > Deploy.curfewStartTime && hourMinute < Deploy.curfewEndTime) {
                    String[] curfewStartTimeSplit = String.valueOf(Deploy.curfewStartTime).split("\\.");
                    String[] curfewEndTimeSplit = String.valueOf(Deploy.curfewEndTime).split("\\.");
                    String format = MessageFormat.format("宵禁时间：#<R>{0}点{1}分≈{2}点{3}分#", curfewStartTimeSplit[0], curfewStartTimeSplit[1], curfewEndTimeSplit[0], curfewEndTimeSplit[1]);
                    MessagePusher.pushMessage(session, new RespMsg(format));
                    return;
                }
            }
            else {
                MessagePusher.pushMessage(session, message);
                return;
            }
        }
        Team team = this.teamService.getTeam((int)role.getRoleId());
        int time = 0;
        int worldNum = 0;
        int needMoney = 0;
        NutMap nutMap = NutMap.NEW();
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (object == null) {
            nutMap.setv("chatTime", Integer.valueOf(time));
            session.setAttribute("chatSpeed", nutMap);
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
            ((AdminService)SpringUtils.getBean(AdminService.class)).blockPlayer(role, System.currentTimeMillis() + 43200000L, "聊天带有敏感词：" + chat.getMsg());
            return;
        }
        nutMap.setv("chatTime", Integer.valueOf(currTime));
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
                    if ((long)role.getCoin() < (long)needMoney) {
                        if ((long)role.getCoin() < (long)needMoney) {
                            return;
                        }
                        role.setCoin(Long.valueOf((long)role.getCoin() - (long)needMoney));
                        MessagePusher.pushMessage(session, new RespNotifyMiscEx(role.getId(), "发言成功，扣除" + AsktaoUtil.getMoneyFormat(needMoney) + "贡币。", false));
                    }
                    this.roleService.refreshRoleCurrency(role);
                }
                ++worldNum;
            }
            else {
                worldNum = 1;
            }
            nutMap.setv("worldNum", Integer.valueOf(worldNum));
            nutMap.setv("chatTime", Integer.valueOf(currTime));
        }
        else if (chat.getType() == 4 && team == null) {
            return;
        }
        session.setAttribute("chatSpeed", nutMap);
        if (chat.getType() == 11) {
            this.sendDanMu(role, team, chat.getMsg());
            return;
        }
        RespChat res = new RespChat();
        if (!Strings.isEmpty(chat.getToken())) {
            res.setToken(chat.getToken());
        }
        res.setMsg(chat.getMsg());
        res.setList(FieldUtils.getChatList(role));
        res.setRoleName(role.getName());
        res.setRoleId((int)role.getRoleId());
        res.setTime(currTime);
        if (chat.getType() == 1) {
            res.setType(1);
            ((BroadcastService)SpringUtils.getBean(BroadcastService.class)).sendLocalMessage(role, res);
        }
        else if (chat.getType() == 2) {
            if ((int)role.getVip() > 0) {
                res.setType(2);
                MessagePusher.pushMessage(role, res);
                return;
            }
            if ((int)role.getLevel() < 10) {
                MessagePusher.pushMessage(role, new RespMsg("世界频道在#<R>10#级开启。"));
                return;
            }
            res.setType(2);
            SenderUtils.pushChatMessage(res);
        }
        else if (chat.getType() == 20) {
            Integer result = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, (int)Integer.valueOf(chat.getPara()), 1, false, true, "喇叭消息");
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
                    Member member = (Member)team.getList().get(i);
                    Role memberRole = this.roleService.getOnlinePlayer((long)member.getId());
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
                return;
            }
        }
        else if (chat.getType() == 5) {
            if (Strings.isEmpty(role.getName())) {
                return;
            }
            PartyService partyService = SpringUtils.getPartyService();
            if (partyService.isChatDeny(role)) {
                long chatDenyEndTime = partyService.getChatDenyEndTime(role) * 1000L;
                if (chatDenyEndTime > 0L && chatDenyEndTime > System.currentTimeMillis()) {
                    String formatDate = TimeUtil.formatDate(new Date(chatDenyEndTime), "yyyy-MM-dd HH:mm:ss");
                    MessagePusher.notify2Player(role, 357, formatDate);
                    return;
                }
            }
            res.setType(5);
            res.setPartyName(role.getName());
            SenderUtils.pushPartyChatMessage(role.getFaction(), res);
        }
        return;
    }
    
    public void addChatCache(String uuId, NutMap itemMap) {
        this.chatCache.put(uuId, itemMap);
    }
    
    public void sendInfo(Long id, String msg) {
        RespChat respChat = new RespChat();
        respChat.setType(10);
        respChat.setMsg(msg);
        MessagePusher.pushMessage((long)id, respChat);
    }
    
    public void sendSystem(String msg, String name) {
        RespSystemChat systemChat = new RespSystemChat();
        systemChat.setName(name);
        systemChat.setType(7);
        systemChat.setMsg(msg);
        SenderUtils.pushChatMessage(systemChat);
    }
    
    public void sendAdnotice(String msg) {
        RespSystemChat systemChat = new RespSystemChat();
        systemChat.setMsg(msg);
        systemChat.setType(21);
        SenderUtils.pushChatMessage(systemChat);
    }
    
    public void privateChat(Role from, Role to, String msg, int time) {
        RespChat res = new RespChat();
        res.setRoleId((int)from.getRoleId());
        res.setRoleName(from.getName());
        res.setFriendId((int)to.getRoleId());
        res.setMsg(msg);
        res.setTime(time);
        res.setType(3);
        res.setList(FieldUtils.getChatList(from));
        SenderUtils.pushChatMessage(res);
    }
    
    public void localChat(Role from, String msg) {
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        RespChat res = new RespChat();
        res.setMsg("当前频道：" + msg);
        res.setList(FieldUtils.getChatList(from));
        res.setRoleName(from.getName());
        res.setRoleId((int)from.getRoleId());
        res.setTime(currTime);
        res.setType(1);
        SenderUtils.pushChatMessage(res);
    }
    
    public void getNpcRespChat(Npc npc, int type, String content) {
        RespChat respChat = new RespChat();
        respChat.setRoleId(npc.getId());
        respChat.setRoleName(npc.getName());
        respChat.setMsg(content);
        respChat.setList(FieldUtils.getChatList(npc));
        respChat.setType(type);
        SenderUtils.pushChatMessage(npc, respChat);
    }
    
    public void sendLaba(String msg) {
        RespChat res = new RespChat();
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        res.setMsg(msg);
        res.setTime(currTime);
        res.setType(19);
        SenderUtils.pushChatMessage(res);
    }
    
    public void reqTempFriendState(Role role, String gid) {
        long uid = Long.parseLong(gid);
        Role targetRole = this.roleService.getOnlinePlayer(uid);
        RespTempFriendState respTempFriendState = new RespTempFriendState();
        respTempFriendState.setGid(gid);
        respTempFriendState.setOnline((int)(byte)((targetRole == null) ? 2 : 1));
        MessagePusher.pushMessage(role, respTempFriendState);
    }
    
    public RespDanMuMessage sendDanMu(Role role, Team team, String msg) {
        RespDanMuMessage danMuMessage = new RespDanMuMessage();
        danMuMessage.setId((int)role.getRoleId());
        danMuMessage.setName(role.getName());
        danMuMessage.setMsg(msg);
        danMuMessage.setList(FieldUtils.getChatList(role));
        if (team != null) {
            danMuMessage.setChannel(4);
            for (int i = 0; i < team.getList().size(); ++i) {
                Member member = (Member)team.getList().get(i);
                if (member.isInTeam()) {
                    MessagePusher.pushMessage((long)member.getId(), danMuMessage);
                }
            }
            return danMuMessage;
        }
        MessagePusher.pushMessage((long)role.getId(), danMuMessage);
        return danMuMessage;
    }
}
