package com.kitty.cross.core.callback;

import com.kitty.common.utils.JsonUtils;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.annotation.StringField;
import com.kitty.mina.message.Message;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

@MessageMeta(module = -3)
public class CReqCallBack extends Message {
  private int index;
  
  private int cmd;
  
  private transient Map<String, String> params = new HashMap<>();
  
  @StringField(100)
  private String data;
  
  public void addParam(String key, String value) {
    this.params.put(key, value);
  }
  
  public int getIndex() {
    return this.index;
  }
  
  public void setIndex(int index) {
    this.index = index;
  }
  
  public int getCmd() {
    return this.cmd;
  }
  
  public void setCmd(int cmd) {
    this.cmd = cmd;
  }
  
  public String getData() {
    return this.data;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  public void serialize() {
    String json = JsonUtils.object2String(this.params);
    this.data = Base64.getEncoder().encodeToString(json.getBytes());
  }
  
  public void deserialize() {
    byte[] json = Base64.getDecoder().decode(this.data);
    this.params = JsonUtils.string2Map(new String(json), String.class, String.class);
  }
  
  public Map<String, String> getParams() {
    return this.params;
  }
}
