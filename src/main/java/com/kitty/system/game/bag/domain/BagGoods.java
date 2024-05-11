
package com.kitty.system.game.bag.domain;

import com.kitty.common.db.DbBagGoodsService;
import java.util.Iterator;
import org.nutz.lang.util.NutMap;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("bag_goods")
public class BagGoods extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private long id;
    @Comment("玩家唯一ID")
    @Column("r_id")
    private long rId;
    @Comment("物品编号")
    @Column("serial")
    private int serial;
    @Comment("背包位置")
    @Column("pos")
    private int pos;
    @Comment("其他属性")
    @Column("rests")
    private NutMap rests;
    @Comment("特殊属性")
    @Column("special")
    private NutMap special;
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(final long rId) {
        this.rId = rId;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public NutMap getRests() {
        return this.rests;
    }
    
    public void setRests(final NutMap rests) {
        this.rests = rests;
    }
    
    public NutMap getSpecial() {
        return this.special;
    }
    
    public void setSpecial(final NutMap special) {
        this.special = special;
    }
    
    public int getNum() {
        int num = 0;
        final NutMap rests = this.getRests();
        for (final String key : rests.keySet()) {
            num += rests.getInt(key, 0);
        }
        return num;
    }
    
    public BagGoods() {
        this.rests = new NutMap();
        this.special = new NutMap();
    }
    
    public BagGoods(final long id, final long rId, final int serial, final int pos) {
        this.rests = new NutMap();
        this.special = new NutMap();
        this.id = id;
        this.rId = rId;
        this.serial = serial;
        this.pos = pos;
    }
    
    public BagGoods(final long id, final long rId, final int serial, final int pos, final NutMap rests) {
        this.rests = new NutMap();
        this.special = new NutMap();
        this.id = id;
        this.rId = rId;
        this.serial = serial;
        this.pos = pos;
        this.rests = rests;
    }
    
    public void save() {
        DbBagGoodsService.getInstance().add2Queue(this);
    }
}
