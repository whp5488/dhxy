package com.kitty.system.game.skill.message;

import com.kitty.system.game.skill.message.vo.Skill;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32747)
public class RespUpdateSkill extends Message
{
    private int id;
    private List<Skill> list;
    
    public RespUpdateSkill() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public List<Skill> getList() {
        return this.list;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setList(final List<Skill> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespUpdateSkill)) {
            return false;
        }
        RespUpdateSkill other = (RespUpdateSkill)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespUpdateSkill;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpdateSkill(id=" + this.getId() + ", list=" + this.getList() + ")";
    }
}
