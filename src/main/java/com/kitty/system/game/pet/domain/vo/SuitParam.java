package com.kitty.system.game.pet.domain.vo;

import java.util.List;
import java.io.Serializable;

public class SuitParam implements Serializable
{
    private Integer serial;
    private String name;
    private List<Integer> petIds;
    
    public SuitParam() {
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public void setSerial(Integer serial) {
        this.serial = serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public List<Integer> getPetIds() {
        return this.petIds;
    }
    
    public void setPetIds(List<Integer> petIds) {
        this.petIds = petIds;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = (int)this.serial * prime;
        return result;
    }
    
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof SuitParam)) {
            SuitParam suitParam = (SuitParam)obj;
            return this.serial.equals(suitParam.getSerial());
        }
        return false;
    }
}
