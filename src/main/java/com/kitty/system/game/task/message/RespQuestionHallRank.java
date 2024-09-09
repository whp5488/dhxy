package com.kitty.system.game.task.message;

import java.util.ArrayList;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53727)
public class RespQuestionHallRank extends Message
{
    private List<String> list;
    
    public RespQuestionHallRank() {
        this.list = new ArrayList<>();
    }
    
    public List<String> getList() {
        return this.list;
    }
    
    public void setList(final List<String> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespQuestionHallRank)) {
            return false;
        }
        RespQuestionHallRank other = (RespQuestionHallRank)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespQuestionHallRank;
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
        return "RespQuestionHallRank(list=" + this.getList() + ")";
    }
}
