package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_new_district_sign")
public class SysDataNewDistrictSign {
  @Column("day")
  private byte day;
  
  @Comment("奖励")
  @Column("content")
  @Default("[]")
  private List<Param> list;

}
