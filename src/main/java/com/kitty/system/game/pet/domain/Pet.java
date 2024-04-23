package com.kitty.system.game.pet.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4PetService;
import com.kitty.system.game.pet.domain.vo.PetCorrection;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.player.serializer.PetSerializerUtil;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.ColType;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.nutz.lang.util.NutMap;

@Table("pet")
public class Pet extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Comment("主人ID")
  @Column("role_id")
  private Integer roleId;
  
  @Comment("宠物ID")
  @Column("pet_id")
  private Integer petId;
  
  @Comment("宠物外观")
  @Column("shape")
  private Integer shape;
  
  @Comment("宠物编号")
  @Column("pet_type")
  private Integer petType;
  
  @Comment("宠物名称")
  @Column("pet_name")
  private String petName;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
  
  public void setPetId(Integer petId) {
    this.petId = petId;
  }
  
  public void setShape(Integer shape) {
    this.shape = shape;
  }
  
  public void setPetType(Integer petType) {
    this.petType = petType;
  }
  
  public void setPetName(String petName) {
    this.petName = petName;
  }
  
  public void setPetLv(Integer petLv) {
    this.petLv = petLv;
  }
  
  public void setRebirth(Integer rebirth) {
    this.rebirth = rebirth;
  }
  
  public void setFpoint(Integer fpoint) {
    this.fpoint = fpoint;
  }
  
  public void setExp(long exp) {
    this.exp = exp;
  }
  
  public void setPetClose(long petClose) {
    this.petClose = petClose;
  }
  
  public void setGg(Integer gg) {
    this.gg = gg;
  }
  
  public void setLx(Integer lx) {
    this.lx = lx;
  }
  
  public void setLl(Integer ll) {
    this.ll = ll;
  }
  
  public void setMj(Integer mj) {
    this.mj = mj;
  }
  
  public void setCurrLife(int currLife) {
    this.currLife = currLife;
  }
  
  public void setMaxLife(Integer maxLife) {
    this.maxLife = maxLife;
  }
  
  public void setCurrMana(Integer currMana) {
    this.currMana = currMana;
  }
  
  public void setMaxMana(Integer maxMana) {
    this.maxMana = maxMana;
  }
  
  public void setScore(Integer score) {
    this.score = score;
  }
  
  public void setGrowthDan(Integer growthDan) {
    this.growthDan = growthDan;
  }
  
  public void setBase(String base) {
    this.base = base;
  }
  
  public void setIsBase(Integer isBase) {
    this.isBase = isBase;
  }
  
  public void setLyn(Integer lyn) {
    this.lyn = lyn;
  }
  
  public void setBone(Integer bone) {
    this.bone = bone;
  }
  
  public void setFly(Integer fly) {
    this.fly = fly;
  }
  
  public void setFlyStarter(int flyStarter) {
    this.flyStarter = flyStarter;
  }
  
  public void setCorrectionInfoJson(String correctionInfoJson) {
    this.correctionInfoJson = correctionInfoJson;
  }
  
  public void setMsgMap(List<NutMap> msgMap) {
    this.msgMap = msgMap;
  }
  
  public void setCloseLimitMap(Map<String, Integer> closeLimitMap) {
    this.closeLimitMap = closeLimitMap;
  }
  
  public void setSkills(List<PetSkill> skills) {
    this.skills = skills;
  }
  
  public void setCorrection(Map<Integer, Integer> correction) {
    this.correction = correction;
  }
  
  public void setAutoAttrib(Map<String, Integer> autoAttrib) {
    this.autoAttrib = autoAttrib;
  }
  
  public void setAutoFightSet(Map<String, Object> autoFightSet) {
    this.autoFightSet = autoFightSet;
  }
  
  public void setDarkBaseMap(Map<Integer, List<PetBase>> darkBaseMap) {
    this.darkBaseMap = darkBaseMap;
  }
  
  public void setColorList(List<Map<String, Byte>> colorList) {
    this.colorList = colorList;
  }
  
  public void setAccColor(Map<Byte, Byte> accColor) {
    this.accColor = accColor;
  }
  
  public void setAccColorTwo(Map<Byte, Byte> accColorTwo) {
    this.accColorTwo = accColorTwo;
  }
  
  public void setBrandNew(Map<Integer, Byte> brandNew) {
    this.brandNew = brandNew;
  }
  
  public void setAwaken(short awaken) {
    this.awaken = awaken;
  }
  
  public void setHorse(long horse) {
    this.horse = horse;
  }
  
  public void setBindTime(int bindTime) {
    this.bindTime = bindTime;
  }
  
  public void setDomesticate(short domesticate) {
    this.domesticate = domesticate;
  }
  
  public void setPetBase(PetBase petBase) {
    this.petBase = petBase;
  }
  
  public void setCorrectionInfo(PetCorrection correctionInfo) {
    this.correctionInfo = correctionInfo;
  }
  
  public String toString() {
    return "Pet(id=" + getId() + ", roleId=" + getRoleId() + ", petId=" + getPetId() + ", shape=" + getShape() + ", petType=" + getPetType() + ", petName=" + getPetName() + ", petLv=" + getPetLv() + ", rebirth=" + getRebirth() + ", fpoint=" + getFpoint() + ", exp=" + getExp() + ", petClose=" + getPetClose() + ", gg=" + getGg() + ", lx=" + getLx() + ", ll=" + getLl() + ", mj=" + getMj() + ", currLife=" + getCurrLife() + ", maxLife=" + getMaxLife() + ", currMana=" + getCurrMana() + ", maxMana=" + getMaxMana() + ", score=" + getScore() + ", growthDan=" + getGrowthDan() + ", base=" + getBase() + ", isBase=" + getIsBase() + ", lyn=" + getLyn() + ", bone=" + getBone() + ", fly=" + getFly() + ", flyStarter=" + getFlyStarter() + ", correctionInfoJson=" + getCorrectionInfoJson() + ", msgMap=" + getMsgMap() + ", closeLimitMap=" + getCloseLimitMap() + ", skills=" + getSkills() + ", correction=" + getCorrection() + ", autoAttrib=" + getAutoAttrib() + ", autoFightSet=" + getAutoFightSet() + ", darkBaseMap=" + getDarkBaseMap() + ", colorList=" + getColorList() + ", accColor=" + getAccColor() + ", accColorTwo=" + getAccColorTwo() + ", brandNew=" + getBrandNew() + ", awaken=" + getAwaken() + ", horse=" + getHorse() + ", bindTime=" + getBindTime() + ", domesticate=" + getDomesticate() + ", petBase=" + getPetBase() + ", correctionInfo=" + getCorrectionInfo() + ")";
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Integer getRoleId() {
    return this.roleId;
  }
  
  public Integer getPetId() {
    return this.petId;
  }
  
  public Integer getShape() {
    return this.shape;
  }
  
  public Integer getPetType() {
    return this.petType;
  }
  
  public String getPetName() {
    return this.petName;
  }
  
  @Comment("宠物等级")
  @Column("pet_lv")
  private Integer petLv = 0;
  
  public Integer getPetLv() {
    return this.petLv;
  }
  
  @Comment("宠物转生次数")
  @Column("rebirth")
  private Integer rebirth = 0;
  
  public Integer getRebirth() {
    return this.rebirth;
  }
  
  @Comment("未分配点数")
  @Column("fpoint")
  private Integer fpoint = 0;
  
  public Integer getFpoint() {
    return this.fpoint;
  }
  
  @Comment("经验值")
  @Column("exp")
  private long exp = 0L;
  
  public long getExp() {
    return this.exp;
  }
  
  @Comment("亲密度")
  @Column("pet_close")
  private long petClose = 0L;
  
  public long getPetClose() {
    return this.petClose;
  }
  
  @Comment("根骨")
  @Column("gg")
  private Integer gg = 0;
  
  public Integer getGg() {
    return this.gg;
  }
  
  @Comment("灵性")
  @Column("lx")
  private Integer lx = 0;
  
  public Integer getLx() {
    return this.lx;
  }
  
  @Comment("力量")
  @Column("ll")
  private Integer ll = 0;
  
  public Integer getLl() {
    return this.ll;
  }
  
  @Comment("敏捷")
  @Column("mj")
  private Integer mj = 0;
  
  @Comment("气血值")
  @Column("curr_life")
  private int currLife;
  
  @Comment("最大气血值")
  @Column("max_life")
  private Integer maxLife;
  
  @Comment("法力值")
  @Column("curr_mana")
  private Integer currMana;
  
  @Comment("最大法力值")
  @Column("max_mana")
  private Integer maxMana;
  
  @Comment("评分")
  @Column("score")
  private Integer score;
  
  public Integer getMj() {
    return this.mj;
  }
  
  public int getCurrLife() {
    return this.currLife;
  }
  
  public Integer getMaxLife() {
    return this.maxLife;
  }
  
  public Integer getCurrMana() {
    return this.currMana;
  }
  
  public Integer getMaxMana() {
    return this.maxMana;
  }
  
  public Integer getScore() {
    return this.score;
  }
  
  @Comment("成长丹：0.未使用 1.已使用")
  @Column("growth_dan")
  private Integer growthDan = Integer.valueOf(0);
  
  @Comment("成长属性{气血-法力-攻击-速度-成长率-品阶}")
  @Column("base")
  private String base;
  
  @Comment("是否洗练（0 未洗练）")
  @Column("is_base")
  private Integer isBase;
  
  public Integer getGrowthDan() {
    return this.growthDan;
  }
  
  public String getBase() {
    return this.base;
  }
  
  public Integer getIsBase() {
    return this.isBase;
  }
  
  @Comment("炼妖次数")
  @Column("lyn")
  private Integer lyn = Integer.valueOf(0);
  
  public Integer getLyn() {
    return this.lyn;
  }
  
  @Comment("龙骨")
  @Column("bone")
  private Integer bone = Integer.valueOf(0);
  
  public Integer getBone() {
    return this.bone;
  }
  
  @Comment("宠物飞升")
  @Column("fly")
  private Integer fly = Integer.valueOf(0);
  
  public Integer getFly() {
    return this.fly;
  }
  
  @Comment("宠物飞升-自定义初始值: 0.根骨 1.灵性 2.力量 3.敏捷")
  @Column("fly_starter")
  private int flyStarter = 0;
  
  @Comment("修炼属性属性信息")
  @Column("correction_info")
  private String correctionInfoJson;
  
  public int getFlyStarter() {
    return this.flyStarter;
  }
  
  public String getCorrectionInfoJson() {
    return this.correctionInfoJson;
  }
  
  @Comment("宠物语言泡泡")
  @Column("msg")
  @Default("[]")
  @ColDefine(customType = "text")
  private List<NutMap> msgMap = new ArrayList<>();
  
  public List<NutMap> getMsgMap() {
    return this.msgMap;
  }
  
  @Comment("亲密丹增加的亲密")
  @Column("close_limit_json")
  @Default("[]")
  @ColDefine(customType = "text")
  private Map<String, Integer> closeLimitMap = new HashMap<>();
  
  public Map<String, Integer> getCloseLimitMap() {
    return this.closeLimitMap;
  }
  
  @Comment("技能")
  @Column("skills_json")
  @Default("[]")
  @ColDefine(customType = "longtext")
  private List<PetSkill> skills = new ArrayList<>();
  
  public List<PetSkill> getSkills() {
    return this.skills;
  }
  
  @Comment("修炼数据：修炼属性<修炼属性，当前加点>")
  @Column("correction_json")
  @Default("[]")
  @ColDefine(customType = "text")
  private Map<Integer, Integer> correction = new HashMap<>();
  
  public Map<Integer, Integer> getCorrection() {
    return this.correction;
  }
  
  @Comment("自动加点")
  @Column("auto_attrib_json")
  @Default("[]")
  @ColDefine(customType = "text")
  private Map<String, Integer> autoAttrib = new HashMap<>();
  
  public Map<String, Integer> getAutoAttrib() {
    return this.autoAttrib;
  }
  
  @Comment("自动战斗相关设置")
  @Column("auto_fight_set")
  @Default("[]")
  @ColDefine(customType = "text")
  private Map<String, Object> autoFightSet = new HashMap<>();
  
  public Map<String, Object> getAutoFightSet() {
    return this.autoFightSet;
  }
  
  @Comment("key: 1.普通 2.高级 3.超级，list:洗练未替换的成长属性{成长率-气血-法力-攻击-速度}")
  @Column("dark_base")
  @ColDefine(customType = "longtext")
  @Default("[]")
  private Map<Integer, List<PetBase>> darkBaseMap = new HashMap<>();
  
  public Map<Integer, List<PetBase>> getDarkBaseMap() {
    return this.darkBaseMap;
  }
  
  @Comment("染色 染色集合  List<Map<颜色编号/是否使用，值>>  a=角色编号  b=是否使用")
  @Column("color")
  @Default("[]")
  @ColDefine(customType = "text")
  private List<Map<String, Byte>> colorList = new ArrayList<>();
  
  public List<Map<String, Byte>> getColorList() {
    return this.colorList;
  }
  
  @Comment("饰品染色部位一 Map<颜色编号，是否使用>")
  @Column("acc_color")
  @ColDefine(customType = "text")
  private Map<Byte, Byte> accColor = new HashMap<>();
  
  public Map<Byte, Byte> getAccColor() {
    return this.accColor;
  }
  
  @Comment("饰品染色部位二 Map<颜色编号，是否使用>")
  @Column("acc_color_two")
  @ColDefine(customType = "text")
  private Map<Byte, Byte> accColorTwo = new HashMap<>();
  
  public Map<Byte, Byte> getAccColorTwo() {
    return this.accColorTwo;
  }
  
  @Comment("幻肤 Map<幻肤编号，是否使用>")
  @Column("brand_new")
  private Map<Integer, Byte> brandNew = new HashMap<>();
  
  public Map<Integer, Byte> getBrandNew() {
    return this.brandNew;
  }
  
  @Comment("觉醒突破次数")
  @Column("awaken")
  private short awaken = 0;
  
  public short getAwaken() {
    return this.awaken;
  }
  
  @Comment("管制坐骑编号：0.则无管制")
  @Column("horse")
  private long horse = 0L;
  
  public long getHorse() {
    return this.horse;
  }
  
  @Comment("绑定到期时间（-1则无）")
  @Column("bind_time")
  private int bindTime = -1;
  
  public int getBindTime() {
    return this.bindTime;
  }
  
  @Comment("周驯养次数")
  @Default("0")
  @ColDefine(type = ColType.INT, width = 1)
  @Column("domesticate")
  private short domesticate = 0;
  
  public short getDomesticate() {
    return this.domesticate;
  }
  
  private PetBase petBase = new PetBase();
  
  public PetBase getPetBase() {
    return this.petBase;
  }
  
  private PetCorrection correctionInfo = new PetCorrection();
  
  public PetCorrection getCorrectionInfo() {
    return this.correctionInfo;
  }
  
  public void save() {
    Db4PetService.getInstance().add2Queue(this);
  }
  
  public void doAfterInit() {
    PetSerializerUtil.deserialize(this);
  }
  
  public void doBeforeSave() {
    PetSerializerUtil.serialize(this);
  }
}
