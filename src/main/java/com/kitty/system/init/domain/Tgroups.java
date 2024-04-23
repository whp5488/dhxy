package com.kitty.system.init.domain;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("t_groups")
public class Tgroups {
  @Column("skid")
  private String skid;
  
  @Column("groups_name")
  private String groupsName;

}
