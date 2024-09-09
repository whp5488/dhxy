package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.Field;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_refine")
public class SysRefine implements Serializable
{
    @Column("id")
    private int id;
    @Column("dict")
    private String dict;
    @Column("position")
    private int position;
    @Column("fields")
    @Default("[]")
    private List<Field> fields;
    @Column("pos")
    @Default("[]")
    private List<Integer> pos;
    
    public SysRefine() {
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public int getPosition() {
        return this.position;
    }
    
    public List<Field> getFields() {
        return this.fields;
    }
    
    public List<Integer> getPos() {
        return this.pos;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setPosition(final int position) {
        this.position = position;
    }
    
    public void setFields(final List<Field> fields) {
        this.fields = fields;
    }
    
    public void setPos(final List<Integer> pos) {
        this.pos = pos;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysRefine)) {
            return false;
        }
        SysRefine other = (SysRefine)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$dict = this.getDict();
        Object other$dict = other.getDict();
        if (!((this$dict != null) ? this$dict.equals(other$dict) : (other$dict == null))) {
            return false;
        }
        if (this.getPosition() != other.getPosition()) {
            return false;
        }
        Object this$fields = this.getFields();
        Object other$fields = other.getFields();
        if (!((this$fields != null) ? this$fields.equals(other$fields) : (other$fields == null))) {
            return false;
        }
        Object this$pos = this.getPos();
        Object other$pos = other.getPos();
        return (this$pos != null) ? this$pos.equals(other$pos) : (other$pos == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysRefine;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        result = result * 59 + this.getPosition();
        Object $fields = this.getFields();
        result = result * 59 + (($fields == null) ? 43 : $fields.hashCode());
        Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysRefine(id=" + this.getId() + ", dict=" + this.getDict() + ", position=" + this.getPosition() + ", fields=" + this.getFields() + ", pos=" + this.getPos() + ")";
    }
}
