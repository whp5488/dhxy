package com.kitty.system.game.npc.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4EventService;
import java.util.Date;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("t_mirror")
public class Mirror extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Comment("角色唯一ID")
  @Column("r_id")
  private Long rId;
  
  @Comment("称号")
  @Column("title")
  private String title;
  
  @Comment("座右铭")
  @Column("motto")
  private String motto;
  
  @Comment("位置信息")
  @Column("pos_info")
  private String posInfo;
  
  @Comment("类型")
  @Column("type")
  private int type;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(Long rId) {
    this.rId = rId;
  }
  
  public void setTitle(String title) {
    this.title = title;
  }
  
  public void setMotto(String motto) {
    this.motto = motto;
  }
  
  public void setPosInfo(String posInfo) {
    this.posInfo = posInfo;
  }
  
  public void setType(int type) {
    this.type = type;
  }
  
  public void setCreateTime(Date createTime) {
    this.createTime = createTime;
  }
  
  public void setExpireTime(Date expireTime) {
    this.expireTime = expireTime;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Long getRId() {
    return this.rId;
  }
  
  public String getTitle() {
    return this.title;
  }
  
  public String getMotto() {
    return this.motto;
  }
  
  public String getPosInfo() {
    return this.posInfo;
  }
  
  public int getType() {
    return this.type;
  }
  
  @Comment("创建时间")
  @Column("create_time")
  private Date createTime = new Date();
  
  public Date getCreateTime() {
    return this.createTime;
  }
  
  @Comment("到期时间")
  @Column("expire_time")
  private Date expireTime = new Date();
  
  public Date getExpireTime() {
    return this.expireTime;
  }
  
  public Mirror(Long id, Long rId, String title, String motto, String posInfo, int type) {
    this.id = id;
    this.rId = rId;
    this.title = title;
    this.motto = motto;
    this.posInfo = posInfo;
    this.type = type;
  }
  
  public void save() {
    Db4EventService.getInstance().add2Queue(this);
  }
  
  public Mirror() {}
}
