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
        this.left = new ArrayList<>();
        this.right = new ArrayList<>();
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
}
