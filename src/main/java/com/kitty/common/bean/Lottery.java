
package com.kitty.common.bean;

public class Lottery
{
    private Integer serial;
    private String name;
    private Double bili;
    private int num;
    
    public Lottery(final Integer serial, final String name, final Double bili, final int num) {
        this.num = 0;
        this.serial = serial;
        this.name = name;
        this.bili = bili;
        this.num = num;
    }
    
    public Lottery(final Integer serial, final String name, final Double bili) {
        this.num = 0;
        this.serial = serial;
        this.name = name;
        this.bili = bili;
    }
    
    public Lottery(final Integer serial) {
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
        final Lottery other = (Lottery)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$bili = this.getBili();
        final Object other$bili = other.getBili();
        if (this$bili == null) {
            if (other$bili == null) {
                return this.getNum() == other.getNum();
            }
        }
        else if (this$bili.equals(other$bili)) {
            return this.getNum() == other.getNum();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Lottery;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $bili = this.getBili();
        result = result * 59 + (($bili == null) ? 43 : $bili.hashCode());
        result = result * 59 + this.getNum();
        return result;
    }
    
    @Override
    public String toString() {
        return "Lottery(serial=" + this.getSerial() + ", name=" + this.getName() + ", bili=" + this.getBili() + ", num=" + this.getNum() + ")";
    }
}
