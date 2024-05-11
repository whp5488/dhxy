
package com.kitty.system.game.general.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8254)
public class ReqAssignAttrib extends Message
{
    private int id;
    private int gg;
    private int lx;
    private int ll;
    private int mj;
    
    public ReqAssignAttrib() {
        this.gg = 0;
        this.lx = 0;
        this.ll = 0;
        this.mj = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getGg() {
        return this.gg;
    }
    
    public int getLx() {
        return this.lx;
    }
    
    public int getLl() {
        return this.ll;
    }
    
    public int getMj() {
        return this.mj;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setGg(final int gg) {
        this.gg = gg;
    }
    
    public void setLx(final int lx) {
        this.lx = lx;
    }
    
    public void setLl(final int ll) {
        this.ll = ll;
    }
    
    public void setMj(final int mj) {
        this.mj = mj;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqAssignAttrib)) {
            return false;
        }
        final ReqAssignAttrib other = (ReqAssignAttrib)o;
        return other.canEqual(this) && this.getId() == other.getId() && this.getGg() == other.getGg() && this.getLx() == other.getLx() && this.getLl() == other.getLl() && this.getMj() == other.getMj();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqAssignAttrib;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getGg();
        result = result * 59 + this.getLx();
        result = result * 59 + this.getLl();
        result = result * 59 + this.getMj();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqAssignAttrib(id=" + this.getId() + ", gg=" + this.getGg() + ", lx=" + this.getLx() + ", ll=" + this.getLl() + ", mj=" + this.getMj() + ")";
    }
}
