
package com.kitty.system.game.fightnew.book.tianfu.pojo;

import java.io.Serializable;

public class TianFuAddition implements Serializable
{
    private short dayInCeLevel;
    private byte tianFuLevel;
    
    public TianFuAddition(final short dayInCeLevel, final byte tianFuLevel) {
        this.dayInCeLevel = dayInCeLevel;
        this.tianFuLevel = tianFuLevel;
    }
    
    public short getDayInCeLevel() {
        return this.dayInCeLevel;
    }
    
    public byte getTianFuLevel() {
        return this.tianFuLevel;
    }
    
    public void setDayInCeLevel(final short dayInCeLevel) {
        this.dayInCeLevel = dayInCeLevel;
    }
    
    public void setTianFuLevel(final byte tianFuLevel) {
        this.tianFuLevel = tianFuLevel;
    }
}
