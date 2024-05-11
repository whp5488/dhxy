
package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.CaRank;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42130)
public class RespCaNomRanking extends Message
{
    private byte type;
    private byte classify;
    public List<CaRank> list;
    
    public RespCaNomRanking() {
        this.type = 0;
        this.classify = 0;
        this.list = new ArrayList<CaRank>(100);
    }
    
    public byte getType() {
        return this.type;
    }
    
    public byte getClassify() {
        return this.classify;
    }
    
    public List<CaRank> getList() {
        return this.list;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setClassify(final byte classify) {
        this.classify = classify;
    }
    
    public void setList(final List<CaRank> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespCaNomRanking)) {
            return false;
        }
        final RespCaNomRanking other = (RespCaNomRanking)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getClassify() != other.getClassify()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespCaNomRanking;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getClassify();
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespCaNomRanking(type=" + this.getType() + ", classify=" + this.getClassify() + ", list=" + this.getList() + ")";
    }
}
