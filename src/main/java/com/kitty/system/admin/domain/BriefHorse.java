
package com.kitty.system.admin.domain;

import java.io.Serializable;

public class BriefHorse implements Serializable
{
    private int id;
    private byte seat;
    private int growUp;
    private int move;
    private String skills;
    
    public int getId() {
        return this.id;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public byte getSeat() {
        return this.seat;
    }
    
    public void setSeat(final byte seat) {
        this.seat = seat;
    }
    
    public int getGrowUp() {
        return this.growUp;
    }
    
    public void setGrowUp(final int growUp) {
        this.growUp = growUp;
    }
    
    public int getMove() {
        return this.move;
    }
    
    public void setMove(final int move) {
        this.move = move;
    }
    
    public String getSkills() {
        return this.skills;
    }
    
    public void setSkills(final String skills) {
        this.skills = skills;
    }
    
    public BriefHorse() {
    }
    
    public BriefHorse(final int id, final byte seat, final int growUp, final int move, final String skills) {
        this.id = id;
        this.seat = seat;
        this.growUp = growUp;
        this.move = move;
        this.skills = skills;
    }
}
