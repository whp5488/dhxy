
package com.kitty.system.game.fightnew.model;

import java.beans.ConstructorProperties;

public class SandClockParam
{
    private int objId;
    private int isClear;
    
    public void setObjId(final int objId) {
        this.objId = objId;
    }
    
    public void setIsClear(final int isClear) {
        this.isClear = isClear;
    }
    
    public int getObjId() {
        return this.objId;
    }
    
    public int getIsClear() {
        return this.isClear;
    }
    
    @ConstructorProperties({ "objId", "isClear" })
    public SandClockParam(final int objId, final int isClear) {
        this.isClear = 1;
        this.objId = objId;
        this.isClear = isClear;
    }
    
    public SandClockParam() {
        this.isClear = 1;
    }
}
