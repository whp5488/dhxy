package com.kitty.system.game.email.domain;

import com.kitty.common.db.Db4EmailService;
import java.util.Date;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("email_accessory")
public class EmailAccessory extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Comment("邮件id")
    @Column("email_id")
    private Long emailId;
    @Comment("类型")
    @Column("type")
    private byte type;
    @Comment("编号")
    @Column("serial")
    private int serial;
    @Comment("数量")
    @Column("num")
    private long num;
    @Comment("是否领取")
    @Column("receive")
    private byte receive;
    @Comment("创建时间")
    @Column("create_time")
    private long createTime;
    
    public EmailAccessory(EmailSystemAccessory emailSystemAccessory, long id, long emailId) {
        this.id = Long.valueOf(id);
        this.emailId = Long.valueOf(emailId);
        this.type = emailSystemAccessory.getType();
        this.serial = emailSystemAccessory.getSerial();
        this.num = Long.parseLong(emailSystemAccessory.getNum());
        this.createTime = new Date().getTime();
    }
    
    public void save() {
        Db4EmailService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public Long getEmailId() {
        return this.emailId;
    }
    
    public byte getType() {
        return this.type;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public long getNum() {
        return this.num;
    }
    
    public byte getReceive() {
        return this.receive;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setEmailId(final Long emailId) {
        this.emailId = emailId;
    }
    
    public void setType(final byte type) {
        this.type = type;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setNum(final long num) {
        this.num = num;
    }
    
    public void setReceive(final byte receive) {
        this.receive = receive;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public EmailAccessory() {
    }
}
