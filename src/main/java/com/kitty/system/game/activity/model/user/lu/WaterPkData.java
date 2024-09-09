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
        this.fight = new ArrayList(3);
    }
    
    public WaterPkData(Team team, short pkValue) {
        this.memberGrade = 0;
        this.memberEquipScore = 0;
        this.time = 0;
        this.state = 0;
        this.fight = new ArrayList(3);
        this.teamId = team.getId();
        this.pkValue = pkValue;
        this.members = this.getMembers(team);
    }
    
    private List<Long> getMembers(Team team) {
        List<Long> members = new ArrayList<>();
        List<Member> list = team.getList();
        for (int i = 0; i < list.size(); ++i) {
            Member member = (Member)list.get(i);
            if (member.isInTeam() && !members.contains(member)) {
                members.add(member.getId());
            }
        }
        return members;
    }
    
    public void subPkValue(short pkValue) {
        this.pkValue -= pkValue;
    }
    
    public void updateTeamMember(Team team) {
        this.members = this.getMembers(team);
    }
    
    public void updateTeamMember(byte state) {
        this.state = state;
    }
    
    public WaterPkData(int teamId, short pkValue, int memberGrade, int memberEquipScore) {
        this.memberGrade = 0;
        this.memberEquipScore = 0;
        this.time = 0;
        this.state = 0;
        this.fight = new ArrayList(3);
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
        return "WaterPkData(teamId=" + this.getTeamId() + ", pkValue=" + (int)this.getPkValue() + ", score=" + (int)this.getScore() + ", memberGrade=" + this.getMemberGrade() + ", memberEquipScore=" + this.getMemberEquipScore() + ", rank=" + (int)this.getRank() + ", members=" + this.getMembers() + ", time=" + this.getTime() + ", state=" + (int)this.getState() + ", fight=" + this.getFight() + ", schedule=" + this.getSchedule() + ")";
    }
}
