
package com.kitty.system.game.market.message;

import com.kitty.system.game.market.message.vo.Columbia;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32901)
public class RespColumbia extends Message
{
    private String para;
    private List<Columbia> list;
    private String price;
    
    public String getPara() {
        return this.para;
    }
    
    public List<Columbia> getList() {
        return this.list;
    }
    
    public String getPrice() {
        return this.price;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    public void setList(final List<Columbia> list) {
        this.list = list;
    }
    
    public void setPrice(final String price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespColumbia)) {
            return false;
        }
        final RespColumbia other = (RespColumbia)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$para = this.getPara();
        final Object other$para = other.getPara();
        Label_0065: {
            if (this$para == null) {
                if (other$para == null) {
                    break Label_0065;
                }
            }
            else if (this$para.equals(other$para)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0102: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0102;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        if (this$price == null) {
            if (other$price == null) {
                return true;
            }
        }
        else if (this$price.equals(other$price)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespColumbia;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespColumbia(para=" + this.getPara() + ", list=" + this.getList() + ", price=" + this.getPrice() + ")";
    }
}
