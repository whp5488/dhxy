package com.kitty.system.game.party.message;

import com.kitty.system.game.party.message.vo.RecvRedBagInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32843)
public class RespPartyRecvRedBag extends Message
{
    private byte type;
    private int totalCoin;
    private int coin;
    private String redbagGid;
    private String senderName;
    private String msg;
    private int sendTime;
    private short count;
    private byte state;
    private byte isSender;
    private byte isRecv;
    private List<RecvRedBagInfo> recvRedBagInfoList;
    
    public RespPartyRecvRedBag() {
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setTotalCoin(final int totalCoin) {
        this.totalCoin = totalCoin;
    }
    
    public void setCoin(final int coin) {
        this.coin = coin;
    }
    
    public void setRedbagGid(final String redbagGid) {
        this.redbagGid = redbagGid;
    }
    
    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setSendTime(final int sendTime) {
        this.sendTime = sendTime;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setIsSender(final byte isSender) {
        this.isSender = isSender;
    }
    
    public void setIsRecv(final byte isRecv) {
        this.isRecv = isRecv;
    }
    
    public void setRecvRedBagInfoList(final List<RecvRedBagInfo> recvRedBagInfoList) {
        this.recvRedBagInfoList = recvRedBagInfoList;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getTotalCoin() {
        return this.totalCoin;
    }
    
    public int getCoin() {
        return this.coin;
    }
    
    public String getRedbagGid() {
        return this.redbagGid;
    }
    
    public String getSenderName() {
        return this.senderName;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getSendTime() {
        return this.sendTime;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public byte getIsSender() {
        return this.isSender;
    }
    
    public byte getIsRecv() {
        return this.isRecv;
    }
    
    public List<RecvRedBagInfo> getRecvRedBagInfoList() {
        return this.recvRedBagInfoList;
    }
}
