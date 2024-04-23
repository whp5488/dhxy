package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45143)
public class RespLoginWaitInLine extends Message {
  public void setLine_name(String line_name) {
    this.line_name = line_name;
  }
  
  public void setExpect_time(int expect_time) {
    this.expect_time = expect_time;
  }
  
  public void setReconnet_time(int reconnet_time) {
    this.reconnet_time = reconnet_time;
  }
  
  public void setWaitCode(int waitCode) {
    this.waitCode = waitCode;
  }
  
  public void setCount(int count) {
    this.count = count;
  }
  
  public void setKeep_alive(byte keep_alive) {
    this.keep_alive = keep_alive;
  }
  
  public void setNeed_wait(byte need_wait) {
    this.need_wait = need_wait;
  }
  
  public void setIndsider_lv(byte indsider_lv) {
    this.indsider_lv = indsider_lv;
  }
  
  public void setGold_coin(int gold_coin) {
    this.gold_coin = gold_coin;
  }
  
  public void setStatus(byte status) {
    this.status = status;
  }
  
  private String line_name = "普通通道";
  
  public String getLine_name() {
    return this.line_name;
  }
  
  private int expect_time = 600;
  
  public int getExpect_time() {
    return this.expect_time;
  }
  
  private int reconnet_time = 3;
  
  public int getReconnet_time() {
    return this.reconnet_time;
  }
  
  private int waitCode = 30;
  
  public int getWaitCode() {
    return this.waitCode;
  }
  
  private int count = 1;
  
  public int getCount() {
    return this.count;
  }
  
  private byte keep_alive = 1;
  
  public byte getKeep_alive() {
    return this.keep_alive;
  }
  
  private byte need_wait = 1;
  
  public byte getNeed_wait() {
    return this.need_wait;
  }
  
  private byte indsider_lv = 1;
  
  public byte getIndsider_lv() {
    return this.indsider_lv;
  }
  
  private int gold_coin = 0;
  
  public int getGold_coin() {
    return this.gold_coin;
  }
  
  private byte status = 0;
  
  public byte getStatus() {
    return this.status;
  }
}
