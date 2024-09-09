package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 65530)
public class RespElseAppear extends Message
{
    private int id;
    private short x;
    private short y;
    private short s;
    private String n;
    private String t;
    private byte c;
    
    public RespElseAppear() {
        this.s = 1;
        this.c = 0;
    }
    
    public int getId() {
        return this.id;
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
    
    public String getT() {
        return this.t;
    }
    
    public byte getC() {
        return this.c;
    }
    
    public void setId(final int id) {
        this.id = id;
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
    
    public void setT(final String t) {
        this.t = t;
    }
    
    public void setC(final byte c) {
        this.c = c;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespElseAppear)) {
            return false;
        }
        RespElseAppear other = (RespElseAppear)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
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
        Object this$n = this.getN();
        Object other$n = other.getN();
        if (!((this$n != null) ? this$n.equals(other$n) : (other$n == null))) {
            return false;
        }
        Object this$t = this.getT();
        Object other$t = other.getT();
        return ((this$t != null) ? this$t.equals(other$t) : (other$t == null)) && this.getC() == other.getC();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespElseAppear;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getS();
        Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        Object $t = this.getT();
        result = result * 59 + (($t == null) ? 43 : $t.hashCode());
        result = result * 59 + this.getC();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespElseAppear(id=" + this.getId() + ", x=" + (int)this.getX() + ", y=" + (int)this.getY() + ", s=" + (int)this.getS() + ", n=" + this.getN() + ", t=" + this.getT() + ", c=" + (int)this.getC() + ")";
    }
}
