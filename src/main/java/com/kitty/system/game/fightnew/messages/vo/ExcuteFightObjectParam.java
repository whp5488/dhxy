
package com.kitty.system.game.fightnew.messages.vo;

import java.beans.ConstructorProperties;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class ExcuteFightObjectParam implements Serializable
{
    private List<Integer> damageType;
    private List<Integer> statusList;
    private List<Integer> clearStatus;
    private List<FiedValue> fiedValues;
    
    public void setDamageType(final List<Integer> damageType) {
        this.damageType = damageType;
    }
    
    public void setStatusList(final List<Integer> statusList) {
        this.statusList = statusList;
    }
    
    public void setClearStatus(final List<Integer> clearStatus) {
        this.clearStatus = clearStatus;
    }
    
    public void setFiedValues(final List<FiedValue> fiedValues) {
        this.fiedValues = fiedValues;
    }
    
    public List<Integer> getDamageType() {
        return this.damageType;
    }
    
    public List<Integer> getStatusList() {
        return this.statusList;
    }
    
    public List<Integer> getClearStatus() {
        return this.clearStatus;
    }
    
    public List<FiedValue> getFiedValues() {
        return this.fiedValues;
    }
    
    @ConstructorProperties({ "damageType", "statusList", "clearStatus", "fiedValues" })
    public ExcuteFightObjectParam(final List<Integer> damageType, final List<Integer> statusList, final List<Integer> clearStatus, final List<FiedValue> fiedValues) {
        this.damageType = damageType;
        this.statusList = statusList;
        this.clearStatus = clearStatus;
        this.fiedValues = fiedValues;
    }
    
    public ExcuteFightObjectParam() {
    }
}
