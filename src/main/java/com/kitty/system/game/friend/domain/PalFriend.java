package com.kitty.system.game.friend.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("pal_friend")
public class PalFriend extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("pal_id")
    private long palId;
    @Column("group_id")
    private long groupId;
    @Column("remark")
    private String remark;
    @Column("close")
    private long close;
    
    public PalFriend() {
        this.groupId = 0L;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(long rId) {
        this.rId = rId;
    }
    
    public long getPalId() {
        return this.palId;
    }
    
    public void setPalId(long palId) {
        this.palId = palId;
    }
    
    public long getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(String remark) {
        this.remark = remark;
    }
    
    public long getClose() {
        return this.close;
    }
    
    public void setClose(long close) {
        this.close = close;
    }
}
