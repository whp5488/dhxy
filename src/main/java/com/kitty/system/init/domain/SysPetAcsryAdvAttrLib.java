package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.Prop;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_pet_acsry_adv_attr_lib")
public class SysPetAcsryAdvAttrLib {
  @Column("dict")
  private String dict;
  
  @Column("attr_serial")
  private int attrSerial;
  
  @Column("prop")
  @Default("[]")
  private List<Prop> propList;
  
  @Column("score")
  private int score;
  
  @Column("skill_id")
  @Default("[]")
  private List<Integer> skillList;
  
  @Column("value")
  @Default("[]")
  private List<Integer> valueList;
  
  @Column("value_show_type")
  private int type;

}
