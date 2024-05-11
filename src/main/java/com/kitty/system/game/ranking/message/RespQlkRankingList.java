
package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.field.QlkRankingField;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42127)
public class RespQlkRankingList extends Message
{
    private short qlkType;
    List<QlkRankingField> qlkRankingFields;
    
    public RespQlkRankingList(final short qlkType, final List<QlkRankingField> qlkRankingFields) {
        this.qlkRankingFields = new ArrayList<QlkRankingField>();
        this.qlkType = qlkType;
        this.qlkRankingFields = qlkRankingFields;
    }
    
    public short getQlkType() {
        return this.qlkType;
    }
    
    public List<QlkRankingField> getQlkRankingFields() {
        return this.qlkRankingFields;
    }
    
    public void setQlkType(final short qlkType) {
        this.qlkType = qlkType;
    }
    
    public void setQlkRankingFields(final List<QlkRankingField> qlkRankingFields) {
        this.qlkRankingFields = qlkRankingFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespQlkRankingList)) {
            return false;
        }
        final RespQlkRankingList other = (RespQlkRankingList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getQlkType() != other.getQlkType()) {
            return false;
        }
        final Object this$qlkRankingFields = this.getQlkRankingFields();
        final Object other$qlkRankingFields = other.getQlkRankingFields();
        if (this$qlkRankingFields == null) {
            if (other$qlkRankingFields == null) {
                return true;
            }
        }
        else if (this$qlkRankingFields.equals(other$qlkRankingFields)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespQlkRankingList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getQlkType();
        final Object $qlkRankingFields = this.getQlkRankingFields();
        result = result * 59 + (($qlkRankingFields == null) ? 43 : $qlkRankingFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespQlkRankingList(qlkType=" + this.getQlkType() + ", qlkRankingFields=" + this.getQlkRankingFields() + ")";
    }
}
