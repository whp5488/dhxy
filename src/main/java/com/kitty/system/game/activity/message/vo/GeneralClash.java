package com.kitty.system.game.activity.message.vo;

public class GeneralClash
{
    private String name;
    private int icon;
    private byte rei;
    private short level;
    
    public String getName() {
        return this.name;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof GeneralClash)) {
            return false;
        }
        GeneralClash other = (GeneralClash)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        return ((this$name != null) ? this$name.equals(other$name) : (other$name == null)) && this.getIcon() == other.getIcon() && this.getRei() == other.getRei() && this.getLevel() == other.getLevel();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof GeneralClash;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "GeneralClash(name=" + this.getName() + ", icon=" + this.getIcon() + ", rei=" + (int)this.getRei() + ", level=" + (int)this.getLevel() + ")";
    }
    
    public GeneralClash(final String name, final int icon, final byte rei, final short level) {
        this.name = name;
        this.icon = icon;
        this.rei = rei;
        this.level = level;
    }
    
    public GeneralClash() {
    }
}
