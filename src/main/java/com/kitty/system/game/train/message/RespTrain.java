package com.kitty.system.game.train.message;

import com.kitty.system.game.train.domain.Train;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20167)
public class RespTrain extends Message
{
    private int qianGrade;
    private int xunGrade;
    private int kanGrade;
    private int genGrade;
    private int kunGrade;
    private int zhenGrade;
    private int liGrade;
    private int duiGrade;
    
    public RespTrain() {
        this.qianGrade = 0;
        this.xunGrade = 0;
        this.kanGrade = 0;
        this.genGrade = 0;
        this.kunGrade = 0;
        this.zhenGrade = 0;
        this.liGrade = 0;
        this.duiGrade = 0;
    }
    
    public RespTrain(Train train) {
        this.qianGrade = 0;
        this.xunGrade = 0;
        this.kanGrade = 0;
        this.genGrade = 0;
        this.kunGrade = 0;
        this.zhenGrade = 0;
        this.liGrade = 0;
        this.duiGrade = 0;
        this.duiGrade = train.getDuiGrade();
        this.genGrade = train.getGenGrade();
        this.kanGrade = train.getKanGrade();
        this.kunGrade = train.getKunGrade();
        this.liGrade = train.getLiGrade();
        this.qianGrade = train.getQianGrade();
        this.xunGrade = train.getXunGrade();
        this.zhenGrade = train.getZhenGrade();
    }
    
    public int getQianGrade() {
        return this.qianGrade;
    }
    
    public int getXunGrade() {
        return this.xunGrade;
    }
    
    public int getKanGrade() {
        return this.kanGrade;
    }
    
    public int getGenGrade() {
        return this.genGrade;
    }
    
    public int getKunGrade() {
        return this.kunGrade;
    }
    
    public int getZhenGrade() {
        return this.zhenGrade;
    }
    
    public int getLiGrade() {
        return this.liGrade;
    }
    
    public int getDuiGrade() {
        return this.duiGrade;
    }
    
    public void setQianGrade(final int qianGrade) {
        this.qianGrade = qianGrade;
    }
    
    public void setXunGrade(final int xunGrade) {
        this.xunGrade = xunGrade;
    }
    
    public void setKanGrade(final int kanGrade) {
        this.kanGrade = kanGrade;
    }
    
    public void setGenGrade(final int genGrade) {
        this.genGrade = genGrade;
    }
    
    public void setKunGrade(final int kunGrade) {
        this.kunGrade = kunGrade;
    }
    
    public void setZhenGrade(final int zhenGrade) {
        this.zhenGrade = zhenGrade;
    }
    
    public void setLiGrade(final int liGrade) {
        this.liGrade = liGrade;
    }
    
    public void setDuiGrade(final int duiGrade) {
        this.duiGrade = duiGrade;
    }
}
