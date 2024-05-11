
package com.kitty.system.game.vo.init;

import java.io.Serializable;

public class Item implements Serializable
{
    private Integer item;
    private Integer num;
    private Integer others;
    
    public Item(final Integer item, final Integer num) {
        this.others = 0;
        this.item = item;
        this.num = num;
    }
    
    public Item(final Integer item, final Integer num, final Integer others) {
        this.others = 0;
        this.item = item;
        this.num = num;
        this.others = others;
    }
    
    public Item() {
        this.others = 0;
    }
    
    public Integer getItem() {
        return this.item;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getOthers() {
        return this.others;
    }
    
    public void setItem(final Integer item) {
        this.item = item;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setOthers(final Integer others) {
        this.others = others;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        final Item other = (Item)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$item = this.getItem();
        final Object other$item = other.getItem();
        Label_0065: {
            if (this$item == null) {
                if (other$item == null) {
                    break Label_0065;
                }
            }
            else if (this$item.equals(other$item)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$num = this.getNum();
        final Object other$num = other.getNum();
        Label_0102: {
            if (this$num == null) {
                if (other$num == null) {
                    break Label_0102;
                }
            }
            else if (this$num.equals(other$num)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$others = this.getOthers();
        final Object other$others = other.getOthers();
        if (this$others == null) {
            if (other$others == null) {
                return true;
            }
        }
        else if (this$others.equals(other$others)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Item;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $item = this.getItem();
        result = result * 59 + (($item == null) ? 43 : $item.hashCode());
        final Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        final Object $others = this.getOthers();
        result = result * 59 + (($others == null) ? 43 : $others.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Item(item=" + this.getItem() + ", num=" + this.getNum() + ", others=" + this.getOthers() + ")";
    }
}
