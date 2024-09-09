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
        this.flagList = new ArrayList<>();
        this.flagColorList = new ArrayList<>();
        this.laceList = new ArrayList<>();
        this.emblemList = new ArrayList<>();
        this.emblemColorList = new ArrayList<>();
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
        RespPartyFlag other = (RespPartyFlag)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$flagList = this.getFlagList();
        Object other$flagList = other.getFlagList();
        if (!((this$flagList != null) ? this$flagList.equals(other$flagList) : (other$flagList == null))) {
            return false;
        }
        Object this$flagColorList = this.getFlagColorList();
        Object other$flagColorList = other.getFlagColorList();
        if (!((this$flagColorList != null) ? this$flagColorList.equals(other$flagColorList) : (other$flagColorList == null))) {
            return false;
        }
        Object this$laceList = this.getLaceList();
        Object other$laceList = other.getLaceList();
        if (!((this$laceList != null) ? this$laceList.equals(other$laceList) : (other$laceList == null))) {
            return false;
        }
        Object this$emblemList = this.getEmblemList();
        Object other$emblemList = other.getEmblemList();
        if (!((this$emblemList != null) ? this$emblemList.equals(other$emblemList) : (other$emblemList == null))) {
            return false;
        }
        Object this$emblemColorList = this.getEmblemColorList();
        Object other$emblemColorList = other.getEmblemColorList();
        if (!((this$emblemColorList != null) ? this$emblemColorList.equals(other$emblemColorList) : (other$emblemColorList == null))) {
            return false;
        }
        Object this$text = this.getText();
        Object other$text = other.getText();
        return (this$text != null) ? this$text.equals(other$text) : (other$text == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPartyFlag;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $flagList = this.getFlagList();
        result = result * 59 + (($flagList == null) ? 43 : $flagList.hashCode());
        Object $flagColorList = this.getFlagColorList();
        result = result * 59 + (($flagColorList == null) ? 43 : $flagColorList.hashCode());
        Object $laceList = this.getLaceList();
        result = result * 59 + (($laceList == null) ? 43 : $laceList.hashCode());
        Object $emblemList = this.getEmblemList();
        result = result * 59 + (($emblemList == null) ? 43 : $emblemList.hashCode());
        Object $emblemColorList = this.getEmblemColorList();
        result = result * 59 + (($emblemColorList == null) ? 43 : $emblemColorList.hashCode());
        Object $text = this.getText();
        result = result * 59 + (($text == null) ? 43 : $text.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPartyFlag(flagList=" + this.getFlagList() + ", flagColorList=" + this.getFlagColorList() + ", laceList=" + this.getLaceList() + ", emblemList=" + this.getEmblemList() + ", emblemColorList=" + this.getEmblemColorList() + ", text=" + this.getText() + ")";
    }
}
