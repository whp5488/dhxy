
package com.kitty.system.game.activity.model.user.hg;

import java.beans.ConstructorProperties;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.Map;
import java.io.Serializable;

public class Hg implements Serializable
{
    private long id;
    private byte type;
    private long time;
    private Map<Long, Byte> map;
    private ScheduledFuture<?> scheduledFuture;
    
    public Hg(final long id, final byte type, final long time) {
        this.map = new HashMap<Long, Byte>(5);
        this.id = id;
        this.type = type;
        this.time = time;
    }
    
    public long getId() {
        return this.id;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public Map<Long, Byte> getMap() {
        return this.map;
    }
    
    public ScheduledFuture<?> getScheduledFuture() {
        return this.scheduledFuture;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }
    
    public void setMap(final Map<Long, Byte> map) {
        this.map = map;
    }
    
    public void setScheduledFuture(final ScheduledFuture<?> scheduledFuture) {
        this.scheduledFuture = scheduledFuture;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Hg)) {
            return false;
        }
        final Hg other = (Hg)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        if (this.getTime() != other.getTime()) {
            return false;
        }
        final Object this$map = this.getMap();
        final Object other$map = other.getMap();
        Label_0106: {
            if (this$map == null) {
                if (other$map == null) {
                    break Label_0106;
                }
            }
            else if (this$map.equals(other$map)) {
                break Label_0106;
            }
            return false;
        }
        final Object this$scheduledFuture = this.getScheduledFuture();
        final Object other$scheduledFuture = other.getScheduledFuture();
        if (this$scheduledFuture == null) {
            if (other$scheduledFuture == null) {
                return true;
            }
        }
        else if (this$scheduledFuture.equals(other$scheduledFuture)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Hg;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final long $id = this.getId();
        result = result * 59 + (int)($id >>> 32 ^ $id);
        result = result * 59 + this.getType();
        final long $time = this.getTime();
        result = result * 59 + (int)($time >>> 32 ^ $time);
        final Object $map = this.getMap();
        result = result * 59 + (($map == null) ? 43 : $map.hashCode());
        final Object $scheduledFuture = this.getScheduledFuture();
        result = result * 59 + (($scheduledFuture == null) ? 43 : $scheduledFuture.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Hg(id=" + this.getId() + ", type=" + this.getType() + ", time=" + this.getTime() + ", map=" + this.getMap() + ", scheduledFuture=" + this.getScheduledFuture() + ")";
    }
    
    @ConstructorProperties({ "id", "type", "time", "map", "scheduledFuture" })
    public Hg(final long id, final byte type, final long time, final Map<Long, Byte> map, final ScheduledFuture<?> scheduledFuture) {
        this.map = new HashMap<Long, Byte>(5);
        this.id = id;
        this.type = type;
        this.time = time;
        this.map = map;
        this.scheduledFuture = scheduledFuture;
    }
    
    public Hg() {
        this.map = new HashMap<Long, Byte>(5);
    }
}
