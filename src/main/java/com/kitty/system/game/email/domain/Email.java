package com.kitty.system.game.email.domain;

import com.kitty.common.db.Db4EmailService;
import java.util.Date;
import org.springframework.data.annotation.Transient;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("email")
public class Email extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Comment("标题")
    @Column("email_title")
    private String emailTitle;
    @Comment("发送者")
    @Column("sender")
    private long sender;
    @Comment("系统邮件id")
    @Column("email_id")
    private Long emailId;
    @Comment("接收者")
    @Column("recipient")
    private long recipient;
    @Comment("邮件内容")
    @Column("content")
    private String content;
    @Comment("是否有附件")
    @Column("accessory")
    private byte accessory;
    @Comment("是否已读")
    @Column("is_read")
    private byte read;
    @Comment("是否删除")
    @Column("is_delete")
    private boolean isDelete;
    @Comment("过期时间")
    @Column("expiration_time")
    private long expirationTime;
    @Comment("创建时间")
    @Column("create_time")
    private long createTime;
    @Transient
    private List<EmailAccessory> accessorys;
    
    public Email(EmailSystem emailSystem, long id, long roleId) {
        this.id = Long.valueOf(id);
        this.sender = 0L;
        this.emailTitle = emailSystem.getEmailTitle();
        this.recipient = roleId;
        this.read = 0;
        this.isDelete = false;
        this.createTime = new Date().getTime();
        if (emailSystem.getAccessory() != null && emailSystem.getAccessory().size() > 0) {
            this.accessory = 1;
        }
        else {
            this.accessory = 0;
        }
        this.content = emailSystem.getContent();
        this.emailId = emailSystem.getId();
        this.expirationTime = emailSystem.getExpirationTime();
    }
    
    public void save() {
        Db4EmailService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public String getEmailTitle() {
        return this.emailTitle;
    }
    
    public long getSender() {
        return this.sender;
    }
    
    public Long getEmailId() {
        return this.emailId;
    }
    
    public long getRecipient() {
        return this.recipient;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public byte getAccessory() {
        return this.accessory;
    }
    
    public byte getRead() {
        return this.read;
    }
    
    @Override
    public boolean isDelete() {
        return this.isDelete;
    }
    
    public long getExpirationTime() {
        return this.expirationTime;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public List<EmailAccessory> getAccessorys() {
        return this.accessorys;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setEmailTitle(final String emailTitle) {
        this.emailTitle = emailTitle;
    }
    
    public void setSender(final long sender) {
        this.sender = sender;
    }
    
    public void setEmailId(final Long emailId) {
        this.emailId = emailId;
    }
    
    public void setRecipient(final long recipient) {
        this.recipient = recipient;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    public void setAccessory(final byte accessory) {
        this.accessory = accessory;
    }
    
    public void setRead(final byte read) {
        this.read = read;
    }
    
    @Override
    public void setDelete(final boolean isDelete) {
        this.isDelete = isDelete;
    }
    
    public void setExpirationTime(final long expirationTime) {
        this.expirationTime = expirationTime;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setAccessorys(final List<EmailAccessory> accessorys) {
        this.accessorys = accessorys;
    }
    
    public Email() {
    }
}
