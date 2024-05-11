
package com.kitty.system.game.tianfu.domain;

import java.beans.ConstructorProperties;
import com.kitty.common.db.DbTianFuService;
import java.util.Date;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("role_tian_fu")
public class RoleTianFu extends BaseEntity<Long>
{
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
    
    public void save() {
        DbTianFuService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public int getTianFuSerial() {
        return this.tianFuSerial;
    }
    
    public long getRoleId() {
        return this.roleId;
    }
    
    public byte getCategory() {
        return this.category;
    }
    
    public byte getLayer() {
        return this.layer;
    }
    
    public byte getLevel() {
        return this.level;
    }
    
    public byte getProductOrder() {
        return this.productOrder;
    }
    
    public int getIsWear() {
        return this.isWear;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public int getToIdentifyCeSerial() {
        return this.toIdentifyCeSerial;
    }
    
    public int getBd() {
        return this.bd;
    }
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public int getStorePos() {
        return this.storePos;
    }
    
    public String getTriggerType() {
        return this.triggerType;
    }
    
    public String getDayInCeGrid() {
        return this.DayInCeGrid;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setTianFuSerial(final int tianFuSerial) {
        this.tianFuSerial = tianFuSerial;
    }
    
    public void setRoleId(final long roleId) {
        this.roleId = roleId;
    }
    
    public void setCategory(final byte category) {
        this.category = category;
    }
    
    public void setLayer(final byte layer) {
        this.layer = layer;
    }
    
    public void setLevel(final byte level) {
        this.level = level;
    }
    
    public void setProductOrder(final byte productOrder) {
        this.productOrder = productOrder;
    }
    
    public void setIsWear(final int isWear) {
        this.isWear = isWear;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    public void setToIdentifyCeSerial(final int toIdentifyCeSerial) {
        this.toIdentifyCeSerial = toIdentifyCeSerial;
    }
    
    public void setBd(final int bd) {
        this.bd = bd;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    public void setStorePos(final int storePos) {
        this.storePos = storePos;
    }
    
    public void setTriggerType(final String triggerType) {
        this.triggerType = triggerType;
    }
    
    public void setDayInCeGrid(final String DayInCeGrid) {
        this.DayInCeGrid = DayInCeGrid;
    }
    
    @ConstructorProperties({ "id", "tianFuSerial", "roleId", "category", "layer", "level", "productOrder", "isWear", "count", "toIdentifyCeSerial", "bd", "createTime", "storePos", "triggerType", "DayInCeGrid" })
    public RoleTianFu(final Long id, final int tianFuSerial, final long roleId, final byte category, final byte layer, final byte level, final byte productOrder, final int isWear, final short count, final int toIdentifyCeSerial, final int bd, final Date createTime, final int storePos, final String triggerType, final String DayInCeGrid) {
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
    
    public RoleTianFu() {
    }
}
