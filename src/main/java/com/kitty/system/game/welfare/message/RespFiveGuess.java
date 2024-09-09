package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40995)
public class RespFiveGuess extends Message
{
    private byte flag;
    private String money;
    private String surplus;
    private int amount;
    private short choice;
    private short prize;
    private String token;
    private short count;
    
    public RespFiveGuess(short count) {
        this.flag = 0;
        this.money = "0";
        this.surplus = "0";
        this.amount = 0;
        this.choice = 0;
        this.prize = 0;
        this.count = count;
    }
    
    public byte getFlag() {
        return this.flag;
    }
    
    public String getMoney() {
        return this.money;
    }
    
    public String getSurplus() {
        return this.surplus;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public short getChoice() {
        return this.choice;
    }
    
    public short getPrize() {
        return this.prize;
    }
    
    public String getToken() {
        return this.token;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public void setFlag(final byte flag) {
        this.flag = flag;
    }
    
    public void setMoney(final String money) {
        this.money = money;
    }
    
    public void setSurplus(final String surplus) {
        this.surplus = surplus;
    }
    
    public void setAmount(final int amount) {
        this.amount = amount;
    }
    
    public void setChoice(final short choice) {
        this.choice = choice;
    }
    
    public void setPrize(final short prize) {
        this.prize = prize;
    }
    
    public void setToken(final String token) {
        this.token = token;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespFiveGuess)) {
            return false;
        }
        RespFiveGuess other = (RespFiveGuess)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getFlag() != other.getFlag()) {
            return false;
        }
        Object this$money = this.getMoney();
        Object other$money = other.getMoney();
        if (!((this$money != null) ? this$money.equals(other$money) : (other$money == null))) {
            return false;
        }
        Object this$surplus = this.getSurplus();
        Object other$surplus = other.getSurplus();
        if (!((this$surplus != null) ? this$surplus.equals(other$surplus) : (other$surplus == null))) {
            return false;
        }
        if (this.getAmount() != other.getAmount()) {
            return false;
        }
        if (this.getChoice() != other.getChoice()) {
            return false;
        }
        if (this.getPrize() != other.getPrize()) {
            return false;
        }
        Object this$token = this.getToken();
        Object other$token = other.getToken();
        return ((this$token != null) ? this$token.equals(other$token) : (other$token == null)) && this.getCount() == other.getCount();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFiveGuess;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFlag();
        Object $money = this.getMoney();
        result = result * 59 + (($money == null) ? 43 : $money.hashCode());
        Object $surplus = this.getSurplus();
        result = result * 59 + (($surplus == null) ? 43 : $surplus.hashCode());
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getChoice();
        result = result * 59 + this.getPrize();
        Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        result = result * 59 + this.getCount();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFiveGuess(flag=" + (int)this.getFlag() + ", money=" + this.getMoney() + ", surplus=" + this.getSurplus() + ", amount=" + this.getAmount() + ", choice=" + (int)this.getChoice() + ", prize=" + (int)this.getPrize() + ", token=" + this.getToken() + ", count=" + (int)this.getCount() + ")";
    }
    
    public RespFiveGuess(final byte flag, final String money, final String surplus, final int amount, final short choice, final short prize, final String token, final short count) {
        this.flag = 0;
        this.money = "0";
        this.surplus = "0";
        this.amount = 0;
        this.choice = 0;
        this.prize = 0;
        this.flag = flag;
        this.money = money;
        this.surplus = surplus;
        this.amount = amount;
        this.choice = choice;
        this.prize = prize;
        this.token = token;
        this.count = count;
    }
    
    public RespFiveGuess() {
        this.flag = 0;
        this.money = "0";
        this.surplus = "0";
        this.amount = 0;
        this.choice = 0;
        this.prize = 0;
    }
}
