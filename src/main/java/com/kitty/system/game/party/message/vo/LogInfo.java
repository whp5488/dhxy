package com.kitty.system.game.party.message.vo;

import com.kitty.mina.annotation.StringField;
import java.io.Serializable;

public class LogInfo implements Serializable
{
    @StringField(1)
    private String content;
    
    public String getContent() {
        return this.content;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LogInfo)) {
            return false;
        }
        LogInfo other = (LogInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$content = this.getContent();
        Object other$content = other.getContent();
        return (this$content != null) ? this$content.equals(other$content) : (other$content == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof LogInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "LogInfo(content=" + this.getContent() + ")";
    }
    
    public LogInfo() {
    }
    
    public LogInfo(final String content) {
        this.content = content;
    }
}
