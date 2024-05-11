
package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_seal_demon")
public class SysDataSealDemon implements Serializable
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
    @Column("scene")
    private int scene;
    @Column("num")
    private long num;
    @Column("war")
    @Default("[]")
    private List<Integer> war;
    @Column("award")
    @Default("[]")
    private List<Param> award;
    
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
    
    public int getScene() {
        return this.scene;
    }
    
    public long getNum() {
        return this.num;
    }
    
    public List<Integer> getWar() {
        return this.war;
    }
    
    public List<Param> getAward() {
        return this.award;
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
    
    public void setScene(final int scene) {
        this.scene = scene;
    }
    
    public void setNum(final long num) {
        this.num = num;
    }
    
    public void setWar(final List<Integer> war) {
        this.war = war;
    }
    
    public void setAward(final List<Param> award) {
        this.award = award;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataSealDemon)) {
            return false;
        }
        final SysDataSealDemon other = (SysDataSealDemon)o;
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
        if (this.getScene() != other.getScene()) {
            return false;
        }
        if (this.getNum() != other.getNum()) {
            return false;
        }
        final Object this$war = this.getWar();
        final Object other$war = other.getWar();
        Label_0192: {
            if (this$war == null) {
                if (other$war == null) {
                    break Label_0192;
                }
            }
            else if (this$war.equals(other$war)) {
                break Label_0192;
            }
            return false;
        }
        final Object this$award = this.getAward();
        final Object other$award = other.getAward();
        if (this$award == null) {
            if (other$award == null) {
                return true;
            }
        }
        else if (this$award.equals(other$award)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataSealDemon;
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
        result = result * 59 + this.getScene();
        final long $num = this.getNum();
        result = result * 59 + (int)($num >>> 32 ^ $num);
        final Object $war = this.getWar();
        result = result * 59 + (($war == null) ? 43 : $war.hashCode());
        final Object $award = this.getAward();
        result = result * 59 + (($award == null) ? 43 : $award.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataSealDemon(id=" + this.getId() + ", name=" + this.getName() + ", title=" + this.getTitle() + ", grade=" + this.getGrade() + ", scene=" + this.getScene() + ", num=" + this.getNum() + ", war=" + this.getWar() + ", award=" + this.getAward() + ")";
    }
}
