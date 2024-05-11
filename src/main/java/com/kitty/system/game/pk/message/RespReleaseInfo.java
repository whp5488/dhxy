
package com.kitty.system.game.pk.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 45232)
public class RespReleaseInfo extends Message
{
    private int roleId;
    private int serial;
    private String name;
    private byte relive;
    private short level;
    private int weapon;
    private int gem;
    private List<Integer> lightEffect;
    private List<Integer> specialIcon;
    private List<FiedValue> list;
    private String bail;
    
    public RespReleaseInfo() {
        this.relive = 0;
        this.level = 0;
        this.weapon = 0;
        this.gem = 0;
        this.lightEffect = new ArrayList<Integer>();
        this.specialIcon = new ArrayList<Integer>();
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getRelive() {
        return this.relive;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getWeapon() {
        return this.weapon;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public List<Integer> getLightEffect() {
        return this.lightEffect;
    }
    
    public List<Integer> getSpecialIcon() {
        return this.specialIcon;
    }
    
    public List<FiedValue> getList() {
        return this.list;
    }
    
    public String getBail() {
        return this.bail;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRelive(final byte relive) {
        this.relive = relive;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setWeapon(final int weapon) {
        this.weapon = weapon;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setLightEffect(final List<Integer> lightEffect) {
        this.lightEffect = lightEffect;
    }
    
    public void setSpecialIcon(final List<Integer> specialIcon) {
        this.specialIcon = specialIcon;
    }
    
    public void setList(final List<FiedValue> list) {
        this.list = list;
    }
    
    public void setBail(final String bail) {
        this.bail = bail;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespReleaseInfo)) {
            return false;
        }
        final RespReleaseInfo other = (RespReleaseInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getRoleId() != other.getRoleId()) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0091: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0091;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0091;
            }
            return false;
        }
        if (this.getRelive() != other.getRelive()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getWeapon() != other.getWeapon()) {
            return false;
        }
        if (this.getGem() != other.getGem()) {
            return false;
        }
        final Object this$lightEffect = this.getLightEffect();
        final Object other$lightEffect = other.getLightEffect();
        Label_0180: {
            if (this$lightEffect == null) {
                if (other$lightEffect == null) {
                    break Label_0180;
                }
            }
            else if (this$lightEffect.equals(other$lightEffect)) {
                break Label_0180;
            }
            return false;
        }
        final Object this$specialIcon = this.getSpecialIcon();
        final Object other$specialIcon = other.getSpecialIcon();
        Label_0217: {
            if (this$specialIcon == null) {
                if (other$specialIcon == null) {
                    break Label_0217;
                }
            }
            else if (this$specialIcon.equals(other$specialIcon)) {
                break Label_0217;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0254: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0254;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0254;
            }
            return false;
        }
        final Object this$bail = this.getBail();
        final Object other$bail = other.getBail();
        if (this$bail == null) {
            if (other$bail == null) {
                return true;
            }
        }
        else if (this$bail.equals(other$bail)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespReleaseInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getRoleId();
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getRelive();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getWeapon();
        result = result * 59 + this.getGem();
        final Object $lightEffect = this.getLightEffect();
        result = result * 59 + (($lightEffect == null) ? 43 : $lightEffect.hashCode());
        final Object $specialIcon = this.getSpecialIcon();
        result = result * 59 + (($specialIcon == null) ? 43 : $specialIcon.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $bail = this.getBail();
        result = result * 59 + (($bail == null) ? 43 : $bail.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespReleaseInfo(roleId=" + this.getRoleId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", relive=" + this.getRelive() + ", level=" + this.getLevel() + ", weapon=" + this.getWeapon() + ", gem=" + this.getGem() + ", lightEffect=" + this.getLightEffect() + ", specialIcon=" + this.getSpecialIcon() + ", list=" + this.getList() + ", bail=" + this.getBail() + ")";
    }
}
