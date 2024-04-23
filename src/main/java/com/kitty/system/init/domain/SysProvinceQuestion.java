package com.kitty.system.init.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_province_question")
public class SysProvinceQuestion implements Serializable {
  private static final long serialVersionUID = 1L;
  
  @Comment("问题编号")
  @Column("serial")
  private int serial;
  
  @Comment("答案")
  @Column("answer")
  private String answer;
  
  @Comment("类型：1.常识，2.地理，3.历史，4.文学，5.饮食，6.游戏")
  @Column("type")
  private byte type;
}
