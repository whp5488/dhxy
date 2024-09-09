package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("t_groups")
public class Tgroups
{
    @Column("skid")
    private String skid;
    @Column("groups_name")
    private String groupsName;
    
    public Tgroups() {
    }
    
    public void setSkid(final String skid) {
        this.skid = skid;
    }
    
    public void setGroupsName(final String groupsName) {
        this.groupsName = groupsName;
    }
    
    public String getSkid() {
        return this.skid;
    }
    
    public String getGroupsName() {
        return this.groupsName;
    }
}
