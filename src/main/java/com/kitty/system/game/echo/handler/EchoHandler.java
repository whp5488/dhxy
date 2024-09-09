package com.kitty.system.game.echo.handler;

import com.kitty.listener.event.HeartBeatEvent;

public abstract class EchoHandler
{
    public EchoHandler() {
    }
    
    public abstract void handleEcho(HeartBeatEvent heartBeatEvent);
}
