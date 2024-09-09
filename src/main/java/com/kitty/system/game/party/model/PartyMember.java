package com.kitty.system.game.party.model;

import com.kitty.common.db.Db4CommonService;
import java.util.ArrayList;
import com.kitty.system.game.role.domain.Role;
import org.nutz.dao.entity.annotation.ColDefine;
import com.kitty.system.game.party.model.vo.PartyTask;
import java.util.List;
import org.nutz.dao.entity.annotation.Comment;
import org.nutz.dao.entity.annotation.Column;
import org.nutz.dao.entity.annotation.Id;
import org.nutz.dao.entity.annotation.Table;
import com.kitty.common.db.BaseEntity;

@Table("party_member")
public class PartyMember extends BaseEntity<Long> implements Comparable<PartyMember>
{
    @Id(auto = false)
    private Long id;
    @Column("serial")
    @Comment("帮派编号")
    private int serial;
    @Column("role_id")
    @Comment("角色编号")
    private int roleId;
    @Column("name")
    @Comment("角色名称")
    private String name;
    @Column("rei")
    @Comment("角色转生 ")
    private byte rei;
    @Column("level")
    @Comment("角色等级 ")
    private short level;
    @Column("grade")
    @Comment("计算后的等级")
    private int grade;
    @Column("race")
    @Comment("种族")
    private int race;
    @Column("role_serial")
    @Comment("角色外观")
    private int roleSerial;
    @Column("job")
    @Comment("帮派职位")
    private byte job;
    @Column("join_time")
    @Comment("加入时间")
    private int joinTime;
    @Column("week_bang_btl")
    @Comment("本周帮战（次数）")
    private int weekBangBtl;
    @Column("this_bang_btl")
    @Comment("本次帮战")
    private int thisBangBtl;
    @Column("week_ec_type")
    @Comment("本周副本（0未做，1已做）")
    private int weekEcType;
    @Column("day_task")
    @Comment("任务记录")
    @ColDefine(customType = "longtext")
    private List<PartyTask> partyTaskList;
    @Column("contribute")
    @Comment("历史贡献")
    private long contribute;
    @Column("team")
    @Comment("团名")
    private String team;
    @Column("title_change")
    @Comment("称号是否变化")
    private boolean titleChange;
    private int lastOnlineTime;
    private byte onLineStatus;
    
    public PartyMember(Role role, int serial, long joinTime, byte job, Long id) {
        this.grade = 0;
        this.race = 1;
        this.weekBangBtl = 0;
        this.thisBangBtl = 0;
        this.weekEcType = 0;
        this.partyTaskList = new ArrayList<>();
        this.contribute = 0L;
        this.team = "";
        this.titleChange = false;
        this.onLineStatus = 0;
        this.id = id;
        this.serial = serial;
        this.roleSerial = (int)role.getSerial();
        this.roleId = (int)role.getRoleId();
        this.name = role.getName();
        this.job = job;
        this.joinTime = (int)(joinTime / 1000L);
        this.lastOnlineTime = (int)(joinTime / 1000L);
        this.rei = role.getRelive().byteValue();
        this.level = role.getLevel().shortValue();
        this.grade = this.rei * 1000 + this.level;
        this.race = (int)role.getRace();
    }
    
    @Override
    public int compareTo(PartyMember o) {
        return 0;
    }
    
    public void save() {
        Db4CommonService.getInstance().add2Queue(this);
    }
    
    @Override
    public Long getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getRoleId() {
        return this.roleId;
    }
    
    public String getName() {
        return this.name;
    }
    
    public byte getRei() {
        return this.rei;
    }
    
    public short getLevel() {
        return this.level;
    }
    
    public int getGrade() {
        return this.grade;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getRoleSerial() {
        return this.roleSerial;
    }
    
    public byte getJob() {
        return this.job;
    }
    
    public int getJoinTime() {
        return this.joinTime;
    }
    
    public int getWeekBangBtl() {
        return this.weekBangBtl;
    }
    
    public int getThisBangBtl() {
        return this.thisBangBtl;
    }
    
    public int getWeekEcType() {
        return this.weekEcType;
    }
    
    public List<PartyTask> getPartyTaskList() {
        return this.partyTaskList;
    }
    
    public long getContribute() {
        return this.contribute;
    }
    
    public String getTeam() {
        return this.team;
    }
    
    public boolean isTitleChange() {
        return this.titleChange;
    }
    
    public int getLastOnlineTime() {
        return this.lastOnlineTime;
    }
    
    public byte getOnLineStatus() {
        return this.onLineStatus;
    }
    
    public void setId(final Long id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setRoleId(final int roleId) {
        this.roleId = roleId;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setRei(final byte rei) {
        this.rei = rei;
    }
    
    public void setLevel(final short level) {
        this.level = level;
    }
    
    public void setGrade(final int grade) {
        this.grade = grade;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setRoleSerial(final int roleSerial) {
        this.roleSerial = roleSerial;
    }
    
    public void setJob(final byte job) {
        this.job = job;
    }
    
    public void setJoinTime(final int joinTime) {
        this.joinTime = joinTime;
    }
    
    public void setWeekBangBtl(final int weekBangBtl) {
        this.weekBangBtl = weekBangBtl;
    }
    
    public void setThisBangBtl(final int thisBangBtl) {
        this.thisBangBtl = thisBangBtl;
    }
    
    public void setWeekEcType(final int weekEcType) {
        this.weekEcType = weekEcType;
    }
    
    public void setPartyTaskList(final List<PartyTask> partyTaskList) {
        this.partyTaskList = partyTaskList;
    }
    
    public void setContribute(final long contribute) {
        this.contribute = contribute;
    }
    
    public void setTeam(final String team) {
        this.team = team;
    }
    
    public void setTitleChange(final boolean titleChange) {
        this.titleChange = titleChange;
    }
    
    public void setLastOnlineTime(final int lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }
    
    public void setOnLineStatus(final byte onLineStatus) {
        this.onLineStatus = onLineStatus;
    }
    
    public PartyMember() {
        this.grade = 0;
        this.race = 1;
        this.weekBangBtl = 0;
        this.thisBangBtl = 0;
        this.weekEcType = 0;
        this.partyTaskList = new ArrayList<>();
        this.contribute = 0L;
        this.team = "";
        this.titleChange = false;
        this.onLineStatus = 0;
    }
}
