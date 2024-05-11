
package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;

public class LifeManaParam
{
    private String saveLife;
    private String saveMana;
    private int maxLife;
    private int maxMana;
    private int currLife;
    private int currMana;
    
    public void setSaveLife(final String saveLife) {
        this.saveLife = saveLife;
    }
    
    public void setSaveMana(final String saveMana) {
        this.saveMana = saveMana;
    }
    
    public void setMaxLife(final int maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setMaxMana(final int maxMana) {
        this.maxMana = maxMana;
    }
    
    public void setCurrLife(final int currLife) {
        this.currLife = currLife;
    }
    
    public void setCurrMana(final int currMana) {
        this.currMana = currMana;
    }
    
    public String getSaveLife() {
        return this.saveLife;
    }
    
    public String getSaveMana() {
        return this.saveMana;
    }
    
    public int getMaxLife() {
        return this.maxLife;
    }
    
    public int getMaxMana() {
        return this.maxMana;
    }
    
    public int getCurrLife() {
        return this.currLife;
    }
    
    public int getCurrMana() {
        return this.currMana;
    }
    
    public LifeManaParam() {
    }
    
    @ConstructorProperties({ "saveLife", "saveMana", "maxLife", "maxMana", "currLife", "currMana" })
    public LifeManaParam(final String saveLife, final String saveMana, final int maxLife, final int maxMana, final int currLife, final int currMana) {
        this.saveLife = saveLife;
        this.saveMana = saveMana;
        this.maxLife = maxLife;
        this.maxMana = maxMana;
        this.currLife = currLife;
        this.currMana = currMana;
    }
}
