
package com.kitty.system.game.enter.vo;

import com.kitty.system.game.vo.TitleInfo;

public class Title
{
    private int serial;
    private int time;
    
    public Title() {
    }
    
    public Title(final int serial, final int time) {
        this.serial = serial;
        this.time = time;
    }
    
    public Title(final TitleInfo titleInfo) {
        this.serial = titleInfo.getSerial();
        this.time = titleInfo.getTime();
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
}
