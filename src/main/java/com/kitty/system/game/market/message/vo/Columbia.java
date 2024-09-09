package com.kitty.system.game.market.message.vo;

import java.io.Serializable;

public class Columbia implements Serializable
{
    private String id;
    private int serial;
    private String price;
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPrice() {
        return this.price;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPrice(final String price) {
        this.price = price;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Columbia)) {
            return false;
        }
        Columbia other = (Columbia)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$price = this.getPrice();
        Object other$price = other.getPrice();
        return (this$price != null) ? this$price.equals(other$price) : (other$price == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Columbia;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Columbia(id=" + this.getId() + ", serial=" + this.getSerial() + ", price=" + this.getPrice() + ")";
    }
    
    public Columbia(final String id, final int serial, final String price) {
        this.id = id;
        this.serial = serial;
        this.price = price;
    }
    
    public Columbia() {
    }
}
