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
        this.k = Integer.valueOf(0);
        this.v = Integer.valueOf(0);
        this.r = Integer.valueOf(-1);
        this.t = Integer.valueOf(0);
        this.d = null;
        this.bd = 0;
    }
    
    public Param(Integer k, Integer v) {
        this.k = Integer.valueOf(0);
        this.v = Integer.valueOf(0);
        this.r = Integer.valueOf(-1);
        this.t = Integer.valueOf(0);
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
    }
    
    public Param(Integer k, Integer v, Integer t) {
        this.k = Integer.valueOf(0);
        this.v = Integer.valueOf(0);
        this.r = Integer.valueOf(-1);
        this.t = Integer.valueOf(0);
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
        this.t = t;
    }
    
    public Param(Integer k, Integer v, Integer t, Integer r) {
        this.k = Integer.valueOf(0);
        this.v = Integer.valueOf(0);
        this.r = Integer.valueOf(-1);
        this.t = Integer.valueOf(0);
        this.d = null;
        this.bd = 0;
        this.k = k;
        this.v = v;
        this.t = t;
        this.r = r;
    }
    
    public Param(Integer k, Integer v, Integer r, Integer t, String d) {
        this.k = Integer.valueOf(0);
        this.v = Integer.valueOf(0);
        this.r = Integer.valueOf(-1);
        this.t = Integer.valueOf(0);
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
        Param other = (Param)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$k = this.getK();
        Object other$k = other.getK();
        if (!((this$k != null) ? this$k.equals(other$k) : (other$k == null))) {
            return false;
        }
        Object this$v = this.getV();
        Object other$v = other.getV();
        if (!((this$v != null) ? this$v.equals(other$v) : (other$v == null))) {
            return false;
        }
        Object this$r = this.getR();
        Object other$r = other.getR();
        if (!((this$r != null) ? this$r.equals(other$r) : (other$r == null))) {
            return false;
        }
        Object this$t = this.getT();
        Object other$t = other.getT();
        if (!((this$t != null) ? this$t.equals(other$t) : (other$t == null))) {
            return false;
        }
        Object this$d = this.getD();
        Object other$d = other.getD();
        return ((this$d != null) ? this$d.equals(other$d) : (other$d == null)) && this.getBd() == other.getBd();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Param;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $k = this.getK();
        result = result * 59 + (($k == null) ? 43 : $k.hashCode());
        Object $v = this.getV();
        result = result * 59 + (($v == null) ? 43 : $v.hashCode());
        Object $r = this.getR();
        result = result * 59 + (($r == null) ? 43 : $r.hashCode());
        Object $t = this.getT();
        result = result * 59 + (($t == null) ? 43 : $t.hashCode());
        Object $d = this.getD();
        result = result * 59 + (($d == null) ? 43 : $d.hashCode());
        result = result * 59 + this.getBd();
        return result;
    }
    
    @Override
    public String toString() {
        return "Param(k=" + this.getK() + ", v=" + this.getV() + ", r=" + this.getR() + ", t=" + this.getT() + ", d=" + this.getD() + ", bd=" + this.getBd() + ")";
    }
}
