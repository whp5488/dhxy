package com.kitty.system.game.enter.message;

public class MapTransmission
{
    private String name;
    private Integer X;
    private Integer Y;
    private Integer dir;
    
    public MapTransmission() {
        this.dir = Integer.valueOf(5);
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getX() {
        return this.X;
    }
    
    public Integer getY() {
        return this.Y;
    }
    
    public Integer getDir() {
        return this.dir;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setX(final Integer X) {
        this.X = X;
    }
    
    public void setY(final Integer Y) {
        this.Y = Y;
    }
    
    public void setDir(final Integer dir) {
        this.dir = dir;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof MapTransmission)) {
            return false;
        }
        MapTransmission other = (MapTransmission)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$X = this.getX();
        Object other$X = other.getX();
        if (!((this$X != null) ? this$X.equals(other$X) : (other$X == null))) {
            return false;
        }
        Object this$Y = this.getY();
        Object other$Y = other.getY();
        if (!((this$Y != null) ? this$Y.equals(other$Y) : (other$Y == null))) {
            return false;
        }
        Object this$dir = this.getDir();
        Object other$dir = other.getDir();
        return (this$dir != null) ? this$dir.equals(other$dir) : (other$dir == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MapTransmission;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $X = this.getX();
        result = result * 59 + (($X == null) ? 43 : $X.hashCode());
        Object $Y = this.getY();
        result = result * 59 + (($Y == null) ? 43 : $Y.hashCode());
        Object $dir = this.getDir();
        result = result * 59 + (($dir == null) ? 43 : $dir.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MapTransmission(name=" + this.getName() + ", X=" + this.getX() + ", Y=" + this.getY() + ", dir=" + this.getDir() + ")";
    }
}
