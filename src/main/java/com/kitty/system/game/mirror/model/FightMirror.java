
package com.kitty.system.game.mirror.model;

import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("t_fightmirror")
public class FightMirror extends BaseEntity
{
    @Id(auto = false)
    private Long id;
    @Column
    private long createTime;
    @Column
    @ColDefine(type = ColType.TEXT)
    private RoleMirror roleMirror;
    @Column
    @ColDefine(type = ColType.TEXT)
    private PetMirror petMirror;
    
    public FightMirror() {
        this.createTime = System.currentTimeMillis();
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setRoleMirror(final RoleMirror roleMirror) {
        this.roleMirror = roleMirror;
    }
    
    public void setPetMirror(final PetMirror petMirror) {
        this.petMirror = petMirror;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public RoleMirror getRoleMirror() {
        return this.roleMirror;
    }
    
    public PetMirror getPetMirror() {
        return this.petMirror;
    }
}
