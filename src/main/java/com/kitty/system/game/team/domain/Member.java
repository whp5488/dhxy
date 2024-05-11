
package com.kitty.system.game.team.domain;

import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class Member implements Serializable
{
    private Long id;
    private Integer roleId;
    private Integer ObjectId;
    private String name;
    private Integer serial;
    private String party;
    private short level;
    private byte relive;
    private Integer icon;
    private int weaponIcon;
    private int gem;
    private Integer sex;
    private Integer race;
    private boolean inTeam;
    private boolean inTeamTemporary;
    private boolean isPartner;
    private String color;
    private int masquerade;
    private List<FiedValue> pet;
    
    public Member() {
        this.weaponIcon = 0;
        this.gem = 0;
        this.inTeam = true;
        this.inTeamTemporary = true;
        this.isPartner = false;
        this.masquerade = 0;
        this.pet = new ArrayList<FiedValue>();
    }
    
    public static Member valueOf(final Role role) {
        final Member member = new Member();
        member.setId(role.getId());
        member.setRoleId(role.getRoleId());
        member.setName(role.getName());
        member.setIcon(role.getIcon());
        member.setWeaponIcon(role.getWeaponIcon());
        member.setLevel(role.getLevel().shortValue());
        member.setRelive(role.getRelive().byteValue());
        member.setSex(role.getSex());
        member.setRace(role.getRace());
        member.setColor(role.getColor());
        member.setSerial(role.getSerial());
        if (role.getExtendBox().getCombatPet() > 0) {
            final Pet pet = SpringUtils.getPetService().selectPet(role).get(role.getExtendBox().getCombatPet());
            if (pet != null) {
                final List<FiedValue> fieldValues = new ArrayList<FiedValue>();
                fieldValues.add(new FiedValue("id", (byte)3, pet.getPetId()));
                fieldValues.add(new FiedValue("level", (byte)3, pet.getPetLv()));
                fieldValues.add(new FiedValue("relive", (byte)3, pet.getRebirth()));
                fieldValues.add(new FiedValue("serial", (byte)3, pet.getPetType()));
                member.setPet(fieldValues);
            }
        }
        final List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            member.setGem(num);
        }
        return member;
    }
    
    public static void valueOf(final Role role, final Member member) {
        member.setId(role.getId());
        member.setRoleId(role.getRoleId());
        member.setName(role.getName());
        member.setIcon(role.getIcon());
        member.setWeaponIcon(role.getWeaponIcon());
        member.setLevel(role.getLevel().shortValue());
        member.setRelive(role.getRelive().byteValue());
        member.setSex(role.getSex());
        member.setRace(role.getRace());
        member.setColor(role.getColor());
        member.setSerial(role.getSerial());
        if (role.getExtendBox().getCombatPet() > 0) {
            final Pet pet = SpringUtils.getPetService().selectPet(role).get(role.getExtendBox().getCombatPet());
            final List<FiedValue> fieldValues = new ArrayList<FiedValue>();
            fieldValues.add(new FiedValue("id", (byte)3, pet.getPetId()));
            fieldValues.add(new FiedValue("level", (byte)3, pet.getPetLv()));
            fieldValues.add(new FiedValue("relive", (byte)3, pet.getRebirth()));
            fieldValues.add(new FiedValue("serial", (byte)3, pet.getPetType()));
            member.setPet(fieldValues);
        }
        final List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            member.setGem(num);
        }
    }
    
    public static Member valueOf(final Partner partner) {
        final Member member = new Member();
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
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"id\":").append(this.id);
        sb.append(",\"roleId\":").append(this.roleId);
        sb.append(",\"name\":\"").append(this.name).append('\"');
        sb.append('}');
        return sb.toString();
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
    
    public int getWeaponIcon() {
        return this.weaponIcon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public Integer getSex() {
        return this.sex;
    }
    
    public Integer getRace() {
        return this.race;
    }
    
    public boolean isInTeam() {
        return this.inTeam;
    }
    
    public boolean isInTeamTemporary() {
        return this.inTeamTemporary;
    }
    
    public boolean isPartner() {
        return this.isPartner;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public int getMasquerade() {
        return this.masquerade;
    }
    
    public List<FiedValue> getPet() {
        return this.pet;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setObjectId(final Integer ObjectId) {
        this.ObjectId = ObjectId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setParty(final String party) {
        this.party = party;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setRelive(final byte relive) {
        this.relive = relive;
    }
    
    public void setIcon(final Integer icon) {
        this.icon = icon;
    }
    
    public void setWeaponIcon(final int weaponIcon) {
        this.weaponIcon = weaponIcon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setSex(final Integer sex) {
        this.sex = sex;
    }
    
    public void setRace(final Integer race) {
        this.race = race;
    }
    
    public void setInTeam(final boolean inTeam) {
        this.inTeam = inTeam;
    }
    
    public void setInTeamTemporary(final boolean inTeamTemporary) {
        this.inTeamTemporary = inTeamTemporary;
    }
    
    public void setPartner(final boolean isPartner) {
        this.isPartner = isPartner;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    public void setMasquerade(final int masquerade) {
        this.masquerade = masquerade;
    }
    
    public void setPet(final List<FiedValue> pet) {
        this.pet = pet;
    }
}
