package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20590)
public class ReqSendFriendMsg extends Message
{
    private int att;
    private String msg;
    
    public ReqSendFriendMsg() {
    }
    
    public int getAtt() {
        return this.att;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setAtt(final int att) {
        this.att = att;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSendFriendMsg)) {
            return false;
        }
        ReqSendFriendMsg other = (ReqSendFriendMsg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAtt() != other.getAtt()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        return (this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSendFriendMsg;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAtt();
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSendFriendMsg(att=" + this.getAtt() + ", msg=" + this.getMsg() + ")";
    }
}
