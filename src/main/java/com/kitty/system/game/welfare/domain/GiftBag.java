package com.kitty.system.game.welfare.domain;

import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.ColDefine;
import com.kitty.system.init.domain.vo.Param;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("t_gift_bag")
public class GiftBag
{
    @Column("name")
    private String name;
    @Column("reward")
    @ColDefine(type = ColType.TEXT)
    private List<Param> rewards;
    @Column("time")
    private long time;
    
    public GiftBag() {
    }
    
    public String getName() {
        return this.name;
    }
    
    public List<Param> getRewards() {
        return this.rewards;
    }
    
    public long getTime() {
        return this.time;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRewards(final List<Param> rewards) {
        this.rewards = rewards;
    }
    
    public void setTime(final long time) {
        this.time = time;
    }

}
