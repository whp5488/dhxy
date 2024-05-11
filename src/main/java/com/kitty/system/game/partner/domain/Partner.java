
package com.kitty.system.game.partner.domain;

import com.kitty.common.db.Db4PartnerService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import com.kitty.system.game.skill.domain.SkillBox;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("partner")
public class Partner extends BaseEntity<Long>
{
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
    @Comment("伙伴等级")
    @Column("partner_lv")
    private short partnerLv;
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
    @Comment("经验值")
    @Column("exp")
    private long exp;
    @Comment("伙伴转生次数")
    @Column("rebirth")
    private byte rebirth;
    @Comment("是否招募，0=否，1=是")
    @Column("is_recruits")
    private byte isRecruits;
    private SkillBox skillBox;
    
    public Partner() {
        this.partnerLv = 0;
        this.exp = 0L;
        this.rebirth = 0;
        this.isRecruits = 0;
        this.skillBox = new SkillBox();
    }
    
    public SkillBox getSkillBox() {
        final String json = this.getSkillBoxJson();
        SkillBox skillBox = null;
        if (StringUtils.isNotEmpty((CharSequence)json)) {
            final String content = ZipUtil.uncompress(json);
            skillBox = JsonUtils.string2Object(content, SkillBox.class);
        }
        return skillBox;
    }
    
    public void save() {
        Db4PartnerService.getInstance().add2Queue(this);
    }
    
    @Override
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
    
    public long getExp() {
        return this.exp;
    }
    
    public byte getRebirth() {
        return this.rebirth;
    }
    
    public byte getIsRecruits() {
        return this.isRecruits;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setPartnerId(final Integer partnerId) {
        this.partnerId = partnerId;
    }
    
    public void setIcon(final Integer icon) {
        this.icon = icon;
    }
    
    public void setPartnerType(final Integer partnerType) {
        this.partnerType = partnerType;
    }
    
    public void setPartnerName(final String partnerName) {
        this.partnerName = partnerName;
    }
    
    public void setSex(final Integer sex) {
        this.sex = sex;
    }
    
    public void setRace(final Integer race) {
        this.race = race;
    }
    
    public void setPartnerLv(final short partnerLv) {
        this.partnerLv = partnerLv;
    }
    
    public void setSkillBoxJson(final String skillBoxJson) {
        this.skillBoxJson = skillBoxJson;
    }
    
    public void setBase(final String base) {
        this.base = base;
    }
    
    public void setCurrLife(final Integer currLife) {
        this.currLife = currLife;
    }
    
    public void setMaxLife(final Integer maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setIsDel(final int isDel) {
        this.isDel = isDel;
    }
    
    public void setPartnerOrder(final byte partnerOrder) {
        this.partnerOrder = partnerOrder;
    }
    
    public void setIsFight(final byte isFight) {
        this.isFight = isFight;
    }
    
    public void setOperDefaultSkillId(final int operDefaultSkillId) {
        this.operDefaultSkillId = operDefaultSkillId;
    }
    
    public void setExp(final long exp) {
        this.exp = exp;
    }
    
    public void setRebirth(final byte rebirth) {
        this.rebirth = rebirth;
    }
    
    public void setIsRecruits(final byte isRecruits) {
        this.isRecruits = isRecruits;
    }
    
    public void setSkillBox(final SkillBox skillBox) {
        this.skillBox = skillBox;
    }
}
