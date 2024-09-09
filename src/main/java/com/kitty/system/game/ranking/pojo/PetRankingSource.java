package com.kitty.system.game.ranking.pojo;

import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.ArrayList;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class PetRankingSource implements Serializable
{
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
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public PetRankingSource(Role role, Pet pet, String petName, int speed, int attack) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
        this.petId = (int)pet.getPetId();
        this.roleId = (int)pet.getRoleId();
        this.petSerial = (int)pet.getPetType();
        this.petName = petName;
        this.speed = speed;
        this.life = (int)pet.getMaxLife();
        this.mana = (int)pet.getMaxMana();
        this.attack = attack;
        this.score = (int)pet.getScore();
        this.rests(role);
    }
    
    public void rests(Role role) {
        this.roleName = role.getName();
        this.serial = (int)role.getSerial();
        EquipService equipService = SpringUtils.getEquipService();
        this.weapon = role.getWeaponIcon();
        this.light_effect = equipService.getWearEffect(role);
        List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
        }
        this.list = SpringUtils.getRoleService().deyList(role.getColor());
    }
    
    @Override
    public boolean equals(Object obj) {
        if ((obj instanceof PetRankingSource)) {
            PetRankingSource petRankingSource = (PetRankingSource)obj;
            return this.petId == petRankingSource.getPetId() && this.roleId == petRankingSource.getRoleId() && this.petSerial == petRankingSource.getPetSerial();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        int prime = 31;
        result = (this.getPetId() + this.getRoleId() + this.petSerial) * prime;
        return result;
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
    
    public int getWeapon() {
        return this.weapon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public List<Integer> getLight_effect() {
        return this.light_effect;
    }
    
    public List<Integer> getSpecial_icon() {
        return this.special_icon;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public void setPetId(final int petId) {
        this.petId = petId;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setPetSerial(final int petSerial) {
        this.petSerial = petSerial;
    }
    
    public void setPetName(final String petName) {
        this.petName = petName;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public void setAttack(final int attack) {
        this.attack = attack;
    }
    
    public void setLife(final int life) {
        this.life = life;
    }
    
    public void setMana(final int mana) {
        this.mana = mana;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setWeapon(final int weapon) {
        this.weapon = weapon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setLight_effect(final List<Integer> light_effect) {
        this.light_effect = light_effect;
    }
    
    public void setSpecial_icon(final List<Integer> special_icon) {
        this.special_icon = special_icon;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public PetRankingSource(final int petId, final int roleId, final String roleName, final int petSerial, final String petName, final int speed, final int attack, final int life, final int mana, final int score, final int serial, final int weapon, final int gem, final List<Integer> light_effect, final List<Integer> special_icon, final List<FiedValue> list) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
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
    
    public PetRankingSource() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<>();
        this.special_icon = new ArrayList<>();
        this.list = new ArrayList<>();
    }
}
