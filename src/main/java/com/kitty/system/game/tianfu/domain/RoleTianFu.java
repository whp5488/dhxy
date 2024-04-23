package com.kitty.system.game.tianfu.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbTianFuService;
import java.beans.ConstructorProperties;
import java.util.Date;

import lombok.Getter;
import lombok.Setter;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Setter
@Getter
@Table("role_tian_fu")
public class RoleTianFu extends BaseEntity<Long> {
  @Id(auto = false)
  @Comment("主键")
  @Column("id")
  private Long id;
  
  @Comment("天策符编号")
  @Column("tian_fu_serial")
  private int tianFuSerial;
  
  @Comment("所属角色编号")
  @Column("role_id")
  private long roleId;
  
  @Comment("所属目录(1=千钧,2=载物,3=御兽)")
  @Column("category")
  private byte category;
  
  @Comment("所在层")
  @Column("layer")
  private byte layer;
  
  @Comment("等级")
  @Column("level")
  private byte level;
  
  @Comment("品阶")
  @Column("product_order")
  private byte productOrder;
  
  @Comment("是否配戴（0=未佩戴，1=佩戴）")
  @Column("is_wear")
  private int isWear;
  
  @Comment("天策符碎片的数量（只有碎片才会用到这个字段）")
  @Column("count")
  private short count;
  
  @Comment("待鉴定天策符编号(鉴定完毕=1，待鉴定=待鉴定编号)")
  @Column("to_identify_ce_serial")
  private int toIdentifyCeSerial;
  
  @Comment("是否绑定")
  @Column("bd")
  private int bd;
  
  @Comment("创建时间")
  @Column("create_time")
  private Date createTime;
  
  @Comment("仓库位置(0=天策符背包/佩戴)")
  @Column("store_pos")
  private int storePos;
  
  private String triggerType;
  
  private String DayInCeGrid;

  @ConstructorProperties({"id", "tianFuSerial", "roleId", "category", "layer", "level", "productOrder", "isWear", "count", "toIdentifyCeSerial", "bd", "createTime", "storePos", "triggerType", "DayInCeGrid"})
  public RoleTianFu(Long id, int tianFuSerial, long roleId, byte category, byte layer, byte level, byte productOrder, int isWear, short count, int toIdentifyCeSerial, int bd, Date createTime, int storePos, String triggerType, String DayInCeGrid) {
    this.id = id;
    this.tianFuSerial = tianFuSerial;
    this.roleId = roleId;
    this.category = category;
    this.layer = layer;
    this.level = level;
    this.productOrder = productOrder;
    this.isWear = isWear;
    this.count = count;
    this.toIdentifyCeSerial = toIdentifyCeSerial;
    this.bd = bd;
    this.createTime = createTime;
    this.storePos = storePos;
    this.triggerType = triggerType;
    this.DayInCeGrid = DayInCeGrid;
  }
  
  public RoleTianFu() {}

  public void save() {
    DbTianFuService.getInstance().add2Queue(this);
  }
}
