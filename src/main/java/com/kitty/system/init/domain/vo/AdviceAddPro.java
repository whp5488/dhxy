
package com.kitty.system.init.domain.vo;

public class AdviceAddPro
{
    private String title;
    private Integer gg;
    private Integer lx;
    private Integer ll;
    private Integer mj;
    private Integer use;
    
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
        final AdviceAddPro other = (AdviceAddPro)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        Label_0065: {
            if (this$title == null) {
                if (other$title == null) {
                    break Label_0065;
                }
            }
            else if (this$title.equals(other$title)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$gg = this.getGg();
        final Object other$gg = other.getGg();
        Label_0102: {
            if (this$gg == null) {
                if (other$gg == null) {
                    break Label_0102;
                }
            }
            else if (this$gg.equals(other$gg)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$lx = this.getLx();
        final Object other$lx = other.getLx();
        Label_0139: {
            if (this$lx == null) {
                if (other$lx == null) {
                    break Label_0139;
                }
            }
            else if (this$lx.equals(other$lx)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$ll = this.getLl();
        final Object other$ll = other.getLl();
        Label_0176: {
            if (this$ll == null) {
                if (other$ll == null) {
                    break Label_0176;
                }
            }
            else if (this$ll.equals(other$ll)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$mj = this.getMj();
        final Object other$mj = other.getMj();
        Label_0213: {
            if (this$mj == null) {
                if (other$mj == null) {
                    break Label_0213;
                }
            }
            else if (this$mj.equals(other$mj)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$use = this.getUse();
        final Object other$use = other.getUse();
        if (this$use == null) {
            if (other$use == null) {
                return true;
            }
        }
        else if (this$use.equals(other$use)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof AdviceAddPro;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        final Object $gg = this.getGg();
        result = result * 59 + (($gg == null) ? 43 : $gg.hashCode());
        final Object $lx = this.getLx();
        result = result * 59 + (($lx == null) ? 43 : $lx.hashCode());
        final Object $ll = this.getLl();
        result = result * 59 + (($ll == null) ? 43 : $ll.hashCode());
        final Object $mj = this.getMj();
        result = result * 59 + (($mj == null) ? 43 : $mj.hashCode());
        final Object $use = this.getUse();
        result = result * 59 + (($use == null) ? 43 : $use.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "AdviceAddPro(title=" + this.getTitle() + ", gg=" + this.getGg() + ", lx=" + this.getLx() + ", ll=" + this.getLl() + ", mj=" + this.getMj() + ", use=" + this.getUse() + ")";
    }
}
