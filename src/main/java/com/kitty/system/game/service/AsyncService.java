
package com.kitty.system.game.service;

import com.kitty.common.start.ServerService;
import com.kitty.cache.JedisUtil;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.model.Pos;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.vo.TitleInfo;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.function.Function;
import java.util.Comparator;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import java.util.HashSet;
import com.kitty.mina.cache.DataCache;
import java.util.Set;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.game.npc.domain.Mirror;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
import java.io.Serializable;
import com.kitty.system.game.task.domain.vo.RecordHallQuestion;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.task.service.handler.activity.KejuTaskHandler;
import java.util.Map;
import org.slf4j.LoggerFactory;
import com.kitty.cache.RedisService;
import com.kitty.system.game.role.service.TitleService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.jdbc.core.JdbcTemplate;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class AsyncService
{
    private final Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PetService petService;
    @Autowired
    private TitleService titleService;
    @Autowired
    private RedisService redisService;
    
    public AsyncService() {
        this.logger = LoggerFactory.getLogger(AsyncService.class);
    }
    
    @Async("asyncServiceExecutor")
    public void kjHallQuestionAward(final Map<Integer, Boolean> memberMap, final int serial, final int num) {
        if (serial <= 0 || num <= 0 || num > 10) {
            return;
        }
        final KejuTaskHandler kejuTaskHandler = SpringUtils.getBean(KejuTaskHandler.class);
        for (final Integer key : memberMap.keySet()) {
            final Boolean flag = memberMap.get(key);
            if (!flag) {
                continue;
            }
            final Role role = this.roleService.getPlayer(key);
            if (role == null) {
                continue;
            }
            final RecordHallQuestion question = this.redisService.get("hall:" + role.getRoleId() + "_" + serial + "_" + num, RecordHallQuestion.class);
            if (question == null) {
                this.logger.error("玩家{}科举殿试，第{}道题获取为空", (Object)role.getName(), (Object)num);
            }
            else {
                String msg = "很可惜，你答错了#<E:17>#";
                int multiple = 3;
                if (question.getR() == 1) {
                    multiple = 4;
                    msg = "恭喜您答对了#<E:93>#";
                }
                this.roleService.addExp(role, DataExpFunc.calculateKj(role.getLevel(), role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "殿试答题", true);
                final Integer combatPet = role.getExtendBox().getCombatPet();
                if (combatPet > 0) {
                    final Pet pet = this.petService.selectPet(role).get(combatPet);
                    this.petService.addExp(role, pet, DataExpFunc.calculateKj(pet.getPetLv(), pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
                }
                if (question.getR() == 1) {
                    kejuTaskHandler.award(role, PartDataPool.commonAwardMap.get("hall"));
                }
                MessagePusher.pushMessage(role, new RespMsg(msg));
                if (num < 10) {
                    continue;
                }
                kejuTaskHandler.leave(role, true);
            }
        }
        if (num >= 10) {
            kejuTaskHandler.rank();
        }
    }
    
    @Async("asyncServiceExecutor")
    public void clearKjTitle() {
        final List<Long> roles = this.jdbcTemplate.queryForList("select id from role;", (Class)Long.class);
        for (final Long id : roles) {
            final Role player = this.roleService.getPlayer(id);
            if (player != null) {
                try {
                    for (final Integer serial : KejuTaskHandler.TITLE) {
                        this.titleService.delTitle(player, serial);
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void clearWaterTitle() {
        final List<Long> roles = this.jdbcTemplate.queryForList("select id from role;", (Class)Long.class);
        for (final Long id : roles) {
            final Role player = this.roleService.getPlayer(id);
            if (player != null) {
                try {
                    this.titleService.delTitle(player, 400001);
                    this.titleService.delTitle(player, 400002);
                    this.titleService.delTitle(player, 400003);
                    this.titleService.delTitle(player, 400004);
                    this.titleService.delTitle(player, 400005);
                    this.titleService.delTitle(player, 400006);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void awardWaterSeason() {
        final int map = 1000;
        final List<Mirror> mirrors = this.dao.query(Mirror.class, Cnd.where("type", "=", 3003));
        Set<Long> set = DataCache.MIRROR_MAP_NPC.get(map);
        if (set == null) {
            set = new HashSet<Long>();
        }
        for (final Mirror mirror : mirrors) {
            set.remove(mirror.getRId());
            mirror.setDelete(true);
            mirror.save();
        }
        DataCache.MIRROR_MAP.clear();
        final List<WaterRankRecord> list = RankingList.WATER_RANK_RECORDS.values().stream().sorted(Comparator.comparing(WaterRankRecord::getScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getEquipScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        for (int i = 0; i < list.size(); ++i) {
            final WaterRankRecord record = list.get(i);
            final Role role = this.roleService.getPlayer(record.getRId());
            if (role != null) {
                if (i < 5) {
                    this.titleService.addTitle(role, new TitleInfo(400001, 0));
                    final String[] split = Const.WATER_MIRRIR_POS.get(i).split(",");
                    final String pos = JsonUtils.object2String(new Pos(Integer.valueOf(split[0]), Integer.valueOf(split[1]), 5, map));
                    final Mirror mirror2 = new Mirror(this.worker.nextId(), role.getId(), "水陆战神", "水陆战神很忙，此处什么都没有留下……", pos, 3003);
                    DataCache.MIRROR_MAP.put(role.getId(), mirror2);
                    set.add(mirror2.getRId());
                    mirror2.save();
                }
                else if (i >= 5 && i < 20) {
                    this.titleService.addTitle(role, new TitleInfo(400002, 0));
                }
                else if (i >= 20 && i < 100) {
                    this.titleService.addTitle(role, new TitleInfo(400003, 0));
                }
                else if (i >= 100 && record.getScore() >= 1000) {
                    this.titleService.addTitle(role, new TitleInfo(400004, 0));
                }
                else if (i >= 100 && record.getScore() >= 500 && record.getScore() <= 999) {
                    this.titleService.addTitle(role, new TitleInfo(400005, 0));
                }
                else if (record.getScore() >= 1) {
                    this.titleService.addTitle(role, new TitleInfo(400006, 0));
                }
            }
        }
        if (set.size() > 0) {
            DataCache.MIRROR_MAP_NPC.put(map, set);
        }
    }
    
    @Async("asyncServiceExecutor")
    public void clearCaTitle() {
        final List<Long> roles = this.jdbcTemplate.queryForList("select id from role;", (Class)Long.class);
        for (final Long id : roles) {
            final Role player = this.roleService.getPlayer(id);
            if (player != null) {
                try {
                    this.titleService.delTitle(player, 900003);
                    this.titleService.delTitle(player, 900002);
                    this.titleService.delTitle(player, 900001);
                    this.titleService.delTitle(player, 900005);
                    this.titleService.delTitle(player, 900004);
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void awardCaSeason() {
        for (final Long key : RankingList.CA_RANK_RECORDS.keySet()) {
            final CaRankRecord record = RankingList.CA_RANK_RECORDS.get(key);
            final Role role = this.roleService.getPlayer(key);
            if (role == null) {
                continue;
            }
            if (record.getGrade() < 1080) {
                if (record.getFullRank() <= 100) {
                    this.titleService.addTitle(role, new TitleInfo(900003, 0));
                }
                else {
                    if (record.getScore() < 5000) {
                        continue;
                    }
                    this.titleService.addTitle(role, new TitleInfo(900004, 0));
                }
            }
            else if (record.getGrade() < 2120) {
                if (record.getFullRank() <= 100) {
                    this.titleService.addTitle(role, new TitleInfo(900002, 0));
                }
                else {
                    if (record.getScore() < 5000) {
                        continue;
                    }
                    this.titleService.addTitle(role, new TitleInfo(900004, 0));
                }
            }
            else if (record.getGrade() < 3120) {
                if (record.getFullRank() <= 100) {
                    this.titleService.addTitle(role, new TitleInfo(900001, 0));
                }
                else {
                    if (record.getScore() < 5000) {
                        continue;
                    }
                    this.titleService.addTitle(role, new TitleInfo(900004, 0));
                }
            }
            else if (record.getFullRank() <= 100) {
                this.titleService.addTitle(role, new TitleInfo(900005, 0));
            }
            else {
                if (record.getScore() < 5000) {
                    continue;
                }
                this.titleService.addTitle(role, new TitleInfo(900004, 0));
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void syncCaRan() {
        List<CaCache> oneList = new ArrayList<CaCache>();
        List<CaCache> twoList = new ArrayList<CaCache>();
        List<CaCache> triList = new ArrayList<CaCache>();
        List<CaCache> fourList = new ArrayList<CaCache>();
        final Set<String> keys = JedisUtil.keys("jzca:*");
        if (keys.size() <= 0) {
            return;
        }
        for (final String key : keys) {
            final String value = JedisUtil.get(key);
            if (value != null) {
                try {
                    final CaCache caCache = JsonUtils.string2Object(value, CaCache.class);
                    if (caCache.getGrade() < 1080) {
                        oneList.add(caCache);
                    }
                    else if (caCache.getGrade() < 2120) {
                        twoList.add(caCache);
                    }
                    else if (caCache.getGrade() < 3120) {
                        triList.add(caCache);
                    }
                    else {
                        fourList.add(caCache);
                    }
                }
                catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
        final String marking = SpringUtils.getBean(ServerService.class).getMarking();
        oneList = oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        twoList = oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        triList = oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        fourList = oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        this.syncCaRan(marking, oneList);
        this.syncCaRan(marking, twoList);
        this.syncCaRan(marking, triList);
        this.syncCaRan(marking, fourList);
    }
    
    public void syncCaRan(final String marking, final List<CaCache> list) {
        for (int i = 0; i < list.size(); ++i) {
            final CaCache caCache = list.get(i);
            if (caCache == null || caCache.getMarking() == null) {
                this.logger.error("syncCaRan数据错误：{}", (Object)caCache);
            }
            else if (caCache.getMarking().equals(marking)) {
                final CaRankRecord record = RankingList.CA_RANK_RECORDS.get(caCache.getId());
                if (record != null) {
                    record.setFullRank(i + 1);
                    record.save();
                }
            }
        }
    }
}
