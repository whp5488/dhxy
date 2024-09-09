package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_map_teleporter")
public class SysMapTeleporter implements Serializable
{
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("类型")
    @Column("type")
    private Integer type;
    @Comment("地图名称")
    @Column("from_map_name")
    private String name;
    @Comment("地图编号")
    @Column("from_map_serial")
    private Integer fromMapSerial;
    @Comment("地图传送点X")
    @Column("from_x")
    private Integer fromX;
    @Comment("地图传送点Y")
    @Column("from_y")
    private Integer fromY;
    @Comment("传送地图编号")
    @Column("to_map_serial")
    private Integer toMapSerial;
    @Comment("传送地图名称")
    @Column("to_map_name")
    private String toMapName;
    @Comment("传送地图X")
    @Column("to_map_x")
    private Integer toMapX;
    @Comment("传送地图Y")
    @Column("to_map_y")
    private Integer toMapY;
    @Comment("传送到地图朝向")
    @Column("dir")
    private Integer dir;
    
    public SysMapTeleporter() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getFromMapSerial() {
        return this.fromMapSerial;
    }
    
    public Integer getFromX() {
        return this.fromX;
    }
    
    public Integer getFromY() {
        return this.fromY;
    }
    
    public Integer getToMapSerial() {
        return this.toMapSerial;
    }
    
    public String getToMapName() {
        return this.toMapName;
    }
    
    public Integer getToMapX() {
        return this.toMapX;
    }
    
    public Integer getToMapY() {
        return this.toMapY;
    }
    
    public Integer getDir() {
        return this.dir;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setFromMapSerial(final Integer fromMapSerial) {
        this.fromMapSerial = fromMapSerial;
    }
    
    public void setFromX(final Integer fromX) {
        this.fromX = fromX;
    }
    
    public void setFromY(final Integer fromY) {
        this.fromY = fromY;
    }
    
    public void setToMapSerial(final Integer toMapSerial) {
        this.toMapSerial = toMapSerial;
    }
    
    public void setToMapName(final String toMapName) {
        this.toMapName = toMapName;
    }
    
    public void setToMapX(final Integer toMapX) {
        this.toMapX = toMapX;
    }
    
    public void setToMapY(final Integer toMapY) {
        this.toMapY = toMapY;
    }
    
    public void setDir(final Integer dir) {
        this.dir = dir;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysMapTeleporter)) {
            return false;
        }
        SysMapTeleporter other = (SysMapTeleporter)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$fromMapSerial = this.getFromMapSerial();
        Object other$fromMapSerial = other.getFromMapSerial();
        if (!((this$fromMapSerial != null) ? this$fromMapSerial.equals(other$fromMapSerial) : (other$fromMapSerial == null))) {
            return false;
        }
        Object this$fromX = this.getFromX();
        Object other$fromX = other.getFromX();
        if (!((this$fromX != null) ? this$fromX.equals(other$fromX) : (other$fromX == null))) {
            return false;
        }
        Object this$fromY = this.getFromY();
        Object other$fromY = other.getFromY();
        if (!((this$fromY != null) ? this$fromY.equals(other$fromY) : (other$fromY == null))) {
            return false;
        }
        Object this$toMapSerial = this.getToMapSerial();
        Object other$toMapSerial = other.getToMapSerial();
        if (!((this$toMapSerial != null) ? this$toMapSerial.equals(other$toMapSerial) : (other$toMapSerial == null))) {
            return false;
        }
        Object this$toMapName = this.getToMapName();
        Object other$toMapName = other.getToMapName();
        if (!((this$toMapName != null) ? this$toMapName.equals(other$toMapName) : (other$toMapName == null))) {
            return false;
        }
        Object this$toMapX = this.getToMapX();
        Object other$toMapX = other.getToMapX();
        if (!((this$toMapX != null) ? this$toMapX.equals(other$toMapX) : (other$toMapX == null))) {
            return false;
        }
        Object this$toMapY = this.getToMapY();
        Object other$toMapY = other.getToMapY();
        if (!((this$toMapY != null) ? this$toMapY.equals(other$toMapY) : (other$toMapY == null))) {
            return false;
        }
        Object this$dir = this.getDir();
        Object other$dir = other.getDir();
        return (this$dir != null) ? this$dir.equals(other$dir) : (other$dir == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysMapTeleporter;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $fromMapSerial = this.getFromMapSerial();
        result = result * 59 + (($fromMapSerial == null) ? 43 : $fromMapSerial.hashCode());
        Object $fromX = this.getFromX();
        result = result * 59 + (($fromX == null) ? 43 : $fromX.hashCode());
        Object $fromY = this.getFromY();
        result = result * 59 + (($fromY == null) ? 43 : $fromY.hashCode());
        Object $toMapSerial = this.getToMapSerial();
        result = result * 59 + (($toMapSerial == null) ? 43 : $toMapSerial.hashCode());
        Object $toMapName = this.getToMapName();
        result = result * 59 + (($toMapName == null) ? 43 : $toMapName.hashCode());
        Object $toMapX = this.getToMapX();
        result = result * 59 + (($toMapX == null) ? 43 : $toMapX.hashCode());
        Object $toMapY = this.getToMapY();
        result = result * 59 + (($toMapY == null) ? 43 : $toMapY.hashCode());
        Object $dir = this.getDir();
        result = result * 59 + (($dir == null) ? 43 : $dir.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysMapTeleporter(id=" + this.getId() + ", type=" + this.getType() + ", name=" + this.getName() + ", fromMapSerial=" + this.getFromMapSerial() + ", fromX=" + this.getFromX() + ", fromY=" + this.getFromY() + ", toMapSerial=" + this.getToMapSerial() + ", toMapName=" + this.getToMapName() + ", toMapX=" + this.getToMapX() + ", toMapY=" + this.getToMapY() + ", dir=" + this.getDir() + ")";
    }
}
