package com.kitty.system.game.account.domain;

import java.util.Date;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Name;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_account")
public class Account extends BaseEntity<String>
{
    @Name
    private String id;
    @Column
    private String userName;
    @Column("mobile")
    private String mobile;
    @Column("channel_code")
    private String channelCode;
    @Column("create_time")
    private Date createTime;
    @Column("is_stop")
    private int isStop;
    
    public Account() {
        this.createTime = new Date();
    }
    
    public void setId(final String id) {
        this.id = id;
    }
    
    public void setUserName(final String userName) {
        this.userName = userName;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public void setChannelCode(final String channelCode) {
        this.channelCode = channelCode;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    public void setIsStop(final int isStop) {
        this.isStop = isStop;
    }
    
    @Override
    public String getId() {
        return this.id;
    }
    
    public String getUserName() {
        return this.userName;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public String getChannelCode() {
        return this.channelCode;
    }
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public int getIsStop() {
        return this.isStop;
    }
}
