package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_shop_npc_drug")
public class SysShopNpcDrug implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("名称")
    @Column("name")
    private String name;
    @Comment("数量")
    @Column("num")
    private Integer num;
    @Comment("价格")
    @Column("price")
    private Integer price;
    @Comment("等级限制")
    @Column("lv_limit")
    private Integer lvLimit;
    @Comment("转生限制")
    @Column("zs_limit")
    private Integer zsLimit;
    @Comment("消费类型")
    @Column("type")
    private Integer type;
    @Comment("折扣")
    @Column("discount")
    private Integer discount;
    
    public SysShopNpcDrug() {
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
    
    public Integer getNum() {
        return this.num;
    }
    
    public Integer getPrice() {
        return this.price;
    }
    
    public Integer getLvLimit() {
        return this.lvLimit;
    }
    
    public Integer getZsLimit() {
        return this.zsLimit;
    }
    
    public Integer getType() {
        return this.type;
    }
    
    public Integer getDiscount() {
        return this.discount;
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
    
    public void setNum(final Integer num) {
        this.num = num;
    }
    
    public void setPrice(final Integer price) {
        this.price = price;
    }
    
    public void setLvLimit(final Integer lvLimit) {
        this.lvLimit = lvLimit;
    }
    
    public void setZsLimit(final Integer zsLimit) {
        this.zsLimit = zsLimit;
    }
    
    public void setType(final Integer type) {
        this.type = type;
    }
    
    public void setDiscount(final Integer discount) {
        this.discount = discount;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysShopNpcDrug)) {
            return false;
        }
        SysShopNpcDrug other = (SysShopNpcDrug)o;
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
        Object this$num = this.getNum();
        Object other$num = other.getNum();
        if (!((this$num != null) ? this$num.equals(other$num) : (other$num == null))) {
            return false;
        }
        Object this$price = this.getPrice();
        Object other$price = other.getPrice();
        if (!((this$price != null) ? this$price.equals(other$price) : (other$price == null))) {
            return false;
        }
        Object this$lvLimit = this.getLvLimit();
        Object other$lvLimit = other.getLvLimit();
        if (!((this$lvLimit != null) ? this$lvLimit.equals(other$lvLimit) : (other$lvLimit == null))) {
            return false;
        }
        Object this$zsLimit = this.getZsLimit();
        Object other$zsLimit = other.getZsLimit();
        if (!((this$zsLimit != null) ? this$zsLimit.equals(other$zsLimit) : (other$zsLimit == null))) {
            return false;
        }
        Object this$type = this.getType();
        Object other$type = other.getType();
        if (!((this$type != null) ? this$type.equals(other$type) : (other$type == null))) {
            return false;
        }
        Object this$discount = this.getDiscount();
        Object other$discount = other.getDiscount();
        return (this$discount != null) ? this$discount.equals(other$discount) : (other$discount == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysShopNpcDrug;
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
        Object $num = this.getNum();
        result = result * 59 + (($num == null) ? 43 : $num.hashCode());
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        Object $lvLimit = this.getLvLimit();
        result = result * 59 + (($lvLimit == null) ? 43 : $lvLimit.hashCode());
        Object $zsLimit = this.getZsLimit();
        result = result * 59 + (($zsLimit == null) ? 43 : $zsLimit.hashCode());
        Object $type = this.getType();
        result = result * 59 + (($type == null) ? 43 : $type.hashCode());
        Object $discount = this.getDiscount();
        result = result * 59 + (($discount == null) ? 43 : $discount.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysShopNpcDrug(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", num=" + this.getNum() + ", price=" + this.getPrice() + ", lvLimit=" + this.getLvLimit() + ", zsLimit=" + this.getZsLimit() + ", type=" + this.getType() + ", discount=" + this.getDiscount() + ")";
    }
}
