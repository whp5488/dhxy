
package com.kitty.system.game.welfare.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_gift_bag_record")
public class GiftBagRecord extends BaseEntity<Long>
{
    @Id
    @Column("id")
    private long id;
    @Column("r_id")
    private long rId;
    @Column("name")
    private String name;
    @Column("time")
    private int time;
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(final long rId) {
        this.rId = rId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public GiftBagRecord() {
    }
    
    public GiftBagRecord(final long rId, final String name) {
        this.rId = rId;
        this.name = name;
        this.time = new Long(System.currentTimeMillis() / 1000L).intValue();
    }
}
