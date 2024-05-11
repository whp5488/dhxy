
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
    
    public Inquire(final long id, final int r, final String n, final int no, final byte re, final short gr, final String xy, final String yl, final String total) {
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
        final Inquire other = (Inquire)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getR() != other.getR()) {
            return false;
        }
        final Object this$n = this.getN();
        final Object other$n = other.getN();
        Label_0092: {
            if (this$n == null) {
                if (other$n == null) {
                    break Label_0092;
                }
            }
            else if (this$n.equals(other$n)) {
                break Label_0092;
            }
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
        final Object this$xy = this.getXy();
        final Object other$xy = other.getXy();
        Label_0168: {
            if (this$xy == null) {
                if (other$xy == null) {
                    break Label_0168;
                }
            }
            else if (this$xy.equals(other$xy)) {
                break Label_0168;
            }
            return false;
        }
        final Object this$yl = this.getYl();
        final Object other$yl = other.getYl();
        Label_0205: {
            if (this$yl == null) {
                if (other$yl == null) {
                    break Label_0205;
                }
            }
            else if (this$yl.equals(other$yl)) {
                break Label_0205;
            }
            return false;
        }
        final Object this$total = this.getTotal();
        final Object other$total = other.getTotal();
        if (this$total == null) {
            if (other$total == null) {
                return true;
            }
        }
        else if (this$total.equals(other$total)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Inquire;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $id = this.getId();
        result = result * 59 + (int)($id >>> 32 ^ $id);
        result = result * 59 + this.getR();
        final Object $n = this.getN();
        result = result * 59 + (($n == null) ? 43 : $n.hashCode());
        result = result * 59 + this.getNo();
        result = result * 59 + this.getRe();
        result = result * 59 + this.getGr();
        final Object $xy = this.getXy();
        result = result * 59 + (($xy == null) ? 43 : $xy.hashCode());
        final Object $yl = this.getYl();
        result = result * 59 + (($yl == null) ? 43 : $yl.hashCode());
        final Object $total = this.getTotal();
        result = result * 59 + (($total == null) ? 43 : $total.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Inquire(id=" + this.getId() + ", r=" + this.getR() + ", n=" + this.getN() + ", no=" + this.getNo() + ", re=" + this.getRe() + ", gr=" + this.getGr() + ", xy=" + this.getXy() + ", yl=" + this.getYl() + ", total=" + this.getTotal() + ")";
    }
}
