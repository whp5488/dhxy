
package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4144)
public class ReqEnterRoom extends Message
{
    private String mapName;
    private int isTaskWalk;
    
    public String getMapName() {
        return this.mapName;
    }
    
    public int getIsTaskWalk() {
        return this.isTaskWalk;
    }
    
    public void setMapName(final String mapName) {
        this.mapName = mapName;
    }
    
    public void setIsTaskWalk(final int isTaskWalk) {
        this.isTaskWalk = isTaskWalk;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqEnterRoom)) {
            return false;
        }
        final ReqEnterRoom other = (ReqEnterRoom)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$mapName = this.getMapName();
        final Object other$mapName = other.getMapName();
        if (this$mapName == null) {
            if (other$mapName == null) {
                return this.getIsTaskWalk() == other.getIsTaskWalk();
            }
        }
        else if (this$mapName.equals(other$mapName)) {
            return this.getIsTaskWalk() == other.getIsTaskWalk();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqEnterRoom;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $mapName = this.getMapName();
        result = result * 59 + (($mapName == null) ? 43 : $mapName.hashCode());
        result = result * 59 + this.getIsTaskWalk();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqEnterRoom(mapName=" + this.getMapName() + ", isTaskWalk=" + this.getIsTaskWalk() + ")";
    }
}
