package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import lombok.Getter;
import lombok.Setter;

import java.beans.ConstructorProperties;

@Setter
@Getter
@MessageMeta(module = 40995)
public class RespFiveGuess extends Message {
  
  @ConstructorProperties({"flag", "money", "surplus", "amount", "choice", "prize", "token", "count"})
  public RespFiveGuess(byte flag, String money, String surplus, int amount, short choice, short prize, String token, short count) {
    this.flag = flag;
    this.money = money;
    this.surplus = surplus;
    this.amount = amount;
    this.choice = choice;
    this.prize = prize;
    this.token = token;
    this.count = count;
  }
  
  private byte flag = 0;

  private String money = "0";

  private String surplus = "0";

  private int amount = 0;

  private short choice = 0;

  private short prize = 0;
  
  private String token;
  
  private short count;

  public RespFiveGuess(short count) {
    this.count = count;
  }
  
  public RespFiveGuess() {}
}
