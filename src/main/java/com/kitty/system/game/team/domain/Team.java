package com.kitty.system.game.team.domain;

import java.util.Arrays;
import com.kitty.system.common.utils.NutConcurrentMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;

public class Team
{
    private int id;
    private String name;
    private Date createTime;
    private List<Member> list;
    private ArrayList<Member> partner;
    private int lastTeamId;
    private String target;
    private String msg;
    private NutConcurrentMap fightLeaveNutMap;
    private int[] raceLimit;
    private int sexLimit;
    private int minLevel;
    private int minRelive;
    private int maxLevel;
    private int maxRelive;
    
    public boolean isLeader(int roleId) {
        return this.list != null && !this.list.isEmpty() && this.list.get(0) != null && ((Member)this.list.get(0)).getRoleId().equals(Integer.valueOf(roleId));
    }
    
    public int getLeaderRoleId() {
        if (this.list == null || this.list.isEmpty()) {
            return 0;
        }
        if (this.list.get(0) == null) {
            return 0;
        }
        return (int)((Member)this.list.get(0)).getRoleId();
    }
    
    public long getLeaderUid() {
        if (this.list == null || this.list.isEmpty()) {
            return 0L;
        }
        if (this.list.get(0) == null) {
            return 0L;
        }
        return (long)((Member)this.list.get(0)).getId();
    }
    
    public Member getMember(int roleId) {
        int i = 0;
        while (i < this.list.size()) {
            Member member = (Member)this.list.get(i);
            if (member != null && member.getRoleId().equals(Integer.valueOf(roleId))) {
                return member;
            }
            ++i;
        }
        return null;
    }
    
    public String getMemberNames() {
        StringBuilder stringBuilder = new StringBuilder();
        if (this.list != null) {
            for (int i = 0; i < this.list.size(); ++i) {
                Member member = (Member)this.list.get(i);
                if (member != null && member.isInTeam()) {
                    if (stringBuilder.length() > 0) {
                        stringBuilder.append("、");
                    }
                    stringBuilder.append(member.getName());
                }
            }
        }
        return stringBuilder.toString();
    }
    
    public Team() {
        this.fightLeaveNutMap = new NutConcurrentMap();
        this.minLevel = 0;
        this.minRelive = 0;
        this.maxLevel = 0;
        this.maxRelive = 0;
    }
    
    public int getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Date getCreateTime() {
        return this.createTime;
    }
    
    public List<Member> getList() {
        return this.list;
    }
    
    public ArrayList<Member> getPartner() {
        return this.partner;
    }
    
    public int getLastTeamId() {
        return this.lastTeamId;
    }
    
