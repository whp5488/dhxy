package com.kitty.system.game.role.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4PlayerService;
import com.kitty.common.model.Pos;
import com.kitty.system.game.confirm.model.RoleConfirm;
import com.kitty.system.game.friend.domain.PalFriend;
import com.kitty.system.game.friend.domain.PalFriendGroup;
import com.kitty.system.game.friend.domain.PalNotify;
import com.kitty.system.game.player.bean.PkInfoBox;
import com.kitty.system.game.player.bean.PlayerExtendBox;
import com.kitty.system.game.player.bean.PlayerRest;
import com.kitty.system.game.player.bean.TaskProgress;
import com.kitty.system.game.player.serializer.PlayerSerializerUtil;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.task.domain.RoleTask;
import java.util.HashMap;
import java.util.Map;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import org.springframework.data.annotation.Transient;

@Table("role")
@JsonIgnoreProperties(ignoreUnknown = true)
public class Role extends BaseEntity<Long> {
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
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setRoleId(Integer roleId) {
    this.roleId = roleId;
  }
  
  public void setSid(String sid) {
    this.sid = sid;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setSex(Integer sex) {
    this.sex = sex;
  }
  
  public void setRace(Integer race) {
    this.race = race;
  }
  
  public void setLevel(Integer level) {
    this.level = level;
  }
  
  public void setRelive(Integer relive) {
    this.relive = relive;
  }
  
  public void setExp(long exp) {
    this.exp = exp;
  }
  
  public void setSaveLife(Long saveLife) {
    this.saveLife = saveLife;
  }
  
  public void setSaveMana(Long saveMana) {
    this.saveMana = saveMana;
  }
  
  public void setGold(Long gold) {
    this.gold = gold;
  }
  
  public void setTc(int tc) {
    this.tc = tc;
  }
  
  public void setTotalGold(long totalGold) {
    this.totalGold = totalGold;
  }
  
  public void setSilver(Long silver) {
    this.silver = silver;
  }
  
  public void setCoin(Long coin) {
    this.coin = coin;
  }
  
  public void setIcon(int icon) {
    this.icon = icon;
  }
  
  public void setWeaponIcon(int weaponIcon) {
    this.weaponIcon = weaponIcon;
  }
  
  public void setMasquerade(int masquerade) {
    this.masquerade = masquerade;
  }
  
  public void setCardExpireTime(int cardExpireTime) {
    this.cardExpireTime = cardExpireTime;
  }
  
  public void setColor(String color) {
    this.color = color;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public void setVip(Integer vip) {
    this.vip = vip;
  }
  
  public void setCurrTitle(int currTitle) {
    this.currTitle = currTitle;
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
  
  public void setFpoint(Integer fpoint) {
    this.fpoint = fpoint;
  }
  
  public void setCurrLife(Integer currLife) {
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
  
  public void setSkills(Map<Integer, RoleSkill> skills) {
    this.skills = skills;
  }
  
  public void setPosJson(String posJson) {
    this.posJson = posJson;
  }
  
  public void setFaction(Integer faction) {
    this.faction = faction;
  }
  
  public void setTaskJson(String taskJson) {
    this.taskJson = taskJson;
  }
  
  public void setTaskProgressJson(String taskProgressJson) {
    this.taskProgressJson = taskProgressJson;
  }
  
  public void setExtendBoxJson(String extendBoxJson) {
    this.extendBoxJson = extendBoxJson;
  }
  
  public void setRestJson(String restJson) {
    this.restJson = restJson;
  }
  
  public void setOfflinNoticeJson(String offlinNoticeJson) {
    this.offlinNoticeJson = offlinNoticeJson;
  }
  
  public void setSafePwd(String safePwd) {
    this.safePwd = safePwd;
  }
  
  public void setDailyFiveResetTime(Long dailyFiveResetTime) {
    this.dailyFiveResetTime = dailyFiveResetTime;
  }
  
  public void setWeeklyResetTime(long weeklyResetTime) {
    this.weeklyResetTime = weeklyResetTime;
  }
  
  public void setMonthResetTime(long monthResetTime) {
    this.monthResetTime = monthResetTime;
  }
  
  public void setWordTime(Long WordTime) {
    this.WordTime = WordTime;
  }
  
  public void setLockTime(Long lockTime) {
    this.lockTime = lockTime;
  }
  
  public void setEnterTime(Long enterTime) {
    this.enterTime = enterTime;
  }
  
  public void setOfLineTime(Long ofLineTime) {
    this.ofLineTime = ofLineTime;
  }
  
  public void setPreOnlineSec(Long preOnlineSec) {
    this.preOnlineSec = preOnlineSec;
  }
  
  public void setMarking(String marking) {
    this.marking = marking;
  }
  
  public void setCreateTime(long createTime) {
    this.createTime = createTime;
  }
  
  public void setSafeResetStartTime(int SafeResetStartTime) {
    this.SafeResetStartTime = SafeResetStartTime;
  }
  
  public void setOfflineNotice(RoleOfflineNotice offlineNotice) {
    this.offlineNotice = offlineNotice;
  }
  
  public void setPos(Pos pos) {
    this.pos = pos;
  }
  
  public void setPkInfoBox(PkInfoBox pkInfoBox) {
    this.pkInfoBox = pkInfoBox;
  }
  
  public void setTask(RoleTask task) {
    this.task = task;
  }
  
  public void setTaskProgress(TaskProgress taskProgress) {
    this.taskProgress = taskProgress;
  }
  
  public void setExtendBox(PlayerExtendBox extendBox) {
    this.extendBox = extendBox;
  }
  
  public void setRest(PlayerRest rest) {
    this.rest = rest;
  }
  
  public void setUnlock(boolean unlock) {
    this.unlock = unlock;
  }
  
  public void setMobile(String mobile) {
    this.mobile = mobile;
  }
  
  public void setExpireTimeMap(Map<String, Integer> expireTimeMap) {
    this.expireTimeMap = expireTimeMap;
  }
  
  public void setLastDead(int lastDead) {
    this.lastDead = lastDead;
  }
  
  public void setPrevFightEndTime(long prevFightEndTime) {
    this.prevFightEndTime = prevFightEndTime;
  }
  
  public void setPrevHeartTime(long prevHeartTime) {
    this.prevHeartTime = prevHeartTime;
  }
  
  public void setOnlineLoginTime(long onlineLoginTime) {
    this.onlineLoginTime = onlineLoginTime;
  }
  
  public void setVisible(int visible) {
    this.visible = visible;
  }
  
  public void setFriendMap(Map<Long, PalFriend> friendMap) {
    this.friendMap = friendMap;
  }
  
  public void setFriendGroupMap(Map<Long, PalFriendGroup> friendGroupMap) {
    this.friendGroupMap = friendGroupMap;
  }
  
  public void setPalNotifyMap(Map<Long, PalNotify> palNotifyMap) {
    this.palNotifyMap = palNotifyMap;
  }
  
  public void setState(int state) {
    this.state = state;
  }
  
  public void setPrevMoveTime(long prevMoveTime) {
    this.prevMoveTime = prevMoveTime;
  }
  
  public void setDistributeKey(int distributeKey) {
    this.distributeKey = distributeKey;
  }
  
  public void setConfirm(RoleConfirm confirm) {
    this.confirm = confirm;
  }
  
  public void setHouse(int house) {
    this.house = house;
  }
  
  public void setCanFight(Boolean canFight) {
    this.canFight = canFight;
  }
  
  public void setTempCache(Map<String, Object> tempCache) {
    this.tempCache = tempCache;
  }
  
  public void setHeartCounts(Map<Integer, Integer> heartCounts) {
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
  
  @Comment("角色转生")
  @Column("relive")
  private Integer relive = Integer.valueOf(0);
  
  public Integer getRelive() {
    return this.relive;
  }
  
  @Comment("角色经验")
  @Column("exp")
  private long exp = 0L;
  
  @Comment("储存气血")
  @Column("save_life")
  private Long saveLife;
  
  @Comment("储存法力")
  @Column("save_mana")
  private Long saveMana;
  
  public long getExp() {
    return this.exp;
  }
  
  public Long getSaveLife() {
    return this.saveLife;
  }
  
  public Long getSaveMana() {
    return this.saveMana;
  }
  
  @Comment("元宝（仙玉）")
  @Column("gold")
  private Long gold = Long.valueOf(0L);
  
  public Long getGold() {
    return this.gold;
  }
  
  @Comment("累计充值")
  @Column("total_charge")
  private int tc = 0;
  
  public int getTc() {
    return this.tc;
  }
  
  @Comment("累计充值仙玉")
  @Column("total_gold")
  private long totalGold = 0L;
  
  public long getTotalGold() {
    return this.totalGold;
  }
  
  @Comment("银币（银元宝）")
  @Column("silver")
  private Long silver = Long.valueOf(0L);
  
  public Long getSilver() {
    return this.silver;
  }
  
  @Comment("铜币（师贡）")
  @Column("coin")
  private Long coin = Long.valueOf(0L);
  
  @Comment("角色外观")
  @Column("icon")
  private int icon;
  
  public Long getCoin() {
    return this.coin;
  }
  
  public int getIcon() {
    return this.icon;
  }
  
  @Comment("已穿戴的武器编号")
  @Column("weapon_icon")
  private int weaponIcon = 0;
  
  public int getWeaponIcon() {
    return this.weaponIcon;
  }
  
  @Comment("变身卡编号：0.未使用")
  @Column("masquerade")
  private int masquerade = 0;
  
  public int getMasquerade() {
    return this.masquerade;
  }
  
  @Comment("变身卡到期时间")
  @Column("card_expire_time")
  private int cardExpireTime = 0;
  
  @Comment("染色")
  @Column("color")
  private String color;
  
  @Comment("角色外观编号")
  @Column("serial")
  private Integer serial;
  
  public int getCardExpireTime() {
    return this.cardExpireTime;
  }
  
  public String getColor() {
    return this.color;
  }
  
  public Integer getSerial() {
    return this.serial;
  }
  
  @Comment("VIP等级")
  @Column("vip")
  private Integer vip = Integer.valueOf(0);
  
  public Integer getVip() {
    return this.vip;
  }
  
  @Comment("激活称谓")
  @Column("curr_title")
  private int currTitle = -1;
  
  public int getCurrTitle() {
    return this.currTitle;
  }
  
  @Comment("基础根骨")
  @Column("gg")
  private Integer gg = Integer.valueOf(0);
  
  public Integer getGg() {
    return this.gg;
  }
  
  @Comment("基础灵性")
  @Column("lx")
  private Integer lx = Integer.valueOf(0);
  
  public Integer getLx() {
    return this.lx;
  }
  
  @Comment("基础力量")
  @Column("ll")
  private Integer ll = Integer.valueOf(0);
  
  public Integer getLl() {
    return this.ll;
  }
  
  @Comment("基础敏捷")
  @Column("mj")
  private Integer mj = Integer.valueOf(0);
  
  public Integer getMj() {
    return this.mj;
  }
  
  @Comment("未分配点数")
  @Column("fpoint")
  private Integer fpoint = Integer.valueOf(0);
  
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
  
  @Comment("玩家技能列表")
  @Column("skill_box_json")
  @ColDefine(customType = "longtext")
  private Map<Integer, RoleSkill> skills = new HashMap<>();
  
  @JSONField(serialize = false)
  @Comment("当前所在地图信息")
  @Column("pos_json")
  private String posJson;
  
  public Map<Integer, RoleSkill> getSkills() {
    return this.skills;
  }
  
  public String getPosJson() {
    return this.posJson;
  }
  
  @Comment("帮派编号（0未加入帮派）")
  @Column("faction")
  private Integer faction = Integer.valueOf(0);
  
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
  
  @Comment("玩家每日重置时间")
  @Column("daily_five_reset_tim")
  private Long dailyFiveResetTime = Long.valueOf(0L);
  
  public Long getDailyFiveResetTime() {
    return this.dailyFiveResetTime;
  }
  
  @Comment("玩家每周重置时间")
  @Column("weekly_reset_time")
  private long weeklyResetTime = 0L;
  
  public long getWeeklyResetTime() {
    return this.weeklyResetTime;
  }
  
  @Comment("玩家每月重置时间")
  @Column("month_reset_time")
  private long monthResetTime = 0L;
  
  public long getMonthResetTime() {
    return this.monthResetTime;
  }
  
  @Comment("禁言时间")
  @Column("word_time")
  @Default("0")
  private Long WordTime = Long.valueOf(0L);
  
  public Long getWordTime() {
    return this.WordTime;
  }
  
  @Comment("封号截止时间")
  @Column("lock_time")
  private Long lockTime = Long.valueOf(0L);
  
  @Comment("本次登录时间")
  @Column("enter_time")
  private Long enterTime;
  
  public Long getLockTime() {
    return this.lockTime;
  }
  
  public Long getEnterTime() {
    return this.enterTime;
  }
  
  @Comment("离线时间")
  @Column("of_line_time")
  private Long ofLineTime = Long.valueOf(0L);
  
  public Long getOfLineTime() {
    return this.ofLineTime;
  }
  
  @Column("pre_online_sec")
  @Comment("之前累计当天在线时长秒数")
  private Long preOnlineSec = Long.valueOf(0L);
  
  @Column("marking")
  @Comment("区组标识")
  private String marking;
  
  public Long getPreOnlineSec() {
    return this.preOnlineSec;
  }
  
  public String getMarking() {
    return this.marking;
  }
  
  @Column("create_time")
  @Comment("创建时间")
  private long createTime = System.currentTimeMillis();
  
  public long getCreateTime() {
    return this.createTime;
  }
  
  @Comment("安全锁强制解锁开始时间")
  @ColDefine(customType = "int", width = 6)
  @Default("0")
  @Column("safe_reset_start_time")
  private int SafeResetStartTime = 0;
  
  public int getSafeResetStartTime() {
    return this.SafeResetStartTime;
  }
  
  private RoleOfflineNotice offlineNotice = new RoleOfflineNotice();
  
  public RoleOfflineNotice getOfflineNotice() {
    return this.offlineNotice;
  }
  
  private Pos pos = new Pos();
  
  public Pos getPos() {
    return this.pos;
  }
  
  private PkInfoBox pkInfoBox = new PkInfoBox();
  
  public PkInfoBox getPkInfoBox() {
    return this.pkInfoBox;
  }
  
  private RoleTask task = new RoleTask();
  
  public RoleTask getTask() {
    return this.task;
  }
  
  private TaskProgress taskProgress = new TaskProgress();
  
  public TaskProgress getTaskProgress() {
    return this.taskProgress;
  }
  
  private PlayerExtendBox extendBox = new PlayerExtendBox();
  
  public PlayerExtendBox getExtendBox() {
    return this.extendBox;
  }
  
  private PlayerRest rest = new PlayerRest();
  
  public PlayerRest getRest() {
    return this.rest;
  }
  
  @Transient
  private boolean unlock = false;
  
  @JSONField(serialize = false)
  @Transient
  private String mobile;
  
  public boolean isUnlock() {
    return this.unlock;
  }
  
  public String getMobile() {
    return this.mobile;
  }
  
  @Transient
  private Map<String, Integer> expireTimeMap = new HashMap<String, Integer>() {
    
    };
  
  public Map<String, Integer> getExpireTimeMap() {
    return this.expireTimeMap;
  }
  
  @Transient
  private int lastDead = 1;
  
  @Transient
  private long prevFightEndTime;
  
  @Transient
  private long prevHeartTime;
  
  @Transient
  private long onlineLoginTime;
  
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
  
  @Transient
  private int visible = 1;
  
  public int getVisible() {
    return this.visible;
  }
  
  @JSONField(serialize = false)
  @Transient
  private Map<Long, PalFriend> friendMap = new HashMap<>();
  
  public Map<Long, PalFriend> getFriendMap() {
    return this.friendMap;
  }
  
  @JSONField(serialize = false)
  @Transient
  private Map<Long, PalFriendGroup> friendGroupMap = new HashMap<>();
  
  public Map<Long, PalFriendGroup> getFriendGroupMap() {
    return this.friendGroupMap;
  }
  
  @JSONField(serialize = false)
  @Transient
  private Map<Long, PalNotify> palNotifyMap = new HashMap<>();
  
  public Map<Long, PalNotify> getPalNotifyMap() {
    return this.palNotifyMap;
  }
  
  @Transient
  private int state = 0;
  
  @Transient
  private long prevMoveTime;
  
  @Transient
  private int distributeKey;
  
  @Transient
  private RoleConfirm confirm;
  
  @Transient
  private int house;
  
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
  
  @Transient
  private volatile Boolean canFight = Boolean.valueOf(true);
  
  @Transient
  private transient Map<String, Object> tempCache;
  
  public Boolean getCanFight() {
    return this.canFight;
  }
  
  public Map<String, Object> getTempCache() {
    return this.tempCache;
  }
  
  @Transient
  private Map<Integer, Integer> heartCounts = new HashMap<>(4);
  
  public Map<Integer, Integer> getHeartCounts() {
    return this.heartCounts;
  }
  
  @JsonIgnore
  public Long getId() {
    return this.id;
  }
  
  public Object pushTempCache(String key, Object value) {
    if (this.tempCache == null)
      this.tempCache = new HashMap<>(4); 
    return this.tempCache.put(key, value);
  }
  
  public <T> T popTempCache(String key) {
    if (this.tempCache == null)
      return null; 
    return (T)this.tempCache.remove(key);
  }
  
  @JsonIgnore
  public <T> T getTempCache(String key, T defaultValue) {
    if (this.tempCache == null)
      return defaultValue; 
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
  
  public void doAfterInit() {
    PlayerSerializerUtil.deserialize(this);
  }
  
  public void doBeforeSave() {
    PlayerSerializerUtil.serialize(this);
  }
}
