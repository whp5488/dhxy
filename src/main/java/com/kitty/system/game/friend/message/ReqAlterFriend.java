package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45214)
public class ReqAlterFriend extends Message
{
    private String id;
    private String remark;
    private int pal;
    
    public ReqAlterFriend() {
    }
    
    public String getId() {
        return this.id;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public int getPal() {
        return this.pal;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setRemark(final String remark) {
        this.remark = remark;
    }
    
    public void setPal(final int pal) {
        this.pal = pal;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAlterFriend)) {
            return false;
        }
        ReqAlterFriend other = (ReqAlterFriend)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$remark = this.getRemark();
        Object other$remark = other.getRemark();
        return ((this$remark != null) ? this$remark.equals(other$remark) : (other$remark == null)) && this.getPal() == other.getPal();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAlterFriend;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $remark = this.getRemark();
        result = result * 59 + (($remark == null) ? 43 : $remark.hashCode());
        result = result * 59 + this.getPal();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAlterFriend(id=" + this.getId() + ", remark=" + this.getRemark() + ", pal=" + this.getPal() + ")";
    }
}
