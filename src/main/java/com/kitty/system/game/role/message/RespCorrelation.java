package com.kitty.system.game.role.message;

import com.kitty.common.utils.CodeUtils;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 107)
public class RespCorrelation extends Message {
  private String mobile;
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  public RespCorrelation() {}
  
  public RespCorrelation(String mobile) {
    this.mobile = CodeUtils.phoneMask(mobile);
  }
}
