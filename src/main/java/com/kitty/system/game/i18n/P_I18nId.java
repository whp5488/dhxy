
package com.kitty.system.game.i18n;

import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("p_i18nid")
public class P_I18nId
{
    @Column
    @Comment("字典")
    private Integer serial;
    @Column
    @Comment("模块")
    private String model;
    @Column
    @Comment("内容")
    private String content;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getModel() {
        return this.model;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setModel(final String model) {
        this.model = model;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof P_I18nId)) {
            return false;
        }
        final P_I18nId other = (P_I18nId)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$model = this.getModel();
        final Object other$model = other.getModel();
        Label_0102: {
            if (this$model == null) {
                if (other$model == null) {
                    break Label_0102;
                }
            }
            else if (this$model.equals(other$model)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$content = this.getContent();
        final Object other$content = other.getContent();
        if (this$content == null) {
            if (other$content == null) {
                return true;
            }
        }
        else if (this$content.equals(other$content)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof P_I18nId;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $model = this.getModel();
        result = result * 59 + (($model == null) ? 43 : $model.hashCode());
        final Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "P_I18nId(serial=" + this.getSerial() + ", model=" + this.getModel() + ", content=" + this.getContent() + ")";
    }
}
