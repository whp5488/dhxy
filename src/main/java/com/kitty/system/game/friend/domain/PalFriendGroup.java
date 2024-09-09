package com.kitty.system.game.friend.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("pal_friend_group")
public class PalFriendGroup extends BaseEntity<Long>
{
    @Id(auto = false)
    private Long id;
    @Column("r_id")
    private long rId;
    @Column("group_name")
    private String groupName;
    @Column("sort")
    private short sort;
    
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
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
    
    public short getSort() {
        return this.sort;
    }
    
    public void setSort(short sort) {
        this.sort = sort;
    }
    
    public PalFriendGroup() {
    }
    
    public PalFriendGroup(Long id, long rId, String groupName, short sort) {
        this.id = id;
        this.rId = rId;
        this.groupName = groupName;
        this.sort = sort;
    }
}
