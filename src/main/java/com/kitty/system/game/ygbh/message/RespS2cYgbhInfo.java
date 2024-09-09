
package com.kitty.system.game.ygbh.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.ygbh.domain.ListA;
import com.kitty.system.game.ygbh.domain.ListB;

import java.util.Objects;

@MessageMeta(module = 8081)
public class RespS2cYgbhInfo extends Message
{
    private ListA listA;
    private ListB listB;
    private int priceA;
    private int priceB;
    
    public RespS2cYgbhInfo() {
    }
    
    public RespS2cYgbhInfo(final int priceA, final int priceB, final ListA listA, final ListB listB) {
        this.listA = listA;
        this.listB = listB;
        this.priceA = priceA;
        this.priceB = priceB;
    }
    
    @Override
    public String toString() {
        return "RespS2cYgbhInfo{listA=" + this.listA + ", listB=" + this.listB + ", priceA=" + this.priceA + ", priceB=" + this.priceB + '}';
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final RespS2cYgbhInfo that = (RespS2cYgbhInfo)o;
        return this.priceA == that.priceA && this.priceB == that.priceB && Objects.equals(this.listA, that.listA) && Objects.equals(this.listB, that.listB);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.listA, this.listB, this.priceA, this.priceB);
    }
    
    public int getPriceA() {
        return this.priceA;
    }
    
    public void setPriceA(final int priceA) {
        this.priceA = priceA;
    }
    
    public int getPriceB() {
        return this.priceB;
    }
    
    public void setPriceB(final int priceB) {
        this.priceB = priceB;
    }
    
    public ListA getListA() {
        return this.listA;
    }
    
    public void setListA(final ListA listA) {
        this.listA = listA;
    }
    
    public ListB getListB() {
        return this.listB;
    }
    
    public void setListB(final ListB listB) {
        this.listB = listB;
    }
}
