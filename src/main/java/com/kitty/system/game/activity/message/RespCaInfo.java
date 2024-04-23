package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.activity.message.vo.CaFightInfo;
import com.kitty.system.game.activity.message.vo.MemberInfo;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 49182)
public class RespCaInfo extends Message {
  public void setRound(byte round) {
    this.round = round;
  }
  
  public void setCurrentSession(byte currentSession) {
    this.currentSession = currentSession;
  }
  
  public void setItself(List<MemberInfo> itself) {
    this.itself = itself;
  }
  
  public void setGroup(byte group) {
    this.group = group;
  }
  
  public void setList(List<CaFightInfo> list) {
    this.list = list;
  }
  
  private byte round = 1;
  
  public byte getRound() {
    return this.round;
  }
  
  private byte currentSession = 0;
  
  public byte getCurrentSession() {
    return this.currentSession;
  }
  
  private List<MemberInfo> itself = new ArrayList<>(5);
  
  private byte group;
  
  private List<CaFightInfo> list;
  
  public List<MemberInfo> getItself() {
    return this.itself;
  }
  
  public byte getGroup() {
    return this.group;
  }
  
  public List<CaFightInfo> getList() {
    return this.list;
  }
}
