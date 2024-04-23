package com.kitty.system.game.role.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import java.beans.ConstructorProperties;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("domesticate")
public class Domesticate extends BaseEntity<Long> {
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Comment("主键")
  @Column("r_id")
  private long rId;
  
  @Comment("宠物或者坐骑ID")
  @Column("rests_id")
  private long restsId;
  
  @Comment("定义：2.宠物 4.坐骑")
  @Column("logic")
  private byte logic;
  
  @Comment("类型：1.帮派修炼 2.家园灵修")
  @Column("type")
  private byte type;
  
  @Comment("已经获取奖励取次数")
  @Column("already")
  private int already;
  
  @Comment("开始时间")
  @Column("create_time")
  private int createTime;
  
  @Comment("上一次发送时间")
  @Column("prev_time")
  private int prevTime;
  
  @Comment("训练次数")
  @Column("count")
  private int count;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRId(long rId) {
    this.rId = rId;
  }
  
  public void setRestsId(long restsId) {
    this.restsId = restsId;
  }
  
  public void setLogic(byte logic) {
    this.logic = logic;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setAlready(int already) {
    this.already = already;
  }
  
  public void setCreateTime(int createTime) {
    this.createTime = createTime;
  }
  
  public void setPrevTime(int prevTime) {
    this.prevTime = prevTime;
  }
  
  public void setCount(int count) {
    this.count = count;
  }
  
  @ConstructorProperties({"id", "rId", "restsId", "logic", "type", "already", "createTime", "prevTime", "count"})
  public Domesticate(Long id, long rId, long restsId, byte logic, byte type, int already, int createTime, int prevTime, int count) {
    this.id = id;
    this.rId = rId;
    this.restsId = restsId;
    this.logic = logic;
    this.type = type;
    this.already = already;
    this.createTime = createTime;
    this.prevTime = prevTime;
    this.count = count;
  }
  
  public Domesticate() {}
  
  public Long getId() {
    return this.id;
  }
  
  public long getRId() {
    return this.rId;
  }
  
  public long getRestsId() {
    return this.restsId;
  }
  
  public byte getLogic() {
    return this.logic;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public int getAlready() {
    return this.already;
  }
  
  public int getCreateTime() {
    return this.createTime;
  }
  
  public int getPrevTime() {
    return this.prevTime;
  }
  
  public int getCount() {
    return this.count;
  }
  
  public void save() {
    Db4CommonService.getInstance().add2Queue(this);
  }
}
