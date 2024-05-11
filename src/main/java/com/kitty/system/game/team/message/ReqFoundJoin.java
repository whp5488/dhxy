
package com.kitty.system.game.team.message;

import java.util.Arrays;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 4156)
public class ReqFoundJoin extends Message
{
    private int roleId;
    private String name;
    private String type;
    private String target;
    private String msg;
    private int[] raceLimit;
    private int sexLimit;
    private int minLevel;
    private int minRelive;
    private int maxLevel;
    private int maxRelive;
    
    public ReqFoundJoin() {
    }
    
    public ReqFoundJoin(final String target, final String msg, final int[] raceLimit, final int sexLimit, final int minLevel, final int minRelive, final int maxLevel, final int maxRelive) {
        this.target = target;
        this.msg = msg;
        this.raceLimit = raceLimit;
        this.sexLimit = sexLimit;
        this.minLevel = minLevel;
        this.minRelive = minRelive;
        this.maxLevel = maxLevel;
        this.maxRelive = maxRelive;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getType() {
        return this.type;
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public int[] getRaceLimit() {
        return this.raceLimit;
    }
    
    public int getSexLimit() {
        return this.sexLimit;
    }
    
    public int getMinLevel() {
        return this.minLevel;
    }
    
    public int getMinRelive() {
        return this.minRelive;
    }
    
    public int getMaxLevel() {
        return this.maxLevel;
    }
    
    public int getMaxRelive() {
        return this.maxRelive;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setType(final String type) {
        this.type = type;
    }
    
    public void setTarget(final String target) {
        this.target = target;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setRaceLimit(final int[] raceLimit) {
        this.raceLimit = raceLimit;
    }
    
    public void setSexLimit(final int sexLimit) {
        this.sexLimit = sexLimit;
    }
    
    public void setMinLevel(final int minLevel) {
        this.minLevel = minLevel;
    }
    
    public void setMinRelive(final int minRelive) {
        this.minRelive = minRelive;
    }
    
    public void setMaxLevel(final int maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    public void setMaxRelive(final int maxRelive) {
        this.maxRelive = maxRelive;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqFoundJoin)) {
            return false;
        }
        final ReqFoundJoin other = (ReqFoundJoin)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0115: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0115;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$target = this.getTarget();
        final Object other$target = other.getTarget();
        Label_0152: {
            if (this$target == null) {
                if (other$target == null) {
                    break Label_0152;
                }
            }
            else if (this$target.equals(other$target)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        if (this$msg == null) {
            if (other$msg == null) {
                return Arrays.equals(this.getRaceLimit(), other.getRaceLimit()) && this.getSexLimit() == other.getSexLimit() && this.getMinLevel() == other.getMinLevel() && this.getMinRelive() == other.getMinRelive() && this.getMaxLevel() == other.getMaxLevel() && this.getMaxRelive() == other.getMaxRelive();
            }
        }
        else if (this$msg.equals(other$msg)) {
            return Arrays.equals(this.getRaceLimit(), other.getRaceLimit()) && this.getSexLimit() == other.getSexLimit() && this.getMinLevel() == other.getMinLevel() && this.getMinRelive() == other.getMinRelive() && this.getMaxLevel() == other.getMaxLevel() && this.getMaxRelive() == other.getMaxRelive();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqFoundJoin;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $target = this.getTarget();
        result = result * 59 + (($target == null) ? 43 : $target.hashCode());
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        result = result * 59 + Arrays.hashCode(this.getRaceLimit());
        result = result * 59 + this.getSexLimit();
        result = result * 59 + this.getMinLevel();
        result = result * 59 + this.getMinRelive();
        result = result * 59 + this.getMaxLevel();
        result = result * 59 + this.getMaxRelive();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqFoundJoin(roleId=" + this.getRoleId() + ", name=" + this.getName() + ", type=" + this.getType() + ", target=" + this.getTarget() + ", msg=" + this.getMsg() + ", raceLimit=" + Arrays.toString(this.getRaceLimit()) + ", sexLimit=" + this.getSexLimit() + ", minLevel=" + this.getMinLevel() + ", minRelive=" + this.getMinRelive() + ", maxLevel=" + this.getMaxLevel() + ", maxRelive=" + this.getMaxRelive() + ")";
    }
}
