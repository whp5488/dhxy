package com.kitty.system.game.vo;

import java.io.Serializable;

public class EasyNpc implements Serializable
{
    private int id;
    private short m;
    private short x;
    private short y;
    private short s;
    private String n;
    private byte t;
    
    public EasyNpc() {
        this.s = 1;
        this.t = 1;
    }
    
    public EasyNpc(int id, short m, short x, short y, short s, String n) {
        this.s = 1;
        this.t = 1;
        this.id = id;
        this.m = m;
        this.x = x;
        this.y = y;
        this.s = s;
        this.n = n;
    }
    
    public EasyNpc(Npc npc, byte t) {
        this.s = 1;
        this.t = 1;
        this.id = npc.getId();
        this.m = (short)npc.getMap();
        this.x = (short)npc.getX();
        this.y = (short)npc.getY();
        this.s = (short)npc.getIcon();
        this.n = npc.getName();
        this.t = t;
    }
    
    public int getId() {
        return this.id;
    }
    
    public short getM() {
        return this.m;
    }
    
    public short getX() {
        return this.x;
    }
    
    public short getY() {
        return this.y;
    }
    
    public short getS() {
        return this.s;
    }
    
    public String getN() {
        return this.n;
    }
    
    public byte getT() {
        return this.t;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setM(final short m) {
        this.m = m;
    }
    
    public void setX(final short x) {
        this.x = x;
    }
    
    public void setY(final short y) {
        this.y = y;
    }
    
    public void setS(final short s) {
        this.s = s;
    }
    
    public void setN(final String n) {
        this.n = n;
    }
    
    public void setT(final byte t) {
        this.t = t;
    }
}
