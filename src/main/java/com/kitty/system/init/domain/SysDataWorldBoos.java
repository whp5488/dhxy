
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
        this.special = new ArrayList<Param>();
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
        final SysDataWorldBoos other = (SysDataWorldBoos)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$title = this.getTitle();
        final Object other$title = other.getTitle();
        Label_0115: {
            if (this$title == null) {
                if (other$title == null) {
                    break Label_0115;
                }
            }
            else if (this$title.equals(other$title)) {
                break Label_0115;
            }
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
        final Object this$war = this.getWar();
        final Object other$war = other.getWar();
        Label_0204: {
            if (this$war == null) {
                if (other$war == null) {
                    break Label_0204;
                }
            }
            else if (this$war.equals(other$war)) {
                break Label_0204;
            }
            return false;
        }
        if (this.getNum() != other.getNum()) {
            return false;
        }
        final Object this$award = this.getAward();
        final Object other$award = other.getAward();
        Label_0255: {
            if (this$award == null) {
                if (other$award == null) {
                    break Label_0255;
                }
            }
            else if (this$award.equals(other$award)) {
                break Label_0255;
            }
            return false;
        }
        final Object this$special = this.getSpecial();
        final Object other$special = other.getSpecial();
        if (this$special == null) {
            if (other$special == null) {
                return true;
            }
        }
        else if (this$special.equals(other$special)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataWorldBoos;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $title = this.getTitle();
        result = result * 59 + (($title == null) ? 43 : $title.hashCode());
        result = result * 59 + this.getGrade();
        result = result * 59 + this.getIcon();
        result = result * 59 + this.getScene();
        result = result * 59 + this.getLevel();
        final Object $war = this.getWar();
        result = result * 59 + (($war == null) ? 43 : $war.hashCode());
        final long $num = this.getNum();
        result = result * 59 + (int)($num >>> 32 ^ $num);
        final Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        final Object $special = this.getSpecial();
        result = result * 59 + (($special == null) ? 43 : $special.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataWorldBoos(id=" + this.getId() + ", name=" + this.getName() + ", title=" + this.getTitle() + ", grade=" + this.getGrade() + ", icon=" + this.getIcon() + ", scene=" + this.getScene() + ", level=" + this.getLevel() + ", war=" + this.getWar() + ", num=" + this.getNum() + ", award=" + this.getAward() + ", special=" + this.getSpecial() + ")";
    }
}
