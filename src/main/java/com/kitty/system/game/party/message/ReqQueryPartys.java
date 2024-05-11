
package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32782)
public class ReqQueryPartys extends Message
{
    private String type;
    private String para;
    private byte page;
    private byte size;
    private byte sort;
    private byte way;
    
    public ReqQueryPartys() {
        this.page = 0;
        this.size = 8;
        this.sort = 4;
        this.way = 0;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public byte getPage() {
        return this.page;
    }
    
    public byte getSize() {
        return this.size;
    }
    
    public byte getSort() {
        return this.sort;
    }
    
    public byte getWay() {
        return this.way;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setPage(final byte page) {
        this.page = page;
    }
    
    public void setSize(final byte size) {
        this.size = size;
    }
    
    public void setSort(final byte sort) {
        this.sort = sort;
    }
    
    public void setWay(final byte way) {
        this.way = way;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqQueryPartys)) {
            return false;
        }
        final ReqQueryPartys other = (ReqQueryPartys)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0065: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0065;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        if (this$para == null) {
            if (other$para == null) {
                return this.getPage() == other.getPage() && this.getSize() == other.getSize() && this.getSort() == other.getSort() && this.getWay() == other.getWay();
            }
        }
        else if (this$para.equals(other$para)) {
            return this.getPage() == other.getPage() && this.getSize() == other.getSize() && this.getSort() == other.getSort() && this.getWay() == other.getWay();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqQueryPartys;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        result = result * 59 + this.getPage();
        result = result * 59 + this.getSize();
        result = result * 59 + this.getSort();
        result = result * 59 + this.getWay();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqQueryPartys(type=" + this.getType() + ", para=" + this.getPara() + ", page=" + this.getPage() + ", size=" + this.getSize() + ", sort=" + this.getSort() + ", way=" + this.getWay() + ")";
    }
}
