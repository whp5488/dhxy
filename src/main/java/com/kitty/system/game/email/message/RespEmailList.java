package com.kitty.system.game.email.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.email.message.vo.EmailParam;
import java.util.List;

@MessageMeta(module = 4321)
public class RespEmailList extends Message {
  private List<EmailParam> emails;
  
  public void setEmails(List<EmailParam> emails) {
    this.emails = emails;
  }
  
  public List<EmailParam> getEmails() {
    return this.emails;
  }
  
  public RespEmailList(List<EmailParam> emails) {
    this.emails = emails;
  }
}
