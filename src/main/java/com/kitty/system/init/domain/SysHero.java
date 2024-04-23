package com.kitty.system.init.domain;

import com.kitty.system.init.domain.vo.AdviceAddPro;
import java.io.Serializable;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Table;

@Table("data_hero")
public class SysHero implements Serializable {
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
  
  public void setId(Integer id) {
    this.id = id;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setRace(int race) {
    this.race = race;
  }
  
  public void setShape(Integer shape) {
    this.shape = shape;
  }
  
  public void setGender(int gender) {
    this.gender = gender;
  }
  
  public void setHp(Integer hp) {
    this.hp = hp;
  }
  
  public void setMp(Integer mp) {
    this.mp = mp;
  }
  
  public void setAp(Integer ap) {
    this.ap = ap;
  }
  
  public void setSp(Integer sp) {
    this.sp = sp;
  }
  
  public void setGg(String gg) {
    this.gg = gg;
  }
  
  public void setGGg(String gGg) {
    this.gGg = gGg;
  }
  
  public void setLx(String lx) {
    this.lx = lx;
  }
  
  public void setGLx(String gLx) {
    this.gLx = gLx;
  }
  
  public void setLl(String ll) {
    this.ll = ll;
  }
  
  public void setGLl(String gLl) {
    this.gLl = gLl;
  }
  
  public void setMj(String mj) {
    this.mj = mj;
  }
  
  public void setGMj(String gMj) {
    this.gMj = gMj;
  }
  
  public void setCd(String cd) {
    this.cd = cd;
  }
  
  public void setGCd(String gCd) {
    this.gCd = gCd;
  }
  
  public void setWxJin(String wxJin) {
    this.wxJin = wxJin;
  }
  
  public void setWxMu(String wxMu) {
    this.wxMu = wxMu;
  }
  
  public void setWxShui(String wxShui) {
    this.wxShui = wxShui;
  }
  
  public void setWxHuo(String wxHuo) {
    this.wxHuo = wxHuo;
  }
  
  public void setWxTu(String wxTu) {
    this.wxTu = wxTu;
  }
  
  public void setAdviceAddPro(List<AdviceAddPro> adviceAddPro) {
    this.adviceAddPro = adviceAddPro;
  }
  
  public void setMirror(int mirror) {
    this.mirror = mirror;
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
}
