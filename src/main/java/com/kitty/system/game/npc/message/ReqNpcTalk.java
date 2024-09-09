package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 12344)
public class ReqNpcTalk extends Message
{
    private int npcId;
    private String msg;
    private String para;
    
    public ReqNpcTalk() {
    }
    
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
        ReqNpcTalk other = (ReqNpcTalk)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNpcId() != other.getNpcId()) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        if (!((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null))) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        return (this$para != null) ? this$para.equals(other$para) : (other$para == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqNpcTalk;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpcId();
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqNpcTalk(npcId=" + this.getNpcId() + ", msg=" + this.getMsg() + ", para=" + this.getPara() + ")";
    }
}
