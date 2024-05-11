
package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20590)
public class ReqSendFriendMsg extends Message
{
    private int att;
    private String msg;
    
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
        final ReqSendFriendMsg other = (ReqSendFriendMsg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAtt() != other.getAtt()) {
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg == null) {
                return true;
            }
        }
        else if (this$msg.equals(other$msg)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSendFriendMsg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAtt();
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSendFriendMsg(att=" + this.getAtt() + ", msg=" + this.getMsg() + ")";
    }
}
