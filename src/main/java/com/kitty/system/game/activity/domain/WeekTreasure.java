package com.kitty.system.game.activity.domain;

import com.kitty.common.db.DbRecordService;
import com.kitty.common.utils.TimeUtil;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("week_treasure")
public class WeekTreasure extends BaseEntity<Long>
{
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("serial")
    private int serial;
    @Column("share")
    private short share;
    @Column("time")
    private int time;
    
    public WeekTreasure(Long id, long rId, int serial) {
        this.serial = 0;
        this.share = 0;
        this.time = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
        this.id = id;
        this.rId = rId;
        this.serial = serial;
    }
    
    public void save() {
        DbRecordService.getInstance().add2Queue(this);
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
    
    public short getShare() {
        return this.share;
    }
    
    public int getTime() {
        return this.time;
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
    
    public void setShare(final short share) {
        this.share = share;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public WeekTreasure() {
        this.serial = 0;
        this.share = 0;
        this.time = new Long(TimeUtil.getTimeMills() / 1000L).intValue();
    }
}
