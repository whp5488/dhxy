package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_common_set")
public class CommonSet
{
    @Column("s_key")
    private String key;
    @Column("s_value")
    @ColDefine(type = ColType.TEXT)
    private String value;
    
    public CommonSet() {
    }
    
    public String getKey() {
        return this.key;
    }
    
    public String getValue() {
        return this.value;
    }
}
