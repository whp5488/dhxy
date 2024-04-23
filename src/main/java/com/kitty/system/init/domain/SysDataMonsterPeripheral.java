package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_data_monster_peripheral")
public class SysDataMonsterPeripheral implements Serializable {
  @Column("serial")
  private int serial;
  
  @Column("weapon_id")
  private int weaponId;
  
  @Column("gem_num")
  private int gemNum;
  
  @Column("color")
  private byte color;
}
