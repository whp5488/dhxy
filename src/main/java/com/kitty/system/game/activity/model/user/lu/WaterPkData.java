
package com.kitty.system.game.activity.model.user.lu;

import com.kitty.system.game.team.domain.Member;
import com.kitty.system.game.team.domain.Team;
import java.util.ArrayList;
import java.util.concurrent.ScheduledFuture;
import com.kitty.system.game.activity.model.user.lu.vo.WaterFight;
import java.util.List;
import java.io.Serializable;

public class WaterPkData implements Serializable
{
    private int teamId;
    private short pkValue;
    private short score;
    private int memberGrade;
    private int memberEquipScore;
    private short rank;
    private List<Long> members;
    private int time;
    private byte state;
    private List<WaterFight> fight;
    private ScheduledFuture<?> schedule;
    
    public WaterPkData() {
        this.memberGrade = 0;
        this.memberEquipScore = 0;
        this.time = 0;
        this.state = 0;
        this.fight = new ArrayList<WaterFight>(3);
    }
    
    public WaterPkData(final Team team, final short pkValue) {
        this.memberGrade = 0;
        this.memberEquipScore = 0;
        this.time = 0;
        this.state = 0;
        this.fight = new ArrayList<WaterFight>(3);
        this.teamId = team.getId();
        this.pkValue = pkValue;
        this.members = this.getMembers(team);
    }
    
    private List<Long> getMembers(final Team team) {
        final List<Long> members = new ArrayList<Long>();
        final List<Member> list = team.getList();
        for (int i = 0; i < list.size(); ++i) {
            final Member member = list.get(i);
            if (member.isInTeam()) {
                if (!members.contains(member)) {
                    members.add(member.getId());
                }
            }
        }
        return members;
    }
    
    public void subPkValue(final short pkValue) {
        this.pkValue -= pkValue;
    }
    
    public void updateTeamMember(final Team team) {
        this.members = this.getMembers(team);
    }
    
    public void updateTeamMember(final byte state) {
        this.state = state;
    }
    
    public WaterPkData(final int teamId, final short pkValue, final int memberGrade, final int memberEquipScore) {
        this.memberGrade = 0;
        this.memberEquipScore = 0;
        this.time = 0;
        this.state = 0;
        this.fight = new ArrayList<WaterFight>(3);
        this.teamId = teamId;
        this.pkValue = pkValue;
        this.memberGrade = memberGrade;
        this.memberEquipScore = memberEquipScore;
    }
    
    public int getTeamId() {
        return this.teamId;
    }
    
    public short getPkValue() {
        return this.pkValue;
    }
    
    public short getScore() {
        return this.score;
    }
    
    public int getMemberGrade() {
        return this.memberGrade;
    }
    
    public int getMemberEquipScore() {
        return this.memberEquipScore;
    }
    
    public short getRank() {
        return this.rank;
    }
    
    public List<Long> getMembers() {
        return this.members;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public List<WaterFight> getFight() {
        return this.fight;
    }
    
    public ScheduledFuture<?> getSchedule() {
        return this.schedule;
    }
    
    public void setTeamId(final int teamId) {
        this.teamId = teamId;
    }
    
    public void setPkValue(final short pkValue) {
        this.pkValue = pkValue;
    }
    
    public void setScore(final short score) {
        this.score = score;
    }
    
    public void setMemberGrade(final int memberGrade) {
        this.memberGrade = memberGrade;
    }
    
    public void setMemberEquipScore(final int memberEquipScore) {
        this.memberEquipScore = memberEquipScore;
    }
    
    public void setRank(final short rank) {
        this.rank = rank;
    }
    
    public void setMembers(final List<Long> members) {
        this.members = members;
    }
    
    public void setTime(final int time) {
        this.time = time;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setFight(final List<WaterFight> fight) {
        this.fight = fight;
    }
    
    public void setSchedule(final ScheduledFuture<?> schedule) {
        this.schedule = schedule;
    }
    
    @Override
    public String toString() {
        return "WaterPkData(teamId=" + this.getTeamId() + ", pkValue=" + this.getPkValue() + ", score=" + this.getScore() + ", memberGrade=" + this.getMemberGrade() + ", memberEquipScore=" + this.getMemberEquipScore() + ", rank=" + this.getRank() + ", members=" + this.getMembers() + ", time=" + this.getTime() + ", state=" + this.getState() + ", fight=" + this.getFight() + ", schedule=" + this.getSchedule() + ")";
    }
}
