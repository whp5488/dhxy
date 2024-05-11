
package com.kitty.system.game.friend.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45214)
public class ReqAlterFriend extends Message
{
    private String id;
    private String remark;
    private int pal;
    
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
        final ReqAlterFriend other = (ReqAlterFriend)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$remark = this.getRemark();
        final Object other$remark = other.getRemark();
        if (this$remark == null) {
            if (other$remark == null) {
                return this.getPal() == other.getPal();
            }
        }
        else if (this$remark.equals(other$remark)) {
            return this.getPal() == other.getPal();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAlterFriend;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $remark = this.getRemark();
        result = result * 59 + (($remark == null) ? 43 : $remark.hashCode());
        result = result * 59 + this.getPal();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAlterFriend(id=" + this.getId() + ", remark=" + this.getRemark() + ", pal=" + this.getPal() + ")";
    }
}
