package com.kitty.listener.event;

import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.EventType;

public class FightRoundParamEvent extends BaseGameEvent {
  public FightRoundParamEvent(EventType evtType) {
    super(evtType);
  }
}
