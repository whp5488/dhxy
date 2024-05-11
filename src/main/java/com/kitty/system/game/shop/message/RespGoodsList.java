
package com.kitty.system.game.shop.message;

import com.kitty.system.game.shop.domain.ItemInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65503)
public class RespGoodsList extends Message
{
    private int type;
    private int id;
    private List<ItemInfo> list;
    
    public RespGoodsList() {
        this.type = 0;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getId() {
        return this.id;
    }
    
    public List<ItemInfo> getList() {
        return this.list;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setList(final List<ItemInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespGoodsList)) {
            return false;
        }
        final RespGoodsList other = (RespGoodsList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getId() != other.getId()) {
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
        return other instanceof RespGoodsList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getId();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespGoodsList(type=" + this.getType() + ", id=" + this.getId() + ", list=" + this.getList() + ")";
    }
}
