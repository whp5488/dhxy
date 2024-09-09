package com.kitty.system.game.party.model;

import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.ZipUtil;
import org.apache.commons.lang3.StringUtils;
import java.util.stream.Collectors;
import org.codehaus.jackson.annotate.JsonIgnore;
import java.util.Iterator;
import com.kitty.common.db.Db4CommonService;
import java.util.concurrent.ConcurrentHashMap;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.common.utils.NutConcurrentMap;
import java.util.Map;
import org.nutz.dao.entity.annotation.Default;
import java.util.List;
import org.nutz.dao.entity.annotation.ColDefine;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("party")
public class Party extends BaseEntity<Long> implements Comparable<Party>
{
    @Id(auto = false)
    @Comment("帮派ID")
    private Long id;
    @Column
    @Comment("帮派编号")
    private Integer serial;
    @Column
    @Comment("帮派名字")
    private String name;
    @Column
    @Comment("帮派等级")
    private byte level;
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
    @Column
    @Comment("帮派资金")
    private long capital;
    @Column
    @Comment("科技资金")
    private long cntech;
    @Column
    @Comment("帮派活跃度")
    private short active;
    @Column
    @Comment("自动收人（0开启，1关闭）")
    private byte consent;
    @Column("consent_level")
    @Comment("自动收人转生等级")
    private short consentLevel;
    @Column
    @Comment("自动拒绝（0开启，1关闭）")
    private byte refuse;
    @Column("refuse_level")
    @Comment("自动拒绝转生等级")
    private short refuseLevel;
    @Column("req_join_json")
    @Comment("申请列表")
    @ColDefine(customType = "longtext")
    private String reqJoinJson;
    @Column("deny_json")
    @ColDefine(customType = "longtext")
    private String denyJson;
    @Column("online_rate")
    @Comment("成员在线率")
    @Default("[]")
    private List<Integer> onlineRate;
    @Column("standings")
    @Comment("战绩")
    private short standings;
    @Comment("旗帜 Map<旗帜索引，是否使用>")
    @Column("flag")
    private Map<Short, Byte> flag;
    @Comment("旗帜颜色 Map<旗帜颜色索引，是否使用>")
    @Column("flag_color")
    private Map<Short, Byte> flagColor;
    @Column("lace")
    @Comment("边饰")
    private Map<Short, Byte> lace;
    @Comment("纹章 Map<纹章索引，是否使用>")
    @Column("emblem")
    private Map<Short, Byte> emblem;
    @Comment("纹章颜色 Map<纹章颜色索引，是否使用>")
    @Column("emblem_color")
    private Map<Short, Byte> emblemColor;
    @Comment("纹章文字")
    @Column("emblem_text")
    private String emblemText;
    private Map<Integer, PartyMember> members;
    private NutConcurrentMap reqJoin;
    private Map<Integer, PartyDeny> denyMap;
    private List<PartyLog> logs;
    private List<PartyRedBag> redBagList;
    private ScheduledFuture<?> schedule;
    private NutConcurrentMap nutMap;
    private List<PartyWarsRecord> warsRecordList;
    
    public Party() {
        this.level = 1;
        this.capital = 10000L;
        this.cntech = 0L;
        this.active = 100;
        this.consent = 0;
        this.consentLevel = 0;
        this.refuse = 1;
        this.refuseLevel = 0;
        this.onlineRate = new ArrayList(3);
        this.standings = 0;
        this.flag = new HashMap();
        this.flagColor = new HashMap();
        this.lace = new HashMap(8);
        this.emblem = new HashMap();
        this.emblemColor = new HashMap();
        this.emblemText = "帮";
        this.members = new ConcurrentHashMap();
        this.reqJoin = new NutConcurrentMap();
        this.denyMap = new ConcurrentHashMap();
        this.logs = new ArrayList<>();
        this.redBagList = new ArrayList<>();
        this.nutMap = new NutConcurrentMap();
        this.warsRecordList = new ArrayList<>();
    }
    
    public void save() {
        Db4CommonService.getInstance().add2Queue(this);
        for (PartyLog log : this.logs) {
            Db4CommonService.getInstance().add2Queue(log);
        }
    }
    
