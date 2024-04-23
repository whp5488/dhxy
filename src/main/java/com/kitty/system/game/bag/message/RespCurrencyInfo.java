package com.kitty.system.game.bag.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import com.kitty.system.game.role.domain.Role;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@MessageMeta(module = 54977)
public class RespCurrencyInfo extends Message {
  private String gold;
  
  private String silver;
  
  private String coin;
  
  private String guoScore;
  
  private String bracScore;
  
  private String naoScore;
  
  private String diScore;
  
  private String arenaScore;
  
  private String longScore;
  
  private String niceScore;
  
  private String helpScore;
  
  private String featsScore;
  
  private String chargeScore;
  
  private String boundGold;

  public RespCurrencyInfo(Role role) {
    this.gold = String.valueOf(role.getGold());
    this.silver = String.valueOf(role.getSilver());
    this.coin = String.valueOf(role.getCoin());
    this.guoScore = String.valueOf(role.getExtendBox().getGuoScore());
    this.bracScore = String.valueOf(role.getExtendBox().getBracScore());
    this.naoScore = String.valueOf(role.getExtendBox().getNaoScore());
    this.diScore = String.valueOf(role.getExtendBox().getDiScore());
    this.arenaScore = String.valueOf(role.getExtendBox().getArenaScore());
    this.longScore = String.valueOf(role.getExtendBox().getLongScore());
    this.niceScore = String.valueOf(role.getExtendBox().getNiceScore());
    this.helpScore = String.valueOf(role.getExtendBox().getHelpScore());
    this.featsScore = String.valueOf(role.getExtendBox().getFeatsScore());
    this.chargeScore = String.valueOf(role.getExtendBox().getChargeScore());
    this.boundGold = String.valueOf(role.getExtendBox().getBoundGold());
  }
}
