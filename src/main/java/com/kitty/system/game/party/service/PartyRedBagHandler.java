package com.kitty.system.game.party.service;

import com.kitty.logs.LoggerFunction;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.game.chat.message.RespChat;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.party.message.RespPartyRecvRedBag;
import com.kitty.system.game.party.message.RespPartyRedBagList;
import com.kitty.system.game.party.message.RespPartyRedBagRecord;
import com.kitty.system.game.party.message.RespRedBagSendInfo;
import com.kitty.system.game.party.message.vo.RecvRedBagInfo;
import com.kitty.system.game.party.model.FetchRedBagRecord;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.game.party.model.PartyRedBag;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PartyRedBagHandler {
  private Logger logger = LoggerFunction.PARTY.getLogger();
  
  @Autowired
  private PartyService partyService;
  
  public void sendRedBagSetting(Role role) {
    RespRedBagSendInfo respRedBagSendInfo = getRespRedBagSendInfo(role);
    MessagePusher.pushMessage(role, (Message)respRedBagSendInfo);
  }
  
  private RespRedBagSendInfo getRespRedBagSendInfo(Role role) {
    RespRedBagSendInfo respRedBagSendInfo = new RespRedBagSendInfo();
    Party party = this.partyService.getParty(role);
    short memberCount = this.partyService.getMemberCount(party);
    respRedBagSendInfo.setMemberCount(memberCount);
    return respRedBagSendInfo;
  }
  
  public void sendRedBag(Role role, short gold, short num, String msg, String format) {
    Party party = this.partyService.getParty(role);
    if (party == null) {
      MessagePusher.notify2Player(role, 319, new Object[0]);
      return;
    } 
    synchronized (party) {
      String check = checkSendRedBag(role, gold, num);
      if (StringUtils.isNotEmpty(check)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(check));
        return;
      } 
      PartyRedBag partyRedBag = addPartyRedBag(role, party, gold, num, msg);
      if (partyRedBag == null)
        return; 
      addMemberSendRecord(role, party, partyRedBag);
      String formatMsg = String.format(format, new Object[] { Long.valueOf(partyRedBag.getId()), Integer.valueOf(1), party.getId(), msg });
      sendPartyChat(role, formatMsg, true);
      RespPartyRedBagList respPartyRedBagList = getRespPartyRedBagList(role);
      MessagePusher.pushMessage(role, (Message)respPartyRedBagList);
      MessagePusher.notify2Player(role, 390, new Object[0]);
    } 
  }
  
  private String checkSendRedBag(Role role, short gold, short num) {
    int minNum = getMinNum(gold);
    int maxNum = getMaxNum(gold);
    if (num < minNum)
      return I18nIdDataPool.getI18nContent(385, new Object[] { Integer.valueOf(minNum) }); 
    if (num > maxNum)
      return I18nIdDataPool.getI18nContent(386, new Object[] { Integer.valueOf(maxNum) }); 
    if (role.getGold().longValue() < gold)
      return I18nIdDataPool.getI18nContent(403); 
    return null;
  }
  
  private int getMaxNum(int gold) {
    return (int)Math.min(100.0D, Math.floor((gold * 3 / 300)));
  }
  
  private int getMinNum(int gold) {
    return (int)Math.max(Math.floor((gold / 600)), 10.0D);
  }
  
  private PartyRedBag addPartyRedBag(Role role, Party party, short gold, short num, String msg) {
    if (role == null || party == null)
      return null; 
    PartyRedBag partyRedBag = new PartyRedBag(role.getRoleId().intValue(), role.getName(), gold, num, msg);
    if (party.getRedBagList() == null)
      party.setRedBagList(new ArrayList()); 
    party.getRedBagList().add(0, partyRedBag);
    int size = party.getRedBagList().size();
    party.save();
    return partyRedBag;
  }
  
  private void addMemberSendRecord(Role role, Party party, PartyRedBag partyRedBag) {}
  
  private RespPartyRedBagList getRespPartyRedBagList(Role role) {
    RespPartyRedBagList respPartyRedBagList = new RespPartyRedBagList();
    return respPartyRedBagList;
  }
  
  private boolean isRedBaExpire(PartyRedBag partyRedBag) {
    return true;
  }
  
  private boolean isRedBagNotExpire(PartyRedBag partyRedBag) {
    return !isRedBaExpire(partyRedBag);
  }
  
  private boolean isRedBagSender(Role role, PartyRedBag partyRedBag) {
    if (role == null || partyRedBag == null)
      return false; 
    return (partyRedBag.getSenderUid() == role.getRoleId().intValue());
  }
  
  private boolean isRedBagFetched(Role role, PartyRedBag partyRedBag) {
    if (role == null || partyRedBag == null)
      return false; 
    return (partyRedBag.getFetchRedBagRecordMap() != null && partyRedBag.getFetchRedBagRecordMap().containsKey(role.getRoleId()));
  }
  
  public void sendPartyChat(Role role, String msg, boolean isRoleSend) {
    RespChat respChat = new RespChat();
    respChat.setMsg(msg);
    respChat.setList(isRoleSend ? FieldUtils.getChatList(role) : new ArrayList());
    respChat.setRoleName(isRoleSend ? role.getName() : null);
    respChat.setRoleId(isRoleSend ? role.getRoleId().intValue() : 0);
    respChat.setTime((int)(System.currentTimeMillis() / 1000L));
    respChat.setType(5);
    SenderUtils.pushPartyChatMessage(role.getFaction(), respChat);
  }
  
  public void showRedBagList(Role role) {
    RespPartyRedBagList respPartyRedBagList = getRespPartyRedBagList(role);
    MessagePusher.pushMessage(role, (Message)respPartyRedBagList);
  }
  
  public void showRedBag(Role role, long redBagGid) {
    RespPartyRecvRedBag respPartyRecvRegBag = getRespPartyRecvRedBag(role, redBagGid, false);
    MessagePusher.pushMessage(role, (Message)respPartyRecvRegBag);
  }
  
  private RespPartyRecvRedBag getRespPartyRecvRedBag(Role role, long redBagGid, boolean isFetchOper) {
    Party party = this.partyService.getParty(role);
    PartyRedBag partyRedBag = getPartyRedBag(party, redBagGid);
    if (partyRedBag == null)
      return null; 
    RespPartyRecvRedBag respPartyRecvRegBag = new RespPartyRecvRedBag();
    FetchRedBagRecord roleFetchRedBagRecord = null;
    if (partyRedBag.getFetchRedBagRecordMap() != null)
      roleFetchRedBagRecord = (FetchRedBagRecord)partyRedBag.getFetchRedBagRecordMap().get(role.getRoleId()); 
    byte type = 0;
    if (isFetchOper) {
      type = (byte)((roleFetchRedBagRecord != null) ? 3 : 2);
    } else {
      type = 1;
    } 
    respPartyRecvRegBag.setType(type);
    respPartyRecvRegBag.setTotalCoin(partyRedBag.getSilver());
    respPartyRecvRegBag.setCoin((roleFetchRedBagRecord != null) ? roleFetchRedBagRecord.getCoin() : 0);
    respPartyRecvRegBag.setRedbagGid(String.valueOf(redBagGid));
    PartyMember partyMember = (PartyMember)party.getMembers().get(Long.valueOf(partyRedBag.getSenderUid()));
    respPartyRecvRegBag.setSenderName((partyMember != null) ? partyMember.getName() : "");
    respPartyRecvRegBag.setMsg(partyRedBag.getMsg());
    respPartyRecvRegBag.setSendTime(partyRedBag.getTime());
    respPartyRecvRegBag.setCount(partyRedBag.getCount());
    byte state = (byte)(isRedBagNotExpire(partyRedBag) ? 1 : 0);
    respPartyRecvRegBag.setState(state);
    byte isSender = (byte)(isRedBagSender(role, partyRedBag) ? 1 : 0);
    respPartyRecvRegBag.setIsSender(isSender);
    byte isRecv = (byte)(isRedBagFetched(role, partyRedBag) ? 1 : 0);
    respPartyRecvRegBag.setIsRecv(isRecv);
    List<RecvRedBagInfo> recvRedBagInfoList = new ArrayList<>();
    respPartyRecvRegBag.setRecvRedBagInfoList(recvRedBagInfoList);
    if (partyRedBag.getFetchRedBagRecordMap() != null)
      for (FetchRedBagRecord fetchRedBagRecord : partyRedBag.getFetchRedBagRecordMap().values()) {
        PartyMember partyMemberObject = (PartyMember)party.getMembers().get(Long.valueOf(fetchRedBagRecord.getRoleUid()));
        if (partyMemberObject == null)
          continue; 
        RecvRedBagInfo recvRedBagInfo = new RecvRedBagInfo();
        recvRedBagInfo.setName(partyMemberObject.getName());
        recvRedBagInfo.setCoin(fetchRedBagRecord.getCoin());
        recvRedBagInfo.setTime(fetchRedBagRecord.getTime());
        recvRedBagInfoList.add(recvRedBagInfo);
      }  
    return respPartyRecvRegBag;
  }
  
  private PartyRedBag getPartyRedBag(Party party, long redBagGid) {
    if (party == null || party.getRedBagList() == null)
      return null; 
    for (PartyRedBag partyRedBag : party.getRedBagList()) {
      if (partyRedBag.getId() == redBagGid)
        return partyRedBag; 
    } 
    return null;
  }
  
  public void fetchRedBag(Role role, long redBagGid) {
    Party party = this.partyService.getParty(role);
    if (party == null) {
      MessagePusher.notify2Player(role, 393, new Object[0]);
      return;
    } 
    synchronized (party) {
      PartyRedBag partyRedBag = getPartyRedBag(party, redBagGid);
      if (isRedBagFetched(role, partyRedBag)) {
        RespPartyRecvRedBag respPartyRecvRedBag = getRespPartyRecvRedBag(role, redBagGid, false);
        MessagePusher.pushMessage(role, (Message)respPartyRecvRedBag);
        return;
      } 
      String check = checkFetchRedBag(role, partyRedBag);
      if (StringUtils.isNotEmpty(check)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(check));
        return;
      } 
      doFetchRedBag(role, partyRedBag);
      RespPartyRecvRedBag respPartyRecvRegBag = getRespPartyRecvRedBag(role, redBagGid, true);
      MessagePusher.pushMessage(role, (Message)respPartyRecvRegBag);
    } 
  }
  
  private String checkFetchRedBag(Role role, PartyRedBag partyRedBag) {
    if (isRedBaExpire(partyRedBag))
      return I18nIdDataPool.getI18nContent(394); 
    Party party = this.partyService.getParty(role);
    if (party == null)
      return I18nIdDataPool.getI18nContent(393); 
    return null;
  }
  
  private void doFetchRedBag(Role role, PartyRedBag partyRedBag) {
    short remainCount = partyRedBag.getRemainCount();
    if (isRedBagFetched(role, partyRedBag))
      return; 
    if (isRedBaExpire(partyRedBag))
      return; 
    short randomSiver = getRandomSiver(partyRedBag);
    addFetchRedBagRecord(role, partyRedBag, randomSiver);
    Party party = this.partyService.getParty(role);
    addMemberRecvRecord(role, partyRedBag, randomSiver, party);
    PartyMember sendPartyMember = (PartyMember)party.getMembers().get(Long.valueOf(partyRedBag.getSenderUid()));
    String content = I18nIdDataPool.getI18nContent(391, new Object[] { role.getName(), sendPartyMember.getName() });
    sendPartyChat(role, content, false);
    party.save();
  }
  
  private short getRandomSiver(PartyRedBag partyRedBag) {
    short siver = 0;
    return siver;
  }
  
  private void addFetchRedBagRecord(Role role, PartyRedBag partyRedBag, short randomSiver) {
    FetchRedBagRecord fetchRedBagRecord = new FetchRedBagRecord(role.getRoleId().intValue(), randomSiver);
    if (partyRedBag.getFetchRedBagRecordMap() == null)
      partyRedBag.setFetchRedBagRecordMap(new HashMap<>()); 
    partyRedBag.getFetchRedBagRecordMap().put(Long.valueOf(fetchRedBagRecord.getRoleUid()), fetchRedBagRecord);
  }
  
  private void addMemberRecvRecord(Role role, PartyRedBag partyRedBag, short siver, Party party) {}
  
  public void showRedBagRecord(Role role, byte type) {
    Party party = this.partyService.getParty(role);
    PartyMember partyMember = this.partyService.getPartyMember(party, role);
    if (type == 1) {
      RespPartyRedBagRecord respPartyRedBagRecord = getRespSendRedBagRecord(type, partyMember);
      MessagePusher.pushMessage(role, (Message)respPartyRedBagRecord);
    } else if (type == 0) {
      RespPartyRedBagRecord respPartyRedBagRecord = getRespRecvRedBagRecord(type, partyMember);
      MessagePusher.pushMessage(role, (Message)respPartyRedBagRecord);
    } 
  }
  
  private RespPartyRedBagRecord getRespSendRedBagRecord(byte type, PartyMember partyMember) {
    RespPartyRedBagRecord respPartyRedBagRecord = new RespPartyRedBagRecord();
    return respPartyRedBagRecord;
  }
  
  private RespPartyRedBagRecord getRespRecvRedBagRecord(byte type, PartyMember partyMember) {
    RespPartyRedBagRecord respPartyRedBagRecord = new RespPartyRedBagRecord();
    respPartyRedBagRecord.setType(type);
    int total = 0;
    int count = 0;
    List<RecvRedBagInfo> redBagInfoList = new ArrayList<>();
    return respPartyRedBagRecord;
  }
  
  public void checkPartyRedBag(Party party) {
    if (party.getRedBagList() == null)
      return; 
    List<PartyRedBag> removeList = new ArrayList<>();
    if (removeList.size() > 0)
      synchronized (party) {
        for (PartyRedBag partyRedBag : removeList)
          party.getRedBagList().remove(partyRedBag); 
        party.save();
      }  
  }
  
  public void delPartyRedBag(Role role) {
    if (role == null)
      return; 
    Party party = this.partyService.getParty(role);
    if (party == null)
      return; 
    if (party.getRedBagList() == null)
      return; 
    List<PartyRedBag> removeList = new ArrayList<>();
    for (PartyRedBag partyRedBag : party.getRedBagList()) {
      if (partyRedBag.getSenderUid() == role.getRoleId().intValue())
        removeList.add(partyRedBag); 
    } 
    if (removeList.size() > 0)
      synchronized (party) {
        for (PartyRedBag partyRedBag : removeList)
          party.getRedBagList().remove(partyRedBag); 
        party.save();
      }  
  }
}
