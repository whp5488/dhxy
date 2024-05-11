
package com.kitty.system.game.tianfu.message;

import java.beans.ConstructorProperties;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4404)
public class ReqTianFuWear extends Message
{
    private byte type;
    private String id;
    private short grid;
    
    public byte getType() {
        return this.type;
    }
    
    public String getId() {
        return this.id;
    }
    
    public short getGrid() {
        return this.grid;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setGrid(final short grid) {
        this.grid = grid;
    }
    
    @ConstructorProperties({ "type", "id", "grid" })
    public ReqTianFuWear(final byte type, final String id, final short grid) {
        this.type = type;
        this.id = id;
        this.grid = grid;
    }
    
    public ReqTianFuWear() {
    }
}
