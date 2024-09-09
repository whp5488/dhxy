package com.kitty.listener.event;

import com.kitty.listener.EventType;
import com.kitty.listener.BaseGameEvent;

public class FightRoundParamEvent extends BaseGameEvent
{
    public FightRoundParamEvent(EventType evtType) {
        super(evtType);
    }
}
