
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
        this.npcTypes = new ArrayList<Integer>();
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
        final SysNpcInfo other = (SysNpcInfo)o;
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
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0102: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0102;
                }
            }
            else if (this$serial.equals(other$serial)) {
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
        final Object this$shape = this.getShape();
        final Object other$shape = other.getShape();
        Label_0176: {
            if (this$shape == null) {
                if (other$shape == null) {
                    break Label_0176;
                }
            }
            else if (this$shape.equals(other$shape)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$posInfo = this.getPosInfo();
        final Object other$posInfo = other.getPosInfo();
        Label_0213: {
            if (this$posInfo == null) {
                if (other$posInfo == null) {
                    break Label_0213;
                }
            }
            else if (this$posInfo.equals(other$posInfo)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$talk = this.getTalk();
        final Object other$talk = other.getTalk();
        Label_0250: {
            if (this$talk == null) {
                if (other$talk == null) {
                    break Label_0250;
                }
            }
            else if (this$talk.equals(other$talk)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$npcTypes = this.getNpcTypes();
        final Object other$npcTypes = other.getNpcTypes();
        Label_0287: {
            if (this$npcTypes == null) {
                if (other$npcTypes == null) {
                    break Label_0287;
                }
            }
            else if (this$npcTypes.equals(other$npcTypes)) {
                break Label_0287;
            }
            return false;
        }
        if (this.getType() != other.getType()) {
            return false;
        }
        final Object this$shipper = this.getShipper();
        final Object other$shipper = other.getShipper();
        Label_0337: {
            if (this$shipper == null) {
                if (other$shipper == null) {
                    break Label_0337;
                }
            }
            else if (this$shipper.equals(other$shipper)) {
                break Label_0337;
            }
            return false;
        }
        if (this.getClassify() != other.getClassify()) {
            return false;
        }
        final Object this$period = this.getPeriod();
        final Object other$period = other.getPeriod();
        Label_0387: {
            if (this$period == null) {
                if (other$period == null) {
                    break Label_0387;
                }
            }
            else if (this$period.equals(other$period)) {
                break Label_0387;
            }
            return false;
        }
        final Object this$pos = this.getPos();
        final Object other$pos = other.getPos();
        if (this$pos == null) {
            if (other$pos == null) {
                return true;
            }
        }
        else if (this$pos.equals(other$pos)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysNpcInfo;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        final Object $posInfo = this.getPosInfo();
        result = result * 59 + (($posInfo == null) ? 43 : $posInfo.hashCode());
        final Object $talk = this.getTalk();
        result = result * 59 + (($talk == null) ? 43 : $talk.hashCode());
        final Object $npcTypes = this.getNpcTypes();
        result = result * 59 + (($npcTypes == null) ? 43 : $npcTypes.hashCode());
        result = result * 59 + this.getType();
        final Object $shipper = this.getShipper();
        result = result * 59 + (($shipper == null) ? 43 : $shipper.hashCode());
        result = result * 59 + this.getClassify();
        final Object $period = this.getPeriod();
        result = result * 59 + (($period == null) ? 43 : $period.hashCode());
        final Object $pos = this.getPos();
        result = result * 59 + (($pos == null) ? 43 : $pos.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysNpcInfo(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", shape=" + this.getShape() + ", posInfo=" + this.getPosInfo() + ", talk=" + this.getTalk() + ", npcTypes=" + this.getNpcTypes() + ", type=" + this.getType() + ", shipper=" + this.getShipper() + ", classify=" + this.getClassify() + ", period=" + this.getPeriod() + ", pos=" + this.getPos() + ")";
    }
}
