
package com.kitty.system.game.ranking.pojo;

import java.beans.ConstructorProperties;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import java.io.Serializable;

public class QlkRankingSource implements Serializable
{
    private int roleId;
    private String roleName;
    private int race;
    private int serial;
    private int qlkj;
    private int qlkm;
    private int qlks;
    private int qlkh;
    private int qlkt;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public QlkRankingSource(final int roleId) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = roleId;
    }
    
    public QlkRankingSource(final Role role, final int[] qlkArrays) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = role.getRoleId();
        this.qlkj = qlkArrays[0];
        this.qlkm = qlkArrays[1];
        this.qlks = qlkArrays[2];
        this.qlkh = qlkArrays[3];
        this.qlkt = qlkArrays[4];
        this.rests(role);
    }
    
    public void rests(final Role role) {
        this.roleName = role.getName();
        this.serial = role.getSerial();
        this.race = role.getRace();
        final EquipService equipService = SpringUtils.getEquipService();
        this.weapon = role.getWeaponIcon();
        this.light_effect = equipService.getWearEffect(role);
        final List<WearEquip> wearEquips = equipService.getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            this.gem = DataGlobalFunc.getWeaponGemNum(wearEquips);
        }
        this.list = SpringUtils.getRoleService().deyList(role.getColor());
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof QlkRankingSource) {
            final QlkRankingSource qlkRankingSource = (QlkRankingSource)obj;
            return this.roleId == qlkRankingSource.getRoleId();
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
    
    public int getRace() {
        return this.race;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getQlkj() {
        return this.qlkj;
    }
    
    public int getQlkm() {
        return this.qlkm;
    }
    
    public int getQlks() {
        return this.qlks;
    }
    
    public int getQlkh() {
        return this.qlkh;
    }
    
    public int getQlkt() {
        return this.qlkt;
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
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setQlkj(final int qlkj) {
        this.qlkj = qlkj;
    }
    
    public void setQlkm(final int qlkm) {
        this.qlkm = qlkm;
    }
    
    public void setQlks(final int qlks) {
        this.qlks = qlks;
    }
    
    public void setQlkh(final int qlkh) {
        this.qlkh = qlkh;
    }
    
    public void setQlkt(final int qlkt) {
        this.qlkt = qlkt;
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
    
    @ConstructorProperties({ "roleId", "roleName", "race", "serial", "qlkj", "qlkm", "qlks", "qlkh", "qlkt", "weapon", "gem", "light_effect", "special_icon", "list" })
    public QlkRankingSource(final int roleId, final String roleName, final int race, final int serial, final int qlkj, final int qlkm, final int qlks, final int qlkh, final int qlkt, final int weapon, final int gem, final List<Integer> light_effect, final List<Integer> special_icon, final List<FiedValue> list) {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = roleId;
        this.roleName = roleName;
        this.race = race;
        this.serial = serial;
        this.qlkj = qlkj;
        this.qlkm = qlkm;
        this.qlks = qlks;
        this.qlkh = qlkh;
        this.qlkt = qlkt;
        this.weapon = weapon;
        this.gem = gem;
        this.light_effect = light_effect;
        this.special_icon = special_icon;
        this.list = list;
    }
    
    public QlkRankingSource() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
    }
}
