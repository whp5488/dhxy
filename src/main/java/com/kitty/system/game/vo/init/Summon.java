package com.kitty.system.game.vo.init;

import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import java.io.Serializable;

public class Summon implements Serializable
{
    private Integer serial;
    private Integer num;
    private Integer rate;
    @Default("[]")
    private List<Integer> monster;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getRate() {
        return this.rate;
    }
    
    public List<Integer> getMonster() {
        return this.monster;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setRate(final Integer rate) {
        this.rate = rate;
    }
    
    public void setMonster(final List<Integer> monster) {
        this.monster = monster;
    }

    public Summon() {
    }
    
    public Summon(final Integer serial, final Integer num, final Integer rate, final List<Integer> monster) {
        this.serial = serial;
        this.num = num;
        this.rate = rate;
        this.monster = monster;
    }
}
