
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
        final SysGuajiMap other = (SysGuajiMap)o;
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
        final Object this$mapSerial = this.getMapSerial();
        final Object other$mapSerial = other.getMapSerial();
        Label_0102: {
            if (this$mapSerial == null) {
                if (other$mapSerial == null) {
                    break Label_0102;
                }
            }
            else if (this$mapSerial.equals(other$mapSerial)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$mapName = this.getMapName();
        final Object other$mapName = other.getMapName();
        Label_0139: {
            if (this$mapName == null) {
                if (other$mapName == null) {
                    break Label_0139;
                }
            }
            else if (this$mapName.equals(other$mapName)) {
                break Label_0139;
            }
            return false;
        }
        if (!Arrays.deepEquals(this.getRecommendLv(), other.getRecommendLv())) {
            return false;
        }
        final Object this$warDataId = this.getWarDataId();
        final Object other$warDataId = other.getWarDataId();
        Label_0192: {
            if (this$warDataId == null) {
                if (other$warDataId == null) {
                    break Label_0192;
                }
            }
            else if (this$warDataId.equals(other$warDataId)) {
                break Label_0192;
            }
            return false;
        }
        final Object this$unlockzs = this.getUnlockzs();
        final Object other$unlockzs = other.getUnlockzs();
        Label_0229: {
            if (this$unlockzs == null) {
                if (other$unlockzs == null) {
                    break Label_0229;
                }
            }
            else if (this$unlockzs.equals(other$unlockzs)) {
                break Label_0229;
            }
            return false;
        }
        final Object this$unlocklv = this.getUnlocklv();
        final Object other$unlocklv = other.getUnlocklv();
        Label_0266: {
            if (this$unlocklv == null) {
                if (other$unlocklv == null) {
                    break Label_0266;
                }
            }
            else if (this$unlocklv.equals(other$unlocklv)) {
                break Label_0266;
            }
            return false;
        }
        final Object this$always_judge_lv_flag = this.getAlways_judge_lv_flag();
        final Object other$always_judge_lv_flag = other.getAlways_judge_lv_flag();
        if (this$always_judge_lv_flag == null) {
            if (other$always_judge_lv_flag == null) {
                return true;
            }
        }
        else if (this$always_judge_lv_flag.equals(other$always_judge_lv_flag)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGuajiMap;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * 59 + (($id == null) ? 43 : $id.hashCode());
        final Object $mapSerial = this.getMapSerial();
        result = result * 59 + (($mapSerial == null) ? 43 : $mapSerial.hashCode());
        final Object $mapName = this.getMapName();
        result = result * 59 + (($mapName == null) ? 43 : $mapName.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getRecommendLv());
        final Object $warDataId = this.getWarDataId();
        result = result * 59 + (($warDataId == null) ? 43 : $warDataId.hashCode());
        final Object $unlockzs = this.getUnlockzs();
        result = result * 59 + (($unlockzs == null) ? 43 : $unlockzs.hashCode());
        final Object $unlocklv = this.getUnlocklv();
        result = result * 59 + (($unlocklv == null) ? 43 : $unlocklv.hashCode());
        final Object $always_judge_lv_flag = this.getAlways_judge_lv_flag();
        result = result * 59 + (($always_judge_lv_flag == null) ? 43 : $always_judge_lv_flag.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGuajiMap(id=" + this.getId() + ", mapSerial=" + this.getMapSerial() + ", mapName=" + this.getMapName() + ", recommendLv=" + Arrays.deepToString(this.getRecommendLv()) + ", warDataId=" + this.getWarDataId() + ", unlockzs=" + this.getUnlockzs() + ", unlocklv=" + this.getUnlocklv() + ", always_judge_lv_flag=" + this.getAlways_judge_lv_flag() + ")";
    }
}
