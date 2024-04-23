package com.kitty.system.game.ranking.pojo;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.beans.ConstructorProperties;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PetRankingSource implements Serializable {
  private int petId;
  
  private int roleId;
  
  private String roleName;
  
  private int petSerial;
  
  private String petName;
  
  private int speed;
  
  private int attack;
  
  private int life;
  
  private int mana;
  
  private int score;
  
  private int serial;
  
  public void setPetId(int petId) {
    this.petId = petId;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setRoleName(String roleName) {
    this.roleName = roleName;
  }
  
  public void setPetSerial(int petSerial) {
    this.petSerial = petSerial;
  }
  
  public void setPetName(String petName) {
    this.petName = petName;
  }
  
  public void setSpeed(int speed) {
    this.speed = speed;
  }
  
  public void setAttack(int attack) {
    this.attack = attack;
  }
  
  public void setLife(int life) {
    this.life = life;
  }
  
  public void setMana(int mana) {
    this.mana = mana;
  }
  
  public void setScore(int score) {
    this.score = score;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setWeapon(int weapon) {
    this.weapon = weapon;
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
  
  @ConstructorProperties({"petId", "roleId", "roleName", "petSerial", "petName", "speed", "attack", "life", "mana", "score", "serial", "weapon", "gem", "light_effect", "special_icon", "list"})
  public PetRankingSource(int petId, int roleId, String roleName, int petSerial, String petName, int speed, int attack, int life, int mana, int score, int serial, int weapon, int gem, List<Integer> light_effect, List<Integer> special_icon, List<FiedValue> list) {
    this.petId = petId;
    this.roleId = roleId;
    this.roleName = roleName;
    this.petSerial = petSerial;
    this.petName = petName;
    this.speed = speed;
    this.attack = attack;
    this.life = life;
    this.mana = mana;
    this.score = score;
    this.serial = serial;
    this.weapon = weapon;
    this.gem = gem;
    this.light_effect = light_effect;
    this.special_icon = special_icon;
    this.list = list;
  }
  
  public int getPetId() {
    return this.petId;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getRoleName() {
    return this.roleName;
  }
  
  public int getPetSerial() {
    return this.petSerial;
  }
  
  public String getPetName() {
    return this.petName;
  }
  
  public int getSpeed() {
    return this.speed;
  }
  
  public int getAttack() {
    return this.attack;
  }
  
  public int getLife() {
    return this.life;
  }
  
  public int getMana() {
    return this.mana;
  }
  
  public int getScore() {
    return this.score;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  private int weapon = 0;
  
  public int getWeapon() {
    return this.weapon;
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
  
  public List<FiedValue> getList() {
    return this.list;
  }
  
  public PetRankingSource(Role role, Pet pet, String petName, int speed, int attack) {
    this.petId = pet.getPetId().intValue();
    this.roleId = pet.getRoleId().intValue();
    this.petSerial = pet.getPetType().intValue();
    this.petName = petName;
    this.speed = speed;
    this.life = pet.getMaxLife().intValue();
    this.mana = pet.getMaxMana().intValue();
    this.attack = attack;
    this.score = pet.getScore().intValue();
    rests(role);
  }
  
  public void rests(Role role) {
    this.roleName = role.getName();
    this.serial = role.getSerial().intValue();
    EquipService equipService = SpringUtils.getEquipService();
    this.weapon = role.getWeaponIcon();
    this.light_effect = equipService.getWearEffect(role);
    List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
    if (wearEquips.size() > 0)
      this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips); 
    this.list = SpringUtils.getRoleService().deyList(role.getColor());
  }
  
  public boolean equals(Object obj) {
    if (obj instanceof PetRankingSource) {
      PetRankingSource petRankingSource = (PetRankingSource)obj;
      if (this.petId == petRankingSource.getPetId() && this.roleId == petRankingSource.getRoleId() && this.petSerial == petRankingSource
        .getPetSerial())
        return true; 
      return false;
    } 
    return false;
  }
  
  public int hashCode() {
    int result = 1;
    int prime = 31;
    result = (getPetId() + getRoleId() + this.petSerial) * prime;
    return result;
  }
  
  public PetRankingSource() {}
}
