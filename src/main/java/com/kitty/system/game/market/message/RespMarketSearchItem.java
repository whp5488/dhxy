package com.kitty.system.game.market.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 33054)
public class RespMarketSearchItem extends Message
{
    private int serial;
    private String price;
    private String minimum;
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPrice(final String price) {
        this.price = price;
    }
    
    public void setMinimum(final String minimum) {
        this.minimum = minimum;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPrice() {
        return this.price;
    }
    
    public String getMinimum() {
        return this.minimum;
    }
    
    public RespMarketSearchItem() {
    }
    
    public RespMarketSearchItem(final int serial, final String price, final String minimum) {
        this.serial = serial;
        this.price = price;
        this.minimum = minimum;
    }
}
