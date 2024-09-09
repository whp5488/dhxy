package com.kitty.system.game.fightnew.messages.old;

import com.kitty.mina.annotation.MessageMeta;
import org.nutz.json.ToJson;
import com.kitty.mina.message.Message;

@ToJson
@MessageMeta(module = 32913)
public class RespFightMsgDlg extends Message
{
    private int id;
    private String msg;
    
    public RespFightMsgDlg() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public void setMsg(String msg) {
        this.msg = msg;
    }
}
