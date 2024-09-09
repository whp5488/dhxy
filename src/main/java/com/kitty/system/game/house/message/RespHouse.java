package com.kitty.system.game.house.message;

import java.util.ArrayList;
import com.kitty.system.game.house.message.vo.HouseEasy;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20578)
public class RespHouse extends Message
{
    private List<HouseEasy> list;
    
    public List<HouseEasy> getList() {
        return this.list;
    }
    
    public void setList(final List<HouseEasy> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespHouse)) {
            return false;
        }
        RespHouse other = (RespHouse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespHouse;
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
        return "RespHouse(list=" + this.getList() + ")";
    }
    
    public RespHouse(final List<HouseEasy> list) {
        this.list = new ArrayList<>();
        this.list = list;
    }
    
    public RespHouse() {
        this.list = new ArrayList<>();
    }
}
