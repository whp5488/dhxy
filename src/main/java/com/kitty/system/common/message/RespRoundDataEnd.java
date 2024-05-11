
package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import org.nutz.json.ToJson;
import com.kitty.mina.message.Message;

@ToJson
@MessageMeta(module = 65000)
public class RespRoundDataEnd extends Message
{
    private String msg;
    private int type;
    private int fightId;
    private int round;
    
    public RespRoundDataEnd() {
        this.type = 0;
    }
    
    public RespRoundDataEnd(final int fightId, final int round, final String msg) {
        this.type = 0;
        this.fightId = fightId;
        this.round = round;
        this.msg = msg;
    }
    
    public RespRoundDataEnd(final int fightId, final int round, final int type, final String msg) {
        this.type = 0;
        this.fightId = fightId;
        this.round = round;
        this.type = type;
        this.msg = msg;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public int getFightId() {
        return this.fightId;
    }
    
    public void setFightId(final int fightId) {
        this.fightId = fightId;
    }
    
    public int getRound() {
        return this.round;
    }
    
    public void setRound(final int round) {
        this.round = round;
    }
}
