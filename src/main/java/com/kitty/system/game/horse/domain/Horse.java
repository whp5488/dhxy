package com.kitty.system.game.horse.domain;

import com.kitty.system.game.player.serializer.HorseSerializerUtil;
import com.kitty.common.db.Db4HorseService;
import java.util.HashMap;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("horse")
public class Horse extends BaseEntity<Long>
{
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Comment("角色ID")
    @Column("r_id")
    private long rId;
    @Comment("坐骑名称")
    @Column("name")
    private String name;
    @Comment("坐骑等级")
    @Column("level")
    private short level;
    @Comment("坐骑编号")
    @Column("serial")
    private int serial;
    @Comment("坐骑经验")
    @Column("exp")
    private long exp;
    @Comment("成长率")
    @Column("grow_up")
    private int growUp;
    @Comment("移动速度")
    @Column("move_sp")
    private int moveSp;
    @Comment("坐骑体力")
    @Column("vit")
    private int vit;
    @Comment("坐骑评分")
    @Column("score")
    private int score;
    @Comment("成长丹使用次数")
    @Column("growth_dan")
    private byte growthDan;
    @Comment("技能<技能编号，经验>")
    @Column("skill")
    private String skillJson;
    @Comment("洗练未替换的成长属性")
    @Column("dark_base")
    @ColDefine(customType = "longtext")
    private String darkBase;
    @Comment("骑乘状态：0.否 1.是")
    @Column("state")
    private byte state;
    @Comment("坐骑位置")
    @Column("seat")
    private byte seat;
    IdentityHashMap<Integer, Long> skill;
    private Map<Byte, List<HorseBase>> darkBaseMap;
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public long getrId() {
        return this.rId;
    }
    
    public void setrId(long rId) {
        this.rId = rId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public void setLevel(short level) {
        this.level = level;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public void setSerial(int serial) {
        this.serial = serial;
    }
    
    public long getExp() {
        return this.exp;
    }
    
    public void setExp(long exp) {
        this.exp = exp;
    }
    
    public int getGrowUp() {
        return this.growUp;
    }
    
    public void setGrowUp(int growUp) {
        this.growUp = growUp;
    }
    
    public int getMoveSp() {
        return this.moveSp;
    }
    
    public void setMoveSp(int moveSp) {
        this.moveSp = moveSp;
    }
    
    public int getVit() {
        return this.vit;
    }
    
    public void setVit(int vit) {
        this.vit = vit;
    }
    
    public int getScore() {
        return this.score;
    }
    
    public void setScore(int score) {
        this.score = score;
    }
    
    public byte getGrowthDan() {
        return this.growthDan;
    }
    
    public void setGrowthDan(byte growthDan) {
        this.growthDan = growthDan;
    }
    
    public String getSkillJson() {
        return this.skillJson;
    }
    
    public void setSkillJson(String skillJson) {
        this.skillJson = skillJson;
    }
    
    public IdentityHashMap<Integer, Long> getSkill() {
        return this.skill;
    }
    
    public void setSkill(IdentityHashMap<Integer, Long> skill) {
        this.skill = skill;
    }
    
    public String getDarkBase() {
        return this.darkBase;
    }
    
    public void setDarkBase(String darkBase) {
        this.darkBase = darkBase;
    }
    
    public Map<Byte, List<HorseBase>> getDarkBaseMap() {
        return this.darkBaseMap;
    }
    
    public void setDarkBaseMap(Map<Byte, List<HorseBase>> darkBaseMap) {
        this.darkBaseMap = darkBaseMap;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public void setState(byte state) {
        this.state = state;
    }
    
    public byte getSeat() {
        return this.seat;
    }
    
    public void setSeat(byte seat) {
        this.seat = seat;
    }
    
    public Horse() {
        this.vit = HorseDataPool.horseVitMax;
        this.score = 0;
        this.growthDan = 0;
        this.state = 0;
        this.skill = new IdentityHashMap(3);
        this.darkBaseMap = new HashMap();
    }
    
    public Horse(Long id, long rId, short level, int serial, String name, long exp, int growUp, int moveSp, byte seat) {
        this.vit = HorseDataPool.horseVitMax;
        this.score = 0;
        this.growthDan = 0;
        this.state = 0;
        this.skill = new IdentityHashMap(3);
        this.darkBaseMap = new HashMap();
        this.id = id;
        this.rId = rId;
        this.level = level;
        this.serial = serial;
        this.name = name;
        this.exp = exp;
        this.growUp = growUp;
        this.moveSp = moveSp;
        this.seat = seat;
    }
    
    public Horse(Long id, long rId, short level, int serial, String name, long exp, int growUp, int moveSp, int vit, int score, byte growthDan, IdentityHashMap<Integer, Long> skill, String darkBase, Map<Byte, List<HorseBase>> darkBaseMap, byte seat) {
        this.vit = HorseDataPool.horseVitMax;
        this.score = 0;
        this.growthDan = 0;
        this.state = 0;
        this.skill = new IdentityHashMap(3);
        this.darkBaseMap = new HashMap();
        this.id = id;
        this.rId = rId;
        this.level = level;
        this.serial = serial;
        this.name = name;
        this.exp = exp;
        this.growUp = growUp;
        this.moveSp = moveSp;
        this.vit = vit;
        this.score = score;
        this.growthDan = growthDan;
        this.skill = skill;
        this.darkBase = darkBase;
        this.darkBaseMap = darkBaseMap;
        this.seat = seat;
    }
    
    public void save() {
        Db4HorseService.getInstance().add2Queue(this);
    }
    
    @Override
    public void doAfterInit() {
        HorseSerializerUtil.deserialize(this);
    }
    
    @Override
    public void doBeforeSave() {
        HorseSerializerUtil.serialize(this);
    }
}
