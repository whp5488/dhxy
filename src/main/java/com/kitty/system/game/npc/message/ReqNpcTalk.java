
package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12344)
public class ReqNpcTalk extends Message
{
    private int npcId;
    private String msg;
    private String para;
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqNpcTalk)) {
            return false;
        }
        final ReqNpcTalk other = (ReqNpcTalk)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNpcId() != other.getNpcId()) {
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        Label_0078: {
            if (this$msg == null) {
                if (other$msg == null) {
                    break Label_0078;
                }
            }
            else if (this$msg.equals(other$msg)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        if (this$para == null) {
            if (other$para == null) {
                return true;
            }
        }
        else if (this$para.equals(other$para)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqNpcTalk;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpcId();
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqNpcTalk(npcId=" + this.getNpcId() + ", msg=" + this.getMsg() + ", para=" + this.getPara() + ")";
    }
}
