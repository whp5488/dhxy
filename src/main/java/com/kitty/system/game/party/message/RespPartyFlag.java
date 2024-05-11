
package com.kitty.system.game.party.message;

import java.util.ArrayList;
import com.kitty.system.game.party.message.vo.PartyFlag;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11802)
public class RespPartyFlag extends Message
{
    private List<PartyFlag> flagList;
    private List<PartyFlag> flagColorList;
    private List<PartyFlag> laceList;
    private List<PartyFlag> emblemList;
    private List<PartyFlag> emblemColorList;
    private String text;
    
    public RespPartyFlag() {
        this.flagList = new ArrayList<PartyFlag>();
        this.flagColorList = new ArrayList<PartyFlag>();
        this.laceList = new ArrayList<PartyFlag>();
        this.emblemList = new ArrayList<PartyFlag>();
        this.emblemColorList = new ArrayList<PartyFlag>();
    }
    
    public List<PartyFlag> getFlagList() {
        return this.flagList;
    }
    
    public List<PartyFlag> getFlagColorList() {
        return this.flagColorList;
    }
    
    public List<PartyFlag> getLaceList() {
        return this.laceList;
    }
    
    public List<PartyFlag> getEmblemList() {
        return this.emblemList;
    }
    
    public List<PartyFlag> getEmblemColorList() {
        return this.emblemColorList;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setFlagList(final List<PartyFlag> flagList) {
        this.flagList = flagList;
    }
    
    public void setFlagColorList(final List<PartyFlag> flagColorList) {
        this.flagColorList = flagColorList;
    }
    
    public void setLaceList(final List<PartyFlag> laceList) {
        this.laceList = laceList;
    }
    
    public void setEmblemList(final List<PartyFlag> emblemList) {
        this.emblemList = emblemList;
    }
    
    public void setEmblemColorList(final List<PartyFlag> emblemColorList) {
        this.emblemColorList = emblemColorList;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPartyFlag)) {
            return false;
        }
        final RespPartyFlag other = (RespPartyFlag)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$flagList = this.getFlagList();
        final Object other$flagList = other.getFlagList();
        Label_0065: {
            if (this$flagList == null) {
                if (other$flagList == null) {
                    break Label_0065;
                }
            }
            else if (this$flagList.equals(other$flagList)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$flagColorList = this.getFlagColorList();
        final Object other$flagColorList = other.getFlagColorList();
        Label_0102: {
            if (this$flagColorList == null) {
                if (other$flagColorList == null) {
                    break Label_0102;
                }
            }
            else if (this$flagColorList.equals(other$flagColorList)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$laceList = this.getLaceList();
        final Object other$laceList = other.getLaceList();
        Label_0139: {
            if (this$laceList == null) {
                if (other$laceList == null) {
                    break Label_0139;
                }
            }
            else if (this$laceList.equals(other$laceList)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$emblemList = this.getEmblemList();
        final Object other$emblemList = other.getEmblemList();
        Label_0176: {
            if (this$emblemList == null) {
                if (other$emblemList == null) {
                    break Label_0176;
                }
            }
            else if (this$emblemList.equals(other$emblemList)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$emblemColorList = this.getEmblemColorList();
        final Object other$emblemColorList = other.getEmblemColorList();
        Label_0213: {
            if (this$emblemColorList == null) {
                if (other$emblemColorList == null) {
                    break Label_0213;
                }
            }
            else if (this$emblemColorList.equals(other$emblemColorList)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$text = this.getText();
        final Object other$text = other.getText();
        if (this$text == null) {
            if (other$text == null) {
                return true;
            }
        }
        else if (this$text.equals(other$text)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartyFlag;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $flagList = this.getFlagList();
        result = result * 59 + (($flagList == null) ? 43 : $flagList.hashCode());
        final Object $flagColorList = this.getFlagColorList();
        result = result * 59 + (($flagColorList == null) ? 43 : $flagColorList.hashCode());
        final Object $laceList = this.getLaceList();
        result = result * 59 + (($laceList == null) ? 43 : $laceList.hashCode());
        final Object $emblemList = this.getEmblemList();
        result = result * 59 + (($emblemList == null) ? 43 : $emblemList.hashCode());
        final Object $emblemColorList = this.getEmblemColorList();
        result = result * 59 + (($emblemColorList == null) ? 43 : $emblemColorList.hashCode());
        final Object $text = this.getText();
        result = result * 59 + (($text == null) ? 43 : $text.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyFlag(flagList=" + this.getFlagList() + ", flagColorList=" + this.getFlagColorList() + ", laceList=" + this.getLaceList() + ", emblemList=" + this.getEmblemList() + ", emblemColorList=" + this.getEmblemColorList() + ", text=" + this.getText() + ")";
    }
}
