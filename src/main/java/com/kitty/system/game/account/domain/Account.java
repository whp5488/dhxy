package com.kitty.system.game.account.domain;

import com.kitty.common.db.BaseEntity;
import java.util.Date;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;

@Table("t_account")
public class Account extends BaseEntity<String> {
  @Name
  private String id;
  
  @Column
  private String userName;
  
  @Column("mobile")
  private String mobile;
  
  @Column("channel_code")
  private String channelCode;
  
  public void setId(String id) {
    this.id = id;
  }
  
  public void setUserName(String userName) {
    this.userName = userName;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public void setChannelCode(String channelCode) {
    this.channelCode = channelCode;
  }
  
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  
  public void setIsStop(int isStop) {
    this.isStop = isStop;
  }
  
  public String getId() {
    return this.id;
  }
  
  public String getUserName() {
    return this.userName;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  public String getChannelCode() {
    return this.channelCode;
  }
  
  @Column("create_time")
  private Date createTime = new Date();
  
  @Column("is_stop")
  private int isStop;
  
  public Date getCreateTime() {
    return this.createTime;
  }
  
  public int getIsStop() {
    return this.isStop;
  }
}