    public String getTarget() {
        return this.target;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public NutConcurrentMap getFightLeaveNutMap() {
        return this.fightLeaveNutMap;
    }
    
    public int[] getRaceLimit() {
        return this.raceLimit;
    }
    
    public int getSexLimit() {
        return this.sexLimit;
    }
    
    public int getMinLevel() {
        return this.minLevel;
    }
    
    public int getMinRelive() {
        return this.minRelive;
    }
    
    public int getMaxLevel() {
        return this.maxLevel;
    }
    
    public int getMaxRelive() {
        return this.maxRelive;
    }
    
    public void setId(final int id) {
        this.id = id;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setCreateTime(final Date createTime) {
        this.createTime = createTime;
    }
    
    public void setList(final List<Member> list) {
        this.list = list;
    }
    
    public void setPartner(final ArrayList<Member> partner) {
        this.partner = partner;
    }
    
    public void setLastTeamId(final int lastTeamId) {
        this.lastTeamId = lastTeamId;
    }
    
    public void setTarget(final String target) {
        this.target = target;
    }
    
    public void setMsg(final String msg) {
        this.msg = msg;
    }
    
    public void setFightLeaveNutMap(final NutConcurrentMap fightLeaveNutMap) {
        this.fightLeaveNutMap = fightLeaveNutMap;
    }
    
    public void setRaceLimit(final int[] raceLimit) {
        this.raceLimit = raceLimit;
    }
    
    public void setSexLimit(final int sexLimit) {
        this.sexLimit = sexLimit;
    }
    
    public void setMinLevel(final int minLevel) {
        this.minLevel = minLevel;
    }
    
    public void setMinRelive(final int minRelive) {
        this.minRelive = minRelive;
    }
    
    public void setMaxLevel(final int maxLevel) {
        this.maxLevel = maxLevel;
    }
    
    public void setMaxRelive(final int maxRelive) {
        this.maxRelive = maxRelive;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Team)) {
            return false;
        }
        Team other = (Team)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$createTime = this.getCreateTime();
        Object other$createTime = other.getCreateTime();
        if (!((this$createTime != null) ? this$createTime.equals(other$createTime) : (other$createTime == null))) {
            return false;
        }
        Object this$list = this.getList();
        Object other$list = other.getList();
        if (!((this$list != null) ? this$list.equals(other$list) : (other$list == null))) {
            return false;
        }
        Object this$partner = this.getPartner();
        Object other$partner = other.getPartner();
        if (!((this$partner != null) ? this$partner.equals(other$partner) : (other$partner == null))) {
            return false;
        }
        if (this.getLastTeamId() != other.getLastTeamId()) {
            return false;
        }
        Object this$target = this.getTarget();
        Object other$target = other.getTarget();
        if (!((this$target != null) ? this$target.equals(other$target) : (other$target == null))) {
            return false;
        }
        Object this$msg = this.getMsg();
        Object other$msg = other.getMsg();
        if (!((this$msg != null) ? this$msg.equals(other$msg) : (other$msg == null))) {
            return false;
        }
        Object this$fightLeaveNutMap = this.getFightLeaveNutMap();
        Object other$fightLeaveNutMap = other.getFightLeaveNutMap();
        return ((this$fightLeaveNutMap != null) ? this$fightLeaveNutMap.equals(other$fightLeaveNutMap) : (other$fightLeaveNutMap == null)) && Arrays.equals(this.getRaceLimit(), other.getRaceLimit()) && this.getSexLimit() == other.getSexLimit() && this.getMinLevel() == other.getMinLevel() && this.getMinRelive() == other.getMinRelive() && this.getMaxLevel() == other.getMaxLevel() && this.getMaxRelive() == other.getMaxRelive();
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Team;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $createTime = this.getCreateTime();
        result = result * 59 + (($createTime == null) ? 43 : $createTime.hashCode());
        Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        Object $partner = this.getPartner();
        result = result * 59 + (($partner == null) ? 43 : $partner.hashCode());
        result = result * 59 + this.getLastTeamId();
        Object $target = this.getTarget();
        result = result * 59 + (($target == null) ? 43 : $target.hashCode());
        Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        Object $fightLeaveNutMap = this.getFightLeaveNutMap();
        result = result * 59 + (($fightLeaveNutMap == null) ? 43 : $fightLeaveNutMap.hashCode());
        result = result * 59 + Arrays.hashCode(this.getRaceLimit());
        result = result * 59 + this.getSexLimit();
        result = result * 59 + this.getMinLevel();
        result = result * 59 + this.getMinRelive();
        result = result * 59 + this.getMaxLevel();
        result = result * 59 + this.getMaxRelive();
        return result;
    }
    
    @Override
    public String toString() {
        return "Team(id=" + this.getId() + ", name=" + this.getName() + ", createTime=" + this.getCreateTime() + ", list=" + this.getList() + ", partner=" + this.getPartner() + ", lastTeamId=" + this.getLastTeamId() + ", target=" + this.getTarget() + ", msg=" + this.getMsg() + ", fightLeaveNutMap=" + this.getFightLeaveNutMap() + ", raceLimit=" + Arrays.toString(this.getRaceLimit()) + ", sexLimit=" + this.getSexLimit() + ", minLevel=" + this.getMinLevel() + ", minRelive=" + this.getMinRelive() + ", maxLevel=" + this.getMaxLevel() + ", maxRelive=" + this.getMaxRelive() + ")";
    }
}
