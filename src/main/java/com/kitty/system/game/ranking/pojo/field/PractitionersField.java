
package com.kitty.system.game.ranking.pojo.field;

import java.math.BigDecimal;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.ranking.pojo.RankingField;

public class PractitionersField extends RankingField
{
    private int roleId;
    private String roleName;
    private int relive;
    private int level;
    private String expPercent;
    private int serial;
    private int weapon;
    private int gem;
    private List<Integer> light_effect;
    private List<Integer> special_icon;
    private List<FiedValue> list;
    
    public PractitionersField() {
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
    }
    
    public PractitionersField(final PractitionersSource obj, final int order, final byte serverId) {
        super(order, serverId);
        this.weapon = 0;
        this.gem = 0;
        this.light_effect = new ArrayList<Integer>();
        this.special_icon = new ArrayList<Integer>();
        this.list = new ArrayList<FiedValue>();
        this.roleId = obj.getRoleId();
        this.roleName = obj.getRoleName();
        this.relive = obj.getRelive();
        this.level = obj.getLevel();
        final String doubleNum = BigDecimalUtil.divide(String.valueOf(obj.getExp()), String.valueOf(obj.getNextLevelNeedExp()));
        final String percent = BigDecimalUtil.multiply(doubleNum, String.valueOf(100));
        final BigDecimal bigDecimal = BigDecimalUtil.decimalScaleBig(0, percent);
        this.expPercent = bigDecimal.toString() + "%";
        this.serial = obj.getSerial();
        this.weapon = obj.getWeapon();
        this.gem = obj.getGem();
        this.light_effect = obj.getLight_effect();
        this.special_icon = obj.getSpecial_icon();
        this.list = obj.getList();
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
    
    public String getExpPercent() {
        return this.expPercent;
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
    
    public void setRelive(final int relive) {
        this.relive = relive;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setExpPercent(final String expPercent) {
        this.expPercent = expPercent;
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
}
