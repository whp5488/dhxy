package com.kitty.system.init.domain.vo;

public class AdviceAddPro
{
    private String title;
    private Integer gg;
    private Integer lx;
    private Integer ll;
    private Integer mj;
    private Integer use;
    
    public AdviceAddPro() {
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public Integer getGg() {
        return this.gg;
    }
    
    public Integer getLx() {
        return this.lx;
    }
    
    public Integer getLl() {
        return this.ll;
    }
    
    public Integer getMj() {
        return this.mj;
    }
    
    public Integer getUse() {
        return this.use;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setGg(final Integer gg) {
        this.gg = gg;
    }
    
    public void setLx(final Integer lx) {
        this.lx = lx;
    }
    
    public void setLl(final Integer ll) {
        this.ll = ll;
    }
    
    public void setMj(final Integer mj) {
        this.mj = mj;
    }
    
    public void setUse(final Integer use) {
        this.use = use;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof AdviceAddPro)) {
            return false;
        }
        AdviceAddPro other = (AdviceAddPro)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$title = this.getTitle();
        Object other$title = other.getTitle();
        if (!((this$title != null) ? this$title.equals(other$title) : (other$title == null))) {
            return false;
        }
        Object this$gg = this.getGg();
        Object other$gg = other.getGg();
        if (!((this$gg != null) ? this$gg.equals(other$gg) : (other$gg == null))) {
            return false;
        }
        Object this$lx = this.getLx();
        Object other$lx = other.getLx();
        if (!((this$lx != null) ? this$lx.equals(other$lx) : (other$lx == null))) {
            return false;
        }
        Object this$ll = this.getLl();
        Object other$ll = other.getLl();
        if (!((this$ll != null) ? this$ll.equals(other$ll) : (other$ll == null))) {
            return false;
        }
        Object this$mj = this.getMj();
        Object other$mj = other.getMj();
        if (!((this$mj != null) ? this$mj.equals(other$mj) : (other$mj == null))) {
            return false;
        }
        Object this$use = this.getUse();
        Object other$use = other.getUse();
        return (this$use != null) ? this$use.equals(other$use) : (other$use == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AdviceAddPro;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        Object $gg = this.getGg();
        result = result * 59 + (($gg == null) ? 43 : $gg.hashCode());
        Object $lx = this.getLx();
        result = result * 59 + (($lx == null) ? 43 : $lx.hashCode());
        Object $ll = this.getLl();
        result = result * 59 + (($ll == null) ? 43 : $ll.hashCode());
        Object $mj = this.getMj();
        result = result * 59 + (($mj == null) ? 43 : $mj.hashCode());
        Object $use = this.getUse();
        result = result * 59 + (($use == null) ? 43 : $use.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "AdviceAddPro(title=" + this.getTitle() + ", gg=" + this.getGg() + ", lx=" + this.getLx() + ", ll=" + this.getLl() + ", mj=" + this.getMj() + ", use=" + this.getUse() + ")";
    }
}
