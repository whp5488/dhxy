package com.kitty.system.game.fightRecord.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4FightRecordService;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("fight_record")
public class FightRecord extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Comment("记录id")
  @Column("record_id")
  private Long recordId;
  
  @Comment("类型 1=进入战斗，2=回合开始，3=战斗数据")
  @Column("type")
  private byte type;
  
  @Comment("战斗id")
  @Column("fight_id")
  private int fightId;
  
  @Comment("回合数")
  @Column("round")
  private short round;
  
  @Comment("消息编号")
  @Column("module")
  private int module;
  
  @Comment("时间")
  @Column("create_time")
  private long createTime;
  
  @Comment("角色的id集合")
  @Column("role_ids")
  private List<Long> roleIds;
  
  @Comment("当回合数据发送给的用户id集合")
  @Column("send_roles")
  private String sendRoles;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRecordId(Long recordId) {
    this.recordId = recordId;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setFightId(int fightId) {
    this.fightId = fightId;
  }
  
  public void setRound(short round) {
    this.round = round;
  }
  
  public void setModule(int module) {
    this.module = module;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setRoleIds(List<Long> roleIds) {
    this.roleIds = roleIds;
  }
  
  public void setSendRoles(String sendRoles) {
    this.sendRoles = sendRoles;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Long getRecordId() {
    return this.recordId;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public int getFightId() {
    return this.fightId;
  }
  
  public short getRound() {
    return this.round;
  }
  
  public int getModule() {
    return this.module;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  public List<Long> getRoleIds() {
    return this.roleIds;
  }
  
  public String getSendRoles() {
    return this.sendRoles;
  }
  
  public void save() {
    Db4FightRecordService.getInstance().add2Queue(this);
  }
}
