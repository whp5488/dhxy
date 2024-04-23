package com.kitty.cross.login.cmessage;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = -7)
public class G2CLeaveCross extends Message {
  private int code;
  
  public int getCode() {
    return this.code;
  }
  
  public void setCode(int code) {
    this.code = code;
  }
}
