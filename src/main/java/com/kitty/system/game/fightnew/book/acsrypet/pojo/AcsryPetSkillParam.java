package com.kitty.system.game.fightnew.book.acsrypet.pojo;

import java.io.Serializable;

public class AcsryPetSkillParam implements Serializable
{
    private int serial;
    private double pval;
    
    public int getSerial() {
        return this.serial;
    }
    
    public double getPval() {
        return this.pval;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPval(final double pval) {
        this.pval = pval;
    }
    
    public AcsryPetSkillParam(final int serial, final double pval) {
        this.serial = serial;
        this.pval = pval;
    }
    
    public AcsryPetSkillParam() {
    }
}
