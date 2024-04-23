package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Param;
import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_common_award")
public class SysCommonAward implements Serializable {
  @Column("dict")
  private String dict;
  
  @Column("award")
  @Default("[]")
  private List<Param> award;
  
  @Column("special")
  private List<Param> special;

}
