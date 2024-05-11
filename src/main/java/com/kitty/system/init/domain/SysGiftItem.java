
package com.kitty.system.init.domain;

import java.util.Arrays;
import org.nutz.dao.entity.annotation.ColDefine;
import com.kitty.system.game.vo.init.Item;
import java.util.List;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_gift_item")
public class SysGiftItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("道具编号")
    @Column("serial")
    private Integer serial;
    @Comment("道具名称")
    @Column("name")
    private String name;
    @Comment("价格")
    @Column("price")
    private Integer price;
    @Comment("回收价格 0直接回收")
    @Column("reprice")
    private Integer reprice;
    @Comment("元宝")
    @Column("gold")
    private Long gold;
    @Comment("银两")
    @Column("silver")
    private Long silver;
    @Comment("贡币")
    @Column("coin")
    private Long coin;
    @Comment("经验：通过计算")
    @Column("exp")
    private int exp;
    @Comment("道具外观")
    @Column("shape")
    private Integer shape;
    @Comment("背包重叠数量")
    @Column("can_merge")
    private Integer canMerge;
    @Comment("绑定状态")
    @Column("bind")
    private Integer bind;
    @Comment("转生需求")
    @Column("need_zs")
    private Integer needZs;
    @Comment("等级需求")
    @Column("need_lv")
    private Integer needLv;
    @Comment("使用日期限制范围")
    @Column("use_need_day")
    @Default("[]")
    private Integer[] useNeedDay;
    @Comment("额外奖励道具")
    @Column("extra")
    @Default("[]")
    @ColDefine(customType = "text")
    private List<Item> extra;
    @Comment("是否概率开启：0.否，1.是")
    @Column("cycle")
    private Integer cycle;
    @Comment("是否开启：0.否，1.是")
    @Column("is_open")
    private Integer isOpen;
    @Comment("参数")
    @Column("param")
    private String param;
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public Integer getReprice() {
        return this.reprice;
    }
    
    public Long getGold() {
        return this.gold;
    }
    
    public Long getSilver() {
        return this.silver;
    }
    
    public Long getCoin() {
        return this.coin;
    }
    
    public int getExp() {
        return this.exp;
    }
    
    public Integer getShape() {
        return this.shape;
    }
    
    public Integer getCanMerge() {
        return this.canMerge;
    }
    
    public Integer getBind() {
        return this.bind;
    }
    
    public Integer getNeedZs() {
        return this.needZs;
    }
    
    public Integer getNeedLv() {
        return this.needLv;
    }
    
    public Integer[] getUseNeedDay() {
        return this.useNeedDay;
    }
    
    public List<Item> getExtra() {
        return this.extra;
    }
    
    public Integer getCycle() {
        return this.cycle;
    }
    
    public Integer getIsOpen() {
        return this.isOpen;
    }
    
    public String getParam() {
        return this.param;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setPrice(final Integer price) {
        this.price = price;
    }
    
    public void setReprice(final Integer reprice) {
        this.reprice = reprice;
    }
    
    public void setGold(final Long gold) {
        this.gold = gold;
    }
    
    public void setSilver(final Long silver) {
        this.silver = silver;
    }
    
    public void setCoin(final Long coin) {
        this.coin = coin;
    }
    
    public void setExp(final int exp) {
        this.exp = exp;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setCanMerge(final Integer canMerge) {
        this.canMerge = canMerge;
    }
    
    public void setBind(final Integer bind) {
        this.bind = bind;
    }
    
    public void setNeedZs(final Integer needZs) {
        this.needZs = needZs;
    }
    
    public void setNeedLv(final Integer needLv) {
        this.needLv = needLv;
    }
    
    public void setUseNeedDay(final Integer[] useNeedDay) {
        this.useNeedDay = useNeedDay;
    }
    
    public void setExtra(final List<Item> extra) {
        this.extra = extra;
    }
    
    public void setCycle(final Integer cycle) {
        this.cycle = cycle;
    }
    
    public void setIsOpen(final Integer isOpen) {
        this.isOpen = isOpen;
    }
    
    public void setParam(final String param) {
        this.param = param;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysGiftItem)) {
            return false;
        }
        final SysGiftItem other = (SysGiftItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$serial = this.getSerial();
        final Object other$serial = other.getSerial();
        Label_0065: {
            if (this$serial == null) {
                if (other$serial == null) {
                    break Label_0065;
                }
            }
            else if (this$serial.equals(other$serial)) {
                break Label_0065;
            }
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0102: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0102;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0102;
            }
            return false;
        }
        final Object this$price = this.getPrice();
        final Object other$price = other.getPrice();
        Label_0139: {
            if (this$price == null) {
                if (other$price == null) {
                    break Label_0139;
                }
            }
            else if (this$price.equals(other$price)) {
                break Label_0139;
            }
            return false;
        }
        final Object this$reprice = this.getReprice();
        final Object other$reprice = other.getReprice();
        Label_0176: {
            if (this$reprice == null) {
                if (other$reprice == null) {
                    break Label_0176;
                }
            }
            else if (this$reprice.equals(other$reprice)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$gold = this.getGold();
        final Object other$gold = other.getGold();
        Label_0213: {
            if (this$gold == null) {
                if (other$gold == null) {
                    break Label_0213;
                }
            }
            else if (this$gold.equals(other$gold)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$silver = this.getSilver();
        final Object other$silver = other.getSilver();
        Label_0250: {
            if (this$silver == null) {
                if (other$silver == null) {
                    break Label_0250;
                }
            }
            else if (this$silver.equals(other$silver)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$coin = this.getCoin();
        final Object other$coin = other.getCoin();
        Label_0287: {
            if (this$coin == null) {
                if (other$coin == null) {
                    break Label_0287;
                }
            }
            else if (this$coin.equals(other$coin)) {
                break Label_0287;
            }
            return false;
        }
        if (this.getExp() != other.getExp()) {
            return false;
        }
        final Object this$shape = this.getShape();
        final Object other$shape = other.getShape();
        Label_0337: {
            if (this$shape == null) {
                if (other$shape == null) {
                    break Label_0337;
                }
            }
            else if (this$shape.equals(other$shape)) {
                break Label_0337;
            }
            return false;
        }
        final Object this$canMerge = this.getCanMerge();
        final Object other$canMerge = other.getCanMerge();
        Label_0374: {
            if (this$canMerge == null) {
                if (other$canMerge == null) {
                    break Label_0374;
                }
            }
            else if (this$canMerge.equals(other$canMerge)) {
                break Label_0374;
            }
            return false;
        }
        final Object this$bind = this.getBind();
        final Object other$bind = other.getBind();
        Label_0411: {
            if (this$bind == null) {
                if (other$bind == null) {
                    break Label_0411;
                }
            }
            else if (this$bind.equals(other$bind)) {
                break Label_0411;
            }
            return false;
        }
        final Object this$needZs = this.getNeedZs();
        final Object other$needZs = other.getNeedZs();
        Label_0448: {
            if (this$needZs == null) {
                if (other$needZs == null) {
                    break Label_0448;
                }
            }
            else if (this$needZs.equals(other$needZs)) {
                break Label_0448;
            }
            return false;
        }
        final Object this$needLv = this.getNeedLv();
        final Object other$needLv = other.getNeedLv();
        Label_0485: {
            if (this$needLv == null) {
                if (other$needLv == null) {
                    break Label_0485;
                }
            }
            else if (this$needLv.equals(other$needLv)) {
                break Label_0485;
            }
            return false;
        }
        if (!Arrays.deepEquals(this.getUseNeedDay(), other.getUseNeedDay())) {
            return false;
        }
        final Object this$extra = this.getExtra();
        final Object other$extra = other.getExtra();
        Label_0538: {
            if (this$extra == null) {
                if (other$extra == null) {
                    break Label_0538;
                }
            }
            else if (this$extra.equals(other$extra)) {
                break Label_0538;
            }
            return false;
        }
        final Object this$cycle = this.getCycle();
        final Object other$cycle = other.getCycle();
        Label_0575: {
            if (this$cycle == null) {
                if (other$cycle == null) {
                    break Label_0575;
                }
            }
            else if (this$cycle.equals(other$cycle)) {
                break Label_0575;
            }
            return false;
        }
        final Object this$isOpen = this.getIsOpen();
        final Object other$isOpen = other.getIsOpen();
        Label_0612: {
            if (this$isOpen == null) {
                if (other$isOpen == null) {
                    break Label_0612;
                }
            }
            else if (this$isOpen.equals(other$isOpen)) {
                break Label_0612;
            }
            return false;
        }
        final Object this$param = this.getParam();
        final Object other$param = other.getParam();
        if (this$param == null) {
            if (other$param == null) {
                return true;
            }
        }
        else if (this$param.equals(other$param)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGiftItem;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        final Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        final Object $gold = this.getGold();
        result = result * 59 + (($gold == null) ? 43 : $gold.hashCode());
        final Object $silver = this.getSilver();
        result = result * 59 + (($silver == null) ? 43 : $silver.hashCode());
        final Object $coin = this.getCoin();
        result = result * 59 + (($coin == null) ? 43 : $coin.hashCode());
        result = result * 59 + this.getExp();
        final Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        final Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        final Object $bind = this.getBind();
        result = result * 59 + (($bind == null) ? 43 : $bind.hashCode());
        final Object $needZs = this.getNeedZs();
        result = result * 59 + (($needZs == null) ? 43 : $needZs.hashCode());
        final Object $needLv = this.getNeedLv();
        result = result * 59 + (($needLv == null) ? 43 : $needLv.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getUseNeedDay());
        final Object $extra = this.getExtra();
        result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
        final Object $cycle = this.getCycle();
        result = result * 59 + (($cycle == null) ? 43 : $cycle.hashCode());
        final Object $isOpen = this.getIsOpen();
        result = result * 59 + (($isOpen == null) ? 43 : $isOpen.hashCode());
        final Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGiftItem(serial=" + this.getSerial() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", gold=" + this.getGold() + ", silver=" + this.getSilver() + ", coin=" + this.getCoin() + ", exp=" + this.getExp() + ", shape=" + this.getShape() + ", canMerge=" + this.getCanMerge() + ", bind=" + this.getBind() + ", needZs=" + this.getNeedZs() + ", needLv=" + this.getNeedLv() + ", useNeedDay=" + Arrays.deepToString(this.getUseNeedDay()) + ", extra=" + this.getExtra() + ", cycle=" + this.getCycle() + ", isOpen=" + this.getIsOpen() + ", param=" + this.getParam() + ")";
    }
}
