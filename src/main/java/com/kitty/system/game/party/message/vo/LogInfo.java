
package com.kitty.system.game.party.message.vo;

import java.beans.ConstructorProperties;
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
        final LogInfo other = (LogInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null) {
            if (other$content == null) {
                return true;
            }
        }
        else if (this$content.equals(other$content)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof LogInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "LogInfo(content=" + this.getContent() + ")";
    }
    
    public LogInfo() {
    }
    
    @ConstructorProperties({ "content" })
    public LogInfo(final String content) {
        this.content = content;
    }
}
