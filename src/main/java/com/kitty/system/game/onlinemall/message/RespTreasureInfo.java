package com.kitty.system.game.onlinemall.message;

import java.util.ArrayList;
import com.kitty.mina.cache.Deploy;
import com.kitty.system.game.onlinemall.message.vo.WeekTreasureInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8411)
public class RespTreasureInfo extends Message
{
    private int count;
    private int original;
    private int price;
    private List<WeekTreasureInfo> list;
    
    public RespTreasureInfo() {
        this.original = 8800;
        this.price = Deploy.treasurePrice;
        this.list = new ArrayList<>();
    }
    
    public int getCount() {
        return this.count;
    }
    
    public int getOriginal() {
        return this.original;
    }
    
    public int getPrice() {
        return this.price;
    }
    
    public List<WeekTreasureInfo> getList() {
        return this.list;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public void setOriginal(final int original) {
        this.original = original;
    }
    
    public void setPrice(final int price) {
        this.price = price;
    }
    
    public void setList(final List<WeekTreasureInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTreasureInfo)) {
            return false;
        }
        RespTreasureInfo other = (RespTreasureInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getCount() != other.getCount()) {
            return false;
        }
        if (this.getOriginal() != other.getOriginal()) {
            return false;
        }
        if (this.getPrice() != other.getPrice()) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTreasureInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getCount();
        result = result * 59 + this.getOriginal();
        result = result * 59 + this.getPrice();
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTreasureInfo(count=" + this.getCount() + ", original=" + this.getOriginal() + ", price=" + this.getPrice() + ", list=" + this.getList() + ")";
    }
}
