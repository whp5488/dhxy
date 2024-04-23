package com.kitty.system.game.party.model;

import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import com.kitty.system.common.utils.NutConcurrentMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Default;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;

@Table("party")
public class Party extends BaseEntity<Long> implements Comparable<Party> {
  @Id(auto = false)
  @Comment("帮派ID")
  private Long id;
  
  @Column
  @Comment("帮派编号")
  private Integer serial;
  
  @Column
  @Comment("帮派名字")
  private String name;
  
  public void setId(Long id) {
    this.id = id;
  }
  
  public void setSerial(Integer serial) {
    this.serial = serial;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setLevel(byte level) {
    this.level = level;
  }
  
  public void setAim(String aim) {
    this.aim = aim;
  }
  
  public void setNotice(String notice) {
    this.notice = notice;
  }
  
  public void setMasterId(Integer masterId) {
    this.masterId = masterId;
  }
  
  public void setMasterName(String masterName) {
    this.masterName = masterName;
  }
  
  public void setCreateTime(int createTime) {
    this.createTime = createTime;
  }
  
  public void setCapital(long capital) {
    this.capital = capital;
  }
  
  public void setCntech(long cntech) {
    this.cntech = cntech;
  }
  
  public void setActive(short active) {
    this.active = active;
  }
  
  public void setConsent(byte consent) {
    this.consent = consent;
  }
  
  public void setConsentLevel(short consentLevel) {
    this.consentLevel = consentLevel;
  }
  
  public void setRefuse(byte refuse) {
    this.refuse = refuse;
  }
  
  public void setRefuseLevel(short refuseLevel) {
    this.refuseLevel = refuseLevel;
  }
  
  public void setReqJoinJson(String reqJoinJson) {
    this.reqJoinJson = reqJoinJson;
  }
  
  public void setDenyJson(String denyJson) {
    this.denyJson = denyJson;
  }
  
  public void setOnlineRate(List<Integer> onlineRate) {
    this.onlineRate = onlineRate;
  }
  
  public void setStandings(short standings) {
    this.standings = standings;
  }
  
  public void setFlag(Map<Short, Byte> flag) {
    this.flag = flag;
  }
  
  public void setFlagColor(Map<Short, Byte> flagColor) {
    this.flagColor = flagColor;
  }
  
  public void setLace(Map<Short, Byte> lace) {
    this.lace = lace;
  }
  
  public void setEmblem(Map<Short, Byte> emblem) {
    this.emblem = emblem;
  }
  
  public void setEmblemColor(Map<Short, Byte> emblemColor) {
    this.emblemColor = emblemColor;
  }
  
  public void setEmblemText(String emblemText) {
    this.emblemText = emblemText;
  }
  
  public void setMembers(Map<Integer, PartyMember> members) {
    this.members = members;
  }
  
  public void setReqJoin(NutConcurrentMap reqJoin) {
    this.reqJoin = reqJoin;
  }
  
  public void setDenyMap(Map<Integer, PartyDeny> denyMap) {
    this.denyMap = denyMap;
  }
  
  public void setLogs(List<PartyLog> logs) {
    this.logs = logs;
  }
  
  public void setRedBagList(List<PartyRedBag> redBagList) {
    this.redBagList = redBagList;
  }
  
  public void setSchedule(ScheduledFuture<?> schedule) {
    this.schedule = schedule;
  }
  
  public void setNutMap(NutConcurrentMap nutMap) {
    this.nutMap = nutMap;
  }
  
  public void setWarsRecordList(List<PartyWarsRecord> warsRecordList) {
    this.warsRecordList = warsRecordList;
  }
  
  public Long getId() {
    return this.id;
  }
  
  public Integer getSerial() {
    return this.serial;
  }
  
  public String getName() {
    return this.name;
  }
  
  @Column
  @Comment("帮派等级")
  private byte level = 1;
  
  @Column
  @Comment("帮派宗旨")
  @ColDefine(width = 2048)
  private String aim;
  
  @Column
  @Comment("帮派公告")
  @ColDefine(width = 2048)
  private String notice;
  
  @Column("master_id")
  @Comment("帮主角色编号")
  private Integer masterId;
  
  @Column("master_name")
  @Comment("帮主角色名称")
  private String masterName;
  
  @Column("create_time")
  @Comment("创建时间")
  private int createTime;
  
  public byte getLevel() {
    return this.level;
  }
  
  public String getAim() {
    return this.aim;
  }
  
  public String getNotice() {
    return this.notice;
  }
  
  public Integer getMasterId() {
    return this.masterId;
  }
  
  public String getMasterName() {
    return this.masterName;
  }
  
  public int getCreateTime() {
    return this.createTime;
  }
  
  @Column
  @Comment("帮派资金")
  private long capital = 10000L;
  
  public long getCapital() {
    return this.capital;
  }
  
  @Column
  @Comment("科技资金")
  private long cntech = 0L;
  
  public long getCntech() {
    return this.cntech;
  }
  
  @Column
  @Comment("帮派活跃度")
  private short active = 100;
  
  public short getActive() {
    return this.active;
  }
  
  @Column
  @Comment("自动收人（0开启，1关闭）")
  private byte consent = 0;
  
  public byte getConsent() {
    return this.consent;
  }
  
  @Column("consent_level")
  @Comment("自动收人转生等级")
  private short consentLevel = 0;
  
  public short getConsentLevel() {
    return this.consentLevel;
  }
  
  @Column
  @Comment("自动拒绝（0开启，1关闭）")
  private byte refuse = 1;
  
  public byte getRefuse() {
    return this.refuse;
  }
  
  @Column("refuse_level")
  @Comment("自动拒绝转生等级")
  private short refuseLevel = 0;
  
  @Column("req_join_json")
  @Comment("申请列表")
  @ColDefine(customType = "longtext")
  private String reqJoinJson;
  
  @Column("deny_json")
  @ColDefine(customType = "longtext")
  private String denyJson;
  
  public short getRefuseLevel() {
    return this.refuseLevel;
  }
  
  public String getReqJoinJson() {
    return this.reqJoinJson;
  }
  
  public String getDenyJson() {
    return this.denyJson;
  }
  
  @Column("online_rate")
  @Comment("成员在线率")
  @Default("[]")
  private List<Integer> onlineRate = new ArrayList<>(3);
  
  public List<Integer> getOnlineRate() {
    return this.onlineRate;
  }
  
  @Column("standings")
  @Comment("战绩")
  private short standings = 0;
  
  public short getStandings() {
    return this.standings;
  }
  
  @Comment("旗帜 Map<旗帜索引，是否使用>")
  @Column("flag")
  private Map<Short, Byte> flag = new HashMap<>();
  
  public Map<Short, Byte> getFlag() {
    return this.flag;
  }
  
  @Comment("旗帜颜色 Map<旗帜颜色索引，是否使用>")
  @Column("flag_color")
  private Map<Short, Byte> flagColor = new HashMap<>();
  
  public Map<Short, Byte> getFlagColor() {
    return this.flagColor;
  }
  
  @Column("lace")
  @Comment("边饰")
  private Map<Short, Byte> lace = new HashMap<>(8);
  
  public Map<Short, Byte> getLace() {
    return this.lace;
  }
  
  @Comment("纹章 Map<纹章索引，是否使用>")
  @Column("emblem")
  private Map<Short, Byte> emblem = new HashMap<>();
  
  public Map<Short, Byte> getEmblem() {
    return this.emblem;
  }
  
  @Comment("纹章颜色 Map<纹章颜色索引，是否使用>")
  @Column("emblem_color")
  private Map<Short, Byte> emblemColor = new HashMap<>();
  
  public Map<Short, Byte> getEmblemColor() {
    return this.emblemColor;
  }
  
  @Comment("纹章文字")
  @Column("emblem_text")
  private String emblemText = "帮";
  
  public String getEmblemText() {
    return this.emblemText;
  }
  
  private Map<Integer, PartyMember> members = new ConcurrentHashMap<>();
  
  public Map<Integer, PartyMember> getMembers() {
    return this.members;
  }
  
  private NutConcurrentMap reqJoin = new NutConcurrentMap();
  
  public NutConcurrentMap getReqJoin() {
    return this.reqJoin;
  }
  
  private Map<Integer, PartyDeny> denyMap = new ConcurrentHashMap<>();
  
  public Map<Integer, PartyDeny> getDenyMap() {
    return this.denyMap;
  }
  
  private List<PartyLog> logs = new ArrayList<>();
  
  public List<PartyLog> getLogs() {
    return this.logs;
  }
  
  private List<PartyRedBag> redBagList = new ArrayList<>();
  
  private ScheduledFuture<?> schedule;
  
  public List<PartyRedBag> getRedBagList() {
    return this.redBagList;
  }
  
  public ScheduledFuture<?> getSchedule() {
    return this.schedule;
  }
  
  private NutConcurrentMap nutMap = new NutConcurrentMap();
  
  public NutConcurrentMap getNutMap() {
    return this.nutMap;
  }
  
  private List<PartyWarsRecord> warsRecordList = new ArrayList<>();
  
  public List<PartyWarsRecord> getWarsRecordList() {
    return this.warsRecordList;
  }
  
  public void save() {
    Db4CommonService.getInstance().add2Queue(this);
    for (PartyLog log : this.logs)
      Db4CommonService.getInstance().add2Queue(log); 
  }
  
  @JsonIgnore
  public List<Integer> getWarsEnemySerial() {
    List<Integer> list = new ArrayList<>();
    int num = (this.warsRecordList.size() > 3) ? 3 : this.warsRecordList.size();
    for (int i = 0; i < num; i++) {
      PartyWarsRecord partyWarsRecord = this.warsRecordList.get(i);
      list.add(Integer.valueOf(partyWarsRecord.getEnemySerial()));
    } 
    return list;
  }
  
  @JsonIgnore
  public int getMemberNum() {
    return this.members.size();
  }
  
  @JsonIgnore
  public int getRating() {
    if (this.warsRecordList.size() <= 0)
      return 0; 
    List<PartyWarsRecord> collect = (List<PartyWarsRecord>)this.warsRecordList.stream().filter(e -> (e.getCombatResults() == 1 || e.getCombatResults() == 2)).collect(Collectors.toList());
    return collect.size();
  }
  
  public int compareTo(Party o) {
    return o.getLevel() - getLevel();
  }
  
  public void doAfterInit() {
    if (StringUtils.isNotEmpty(this.reqJoinJson)) {
      String uncompress = ZipUtil.uncompress(this.reqJoinJson);
      this.reqJoin = (NutConcurrentMap)JsonUtils.string2Object(uncompress, NutConcurrentMap.class);
    } 
    if (StringUtils.isNotEmpty(this.denyJson)) {
      String uncompress = ZipUtil.uncompress(this.denyJson);
      this.denyMap = JsonUtils.string2Map(uncompress, Integer.class, PartyDeny.class);
    } 
  }
  
  public void doBeforeSave() {
    if (this.reqJoin != null) {
      String string = JsonUtils.object2String(this.reqJoin);
      this.reqJoinJson = ZipUtil.compress(string);
    } 
    if (this.denyMap != null) {
      String string = JsonUtils.object2String(this.denyMap);
      this.denyJson = ZipUtil.compress(string);
    } 
  }
}
