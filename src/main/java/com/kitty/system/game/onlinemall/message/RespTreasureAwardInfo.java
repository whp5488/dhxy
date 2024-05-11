
package com.kitty.system.game.onlinemall.message;

import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8412)
public class RespTreasureAwardInfo extends Message
{
    private List<FieldProp> normal;
    private List<FieldProp> senior;
    private List<FieldProp> supreme;
    
    public RespTreasureAwardInfo() {
        this.normal = new ArrayList<FieldProp>();
        this.senior = new ArrayList<FieldProp>();
        this.supreme = new ArrayList<FieldProp>();
    }
    
    public List<FieldProp> getNormal() {
        return this.normal;
    }
    
    public List<FieldProp> getSenior() {
        return this.senior;
    }
    
    public List<FieldProp> getSupreme() {
        return this.supreme;
    }
    
    public void setNormal(final List<FieldProp> normal) {
        this.normal = normal;
    }
    
    public void setSenior(final List<FieldProp> senior) {
        this.senior = senior;
    }
    
    public void setSupreme(final List<FieldProp> supreme) {
        this.supreme = supreme;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespTreasureAwardInfo)) {
            return false;
        }
        final RespTreasureAwardInfo other = (RespTreasureAwardInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$normal = this.getNormal();
        final Object other$normal = other.getNormal();
        Label_0065: {
            if (this$normal == null) {
                if (other$normal == null) {
                    break Label_0065;
                }
            }
            else if (this$normal.equals(other$normal)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$senior = this.getSenior();
        final Object other$senior = other.getSenior();
        Label_0102: {
            if (this$senior == null) {
                if (other$senior == null) {
                    break Label_0102;
                }
            }
            else if (this$senior.equals(other$senior)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$supreme = this.getSupreme();
        final Object other$supreme = other.getSupreme();
        if (this$supreme == null) {
            if (other$supreme == null) {
                return true;
            }
        }
        else if (this$supreme.equals(other$supreme)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTreasureAwardInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $normal = this.getNormal();
        result = result * 59 + (($normal == null) ? 43 : $normal.hashCode());
        final Object $senior = this.getSenior();
        result = result * 59 + (($senior == null) ? 43 : $senior.hashCode());
        final Object $supreme = this.getSupreme();
        result = result * 59 + (($supreme == null) ? 43 : $supreme.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTreasureAwardInfo(normal=" + this.getNormal() + ", senior=" + this.getSenior() + ", supreme=" + this.getSupreme() + ")";
    }
}
