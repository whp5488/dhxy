package com.kitty.system.game.role.domain;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class Goods implements Serializable
{
    private String id;
    private Integer num;
    private Integer type;
    private Integer position;
    private List<FiedValue> rests;
    
    public String getId() {
        return this.id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public Integer getNum() {
        return this.num;
    }
    
    public void setNum(Integer num) {
        this.num = num;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public void setType(Integer type) {
        this.type = type;
    }
    
    public Integer getPosition() {
        return this.position;
    }
    
    public void setPosition(Integer position) {
        this.position = position;
    }
    
    public List<FiedValue> getRests() {
        return this.rests;
    }
    
    public void setRests(List<FiedValue> rests) {
        this.rests = rests;
    }
    
    public Goods() {
        this.rests = new ArrayList<>();
    }
    
    public Goods(Integer num, Integer type, Integer position) {
        this.rests = new ArrayList<>();
        this.num = num;
        this.type = type;
        this.position = position;
    }
    
    public Goods(Integer num, Integer type, Integer position, List<FiedValue> rests) {
        this.rests = new ArrayList<>();
        this.num = num;
        this.type = type;
        this.position = position;
        this.rests = rests;
    }
    
    public Goods(String id, Integer num, Integer type, Integer position, List<FiedValue> rests) {
        this.rests = new ArrayList<>();
        this.id = id;
        this.num = num;
        this.type = type;
        this.position = position;
        this.rests = rests;
    }
}
