
package com.kitty.system.game.house.message;

import java.beans.ConstructorProperties;
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
        final RespHouse other = (RespHouse)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespHouse;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespHouse(list=" + this.getList() + ")";
    }
    
    @ConstructorProperties({ "list" })
    public RespHouse(final List<HouseEasy> list) {
        this.list = new ArrayList<HouseEasy>();
        this.list = list;
    }
    
    public RespHouse() {
        this.list = new ArrayList<HouseEasy>();
    }
}
