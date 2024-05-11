
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 16570)
public class ReqPartyModifyMember extends Message
{
    private Integer id;
    private String name;
    private byte job;
    
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
        final ReqPartyModifyMember other = (ReqPartyModifyMember)o;
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
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return this.getJob() == other.getJob();
            }
        }
        else if (this$name.equals(other$name)) {
            return this.getJob() == other.getJob();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqPartyModifyMember;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getJob();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqPartyModifyMember(id=" + this.getId() + ", name=" + this.getName() + ", job=" + this.getJob() + ")";
    }
}
