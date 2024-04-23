package com.kitty.system.game.team.domain;

import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Member implements Serializable {
  private Long id;
  
  private Integer roleId;
  
  private Integer ObjectId;
  
  private String name;
  
  private Integer serial;
  
  private String party;
  
  private short level;
  
  private byte relive;
  
  private Integer icon;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
  
  public void setObjectId(Integer ObjectId) {
    this.ObjectId = ObjectId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public void setParty(String party) {
    this.party = party;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setRelive(byte relive) {
    this.relive = relive;
  }
  
  public void setIcon(Integer icon) {
    this.icon = icon;
  }
  
  public void setWeaponIcon(int weaponIcon) {
    this.weaponIcon = weaponIcon;
  }
  
  public void setGem(int gem) {
    this.gem = gem;
  }
  
  public void setSex(Integer sex) {
    this.sex = sex;
  }
  
  public void setRace(Integer race) {
    this.race = race;
  }
  
  public void setInTeam(boolean inTeam) {
    this.inTeam = inTeam;
  }
  
  public void setInTeamTemporary(boolean inTeamTemporary) {
    this.inTeamTemporary = inTeamTemporary;
  }
  
  public void setPartner(boolean isPartner) {
    this.isPartner = isPartner;
  }
  
  public void setColor(String color) {
    this.color = color;
  }
  
  public void setMasquerade(int masquerade) {
    this.masquerade = masquerade;
  }
  
  public void setPet(List<FiedValue> pet) {
    this.pet = pet;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Integer getRoleId() {
    return this.roleId;
  }
  
  public Integer getObjectId() {
    return this.ObjectId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public String getParty() {
    return this.party;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  public byte getRelive() {
    return this.relive;
  }
  
  public Integer getIcon() {
    return this.icon;
  }
  
  private int weaponIcon = 0;
  
  public int getWeaponIcon() {
    return this.weaponIcon;
  }
  
  private int gem = 0;
  
  private Integer sex;
  
  private Integer race;
  
  public int getGem() {
    return this.gem;
  }
  
  public Integer getSex() {
    return this.sex;
  }
  
  public Integer getRace() {
    return this.race;
  }
  
  private boolean inTeam = true;
  
  public boolean isInTeam() {
    return this.inTeam;
  }
  
  private boolean inTeamTemporary = true;
  
  public boolean isInTeamTemporary() {
    return this.inTeamTemporary;
  }
  
  private boolean isPartner = false;
  
  private String color;
  
  public boolean isPartner() {
    return this.isPartner;
  }
  
  public String getColor() {
    return this.color;
  }
  
  private int masquerade = 0;
  
  public int getMasquerade() {
    return this.masquerade;
  }
  
  private List<FiedValue> pet = new ArrayList<>();
  
  public List<FiedValue> getPet() {
    return this.pet;
  }
  
  public static Member valueOf(Role role) {
    Member member = new Member();
    member.setId(role.getId());
    member.setRoleId(role.getRoleId());
    member.setName(role.getName());
    member.setIcon(Integer.valueOf(role.getIcon()));
    member.setWeaponIcon(role.getWeaponIcon());
    member.setLevel(role.getLevel().shortValue());
    member.setRelive(role.getRelive().byteValue());
    member.setSex(role.getSex());
    member.setRace(role.getRace());
    member.setColor(role.getColor());
    member.setSerial(role.getSerial());
    if (role.getExtendBox().getCombatPet().intValue() > 0) {
      Pet pet = (Pet)SpringUtils.getPetService().selectPet(role).get(role.getExtendBox().getCombatPet());
      if (pet != null) {
        List<FiedValue> fieldValues = new ArrayList<>();
        fieldValues.add(new FiedValue("id", (byte)3, pet.getPetId()));
        fieldValues.add(new FiedValue("level", (byte)3, pet.getPetLv()));
        fieldValues.add(new FiedValue("relive", (byte)3, pet.getRebirth()));
        fieldValues.add(new FiedValue("serial", (byte)3, pet.getPetType()));
        member.setPet(fieldValues);
      } 
    } 
    List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
    if (wearEquips.size() > 0) {
      int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
      member.setGem(num);
    } 
    return member;
  }
  
  public static void valueOf(Role role, Member member) {
    member.setId(role.getId());
    member.setRoleId(role.getRoleId());
    member.setName(role.getName());
    member.setIcon(Integer.valueOf(role.getIcon()));
    member.setWeaponIcon(role.getWeaponIcon());
    member.setLevel(role.getLevel().shortValue());
    member.setRelive(role.getRelive().byteValue());
    member.setSex(role.getSex());
    member.setRace(role.getRace());
    member.setColor(role.getColor());
    member.setSerial(role.getSerial());
    if (role.getExtendBox().getCombatPet().intValue() > 0) {
      Pet pet = (Pet)SpringUtils.getPetService().selectPet(role).get(role.getExtendBox().getCombatPet());
      List<FiedValue> fieldValues = new ArrayList<>();
      fieldValues.add(new FiedValue("id", (byte)3, pet.getPetId()));
      fieldValues.add(new FiedValue("level", (byte)3, pet.getPetLv()));
      fieldValues.add(new FiedValue("relive", (byte)3, pet.getRebirth()));
      fieldValues.add(new FiedValue("serial", (byte)3, pet.getPetType()));
      member.setPet(fieldValues);
    } 
    List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
    if (wearEquips.size() > 0) {
      int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
      member.setGem(num);
    } 
  }
  
  public static Member valueOf(Partner partner) {
    Member member = new Member();
    member.setId(partner.getId());
    member.setRoleId(partner.getRoleId());
    member.setName(partner.getPartnerName());
    member.setIcon(partner.getIcon());
    member.setLevel(partner.getPartnerLv());
    member.setRelive(partner.getRebirth());
    member.setSex(partner.getSex());
    member.setRace(partner.getRace());
    member.setSerial(partner.getPartnerType());
    member.setObjectId(partner.getPartnerId());
    member.setPartner(true);
    return member;
  }
  
  public String toString() {
    StringBuilder sb = new StringBuilder("{");
    sb.append("\"id\":")
      .append(this.id);
    sb.append(",\"roleId\":")
      .append(this.roleId);
    sb.append(",\"name\":\"")
      .append(this.name).append('"');
    sb.append('}');
    return sb.toString();
  }
}
