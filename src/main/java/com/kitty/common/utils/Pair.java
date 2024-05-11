
package com.kitty.common.utils;

public class Pair<F, S>
{
    private final F first;
    private final S second;
    
    public Pair(final F first, final S second) {
        this.first = first;
        this.second = second;
    }
    
    public F getFirst() {
        return this.first;
    }
    
    public S getSecond() {
        return this.second;
    }
    
    @Override
    public String toString() {
        return "Pair [first=" + this.first + ", second=" + this.second + "]";
    }
}
