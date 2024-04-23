package com.kitty.system.game.fightRecord.domain;

import com.kitty.common.db.Db4FightSkillRecordService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.List;

public class FightSkillRecord {
  private long fightId;
  
  private String roleIds;
  
  private long createTime;
  
  private long destroyTime;
  
  private String data;
  
  private String marking;
  
  private List<SkillRecord> dataList;
  
  private String dataM;
  
  public void setFightId(long fightId) {
    this.fightId = fightId;
  }
  
  public void setRoleIds(String roleIds) {
    this.roleIds = roleIds;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setDestroyTime(long destroyTime) {
    this.destroyTime = destroyTime;
  }
  
  public void setData(String data) {
    this.data = data;
  }
  
  public void setMarking(String marking) {
    this.marking = marking;
  }
  
  public void setDataList(List<SkillRecord> dataList) {
    this.dataList = dataList;
  }
  
  public void setDataM(String dataM) {
    this.dataM = dataM;
  }
  
  public FightSkillRecord() {
    this.dataList = new ArrayList<>();
  }
  
  @ConstructorProperties({"fightId", "roleIds", "createTime", "destroyTime", "data", "marking", "dataList", "dataM"})
  public FightSkillRecord(long fightId, String roleIds, long createTime, long destroyTime, String data, String marking, List<SkillRecord> dataList, String dataM) {
    this.dataList = new ArrayList<>();
    this.fightId = fightId;
    this.roleIds = roleIds;
    this.createTime = createTime;
    this.destroyTime = destroyTime;
    this.data = data;
    this.marking = marking;
    this.dataList = dataList;
    this.dataM = dataM;
  }
  
  public long getFightId() {
    return this.fightId;
  }
  
  public String getRoleIds() {
    return this.roleIds;
  }
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  public long getDestroyTime() {
    return this.destroyTime;
  }
  
  public String getData() {
    return this.data;
  }
  
  public String getMarking() {
    return this.marking;
  }
  
  public List<SkillRecord> getDataList() {
    return this.dataList;
  }
  
  public String getDataM() {
    return this.dataM;
  }
  
  public void doBeforeSave() {
    if (this.dataList != null && this.dataList.size() > 0) {
      String dataStr = JsonUtils.object2String(this.dataList);
      this.data = ZipUtil.compress(dataStr);
    } 
  }
  
  public void doAfterInit() {
    if (this.data != null && !this.data.isEmpty()) {
      String content = ZipUtil.uncompress(this.data);
      this.dataM = content;
      this.dataList = (List<SkillRecord>)JsonUtils.string2Collection(content, List.class, SkillRecord.class);
    } 
  }
  
  public void save() {
    Db4FightSkillRecordService.getInstance().add2Queue(this);
  }
}
