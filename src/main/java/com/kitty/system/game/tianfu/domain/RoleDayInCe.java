package com.kitty.system.game.tianfu.domain;

import com.kitty.common.db.DbTianFuService;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("role_day_in_ce")
public class RoleDayInCe extends BaseEntity<Long>
{
    @Id(auto = false)
    @Comment("主键")
    @Column("id")
    private Long id;
    @Comment("所属角色编号")
    @Column("role_id")
    private long roleId;
    @Comment("等级")
    @Column("level")
    private short level;
    @Comment("千钧符第一层第一个格子")
    @Column("gattaca_1_1")
    private long gattaca11;
    @Comment("千钧符第一层第一个格子")
    @Column("gattaca_1_2")
    private long gattaca12;
    @Comment("千钧符第二层第一个格子")
    @Column("gattaca_2_1")
    private long gattaca21;
    @Comment("千钧符第二层第二个格子")
    @Column("gattaca_2_2")
    private long gattaca22;
    @Comment("千钧符第三层第一个格子")
    @Column("gattaca_3_1")
    private long gattaca31;
    @Comment("千钧符第三层第二个格子")
    @Column("gattaca_3_2")
    private long gattaca32;
    @Comment("千钧符第四层第一个格子")
    @Column("gattaca_4_1")
    private long gattaca41;
    @Comment("千钧符第四层第二个格子")
    @Column("gattaca_4_2")
    private long gattaca42;
    @Comment("千钧符第五层第一个格子")
    @Column("gattaca_5_1")
    private long gattaca51;
    @Comment("千钧符通用格子")
    @Column("gattaca")
    private long gattaca;
    @Comment("载物符第一层第一个格子")
    @Column("load_obj_1_1")
    private long loadObj11;
    @Comment("载物符第一层第二个格子")
    @Column("load_obj_1_2")
    private long loadObj12;
    @Comment("载物符第二层第一个格子")
    @Column("load_obj_2_1")
    private long loadObj21;
    @Comment("载物符第二层第二个格子")
    @Column("load_obj_2_2")
    private long loadObj22;
    @Comment("载物符第三层第一个格子")
    @Column("load_obj_3_1")
    private long loadObj31;
    @Comment("载物符第三层第二个格子")
    @Column("load_obj_3_2")
    private long loadObj32;
    @Comment("载物符第四层第一个格子")
    @Column("load_obj_4_1")
    private long loadObj41;
    @Comment("载物符第四层第二个格子")
    @Column("load_obj_4_2")
    private long loadObj42;
    @Comment("载物符第五层第一个格子")
    @Column("load_obj_5_1")
    private long loadObj51;
    @Comment("载物符通用格子")
    @Column("load_obj")
    private long loadObj;
    @Comment("御兽符第一层第一个格子")
    @Column("drive_beasts_1_1")
    private long driveBeasts11;
    @Comment("御兽符第一层第二个格子")
    @Column("drive_beasts_1_2")
    private long driveBeasts12;
    @Comment("御兽符第二层第一个格子")
    @Column("drive_beasts_2_1")
    private long driveBeasts21;
    @Comment("御兽符第二层第二个格子")
    @Column("drive_beasts_2_2")
    private long driveBeasts22;
    @Comment("御兽符第三层第一个格子")
    @Column("drive_beasts_3_1")
    private long driveBeasts31;
    @Comment("御兽符第三层第二个格子")
    @Column("drive_beasts_3_2")
    private long driveBeasts32;
    @Comment("御兽符第四层第一个格子")
    @Column("drive_beasts_4_1")
    private long driveBeasts41;
    @Comment("御兽符第四层第二个格子")
    @Column("drive_beasts_4_2")
    private long driveBeasts42;
    @Comment("御兽符第五层第一个格子")
    @Column("drive_beasts_5_1")
    private long driveBeasts51;
    @Comment("御兽符通用格子")
    @Column("drive_beasts")
    private long driveBeasts;
    
