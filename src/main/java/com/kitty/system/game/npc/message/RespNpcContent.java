package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8247)
public class RespNpcContent extends Message
{
    private int id;
    private int icon;
    private String npcName;
    private String talk;
    private String content;
    
    public RespNpcContent() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public String getNpcName() {
        return this.npcName;
    }
    
    public String getTalk() {
        return this.talk;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setNpcName(final String npcName) {
        this.npcName = npcName;
    }
    
    public void setTalk(final String talk) {
        this.talk = talk;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespNpcContent)) {
            return false;
        }
        RespNpcContent other = (RespNpcContent)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        Object this$npcName = this.getNpcName();
        Object other$npcName = other.getNpcName();
        if (!((this$npcName != null) ? this$npcName.equals(other$npcName) : (other$npcName == null))) {
            return false;
        }
        Object this$talk = this.getTalk();
        Object other$talk = other.getTalk();
        if (!((this$talk != null) ? this$talk.equals(other$talk) : (other$talk == null))) {
            return false;
        }
        Object this$content = this.getContent();
        Object other$content = other.getContent();
        return (this$content != null) ? this$content.equals(other$content) : (other$content == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespNpcContent;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        result = result * 59 + this.getIcon();
        Object $npcName = this.getNpcName();
        result = result * 59 + (($npcName == null) ? 43 : $npcName.hashCode());
        Object $talk = this.getTalk();
        result = result * 59 + (($talk == null) ? 43 : $talk.hashCode());
        Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespNpcContent(id=" + this.getId() + ", icon=" + this.getIcon() + ", npcName=" + this.getNpcName() + ", talk=" + this.getTalk() + ", content=" + this.getContent() + ")";
    }
}
