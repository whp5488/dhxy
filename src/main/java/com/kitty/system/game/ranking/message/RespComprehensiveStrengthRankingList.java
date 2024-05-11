
package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.field.ComprehensiveStrengthRankingField;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42124)
public class RespComprehensiveStrengthRankingList extends Message
{
    private byte race;
    private byte serverId;
    List<ComprehensiveStrengthRankingField> ComprehensiveStrengthList;
    
    public RespComprehensiveStrengthRankingList(final byte race, final byte serverId, final List<ComprehensiveStrengthRankingField> comprehensiveStrengthList) {
        this.ComprehensiveStrengthList = new ArrayList<ComprehensiveStrengthRankingField>();
        this.race = race;
        this.serverId = serverId;
        this.ComprehensiveStrengthList = comprehensiveStrengthList;
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public List<ComprehensiveStrengthRankingField> getComprehensiveStrengthList() {
        return this.ComprehensiveStrengthList;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    public void setComprehensiveStrengthList(final List<ComprehensiveStrengthRankingField> ComprehensiveStrengthList) {
        this.ComprehensiveStrengthList = ComprehensiveStrengthList;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespComprehensiveStrengthRankingList)) {
            return false;
        }
        final RespComprehensiveStrengthRankingList other = (RespComprehensiveStrengthRankingList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getServerId() != other.getServerId()) {
            return false;
        }
        final Object this$ComprehensiveStrengthList = this.getComprehensiveStrengthList();
        final Object other$ComprehensiveStrengthList = other.getComprehensiveStrengthList();
        if (this$ComprehensiveStrengthList == null) {
            if (other$ComprehensiveStrengthList == null) {
                return true;
            }
        }
        else if (this$ComprehensiveStrengthList.equals(other$ComprehensiveStrengthList)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespComprehensiveStrengthRankingList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRace();
        result = result * 59 + this.getServerId();
        final Object $ComprehensiveStrengthList = this.getComprehensiveStrengthList();
        result = result * 59 + (($ComprehensiveStrengthList == null) ? 43 : $ComprehensiveStrengthList.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespComprehensiveStrengthRankingList(race=" + this.getRace() + ", serverId=" + this.getServerId() + ", ComprehensiveStrengthList=" + this.getComprehensiveStrengthList() + ")";
    }
}
