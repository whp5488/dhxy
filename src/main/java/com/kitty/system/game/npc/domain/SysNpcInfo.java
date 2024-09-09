package com.kitty.system.game.npc.domain;

import java.util.ArrayList;
import com.kitty.common.model.Pos;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_npc_info")
public class SysNpcInfo implements Serializable
{
    private static final long serialVersionUID = 1L;
    public static final int TYPE_NORMAL = 1;
    public static final int TYPE_UNUSUAL = 2;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("npc名字")
    @Column("name")
    private String name;
    @Comment("外貌")
    @Column("shape")
    private Integer shape;
    @Comment("位置信息")
    @Column("pos_info")
    private String posInfo;
    @Comment("对话内容")
    @Column("talk")
    private String talk;
    @Comment("npc按钮")
    @Column("npc_types")
    @Default("[]")
    private List<Integer> npcTypes;
    @Comment("类型")
    @Column("type")
    private int type;
    @Comment("货摊")
    @Column("shipper")
    private String shipper;
    @Comment("分类：0.场景 其他：对应活动或者副本编号")
    @Column("classify")
    private int classify;
    @Comment("周期性显示在地图")
    @Column("period")
    @Default("[]")
    private List<Integer> period;
    private Pos pos;
    
    public SysNpcInfo() {
        this.npcTypes = new ArrayList<>();
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getShape() {
        return this.shape;
    }
    
    public String getPosInfo() {
        return this.posInfo;
    }
    
    public String getTalk() {
        return this.talk;
    }
    
    public List<Integer> getNpcTypes() {
        return this.npcTypes;
    }
    
    public int getType() {
        return this.type;
    }
    
    public String getShipper() {
        return this.shipper;
    }
    
    public int getClassify() {
        return this.classify;
    }
    
    public List<Integer> getPeriod() {
        return this.period;
    }
    
    public Pos getPos() {
        return this.pos;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setPosInfo(final String posInfo) {
        this.posInfo = posInfo;
    }
    
    public void setTalk(final String talk) {
        this.talk = talk;
    }
    
    public void setNpcTypes(final List<Integer> npcTypes) {
        this.npcTypes = npcTypes;
    }
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public void setShipper(final String shipper) {
        this.shipper = shipper;
    }
    
    public void setClassify(final int classify) {
        this.classify = classify;
    }
    
    public void setPeriod(final List<Integer> period) {
        this.period = period;
    }
    
    public void setPos(final Pos pos) {
        this.pos = pos;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysNpcInfo)) {
            return false;
        }
        SysNpcInfo other = (SysNpcInfo)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$serial = this.getSerial();
        Object other$serial = other.getSerial();
        if (!((this$serial != null) ? this$serial.equals(other$serial) : (other$serial == null))) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$shape = this.getShape();
        Object other$shape = other.getShape();
        if (!((this$shape != null) ? this$shape.equals(other$shape) : (other$shape == null))) {
            return false;
        }
        Object this$posInfo = this.getPosInfo();
        Object other$posInfo = other.getPosInfo();
        if (!((this$posInfo != null) ? this$posInfo.equals(other$posInfo) : (other$posInfo == null))) {
            return false;
        }
        Object this$talk = this.getTalk();
        Object other$talk = other.getTalk();
        if (!((this$talk != null) ? this$talk.equals(other$talk) : (other$talk == null))) {
            return false;
        }
        Object this$npcTypes = this.getNpcTypes();
        Object other$npcTypes = other.getNpcTypes();
        if (!((this$npcTypes != null) ? this$npcTypes.equals(other$npcTypes) : (other$npcTypes == null))) {
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        Object this$shipper = this.getShipper();
        Object other$shipper = other.getShipper();
        if (!((this$shipper != null) ? this$shipper.equals(other$shipper) : (other$shipper == null))) {
            return false;
        }
        if (this.getClassify() != other.getClassify()) {
            return false;
        }
        Object this$period = this.getPeriod();
        Object other$period = other.getPeriod();
        if (!((this$period != null) ? this$period.equals(other$period) : (other$period == null))) {
            return false;
        }
        Object this$pos = this.getPos();
        Object other$pos = other.getPos();
        return (this$pos != null) ? this$pos.equals(other$pos) : (other$pos == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysNpcInfo;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        Object $posInfo = this.getPosInfo();
        result = result * 59 + (($posInfo == null) ? 43 : $posInfo.hashCode());
        Object $talk = this.getTalk();
        result = result * 59 + (($talk == null) ? 43 : $talk.hashCode());
        Object $npcTypes = this.getNpcTypes();
        result = result * 59 + (($npcTypes == null) ? 43 : $npcTypes.hashCode());
        result = result * 59 + this.getType();
        Object $shipper = this.getShipper();
        result = result * 59 + (($shipper == null) ? 43 : $shipper.hashCode());
        result = result * 59 + this.getClassify();
        Object $period = this.getPeriod();
        result = result * 59 + (($period == null) ? 43 : $period.hashCode());
        Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysNpcInfo(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", shape=" + this.getShape() + ", posInfo=" + this.getPosInfo() + ", talk=" + this.getTalk() + ", npcTypes=" + this.getNpcTypes() + ", type=" + this.getType() + ", shipper=" + this.getShipper() + ", classify=" + this.getClassify() + ", period=" + this.getPeriod() + ", pos=" + this.getPos() + ")";
    }
}
