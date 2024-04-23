package com.kitty.system.game.welfare.domain;

import com.kitty.system.init.domain.vo.Param;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_gift_bag")
public class GiftBag {
  @Column("name")
  private String name;
  
  @Column("reward")
  @ColDefine(type = ColType.TEXT)
  private List<Param> rewards;
  
  @Column("time")
  private long time;

}
