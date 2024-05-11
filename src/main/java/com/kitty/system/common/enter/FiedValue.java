
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
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public byte getVT() {
        return this.VT;
    }
    
    public void setVT(final byte VT) {
        this.VT = VT;
    }
    
    public Object getValue() {
        return this.value;
    }
    
    public void setValue(final Object value) {
        this.value = value;
    }
    
    public FiedValue() {
    }
    
    public FiedValue(final String type, final byte VT, final Object value) {
        this.type = type;
        this.VT = VT;
        this.value = value;
    }
}
