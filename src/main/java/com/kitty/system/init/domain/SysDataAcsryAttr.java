package com.kitty.system.init.domain;

import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.init.domain.vo.Prop;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_acsry_attr_lib")
public class SysDataAcsryAttr {
  @Column("dict")
  private String dict;
  
  @Column("base_attr")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<FieldValue> baseAttrList;
  
  @Column("rnd_range")
  @Default("[]")
  private List<Prop> prop;

}
