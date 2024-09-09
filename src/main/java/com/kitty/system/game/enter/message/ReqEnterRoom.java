package com.kitty.system.game.enter.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 5155)
public class ReqEnterRoom extends Message
{
    private int isTaskWalk;
    private String mapName;
    
    public ReqEnterRoom() {
    }
    
    public int getIsTaskWalk() {
        return this.isTaskWalk;
    }
    
    public String getMapName() {
        return this.mapName;
    }
    
    public void setIsTaskWalk(final int isTaskWalk) {
        this.isTaskWalk = isTaskWalk;
    }
    
    public void setMapName(final String mapName) {
        this.mapName = mapName;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqEnterRoom)) {
            return false;
        }
        ReqEnterRoom other = (ReqEnterRoom)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getIsTaskWalk() != other.getIsTaskWalk()) {
            return false;
        }
        Object this$mapName = this.getMapName();
        Object other$mapName = other.getMapName();
        return (this$mapName != null) ? this$mapName.equals(other$mapName) : (other$mapName == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqEnterRoom;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIsTaskWalk();
        Object $mapName = this.getMapName();
        result = result * 59 + (($mapName == null) ? 43 : $mapName.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqEnterRoom(isTaskWalk=" + this.getIsTaskWalk() + ", mapName=" + this.getMapName() + ")";
    }
}
