package com.kitty.system.init.rank.thread;

import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.map.HashedMap;

public class RankComprehensiveThread implements Runnable {
  private int index = 0;
  
  private List<Role> list;
  
  private Semaphore semaphore;
  
  private CountDownLatch countDownLatch;
  
  public RankComprehensiveThread(int index, List<Role> list, CountDownLatch countDownLatch, Semaphore semaphore) {
    this.index = index;
    this.list = list;
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
    Object[] objects = { new HashedMap() };
    role.doAfterInit();
    objects[0] = RankingUtils.getTotalRepair(role, (List)RankingList.petMaps.get(role.getRoleId()), (List)RankingList.wearEquipMaps.get(role.getRoleId()));
    ComprehensiveStrengthSource ComprehensiveStrengthSource = new ComprehensiveStrengthSource(role, (Map)objects[0]);
    RankingList.comprehensiveStrengthSources.add(ComprehensiveStrengthSource);
  }
}
