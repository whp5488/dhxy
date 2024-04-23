package com.kitty.system.game.fightRecord.service;

import com.kitty.common.thread.IdWorker;
import com.kitty.mina.message.Message;
import com.kitty.record.domain.FightRecord;
import com.kitty.record.domain.FightRecordData;
import com.kitty.system.game.fightRecord.domain.FightSkillRecord;
import com.kitty.system.game.fightRecord.message.RespFightRecords;
import com.kitty.system.game.fightRecord.pojo.MiniFightRecord;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.system.game.role.domain.Role;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

@Service
public class FightRecordService {
  @Autowired
  private Dao dao;
  
  @Autowired
  private MongoTemplate mongoTemplate;
  
  @Autowired
  private IdWorker idWorker;
  
  public void saveRecord(Fight fight, Message message, String sendRoleIds) {}
  
  private byte[] encode(Message message) {
    ByteArrayOutputStream bos = new ByteArrayOutputStream();
    try {
      ObjectOutputStream out = new ObjectOutputStream(bos);
      out.writeObject(message);
      byte[] bytes = bos.toByteArray();
      return bytes;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    } 
  }
  
  private Message decode(byte[] bytes) {
    ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
    try {
      ObjectInputStream in = new ObjectInputStream(bis);
      Message msg = (Message)in.readObject();
      return msg;
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } 
    return null;
  }
  
  public void playerInFight(long roleId, long recordId) {
    Query query = new Query();
    query.addCriteria((CriteriaDefinition)Criteria.where("id").is(Long.valueOf(recordId)));
    FightRecordData fightRecordData = (FightRecordData)this.mongoTemplate.findOne(query, FightRecordData.class, "fightRecordData");
    if (fightRecordData != null) {
      Message msg = decode(fightRecordData.getMsgData());
      if (msg != null)
        FightMessageUtil.sendFightPackage(roleId, msg); 
    } 
  }
  
  public void playerFightMsg(long roleId, long recordId, short round) {
    Query query = new Query();
    query.addCriteria((CriteriaDefinition)Criteria.where("recordId").is(Long.valueOf(recordId)));
    query.addCriteria((CriteriaDefinition)Criteria.where("round").is(Short.valueOf(round)));
    query.with(new Sort(Sort.Direction.ASC, new String[] { "createTime" }));
    List<FightRecord> records = this.mongoTemplate.find(query, FightRecord.class, "fightRecord");
    if (records != null && records.size() > 0)
      for (FightRecord record : records) {
        query = new Query();
        query.addCriteria((CriteriaDefinition)Criteria.where("id").is(record.getId()));
        FightRecordData fightRecordData = (FightRecordData)this.mongoTemplate.findOne(query, FightRecordData.class, "fightRecordData");
        if (fightRecordData != null) {
          Message msg = decode(fightRecordData.getMsgData());
          if (msg != null)
            FightMessageUtil.sendFightPackage(roleId, msg); 
        } 
      }  
  }
  
  public void getPlayerFightRecord(Role role, byte type, long objId) {
    Query query = new Query();
    query.addCriteria((CriteriaDefinition)Criteria.where("type").is(Integer.valueOf(1)));
    if (type == 1) {
      query.addCriteria((CriteriaDefinition)Criteria.where("fightId").is(Long.valueOf(objId)));
    } else if (type == 2) {
      query.addCriteria((CriteriaDefinition)Criteria.where("roleIds").regex(".*?" + objId + ".*"));
    } else {
      return;
    } 
    List<FightRecord> list = this.mongoTemplate.find(query, FightRecord.class, "fightRecord");
    List<MiniFightRecord> returnList = new ArrayList<>();
    for (FightRecord record : list) {
      MiniFightRecord miniFightRecord = new MiniFightRecord(record);
      returnList.add(miniFightRecord);
    } 
    if (returnList != null && returnList.size() > 0) {
      RespFightRecords respFightRecords = new RespFightRecords(String.valueOf(objId), returnList);
      FightMessageUtil.sendFightPackage(role.getId().longValue(), (Message)respFightRecords);
    } 
  }
  
  public FightSkillRecord testGetFightSkillRecord(long recordId) {
    FightSkillRecord fightSkillRecord = (FightSkillRecord)this.dao.fetch(FightSkillRecord.class, (Condition)Cnd.where("id", "=", Long.valueOf(recordId)));
    return fightSkillRecord;
  }
  
  public List<FightSkillRecord> testGetFightSkillRecords(long startTime, long endTime, long roleId) {
    Sql sql = Sqls.create("select * from (select * from fight_skill_record where time < " + endTime + " and time > " + startTime + ") t");
    sql.setCallback(Sqls.callback.entities());
    sql.setEntity(this.dao.getEntity(FightSkillRecord.class));
    this.dao.execute(sql);
    List<FightSkillRecord> list = sql.getList(FightSkillRecord.class);
    List<FightSkillRecord> returnList = new ArrayList<>();
    if (list != null && list.size() > 0)
      for (FightSkillRecord fightSkillRecord : list) {
        fightSkillRecord.doAfterInit();
        if (fightSkillRecord.getDataM().contains(roleId + ""))
          returnList.add(fightSkillRecord); 
      }  
    return returnList;
  }
  
  public List<FightSkillRecord> testGetFightSkillRecordsNew(long startTime, long endTime, long roleId) {
    Sql sql = Sqls.create("select * from fight_skill_record where time < " + endTime + " and time > " + startTime + " and role_ids like '%" + roleId + "%'");
    sql.setCallback(Sqls.callback.entities());
    sql.setEntity(this.dao.getEntity(FightSkillRecord.class));
    this.dao.execute(sql);
    List<FightSkillRecord> list = sql.getList(FightSkillRecord.class);
    for (FightSkillRecord fightSkillRecord : list)
      fightSkillRecord.doAfterInit(); 
    return list;
  }
  
  public void clearFightRecordScheduled() {
    long time = System.currentTimeMillis() - 172800000L;
    Query query = new Query((CriteriaDefinition)Criteria.where("createTime").lt(Long.valueOf(time)));
    this.mongoTemplate.remove(query, FightRecord.class);
    this.mongoTemplate.remove(query, FightRecordData.class);
    this.mongoTemplate.remove(query, FightSkillRecord.class);
  }
}
