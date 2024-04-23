package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 61601)
public class RespOpenParty extends Message {
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
  
  public void setSerial(int serial) {
    this.serial = serial;
  }
  
  public void setCreateTime(int createTime) {
    this.createTime = createTime;
  }
  
  public void setLevel(byte level) {
    this.level = level;
  }
  
  public void setCount(short count) {
    this.count = count;
  }
  
  public void setOnlineMember(short onlineMember) {
    this.onlineMember = onlineMember;
  }
  
  public void setName(String name) {
    this.name = name;
  }
  
  public void setNotice(String notice) {
    this.notice = notice;
  }
  
  public void setAim(String aim) {
    this.aim = aim;
  }
  
  public void setJoinTime(int joinTime) {
    this.joinTime = joinTime;
  }
  
  public void setActive(short active) {
    this.active = active;
  }
  
  public void setCapital(String capital) {
    this.capital = capital;
  }
  
  public void setCntech(String cntech) {
    this.cntech = cntech;
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
  
  public void setJob(byte job) {
    this.job = job;
  }
  
  public void setSum(short sum) {
    this.sum = sum;
  }
  
  public void setWin(short win) {
    this.win = win;
  }
  
  public void setLose(short lose) {
    this.lose = lose;
  }
  
  public void setStandings(short standings) {
    this.standings = standings;
  }
  
  public void setMaster(List<FiedValue> master) {
    this.master = master;
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
  
  private byte job = 8;
  
  public byte getJob() {
    return this.job;
  }
  
  private short sum = 0;
  
  public short getSum() {
    return this.sum;
  }
  
  private short win = 0;
  
  public short getWin() {
    return this.win;
  }
  
  private short lose = 0;
  
  public short getLose() {
    return this.lose;
  }
  
  private short standings = 0;
  
  public short getStandings() {
    return this.standings;
  }
  
  private List<FiedValue> master = new ArrayList<>();
  
  public List<FiedValue> getMaster() {
    return this.master;
  }
}
