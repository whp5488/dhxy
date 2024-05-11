
package com.kitty.system.game.fightRecord.service;

import org.nutz.dao.sql.Sql;
import org.nutz.dao.Sqls;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.game.fightRecord.domain.FightSkillRecord;
import com.kitty.system.game.fightRecord.message.RespFightRecords;
import com.kitty.system.game.fightRecord.pojo.MiniFightRecord;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import java.util.List;
import com.kitty.record.domain.FightRecord;
import org.springframework.data.domain.Sort;
import com.kitty.system.game.fightnew.utils.FightMessageUtil;
import com.kitty.record.domain.FightRecordData;
import org.springframework.data.mongodb.core.query.CriteriaDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.ObjectOutputStream;
import java.io.ByteArrayOutputStream;
import com.kitty.mina.message.Message;
import com.kitty.system.game.fightnew.bean.Fight;
import com.kitty.common.thread.IdWorker;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class FightRecordService
{
    @Autowired
    private Dao dao;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private IdWorker idWorker;
    
    public void saveRecord(final Fight fight, final Message message, final String sendRoleIds) {
    }
    
    private byte[] encode(final Message message) {
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try {
            final ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(message);
            final byte[] bytes = bos.toByteArray();
            return bytes;
        }
        catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    private Message decode(final byte[] bytes) {
        final ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        try {
            final ObjectInputStream in = new ObjectInputStream(bis);
            final Message msg = (Message)in.readObject();
            return msg;
        }
        catch (final IOException e) {
            e.printStackTrace();
        }
        catch (final ClassNotFoundException e2) {
            e2.printStackTrace();
        }
        return null;
    }
    
    public void playerInFight(final long roleId, final long recordId) {
        final Query query = new Query();
        query.addCriteria((CriteriaDefinition)Criteria.where("id").is((Object)recordId));
        final FightRecordData fightRecordData = (FightRecordData)this.mongoTemplate.findOne(query, (Class)FightRecordData.class, "fightRecordData");
        if (fightRecordData != null) {
            final Message msg = this.decode(fightRecordData.getMsgData());
            if (msg != null) {
                FightMessageUtil.sendFightPackage(roleId, msg);
            }
        }
    }
    
    public void playerFightMsg(final long roleId, final long recordId, final short round) {
        Query query = new Query();
        query.addCriteria((CriteriaDefinition)Criteria.where("recordId").is((Object)recordId));
        query.addCriteria((CriteriaDefinition)Criteria.where("round").is((Object)round));
        query.with(new Sort(Sort.Direction.ASC, new String[] { "createTime" }));
        final List<FightRecord> records = this.mongoTemplate.find(query, (Class)FightRecord.class, "fightRecord");
        if (records != null && records.size() > 0) {
            for (final FightRecord record : records) {
                query = new Query();
                query.addCriteria((CriteriaDefinition)Criteria.where("id").is((Object)record.getId()));
                final FightRecordData fightRecordData = (FightRecordData)this.mongoTemplate.findOne(query, (Class)FightRecordData.class, "fightRecordData");
                if (fightRecordData != null) {
                    final Message msg = this.decode(fightRecordData.getMsgData());
                    if (msg == null) {
                        continue;
                    }
                    FightMessageUtil.sendFightPackage(roleId, msg);
                }
            }
        }
    }
    
    public void getPlayerFightRecord(final Role role, final byte type, final long objId) {
        final Query query = new Query();
        query.addCriteria((CriteriaDefinition)Criteria.where("type").is((Object)1));
        if (type == 1) {
            query.addCriteria((CriteriaDefinition)Criteria.where("fightId").is((Object)objId));
        }
        else {
            if (type != 2) {
                return;
            }
            query.addCriteria((CriteriaDefinition)Criteria.where("roleIds").regex(".*?" + objId + ".*"));
        }
        final List<FightRecord> list = this.mongoTemplate.find(query, (Class)FightRecord.class, "fightRecord");
        final List<MiniFightRecord> returnList = new ArrayList<MiniFightRecord>();
        for (final FightRecord record : list) {
            final MiniFightRecord miniFightRecord = new MiniFightRecord(record);
            returnList.add(miniFightRecord);
        }
        if (returnList != null && returnList.size() > 0) {
            final RespFightRecords respFightRecords = new RespFightRecords(String.valueOf(objId), returnList);
            FightMessageUtil.sendFightPackage(role.getId(), respFightRecords);
        }
    }
    
    public FightSkillRecord testGetFightSkillRecord(final long recordId) {
        final FightSkillRecord fightSkillRecord = (FightSkillRecord)this.dao.fetch(FightSkillRecord.class, Cnd.where("id", "=", (Object)recordId));
        return fightSkillRecord;
    }
    
    public List<FightSkillRecord> testGetFightSkillRecords(final long startTime, final long endTime, final long roleId) {
        final Sql sql = Sqls.create("select * from (select * from fight_skill_record where time < " + endTime + " and time > " + startTime + ") t");
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(this.dao.getEntity(FightSkillRecord.class));
        this.dao.execute(sql);
        final List<FightSkillRecord> list = sql.getList(FightSkillRecord.class);
        final List<FightSkillRecord> returnList = new ArrayList<FightSkillRecord>();
        if (list != null && list.size() > 0) {
            for (final FightSkillRecord fightSkillRecord : list) {
                fightSkillRecord.doAfterInit();
                if (fightSkillRecord.getDataM().contains(roleId + "")) {
                    returnList.add(fightSkillRecord);
                }
            }
        }
        return returnList;
    }
    
    public List<FightSkillRecord> testGetFightSkillRecordsNew(final long startTime, final long endTime, final long roleId) {
        final Sql sql = Sqls.create("select * from fight_skill_record where time < " + endTime + " and time > " + startTime + " and role_ids like '%" + roleId + "%'");
        sql.setCallback(Sqls.callback.entities());
        sql.setEntity(this.dao.getEntity(FightSkillRecord.class));
        this.dao.execute(sql);
        final List<FightSkillRecord> list = sql.getList(FightSkillRecord.class);
        for (final FightSkillRecord fightSkillRecord : list) {
            fightSkillRecord.doAfterInit();
        }
        return list;
    }
    
    public void clearFightRecordScheduled() {
        final long time = System.currentTimeMillis() - 172800000L;
        final Query query = new Query(Criteria.where("createTime").lt(time));
        this.mongoTemplate.remove(query, FightRecord.class);
        this.mongoTemplate.remove(query, FightRecordData.class);
        this.mongoTemplate.remove(query, FightSkillRecord.class);
    }
}
