package com.kitty.system.game.equip.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 32779)
public class RespPetEquipDisplace extends Message
{
    private String main;
    private String second;
    
    public String getMain() {
        return this.main;
    }
    
    public String getSecond() {
        return this.second;
    }
    
    public void setMain(final String main) {
        this.main = main;
    }
    
    public void setSecond(final String second) {
        this.second = second;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespPetEquipDisplace)) {
            return false;
        }
        RespPetEquipDisplace other = (RespPetEquipDisplace)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$main = this.getMain();
        Object other$main = other.getMain();
        if (!((this$main != null) ? this$main.equals(other$main) : (other$main == null))) {
            return false;
        }
        Object this$second = this.getSecond();
        Object other$second = other.getSecond();
        return (this$second != null) ? this$second.equals(other$second) : (other$second == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetEquipDisplace;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $main = this.getMain();
        result = result * 59 + (($main == null) ? 43 : $main.hashCode());
        Object $second = this.getSecond();
        result = result * 59 + (($second == null) ? 43 : $second.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetEquipDisplace(main=" + this.getMain() + ", second=" + this.getSecond() + ")";
    }
    
    public RespPetEquipDisplace(final String main, final String second) {
        this.main = main;
        this.second = second;
    }
    
    public RespPetEquipDisplace() {
    }
}
