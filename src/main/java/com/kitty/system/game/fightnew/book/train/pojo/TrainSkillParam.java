package com.kitty.system.game.fightnew.book.train.pojo;

import java.util.List;

public class TrainSkillParam
{
    private List<Integer> powers;
    private List<Integer> activeConds;
    private int posLevelSum;
    private double percent;
    
    public List<Integer> getPowers() {
        return this.powers;
    }
    
    public List<Integer> getActiveConds() {
        return this.activeConds;
    }
    
    public int getPosLevelSum() {
        return this.posLevelSum;
    }
    
    public double getPercent() {
        return this.percent;
    }
    
    public void setPowers(final List<Integer> powers) {
        this.powers = powers;
    }
    
    public void setActiveConds(final List<Integer> activeConds) {
        this.activeConds = activeConds;
    }
    
    public void setPosLevelSum(final int posLevelSum) {
        this.posLevelSum = posLevelSum;
    }
    
    public void setPercent(final double percent) {
        this.percent = percent;
    }
    
    public TrainSkillParam() {
    }
    
    public TrainSkillParam(final List<Integer> powers, final List<Integer> activeConds, final int posLevelSum, final double percent) {
        this.powers = powers;
        this.activeConds = activeConds;
        this.posLevelSum = posLevelSum;
        this.percent = percent;
    }
}
