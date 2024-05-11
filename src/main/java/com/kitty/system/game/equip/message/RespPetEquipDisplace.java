
package com.kitty.system.game.equip.message;

import java.beans.ConstructorProperties;
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
        final RespPetEquipDisplace other = (RespPetEquipDisplace)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$main = this.getMain();
        final Object other$main = other.getMain();
        Label_0065: {
            if (this$main == null) {
                if (other$main == null) {
                    break Label_0065;
                }
            }
            else if (this$main.equals(other$main)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$second = this.getSecond();
        final Object other$second = other.getSecond();
        if (this$second == null) {
            if (other$second == null) {
                return true;
            }
        }
        else if (this$second.equals(other$second)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespPetEquipDisplace;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $main = this.getMain();
        result = result * 59 + (($main == null) ? 43 : $main.hashCode());
        final Object $second = this.getSecond();
        result = result * 59 + (($second == null) ? 43 : $second.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespPetEquipDisplace(main=" + this.getMain() + ", second=" + this.getSecond() + ")";
    }
    
    @ConstructorProperties({ "main", "second" })
    public RespPetEquipDisplace(final String main, final String second) {
        this.main = main;
        this.second = second;
    }
    
    public RespPetEquipDisplace() {
    }
}
