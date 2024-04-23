package com.kitty.system.game.party.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.party.message.vo.PartyFlag;
import java.util.ArrayList;
import java.util.List;

@MessageMeta(module = 11802)
public class RespPartyFlag extends Message {
  public void setFlagList(List<PartyFlag> flagList) {
    this.flagList = flagList;
  }
  
  public void setFlagColorList(List<PartyFlag> flagColorList) {
    this.flagColorList = flagColorList;
  }
  
  public void setLaceList(List<PartyFlag> laceList) {
    this.laceList = laceList;
  }
  
  public void setEmblemList(List<PartyFlag> emblemList) {
    this.emblemList = emblemList;
  }
  
  public void setEmblemColorList(List<PartyFlag> emblemColorList) {
    this.emblemColorList = emblemColorList;
  }
  
  public void setText(String text) {
    this.text = text;
  }
  
  private List<PartyFlag> flagList = new ArrayList<>();
  
  public List<PartyFlag> getFlagList() {
    return this.flagList;
  }
  
  private List<PartyFlag> flagColorList = new ArrayList<>();
  
  public List<PartyFlag> getFlagColorList() {
    return this.flagColorList;
  }
  
  private List<PartyFlag> laceList = new ArrayList<>();
  
  public List<PartyFlag> getLaceList() {
    return this.laceList;
  }
  
  private List<PartyFlag> emblemList = new ArrayList<>();
  
  public List<PartyFlag> getEmblemList() {
    return this.emblemList;
  }
  
  private List<PartyFlag> emblemColorList = new ArrayList<>();
  
  private String text;
  
  public List<PartyFlag> getEmblemColorList() {
    return this.emblemColorList;
  }
  
  public String getText() {
    return this.text;
  }
}
