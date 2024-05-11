
package com.kitty.system.game.friend.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("pal_notify")
public class PalNotify extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("type")
    private byte type;
    @Column("sender")
    private long sender;
    @Column("att")
    private long att;
    @Column("status")
    private byte status;
    @Column("times_tamp")
    private int timesTamp;
    @Column("group_id")
    private long groupId;
    @Column("remark")
    private String remark;
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public long getSender() {
        return this.sender;
    }
    
    public void setSender(final long sender) {
        this.sender = sender;
    }
    
    public long getAtt() {
        return this.att;
    }
    
    public void setAtt(final long att) {
        this.att = att;
    }
    
    public byte getStatus() {
        return this.status;
    }
    
    public void setStatus(final byte status) {
        this.status = status;
    }
    
    public int getTimesTamp() {
        return this.timesTamp;
    }
    
    public void setTimesTamp(final int timesTamp) {
        this.timesTamp = timesTamp;
    }
    
    public long getGroupId() {
        return this.groupId;
    }
    
    public void setGroupId(final long groupId) {
        this.groupId = groupId;
    }
    
    public String getRemark() {
        return this.remark;
    }
    
    public void setRemark(final String remark) {
        this.remark = remark;
    }
    
    public PalNotify() {
    }
    
    public PalNotify(final Long id, final byte type, final long sender, final long att, final byte status, final int timesTamp, final long groupId, final String remark) {
        this.id = id;
        this.type = type;
        this.sender = sender;
        this.att = att;
        this.status = status;
        this.timesTamp = timesTamp;
        this.groupId = groupId;
        this.remark = remark;
    }
}
