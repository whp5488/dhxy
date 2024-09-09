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
        SysPet other = (SysPet)o;
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
        Object this$skills = this.getSkills();
        Object other$skills = other.getSkills();
        if (!((this$skills != null) ? this$skills.equals(other$skills) : (other$skills == null))) {
            return false;
        }
        Object this$levelType = this.getLevelType();
        Object other$levelType = other.getLevelType();
        if (!((this$levelType != null) ? this$levelType.equals(other$levelType) : (other$levelType == null))) {
            return false;
        }
        Object this$shape = this.getShape();
        Object other$shape = other.getShape();
        if (!((this$shape != null) ? this$shape.equals(other$shape) : (other$shape == null))) {
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
        Object this$hpMin = this.getHpMin();
        Object other$hpMin = other.getHpMin();
        if (!((this$hpMin != null) ? this$hpMin.equals(other$hpMin) : (other$hpMin == null))) {
            return false;
        }
        Object this$hpMax = this.getHpMax();
        Object other$hpMax = other.getHpMax();
        if (!((this$hpMax != null) ? this$hpMax.equals(other$hpMax) : (other$hpMax == null))) {
            return false;
        }
        Object this$mpMin = this.getMpMin();
        Object other$mpMin = other.getMpMin();
        if (!((this$mpMin != null) ? this$mpMin.equals(other$mpMin) : (other$mpMin == null))) {
            return false;
        }
        Object this$mpMax = this.getMpMax();
        Object other$mpMax = other.getMpMax();
        if (!((this$mpMax != null) ? this$mpMax.equals(other$mpMax) : (other$mpMax == null))) {
            return false;
        }
        Object this$apMin = this.getApMin();
        Object other$apMin = other.getApMin();
        if (!((this$apMin != null) ? this$apMin.equals(other$apMin) : (other$apMin == null))) {
            return false;
        }
        Object this$apMax = this.getApMax();
        Object other$apMax = other.getApMax();
        if (!((this$apMax != null) ? this$apMax.equals(other$apMax) : (other$apMax == null))) {
            return false;
        }
        Object this$spMin = this.getSpMin();
        Object other$spMin = other.getSpMin();
        if (!((this$spMin != null) ? this$spMin.equals(other$spMin) : (other$spMin == null))) {
            return false;
        }
        Object this$spMax = this.getSpMax();
        Object other$spMax = other.getSpMax();
        if (!((this$spMax != null) ? this$spMax.equals(other$spMax) : (other$spMax == null))) {
            return false;
        }
        Object this$wxJin = this.getWxJin();
        Object other$wxJin = other.getWxJin();
        if (!((this$wxJin != null) ? this$wxJin.equals(other$wxJin) : (other$wxJin == null))) {
            return false;
        }
        Object this$wxMu = this.getWxMu();
        Object other$wxMu = other.getWxMu();
        if (!((this$wxMu != null) ? this$wxMu.equals(other$wxMu) : (other$wxMu == null))) {
            return false;
        }
        Object this$wxShui = this.getWxShui();
        Object other$wxShui = other.getWxShui();
        if (!((this$wxShui != null) ? this$wxShui.equals(other$wxShui) : (other$wxShui == null))) {
            return false;
        }
        Object this$wxHuo = this.getWxHuo();
        Object other$wxHuo = other.getWxHuo();
        if (!((this$wxHuo != null) ? this$wxHuo.equals(other$wxHuo) : (other$wxHuo == null))) {
            return false;
        }
        Object this$wxTu = this.getWxTu();
        Object other$wxTu = other.getWxTu();
        if (!((this$wxTu != null) ? this$wxTu.equals(other$wxTu) : (other$wxTu == null))) {
            return false;
        }
        Object this$skillNo = this.getSkillNo();
        Object other$skillNo = other.getSkillNo();
        if (!((this$skillNo != null) ? this$skillNo.equals(other$skillNo) : (other$skillNo == null))) {
            return false;
        }
        Object this$petType = this.getPetType();
        Object other$petType = other.getPetType();
        if (!((this$petType != null) ? this$petType.equals(other$petType) : (other$petType == null))) {
            return false;
        }
        Object this$adviceAddPro = this.getAdviceAddPro();
        Object other$adviceAddPro = other.getAdviceAddPro();
        if (!((this$adviceAddPro != null) ? this$adviceAddPro.equals(other$adviceAddPro) : (other$adviceAddPro == null))) {
            return false;
        }
        Object this$openZs = this.getOpenZs();
        Object other$openZs = other.getOpenZs();
        if (!((this$openZs != null) ? this$openZs.equals(other$openZs) : (other$openZs == null))) {
            return false;
        }
        Object this$openLv = this.getOpenLv();
        Object other$openLv = other.getOpenLv();
        if (!((this$openLv != null) ? this$openLv.equals(other$openLv) : (other$openLv == null))) {
            return false;
        }
        Object this$needItem = this.getNeedItem();
        Object other$needItem = other.getNeedItem();
        if (!((this$needItem != null) ? this$needItem.equals(other$needItem) : (other$needItem == null))) {
            return false;
        }
        Object this$canCompose = this.getCanCompose();
        Object other$canCompose = other.getCanCompose();
        if (!((this$canCompose != null) ? this$canCompose.equals(other$canCompose) : (other$canCompose == null))) {
            return false;
        }
        Object this$catchRatio = this.getCatchRatio();
        Object other$catchRatio = other.getCatchRatio();
        if (!((this$catchRatio != null) ? this$catchRatio.equals(other$catchRatio) : (other$catchRatio == null))) {
            return false;
        }
        Object this$catchMap = this.getCatchMap();
        Object other$catchMap = other.getCatchMap();
        if (!((this$catchMap != null) ? this$catchMap.equals(other$catchMap) : (other$catchMap == null))) {
            return false;
        }
        Object this$pacc = this.getPacc();
        Object other$pacc = other.getPacc();
        if (!((this$pacc != null) ? this$pacc.equals(other$pacc) : (other$pacc == null))) {
            return false;
        }
        Object this$staticInitVarScore = this.getStaticInitVarScore();
        Object other$staticInitVarScore = other.getStaticInitVarScore();
        if (!((this$staticInitVarScore != null) ? this$staticInitVarScore.equals(other$staticInitVarScore) : (other$staticInitVarScore == null))) {
            return false;
        }
        Object this$color = this.getColor();
        Object other$color = other.getColor();
        if (!((this$color != null) ? this$color.equals(other$color) : (other$color == null))) {
            return false;
        }
        Object this$jxSkill = this.getJxSkill();
        Object other$jxSkill = other.getJxSkill();
        if (!((this$jxSkill != null) ? this$jxSkill.equals(other$jxSkill) : (other$jxSkill == null))) {
            return false;
        }
        Object this$accColor = this.getAccColor();
        Object other$accColor = other.getAccColor();
        if (!((this$accColor != null) ? this$accColor.equals(other$accColor) : (other$accColor == null))) {
            return false;
        }
        Object this$brandNew = this.getBrandNew();
        Object other$brandNew = other.getBrandNew();
        return ((this$brandNew != null) ? this$brandNew.equals(other$brandNew) : (other$brandNew == null)) && this.getCanMerge() == other.getCanMerge();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysPet;
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
        Object $skills = this.getSkills();
        result = result * 59 + (($skills == null) ? 43 : $skills.hashCode());
        Object $levelType = this.getLevelType();
        result = result * 59 + (($levelType == null) ? 43 : $levelType.hashCode());
        Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        long $growUpH = Double.doubleToLongBits(this.getGrowUpH());
        result = result * 59 + (int)($growUpH >>> 32 ^ $growUpH);
        result = result * 59 + Arrays.hashCode(this.getGrowthRate());
        result = result * 59 + Arrays.hashCode(this.getWeight());
        Object $hpMin = this.getHpMin();
        result = result * 59 + (($hpMin == null) ? 43 : $hpMin.hashCode());
        Object $hpMax = this.getHpMax();
        result = result * 59 + (($hpMax == null) ? 43 : $hpMax.hashCode());
        Object $mpMin = this.getMpMin();
        result = result * 59 + (($mpMin == null) ? 43 : $mpMin.hashCode());
        Object $mpMax = this.getMpMax();
        result = result * 59 + (($mpMax == null) ? 43 : $mpMax.hashCode());
        Object $apMin = this.getApMin();
        result = result * 59 + (($apMin == null) ? 43 : $apMin.hashCode());
        Object $apMax = this.getApMax();
        result = result * 59 + (($apMax == null) ? 43 : $apMax.hashCode());
        Object $spMin = this.getSpMin();
        result = result * 59 + (($spMin == null) ? 43 : $spMin.hashCode());
        Object $spMax = this.getSpMax();
        result = result * 59 + (($spMax == null) ? 43 : $spMax.hashCode());
        Object $wxJin = this.getWxJin();
        result = result * 59 + (($wxJin == null) ? 43 : $wxJin.hashCode());
        Object $wxMu = this.getWxMu();
        result = result * 59 + (($wxMu == null) ? 43 : $wxMu.hashCode());
        Object $wxShui = this.getWxShui();
        result = result * 59 + (($wxShui == null) ? 43 : $wxShui.hashCode());
        Object $wxHuo = this.getWxHuo();
        result = result * 59 + (($wxHuo == null) ? 43 : $wxHuo.hashCode());
        Object $wxTu = this.getWxTu();
        result = result * 59 + (($wxTu == null) ? 43 : $wxTu.hashCode());
        Object $skillNo = this.getSkillNo();
        result = result * 59 + (($skillNo == null) ? 43 : $skillNo.hashCode());
        Object $petType = this.getPetType();
        result = result * 59 + (($petType == null) ? 43 : $petType.hashCode());
        Object $adviceAddPro = this.getAdviceAddPro();
        result = result * 59 + (($adviceAddPro == null) ? 43 : $adviceAddPro.hashCode());
        Object $openZs = this.getOpenZs();
        result = result * 59 + (($openZs == null) ? 43 : $openZs.hashCode());
        Object $openLv = this.getOpenLv();
        result = result * 59 + (($openLv == null) ? 43 : $openLv.hashCode());
        Object $needItem = this.getNeedItem();
        result = result * 59 + (($needItem == null) ? 43 : $needItem.hashCode());
        Object $canCompose = this.getCanCompose();
        result = result * 59 + (($canCompose == null) ? 43 : $canCompose.hashCode());
        Object $catchRatio = this.getCatchRatio();
        result = result * 59 + (($catchRatio == null) ? 43 : $catchRatio.hashCode());
        Object $catchMap = this.getCatchMap();
        result = result * 59 + (($catchMap == null) ? 43 : $catchMap.hashCode());
        Object $pacc = this.getPacc();
        result = result * 59 + (($pacc == null) ? 43 : $pacc.hashCode());
        Object $staticInitVarScore = this.getStaticInitVarScore();
        result = result * 59 + (($staticInitVarScore == null) ? 43 : $staticInitVarScore.hashCode());
        Object $color = this.getColor();
        result = result * 59 + (($color == null) ? 43 : $color.hashCode());
        Object $jxSkill = this.getJxSkill();
        result = result * 59 + (($jxSkill == null) ? 43 : $jxSkill.hashCode());
        Object $accColor = this.getAccColor();
        result = result * 59 + (($accColor == null) ? 43 : $accColor.hashCode());
        Object $brandNew = this.getBrandNew();
        result = result * 59 + (($brandNew == null) ? 43 : $brandNew.hashCode());
        result = result * 59 + this.getCanMerge();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysPet(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", skills=" + this.getSkills() + ", levelType=" + this.getLevelType() + ", shape=" + this.getShape() + ", growUpH=" + this.getGrowUpH() + ", growthRate=" + Arrays.toString(this.getGrowthRate()) + ", Weight=" + Arrays.toString(this.getWeight()) + ", hpMin=" + this.getHpMin() + ", hpMax=" + this.getHpMax() + ", mpMin=" + this.getMpMin() + ", mpMax=" + this.getMpMax() + ", apMin=" + this.getApMin() + ", apMax=" + this.getApMax() + ", spMin=" + this.getSpMin() + ", spMax=" + this.getSpMax() + ", wxJin=" + this.getWxJin() + ", wxMu=" + this.getWxMu() + ", wxShui=" + this.getWxShui() + ", wxHuo=" + this.getWxHuo() + ", wxTu=" + this.getWxTu() + ", skillNo=" + this.getSkillNo() + ", petType=" + this.getPetType() + ", adviceAddPro=" + this.getAdviceAddPro() + ", openZs=" + this.getOpenZs() + ", openLv=" + this.getOpenLv() + ", needItem=" + this.getNeedItem() + ", canCompose=" + this.getCanCompose() + ", catchRatio=" + this.getCatchRatio() + ", catchMap=" + this.getCatchMap() + ", pacc=" + this.getPacc() + ", staticInitVarScore=" + this.getStaticInitVarScore() + ", color=" + this.getColor() + ", jxSkill=" + this.getJxSkill() + ", accColor=" + this.getAccColor() + ", brandNew=" + this.getBrandNew() + ", canMerge=" + this.getCanMerge() + ")";
    }
}
