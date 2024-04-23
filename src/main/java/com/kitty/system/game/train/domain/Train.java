package com.kitty.system.game.train.domain;

import com.kitty.common.db.BaseEntity;
import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("train")
public class Train extends BaseEntity<Long> {
  public String toString() {
    return "Train()";
  }
  
  public Long getId() {
    return null;
  }
}
