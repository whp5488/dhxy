
package com.kitty.system.game.horse.message.vo;

import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;

public class Mount
{
    private String id;
    private int serial;
    private String name;
    private short level;
    private int moveSp;
    private int growUp;
    private byte growthDan;
    private int vit;
    private String exp;
    private int score;
    private byte state;
    private List<FiedValue> skill;
    
    public Mount() {
        this.growthDan = 0;
        this.score = 0;
        this.state = 0;
    }
    
    public Mount(final Horse horse) {
        this.growthDan = 0;
        this.score = 0;
        this.state = 0;
        this.id = String.valueOf(horse.getId());
        this.serial = horse.getSerial();
        this.name = horse.getName();
        this.level = horse.getLevel();
        this.moveSp = horse.getMoveSp();
        this.growUp = horse.getGrowUp();
        this.growthDan = horse.getGrowthDan();
        this.vit = horse.getVit();
        this.exp = String.valueOf(horse.getExp());
        this.score = horse.getScore();
        this.state = horse.getState();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        for (final Integer key : horse.getSkill().keySet()) {
            list.add(new FiedValue(String.valueOf(key), (byte)4, String.valueOf(horse.getSkill().get(key))));
        }
        this.skill = list;
    }
    
    public String getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getMoveSp() {
        return this.moveSp;
    }
    
    public int getGrowUp() {
        return this.growUp;
    }
    
    public byte getGrowthDan() {
        return this.growthDan;
    }
    
    public int getVit() {
        return this.vit;
    }
    
    public String getExp() {
        return this.exp;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public List<FiedValue> getSkill() {
        return this.skill;
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setMoveSp(final int moveSp) {
        this.moveSp = moveSp;
    }
    
    public void setGrowUp(final int growUp) {
        this.growUp = growUp;
    }
    
    public void setGrowthDan(final byte growthDan) {
        this.growthDan = growthDan;
    }
    
    public void setVit(final int vit) {
        this.vit = vit;
    }
    
    public void setExp(final String exp) {
        this.exp = exp;
    }
    
    public void setScore(final int score) {
        this.score = score;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setSkill(final List<FiedValue> skill) {
        this.skill = skill;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Mount)) {
            return false;
        }
        final Mount other = (Mount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0115: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0115;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0115;
            }
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getMoveSp() != other.getMoveSp()) {
            return false;
        }
        if (this.getGrowUp() != other.getGrowUp()) {
            return false;
        }
        if (this.getGrowthDan() != other.getGrowthDan()) {
            return false;
        }
        if (this.getVit() != other.getVit()) {
            return false;
        }
        final Object this$exp = this.getExp();
        final Object other$exp = other.getExp();
        Label_0217: {
            if (this$exp == null) {
                if (other$exp == null) {
                    break Label_0217;
                }
            }
            else if (this$exp.equals(other$exp)) {
                break Label_0217;
            }
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getState() != other.getState()) {
            return false;
        }
        final Object this$skill = this.getSkill();
        final Object other$skill = other.getSkill();
        if (this$skill == null) {
            if (other$skill == null) {
                return true;
            }
        }
        else if (this$skill.equals(other$skill)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Mount;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getMoveSp();
        result = result * 59 + this.getGrowUp();
        result = result * 59 + this.getGrowthDan();
        result = result * 59 + this.getVit();
        final Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        result = result * 59 + this.getScore();
        result = result * 59 + this.getState();
        final Object $skill = this.getSkill();
        result = result * 59 + (($skill == null) ? 43 : $skill.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Mount(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", level=" + this.getLevel() + ", moveSp=" + this.getMoveSp() + ", growUp=" + this.getGrowUp() + ", growthDan=" + this.getGrowthDan() + ", vit=" + this.getVit() + ", exp=" + this.getExp() + ", score=" + this.getScore() + ", state=" + this.getState() + ", skill=" + this.getSkill() + ")";
    }
}
