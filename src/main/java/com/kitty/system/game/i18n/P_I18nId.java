package com.kitty.system.game.i18n;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;

@Table("p_i18nid")
public class P_I18nId {
  @Column
  @Comment("字典")
  private Integer serial;
  
  @Column
  @Comment("模块")
  private String model;
  
  @Column
  @Comment("内容")
  private String content;
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public void setModel(String model) {
    this.model = model;
  }
  
  public void setContent(String content) {
    this.content = content;
  }
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public String getModel() {
    return this.model;
  }
  
  public String getContent() {
    return this.content;
  }
}
