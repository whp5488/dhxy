
package com.kitty.system.game.pet.domain;

import com.kitty.system.game.player.serializer.PetSerializerUtil;
import com.kitty.common.db.Db4PetService;
import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.system.game.pet.domain.vo.PetCorrection;
import org.nutz.dao.entity.annotation.ColType;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.Map;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.lang.util.NutMap;
import java.util.List;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("pet")
public class Pet extends BaseEntity<Long>
{
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
    @Comment("宠物等级")
    @Column("pet_lv")
    private Integer petLv;
    @Comment("宠物转生次数")
    @Column("rebirth")
    private Integer rebirth;
    @Comment("未分配点数")
    @Column("fpoint")
    private Integer fpoint;
    @Comment("经验值")
    @Column("exp")
    private long exp;
    @Comment("亲密度")
    @Column("pet_close")
    private long petClose;
    @Comment("根骨")
    @Column("gg")
    private Integer gg;
    @Comment("灵性")
    @Column("lx")
    private Integer lx;
    @Comment("力量")
    @Column("ll")
    private Integer ll;
    @Comment("敏捷")
    @Column("mj")
    private Integer mj;
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
    @Comment("成长丹：0.未使用 1.已使用")
    @Column("growth_dan")
    private Integer growthDan;
    @Comment("成长属性{气血-法力-攻击-速度-成长率-品阶}")
    @Column("base")
    private String base;
    @Comment("是否洗练（0 未洗练）")
    @Column("is_base")
    private Integer isBase;
    @Comment("炼妖次数")
    @Column("lyn")
    private Integer lyn;
    @Comment("龙骨")
    @Column("bone")
    private Integer bone;
    @Comment("宠物飞升")
    @Column("fly")
    private Integer fly;
    @Comment("宠物飞升-自定义初始值: 0.根骨 1.灵性 2.力量 3.敏捷")
    @Column("fly_starter")
    private int flyStarter;
    @Comment("修炼属性属性信息")
    @Column("correction_info")
    private String correctionInfoJson;
    @Comment("宠物语言泡泡")
    @Column("msg")
    @Default("[]")
    @ColDefine(customType = "text")
    private List<NutMap> msgMap;
    @Comment("亲密丹增加的亲密")
    @Column("close_limit_json")
    @Default("[]")
    @ColDefine(customType = "text")
    private Map<String, Integer> closeLimitMap;
    @Comment("技能")
    @Column("skills_json")
    @Default("[]")
    @ColDefine(customType = "longtext")
    private List<PetSkill> skills;
    @Comment("修炼数据：修炼属性<修炼属性，当前加点>")
    @Column("correction_json")
    @Default("[]")
    @ColDefine(customType = "text")
    private Map<Integer, Integer> correction;
    @Comment("自动加点")
    @Column("auto_attrib_json")
    @Default("[]")
    @ColDefine(customType = "text")
    private Map<String, Integer> autoAttrib;
    @Comment("自动战斗相关设置")
    @Column("auto_fight_set")
    @Default("[]")
    @ColDefine(customType = "text")
    private Map<String, Object> autoFightSet;
    @Comment("key: 1.普通 2.高级 3.超级，list:洗练未替换的成长属性{成长率-气血-法力-攻击-速度}")
    @Column("dark_base")
    @ColDefine(customType = "longtext")
    @Default("[]")
    private Map<Integer, List<PetBase>> darkBaseMap;
    @Comment("染色 染色集合  List<Map<颜色编号/是否使用，值>>  a=角色编号  b=是否使用")
    @Column("color")
    @Default("[]")
    @ColDefine(customType = "text")
    private List<Map<String, Byte>> colorList;
    @Comment("饰品染色部位一 Map<颜色编号，是否使用>")
    @Column("acc_color")
    @ColDefine(customType = "text")
    private Map<Byte, Byte> accColor;
    @Comment("饰品染色部位二 Map<颜色编号，是否使用>")
    @Column("acc_color_two")
    @ColDefine(customType = "text")
    private Map<Byte, Byte> accColorTwo;
    @Comment("幻肤 Map<幻肤编号，是否使用>")
    @Column("brand_new")
    private Map<Integer, Byte> brandNew;
    @Comment("觉醒突破次数")
    @Column("awaken")
    private short awaken;
    @Comment("管制坐骑编号：0.则无管制")
    @Column("horse")
    private long horse;
    @Comment("绑定到期时间（-1则无）")
    @Column("bind_time")
    private int bindTime;
    @Comment("周驯养次数")
    @Default("0")
    @ColDefine(type = ColType.INT, width = 1)
    @Column("domesticate")
    private short domesticate;
    private PetBase petBase;
    private PetCorrection correctionInfo;
    
