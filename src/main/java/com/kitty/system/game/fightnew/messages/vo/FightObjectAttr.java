package com.kitty.system.game.fightnew.messages.vo;

import com.kitty.system.common.enter.FiedValue;
import java.util.List;

public class FightObjectAttr
{
    private int objId;
    private List<FiedValue> attrs;
    
    public int getObjId() {
        return this.objId;
    }
    
    public List<FiedValue> getAttrs() {
        return this.attrs;
    }
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setAttrs(final List<FiedValue> attrs) {
        this.attrs = attrs;
    }
    
    public FightObjectAttr(final int objId, final List<FiedValue> attrs) {
        this.objId = objId;
        this.attrs = attrs;
    }
    
    public FightObjectAttr() {
    }
}
