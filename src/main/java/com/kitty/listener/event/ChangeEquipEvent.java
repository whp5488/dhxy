package com.kitty.listener.event;

import com.kitty.listener.BasePlayerEvent;
import com.kitty.listener.EventType;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.role.domain.Role;

public class ChangeEquipEvent extends BasePlayerEvent {
  private Role role;
  
  private BagEquip bagEquip;
  
  private WearEquip wearEquip;
  
  public Role getRole() {
    return this.role;
  }
  
  public BagEquip getBagEquip() {
    return this.bagEquip;
  }
  
  public WearEquip getWearEquip() {
    return this.wearEquip;
  }
  
  public ChangeEquipEvent(EventType evtType, Role role, BagEquip roleEquip, WearEquip wearEquip) {
    super(evtType, role);
    this.role = role;
    this.bagEquip = roleEquip;
    this.wearEquip = wearEquip;
  }
}
