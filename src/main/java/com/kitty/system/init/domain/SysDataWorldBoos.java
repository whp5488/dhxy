package com.kitty.system.init.domain;

import java.util.ArrayList;
import com.kitty.system.init.domain.vo.Param;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_world_boos")
public class SysDataWorldBoos implements Serializable
{
    @Column("id")
    @Id
    private int id;
    @Column("name")
    private String name;
    @Column("title")
    private String title;
    @Column("grade")
    private int grade;
    @Column("icon")
    private int icon;
    @Column("scene")
    private int scene;
    @Column("level")
    private int level;
    @Column("war")
    @Default("[]")
    private List<Integer> war;
    @Column("num")
    private long num;
    @Column("award")
    @Default("[]")
    private List<Param> award;
    @Column("special")
    @Default("[]")
    private List<Param> special;
    
    public SysDataWorldBoos() {
        this.special = new ArrayList<>();
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getTitle() {
        return this.title;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public int getScene() {
        return this.scene;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public List<Integer> getWar() {
        return this.war;
    }
    
    public long getNum() {
        return this.num;
    }
    
    public List<Param> getAward() {
        return this.award;
    }
    
    public List<Param> getSpecial() {
        return this.special;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setTitle(final String title) {
        this.title = title;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setScene(final int scene) {
        this.scene = scene;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setWar(final List<Integer> war) {
        this.war = war;
    }
    
    public void setNum(final long num) {
        this.num = num;
    }
    
    public void setAward(final List<Param> award) {
        this.award = award;
    }
    
    public void setSpecial(final List<Param> special) {
        this.special = special;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataWorldBoos)) {
            return false;
        }
        SysDataWorldBoos other = (SysDataWorldBoos)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$title = this.getTitle();
        Object other$title = other.getTitle();
        if (!((this$title != null) ? this$title.equals(other$title) : (other$title == null))) {
            return false;
        }
        if (this.getGrade() != other.getGrade()) {
            return false;
        }
        if (this.getIcon() != other.getIcon()) {
            return false;
        }
        if (this.getScene() != other.getScene()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        Object this$war = this.getWar();
        Object other$war = other.getWar();
        if (!((this$war != null) ? this$war.equals(other$war) : (other$war == null))) {
            return false;
        }
        if (this.getNum() != other.getNum()) {
            return false;
        }
        Object this$award = this.getAward();
        Object other$award = other.getAward();
        if (!((this$award != null) ? this$award.equals(other$award) : (other$award == null))) {
            return false;
        }
        Object this$special = this.getSpecial();
        Object other$special = other.getSpecial();
        return (this$special != null) ? this$special.equals(other$special) : (other$special == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataWorldBoos;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        result = result * 59 + this.getGrade();
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getScene();
        result = result * 59 + this.getLevel();
        Object $war = this.getWar();
        result = result * 59 + (($war == null) ? 43 : $war.hashCode());
        long $num = this.getNum();
        result = result * 59 + (int)($num >>> 32 ^ $num);
        Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        Object $special = this.getSpecial();
        result = result * 59 + (($special == null) ? 43 : $special.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataWorldBoos(id=" + this.getId() + ", name=" + this.getName() + ", title=" + this.getTitle() + ", grade=" + this.getGrade() + ", icon=" + this.getIcon() + ", scene=" + this.getScene() + ", level=" + this.getLevel() + ", war=" + this.getWar() + ", num=" + this.getNum() + ", award=" + this.getAward() + ", special=" + this.getSpecial() + ")";
    }
}
