package com.kitty.system.game.mind.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20300)
public class ReqUpgradeMind extends Message
{
    private int serial;
    private int type;
    private int operationType;
    private String param;
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getOperationType() {
        return this.operationType;
    }
    
    public String getParam() {
        return this.param;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setOperationType(final int operationType) {
        this.operationType = operationType;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    public ReqUpgradeMind(final int serial, final int type, final int operationType, final String param) {
        this.operationType = 0;
        this.serial = serial;
        this.type = type;
        this.operationType = operationType;
        this.param = param;
    }
    
    public ReqUpgradeMind() {
        this.operationType = 0;
    }
}
