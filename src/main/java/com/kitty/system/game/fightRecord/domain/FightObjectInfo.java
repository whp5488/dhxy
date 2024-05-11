
package com.kitty.system.game.fightRecord.domain;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.io.Serializable;

public class FightObjectInfo implements Serializable
{
    private Map<Integer, List<Integer>> petMap;
    private Map<Integer, Map<Integer, Map<Integer, Integer>>> statusNumMap;
    
    public Map<Integer, List<Integer>> getPetMap() {
        return this.petMap;
    }
    
    public Map<Integer, Map<Integer, Map<Integer, Integer>>> getStatusNumMap() {
        return this.statusNumMap;
    }
    
    public void setPetMap(final Map<Integer, List<Integer>> petMap) {
        this.petMap = petMap;
    }
    
    public void setStatusNumMap(final Map<Integer, Map<Integer, Map<Integer, Integer>>> statusNumMap) {
        this.statusNumMap = statusNumMap;
    }
    
    @ConstructorProperties({ "petMap", "statusNumMap" })
    public FightObjectInfo(final Map<Integer, List<Integer>> petMap, final Map<Integer, Map<Integer, Map<Integer, Integer>>> statusNumMap) {
        this.petMap = new HashMap<Integer, List<Integer>>();
        this.statusNumMap = new HashMap<Integer, Map<Integer, Map<Integer, Integer>>>();
        this.petMap = petMap;
        this.statusNumMap = statusNumMap;
    }
    
    public FightObjectInfo() {
        this.petMap = new HashMap<Integer, List<Integer>>();
        this.statusNumMap = new HashMap<Integer, Map<Integer, Map<Integer, Integer>>>();
    }
}
