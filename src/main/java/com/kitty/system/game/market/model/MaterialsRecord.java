
package com.kitty.system.game.market.model;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_materials_record")
public class MaterialsRecord extends BaseEntity<Long>
{
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Column("time")
    private int time;
    @Column("serial")
    private int serial;
    @Column("publish")
    private long publish;
    @Column("consume")
    private long consume;
    
    public MaterialsRecord() {
        this.time = 0;
        this.serial = 0;
        this.publish = 0L;
        this.consume = 0L;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public long getPublish() {
        return this.publish;
    }
    
    public long getConsume() {
        return this.consume;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setPublish(final long publish) {
        this.publish = publish;
    }
    
    public void setConsume(final long consume) {
        this.consume = consume;
    }
}
