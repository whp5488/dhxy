
package com.kitty.system.game.activity.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import java.util.List;

public class Dugeon
{
    private long id;
    private long createTime;
    private long endTime;
    private List<Long> memberList;
    
    public Dugeon() {
    }
    
    public Dugeon(final long id, final List<Long> memberList) {
        this.id = id;
        this.createTime = System.currentTimeMillis();
        this.endTime = this.createTime + 7200000L;
        this.memberList = memberList;
    }
    
    @JsonIgnore
    public boolean isCreator(final long uid) {
        return this.memberList.get(0) == uid;
    }
    
    @JsonIgnore
    public boolean isOverTime() {
        return System.currentTimeMillis() >= this.endTime;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setEndTime(final long endTime) {
        this.endTime = endTime;
    }
    
    public void setMemberList(final List<Long> memberList) {
        this.memberList = memberList;
    }
    
    public long getId() {
        return this.id;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public long getEndTime() {
        return this.endTime;
    }
    
    public List<Long> getMemberList() {
        return this.memberList;
    }
}
