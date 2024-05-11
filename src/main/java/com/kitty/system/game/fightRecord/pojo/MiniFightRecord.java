
package com.kitty.system.game.fightRecord.pojo;

import com.kitty.record.domain.FightRecord;
import java.io.Serializable;

public class MiniFightRecord implements Serializable
{
    private String recordId;
    private String createTime;
    
    public MiniFightRecord(final FightRecord record) {
        this.recordId = String.valueOf(record.getId());
        this.createTime = String.valueOf(record.getCreateTime());
    }
    
    public String getRecordId() {
        return this.recordId;
    }
    
    public String getCreateTime() {
        return this.createTime;
    }
    
    public void setRecordId(final String recordId) {
        this.recordId = recordId;
    }
    
    public void setCreateTime(final String createTime) {
        this.createTime = createTime;
    }
}
