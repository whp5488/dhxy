package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry_suit_color")
public class SysDataAcsrySuitColor
{
    @Column("id")
    private int id;
    @Column("skill_serial")
    private int skillSerial;
    @Column("suit_dist")
    private String suitDist;
    @Column("suit_name")
    private String suitName;
    @Column("color_id")
    private byte colorId;
    
    public int getId() {
        return this.id;
    }
    
    public int getSkillSerial() {
        return this.skillSerial;
    }
    
    public String getSuitDist() {
        return this.suitDist;
    }
    
    public String getSuitName() {
        return this.suitName;
    }
    
    public byte getColorId() {
        return this.colorId;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setSkillSerial(final int skillSerial) {
        this.skillSerial = skillSerial;
    }
    
    public void setSuitDist(final String suitDist) {
        this.suitDist = suitDist;
    }
    
    public void setSuitName(final String suitName) {
        this.suitName = suitName;
    }
    
    public void setColorId(final byte colorId) {
        this.colorId = colorId;
    }
    
    public SysDataAcsrySuitColor() {
    }
    
    public SysDataAcsrySuitColor(final int id, final int skillSerial, final String suitDist, final String suitName, final byte colorId) {
        this.id = id;
        this.skillSerial = skillSerial;
        this.suitDist = suitDist;
        this.suitName = suitName;
        this.colorId = colorId;
    }
}
