package com.kitty.system.game.enter.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65531)
public class RespMapTransmission extends Message
{
    private Integer add_exit;
    private List<MapTransmission> list;
    
    public RespMapTransmission() {
        this.add_exit = Integer.valueOf(1);
    }
    
    public Integer getAdd_exit() {
        return this.add_exit;
    }
    
    public List<MapTransmission> getList() {
        return this.list;
    }
    
    public void setAdd_exit(final Integer add_exit) {
        this.add_exit = add_exit;
    }
    
    public void setList(final List<MapTransmission> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMapTransmission)) {
            return false;
        }
        RespMapTransmission other = (RespMapTransmission)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$add_exit = this.getAdd_exit();
        Object other$add_exit = other.getAdd_exit();
        if (!((this$add_exit != null) ? this$add_exit.equals(other$add_exit) : (other$add_exit == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMapTransmission;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $add_exit = this.getAdd_exit();
        result = result * 59 + (($add_exit == null) ? 43 : $add_exit.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMapTransmission(add_exit=" + this.getAdd_exit() + ", list=" + this.getList() + ")";
    }
}
