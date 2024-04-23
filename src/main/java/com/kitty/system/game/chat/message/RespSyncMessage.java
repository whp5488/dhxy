package com.kitty.system.game.chat.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.util.Arrays;

@MessageMeta(module = 64977)
public class RespSyncMessage extends Message {
  private int code;
  
  private byte[] msg;
  
  public void setCode(int code) {
    this.code = code;
  }
  
  public void setMsg(byte[] msg) {
    this.msg = msg;
  }
  
  public int getCode() {
    return this.code;
  }
  
  public byte[] getMsg() {
    return this.msg;
  }
}