    public void save() {
        DbTianFuService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public long getRoleId() {
        return this.roleId;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public long getGattaca11() {
        return this.gattaca11;
    }
    
    public long getGattaca12() {
        return this.gattaca12;
    }
    
    public long getGattaca21() {
        return this.gattaca21;
    }
    
    public long getGattaca22() {
        return this.gattaca22;
    }
    
    public long getGattaca31() {
        return this.gattaca31;
    }
    
    public long getGattaca32() {
        return this.gattaca32;
    }
    
    public long getGattaca41() {
        return this.gattaca41;
    }
    
    public long getGattaca42() {
        return this.gattaca42;
    }
    
    public long getGattaca51() {
        return this.gattaca51;
    }
    
    public long getGattaca() {
        return this.gattaca;
    }
    
    public long getLoadObj11() {
        return this.loadObj11;
    }
    
    public long getLoadObj12() {
        return this.loadObj12;
    }
    
    public long getLoadObj21() {
        return this.loadObj21;
    }
    
    public long getLoadObj22() {
        return this.loadObj22;
    }
    
    public long getLoadObj31() {
        return this.loadObj31;
    }
    
    public long getLoadObj32() {
        return this.loadObj32;
    }
    
    public long getLoadObj41() {
        return this.loadObj41;
    }
    
    public long getLoadObj42() {
        return this.loadObj42;
    }
    
    public long getLoadObj51() {
        return this.loadObj51;
    }
    
    public long getLoadObj() {
        return this.loadObj;
    }
    
    public long getDriveBeasts11() {
        return this.driveBeasts11;
    }
    
    public long getDriveBeasts12() {
        return this.driveBeasts12;
    }
    
    public long getDriveBeasts21() {
        return this.driveBeasts21;
    }
    
    public long getDriveBeasts22() {
        return this.driveBeasts22;
    }
    
    public long getDriveBeasts31() {
        return this.driveBeasts31;
    }
    
    public long getDriveBeasts32() {
        return this.driveBeasts32;
    }
    
    public long getDriveBeasts41() {
        return this.driveBeasts41;
    }
    
    public long getDriveBeasts42() {
        return this.driveBeasts42;
    }
    
    public long getDriveBeasts51() {
        return this.driveBeasts51;
    }
    
    public long getDriveBeasts() {
        return this.driveBeasts;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final long roleId) {
        this.roleId = roleId;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setGattaca11(final long gattaca11) {
        this.gattaca11 = gattaca11;
    }
    
    public void setGattaca12(final long gattaca12) {
        this.gattaca12 = gattaca12;
    }
    
    public void setGattaca21(final long gattaca21) {
        this.gattaca21 = gattaca21;
    }
    
    public void setGattaca22(final long gattaca22) {
        this.gattaca22 = gattaca22;
    }
    
    public void setGattaca31(final long gattaca31) {
        this.gattaca31 = gattaca31;
    }
    
    public void setGattaca32(final long gattaca32) {
        this.gattaca32 = gattaca32;
    }
    
    public void setGattaca41(final long gattaca41) {
        this.gattaca41 = gattaca41;
    }
    
    public void setGattaca42(final long gattaca42) {
        this.gattaca42 = gattaca42;
    }
    
    public void setGattaca51(final long gattaca51) {
        this.gattaca51 = gattaca51;
    }
    
    public void setGattaca(final long gattaca) {
        this.gattaca = gattaca;
    }
    
    public void setLoadObj11(final long loadObj11) {
        this.loadObj11 = loadObj11;
    }
    
    public void setLoadObj12(final long loadObj12) {
        this.loadObj12 = loadObj12;
    }
    
    public void setLoadObj21(final long loadObj21) {
        this.loadObj21 = loadObj21;
    }
    
    public void setLoadObj22(final long loadObj22) {
        this.loadObj22 = loadObj22;
    }
    
    public void setLoadObj31(final long loadObj31) {
        this.loadObj31 = loadObj31;
    }
    
    public void setLoadObj32(final long loadObj32) {
        this.loadObj32 = loadObj32;
    }
    
    public void setLoadObj41(final long loadObj41) {
        this.loadObj41 = loadObj41;
    }
    
    public void setLoadObj42(final long loadObj42) {
        this.loadObj42 = loadObj42;
    }
    
    public void setLoadObj51(final long loadObj51) {
        this.loadObj51 = loadObj51;
    }
    
    public void setLoadObj(final long loadObj) {
        this.loadObj = loadObj;
    }
    
    public void setDriveBeasts11(final long driveBeasts11) {
        this.driveBeasts11 = driveBeasts11;
    }
    
    public void setDriveBeasts12(final long driveBeasts12) {
        this.driveBeasts12 = driveBeasts12;
    }
    
    public void setDriveBeasts21(final long driveBeasts21) {
        this.driveBeasts21 = driveBeasts21;
    }
    
    public void setDriveBeasts22(final long driveBeasts22) {
        this.driveBeasts22 = driveBeasts22;
    }
    
    public void setDriveBeasts31(final long driveBeasts31) {
        this.driveBeasts31 = driveBeasts31;
    }
    
    public void setDriveBeasts32(final long driveBeasts32) {
        this.driveBeasts32 = driveBeasts32;
    }
    
    public void setDriveBeasts41(final long driveBeasts41) {
        this.driveBeasts41 = driveBeasts41;
    }
    
    public void setDriveBeasts42(final long driveBeasts42) {
        this.driveBeasts42 = driveBeasts42;
    }
    
    public void setDriveBeasts51(final long driveBeasts51) {
        this.driveBeasts51 = driveBeasts51;
    }
    
    public void setDriveBeasts(final long driveBeasts) {
        this.driveBeasts = driveBeasts;
    }
    
    public RoleDayInCe(final Long id, final long roleId, final short level, final long gattaca11, final long gattaca12, final long gattaca21, final long gattaca22, final long gattaca31, final long gattaca32, final long gattaca41, final long gattaca42, final long gattaca51, final long gattaca, final long loadObj11, final long loadObj12, final long loadObj21, final long loadObj22, final long loadObj31, final long loadObj32, final long loadObj41, final long loadObj42, final long loadObj51, final long loadObj, final long driveBeasts11, final long driveBeasts12, final long driveBeasts21, final long driveBeasts22, final long driveBeasts31, final long driveBeasts32, final long driveBeasts41, final long driveBeasts42, final long driveBeasts51, final long driveBeasts) {
        this.gattaca = 1L;
        this.loadObj = 1L;
        this.driveBeasts = 1L;
        this.id = id;
        this.roleId = roleId;
        this.level = level;
        this.gattaca11 = gattaca11;
        this.gattaca12 = gattaca12;
        this.gattaca21 = gattaca21;
        this.gattaca22 = gattaca22;
        this.gattaca31 = gattaca31;
        this.gattaca32 = gattaca32;
        this.gattaca41 = gattaca41;
        this.gattaca42 = gattaca42;
        this.gattaca51 = gattaca51;
        this.gattaca = gattaca;
        this.loadObj11 = loadObj11;
        this.loadObj12 = loadObj12;
        this.loadObj21 = loadObj21;
        this.loadObj22 = loadObj22;
        this.loadObj31 = loadObj31;
        this.loadObj32 = loadObj32;
        this.loadObj41 = loadObj41;
        this.loadObj42 = loadObj42;
        this.loadObj51 = loadObj51;
        this.loadObj = loadObj;
        this.driveBeasts11 = driveBeasts11;
        this.driveBeasts12 = driveBeasts12;
        this.driveBeasts21 = driveBeasts21;
        this.driveBeasts22 = driveBeasts22;
        this.driveBeasts31 = driveBeasts31;
        this.driveBeasts32 = driveBeasts32;
        this.driveBeasts41 = driveBeasts41;
        this.driveBeasts42 = driveBeasts42;
        this.driveBeasts51 = driveBeasts51;
        this.driveBeasts = driveBeasts;
    }
    
    public RoleDayInCe() {
        this.gattaca = 1L;
        this.loadObj = 1L;
        this.driveBeasts = 1L;
    }
}
