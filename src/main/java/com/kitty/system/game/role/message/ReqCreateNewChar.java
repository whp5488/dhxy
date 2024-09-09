package com.kitty.system.game.role.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 9005)
public class ReqCreateNewChar extends Message
{
    private int a;
    private String name;
    private Integer sex;
    private Integer race;
    private Integer icon;
    private Integer serial;
    private String marking;
    private int b;
    
    public ReqCreateNewChar() {
    }
    
    public int getA() {
        return this.a;
    }
    
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
    
    public int getB() {
        return this.b;
    }
    
    public void setA(final int a) {
        this.a = a;
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
    
    public void setB(final int b) {
        this.b = b;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof ReqCreateNewChar)) {
            return false;
        }
        ReqCreateNewChar other = (ReqCreateNewChar)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getA() != other.getA()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$sex = this.getSex();
        Object other$sex = other.getSex();
        if (!((this$sex != null) ? this$sex.equals(other$sex) : (other$sex == null))) {
            return false;
        }
        Object this$race = this.getRace();
        Object other$race = other.getRace();
        if (!((this$race != null) ? this$race.equals(other$race) : (other$race == null))) {
            return false;
        }
        Object this$icon = this.getIcon();
        Object other$icon = other.getIcon();
        if (!((this$icon != null) ? this$icon.equals(other$icon) : (other$icon == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$marking = this.getMarking();
        Object other$marking = other.getMarking();
        return ((this$marking != null) ? this$marking.equals(other$marking) : (other$marking == null)) && this.getB() == other.getB();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof ReqCreateNewChar;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getA();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $sex = this.getSex();
        result = result * 59 + (($sex == null) ? 43 : $sex.hashCode());
        Object $race = this.getRace();
        result = result * 59 + (($race == null) ? 43 : $race.hashCode());
        Object $icon = this.getIcon();
        result = result * 59 + (($icon == null) ? 43 : $icon.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        result = result * 59 + this.getB();
        return result;
    }
    
    @Override
    public String toString() {
        return "ReqCreateNewChar(a=" + this.getA() + ", name=" + this.getName() + ", sex=" + this.getSex() + ", race=" + this.getRace() + ", icon=" + this.getIcon() + ", serial=" + this.getSerial() + ", marking=" + this.getMarking() + ", b=" + this.getB() + ")";
    }
}
