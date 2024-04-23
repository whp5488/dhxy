package com.kitty.system.game.tianfu.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbTianFuService;
import java.beans.ConstructorProperties;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("role_day_in_ce")
public class RoleDayInCe extends BaseEntity<Long> {
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

  @ConstructorProperties({"id", "roleId", "level", "gattaca11", "gattaca12", "gattaca21", "gattaca22", "gattaca31", "gattaca32", "gattaca41", "gattaca42", "gattaca51", "gattaca", "loadObj11", "loadObj12", "loadObj21", "loadObj22", "loadObj31", "loadObj32", "loadObj41", "loadObj42", "loadObj51", "loadObj", "driveBeasts11", "driveBeasts12", "driveBeasts21", "driveBeasts22", "driveBeasts31", "driveBeasts32", "driveBeasts41", "driveBeasts42", "driveBeasts51", "driveBeasts"})
  public RoleDayInCe(Long id, long roleId, short level, long gattaca11, long gattaca12, long gattaca21, long gattaca22, long gattaca31, long gattaca32, long gattaca41, long gattaca42, long gattaca51, long gattaca, long loadObj11, long loadObj12, long loadObj21, long loadObj22, long loadObj31, long loadObj32, long loadObj41, long loadObj42, long loadObj51, long loadObj, long driveBeasts11, long driveBeasts12, long driveBeasts21, long driveBeasts22, long driveBeasts31, long driveBeasts32, long driveBeasts41, long driveBeasts42, long driveBeasts51, long driveBeasts) {
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

  @Comment("千钧符通用格子")
  @Column("gattaca")
  private long gattaca = 1L;
  
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
  private long loadObj = 1L;
  
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
  private long driveBeasts = 1L;

  public void save() {
    DbTianFuService.getInstance().add2Queue(this);
  }
  
  public RoleDayInCe() {}
}
