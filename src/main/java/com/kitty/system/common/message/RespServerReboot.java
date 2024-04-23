package com.kitty.system.common.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45565)
public class RespServerReboot extends Message {
  String tips = "由于游戏服务器维护，您的角色已断开连接。";

  public void setTips(String tips) {
    this.tips = tips;
  }
  
  public String getTips() {
    return this.tips;
  }
}
