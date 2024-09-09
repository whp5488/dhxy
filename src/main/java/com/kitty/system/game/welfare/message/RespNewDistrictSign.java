package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41240)
public class RespNewDistrictSign extends Message
{
    private String json;
    private int day;
    
    public String getJson() {
        return this.json;
    }
    
    public int getDay() {
        return this.day;
    }
    
    public void setJson(final String json) {
        this.json = json;
    }
    
    public void setDay(final int day) {
        this.day = day;
    }

    public RespNewDistrictSign(final String json, final int day) {
        this.day = 1;
        this.json = json;
        this.day = day;
    }
    
    public RespNewDistrictSign() {
        this.day = 1;
    }
}
