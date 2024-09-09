package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 11803)
public class ReqSavePartyFlag extends Message
{
    private short flag;
    private short flagColor;
    private short lace;
    private short emblem;
    private short emblemColor;
    private String text;
    
    public ReqSavePartyFlag() {
    }
    
    public short getFlag() {
        return this.flag;
    }
    
    public short getFlagColor() {
        return this.flagColor;
    }
    
    public short getLace() {
        return this.lace;
    }
    
    public short getEmblem() {
        return this.emblem;
    }
    
    public short getEmblemColor() {
        return this.emblemColor;
    }
    
    public String getText() {
        return this.text;
    }
    
    public void setFlag(final short flag) {
        this.flag = flag;
    }
    
    public void setFlagColor(final short flagColor) {
        this.flagColor = flagColor;
    }
    
    public void setLace(final short lace) {
        this.lace = lace;
    }
    
    public void setEmblem(final short emblem) {
        this.emblem = emblem;
    }
    
    public void setEmblemColor(final short emblemColor) {
        this.emblemColor = emblemColor;
    }
    
    public void setText(final String text) {
        this.text = text;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqSavePartyFlag)) {
            return false;
        }
        ReqSavePartyFlag other = (ReqSavePartyFlag)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getFlag() != other.getFlag()) {
            return false;
        }
        if (this.getFlagColor() != other.getFlagColor()) {
            return false;
        }
        if (this.getLace() != other.getLace()) {
            return false;
        }
        if (this.getEmblem() != other.getEmblem()) {
            return false;
        }
        if (this.getEmblemColor() != other.getEmblemColor()) {
            return false;
        }
        Object this$text = this.getText();
        Object other$text = other.getText();
        return (this$text != null) ? this$text.equals(other$text) : (other$text == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqSavePartyFlag;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFlag();
        result = result * 59 + this.getFlagColor();
        result = result * 59 + this.getLace();
        result = result * 59 + this.getEmblem();
        result = result * 59 + this.getEmblemColor();
        Object $text = this.getText();
        result = result * 59 + (($text == null) ? 43 : $text.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqSavePartyFlag(flag=" + (int)this.getFlag() + ", flagColor=" + (int)this.getFlagColor() + ", lace=" + (int)this.getLace() + ", emblem=" + (int)this.getEmblem() + ", emblemColor=" + (int)this.getEmblemColor() + ", text=" + this.getText() + ")";
    }
}
