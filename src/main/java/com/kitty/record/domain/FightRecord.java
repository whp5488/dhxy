
package com.kitty.record.domain;

import com.kitty.record.FightRecordThread;
import java.util.List;
import java.io.Serializable;

public class FightRecord implements Serializable
{
    private Long id;
    private Long recordId;
    private byte type;
    private int fightId;
    private short round;
    private int module;
    private long createTime;
    private List<Long> roleIds;
    private String marking;
    private String sendRoles;
    
    public void save() {
        FightRecordThread.getInstance().add2Queue(this);
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
    
    public String getMarking() {
        return this.marking;
    }
    
    public String getSendRoles() {
        return this.sendRoles;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRecordId(final Long recordId) {
        this.recordId = recordId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setFightId(final int fightId) {
        this.fightId = fightId;
    }
    
    public void setRound(final short round) {
        this.round = round;
    }
    
    public void setModule(final int module) {
        this.module = module;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setRoleIds(final List<Long> roleIds) {
        this.roleIds = roleIds;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    public void setSendRoles(final String sendRoles) {
        this.sendRoles = sendRoles;
    }
}
