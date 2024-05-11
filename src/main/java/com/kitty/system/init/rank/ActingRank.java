
package com.kitty.system.init.rank;

import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.init.rank.thread.RankEquipThread;
import com.kitty.system.init.rank.thread.RankComprehensiveThread;
import java.util.concurrent.ThreadPoolExecutor;
import com.kitty.system.init.rank.thread.RankPetThread;
import java.util.concurrent.Semaphore;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.cache.JedisUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import java.util.Map;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.service.InitService;

public class ActingRank
{
    private static int MAX_THREADS;
    private static int EXPIRED_PAGE_SIZE;
    
    public static void initRank() {
        final InitService initService = SpringUtils.getBean(InitService.class);
        initCaRank(initService);
    }
    
    public static void initCaRank(final InitService initService) {
        final Map<Long, CaRankRecord> longCaRankRecordMap = initService.initCaRank();
        RankingList.CA_RANK_RECORDS.putAll(longCaRankRecordMap);
        final RoleService roleService = SpringUtils.getRoleService();
        final ServerService serverService = SpringUtils.getBean(ServerService.class);
        for (final Long key : longCaRankRecordMap.keySet()) {
            final Role role = roleService.getPlayer(key);
            if (role != null) {
                final CaRankRecord record = longCaRankRecordMap.get(key);
                final CaCache caCache = new CaCache();
                caCache.setId(role.getId());
                caCache.setRoleId(role.getRoleId());
                caCache.setSerial(role.getSerial());
                caCache.setName(role.getName());
                caCache.setScore(record.getScore());
                caCache.setGrade(role.getRelive() * 1000 + role.getLevel());
                caCache.setFactionName(roleService.getFactionName(role));
                caCache.setMarking(role.getMarking());
                JedisUtil.set("jzca:" + serverService.getMarking() + "_" + key, JsonUtils.object2String(caCache));
            }
        }
    }
    
    public static void initPetRanking(final List<Role> roles, final List<Pet> pets) {
        if (pets == null || roles == null) {
            return;
        }
        final Map<Integer, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getRoleId, Role -> Role, (oldValue, newValue) -> newValue));
        final int listSize = pets.size();
        int runSize;
        if (listSize % ActingRank.EXPIRED_PAGE_SIZE == 0) {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE;
        }
        else {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE + 1;
        }
        final ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
        final CountDownLatch countDownLatch = new CountDownLatch(runSize);
        final Semaphore semaphore = new Semaphore(ActingRank.MAX_THREADS);
        List handleList = null;
        for (int i = 0; i < runSize; ++i) {
            if (i + 1 == runSize) {
                final int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                final int endIndex = pets.size();
                handleList = pets.subList(startIndex, endIndex);
            }
            else {
                final int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                final int endIndex = (i + 1) * ActingRank.EXPIRED_PAGE_SIZE;
                handleList = pets.subList(startIndex, endIndex);
            }
            final RankPetThread task = new RankPetThread(i, handleList, roleMap, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
    }
    
    public static void initComprehensiveStrengthSource(final List<Role> list) {
        final int listSize = list.size();
        int runSize;
        if (listSize % ActingRank.EXPIRED_PAGE_SIZE == 0) {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE;
        }
        else {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE + 1;
        }
        final ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
        final CountDownLatch countDownLatch = new CountDownLatch(runSize);
        final Semaphore semaphore = new Semaphore(ActingRank.MAX_THREADS);
        List handleList = null;
        for (int i = 0; i < runSize; ++i) {
            if (i + 1 == runSize) {
                final int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                final int endIndex = list.size();
                handleList = list.subList(startIndex, endIndex);
            }
            else {
                final int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                final int endIndex = (i + 1) * ActingRank.EXPIRED_PAGE_SIZE;
                handleList = list.subList(startIndex, endIndex);
            }
            final RankComprehensiveThread task = new RankComprehensiveThread(i, handleList, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
    }
    
    public static void initWeaponsRankingSource(final List<Role> list) {
        final Map<Long, List<WearEquip>> wearEquipMap = RankingList.wearEquipMaps;
        if (list == null || list.size() <= 0 || wearEquipMap == null || wearEquipMap.size() <= 0) {
            return;
        }
        final int listSize = list.size();
        int runSize;
        if (listSize % ActingRank.EXPIRED_PAGE_SIZE == 0) {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE;
        }
        else {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE + 1;
        }
        final ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
        final CountDownLatch countDownLatch = new CountDownLatch(runSize);
        final Semaphore semaphore = new Semaphore(ActingRank.MAX_THREADS);
        List handleList = null;
        for (int i = 0; i < runSize; ++i) {
            if (i + 1 == runSize) {
                final int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                final int endIndex = list.size();
                handleList = list.subList(startIndex, endIndex);
            }
            else {
                final int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                final int endIndex = (i + 1) * ActingRank.EXPIRED_PAGE_SIZE;
                handleList = list.subList(startIndex, endIndex);
            }
            final RankEquipThread task = new RankEquipThread(i, handleList, wearEquipMap, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        }
        catch (final InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
    }
    
    public static void initRoleRune(final List<Role> roles) {
        final RoleTianFuService fuService = SpringUtils.getBean(RoleTianFuService.class);
        final RoleDayInCeService ceService = SpringUtils.getBean(RoleDayInCeService.class);
        for (final Role role : roles) {
            ceService.getDataForRoleId(role.getId());
            fuService.getDataByRoleId(role.getId());
        }
    }
    
    public static void clearRoleRune() {
        DataCache.ROLE_DAY_IN_CE.clear();
        DataCache.ROLE_TIAN_FU.clear();
    }
    
    static {
        ActingRank.MAX_THREADS = 16;
        ActingRank.EXPIRED_PAGE_SIZE = 1000;
    }
}
