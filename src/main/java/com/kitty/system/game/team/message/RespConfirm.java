package com.kitty.system.game.team.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 45240)
public class RespConfirm extends Message {
  private String title;
  
  private String tips;

  private int downCount = 0;

  private int onlyConfirm = 0;
  
  private String confirmType;

  private String confirmText = "确认";

  private String cancelText = "取消";
  
  private int show_dlg_mode;
  
  private String countDownTips;

  private String para_str = "{}";

  private int no_close_btn = 0;

}
