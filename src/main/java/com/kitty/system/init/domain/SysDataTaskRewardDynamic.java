
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_task_reward_dynamic")
public class SysDataTaskRewardDynamic implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("标识")
    @Column("identify")
    private int identify;
    @Comment("字典")
    @Column("dict")
    private String dict;
    @Comment("物品编号")
    @Column("serial")
    private int serial;
    @Comment("比例计算")
    @Column("rate_calculate")
    private String rateCalculate;
    @Comment("数量计算")
    @Column("num_calculate")
    private String numCalculate;
    @Comment("公告编号")
    @Column("notice")
    private int notice;
    
    public int getIdentify() {
        return this.identify;
    }
    
    public String getDict() {
        return this.dict;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getRateCalculate() {
        return this.rateCalculate;
    }
    
    public String getNumCalculate() {
        return this.numCalculate;
    }
    
    public int getNotice() {
        return this.notice;
    }
    
    public void setIdentify(final int identify) {
        this.identify = identify;
    }
    
    public void setDict(final String dict) {
        this.dict = dict;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRateCalculate(final String rateCalculate) {
        this.rateCalculate = rateCalculate;
    }
    
    public void setNumCalculate(final String numCalculate) {
        this.numCalculate = numCalculate;
    }
    
    public void setNotice(final int notice) {
        this.notice = notice;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataTaskRewardDynamic)) {
            return false;
        }
        final SysDataTaskRewardDynamic other = (SysDataTaskRewardDynamic)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getIdentify() != other.getIdentify()) {
            return false;
        }
        final Object this$dict = this.getDict();
        final Object other$dict = other.getDict();
        Label_0078: {
            if (this$dict == null) {
                if (other$dict == null) {
                    break Label_0078;
                }
            }
            else if (this$dict.equals(other$dict)) {
                break Label_0078;
            }
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        final Object this$rateCalculate = this.getRateCalculate();
        final Object other$rateCalculate = other.getRateCalculate();
        Label_0128: {
            if (this$rateCalculate == null) {
                if (other$rateCalculate == null) {
                    break Label_0128;
                }
            }
            else if (this$rateCalculate.equals(other$rateCalculate)) {
                break Label_0128;
            }
            return false;
        }
        final Object this$numCalculate = this.getNumCalculate();
        final Object other$numCalculate = other.getNumCalculate();
        if (this$numCalculate == null) {
            if (other$numCalculate == null) {
                return this.getNotice() == other.getNotice();
            }
        }
        else if (this$numCalculate.equals(other$numCalculate)) {
            return this.getNotice() == other.getNotice();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataTaskRewardDynamic;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getIdentify();
        final Object $dict = this.getDict();
        result = result * 59 + (($dict == null) ? 43 : $dict.hashCode());
        result = result * 59 + this.getSerial();
        final Object $rateCalculate = this.getRateCalculate();
        result = result * 59 + (($rateCalculate == null) ? 43 : $rateCalculate.hashCode());
        final Object $numCalculate = this.getNumCalculate();
        result = result * 59 + (($numCalculate == null) ? 43 : $numCalculate.hashCode());
        result = result * 59 + this.getNotice();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataTaskRewardDynamic(identify=" + this.getIdentify() + ", dict=" + this.getDict() + ", serial=" + this.getSerial() + ", rateCalculate=" + this.getRateCalculate() + ", numCalculate=" + this.getNumCalculate() + ", notice=" + this.getNotice() + ")";
    }
}
