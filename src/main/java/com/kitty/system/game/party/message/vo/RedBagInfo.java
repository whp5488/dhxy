
package com.kitty.system.game.party.message.vo;

public class RedBagInfo
{
    private String redBgGid;
    private String senderName;
    private String msg;
    private int time;
    private short count;
    private byte state;
    private byte isSender;
    private byte isRecv;
    
    public String getRedBgGid() {
        return this.redBgGid;
    }
    
    public String getSenderName() {
        return this.senderName;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getTime() {
        return this.time;
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
    
    public void setRedBgGid(final String redBgGid) {
        this.redBgGid = redBgGid;
    }
    
    public void setSenderName(final String senderName) {
        this.senderName = senderName;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setTime(final int time) {
        this.time = time;
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
}
