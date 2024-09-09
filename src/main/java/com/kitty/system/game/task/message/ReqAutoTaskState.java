package com.kitty.system.game.task.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 53958)
public class ReqAutoTaskState extends Message
{
    private byte actionType;
    private byte stage;
    
    public ReqAutoTaskState() {
    }
    
    public void setActionType(final byte actionType) {
        this.actionType = actionType;
    }
    
    public void setStage(final byte stage) {
        this.stage = stage;
    }
    
    public byte getActionType() {
        return this.actionType;
    }
    
    public byte getStage() {
        return this.stage;
    }
}
