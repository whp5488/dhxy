
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
    
    public void setX(final Integer x) {
        this.x = x;
    }
    
    public Integer getY() {
        return this.y;
    }
    
    public void setY(final Integer y) {
        this.y = y;
    }
    
    public Integer getDir() {
        return this.dir;
    }
    
    public void setDir(final Integer dir) {
        this.dir = dir;
    }
    
    public Integer getMapId() {
        return this.mapId;
    }
    
    public void setMapId(final Integer mapId) {
        this.mapId = mapId;
    }
    
    public Pos(final Integer x, final Integer y, final Integer dir, final Integer mapId) {
        this.x = x;
        this.y = y;
        this.dir = dir;
        this.mapId = mapId;
    }
    
    public Pos() {
        this.x = 155;
        this.y = 70;
        this.dir = 5;
        this.mapId = 1010;
    }
    
    @Override
    public String toString() {
        return "Pos{x=" + this.x + ", y=" + this.y + ", dir=" + this.dir + ", mapId=" + this.mapId + '}';
    }
}
