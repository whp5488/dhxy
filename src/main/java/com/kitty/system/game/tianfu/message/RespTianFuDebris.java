
package com.kitty.system.game.tianfu.message;

import java.beans.ConstructorProperties;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 44001)
public class RespTianFuDebris extends Message
{
    private String id;
    private int num;
    
    public RespTianFuDebris(final RoleTianFu roleTianFu) {
        this.id = String.valueOf(roleTianFu.getId());
        this.num = roleTianFu.getCount();
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    public RespTianFuDebris() {
    }
    
    @ConstructorProperties({ "id", "num" })
    public RespTianFuDebris(final String id, final int num) {
        this.id = id;
        this.num = num;
    }
}
