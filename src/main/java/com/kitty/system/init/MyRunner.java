
package com.kitty.system.init;

import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import java.util.Map;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import java.util.Collection;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.List;
import com.kitty.system.init.rank.ActingRank;
import com.kitty.system.game.email.pool.EmailDataPool;
import com.kitty.system.game.house.pool.HouseDataPool;
import com.kitty.system.game.activity.ActivityDataPool;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.npc.NpcDataPool;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.game.fightnew.pool.FightDataPool;
import com.kitty.system.game.market.pool.MarketDataPool;
import com.kitty.system.game.party.pool.PartyDataPool;
import com.kitty.system.game.task.pool.TaskDataPool;
import com.kitty.system.game.map.pool.MapDataPool;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.game.horse.pool.HorseDataPool;
import com.kitty.system.game.partner.pool.PartnerDataPool;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.pool.ExpDataPool;
import com.kitty.system.game.onlinemall.pool.MallDataPool;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.game.dirtywords.WordDisposePool;
import java.io.Serializable;
import com.kitty.cache.JedisUtil;
import org.springframework.boot.ApplicationArguments;
import com.kitty.rabbit.RabbitService;
import com.kitty.cache.RedisService;
import com.kitty.common.start.ServerService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.system.init.service.InitService;
import org.springframework.stereotype.Service;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.boot.ApplicationRunner;

@Component
@Order(1)
@Service
public class MyRunner implements ApplicationRunner
{
    @Autowired
    InitService initService;
    @Autowired
    ServerService serverService;
    @Autowired
    RedisService redisService;
    @Autowired
    RabbitService rabbitService;
    
    public void run(final ApplicationArguments var) {
        JedisUtil.load();
        this.redisService.delContain("*_countdown_*");
        this.rabbitService.purgeDelay();
        final String[] split = this.serverService.getMarking().split(",");
        if (split.length > 1) {
            for (final String key : split) {
                JedisUtil.delContain("jzca:" + key + "_*");
                JedisUtil.delContain("jzca:" + this.serverService.getMarking() + "_*");
            }
        }
        else {
            JedisUtil.delContain("jzca:" + this.serverService.getMarking() + "_*");
        }
        WordDisposePool.load();
        ConvertDataPool.load();
        PartDataPool.load();
        EquipDataPool.load();
        MallDataPool.load();
        ExpDataPool.load();
        PetDataPool.load();
        PartnerDataPool.load();
        HorseDataPool.load();
        WelfareDataPool.load();
        MapDataPool.load();
        TaskDataPool.load();
        PartyDataPool.load();
        MarketDataPool.load();
        FightDataPool.load();
        ItemDataPool.load();
        CardDataPool.load();
        NpcDataPool.load();
        SkillDataPool.load();
        ActivityDataPool.load();
        HouseDataPool.load();
        EmailDataPool.load();
        final List<Role> roles = this.initService.getRoles();
        final List<Pet> pets = this.initService.getPets();
        this.initService.getWearEquips();
        ActingRank.initRoleRune(roles);
        ActingRank.initPetRanking(roles, pets);
        ActingRank.initComprehensiveStrengthSource(roles);
        ActingRank.initWeaponsRankingSource(roles);
        this.initQlkRankingSource(roles);
        this.initPractitionersSource(roles);
        this.initWaterRank();
        this.initWaterMirror();
        ActingRank.clearRoleRune();
    }
    
    public void initQlkRankingSource(final List<Role> roles) {
        RankingList.qlkRankingSources.addAll(this.initService.initQlkRankingSource(roles));
    }
    
    public void initPractitionersSource(final List<Role> roles) {
        RankingList.practitionersSources.addAll(this.initService.initPractitionersSource(roles));
    }
    
    public void initWaterRank() {
        RankingList.WATER_RANK_RECORDS.putAll(this.initService.initWaterRank());
    }
    
    public void initWaterMirror() {
        this.initService.initWaterMirror();
    }
}
