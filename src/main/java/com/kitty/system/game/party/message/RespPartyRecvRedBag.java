package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.message.vo.RecvRedBagInfo;
import java.util.List;

@MessageMeta(module = 32843)
public class RespPartyRecvRedBag extends Message {
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
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setTotalCoin(int totalCoin) {
    this.totalCoin = totalCoin;
  }
  
  public void setCoin(int coin) {
    this.coin = coin;
  }
  
  public void setRedbagGid(String redbagGid) {
    this.redbagGid = redbagGid;
  }
  
  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }
  
  public void setMsg(String msg) {
    this.msg = msg;
  }
  
  public void setSendTime(int sendTime) {
    this.sendTime = sendTime;
  }
  
  public void setCount(short count) {
    this.count = count;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public void setIsSender(byte isSender) {
    this.isSender = isSender;
  }
  
  public void setIsRecv(byte isRecv) {
    this.isRecv = isRecv;
  }
  
  public void setRecvRedBagInfoList(List<RecvRedBagInfo> recvRedBagInfoList) {
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
