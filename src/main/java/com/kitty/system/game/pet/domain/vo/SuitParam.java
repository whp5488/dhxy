
package com.kitty.system.game.pet.domain.vo;

import java.util.List;
import java.io.Serializable;

public class SuitParam implements Serializable
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
    
    @Override
    public int hashCode() {
        int result = 1;
        final int prime = 31;
        result = this.serial * prime;
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof SuitParam) {
            final SuitParam suitParam = (SuitParam)obj;
            return this.serial.equals(suitParam.getSerial());
        }
        return false;
    }
}
