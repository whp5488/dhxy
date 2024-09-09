package com.kitty.system.game.fightnew.imbue.bean;

import java.util.ArrayList;
import java.util.List;
import java.io.Serializable;

public class Imbue implements Serializable
{
    private List<ImbueParam> status;
    private List<ImbueParam> skills;
    private List<ImbueParam> rounds;
    private List<ImbueParam> ignores;
    private double temp;
    
    public Imbue() {
        this.status = new ArrayList<>();
        this.skills = new ArrayList<>();
        this.rounds = new ArrayList<>();
        this.ignores = new ArrayList<>();
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
    
    public List<ImbueParam> getIgnores() {
        return this.ignores;
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
    
    public void setIgnores(final List<ImbueParam> ignores) {
        this.ignores = ignores;
    }
    
    public void setTemp(final double temp) {
        this.temp = temp;
    }
}
