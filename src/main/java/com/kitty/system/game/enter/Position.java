package com.kitty.system.game.enter;

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
        Position other = (Position)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$x = this.getX();
        Object other$x = other.getX();
        if (!((this$x != null) ? this$x.equals(other$x) : (other$x == null))) {
            return false;
        }
        Object this$y = this.getY();
        Object other$y = other.getY();
        return (this$y != null) ? this$y.equals(other$y) : (other$y == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Position;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $x = this.getX();
        result = result * 59 + (($x == null) ? 43 : $x.hashCode());
        Object $y = this.getY();
        result = result * 59 + (($y == null) ? 43 : $y.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Position(x=" + this.getX() + ", y=" + this.getY() + ")";
    }
    
    public Position() {
    }
    
    public Position(final Integer x, final Integer y) {
        this.x = x;
        this.y = y;
    }
}
