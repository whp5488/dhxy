
package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32984)
public class ReqAutoFightSetData extends Message
{
    private int id;
    private int action;
    private int type;
    private int skillId;
    private int born;
    
    public ReqAutoFightSetData() {
        this.action = 1;
        this.born = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getAction() {
        return this.action;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getBorn() {
        return this.born;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setAction(final int action) {
        this.action = action;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setBorn(final int born) {
        this.born = born;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAutoFightSetData)) {
            return false;
        }
        final ReqAutoFightSetData other = (ReqAutoFightSetData)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getAction() == other.getAction() && this.getType() == other.getType() && this.getSkillId() == other.getSkillId() && this.getBorn() == other.getBorn();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAutoFightSetData;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getAction();
        result = result * 59 + this.getType();
        result = result * 59 + this.getSkillId();
        result = result * 59 + this.getBorn();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAutoFightSetData(id=" + this.getId() + ", action=" + this.getAction() + ", type=" + this.getType() + ", skillId=" + this.getSkillId() + ", born=" + this.getBorn() + ")";
    }
}
