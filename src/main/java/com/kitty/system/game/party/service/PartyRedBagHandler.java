package com.kitty.system.game.party.service;

import com.kitty.system.game.party.message.RespPartyRedBagRecord;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import com.kitty.system.game.party.message.vo.RecvRedBagInfo;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.game.party.model.FetchRedBagRecord;
import com.kitty.system.game.party.message.RespPartyRecvRedBag;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.game.chat.message.RespChat;
import java.util.ArrayList;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.party.message.RespPartyRedBagList;
import com.kitty.system.game.party.model.PartyRedBag;
import com.kitty.system.common.message.RespMsg;
import org.apache.commons.lang3.StringUtils;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.message.RespRedBagSendInfo;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.role.domain.Role;
import com.kitty.logs.LoggerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class PartyRedBagHandler
{
    private Logger logger;
    @Autowired
    private PartyService partyService;
    
    public PartyRedBagHandler() {
        this.logger = LoggerFunction.PARTY.getLogger();
    }
    
    public void sendRedBagSetting(Role role) {
        RespRedBagSendInfo respRedBagSendInfo = this.getRespRedBagSendInfo(role);
        MessagePusher.pushMessage(role, respRedBagSendInfo);
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
            String check = this.checkSendRedBag(role, gold, num);
            if (StringUtils.isNotEmpty(check)) {
                MessagePusher.pushMessage(role, new RespMsg(check));
                return;
            }
            PartyRedBag partyRedBag = this.addPartyRedBag(role, party, gold, num, msg);
            if (partyRedBag == null) {
                return;
            }
            this.addMemberSendRecord(role, party, partyRedBag);
            String formatMsg = String.format(format, Long.valueOf(partyRedBag.getId()), Integer.valueOf(1), party.getId(), msg);
            this.sendPartyChat(role, formatMsg, true);
            RespPartyRedBagList respPartyRedBagList = this.getRespPartyRedBagList(role);
            MessagePusher.pushMessage(role, respPartyRedBagList);
            MessagePusher.notify2Player(role, 390, new Object[0]);
        }
        return;
    }
    
    private String checkSendRedBag(Role role, short gold, short num) {
        int minNum = this.getMinNum((int)gold);
        int maxNum = this.getMaxNum((int)gold);
        if (num < minNum) {
            return I18nIdDataPool.getI18nContent(385, Integer.valueOf(minNum));
        }
        if (num > maxNum) {
            return I18nIdDataPool.getI18nContent(386, Integer.valueOf(maxNum));
        }
        if ((long)role.getGold() < (long)gold) {
            return I18nIdDataPool.getI18nContent(403);
        }
        return null;
    }
    
    private int getMaxNum(int gold) {
        return (int)Math.min(100.0, Math.floor((double)(gold * 3 / 300)));
    }
    
    private int getMinNum(int gold) {
        return (int)Math.max(Math.floor((double)(gold / 600)), 10.0);
    }
    
    private PartyRedBag addPartyRedBag(Role role, Party party, short gold, short num, String msg) {
        if (role == null || party == null) {
            return null;
        }
        PartyRedBag partyRedBag = new PartyRedBag((long)(int)role.getRoleId(), role.getName(), gold, num, msg);
        if (party.getRedBagList() == null) {
            party.setRedBagList(new ArrayList<>());
        }
        party.getRedBagList().add(0, partyRedBag);
        int size = party.getRedBagList().size();
        party.save();
        return partyRedBag;
    }
    
    private void addMemberSendRecord(Role role, Party party, PartyRedBag partyRedBag) {
    }
    
    private RespPartyRedBagList getRespPartyRedBagList(Role role) {
        RespPartyRedBagList respPartyRedBagList = new RespPartyRedBagList();
        return respPartyRedBagList;
    }
    
    private boolean isRedBaExpire(PartyRedBag partyRedBag) {
        return true;
    }
    
    private boolean isRedBagNotExpire(PartyRedBag partyRedBag) {
        return !this.isRedBaExpire(partyRedBag);
    }
    
    private boolean isRedBagSender(Role role, PartyRedBag partyRedBag) {
        return role != null && partyRedBag != null && partyRedBag.getSenderUid() == (long)(int)role.getRoleId();
    }
    
    private boolean isRedBagFetched(Role role, PartyRedBag partyRedBag) {
        return role != null && partyRedBag != null && partyRedBag.getFetchRedBagRecordMap() != null && partyRedBag.getFetchRedBagRecordMap().containsKey(role.getRoleId());
    }
    
    public void sendPartyChat(Role role, String msg, boolean isRoleSend) {
        RespChat respChat = new RespChat();
        respChat.setMsg(msg);
        respChat.setList(isRoleSend ? FieldUtils.getChatList(role) : new ArrayList<>());
        respChat.setRoleName(isRoleSend ? role.getName() : null);
        respChat.setRoleId(isRoleSend ? ((int)role.getRoleId()) : 0);
        respChat.setTime((int)(System.currentTimeMillis() / 1000L));
        respChat.setType(5);
        SenderUtils.pushPartyChatMessage(role.getFaction(), respChat);
    }
    
    public void showRedBagList(Role role) {
        RespPartyRedBagList respPartyRedBagList = this.getRespPartyRedBagList(role);
        MessagePusher.pushMessage(role, respPartyRedBagList);
    }
    
    public void showRedBag(Role role, long redBagGid) {
        RespPartyRecvRedBag respPartyRecvRegBag = this.getRespPartyRecvRedBag(role, redBagGid, false);
        MessagePusher.pushMessage(role, respPartyRecvRegBag);
    }
    
    private RespPartyRecvRedBag getRespPartyRecvRedBag(Role role, long redBagGid, boolean isFetchOper) {
        Party party = this.partyService.getParty(role);
        PartyRedBag partyRedBag = this.getPartyRedBag(party, redBagGid);
        if (partyRedBag == null) {
            return null;
        }
        RespPartyRecvRedBag respPartyRecvRegBag = new RespPartyRecvRedBag();
        FetchRedBagRecord roleFetchRedBagRecord = null;
        if (partyRedBag.getFetchRedBagRecordMap() != null) {
            roleFetchRedBagRecord = (FetchRedBagRecord)partyRedBag.getFetchRedBagRecordMap().get(role.getRoleId());
        }
        byte type = 0;
        if (isFetchOper) {
            type = (byte)((roleFetchRedBagRecord != null) ? 3 : 2);
        }
        else {
            type = 1;
        }
        respPartyRecvRegBag.setType(type);
        respPartyRecvRegBag.setTotalCoin((int)partyRedBag.getSilver());
        respPartyRecvRegBag.setCoin((roleFetchRedBagRecord != null) ? roleFetchRedBagRecord.getCoin() : 0);
        respPartyRecvRegBag.setRedbagGid(String.valueOf(redBagGid));
        PartyMember partyMember = (PartyMember)party.getMembers().get(Long.valueOf(partyRedBag.getSenderUid()));
        respPartyRecvRegBag.setSenderName((partyMember != null) ? partyMember.getName() : "");
        respPartyRecvRegBag.setMsg(partyRedBag.getMsg());
        respPartyRecvRegBag.setSendTime(partyRedBag.getTime());
        respPartyRecvRegBag.setCount(partyRedBag.getCount());
        byte state = (byte)(this.isRedBagNotExpire(partyRedBag) ? 1 : 0);
        respPartyRecvRegBag.setState(state);
        byte isSender = (byte)(this.isRedBagSender(role, partyRedBag) ? 1 : 0);
        respPartyRecvRegBag.setIsSender(isSender);
        byte isRecv = (byte)(this.isRedBagFetched(role, partyRedBag) ? 1 : 0);
        respPartyRecvRegBag.setIsRecv(isRecv);
        List<RecvRedBagInfo> recvRedBagInfoList = new ArrayList<>();
        respPartyRecvRegBag.setRecvRedBagInfoList(recvRedBagInfoList);
        if (partyRedBag.getFetchRedBagRecordMap() != null) {
            for (FetchRedBagRecord fetchRedBagRecord : partyRedBag.getFetchRedBagRecordMap().values()) {
                PartyMember partyMemberObject = (PartyMember)party.getMembers().get(Long.valueOf(fetchRedBagRecord.getRoleUid()));
                if (partyMemberObject == null) {
                    continue;
                }
                RecvRedBagInfo recvRedBagInfo = new RecvRedBagInfo();
                recvRedBagInfo.setName(partyMemberObject.getName());
                recvRedBagInfo.setCoin((int)fetchRedBagRecord.getCoin());
                recvRedBagInfo.setTime(fetchRedBagRecord.getTime());
                recvRedBagInfoList.add(recvRedBagInfo);
            }
            return respPartyRecvRegBag;
        }
        return respPartyRecvRegBag;
    }
    
    private PartyRedBag getPartyRedBag(Party party, long redBagGid) {
        if (party == null || party.getRedBagList() == null) {
            return null;
        }
        for (PartyRedBag partyRedBag : party.getRedBagList()) {
            if (partyRedBag.getId() == redBagGid) {
                return partyRedBag;
            }
            continue;
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
            PartyRedBag partyRedBag = this.getPartyRedBag(party, redBagGid);
            if (this.isRedBagFetched(role, partyRedBag)) {
                RespPartyRecvRedBag respPartyRecvRegBag2 = this.getRespPartyRecvRedBag(role, redBagGid, false);
                MessagePusher.pushMessage(role, respPartyRecvRegBag2);
                return;
            }
            String check = this.checkFetchRedBag(role, partyRedBag);
            if (StringUtils.isNotEmpty(check)) {
                MessagePusher.pushMessage(role, new RespMsg(check));
                return;
            }
            this.doFetchRedBag(role, partyRedBag);
            RespPartyRecvRedBag respPartyRecvRegBag = this.getRespPartyRecvRedBag(role, redBagGid, true);
            MessagePusher.pushMessage(role, respPartyRecvRegBag);
        }
        return;
    }
    
    private String checkFetchRedBag(Role role, PartyRedBag partyRedBag) {
        if (this.isRedBaExpire(partyRedBag)) {
            return I18nIdDataPool.getI18nContent(394);
        }
        Party party = this.partyService.getParty(role);
        if (party == null) {
            return I18nIdDataPool.getI18nContent(393);
        }
        return null;
    }
    
    private void doFetchRedBag(Role role, PartyRedBag partyRedBag) {
        short remainCount = partyRedBag.getRemainCount();
        if (this.isRedBagFetched(role, partyRedBag)) {
            return;
        }
        if (this.isRedBaExpire(partyRedBag)) {
            return;
        }
        short randomSiver = this.getRandomSiver(partyRedBag);
        this.addFetchRedBagRecord(role, partyRedBag, randomSiver);
        Party party = this.partyService.getParty(role);
        this.addMemberRecvRecord(role, partyRedBag, randomSiver, party);
        PartyMember sendPartyMember = (PartyMember)party.getMembers().get(Long.valueOf(partyRedBag.getSenderUid()));
        String content = I18nIdDataPool.getI18nContent(391, role.getName(), sendPartyMember.getName());
        this.sendPartyChat(role, content, false);
        party.save();
        return;
    }
    
    private short getRandomSiver(PartyRedBag partyRedBag) {
        short siver = 0;
        return siver;
    }
    
    private void addFetchRedBagRecord(Role role, PartyRedBag partyRedBag, short randomSiver) {
        FetchRedBagRecord fetchRedBagRecord = new FetchRedBagRecord((long)(int)role.getRoleId(), randomSiver);
        if (partyRedBag.getFetchRedBagRecordMap() == null) {
            partyRedBag.setFetchRedBagRecordMap(new HashMap());
        }
        partyRedBag.getFetchRedBagRecordMap().put(Long.valueOf(fetchRedBagRecord.getRoleUid()), fetchRedBagRecord);
    }
    
    private void addMemberRecvRecord(Role role, PartyRedBag partyRedBag, short siver, Party party) {
    }
    
    public void showRedBagRecord(Role role, byte type) {
        Party party = this.partyService.getParty(role);
        PartyMember partyMember = this.partyService.getPartyMember(party, role);
        if (type == 1) {
            RespPartyRedBagRecord respPartyRedBagRecord = this.getRespSendRedBagRecord(type, partyMember);
            MessagePusher.pushMessage(role, respPartyRedBagRecord);
        }
        else if (type == 0) {
            RespPartyRedBagRecord respPartyRedBagRecord = this.getRespRecvRedBagRecord(type, partyMember);
            MessagePusher.pushMessage(role, respPartyRedBagRecord);
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
        if (party.getRedBagList() == null) {
            return;
        }
        List<PartyRedBag> removeList = new ArrayList<>();
        if (removeList.size() > 0) {
            synchronized (party) {
                for (PartyRedBag partyRedBag : removeList) {
                    party.getRedBagList().remove(partyRedBag);
                }
                party.save();
            }
        }
        return;
    }
    
    public void delPartyRedBag(Role role) {
        if (role == null) {
            return;
        }
        Party party = this.partyService.getParty(role);
        if (party == null) {
            return;
        }
        if (party.getRedBagList() == null) {
            return;
        }
        List<PartyRedBag> removeList = new ArrayList<>();
        for (PartyRedBag partyRedBag : party.getRedBagList()) {
            if (partyRedBag.getSenderUid() == (long)(int)role.getRoleId()) {
                removeList.add(partyRedBag);
            }
            else {
                continue;
            }
        }
        if (removeList.size() > 0) {
            synchronized (party) {
                for (PartyRedBag partyRedBag2 : removeList) {
                    party.getRedBagList().remove(partyRedBag2);
                }
                party.save();
            }
        }
        return;
    }
}
