
package com.kitty.system.init.domain;

import java.beans.ConstructorProperties;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry_skill")
public class SysDataAcsrySkill
{
    @Column("serial")
    private int serial;
    @Column("name")
    private int name;
    @Column("serial")
    private int initiative;
    @Column("trigger_type")
    @Default("[]")
    private List<String> triggerType;
    @Column("forthwith_type")
    @Default("[]")
    private List<Integer> forthwithType;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getName() {
        return this.name;
    }
    
    public int getInitiative() {
        return this.initiative;
    }
    
    public List<String> getTriggerType() {
        return this.triggerType;
    }
    
    public List<Integer> getForthwithType() {
        return this.forthwithType;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final int name) {
        this.name = name;
    }
    
    public void setInitiative(final int initiative) {
        this.initiative = initiative;
    }
    
    public void setTriggerType(final List<String> triggerType) {
        this.triggerType = triggerType;
    }
    
    public void setForthwithType(final List<Integer> forthwithType) {
        this.forthwithType = forthwithType;
    }
    
    public SysDataAcsrySkill() {
    }
    
    @ConstructorProperties({ "serial", "name", "initiative", "triggerType", "forthwithType" })
    public SysDataAcsrySkill(final int serial, final int name, final int initiative, final List<String> triggerType, final List<Integer> forthwithType) {
        this.serial = serial;
        this.name = name;
        this.initiative = initiative;
        this.triggerType = triggerType;
        this.forthwithType = forthwithType;
    }
}
