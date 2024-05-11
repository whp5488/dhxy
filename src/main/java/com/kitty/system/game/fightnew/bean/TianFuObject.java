
package com.kitty.system.game.fightnew.bean;

import java.io.Serializable;

public class TianFuObject implements Serializable
{
    private long roleTianFuId;
    private int ceSerial;
    private byte level;
    private byte productOrder;
    private String triggerType;
    
    public long getRoleTianFuId() {
        return this.roleTianFuId;
    }
    
    public int getCeSerial() {
        return this.ceSerial;
    }
    
    public byte getLevel() {
        return this.level;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public String getTriggerType() {
        return this.triggerType;
    }
    
    public void setRoleTianFuId(final long roleTianFuId) {
        this.roleTianFuId = roleTianFuId;
    }
    
    public void setCeSerial(final int ceSerial) {
        this.ceSerial = ceSerial;
    }
    
    public void setLevel(final byte level) {
        this.level = level;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    public void setTriggerType(final String triggerType) {
        this.triggerType = triggerType;
    }
}
