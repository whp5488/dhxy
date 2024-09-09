package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.MaterTeamRank;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42129)
public class RespMaterTeamRanking extends Message
{
    public List<MaterTeamRank> list;
    
    public RespMaterTeamRanking() {
        this.list = new ArrayList(100);
    }
    
    public List<MaterTeamRank> getList() {
        return this.list;
    }
    
    public void setList(final List<MaterTeamRank> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMaterTeamRanking)) {
            return false;
        }
        RespMaterTeamRanking other = (RespMaterTeamRanking)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        return (this$list != null) ? this$list.equals(other$list) : (other$list == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMaterTeamRanking;
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
        return "RespMaterTeamRanking(list=" + this.getList() + ")";
    }
}
