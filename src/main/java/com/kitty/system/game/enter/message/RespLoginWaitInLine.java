
package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45143)
public class RespLoginWaitInLine extends Message
{
    private String line_name;
    private int expect_time;
    private int reconnet_time;
    private int waitCode;
    private int count;
    private byte keep_alive;
    private byte need_wait;
    private byte indsider_lv;
    private int gold_coin;
    private byte status;
    
    public RespLoginWaitInLine() {
        this.line_name = "普通通道";
        this.expect_time = 600;
        this.reconnet_time = 3;
        this.waitCode = 30;
        this.count = 1;
        this.keep_alive = 1;
        this.need_wait = 1;
        this.indsider_lv = 1;
        this.gold_coin = 0;
        this.status = 0;
    }
    
    public String getLine_name() {
        return this.line_name;
    }
    
    public int getExpect_time() {
        return this.expect_time;
    }
    
    public int getReconnet_time() {
        return this.reconnet_time;
    }
    
    public int getWaitCode() {
        return this.waitCode;
    }
    
    public int getCount() {
        return this.count;
    }
    
    public byte getKeep_alive() {
        return this.keep_alive;
    }
    
    public byte getNeed_wait() {
        return this.need_wait;
    }
    
    public byte getIndsider_lv() {
        return this.indsider_lv;
    }
    
    public int getGold_coin() {
        return this.gold_coin;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public void setLine_name(final String line_name) {
        this.line_name = line_name;
    }
    
    public void setExpect_time(final int expect_time) {
        this.expect_time = expect_time;
    }
    
    public void setReconnet_time(final int reconnet_time) {
        this.reconnet_time = reconnet_time;
    }
    
    public void setWaitCode(final int waitCode) {
        this.waitCode = waitCode;
    }
    
    public void setCount(final int count) {
        this.count = count;
    }
    
    public void setKeep_alive(final byte keep_alive) {
        this.keep_alive = keep_alive;
    }
    
    public void setNeed_wait(final byte need_wait) {
        this.need_wait = need_wait;
    }
    
    public void setIndsider_lv(final byte indsider_lv) {
        this.indsider_lv = indsider_lv;
    }
    
    public void setGold_coin(final int gold_coin) {
        this.gold_coin = gold_coin;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
}
