package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20701)
public class ReqChatObjectDetail extends Message
{
    private String id;
    private byte type;
    private int ownerRoleId;
    
    public ReqChatObjectDetail() {
    }
    
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
        ReqChatObjectDetail other = (ReqChatObjectDetail)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        return ((this$id != null) ? this$id.equals(other$id) : (other$id == null)) && this.getType() == other.getType() && this.getOwnerRoleId() == other.getOwnerRoleId();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqChatObjectDetail;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getType();
        result = result * 59 + this.getOwnerRoleId();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqChatObjectDetail(id=" + this.getId() + ", type=" + (int)this.getType() + ", ownerRoleId=" + this.getOwnerRoleId() + ")";
    }
}
