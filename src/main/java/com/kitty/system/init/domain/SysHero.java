
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
        final SysHero other = (SysHero)o;
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
        if (this.getRace() != other.getRace()) {
            return false;
        }
        final Object this$shape = this.getShape();
        final Object other$shape = other.getShape();
        Label_0189: {
            if (this$shape == null) {
                if (other$shape == null) {
                    break Label_0189;
                }
            }
            else if (this$shape.equals(other$shape)) {
                break Label_0189;
            }
            return false;
        }
        if (this.getGender() != other.getGender()) {
            return false;
        }
        final Object this$hp = this.getHp();
        final Object other$hp = other.getHp();
        Label_0239: {
            if (this$hp == null) {
                if (other$hp == null) {
                    break Label_0239;
                }
            }
            else if (this$hp.equals(other$hp)) {
                break Label_0239;
            }
            return false;
        }
        final Object this$mp = this.getMp();
        final Object other$mp = other.getMp();
        Label_0276: {
            if (this$mp == null) {
                if (other$mp == null) {
                    break Label_0276;
                }
            }
            else if (this$mp.equals(other$mp)) {
                break Label_0276;
            }
            return false;
        }
        final Object this$ap = this.getAp();
        final Object other$ap = other.getAp();
        Label_0313: {
            if (this$ap == null) {
                if (other$ap == null) {
                    break Label_0313;
                }
            }
            else if (this$ap.equals(other$ap)) {
                break Label_0313;
            }
            return false;
        }
        final Object this$sp = this.getSp();
        final Object other$sp = other.getSp();
        Label_0350: {
            if (this$sp == null) {
                if (other$sp == null) {
                    break Label_0350;
                }
            }
            else if (this$sp.equals(other$sp)) {
                break Label_0350;
            }
            return false;
        }
        final Object this$gg = this.getGg();
        final Object other$gg = other.getGg();
        Label_0387: {
            if (this$gg == null) {
                if (other$gg == null) {
                    break Label_0387;
                }
            }
            else if (this$gg.equals(other$gg)) {
                break Label_0387;
            }
            return false;
        }
        final Object this$gGg = this.getGGg();
        final Object other$gGg = other.getGGg();
        Label_0424: {
            if (this$gGg == null) {
                if (other$gGg == null) {
                    break Label_0424;
                }
            }
            else if (this$gGg.equals(other$gGg)) {
                break Label_0424;
            }
            return false;
        }
        final Object this$lx = this.getLx();
        final Object other$lx = other.getLx();
        Label_0461: {
            if (this$lx == null) {
                if (other$lx == null) {
                    break Label_0461;
                }
            }
            else if (this$lx.equals(other$lx)) {
                break Label_0461;
            }
            return false;
        }
        final Object this$gLx = this.getGLx();
        final Object other$gLx = other.getGLx();
        Label_0498: {
            if (this$gLx == null) {
                if (other$gLx == null) {
                    break Label_0498;
                }
            }
            else if (this$gLx.equals(other$gLx)) {
                break Label_0498;
            }
            return false;
        }
        final Object this$ll = this.getLl();
        final Object other$ll = other.getLl();
        Label_0535: {
            if (this$ll == null) {
                if (other$ll == null) {
                    break Label_0535;
                }
            }
            else if (this$ll.equals(other$ll)) {
                break Label_0535;
            }
            return false;
        }
        final Object this$gLl = this.getGLl();
        final Object other$gLl = other.getGLl();
        Label_0572: {
            if (this$gLl == null) {
                if (other$gLl == null) {
                    break Label_0572;
                }
            }
            else if (this$gLl.equals(other$gLl)) {
                break Label_0572;
            }
            return false;
        }
        final Object this$mj = this.getMj();
        final Object other$mj = other.getMj();
        Label_0609: {
            if (this$mj == null) {
                if (other$mj == null) {
                    break Label_0609;
                }
            }
            else if (this$mj.equals(other$mj)) {
                break Label_0609;
            }
            return false;
        }
        final Object this$gMj = this.getGMj();
        final Object other$gMj = other.getGMj();
        Label_0646: {
            if (this$gMj == null) {
                if (other$gMj == null) {
                    break Label_0646;
                }
            }
            else if (this$gMj.equals(other$gMj)) {
                break Label_0646;
            }
            return false;
        }
        final Object this$cd = this.getCd();
        final Object other$cd = other.getCd();
        Label_0683: {
            if (this$cd == null) {
                if (other$cd == null) {
                    break Label_0683;
                }
            }
            else if (this$cd.equals(other$cd)) {
                break Label_0683;
            }
            return false;
        }
        final Object this$gCd = this.getGCd();
        final Object other$gCd = other.getGCd();
        Label_0720: {
            if (this$gCd == null) {
                if (other$gCd == null) {
                    break Label_0720;
                }
            }
            else if (this$gCd.equals(other$gCd)) {
                break Label_0720;
            }
            return false;
        }
        final Object this$wxJin = this.getWxJin();
        final Object other$wxJin = other.getWxJin();
        Label_0757: {
            if (this$wxJin == null) {
                if (other$wxJin == null) {
                    break Label_0757;
                }
            }
            else if (this$wxJin.equals(other$wxJin)) {
                break Label_0757;
            }
            return false;
        }
        final Object this$wxMu = this.getWxMu();
        final Object other$wxMu = other.getWxMu();
        Label_0794: {
            if (this$wxMu == null) {
                if (other$wxMu == null) {
                    break Label_0794;
                }
            }
            else if (this$wxMu.equals(other$wxMu)) {
                break Label_0794;
            }
            return false;
        }
        final Object this$wxShui = this.getWxShui();
        final Object other$wxShui = other.getWxShui();
        Label_0831: {
            if (this$wxShui == null) {
                if (other$wxShui == null) {
                    break Label_0831;
                }
            }
            else if (this$wxShui.equals(other$wxShui)) {
                break Label_0831;
            }
            return false;
        }
        final Object this$wxHuo = this.getWxHuo();
        final Object other$wxHuo = other.getWxHuo();
        Label_0868: {
            if (this$wxHuo == null) {
                if (other$wxHuo == null) {
                    break Label_0868;
                }
            }
            else if (this$wxHuo.equals(other$wxHuo)) {
                break Label_0868;
            }
            return false;
        }
        final Object this$wxTu = this.getWxTu();
        final Object other$wxTu = other.getWxTu();
        Label_0905: {
            if (this$wxTu == null) {
                if (other$wxTu == null) {
                    break Label_0905;
                }
            }
            else if (this$wxTu.equals(other$wxTu)) {
                break Label_0905;
            }
            return false;
        }
        final Object this$adviceAddPro = this.getAdviceAddPro();
        final Object other$adviceAddPro = other.getAdviceAddPro();
        if (this$adviceAddPro == null) {
            if (other$adviceAddPro == null) {
                return this.getMirror() == other.getMirror();
            }
        }
        else if (this$adviceAddPro.equals(other$adviceAddPro)) {
            return this.getMirror() == other.getMirror();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof SysHero;
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
        result = result * 59 + this.getRace();
        final Object $shape = this.getShape();
        result = result * 59 + (($shape == null) ? 43 : $shape.hashCode());
        result = result * 59 + this.getGender();
        final Object $hp = this.getHp();
        result = result * 59 + (($hp == null) ? 43 : $hp.hashCode());
        final Object $mp = this.getMp();
        result = result * 59 + (($mp == null) ? 43 : $mp.hashCode());
        final Object $ap = this.getAp();
        result = result * 59 + (($ap == null) ? 43 : $ap.hashCode());
        final Object $sp = this.getSp();
        result = result * 59 + (($sp == null) ? 43 : $sp.hashCode());
        final Object $gg = this.getGg();
        result = result * 59 + (($gg == null) ? 43 : $gg.hashCode());
        final Object $gGg = this.getGGg();
        result = result * 59 + (($gGg == null) ? 43 : $gGg.hashCode());
        final Object $lx = this.getLx();
        result = result * 59 + (($lx == null) ? 43 : $lx.hashCode());
        final Object $gLx = this.getGLx();
        result = result * 59 + (($gLx == null) ? 43 : $gLx.hashCode());
        final Object $ll = this.getLl();
        result = result * 59 + (($ll == null) ? 43 : $ll.hashCode());
        final Object $gLl = this.getGLl();
        result = result * 59 + (($gLl == null) ? 43 : $gLl.hashCode());
        final Object $mj = this.getMj();
        result = result * 59 + (($mj == null) ? 43 : $mj.hashCode());
        final Object $gMj = this.getGMj();
        result = result * 59 + (($gMj == null) ? 43 : $gMj.hashCode());
        final Object $cd = this.getCd();
        result = result * 59 + (($cd == null) ? 43 : $cd.hashCode());
        final Object $gCd = this.getGCd();
        result = result * 59 + (($gCd == null) ? 43 : $gCd.hashCode());
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
        final Object $adviceAddPro = this.getAdviceAddPro();
        result = result * 59 + (($adviceAddPro == null) ? 43 : $adviceAddPro.hashCode());
        result = result * 59 + this.getMirror();
        return result;
    }
    
    @Override
    public String toString() {
        return "SysHero(id=" + this.getId() + ", serial=" + this.getSerial() + ", name=" + this.getName() + ", race=" + this.getRace() + ", shape=" + this.getShape() + ", gender=" + this.getGender() + ", hp=" + this.getHp() + ", mp=" + this.getMp() + ", ap=" + this.getAp() + ", sp=" + this.getSp() + ", gg=" + this.getGg() + ", gGg=" + this.getGGg() + ", lx=" + this.getLx() + ", gLx=" + this.getGLx() + ", ll=" + this.getLl() + ", gLl=" + this.getGLl() + ", mj=" + this.getMj() + ", gMj=" + this.getGMj() + ", cd=" + this.getCd() + ", gCd=" + this.getGCd() + ", wxJin=" + this.getWxJin() + ", wxMu=" + this.getWxMu() + ", wxShui=" + this.getWxShui() + ", wxHuo=" + this.getWxHuo() + ", wxTu=" + this.getWxTu() + ", adviceAddPro=" + this.getAdviceAddPro() + ", mirror=" + this.getMirror() + ")";
    }
}
