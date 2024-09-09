package com.kitty.system.game.train.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 20200)
public class ReqUpgradeSeal extends Message
{
    private String id;
    private int type;
    private int operationType;
    private String param;
    
    public String getId() {
        return this.id;
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
    
    public void setId(final String id) {
        this.id = id;
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

    public ReqUpgradeSeal(final String id, final int type, final int operationType, final String param) {
        this.operationType = 0;
        this.id = id;
        this.type = type;
        this.operationType = operationType;
        this.param = param;
    }
    
    public ReqUpgradeSeal() {
        this.operationType = 0;
    }
}
