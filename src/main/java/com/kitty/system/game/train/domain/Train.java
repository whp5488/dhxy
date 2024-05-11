
package com.kitty.system.game.train.domain;

import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("train")
public class Train extends BaseEntity<Long>
{
    @Override
    public Long getId() {
        return null;
    }
    
    @Override
    public String toString() {
        return "Train()";
    }
}
