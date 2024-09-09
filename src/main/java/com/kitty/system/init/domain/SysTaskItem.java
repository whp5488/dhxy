package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("t_task_item")
public class SysTaskItem implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("道具id")
    @Column("item_id")
    private Integer itemId;
    @Comment("道具名称")
    @Column("name")
    private String name;
    @Comment("价格")
    @Column("price")
    private Integer price;
    @Comment("重新定价")
    @Column("reprice")
    private Integer reprice;
    @Comment("道具形状")
    @Column("itemShape")
    private Integer itemshape;
    @Comment("背包重叠数量")
    @Column("canMerge")
    private Integer canMerge;
    @Comment("提示")
    @Column("tips")
    private String tips;
    @Comment("描述")
    @Column("des")
    private String des;
    @Comment("道具品阶")
    @Column("itemPJ")
    private Integer itempj;
    @Comment("效果id")
    @Column("xiaoguoID")
    private String xiaoguoid;
    @Comment("次数")
    @Column("cishu")
    private Integer cishu;
    @Comment("同步")
    @Column("tongbu")
    private Integer tongbu;
    @Comment("类型")
    @Column("leix")
    private Integer leix;
    @Comment("位置")
    @Column("weizhi")
    private String weizhi;
    
    public SysTaskItem() {
    }
    
    public Integer getItemId() {
        return this.itemId;
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
    
    public Integer getItemshape() {
        return this.itemshape;
    }
    
    public Integer getCanMerge() {
        return this.canMerge;
    }
    
    public String getTips() {
        return this.tips;
    }
    
    public String getDes() {
        return this.des;
    }
    
    public Integer getItempj() {
        return this.itempj;
    }
    
    public String getXiaoguoid() {
        return this.xiaoguoid;
    }
    
    public Integer getCishu() {
        return this.cishu;
    }
    
    public Integer getTongbu() {
        return this.tongbu;
    }
    
    public Integer getLeix() {
        return this.leix;
    }
    
    public String getWeizhi() {
        return this.weizhi;
    }
    
    public void setItemId(final Integer itemId) {
        this.itemId = itemId;
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
    
    public void setItemshape(final Integer itemshape) {
        this.itemshape = itemshape;
    }
    
    public void setCanMerge(final Integer canMerge) {
        this.canMerge = canMerge;
    }
    
    public void setTips(final String tips) {
        this.tips = tips;
    }
    
    public void setDes(final String des) {
        this.des = des;
    }
    
    public void setItempj(final Integer itempj) {
        this.itempj = itempj;
    }
    
    public void setXiaoguoid(final String xiaoguoid) {
        this.xiaoguoid = xiaoguoid;
    }
    
    public void setCishu(final Integer cishu) {
        this.cishu = cishu;
    }
    
    public void setTongbu(final Integer tongbu) {
        this.tongbu = tongbu;
    }
    
    public void setLeix(final Integer leix) {
        this.leix = leix;
    }
    
    public void setWeizhi(final String weizhi) {
        this.weizhi = weizhi;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysTaskItem)) {
            return false;
        }
        SysTaskItem other = (SysTaskItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        Object this$itemId = this.getItemId();
        Object other$itemId = other.getItemId();
        if (!((this$itemId != null) ? this$itemId.equals(other$itemId) : (other$itemId == null))) {
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
        Object this$itemshape = this.getItemshape();
        Object other$itemshape = other.getItemshape();
        if (!((this$itemshape != null) ? this$itemshape.equals(other$itemshape) : (other$itemshape == null))) {
            return false;
        }
        Object this$canMerge = this.getCanMerge();
        Object other$canMerge = other.getCanMerge();
        if (!((this$canMerge != null) ? this$canMerge.equals(other$canMerge) : (other$canMerge == null))) {
            return false;
        }
        Object this$tips = this.getTips();
        Object other$tips = other.getTips();
        if (!((this$tips != null) ? this$tips.equals(other$tips) : (other$tips == null))) {
            return false;
        }
        Object this$des = this.getDes();
        Object other$des = other.getDes();
        if (!((this$des != null) ? this$des.equals(other$des) : (other$des == null))) {
            return false;
        }
        Object this$itempj = this.getItempj();
        Object other$itempj = other.getItempj();
        if (!((this$itempj != null) ? this$itempj.equals(other$itempj) : (other$itempj == null))) {
            return false;
        }
        Object this$xiaoguoid = this.getXiaoguoid();
        Object other$xiaoguoid = other.getXiaoguoid();
        if (!((this$xiaoguoid != null) ? this$xiaoguoid.equals(other$xiaoguoid) : (other$xiaoguoid == null))) {
            return false;
        }
        Object this$cishu = this.getCishu();
        Object other$cishu = other.getCishu();
        if (!((this$cishu != null) ? this$cishu.equals(other$cishu) : (other$cishu == null))) {
            return false;
        }
        Object this$tongbu = this.getTongbu();
        Object other$tongbu = other.getTongbu();
        if (!((this$tongbu != null) ? this$tongbu.equals(other$tongbu) : (other$tongbu == null))) {
            return false;
        }
        Object this$leix = this.getLeix();
        Object other$leix = other.getLeix();
        if (!((this$leix != null) ? this$leix.equals(other$leix) : (other$leix == null))) {
            return false;
        }
        Object this$weizhi = this.getWeizhi();
        Object other$weizhi = other.getWeizhi();
        return (this$weizhi != null) ? this$weizhi.equals(other$weizhi) : (other$weizhi == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysTaskItem;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        Object $itemId = this.getItemId();
        result = result * 59 + (($itemId == null) ? 43 : $itemId.hashCode());
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        Object $itemshape = this.getItemshape();
        result = result * 59 + (($itemshape == null) ? 43 : $itemshape.hashCode());
        Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        Object $des = this.getDes();
        result = result * 59 + (($des == null) ? 43 : $des.hashCode());
        Object $itempj = this.getItempj();
        result = result * 59 + (($itempj == null) ? 43 : $itempj.hashCode());
        Object $xiaoguoid = this.getXiaoguoid();
        result = result * 59 + (($xiaoguoid == null) ? 43 : $xiaoguoid.hashCode());
        Object $cishu = this.getCishu();
        result = result * 59 + (($cishu == null) ? 43 : $cishu.hashCode());
        Object $tongbu = this.getTongbu();
        result = result * 59 + (($tongbu == null) ? 43 : $tongbu.hashCode());
        Object $leix = this.getLeix();
        result = result * 59 + (($leix == null) ? 43 : $leix.hashCode());
        Object $weizhi = this.getWeizhi();
        result = result * 59 + (($weizhi == null) ? 43 : $weizhi.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysTaskItem(itemId=" + this.getItemId() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", itemshape=" + this.getItemshape() + ", canMerge=" + this.getCanMerge() + ", tips=" + this.getTips() + ", des=" + this.getDes() + ", itempj=" + this.getItempj() + ", xiaoguoid=" + this.getXiaoguoid() + ", cishu=" + this.getCishu() + ", tongbu=" + this.getTongbu() + ", leix=" + this.getLeix() + ", weizhi=" + this.getWeizhi() + ")";
    }
}
