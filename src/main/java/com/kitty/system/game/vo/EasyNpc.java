
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
    
    public EasyNpc(final int id, final short m, final short x, final short y, final short s, final String n) {
        this.s = 1;
        this.t = 1;
        this.id = id;
        this.m = m;
        this.x = x;
        this.y = y;
        this.s = s;
        this.n = n;
    }
    
    public EasyNpc(final Npc npc, final byte t) {
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
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof EasyNpc)) {
            return false;
        }
        final EasyNpc other = (EasyNpc)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getM() != other.getM()) {
            return false;
        }
        if (this.getX() != other.getX()) {
            return false;
        }
        if (this.getY() != other.getY()) {
            return false;
        }
        if (this.getS() != other.getS()) {
            return false;
        }
        final Object this$n = this.getN();
        final Object other$n = other.getN();
        if (this$n == null) {
            if (other$n == null) {
                return this.getT() == other.getT();
            }
        }
        else if (this$n.equals(other$n)) {
            return this.getT() == other.getT();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof EasyNpc;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getM();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getS();
        final Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        result = result * 59 + this.getT();
        return result;
    }
    
    @Override
    public String toString() {
        return "EasyNpc(id=" + this.getId() + ", m=" + this.getM() + ", x=" + this.getX() + ", y=" + this.getY() + ", s=" + this.getS() + ", n=" + this.getN() + ", t=" + this.getT() + ")";
    }
}
