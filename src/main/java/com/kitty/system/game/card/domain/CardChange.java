package com.kitty.system.game.card.domain;

import com.kitty.common.db.DbCardChangeService;
import org.nutz.lang.util.NutMap;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("card_change")
public class CardChange extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private long id;
    @Comment("玩家唯一ID")
    @Column("r_id")
    private long rId;
    @Comment("变身卡编号")
    @Column("serial")
    private int serial;
    @Comment("其他属性")
    @Column("rests")
    private NutMap rests;
    
    public CardChange() {
        this.rests = new NutMap();
    }
    
    @Override
    public Long getId() {
        return Long.valueOf(this.id);
    }
    
    public void setId(long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(long rId) {
        this.rId = rId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(int serial) {
        this.serial = serial;
    }
    
    public NutMap getRests() {
        return this.rests;
    }
    
    public void setRests(NutMap rests) {
        this.rests = rests;
    }
    
    public void save() {
        DbCardChangeService.getInstance().add2Queue(this);
    }
}
