
package com.kitty.system.game.tianfu.message;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 44000)
public class RespTianFuList extends Message
{
    private byte type;
    private List<RoleTianFuClient> tianFuList;
    
    public byte getType() {
        return this.type;
    }
    
    public List<RoleTianFuClient> getTianFuList() {
        return this.tianFuList;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setTianFuList(final List<RoleTianFuClient> tianFuList) {
        this.tianFuList = tianFuList;
    }
    
    @ConstructorProperties({ "type", "tianFuList" })
    public RespTianFuList(final byte type, final List<RoleTianFuClient> tianFuList) {
        this.type = 0;
        this.tianFuList = new ArrayList<RoleTianFuClient>();
        this.type = type;
        this.tianFuList = tianFuList;
    }
    
    public RespTianFuList() {
        this.type = 0;
        this.tianFuList = new ArrayList<RoleTianFuClient>();
    }
}
