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
    
    public Mount(Horse horse) {
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
        List<FiedValue> list = new ArrayList<>();
        for (Integer key : horse.getSkill().keySet()) {
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
        Mount other = (Mount)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
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
        Object this$exp = this.getExp();
        Object other$exp = other.getExp();
        if (!((this$exp != null) ? this$exp.equals(other$exp) : (other$exp == null))) {
            return false;
        }
        if (this.getScore() != other.getScore()) {
            return false;
        }
        if (this.getState() != other.getState()) {
            return false;
        }
        Object this$skill = this.getSkill();
        Object other$skill = other.getSkill();
        return (this$skill != null) ? this$skill.equals(other$skill) : (other$skill == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Mount;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        result = result * 59 + this.getSerial();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getMoveSp();
        result = result * 59 + this.getGrowUp();
        result = result * 59 + this.getGrowthDan();
        result = result * 59 + this.getVit();
        Object $exp = this.getExp();
        result = result * 59 + (($exp == null) ? 43 : $exp.hashCode());
        result = result * 59 + this.getScore();
        result = result * 59 + this.getState();
        Object $skill = this.getSkill();
        result = result * 59 + (($skill == null) ? 43 : $skill.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "Mount(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", level=" + (int)this.getLevel() + ", moveSp=" + this.getMoveSp() + ", growUp=" + this.getGrowUp() + ", growthDan=" + (int)this.getGrowthDan() + ", vit=" + this.getVit() + ", exp=" + this.getExp() + ", score=" + this.getScore() + ", state=" + (int)this.getState() + ", skill=" + this.getSkill() + ")";
    }
}
