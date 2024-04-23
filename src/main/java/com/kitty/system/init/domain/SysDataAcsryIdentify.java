package com.kitty.system.init.domain;

import com.kitty.system.game.vo.init.Item;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry_identify")
public class SysDataAcsryIdentify {
  @Column("suite_type")
  private String suiteType;

  @Column("silver")
  private long silver = 0L;
  
  @Column("item")
  @Default("[]")
  private List<Item> list;

}
