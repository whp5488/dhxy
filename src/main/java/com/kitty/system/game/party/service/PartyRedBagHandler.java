
package com.kitty.system.game.party.service;

import com.kitty.system.game.party.message.RespPartyRedBagRecord;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;
import com.kitty.system.game.party.message.vo.RecvRedBagInfo;
import com.kitty.system.game.party.model.PartyMember;
import com.kitty.system.game.party.model.FetchRedBagRecord;
import com.kitty.system.game.party.message.RespPartyRecvRedBag;
import com.kitty.system.common.utils.SenderUtils;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.common.utils.FieldUtils;
import com.kitty.system.game.chat.message.RespChat;
import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.i18n.I18nIdDataPool;
import com.kitty.system.game.party.message.RespPartyRedBagList;
import com.kitty.system.game.party.model.PartyRedBag;
import com.kitty.system.common.message.RespMsg;
import org.apache.commons.lang3.StringUtils;
import com.kitty.system.game.party.model.Party;
import com.kitty.system.game.party.message.RespRedBagSendInfo;
import com.kitty.mina.message.Message;
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
    
    public void sendRedBagSetting(final Role role) {
        final RespRedBagSendInfo respRedBagSendInfo = this.getRespRedBagSendInfo(role);
        MessagePusher.pushMessage(role, respRedBagSendInfo);
    }
    
    private RespRedBagSendInfo getRespRedBagSendInfo(final Role role) {
        final RespRedBagSendInfo respRedBagSendInfo = new RespRedBagSendInfo();
        final Party party = this.partyService.getParty(role);
        final short memberCount = this.partyService.getMemberCount(party);
        respRedBagSendInfo.setMemberCount(memberCount);
        return respRedBagSendInfo;
    }
    
    public void sendRedBag(final Role role, final short gold, final short num, final String msg, final String format) {
        final Party party = this.partyService.getParty(role);
        if (party == null) {
            MessagePusher.notify2Player(role, 319);
            return;
        }
        synchronized (party) {
            final String check = this.checkSendRedBag(role, gold, num);
            if (StringUtils.isNotEmpty(check)) {
                MessagePusher.pushMessage(role, new RespMsg(check));
                return;
            }
            final PartyRedBag partyRedBag = this.addPartyRedBag(role, party, gold, num, msg);
            if (partyRedBag == null) {
                return;
            }
            this.addMemberSendRecord(role, party, partyRedBag);
            final String formatMsg = String.format(format, partyRedBag.getId(), 1, party.getId(), msg);
            this.sendPartyChat(role, formatMsg, true);
            final RespPartyRedBagList respPartyRedBagList = this.getRespPartyRedBagList(role);
            MessagePusher.pushMessage(role, respPartyRedBagList);
            MessagePusher.notify2Player(role, 390);
        }
    }
    
    private String checkSendRedBag(final Role role, final short gold, final short num) {
        final int minNum = this.getMinNum(gold);
        final int maxNum = this.getMaxNum(gold);
        if (num < minNum) {
            return I18nIdDataPool.getI18nContent(385, minNum);
        }
        if (num > maxNum) {
            return I18nIdDataPool.getI18nContent(386, maxNum);
        }
        if (role.getGold() < gold) {
            return I18nIdDataPool.getI18nContent(403);
        }
        return null;
    }
    
    private int getMaxNum(final int gold) {
        return (int)Math.min(100.0, Math.floor(gold * 3 / 300));
    }
    
    private int getMinNum(final int gold) {
        return (int)Math.max(Math.floor(gold / 600), 10.0);
    }
    
    private PartyRedBag addPartyRedBag(final Role role, final Party party, final short gold, final short num, final String msg) {
        if (role == null || party == null) {
            return null;
        }
        final PartyRedBag partyRedBag = new PartyRedBag(role.getRoleId(), role.getName(), gold, num, msg);
        if (party.getRedBagList() == null) {
            party.setRedBagList(new ArrayList<>());
        }
        party.getRedBagList().add(0, partyRedBag);
        final int size = party.getRedBagList().size();
        party.save();
        return partyRedBag;
    }
    
    private void addMemberSendRecord(final Role role, final Party party, final PartyRedBag partyRedBag) {
    }
    
    private RespPartyRedBagList getRespPartyRedBagList(final Role role) {
        final RespPartyRedBagList respPartyRedBagList = new RespPartyRedBagList();
        return respPartyRedBagList;
    }
    
    private boolean isRedBaExpire(final PartyRedBag partyRedBag) {
        return true;
    }
    
    private boolean isRedBagNotExpire(final PartyRedBag partyRedBag) {
        return !this.isRedBaExpire(partyRedBag);
    }
    
    private boolean isRedBagSender(final Role role, final PartyRedBag partyRedBag) {
        return role != null && partyRedBag != null && partyRedBag.getSenderUid() == role.getRoleId();
    }
    
    private boolean isRedBagFetched(final Role role, final PartyRedBag partyRedBag) {
        return role != null && partyRedBag != null && partyRedBag.getFetchRedBagRecordMap() != null && partyRedBag.getFetchRedBagRecordMap().containsKey(role.getRoleId());
    }
    
    public void sendPartyChat(final Role role, final String msg, final boolean isRoleSend) {
        final RespChat respChat = new RespChat();
        respChat.setMsg(msg);
        respChat.setList(isRoleSend ? FieldUtils.getChatList(role) : new ArrayList<FiedValue>());
        respChat.setRoleName(isRoleSend ? role.getName() : null);
        respChat.setRoleId(isRoleSend ? role.getRoleId() : 0);
        respChat.setTime((int)(System.currentTimeMillis() / 1000L));
        respChat.setType(5);
        SenderUtils.pushPartyChatMessage(role.getFaction(), respChat);
    }
    
    public void showRedBagList(final Role role) {
        final RespPartyRedBagList respPartyRedBagList = this.getRespPartyRedBagList(role);
        MessagePusher.pushMessage(role, respPartyRedBagList);
    }
    
    public void showRedBag(final Role role, final long redBagGid) {
        final RespPartyRecvRedBag respPartyRecvRegBag = this.getRespPartyRecvRedBag(role, redBagGid, false);
        MessagePusher.pushMessage(role, respPartyRecvRegBag);
    }
    
    private RespPartyRecvRedBag getRespPartyRecvRedBag(final Role role, final long redBagGid, final boolean isFetchOper) {
        final Party party = this.partyService.getParty(role);
        final PartyRedBag partyRedBag = this.getPartyRedBag(party, redBagGid);
        if (partyRedBag == null) {
            return null;
        }
        final RespPartyRecvRedBag respPartyRecvRegBag = new RespPartyRecvRedBag();
        FetchRedBagRecord roleFetchRedBagRecord = null;
        if (partyRedBag.getFetchRedBagRecordMap() != null) {
            roleFetchRedBagRecord = partyRedBag.getFetchRedBagRecordMap().get(role.getRoleId());
        }
        byte type = 0;
        if (isFetchOper) {
            type = (byte)((roleFetchRedBagRecord != null) ? 3 : 2);
        }
        else {
            type = 1;
        }
        respPartyRecvRegBag.setType(type);
        respPartyRecvRegBag.setTotalCoin(partyRedBag.getSilver());
        respPartyRecvRegBag.setCoin((roleFetchRedBagRecord != null) ? roleFetchRedBagRecord.getCoin() : 0);
        respPartyRecvRegBag.setRedbagGid(String.valueOf(redBagGid));
        final PartyMember partyMember = party.getMembers().get(partyRedBag.getSenderUid());
        respPartyRecvRegBag.setSenderName((partyMember != null) ? partyMember.getName() : "");
        respPartyRecvRegBag.setMsg(partyRedBag.getMsg());
        respPartyRecvRegBag.setSendTime(partyRedBag.getTime());
        respPartyRecvRegBag.setCount(partyRedBag.getCount());
        final byte state = (byte)(this.isRedBagNotExpire(partyRedBag) ? 1 : 0);
        respPartyRecvRegBag.setState(state);
        final byte isSender = (byte)(this.isRedBagSender(role, partyRedBag) ? 1 : 0);
        respPartyRecvRegBag.setIsSender(isSender);
        final byte isRecv = (byte)(this.isRedBagFetched(role, partyRedBag) ? 1 : 0);
        respPartyRecvRegBag.setIsRecv(isRecv);
        final List<RecvRedBagInfo> recvRedBagInfoList = new ArrayList<RecvRedBagInfo>();
        respPartyRecvRegBag.setRecvRedBagInfoList(recvRedBagInfoList);
        if (partyRedBag.getFetchRedBagRecordMap() != null) {
            for (final FetchRedBagRecord fetchRedBagRecord : partyRedBag.getFetchRedBagRecordMap().values()) {
                final PartyMember partyMemberObject = party.getMembers().get(fetchRedBagRecord.getRoleUid());
                if (partyMemberObject == null) {
                    continue;
                }
                final RecvRedBagInfo recvRedBagInfo = new RecvRedBagInfo();
                recvRedBagInfo.setName(partyMemberObject.getName());
                recvRedBagInfo.setCoin(fetchRedBagRecord.getCoin());
                recvRedBagInfo.setTime(fetchRedBagRecord.getTime());
                recvRedBagInfoList.add(recvRedBagInfo);
            }
        }
        return respPartyRecvRegBag;
    }
    
    private PartyRedBag getPartyRedBag(final Party party, final long redBagGid) {
        if (party == null || party.getRedBagList() == null) {
            return null;
        }
        for (final PartyRedBag partyRedBag : party.getRedBagList()) {
            if (partyRedBag.getId() == redBagGid) {
                return partyRedBag;
            }
        }
        return null;
    }
    
    public void fetchRedBag(final Role role, final long redBagGid) {
        final Party party = this.partyService.getParty(role);
        if (party == null) {
            MessagePusher.notify2Player(role, 393);
            return;
        }
        synchronized (party) {
            final PartyRedBag partyRedBag = this.getPartyRedBag(party, redBagGid);
            if (this.isRedBagFetched(role, partyRedBag)) {
                final RespPartyRecvRedBag respPartyRecvRegBag = this.getRespPartyRecvRedBag(role, redBagGid, false);
                MessagePusher.pushMessage(role, respPartyRecvRegBag);
                return;
            }
            final String check = this.checkFetchRedBag(role, partyRedBag);
            if (StringUtils.isNotEmpty(check)) {
                MessagePusher.pushMessage(role, new RespMsg(check));
                return;
            }
            this.doFetchRedBag(role, partyRedBag);
            final RespPartyRecvRedBag respPartyRecvRegBag2 = this.getRespPartyRecvRedBag(role, redBagGid, true);
            MessagePusher.pushMessage(role, respPartyRecvRegBag2);
        }
    }
    
    private String checkFetchRedBag(final Role role, final PartyRedBag partyRedBag) {
        if (this.isRedBaExpire(partyRedBag)) {
            return I18nIdDataPool.getI18nContent(394);
        }
        final Party party = this.partyService.getParty(role);
        if (party == null) {
            return I18nIdDataPool.getI18nContent(393);
        }
        return null;
    }
    
    private void doFetchRedBag(final Role role, final PartyRedBag partyRedBag) {
        final short remainCount = partyRedBag.getRemainCount();
        if (this.isRedBagFetched(role, partyRedBag)) {
            return;
        }
        if (this.isRedBaExpire(partyRedBag)) {
            return;
        }
        final short randomSiver = this.getRandomSiver(partyRedBag);
        this.addFetchRedBagRecord(role, partyRedBag, randomSiver);
        final Party party = this.partyService.getParty(role);
        this.addMemberRecvRecord(role, partyRedBag, randomSiver, party);
        final PartyMember sendPartyMember = party.getMembers().get(partyRedBag.getSenderUid());
        final String content = I18nIdDataPool.getI18nContent(391, role.getName(), sendPartyMember.getName());
        this.sendPartyChat(role, content, false);
        party.save();
    }
    
    private short getRandomSiver(final PartyRedBag partyRedBag) {
        final short siver = 0;
        return siver;
    }
    
    private void addFetchRedBagRecord(final Role role, final PartyRedBag partyRedBag, final short randomSiver) {
        final FetchRedBagRecord fetchRedBagRecord = new FetchRedBagRecord(role.getRoleId(), randomSiver);
        if (partyRedBag.getFetchRedBagRecordMap() == null) {
            partyRedBag.setFetchRedBagRecordMap(new HashMap<Long, FetchRedBagRecord>());
        }
        partyRedBag.getFetchRedBagRecordMap().put(fetchRedBagRecord.getRoleUid(), fetchRedBagRecord);
    }
    
    private void addMemberRecvRecord(final Role role, final PartyRedBag partyRedBag, final short siver, final Party party) {
    }
    
    public void showRedBagRecord(final Role role, final byte type) {
        final Party party = this.partyService.getParty(role);
        final PartyMember partyMember = this.partyService.getPartyMember(party, role);
        if (type == 1) {
            final RespPartyRedBagRecord respPartyRedBagRecord = this.getRespSendRedBagRecord(type, partyMember);
            MessagePusher.pushMessage(role, respPartyRedBagRecord);
        }
        else if (type == 0) {
            final RespPartyRedBagRecord respPartyRedBagRecord = this.getRespRecvRedBagRecord(type, partyMember);
            MessagePusher.pushMessage(role, respPartyRedBagRecord);
        }
    }
    
    private RespPartyRedBagRecord getRespSendRedBagRecord(final byte type, final PartyMember partyMember) {
        final RespPartyRedBagRecord respPartyRedBagRecord = new RespPartyRedBagRecord();
        return respPartyRedBagRecord;
    }
    
    private RespPartyRedBagRecord getRespRecvRedBagRecord(final byte type, final PartyMember partyMember) {
        final RespPartyRedBagRecord respPartyRedBagRecord = new RespPartyRedBagRecord();
        respPartyRedBagRecord.setType(type);
        final int total = 0;
        final int count = 0;
        final List<RecvRedBagInfo> redBagInfoList = new ArrayList<RecvRedBagInfo>();
        return respPartyRedBagRecord;
    }
    
    public void checkPartyRedBag(final Party party) {
        if (party.getRedBagList() == null) {
            return;
        }
        final List<PartyRedBag> removeList = new ArrayList<PartyRedBag>();
        if (removeList.size() > 0) {
            synchronized (party) {
                for (final PartyRedBag partyRedBag : removeList) {
                    party.getRedBagList().remove(partyRedBag);
                }
                party.save();
            }
        }
    }
    
    public void delPartyRedBag(final Role role) {
        if (role == null) {
            return;
        }
        final Party party = this.partyService.getParty(role);
        if (party == null) {
            return;
        }
        if (party.getRedBagList() == null) {
            return;
        }
        final List<PartyRedBag> removeList = new ArrayList<PartyRedBag>();
        for (final PartyRedBag partyRedBag : party.getRedBagList()) {
            if (partyRedBag.getSenderUid() == role.getRoleId()) {
                removeList.add(partyRedBag);
            }
        }
        if (removeList.size() > 0) {
            synchronized (party) {
                for (final PartyRedBag partyRedBag2 : removeList) {
                    party.getRedBagList().remove(partyRedBag2);
                }
                party.save();
            }
        }
    }
}
