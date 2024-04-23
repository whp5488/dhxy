package com.kitty.system.game.partner.domain;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4PartnerService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.game.skill.domain.SkillBox;
import org.apache.commons.lang3.StringUtils;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("partner")
public class Partner extends BaseEntity<Long> {
  @Comment("主键")
  @Id(auto = false)
  @Column("id")
  private Long id;
  
  @Comment("主人ID")
  @Column("role_id")
  private Integer roleId;
  
  @Comment("伙伴id")
  @Column("partner_id")
  private Integer partnerId;
  
  @Comment("伙伴外观")
  @Column("icon")
  private Integer icon;
  
  @Comment("伙伴编号")
  @Column("partner_type")
  private Integer partnerType;
  
  @Comment("伙伴名称")
  @Column("partner_name")
  private String partnerName;
  
  @Comment("性别")
  @Column("sex")
  private Integer sex;
  
  @Comment("种族编号（1人 2仙 3魔 4鬼 5龙）")
  @Column("race")
  private Integer race;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
  
  public void setPartnerId(Integer partnerId) {
    this.partnerId = partnerId;
  }
  
  public void setIcon(Integer icon) {
    this.icon = icon;
  }
  
  public void setPartnerType(Integer partnerType) {
    this.partnerType = partnerType;
  }
  
  public void setPartnerName(String partnerName) {
    this.partnerName = partnerName;
  }
  
  public void setSex(Integer sex) {
    this.sex = sex;
  }
  
  public void setRace(Integer race) {
    this.race = race;
  }
  
  public void setPartnerLv(short partnerLv) {
    this.partnerLv = partnerLv;
  }
  
  public void setSkillBoxJson(String skillBoxJson) {
    this.skillBoxJson = skillBoxJson;
  }
  
  public void setBase(String base) {
    this.base = base;
  }
  
  public void setCurrLife(Integer currLife) {
    this.currLife = currLife;
  }
  
  public void setMaxLife(Integer maxLife) {
    this.maxLife = maxLife;
  }
  
  public void setIsDel(int isDel) {
    this.isDel = isDel;
  }
  
  public void setPartnerOrder(byte partnerOrder) {
    this.partnerOrder = partnerOrder;
  }
  
  public void setIsFight(byte isFight) {
    this.isFight = isFight;
  }
  
  public void setOperDefaultSkillId(int operDefaultSkillId) {
    this.operDefaultSkillId = operDefaultSkillId;
  }
  
  public void setExp(long exp) {
    this.exp = exp;
  }
  
  public void setRebirth(byte rebirth) {
    this.rebirth = rebirth;
  }
  
  public void setIsRecruits(byte isRecruits) {
    this.isRecruits = isRecruits;
  }
  
  public void setSkillBox(SkillBox skillBox) {
    this.skillBox = skillBox;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Integer getRoleId() {
    return this.roleId;
  }
  
  public Integer getPartnerId() {
    return this.partnerId;
  }
  
  public Integer getIcon() {
    return this.icon;
  }
  
  public Integer getPartnerType() {
    return this.partnerType;
  }
  
  public String getPartnerName() {
    return this.partnerName;
  }
  
  public Integer getSex() {
    return this.sex;
  }
  
  public Integer getRace() {
    return this.race;
  }
  
  @Comment("伙伴等级")
  @Column("partner_lv")
  private short partnerLv = 0;
  
  @Comment("伙伴技能列表")
  @Column("skill_box_json")
  private String skillBoxJson;
  
  @Comment("成长属性{气血-法力-攻击-速度-成长率-品阶}")
  @Column("base")
  private String base;
  
  @Comment("气血值")
  @Column("curr_life")
  private Integer currLife;
  
  @Comment("最大气血值")
  @Column("max_life")
  private Integer maxLife;
  
  @Comment("是否删除 -1 是")
  @Column("is_del")
  private int isDel;
  
  @Comment("顺序")
  @Column("partner_order")
  private byte partnerOrder;
  
  @Comment("是否参战:1=参战，0=休息")
  @Column("is_fight")
  private byte isFight;
  
  @Comment("默认使用的技能")
  @Column("oper_default_skill_id")
  private int operDefaultSkillId;
  
  public short getPartnerLv() {
    return this.partnerLv;
  }
  
  public String getSkillBoxJson() {
    return this.skillBoxJson;
  }
  
  public String getBase() {
    return this.base;
  }
  
  public Integer getCurrLife() {
    return this.currLife;
  }
  
  public Integer getMaxLife() {
    return this.maxLife;
  }
  
  public int getIsDel() {
    return this.isDel;
  }
  
  public byte getPartnerOrder() {
    return this.partnerOrder;
  }
  
  public byte getIsFight() {
    return this.isFight;
  }
  
  public int getOperDefaultSkillId() {
    return this.operDefaultSkillId;
  }
  
  @Comment("经验值")
  @Column("exp")
  private long exp = 0L;
  
  public long getExp() {
    return this.exp;
  }
  
  @Comment("伙伴转生次数")
  @Column("rebirth")
  private byte rebirth = 0;
  
  public byte getRebirth() {
    return this.rebirth;
  }
  
  @Comment("是否招募，0=否，1=是")
  @Column("is_recruits")
  private byte isRecruits = 0;
  
  public byte getIsRecruits() {
    return this.isRecruits;
  }
  
  private SkillBox skillBox = new SkillBox();
  
  public SkillBox getSkillBox() {
    String json = getSkillBoxJson();
    SkillBox skillBox = null;
    if (StringUtils.isNotEmpty(json)) {
      String content = ZipUtil.uncompress(json);
      skillBox = (SkillBox)JsonUtils.string2Object(content, SkillBox.class);
    } 
    return skillBox;
  }
  
  public void save() {
    Db4PartnerService.getInstance().add2Queue(this);
  }
}
