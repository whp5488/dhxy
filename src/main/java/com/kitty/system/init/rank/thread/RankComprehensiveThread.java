
package com.kitty.system.init.rank.thread;

import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import java.util.Map;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.pet.domain.Pet;
import org.apache.commons.collections.map.HashedMap;
import java.util.Collection;
import org.apache.commons.collections.CollectionUtils;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import com.kitty.system.game.role.domain.Role;
import java.util.List;

public class RankComprehensiveThread implements Runnable
{
    private int index;
    private List<Role> list;
    private Semaphore semaphore;
    private CountDownLatch countDownLatch;
    
    public RankComprehensiveThread(final int index, final List<Role> list, final CountDownLatch countDownLatch, final Semaphore semaphore) {
        this.index = 0;
        this.index = index;
        this.list = list;
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
        final Object[] objects = { new HashedMap() };
        role.doAfterInit();
        objects[0] = RankingUtils.getTotalRepair(role, RankingList.petMaps.get(role.getRoleId()), RankingList.wearEquipMaps.get(role.getRoleId()));
        final ComprehensiveStrengthSource ComprehensiveStrengthSource = new ComprehensiveStrengthSource(role, (Map<String, Integer>)objects[0]);
        RankingList.comprehensiveStrengthSources.add(ComprehensiveStrengthSource);
    }
}
