
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 6552)
public class RespRefreshStore extends Message
{
    private String saveLife;
    private String saveMana;
    
    public RespRefreshStore(final Long saveLife, final Long saveMana) {
        this.saveLife = String.valueOf(saveLife);
        this.saveMana = String.valueOf(saveMana);
    }
    
    public String getSaveLife() {
        return this.saveLife;
    }
    
    public String getSaveMana() {
        return this.saveMana;
    }
    
    public void setSaveLife(final String saveLife) {
        this.saveLife = saveLife;
    }
    
    public void setSaveMana(final String saveMana) {
        this.saveMana = saveMana;
    }
    
    public RespRefreshStore() {
    }
}
