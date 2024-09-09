package com.kitty.common.bean;

public class Lottery
{
    private Integer serial;
    private String name;
    private Double bili;
    private int num;
    
    public Lottery(Integer serial, String name, Double bili, int num) {
        this.num = 0;
        this.serial = serial;
        this.name = name;
        this.bili = bili;
        this.num = num;
    }
    
    public Lottery(Integer serial, String name, Double bili) {
        this.num = 0;
        this.serial = serial;
        this.name = name;
        this.bili = bili;
    }
    
    public Lottery(Integer serial) {
        this.num = 0;
        this.serial = serial;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Double getBili() {
        return this.bili;
    }
    
    public int getNum() {
        return this.num;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setBili(final Double bili) {
        this.bili = bili;
    }
    
    public void setNum(final int num) {
        this.num = num;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Lottery)) {
            return false;
        }
        Lottery other = (Lottery)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$bili = this.getBili();
        Object other$bili = other.getBili();
        return ((this$bili != null) ? this$bili.equals(other$bili) : (other$bili == null)) && this.getNum() == other.getNum();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Lottery;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $bili = this.getBili();
        result = result * 59 + (($bili == null) ? 43 : $bili.hashCode());
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "Lottery(serial=" + this.getSerial() + ", name=" + this.getName() + ", bili=" + this.getBili() + ", num=" + this.getNum() + ")";
    }
}
