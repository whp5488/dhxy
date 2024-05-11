
package com.kitty.system.init.domain;

import java.util.Arrays;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.init.domain.vo.AdviceAddPro;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_pet")
public class SysPet implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("宠物名称")
    @Column("name")
    private String name;
    @Comment("天生技能")
    @Column("skills")
    @Default("[]")
    private List<Integer> skills;
    @Comment("等级类型 1.普通 2.高级 3.特殊 4.神兽 5.珍惜神兽")
    @Column("level_type")
    private Integer levelType;
    @Comment("外观")
    @Column("shape")
    private Integer shape;
    @Comment("高成长")
    @Column("grow_up_h")
    private double growUpH;
    @Comment("成长率")
    @Column("growth_rate")
    @Default("[]")
    private int[] growthRate;
    @Comment("权重数据")
    @Column("weight")
    @Default("[]")
    private int[] Weight;
    @Comment("初始生命值-min")
    @Column("hp_min")
    private Integer hpMin;
    @Comment("初始生命值-max")
    @Column("hp_max")
    private Integer hpMax;
    @Comment("初始法力值-min")
    @Column("mp_min")
    private Integer mpMin;
    @Comment("初始法力值-max")
    @Column("mp_max")
    private Integer mpMax;
    @Comment("初始攻击力-min")
    @Column("ap_min")
    private Integer apMin;
    @Comment("初始攻击力-max")
    @Column("ap_max")
    private Integer apMax;
    @Comment("初始速度-min")
    @Column("sp_min")
    private Integer spMin;
    @Comment("初始速度-max")
    @Column("sp_max")
    private Integer spMax;
    @Comment("五行-金")
    @Column("wx_jin")
    private String wxJin;
    @Comment("五行-木")
    @Column("wx_mu")
    private String wxMu;
    @Comment("五行-水")
    @Column("wx_shui")
    private String wxShui;
    @Comment("五行-火")
    @Column("wx_huo")
    private String wxHuo;
    @Comment("五行-土")
    @Column("wx_tu")
    private String wxTu;
    @Comment("使用的魔法编号")
    @Column("skill_no")
    private Integer skillNo;
    @Comment("宠物类型:1.物理宠物,2.法术宠物")
    @Column("pet_type")
    private Integer petType;
    @Comment("推荐加点")
    @Column("advice_add_pro")
    @Default("[]")
    private List<AdviceAddPro> adviceAddPro;
    @Comment("携带转生")
    @Column("open_zs")
    private Integer openZs;
    @Comment("携带等级")
    @Column("open_lv")
    private Integer openLv;
    @Comment("合成需要的道具")
    @Column("need_item")
    @Default("[]")
    private List<Item> needItem;
    @Comment("是否可合成：0.否，1.是")
    @Column("can_compose")
    private Integer canCompose;
    @Comment("捕捉几率")
    @Column("catch_ratio")
    private String catchRatio;
    @Comment("捕捉的地图bia")
    @Column("catch_map")
    private Integer catchMap;
    @Comment("命中率")
    @Column("pacc")
    private String pacc;
    @Comment("不变的分数")
    @Column("static_init_var_score")
    private Integer staticInitVarScore;
    @Comment("宠物可获取的颜色值")
    @Column("color")
    @Default("[]")
    private List<Byte> color;
    @Comment("觉醒技能")
    @Column("jx_skill")
    private Integer jxSkill;
    @Comment("饰品染色")
    @Column("acc_color")
    private String accColor;
    @Comment("可使用的幻肤道具")
    @Column("brand_new")
    @Default("[]")
    private List<Integer> brandNew;
    private int canMerge;
    
    public SysPet() {
        this.canMerge = 1;
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
    
    public List<Integer> getSkills() {
        return this.skills;
    }
    
    public Integer getLevelType() {
        return this.levelType;
    }
    
    public Integer getShape() {
        return this.shape;
    }
    
    public double getGrowUpH() {
        return this.growUpH;
    }
    
    public int[] getGrowthRate() {
        return this.growthRate;
    }
    
    public int[] getWeight() {
        return this.Weight;
    }
    
    public Integer getHpMin() {
        return this.hpMin;
    }
    
    public Integer getHpMax() {
        return this.hpMax;
    }
    
    public Integer getMpMin() {
        return this.mpMin;
    }
    
    public Integer getMpMax() {
        return this.mpMax;
    }
    
    public Integer getApMin() {
        return this.apMin;
    }
    
    public Integer getApMax() {
        return this.apMax;
    }
    
    public Integer getSpMin() {
        return this.spMin;
    }
    
    public Integer getSpMax() {
        return this.spMax;
    }
    
    public String getWxJin() {
        return this.wxJin;
    }
    
    public String getWxMu() {
        return this.wxMu;
    }
    
    public String getWxShui() {
        return this.wxShui;
    }
    
    public String getWxHuo() {
        return this.wxHuo;
    }
    
    public String getWxTu() {
        return this.wxTu;
    }
    
    public Integer getSkillNo() {
        return this.skillNo;
    }
    
    public Integer getPetType() {
        return this.petType;
    }
    
    public List<AdviceAddPro> getAdviceAddPro() {
        return this.adviceAddPro;
    }
    
    public Integer getOpenZs() {
        return this.openZs;
    }
    
    public Integer getOpenLv() {
        return this.openLv;
    }
    
    public List<Item> getNeedItem() {
        return this.needItem;
    }
    
    public Integer getCanCompose() {
        return this.canCompose;
    }
    
    public String getCatchRatio() {
        return this.catchRatio;
    }
    
    public Integer getCatchMap() {
        return this.catchMap;
    }
    
    public String getPacc() {
        return this.pacc;
    }
    
    public Integer getStaticInitVarScore() {
        return this.staticInitVarScore;
    }
    
    public List<Byte> getColor() {
        return this.color;
    }
    
    public Integer getJxSkill() {
        return this.jxSkill;
    }
    
    public String getAccColor() {
        return this.accColor;
    }
    
    public List<Integer> getBrandNew() {
        return this.brandNew;
    }
    
    public int getCanMerge() {
        return this.canMerge;
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
    
    public void setSkills(final List<Integer> skills) {
        this.skills = skills;
    }
    
    public void setLevelType(final Integer levelType) {
        this.levelType = levelType;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setGrowUpH(final double growUpH) {
        this.growUpH = growUpH;
    }
    
    public void setGrowthRate(final int[] growthRate) {
        this.growthRate = growthRate;
    }
    
    public void setWeight(final int[] Weight) {
        this.Weight = Weight;
    }
    
    public void setHpMin(final Integer hpMin) {
        this.hpMin = hpMin;
    }
    
    public void setHpMax(final Integer hpMax) {
        this.hpMax = hpMax;
    }
    
    public void setMpMin(final Integer mpMin) {
        this.mpMin = mpMin;
    }
    
    public void setMpMax(final Integer mpMax) {
        this.mpMax = mpMax;
    }
    
    public void setApMin(final Integer apMin) {
        this.apMin = apMin;
    }
    
    public void setApMax(final Integer apMax) {
        this.apMax = apMax;
    }
    
    public void setSpMin(final Integer spMin) {
        this.spMin = spMin;
    }
    
    public void setSpMax(final Integer spMax) {
        this.spMax = spMax;
    }
    
    public void setWxJin(final String wxJin) {
        this.wxJin = wxJin;
    }
    
    public void setWxMu(final String wxMu) {
        this.wxMu = wxMu;
    }
    
    public void setWxShui(final String wxShui) {
        this.wxShui = wxShui;
    }
    
    public void setWxHuo(final String wxHuo) {
        this.wxHuo = wxHuo;
    }
    
    public void setWxTu(final String wxTu) {
        this.wxTu = wxTu;
    }
    
    public void setSkillNo(final Integer skillNo) {
        this.skillNo = skillNo;
    }
    
    public void setPetType(final Integer petType) {
        this.petType = petType;
    }
    
    public void setAdviceAddPro(final List<AdviceAddPro> adviceAddPro) {
        this.adviceAddPro = adviceAddPro;
    }
    
    public void setOpenZs(final Integer openZs) {
        this.openZs = openZs;
    }
    
    public void setOpenLv(final Integer openLv) {
        this.openLv = openLv;
    }
    
    public void setNeedItem(final List<Item> needItem) {
        this.needItem = needItem;
    }
    
    public void setCanCompose(final Integer canCompose) {
        this.canCompose = canCompose;
    }
    
    public void setCatchRatio(final String catchRatio) {
        this.catchRatio = catchRatio;
    }
    
    public void setCatchMap(final Integer catchMap) {
        this.catchMap = catchMap;
    }
    
    public void setPacc(final String pacc) {
        this.pacc = pacc;
    }
    
    public void setStaticInitVarScore(final Integer staticInitVarScore) {
        this.staticInitVarScore = staticInitVarScore;
    }
    
    public void setColor(final List<Byte> color) {
        this.color = color;
    }
    
    public void setJxSkill(final Integer jxSkill) {
        this.jxSkill = jxSkill;
    }
    
    public void setAccColor(final String accColor) {
        this.accColor = accColor;
    }
    
    public void setBrandNew(final List<Integer> brandNew) {
        this.brandNew = brandNew;
    }
    
    public void setCanMerge(final int canMerge) {
        this.canMerge = canMerge;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysPet)) {
            return false;
        }
        final SysPet other = (SysPet)o;
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
        final Object this$skills = this.getSkills();
        final Object other$skills = other.getSkills();
        Label_0176: {
            if (this$skills == null) {
                if (other$skills == null) {
                    break Label_0176;
                }
            }
            else if (this$skills.equals(other$skills)) {
                break Label_0176;
            }
            return false;
        }
        final Object this$levelType = this.getLevelType();
        final Object other$levelType = other.getLevelType();
        Label_0213: {
            if (this$levelType == null) {
                if (other$levelType == null) {
                    break Label_0213;
                }
            }
            else if (this$levelType.equals(other$levelType)) {
                break Label_0213;
            }
            return false;
        }
        final Object this$shape = this.getShape();
        final Object other$shape = other.getShape();
        Label_0250: {
            if (this$shape == null) {
                if (other$shape == null) {
                    break Label_0250;
                }
            }
            else if (this$shape.equals(other$shape)) {
                break Label_0250;
            }
            return false;
        }
        if (Double.compare(this.getGrowUpH(), other.getGrowUpH()) != 0) {
            return false;
        }
        if (!Arrays.equals(this.getGrowthRate(), other.getGrowthRate())) {
            return false;
        }
        if (!Arrays.equals(this.getWeight(), other.getWeight())) {
            return false;
        }
        final Object this$hpMin = this.getHpMin();
        final Object other$hpMin = other.getHpMin();
        Label_0335: {
            if (this$hpMin == null) {
                if (other$hpMin == null) {
                    break Label_0335;
                }
            }
            else if (this$hpMin.equals(other$hpMin)) {
                break Label_0335;
            }
            return false;
        }
        final Object this$hpMax = this.getHpMax();
        final Object other$hpMax = other.getHpMax();
        Label_0372: {
            if (this$hpMax == null) {
                if (other$hpMax == null) {
                    break Label_0372;
                }
            }
            else if (this$hpMax.equals(other$hpMax)) {
                break Label_0372;
            }
            return false;
        }
        final Object this$mpMin = this.getMpMin();
        final Object other$mpMin = other.getMpMin();
        Label_0409: {
            if (this$mpMin == null) {
                if (other$mpMin == null) {
                    break Label_0409;
                }
            }
            else if (this$mpMin.equals(other$mpMin)) {
                break Label_0409;
            }
            return false;
        }
        final Object this$mpMax = this.getMpMax();
        final Object other$mpMax = other.getMpMax();
        Label_0446: {
            if (this$mpMax == null) {
                if (other$mpMax == null) {
                    break Label_0446;
                }
            }
            else if (this$mpMax.equals(other$mpMax)) {
                break Label_0446;
            }
            return false;
        }
        final Object this$apMin = this.getApMin();
        final Object other$apMin = other.getApMin();
        Label_0483: {
            if (this$apMin == null) {
                if (other$apMin == null) {
                    break Label_0483;
                }
            }
            else if (this$apMin.equals(other$apMin)) {
                break Label_0483;
            }
            return false;
        }
        final Object this$apMax = this.getApMax();
        final Object other$apMax = other.getApMax();
        Label_0520: {
            if (this$apMax == null) {
                if (other$apMax == null) {
                    break Label_0520;
                }
            }
            else if (this$apMax.equals(other$apMax)) {
                break Label_0520;
            }
            return false;
        }
        final Object this$spMin = this.getSpMin();
        final Object other$spMin = other.getSpMin();
        Label_0557: {
            if (this$spMin == null) {
                if (other$spMin == null) {
                    break Label_0557;
                }
            }
            else if (this$spMin.equals(other$spMin)) {
                break Label_0557;
            }
            return false;
        }
        final Object this$spMax = this.getSpMax();
        final Object other$spMax = other.getSpMax();
        Label_0594: {
            if (this$spMax == null) {
                if (other$spMax == null) {
                    break Label_0594;
                }
            }
            else if (this$spMax.equals(other$spMax)) {
                break Label_0594;
            }
            return false;
        }
        final Object this$wxJin = this.getWxJin();
        final Object other$wxJin = other.getWxJin();
        Label_0631: {
            if (this$wxJin == null) {
                if (other$wxJin == null) {
                    break Label_0631;
                }
            }
            else if (this$wxJin.equals(other$wxJin)) {
                break Label_0631;
            }
            return false;
        }
        final Object this$wxMu = this.getWxMu();
        final Object other$wxMu = other.getWxMu();
        Label_0668: {
            if (this$wxMu == null) {
                if (other$wxMu == null) {
                    break Label_0668;
                }
            }
            else if (this$wxMu.equals(other$wxMu)) {
                break Label_0668;
            }
            return false;
        }
        final Object this$wxShui = this.getWxShui();
        final Object other$wxShui = other.getWxShui();
        Label_0705: {
            if (this$wxShui == null) {
                if (other$wxShui == null) {
                    break Label_0705;
                }
            }
            else if (this$wxShui.equals(other$wxShui)) {
                break Label_0705;
            }
            return false;
        }
        final Object this$wxHuo = this.getWxHuo();
        final Object other$wxHuo = other.getWxHuo();
        Label_0742: {
            if (this$wxHuo == null) {
                if (other$wxHuo == null) {
                    break Label_0742;
                }
            }
            else if (this$wxHuo.equals(other$wxHuo)) {
                break Label_0742;
            }
            return false;
        }
        final Object this$wxTu = this.getWxTu();
        final Object other$wxTu = other.getWxTu();
        Label_0779: {
            if (this$wxTu == null) {
                if (other$wxTu == null) {
                    break Label_0779;
                }
            }
            else if (this$wxTu.equals(other$wxTu)) {
                break Label_0779;
            }
            return false;
        }
        final Object this$skillNo = this.getSkillNo();
        final Object other$skillNo = other.getSkillNo();
        Label_0816: {
            if (this$skillNo == null) {
                if (other$skillNo == null) {
                    break Label_0816;
                }
            }
            else if (this$skillNo.equals(other$skillNo)) {
                break Label_0816;
            }
            return false;
        }
        final Object this$petType = this.getPetType();
        final Object other$petType = other.getPetType();
        Label_0853: {
            if (this$petType == null) {
                if (other$petType == null) {
                    break Label_0853;
                }
            }
            else if (this$petType.equals(other$petType)) {
                break Label_0853;
            }
            return false;
        }
        final Object this$adviceAddPro = this.getAdviceAddPro();
        final Object other$adviceAddPro = other.getAdviceAddPro();
        Label_0890: {
            if (this$adviceAddPro == null) {
                if (other$adviceAddPro == null) {
                    break Label_0890;
                }
            }
            else if (this$adviceAddPro.equals(other$adviceAddPro)) {
                break Label_0890;
            }
            return false;
        }
        final Object this$openZs = this.getOpenZs();
        final Object other$openZs = other.getOpenZs();
        Label_0927: {
            if (this$openZs == null) {
                if (other$openZs == null) {
                    break Label_0927;
                }
            }
            else if (this$openZs.equals(other$openZs)) {
                break Label_0927;
            }
            return false;
        }
        final Object this$openLv = this.getOpenLv();
        final Object other$openLv = other.getOpenLv();
        Label_0964: {
            if (this$openLv == null) {
                if (other$openLv == null) {
                    break Label_0964;
                }
            }
            else if (this$openLv.equals(other$openLv)) {
                break Label_0964;
            }
            return false;
        }
        final Object this$needItem = this.getNeedItem();
        final Object other$needItem = other.getNeedItem();
        Label_1001: {
            if (this$needItem == null) {
                if (other$needItem == null) {
                    break Label_1001;
                }
            }
            else if (this$needItem.equals(other$needItem)) {
                break Label_1001;
            }
            return false;
        }
        final Object this$canCompose = this.getCanCompose();
        final Object other$canCompose = other.getCanCompose();
        Label_1038: {
            if (this$canCompose == null) {
                if (other$canCompose == null) {
                    break Label_1038;
                }
            }
            else if (this$canCompose.equals(other$canCompose)) {
                break Label_1038;
            }
            return false;
        }
        final Object this$catchRatio = this.getCatchRatio();
        final Object other$catchRatio = other.getCatchRatio();
        Label_1075: {
            if (this$catchRatio == null) {
                if (other$catchRatio == null) {
                    break Label_1075;
                }
            }
            else if (this$catchRatio.equals(other$catchRatio)) {
                break Label_1075;
            }
            return false;
        }
        final Object this$catchMap = this.getCatchMap();
        final Object other$catchMap = other.getCatchMap();
        Label_1112: {
            if (this$catchMap == null) {
                if (other$catchMap == null) {
                    break Label_1112;
                }
            }
            else if (this$catchMap.equals(other$catchMap)) {
                break Label_1112;
            }
            return false;
        }
        final Object this$pacc = this.getPacc();
        final Object other$pacc = other.getPacc();
        Label_1149: {
            if (this$pacc == null) {
                if (other$pacc == null) {
                    break Label_1149;
                }
            }
            else if (this$pacc.equals(other$pacc)) {
                break Label_1149;
            }
            return false;
        }
        final Object this$staticInitVarScore = this.getStaticInitVarScore();
        final Object other$staticInitVarScore = other.getStaticInitVarScore();
        Label_1186: {
            if (this$staticInitVarScore == null) {
                if (other$staticInitVarScore == null) {
                    break Label_1186;
                }
            }
            else if (this$staticInitVarScore.equals(other$staticInitVarScore)) {
                break Label_1186;
            }
            return false;
        }
        final Object this$color = this.getColor();
        final Object other$color = other.getColor();
        Label_1223: {
            if (this$color == null) {
                if (other$color == null) {
                    break Label_1223;
                }
            }
            else if (this$color.equals(other$color)) {
                break Label_1223;
            }
            return false;
        }
        final Object this$jxSkill = this.getJxSkill();
        final Object other$jxSkill = other.getJxSkill();
        Label_1260: {
            if (this$jxSkill == null) {
                if (other$jxSkill == null) {
                    break Label_1260;
                }
            }
            else if (this$jxSkill.equals(other$jxSkill)) {
                break Label_1260;
            }
            return false;
        }
        final Object this$accColor = this.getAccColor();
        final Object other$accColor = other.getAccColor();
        Label_1297: {
            if (this$accColor == null) {
                if (other$accColor == null) {
                    break Label_1297;
                }
            }
            else if (this$accColor.equals(other$accColor)) {
                break Label_1297;
            }
            return false;
        }
        final Object this$brandNew = this.getBrandNew();
        final Object other$brandNew = other.getBrandNew();
        if (this$brandNew == null) {
            if (other$brandNew == null) {
                return this.getCanMerge() == other.getCanMerge();
            }
        }
        else if (this$brandNew.equals(other$brandNew)) {
            return this.getCanMerge() == other.getCanMerge();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPet;
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
        final Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        final Object $levelType = this.getLevelType();
        result = result * 59 + (($levelType == null) ? 43 : $levelType.hashCode());
        final Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        final long $growUpH = Double.doubleToLongBits(this.getGrowUpH());
        result = result * 59 + (int)($growUpH >>> 32 ^ $growUpH);
        result = result * 59 + Arrays.hashCode(this.getGrowthRate());
        result = result * 59 + Arrays.hashCode(this.getWeight());
        final Object $hpMin = this.getHpMin();
        result = result * 59 + (($hpMin == null) ? 43 : $hpMin.hashCode());
        final Object $hpMax = this.getHpMax();
        result = result * 59 + (($hpMax == null) ? 43 : $hpMax.hashCode());
        final Object $mpMin = this.getMpMin();
        result = result * 59 + (($mpMin == null) ? 43 : $mpMin.hashCode());
        final Object $mpMax = this.getMpMax();
        result = result * 59 + (($mpMax == null) ? 43 : $mpMax.hashCode());
        final Object $apMin = this.getApMin();
        result = result * 59 + (($apMin == null) ? 43 : $apMin.hashCode());
        final Object $apMax = this.getApMax();
        result = result * 59 + (($apMax == null) ? 43 : $apMax.hashCode());
        final Object $spMin = this.getSpMin();
        result = result * 59 + (($spMin == null) ? 43 : $spMin.hashCode());
        final Object $spMax = this.getSpMax();
        result = result * 59 + (($spMax == null) ? 43 : $spMax.hashCode());
        final Object $wxJin = this.getWxJin();
        result = result * 59 + (($wxJin == null) ? 43 : $wxJin.hashCode());
        final Object $wxMu = this.getWxMu();
        result = result * 59 + (($wxMu == null) ? 43 : $wxMu.hashCode());
        final Object $wxShui = this.getWxShui();
        result = result * 59 + (($wxShui == null) ? 43 : $wxShui.hashCode());
        final Object $wxHuo = this.getWxHuo();
        result = result * 59 + (($wxHuo == null) ? 43 : $wxHuo.hashCode());
        final Object $wxTu = this.getWxTu();
        result = result * 59 + (($wxTu == null) ? 43 : $wxTu.hashCode());
        final Object $skillNo = this.getSkillNo();
        result = result * 59 + (($skillNo == null) ? 43 : $skillNo.hashCode());
        final Object $petType = this.getPetType();
        result = result * 59 + (($petType == null) ? 43 : $petType.hashCode());
        final Object $adviceAddPro = this.getAdviceAddPro();
        result = result * 59 + (($adviceAddPro == null) ? 43 : $adviceAddPro.hashCode());
        final Object $openZs = this.getOpenZs();
        result = result * 59 + (($openZs == null) ? 43 : $openZs.hashCode());
        final Object $openLv = this.getOpenLv();
        result = result * 59 + (($openLv == null) ? 43 : $openLv.hashCode());
        final Object $needItem = this.getNeedItem();
        result = result * 59 + (($needItem == null) ? 43 : $needItem.hashCode());
        final Object $canCompose = this.getCanCompose();
        result = result * 59 + (($canCompose == null) ? 43 : $canCompose.hashCode());
        final Object $catchRatio = this.getCatchRatio();
        result = result * 59 + (($catchRatio == null) ? 43 : $catchRatio.hashCode());
        final Object $catchMap = this.getCatchMap();
        result = result * 59 + (($catchMap == null) ? 43 : $catchMap.hashCode());
        final Object $pacc = this.getPacc();
        result = result * 59 + (($pacc == null) ? 43 : $pacc.hashCode());
        final Object $staticInitVarScore = this.getStaticInitVarScore();
        result = result * 59 + (($staticInitVarScore == null) ? 43 : $staticInitVarScore.hashCode());
        final Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        final Object $jxSkill = this.getJxSkill();
        result = result * 59 + (($jxSkill == null) ? 43 : $jxSkill.hashCode());
        final Object $accColor = this.getAccColor();
        result = result * 59 + (($accColor == null) ? 43 : $accColor.hashCode());
        final Object $brandNew = this.getBrandNew();
        result = result * 59 + (($brandNew == null) ? 43 : $brandNew.hashCode());
        result = result * 59 + this.getCanMerge();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPet(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", skills=" + this.getSkills() + ", levelType=" + this.getLevelType() + ", shape=" + this.getShape() + ", growUpH=" + this.getGrowUpH() + ", growthRate=" + Arrays.toString(this.getGrowthRate()) + ", Weight=" + Arrays.toString(this.getWeight()) + ", hpMin=" + this.getHpMin() + ", hpMax=" + this.getHpMax() + ", mpMin=" + this.getMpMin() + ", mpMax=" + this.getMpMax() + ", apMin=" + this.getApMin() + ", apMax=" + this.getApMax() + ", spMin=" + this.getSpMin() + ", spMax=" + this.getSpMax() + ", wxJin=" + this.getWxJin() + ", wxMu=" + this.getWxMu() + ", wxShui=" + this.getWxShui() + ", wxHuo=" + this.getWxHuo() + ", wxTu=" + this.getWxTu() + ", skillNo=" + this.getSkillNo() + ", petType=" + this.getPetType() + ", adviceAddPro=" + this.getAdviceAddPro() + ", openZs=" + this.getOpenZs() + ", openLv=" + this.getOpenLv() + ", needItem=" + this.getNeedItem() + ", canCompose=" + this.getCanCompose() + ", catchRatio=" + this.getCatchRatio() + ", catchMap=" + this.getCatchMap() + ", pacc=" + this.getPacc() + ", staticInitVarScore=" + this.getStaticInitVarScore() + ", color=" + this.getColor() + ", jxSkill=" + this.getJxSkill() + ", accColor=" + this.getAccColor() + ", brandNew=" + this.getBrandNew() + ", canMerge=" + this.getCanMerge() + ")";
    }
}
