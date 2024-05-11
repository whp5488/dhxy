
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
    
    public boolean isLeader(final int roleId) {
        return this.list != null && !this.list.isEmpty() && this.list.get(0) != null && this.list.get(0).getRoleId().equals(roleId);
    }
    
    public int getLeaderRoleId() {
        if (this.list == null || this.list.isEmpty()) {
            return 0;
        }
        if (this.list.get(0) == null) {
            return 0;
        }
        return this.list.get(0).getRoleId();
    }
    
    public long getLeaderUid() {
        if (this.list == null || this.list.isEmpty()) {
            return 0L;
        }
        if (this.list.get(0) == null) {
            return 0L;
        }
        return this.list.get(0).getId();
    }
    
    public Member getMember(final int roleId) {
        for (int i = 0; i < this.list.size(); ++i) {
            final Member member = this.list.get(i);
            if (member != null && member.getRoleId().equals(roleId)) {
                return member;
            }
        }
        return null;
    }
    
    public String getMemberNames() {
        final StringBuilder stringBuilder = new StringBuilder();
        if (this.list != null) {
            for (int i = 0; i < this.list.size(); ++i) {
                final Member member = this.list.get(i);
                if (member != null) {
                    if (member.isInTeam()) {
                        if (stringBuilder.length() > 0) {
                            stringBuilder.append("、");
                        }
                        stringBuilder.append(member.getName());
                    }
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
        final Team other = (Team)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getId() != other.getId()) {
            return false;
        }
        final Object this$name = this.getName();
        final Object other$name = other.getName();
        Label_0078: {
            if (this$name == null) {
                if (other$name == null) {
                    break Label_0078;
                }
            }
            else if (this$name.equals(other$name)) {
                break Label_0078;
            }
            return false;
        }
        final Object this$createTime = this.getCreateTime();
        final Object other$createTime = other.getCreateTime();
        Label_0115: {
            if (this$createTime == null) {
                if (other$createTime == null) {
                    break Label_0115;
                }
            }
            else if (this$createTime.equals(other$createTime)) {
                break Label_0115;
            }
            return false;
        }
        final Object this$list = this.getList();
        final Object other$list = other.getList();
        Label_0152: {
            if (this$list == null) {
                if (other$list == null) {
                    break Label_0152;
                }
            }
            else if (this$list.equals(other$list)) {
                break Label_0152;
            }
            return false;
        }
        final Object this$partner = this.getPartner();
        final Object other$partner = other.getPartner();
        Label_0189: {
            if (this$partner == null) {
                if (other$partner == null) {
                    break Label_0189;
                }
            }
            else if (this$partner.equals(other$partner)) {
                break Label_0189;
            }
            return false;
        }
        if (this.getLastTeamId() != other.getLastTeamId()) {
            return false;
        }
        final Object this$target = this.getTarget();
        final Object other$target = other.getTarget();
        Label_0239: {
            if (this$target == null) {
                if (other$target == null) {
                    break Label_0239;
                }
            }
            else if (this$target.equals(other$target)) {
                break Label_0239;
            }
            return false;
        }
        final Object this$msg = this.getMsg();
        final Object other$msg = other.getMsg();
        Label_0276: {
            if (this$msg == null) {
                if (other$msg == null) {
                    break Label_0276;
                }
            }
            else if (this$msg.equals(other$msg)) {
                break Label_0276;
            }
            return false;
        }
        final Object this$fightLeaveNutMap = this.getFightLeaveNutMap();
        final Object other$fightLeaveNutMap = other.getFightLeaveNutMap();
        if (this$fightLeaveNutMap == null) {
            if (other$fightLeaveNutMap == null) {
                return Arrays.equals(this.getRaceLimit(), other.getRaceLimit()) && this.getSexLimit() == other.getSexLimit() && this.getMinLevel() == other.getMinLevel() && this.getMinRelive() == other.getMinRelive() && this.getMaxLevel() == other.getMaxLevel() && this.getMaxRelive() == other.getMaxRelive();
            }
        }
        else if (this$fightLeaveNutMap.equals(other$fightLeaveNutMap)) {
            return Arrays.equals(this.getRaceLimit(), other.getRaceLimit()) && this.getSexLimit() == other.getSexLimit() && this.getMinLevel() == other.getMinLevel() && this.getMinRelive() == other.getMinRelive() && this.getMaxLevel() == other.getMaxLevel() && this.getMaxRelive() == other.getMaxRelive();
        }
        return false;
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof Team;
    }
    
    @Override
    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getId();
        final Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        final Object $createTime = this.getCreateTime();
        result = result * 59 + (($createTime == null) ? 43 : $createTime.hashCode());
        final Object $list = this.getList();
        result = result * 59 + (($list == null) ? 43 : $list.hashCode());
        final Object $partner = this.getPartner();
        result = result * 59 + (($partner == null) ? 43 : $partner.hashCode());
        result = result * 59 + this.getLastTeamId();
        final Object $target = this.getTarget();
        result = result * 59 + (($target == null) ? 43 : $target.hashCode());
        final Object $msg = this.getMsg();
        result = result * 59 + (($msg == null) ? 43 : $msg.hashCode());
        final Object $fightLeaveNutMap = this.getFightLeaveNutMap();
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
