package com.kitty.system.game.activity.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbRecordService;
import com.kitty.common.utils.TimeUtil;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("week_treasure")
public class WeekTreasure extends BaseEntity<Long> {
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Column("r_id")
  private long rId;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setShare(short share) {
    this.share = share;
  }
  
  public void setTime(int time) {
    this.time = time;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  @Column("serial")
  private int serial = 0;
  
  public int getSerial() {
    return this.serial;
  }
  
  @Column("share")
  private short share = 0;
  
  public short getShare() {
    return this.share;
  }
  
  @Column("time")
  private int time = (new Long(
      TimeUtil.getTimeMills() / 1000L)).intValue();
  
  public int getTime() {
    return this.time;
  }
  
  public WeekTreasure(Long id, long rId, int serial) {
    this.id = id;
    this.rId = rId;
    this.serial = serial;
  }
  
  public void save() {
    DbRecordService.getInstance().add2Queue(this);
  }
  
  public WeekTreasure() {}
}
