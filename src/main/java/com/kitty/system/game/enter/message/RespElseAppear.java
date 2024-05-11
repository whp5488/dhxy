
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
        final RespElseAppear other = (RespElseAppear)o;
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
        final Object this$n = this.getN();
        final Object other$n = other.getN();
        Label_0117: {
            if (this$n == null) {
                if (other$n == null) {
                    break Label_0117;
                }
            }
            else if (this$n.equals(other$n)) {
                break Label_0117;
            }
            return false;
        }
        final Object this$t = this.getT();
        final Object other$t = other.getT();
        if (this$t == null) {
            if (other$t == null) {
                return this.getC() == other.getC();
            }
        }
        else if (this$t.equals(other$t)) {
            return this.getC() == other.getC();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespElseAppear;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getX();
        result = result * 59 + this.getY();
        result = result * 59 + this.getS();
        final Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        final Object $t = this.getT();
        result = result * 59 + (($t == null) ? 43 : $t.hashCode());
        result = result * 59 + this.getC();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespElseAppear(id=" + this.getId() + ", x=" + this.getX() + ", y=" + this.getY() + ", s=" + this.getS() + ", n=" + this.getN() + ", t=" + this.getT() + ", c=" + this.getC() + ")";
    }
}
