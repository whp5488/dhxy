package com.kitty.system.game.service;

import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.user.ca.vo.CaCache;
import com.kitty.cache.JedisUtil;
import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.model.Pos;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.vo.TitleInfo;
import java.util.stream.Collectors;
import java.util.Comparator;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import com.kitty.system.game.ranking.RankingList;
import java.util.HashSet;
import com.kitty.mina.cache.DataCache;
import java.util.Set;
import org.nutz.dao.Cnd;
import com.kitty.system.game.npc.domain.Mirror;
import java.util.List;
import org.springframework.scheduling.annotation.Async;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.common.data.DataExpFunc;
import com.kitty.system.enums.item.ItemClassify;
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
    public void kjHallQuestionAward(Map<Integer, Boolean> memberMap, int serial, int num) {
        if (serial <= 0 || num <= 0 || num > 10) {
            return;
        }
        KejuTaskHandler kejuTaskHandler = (KejuTaskHandler)SpringUtils.getBean(KejuTaskHandler.class);
        for (Integer key : memberMap.keySet()) {
            Boolean flag = (Boolean)memberMap.get(key);
            if (!(boolean)flag) {
                continue;
            }
            Role role = this.roleService.getPlayer((int)key);
            if (role != null) {
                RecordHallQuestion question = (RecordHallQuestion)this.redisService.get("hall:" + role.getRoleId() + "_" + serial + "_" + num, RecordHallQuestion.class);
                if (question == null) {
                    this.logger.error("玩家{}科举殿试，第{}道题获取为空", role.getName(), Integer.valueOf(num));
                }
                else {
                    String msg = "很可惜，你答错了#<E:17>#";
                    int multiple = 3;
                    if (question.getR() == 1) {
                        multiple = 4;
                        msg = "恭喜您答对了#<E:93>#";
                    }
                    this.roleService.addExp(role, DataExpFunc.calculateKj((int)role.getLevel(), (int)role.getRelive(), ItemClassify.HERO_EXP.getId(), multiple), "殿试答题", true);
                    Integer combatPet = role.getExtendBox().getCombatPet();
                    if ((int)combatPet > 0) {
                        Pet pet = (Pet)this.petService.selectPet(role).get(combatPet);
                        this.petService.addExp(role, pet, DataExpFunc.calculateKj((int)pet.getPetLv(), (int)pet.getRebirth(), ItemClassify.PET_EXP.getId(), multiple), true);
                    }
                    if (question.getR() == 1) {
                        kejuTaskHandler.award(role, (SysCommonAward)PartDataPool.commonAwardMap.get("hall"));
                    }
                    MessagePusher.pushMessage(role, new RespMsg(msg));
                    if (num >= 10) {
                        kejuTaskHandler.leave(role, true);
                    }
                    else {
                        continue;
                    }
                }
            }
            else {
                continue;
            }
        }
        if (num >= 10) {
            kejuTaskHandler.rank();
        }
        return;
    }
    
    @Async("asyncServiceExecutor")
    public void clearKjTitle() {
        List<Long> roles = this.jdbcTemplate.queryForList("select id from role;", Long.class);
        for (Long id : roles) {
            Role player = this.roleService.getPlayer((long)id);
            if (player != null) {
                try {
                    for (Integer serial : KejuTaskHandler.TITLE) {
                        this.titleService.delTitle(player, (int)serial);
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                continue;
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void clearWaterTitle() {
        List<Long> roles = this.jdbcTemplate.queryForList("select id from role;", Long.class);
        for (Long id : roles) {
            Role player = this.roleService.getPlayer((long)id);
            if (player != null) {
                try {
                    this.titleService.delTitle(player, 400001);
                    this.titleService.delTitle(player, 400002);
                    this.titleService.delTitle(player, 400003);
                    this.titleService.delTitle(player, 400004);
                    this.titleService.delTitle(player, 400005);
                    this.titleService.delTitle(player, 400006);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                continue;
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void awardWaterSeason() {
        int map = 1000;
        List<Mirror> mirrors = this.dao.query(Mirror.class, Cnd.where("type", "=", Integer.valueOf(3003)));
        Set<Long> set = (Set)DataCache.MIRROR_MAP_NPC.get(Integer.valueOf(map));
        if (set == null) {
            set = new HashSet();
        }
        for (Mirror mirror : mirrors) {
            set.remove(mirror.getRId());
            mirror.setDelete(true);
            mirror.save();
        }
        DataCache.MIRROR_MAP.clear();
        List<WaterRankRecord> list = (List)RankingList.WATER_RANK_RECORDS.values().stream().sorted(Comparator.comparing(WaterRankRecord::getScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getEquipScore, Comparator.reverseOrder()).thenComparing(WaterRankRecord::getGrade, Comparator.reverseOrder())).collect(Collectors.toList());
        for (int i = 0; i < list.size(); ++i) {
            WaterRankRecord record = (WaterRankRecord)list.get(i);
            Role role = this.roleService.getPlayer(record.getRId());
            if (role != null) {
                if (i < 5) {
                    this.titleService.addTitle(role, new TitleInfo(400001, 0));
                    String[] split = ((String)Const.WATER_MIRRIR_POS.get(i)).split(",");
                    String pos = JsonUtils.object2String(new Pos(Integer.valueOf(split[0]), Integer.valueOf(split[1]), Integer.valueOf(5), Integer.valueOf(map)));
                    Mirror mirror2 = new Mirror(Long.valueOf(this.worker.nextId()), role.getId(), "水陆战神", "水陆战神很忙，此处什么都没有留下……", pos, 3003);
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
            DataCache.MIRROR_MAP_NPC.put(Integer.valueOf(map), set);
        }
    }
    
    @Async("asyncServiceExecutor")
    public void clearCaTitle() {
        List<Long> roles = this.jdbcTemplate.queryForList("select id from role;", Long.class);
        for (Long id : roles) {
            Role player = this.roleService.getPlayer((long)id);
            if (player != null) {
                try {
                    this.titleService.delTitle(player, 900003);
                    this.titleService.delTitle(player, 900002);
                    this.titleService.delTitle(player, 900001);
                    this.titleService.delTitle(player, 900005);
                    this.titleService.delTitle(player, 900004);
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                continue;
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void awardCaSeason() {
        for (Long key : RankingList.CA_RANK_RECORDS.keySet()) {
            CaRankRecord record = (CaRankRecord)RankingList.CA_RANK_RECORDS.get(key);
            Role role = this.roleService.getPlayer((long)key);
            if (role == null) {
                continue;
            }
            if (record.getGrade() < 1080) {
                if (record.getFullRank() <= 100) {
                    this.titleService.addTitle(role, new TitleInfo(900003, 0));
                }
                else if (record.getScore() >= 5000) {
                    this.titleService.addTitle(role, new TitleInfo(900004, 0));
                }
                else {
                    continue;
                }
            }
            else if (record.getGrade() < 2120) {
                if (record.getFullRank() <= 100) {
                    this.titleService.addTitle(role, new TitleInfo(900002, 0));
                }
                else if (record.getScore() >= 5000) {
                    this.titleService.addTitle(role, new TitleInfo(900004, 0));
                }
                else {
                    continue;
                }
            }
            else if (record.getGrade() < 3120) {
                if (record.getFullRank() <= 100) {
                    this.titleService.addTitle(role, new TitleInfo(900001, 0));
                }
                else if (record.getScore() >= 5000) {
                    this.titleService.addTitle(role, new TitleInfo(900004, 0));
                }
                else {
                    continue;
                }
            }
            else if (record.getFullRank() <= 100) {
                this.titleService.addTitle(role, new TitleInfo(900005, 0));
            }
            else if (record.getScore() >= 5000) {
                this.titleService.addTitle(role, new TitleInfo(900004, 0));
            }
            else {
                continue;
            }
        }
    }
    
    @Async("asyncServiceExecutor")
    public void syncCaRan() {
        List<CaCache> oneList = new ArrayList<>();
        List<CaCache> twoList = new ArrayList<>();
        List<CaCache> triList = new ArrayList<>();
        List<CaCache> fourList = new ArrayList<>();
        Set<String> keys = JedisUtil.keys("jzca:*");
        if (keys.size() <= 0) {
            return;
        }
        for (String key : keys) {
            String value = JedisUtil.get(key);
            if (value != null) {
                try {
                    CaCache caCache = (CaCache)JsonUtils.string2Object(value, CaCache.class);
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
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else {
                continue;
            }
        }
        String marking = ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking();
        oneList = (List)oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        twoList = (List)oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        triList = (List)oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        fourList = (List)oneList.stream().sorted(Comparator.comparing(CaCache::getScore, Comparator.reverseOrder())).collect(Collectors.toList());
        this.syncCaRan(marking, oneList);
        this.syncCaRan(marking, twoList);
        this.syncCaRan(marking, triList);
        this.syncCaRan(marking, fourList);
        return;
    }
    
    public void syncCaRan(String marking, List<CaCache> list) {
        for (int i = 0; i < list.size(); ++i) {
            CaCache caCache = (CaCache)list.get(i);
            if (caCache == null || caCache.getMarking() == null) {
                this.logger.error("syncCaRan数据错误：{}", caCache);
            }
            else if (caCache.getMarking().equals(marking)) {
                CaRankRecord record = (CaRankRecord)RankingList.CA_RANK_RECORDS.get(caCache.getId());
                if (record != null) {
                    record.setFullRank(i + 1);
                    record.save();
                }
            }
        }
    }
}
