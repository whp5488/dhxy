
package com.kitty.system.init.rank.thread;

import java.util.Iterator;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.ranking.RankingList;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import java.util.List;

public class RankEquipThread implements Runnable
{
    private int index;
    private List<Role> list;
    private Map<Long, List<WearEquip>> wearEquipMap;
    private Semaphore semaphore;
    private CountDownLatch countDownLatch;
    
    public RankEquipThread(final int index, final List<Role> list, final Map<Long, List<WearEquip>> wearEquipMap, final CountDownLatch countDownLatch, final Semaphore semaphore) {
        this.index = 0;
        this.index = index;
        this.list = list;
        this.wearEquipMap = wearEquipMap;
        this.countDownLatch = countDownLatch;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        if (!CollectionUtils.isEmpty((Collection)this.list)) {
            try {
                this.semaphore.acquire();
                this.list.stream().forEach(e -> this.neaten(e));
            }
            catch (final InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                this.semaphore.release();
            }
        }
        this.countDownLatch.countDown();
    }
    
    public void neaten(final Role role) {
        final List<WearEquip> wearEquips = this.wearEquipMap.get(role.getId());
        if (wearEquips != null) {
            for (final WearEquip wearEquip : wearEquips) {
                wearEquip.doAfterInit();
                RankingList.weaponsRankingSources.add(RankingUtils.createWeaponsRankingSource(role, wearEquip, 0));
            }
        }
    }
}
