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
        this.normal = new ArrayList<>();
        this.senior = new ArrayList<>();
        this.supreme = new ArrayList<>();
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
        RespTreasureAwardInfo other = (RespTreasureAwardInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$normal = this.getNormal();
        Object other$normal = other.getNormal();
        if (!((this$normal != null) ? this$normal.equals(other$normal) : (other$normal == null))) {
            return false;
        }
        Object this$senior = this.getSenior();
        Object other$senior = other.getSenior();
        if (!((this$senior != null) ? this$senior.equals(other$senior) : (other$senior == null))) {
            return false;
        }
        Object this$supreme = this.getSupreme();
        Object other$supreme = other.getSupreme();
        return (this$supreme != null) ? this$supreme.equals(other$supreme) : (other$supreme == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespTreasureAwardInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $normal = this.getNormal();
        result = result * 59 + (($normal == null) ? 43 : $normal.hashCode());
        Object $senior = this.getSenior();
        result = result * 59 + (($senior == null) ? 43 : $senior.hashCode());
        Object $supreme = this.getSupreme();
        result = result * 59 + (($supreme == null) ? 43 : $supreme.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespTreasureAwardInfo(normal=" + this.getNormal() + ", senior=" + this.getSenior() + ", supreme=" + this.getSupreme() + ")";
    }
}
