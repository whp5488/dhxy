package com.kitty.system.init.rank.thread;

import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import org.apache.commons.collections.CollectionUtils;

public class RankEquipThread implements Runnable {
  private int index = 0;
  
  private List<Role> list;
  
  private Map<Long, List<WearEquip>> wearEquipMap;
  
  private Semaphore semaphore;
  
  private CountDownLatch countDownLatch;
  
  public RankEquipThread(int index, List<Role> list, Map<Long, List<WearEquip>> wearEquipMap, CountDownLatch countDownLatch, Semaphore semaphore) {
    this.index = index;
    this.list = list;
    this.wearEquipMap = wearEquipMap;
    this.countDownLatch = countDownLatch;
    this.semaphore = semaphore;
  }
  
  public void run() {
    if (!CollectionUtils.isEmpty(this.list))
      try {
        this.semaphore.acquire();
        this.list.stream().forEach(e -> neaten(e));
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        this.semaphore.release();
      }  
    this.countDownLatch.countDown();
  }
  
  public void neaten(Role role) {
    List<WearEquip> wearEquips = this.wearEquipMap.get(role.getId());
    if (wearEquips != null)
      for (WearEquip wearEquip : wearEquips) {
        wearEquip.doAfterInit();
        RankingList.weaponsRankingSources.add(RankingUtils.createWeaponsRankingSource(role, wearEquip, 0));
      }  
  }
}
