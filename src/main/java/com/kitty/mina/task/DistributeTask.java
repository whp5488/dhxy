
package com.kitty.mina.task;

public interface DistributeTask extends Distributable
{
    String getName();
    
    void action();
}
