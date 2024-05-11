
package com.kitty.system.game.welfare.message;

import java.beans.ConstructorProperties;
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
    
    public RespFiveGuess(final short count) {
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
        final RespFiveGuess other = (RespFiveGuess)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getFlag() != other.getFlag()) {
            return false;
        }
        final Object this$money = this.getMoney();
        final Object other$money = other.getMoney();
        Label_0078: {
            if (this$money == null) {
                if (other$money == null) {
                    break Label_0078;
                }
            }
            else if (this$money.equals(other$money)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$surplus = this.getSurplus();
        final Object other$surplus = other.getSurplus();
        Label_0115: {
            if (this$surplus == null) {
                if (other$surplus == null) {
                    break Label_0115;
                }
            }
            else if (this$surplus.equals(other$surplus)) {
                break Label_0115;
            }
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
        final Object this$token = this.getToken();
        final Object other$token = other.getToken();
        if (this$token == null) {
            if (other$token == null) {
                return this.getCount() == other.getCount();
            }
        }
        else if (this$token.equals(other$token)) {
            return this.getCount() == other.getCount();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespFiveGuess;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getFlag();
        final Object $money = this.getMoney();
        result = result * 59 + (($money == null) ? 43 : $money.hashCode());
        final Object $surplus = this.getSurplus();
        result = result * 59 + (($surplus == null) ? 43 : $surplus.hashCode());
        result = result * 59 + this.getAmount();
        result = result * 59 + this.getChoice();
        result = result * 59 + this.getPrize();
        final Object $token = this.getToken();
        result = result * 59 + (($token == null) ? 43 : $token.hashCode());
        result = result * 59 + this.getCount();
        return result;
    }
    
    @Override
    public String toString() {
        return "RespFiveGuess(flag=" + this.getFlag() + ", money=" + this.getMoney() + ", surplus=" + this.getSurplus() + ", amount=" + this.getAmount() + ", choice=" + this.getChoice() + ", prize=" + this.getPrize() + ", token=" + this.getToken() + ", count=" + this.getCount() + ")";
    }
    
    @ConstructorProperties({ "flag", "money", "surplus", "amount", "choice", "prize", "token", "count" })
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
