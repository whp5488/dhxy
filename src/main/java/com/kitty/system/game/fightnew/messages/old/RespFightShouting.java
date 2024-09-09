package com.kitty.system.game.fightnew.messages.old;

public class RespFightShouting
{
    private int id;
    private short channel;
    private String msg;
    private int show_time;
    private int msgDelayTime;
    
    public RespFightShouting() {
        this.channel = 14;
        this.show_time = 2000;
    }
    
    public int getId() {
        return this.id;
    }
    
    public short getChannel() {
        return this.channel;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getShow_time() {
        return this.show_time;
    }
    
    public int getMsgDelayTime() {
        return this.msgDelayTime;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setChannel(final short channel) {
        this.channel = channel;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setShow_time(final int show_time) {
        this.show_time = show_time;
    }
    
    public void setMsgDelayTime(final int msgDelayTime) {
        this.msgDelayTime = msgDelayTime;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFightShouting)) {
            return false;
        }
        RespFightShouting other = (RespFightShouting)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getChannel() != other.getChannel()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        return ((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null)) && this.getShow_time() == other.getShow_time() && this.getMsgDelayTime() == other.getMsgDelayTime();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFightShouting;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getChannel();
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + this.getShow_time();
        result = result * 59 + this.getMsgDelayTime();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFightShouting(id=" + this.getId() + ", channel=" + (int)this.getChannel() + ", msg=" + this.getMsg() + ", show_time=" + this.getShow_time() + ", msgDelayTime=" + this.getMsgDelayTime() + ")";
    }
}
