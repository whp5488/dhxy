
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
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(final long rId) {
        this.rId = rId;
    }
    
    public String getGroupName() {
        return this.groupName;
    }
    
    public void setGroupName(final String groupName) {
        this.groupName = groupName;
    }
    
    public short getSort() {
        return this.sort;
    }
    
    public void setSort(final short sort) {
        this.sort = sort;
    }
    
    public PalFriendGroup() {
    }
    
    public PalFriendGroup(final Long id, final long rId, final String groupName, final short sort) {
        this.id = id;
        this.rId = rId;
        this.groupName = groupName;
        this.sort = sort;
    }
}
