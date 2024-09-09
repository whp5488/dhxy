package com.kitty.cross.core.callback;

import com.kitty.common.utils.JsonUtils;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -4)
public class CRespCallBack extends Message
{
    private int index;
    private String data;
    private String msgClass;
    
    public CRespCallBack() {
    }
    
    public static CRespCallBack valueOf(Message message) {
        CRespCallBack response = new CRespCallBack();
        response.data = JsonUtils.object2String(message);
        response.msgClass = message.getClass().getName();
        return response;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(int index) {
        this.index = index;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(String data) {
        this.data = data;
    }
    
    public String getMsgClass() {
        return this.msgClass;
    }
    
    public void setMsgClass(String msgClass) {
        this.msgClass = msgClass;
    }
}
