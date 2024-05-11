
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
        final SysMapTeleporter other = (SysMapTeleporter)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        Label_0065: {
            if (this$id == null) {
                if (other$id == null) {
                    break Label_0065;
                }
            }
            else if (this$id.equals(other$id)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$type = this.getType();
        final Object other$type = other.getType();
        Label_0102: {
            if (this$type == null) {
                if (other$type == null) {
                    break Label_0102;
                }
            }
            else if (this$type.equals(other$type)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0139: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0139;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$fromMapSerial = this.getFromMapSerial();
        final Object other$fromMapSerial = other.getFromMapSerial();
        Label_0176: {
            if (this$fromMapSerial == null) {
                if (other$fromMapSerial == null) {
                    break Label_0176;
                }
            }
            else if (this$fromMapSerial.equals(other$fromMapSerial)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$fromX = this.getFromX();
        final Object other$fromX = other.getFromX();
        Label_0213: {
            if (this$fromX == null) {
                if (other$fromX == null) {
                    break Label_0213;
                }
            }
            else if (this$fromX.equals(other$fromX)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$fromY = this.getFromY();
        final Object other$fromY = other.getFromY();
        Label_0250: {
            if (this$fromY == null) {
                if (other$fromY == null) {
                    break Label_0250;
                }
            }
            else if (this$fromY.equals(other$fromY)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$toMapSerial = this.getToMapSerial();
        final Object other$toMapSerial = other.getToMapSerial();
        Label_0287: {
            if (this$toMapSerial == null) {
                if (other$toMapSerial == null) {
                    break Label_0287;
                }
            }
            else if (this$toMapSerial.equals(other$toMapSerial)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$toMapName = this.getToMapName();
        final Object other$toMapName = other.getToMapName();
        Label_0324: {
            if (this$toMapName == null) {
                if (other$toMapName == null) {
                    break Label_0324;
                }
            }
            else if (this$toMapName.equals(other$toMapName)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$toMapX = this.getToMapX();
        final Object other$toMapX = other.getToMapX();
        Label_0361: {
            if (this$toMapX == null) {
                if (other$toMapX == null) {
                    break Label_0361;
                }
            }
            else if (this$toMapX.equals(other$toMapX)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$toMapY = this.getToMapY();
        final Object other$toMapY = other.getToMapY();
        Label_0398: {
            if (this$toMapY == null) {
                if (other$toMapY == null) {
                    break Label_0398;
                }
            }
            else if (this$toMapY.equals(other$toMapY)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$dir = this.getDir();
        final Object other$dir = other.getDir();
        if (this$dir == null) {
            if (other$dir == null) {
                return true;
            }
        }
        else if (this$dir.equals(other$dir)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysMapTeleporter;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $fromMapSerial = this.getFromMapSerial();
        result = result * 59 + (($fromMapSerial == null) ? 43 : $fromMapSerial.hashCode());
        final Object $fromX = this.getFromX();
        result = result * 59 + (($fromX == null) ? 43 : $fromX.hashCode());
        final Object $fromY = this.getFromY();
        result = result * 59 + (($fromY == null) ? 43 : $fromY.hashCode());
        final Object $toMapSerial = this.getToMapSerial();
        result = result * 59 + (($toMapSerial == null) ? 43 : $toMapSerial.hashCode());
        final Object $toMapName = this.getToMapName();
        result = result * 59 + (($toMapName == null) ? 43 : $toMapName.hashCode());
        final Object $toMapX = this.getToMapX();
        result = result * 59 + (($toMapX == null) ? 43 : $toMapX.hashCode());
        final Object $toMapY = this.getToMapY();
        result = result * 59 + (($toMapY == null) ? 43 : $toMapY.hashCode());
        final Object $dir = this.getDir();
        result = result * 59 + (($dir == null) ? 43 : $dir.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysMapTeleporter(id=" + this.getId() + ", type=" + this.getType() + ", name=" + this.getName() + ", fromMapSerial=" + this.getFromMapSerial() + ", fromX=" + this.getFromX() + ", fromY=" + this.getFromY() + ", toMapSerial=" + this.getToMapSerial() + ", toMapName=" + this.getToMapName() + ", toMapX=" + this.getToMapX() + ", toMapY=" + this.getToMapY() + ", dir=" + this.getDir() + ")";
    }
}
