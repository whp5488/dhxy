
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
        final SysRefine other = (SysRefine)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        final Object this$dict = this.getDict();
        final Object other$dict = other.getDict();
        Label_0078: {
            if (this$dict == null) {
                if (other$dict == null) {
                    break Label_0078;
                }
            }
            else if (this$dict.equals(other$dict)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getPosition() != other.getPosition()) {
            return false;
        }
        final Object this$fields = this.getFields();
        final Object other$fields = other.getFields();
        Label_0128: {
            if (this$fields == null) {
                if (other$fields == null) {
                    break Label_0128;
                }
            }
            else if (this$fields.equals(other$fields)) {
                break Label_0128;
            }
            return false;
        }
        final Object this$pos = this.getPos();
        final Object other$pos = other.getPos();
        if (this$pos == null) {
            if (other$pos == null) {
                return true;
            }
        }
        else if (this$pos.equals(other$pos)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysRefine;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        result = result * 59 + this.getPosition();
        final Object $fields = this.getFields();
        result = result * 59 + (($fields == null) ? 43 : $fields.hashCode());
        final Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysRefine(id=" + this.getId() + ", dict=" + this.getDict() + ", position=" + this.getPosition() + ", fields=" + this.getFields() + ", pos=" + this.getPos() + ")";
    }
}
