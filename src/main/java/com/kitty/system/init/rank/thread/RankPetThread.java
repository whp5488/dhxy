package com.kitty.system.init.rank.thread;

import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.pojo.PetRankingSource;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.common.utils.spring.SpringUtils;
import org.apache.commons.collections.CollectionUtils;
import com.kitty.system.game.pet.pool.PetDataPool;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Semaphore;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.role.domain.Role;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;

public class RankPetThread implements Runnable
{
    private int index;
    private List<Pet> list;
    private Map<Integer, Role> roleMap;
    private Map<Integer, SysPet> sysPetMap;
    private Semaphore semaphore;
    private CountDownLatch countDownLatch;
    
    public RankPetThread(int index, List<Pet> list, Map<Integer, Role> roleMap, CountDownLatch countDownLatch, Semaphore semaphore) {
        this.index = 0;
        this.sysPetMap = PetDataPool.petMap;
        this.index = index;
        this.list = list;
        this.roleMap = roleMap;
        this.countDownLatch = countDownLatch;
        this.semaphore = semaphore;
    }
    
    @Override
    public void run() {
        if (!CollectionUtils.isEmpty(this.list)) {
            try {
                this.semaphore.acquire();
                this.list.stream().forEach(e -> this.neaten(e));
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            finally {
                this.semaphore.release();
            }
        }
        this.countDownLatch.countDown();
    }
    
    public void neaten(Pet pet) {
        pet.doAfterInit();
        Role role = (Role)this.roleMap.get(pet.getRoleId());
        if (role == null) {
            return;
        }
        List<PetEquip> equips = SpringUtils.getPetService().getPetEffective((long)role.getId(), pet);
        PetRankingSource petRankingSource = new PetRankingSource(role, pet, ((SysPet)this.sysPetMap.get(pet.getPetType())).getName(), CountUtils.countPetSpeed(pet, false), CountUtils.countPetPhyPower(pet, equips));
        RankingList.petRankingListSource.add(petRankingSource);
        return;
    }
}
