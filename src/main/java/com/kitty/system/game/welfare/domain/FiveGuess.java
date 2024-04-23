package com.kitty.system.game.welfare.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbRecordService;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("five_guess")
public class FiveGuess extends BaseEntity<Long> {
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Column("r_id")
  private long rId;
  
  @Column("money")
  private long money = 0L;

  @Column("amount")
  private int amount = 0;
  
  @Column("choice")
  private short choice = 0;
  
  @Column("prize")
  private short prize = 0;
  
  @Column("token")
  private String token;

  @Column("count")
  private short count = WelfareDataPool.FIVE_GUESS_COUNT;
  
  public FiveGuess(Long id, long rId) {
    this.id = id;
    this.rId = rId;
  }
  
  public void clear() {
    this.amount = 0;
    this.choice = 0;
    this.prize = 0;
    this.token = null;
  }
  
  public void save() {
    DbRecordService.getInstance().add2Queue(this);
  }
  
  public FiveGuess() {}
}
