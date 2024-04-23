package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_party_practice")
public class SysPartyPractice implements Serializable {
  @Column("level")
  private int level;
  
  @Column("tribute")
  private int tribute;
  
  @Column("coin")
  private int coin;
  
  @Column("need")
  private int need;

}
