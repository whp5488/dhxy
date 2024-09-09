package com.kitty.mina.task;

public interface DistributeTask extends Distributable
{
    public abstract String getName();
    
    public abstract void action();
}
