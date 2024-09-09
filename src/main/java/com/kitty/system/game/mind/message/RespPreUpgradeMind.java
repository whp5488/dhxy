package com.kitty.system.game.mind.message;

import com.kitty.system.game.mind.helper.vo.MindNeo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20301)
public class RespPreUpgradeMind extends Message
{
    private int serial;
    private List<MindNeo> list;
    private byte ask;
    
    public int getSerial() {
        return this.serial;
    }
    
    public List<MindNeo> getList() {
        return this.list;
    }
    
    public byte getAsk() {
        return this.ask;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setList(final List<MindNeo> list) {
        this.list = list;
    }
    
    public void setAsk(final byte ask) {
        this.ask = ask;
    }
    
    public RespPreUpgradeMind() {
    }
    
    public RespPreUpgradeMind(final int serial, final List<MindNeo> list, final byte ask) {
        this.serial = serial;
        this.list = list;
        this.ask = ask;
    }
}
