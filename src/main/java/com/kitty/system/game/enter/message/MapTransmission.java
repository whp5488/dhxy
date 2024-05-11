
package com.kitty.system.game.enter.message;

public class MapTransmission
{
    private String name;
    private Integer X;
    private Integer Y;
    private Integer dir;
    
    public MapTransmission() {
        this.dir = 5;
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
        final MapTransmission other = (MapTransmission)o;
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
        final Object this$X = this.getX();
        final Object other$X = other.getX();
        Label_0102: {
            if (this$X == null) {
                if (other$X == null) {
                    break Label_0102;
                }
            }
            else if (this$X.equals(other$X)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$Y = this.getY();
        final Object other$Y = other.getY();
        Label_0139: {
            if (this$Y == null) {
                if (other$Y == null) {
                    break Label_0139;
                }
            }
            else if (this$Y.equals(other$Y)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$dir = this.getDir();
        final Object other$dir = other.getDir();
        if (this$dir == null) {
            if (other$dir == null) {
                return true;
            }
        }
        else if (this$dir.equals(other$dir)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof MapTransmission;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $X = this.getX();
        result = result * 59 + (($X == null) ? 43 : $X.hashCode());
        final Object $Y = this.getY();
        result = result * 59 + (($Y == null) ? 43 : $Y.hashCode());
        final Object $dir = this.getDir();
        result = result * 59 + (($dir == null) ? 43 : $dir.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "MapTransmission(name=" + this.getName() + ", X=" + this.getX() + ", Y=" + this.getY() + ", dir=" + this.getDir() + ")";
    }
}
