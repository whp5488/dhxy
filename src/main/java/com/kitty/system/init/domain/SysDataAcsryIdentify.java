
package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Table;

@Table("data_acsry_identify")
public class SysDataAcsryIdentify
{
    @Column("suite_type")
    private String suiteType;
    @Column("silver")
    private long silver;
    @Column("item")
    @Default("[]")
    private List<Item> list;
    
    public SysDataAcsryIdentify() {
        this.silver = 0L;
    }
    
    public String getSuiteType() {
        return this.suiteType;
    }
    
    public long getSilver() {
        return this.silver;
    }
    
    public List<Item> getList() {
        return this.list;
    }
    
    public void setSuiteType(final String suiteType) {
        this.suiteType = suiteType;
    }
    
    public void setSilver(final long silver) {
        this.silver = silver;
    }
    
    public void setList(final List<Item> list) {
        this.list = list;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysDataAcsryIdentify)) {
            return false;
        }
        final SysDataAcsryIdentify other = (SysDataAcsryIdentify)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$suiteType = this.getSuiteType();
        final Object other$suiteType = other.getSuiteType();
        Label_0065: {
            if (this$suiteType == null) {
                if (other$suiteType == null) {
                    break Label_0065;
                }
            }
            else if (this$suiteType.equals(other$suiteType)) {
                break Label_0065;
            }
            return false;
        }
        if (this.getSilver() != other.getSilver()) {
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        if (this$list == null) {
            if (other$list == null) {
                return true;
            }
        }
        else if (this$list.equals(other$list)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysDataAcsryIdentify;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $suiteType = this.getSuiteType();
        result = result * 59 + (($suiteType == null) ? 43 : $suiteType.hashCode());
        final long $silver = this.getSilver();
        result = result * 59 + (int)($silver >>> 32 ^ $silver);
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysDataAcsryIdentify(suiteType=" + this.getSuiteType() + ", silver=" + this.getSilver() + ", list=" + this.getList() + ")";
    }
}