    public Pet() {
        this.petLv = 0;
        this.rebirth = 0;
        this.fpoint = 0;
        this.exp = 0L;
        this.petClose = 0L;
        this.gg = 0;
        this.lx = 0;
        this.ll = 0;
        this.mj = 0;
        this.growthDan = 0;
        this.lyn = 0;
        this.bone = 0;
        this.fly = 0;
        this.flyStarter = 0;
        this.msgMap = new ArrayList<NutMap>();
        this.closeLimitMap = new HashMap<String, Integer>();
        this.skills = new ArrayList<PetSkill>();
        this.correction = new HashMap<Integer, Integer>();
        this.autoAttrib = new HashMap<String, Integer>();
        this.autoFightSet = new HashMap<String, Object>();
        this.darkBaseMap = new HashMap<Integer, List<PetBase>>();
        this.colorList = new ArrayList<Map<String, Byte>>();
        this.accColor = new HashMap<Byte, Byte>();
        this.accColorTwo = new HashMap<Byte, Byte>();
        this.brandNew = new HashMap<Integer, Byte>();
        this.awaken = 0;
        this.horse = 0L;
        this.bindTime = -1;
        this.domesticate = 0;
        this.petBase = new PetBase();
        this.correctionInfo = new PetCorrection();
    }
    
    public void save() {
        Db4PetService.getInstance().add2Queue(this);
    }
    
    @Override
    public void doAfterInit() {
        PetSerializerUtil.deserialize(this);
    }
    
    @Override
    public void doBeforeSave() {
        PetSerializerUtil.serialize(this);
    }
    
    @Override
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
    
    public Integer getPetLv() {
        return this.petLv;
    }
    
    public Integer getRebirth() {
        return this.rebirth;
    }
    
    public Integer getFpoint() {
        return this.fpoint;
    }
    
    public long getExp() {
        return this.exp;
    }
    
    public long getPetClose() {
        return this.petClose;
    }
    
    public Integer getGg() {
        return this.gg;
    }
    
    public Integer getLx() {
        return this.lx;
    }
    
    public Integer getLl() {
        return this.ll;
    }
    
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
    
    public Integer getGrowthDan() {
        return this.growthDan;
    }
    
    public String getBase() {
        return this.base;
    }
    
    public Integer getIsBase() {
        return this.isBase;
    }
    
    public Integer getLyn() {
        return this.lyn;
    }
    
    public Integer getBone() {
        return this.bone;
    }
    
    public Integer getFly() {
        return this.fly;
    }
    
    public int getFlyStarter() {
        return this.flyStarter;
    }
    
    public String getCorrectionInfoJson() {
        return this.correctionInfoJson;
    }
    
    public List<NutMap> getMsgMap() {
        return this.msgMap;
    }
    
    public Map<String, Integer> getCloseLimitMap() {
        return this.closeLimitMap;
    }
    
    public List<PetSkill> getSkills() {
        return this.skills;
    }
    
    public Map<Integer, Integer> getCorrection() {
        return this.correction;
    }
    
    public Map<String, Integer> getAutoAttrib() {
        return this.autoAttrib;
    }
    
    public Map<String, Object> getAutoFightSet() {
        return this.autoFightSet;
    }
    
    public Map<Integer, List<PetBase>> getDarkBaseMap() {
        return this.darkBaseMap;
    }
    
    public List<Map<String, Byte>> getColorList() {
        return this.colorList;
    }
    
    public Map<Byte, Byte> getAccColor() {
        return this.accColor;
    }
    
    public Map<Byte, Byte> getAccColorTwo() {
        return this.accColorTwo;
    }
    
    public Map<Integer, Byte> getBrandNew() {
        return this.brandNew;
    }
    
    public short getAwaken() {
        return this.awaken;
    }
    
    public long getHorse() {
        return this.horse;
    }
    
    public int getBindTime() {
        return this.bindTime;
    }
    
    public short getDomesticate() {
        return this.domesticate;
    }
    
    public PetBase getPetBase() {
        return this.petBase;
    }
    
