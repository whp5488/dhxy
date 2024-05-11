
package com.kitty.system.game.equip.message;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 41239)
public class RespUpgradePreview extends Message
{
    private int type;
    private int serial;
    private String name;
    private int level;
    private List<FiedValue> listOne;
    private List<FiedValue> listTwo;
    
    public RespUpgradePreview() {
        this.type = 0;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public List<FiedValue> getListOne() {
        return this.listOne;
    }
    
    public List<FiedValue> getListTwo() {
        return this.listTwo;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setListOne(final List<FiedValue> listOne) {
        this.listOne = listOne;
    }
    
    public void setListTwo(final List<FiedValue> listTwo) {
        this.listTwo = listTwo;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespUpgradePreview)) {
            return false;
        }
        final RespUpgradePreview other = (RespUpgradePreview)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0091: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0091;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0091;
            }
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        final Object this$listOne = this.getListOne();
        final Object other$listOne = other.getListOne();
        Label_0141: {
            if (this$listOne == null) {
                if (other$listOne == null) {
                    break Label_0141;
                }
            }
            else if (this$listOne.equals(other$listOne)) {
                break Label_0141;
            }
            return false;
        }
        final Object this$listTwo = this.getListTwo();
        final Object other$listTwo = other.getListTwo();
        if (this$listTwo == null) {
            if (other$listTwo == null) {
                return true;
            }
        }
        else if (this$listTwo.equals(other$listTwo)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespUpgradePreview;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getLevel();
        final Object $listOne = this.getListOne();
        result = result * 59 + (($listOne == null) ? 43 : $listOne.hashCode());
        final Object $listTwo = this.getListTwo();
        result = result * 59 + (($listTwo == null) ? 43 : $listTwo.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespUpgradePreview(type=" + this.getType() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", level=" + this.getLevel() + ", listOne=" + this.getListOne() + ", listTwo=" + this.getListTwo() + ")";
    }
}
