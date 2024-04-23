package com.kitty.common.db;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
public abstract class BaseEntity<PK extends Comparable<PK> & Serializable> implements Serializable {
  protected boolean delete = false;

  // 注释不强制实现
  // public abstract PK getId();
  
  public void doAfterInit() {}
  
  public void doBeforeSave() {}
}
