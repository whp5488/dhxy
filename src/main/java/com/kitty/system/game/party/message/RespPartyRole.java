package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 8369)
public class RespPartyRole extends Message {
  private int roleId;
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setWeaponIcon(int weaponIcon) {
    this.weaponIcon = weaponIcon;
  }
  
  public void setGem(int gem) {
    this.gem = gem;
  }
  
  public void setLight_effect(List<Integer> light_effect) {
    this.light_effect = light_effect;
  }
  
  public void setSpecial_icon(List<Integer> special_icon) {
    this.special_icon = special_icon;
  }
  
  public void setList(List<FiedValue> list) {
    this.list = list;
  }
  
  public void setState(byte state) {
    this.state = state;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  private int weaponIcon = 0;
  
  public int getWeaponIcon() {
    return this.weaponIcon;
  }
  
  private int gem = 0;
  
  public int getGem() {
    return this.gem;
  }
  
  private List<Integer> light_effect = new ArrayList<>();
  
  public List<Integer> getLight_effect() {
    return this.light_effect;
  }
  
  private List<Integer> special_icon = new ArrayList<>();
  
  public List<Integer> getSpecial_icon() {
    return this.special_icon;
  }
  
  private List<FiedValue> list = new ArrayList<>();
  
  private byte state;
  
  public List<FiedValue> getList() {
    return this.list;
  }
  
  public byte getState() {
    return this.state;
  }
}
