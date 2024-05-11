
package com.kitty.system.game.fightnew.imbue.bean;

import java.util.ArrayList;
import java.util.List;

public class Imbue
{
    private List<ImbueParam> status;
    private List<ImbueParam> skills;
    private List<ImbueParam> rounds;
    private double temp;
    
    public Imbue() {
        this.status = new ArrayList<ImbueParam>();
        this.skills = new ArrayList<ImbueParam>();
        this.rounds = new ArrayList<ImbueParam>();
        this.temp = 0.0;
    }
    
    public List<ImbueParam> getStatus() {
        return this.status;
    }
    
    public List<ImbueParam> getSkills() {
        return this.skills;
    }
    
    public List<ImbueParam> getRounds() {
        return this.rounds;
    }
    
    public double getTemp() {
        return this.temp;
    }
    
    public void setStatus(final List<ImbueParam> status) {
        this.status = status;
    }
    
    public void setSkills(final List<ImbueParam> skills) {
        this.skills = skills;
    }
    
    public void setRounds(final List<ImbueParam> rounds) {
        this.rounds = rounds;
    }
    
    public void setTemp(final double temp) {
        this.temp = temp;
    }
}
