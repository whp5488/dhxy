package com.kitty.system.game.vo;

import java.io.Serializable;

public class TitleInfo implements Serializable
{
    private int serial;
    private int time;
    
    public TitleInfo(int serial, int time) {
        this.serial = serial;
        this.time = time;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public TitleInfo() {
    }
}
