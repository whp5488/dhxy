
package com.kitty.system.game.pet.message;

import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 42140)
public class RespAssistPetList extends Message
{
    private Integer serial;
    private String name;
    private List<Integer> petIds;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public List<Integer> getPetIds() {
        return this.petIds;
    }
    
    public void setPetIds(final List<Integer> petIds) {
        this.petIds = petIds;
    }
}
