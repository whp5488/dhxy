
package com.kitty.system.game.player;

import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import com.kitty.common.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.game.role.domain.Role;
import java.io.Serializable;

public class PlayerProfile implements Serializable
{
    private String sid;
    private Integer roleId;
    private Long id;
    private String name;
    private int icon;
    private int serial;
    private int level;
    private long exp;
    private long nextExp;
    private int relive;
    private byte race;
    private long enterTime;
    private long lastLoginTime;
    private byte sex;
    private int faction;
    private byte drill;
    private String marking;
    
    public PlayerProfile(final Role role) {
        this.drill = 0;
        this.sid = role.getSid();
        this.id = role.getId();
        this.name = role.getName();
        this.icon = role.getIcon();
        this.serial = role.getSerial();
        this.level = role.getLevel();
        this.exp = role.getExp();
        this.nextExp = FormulaHero.formulaHeroLvUpExp(role.getLevel(), role.getLevel() + 1, role.getRelive());
        this.roleId = role.getRoleId();
        this.race = role.getRace().byteValue();
        this.sex = role.getSex().byteValue();
        this.relive = role.getRelive();
        this.enterTime = role.getEnterTime();
        this.faction = role.getFaction();
        this.drill = role.getExtendBox().getDrill();
        this.marking = role.getMarking();
    }
    
    public PlayerProfile(final String sid, final Integer roleId, final Long id, final String name, final Integer icon, final Integer serial, final Integer level, final long exp, final Integer relive, final Integer race, final long enterTime, final long lastLoginTime, final Integer sex, final int faction, final String extend, final String marking) {
        this.drill = 0;
        this.sid = sid;
        this.roleId = roleId;
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.serial = serial;
        this.level = level;
        this.exp = exp;
        this.nextExp = FormulaHero.formulaHeroLvUpExp(level, level + 1, relive);
        this.relive = relive;
        this.race = race.byteValue();
        this.enterTime = enterTime;
        this.lastLoginTime = lastLoginTime;
        this.sex = sex.byteValue();
        this.faction = faction;
        if (StringUtils.isNotEmpty((CharSequence)extend)) {
            final String content = ZipUtil.uncompress(extend);
            final PlayerExtendBox box = JsonUtils.string2Object(content, PlayerExtendBox.class);
            this.drill = box.getDrill();
        }
        this.marking = marking;
    }
    
    public PlayerProfile() {
        this.drill = 0;
    }
    
    public String getSid() {
        return this.sid;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public Long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public long getExp() {
        return this.exp;
    }
    
    public long getNextExp() {
        return this.nextExp;
    }
    
    public int getRelive() {
        return this.relive;
    }
    
    public byte getRace() {
        return this.race;
    }
    
    public long getEnterTime() {
        return this.enterTime;
    }
    
    public long getLastLoginTime() {
        return this.lastLoginTime;
    }
    
    public byte getSex() {
        return this.sex;
    }
    
    public int getFaction() {
        return this.faction;
    }
    
    public byte getDrill() {
        return this.drill;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public void setSid(final String sid) {
        this.sid = sid;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setExp(final long exp) {
        this.exp = exp;
    }
    
    public void setNextExp(final long nextExp) {
        this.nextExp = nextExp;
    }
    
    public void setRelive(final int relive) {
        this.relive = relive;
    }
    
    public void setRace(final byte race) {
        this.race = race;
    }
    
    public void setEnterTime(final long enterTime) {
        this.enterTime = enterTime;
    }
    
    public void setLastLoginTime(final long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
    
    public void setSex(final byte sex) {
        this.sex = sex;
    }
    
    public void setFaction(final int faction) {
        this.faction = faction;
    }
    
    public void setDrill(final byte drill) {
        this.drill = drill;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof PlayerProfile)) {
            return false;
        }
        final PlayerProfile other = (PlayerProfile)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$sid = this.getSid();
        final Object other$sid = other.getSid();
        Label_0065: {
            if (this$sid == null) {
                if (other$sid == null) {
                    break Label_0065;
                }
            }
            else if (this$sid.equals(other$sid)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$roleId = this.getRoleId();
        final Object other$roleId = other.getRoleId();
        Label_0102: {
            if (this$roleId == null) {
                if (other$roleId == null) {
                    break Label_0102;
                }
            }
            else if (this$roleId.equals(other$roleId)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0139: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0139;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0176: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0176;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0176;
            }
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getExp() != other.getExp()) {
            return false;
        }
        if (this.getNextExp() != other.getNextExp()) {
            return false;
        }
        if (this.getRelive() != other.getRelive()) {
            return false;
        }
        if (this.getRace() != other.getRace()) {
            return false;
        }
        if (this.getEnterTime() != other.getEnterTime()) {
            return false;
        }
        if (this.getLastLoginTime() != other.getLastLoginTime()) {
            return false;
        }
        if (this.getSex() != other.getSex()) {
            return false;
        }
        if (this.getFaction() != other.getFaction()) {
            return false;
        }
        if (this.getDrill() != other.getDrill()) {
            return false;
        }
        final Object this$marking = this.getMarking();
        final Object other$marking = other.getMarking();
        if (this$marking == null) {
            if (other$marking == null) {
                return true;
            }
        }
        else if (this$marking.equals(other$marking)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof PlayerProfile;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $sid = this.getSid();
        result = result * 59 + (($sid == null) ? 43 : $sid.hashCode());
        final Object $roleId = this.getRoleId();
        result = result * 59 + (($roleId == null) ? 43 : $roleId.hashCode());
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getLevel();
        final long $exp = this.getExp();
        result = result * 59 + (int)($exp >>> 32 ^ $exp);
        final long $nextExp = this.getNextExp();
        result = result * 59 + (int)($nextExp >>> 32 ^ $nextExp);
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getRace();
        final long $enterTime = this.getEnterTime();
        result = result * 59 + (int)($enterTime >>> 32 ^ $enterTime);
        final long $lastLoginTime = this.getLastLoginTime();
        result = result * 59 + (int)($lastLoginTime >>> 32 ^ $lastLoginTime);
        result = result * 59 + this.getSex();
        result = result * 59 + this.getFaction();
        result = result * 59 + this.getDrill();
        final Object $marking = this.getMarking();
        result = result * 59 + (($marking == null) ? 43 : $marking.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "PlayerProfile(sid=" + this.getSid() + ", roleId=" + this.getRoleId() + ", id=" + this.getId() + ", name=" + this.getName() + ", icon=" + this.getIcon() + ", serial=" + this.getSerial() + ", level=" + this.getLevel() + ", exp=" + this.getExp() + ", nextExp=" + this.getNextExp() + ", relive=" + this.getRelive() + ", race=" + this.getRace() + ", enterTime=" + this.getEnterTime() + ", lastLoginTime=" + this.getLastLoginTime() + ", sex=" + this.getSex() + ", faction=" + this.getFaction() + ", drill=" + this.getDrill() + ", marking=" + this.getMarking() + ")";
    }
}
