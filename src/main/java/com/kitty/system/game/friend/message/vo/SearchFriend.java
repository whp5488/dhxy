
package com.kitty.system.game.friend.message.vo;

public class SearchFriend
{
    private int id;
    private String name;
    private int serial;
    private byte rei;
    private short level;
    
    public SearchFriend() {
    }
    
    public SearchFriend(final int id, final String name, final int serial, final byte rei, final short level) {
        this.id = id;
        this.name = name;
        this.serial = serial;
        this.rei = rei;
        this.level = level;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
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
        if (!(o instanceof SearchFriend)) {
            return false;
        }
        final SearchFriend other = (SearchFriend)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        if (this$name == null) {
            if (other$name == null) {
                return this.getSerial() == other.getSerial() && this.getRei() == other.getRei() && this.getLevel() == other.getLevel();
            }
        }
        else if (this$name.equals(other$name)) {
            return this.getSerial() == other.getSerial() && this.getRei() == other.getRei() && this.getLevel() == other.getLevel();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SearchFriend;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getRei();
        result = result * 59 + this.getLevel();
        return result;
    }
    
    @Override
    public String toString() {
        return "SearchFriend(id=" + this.getId() + ", name=" + this.getName() + ", serial=" + this.getSerial() + ", rei=" + this.getRei() + ", level=" + this.getLevel() + ")";
    }
}
