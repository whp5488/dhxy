package com.kitty.system.game.ranking.message;

import com.kitty.system.game.ranking.pojo.RankingField;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42122)
public class RespMyRanking extends Message
{
    private byte type;
    private int serial;
    private byte serverId;
    private RankingField rankingFields;
    
    public RespMyRanking(byte type, int serial, byte serverId, RankingField rankingFields) {
        this.type = type;
        this.serial = serial;
        this.serverId = serverId;
        this.rankingFields = rankingFields;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getServerId() {
        return this.serverId;
    }
    
    public RankingField getRankingFields() {
        return this.rankingFields;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setServerId(final byte serverId) {
        this.serverId = serverId;
    }
    
    public void setRankingFields(final RankingField rankingFields) {
        this.rankingFields = rankingFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMyRanking)) {
            return false;
        }
        RespMyRanking other = (RespMyRanking)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getServerId() != other.getServerId()) {
            return false;
        }
        Object this$rankingFields = this.getRankingFields();
        Object other$rankingFields = other.getRankingFields();
        return (this$rankingFields != null) ? this$rankingFields.equals(other$rankingFields) : (other$rankingFields == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMyRanking;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getServerId();
        Object $rankingFields = this.getRankingFields();
        result = result * 59 + (($rankingFields == null) ? 43 : $rankingFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMyRanking(type=" + (int)this.getType() + ", serial=" + this.getSerial() + ", serverId=" + (int)this.getServerId() + ", rankingFields=" + this.getRankingFields() + ")";
    }
}
