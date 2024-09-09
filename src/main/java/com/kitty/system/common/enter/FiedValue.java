package com.kitty.system.common.enter;

import com.alibaba.fastjson.annotation.JSONField;
import java.io.Serializable;

public class FiedValue implements Serializable
{
    private String type;
    @JSONField(name = "VT")
    private byte VT;
    private Object value;
    
    public String getType() {
        return this.type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public byte getVT() {
        return this.VT;
    }
    
    public void setVT(byte VT) {
        this.VT = VT;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(Object value) {
        this.value = value;
    }
    
    public FiedValue() {
    }
    
    public FiedValue(String type, byte VT, Object value) {
        this.type = type;
        this.VT = VT;
        this.value = value;
    }
}
