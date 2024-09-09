package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 36889)
public class RespFightCommandInfo extends Message
{
    private int id;
    private int type;
    private int skillId;
    private int action;
    private int born;
    
    public RespFightCommandInfo() {
        this.type = 0;
        this.skillId = 0;
        this.action = 1;
        this.born = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getAction() {
        return this.action;
    }
    
    public int getBorn() {
        return this.born;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setAction(final int action) {
        this.action = action;
    }
    
    public void setBorn(final int born) {
        this.born = born;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFightCommandInfo)) {
            return false;
        }
        RespFightCommandInfo other = (RespFightCommandInfo)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getType() == other.getType() && this.getSkillId() == other.getSkillId() && this.getAction() == other.getAction() && this.getBorn() == other.getBorn();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFightCommandInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getType();
        result = result * 59 + this.getSkillId();
        result = result * 59 + this.getAction();
        result = result * 59 + this.getBorn();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFightCommandInfo(id=" + this.getId() + ", type=" + this.getType() + ", skillId=" + this.getSkillId() + ", action=" + this.getAction() + ", born=" + this.getBorn() + ")";
    }
}
