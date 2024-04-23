package com.kitty.system.game.party.service;

import com.alibaba.fastjson.JSONObject;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdGenerator;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.PageUtil;
import com.kitty.common.utils.StringUtils;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.listener.EventType;
import com.kitty.logs.LoggerFunction;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.mina.task.ThreadLocalUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.game.activity.handler.timing.wars.GangWarsHandler;
import com.kitty.system.game.activity.model.user.wars.GangWarsData;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.game.enter.message.ReqTelePort;
import com.kitty.system.game.enter.message.RespNotifyMisc;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.friend.RoleView;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.party.event.QuitPartyEvent;
import com.kitty.system.game.party.message.ReqCreateParty;
import com.kitty.system.game.party.message.ReqPartyMembers;
import com.kitty.system.game.party.message.ReqPartyRejectLevel;
import com.kitty.system.game.party.message.ReqPartyUnlockFlag;
import com.kitty.system.game.party.message.ReqQueryPartys;
import com.kitty.system.game.party.message.ReqSavePartyFlag;
import com.kitty.system.game.party.message.RespJoinPartyList;
import com.kitty.system.game.party.message.RespOpenParty;
import com.kitty.system.game.party.message.RespPartyFlag;
import com.kitty.system.game.party.message.RespPartyInfo;
import com.kitty.system.game.party.message.RespPartyLog;
import com.kitty.system.game.party.message.RespPartyMembers;
import com.kitty.system.game.party.message.RespPartyNotification;
import com.kitty.system.game.party.message.RespPartyRole;
import com.kitty.system.game.party.message.vo.LogInfo;
import com.kitty.system.game.party.message.vo.PartyFlag;
import com.kitty.system.game.party.message.vo.PartyLogInfo;
import com.kitty.system.game.party.model.JoinMemberInfo;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.model.PartyDeny;
import com.kitty.system.game.party.model.PartyInfo;
import com.kitty.system.game.party.model.PartyLog;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.game.party.model.PartyWarsRecord;
import com.kitty.system.game.party.model.ReqJoinMember;
import com.kitty.system.game.party.model.vo.PartyMemberObject;
import com.kitty.system.game.party.model.vo.PartyTask;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.game.team.message.RespClearnRequest;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.TitleInfo;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysPartyFlag;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import org.apache.commons.lang3.time.StopWatch;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class PartyService {
  private Logger logger = LoggerFunction.PARTY.getLogger();
  
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
  
  public void loadParty() {
    Map<Integer, Party> partyMap = new ConcurrentHashMap<>();
    Map<String, Integer> name2Ids = new ConcurrentHashMap<>();
    Dao dao = (Dao)SpringUtils.getBean(Dao.class);
    List<Party> list = dao.query(Party.class, (Condition)Cnd.NEW());
    for (Party party : list) {
      party.doAfterInit();
      List<PartyMember> members = dao.query(PartyMember.class, (Condition)Cnd.where("serial", "=", party.getSerial()).groupBy(new String[] { "role_id" }).desc("join_time"));
      for (PartyMember member : members)
        party.getMembers().put(Integer.valueOf(member.getRoleId()), member); 
      List<PartyWarsRecord> warsRecordList = dao.query(PartyWarsRecord.class, (Condition)Cnd.where("serial", "=", party.getSerial()));
      party.getWarsRecordList().addAll(warsRecordList);
      List<PartyLog> partyLogs = dao.query(PartyLog.class, (Condition)Cnd.where("serial", "=", party.getSerial()));
      party.setLogs(partyLogs);
      partyMap.put(party.getSerial(), party);
      name2Ids.put(party.getName(), party.getSerial());
    } 
    PartyDataPool.partyMap.putAll(partyMap);
    PartyDataPool.partyName2Id.putAll(name2Ids);
  }
  
  public Party getParty(Role role) {
    if (role == null)
      return null; 
    return getParty(role.getFaction());
  }
  
  public Party getParty(Integer serial) {
    return (Party)PartyDataPool.partyMap.get(serial);
  }
  
  public Party getPartyByName(String partyName) {
    if (StringUtils.isEmpty(partyName))
      return null; 
    Integer serial = (Integer)PartyDataPool.partyName2Id.get(partyName);
    if (serial != null)
      return getParty(serial); 
    return null;
  }
  
  public void confirmCreateParty(Role role, ReqCreateParty createParty) {
    if (role == null)
      return; 
    if (checkNotice(role, createParty.getAnnounce())) {
      if (!checkCreateParty(role, createParty.getPartyName()))
        return; 
      this.roleService.deductCurrency(role, Const.RESTYPE_GOLD, 20L, "创建帮派");
      Party party = newParty(role, createParty);
      if (party != null)
        MessagePusher.pushMessage(role, (Message)new RespHint(4, (short)1)); 
      String content = I18nIdDataPool.getI18nContent(361, new Object[] { Integer.valueOf(20), party.getName() });
      MessagePusher.pushMessage(role, (Message)new RespNotifyMisc(content));
    } 
  }
  
  private boolean checkCreateParty(Role role, String partyName) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
      Message message = DataGlobalFunc.operationalConstraint(role, Deploy.party, "充值#<R>%d#才可以创建帮派！");
      if (message != null) {
        MessagePusher.pushMessage(role, message);
        return false;
      } 
      if (PartyDataPool.partyMap.size() >= Deploy.party_limit) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("创建帮派已上限"));
        return false;
      } 
    } 
    boolean check = checkPartyName(role, partyName);
    if (!check)
      return false; 
    if (isHaveParty(role)) {
      MessagePusher.notify2Player(role, 302, new Object[0]);
      return false;
    } 
    if (role.getLevel().intValue() < 30) {
      MessagePusher.notify2Player(role, 303, new Object[0]);
      return false;
    } 
    if (role.getGold().longValue() < 20L) {
      MessagePusher.notify2Player(role, 403, new Object[0]);
      return false;
    } 
    return true;
  }
  
  public void partyFlag(Party party, Role role) {
    RespPartyFlag partyFlag = new RespPartyFlag();
    partyFlag.setText(party.getEmblemText());
    party.getFlag().forEach((key, value) -> partyFlag.getFlagList().add(new PartyFlag(key.shortValue(), value.byteValue())));
    party.getFlagColor().forEach((key, value) -> partyFlag.getFlagColorList().add(new PartyFlag(key.shortValue(), value.byteValue())));
    party.getLace().forEach((key, value) -> partyFlag.getLaceList().add(new PartyFlag(key.shortValue(), value.byteValue())));
    party.getEmblem().forEach((key, value) -> partyFlag.getEmblemList().add(new PartyFlag(key.shortValue(), value.byteValue())));
    party.getEmblemColor().forEach((key, value) -> partyFlag.getEmblemColorList().add(new PartyFlag(key.shortValue(), value.byteValue())));
    MessagePusher.pushMessage(role.getId().longValue(), (Message)partyFlag);
  }
  
  public void unlockPartyFlag(Party party, Role role, ReqPartyUnlockFlag reqPartyUnlockFlag) {
    Byte flag, lace, emblem, flagColor, emblemColor;
    if (1 != getPermission(role)) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("帮主才可以操作战旗哦！"));
      return;
    } 
    SysPartyFlag sysPartyFlag = PartyDataPool.partyFlagList.stream().filter(e -> (e.getIndex() == reqPartyUnlockFlag.getIndex() && e.getClassify() == reqPartyUnlockFlag.getType())).findFirst().orElse(null);
    if (sysPartyFlag == null)
      return; 
    switch (reqPartyUnlockFlag.getType()) {
      case 0:
        flag = (Byte)party.getFlag().get(Short.valueOf(reqPartyUnlockFlag.getIndex()));
        if (flag != null) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经解锁该旗帜！"));
          return;
        } 
        if (this.roleService.deductCurrency(role, Integer.valueOf(sysPartyFlag.getType()), sysPartyFlag.getPrice(), "解锁帮派旗帜")) {
          party.getFlag().put(Short.valueOf(sysPartyFlag.getIndex()), Byte.valueOf((byte)0));
          party.save();
          partyFlag(party, role);
        } 
        break;
      case 1:
        lace = (Byte)party.getLace().get(Short.valueOf(reqPartyUnlockFlag.getIndex()));
        if (lace != null) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经解锁该边饰！"));
          return;
        } 
        if (this.roleService.deductCurrency(role, Integer.valueOf(sysPartyFlag.getType()), sysPartyFlag.getPrice(), "解锁帮派边饰")) {
          party.getLace().put(Short.valueOf(sysPartyFlag.getIndex()), Byte.valueOf((byte)0));
          party.save();
          partyFlag(party, role);
        } 
        break;
      case 2:
        emblem = (Byte)party.getEmblem().get(Short.valueOf(reqPartyUnlockFlag.getIndex()));
        if (emblem != null) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经解锁该纹章！"));
          return;
        } 
        if (this.roleService.deductCurrency(role, Integer.valueOf(sysPartyFlag.getType()), sysPartyFlag.getPrice(), "解锁帮派纹章")) {
          party.getEmblem().put(Short.valueOf(sysPartyFlag.getIndex()), Byte.valueOf((byte)0));
          party.save();
          partyFlag(party, role);
        } 
        break;
      case 3:
        flagColor = (Byte)party.getFlagColor().get(Short.valueOf(reqPartyUnlockFlag.getIndex()));
        if (flagColor != null) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经解锁该旗帜颜色！"));
          return;
        } 
        if (this.roleService.deductCurrency(role, Integer.valueOf(sysPartyFlag.getType()), sysPartyFlag.getPrice(), "解锁帮派旗帜颜色")) {
          party.getFlagColor().put(Short.valueOf(sysPartyFlag.getIndex()), Byte.valueOf((byte)0));
          party.save();
          partyFlag(party, role);
        } 
        break;
      case 4:
        emblemColor = (Byte)party.getEmblemColor().get(Short.valueOf(reqPartyUnlockFlag.getIndex()));
        if (emblemColor != null) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你已经解锁该纹章颜色！"));
          return;
        } 
        if (this.roleService.deductCurrency(role, Integer.valueOf(sysPartyFlag.getType()), sysPartyFlag.getPrice(), "解锁帮派纹章颜色")) {
          party.getEmblemColor().put(Short.valueOf(sysPartyFlag.getIndex()), Byte.valueOf((byte)0));
          party.save();
          partyFlag(party, role);
        } 
        break;
    } 
  }
  
  public void savePartyFlag(Party party, Role role, ReqSavePartyFlag reqPartyFlag) {
    if (1 != getPermission(role)) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("帮主才可以操作战旗哦！"));
      return;
    } 
    if (party.getFlag().get(Short.valueOf(reqPartyFlag.getFlag())) == null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("本帮尚未解锁该旗帜"));
      return;
    } 
    if (party.getFlagColor().get(Short.valueOf(reqPartyFlag.getFlagColor())) == null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("本帮尚未解锁该旗帜颜色"));
      return;
    } 
    if (party.getLace().get(Short.valueOf(reqPartyFlag.getLace())) == null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("本帮尚未解锁该边饰"));
      return;
    } 
    if (party.getEmblem().get(Short.valueOf(reqPartyFlag.getEmblem())) == null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("本帮尚未解锁该纹章"));
      return;
    } 
    if (party.getEmblemColor().get(Short.valueOf(reqPartyFlag.getEmblemColor())) == null) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("本帮尚未解锁该纹章"));
      return;
    } 
    if (reqPartyFlag.getText() == null || !StringUtils.isChineseCharacter(reqPartyFlag.getText())) {
      MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("必须是一个汉字"));
      return;
    } 
    party.setEmblemText(reqPartyFlag.getText());
    party.getFlag().forEach((key, value) -> {
          if (key.equals(Short.valueOf(reqPartyFlag.getFlag()))) {
            party.getFlag().put(key, Byte.valueOf((byte)1));
          } else {
            party.getFlag().put(key, Byte.valueOf((byte)0));
          } 
        });
    party.getFlagColor().forEach((key, value) -> {
          if (key.equals(Short.valueOf(reqPartyFlag.getFlagColor()))) {
            party.getFlagColor().put(key, Byte.valueOf((byte)1));
          } else {
            party.getFlagColor().put(key, Byte.valueOf((byte)0));
          } 
        });
    party.getLace().forEach((key, value) -> {
          if (key.equals(Short.valueOf(reqPartyFlag.getLace()))) {
            party.getLace().put(key, Byte.valueOf((byte)1));
          } else {
            party.getLace().put(key, Byte.valueOf((byte)0));
          } 
        });
    party.getEmblem().forEach((key, value) -> {
          if (key.equals(Short.valueOf(reqPartyFlag.getEmblem()))) {
            party.getEmblem().put(key, Byte.valueOf((byte)1));
          } else {
            party.getEmblem().put(key, Byte.valueOf((byte)0));
          } 
        });
    party.getEmblemColor().forEach((key, value) -> {
          if (key.equals(Short.valueOf(reqPartyFlag.getEmblemColor()))) {
            party.getEmblemColor().put(key, Byte.valueOf((byte)1));
          } else {
            party.getEmblemColor().put(key, Byte.valueOf((byte)0));
          } 
        });
    party.save();
    partyFlag(party, role);
    MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("保存成功"));
  }
  
  public void getRespOpenPatry(Party party, Role role) {
    RespOpenParty respOpenParty = new RespOpenParty();
    try {
      respOpenParty.setSerial(party.getSerial().intValue());
      respOpenParty.setCreateTime(Math.toIntExact(party.getCreateTime() / 1000L));
      respOpenParty.setLevel(party.getLevel());
      respOpenParty.setCount(getPartyTotalCount(party));
      respOpenParty.setOnlineMember(getPartyOnlineCount(party));
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
      PartyMember partyMember = (PartyMember)party.getMembers().get(role.getRoleId());
      if (partyMember != null) {
        respOpenParty.setJoinTime(partyMember.getJoinTime());
        respOpenParty.setJob(partyMember.getJob());
      } 
      respOpenParty.setStandings(party.getStandings());
      respOpenParty.setSum((short)party.getWarsRecordList().size());
      respOpenParty.setWin((short)party.getRating());
      respOpenParty.setLose((short)(respOpenParty.getSum() - respOpenParty.getWin()));
      PartyMember member = (PartyMember)party.getMembers().get(party.getMasterId());
      if (member != null) {
        Role player = this.roleService.getPlayer(member.getRoleId());
        respOpenParty.getMaster().add(new FiedValue("n", (byte)4, party.getMasterName()));
        respOpenParty.getMaster().add(new FiedValue("s", (byte)2, Integer.valueOf(member.getRoleSerial())));
        respOpenParty.getMaster().addAll(this.roleService.deyList(player.getColor()));
        respOpenParty.getMaster().add(new FiedValue("l", (byte)4, JsonUtils.object2String(this.equipService.getWearEffect(player))));
        respOpenParty.getMaster().add(new FiedValue("f", (byte)4, JsonUtils.object2String(new ArrayList())));
        respOpenParty.getMaster().add(new FiedValue("w", (byte)3, Integer.valueOf(player.getWeaponIcon())));
        int gem = 0;
        List<WearEquip> wearEquips = this.equipService.getEquipValid(player, false);
        if (wearEquips.size() > 0)
          gem = DataGlobalFunc.getWeaponGemNum(wearEquips); 
        respOpenParty.getMaster().add(new FiedValue("gem", (byte)2, Integer.valueOf(gem)));
      } else {
        this.logger.error("查询帮主信息错误，帮派编号：{}", party.getSerial());
      } 
      MessagePusher.pushMessage(role, (Message)respOpenParty);
    } catch (Exception e) {
      this.logger.error("发送内政异常");
      e.printStackTrace();
    } 
  }
  
  public short getPartyOnlineCount(Party party) {
    if (party == null)
      return 0; 
    if (party.getMembers() != null) {
      short count = 0;
      for (PartyMember member : party.getMembers().values()) {
        if (this.roleService.isOnline(member.getRoleId()))
          count = (short)(count + 1); 
      } 
      return count;
    } 
    return 0;
  }
  
  private short getPartyTotalCount(Party party) {
    if (party == null)
      return 0; 
    return (short)party.getMembers().size();
  }
  
  private Party newParty(Role role, ReqCreateParty createParty) {
    Party party = new Party();
    party.setId(Long.valueOf(IdGenerator.getNextId()));
    party.setSerial(Integer.valueOf(SpringUtils.getIdentityService().getNextId(IdentityType.PARTY)));
    party.setName(createParty.getPartyName().trim());
    party.setMasterId(role.getRoleId());
    party.setMasterName(role.getName());
    party.setCreateTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
    party.setAim(createParty.getAnnounce());
    createParty.setConsent(createParty.getConsent());
    party.getFlag().put(Short.valueOf((short)1), Byte.valueOf((byte)1));
    party.getFlagColor().put(Short.valueOf((short)1), Byte.valueOf((byte)1));
    party.getLace().put(Short.valueOf((short)1), Byte.valueOf((byte)1));
    party.getEmblem().put(Short.valueOf((short)1), Byte.valueOf((byte)1));
    party.getEmblemColor().put(Short.valueOf((short)1), Byte.valueOf((byte)1));
    addPartyMember(party, role, (byte)70);
    PartyDataPool.partyMap.put(party.getSerial(), party);
    PartyDataPool.partyName2Id.put(party.getName(), party.getSerial());
    party.save();
    role.setFaction(party.getSerial());
    role.save();
    addPartyTitle(party, role);
    this.roleService.refreshParty(role);
    return party;
  }
  
  public PartyMember addPartyMember(Party party, Role role, byte job) {
    long now = System.currentTimeMillis();
    PartyMember partyMember = new PartyMember(role, party.getSerial().intValue(), now, job, Long.valueOf(IdGenerator.getNextId()));
    partyMember.save();
    party.getMembers().put(Integer.valueOf(partyMember.getRoleId()), partyMember);
    return partyMember;
  }
  
  private PartyMember addPartyMember(Party party, RoleView roleView, byte job) {
    long now = System.currentTimeMillis();
    PartyMember partyMember = new PartyMember(roleView.getRole(), party.getSerial().intValue(), now, job, Long.valueOf(IdGenerator.getNextId()));
    partyMember.save();
    party.getMembers().put(Integer.valueOf(partyMember.getRoleId()), partyMember);
    return partyMember;
  }
  
  private boolean isHaveParty(Role role) {
    if (role == null)
      return true; 
    return (getParty(role.getFaction()) != null);
  }
  
  private boolean isPartyNameExist(String partyName) {
    return (getPartyByName(partyName) != null);
  }
  
  public void queryParties(Role role, ReqQueryPartys queryParty) {
    List<Party> parties = getSortedParties();
    if ("fuzzy_by".equals(queryParty.getType()))
      parties = (List<Party>)parties.stream().filter(party -> (party.getSerial().equals(queryParty.getPara()) || party.getName().contains(queryParty.getPara()))).collect(Collectors.toList()); 
    sendParties(role, queryParty, parties);
  }
  
  public void sendParties(Role role, ReqQueryPartys queryParty, List<Party> parties) {
    RespPartyInfo respPartyInfo = new RespPartyInfo();
    respPartyInfo.setOrder(queryParty.getType());
    respPartyInfo.setPage(queryParty.getPage());
    respPartyInfo.setSort(queryParty.getSort());
    respPartyInfo.setWay(queryParty.getWay());
    List<PartyInfo> list = new ArrayList<>();
    if (parties.size() > 0) {
      parties = PageUtil.subList(parties, Integer.valueOf(queryParty.getPage()), Integer.valueOf(queryParty.getSize()));
      for (Party party : parties) {
        short partyTotalCount = getPartyTotalCount(party);
        PartyInfo partyInfo = new PartyInfo();
        partyInfo.setSerial(party.getSerial().intValue());
        partyInfo.setName(party.getName());
        partyInfo.setLevel(party.getLevel());
        partyInfo.setMembers(partyTotalCount);
        partyInfo.setActive(party.getActive());
        partyInfo.setAim(party.getAim());
        partyInfo.setStandings(party.getStandings());
        partyInfo.setSum((short)party.getWarsRecordList().size());
        partyInfo.setWin((short)party.getRating());
        partyInfo.setLose((short)(partyInfo.getSum() - partyInfo.getWin()));
        PartyMember member = (PartyMember)party.getMembers().get(party.getMasterId());
        if (member != null) {
          partyInfo.getMaster().add(new FiedValue("n", (byte)4, party.getMasterName()));
          partyInfo.getMaster().add(new FiedValue("s", (byte)2, Integer.valueOf(member.getRoleSerial())));
          partyInfo.getMaster().add(new FiedValue("g", (byte)2, Integer.valueOf(member.getRei() * 1000 + member.getLevel())));
          list.add(partyInfo);
        } else {
          this.logger.error("查询帮主信息错误，帮派编号：{}", party.getSerial());
        } 
        Short flag = party.getFlag().entrySet().stream().filter(entry -> ((Byte)entry.getValue()).equals(Byte.valueOf((byte)1))).map(Map.Entry::getKey).findFirst().orElse(null);
        if (flag == null)
          flag = Short.valueOf((short)1); 
        partyInfo.getFlag().add(new FiedValue("qz", (byte)2, flag));
        Short lace = party.getLace().entrySet().stream().filter(entry -> ((Byte)entry.getValue()).equals(Byte.valueOf((byte)1))).map(Map.Entry::getKey).findFirst().orElse(null);
        if (lace == null)
          lace = Short.valueOf((short)1); 
        partyInfo.getFlag().add(new FiedValue("bs", (byte)2, lace));
        Short emblem = party.getEmblem().entrySet().stream().filter(entry -> ((Byte)entry.getValue()).equals(Byte.valueOf((byte)1))).map(Map.Entry::getKey).findFirst().orElse(null);
        if (emblem == null)
          emblem = Short.valueOf((short)1); 
        partyInfo.getFlag().add(new FiedValue("wz", (byte)2, emblem));
        Short flagC = party.getFlagColor().entrySet().stream().filter(entry -> ((Byte)entry.getValue()).equals(Byte.valueOf((byte)1))).map(Map.Entry::getKey).findFirst().orElse(null);
        if (flagC == null)
          flagC = Short.valueOf((short)1); 
        partyInfo.getFlag().add(new FiedValue("qzc", (byte)2, flagC));
        Short emblemC = party.getEmblemColor().entrySet().stream().filter(entry -> ((Byte)entry.getValue()).equals(Byte.valueOf((byte)1))).map(Map.Entry::getKey).findFirst().orElse(null);
        if (emblemC == null)
          emblemC = Short.valueOf((short)1); 
        partyInfo.getFlag().add(new FiedValue("wzc", (byte)2, emblemC));
        String text = party.getEmblemText();
        if (text == null || text.isEmpty())
          text = "帮"; 
        partyInfo.getFlag().add(new FiedValue("text", (byte)4, text));
      } 
      if (queryParty.getSort() == 1) {
        if (queryParty.getWay() == 0) {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getSerial)).collect(Collectors.toList());
        } else {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getSerial).reversed()).collect(Collectors.toList());
        } 
      } else if (queryParty.getSort() == 2) {
        if (queryParty.getWay() == 0) {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getName)).collect(Collectors.toList());
        } else {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getName).reversed()).collect(Collectors.toList());
        } 
      } else if (queryParty.getSort() == 3) {
        if (queryParty.getWay() == 0) {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getLevel)).collect(Collectors.toList());
        } else {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getLevel).reversed()).collect(Collectors.toList());
        } 
      } else if (queryParty.getSort() == 4) {
        if (queryParty.getWay() == 0) {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getMembers)).collect(Collectors.toList());
        } else {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getMembers).reversed()).collect(Collectors.toList());
        } 
      } else if (queryParty.getSort() == 5) {
        if (queryParty.getWay() == 0) {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getActive)).collect(Collectors.toList());
        } else {
          list = (List<PartyInfo>)list.stream().sorted(Comparator.comparing(PartyInfo::getActive).reversed()).collect(Collectors.toList());
        } 
      } 
      respPartyInfo.setList(list);
    } 
    MessagePusher.pushMessage(role, (Message)respPartyInfo);
  }
  
  public List<Party> getSortedParties() {
    ArrayList<Party> partyList = new ArrayList<>(PartyDataPool.partyMap.values());
    partyList.sort(Comparator.<Party, Comparable>comparing(Party::getLevel).reversed());
    return partyList;
  }
  
  public void reqJoinParty(Role role, String partyName) {
    if (role == null || StringUtils.isEmpty(partyName))
      return; 
    Party party = getPartyByName(partyName);
    if (party == null)
      return; 
    if (isHaveParty(role)) {
      MessagePusher.notify2Player(role, 304, new Object[0]);
      return;
    } 
    if (isPartyFull(party)) {
      MessagePusher.notify2Player(role, 305, new Object[0]);
      return;
    } 
    int curve = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    if (party.getConsent() == 0 && curve >= party.getConsentLevel()) {
      addPartyMember(party, new RoleView(role), (byte)10);
      doJoinParty(party, role);
      MessagePusher.pushMessage(role, (Message)new RespNotifyMisc(I18nIdDataPool.getI18nContent(316, new Object[] { party.getName() })));
      npcPartyChat(party, I18nIdDataPool.getI18nContent(315, new Object[] { role.getName() }));
      addPartyLog(party, 349, new String[] { role.getRoleId().toString(), role.getName() });
    } else {
      ReqJoinMember reqJoinMember = new ReqJoinMember(role.getRoleId().intValue(), role.getName(), Math.toIntExact(System.currentTimeMillis() / 1000L), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().shortValue(), new ArrayList());
      party.getReqJoin().put(role.getName(), reqJoinMember);
      notifyOnlineManager(party, (Message)new RespPartyNotification((short)2));
    } 
  }
  
  private boolean isPartyFull(Party party) {
    short partyTotalCount = getPartyTotalCount(party);
    int partyMaxCount = getPartyMaxMembers(party.getLevel());
    return (partyTotalCount >= partyMaxCount);
  }
  
  public void oneKeyJoin(Role role) {
    if (role == null)
      return; 
    if (isHaveParty(role))
      return; 
    boolean joined = false;
    List<Party> sortedParties = getSortedParties();
    for (Party party : sortedParties) {
      if (isPartyFull(party))
        continue; 
      joined = autoJoin(role, party);
      if (joined)
        break; 
    } 
    if (!joined)
      for (Party party : sortedParties) {
        if (isPartyFull(party))
          continue; 
        ReqJoinMember reqJoinMember = new ReqJoinMember(role.getRoleId().intValue(), role.getName(), Math.toIntExact(System.currentTimeMillis() / 1000L), role.getRelive().byteValue(), role.getLevel().shortValue(), role.getSerial().shortValue(), new ArrayList());
        party.getReqJoin().put(role.getName(), reqJoinMember);
        notifyOnlineManager(party, (Message)new RespPartyNotification((short)2));
      }  
  }
  
  private void notifyOnlineManager(Party party, Message message) {
    if (party == null || message == null)
      return; 
    try {
      for (Integer key : party.getMembers().keySet()) {
        PartyMember member = (PartyMember)party.getMembers().get(key);
        if (member.getJob() > 10) {
          Role role = SpringUtils.getRoleService().getPlayerBy(key.intValue());
          if (role == null)
            continue; 
          MessagePusher.pushMessage(role, message);
        } 
      } 
    } catch (ConcurrentModificationException concurrentModificationException) {}
  }
  
  private boolean autoJoin(Role role, Party party) {
    if (role == null || party == null)
      return false; 
    int curve = role.getRelive().intValue() * 1000 + role.getLevel().intValue();
    if (party.getConsent() == 0 && curve >= party.getConsentLevel()) {
      addPartyMember(party, new RoleView(role), (byte)10);
      doJoinParty(party, role);
      String acceptContent = I18nIdDataPool.getI18nContent(316, new Object[] { party.getName() });
      MessagePusher.pushMessage(role, (Message)new RespNotifyMisc(acceptContent));
      String content = I18nIdDataPool.getI18nContent(315, new Object[] { role.getName() });
      npcPartyChat(party, content);
      String mailTitle = I18nIdDataPool.getI18nContent(317);
      String mailContent = I18nIdDataPool.getI18nContent(318, new Object[] { party.getName(), party.getName() });
      addPartyLog(party, 349, new String[] { role.getRoleId().toString(), role.getName() });
      return true;
    } 
    return false;
  }
  
  public void checkJoinList(Role role) {
    if (role == null)
      return; 
    Party party = getParty(role);
    if (party == null)
      return; 
    RespJoinPartyList joinPartyList = new RespJoinPartyList();
    ArrayList<JoinMemberInfo> list = new ArrayList<>();
    NutConcurrentMap reqJoin = party.getReqJoin();
    for (Map.Entry<String, Object> entry : (Iterable<Map.Entry<String, Object>>)reqJoin.entrySet()) {
      JoinMemberInfo join = new JoinMemberInfo();
      try {
        String jsonString = JSONObject.toJSONString(entry.getValue());
        ReqJoinMember member = (ReqJoinMember)JSONObject.parseObject(jsonString, ReqJoinMember.class);
        join.setName(entry.getKey());
        join.setId(member.getId());
        join.setRei(member.getRei());
        join.setLevel(member.getLevel());
        join.setSerial(member.getSerial());
        list.add(join);
      } catch (Exception e) {
        this.logger.error("查看帮派申请列表异常：{}，玩家编号：{}", e.getMessage(), role.getId());
      } 
    } 
    joinPartyList.setList(list);
    MessagePusher.pushMessage(role, (Message)joinPartyList);
  }
  
  public void agreeJoinParty(Role role, String name) {
    try {
      if (role == null || StringUtils.isEmpty(name))
        return; 
      Party party = getParty(role.getFaction());
      if (party == null)
        return; 
      if (!isAgreeOrDenyApply(role))
        return; 
      ArrayList<String> names = new ArrayList<>();
      if ("all".equals(name)) {
        Set<String> keySet = party.getReqJoin().keySet();
        if (keySet != null)
          names.addAll(keySet); 
      } else {
        names.add(name);
      } 
      RespClearnRequest respClearnRequest = new RespClearnRequest();
      respClearnRequest.setType("party");
      respClearnRequest.setNames(names);
      MessagePusher.pushMessage(role, (Message)respClearnRequest);
      for (String tempName : names) {
        ReqJoinMember reqJoinMember = (ReqJoinMember)party.getReqJoin().getAs(tempName, ReqJoinMember.class, null);
        party.getReqJoin().remove(tempName);
        if (reqJoinMember == null)
          continue; 
        Role player = this.roleService.getPlayer(reqJoinMember.getId());
        if (player == null)
          continue; 
        RoleView reqRoleView = new RoleView(player);
        if (isHaveParty(reqRoleView.getRole())) {
          MessagePusher.notify2Player(role, 306, new Object[0]);
          continue;
        } 
        if (!reqRoleView.isOnline() && ((Integer)PartyDataPool.offlineRoleParty.getOrDefault(Integer.valueOf(reqJoinMember.getId()), Integer.valueOf(0))).intValue() > 0) {
          MessagePusher.notify2Player(role, 306, new Object[0]);
          continue;
        } 
        addPartyMember(party, reqRoleView, (byte)10);
        Role reqRole = reqRoleView.getRole();
        if (reqRole != null) {
          ThreadLocalUtil.addLocalTask(reqRole, () -> joinParty(party, reqRole));
        } else {
          PartyDataPool.offlineRoleParty.put(Integer.valueOf(reqJoinMember.getId()), party.getSerial());
        } 
        MessagePusher.pushMessage(role, (Message)new RespMsg("收人成功"));
        String content = I18nIdDataPool.getI18nContent(315, new Object[] { reqRoleView.getName() });
        npcPartyChat(party, content);
        checkPartyMember(role, new ReqPartyMembers());
        addPartyLog(party, 349, new String[] { role.getRoleId().toString(), role.getName() });
      } 
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private boolean isAgreeOrDenyApply(Role role) {
    byte permission = getPermission(role);
    return (permission == 1 || permission == 2 || permission == 4 || permission == 3 || permission == 5 || permission == 6);
  }
  
  private void joinParty(Party party, Role role) {
    doJoinParty(party, role);
    String acceptContent = I18nIdDataPool.getI18nContent(316, new Object[] { party.getName() });
    MessagePusher.pushMessage(role, (Message)new RespNotifyMisc(acceptContent));
    String mailTitle = I18nIdDataPool.getI18nContent(317);
    String mailContent = I18nIdDataPool.getI18nContent(318, new Object[] { party.getName(), party.getName() });
  }
  
  private void doJoinParty(Party party, Role role) {
    if (party == null || role == null)
      return; 
    role.setFaction(party.getSerial());
    addPartyTitle(party, role);
    role.save();
    this.roleService.refreshParty(role);
    MessagePusher.pushMessage(role, (Message)new RespHint(5, (short)1));
  }
  
  public void addPartyTitle(Party party, Role role) {
    PartyMember partyMember = getPartyMember(party, role);
    if (partyMember == null)
      return; 
    String partyJobName = getPartyJobName(partyMember);
    if (partyJobName != null)
      this.titleService.addTitle(role, new TitleInfo(getTitle(partyMember), 0)); 
  }
  
  public void delPartyTitle(Role role) {
    for (Integer value : PartyDataPool.jobId2TitleSerial.values())
      this.titleService.delTitle(role, value.intValue()); 
  }
  
  public PartyMember getPartyMember(Party party, Role role) {
    if (party == null || role == null)
      return null; 
    return (PartyMember)party.getMembers().get(role.getRoleId());
  }
  
  public void getPartyRoleInfo(Role role, int roleId) {
    Party party = getParty(role);
    if (party == null)
      return; 
    if (role.getRoleId().equals(Integer.valueOf(roleId)))
      return; 
    Role player = this.roleService.getPlayer(roleId);
    if (player != null) {
      Party query = getParty(player.getFaction());
      if (query == null || !query.getSerial().equals(party.getSerial()))
        return; 
      if (party != null) {
        PartyMember member = (PartyMember)query.getMembers().get(Integer.valueOf(roleId));
        RespPartyRole partyRole = new RespPartyRole();
        PartyDeny deny = (PartyDeny)query.getDenyMap().get(Integer.valueOf(member.getRoleId()));
        if (deny != null) {
          partyRole.setState((byte)1);
        } else {
          partyRole.setState((byte)0);
        } 
        partyRole.setList(this.roleService.deyList(player.getColor()));
        List<WearEquip> wearEquips = this.equipService.getEquipValid(player, false);
        if (wearEquips.size() > 0) {
          partyRole.setWeaponIcon(player.getWeaponIcon());
          int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
          partyRole.setGem(num);
        } 
        partyRole.setLight_effect(this.equipService.getWearEffect(player));
        partyRole.setRoleId(player.getRoleId().intValue());
        MessagePusher.pushMessage(role, (Message)partyRole);
      } 
    } 
  }
  
  public void checkPartyMember(Role role, ReqPartyMembers reqPartyMembers) {
    Party party = getParty(role);
    if (party == null || party.getMembers().get(role.getRoleId()) == null) {
      clearParty(role);
      return;
    } 
    RespPartyMembers respPartyMembers = getRespPartyMembers(party, role, reqPartyMembers);
    MessagePusher.pushMessage(role, (Message)respPartyMembers);
  }
  
  public RespPartyMembers getRespPartyMembers(Party party, Role role, ReqPartyMembers reqMembers) {
    RespPartyMembers respPartyMembers = new RespPartyMembers();
    try {
      List<PartyMember> members = getMembers(role, party, reqMembers);
      if (reqMembers.getPage() == 1 && reqMembers.getSort() == 4 && reqMembers.getWay() == 0 && reqMembers.getList().isEmpty() && reqMembers.getPara().isEmpty()) {
        List<PartyMember> memberList = new ArrayList<>();
        PartyMember member = (PartyMember)party.getMembers().get(role.getRoleId());
        byte onLineStatus = (byte)(this.roleService.isOnline(member.getRoleId()) ? 1 : 0);
        member.setOnLineStatus(onLineStatus);
        if (onLineStatus == 0) {
          Role r = this.roleService.getPlayer(role.getRoleId().intValue());
          if (r != null)
            member.setLastOnlineTime(Math.toIntExact(r.getOfLineTime().longValue() / 1000L)); 
        } 
        memberList.add(member);
        memberList.addAll(members);
        members = memberList;
      } 
      List<PartyMemberObject> list = new ArrayList<>();
      for (PartyMember member : members) {
        PartyMemberObject object = new PartyMemberObject();
        BeanUtils.copyBeanProp(object, member);
        object.setRei(member.getRei());
        object.setLevel(member.getLevel());
        object.setContribute(String.valueOf(member.getContribute()));
        if (this.roleService.isOnline(member.getRoleId()))
          object.setOnLineStatus((byte)1); 
        int taskCount = 0;
        if (reqMembers.getSort() == 13) {
          taskCount = ((Integer)member.getPartyTaskList().stream().collect(Collectors.summingInt(PartyTask::getCount))).intValue();
        } else if (reqMembers.getSort() == 12) {
          int millis = TimeUtil.dayInMillis(TimeUtil.yesterday());
          PartyTask partyTask = member.getPartyTaskList().stream().filter(task -> (task.getTime() == millis)).findFirst().orElse(null);
          if (partyTask != null)
            taskCount = partyTask.getCount(); 
        } else {
          int millis = TimeUtil.dayInMillis(TimeUtil.when());
          PartyTask partyTask = member.getPartyTaskList().stream().filter(task -> (task.getTime() == millis)).findFirst().orElse(null);
          if (partyTask != null)
            taskCount = partyTask.getCount(); 
        } 
        object.setPartyTask(taskCount);
        list.add(object);
      } 
      if (reqMembers.getSort() == 11 || reqMembers.getSort() == 12 || reqMembers.getSort() == 13)
        if (reqMembers.getWay() == 0) {
          list = (List<PartyMemberObject>)list.stream().sorted(Comparator.comparing(PartyMemberObject::getPartyTask)).collect(Collectors.toList());
        } else {
          list = (List<PartyMemberObject>)list.stream().sorted(Comparator.comparing(PartyMemberObject::getPartyTask).reversed()).collect(Collectors.toList());
        }  
      respPartyMembers.setList(list);
      respPartyMembers.setPage(reqMembers.getPage());
      respPartyMembers.setTail(getMemberCount(party));
    } catch (Exception e) {
      e.printStackTrace();
      this.logger.error("获取该帮派成员信息错误");
    } 
    return respPartyMembers;
  }
  
  public List<PartyMember> getMembers(Role role, Party party, ReqPartyMembers reqMembers) {
    List<PartyMember> objects = new ArrayList<>();
    List<PartyMember> members = (List<PartyMember>)party.getMembers().values().stream().collect(Collectors.toList());
    if (reqMembers.getList().isEmpty() && reqMembers.getPara().isEmpty()) {
      if (reqMembers.getPage() == 1 && reqMembers.getSort() == 4 && reqMembers.getWay() == 0)
        for (int i = 0; i < members.size(); i++) {
          PartyMember member = members.get(i);
          if (role.getRoleId().equals(Integer.valueOf(member.getRoleId()))) {
            members.remove(i);
            break;
          } 
        }  
    } else {
      List<Integer> serials = new ArrayList<>();
      if (!reqMembers.getList().isEmpty()) {
        List<Integer> arrays = new ArrayList<>();
        try {
          arrays = (List<Integer>)JsonUtils.string2Collection(reqMembers.getList(), List.class, Integer.class);
          serials = DataGlobalFunc.getRoleCareerSerial(arrays);
        } catch (Exception e) {
          this.logger.error("获取种族搜索数据错误：{}", reqMembers.getList());
          e.printStackTrace();
        } 
      } 
      List<PartyMember> list1 = (List<PartyMember>)party.getMembers().values().stream().collect(Collectors.toList());
      List<Integer> finalSerials = serials;
      members = (List<PartyMember>)list1.stream().filter(member -> (member.getName().equals(reqMembers.getPara()) || finalSerials.contains(Integer.valueOf(member.getRoleSerial())))).collect(Collectors.toList());
    } 
    List<PartyMember> list = PageUtil.subList(members, Integer.valueOf(reqMembers.getPage()), Integer.valueOf(reqMembers.getSize()));
    objects.addAll(list);
    objects.forEach(member -> {
          byte onLineStatus = (byte)(this.roleService.isOnline(member.getRoleId()) ? 1 : 0);
          member.setOnLineStatus(onLineStatus);
          if (onLineStatus == 0) {
            Role r = this.roleService.getPlayer(role.getRoleId().intValue());
            if (r != null)
              member.setLastOnlineTime(Math.toIntExact(r.getOfLineTime().longValue() / 1000L)); 
          } 
        });
    if (reqMembers.getSort() == 1) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getName)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getName).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 2) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getGrade)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getGrade).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 3) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getRace)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getRace).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 4) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getJob)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getJob).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 5) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getTeam)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getTeam).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 6) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getJoinTime)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getJoinTime).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 7) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getOnLineStatus)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getOnLineStatus).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 8) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getWeekBangBtl)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getWeekBangBtl).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 9) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getThisBangBtl)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getThisBangBtl).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 10) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getWeekEcType)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getWeekEcType).reversed()).collect(Collectors.toList());
      } 
    } else if (reqMembers.getSort() == 14) {
      if (reqMembers.getWay() == 0) {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getContribute)).collect(Collectors.toList());
      } else {
        objects = (List<PartyMember>)objects.stream().sorted(Comparator.comparing(PartyMember::getContribute).reversed()).collect(Collectors.toList());
      } 
    } 
    return objects;
  }
  
  public void refuseJoinParty(Role role, String name) {
    try {
      if (role == null || StringUtils.isEmpty(name))
        return; 
      Party party = getParty(role.getFaction());
      if (party == null)
        return; 
      if (!isAgreeOrDenyApply(role))
        return; 
      RespClearnRequest respClearnRequest = new RespClearnRequest();
      List<String> names = new ArrayList<>();
      if ("all".equals(name)) {
        Set<String> keySet = party.getReqJoin().keySet();
        if (keySet != null)
          names.addAll(keySet); 
      } else {
        names.add(name);
        respClearnRequest.setNames(names);
      } 
      respClearnRequest.setType("party");
      MessagePusher.pushMessage(role, (Message)respClearnRequest);
      NutConcurrentMap reqJoin = party.getReqJoin();
      if (reqJoin == null)
        return; 
      for (String tempName : names) {
        ReqJoinMember reqJoinAs = (ReqJoinMember)reqJoin.getAs(tempName, ReqJoinMember.class, null);
        if (reqJoin != null) {
          Role sessionRole = SpringUtils.getRoleService().getOnlinePlayer(reqJoinAs.getId());
          if (sessionRole != null)
            MessagePusher.notify2Player(sessionRole, 307, new Object[0]); 
        } 
        reqJoin.remove(tempName);
      } 
      party.save();
    } catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void upgrade(Role role) {
    Party party = getParty(role.getFaction());
    if (party == null)
      return; 
    if (!isAgreeOrDenyApply(role))
      return; 
    byte nextLevel = (byte)(party.getLevel() + 1);
    Integer capital = (Integer)PartyDataPool.partyUpgrades.get(Byte.valueOf(nextLevel));
    if (capital == null) {
      MessagePusher.notify2Player(role, 372, new Object[0]);
      return;
    } 
    if (party.getCapital() < capital.intValue()) {
      MessagePusher.notify2Player(role, 373, new Object[] { capital });
      return;
    } 
    if (!this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, ((Integer)PartyDataPool.partyUpgrades.get(Byte.valueOf(nextLevel))).intValue(), "升级帮派"))
      return; 
    party.setLevel(nextLevel);
    party.save();
    getRespOpenPatry(party, role);
  }
  
  public void modifyNotice(Role role, String notice, byte type) {
    Party party = getParty(role.getFaction());
    if (party == null)
      return; 
    if (!isPartyLeader(role)) {
      MessagePusher.notify2Player(role, 345, new Object[0]);
      return;
    } 
    if (checkNotice(role, notice)) {
      if (type == 0) {
        party.setNotice(notice);
      } else {
        party.setAim(notice);
      } 
      party.save();
    } 
    getRespOpenPatry(party, role);
    MessagePusher.notify2Player(role, 358, new Object[0]);
  }
  
  public boolean checkNotice(Role role, String notice) {
    if (notice.length() > 2048) {
      MessagePusher.notify2Player(role, 362, new Object[0]);
      return false;
    } 
    List<String> set = WordDisposePool.checkDirtyWords(notice);
    if (set.size() >= 1) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("输入的内容存在敏感词。"));
      return false;
    } 
    return true;
  }
  
  public void kickOut(Role role, int id) {
    if (role == null)
      return; 
    if (role.getRoleId().intValue() == id)
      return; 
    GangWarsData objData = ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).getObjData();
    if (objData != null && objData.getWarsMap().get(role.getFaction()) != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("正临帮战，大敌当前，应精诚团结，一致对外，不宜踢人、离帮"));
      return;
    } 
    Party party = getParty(role.getFaction());
    if (party == null)
      return; 
    if (!isInParty(party, id)) {
      MessagePusher.notify2Player(role, 308, new Object[0]);
      return;
    } 
    if (!isKickPermission(role)) {
      MessagePusher.notify2Player(role, 309, new Object[0]);
      return;
    } 
    if (isPermissionHigher(party, role.getRoleId().intValue(), id)) {
      MessagePusher.notify2Player(role, 310, new Object[0]);
      return;
    } 
    doKickOut(role, party, id);
  }
  
  private boolean isPermissionHigher(Role firstRole, Role secondRole) {
    byte firstPermission = getPermission(firstRole);
    byte secondPermission = getPermission(secondRole);
    return (firstPermission < secondPermission);
  }
  
  private boolean isPermissionHigher(Party party, int firstId, int secondId) {
    PartyMember firstPartyMember = (PartyMember)party.getMembers().get(Integer.valueOf(firstId));
    PartyMember secondPartyMember = (PartyMember)party.getMembers().get(Integer.valueOf(secondId));
    byte firstPermission = firstPartyMember.getJob();
    byte secondPermission = secondPartyMember.getJob();
    return (firstPermission < secondPermission);
  }
  
  private boolean isKickPermission(Role role) {
    if (role == null)
      return false; 
    byte permission = getPermission(role);
    return (permission == 1 || permission == 2);
  }
  
  private boolean isExistPermission(Party party, byte job) {
    List<PartyMember> list = (List<PartyMember>)party.getMembers().values().stream().filter(member -> (member.getJob() == job)).collect(Collectors.toList());
    return (list.size() > 0);
  }
  
  private byte getPermission(Role role) {
    String partyJob = getPartyJob(role);
    if (partyJob != null) {
      Byte permission = (Byte)PartyDataPool.partyPermissions.get(partyJob);
      if (permission != null)
        return permission.byteValue(); 
    } 
    return 8;
  }
  
  public String getPartyJob(Role role) {
    Party party = getParty(role);
    if (party == null)
      return null; 
    PartyMember partyMember = getPartyMember(party, role);
    return getPartyJobName(partyMember);
  }
  
  public String getPartyJob(Party party, Integer id) {
    if (party == null)
      return null; 
    PartyMember partyMember = getPartyMember(party, id);
    return getPartyJobName(partyMember);
  }
  
  public PartyMember getPartyMember(Party party, Integer id) {
    if (party == null)
      return null; 
    return (PartyMember)party.getMembers().get(id);
  }
  
  private void doKickOut(Role role, Party party, int id) {
    if (party == null)
      return; 
    RoleView roleView = new RoleView(this.roleService.getPlayer(id));
    String content = I18nIdDataPool.getI18nContent(314, new Object[] { getPartyJob(role), role.getName(), roleView.getName() });
    npcPartyChat(party, content);
    Role targetRole = roleView.getRole();
    if (targetRole != null) {
      ThreadLocalUtil.addLocalTask(targetRole, () -> leaveParty(party, targetRole));
    } else {
      PartyDataPool.offlineRoleParty.put(Integer.valueOf(id), Integer.valueOf(-1));
    } 
    PartyMember member = (PartyMember)party.getMembers().get(Integer.valueOf(id));
    member.setDelete(true);
    member.save();
    party.getMembers().remove(Integer.valueOf(id));
    MessagePusher.notify2Player(role, 311, new Object[] { roleView.getName() });
    MessagePusher.pushMessage(role, (Message)new RespHint(7, (short)1));
    addPartyLog(party, 351, new String[] { role.getRoleId().toString(), role.getName(), String.valueOf(roleView.getRoleId()), roleView.getName() });
  }
  
  private void leaveParty(Party party, Role role) {
    doLeaveParty(role);
    if (isInPartyMap(role))
      leavePartyMap(role); 
    String kickContent = I18nIdDataPool.getI18nContent(313, new Object[] { party.getName() });
    MessagePusher.pushMessage(role, (Message)new RespNotifyMisc(kickContent));
    String mailTitle = I18nIdDataPool.getI18nContent(312);
    String mailContent = I18nIdDataPool.getI18nContent(313, new Object[] { party.getName() });
  }
  
  public void doLeaveParty(Role role) {
    role.setFaction(Integer.valueOf(0));
    role.save();
    delPartyTitle(role);
    this.roleService.refreshParty(role);
  }
  
  public void npcPartyChat(Party party, String content) {
    if (party == null || StringUtils.isEmpty(content))
      return; 
    Npc npc = SpringUtils.getMapService().getNpc(90999);
    SenderUtils.pushPartyChatMessage(party.getSerial(), getPartyRespChat(npc, party.getName(), content));
  }
  
  private RespChat getPartyRespChat(Npc npc, String partyName, String content) {
    RespChat respChat = new RespChat();
    respChat.setRoleId(npc.getId());
    respChat.setRoleName(npc.getName());
    respChat.setPartyName(partyName);
    respChat.setMsg(content);
    respChat.setList(FieldUtils.getChatList(npc));
    respChat.setType(5);
    return respChat;
  }
  
  public void setAutoAgreeSetting(Role role, ReqPartyRejectLevel reqPartyRejectLevel) {
    if (role == null)
      return; 
    Party party = getParty(role.getFaction());
    if (party == null) {
      MessagePusher.notify2Player(role, 319, new Object[0]);
      return;
    } 
    if (!isAgreeOrDenyApply(role)) {
      MessagePusher.notify2Player(role, 345, new Object[0]);
      return;
    } 
    party.setConsent(reqPartyRejectLevel.getConsent());
    party.setConsentLevel(reqPartyRejectLevel.getConsentLevel());
    party.setRefuse(reqPartyRejectLevel.getRefuse());
    party.setRefuseLevel(reqPartyRejectLevel.getRefuseLevel());
    party.save();
    MessagePusher.pushMessage(role, (Message)new RespMsg("设置成功"));
    MessagePusher.pushMessage(role, (Message)new RespHint(6, (short)1));
  }
  
  public void quitParty(Role role) {
    if (role == null)
      return; 
    Party party = getParty(role.getFaction());
    if (party == null) {
      MessagePusher.notify2Player(role, 319, new Object[0]);
      return;
    } 
    GangWarsData objData = ((GangWarsHandler)SpringUtils.getBean(GangWarsHandler.class)).getObjData();
    if (objData != null && objData.getWarsMap().get(role.getFaction()) != null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("正临帮战，大敌当前，应精诚团结，一致对外，不宜踢人、离帮"));
      return;
    } 
    String partyJob = getPartyJob(role);
    if ("帮主".equals(partyJob)) {
      PartyMember partyMember = null;
      List<PartyMember> collect = (List<PartyMember>)party.getMembers().values().stream().filter(e -> (e.getJob() > 10 && !role.getRoleId().equals(Integer.valueOf(e.getRoleId())))).collect(Collectors.toList());
      if (collect.size() > 0) {
        collect = (List<PartyMember>)collect.stream().sorted(Comparator.comparing(PartyMember::getJob, Comparator.reverseOrder())).collect(Collectors.toList());
        partyMember = collect.get(0);
      } else {
        List<PartyMember> list = (List<PartyMember>)party.getMembers().values().stream().filter(e -> (e.getJob() == 10)).collect(Collectors.toList());
        list = (List<PartyMember>)list.stream().sorted(Comparator.comparing(PartyMember::getContribute, Comparator.reverseOrder())).collect(Collectors.toList());
        if (list != null && list.size() > 0)
          partyMember = list.get(0); 
      } 
      if (partyMember == null)
        return; 
      Role player = this.roleService.getPlayer(partyMember.getRoleId());
      if (party == null)
        return; 
      partyMember.setJob((byte)70);
      partyMember.save();
      party.setMasterId(player.getRoleId());
      party.setMasterName(player.getName());
      delPartyTitle(player);
      addPartyTitle(party, player);
    } 
    doLeaveParty(role);
    PartyMember member = (PartyMember)party.getMembers().get(role.getRoleId());
    if (member != null) {
      member.setDelete(true);
      member.save();
    } 
    party.getMembers().remove(role.getRoleId());
    npcPartyChat(party, I18nIdDataPool.getI18nContent(322, new Object[] { role.getName() }));
    EventDispatcher.getInstance().fireEvent((BaseGameEvent)new QuitPartyEvent(EventType.QUIT_PARTY, role, party));
    if (isInPartyMap(role))
      leavePartyMap(role); 
    MessagePusher.notify2Player(role, 321, new Object[] { party.getName() });
    addPartyLog(party, 350, new String[] { role.getRoleId().toString(), role.getName() });
  }
  
  public boolean isInPartyMap(Role role) {
    if (role == null)
      return false; 
    return (role.getPos().getMapId().intValue() == 3002 || role.getPos().getMapId().intValue() == 3021 || role.getPos().getMapId().intValue() == 3022);
  }
  
  public void leavePartyMap(Role role) {
    ReqTelePort reqTelePort = new ReqTelePort();
    SpringUtils.getMapService().changeMap(role, reqTelePort, null);
  }
  
  public void handleLogin(Role role) {
    Party party = getParty(role.getFaction());
    Integer newPartyId = (Integer)PartyDataPool.offlineRoleParty.remove(role.getRoleId());
    if (party != null) {
      if (newPartyId != null && newPartyId.intValue() == -1) {
        leaveParty(party, role);
      } else if (isAgreeOrDenyApply(role) && isHaveJoinRequest(party)) {
        RespPartyNotification respPartyNotification = new RespPartyNotification((short)2);
        MessagePusher.pushMessage(role, (Message)respPartyNotification);
      } 
    } else {
      if (isInPartyMap(role))
        leavePartyMap(role); 
      if (newPartyId != null && newPartyId.intValue() > 0) {
        party = getParty(newPartyId);
        joinParty(party, role);
      } 
    } 
  }
  
  public void prohibitSpeaking(Role role, int id, String name, short hours) {
    if (role == null)
      return; 
    if (role.getRoleId().equals(Integer.valueOf(id)))
      return; 
    Party party = getParty(role.getFaction());
    if (party == null)
      return; 
    if (!isInParty(party, id)) {
      MessagePusher.notify2Player(role, 308, new Object[0]);
      return;
    } 
    if (!isDenyPermission(role)) {
      MessagePusher.notify2Player(role, 335, new Object[0]);
      return;
    } 
    if (isPermissionHigher(party, role.getRoleId().intValue(), id)) {
      MessagePusher.notify2Player(role, 336, new Object[0]);
      return;
    } 
    int endTime = (int)((System.currentTimeMillis() + hours * 3600000L) / 1000L);
    PartyDeny partyDeny = new PartyDeny(endTime);
    party.getDenyMap().put(Integer.valueOf(id), partyDeny);
    party.save();
    String content = I18nIdDataPool.getI18nContent(337, new Object[] { name, Short.valueOf(hours) });
    npcPartyChat(party, content);
    MessagePusher.pushMessage(role, (Message)new RespMsg("禁言成功"));
    Role targetRole = SpringUtils.getRoleService().getOnlinePlayer(id);
    if (targetRole != null) {
      String mailTitle = I18nIdDataPool.getI18nContent(338);
      String str1 = I18nIdDataPool.getI18nContent(339, new Object[] { Short.valueOf(hours), Short.valueOf(hours) });
    } 
  }
  
  private boolean isDenyPermission(Role role) {
    byte permission = getPermission(role);
    return (permission == 1 || permission == 2);
  }
  
  public byte getJob(Role role) {
    Party party = getParty(role.getFaction());
    if (party == null)
      return 0; 
    PartyMember partyMember = (PartyMember)party.getMembers().get(role.getRoleId());
    if (partyMember == null)
      return 0; 
    return partyMember.getJob();
  }
  
  public void changeJob(Role role, int id, String name, byte career) {
    if (role == null)
      return; 
    if (role.getRoleId().intValue() == id)
      return; 
    Party party = getParty(role.getFaction());
    if (party == null)
      return; 
    if (!isInParty(party, id)) {
      MessagePusher.notify2Player(role, 308, new Object[0]);
      return;
    } 
    if (!isChangeJobPermission(role)) {
      MessagePusher.notify2Player(role, 326, new Object[0]);
      return;
    } 
    if (isPermissionHigher(party, role.getRoleId().intValue(), id)) {
      MessagePusher.notify2Player(role, 327, new Object[0]);
      return;
    } 
    Byte jobId = (Byte)PartyDataPool.partyCareer.get(Byte.valueOf(career));
    if (isExistPermission(party, jobId.byteValue())) {
      MessagePusher.notify2Player(role, 397, new Object[0]);
      return;
    } 
    String jobName = (String)PartyDataPool.jobId2Names.get(jobId);
    String oldJobName = getPartyJob(party, Integer.valueOf(id));
    long uid = SpringUtils.getPlayerService().getUidBy(id);
    RoleView roleView = new RoleView(uid);
    Role targetRole = roleView.getRole();
    PartyMember partyMember = (PartyMember)party.getMembers().get(Integer.valueOf(id));
    partyMember.setJob(jobId.byteValue());
    partyMember.save();
    RespPartyMembers respPartyMembers = getRespPartyMembers(party, role, new ReqPartyMembers());
    MessagePusher.pushMessage(role, (Message)respPartyMembers);
    MessagePusher.pushMessage(role, (Message)new RespMsg("任命成功"));
    String content = I18nIdDataPool.getI18nContent(328, new Object[] { role.getName(), name, oldJobName, jobName });
    npcPartyChat(party, content);
    if (targetRole != null) {
      String mailTitle = I18nIdDataPool.getI18nContent(330);
      String str1 = I18nIdDataPool.getI18nContent(331, new Object[] { role.getName(), oldJobName, jobName });
    } 
    String partyJob = getPartyJob(role);
    addPartyLog(party, 353, new String[] { partyJob, role.getRoleId().toString(), role.getName(), String.valueOf(roleView.getRoleId()), roleView.getName(), jobName });
    party.save();
  }
  
  private boolean isChangeJobPermission(Role role) {
    byte permission = getPermission(role);
    return (permission == 1 || permission == 2);
  }
  
  private boolean isPartyLeader(Role role) {
    String partyJob = getPartyJob(role);
    return "帮主".equals(partyJob);
  }
  
  private boolean isInParty(Party party, int roleId) {
    if (party == null)
      return false; 
    return party.getMembers().containsKey(Integer.valueOf(roleId));
  }
  
  public void cancelProhibitSpeaking(Role role, int id, String name) {
    if (role == null)
      return; 
    Party party = getParty(role.getFaction());
    if (party == null)
      return; 
    if (!isInParty(party, id)) {
      MessagePusher.notify2Player(role, 308, new Object[0]);
      return;
    } 
    if (!isDenyPermission(role)) {
      MessagePusher.notify2Player(role, 340, new Object[] { "解除禁言" });
      return;
    } 
    party.getDenyMap().remove(Integer.valueOf(id));
    party.save();
    String content = I18nIdDataPool.getI18nContent(341, new Object[] { name });
    npcPartyChat(party, content);
    MessagePusher.pushMessage(role, (Message)new RespMsg("解除禁言成功"));
    Role targetRole = SpringUtils.getRoleService().getOnlinePlayer(id);
    if (targetRole != null) {
      String mailTitle = I18nIdDataPool.getI18nContent(338);
      String str1 = I18nIdDataPool.getI18nContent(342);
    } 
  }
  
  public void getPartyLog(Role role) {
    MessagePusher.pushMessage(role, (Message)getLog(role));
  }
  
  private RespPartyLog getLog(Role role) {
    RespPartyLog respPartyLog = new RespPartyLog();
    Party party = getParty(role);
    if (party == null)
      return respPartyLog; 
    List<PartyLog> logs = new ArrayList<>(party.getLogs());
    logs.sort(Comparator.<PartyLog, Comparable>comparing(PartyLog::getTime).reversed());
    Map<Integer, List<LogInfo>> map = new HashMap<>();
    for (PartyLog log : logs) {
      int time = TimeUtil.dayInMillis(TimeUtil.stampToDate((long)(log.getTime() * 1000.0D)));
      List<LogInfo> strings = map.get(Integer.valueOf(time));
      if (strings == null)
        if (map.size() < 3) {
          strings = new ArrayList<>();
        } else {
          break;
        }  
      String content = I18nIdDataPool.getI18nContent(log.getTkId(), (Object[])log.getArgs());
      strings.add(new LogInfo(content));
      map.put(Integer.valueOf(time), strings);
    } 
    List<PartyLogInfo> list = new ArrayList<>();
    for (Integer key : map.keySet()) {
      PartyLogInfo partyLogInfo = new PartyLogInfo();
      partyLogInfo.setTime(key.intValue());
      partyLogInfo.setList(map.get(key));
      list.add(partyLogInfo);
    } 
    respPartyLog.setList(list);
    return respPartyLog;
  }
  
  public void rename(Role role, String name) {
    if (role == null)
      return; 
    Party party = getParty(role.getFaction());
    if (party == null) {
      MessagePusher.notify2Player(role, 319, new Object[0]);
      return;
    } 
    if (!isPartyLeader(role)) {
      MessagePusher.notify2Player(role, 345, new Object[0]);
      return;
    } 
    boolean check = checkPartyName(role, name);
    if (!check)
      return; 
    doRename(party, name);
  }
  
  private void doRename(Party party, String name) {
    PartyDataPool.partyName2Id.remove(party.getName());
    party.setName(name);
    party.save();
    PartyDataPool.partyName2Id.put(party.getName(), party.getSerial());
  }
  
  private void addPartyLog(Party party, int i18nId, String[] args) {
    party.getLogs().add(new PartyLog(i18nId, args, party.getSerial().intValue()));
    int size = party.getLogs().size();
    if (size > 50)
      for (int i = 0, loop = size - 50; i < loop; i++)
        party.getLogs().remove(0);  
    party.save();
  }
  
  private String getPartyJobName(PartyMember partyMember) {
    if (partyMember == null)
      return null; 
    return (String)PartyDataPool.jobId2Names.get(Byte.valueOf(partyMember.getJob()));
  }
  
  private boolean checkPartyName(Role role, String name) {
    if (StringUtils.isBlank(name)) {
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
    if (isPartyNameExist(name)) {
      MessagePusher.notify2Player(role, 301, new Object[0]);
      return false;
    } 
    List<String> set = WordDisposePool.checkDirtyWords(name);
    if (set.size() >= 1) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("帮派名存在敏感词。"));
      return false;
    } 
    return true;
  }
  
  public void sendPartyMail(Role role, String title, String mailMsg) {
    if (role == null)
      return; 
    MessagePusher.pushMessage(role, (Message)new RespMsg("公告已发送。"));
  }
  
  private boolean isSendMailPermission(Role role) {
    byte permission = getPermission(role);
    return (permission == 1 || permission == 2);
  }
  
  private boolean isManager(String jobName) {
    if (Const.MANAGER_JOBS.contains(jobName))
      return true; 
    return false;
  }
  
  public boolean isChatDeny(Role role) {
    Party party = getParty(role);
    if (party == null)
      return true; 
    return party.getDenyMap().containsKey(role.getRoleId());
  }
  
  public long getChatDenyEndTime(Role role) {
    Party party = getParty(role);
    if (party == null)
      return 0L; 
    PartyDeny partyDeny = (PartyDeny)party.getDenyMap().get(role.getRoleId());
    if (partyDeny == null)
      return 0L; 
    return partyDeny.getEndTime();
  }
  
  public void check() {
    Party party = null;
    Set<Integer> ids = PartyDataPool.partyMap.keySet();
    for (Integer id : ids) {
      party = (Party)PartyDataPool.partyMap.get(id);
      if (party == null)
        continue; 
      checkParty(party);
    } 
  }
  
  private void checkParty(Party party) {
    if (party == null)
      return; 
    checkChatDeny(party);
    this.redBagHandler.checkPartyRedBag(party);
  }
  
  private void checkChatDeny(Party party) {
    long now = System.currentTimeMillis();
    PartyDeny partyDeny = null;
    Set<Integer> ids = party.getDenyMap().keySet();
    List<Integer> del = null;
    for (Integer id : ids) {
      partyDeny = (PartyDeny)party.getDenyMap().get(id);
      if (partyDeny == null)
        continue; 
      if (now > partyDeny.getEndTime() * 1000L) {
        if (del == null)
          del = new ArrayList<>(); 
        del.add(id);
      } 
    } 
    if (del != null)
      for (Integer id : del) {
        party.getDenyMap().remove(id);
        party.save();
      }  
  }
  
  private PartyMember getPartyMember(Party party, String name) {
    if (party == null || name == null)
      return null; 
    Collection<PartyMember> memberObjects = party.getMembers().values();
    for (PartyMember partyMember : memberObjects) {
      if (name.equals(partyMember.getName()))
        return partyMember; 
    } 
    return null;
  }
  
  public int getTitle(PartyMember partyMember) {
    return ((Integer)PartyDataPool.jobId2TitleSerial.get(Byte.valueOf(partyMember.getJob()))).intValue();
  }
  
  private boolean isHaveJoinRequest(Party party) {
    if (party == null)
      return false; 
    return (party.getReqJoin() != null && party.getReqJoin().size() > 0);
  }
  
  public void addConstruction(Party party, long value) {
    if (value <= 0L || party == null)
      return; 
    party.setCapital(party.getCapital() + value);
    party.save();
  }
  
  public void substractCapital(Role role, int value) {
    if (role == null)
      return; 
    if (value <= 0)
      return; 
    Party party = getParty(role);
    if (party == null)
      return; 
    long before = party.getCapital();
    long current = before - value;
    if (current < 0L)
      current = 0L; 
    party.setCapital(current);
    party.save();
  }
  
  public short getMemberCount(Party party) {
    if (party != null && party.getMembers() != null)
      return (short)party.getMembers().size(); 
    return 0;
  }
  
  public int getPartyMaxMembers(int level) {
    int maxMembers = 0;
    if (level == 1) {
      maxMembers = 150;
    } else if (level == 2) {
      maxMembers = 200;
    } else if (level == 3) {
      maxMembers = 250;
    } else if (level == 4) {
      maxMembers = 300;
    } else if (level == 5) {
      maxMembers = 310;
    } else if (level == 6) {
      maxMembers = 320;
    } else if (level == 7) {
      maxMembers = 330;
    } else if (level == 8) {
      maxMembers = 340;
    } 
    return maxMembers;
  }
  
  private PartyMember getMemberByName(Party party, String name) {
    if (party == null)
      return null; 
    Collection<PartyMember> values = party.getMembers().values();
    for (PartyMember partyMember : values) {
      if (partyMember.getName().equals(name))
        return partyMember; 
    } 
    return null;
  }
  
  public void handleLogout(Role role) {
    Party party = getParty(role);
    if (party == null)
      return; 
    PartyMember partyMember = getPartyMember(party, role);
    if (partyMember == null)
      return; 
    long now = System.currentTimeMillis();
    partyMember.setLastOnlineTime((int)(now / 1000L));
  }
  
  public void handleRoleChange(Role role, String currName) {
    if (role.getFaction().intValue() <= 0)
      return; 
    Party party = getParty(role);
    if (party == null)
      return; 
    PartyMember partyMember = getPartyMember(party, role);
    if (partyMember != null) {
      if (currName != null)
        partyMember.setName(currName); 
      partyMember.setRoleSerial(role.getSerial().intValue());
      partyMember.save();
    } 
    if (role.getRoleId().equals(party.getMasterId()) && currName != null) {
      party.setMasterName(currName);
      party.save();
    } 
  }
  
  public void handleRoleLevelUpEvent(Role role) {
    Party party = getParty(role);
    if (party == null)
      return; 
    PartyMember partyMember = getPartyMember(party, role);
    if (partyMember != null) {
      partyMember.setRei(role.getRelive().byteValue());
      partyMember.setLevel(role.getLevel().shortValue());
      partyMember.setGrade(role.getRelive().intValue() * 1000 + role.getLevel().intValue());
      partyMember.save();
    } 
  }
  
  public void updatePartyTask(Role role, int count, long calculate) {
    Party party = getParty(role);
    if (party == null)
      return; 
    PartyMember member = (PartyMember)party.getMembers().get(role.getRoleId());
    if (member != null) {
      int millis = TimeUtil.dayInMillis(TimeUtil.when());
      PartyTask partyTask = member.getPartyTaskList().stream().filter(e -> (e.getTime() == millis)).findFirst().orElse(null);
      if (partyTask == null) {
        partyTask = new PartyTask();
        member.getPartyTaskList().add(partyTask);
      } 
      partyTask.setTime(millis);
      partyTask.setCount(count);
      member.setContribute(member.getContribute() + calculate);
      member.save();
      addConstruction(party, BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Integer.valueOf(36 + (party.getLevel() - 1) * 18)).longValue() * 2L);
    } 
  }
  
  public void resetPartyTask() {
    int millis = TimeUtil.dayInMillis(TimeUtil.when());
    for (Party party : PartyDataPool.partyMap.values()) {
      for (PartyMember member : party.getMembers().values()) {
        member.getPartyTaskList().removeIf(e -> (e.getTime() < millis));
        member.save();
      } 
    } 
  }
  
  public void updateActivation() {
    int week = TimeUtil.getWeek();
    int millis = TimeUtil.dayInMillis(TimeUtil.when());
    for (Party party : PartyDataPool.partyMap.values()) {
      int size = party.getMembers().size();
      int online = 0;
      for (PartyMember member : party.getMembers().values()) {
        if (this.roleService.isOnline(member.getRoleId())) {
          online++;
          continue;
        } 
        Role player = this.roleService.getPlayer(member.getRoleId());
        if (player != null) {
          if (player.getOfLineTime().longValue() > 0L) {
            int time = TimeUtil.dayInMillis(TimeUtil.stampToDate(player.getOfLineTime().longValue()));
            if (time >= millis)
              online++; 
            continue;
          } 
          online++;
        } 
      } 
      String divide = BigDecimalUtil.divide(String.valueOf(online), String.valueOf(size));
      party.getOnlineRate().add(Integer.valueOf((int)(Double.valueOf(divide).doubleValue() * 100.0D)));
      if (party.getOnlineRate().size() >= 3) {
        int sum = party.getOnlineRate().stream().mapToInt(Integer::valueOf).sum();
        party.setActive(BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Integer.valueOf(sum / 3)).shortValue());
        party.getOnlineRate().clear();
      } 
      party.save();
    } 
    if (week == 4)
      mergeParty(); 
  }
  
  public void mergeParty() {
    StopWatch stopWatch = new StopWatch();
    stopWatch.start();
    Map<Integer, Party> map = new HashMap<>();
    for (Party party : PartyDataPool.partyMap.values()) {
      if (party.getActive() < 30)
        map.put(party.getSerial(), party); 
    } 
    int joinTime = (int)(System.currentTimeMillis() / 1000L);
    List<Party> list = (List<Party>)PartyDataPool.partyMap.values().stream().sorted(Comparator.comparing(Party::getActive, Comparator.reverseOrder())).collect(Collectors.toList());
    for (Party party : map.values()) {
      for (PartyMember value : party.getMembers().values()) {
        int serial = 0;
        for (int i = 0; i < list.size(); i++) {
          Party p = list.get(i);
          if (isPartyFull(p)) {
            list.removeIf(e -> e.getSerial().equals(p.getSerial()));
          } else {
            serial = p.getSerial().intValue();
            Map<Integer, PartyMember> members = p.getMembers();
            value.setSerial(p.getSerial().intValue());
            value.setJob((byte)10);
            value.setPartyTaskList(new ArrayList());
            value.setJoinTime(joinTime);
            value.save();
            members.put(Integer.valueOf(value.getRoleId()), value);
          } 
        } 
        Role player = this.roleService.getPlayer(value.getRoleId());
        if (player != null) {
          if (serial == 0) {
            value.setDelete(true);
            value.save();
          } 
          player.setFaction(Integer.valueOf(serial));
          player.save();
        } 
      } 
      party.setDelete(true);
      party.save();
      PartyDataPool.partyMap.remove(party.getSerial());
    } 
    stopWatch.stop();
    this.logger.error("合并帮派，耗時{}毫秒", Long.valueOf(stopWatch.getTime()));
  }
  
  public void clearParty(Role role) {
    role.setFaction(Integer.valueOf(0));
    this.roleService.refreshParty(role);
    delPartyTitle(role);
    role.save();
    this.jdbcTemplate.update("delete from party_member where role_id = " + role.getRoleId());
  }
}
