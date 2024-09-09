package com.kitty.system.game.bag.domain;

public class Range<T>
{
    private T min;
    private T max;
    
    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }
    
    public T getMin() {
        return this.min;
    }
    
    public T getMax() {
        return this.max;
    }
}
