package com.kitty.system.game.role.domain;

import com.kitty.common.db.Db4CommonService;
import java.util.HashMap;
import java.util.Map;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("setting")
public class Setting extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private long id;
    @Comment("玩家唯一ID")
    @Column("r_id")
    private long rId;
    @Comment("指令")
    @Column("instruct")
    private Map<Short, Map<Byte, String>> special;
    @Comment("插件到期时间")
    @Column("plug_end_time")
    private int plugEndTime;
    
    @Override
    public Long getId() {
        return Long.valueOf(this.id);
    }
    
    public Setting() {
        this.special = new HashMap();
        this.plugEndTime = 0;
    }
    
    public Setting(long id, long rId) {
        this.special = new HashMap();
        this.plugEndTime = 0;
        this.id = id;
        this.rId = rId;
    }
    
    public void save() {
        Db4CommonService.getInstance().add2Queue(this);
    }
    
    public long getRId() {
        return this.rId;
    }
    
    public Map<Short, Map<Byte, String>> getSpecial() {
        return this.special;
    }
    
    public int getPlugEndTime() {
        return this.plugEndTime;
    }
    
    public void setId(final long id) {
        this.id = id;
    }
    
    public void setRId(final long rId) {
        this.rId = rId;
    }
    
    public void setSpecial(final Map<Short, Map<Byte, String>> special) {
        this.special = special;
    }
    
    public void setPlugEndTime(final int plugEndTime) {
        this.plugEndTime = plugEndTime;
    }
}
