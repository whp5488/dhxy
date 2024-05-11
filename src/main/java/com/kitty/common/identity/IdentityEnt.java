
package com.kitty.common.identity;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Index;
import org.nutz.dao.entity.annotation.TableIndexes;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_identity")
@TableIndexes({ @Index(fields = { "type" }, unique = true) })
public class IdentityEnt extends BaseEntity<Integer>
{
    @Id(auto = false)
    private Integer type;
    @Column
    private int currId;
    
    public static IdentityEnt valueOf(final int type, final int seed) {
        final IdentityEnt ent = new IdentityEnt();
        ent.type = type;
        ent.currId = seed;
        return ent;
    }
    
    public int getType() {
        return this.type;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public int getCurrId() {
        return this.currId;
    }
    
    public void setCurrId(final int currId) {
        this.currId = currId;
    }
    
    public synchronized int getNextId() {
        return this.currId++;
    }
    
    @Override
    public Integer getId() {
        return this.type;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
}
