
package com.kitty.system.game.npc.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45057)
public class ReqTaskTalk extends Message
{
    public static final short JUMP_TALK = 1;
    public static final short NEXT_TALK = 2;
    public static final short END_TALK = 3;
    private int talkId;
    private short type;
    private String para;
    
    public int getTalkId() {
        return this.talkId;
    }
    
    public void setTalkId(final int talkId) {
        this.talkId = talkId;
    }
    
    public short getType() {
        return this.type;
    }
    
    public void setType(final short type) {
        this.type = type;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
}
