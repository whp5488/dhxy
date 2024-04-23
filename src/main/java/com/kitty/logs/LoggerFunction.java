package com.kitty.logs;

import org.slf4j.Logger;

public enum LoggerFunction {
  MARKET, HORSE, MAIL, FIGHT, UPGRADE_EQUIP, FEED_PET, FEED_PARTNER, ITEM_PARTNER, GM, TASK, ACTIVITY, GOLD, MONEY, PARTY, SCENE, FORBID, CROSS, PAY, MONOIOR, RUNE, BUG;
  
  public Logger getLogger() {
    return (Logger)LoggerBuilder.getLogger(name().toLowerCase());
  }
}
