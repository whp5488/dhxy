
package com.kitty.system.game.email.domain;

import com.kitty.common.db.Db4EmailService;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("email_system")
public class EmailSystem extends BaseEntity<Long>
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
    private int sender;
    @Comment("接收者")
    @Column("recipient")
    private long recipient;
    @Comment("限制条件")
    @Column("restrictions")
    private String restrictions;
    @Comment("邮件内容")
    @Column("content")
    private String content;
    @Comment("附件内容")
    @Column("accessory")
    @Default("[]")
    private List<EmailSystemAccessory> accessory;
    @Comment("是否删除")
    @Column("is_delete")
    private boolean elete;
    @Comment("过期时间")
    @Column("expiration_time")
    private long expirationTime;
    @Comment("创建时间")
    @Column("create_time")
    private long createTime;
    
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
    
    public int getSender() {
        return this.sender;
    }
    
    public long getRecipient() {
        return this.recipient;
    }
    
    public String getRestrictions() {
        return this.restrictions;
    }
    
    public String getContent() {
        return this.content;
    }
    
    public List<EmailSystemAccessory> getAccessory() {
        return this.accessory;
    }
    
    public boolean isElete() {
        return this.elete;
    }
    
    public long getExpirationTime() {
        return this.expirationTime;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setEmailTitle(final String emailTitle) {
        this.emailTitle = emailTitle;
    }
    
    public void setSender(final int sender) {
        this.sender = sender;
    }
    
    public void setRecipient(final long recipient) {
        this.recipient = recipient;
    }
    
    public void setRestrictions(final String restrictions) {
        this.restrictions = restrictions;
    }
    
    public void setContent(final String content) {
        this.content = content;
    }
    
    public void setAccessory(final List<EmailSystemAccessory> accessory) {
        this.accessory = accessory;
    }
    
    public void setElete(final boolean elete) {
        this.elete = elete;
    }
    
    public void setExpirationTime(final long expirationTime) {
        this.expirationTime = expirationTime;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
}
