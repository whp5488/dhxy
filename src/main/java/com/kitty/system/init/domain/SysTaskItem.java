
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
        final SysTaskItem other = (SysTaskItem)o;
        if (!other.canEqual(this)) {
            return false;
        }
        final Object this$itemId = this.getItemId();
        final Object other$itemId = other.getItemId();
        Label_0065: {
            if (this$itemId == null) {
                if (other$itemId == null) {
                    break Label_0065;
                }
            }
            else if (this$itemId.equals(other$itemId)) {
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
        final Object this$itemshape = this.getItemshape();
        final Object other$itemshape = other.getItemshape();
        Label_0213: {
            if (this$itemshape == null) {
                if (other$itemshape == null) {
                    break Label_0213;
                }
            }
            else if (this$itemshape.equals(other$itemshape)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$canMerge = this.getCanMerge();
        final Object other$canMerge = other.getCanMerge();
        Label_0250: {
            if (this$canMerge == null) {
                if (other$canMerge == null) {
                    break Label_0250;
                }
            }
            else if (this$canMerge.equals(other$canMerge)) {
                break Label_0250;
            }
            return false;
        }
        final Object this$tips = this.getTips();
        final Object other$tips = other.getTips();
        Label_0287: {
            if (this$tips == null) {
                if (other$tips == null) {
                    break Label_0287;
                }
            }
            else if (this$tips.equals(other$tips)) {
                break Label_0287;
            }
            return false;
        }
        final Object this$des = this.getDes();
        final Object other$des = other.getDes();
        Label_0324: {
            if (this$des == null) {
                if (other$des == null) {
                    break Label_0324;
                }
            }
            else if (this$des.equals(other$des)) {
                break Label_0324;
            }
            return false;
        }
        final Object this$itempj = this.getItempj();
        final Object other$itempj = other.getItempj();
        Label_0361: {
            if (this$itempj == null) {
                if (other$itempj == null) {
                    break Label_0361;
                }
            }
            else if (this$itempj.equals(other$itempj)) {
                break Label_0361;
            }
            return false;
        }
        final Object this$xiaoguoid = this.getXiaoguoid();
        final Object other$xiaoguoid = other.getXiaoguoid();
        Label_0398: {
            if (this$xiaoguoid == null) {
                if (other$xiaoguoid == null) {
                    break Label_0398;
                }
            }
            else if (this$xiaoguoid.equals(other$xiaoguoid)) {
                break Label_0398;
            }
            return false;
        }
        final Object this$cishu = this.getCishu();
        final Object other$cishu = other.getCishu();
        Label_0435: {
            if (this$cishu == null) {
                if (other$cishu == null) {
                    break Label_0435;
                }
            }
            else if (this$cishu.equals(other$cishu)) {
                break Label_0435;
            }
            return false;
        }
        final Object this$tongbu = this.getTongbu();
        final Object other$tongbu = other.getTongbu();
        Label_0472: {
            if (this$tongbu == null) {
                if (other$tongbu == null) {
                    break Label_0472;
                }
            }
            else if (this$tongbu.equals(other$tongbu)) {
                break Label_0472;
            }
            return false;
        }
        final Object this$leix = this.getLeix();
        final Object other$leix = other.getLeix();
        Label_0509: {
            if (this$leix == null) {
                if (other$leix == null) {
                    break Label_0509;
                }
            }
            else if (this$leix.equals(other$leix)) {
                break Label_0509;
            }
            return false;
        }
        final Object this$weizhi = this.getWeizhi();
        final Object other$weizhi = other.getWeizhi();
        if (this$weizhi == null) {
            if (other$weizhi == null) {
                return true;
            }
        }
        else if (this$weizhi.equals(other$weizhi)) {
            return true;
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysTaskItem;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $itemId = this.getItemId();
        result = result * 59 + (($itemId == null) ? 43 : $itemId.hashCode());
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $price = this.getPrice();
        result = result * 59 + (($price == null) ? 43 : $price.hashCode());
        final Object $reprice = this.getReprice();
        result = result * 59 + (($reprice == null) ? 43 : $reprice.hashCode());
        final Object $itemshape = this.getItemshape();
        result = result * 59 + (($itemshape == null) ? 43 : $itemshape.hashCode());
        final Object $canMerge = this.getCanMerge();
        result = result * 59 + (($canMerge == null) ? 43 : $canMerge.hashCode());
        final Object $tips = this.getTips();
        result = result * 59 + (($tips == null) ? 43 : $tips.hashCode());
        final Object $des = this.getDes();
        result = result * 59 + (($des == null) ? 43 : $des.hashCode());
        final Object $itempj = this.getItempj();
        result = result * 59 + (($itempj == null) ? 43 : $itempj.hashCode());
        final Object $xiaoguoid = this.getXiaoguoid();
        result = result * 59 + (($xiaoguoid == null) ? 43 : $xiaoguoid.hashCode());
        final Object $cishu = this.getCishu();
        result = result * 59 + (($cishu == null) ? 43 : $cishu.hashCode());
        final Object $tongbu = this.getTongbu();
        result = result * 59 + (($tongbu == null) ? 43 : $tongbu.hashCode());
        final Object $leix = this.getLeix();
        result = result * 59 + (($leix == null) ? 43 : $leix.hashCode());
        final Object $weizhi = this.getWeizhi();
        result = result * 59 + (($weizhi == null) ? 43 : $weizhi.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "SysTaskItem(itemId=" + this.getItemId() + ", name=" + this.getName() + ", price=" + this.getPrice() + ", reprice=" + this.getReprice() + ", itemshape=" + this.getItemshape() + ", canMerge=" + this.getCanMerge() + ", tips=" + this.getTips() + ", des=" + this.getDes() + ", itempj=" + this.getItempj() + ", xiaoguoid=" + this.getXiaoguoid() + ", cishu=" + this.getCishu() + ", tongbu=" + this.getTongbu() + ", leix=" + this.getLeix() + ", weizhi=" + this.getWeizhi() + ")";
    }
}
