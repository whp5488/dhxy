
package com.kitty.system.game.npc.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6889)
public class RespWorship extends Message
{
    private int npcId;
    private String name;
    private String motto;
    
    public int getNpcId() {
        return this.npcId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getMotto() {
        return this.motto;
    }
    
    public void setNpcId(final int npcId) {
        this.npcId = npcId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setMotto(final String motto) {
        this.motto = motto;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespWorship)) {
            return false;
        }
        final RespWorship other = (RespWorship)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getNpcId() != other.getNpcId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$motto = this.getMotto();
        final Object other$motto = other.getMotto();
        if (this$motto == null) {
            if (other$motto == null) {
                return true;
            }
        }
        else if (this$motto.equals(other$motto)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespWorship;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getNpcId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $motto = this.getMotto();
        result = result * 59 + (($motto == null) ? 43 : $motto.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespWorship(npcId=" + this.getNpcId() + ", name=" + this.getName() + ", motto=" + this.getMotto() + ")";
    }
    
    @ConstructorProperties({ "npcId", "name", "motto" })
    public RespWorship(final int npcId, final String name, final String motto) {
        this.npcId = npcId;
        this.name = name;
        this.motto = motto;
    }
    
    public RespWorship() {
    }
}
