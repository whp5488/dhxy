
package com.kitty.system.game.market.message.vo;

import java.beans.ConstructorProperties;
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
        final Columbia other = (Columbia)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
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
        return other instanceof Columbia;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Columbia(id=" + this.getId() + ", serial=" + this.getSerial() + ", price=" + this.getPrice() + ")";
    }
    
    @ConstructorProperties({ "id", "serial", "price" })
    public Columbia(final String id, final int serial, final String price) {
        this.id = id;
        this.serial = serial;
        this.price = price;
    }
    
    public Columbia() {
    }
}
