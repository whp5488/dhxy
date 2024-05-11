
package com.kitty.system.game.email.domain;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class EmailSystemAccessory implements Serializable
{
    private byte type;
    private int serial;
    private String num;
    
    public byte getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getNum() {
        return this.num;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final String num) {
        this.num = num;
    }
    
    public EmailSystemAccessory() {
    }
    
    @ConstructorProperties({ "type", "serial", "num" })
    public EmailSystemAccessory(final byte type, final int serial, final String num) {
        this.type = type;
        this.serial = serial;
        this.num = num;
    }
}
