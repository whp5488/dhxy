package com.kitty.system.init.domain;

import com.kitty.system.common.enter.FieldValue;
import com.kitty.system.init.domain.vo.Prop;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_pet_acsry_attr_lib")
public class SysPetAcsryAttrLib {
  @Column("dict")
  private String dict;
  
  @Column("base_attr")
  @Default("[]")
  private List<FieldValue> baseAttr;
  
  @Column("prop_by_rarity")
  @Default("[]")
  private Map<Byte, List<Prop>> propByRarity;
}
