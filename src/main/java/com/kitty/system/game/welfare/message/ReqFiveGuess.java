
package com.kitty.system.game.welfare.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 40994)
public class ReqFiveGuess extends Message
{
    private int amount;
    private short choice;
    
    public ReqFiveGuess() {
        this.amount = 0;
    }
    
    public int getAmount() {
        return this.amount;
    }
    
    public short getChoice() {
        return this.choice;
    }
}
