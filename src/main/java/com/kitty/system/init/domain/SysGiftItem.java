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
    
    public SysGiftItem() {
    }
    
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
        SysGiftItem other = (SysGiftItem)o;
        if (!other.canEqual(this)) {
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
        Object this$price = this.getPrice();
        Object other$price = other.getPrice();
        if (!((this$price != null) ? this$price.equals(other$price) : (other$price == null))) {
            return false;
        }
        Object this$reprice = this.getReprice();
        Object other$reprice = other.getReprice();
        if (!((this$reprice != null) ? this$reprice.equals(other$reprice) : (other$reprice == null))) {
            return false;
        }
        Object this$gold = this.getGold();
        Object other$gold = other.getGold();
        if (!((this$gold != null) ? this$gold.equals(other$gold) : (other$gold == null))) {
            return false;
        }
        Object this$silver = this.getSilver();
        Object other$silver = other.getSilver();
        if (!((this$silver != null) ? this$silver.equals(other$silver) : (other$silver == null))) {
            return false;
        }
        Object this$coin = this.getCoin();
        Object other$coin = other.getCoin();
        if (!((this$coin != null) ? this$coin.equals(other$coin) : (other$coin == null))) {
            return false;
        }
        if (this.getExp() != other.getExp()) {
            return false;
        }
        Object this$shape = this.getShape();
        Object other$shape = other.getShape();
        if (!((this$shape != null) ? this$shape.equals(other$shape) : (other$shape == null))) {
            return false;
        }
        Object this$canMerge = this.getCanMerge();
        Object other$canMerge = other.getCanMerge();
        if (!((this$canMerge != null) ? this$canMerge.equals(other$canMerge) : (other$canMerge == null))) {
            return false;
        }
        Object this$bind = this.getBind();
        Object other$bind = other.getBind();
        if (!((this$bind != null) ? this$bind.equals(other$bind) : (other$bind == null))) {
            return false;
        }
        Object this$needZs = this.getNeedZs();
        Object other$needZs = other.getNeedZs();
        if (!((this$needZs != null) ? this$needZs.equals(other$needZs) : (other$needZs == null))) {
            return false;
        }
        Object this$needLv = this.getNeedLv();
        Object other$needLv = other.getNeedLv();
        if (!((this$needLv != null) ? this$needLv.equals(other$needLv) : (other$needLv == null))) {
            return false;
        }
        if (!Arrays.deepEquals(this.getUseNeedDay(), other.getUseNeedDay())) {
            return false;
        }
        Object this$extra = this.getExtra();
        Object other$extra = other.getExtra();
        if (!((this$extra != null) ? this$extra.equals(other$extra) : (other$extra == null))) {
            return false;
        }
        Object this$cycle = this.getCycle();
        Object other$cycle = other.getCycle();
        if (!((this$cycle != null) ? this$cycle.equals(other$cycle) : (other$cycle == null))) {
            return false;
        }
        Object this$isOpen = this.getIsOpen();
        Object other$isOpen = other.getIsOpen();
        if (!((this$isOpen != null) ? this$isOpen.equals(other$isOpen) : (other$isOpen == null))) {
            return false;
        }
        Object this$param = this.getParam();
        Object other$param = other.getParam();
        return (this$param != null) ? this$param.equals(other$param) : (other$param == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysGiftItem;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $serial = this.getSerial();
        result = result * 59 + (($serial == null) ? 43 : $serial.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        Object $gold = this.getGold();
        result = result * 59 + (($gold == null) ? 43 : $gold.hashCode());
        Object $silver = this.getSilver();
        result = result * 59 + (($silver == null) ? 43 : $silver.hashCode());
        Object $coin = this.getCoin();
        result = result * 59 + (($coin == null) ? 43 : $coin.hashCode());
        result = result * 59 + this.getExp();
        Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        Object $bind = this.getBind();
        result = result * 59 + (($bind == null) ? 43 : $bind.hashCode());
        Object $needZs = this.getNeedZs();
        result = result * 59 + (($needZs == null) ? 43 : $needZs.hashCode());
        Object $needLv = this.getNeedLv();
        result = result * 59 + (($needLv == null) ? 43 : $needLv.hashCode());
        result = result * 59 + Arrays.deepHashCode(this.getUseNeedDay());
        Object $extra = this.getExtra();
        result = result * 59 + (($extra == null) ? 43 : $extra.hashCode());
        Object $cycle = this.getCycle();
        result = result * 59 + (($cycle == null) ? 43 : $cycle.hashCode());
        Object $isOpen = this.getIsOpen();
        result = result * 59 + (($isOpen == null) ? 43 : $isOpen.hashCode());
        Object $param = this.getParam();
        result = result * 59 + (($param == null) ? 43 : $param.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysGiftItem(serial=" + this.getSerial() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", gold=" + this.getGold() + ", silver=" + this.getSilver() + ", coin=" + this.getCoin() + ", exp=" + this.getExp() + ", shape=" + this.getShape() + ", canMerge=" + this.getCanMerge() + ", bind=" + this.getBind() + ", needZs=" + this.getNeedZs() + ", needLv=" + this.getNeedLv() + ", useNeedDay=" + Arrays.deepToString(this.getUseNeedDay()) + ", extra=" + this.getExtra() + ", cycle=" + this.getCycle() + ", isOpen=" + this.getIsOpen() + ", param=" + this.getParam() + ")";
    }
}
