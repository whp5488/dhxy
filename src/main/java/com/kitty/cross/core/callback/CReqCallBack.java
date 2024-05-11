
package com.kitty.cross.core.callback;

import java.util.Base64;
import com.kitty.common.utils.JsonUtils;
import java.util.HashMap;
import com.kitty.mina.annotation.StringField;
import java.util.Map;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -3)
public class CReqCallBack extends Message
{
    private int index;
    private int cmd;
    private transient Map<String, String> params;
    @StringField(100)
    private String data;
    
    public CReqCallBack() {
        this.params = new HashMap<String, String>();
    }
    
    public void addParam(final String key, final String value) {
        this.params.put(key, value);
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public void setIndex(final int index) {
        this.index = index;
    }
    
    public int getCmd() {
        return this.cmd;
    }
    
    public void setCmd(final int cmd) {
        this.cmd = cmd;
    }
    
    public String getData() {
        return this.data;
    }
    
    public void setData(final String data) {
        this.data = data;
    }
    
    public void serialize() {
        final String json = JsonUtils.object2String(this.params);
        this.data = Base64.getEncoder().encodeToString(json.getBytes());
    }
    
    public void deserialize() {
        final byte[] json = Base64.getDecoder().decode(this.data);
        this.params = JsonUtils.string2Map(new String(json), String.class, String.class);
    }
    
    public Map<String, String> getParams() {
        return this.params;
    }
}
