package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_map_info")
public class SysMapInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("")
    @Column("id")
    private Integer id;
    @Comment("地图名称")
    @Column("name")
    private String name;
    @Comment("地图编号")
    @Column("map_serial")
    private Integer mapSerial;
    @Comment("等级限制")
    @Column("lv_limit")
    private String lvLimit;
    @Comment("出生点")
    @Column("born_point")
    private String bornPoint;
    @Comment("战斗地图（0否 1是 ）")
    @Column("war_map")
    private Integer warMap;
    @Comment("地图类型（0否 1是 ）")
    @Column("map_type")
    private Integer mapType;
    @Comment("跨服地图（0否 1是 ）")
    @Column("cankuafu")
    private Integer canKuaFu;
    @Comment("是否能直接离开（0否 1是 ）")
    @Column("caneasyleave")
    private int caneasyleave;
    @Comment("地图宽")
    @Column("bgw")
    private int bgw;
    @Comment("地图高")
    @Column("bgh")
    private int bgh;
    @Comment("是否是pk地图（0否 1是 ）")
    @Column("is_pk_map")
    private int isPkMap;
    @Comment("通缉地图（0否 1是 ）")
    @Column("is_wanted")
    private int isWanted;
    @Comment("副本（0否 1是 ）")
    @Column("fuben")
    private Integer fuBen;
    @Comment("等级范围")
    @Column("level_scope")
    private String levelScope;
    @Comment("地图跳转提示")
    @Column("how_to_out")
    private String howToOut;
    
    public SysMapInfo() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getMapSerial() {
        return this.mapSerial;
    }
    
    public String getLvLimit() {
        return this.lvLimit;
    }
    
    public String getBornPoint() {
        return this.bornPoint;
    }
    
    public Integer getWarMap() {
        return this.warMap;
    }
    
    public Integer getMapType() {
        return this.mapType;
    }
    
    public Integer getCanKuaFu() {
        return this.canKuaFu;
    }
    
    public int getCaneasyleave() {
        return this.caneasyleave;
    }
    
    public int getBgw() {
        return this.bgw;
    }
    
    public int getBgh() {
        return this.bgh;
    }
    
    public int getIsPkMap() {
        return this.isPkMap;
    }
    
    public int getIsWanted() {
        return this.isWanted;
    }
    
    public Integer getFuBen() {
        return this.fuBen;
    }
    
    public String getLevelScope() {
        return this.levelScope;
    }
    
    public String getHowToOut() {
        return this.howToOut;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setMapSerial(final Integer mapSerial) {
        this.mapSerial = mapSerial;
    }
    
    public void setLvLimit(final String lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setBornPoint(final String bornPoint) {
        this.bornPoint = bornPoint;
    }
    
    public void setWarMap(final Integer warMap) {
        this.warMap = warMap;
    }
    
    public void setMapType(final Integer mapType) {
        this.mapType = mapType;
    }
    
    public void setCanKuaFu(final Integer canKuaFu) {
        this.canKuaFu = canKuaFu;
    }
    
    public void setCaneasyleave(final int caneasyleave) {
        this.caneasyleave = caneasyleave;
    }
    
    public void setBgw(final int bgw) {
        this.bgw = bgw;
    }
    
    public void setBgh(final int bgh) {
        this.bgh = bgh;
    }
    
    public void setIsPkMap(final int isPkMap) {
        this.isPkMap = isPkMap;
    }
    
    public void setIsWanted(final int isWanted) {
        this.isWanted = isWanted;
    }
    
    public void setFuBen(final Integer fuBen) {
        this.fuBen = fuBen;
    }
    
    public void setLevelScope(final String levelScope) {
        this.levelScope = levelScope;
    }
    
    public void setHowToOut(final String howToOut) {
        this.howToOut = howToOut;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysMapInfo)) {
            return false;
        }
        SysMapInfo other = (SysMapInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$mapSerial = this.getMapSerial();
        Object other$mapSerial = other.getMapSerial();
        if (!((this$mapSerial != null) ? this$mapSerial.equals(other$mapSerial) : (other$mapSerial == null))) {
            return false;
        }
        Object this$lvLimit = this.getLvLimit();
        Object other$lvLimit = other.getLvLimit();
        if (!((this$lvLimit != null) ? this$lvLimit.equals(other$lvLimit) : (other$lvLimit == null))) {
            return false;
        }
        Object this$bornPoint = this.getBornPoint();
        Object other$bornPoint = other.getBornPoint();
        if (!((this$bornPoint != null) ? this$bornPoint.equals(other$bornPoint) : (other$bornPoint == null))) {
            return false;
        }
        Object this$warMap = this.getWarMap();
        Object other$warMap = other.getWarMap();
        if (!((this$warMap != null) ? this$warMap.equals(other$warMap) : (other$warMap == null))) {
            return false;
        }
        Object this$mapType = this.getMapType();
        Object other$mapType = other.getMapType();
        if (!((this$mapType != null) ? this$mapType.equals(other$mapType) : (other$mapType == null))) {
            return false;
        }
        Object this$canKuaFu = this.getCanKuaFu();
        Object other$canKuaFu = other.getCanKuaFu();
        if (!((this$canKuaFu != null) ? this$canKuaFu.equals(other$canKuaFu) : (other$canKuaFu == null))) {
            return false;
        }
        if (this.getCaneasyleave() != other.getCaneasyleave()) {
            return false;
        }
        if (this.getBgw() != other.getBgw()) {
            return false;
        }
        if (this.getBgh() != other.getBgh()) {
            return false;
        }
        if (this.getIsPkMap() != other.getIsPkMap()) {
            return false;
        }
        if (this.getIsWanted() != other.getIsWanted()) {
            return false;
        }
        Object this$fuBen = this.getFuBen();
        Object other$fuBen = other.getFuBen();
        if (!((this$fuBen != null) ? this$fuBen.equals(other$fuBen) : (other$fuBen == null))) {
            return false;
        }
        Object this$levelScope = this.getLevelScope();
        Object other$levelScope = other.getLevelScope();
        if (!((this$levelScope != null) ? this$levelScope.equals(other$levelScope) : (other$levelScope == null))) {
            return false;
        }
        Object this$howToOut = this.getHowToOut();
        Object other$howToOut = other.getHowToOut();
        return (this$howToOut != null) ? this$howToOut.equals(other$howToOut) : (other$howToOut == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysMapInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $mapSerial = this.getMapSerial();
        result = result * 59 + (($mapSerial == null) ? 43 : $mapSerial.hashCode());
        Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        Object $bornPoint = this.getBornPoint();
        result = result * 59 + (($bornPoint == null) ? 43 : $bornPoint.hashCode());
        Object $warMap = this.getWarMap();
        result = result * 59 + (($warMap == null) ? 43 : $warMap.hashCode());
        Object $mapType = this.getMapType();
        result = result * 59 + (($mapType == null) ? 43 : $mapType.hashCode());
        Object $canKuaFu = this.getCanKuaFu();
        result = result * 59 + (($canKuaFu == null) ? 43 : $canKuaFu.hashCode());
        result = result * 59 + this.getCaneasyleave();
        result = result * 59 + this.getBgw();
        result = result * 59 + this.getBgh();
        result = result * 59 + this.getIsPkMap();
        result = result * 59 + this.getIsWanted();
        Object $fuBen = this.getFuBen();
        result = result * 59 + (($fuBen == null) ? 43 : $fuBen.hashCode());
        Object $levelScope = this.getLevelScope();
        result = result * 59 + (($levelScope == null) ? 43 : $levelScope.hashCode());
        Object $howToOut = this.getHowToOut();
        result = result * 59 + (($howToOut == null) ? 43 : $howToOut.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysMapInfo(id=" + this.getId() + ", name=" + this.getName() + ", mapSerial=" + this.getMapSerial() + ", lvLimit=" + this.getLvLimit() + ", bornPoint=" + this.getBornPoint() + ", warMap=" + this.getWarMap() + ", mapType=" + this.getMapType() + ", canKuaFu=" + this.getCanKuaFu() + ", caneasyleave=" + this.getCaneasyleave() + ", bgw=" + this.getBgw() + ", bgh=" + this.getBgh() + ", isPkMap=" + this.getIsPkMap() + ", isWanted=" + this.getIsWanted() + ", fuBen=" + this.getFuBen() + ", levelScope=" + this.getLevelScope() + ", howToOut=" + this.getHowToOut() + ")";
    }
}
