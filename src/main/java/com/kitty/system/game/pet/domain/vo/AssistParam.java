package com.kitty.system.game.pet.domain.vo;

import java.io.Serializable;

public class AssistParam implements Serializable
{
    private Integer petId;
    private boolean isTrigger;
    
    public AssistParam() {
        this.isTrigger = false;
    }
    
    public Integer getPetId() {
        return this.petId;
    }
    
    public void setPetId(Integer petId) {
        this.petId = petId;
    }
    
    public boolean isTrigger() {
        return this.isTrigger;
    }
    
    public void setTrigger(boolean trigger) {
        this.isTrigger = trigger;
    }
}
