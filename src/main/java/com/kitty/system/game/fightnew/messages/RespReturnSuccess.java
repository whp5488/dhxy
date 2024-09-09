package com.kitty.system.game.fightnew.messages;

import com.kitty.system.game.fightnew.messages.old.RespFightCommandInfo;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 3587)
public class RespReturnSuccess extends Message
{
    private int objId;
    private byte type;
    List<RespFightCommandInfo> fightCommandInfos;
    
    public int getObjId() {
        return this.objId;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public List<RespFightCommandInfo> getFightCommandInfos() {
        return this.fightCommandInfos;
    }
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setFightCommandInfos(final List<RespFightCommandInfo> fightCommandInfos) {
        this.fightCommandInfos = fightCommandInfos;
    }
    
    public RespReturnSuccess(final int objId, final byte type, final List<RespFightCommandInfo> fightCommandInfos) {
        this.objId = objId;
        this.type = type;
        this.fightCommandInfos = fightCommandInfos;
    }
    
    public RespReturnSuccess() {
    }
}
