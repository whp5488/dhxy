
package com.kitty.system.game.equip.domain;

import com.kitty.common.db.Db4CommonService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("effect")
public class Effect extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("serial")
    private int serial;
    @Column("state")
    private byte state;
    @Column("time")
    private int time;
    private byte part;
    
    public Effect() {
        this.part = 0;
    }
    
    public Effect(final Long id, final long rId, final int serial, final byte state, final int time, final byte part) {
        this.part = 0;
        this.id = id;
        this.rId = rId;
        this.serial = serial;
        this.state = state;
        this.time = time;
        this.part = part;
    }
    
    public void save() {
        Db4CommonService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public byte getPart() {
        return this.part;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setPart(final byte part) {
        this.part = part;
    }
}
