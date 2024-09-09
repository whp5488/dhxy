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
import java.util.stream.Collectors;
import com.kitty.system.game.pet.domain.Pet;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.system.game.role.service.RoleService;
import java.util.Map;
import com.kitty.cache.JedisUtil;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.init.service.InitService;

public class ActingRank
{
    private static int MAX_THREADS;
    private static int EXPIRED_PAGE_SIZE;
    
    public ActingRank() {
    }
    
    public static void initRank() {
        InitService initService = SpringUtils.getBean(InitService.class);
        initCaRank(initService);
    }
    
    public static void initCaRank(InitService initService) {
        Map<Long, CaRankRecord> longCaRankRecordMap = initService.initCaRank();
        RankingList.CA_RANK_RECORDS.putAll(longCaRankRecordMap);
        RoleService roleService = SpringUtils.getRoleService();
        ServerService serverService = SpringUtils.getBean(ServerService.class);
        for (Long key : longCaRankRecordMap.keySet()) {
            Role role = roleService.getPlayer(key);
            if (role != null) {
                CaRankRecord record = longCaRankRecordMap.get(key);
                CaCache caCache = new CaCache();
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
    
    public static void initPetRanking(List<Role> roles, List<Pet> pets) {
        if (pets == null || roles == null) {
            return;
        }
        Map<Integer, Role> roleMap = roles.stream().collect(Collectors.toMap(Role::getRoleId, Role -> Role, (oldValue, newValue) -> newValue));
        int listSize = pets.size();
        int runSize;
        if (listSize % ActingRank.EXPIRED_PAGE_SIZE == 0) {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE;
        }
        else {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE + 1;
        }
        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
        CountDownLatch countDownLatch = new CountDownLatch(runSize);
        Semaphore semaphore = new Semaphore(ActingRank.MAX_THREADS);
        List<Pet> handleList = null;
        for (int i = 0; i < runSize; ++i) {
            if (i + 1 == runSize) {
                int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                int endIndex = pets.size();
                handleList = pets.subList(startIndex, endIndex);
            }
            else {
                int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                int endIndex = (i + 1) * ActingRank.EXPIRED_PAGE_SIZE;
                handleList = pets.subList(startIndex, endIndex);
            }
            RankPetThread task = new RankPetThread(i, handleList, roleMap, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
        return;
    }
    
    public static void initComprehensiveStrengthSource(List<Role> list) {
        int listSize = list.size();
        int runSize;
        if (listSize % ActingRank.EXPIRED_PAGE_SIZE == 0) {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE;
        }
        else {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE + 1;
        }
        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
        CountDownLatch countDownLatch = new CountDownLatch(runSize);
        Semaphore semaphore = new Semaphore(ActingRank.MAX_THREADS);
        List<Role> handleList = null;
        for (int i = 0; i < runSize; ++i) {
            if (i + 1 == runSize) {
                int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                int endIndex = list.size();
                handleList = list.subList(startIndex, endIndex);
            }
            else {
                int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                int endIndex = (i + 1) * ActingRank.EXPIRED_PAGE_SIZE;
                handleList = list.subList(startIndex, endIndex);
            }
            RankComprehensiveThread task = new RankComprehensiveThread(i, handleList, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
    }
    
    public static void initWeaponsRankingSource(List<Role> list) {
        Map<Long, List<WearEquip>> wearEquipMap = RankingList.wearEquipMaps;
        if (list == null || list.size() <= 0 || wearEquipMap == null || wearEquipMap.size() <= 0) {
            return;
        }
        int listSize = list.size();
        int runSize;
        if (listSize % ActingRank.EXPIRED_PAGE_SIZE == 0) {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE;
        }
        else {
            runSize = listSize / ActingRank.EXPIRED_PAGE_SIZE + 1;
        }
        ThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(runSize);
        CountDownLatch countDownLatch = new CountDownLatch(runSize);
        Semaphore semaphore = new Semaphore(ActingRank.MAX_THREADS);
        List<Role> handleList = null;
        for (int i = 0; i < runSize; ++i) {
            if (i + 1 == runSize) {
                int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                int endIndex = list.size();
                handleList = list.subList(startIndex, endIndex);
            }
            else {
                int startIndex = i * ActingRank.EXPIRED_PAGE_SIZE;
                int endIndex = (i + 1) * ActingRank.EXPIRED_PAGE_SIZE;
                handleList = list.subList(startIndex, endIndex);
            }
            RankEquipThread task = new RankEquipThread(i, handleList, wearEquipMap, countDownLatch, semaphore);
            executor.execute(task);
        }
        try {
            countDownLatch.await();
        }
        catch (InterruptedException e) {
            e.printStackTrace();
        }
        finally {
            executor.shutdown();
        }
    }
    
    public static void initRoleRune(List<Role> roles) {
        RoleTianFuService fuService = SpringUtils.getBean(RoleTianFuService.class);
        RoleDayInCeService ceService = SpringUtils.getBean(RoleDayInCeService.class);
        for (Role role : roles) {
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
