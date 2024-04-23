package com.kitty.system.game.party.model;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import com.kitty.system.game.party.model.vo.PartyTask;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("party_member")
public class PartyMember extends BaseEntity<Long> implements Comparable<PartyMember> {
  @Id(auto = false)
  private Long id;
  
  @Column("serial")
  @Comment("帮派编号")
  private int serial;
  
  @Column("role_id")
  @Comment("角色编号")
  private int roleId;
  
  @Column("name")
  @Comment("角色名称")
  private String name;
  
  @Column("rei")
  @Comment("角色转生 ")
  private byte rei;
  
  @Column("level")
  @Comment("角色等级 ")
  private short level;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setRoleId(int roleId) {
    this.roleId = roleId;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRei(byte rei) {
    this.rei = rei;
  }
  
  public void setLevel(short level) {
    this.level = level;
  }
  
  public void setGrade(int grade) {
    this.grade = grade;
  }
  
  public void setRace(int race) {
    this.race = race;
  }
  
  public void setRoleSerial(int roleSerial) {
    this.roleSerial = roleSerial;
  }
  
  public void setJob(byte job) {
    this.job = job;
  }
  
  public void setJoinTime(int joinTime) {
    this.joinTime = joinTime;
  }
  
  public void setWeekBangBtl(int weekBangBtl) {
    this.weekBangBtl = weekBangBtl;
  }
  
  public void setThisBangBtl(int thisBangBtl) {
    this.thisBangBtl = thisBangBtl;
  }
  
  public void setWeekEcType(int weekEcType) {
    this.weekEcType = weekEcType;
  }
  
  public void setPartyTaskList(List<PartyTask> partyTaskList) {
    this.partyTaskList = partyTaskList;
  }
  
  public void setContribute(long contribute) {
    this.contribute = contribute;
  }
  
  public void setTeam(String team) {
    this.team = team;
  }
  
  public void setTitleChange(boolean titleChange) {
    this.titleChange = titleChange;
  }
  
  public void setLastOnlineTime(int lastOnlineTime) {
    this.lastOnlineTime = lastOnlineTime;
  }
  
  public void setOnLineStatus(byte onLineStatus) {
    this.onLineStatus = onLineStatus;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public int getSerial() {
    return this.serial;
  }
  
  public int getRoleId() {
    return this.roleId;
  }
  
  public String getName() {
    return this.name;
  }
  
  public byte getRei() {
    return this.rei;
  }
  
  public short getLevel() {
    return this.level;
  }
  
  @Column("grade")
  @Comment("计算后的等级")
  private int grade = 0;
  
  public int getGrade() {
    return this.grade;
  }
  
  @Column("race")
  @Comment("种族")
  private int race = 1;
  
  @Column("role_serial")
  @Comment("角色外观")
  private int roleSerial;
  
  @Column("job")
  @Comment("帮派职位")
  private byte job;
  
  @Column("join_time")
  @Comment("加入时间")
  private int joinTime;
  
  public int getRace() {
    return this.race;
  }
  
  public int getRoleSerial() {
    return this.roleSerial;
  }
  
  public byte getJob() {
    return this.job;
  }
  
  public int getJoinTime() {
    return this.joinTime;
  }
  
  @Column("week_bang_btl")
  @Comment("本周帮战（次数）")
  private int weekBangBtl = 0;
  
  public int getWeekBangBtl() {
    return this.weekBangBtl;
  }
  
  @Column("this_bang_btl")
  @Comment("本次帮战")
  private int thisBangBtl = 0;
  
  public int getThisBangBtl() {
    return this.thisBangBtl;
  }
  
  @Column("week_ec_type")
  @Comment("本周副本（0未做，1已做）")
  private int weekEcType = 0;
  
  public int getWeekEcType() {
    return this.weekEcType;
  }
  
  @Column("day_task")
  @Comment("任务记录")
  @ColDefine(customType = "longtext")
  private List<PartyTask> partyTaskList = new ArrayList<>();
  
  public List<PartyTask> getPartyTaskList() {
    return this.partyTaskList;
  }
  
  @Column("contribute")
  @Comment("历史贡献")
  private long contribute = 0L;
  
  public long getContribute() {
    return this.contribute;
  }
  
  @Column("team")
  @Comment("团名")
  private String team = "";
  
  public String getTeam() {
    return this.team;
  }
  
  @Column("title_change")
  @Comment("称号是否变化")
  private boolean titleChange = false;
  
  private int lastOnlineTime;
  
  public boolean isTitleChange() {
    return this.titleChange;
  }
  
  public int getLastOnlineTime() {
    return this.lastOnlineTime;
  }
  
  private byte onLineStatus = 0;
  
  public byte getOnLineStatus() {
    return this.onLineStatus;
  }
  
  public PartyMember(Role role, int serial, long joinTime, byte job, Long id) {
    this.id = id;
    this.serial = serial;
    this.roleSerial = role.getSerial().intValue();
    this.roleId = role.getRoleId().intValue();
    this.name = role.getName();
    this.job = job;
    this.joinTime = (int)(joinTime / 1000L);
    this.lastOnlineTime = (int)(joinTime / 1000L);
    this.rei = role.getRelive().byteValue();
    this.level = role.getLevel().shortValue();
    this.grade = this.rei * 1000 + this.level;
    this.race = role.getRace().intValue();
  }
  
  public int compareTo(PartyMember o) {
    return 0;
  }
  
  public void save() {
    Db4CommonService.getInstance().add2Queue(this);
  }
  
  public PartyMember() {}
}
