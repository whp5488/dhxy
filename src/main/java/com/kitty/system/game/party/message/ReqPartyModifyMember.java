package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16570)
public class ReqPartyModifyMember extends Message
{
    private Integer id;
    private String name;
    private byte job;
    
    public ReqPartyModifyMember() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getJob() {
        return this.job;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setJob(final byte job) {
        this.job = job;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqPartyModifyMember)) {
            return false;
        }
        ReqPartyModifyMember other = (ReqPartyModifyMember)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getJob() == other.getJob();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyModifyMember;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getJob();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyModifyMember(id=" + this.getId() + ", name=" + this.getName() + ", job=" + (int)this.getJob() + ")";
    }
}
