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
        this.list = new ArrayList(100);
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
        RespMaterNomRanking other = (RespMaterNomRanking)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMaterNomRanking;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMaterNomRanking(list=" + this.getList() + ")";
    }
}
