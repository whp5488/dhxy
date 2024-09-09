package com.kitty.system.game.suit.domain;

import com.kitty.common.db.Db4CommonService;
import java.util.Collections;
import java.util.ArrayList;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("suit")
public class Suit extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private long id;
    @Comment("玩家唯一ID")
    @Column("r_id")
    private long rId;
    @Comment("套装方案名称")
    @Column("name")
    private String name;
    @Comment("装备集合")
    @Column("equip")
    @Default("[]")
    private List<Long> equip;
    @Comment("状态：0.未启用 1.启用")
    @Column("state")
    private byte state;
    @Comment("开启时间")
    @Column("time")
    private int time;
    
    public Suit() {
        this.equip = Collections.synchronizedList(new ArrayList<>());
    }
    
    public Suit(long id, long rId, String name, byte state) {
        this.equip = Collections.synchronizedList(new ArrayList<>());
        this.id = id;
        this.rId = rId;
        this.time = (int)(System.currentTimeMillis() / 1000L);
        this.name = name;
        this.state = state;
    }
    
    @Override
    public Long getId() {
        return Long.valueOf(this.id);
    }
    
    public void save() {
        Db4CommonService.getInstance().add2Queue(this);
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Long> getEquip() {
        return this.equip;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setEquip(final List<Long> equip) {
        this.equip = equip;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
}