    @JsonIgnore
    public List<Integer> getWarsEnemySerial() {
        List<Integer> list = new ArrayList<>();
        for (int num = (this.warsRecordList.size() > 3) ? 3 : this.warsRecordList.size(), i = 0; i < num; ++i) {
            PartyWarsRecord partyWarsRecord = (PartyWarsRecord)this.warsRecordList.get(i);
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
        if (this.warsRecordList.size() <= 0) {
            return 0;
        }
        List<PartyWarsRecord> collect = (List)this.warsRecordList.stream().filter(e -> e.getCombatResults() == 1 || e.getCombatResults() == 2).collect(Collectors.toList());
        return collect.size();
    }
    
    @Override
    public int compareTo(Party o) {
        return o.getLevel() - this.getLevel();
    }
    
    @Override
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
    
    @Override
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
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setSerial(final Integer serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final byte level) {
        this.level = level;
    }
    
    public void setAim(final String aim) {
        this.aim = aim;
    }
    
    public void setNotice(final String notice) {
        this.notice = notice;
    }
    
    public void setMasterId(final Integer masterId) {
        this.masterId = masterId;
    }
    
    public void setMasterName(final String masterName) {
        this.masterName = masterName;
    }
    
    public void setCreateTime(final int createTime) {
        this.createTime = createTime;
    }
    
    public void setCapital(final long capital) {
        this.capital = capital;
    }
    
    public void setCntech(final long cntech) {
        this.cntech = cntech;
    }
    
    public void setActive(final short active) {
        this.active = active;
    }
    
    public void setConsent(final byte consent) {
        this.consent = consent;
    }
    
    public void setConsentLevel(final short consentLevel) {
        this.consentLevel = consentLevel;
    }
    
    public void setRefuse(final byte refuse) {
        this.refuse = refuse;
    }
    
    public void setRefuseLevel(final short refuseLevel) {
        this.refuseLevel = refuseLevel;
    }
    
    public void setReqJoinJson(final String reqJoinJson) {
        this.reqJoinJson = reqJoinJson;
    }
    
    public void setDenyJson(final String denyJson) {
        this.denyJson = denyJson;
    }
    
    public void setOnlineRate(final List<Integer> onlineRate) {
        this.onlineRate = onlineRate;
    }
    
    public void setStandings(final short standings) {
        this.standings = standings;
    }
    
    public void setFlag(final Map<Short, Byte> flag) {
        this.flag = flag;
    }
    
    public void setFlagColor(final Map<Short, Byte> flagColor) {
        this.flagColor = flagColor;
    }
    
    public void setLace(final Map<Short, Byte> lace) {
        this.lace = lace;
    }
    
    public void setEmblem(final Map<Short, Byte> emblem) {
        this.emblem = emblem;
    }
    
    public void setEmblemColor(final Map<Short, Byte> emblemColor) {
        this.emblemColor = emblemColor;
    }
    
    public void setEmblemText(final String emblemText) {
        this.emblemText = emblemText;
    }
    
    public void setMembers(final Map<Integer, PartyMember> members) {
        this.members = members;
    }
    
    public void setReqJoin(final NutConcurrentMap reqJoin) {
        this.reqJoin = reqJoin;
    }
    
    public void setDenyMap(final Map<Integer, PartyDeny> denyMap) {
        this.denyMap = denyMap;
    }
    
    public void setLogs(final List<PartyLog> logs) {
        this.logs = logs;
    }
    
    public void setRedBagList(final List<PartyRedBag> redBagList) {
        this.redBagList = redBagList;
    }
    
    public void setSchedule(final ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }
    
    public void setNutMap(final NutConcurrentMap nutMap) {
        this.nutMap = nutMap;
    }
    
    public void setWarsRecordList(final List<PartyWarsRecord> warsRecordList) {
        this.warsRecordList = warsRecordList;
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public Integer getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
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
    
    public long getCapital() {
        return this.capital;
    }
    
    public long getCntech() {
        return this.cntech;
    }
    
    public short getActive() {
        return this.active;
    }
    
    public byte getConsent() {
        return this.consent;
    }
    
    public short getConsentLevel() {
        return this.consentLevel;
    }
    
    public byte getRefuse() {
        return this.refuse;
    }
    
    public short getRefuseLevel() {
        return this.refuseLevel;
    }
    
    public String getReqJoinJson() {
        return this.reqJoinJson;
    }
    
    public String getDenyJson() {
        return this.denyJson;
    }
    
    public List<Integer> getOnlineRate() {
        return this.onlineRate;
    }
    
    public short getStandings() {
        return this.standings;
    }
    
    public Map<Short, Byte> getFlag() {
        return this.flag;
    }
    
    public Map<Short, Byte> getFlagColor() {
        return this.flagColor;
    }
    
    public Map<Short, Byte> getLace() {
        return this.lace;
    }
    
    public Map<Short, Byte> getEmblem() {
        return this.emblem;
    }
    
    public Map<Short, Byte> getEmblemColor() {
        return this.emblemColor;
    }
    
    public String getEmblemText() {
        return this.emblemText;
    }
    
    public Map<Integer, PartyMember> getMembers() {
        return this.members;
    }
    
    public NutConcurrentMap getReqJoin() {
        return this.reqJoin;
    }
    
    public Map<Integer, PartyDeny> getDenyMap() {
        return this.denyMap;
    }
    
    public List<PartyLog> getLogs() {
        return this.logs;
    }
    
    public List<PartyRedBag> getRedBagList() {
        return this.redBagList;
    }
    
    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }
    
    public NutConcurrentMap getNutMap() {
        return this.nutMap;
    }
    
    public List<PartyWarsRecord> getWarsRecordList() {
        return this.warsRecordList;
    }
}
