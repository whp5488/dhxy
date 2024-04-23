package com.kitty.system.game.activity.message;

import com.kitty.mina.annotation.MessageMeta;
import com.kitty.mina.message.Message;
import java.beans.ConstructorProperties;

@MessageMeta(module = 49168)
public class RespGangWarsGateLife extends Message {
  public void setSideLife(short sideLife) {
    this.sideLife = sideLife;
  }
  
  public void setEnemyLife(short enemyLife) {
    this.enemyLife = enemyLife;
  }
  
  public void setType(byte type) {
    this.type = type;
  }
  
  public void setStance(byte stance) {
    this.stance = stance;
  }
  
  @ConstructorProperties({"sideLife", "enemyLife", "type", "stance"})
  public RespGangWarsGateLife(short sideLife, short enemyLife, byte type, byte stance) {
    this.sideLife = sideLife;
    this.enemyLife = enemyLife;
    this.type = type;
    this.stance = stance;
  }
  
  private short sideLife = 0;
  
  public short getSideLife() {
    return this.sideLife;
  }
  
  private short enemyLife = 0;
  
  private byte type;
  
  private byte stance;
  
  public short getEnemyLife() {
    return this.enemyLife;
  }
  
  public byte getType() {
    return this.type;
  }
  
  public byte getStance() {
    return this.stance;
  }
  
  public RespGangWarsGateLife() {}
}
