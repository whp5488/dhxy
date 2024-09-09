package com.kitty.system.game.fightRecord.controller;

import com.kitty.system.game.role.domain.Role;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.fightRecord.message.ReqFightRecords;
import org.apache.mina.core.session.IoSession;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.Iterator;
import java.util.List;
import java.util.Date;
import com.kitty.system.game.fightRecord.domain.FightSkillRecord;
import java.text.SimpleDateFormat;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.game.fightRecord.service.FightRecordService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

@Controller
@RequestMapping({ "/fightRecord" })
public class FightRecordController
{
    @Autowired
    FightRecordService fightRecordService;
    
    public FightRecordController() {
    }
    
    @GetMapping({ "/get" })
    public void get(long startTime, long endTime, long roleId) {
        List<FightSkillRecord> fightSkillRecords = this.fightRecordService.testGetFightSkillRecordsNew(startTime, endTime, roleId);
        if (fightSkillRecords != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (FightSkillRecord fightSkillRecord : fightSkillRecords) {
                System.out.println(fightSkillRecord.getFightId());
                String format = sdf.format(new Date(fightSkillRecord.getCreateTime()));
                System.out.println(format);
                System.out.println(fightSkillRecord.getDataM());
            }
        }
    }
    
    @com.kitty.mina.annotation.RequestMapping
    public void returnRoundEnd(IoSession session, ReqFightRecords reqFightRecords) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        try {
            byte type = reqFightRecords.getType();
            long objId = Long.parseLong(reqFightRecords.getObjId());
            this.fightRecordService.getPlayerFightRecord(role, type, objId);
        }
        catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return;
    }
}
