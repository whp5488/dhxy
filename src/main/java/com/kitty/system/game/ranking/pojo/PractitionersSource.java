
package com.kitty.system.game.ranking.pojo;

import java.beans.ConstructorProperties;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class PractitionersSource implements Serializable
{
    private int roleId;
    private String roleName;
    private int relive;
    private int level;
    private int reliveAndLevel;
    private long exp;
    private long nextLevelNeedExp;
    private int serial;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public PractitionersSource(final Role role) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = role.getRoleId();
        this.roleName = role.getName();
    }
    
    public PractitionersSource(final Role role, final long nextLevelNeedExp) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = role.getRoleId();
        this.relive = role.getRelive();
        this.level = role.getLevel();
        this.reliveAndLevel = this.relive * 1000 + this.level;
        this.exp = role.getExp();
        this.nextLevelNeedExp = nextLevelNeedExp;
        this.rests(role);
    }
    
    public void rests(final Role role) {
        this.roleName = role.getName();
        this.serial = role.getSerial();
        final EquipService equipService = SpringUtils.getEquipService();
        this.weapon = role.getWeaponIcon();
        this.light_effect = equipService.getWearEffect(role);
        final List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
        }
        this.list = SpringUtils.getRoleService().deyList(role.getColor());
    }
    
    public void setRelive(final int relive) {
        this.relive = relive;
        this.reliveAndLevel = this.relive * 1000 + this.level;
    }
    
    public void setLevel(final int level) {
        this.level = level;
        this.reliveAndLevel = this.relive * 1000 + this.level;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof PractitionersSource) {
            final PractitionersSource practitionersSource = (PractitionersSource)obj;
            return this.roleId == practitionersSource.getRoleId();
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        int result = 1;
        final int prime = 31;
        result = this.getRoleId() * prime;
        return result;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getRoleName() {
        return this.roleName;
    }
    
    public int getRelive() {
        return this.relive;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getReliveAndLevel() {
        return this.reliveAndLevel;
    }
    
    public long getExp() {
        return this.exp;
    }
    
    public long getNextLevelNeedExp() {
        return this.nextLevelNeedExp;
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
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setRoleName(final String roleName) {
        this.roleName = roleName;
    }
    
    public void setReliveAndLevel(final int reliveAndLevel) {
        this.reliveAndLevel = reliveAndLevel;
    }
    
    public void setExp(final long exp) {
        this.exp = exp;
    }
    
    public void setNextLevelNeedExp(final long nextLevelNeedExp) {
        this.nextLevelNeedExp = nextLevelNeedExp;
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
    
    @ConstructorProperties({ "roleId", "roleName", "relive", "level", "reliveAndLevel", "exp", "nextLevelNeedExp", "serial", "weapon", "gem", "light_effect", "special_icon", "list" })
    public PractitionersSource(final int roleId, final String roleName, final int relive, final int level, final int reliveAndLevel, final long exp, final long nextLevelNeedExp, final int serial, final int weapon, final int gem, final List<Integer> light_effect, final List<Integer> special_icon, final List<FiedValue> list) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = roleId;
        this.roleName = roleName;
        this.relive = relive;
        this.level = level;
        this.reliveAndLevel = reliveAndLevel;
        this.exp = exp;
        this.nextLevelNeedExp = nextLevelNeedExp;
        this.serial = serial;
        this.weapon = weapon;
        this.gem = gem;
        this.light_effect = light_effect;
        this.special_icon = special_icon;
        this.list = list;
    }
    
    public PractitionersSource() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
    }
}
