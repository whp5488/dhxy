package com.kitty.system.game.skill.message;

import com.kitty.system.game.vo.FieldByte;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8312)
public class RespSkillEffects extends Message
{
    private List<FieldByte> list;
    
    public RespSkillEffects() {
    }
    
    public List<FieldByte> getList() {
        return this.list;
    }
    
    public void setList(final List<FieldByte> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespSkillEffects)) {
            return false;
        }
        RespSkillEffects other = (RespSkillEffects)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespSkillEffects;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespSkillEffects(list=" + this.getList() + ")";
    }
}
