package com.kitty.system.game.onlinemall.message;

import com.kitty.system.game.onlinemall.domain.OnlineMallInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65499)
public class RespOnlineMall extends Message
{
    private int type;
    private List<OnlineMallInfo> list;
    
    public RespOnlineMall() {
        this.type = 0;
    }
    
    public int getType() {
        return this.type;
    }
    
    public List<OnlineMallInfo> getList() {
        return this.list;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setList(final List<OnlineMallInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespOnlineMall)) {
            return false;
        }
        RespOnlineMall other = (RespOnlineMall)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespOnlineMall;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespOnlineMall(type=" + this.getType() + ", list=" + this.getList() + ")";
    }
}
