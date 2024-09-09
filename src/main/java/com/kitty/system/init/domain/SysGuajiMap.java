package com.kitty.system.init.domain;

import java.util.Arrays;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_guaji_map")
public class SysGuajiMap implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("地图编号")
    @Column("map_serial")
    private Integer mapSerial;
    @Comment("地图名称")
    @Column("map_name")
    private String mapName;
    @Comment("建议等级")
    @Column("recommend_lv")
    @Default("[]")
    private Integer[] recommendLv;
    @Comment("战斗外键")
    @Column("war_data_id")
    private Integer warDataId;
    @Comment("转生需求")
    @Column("unlock_zs")
    private Integer unlockzs;
    @Comment("等级需求")
    @Column("unlock_lv")
    private Integer unlocklv;
    @Comment("")
    @Column("always_judge_lv_flag")
    private Integer always_judge_lv_flag;
    
    public SysGuajiMap() {
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public Integer getMapSerial() {
        return this.mapSerial;
    }
    
    public String getMapName() {
        return this.mapName;
    }
    
    public Integer[] getRecommendLv() {
        return this.recommendLv;
    }
    
    public Integer getWarDataId() {
        return this.warDataId;
    }
    
    public Integer getUnlockzs() {
        return this.unlockzs;
    }
    
    public Integer getUnlocklv() {
        return this.unlocklv;
    }
    
    public Integer getAlways_judge_lv_flag() {
        return this.always_judge_lv_flag;
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setMapSerial(final Integer mapSerial) {
        this.mapSerial = mapSerial;
    }
    
    public void setMapName(final String mapName) {
        this.mapName = mapName;
    }
    
    public void setRecommendLv(final Integer[] recommendLv) {
        this.recommendLv = recommendLv;
    }
    
    public void setWarDataId(final Integer warDataId) {
        this.warDataId = warDataId;
    }
    
    public void setUnlockzs(final Integer unlockzs) {
        this.unlockzs = unlockzs;
    }
    
    public void setUnlocklv(final Integer unlocklv) {
        this.unlocklv = unlocklv;
    }
    
    public void setAlways_judge_lv_flag(final Integer always_judge_lv_flag) {
        this.always_judge_lv_flag = always_judge_lv_flag;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysGuajiMap)) {
            return false;
        }
        SysGuajiMap other = (SysGuajiMap)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$id = this.getId();
        Object other$id = other.getId();
        if (!((this$id != null) ? this$id.equals(other$id) : (other$id == null))) {
            return false;
        }
        Object this$mapSerial = this.getMapSerial();
        Object other$mapSerial = other.getMapSerial();
        if (!((this$mapSerial != null) ? this$mapSerial.equals(other$mapSerial) : (other$mapSerial == null))) {
            return false;
        }
        Object this$mapName = this.getMapName();
        Object other$mapName = other.getMapName();
        if (!((this$mapName != null) ? this$mapName.equals(other$mapName) : (other$mapName == null))) {
            return false;
        }
        if (!Arrays.deepEquals(this.getRecommendLv(), other.getRecommendLv())) {
            return false;
        }
        Object this$warDataId = this.getWarDataId();
        Object other$warDataId = other.getWarDataId();
        if (!((this$warDataId != null) ? this$warDataId.equals(other$warDataId) : (other$warDataId == null))) {
            return false;
        }
        Object this$unlockzs = this.getUnlockzs();
        Object other$unlockzs = other.getUnlockzs();
        if (!((this$unlockzs != null) ? this$unlockzs.equals(other$unlockzs) : (other$unlockzs == null))) {
            return false;
        }
        Object this$unlocklv = this.getUnlocklv();
        Object other$unlocklv = other.getUnlocklv();
        if (!((this$unlocklv != null) ? this$unlocklv.equals(other$unlocklv) : (other$unlocklv == null))) {
            return false;
        }
        Object this$always_judge_lv_flag = this.getAlways_judge_lv_flag();
        Object other$always_judge_lv_flag = other.getAlways_judge_lv_flag();
        return (this$always_judge_lv_flag != null) ? this$always_judge_lv_flag.equals(other$always_judge_lv_flag) : (other$always_judge_lv_flag == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGuajiMap;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        Object $mapSerial = this.getMapSerial();
        result = result * 59 + (($mapSerial == null) ? 43 : $mapSerial.hashCode());
        Object $mapName = this.getMapName();
        result = result * 59 + (($mapName == null) ? 43 : $mapName.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getRecommendLv());
        Object $warDataId = this.getWarDataId();
        result = result * 59 + (($warDataId == null) ? 43 : $warDataId.hashCode());
        Object $unlockzs = this.getUnlockzs();
        result = result * 59 + (($unlockzs == null) ? 43 : $unlockzs.hashCode());
        Object $unlocklv = this.getUnlocklv();
        result = result * 59 + (($unlocklv == null) ? 43 : $unlocklv.hashCode());
        Object $always_judge_lv_flag = this.getAlways_judge_lv_flag();
        result = result * 59 + (($always_judge_lv_flag == null) ? 43 : $always_judge_lv_flag.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGuajiMap(id=" + this.getId() + ", mapSerial=" + this.getMapSerial() + ", mapName=" + this.getMapName() + ", recommendLv=" + Arrays.deepToString(this.getRecommendLv()) + ", warDataId=" + this.getWarDataId() + ", unlockzs=" + this.getUnlockzs() + ", unlocklv=" + this.getUnlocklv() + ", always_judge_lv_flag=" + this.getAlways_judge_lv_flag() + ")";
    }
}
