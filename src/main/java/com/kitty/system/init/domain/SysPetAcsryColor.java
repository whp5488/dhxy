package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.PetAccColor;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("data_pet_acsry_color")
public class SysPetAcsryColor {
  @Column("dict")
  private String dict;
  
  @Column("value")
  private Map<Byte, PetAccColor> value;
}
