
package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 8284)
public class ReqCreateNewChar extends Message
{
    private String name;
    private Integer sex;
    private Integer race;
    private Integer icon;
    private Integer serial;
    private String marking;
    
    public String getName() {
        return this.name;
    }
    
    public Integer getSex() {
        return this.sex;
    }
    
    public Integer getRace() {
        return this.race;
    }
    
    public Integer getIcon() {
        return this.icon;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSex(final Integer sex) {
        this.sex = sex;
    }
    
    public void setRace(final Integer race) {
        this.race = race;
    }
    
    public void setIcon(final Integer icon) {
        this.icon = icon;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqCreateNewChar)) {
            return false;
        }
        final ReqCreateNewChar other = (ReqCreateNewChar)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0065: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0065;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$sex = this.getSex();
        final Object other$sex = other.getSex();
        Label_0102: {
            if (this$sex == null) {
                if (other$sex == null) {
                    break Label_0102;
                }
            }
            else if (this$sex.equals(other$sex)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$race = this.getRace();
        final Object other$race = other.getRace();
        Label_0139: {
            if (this$race == null) {
                if (other$race == null) {
                    break Label_0139;
                }
            }
            else if (this$race.equals(other$race)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$icon = this.getIcon();
        final Object other$icon = other.getIcon();
        Label_0176: {
            if (this$icon == null) {
                if (other$icon == null) {
                    break Label_0176;
                }
            }
            else if (this$icon.equals(other$icon)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0213: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0213;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        if (this$marking == null) {
            if (other$marking == null) {
                return true;
            }
        }
        else if (this$marking.equals(other$marking)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqCreateNewChar;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $sex = this.getSex();
        result = result * 59 + (($sex == null) ? 43 : $sex.hashCode());
        final Object $race = this.getRace();
        result = result * 59 + (($race == null) ? 43 : $race.hashCode());
        final Object $icon = this.getIcon();
        result = result * 59 + (($icon == null) ? 43 : $icon.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqCreateNewChar(name=" + this.getName() + ", sex=" + this.getSex() + ", race=" + this.getRace() + ", icon=" + this.getIcon() + ", serial=" + this.getSerial() + ", marking=" + this.getMarking() + ")";
    }
}
