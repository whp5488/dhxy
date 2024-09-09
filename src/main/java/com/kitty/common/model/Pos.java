package com.kitty.common.model;

import java.io.Serializable;

public class Pos implements Serializable
{
    private Integer x;
    private Integer y;
    private Integer dir;
    private Integer mapId;
    
    public Integer getX() {
        return this.x;
    }
    
    public void setX(Integer x) {
        this.x = x;
    }
    
    public Integer getY() {
        return this.y;
    }
    
    public void setY(Integer y) {
        this.y = y;
    }
    
    public Integer getDir() {
        return this.dir;
    }
    
    public void setDir(Integer dir) {
        this.dir = dir;
    }
    
    public Integer getMapId() {
        return this.mapId;
    }
    
    public void setMapId(Integer mapId) {
        this.mapId = mapId;
    }
    
    public Pos(Integer x, Integer y, Integer dir, Integer mapId) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.mapId = mapId;
    }
    
    public Pos() {
        this.x = Integer.valueOf(155);
        this.y = Integer.valueOf(70);
        this.dir = Integer.valueOf(5);
        this.mapId = Integer.valueOf(1010);
    }
    
    @Override
    public String toString() {
        return "Pos{x=" + this.x + ", y=" + this.y + ", dir=" + this.dir + ", mapId=" + this.mapId + '}';
    }
}
