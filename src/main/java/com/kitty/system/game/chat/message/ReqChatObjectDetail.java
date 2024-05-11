
package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20701)
public class ReqChatObjectDetail extends Message
{
    private String id;
    private byte type;
    private int ownerRoleId;
    
    public String getId() {
        return this.id;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getOwnerRoleId() {
        return this.ownerRoleId;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setOwnerRoleId(final int ownerRoleId) {
        this.ownerRoleId = ownerRoleId;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqChatObjectDetail)) {
            return false;
        }
        final ReqChatObjectDetail other = (ReqChatObjectDetail)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null) {
            if (other$id == null) {
                return this.getType() == other.getType() && this.getOwnerRoleId() == other.getOwnerRoleId();
            }
        }
        else if (this$id.equals(other$id)) {
            return this.getType() == other.getType() && this.getOwnerRoleId() == other.getOwnerRoleId();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqChatObjectDetail;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getType();
        result = result * 59 + this.getOwnerRoleId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChatObjectDetail(id=" + this.getId() + ", type=" + this.getType() + ", ownerRoleId=" + this.getOwnerRoleId() + ")";
    }
}
