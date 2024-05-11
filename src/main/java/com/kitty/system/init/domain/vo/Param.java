
package com.kitty.system.init.domain.vo;

import java.io.Serializable;

public class Param implements Serializable
{
    private Integer k;
    private Integer v;
    private Integer r;
    private Integer t;
    private String d;
    private int bd;
    
    public Param() {
        this.k = 0;
        this.v = 0;
        this.r = -1;
        this.t = 0;
        this.d = null;
        this.bd = 0;
    }
    
    public Param(final Integer k, final Integer v) {
        this.k = 0;
        this.v = 0;
        this.r = -1;
        this.t = 0;
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
    }
    
    public Param(final Integer k, final Integer v, final Integer t) {
        this.k = 0;
        this.v = 0;
        this.r = -1;
        this.t = 0;
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
        this.t = t;
    }
    
    public Param(final Integer k, final Integer v, final Integer t, final Integer r) {
        this.k = 0;
        this.v = 0;
        this.r = -1;
        this.t = 0;
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
        this.t = t;
        this.r = r;
    }
    
    public Param(final Integer k, final Integer v, final Integer r, final Integer t, final String d) {
        this.k = 0;
        this.v = 0;
        this.r = -1;
        this.t = 0;
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
        this.r = r;
        this.t = t;
        this.d = d;
    }
    
    public Integer getK() {
        return this.k;
    }
    
    public Integer getV() {
        return this.v;
    }
    
    public Integer getR() {
        return this.r;
    }
    
    public Integer getT() {
        return this.t;
    }
    
    public String getD() {
        return this.d;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public void setK(final Integer k) {
        this.k = k;
    }
    
    public void setV(final Integer v) {
        this.v = v;
    }
    
    public void setR(final Integer r) {
        this.r = r;
    }
    
    public void setT(final Integer t) {
        this.t = t;
    }
    
    public void setD(final String d) {
        this.d = d;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Param)) {
            return false;
        }
        final Param other = (Param)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$k = this.getK();
        final Object other$k = other.getK();
        Label_0065: {
            if (this$k == null) {
                if (other$k == null) {
                    break Label_0065;
                }
            }
            else if (this$k.equals(other$k)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$v = this.getV();
        final Object other$v = other.getV();
        Label_0102: {
            if (this$v == null) {
                if (other$v == null) {
                    break Label_0102;
                }
            }
            else if (this$v.equals(other$v)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$r = this.getR();
        final Object other$r = other.getR();
        Label_0139: {
            if (this$r == null) {
                if (other$r == null) {
                    break Label_0139;
                }
            }
            else if (this$r.equals(other$r)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$t = this.getT();
        final Object other$t = other.getT();
        Label_0176: {
            if (this$t == null) {
                if (other$t == null) {
                    break Label_0176;
                }
            }
            else if (this$t.equals(other$t)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$d = this.getD();
        final Object other$d = other.getD();
        if (this$d == null) {
            if (other$d == null) {
                return this.getBd() == other.getBd();
            }
        }
        else if (this$d.equals(other$d)) {
            return this.getBd() == other.getBd();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Param;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $k = this.getK();
        result = result * 59 + (($k == null) ? 43 : $k.hashCode());
        final Object $v = this.getV();
        result = result * 59 + (($v == null) ? 43 : $v.hashCode());
        final Object $r = this.getR();
        result = result * 59 + (($r == null) ? 43 : $r.hashCode());
        final Object $t = this.getT();
        result = result * 59 + (($t == null) ? 43 : $t.hashCode());
        final Object $d = this.getD();
        result = result * 59 + (($d == null) ? 43 : $d.hashCode());
        result = result * 59 + this.getBd();
        return result;
    }
    
    @Override
    public String toString() {
        return "Param(k=" + this.getK() + ", v=" + this.getV() + ", r=" + this.getR() + ", t=" + this.getT() + ", d=" + this.getD() + ", bd=" + this.getBd() + ")";
    }
}
