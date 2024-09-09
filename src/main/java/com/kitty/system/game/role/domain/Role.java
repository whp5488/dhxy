package com.kitty.system.game.role.domain;

import com.kitty.system.game.player.serializer.PlayerSerializerUtil;
import com.kitty.common.db.Db4PlayerService;
import org.codehaus.jackson.annotate.JsonIgnore;
import java.util.HashMap;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.friend.domain.PalNotify;
import com.kitty.system.game.friend.domain.PalFriendGroup;
import com.kitty.system.game.friend.domain.PalFriend;
import org.springframework.data.annotation.Transient;
import com.kitty.system.game.player.bean.PlayerRest;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import com.kitty.system.game.player.bean.TaskProgress;
import com.kitty.system.game.task.domain.RoleTask;
import com.kitty.system.game.player.bean.PkInfoBox;
import com.kitty.common.model.Pos;
import org.nutz.dao.entity.annotation.Default;
import com.alibaba.fastjson.annotation.JSONField;
import org.nutz.dao.entity.annotation.ColDefine;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Map;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Comment;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseEntity<Long>
{
    private static final long serialVersionUID = 1L;
    @Comment("主键")
    @Id(auto = false)
    @Column("id")
    private Long id;
    @Comment("角色面板ID")
    @Column("role_id")
    private Integer roleId;
    @Comment("账号标识")
    @Column("s_id")
    private String sid;
    @Comment("角色名称")
    @Column("name")
    private String name;
    @Comment("角色性别（1 男，2 女 ）")
    @Column("sex")
    private Integer sex;
    @Comment("种族编号（1人 2仙 3魔 4鬼 5龙）")
    @Column("race")
    private Integer race;
    @Comment("角色等级")
    @Column("level")
    private Integer level;
    @Comment("角色转生")
    @Column("relive")
    private Integer relive;
    @Comment("角色经验")
    @Column("exp")
    private long exp;
    @Comment("储存气血")
    @Column("save_life")
    private Long saveLife;
    @Comment("储存法力")
    @Column("save_mana")
    private Long saveMana;
    @Comment("元宝（仙玉）")
    @Column("gold")
    private Long gold;
    @Comment("累计充值")
    @Column("total_charge")
    private int tc;
    @Comment("累计充值仙玉")
    @Column("total_gold")
    private long totalGold;
    @Comment("银币（银元宝）")
    @Column("silver")
    private Long silver;
    @Comment("铜币（师贡）")
    @Column("coin")
    private Long coin;
    @Comment("角色外观")
    @Column("icon")
    private int icon;
    @Comment("已穿戴的武器编号")
    @Column("weapon_icon")
    private int weaponIcon;
    @Comment("变身卡编号：0.未使用")
    @Column("masquerade")
    private int masquerade;
    @Comment("变身卡到期时间")
    @Column("card_expire_time")
    private int cardExpireTime;
    @Comment("染色")
    @Column("color")
    private String color;
    @Comment("角色外观编号")
    @Column("serial")
    private Integer serial;
    @Comment("VIP等级")
    @Column("vip")
    private Integer vip;
    @Comment("激活称谓")
    @Column("curr_title")
    private int currTitle;
    @Comment("基础根骨")
    @Column("gg")
    private Integer gg;
    @Comment("基础灵性")
    @Column("lx")
    private Integer lx;
    @Comment("基础力量")
    @Column("ll")
    private Integer ll;
    @Comment("基础敏捷")
    @Column("mj")
    private Integer mj;
    @Comment("未分配点数")
    @Column("fpoint")
    private Integer fpoint;
    @Comment("气血值")
    @Column("curr_life")
    private Integer currLife;
    @Comment("最大气血值")
    @Column("max_life")
    private Integer maxLife;
    @Comment("法力值")
    @Column("curr_mana")
    private Integer currMana;
    @Comment("最大法力值")
    @Column("max_mana")
    private Integer maxMana;
    @Comment("玩家技能列表")
    @Column("skill_box_json")
    @ColDefine(customType = "longtext")
    private Map<Integer, RoleSkill> skills;
    @JSONField(serialize = false)
    @Comment("当前所在地图信息")
    @Column("pos_json")
    private String posJson;
    @Comment("帮派编号（0未加入帮派）")
    @Column("faction")
    private Integer faction;
    @JSONField(serialize = false)
    @Comment("任务数据")
    @Column("task_json")
    @ColDefine(customType = "longtext")
    private String taskJson;
    @JSONField(serialize = false)
    @Comment("任务进度数据")
    @Column("task_progress_json")
    @ColDefine(customType = "longtext")
    private String taskProgressJson;
    @JSONField(serialize = false)
    @Comment("数据杂项仓库")
    @Column("extend_box_json")
    @ColDefine(customType = "longtext")
    private String extendBoxJson;
    @JSONField(serialize = false)
    @Comment("其他数据杂项")
    @Column("rest_json")
    @ColDefine(customType = "longtext")
    private String restJson;
    @JSONField(serialize = false)
    @Comment("离线通知")
    @Column("offline_notice_json")
    @ColDefine(customType = "longtext")
    private String offlinNoticeJson;
    @Comment("安全锁")
    @Column("safe_pwd")
    private String safePwd;
    @Comment("玩家每日重置时间")
    @Column("daily_five_reset_tim")
    private Long dailyFiveResetTime;
    @Comment("玩家每周重置时间")
    @Column("weekly_reset_time")
    private long weeklyResetTime;
    @Comment("玩家每月重置时间")
    @Column("month_reset_time")
    private long monthResetTime;
    @Comment("禁言时间")
    @Column("word_time")
    @Default("0")
    private Long WordTime;
    @Comment("封号截止时间")
    @Column("lock_time")
    private Long lockTime;
    @Comment("本次登录时间")
    @Column("enter_time")
    private Long enterTime;
    @Comment("离线时间")
    @Column("of_line_time")
    private Long ofLineTime;
    @Column("pre_online_sec")
    @Comment("之前累计当天在线时长秒数")
    private Long preOnlineSec;
    @Column("marking")
    @Comment("区组标识")
    private String marking;
    @Column("create_time")
    @Comment("创建时间")
    private long createTime;
    @Comment("安全锁强制解锁开始时间")
    @ColDefine(customType = "int", width = 6)
    @Default("0")
    @Column("safe_reset_start_time")
    private int SafeResetStartTime;
    private RoleOfflineNotice offlineNotice;
    private Pos pos;
    private PkInfoBox pkInfoBox;
    private RoleTask task;
    private TaskProgress taskProgress;
    private PlayerExtendBox extendBox;
    private PlayerRest rest;
    @Transient
    private boolean unlock;
    @JSONField(serialize = false)
    @Transient
    private String mobile;
    @Transient
    private Map<String, Integer> expireTimeMap;
    @Transient
    private int lastDead;
    @Transient
    private long prevFightEndTime;
    @Transient
    private long prevHeartTime;
    @Transient
    private long onlineLoginTime;
    @Transient
    private int visible;
    @JSONField(serialize = false)
    @Transient
    private Map<Long, PalFriend> friendMap;
    @JSONField(serialize = false)
    @Transient
    private Map<Long, PalFriendGroup> friendGroupMap;
    @JSONField(serialize = false)
    @Transient
    private Map<Long, PalNotify> palNotifyMap;
    @Transient
    private int state;
    @Transient
    private long prevMoveTime;
    @Transient
    private int distributeKey;
    @Transient
    private RoleConfirm confirm;
    @Transient
    private int house;
    @Transient
    private volatile Boolean canFight;
    @Transient
    private transient Map<String, Object> tempCache;
    @Transient
    private Map<Integer, Integer> heartCounts;
    
    public Role() {
        this.relive = Integer.valueOf(0);
        this.exp = 0L;
        this.gold = Long.valueOf(0L);
        this.tc = 0;
        this.totalGold = 0L;
        this.silver = Long.valueOf(0L);
        this.coin = Long.valueOf(0L);
        this.weaponIcon = 0;
        this.masquerade = 0;
        this.cardExpireTime = 0;
        this.vip = Integer.valueOf(0);
        this.currTitle = -1;
        this.gg = Integer.valueOf(0);
        this.lx = Integer.valueOf(0);
        this.ll = Integer.valueOf(0);
        this.mj = Integer.valueOf(0);
        this.fpoint = Integer.valueOf(0);
        this.skills = new HashMap();
        this.faction = Integer.valueOf(0);
        this.dailyFiveResetTime = Long.valueOf(0L);
        this.weeklyResetTime = 0L;
        this.monthResetTime = 0L;
        this.WordTime = Long.valueOf(0L);
        this.lockTime = Long.valueOf(0L);
        this.ofLineTime = Long.valueOf(0L);
        this.preOnlineSec = Long.valueOf(0L);
        this.createTime = System.currentTimeMillis();
        this.SafeResetStartTime = 0;
        this.offlineNotice = new RoleOfflineNotice();
        this.pos = new Pos();
        this.pkInfoBox = new PkInfoBox();
        this.task = new RoleTask();
        this.taskProgress = new TaskProgress();
        this.extendBox = new PlayerExtendBox();
        this.rest = new PlayerRest();
        this.unlock = false;
        this.expireTimeMap = new HashMap<String, Integer>() {
            {
                this.put("incenseExpireTime", Integer.valueOf(0));
            }
        };
        this.lastDead = 1;
        this.visible = 1;
        this.friendMap = new HashMap();
        this.friendGroupMap = new HashMap();
        this.palNotifyMap = new HashMap();
        this.state = 0;
        this.canFight = Boolean.valueOf(true);
        this.heartCounts = new HashMap(4);
    }
    
    @JsonIgnore
    @Override
    public Long getId() {
        return this.id;
    }
    
    public Object pushTempCache(String key, Object value) {
        if (this.tempCache == null) {
            this.tempCache = new HashMap(4);
        }
        return this.tempCache.put(key, value);
    }
    
    public <T> T popTempCache(String key) {
        if (this.tempCache == null) {
            return null;
        }
        return (T)this.tempCache.remove(key);
    }
    
    @JsonIgnore
    public <T> T getTempCache(String key, T defaultValue) {
        if (this.tempCache == null) {
            return defaultValue;
        }
        return (T)this.tempCache.getOrDefault(key, defaultValue);
    }
    
    public void setPos(int x, int y, int dir, int mapId) {
        this.pos.setX(Integer.valueOf(x));
        this.pos.setY(Integer.valueOf(y));
        this.pos.setDir(Integer.valueOf(dir));
        this.pos.setMapId(Integer.valueOf(mapId));
    }
    
    public void save() {
        Db4PlayerService.getInstance().add2Queue(this);
    }
    
    @Override
    public void doAfterInit() {
        PlayerSerializerUtil.deserialize(this);
    }
    
    @Override
    public void doBeforeSave() {
        PlayerSerializerUtil.serialize(this);
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setRoleId(final Integer roleId) {
        this.roleId = roleId;
    }
    
    public void setSid(final String sid) {
        this.sid = sid;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setSex(final Integer sex) {
        this.sex = sex;
    }
    
    public void setRace(final Integer race) {
        this.race = race;
    }
    
    public void setLevel(final Integer level) {
        this.level = level;
    }
    
    public void setRelive(final Integer relive) {
        this.relive = relive;
    }
    
    public void setExp(final long exp) {
        this.exp = exp;
    }
    
    public void setSaveLife(final Long saveLife) {
        this.saveLife = saveLife;
    }
    
    public void setSaveMana(final Long saveMana) {
        this.saveMana = saveMana;
    }
    
    public void setGold(final Long gold) {
        this.gold = gold;
    }
    
    public void setTc(final int tc) {
        this.tc = tc;
    }
    
    public void setTotalGold(final long totalGold) {
        this.totalGold = totalGold;
    }
    
    public void setSilver(final Long silver) {
        this.silver = silver;
    }
    
    public void setCoin(final Long coin) {
        this.coin = coin;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setWeaponIcon(final int weaponIcon) {
        this.weaponIcon = weaponIcon;
    }
    
    public void setMasquerade(final int masquerade) {
        this.masquerade = masquerade;
    }
    
    public void setCardExpireTime(final int cardExpireTime) {
        this.cardExpireTime = cardExpireTime;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setVip(final Integer vip) {
        this.vip = vip;
    }
    
    public void setCurrTitle(final int currTitle) {
        this.currTitle = currTitle;
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
    
    public void setFpoint(final Integer fpoint) {
        this.fpoint = fpoint;
    }
    
    public void setCurrLife(final Integer currLife) {
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
    
    public void setSkills(final Map<Integer, RoleSkill> skills) {
        this.skills = skills;
    }
    
    public void setPosJson(final String posJson) {
        this.posJson = posJson;
    }
    
    public void setFaction(final Integer faction) {
        this.faction = faction;
    }
    
    public void setTaskJson(final String taskJson) {
        this.taskJson = taskJson;
    }
    
    public void setTaskProgressJson(final String taskProgressJson) {
        this.taskProgressJson = taskProgressJson;
    }
    
    public void setExtendBoxJson(final String extendBoxJson) {
        this.extendBoxJson = extendBoxJson;
    }
    
    public void setRestJson(final String restJson) {
        this.restJson = restJson;
    }
    
    public void setOfflinNoticeJson(final String offlinNoticeJson) {
        this.offlinNoticeJson = offlinNoticeJson;
    }
    
    public void setSafePwd(final String safePwd) {
        this.safePwd = safePwd;
    }
    
    public void setDailyFiveResetTime(final Long dailyFiveResetTime) {
        this.dailyFiveResetTime = dailyFiveResetTime;
    }
    
    public void setWeeklyResetTime(final long weeklyResetTime) {
        this.weeklyResetTime = weeklyResetTime;
    }
    
    public void setMonthResetTime(final long monthResetTime) {
        this.monthResetTime = monthResetTime;
    }
    
    public void setWordTime(final Long WordTime) {
        this.WordTime = WordTime;
    }
    
    public void setLockTime(final Long lockTime) {
        this.lockTime = lockTime;
    }
    
    public void setEnterTime(final Long enterTime) {
        this.enterTime = enterTime;
    }
    
    public void setOfLineTime(final Long ofLineTime) {
        this.ofLineTime = ofLineTime;
    }
    
    public void setPreOnlineSec(final Long preOnlineSec) {
        this.preOnlineSec = preOnlineSec;
    }
    
    public void setMarking(final String marking) {
        this.marking = marking;
    }
    
    public void setCreateTime(final long createTime) {
        this.createTime = createTime;
    }
    
    public void setSafeResetStartTime(final int SafeResetStartTime) {
        this.SafeResetStartTime = SafeResetStartTime;
    }
    
    public void setOfflineNotice(final RoleOfflineNotice offlineNotice) {
        this.offlineNotice = offlineNotice;
    }
    
    public void setPos(final Pos pos) {
        this.pos = pos;
    }
    
    public void setPkInfoBox(final PkInfoBox pkInfoBox) {
        this.pkInfoBox = pkInfoBox;
    }
    
    public void setTask(final RoleTask task) {
        this.task = task;
    }
    
    public void setTaskProgress(final TaskProgress taskProgress) {
        this.taskProgress = taskProgress;
    }
    
    public void setExtendBox(final PlayerExtendBox extendBox) {
        this.extendBox = extendBox;
    }
    
    public void setRest(final PlayerRest rest) {
        this.rest = rest;
    }
    
    public void setUnlock(final boolean unlock) {
        this.unlock = unlock;
    }
    
    public void setMobile(final String mobile) {
        this.mobile = mobile;
    }
    
    public void setExpireTimeMap(final Map<String, Integer> expireTimeMap) {
        this.expireTimeMap = expireTimeMap;
    }
    
    public void setLastDead(final int lastDead) {
        this.lastDead = lastDead;
    }
    
    public void setPrevFightEndTime(final long prevFightEndTime) {
        this.prevFightEndTime = prevFightEndTime;
    }
    
    public void setPrevHeartTime(final long prevHeartTime) {
        this.prevHeartTime = prevHeartTime;
    }
    
    public void setOnlineLoginTime(final long onlineLoginTime) {
        this.onlineLoginTime = onlineLoginTime;
    }
    
    public void setVisible(final int visible) {
        this.visible = visible;
    }
    
    public void setFriendMap(final Map<Long, PalFriend> friendMap) {
        this.friendMap = friendMap;
    }
    
    public void setFriendGroupMap(final Map<Long, PalFriendGroup> friendGroupMap) {
        this.friendGroupMap = friendGroupMap;
    }
    
    public void setPalNotifyMap(final Map<Long, PalNotify> palNotifyMap) {
        this.palNotifyMap = palNotifyMap;
    }
    
    public void setState(final int state) {
        this.state = state;
    }
    
    public void setPrevMoveTime(final long prevMoveTime) {
        this.prevMoveTime = prevMoveTime;
    }
    
    public void setDistributeKey(final int distributeKey) {
        this.distributeKey = distributeKey;
    }
    
    public void setConfirm(final RoleConfirm confirm) {
        this.confirm = confirm;
    }
    
    public void setHouse(final int house) {
        this.house = house;
    }
    
    public void setCanFight(final Boolean canFight) {
        this.canFight = canFight;
    }
    
    public void setTempCache(final Map<String, Object> tempCache) {
        this.tempCache = tempCache;
    }
    
    public void setHeartCounts(final Map<Integer, Integer> heartCounts) {
        this.heartCounts = heartCounts;
    }
    
    public Integer getRoleId() {
        return this.roleId;
    }
    
    public String getSid() {
        return this.sid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Integer getSex() {
        return this.sex;
    }
    
    public Integer getRace() {
        return this.race;
    }
    
    public Integer getLevel() {
        return this.level;
    }
    
    public Integer getRelive() {
        return this.relive;
    }
    
    public long getExp() {
        return this.exp;
    }
    
    public Long getSaveLife() {
        return this.saveLife;
    }
    
    public Long getSaveMana() {
        return this.saveMana;
    }
    
    public Long getGold() {
        return this.gold;
    }
    
    public int getTc() {
        return this.tc;
    }
    
    public long getTotalGold() {
        return this.totalGold;
    }
    
    public Long getSilver() {
        return this.silver;
    }
    
    public Long getCoin() {
        return this.coin;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public int getWeaponIcon() {
        return this.weaponIcon;
    }
    
    public int getMasquerade() {
        return this.masquerade;
    }
    
    public int getCardExpireTime() {
        return this.cardExpireTime;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public Integer getVip() {
        return this.vip;
    }
    
    public int getCurrTitle() {
        return this.currTitle;
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
    
    public Integer getFpoint() {
        return this.fpoint;
    }
    
    public Integer getCurrLife() {
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
    
    public Map<Integer, RoleSkill> getSkills() {
        return this.skills;
    }
    
    public String getPosJson() {
        return this.posJson;
    }
    
    public Integer getFaction() {
        return this.faction;
    }
    
    public String getTaskJson() {
        return this.taskJson;
    }
    
    public String getTaskProgressJson() {
        return this.taskProgressJson;
    }
    
    public String getExtendBoxJson() {
        return this.extendBoxJson;
    }
    
    public String getRestJson() {
        return this.restJson;
    }
    
    public String getOfflinNoticeJson() {
        return this.offlinNoticeJson;
    }
    
    public String getSafePwd() {
        return this.safePwd;
    }
    
    public Long getDailyFiveResetTime() {
        return this.dailyFiveResetTime;
    }
    
    public long getWeeklyResetTime() {
        return this.weeklyResetTime;
    }
    
    public long getMonthResetTime() {
        return this.monthResetTime;
    }
    
    public Long getWordTime() {
        return this.WordTime;
    }
    
    public Long getLockTime() {
        return this.lockTime;
    }
    
    public Long getEnterTime() {
        return this.enterTime;
    }
    
    public Long getOfLineTime() {
        return this.ofLineTime;
    }
    
    public Long getPreOnlineSec() {
        return this.preOnlineSec;
    }
    
    public String getMarking() {
        return this.marking;
    }
    
    public long getCreateTime() {
        return this.createTime;
    }
    
    public int getSafeResetStartTime() {
        return this.SafeResetStartTime;
    }
    
    public RoleOfflineNotice getOfflineNotice() {
        return this.offlineNotice;
    }
    
    public Pos getPos() {
        return this.pos;
    }
    
    public PkInfoBox getPkInfoBox() {
        return this.pkInfoBox;
    }
    
    public RoleTask getTask() {
        return this.task;
    }
    
    public TaskProgress getTaskProgress() {
        return this.taskProgress;
    }
    
    public PlayerExtendBox getExtendBox() {
        return this.extendBox;
    }
    
    public PlayerRest getRest() {
        return this.rest;
    }
    
    public boolean isUnlock() {
        return this.unlock;
    }
    
    public String getMobile() {
        return this.mobile;
    }
    
    public Map<String, Integer> getExpireTimeMap() {
        return this.expireTimeMap;
    }
    
    public int getLastDead() {
        return this.lastDead;
    }
    
    public long getPrevFightEndTime() {
        return this.prevFightEndTime;
    }
    
    public long getPrevHeartTime() {
        return this.prevHeartTime;
    }
    
    public long getOnlineLoginTime() {
        return this.onlineLoginTime;
    }
    
    public int getVisible() {
        return this.visible;
    }
    
    public Map<Long, PalFriend> getFriendMap() {
        return this.friendMap;
    }
    
    public Map<Long, PalFriendGroup> getFriendGroupMap() {
        return this.friendGroupMap;
    }
    
    public Map<Long, PalNotify> getPalNotifyMap() {
        return this.palNotifyMap;
    }
    
    public int getState() {
        return this.state;
    }
    
    public long getPrevMoveTime() {
        return this.prevMoveTime;
    }
    
    public int getDistributeKey() {
        return this.distributeKey;
    }
    
    public RoleConfirm getConfirm() {
        return this.confirm;
    }
    
    public int getHouse() {
        return this.house;
    }
    
    public Boolean getCanFight() {
        return this.canFight;
    }
    
    public Map<String, Object> getTempCache() {
        return this.tempCache;
    }
    
    public Map<Integer, Integer> getHeartCounts() {
        return this.heartCounts;
    }
}
