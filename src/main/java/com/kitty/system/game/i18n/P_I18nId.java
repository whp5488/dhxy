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
    
    public P_I18nId() {
    }
    
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
        P_I18nId other = (P_I18nId)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$model = this.getModel();
        Object other$model = other.getModel();
        if (!((this$model != null) ? this$model.equals(other$model) : (other$model == null))) {
            return false;
        }
        Object this$content = this.getContent();
        Object other$content = other.getContent();
        return (this$content != null) ? this$content.equals(other$content) : (other$content == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof P_I18nId;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $model = this.getModel();
        result = result * 59 + (($model == null) ? 43 : $model.hashCode());
        Object $content = this.getContent();
        result = result * 59 + (($content == null) ? 43 : $content.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "P_I18nId(serial=" + this.getSerial() + ", model=" + this.getModel() + ", content=" + this.getContent() + ")";
    }
}
