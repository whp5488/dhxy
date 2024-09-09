package com.kitty.system.common.message;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20481)
public class RespNotifyMiscEx extends Message
{
    private String msg;
    private int time;
    
    public RespNotifyMiscEx setMsg(String msg) {
        this.msg = msg;
        return this;
    }
    
    public RespNotifyMiscEx(Long id, String msg, boolean flag) {
        this.msg = msg;
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (flag) {
            ((ChatService)SpringUtils.getBean(ChatService.class)).sendInfo(id, msg);
        }
    }
    
    public RespNotifyMiscEx(String msg) {
        this.msg = msg;
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
    }
    
    public RespNotifyMiscEx() {
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
}
