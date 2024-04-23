package com.kitty.system.game.welfare.domain;

import com.kitty.common.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_gift_bag_record")
public class GiftBagRecord extends BaseEntity<Long> {
  @Id
  @Column("id")
  private long id;
  
  @Column("r_id")
  private long rId;
  
  @Column("name")
  private String name;
  
  @Column("time")
  private int time;

  public GiftBagRecord() {}
  
  public GiftBagRecord(long rId, String name) {
    this.rId = rId;
    this.name = name;
    this.time = (new Long(System.currentTimeMillis() / 1000L)).intValue();
  }
}
