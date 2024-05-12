
package com.kitty.system.game.party.service;

import org.apache.commons.lang3.time.StopWatch;
import java.util.function.ToIntFunction;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.party.message.vo.PartyLogInfo;
import com.kitty.system.game.party.message.vo.LogInfo;
import java.util.HashMap;
import com.kitty.system.game.party.message.RespPartyLog;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.listener.BaseGameEvent;
import com.kitty.system.game.party.event.QuitPartyEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.party.message.ReqPartyRejectLevel;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.party.model.vo.PartyTask;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.party.model.vo.PartyMemberObject;
import com.kitty.system.game.party.message.RespPartyMembers;
import com.kitty.system.game.party.model.PartyDeny;
import com.kitty.system.game.party.message.RespPartyRole;
import com.kitty.system.game.vo.TitleInfo;
import java.util.Set;
import com.kitty.system.game.party.message.ReqPartyMembers;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.game.team.message.RespClearnRequest;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.alibaba.fastjson.JSONObject;
import com.kitty.system.game.party.model.JoinMemberInfo;
import com.kitty.system.game.party.message.RespJoinPartyList;
import java.util.ConcurrentModificationException;
import com.kitty.system.game.party.message.RespPartyNotification;
import com.kitty.system.game.party.model.ReqJoinMember;
import java.util.Comparator;
import java.util.function.Function;
import com.kitty.common.utils.PageUtil;
import com.kitty.system.game.party.model.PartyInfo;
import com.kitty.system.game.party.message.RespPartyInfo;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.party.message.ReqQueryPartys;
import com.kitty.system.game.friend.RoleView;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.thread.IdGenerator;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.ArrayList;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.party.message.RespOpenParty;
import com.kitty.system.game.party.message.ReqSavePartyFlag;
import com.kitty.system.init.domain.SysPartyFlag;
import com.kitty.system.game.party.message.ReqPartyUnlockFlag;
import com.kitty.system.game.party.message.vo.PartyFlag;
import com.kitty.system.game.party.message.RespPartyFlag;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.mina.cache.Deploy;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.enter.message.RespNotifyMisc;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.party.message.ReqCreateParty;
import com.kitty.common.utils.StringUtils;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.party.model.PartyLog;
import java.util.Collection;
import com.kitty.system.game.party.model.PartyWarsRecord;
import com.kitty.system.game.party.model.PartyMember;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.common.utils.spring.SpringUtils;
import org.nutz.dao.Dao;
import com.kitty.system.game.party.model.Party;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.logs.LoggerFunction;
import org.springframework.jdbc.core.JdbcTemplate;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.role.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.role.service.RoleService;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PartyService
{
    private Logger logger;
    private static final String LOG_DATE_FORMAT = "yyyy-MM-dd";
    @Autowired
    RoleService roleService;
    @Autowired
    TitleService titleService;
    @Autowired
    EquipService equipService;
    @Autowired
    private PartyRedBagHandler redBagHandler;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    public PartyService() {
        this.logger = LoggerFunction.PARTY.getLogger();
    }
    
    public void loadParty() {
        final Map<Integer, Party> partyMap = new ConcurrentHashMap<Integer, Party>();
        final Map<String, Integer> name2Ids = new ConcurrentHashMap<String, Integer>();
        final Dao dao = SpringUtils.getBean(Dao.class);
        final List<Party> list = dao.query(Party.class, Cnd.NEW());
        for (final Party party : list) {
            party.doAfterInit();
            final List<PartyMember> members = dao.query(PartyMember.class, Cnd.where("serial", "=", party.getSerial()).groupBy(new String[] { "role_id" }).desc("join_time"));
            for (final PartyMember member : members) {
                party.getMembers().put(member.getRoleId(), member);
            }
            final List<PartyWarsRecord> warsRecordList = dao.query(PartyWarsRecord.class, Cnd.where("serial", "=", party.getSerial()));
            party.getWarsRecordList().addAll(warsRecordList);
            final List<PartyLog> partyLogs = dao.query(PartyLog.class, Cnd.where("serial", "=", party.getSerial()));
            party.setLogs(partyLogs);
            partyMap.put(party.getSerial(), party);
            name2Ids.put(party.getName(), party.getSerial());
        }
        PartyDataPool.partyMap.putAll(partyMap);
        PartyDataPool.partyName2Id.putAll(name2Ids);
    }
    
    public Party getParty(final Role role) {
        if (role == null) {
            return null;
        }
        return this.getParty(role.getFaction());
    }
    
    public Party getParty(final Integer serial) {
        return PartyDataPool.partyMap.get(serial);
    }
    
    public Party getPartyByName(final String partyName) {
        if (StringUtils.isEmpty(partyName)) {
            return null;
        }
        final Integer serial = PartyDataPool.partyName2Id.get(partyName);
        if (serial != null) {
            return this.getParty(serial);
        }
        return null;
    }
    
    public void confirmCreateParty(final Role role, final ReqCreateParty createParty) {
        if (role == null) {
            return;
        }
        if (this.checkNotice(role, createParty.getAnnounce())) {
            if (!this.checkCreateParty(role, createParty.getPartyName())) {
                return;
            }
            this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 20L, "创建帮派");
            final Party party = this.newParty(role, createParty);
            if (party != null) {
                MessagePusher.pushMessage(role, new RespHint(4, (short)1));
            }
            final String content = I18nIdDataPool.getI18nContent(361, 20, party.getName());
            MessagePusher.pushMessage(role, new RespNotifyMisc(content));
        }
    }
    
    private boolean checkCreateParty(final Role role, final String partyName) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            final Message message = DataGlobalFunc.operationalConstraint(role, Deploy.party, "充值#<R>%d#才可以创建帮派！");
            if (message != null) {
                MessagePusher.pushMessage(role, message);
                return false;
            }
            if (PartyDataPool.partyMap.size() >= Deploy.party_limit) {
                MessagePusher.pushMessage(role, new RespMsg("创建帮派已上限"));
                return false;
            }
        }
        final boolean check = this.checkPartyName(role, partyName);
        if (!check) {
            return false;
        }
        if (this.isHaveParty(role)) {
            MessagePusher.notify2Player(role, 302, new Object[0]);
            return false;
        }
        if (role.getLevel() < 30) {
            MessagePusher.notify2Player(role, 303, new Object[0]);
            return false;
        }
        if (role.getGold() < 20L) {
            MessagePusher.notify2Player(role, 403, new Object[0]);
            return false;
        }
        return true;
    }
    
    public void partyFlag(final Party party, final Role role) {
        final RespPartyFlag partyFlag = new RespPartyFlag();
        partyFlag.setText(party.getEmblemText());
        party.getFlag().forEach((key, value) -> partyFlag.getFlagList().add(new PartyFlag(key, value)));
        party.getFlagColor().forEach((key, value) -> partyFlag.getFlagColorList().add(new PartyFlag(key, value)));
        party.getLace().forEach((key, value) -> partyFlag.getLaceList().add(new PartyFlag(key, value)));
        party.getEmblem().forEach((key, value) -> partyFlag.getEmblemList().add(new PartyFlag(key, value)));
        party.getEmblemColor().forEach((key, value) -> partyFlag.getEmblemColorList().add(new PartyFlag(key, value)));
        MessagePusher.pushMessage(role.getId(), partyFlag);
    }
    
    public void unlockPartyFlag(final Party party, final Role role, final ReqPartyUnlockFlag reqPartyUnlockFlag) {
        if (1 != this.getPermission(role)) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("帮主才可以操作战旗哦！"));
            return;
        }
        final SysPartyFlag sysPartyFlag = PartyDataPool.partyFlagList.stream().filter(e -> e.getIndex() == reqPartyUnlockFlag.getIndex() && e.getClassify() == reqPartyUnlockFlag.getType()).findFirst().orElse(null);
        if (sysPartyFlag == null) {
            return;
        }
        switch (reqPartyUnlockFlag.getType()) {
            case 0: {
                final Byte flag = party.getFlag().get(reqPartyUnlockFlag.getIndex());
                if (flag != null) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg("你已经解锁该旗帜！"));
                    return;
                }
                if (this.roleService.deductCurrency(role, sysPartyFlag.getType(), sysPartyFlag.getPrice(), "解锁帮派旗帜")) {
                    party.getFlag().put(sysPartyFlag.getIndex(), (byte)0);
                    party.save();
                    this.partyFlag(party, role);
                    break;
                }
                break;
            }
            case 1: {
                final Byte lace = party.getLace().get(reqPartyUnlockFlag.getIndex());
                if (lace != null) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg("你已经解锁该边饰！"));
                    return;
                }
                if (this.roleService.deductCurrency(role, sysPartyFlag.getType(), sysPartyFlag.getPrice(), "解锁帮派边饰")) {
                    party.getLace().put(sysPartyFlag.getIndex(), (byte)0);
                    party.save();
                    this.partyFlag(party, role);
                    break;
                }
                break;
            }
            case 2: {
                final Byte emblem = party.getEmblem().get(reqPartyUnlockFlag.getIndex());
                if (emblem != null) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg("你已经解锁该纹章！"));
                    return;
                }
                if (this.roleService.deductCurrency(role, sysPartyFlag.getType(), sysPartyFlag.getPrice(), "解锁帮派纹章")) {
                    party.getEmblem().put(sysPartyFlag.getIndex(), (byte)0);
                    party.save();
                    this.partyFlag(party, role);
                    break;
                }
                break;
            }
            case 3: {
                final Byte flagColor = party.getFlagColor().get(reqPartyUnlockFlag.getIndex());
                if (flagColor != null) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg("你已经解锁该旗帜颜色！"));
                    return;
                }
                if (this.roleService.deductCurrency(role, sysPartyFlag.getType(), sysPartyFlag.getPrice(), "解锁帮派旗帜颜色")) {
                    party.getFlagColor().put(sysPartyFlag.getIndex(), (byte)0);
                    party.save();
                    this.partyFlag(party, role);
                    break;
                }
                break;
            }
            case 4: {
                final Byte emblemColor = party.getEmblemColor().get(reqPartyUnlockFlag.getIndex());
                if (emblemColor != null) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg("你已经解锁该纹章颜色！"));
                    return;
                }
                if (this.roleService.deductCurrency(role, sysPartyFlag.getType(), sysPartyFlag.getPrice(), "解锁帮派纹章颜色")) {
                    party.getEmblemColor().put(sysPartyFlag.getIndex(), (byte)0);
                    party.save();
                    this.partyFlag(party, role);
                    break;
                }
                break;
            }
        }
    }
    
    public void savePartyFlag(final Party party, final Role role, final ReqSavePartyFlag reqPartyFlag) {
        if (1 != this.getPermission(role)) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("帮主才可以操作战旗哦！"));
            return;
        }
        if (party.getFlag().get(reqPartyFlag.getFlag()) == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("本帮尚未解锁该旗帜"));
            return;
        }
        if (party.getFlagColor().get(reqPartyFlag.getFlagColor()) == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("本帮尚未解锁该旗帜颜色"));
            return;
        }
        if (party.getLace().get(reqPartyFlag.getLace()) == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("本帮尚未解锁该边饰"));
            return;
        }
        if (party.getEmblem().get(reqPartyFlag.getEmblem()) == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("本帮尚未解锁该纹章"));
            return;
        }
        if (party.getEmblemColor().get(reqPartyFlag.getEmblemColor()) == null) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("本帮尚未解锁该纹章"));
            return;
        }
        if (reqPartyFlag.getText() == null || !StringUtils.isChineseCharacter(reqPartyFlag.getText())) {
            MessagePusher.pushMessage(role.getId(), new RespMsg("必须是一个汉字"));
            return;
        }
        party.setEmblemText(reqPartyFlag.getText());
        party.getFlag().forEach((key, value) -> {
            if (key.equals(reqPartyFlag.getFlag())) {
                party.getFlag().put(key, (byte)1);
            }
            else {
                party.getFlag().put(key, (byte)0);
            }
            return;
        });
        party.getFlagColor().forEach((key, value) -> {
            if (key.equals(reqPartyFlag.getFlagColor())) {
                party.getFlagColor().put(key, (byte)1);
            }
            else {
                party.getFlagColor().put(key, (byte)0);
            }
            return;
        });
        party.getLace().forEach((key, value) -> {
            if (key.equals(reqPartyFlag.getLace())) {
                party.getLace().put(key, (byte)1);
            }
            else {
                party.getLace().put(key, (byte)0);
            }
            return;
        });
        party.getEmblem().forEach((key, value) -> {
            if (key.equals(reqPartyFlag.getEmblem())) {
                party.getEmblem().put(key, (byte)1);
            }
            else {
                party.getEmblem().put(key, (byte)0);
            }
            return;
        });
        party.getEmblemColor().forEach((key, value) -> {
            if (key.equals(reqPartyFlag.getEmblemColor())) {
                party.getEmblemColor().put(key, (byte)1);
            }
            else {
                party.getEmblemColor().put(key, (byte)0);
            }
            return;
        });
        party.save();
        this.partyFlag(party, role);
        MessagePusher.pushMessage(role.getId(), new RespMsg("保存成功"));
    }
    
    public void getRespOpenPatry(final Party party, final Role role) {
        final RespOpenParty respOpenParty = new RespOpenParty();
        try {
            respOpenParty.setSerial(party.getSerial());
            respOpenParty.setCreateTime(Math.toIntExact(party.getCreateTime() / 1000L));
            respOpenParty.setLevel(party.getLevel());
            respOpenParty.setCount(this.getPartyTotalCount(party));
            respOpenParty.setOnlineMember(this.getPartyOnlineCount(party));
            respOpenParty.setName(party.getName());
            respOpenParty.setNotice(party.getNotice());
            respOpenParty.setAim(party.getAim());
            respOpenParty.setActive(party.getActive());
            respOpenParty.setCapital(String.valueOf(party.getCapital()));
            respOpenParty.setCntech(String.valueOf(party.getCntech()));
            respOpenParty.setConsent(party.getConsent());
            respOpenParty.setConsentLevel(party.getConsentLevel());
            respOpenParty.setRefuse(party.getRefuse());
            respOpenParty.setRefuseLevel(party.getRefuseLevel());
            final PartyMember partyMember = party.getMembers().get(role.getRoleId());
            if (partyMember != null) {
                respOpenParty.setJoinTime(partyMember.getJoinTime());
                respOpenParty.setJob(partyMember.getJob());
            }
            respOpenParty.setStandings(party.getStandings());
            respOpenParty.setSum((short)party.getWarsRecordList().size());
            respOpenParty.setWin((short)party.getRating());
            respOpenParty.setLose((short)(respOpenParty.getSum() - respOpenParty.getWin()));
            final PartyMember member = party.getMembers().get(party.getMasterId());
            if (member != null) {
                final Role player = this.roleService.getPlayer(member.getRoleId());
                respOpenParty.getMaster().add(new FiedValue("n", (byte)4, party.getMasterName()));
                respOpenParty.getMaster().add(new FiedValue("s", (byte)2, member.getRoleSerial()));
                respOpenParty.getMaster().addAll(this.roleService.deyList(player.getColor()));
                respOpenParty.getMaster().add(new FiedValue("l", (byte)4, JsonUtils.object2String(this.equipService.getWearEffect(player))));
                respOpenParty.getMaster().add(new FiedValue("f", (byte)4, JsonUtils.object2String(new ArrayList())));
                respOpenParty.getMaster().add(new FiedValue("w", (byte)3, player.getWeaponIcon()));
                int gem = 0;
                final List<WearEquip> wearEquips = this.equipService.getEquipValid(player, false);
                if (wearEquips.size() > 0) {
                    gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
                }
                respOpenParty.getMaster().add(new FiedValue("gem", (byte)2, gem));
            }
            else {
                this.logger.error("查询帮主信息错误，帮派编号：{}", (Object)party.getSerial());
            }
            MessagePusher.pushMessage(role, respOpenParty);
        }
        catch (final Exception e) {
            this.logger.error("发送内政异常");
            e.printStackTrace();
        }
    }
    
    public short getPartyOnlineCount(final Party party) {
        if (party == null) {
            return 0;
        }
        if (party.getMembers() != null) {
            short count = 0;
            for (final PartyMember member : party.getMembers().values()) {
                if (this.roleService.isOnline(member.getRoleId())) {
                    ++count;
                }
            }
            return count;
        }
        return 0;
    }
    
    private short getPartyTotalCount(final Party party) {
        if (party == null) {
            return 0;
        }
        return (short)party.getMembers().size();
    }
    
    private Party newParty(final Role role, final ReqCreateParty createParty) {
        final Party party = new Party();
        party.setId(IdGenerator.getNextId());
        party.setSerial(SpringUtils.getIdentityService().getNextId(IdentityType.PARTY));
        party.setName(createParty.getPartyName().trim());
        party.setMasterId(role.getRoleId());
        party.setMasterName(role.getName());
        party.setCreateTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
        party.setAim(createParty.getAnnounce());
        createParty.setConsent(createParty.getConsent());
        party.getFlag().put((short)1, (byte)1);
        party.getFlagColor().put((short)1, (byte)1);
        party.getLace().put((short)1, (byte)1);
        party.getEmblem().put((short)1, (byte)1);
        party.getEmblemColor().put((short)1, (byte)1);
        this.addPartyMember(party, role, (byte)70);
        PartyDataPool.partyMap.put(party.getSerial(), party);
        PartyDataPool.partyName2Id.put(party.getName(), party.getSerial());
        party.save();
        role.setFaction(party.getSerial());
        role.save();
        this.addPartyTitle(party, role);
        this.roleService.refreshParty(role);
        return party;
    }
    
    public PartyMember addPartyMember(final Party party, final Role role, final byte job) {
        final long now = System.currentTimeMillis();
        final PartyMember partyMember = new PartyMember(role, party.getSerial(), now, job, IdGenerator.getNextId());
        partyMember.save();
        party.getMembers().put(partyMember.getRoleId(), partyMember);
        return partyMember;
    }
    
    private PartyMember addPartyMember(final Party party, final RoleView roleView, final byte job) {
        final long now = System.currentTimeMillis();
        final PartyMember partyMember = new PartyMember(roleView.getRole(), party.getSerial(), now, job, IdGenerator.getNextId());
        partyMember.save();
        party.getMembers().put(partyMember.getRoleId(), partyMember);
        return partyMember;
    }
    
    private boolean isHaveParty(final Role role) {
        return role == null || this.getParty(role.getFaction()) != null;
    }
    
    private boolean isPartyNameExist(final String partyName) {
        return this.getPartyByName(partyName) != null;
    }
    
    public void queryParties(final Role role, final ReqQueryPartys queryParty) {
        List<Party> parties = this.getSortedParties();
        if ("fuzzy_by".equals(queryParty.getType())) {
            parties = parties.stream().filter(party -> party.getSerial().equals(queryParty.getPara()) || party.getName().contains(queryParty.getPara())).collect(Collectors.toList());
        }
        this.sendParties(role, queryParty, parties);
    }
    
    public void sendParties(final Role role, final ReqQueryPartys queryParty, List<Party> parties) {
        final RespPartyInfo respPartyInfo = new RespPartyInfo();
        respPartyInfo.setOrder(queryParty.getType());
        respPartyInfo.setPage(queryParty.getPage());
        respPartyInfo.setSort(queryParty.getSort());
        respPartyInfo.setWay(queryParty.getWay());
        List<PartyInfo> list = new ArrayList<PartyInfo>();
        if (parties.size() > 0) {
            parties = PageUtil.subList(parties, (int)queryParty.getPage(), (int)queryParty.getSize());
            for (final Party party : parties) {
                final short partyTotalCount = this.getPartyTotalCount(party);
                final PartyInfo partyInfo = new PartyInfo();
                partyInfo.setSerial(party.getSerial());
                partyInfo.setName(party.getName());
                partyInfo.setLevel(party.getLevel());
                partyInfo.setMembers(partyTotalCount);
                partyInfo.setActive(party.getActive());
                partyInfo.setAim(party.getAim());
                partyInfo.setStandings(party.getStandings());
                partyInfo.setSum((short)party.getWarsRecordList().size());
                partyInfo.setWin((short)party.getRating());
                partyInfo.setLose((short)(partyInfo.getSum() - partyInfo.getWin()));
                final PartyMember member = party.getMembers().get(party.getMasterId());
                if (member != null) {
                    partyInfo.getMaster().add(new FiedValue("n", (byte)4, party.getMasterName()));
                    partyInfo.getMaster().add(new FiedValue("s", (byte)2, member.getRoleSerial()));
                    partyInfo.getMaster().add(new FiedValue("g", (byte)2, member.getRei() * 1000 + member.getLevel()));
                    list.add(partyInfo);
                }
                else {
                    this.logger.error("查询帮主信息错误，帮派编号：{}", (Object)party.getSerial());
                }
                Short flag = party.getFlag().entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(Map.Entry::getKey).findFirst().orElse(null);
                if (flag == null) {
                    flag = 1;
                }
                partyInfo.getFlag().add(new FiedValue("qz", (byte)2, flag));
                Short lace = party.getLace().entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(Map.Entry::getKey).findFirst().orElse(null);
                if (lace == null) {
                    lace = 1;
                }
                partyInfo.getFlag().add(new FiedValue("bs", (byte)2, lace));
                Short emblem = party.getEmblem().entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(Map.Entry::getKey).findFirst().orElse(null);
                if (emblem == null) {
                    emblem = 1;
                }
                partyInfo.getFlag().add(new FiedValue("wz", (byte)2, emblem));
                Short flagC = party.getFlagColor().entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(Map.Entry::getKey).findFirst().orElse(null);
                if (flagC == null) {
                    flagC = 1;
                }
                partyInfo.getFlag().add(new FiedValue("qzc", (byte)2, flagC));
                Short emblemC = party.getEmblemColor().entrySet().stream().filter(entry -> entry.getValue().equals(1)).map(Map.Entry::getKey).findFirst().orElse(null);
                if (emblemC == null) {
                    emblemC = 1;
                }
                partyInfo.getFlag().add(new FiedValue("wzc", (byte)2, emblemC));
                String text = party.getEmblemText();
                if (text == null || text.isEmpty()) {
                    text = "帮";
                }
                partyInfo.getFlag().add(new FiedValue("text", (byte)4, text));
            }
            if (queryParty.getSort() == 1) {
                if (queryParty.getWay() == 0) {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getSerial)).collect(Collectors.toList());
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getSerial).reversed()).collect(Collectors.toList());
                }
            }
            else if (queryParty.getSort() == 2) {
                if (queryParty.getWay() == 0) {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getName)).collect(Collectors.toList());
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getName).reversed()).collect(Collectors.toList());
                }
            }
            else if (queryParty.getSort() == 3) {
                if (queryParty.getWay() == 0) {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getLevel)).collect(Collectors.toList());
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getLevel).reversed()).collect(Collectors.toList());
                }
            }
            else if (queryParty.getSort() == 4) {
                if (queryParty.getWay() == 0) {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getMembers)).collect(Collectors.toList());
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getMembers).reversed()).collect(Collectors.toList());
                }
            }
            else if (queryParty.getSort() == 5) {
                if (queryParty.getWay() == 0) {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getActive)).collect(Collectors.toList());
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(PartyInfo::getActive).reversed()).collect(Collectors.toList());
                }
            }
            respPartyInfo.setList(list);
        }
        MessagePusher.pushMessage(role, respPartyInfo);
    }
    
    public List<Party> getSortedParties() {
        final ArrayList<Party> partyList = new ArrayList<Party>(PartyDataPool.partyMap.values());
        partyList.sort(Comparator.comparing(Party::getLevel).reversed());
        return partyList;
    }
    
    public void reqJoinParty(final Role role, final String partyName) {
        if (role == null || StringUtils.isEmpty(partyName)) {
            return;
        }
        final Party party = this.getPartyByName(partyName);
        if (party == null) {
            return;
        }
        if (this.isHaveParty(role)) {
            MessagePusher.notify2Player(role, 304, new Object[0]);
            return;
        }
        if (this.isPartyFull(party)) {
            MessagePusher.notify2Player(role, 305, new Object[0]);
            return;
        }
        final int curve = role.getRelive() * 1000 + role.getLevel();
        if (party.getConsent() == 0 && curve >= party.getConsentLevel()) {
            this.addPartyMember(party, new RoleView(role), (byte)10);
            this.doJoinParty(party, role);
            MessagePusher.pushMessage(role, new RespNotifyMisc(I18nIdDataPool.getI18nContent(316, party.getName())));
            this.npcPartyChat(party, I18nIdDataPool.getI18nContent(315, role.getName()));
            this.addPartyLog(party, 349, new String[] { role.getRoleId().toString(), role.getName() });
        }
        else {
            final ReqJoinMember reqJoinMember = new ReqJoinMember(role.getRoleId(), role.getName(), Math.toIntExact(System.currentTimeMillis() / 1000L), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().shortValue(), new ArrayList<Integer>());
            (party.getReqJoin()).put(role.getName(), reqJoinMember);
            this.notifyOnlineManager(party, new RespPartyNotification((short)2));
        }
    }
    
    private boolean isPartyFull(final Party party) {
        final short partyTotalCount = this.getPartyTotalCount(party);
        final int partyMaxCount = this.getPartyMaxMembers(party.getLevel());
        return partyTotalCount >= partyMaxCount;
    }
    
    public void oneKeyJoin(final Role role) {
        if (role == null) {
            return;
        }
        if (this.isHaveParty(role)) {
            return;
        }
        boolean joined = false;
        final List<Party> sortedParties = this.getSortedParties();
        for (final Party party : sortedParties) {
            if (this.isPartyFull(party)) {
                continue;
            }
            joined = this.autoJoin(role, party);
            if (joined) {
                break;
            }
        }
        if (!joined) {
            for (final Party party : sortedParties) {
                if (this.isPartyFull(party)) {
                    continue;
                }
                final ReqJoinMember reqJoinMember = new ReqJoinMember(role.getRoleId(), role.getName(), Math.toIntExact(System.currentTimeMillis() / 1000L), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().shortValue(), new ArrayList<Integer>());
                (party.getReqJoin()).put(role.getName(), reqJoinMember);
                this.notifyOnlineManager(party, new RespPartyNotification((short)2));
            }
        }
    }
    
    private void notifyOnlineManager(final Party party, final Message message) {
        if (party == null || message == null) {
            return;
        }
        try {
            for (final Integer key : party.getMembers().keySet()) {
                final PartyMember member = party.getMembers().get(key);
                if (member.getJob() > 10) {
                    final Role role = SpringUtils.getRoleService().getPlayerBy(key);
                    if (role == null) {
                        continue;
                    }
                    MessagePusher.pushMessage(role, message);
                }
            }
        }
        catch (final ConcurrentModificationException ex) {}
    }
    
    private boolean autoJoin(final Role role, final Party party) {
        if (role == null || party == null) {
            return false;
        }
        final int curve = role.getRelive() * 1000 + role.getLevel();
        if (party.getConsent() == 0 && curve >= party.getConsentLevel()) {
            this.addPartyMember(party, new RoleView(role), (byte)10);
            this.doJoinParty(party, role);
            final String acceptContent = I18nIdDataPool.getI18nContent(316, party.getName());
            MessagePusher.pushMessage(role, new RespNotifyMisc(acceptContent));
            final String content = I18nIdDataPool.getI18nContent(315, role.getName());
            this.npcPartyChat(party, content);
            final String mailTitle = I18nIdDataPool.getI18nContent(317);
            final String mailContent = I18nIdDataPool.getI18nContent(318, party.getName(), party.getName());
            this.addPartyLog(party, 349, new String[] { role.getRoleId().toString(), role.getName() });
            return true;
        }
        return false;
    }
    
    public void checkJoinList(final Role role) {
        if (role == null) {
            return;
        }
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        final RespJoinPartyList joinPartyList = new RespJoinPartyList();
        final ArrayList<JoinMemberInfo> list = new ArrayList<JoinMemberInfo>();
        final NutConcurrentMap reqJoin = party.getReqJoin();
        for (final Map.Entry<String, Object> entry : reqJoin.entrySet()) {
            final JoinMemberInfo join = new JoinMemberInfo();
            try {
                final String jsonString = JSONObject.toJSONString(entry.getValue());
                final ReqJoinMember member = (ReqJoinMember)JSONObject.parseObject(jsonString, (Class)ReqJoinMember.class);
                join.setName(entry.getKey());
                join.setId(member.getId());
                join.setRei(member.getRei());
                join.setLevel(member.getLevel());
                join.setSerial(member.getSerial());
                list.add(join);
            }
            catch (final Exception e) {
                this.logger.error("查看帮派申请列表异常：{}，玩家编号：{}", (Object)e.getMessage(), (Object)role.getId());
            }
        }
        joinPartyList.setList(list);
        MessagePusher.pushMessage(role, joinPartyList);
    }
    
    public void agreeJoinParty(final Role role, final String name) {
        try {
            if (role == null || StringUtils.isEmpty(name)) {
                return;
            }
            final Party party = this.getParty(role.getFaction());
            if (party == null) {
                return;
            }
            if (!this.isAgreeOrDenyApply(role)) {
                return;
            }
            final ArrayList<String> names = new ArrayList<String>();
            if ("all".equals(name)) {
                final Set<String> keySet = party.getReqJoin().keySet();
                if (keySet != null) {
                    names.addAll(keySet);
                }
            }
            else {
                names.add(name);
            }
            final RespClearnRequest respClearnRequest = new RespClearnRequest();
            respClearnRequest.setType("party");
            respClearnRequest.setNames(names);
            MessagePusher.pushMessage(role, respClearnRequest);
            for (final String tempName : names) {
                final ReqJoinMember reqJoinMember = party.getReqJoin().getAs(tempName, ReqJoinMember.class, null);
                party.getReqJoin().remove(tempName);
                if (reqJoinMember == null) {
                    continue;
                }
                final Role player = this.roleService.getPlayer(reqJoinMember.getId());
                if (player == null) {
                    continue;
                }
                final RoleView reqRoleView = new RoleView(player);
                if (this.isHaveParty(reqRoleView.getRole())) {
                    MessagePusher.notify2Player(role, 306, new Object[0]);
                }
                else if (!reqRoleView.isOnline() && PartyDataPool.offlineRoleParty.getOrDefault(reqJoinMember.getId(), 0) > 0) {
                    MessagePusher.notify2Player(role, 306, new Object[0]);
                }
                else {
                    this.addPartyMember(party, reqRoleView, (byte)10);
                    final Role reqRole = reqRoleView.getRole();
                    if (reqRole != null) {
                        ThreadLocalUtil.addLocalTask(reqRole, () -> this.joinParty(party, reqRole));
                    }
                    else {
                        PartyDataPool.offlineRoleParty.put(reqJoinMember.getId(), party.getSerial());
                    }
                    MessagePusher.pushMessage(role, new RespMsg("收人成功"));
                    final String content = I18nIdDataPool.getI18nContent(315, reqRoleView.getName());
                    this.npcPartyChat(party, content);
                    this.checkPartyMember(role, new ReqPartyMembers());
                    this.addPartyLog(party, 349, new String[] { role.getRoleId().toString(), role.getName() });
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    private boolean isAgreeOrDenyApply(final Role role) {
        final byte permission = this.getPermission(role);
        return permission == 1 || permission == 2 || permission == 4 || permission == 3 || permission == 5 || permission == 6;
    }
    
    private void joinParty(final Party party, final Role role) {
        this.doJoinParty(party, role);
        final String acceptContent = I18nIdDataPool.getI18nContent(316, party.getName());
        MessagePusher.pushMessage(role, new RespNotifyMisc(acceptContent));
        final String mailTitle = I18nIdDataPool.getI18nContent(317);
        final String mailContent = I18nIdDataPool.getI18nContent(318, party.getName(), party.getName());
    }
    
    private void doJoinParty(final Party party, final Role role) {
        if (party == null || role == null) {
            return;
        }
        role.setFaction(party.getSerial());
        this.addPartyTitle(party, role);
        role.save();
        this.roleService.refreshParty(role);
        MessagePusher.pushMessage(role, new RespHint(5, (short)1));
    }
    
    public void addPartyTitle(final Party party, final Role role) {
        final PartyMember partyMember = this.getPartyMember(party, role);
        if (partyMember == null) {
            return;
        }
        final String partyJobName = this.getPartyJobName(partyMember);
        if (partyJobName != null) {
            this.titleService.addTitle(role, new TitleInfo(this.getTitle(partyMember), 0));
        }
    }
    
    public void delPartyTitle(final Role role) {
        for (final Integer value : PartyDataPool.jobId2TitleSerial.values()) {
            this.titleService.delTitle(role, value);
        }
    }
    
    public PartyMember getPartyMember(final Party party, final Role role) {
        if (party == null || role == null) {
            return null;
        }
        return party.getMembers().get(role.getRoleId());
    }
    
    public void getPartyRoleInfo(final Role role, final int roleId) {
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        if (role.getRoleId().equals(roleId)) {
            return;
        }
        final Role player = this.roleService.getPlayer(roleId);
        if (player != null) {
            final Party query = this.getParty(player.getFaction());
            if (query == null || !query.getSerial().equals(party.getSerial())) {
                return;
            }
            if (party != null) {
                final PartyMember member = query.getMembers().get(roleId);
                final RespPartyRole partyRole = new RespPartyRole();
                final PartyDeny deny = query.getDenyMap().get(member.getRoleId());
                if (deny != null) {
                    partyRole.setState((byte)1);
                }
                else {
                    partyRole.setState((byte)0);
                }
                partyRole.setList(this.roleService.deyList(player.getColor()));
                final List<WearEquip> wearEquips = this.equipService.getEquipValid(player, false);
                if (wearEquips.size() > 0) {
                    partyRole.setWeaponIcon(player.getWeaponIcon());
                    final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
                    partyRole.setGem(num);
                }
                partyRole.setLight_effect(this.equipService.getWearEffect(player));
                partyRole.setRoleId(player.getRoleId());
                MessagePusher.pushMessage(role, partyRole);
            }
        }
    }
    
    public void checkPartyMember(final Role role, final ReqPartyMembers reqPartyMembers) {
        final Party party = this.getParty(role);
        if (party == null || party.getMembers().get(role.getRoleId()) == null) {
            this.clearParty(role);
            return;
        }
        final RespPartyMembers respPartyMembers = this.getRespPartyMembers(party, role, reqPartyMembers);
        MessagePusher.pushMessage(role, respPartyMembers);
    }
    
    public RespPartyMembers getRespPartyMembers(final Party party, final Role role, final ReqPartyMembers reqMembers) {
        final RespPartyMembers respPartyMembers = new RespPartyMembers();
        try {
            List<PartyMember> members = this.getMembers(role, party, reqMembers);
            if (reqMembers.getPage() == 1 && reqMembers.getSort() == 4 && reqMembers.getWay() == 0 && reqMembers.getList().isEmpty() && reqMembers.getPara().isEmpty()) {
                final List<PartyMember> memberList = new ArrayList<PartyMember>();
                final PartyMember member = party.getMembers().get(role.getRoleId());
                final byte onLineStatus = (byte)(this.roleService.isOnline(member.getRoleId()) ? 1 : 0);
                member.setOnLineStatus(onLineStatus);
                if (onLineStatus == 0) {
                    final Role r = this.roleService.getPlayer(role.getRoleId());
                    if (r != null) {
                        member.setLastOnlineTime(Math.toIntExact(r.getOfLineTime() / 1000L));
                    }
                }
                memberList.add(member);
                memberList.addAll(members);
                members = memberList;
            }
            List<PartyMemberObject> list = new ArrayList<PartyMemberObject>();
            for (final PartyMember member2 : members) {
                final PartyMemberObject object = new PartyMemberObject();
                BeanUtils.copyBeanProp(object, member2);
                object.setRei(member2.getRei());
                object.setLevel(member2.getLevel());
                object.setContribute(String.valueOf(member2.getContribute()));
                if (this.roleService.isOnline(member2.getRoleId())) {
                    object.setOnLineStatus((byte)1);
                }
                int taskCount = 0;
                if (reqMembers.getSort() == 13) {
                    taskCount = member2.getPartyTaskList().stream().collect(Collectors.summingInt(PartyTask::getCount));
                }
                else if (reqMembers.getSort() == 12) {
                    final int millis = TimeUtil.dayInMillis(TimeUtil.yesterday());
                    final PartyTask partyTask = member2.getPartyTaskList().stream().filter(task -> task.getTime() == millis).findFirst().orElse(null);
                    if (partyTask != null) {
                        taskCount = partyTask.getCount();
                    }
                }
                else {
                    final int millis = TimeUtil.dayInMillis(TimeUtil.when());
                    final PartyTask partyTask = member2.getPartyTaskList().stream().filter(task -> task.getTime() == millis).findFirst().orElse(null);
                    if (partyTask != null) {
                        taskCount = partyTask.getCount();
                    }
                }
                object.setPartyTask(taskCount);
                list.add(object);
            }
            if (reqMembers.getSort() == 11 || reqMembers.getSort() == 12 || reqMembers.getSort() == 13) {
                if (reqMembers.getWay() == 0) {
                    list = list.stream().sorted(Comparator.comparing(PartyMemberObject::getPartyTask)).collect(Collectors.toList());
                }
                else {
                    list = list.stream().sorted(Comparator.comparing(PartyMemberObject::getPartyTask).reversed()).collect(Collectors.toList());
                }
            }
            respPartyMembers.setList(list);
            respPartyMembers.setPage(reqMembers.getPage());
            respPartyMembers.setTail(this.getMemberCount(party));
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("获取该帮派成员信息错误");
        }
        return respPartyMembers;
    }
    
    public List<PartyMember> getMembers(final Role role, final Party party, final ReqPartyMembers reqMembers) {
        List<PartyMember> objects = new ArrayList<PartyMember>();
        List<PartyMember> members = party.getMembers().values().stream().collect(Collectors.toList());
        if (reqMembers.getList().isEmpty() && reqMembers.getPara().isEmpty()) {
            if (reqMembers.getPage() == 1 && reqMembers.getSort() == 4 && reqMembers.getWay() == 0) {
                for (int i = 0; i < members.size(); ++i) {
                    PartyMember member = members.get(i);
                    if (role.getRoleId().equals(member.getRoleId())) {
                        members.remove(i);
                        break;
                    }
                }
            }
        }
        else {
            List<Integer> serials = new ArrayList<Integer>();
            if (!reqMembers.getList().isEmpty()) {
                List<Integer> arrays = new ArrayList<Integer>();
                try {
                    arrays = JsonUtils.string2Collection(reqMembers.getList(), List.class, Integer.class);
                    serials = DataGlobalFunc.getRoleCareerSerial(arrays);
                }
                catch (final Exception e) {
                    this.logger.error("获取种族搜索数据错误：{}", (Object)reqMembers.getList());
                    e.printStackTrace();
                }
            }
            final List<PartyMember> list = party.getMembers().values().stream().collect(Collectors.toList());
            final List<Integer> finalSerials = serials;
            members = list.stream().filter(member -> member.getName().equals(reqMembers.getPara()) || finalSerials.contains(member.getRoleSerial())).collect(Collectors.toList());
        }
        final List<PartyMember> list2 = PageUtil.subList(members, (int)reqMembers.getPage(), (int)reqMembers.getSize());
        objects.addAll(list2);
        objects.forEach(member -> {
            final byte onLineStatus = (byte)(this.roleService.isOnline(member.getRoleId()) ? 1 : 0);
            member.setOnLineStatus(onLineStatus);
            if (onLineStatus == 0) {
                final Role r = this.roleService.getPlayer(role.getRoleId());
                if (r != null) {
                    member.setLastOnlineTime(Math.toIntExact(r.getOfLineTime() / 1000L));
                }
            }
            return;
        });
        if (reqMembers.getSort() == 1) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getName)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getName).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 2) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getGrade)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getGrade).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 3) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getRace)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getRace).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 4) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getJob)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getJob).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 5) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getTeam)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getTeam).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 6) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getJoinTime)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getJoinTime).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 7) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getOnLineStatus)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getOnLineStatus).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 8) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getWeekBangBtl)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getWeekBangBtl).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 9) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getThisBangBtl)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getThisBangBtl).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 10) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getWeekEcType)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getWeekEcType).reversed()).collect(Collectors.toList());
            }
        }
        else if (reqMembers.getSort() == 14) {
            if (reqMembers.getWay() == 0) {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getContribute)).collect(Collectors.toList());
            }
            else {
                objects = objects.stream().sorted(Comparator.comparing(PartyMember::getContribute).reversed()).collect(Collectors.toList());
            }
        }
        return objects;
    }
    
    public void refuseJoinParty(final Role role, final String name) {
        try {
            if (role == null || StringUtils.isEmpty(name)) {
                return;
            }
            final Party party = this.getParty(role.getFaction());
            if (party == null) {
                return;
            }
            if (!this.isAgreeOrDenyApply(role)) {
                return;
            }
            final RespClearnRequest respClearnRequest = new RespClearnRequest();
            final List<String> names = new ArrayList<String>();
            if ("all".equals(name)) {
                final Set<String> keySet = party.getReqJoin().keySet();
                if (keySet != null) {
                    names.addAll(keySet);
                }
            }
            else {
                names.add(name);
                respClearnRequest.setNames(names);
            }
            respClearnRequest.setType("party");
            MessagePusher.pushMessage(role, respClearnRequest);
            final NutConcurrentMap reqJoin = party.getReqJoin();
            if (reqJoin == null) {
                return;
            }
            for (final String tempName : names) {
                final ReqJoinMember reqJoinAs = reqJoin.getAs(tempName, ReqJoinMember.class, null);
                if (reqJoin != null) {
                    final Role sessionRole = SpringUtils.getRoleService().getOnlinePlayer(reqJoinAs.getId());
                    if (sessionRole != null) {
                        MessagePusher.notify2Player(sessionRole, 307, new Object[0]);
                    }
                }
                reqJoin.remove(tempName);
            }
            party.save();
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public void upgrade(final Role role) {
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return;
        }
        if (!this.isAgreeOrDenyApply(role)) {
            return;
        }
        final byte nextLevel = (byte)(party.getLevel() + 1);
        final Integer capital = PartyDataPool.partyUpgrades.get(nextLevel);
        if (capital == null) {
            MessagePusher.notify2Player(role, 372, new Object[0]);
            return;
        }
        if (party.getCapital() < capital) {
            MessagePusher.notify2Player(role, 373, capital);
            return;
        }
        if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, PartyDataPool.partyUpgrades.get(nextLevel), "升级帮派")) {
            return;
        }
        party.setLevel(nextLevel);
        party.save();
        this.getRespOpenPatry(party, role);
    }
    
    public void modifyNotice(final Role role, final String notice, final byte type) {
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return;
        }
        if (!this.isPartyLeader(role)) {
            MessagePusher.notify2Player(role, 345, new Object[0]);
            return;
        }
        if (this.checkNotice(role, notice)) {
            if (type == 0) {
                party.setNotice(notice);
            }
            else {
                party.setAim(notice);
            }
            party.save();
        }
        this.getRespOpenPatry(party, role);
        MessagePusher.notify2Player(role, 358, new Object[0]);
    }
    
    public boolean checkNotice(final Role role, final String notice) {
        if (notice.length() > 2048) {
            MessagePusher.notify2Player(role, 362, new Object[0]);
            return false;
        }
        final List<String> set = WordDisposePool.checkDirtyWords(notice);
        if (set.size() >= 1) {
            MessagePusher.pushMessage(role, new RespMsg("输入的内容存在敏感词。"));
            return false;
        }
        return true;
    }
    
    public void kickOut(final Role role, final int id) {
        if (role == null) {
            return;
        }
        if (role.getRoleId() == id) {
            return;
        }
        final GangWarsData objData = SpringUtils.getBean(GangWarsHandler.class).getObjData();
        if (objData != null && objData.getWarsMap().get(role.getFaction()) != null) {
            MessagePusher.pushMessage(role, new RespMsg("正临帮战，大敌当前，应精诚团结，一致对外，不宜踢人、离帮"));
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return;
        }
        if (!this.isInParty(party, id)) {
            MessagePusher.notify2Player(role, 308, new Object[0]);
            return;
        }
        if (!this.isKickPermission(role)) {
            MessagePusher.notify2Player(role, 309, new Object[0]);
            return;
        }
        if (this.isPermissionHigher(party, role.getRoleId(), id)) {
            MessagePusher.notify2Player(role, 310, new Object[0]);
            return;
        }
        this.doKickOut(role, party, id);
    }
    
    private boolean isPermissionHigher(final Role firstRole, final Role secondRole) {
        final byte firstPermission = this.getPermission(firstRole);
        final byte secondPermission = this.getPermission(secondRole);
        return firstPermission < secondPermission;
    }
    
    private boolean isPermissionHigher(final Party party, final int firstId, final int secondId) {
        final PartyMember firstPartyMember = party.getMembers().get(firstId);
        final PartyMember secondPartyMember = party.getMembers().get(secondId);
        final byte firstPermission = firstPartyMember.getJob();
        final byte secondPermission = secondPartyMember.getJob();
        return firstPermission < secondPermission;
    }
    
    private boolean isKickPermission(final Role role) {
        if (role == null) {
            return false;
        }
        final byte permission = this.getPermission(role);
        return permission == 1 || permission == 2;
    }
    
    private boolean isExistPermission(final Party party, final byte job) {
        final List<PartyMember> list = party.getMembers().values().stream().filter(member -> member.getJob() == job).collect(Collectors.toList());
        return list.size() > 0;
    }
    
    private byte getPermission(final Role role) {
        final String partyJob = this.getPartyJob(role);
        if (partyJob != null) {
            final Byte permission = PartyDataPool.partyPermissions.get(partyJob);
            if (permission != null) {
                return permission;
            }
        }
        return 8;
    }
    
    public String getPartyJob(final Role role) {
        final Party party = this.getParty(role);
        if (party == null) {
            return null;
        }
        final PartyMember partyMember = this.getPartyMember(party, role);
        return this.getPartyJobName(partyMember);
    }
    
    public String getPartyJob(final Party party, final Integer id) {
        if (party == null) {
            return null;
        }
        final PartyMember partyMember = this.getPartyMember(party, id);
        return this.getPartyJobName(partyMember);
    }
    
    public PartyMember getPartyMember(final Party party, final Integer id) {
        if (party == null) {
            return null;
        }
        return party.getMembers().get(id);
    }
    
    private void doKickOut(final Role role, final Party party, final int id) {
        if (party == null) {
            return;
        }
        final RoleView roleView = new RoleView(this.roleService.getPlayer(id));
        final String content = I18nIdDataPool.getI18nContent(314, this.getPartyJob(role), role.getName(), roleView.getName());
        this.npcPartyChat(party, content);
        final Role targetRole = roleView.getRole();
        if (targetRole != null) {
            ThreadLocalUtil.addLocalTask(targetRole, () -> this.leaveParty(party, targetRole));
        }
        else {
            PartyDataPool.offlineRoleParty.put(id, -1);
        }
        final PartyMember member = party.getMembers().get(id);
        member.setDelete(true);
        member.save();
        party.getMembers().remove(id);
        MessagePusher.notify2Player(role, 311, roleView.getName());
        MessagePusher.pushMessage(role, new RespHint(7, (short)1));
        this.addPartyLog(party, 351, new String[] { role.getRoleId().toString(), role.getName(), String.valueOf(roleView.getRoleId()), roleView.getName() });
    }
    
    private void leaveParty(final Party party, final Role role) {
        this.doLeaveParty(role);
        if (this.isInPartyMap(role)) {
            this.leavePartyMap(role);
        }
        final String kickContent = I18nIdDataPool.getI18nContent(313, party.getName());
        MessagePusher.pushMessage(role, new RespNotifyMisc(kickContent));
        final String mailTitle = I18nIdDataPool.getI18nContent(312);
        final String mailContent = I18nIdDataPool.getI18nContent(313, party.getName());
    }
    
    public void doLeaveParty(final Role role) {
        role.setFaction(0);
        role.save();
        this.delPartyTitle(role);
        this.roleService.refreshParty(role);
    }
    
    public void npcPartyChat(final Party party, final String content) {
        if (party == null || StringUtils.isEmpty(content)) {
            return;
        }
        final Npc npc = SpringUtils.getMapService().getNpc(90999);
        SenderUtils.pushPartyChatMessage(party.getSerial(), this.getPartyRespChat(npc, party.getName(), content));
    }
    
    private RespChat getPartyRespChat(final Npc npc, final String partyName, final String content) {
        final RespChat respChat = new RespChat();
        respChat.setRoleId(npc.getId());
        respChat.setRoleName(npc.getName());
        respChat.setPartyName(partyName);
        respChat.setMsg(content);
        respChat.setList(FieldUtils.getChatList(npc));
        respChat.setType(5);
        return respChat;
    }
    
    public void setAutoAgreeSetting(final Role role, final ReqPartyRejectLevel reqPartyRejectLevel) {
        if (role == null) {
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            MessagePusher.notify2Player(role, 319, new Object[0]);
            return;
        }
        if (!this.isAgreeOrDenyApply(role)) {
            MessagePusher.notify2Player(role, 345, new Object[0]);
            return;
        }
        party.setConsent(reqPartyRejectLevel.getConsent());
        party.setConsentLevel(reqPartyRejectLevel.getConsentLevel());
        party.setRefuse(reqPartyRejectLevel.getRefuse());
        party.setRefuseLevel(reqPartyRejectLevel.getRefuseLevel());
        party.save();
        MessagePusher.pushMessage(role, new RespMsg("设置成功"));
        MessagePusher.pushMessage(role, new RespHint(6, (short)1));
    }
    
    public void quitParty(final Role role) {
        if (role == null) {
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            MessagePusher.notify2Player(role, 319, new Object[0]);
            return;
        }
        final GangWarsData objData = SpringUtils.getBean(GangWarsHandler.class).getObjData();
        if (objData != null && objData.getWarsMap().get(role.getFaction()) != null) {
            MessagePusher.pushMessage(role, new RespMsg("正临帮战，大敌当前，应精诚团结，一致对外，不宜踢人、离帮"));
            return;
        }
        final String partyJob = this.getPartyJob(role);
        if ("帮主".equals(partyJob)) {
            PartyMember member = null;
            List<PartyMember> collect = party.getMembers().values().stream().filter(e -> e.getJob() > 10 && !role.getRoleId().equals(e.getRoleId())).collect(Collectors.toList());
            if (collect.size() > 0) {
                collect = collect.stream().sorted(Comparator.comparing(PartyMember::getJob, Comparator.reverseOrder())).collect(Collectors.toList());
                member = collect.get(0);
            }
            else {
                List<PartyMember> list = party.getMembers().values().stream().filter(e -> e.getJob() == 10).collect(Collectors.toList());
                list = list.stream().sorted(Comparator.comparing(PartyMember::getContribute, Comparator.reverseOrder())).collect(Collectors.toList());
                if (list != null && list.size() > 0) {
                    member = list.get(0);
                }
            }
            if (member == null) {
                return;
            }
            final Role player = this.roleService.getPlayer(member.getRoleId());
            if (party == null) {
                return;
            }
            member.setJob((byte)70);
            member.save();
            party.setMasterId(player.getRoleId());
            party.setMasterName(player.getName());
            this.delPartyTitle(player);
            this.addPartyTitle(party, player);
        }
        this.doLeaveParty(role);
        PartyMember member = party.getMembers().get(role.getRoleId());
        if (member != null) {
            member.setDelete(true);
            member.save();
        }
        party.getMembers().remove(role.getRoleId());
        this.npcPartyChat(party, I18nIdDataPool.getI18nContent(322, role.getName()));
        EventDispatcher.getInstance().fireEvent(new QuitPartyEvent(EventType.QUIT_PARTY, role, party));
        if (this.isInPartyMap(role)) {
            this.leavePartyMap(role);
        }
        MessagePusher.notify2Player(role, 321, party.getName());
        this.addPartyLog(party, 350, new String[] { role.getRoleId().toString(), role.getName() });
    }
    
    public boolean isInPartyMap(final Role role) {
        return role != null && (role.getPos().getMapId() == 3002 || role.getPos().getMapId() == 3021 || role.getPos().getMapId() == 3022);
    }
    
    public void leavePartyMap(final Role role) {
        final ReqTelePort reqTelePort = new ReqTelePort();
        SpringUtils.getMapService().changeMap(role, reqTelePort, null);
    }
    
    public void handleLogin(final Role role) {
        Party party = this.getParty(role.getFaction());
        final Integer newPartyId = PartyDataPool.offlineRoleParty.remove(role.getRoleId());
        if (party != null) {
            if (newPartyId != null && newPartyId == -1) {
                this.leaveParty(party, role);
            }
            else if (this.isAgreeOrDenyApply(role) && this.isHaveJoinRequest(party)) {
                final RespPartyNotification respPartyNotification = new RespPartyNotification((short)2);
                MessagePusher.pushMessage(role, respPartyNotification);
            }
        }
        else {
            if (this.isInPartyMap(role)) {
                this.leavePartyMap(role);
            }
            if (newPartyId != null && newPartyId > 0) {
                party = this.getParty(newPartyId);
                this.joinParty(party, role);
            }
        }
    }
    
    public void prohibitSpeaking(final Role role, final int id, final String name, final short hours) {
        if (role == null) {
            return;
        }
        if (role.getRoleId().equals(id)) {
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return;
        }
        if (!this.isInParty(party, id)) {
            MessagePusher.notify2Player(role, 308, new Object[0]);
            return;
        }
        if (!this.isDenyPermission(role)) {
            MessagePusher.notify2Player(role, 335, new Object[0]);
            return;
        }
        if (this.isPermissionHigher(party, role.getRoleId(), id)) {
            MessagePusher.notify2Player(role, 336, new Object[0]);
            return;
        }
        final int endTime = (int)((System.currentTimeMillis() + hours * 3600000L) / 1000L);
        final PartyDeny partyDeny = new PartyDeny(endTime);
        party.getDenyMap().put(id, partyDeny);
        party.save();
        final String content = I18nIdDataPool.getI18nContent(337, name, hours);
        this.npcPartyChat(party, content);
        MessagePusher.pushMessage(role, new RespMsg("禁言成功"));
        final Role targetRole = SpringUtils.getRoleService().getOnlinePlayer(id);
        if (targetRole != null) {
            final String mailTitle = I18nIdDataPool.getI18nContent(338);
            I18nIdDataPool.getI18nContent(339, hours, hours);
        }
    }
    
    private boolean isDenyPermission(final Role role) {
        final byte permission = this.getPermission(role);
        return permission == 1 || permission == 2;
    }
    
    public byte getJob(final Role role) {
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return 0;
        }
        final PartyMember partyMember = party.getMembers().get(role.getRoleId());
        if (partyMember == null) {
            return 0;
        }
        return partyMember.getJob();
    }
    
    public void changeJob(final Role role, final int id, final String name, final byte career) {
        if (role == null) {
            return;
        }
        if (role.getRoleId() == id) {
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return;
        }
        if (!this.isInParty(party, id)) {
            MessagePusher.notify2Player(role, 308, new Object[0]);
            return;
        }
        if (!this.isChangeJobPermission(role)) {
            MessagePusher.notify2Player(role, 326, new Object[0]);
            return;
        }
        if (this.isPermissionHigher(party, role.getRoleId(), id)) {
            MessagePusher.notify2Player(role, 327, new Object[0]);
            return;
        }
        final Byte jobId = PartyDataPool.partyCareer.get(career);
        if (this.isExistPermission(party, jobId)) {
            MessagePusher.notify2Player(role, 397, new Object[0]);
            return;
        }
        final String jobName = PartyDataPool.jobId2Names.get(jobId);
        final String oldJobName = this.getPartyJob(party, id);
        final long uid = SpringUtils.getPlayerService().getUidBy(id);
        final RoleView roleView = new RoleView(uid);
        final Role targetRole = roleView.getRole();
        final PartyMember partyMember = party.getMembers().get(id);
        partyMember.setJob(jobId);
        partyMember.save();
        final RespPartyMembers respPartyMembers = this.getRespPartyMembers(party, role, new ReqPartyMembers());
        MessagePusher.pushMessage(role, respPartyMembers);
        MessagePusher.pushMessage(role, new RespMsg("任命成功"));
        final String content = I18nIdDataPool.getI18nContent(328, role.getName(), name, oldJobName, jobName);
        this.npcPartyChat(party, content);
        if (targetRole != null) {
            final String mailTitle = I18nIdDataPool.getI18nContent(330);
            I18nIdDataPool.getI18nContent(331, role.getName(), oldJobName, jobName);
        }
        final String partyJob = this.getPartyJob(role);
        this.addPartyLog(party, 353, new String[] { partyJob, role.getRoleId().toString(), role.getName(), String.valueOf(roleView.getRoleId()), roleView.getName(), jobName });
        party.save();
    }
    
    private boolean isChangeJobPermission(final Role role) {
        final byte permission = this.getPermission(role);
        return permission == 1 || permission == 2;
    }
    
    private boolean isPartyLeader(final Role role) {
        final String partyJob = this.getPartyJob(role);
        return "帮主".equals(partyJob);
    }
    
    private boolean isInParty(final Party party, final int roleId) {
        return party != null && party.getMembers().containsKey(roleId);
    }
    
    public void cancelProhibitSpeaking(final Role role, final int id, final String name) {
        if (role == null) {
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            return;
        }
        if (!this.isInParty(party, id)) {
            MessagePusher.notify2Player(role, 308, new Object[0]);
            return;
        }
        if (!this.isDenyPermission(role)) {
            MessagePusher.notify2Player(role, 340, "解除禁言");
            return;
        }
        party.getDenyMap().remove(id);
        party.save();
        final String content = I18nIdDataPool.getI18nContent(341, name);
        this.npcPartyChat(party, content);
        MessagePusher.pushMessage(role, new RespMsg("解除禁言成功"));
        final Role targetRole = SpringUtils.getRoleService().getOnlinePlayer(id);
        if (targetRole != null) {
            final String mailTitle = I18nIdDataPool.getI18nContent(338);
            I18nIdDataPool.getI18nContent(342);
        }
    }
    
    public void getPartyLog(final Role role) {
        MessagePusher.pushMessage(role, this.getLog(role));
    }
    
    private RespPartyLog getLog(final Role role) {
        final RespPartyLog respPartyLog = new RespPartyLog();
        final Party party = this.getParty(role);
        if (party == null) {
            return respPartyLog;
        }
        final List<PartyLog> logs = new ArrayList<PartyLog>(party.getLogs());
        logs.sort(Comparator.comparing(PartyLog::getTime).reversed());
        final Map<Integer, List<LogInfo>> map = new HashMap<Integer, List<LogInfo>>();
        for (final PartyLog log : logs) {
            final int time = TimeUtil.dayInMillis(TimeUtil.stampToDate((long)(log.getTime() * 1000.0)));
            List<LogInfo> strings = map.get(time);
            if (strings == null) {
                if (map.size() >= 3) {
                    break;
                }
                strings = new ArrayList<LogInfo>();
            }
            final String content = I18nIdDataPool.getI18nContent(log.getTkId(), (Object[])log.getArgs());
            strings.add(new LogInfo(content));
            map.put(time, strings);
        }
        final List<PartyLogInfo> list = new ArrayList<PartyLogInfo>();
        for (final Integer key : map.keySet()) {
            final PartyLogInfo partyLogInfo = new PartyLogInfo();
            partyLogInfo.setTime(key);
            partyLogInfo.setList(map.get(key));
            list.add(partyLogInfo);
        }
        respPartyLog.setList(list);
        return respPartyLog;
    }
    
    public void rename(final Role role, final String name) {
        if (role == null) {
            return;
        }
        final Party party = this.getParty(role.getFaction());
        if (party == null) {
            MessagePusher.notify2Player(role, 319, new Object[0]);
            return;
        }
        if (!this.isPartyLeader(role)) {
            MessagePusher.notify2Player(role, 345, new Object[0]);
            return;
        }
        final boolean check = this.checkPartyName(role, name);
        if (!check) {
            return;
        }
        this.doRename(party, name);
    }
    
    private void doRename(final Party party, final String name) {
        PartyDataPool.partyName2Id.remove(party.getName());
        party.setName(name);
        party.save();
        PartyDataPool.partyName2Id.put(party.getName(), party.getSerial());
    }
    
    private void addPartyLog(final Party party, final int i18nId, final String[] args) {
        party.getLogs().add(new PartyLog(i18nId, args, party.getSerial()));
        final int size = party.getLogs().size();
        if (size > 50) {
            for (int i = 0, loop = size - 50; i < loop; ++i) {
                party.getLogs().remove(0);
            }
        }
        party.save();
    }
    
    private String getPartyJobName(final PartyMember partyMember) {
        if (partyMember == null) {
            return null;
        }
        return PartyDataPool.jobId2Names.get(partyMember.getJob());
    }
    
    private boolean checkPartyName(final Role role, final String name) {
        if (StringUtils.isBlank((CharSequence)name)) {
            MessagePusher.notify2Player(role, 348, new Object[0]);
            return false;
        }
        if (name.length() < 2) {
            MessagePusher.notify2Player(role, 346, new Object[0]);
            return false;
        }
        if (name.length() > 6) {
            MessagePusher.notify2Player(role, 347, new Object[0]);
            return false;
        }
        if (this.isPartyNameExist(name)) {
            MessagePusher.notify2Player(role, 301, new Object[0]);
            return false;
        }
        final List<String> set = WordDisposePool.checkDirtyWords(name);
        if (set.size() >= 1) {
            MessagePusher.pushMessage(role, new RespMsg("帮派名存在敏感词。"));
            return false;
        }
        return true;
    }
    
    public void sendPartyMail(final Role role, final String title, final String mailMsg) {
        if (role == null) {
            return;
        }
        MessagePusher.pushMessage(role, new RespMsg("公告已发送。"));
    }
    
    private boolean isSendMailPermission(final Role role) {
        final byte permission = this.getPermission(role);
        return permission == 1 || permission == 2;
    }
    
    private boolean isManager(final String jobName) {
        return Const.MANAGER_JOBS.contains(jobName);
    }
    
    public boolean isChatDeny(final Role role) {
        final Party party = this.getParty(role);
        return party == null || party.getDenyMap().containsKey(role.getRoleId());
    }
    
    public long getChatDenyEndTime(final Role role) {
        final Party party = this.getParty(role);
        if (party == null) {
            return 0L;
        }
        final PartyDeny partyDeny = party.getDenyMap().get(role.getRoleId());
        if (partyDeny == null) {
            return 0L;
        }
        return partyDeny.getEndTime();
    }
    
    public void check() {
        Party party = null;
        final Set<Integer> ids = PartyDataPool.partyMap.keySet();
        for (final Integer id : ids) {
            party = PartyDataPool.partyMap.get(id);
            if (party == null) {
                continue;
            }
            this.checkParty(party);
        }
    }
    
    private void checkParty(final Party party) {
        if (party == null) {
            return;
        }
        this.checkChatDeny(party);
        this.redBagHandler.checkPartyRedBag(party);
    }
    
    private void checkChatDeny(final Party party) {
        final long now = System.currentTimeMillis();
        PartyDeny partyDeny = null;
        final Set<Integer> ids = party.getDenyMap().keySet();
        List<Integer> del = null;
        for (final Integer id : ids) {
            partyDeny = party.getDenyMap().get(id);
            if (partyDeny == null) {
                continue;
            }
            if (now <= partyDeny.getEndTime() * 1000L) {
                continue;
            }
            if (del == null) {
                del = new ArrayList<Integer>();
            }
            del.add(id);
        }
        if (del != null) {
            for (final Integer id : del) {
                party.getDenyMap().remove(id);
                party.save();
            }
        }
    }
    
    private PartyMember getPartyMember(final Party party, final String name) {
        if (party == null || name == null) {
            return null;
        }
        final Collection<PartyMember> memberObjects = party.getMembers().values();
        for (final PartyMember partyMember : memberObjects) {
            if (name.equals(partyMember.getName())) {
                return partyMember;
            }
        }
        return null;
    }
    
    public int getTitle(final PartyMember partyMember) {
        return PartyDataPool.jobId2TitleSerial.get(partyMember.getJob());
    }
    
    private boolean isHaveJoinRequest(final Party party) {
        return party != null && party.getReqJoin() != null && party.getReqJoin().size() > 0;
    }
    
    public void addConstruction(final Party party, final long value) {
        if (value <= 0L || party == null) {
            return;
        }
        party.setCapital(party.getCapital() + value);
        party.save();
    }
    
    public void substractCapital(final Role role, final int value) {
        if (role == null) {
            return;
        }
        if (value <= 0) {
            return;
        }
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        final long before = party.getCapital();
        long current = before - value;
        if (current < 0L) {
            current = 0L;
        }
        party.setCapital(current);
        party.save();
    }
    
    public short getMemberCount(final Party party) {
        if (party != null && party.getMembers() != null) {
            return (short)party.getMembers().size();
        }
        return 0;
    }
    
    public int getPartyMaxMembers(final int level) {
        int maxMembers = 0;
        if (level == 1) {
            maxMembers = 150;
        }
        else if (level == 2) {
            maxMembers = 200;
        }
        else if (level == 3) {
            maxMembers = 250;
        }
        else if (level == 4) {
            maxMembers = 300;
        }
        else if (level == 5) {
            maxMembers = 310;
        }
        else if (level == 6) {
            maxMembers = 320;
        }
        else if (level == 7) {
            maxMembers = 330;
        }
        else if (level == 8) {
            maxMembers = 340;
        }
        return maxMembers;
    }
    
    private PartyMember getMemberByName(final Party party, final String name) {
        if (party == null) {
            return null;
        }
        final Collection<PartyMember> values = party.getMembers().values();
        for (final PartyMember partyMember : values) {
            if (partyMember.getName().equals(name)) {
                return partyMember;
            }
        }
        return null;
    }
    
    public void handleLogout(final Role role) {
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        final PartyMember partyMember = this.getPartyMember(party, role);
        if (partyMember == null) {
            return;
        }
        final long now = System.currentTimeMillis();
        partyMember.setLastOnlineTime((int)(now / 1000L));
    }
    
    public void handleRoleChange(final Role role, final String currName) {
        if (role.getFaction() <= 0) {
            return;
        }
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        final PartyMember partyMember = this.getPartyMember(party, role);
        if (partyMember != null) {
            if (currName != null) {
                partyMember.setName(currName);
            }
            partyMember.setRoleSerial(role.getSerial());
            partyMember.save();
        }
        if (role.getRoleId().equals(party.getMasterId()) && currName != null) {
            party.setMasterName(currName);
            party.save();
        }
    }
    
    public void handleRoleLevelUpEvent(final Role role) {
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        final PartyMember partyMember = this.getPartyMember(party, role);
        if (partyMember != null) {
            partyMember.setRei(role.getRelive().byteValue());
            partyMember.setLevel(role.getLevel().shortValue());
            partyMember.setGrade(role.getRelive() * 1000 + role.getLevel());
            partyMember.save();
        }
    }
    
    public void updatePartyTask(final Role role, final int count, final long calculate) {
        final Party party = this.getParty(role);
        if (party == null) {
            return;
        }
        final PartyMember member = party.getMembers().get(role.getRoleId());
        if (member != null) {
            final int millis = TimeUtil.dayInMillis(TimeUtil.when());
            PartyTask partyTask = member.getPartyTaskList().stream().filter(e -> e.getTime() == millis).findFirst().orElse(null);
            if (partyTask == null) {
                partyTask = new PartyTask();
                member.getPartyTaskList().add(partyTask);
            }
            partyTask.setTime(millis);
            partyTask.setCount(count);
            member.setContribute(member.getContribute() + calculate);
            member.save();
            this.addConstruction(party, BigDecimalUtil.decimalScaleDown(0, 36 + (party.getLevel() - 1) * 18).longValue() * 2L);
        }
    }
    
    public void resetPartyTask() {
        final int millis = TimeUtil.dayInMillis(TimeUtil.when());
        for (final Party party : PartyDataPool.partyMap.values()) {
            for (final PartyMember member : party.getMembers().values()) {
                member.getPartyTaskList().removeIf(e -> e.getTime() < millis);
                member.save();
            }
        }
    }
    
    public void updateActivation() {
        final int week = TimeUtil.getWeek();
        final int millis = TimeUtil.dayInMillis(TimeUtil.when());
        for (final Party party : PartyDataPool.partyMap.values()) {
            final int size = party.getMembers().size();
            int online = 0;
            for (final PartyMember member : party.getMembers().values()) {
                if (this.roleService.isOnline(member.getRoleId())) {
                    ++online;
                }
                else {
                    final Role player = this.roleService.getPlayer(member.getRoleId());
                    if (player == null) {
                        continue;
                    }
                    if (player.getOfLineTime() > 0L) {
                        final int time = TimeUtil.dayInMillis(TimeUtil.stampToDate(player.getOfLineTime()));
                        if (time < millis) {
                            continue;
                        }
                        ++online;
                    }
                    else {
                        ++online;
                    }
                }
            }
            final String divide = BigDecimalUtil.divide(String.valueOf(online), String.valueOf(size));
            party.getOnlineRate().add((int)(Double.valueOf(divide) * 100.0));
            if (party.getOnlineRate().size() >= 3) {
                final int sum = party.getOnlineRate().stream().mapToInt(Integer::valueOf).sum();
                party.setActive(BigDecimalUtil.decimalScaleBig(0, sum / 3).shortValue());
                party.getOnlineRate().clear();
            }
            party.save();
        }
        if (week == 4) {
            this.mergeParty();
        }
    }
    
    public void mergeParty() {
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        final Map<Integer, Party> map = new HashMap<Integer, Party>();
        for (final Party party : PartyDataPool.partyMap.values()) {
            if (party.getActive() < 30) {
                map.put(party.getSerial(), party);
            }
        }
        final int joinTime = (int)(System.currentTimeMillis() / 1000L);
        final List<Party> list = PartyDataPool.partyMap.values().stream().sorted(Comparator.comparing(Party::getActive, Comparator.reverseOrder())).collect(Collectors.toList());
        for (final Party party2 : map.values()) {
            for (final PartyMember value : party2.getMembers().values()) {
                int serial = 0;
                for (int i = 0; i < list.size(); ++i) {
                    final Party p = list.get(i);
                    if (this.isPartyFull(p)) {
                        list.removeIf(e -> e.getSerial().equals(p.getSerial()));
                    }
                    else {
                        serial = p.getSerial();
                        final Map<Integer, PartyMember> members = p.getMembers();
                        value.setSerial(p.getSerial());
                        value.setJob((byte)10);
                        value.setPartyTaskList(new ArrayList<PartyTask>());
                        value.setJoinTime(joinTime);
                        value.save();
                        members.put(value.getRoleId(), value);
                    }
                }
                final Role player = this.roleService.getPlayer(value.getRoleId());
                if (player != null) {
                    if (serial == 0) {
                        value.setDelete(true);
                        value.save();
                    }
                    player.setFaction(serial);
                    player.save();
                }
            }
            party2.setDelete(true);
            party2.save();
            PartyDataPool.partyMap.remove(party2.getSerial());
        }
        stopWatch.stop();
        this.logger.error("合并帮派，耗時{}毫秒", (Object)stopWatch.getTime());
    }
    
    public void clearParty(final Role role) {
        role.setFaction(0);
        this.roleService.refreshParty(role);
        this.delPartyTitle(role);
        role.save();
        this.jdbcTemplate.update("delete from party_member where role_id = " + role.getRoleId());
    }
}
