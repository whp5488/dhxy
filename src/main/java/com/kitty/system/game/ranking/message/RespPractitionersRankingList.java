
package com.kitty.system.game.ranking.message;

import java.util.ArrayList;
import com.kitty.system.game.ranking.pojo.field.PractitionersField;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42125)
public class RespPractitionersRankingList extends Message
{
    List<PractitionersField> practitionersFields;
    
    public RespPractitionersRankingList(final List<PractitionersField> practitionersFields) {
        this.practitionersFields = new ArrayList<PractitionersField>();
        this.practitionersFields = practitionersFields;
    }
    
    public List<PractitionersField> getPractitionersFields() {
        return this.practitionersFields;
    }
    
    public void setPractitionersFields(final List<PractitionersField> practitionersFields) {
        this.practitionersFields = practitionersFields;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPractitionersRankingList)) {
            return false;
        }
        final RespPractitionersRankingList other = (RespPractitionersRankingList)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$practitionersFields = this.getPractitionersFields();
        final Object other$practitionersFields = other.getPractitionersFields();
        if (this$practitionersFields == null) {
            if (other$practitionersFields == null) {
                return true;
            }
        }
        else if (this$practitionersFields.equals(other$practitionersFields)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPractitionersRankingList;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $practitionersFields = this.getPractitionersFields();
        result = result * 59 + (($practitionersFields == null) ? 43 : $practitionersFields.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPractitionersRankingList(practitionersFields=" + this.getPractitionersFields() + ")";
    }
}