    public PetCorrection getCorrectionInfo() {
        return this.correctionInfo;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setPetId(final Integer petId) {
        this.petId = petId;
    }
    
    public void setShape(final Integer shape) {
        this.shape = shape;
    }
    
    public void setPetType(final Integer petType) {
        this.petType = petType;
    }
    
    public void setPetName(final String petName) {
        this.petName = petName;
    }
    
    public void setPetLv(final Integer petLv) {
        this.petLv = petLv;
    }
    
    public void setRebirth(final Integer rebirth) {
        this.rebirth = rebirth;
    }
    
    public void setFpoint(final Integer fpoint) {
        this.fpoint = fpoint;
    }
    
    public void setExp(final long exp) {
        this.exp = exp;
    }
    
    public void setPetClose(final long petClose) {
        this.petClose = petClose;
    }
    
    public void setGg(final Integer gg) {
        this.gg = gg;
    }
    
    public void setLx(final Integer lx) {
        this.lx = lx;
    }
    
    public void setLl(final Integer ll) {
        this.ll = ll;
    }
    
    public void setMj(final Integer mj) {
        this.mj = mj;
    }
    
    public void setCurrLife(final int currLife) {
        this.currLife = currLife;
    }
    
    public void setMaxLife(final Integer maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setCurrMana(final Integer currMana) {
        this.currMana = currMana;
    }
    
    public void setMaxMana(final Integer maxMana) {
        this.maxMana = maxMana;
    }
    
    public void setScore(final Integer score) {
        this.score = score;
    }
    
    public void setGrowthDan(final Integer growthDan) {
        this.growthDan = growthDan;
    }
    
    public void setBase(final String base) {
        this.base = base;
    }
    
    public void setIsBase(final Integer isBase) {
        this.isBase = isBase;
    }
    
    public void setLyn(final Integer lyn) {
        this.lyn = lyn;
    }
    
    public void setBone(final Integer bone) {
        this.bone = bone;
    }
    
    public void setFly(final Integer fly) {
        this.fly = fly;
    }
    
    public void setFlyStarter(final int flyStarter) {
        this.flyStarter = flyStarter;
    }
    
    public void setCorrectionInfoJson(final String correctionInfoJson) {
        this.correctionInfoJson = correctionInfoJson;
    }
    
    public void setMsgMap(final List<NutMap> msgMap) {
        this.msgMap = msgMap;
    }
    
    public void setCloseLimitMap(final Map<String, Integer> closeLimitMap) {
        this.closeLimitMap = closeLimitMap;
    }
    
    public void setSkills(final List<PetSkill> skills) {
        this.skills = skills;
    }
    
    public void setCorrection(final Map<Integer, Integer> correction) {
        this.correction = correction;
    }
    
    public void setAutoAttrib(final Map<String, Integer> autoAttrib) {
        this.autoAttrib = autoAttrib;
    }
    
    public void setAutoFightSet(final Map<String, Object> autoFightSet) {
        this.autoFightSet = autoFightSet;
    }
    
    public void setDarkBaseMap(final Map<Integer, List<PetBase>> darkBaseMap) {
        this.darkBaseMap = darkBaseMap;
    }
    
    public void setColorList(final List<Map<String, Byte>> colorList) {
        this.colorList = colorList;
    }
    
    public void setAccColor(final Map<Byte, Byte> accColor) {
        this.accColor = accColor;
    }
    
    public void setAccColorTwo(final Map<Byte, Byte> accColorTwo) {
        this.accColorTwo = accColorTwo;
    }
    
    public void setBrandNew(final Map<Integer, Byte> brandNew) {
        this.brandNew = brandNew;
    }
    
    public void setAwaken(final short awaken) {
        this.awaken = awaken;
    }
    
    public void setHorse(final long horse) {
        this.horse = horse;
    }
    
    public void setBindTime(final int bindTime) {
        this.bindTime = bindTime;
    }
    
    public void setDomesticate(final short domesticate) {
        this.domesticate = domesticate;
    }
    
    public void setPetBase(final PetBase petBase) {
        this.petBase = petBase;
    }
    
    public void setCorrectionInfo(final PetCorrection correctionInfo) {
        this.correctionInfo = correctionInfo;
    }
    
    @Override
    public String toString() {
        return "Pet(id=" + this.getId() + ", roleId=" + this.getRoleId() + ", petId=" + this.getPetId() + ", shape=" + this.getShape() + ", petType=" + this.getPetType() + ", petName=" + this.getPetName() + ", petLv=" + this.getPetLv() + ", rebirth=" + this.getRebirth() + ", fpoint=" + this.getFpoint() + ", exp=" + this.getExp() + ", petClose=" + this.getPetClose() + ", gg=" + this.getGg() + ", lx=" + this.getLx() + ", ll=" + this.getLl() + ", mj=" + this.getMj() + ", currLife=" + this.getCurrLife() + ", maxLife=" + this.getMaxLife() + ", currMana=" + this.getCurrMana() + ", maxMana=" + this.getMaxMana() + ", score=" + this.getScore() + ", growthDan=" + this.getGrowthDan() + ", base=" + this.getBase() + ", isBase=" + this.getIsBase() + ", lyn=" + this.getLyn() + ", bone=" + this.getBone() + ", fly=" + this.getFly() + ", flyStarter=" + this.getFlyStarter() + ", correctionInfoJson=" + this.getCorrectionInfoJson() + ", msgMap=" + this.getMsgMap() + ", closeLimitMap=" + this.getCloseLimitMap() + ", skills=" + this.getSkills() + ", correction=" + this.getCorrection() + ", autoAttrib=" + this.getAutoAttrib() + ", autoFightSet=" + this.getAutoFightSet() + ", darkBaseMap=" + this.getDarkBaseMap() + ", colorList=" + this.getColorList() + ", accColor=" + this.getAccColor() + ", accColorTwo=" + this.getAccColorTwo() + ", brandNew=" + this.getBrandNew() + ", awaken=" + this.getAwaken() + ", horse=" + this.getHorse() + ", bindTime=" + this.getBindTime() + ", domesticate=" + this.getDomesticate() + ", petBase=" + this.getPetBase() + ", correctionInfo=" + this.getCorrectionInfo() + ")";
    }
}
