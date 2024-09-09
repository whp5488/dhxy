package com.kitty.system.game.train.message;

import com.kitty.system.game.train.helper.vo.SealNeo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20169)
public class RespPreUpgradeSeal extends Message
{
    private String id;
    private List<SealNeo> list;
    private byte ask;
    
    public String getId() {
        return this.id;
    }
    
    public List<SealNeo> getList() {
        return this.list;
    }
    
    public byte getAsk() {
        return this.ask;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setList(final List<SealNeo> list) {
        this.list = list;
    }
    
    public void setAsk(final byte ask) {
        this.ask = ask;
    }

    public RespPreUpgradeSeal() {
    }
    
    public RespPreUpgradeSeal(final String id, final List<SealNeo> list, final byte ask) {
        this.id = id;
        this.list = list;
        this.ask = ask;
    }
}
