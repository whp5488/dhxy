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
    
    public RespColumbia() {
    }
    
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
        RespColumbia other = (RespColumbia)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$para = this.getPara();
        Object other$para = other.getPara();
        if (!((this$para != null) ? this$para.equals(other$para) : (other$para == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        Object this$price = this.getPrice();
        Object other$price = other.getPrice();
        return (this$price != null) ? this$price.equals(other$price) : (other$price == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespColumbia;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $para = this.getPara();
        result = result * 59 + (($para == null) ? 43 : $para.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespColumbia(para=" + this.getPara() + ", list=" + this.getList() + ", price=" + this.getPrice() + ")";
    }
}
