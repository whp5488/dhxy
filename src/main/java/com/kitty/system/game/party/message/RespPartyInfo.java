
package com.kitty.system.game.party.message;

import com.kitty.system.game.party.model.PartyInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40977)
public class RespPartyInfo extends Message
{
    private String order;
    private byte page;
    private byte sort;
    private byte way;
    private List<PartyInfo> list;
    
    public String getOrder() {
        return this.order;
    }
    
    public byte getPage() {
        return this.page;
    }
    
    public byte getSort() {
        return this.sort;
    }
    
    public byte getWay() {
        return this.way;
    }
    
    public List<PartyInfo> getList() {
        return this.list;
    }
    
    public void setOrder(final String order) {
        this.order = order;
    }
    
    public void setPage(final byte page) {
        this.page = page;
    }
    
    public void setSort(final byte sort) {
        this.sort = sort;
    }
    
    public void setWay(final byte way) {
        this.way = way;
    }
    
    public void setList(final List<PartyInfo> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPartyInfo)) {
            return false;
        }
        final RespPartyInfo other = (RespPartyInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$order = this.getOrder();
        final Object other$order = other.getOrder();
        Label_0065: {
            if (this$order == null) {
                if (other$order == null) {
                    break Label_0065;
                }
            }
            else if (this$order.equals(other$order)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getPage() != other.getPage()) {
            return false;
        }
        if (this.getSort() != other.getSort()) {
            return false;
        }
        if (this.getWay() != other.getWay()) {
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
        return other instanceof RespPartyInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $order = this.getOrder();
        result = result * 59 + (($order == null) ? 43 : $order.hashCode());
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSort();
        result = result * 59 + this.getWay();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyInfo(order=" + this.getOrder() + ", page=" + this.getPage() + ", sort=" + this.getSort() + ", way=" + this.getWay() + ", list=" + this.getList() + ")";
    }
}
