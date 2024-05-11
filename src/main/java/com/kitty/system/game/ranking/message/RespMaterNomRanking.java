
package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.MaterRank;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42128)
public class RespMaterNomRanking extends Message
{
    public List<MaterRank> list;
    
    public RespMaterNomRanking() {
        this.list = new ArrayList<MaterRank>(100);
    }
    
    public List<MaterRank> getList() {
        return this.list;
    }
    
    public void setList(final List<MaterRank> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMaterNomRanking)) {
            return false;
        }
        final RespMaterNomRanking other = (RespMaterNomRanking)o;
        if (!other.canEqual(this)) {
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
        return other instanceof RespMaterNomRanking;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMaterNomRanking(list=" + this.getList() + ")";
    }
}
