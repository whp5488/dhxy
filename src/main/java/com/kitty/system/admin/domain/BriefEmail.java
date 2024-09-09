package com.kitty.system.admin.domain;

import com.kitty.system.game.email.domain.EmailSystemAccessory;
import java.util.List;
import java.io.Serializable;

public class BriefEmail implements Serializable
{
    private int rec;
    private String title;
    private String con;
    private Long time;
    private int sen;
    private List<EmailSystemAccessory> list;
    
    public BriefEmail() {
    }
    
    public int getRec() {
        return this.rec;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public String getCon() {
        return this.con;
    }
    
    public Long getTime() {
        return this.time;
    }
    
    public int getSen() {
        return this.sen;
    }
    
    public List<EmailSystemAccessory> getList() {
        return this.list;
    }
    
    public void setRec(final int rec) {
        this.rec = rec;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setCon(final String con) {
        this.con = con;
    }
    
    public void setTime(final Long time) {
        this.time = time;
    }
    
    public void setSen(final int sen) {
        this.sen = sen;
    }
    
    public void setList(final List<EmailSystemAccessory> list) {
        this.list = list;
    }
}
