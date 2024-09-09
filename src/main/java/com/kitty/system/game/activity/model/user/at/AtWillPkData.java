package com.kitty.system.game.activity.model.user.at;

import java.util.ArrayList;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.game.team.domain.Member;
import java.util.List;
import java.io.Serializable;

public class AtWillPkData implements Serializable
{
    private int teamId;
    private short pkValue;
    private short score;
    private List<Member> members;
    private long prevPKTime;
    private byte state;
    private long leave;
    
    public AtWillPkData() {
        this.score = 0;
        this.state = 0;
        this.leave = 0L;
    }
    
    public AtWillPkData(Team team, short pkValue) {
        this.score = 0;
        this.state = 0;
        this.leave = 0L;
        this.teamId = team.getId();
        this.pkValue = pkValue;
        this.score = 0;
        List<Member> members = this.getMembers(team);
        this.members = members;
    }
    
    private List<Member> getMembers(Team team) {
        List<Member> members = new ArrayList<>();
        List<Member> list = team.getList();
        for (int i = 0; i < list.size(); ++i) {
            Member member = (Member)list.get(i);
            if (member.isInTeam() && !members.contains(member)) {
                members.add(member);
            }
        }
        return members;
    }
    
    public void addTotalScore(short score) {
        this.score += score;
    }
    
    public void subTotalScore() {
        this.score -= 2;
        if (this.score <= 0) {
            this.score = 0;
        }
    }
    
    public void substractPkValue(short pkValue) {
        this.pkValue -= pkValue;
    }
    
    public void updateTeamMember(Team team, byte state) {
        List<Member> members = this.getMembers(team);
        this.members = members;
        this.state = state;
    }
    
    public void updateTeamMember(Team team) {
        List<Member> members = this.getMembers(team);
        this.members = members;
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
    
    public List<Member> getMembers() {
        return this.members;
    }
    
    public long getPrevPKTime() {
        return this.prevPKTime;
    }
    
    public byte getState() {
        return this.state;
    }
    
    public long getLeave() {
        return this.leave;
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
    
    public void setMembers(final List<Member> members) {
        this.members = members;
    }
    
    public void setPrevPKTime(final long prevPKTime) {
        this.prevPKTime = prevPKTime;
    }
    
    public void setState(final byte state) {
        this.state = state;
    }
    
    public void setLeave(final long leave) {
        this.leave = leave;
    }
    
    @Override
    public String toString() {
        return "AtWillPkData(teamId=" + this.getTeamId() + ", pkValue=" + (int)this.getPkValue() + ", score=" + (int)this.getScore() + ", members=" + this.getMembers() + ", prevPKTime=" + this.getPrevPKTime() + ", state=" + (int)this.getState() + ", leave=" + this.getLeave() + ")";
    }
}
