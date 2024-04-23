package com.kitty.system.game.tianfu.pojo;

import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Setter
@Getter
public class RoleTianFuClient {
  private String id;
  
  private int typeId;
  
  private byte grade;
  
  private byte usePos;
  
  private int bd;
  
  private short count;
  
  private int storePos;

  public RoleTianFuClient() {}

  public RoleTianFuClient(RoleTianFu roleTianFu) {
    this.id = String.valueOf(roleTianFu.getId());
    if (roleTianFu.getToIdentifyCeSerial() != 1) {
      this.typeId = roleTianFu.getToIdentifyCeSerial();
    } else {
      this.typeId = roleTianFu.getTianFuSerial();
    } 
    this.grade = roleTianFu.getLevel();
    Map<Long, String> tianFuGridMap = TianFuUtils.getTianFuGridMap((RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(Long.valueOf(roleTianFu.getRoleId())));
    String tianFuName = tianFuGridMap.get(roleTianFu.getId());
    Byte tianFuClientName = (Byte)TianFuUtils.server2clientGridMap.get(tianFuName);
    if (tianFuClientName != null && tianFuClientName.byteValue() > 0) {
      this.usePos = tianFuClientName.byteValue();
    } else {
      this.usePos = 0;
    } 
    this.bd = roleTianFu.getBd();
    this.count = roleTianFu.getCount();
    this.storePos = roleTianFu.getStorePos();
  }
}
