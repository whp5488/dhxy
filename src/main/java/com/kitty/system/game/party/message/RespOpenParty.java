package com.kitty.system.game.party.message;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;

@MessageMeta(module = 61601)
public class RespOpenParty extends Message
{
    private int serial;
    private int createTime;
    private byte level;
    private short count;
    private short onlineMember;
    private String name;
    private String notice;
    private String aim;
    private int joinTime;
    private short active;
    private String capital;
    private String cntech;
    private byte consent;
    private short consentLevel;
    private byte refuse;
    private short refuseLevel;
    private byte job;
    private short sum;
    private short win;
    private short lose;
    private short standings;
    private List<FiedValue> master;
    
    public RespOpenParty() {
        this.job = 8;
        this.sum = 0;
        this.win = 0;
        this.lose = 0;
        this.standings = 0;
        this.master = new ArrayList<>();
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public int getCreateTime() {
        return this.createTime;
    }
    
    public byte getLevel() {
        return this.level;
    }
    
    public short getCount() {
        return this.count;
    }
    
    public short getOnlineMember() {
        return this.onlineMember;
    }
    
    public String getName() {
        return this.name;
    }
    
    public String getNotice() {
        return this.notice;
    }
    
    public String getAim() {
        return this.aim;
    }
    
    public int getJoinTime() {
        return this.joinTime;
    }
    
    public short getActive() {
        return this.active;
    }
    
    public String getCapital() {
        return this.capital;
    }
    
    public String getCntech() {
        return this.cntech;
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
    
    public byte getJob() {
        return this.job;
    }
    
    public short getSum() {
        return this.sum;
    }
    
    public short getWin() {
        return this.win;
    }
    
    public short getLose() {
        return this.lose;
    }
    
    public short getStandings() {
        return this.standings;
    }
    
    public List<FiedValue> getMaster() {
        return this.master;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setCreateTime(final int createTime) {
        this.createTime = createTime;
    }
    
    public void setLevel(final byte level) {
        this.level = level;
    }
    
    public void setCount(final short count) {
        this.count = count;
    }
    
    public void setOnlineMember(final short onlineMember) {
        this.onlineMember = onlineMember;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setNotice(final String notice) {
        this.notice = notice;
    }
    
    public void setAim(final String aim) {
        this.aim = aim;
    }
    
    public void setJoinTime(final int joinTime) {
        this.joinTime = joinTime;
    }
    
    public void setActive(final short active) {
        this.active = active;
    }
    
    public void setCapital(final String capital) {
        this.capital = capital;
    }
    
    public void setCntech(final String cntech) {
        this.cntech = cntech;
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
    
    public void setJob(final byte job) {
        this.job = job;
    }
    
    public void setSum(final short sum) {
        this.sum = sum;
    }
    
    public void setWin(final short win) {
        this.win = win;
    }
    
    public void setLose(final short lose) {
        this.lose = lose;
    }
    
    public void setStandings(final short standings) {
        this.standings = standings;
    }
    
    public void setMaster(final List<FiedValue> master) {
        this.master = master;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof RespOpenParty)) {
            return false;
        }
        RespOpenParty other = (RespOpenParty)o;
        if (!other.canEqual(this)) {
            return false;
        }
        if (this.getSerial() != other.getSerial()) {
            return false;
        }
        if (this.getCreateTime() != other.getCreateTime()) {
            return false;
        }
        if (this.getLevel() != other.getLevel()) {
            return false;
        }
        if (this.getCount() != other.getCount()) {
            return false;
        }
        if (this.getOnlineMember() != other.getOnlineMember()) {
            return false;
        }
        Object this$name = this.getName();
        Object other$name = other.getName();
        if (!((this$name != null) ? this$name.equals(other$name) : (other$name == null))) {
            return false;
        }
        Object this$notice = this.getNotice();
        Object other$notice = other.getNotice();
        if (!((this$notice != null) ? this$notice.equals(other$notice) : (other$notice == null))) {
            return false;
        }
        Object this$aim = this.getAim();
        Object other$aim = other.getAim();
        if (!((this$aim != null) ? this$aim.equals(other$aim) : (other$aim == null))) {
            return false;
        }
        if (this.getJoinTime() != other.getJoinTime()) {
            return false;
        }
        if (this.getActive() != other.getActive()) {
            return false;
        }
        Object this$capital = this.getCapital();
        Object other$capital = other.getCapital();
        if (!((this$capital != null) ? this$capital.equals(other$capital) : (other$capital == null))) {
            return false;
        }
        Object this$cntech = this.getCntech();
        Object other$cntech = other.getCntech();
        if (!((this$cntech != null) ? this$cntech.equals(other$cntech) : (other$cntech == null))) {
            return false;
        }
        if (this.getConsent() != other.getConsent()) {
            return false;
        }
        if (this.getConsentLevel() != other.getConsentLevel()) {
            return false;
        }
        if (this.getRefuse() != other.getRefuse()) {
            return false;
        }
        if (this.getRefuseLevel() != other.getRefuseLevel()) {
            return false;
        }
        if (this.getJob() != other.getJob()) {
            return false;
        }
        if (this.getSum() != other.getSum()) {
            return false;
        }
        if (this.getWin() != other.getWin()) {
            return false;
        }
        if (this.getLose() != other.getLose()) {
            return false;
        }
        if (this.getStandings() != other.getStandings()) {
            return false;
        }
        Object this$master = this.getMaster();
        Object other$master = other.getMaster();
        return (this$master != null) ? this$master.equals(other$master) : (other$master == null);
    }
    
    protected boolean canEqual(final Object other) {
        return other instanceof RespOpenParty;
    }
    
    @Override
    public int hashCode() {
        int PRIME = 59;
        int result = 1;
        result = result * 59 + this.getSerial();
        result = result * 59 + this.getCreateTime();
        result = result * 59 + this.getLevel();
        result = result * 59 + this.getCount();
        result = result * 59 + this.getOnlineMember();
        Object $name = this.getName();
        result = result * 59 + (($name == null) ? 43 : $name.hashCode());
        Object $notice = this.getNotice();
        result = result * 59 + (($notice == null) ? 43 : $notice.hashCode());
        Object $aim = this.getAim();
        result = result * 59 + (($aim == null) ? 43 : $aim.hashCode());
        result = result * 59 + this.getJoinTime();
        result = result * 59 + this.getActive();
        Object $capital = this.getCapital();
        result = result * 59 + (($capital == null) ? 43 : $capital.hashCode());
        Object $cntech = this.getCntech();
        result = result * 59 + (($cntech == null) ? 43 : $cntech.hashCode());
        result = result * 59 + this.getConsent();
        result = result * 59 + this.getConsentLevel();
        result = result * 59 + this.getRefuse();
        result = result * 59 + this.getRefuseLevel();
        result = result * 59 + this.getJob();
        result = result * 59 + this.getSum();
        result = result * 59 + this.getWin();
        result = result * 59 + this.getLose();
        result = result * 59 + this.getStandings();
        Object $master = this.getMaster();
        result = result * 59 + (($master == null) ? 43 : $master.hashCode());
        return result;
    }
    
    @Override
    public String toString() {
        return "RespOpenParty(serial=" + this.getSerial() + ", createTime=" + this.getCreateTime() + ", level=" + (int)this.getLevel() + ", count=" + (int)this.getCount() + ", onlineMember=" + (int)this.getOnlineMember() + ", name=" + this.getName() + ", notice=" + this.getNotice() + ", aim=" + this.getAim() + ", joinTime=" + this.getJoinTime() + ", active=" + (int)this.getActive() + ", capital=" + this.getCapital() + ", cntech=" + this.getCntech() + ", consent=" + (int)this.getConsent() + ", consentLevel=" + (int)this.getConsentLevel() + ", refuse=" + (int)this.getRefuse() + ", refuseLevel=" + (int)this.getRefuseLevel() + ", job=" + (int)this.getJob() + ", sum=" + (int)this.getSum() + ", win=" + (int)this.getWin() + ", lose=" + (int)this.getLose() + ", standings=" + (int)this.getStandings() + ", master=" + this.getMaster() + ")";
    }
}
