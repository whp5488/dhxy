package com.kitty.system.game.activity.model;

import com.kitty.common.db.Db4EventService;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_event")
public class Event extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Column("serial")
    private int serial;
    @Column("para")
    @ColDefine(customType = "text")
    private String para;
    
    public Event() {
    }
    
    public Event(Long id, int serial) {
        this.id = id;
        this.serial = serial;
    }
    
    public void save() {
        Db4EventService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getPara() {
        return this.para;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPara(final String para) {
        this.para = para;
    }
    
    @Override
    public String toString() {
        return "Event(id=" + this.getId() + ", serial=" + this.getSerial() + ", para=" + this.getPara() + ")";
    }
}
