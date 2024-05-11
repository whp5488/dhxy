
package com.kitty.system.game.welfare.message;

import java.util.ArrayList;
import com.kitty.system.game.welfare.message.vo.MagicBox;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45387)
public class RespMagicBoxInfo extends Message
{
    private int affiliation;
    private byte type;
    private int serial;
    private List<MagicBox> left;
    private List<MagicBox> right;
    
    public RespMagicBoxInfo() {
        this.type = -1;
        this.serial = 0;
        this.left = new ArrayList<MagicBox>();
        this.right = new ArrayList<MagicBox>();
    }
    
    public int getAffiliation() {
        return this.affiliation;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<MagicBox> getLeft() {
        return this.left;
    }
    
    public List<MagicBox> getRight() {
        return this.right;
    }
    
    public void setAffiliation(final int affiliation) {
        this.affiliation = affiliation;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setLeft(final List<MagicBox> left) {
        this.left = left;
    }
    
    public void setRight(final List<MagicBox> right) {
        this.right = right;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespMagicBoxInfo)) {
            return false;
        }
        final RespMagicBoxInfo other = (RespMagicBoxInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getAffiliation() != other.getAffiliation()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$left = this.getLeft();
        final Object other$left = other.getLeft();
        Label_0104: {
            if (this$left == null) {
                if (other$left == null) {
                    break Label_0104;
                }
            }
            else if (this$left.equals(other$left)) {
                break Label_0104;
            }
            return false;
        }
        final Object this$right = this.getRight();
        final Object other$right = other.getRight();
        if (this$right == null) {
            if (other$right == null) {
                return true;
            }
        }
        else if (this$right.equals(other$right)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespMagicBoxInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getAffiliation();
        result = result * 59 + this.getType();
        result = result * 59 + this.getSerial();
        final Object $left = this.getLeft();
        result = result * 59 + (($left == null) ? 43 : $left.hashCode());
        final Object $right = this.getRight();
        result = result * 59 + (($right == null) ? 43 : $right.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespMagicBoxInfo(affiliation=" + this.getAffiliation() + ", type=" + this.getType() + ", serial=" + this.getSerial() + ", left=" + this.getLeft() + ", right=" + this.getRight() + ")";
    }
}
