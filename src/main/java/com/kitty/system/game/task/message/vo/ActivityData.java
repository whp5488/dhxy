
package com.kitty.system.game.task.message.vo;

import java.io.Serializable;

public class ActivityData implements Serializable
{
    private short serial;
    private short num;
    private byte liveness;
    
    public ActivityData(final short serial, final short num, final byte liveness) {
        this.liveness = 0;
        this.serial = serial;
        this.liveness = liveness;
        this.num = num;
    }
    
    public ActivityData() {
        this.liveness = 0;
    }
    
    public short getSerial() {
        return this.serial;
    }
    
    public void setSerial(final short serial) {
        this.serial = serial;
    }
    
    public byte getLiveness() {
        return this.liveness;
    }
    
    public void setLiveness(final byte liveness) {
        this.liveness = liveness;
    }
    
    public short getNum() {
        return this.num;
    }
    
    public void setNum(final short num) {
        this.num = num;
    }
}
