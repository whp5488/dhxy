package com.kitty.system.init.domain;

import org.nutz.dao.entity.annotation.Default;
import com.kitty.system.init.domain.vo.AdviceAddPro;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import java.io.Serializable;

@Table("data_hero")
public class SysHero implements Serializable
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Column("id")
    private Integer id;
    @Comment("编号")
    @Column("serial")
    private Integer serial;
    @Comment("角色名称")
    @Column("name")
    private String name;
    @Comment("种族")
    @Column("race")
    private int race;
    @Comment("外貌")
    @Column("shape")
    private Integer shape;
    @Comment("性别")
    @Column("gender")
    private int gender;
    @Comment("气血")
    @Column("hp")
    private Integer hp;
    @Comment("法力")
    @Column("mp")
    private Integer mp;
    @Comment("伤害")
    @Column("ap")
    private Integer ap;
    @Comment("速度")
    @Column("sp")
    private Integer sp;
    @Comment("根骨")
    @Column("gg")
    private String gg;
    @Comment("根骨 对属性成长（生命值种族修正）")
    @Column("g_gg")
    private String gGg;
    @Comment("灵性")
    @Column("lx")
    private String lx;
    @Comment("灵性 对属性成长（生命值种族修正）")
    @Column("g_lx")
    private String gLx;
    @Comment("力量")
    @Column("ll")
    private String ll;
    @Comment("力量 对属性成长（攻击力种族修正）")
    @Column("g_ll")
    private String gLl;
    @Comment("敏捷")
    @Column("mj")
    private String mj;
    @Comment("敏捷 对属性成长（速度值种族修正）")
    @Column("g_mj")
    private String gMj;
    @Comment("冷却")
    @Column("cd")
    private String cd;
    @Comment("")
    @Column("g_cd")
    private String gCd;
    @Comment("五行金")
    @Column("wx_jin")
    private String wxJin;
    @Comment("五行木")
    @Column("wx_mu")
    private String wxMu;
    @Comment("五行水")
    @Column("wx_shui")
    private String wxShui;
    @Comment("五行火")
    @Column("wx_huo")
    private String wxHuo;
    @Comment("五行土")
    @Column("wx_tu")
    private String wxTu;
    @Comment("推荐加点")
    @Column("advice_add_pro")
    @Default("[]")
    private List<AdviceAddPro> adviceAddPro;
    @Comment("镜像")
    @Column("mirror")
    private int mirror;
    
    public SysHero() {
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
    
    public int getRace() {
        return this.race;
    }
    
    public Integer getShape() {
        return this.shape;
    }
    
    public int getGender() {
        return this.gender;
    }
    
    public Integer getHp() {
        return this.hp;
    }
    
    public Integer getMp() {
        return this.mp;
    }
    
    public Integer getAp() {
        return this.ap;
    }
    
    public Integer getSp() {
        return this.sp;
    }
    
    public String getGg() {
        return this.gg;
    }
    
    public String getGGg() {
        return this.gGg;
    }
    
    public String getLx() {
        return this.lx;
    }
    
    public String getGLx() {
        return this.gLx;
    }
    
    public String getLl() {
        return this.ll;
    }
    
    public String getGLl() {
        return this.gLl;
    }
    
    public String getMj() {
        return this.mj;
    }
    
    public String getGMj() {
        return this.gMj;
    }
    
    public String getCd() {
        return this.cd;
    }
    
    public String getGCd() {
        return this.gCd;
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
    
    public List<AdviceAddPro> getAdviceAddPro() {
        return this.adviceAddPro;
    }
    
    public int getMirror() {
        return this.mirror;
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
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setGender(final int gender) {
        this.gender = gender;
    }
    
    public void setHp(final Integer hp) {
        this.hp = hp;
    }
    
    public void setMp(final Integer mp) {
        this.mp = mp;
    }
    
    public void setAp(final Integer ap) {
        this.ap = ap;
    }
    
    public void setSp(final Integer sp) {
        this.sp = sp;
    }
    
    public void setGg(final String gg) {
        this.gg = gg;
    }
    
    public void setGGg(final String gGg) {
        this.gGg = gGg;
    }
    
    public void setLx(final String lx) {
        this.lx = lx;
    }
    
    public void setGLx(final String gLx) {
        this.gLx = gLx;
    }
    
    public void setLl(final String ll) {
        this.ll = ll;
    }
    
    public void setGLl(final String gLl) {
        this.gLl = gLl;
    }
    
    public void setMj(final String mj) {
        this.mj = mj;
    }
    
    public void setGMj(final String gMj) {
        this.gMj = gMj;
    }
    
    public void setCd(final String cd) {
        this.cd = cd;
    }
    
    public void setGCd(final String gCd) {
        this.gCd = gCd;
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
    
    public void setAdviceAddPro(final List<AdviceAddPro> adviceAddPro) {
        this.adviceAddPro = adviceAddPro;
    }
    
    public void setMirror(final int mirror) {
        this.mirror = mirror;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof SysHero)) {
            return false;
        }
        SysHero other = (SysHero)o;
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
        if (this.getRace() != other.getRace()) {
            return false;
        }
        Object this$shape = this.getShape();
        Object other$shape = other.getShape();
        if (!((this$shape != null) ? this$shape.equals(other$shape) : (other$shape == null))) {
            return false;
        }
        if (this.getGender() != other.getGender()) {
            return false;
        }
        Object this$hp = this.getHp();
        Object other$hp = other.getHp();
        if (!((this$hp != null) ? this$hp.equals(other$hp) : (other$hp == null))) {
            return false;
        }
        Object this$mp = this.getMp();
        Object other$mp = other.getMp();
        if (!((this$mp != null) ? this$mp.equals(other$mp) : (other$mp == null))) {
            return false;
        }
        Object this$ap = this.getAp();
        Object other$ap = other.getAp();
        if (!((this$ap != null) ? this$ap.equals(other$ap) : (other$ap == null))) {
            return false;
        }
        Object this$sp = this.getSp();
        Object other$sp = other.getSp();
        if (!((this$sp != null) ? this$sp.equals(other$sp) : (other$sp == null))) {
            return false;
        }
        Object this$gg = this.getGg();
        Object other$gg = other.getGg();
        if (!((this$gg != null) ? this$gg.equals(other$gg) : (other$gg == null))) {
            return false;
        }
        Object this$gGg = this.getGGg();
        Object other$gGg = other.getGGg();
        if (!((this$gGg != null) ? this$gGg.equals(other$gGg) : (other$gGg == null))) {
            return false;
        }
        Object this$lx = this.getLx();
        Object other$lx = other.getLx();
        if (!((this$lx != null) ? this$lx.equals(other$lx) : (other$lx == null))) {
            return false;
        }
        Object this$gLx = this.getGLx();
        Object other$gLx = other.getGLx();
        if (!((this$gLx != null) ? this$gLx.equals(other$gLx) : (other$gLx == null))) {
            return false;
        }
        Object this$ll = this.getLl();
        Object other$ll = other.getLl();
        if (!((this$ll != null) ? this$ll.equals(other$ll) : (other$ll == null))) {
            return false;
        }
        Object this$gLl = this.getGLl();
        Object other$gLl = other.getGLl();
        if (!((this$gLl != null) ? this$gLl.equals(other$gLl) : (other$gLl == null))) {
            return false;
        }
        Object this$mj = this.getMj();
        Object other$mj = other.getMj();
        if (!((this$mj != null) ? this$mj.equals(other$mj) : (other$mj == null))) {
            return false;
        }
        Object this$gMj = this.getGMj();
        Object other$gMj = other.getGMj();
        if (!((this$gMj != null) ? this$gMj.equals(other$gMj) : (other$gMj == null))) {
            return false;
        }
        Object this$cd = this.getCd();
        Object other$cd = other.getCd();
        if (!((this$cd != null) ? this$cd.equals(other$cd) : (other$cd == null))) {
            return false;
        }
        Object this$gCd = this.getGCd();
        Object other$gCd = other.getGCd();
        if (!((this$gCd != null) ? this$gCd.equals(other$gCd) : (other$gCd == null))) {
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
        Object this$adviceAddPro = this.getAdviceAddPro();
        Object other$adviceAddPro = other.getAdviceAddPro();
        return ((this$adviceAddPro != null) ? this$adviceAddPro.equals(other$adviceAddPro) : (other$adviceAddPro == null)) && this.getMirror() == other.getMirror();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysHero;
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
        result = result * 59 + this.getRace();
        Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        result = result * 59 + this.getGender();
        Object $hp = this.getHp();
        result = result * 59 + (($hp == null) ? 43 : $hp.hashCode());
        Object $mp = this.getMp();
        result = result * 59 + (($mp == null) ? 43 : $mp.hashCode());
        Object $ap = this.getAp();
        result = result * 59 + (($ap == null) ? 43 : $ap.hashCode());
        Object $sp = this.getSp();
        result = result * 59 + (($sp == null) ? 43 : $sp.hashCode());
        Object $gg = this.getGg();
        result = result * 59 + (($gg == null) ? 43 : $gg.hashCode());
        Object $gGg = this.getGGg();
        result = result * 59 + (($gGg == null) ? 43 : $gGg.hashCode());
        Object $lx = this.getLx();
        result = result * 59 + (($lx == null) ? 43 : $lx.hashCode());
        Object $gLx = this.getGLx();
        result = result * 59 + (($gLx == null) ? 43 : $gLx.hashCode());
        Object $ll = this.getLl();
        result = result * 59 + (($ll == null) ? 43 : $ll.hashCode());
        Object $gLl = this.getGLl();
        result = result * 59 + (($gLl == null) ? 43 : $gLl.hashCode());
        Object $mj = this.getMj();
        result = result * 59 + (($mj == null) ? 43 : $mj.hashCode());
        Object $gMj = this.getGMj();
        result = result * 59 + (($gMj == null) ? 43 : $gMj.hashCode());
        Object $cd = this.getCd();
        result = result * 59 + (($cd == null) ? 43 : $cd.hashCode());
        Object $gCd = this.getGCd();
        result = result * 59 + (($gCd == null) ? 43 : $gCd.hashCode());
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
        Object $adviceAddPro = this.getAdviceAddPro();
        result = result * 59 + (($adviceAddPro == null) ? 43 : $adviceAddPro.hashCode());
        result = result * 59 + this.getMirror();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysHero(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", race=" + this.getRace() + ", shape=" + this.getShape() + ", gender=" + this.getGender() + ", hp=" + this.getHp() + ", mp=" + this.getMp() + ", ap=" + this.getAp() + ", sp=" + this.getSp() + ", gg=" + this.getGg() + ", gGg=" + this.getGGg() + ", lx=" + this.getLx() + ", gLx=" + this.getGLx() + ", ll=" + this.getLl() + ", gLl=" + this.getGLl() + ", mj=" + this.getMj() + ", gMj=" + this.getGMj() + ", cd=" + this.getCd() + ", gCd=" + this.getGCd() + ", wxJin=" + this.getWxJin() + ", wxMu=" + this.getWxMu() + ", wxShui=" + this.getWxShui() + ", wxHuo=" + this.getWxHuo() + ", wxTu=" + this.getWxTu() + ", adviceAddPro=" + this.getAdviceAddPro() + ", mirror=" + this.getMirror() + ")";
    }
}
