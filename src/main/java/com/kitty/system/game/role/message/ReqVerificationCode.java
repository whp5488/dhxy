package com.kitty.system.game.role.message;

import com.kitty.common.utils.CodeUtils;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 105)
public class ReqVerificationCode extends Message {
  private String mobile;

  public ReqVerificationCode() {}
  
  public ReqVerificationCode(String mobile) {
    this.mobile = CodeUtils.phoneMask(mobile);
  }
}
