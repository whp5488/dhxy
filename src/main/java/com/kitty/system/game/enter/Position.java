
package com.kitty.system.game.enter;

import java.beans.ConstructorProperties;
import java.io.Serializable;

public class Position implements Serializable
{
    private Integer x;
    private Integer y;
    
    public Integer getX() {
        return this.x;
    }
    
    public Integer getY() {
        return this.y;
    }
    
    public void setX(final Integer x) {
        this.x = x;
    }
    
    public void setY(final Integer y) {
        this.y = y;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Position)) {
            return false;
        }
        final Position other = (Position)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$x = this.getX();
        final Object other$x = other.getX();
        Label_0065: {
            if (this$x == null) {
                if (other$x == null) {
                    break Label_0065;
                }
            }
            else if (this$x.equals(other$x)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$y = this.getY();
        final Object other$y = other.getY();
        if (this$y == null) {
            if (other$y == null) {
                return true;
            }
        }
        else if (this$y.equals(other$y)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Position;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $x = this.getX();
        result = result * 59 + (($x == null) ? 43 : $x.hashCode());
        final Object $y = this.getY();
        result = result * 59 + (($y == null) ? 43 : $y.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Position(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
    
    public Position() {
    }
    
    @ConstructorProperties({ "x", "y" })
    public Position(final Integer x, final Integer y) {
        this.x = x;
        this.y = y;
    }
}
