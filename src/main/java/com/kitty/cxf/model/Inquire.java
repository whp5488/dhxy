package com.kitty.cxf.model;

import java.io.Serializable;

public class Inquire implements Serializable
{
    private long id;
    private int r;
    private String n;
    private int no;
    private byte re;
    private short gr;
    private String xy;
    private String yl;
    private String total;
    
    public Inquire() {
    }
    
    public Inquire(long id, int r, String n, int no, byte re, short gr, String xy, String yl, String total) {
        this.id = id;
        this.r = r;
        this.n = n;
        this.no = no;
        this.re = re;
        this.gr = gr;
        this.xy = xy;
        this.yl = yl;
        this.total = total;
    }
    
    public long getId() {
        return this.id;
    }
    
    public int getR() {
        return this.r;
    }
    
    public String getN() {
        return this.n;
    }
    
    public int getNo() {
        return this.no;
    }
    
    public byte getRe() {
        return this.re;
    }
    
    public short getGr() {
        return this.gr;
    }
    
    public String getXy() {
        return this.xy;
    }
    
    public String getYl() {
        return this.yl;
    }
    
    public String getTotal() {
        return this.total;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setR(final int r) {
        this.r = r;
    }
    
    public void setN(final String n) {
        this.n = n;
    }
    
    public void setNo(final int no) {
        this.no = no;
    }
    
    public void setRe(final byte re) {
        this.re = re;
    }
    
    public void setGr(final short gr) {
        this.gr = gr;
    }
    
    public void setXy(final String xy) {
        this.xy = xy;
    }
    
    public void setYl(final String yl) {
        this.yl = yl;
    }
    
    public void setTotal(final String total) {
        this.total = total;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Inquire)) {
            return false;
        }
        Inquire other = (Inquire)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getR() != other.getR()) {
            return false;
        }
        Object this$n = this.getN();
        Object other$n = other.getN();
        if (!((this$n != null) ? this$n.equals(other$n) : (other$n == null))) {
            return false;
        }
        if (this.getNo() != other.getNo()) {
            return false;
        }
        if (this.getRe() != other.getRe()) {
            return false;
        }
        if (this.getGr() != other.getGr()) {
            return false;
        }
        Object this$xy = this.getXy();
        Object other$xy = other.getXy();
        if (!((this$xy != null) ? this$xy.equals(other$xy) : (other$xy == null))) {
            return false;
        }
        Object this$yl = this.getYl();
        Object other$yl = other.getYl();
        if (!((this$yl != null) ? this$yl.equals(other$yl) : (other$yl == null))) {
            return false;
        }
        Object this$total = this.getTotal();
        Object other$total = other.getTotal();
        return (this$total != null) ? this$total.equals(other$total) : (other$total == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Inquire;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        long $id = this.getId();
        result = result * 59 + (int)($id >>> 32 ^ $id);
        result = result * 59 + this.getR();
        Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        result = result * 59 + this.getNo();
        result = result * 59 + this.getRe();
        result = result * 59 + this.getGr();
        Object $xy = this.getXy();
        result = result * 59 + (($xy == null) ? 43 : $xy.hashCode());
        Object $yl = this.getYl();
        result = result * 59 + (($yl == null) ? 43 : $yl.hashCode());
        Object $total = this.getTotal();
        result = result * 59 + (($total == null) ? 43 : $total.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Inquire(id=" + this.getId() + ", r=" + this.getR() + ", n=" + this.getN() + ", no=" + this.getNo() + ", re=" + (int)this.getRe() + ", gr=" + (int)this.getGr() + ", xy=" + this.getXy() + ", yl=" + this.getYl() + ", total=" + this.getTotal() + ")";
    }
}
