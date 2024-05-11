
package com.kitty.system.init.service;

import com.kitty.system.game.pet.domain.Pet;
import java.util.Collection;
import java.util.Date;
import com.kitty.system.game.email.domain.EmailSystem;
import java.util.concurrent.CopyOnWriteArrayList;
import com.kitty.system.init.domain.SysPetAcsryColor;
import com.kitty.system.init.domain.vo.PetAccColor;
import com.kitty.system.init.domain.SysDataFurniture;
import com.kitty.system.init.domain.SysDataAcsrySkill;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import java.util.HashSet;
import com.kitty.mina.cache.DataCache;
import java.util.Set;
import com.kitty.common.model.Pos;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import java.util.Collections;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.init.domain.CommonSet;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.init.domain.SysLevelExp;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.domain.SysPartner;
import com.kitty.system.init.domain.SysDataCardFiveExercise;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysDataPrizeDraw;
import com.kitty.system.init.domain.SysDataNewDistrictSign;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.system.init.domain.SysDataChargeAward;
import com.kitty.system.init.domain.SysDataGradeAward;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.system.init.domain.SysDataMagicBox;
import com.kitty.system.init.domain.SysDataLotteryRare;
import com.kitty.system.init.domain.SysDataLotterySuper;
import com.kitty.system.init.domain.SysDataLottery;
import com.kitty.system.init.domain.SysDataLuckDraw;
import com.kitty.system.init.domain.SysDataTaskSurround;
import com.kitty.system.init.domain.SysDataThreeScopeDemon;
import com.kitty.system.init.domain.SysDataSealDemon;
import com.kitty.system.init.domain.SysDataStarOfficer;
import com.kitty.system.init.domain.SysDataPartyBandit;
import com.kitty.system.init.domain.SysDataWorldBoos;
import com.kitty.system.init.domain.SysDataTaskForeign;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import com.kitty.system.init.domain.SysDataTaskReward;
import com.kitty.system.init.domain.SysDataTaskEventForeign;
import com.kitty.system.init.domain.SysDataTaskEvent;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.system.init.domain.SysDataTaskFormula;
import com.kitty.system.init.domain.SysDataTaskNpcNameType;
import com.kitty.system.init.domain.SysDataTaskNpcName;
import com.kitty.system.init.domain.SysDataTaskNpc;
import com.kitty.system.init.domain.SysDataTaskKeju;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.system.init.domain.SysDataTaskDragon;
import com.kitty.system.init.domain.SysDataTaskCarnival;
import com.kitty.system.init.domain.SysDataTaskGodsCourt;
import com.kitty.system.init.domain.SysDataTaskCatchesGhosts;
import com.kitty.system.init.domain.SysDataTaskFive;
import com.kitty.system.init.domain.SysDataTaskXm;
import com.kitty.system.init.domain.SysDataTaskLookFor;
import com.kitty.system.init.domain.SysDataTaskTower;
import com.kitty.system.init.domain.SysDataTaskLoop;
import com.kitty.system.init.domain.SysDataTaskParty;
import com.kitty.system.init.domain.SysDataTaskTreasureMap;
import com.kitty.system.init.domain.SysDataTaskSchool;
import com.kitty.system.init.domain.SysDataTaskMain;
import com.kitty.system.init.domain.SysRefine;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysTaskItem;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysDataStall;
import com.kitty.system.init.domain.SysDataStallMenu;
import com.kitty.system.init.domain.SysDataMonsterPeripheral;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysWxOtherWarData;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.init.domain.SysDataAcsrySuitColor;
import com.kitty.system.init.domain.SysDataAcsryAttr;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.init.domain.SysDataAcsryIdentify;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.init.domain.SysPartyPractice;
import com.kitty.system.init.domain.SysUpgrade;
import java.util.function.Supplier;
import java.util.Comparator;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import java.util.Iterator;
import java.util.HashMap;
import java.util.stream.Collector;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopNpcGrocery;
import com.kitty.system.init.domain.SysShopNpcEquip;
import com.kitty.system.init.domain.SysShopNpcDrug;
import com.kitty.system.init.domain.SysShopNpcPet;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysShopNpcNice;
import com.kitty.system.init.domain.SysShopNpcNao;
import com.kitty.system.init.domain.SysShopNpcLong;
import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.init.domain.SysShopNpcGuo;
import com.kitty.system.init.domain.SysShopNpcFeats;
import com.kitty.system.init.domain.SysShopNpcDi;
import com.kitty.system.init.domain.SysShopNpcBrac;
import com.kitty.system.init.domain.SysShopNpcArena;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysPetExercise;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysPartyFlag;
import com.kitty.system.init.domain.SysNpcTypeInfo;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.init.domain.SysMapTeleporter;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysGuajiMap;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysRoleChangeColor;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.domain.HorseSkill;
import com.kitty.system.init.domain.SysHorse;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysCanMove;
import org.nutz.dao.sql.Sql;
import com.kitty.common.utils.TimeUtil;
import org.nutz.dao.Sqls;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.activity.model.Event;
import com.kitty.system.game.welfare.domain.GiftBag;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.kitty.system.init.domain.Tgroups;
import java.util.Map;
import com.kitty.common.mysql.TargetDataSource;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.game.dirtywords.domain.Word;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class InitService
{
    @Autowired
    Dao dao;
    
    @TargetDataSource(name = "ac")
    public List<Word> getWord() {
        return this.dao.query(Word.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "ac")
    public Map<String, Tgroups> initGroups() {
        final List<Tgroups> list = this.dao.query(Tgroups.class, Cnd.where("skid", "!=", (Object)"NULL"));
        return list.stream().collect(Collectors.toMap(Tgroups::getSkid, Tgroups -> Tgroups));
    }
    
    public Map<String, GiftBag> initGiftBag() {
        final List<GiftBag> list = this.dao.query(GiftBag.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(GiftBag::getName, GiftBag -> GiftBag));
    }
    
    public Map<Integer, Event> initEvent() {
        final List<Event> list = this.dao.query(Event.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(Event::getSerial, Event -> Event));
    }
    
    @TargetDataSource(name = "ac")
    public long initDate() {
        final String[] split = SpringUtils.getBean(ServerService.class).getMarking().split(",");
        Sql sql = Sqls.create("SELECT opday FROM t_groups WHERE skid = '" + split[0] + "'");
        sql = sql.setCallback(Sqls.callback.str());
        this.dao.execute(sql);
        if (sql.getString() != null) {
            return TimeUtil.timeInMillis(sql.getString());
        }
        return System.currentTimeMillis();
    }
    
    @TargetDataSource(name = "se")
    public List<SysCanMove> initCanMove() {
        final List<SysCanMove> list = this.dao.query(SysCanMove.class, Cnd.NEW());
        return list;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPet> initPet() {
        final List<SysPet> list = this.dao.query(SysPet.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPet::getSerial, SysPet -> SysPet));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysHorse> initHorse() {
        final List<SysHorse> list = this.dao.query(SysHorse.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysHorse::getSerial, SysHorse -> SysHorse));
    }
    
    @TargetDataSource(name = "se")
    public List<HorseSkill> initHorseSkill() {
        return this.dao.query(HorseSkill.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysReiEn> initReiEn() {
        final List<SysReiEn> list = this.dao.query(SysReiEn.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysReiEn::getSerial, SysReiEn -> SysReiEn));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysCountryQuestion> initCountryQuestionLib() {
        final List<SysCountryQuestion> list = this.dao.query(SysCountryQuestion.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysCountryQuestion::getSerial, SysCountryQuestion -> SysCountryQuestion));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysProvinceQuestion> initProvinceQuestion() {
        final List<SysProvinceQuestion> list = this.dao.query(SysProvinceQuestion.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysProvinceQuestion::getSerial, SysProvinceQuestion -> SysProvinceQuestion));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysHallQuestion> initHallQuestion() {
        final List<SysHallQuestion> list = this.dao.query(SysHallQuestion.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysHallQuestion::getSerial, SysHallQuestion -> SysHallQuestion));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysRoleChangeColor> initRoleChangeColor() {
        final List<SysRoleChangeColor> list = this.dao.query(SysRoleChangeColor.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysRoleChangeColor::getSerial, SysRoleChangeColor -> SysRoleChangeColor));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysGiftItem> initGiftItem() {
        final List<SysGiftItem> list = this.dao.query(SysGiftItem.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysGiftItem::getSerial, SysGiftItem -> SysGiftItem));
    }
    
    @TargetDataSource(name = "se")
    public List<SysGiftItemOpen> initGiftItemOpen() {
        final List<SysGiftItemOpen> list = this.dao.query(SysGiftItemOpen.class, Cnd.NEW());
        return list;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysGuajiMap> initGuajiMap() {
        final List<SysGuajiMap> list = this.dao.query(SysGuajiMap.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysGuajiMap::getMapSerial, SysGuajiMap -> SysGuajiMap));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysGuajiWar> initGuajiWar() {
        final List<SysGuajiWar> list = this.dao.query(SysGuajiWar.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysGuajiWar::getSerial, SysGuajiWar -> SysGuajiWar));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysHero> initHero() {
        final List<SysHero> list = this.dao.query(SysHero.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysHero::getSerial, SysHero -> SysHero));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysCommonAward> initCommonAward() {
        final List<SysCommonAward> list = this.dao.query(SysCommonAward.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysCommonAward::getDict, SysCommonAward -> SysCommonAward));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysMapInfo> initMapinfo() {
        final List<SysMapInfo> list = this.dao.query(SysMapInfo.class, Cnd.NEW());
        SpringUtils.getBean(MapService.class).initMapInfo(list);
        return list.stream().collect(Collectors.toMap(SysMapInfo::getMapSerial, SysMapinfo -> SysMapinfo));
    }
    
    @TargetDataSource(name = "se")
    public List<SysMapTeleporter> initMapteleporter() {
        return this.dao.query(SysMapTeleporter.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysNpcInfo> initNpcInfo() {
        return this.dao.query(SysNpcInfo.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysNpcTypeInfo> initNpcTypeInfo() {
        final List<SysNpcTypeInfo> list = this.dao.query(SysNpcTypeInfo.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysNpcTypeInfo::getId, SysNpcTypeInfo -> SysNpcTypeInfo));
    }
    
    @TargetDataSource(name = "se")
    public List<SysPartyFlag> initPartyFlag() {
        return this.dao.query(SysPartyFlag.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysOtherItem> initOtherItem() {
        final List<SysOtherItem> list = this.dao.query(SysOtherItem.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysOtherItem::getSerial, SysOtherItem -> SysOtherItem));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataAcsryEgg> initAcsryEgg() {
        final List<SysDataAcsryEgg> list = this.dao.query(SysDataAcsryEgg.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataAcsryEgg::getSerial, SysDataAcsryEgg -> SysDataAcsryEgg));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysEffect> initEffect() {
        final List<SysEffect> list = this.dao.query(SysEffect.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysEffect::getSerial, SysEffect -> SysEffect));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetExercise> initPetExercise() {
        final List<SysPetExercise> list = this.dao.query(SysPetExercise.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPetExercise::getSoul, SysPetExercise -> SysPetExercise));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysRbAttr> initRbattr() {
        final List<SysRbAttr> list = this.dao.query(SysRbAttr.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysRbAttr::getId, SysRbattr -> SysRbattr));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcArena> initShopNpcArena() {
        final List<SysShopNpcArena> list = this.dao.query(SysShopNpcArena.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcArena::getSerial, SysShopNpcArena -> SysShopNpcArena));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcBrac> initShopNpcBrac() {
        final List<SysShopNpcBrac> list = this.dao.query(SysShopNpcBrac.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcBrac::getSerial, SysShopNpcBrac -> SysShopNpcBrac));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcDi> initShopNpcDi() {
        final List<SysShopNpcDi> list = this.dao.query(SysShopNpcDi.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcDi::getSerial, SysShopNpcDi -> SysShopNpcDi));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcFeats> initShopNpcFeats() {
        final List<SysShopNpcFeats> list = this.dao.query(SysShopNpcFeats.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcFeats::getSerial, SysShopNpcFeats -> SysShopNpcFeats));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcGuo> initShopNpcGuo() {
        final List<SysShopNpcGuo> list = this.dao.query(SysShopNpcGuo.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcGuo::getSerial, SysShopNpcGuo -> SysShopNpcGuo));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcGuoRecycle> initShopNpcGuoRecycle() {
        final List<SysShopNpcGuoRecycle> list = this.dao.query(SysShopNpcGuoRecycle.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcGuoRecycle::getSerial, SysShopNpcGuoRecycle -> SysShopNpcGuoRecycle));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcLong> initShopNpcLong() {
        final List<SysShopNpcLong> list = this.dao.query(SysShopNpcLong.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcLong::getSerial, SysShopNpcLong -> SysShopNpcLong));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcNao> initShopNpcNao() {
        final List<SysShopNpcNao> list = this.dao.query(SysShopNpcNao.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcNao::getSerial, SysShopNpcNao -> SysShopNpcNao));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcNice> initShopNpcNice() {
        final List<SysShopNpcNice> list = this.dao.query(SysShopNpcNice.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcNice::getSerial, SysShopNpcNice -> SysShopNpcNice));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDrug> initDrug() {
        final List<SysDrug> list = this.dao.query(SysDrug.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDrug::getSerial, SysDrug -> SysDrug));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcPet> initShopNpcPet() {
        final List<SysShopNpcPet> list = this.dao.query(SysShopNpcPet.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcPet::getSerial, SysShopNpcPet -> SysShopNpcPet));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcDrug> initShopNpcDrug() {
        final List<SysShopNpcDrug> list = this.dao.query(SysShopNpcDrug.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcDrug::getSerial, SysShopNpcDrug -> SysShopNpcDrug));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcEquip> initShopNpcEquip() {
        final List<SysShopNpcEquip> list = this.dao.query(SysShopNpcEquip.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcEquip::getSerial, SysShopNpcEquip -> SysShopNpcEquip));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcGrocery> initShopNpcGrocery() {
        final List<SysShopNpcGrocery> list = this.dao.query(SysShopNpcGrocery.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopNpcGrocery::getSerial, SysShopNpcGrocery -> SysShopNpcGrocery));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopOther> initShopOther() {
        final List<SysShopOther> list = this.dao.query(SysShopOther.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopOther::getSerial, SysShopOther -> SysShopOther));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopStrengthenMaterial> initShopStrengthenMaterial() {
        final List<SysShopStrengthenMaterial> list = this.dao.query(SysShopStrengthenMaterial.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysShopStrengthenMaterial::getSerial, SysShopStrengthenMaterial -> SysShopStrengthenMaterial));
    }
    
    @TargetDataSource(name = "se")
    public Object[] initSkill() {
        final List<SysSkill> list = this.dao.query(SysSkill.class, Cnd.NEW());
        final Map<Integer, SysSkill> skillMap = list.stream().collect(Collectors.toMap(SysSkill::getSerial, SysSkill -> SysSkill));
        final Map<Integer, List<SysSkill>> raceGroupSkill = list.stream().collect(Collectors.groupingBy(SysSkill::getRace));
        final Map<Integer, Map<Integer, Map<Integer, List<SysSkill>>>> groupSkillMap = new HashMap<Integer, Map<Integer, Map<Integer, List<SysSkill>>>>();
        Map<Integer, Map<Integer, List<SysSkill>>> sexGroupMap = null;
        for (final Map.Entry<Integer, List<SysSkill>> entry : raceGroupSkill.entrySet()) {
            sexGroupMap = new HashMap<Integer, Map<Integer, List<SysSkill>>>();
            final Map<Integer, List<SysSkill>> sexSkillMap = (Map<Integer, List<SysSkill>>)entry.getValue().stream().collect(Collectors.groupingBy(SysSkill::getSex));
            groupSkillMap.put(entry.getKey(), sexGroupMap);
            for (final Map.Entry<Integer, List<SysSkill>> entry2 : sexSkillMap.entrySet()) {
                final Map<Integer, List<SysSkill>> attrGroupMap = (Map<Integer, List<SysSkill>>)entry2.getValue().stream().collect(Collectors.groupingBy(SysSkill::getAttr));
                sexGroupMap.put(entry2.getKey(), attrGroupMap);
            }
        }
        return new Object[] { skillMap, groupSkillMap };
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysSkillUpGradeConsume> initSkillUpGradeConsume() {
        final List<SysSkillUpGradeConsume> list = this.dao.query(SysSkillUpGradeConsume.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysSkillUpGradeConsume::getGrade, SysSkillExplain -> SysSkillExplain));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetSkill> initPetSkill() {
        final List<SysPetSkill> list = this.dao.query(SysPetSkill.class, Cnd.NEW());
        this.initPassivePetSkill(list);
        return list.stream().collect(Collectors.toMap(SysPetSkill::getSerial, SysPetSkill -> SysPetSkill));
    }
    
    private void initPassivePetSkill(final List<SysPetSkill> list) {
        final List<SysPetSkill> passiveSkills = new ArrayList<SysPetSkill>();
        final Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps = new LinkedHashMap<String, Map<Integer, SysPetSkill>>();
        final Map<Integer, SysPetSkill> initiativeTriggerPassiveMap = new LinkedHashMap<Integer, SysPetSkill>();
        Map<Integer, SysPetSkill> initiativeSkillMap = new LinkedHashMap<Integer, SysPetSkill>();
        for (final SysPetSkill petSkill : list) {
            if (0 == petSkill.getInitiative()) {
                passiveSkills.add(petSkill);
            }
            else if (1 == petSkill.getInitiative() && petSkill.getPassiveTriggerSkill() != null && !"".equals(petSkill.getPassiveTriggerSkill())) {
                initiativeTriggerPassiveMap.put(petSkill.getSerial(), petSkill);
                initiativeSkillMap.put(petSkill.getSerial(), petSkill);
            }
            else if (2 == petSkill.getInitiative()) {
                passiveSkills.add(petSkill);
                initiativeSkillMap.put(petSkill.getSerial(), petSkill);
            }
            else {
                initiativeSkillMap.put(petSkill.getSerial(), petSkill);
            }
        }
        for (final PET_PASSIVE_SKILL_TRIGGER_DICT dict : PET_PASSIVE_SKILL_TRIGGER_DICT.values()) {
            Map<Integer, SysPetSkill> petSkillMap = new HashMap<Integer, SysPetSkill>();
            for (int i = passiveSkills.size() - 1; i >= 0; --i) {
                if (dict.getValue().equals(passiveSkills.get(i).getTriggerType())) {
                    petSkillMap.put(passiveSkills.get(i).getSerial(), passiveSkills.get(i));
                    passiveSkills.remove(i);
                }
            }
            petSkillMap = this.petSkillMapSort(petSkillMap);
            data_pet_skill_maps.put(dict.getValue(), petSkillMap);
        }
        for (int j = passiveSkills.size() - 1; j >= 0; --j) {
            this.otherTypeSkill(data_pet_skill_maps, passiveSkills.get(j));
        }
        data_pet_skill_maps.put(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue(), initiativeTriggerPassiveMap);
        initiativeSkillMap = this.petSkillMapSort(initiativeSkillMap);
        data_pet_skill_maps.put(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), initiativeSkillMap);
        PetTriggerPassiveSkill.data_pet_skill_maps = data_pet_skill_maps;
    }
    
    private void otherTypeSkill(final Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps, final SysPetSkill sysPetSkill) {
        final List<String> lists = PET_PASSIVE_SKILL_TRIGGER_DICT.getSkillTypeOther().get(sysPetSkill.getSerial());
        if (lists != null && lists.size() > 0) {
            for (final String type : lists) {
                Map<Integer, SysPetSkill> integerSysPetSkillMap = data_pet_skill_maps.get(type);
                if (integerSysPetSkillMap == null) {
                    integerSysPetSkillMap = new HashMap<Integer, SysPetSkill>();
                    data_pet_skill_maps.put(type, integerSysPetSkillMap);
                }
                integerSysPetSkillMap.put(sysPetSkill.getSerial(), sysPetSkill);
                this.petSkillMapSort(integerSysPetSkillMap);
            }
        }
    }
    
    private Map<Integer, SysPetSkill> petSkillMapSort(final Map<Integer, SysPetSkill> sourceMap) {
        Map<Integer, SysPetSkill> result = new LinkedHashMap<Integer, SysPetSkill>();
        result = sourceMap.entrySet().stream().sorted(Comparator.comparing(e -> e.getValue().getExecutionSequence())).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
        return result;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysUpgrade> initUpgrade() {
        final List<SysUpgrade> list = this.dao.query(SysUpgrade.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysUpgrade::getId, SysUpgrade -> SysUpgrade));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPartyPractice> initPartyPractice() {
        final List<SysPartyPractice> list = this.dao.query(SysPartyPractice.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPartyPractice::getLevel, SysPartyPractice -> SysPartyPractice));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysAboveAttrLib> initAboveAttrLib() {
        final List<SysAboveAttrLib> list = this.dao.query(SysAboveAttrLib.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysAboveAttrLib::getDict, SysAboveAttrLib -> SysAboveAttrLib));
    }
    
    @TargetDataSource(name = "se")
    public List<SysPurifyAttr> initPurify() {
        return this.dao.query(SysPurifyAttr.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataAcsry> initAcsry() {
        final List<SysDataAcsry> list = this.dao.query(SysDataAcsry.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataAcsry::getSerial, SysDataAcsry -> SysDataAcsry));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataAcsryIdentify> initAcsryAIdentify() {
        final List<SysDataAcsryIdentify> list = this.dao.query(SysDataAcsryIdentify.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataAcsryIdentify::getSuiteType, SysDataAcsryIdentify -> SysDataAcsryIdentify));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataAcsrySuit> initAcsrySuit() {
        final List<SysDataAcsrySuit> list = this.dao.query(SysDataAcsrySuit.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataAcsrySuit::getDist, SysDataAcsrySuit -> SysDataAcsrySuit));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataAcsryAttr> initAcsryAttr() {
        final List<SysDataAcsryAttr> list = this.dao.query(SysDataAcsryAttr.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataAcsryAttr::getDict, SysDataAcsryAttr -> SysDataAcsryAttr));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, Map<Integer, SysDataAcsrySuitColor>> initAcsrySuitColor() {
        final List<SysDataAcsrySuitColor> sysDataAcsrySuitColors = this.dao.query(SysDataAcsrySuitColor.class, Cnd.NEW());
        final Map<String, Map<Integer, SysDataAcsrySuitColor>> map = new HashMap<String, Map<Integer, SysDataAcsrySuitColor>>();
        for (final SysDataAcsrySuitColor acsrySuitColor : sysDataAcsrySuitColors) {
            Map<Integer, SysDataAcsrySuitColor> mapC = map.get(acsrySuitColor.getSuitDist());
            if (mapC == null) {
                mapC = new HashMap<Integer, SysDataAcsrySuitColor>();
                map.put(acsrySuitColor.getSuitDist(), mapC);
            }
            mapC.put(acsrySuitColor.getSkillSerial(), acsrySuitColor);
        }
        return map;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetAcsry> initPetAcsry() {
        final List<SysPetAcsry> list = this.dao.query(SysPetAcsry.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPetAcsry::getSerial, SysPetAcsry -> SysPetAcsry));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetAcsryAdvAttrLib> initPetAcsryAdvAttrLib() {
        final List<SysPetAcsryAdvAttrLib> list = this.dao.query(SysPetAcsryAdvAttrLib.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPetAcsryAdvAttrLib::getAttrSerial, SysPetAcsryAdvAttrLib -> SysPetAcsryAdvAttrLib));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysPetAcsryAttrLib> initPetAcsryAttrLib() {
        final List<SysPetAcsryAttrLib> list = this.dao.query(SysPetAcsryAttrLib.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPetAcsryAttrLib::getDict, SysPetAcsryAttrLib -> SysPetAcsryAttrLib));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysWarRole> initWarRole() {
        final List<SysWarRole> list = this.dao.query(SysWarRole.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysWarRole::getSerial, SysWarRole -> SysWarRole));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysWxOtherWarData> initWxOtherWarData() {
        final List<SysWxOtherWarData> list = this.dao.query(SysWxOtherWarData.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysWxOtherWarData::getId, SysWxOtherWarData -> SysWxOtherWarData));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataLimitShop> initLimitShop() {
        final List<SysDataLimitShop> list = this.dao.query(SysDataLimitShop.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataLimitShop::getSerial, SysDataLimitShop -> SysDataLimitShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataRecommendShop> initRecommendShop() {
        final List<SysDataRecommendShop> list = this.dao.query(SysDataRecommendShop.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataRecommendShop::getSerial, SysDataRecommendShop -> SysDataRecommendShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysBoundShop> initBoundShop() {
        final List<SysBoundShop> list = this.dao.query(SysBoundShop.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysBoundShop::getSerial, SysBoundShop -> SysBoundShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataMonster> initDataMonster() {
        final List<SysDataMonster> list = this.dao.query(SysDataMonster.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataMonster::getSerial, SysDataMonster -> SysDataMonster));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataMonsterPeripheral> initMonsterPeripheral() {
        final List<SysDataMonsterPeripheral> list = this.dao.query(SysDataMonsterPeripheral.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataMonsterPeripheral::getSerial, SysDataMonsterPeripheral -> SysDataMonsterPeripheral));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataStallMenu> initDataStallMenu() {
        final List<SysDataStallMenu> list = this.dao.query(SysDataStallMenu.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataStallMenu::getSerial, SysDataStallMenu -> SysDataStallMenu));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataStall> initDataStall() {
        final List<SysDataStall> list = this.dao.query(SysDataStall.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataStall::getSerial, SysDataStall -> SysDataStall));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataStuff> initDataStuff() {
        final List<SysDataStuff> list = this.dao.query(SysDataStuff.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataStuff::getSerial, SysDataStuff -> SysDataStuff));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysTaskItem> initTaskItem() {
        final List<SysTaskItem> list = this.dao.query(SysTaskItem.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysTaskItem::getItemId, SysTaskItem -> SysTaskItem));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysNewBieEq> initNewBieEq() {
        final List<SysNewBieEq> list = this.dao.query(SysNewBieEq.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysNewBieEq::getSerial, SysNewBieEq -> SysNewBieEq));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysSeniorBieEq> initSeniorBieEq() {
        final List<SysSeniorBieEq> list = this.dao.query(SysSeniorBieEq.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysSeniorBieEq::getSerial, SysSeniorBieEq -> SysSeniorBieEq));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysMagicalBieEq> initMagicalBieEq() {
        final List<SysMagicalBieEq> list = this.dao.query(SysMagicalBieEq.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysMagicalBieEq::getSerial, SysMagicalBieEq -> SysMagicalBieEq));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysSinBieEq> initSinBieEq() {
        final List<SysSinBieEq> list = this.dao.query(SysSinBieEq.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysSinBieEq::getSerial, SysSinBieEq -> SysSinBieEq));
    }
    
    @TargetDataSource(name = "se")
    public List<SysRefine> initRefine() {
        return this.dao.query(SysRefine.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskMain> initTaskMain() {
        final List<SysDataTaskMain> list = this.dao.query(SysDataTaskMain.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskMain::getSerial, SysDataTaskMain -> SysDataTaskMain));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskSchool> initTaskSchool() {
        final List<SysDataTaskSchool> list = this.dao.query(SysDataTaskSchool.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskSchool::getSerial, SysDataTaskSchool -> SysDataTaskSchool));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskTreasureMap> initTaskTreasureMap() {
        return this.dao.query(SysDataTaskTreasureMap.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskParty> initTaskParty() {
        return this.dao.query(SysDataTaskParty.class, Cnd.where("is_load", "=", (Object)1));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskLoop> initTaskLoop() {
        return this.dao.query(SysDataTaskLoop.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskTower> initTaskTower() {
        return this.dao.query(SysDataTaskTower.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskLookFor> initTaskLookFor() {
        return this.dao.query(SysDataTaskLookFor.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskXm> initTaskXm() {
        return this.dao.query(SysDataTaskXm.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskFive> initTaskFive() {
        return this.dao.query(SysDataTaskFive.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskCatchesGhosts> initTaskCatchesGhosts() {
        return this.dao.query(SysDataTaskCatchesGhosts.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskGodsCourt> initTaskGodsCourt() {
        return this.dao.query(SysDataTaskGodsCourt.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskCarnival> initTaskCarnival() {
        return this.dao.query(SysDataTaskCarnival.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskDragon> initTaskDragon() {
        return this.dao.query(SysDataTaskDragon.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskMo> initTaskMo() {
        return this.dao.query(SysDataTaskMo.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskMoAward> initTaskMoAward() {
        return this.dao.query(SysDataTaskMoAward.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskKeju> initTaskKj() {
        return this.dao.query(SysDataTaskKeju.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskNpc> initTaskNpc() {
        final List<SysDataTaskNpc> list = this.dao.query(SysDataTaskNpc.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskNpc::getSerial, SysDataTaskNpc -> SysDataTaskNpc));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskNpcName> initNpcName() {
        final List<SysDataTaskNpcName> list = this.dao.query(SysDataTaskNpcName.class, Cnd.NEW());
        return list;
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataTaskNpcNameType> initNpcNameType() {
        final List<SysDataTaskNpcNameType> list = this.dao.query(SysDataTaskNpcNameType.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskNpcNameType::getKey, SysDataTaskNpcNameType -> SysDataTaskNpcNameType));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskFormula> initTaskFormula() {
        final List<SysDataTaskFormula> list = this.dao.query(SysDataTaskFormula.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskFormula::getSerial, SysDataTaskFormula -> SysDataTaskFormula));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskFight> initTaskFight() {
        final List<SysDataTaskFight> list = this.dao.query(SysDataTaskFight.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskFight::getSerial, SysDataTaskFight -> SysDataTaskFight));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskCondition> initTaskCondition() {
        final List<SysDataTaskCondition> list = this.dao.query(SysDataTaskCondition.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskCondition::getSubmitCondition, SysDataTaskCondition -> SysDataTaskCondition));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskEvent> initTaskEvent() {
        final List<SysDataTaskEvent> list = this.dao.query(SysDataTaskEvent.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskEvent::getSerial, SysDataTaskEvent -> SysDataTaskEvent));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskEventForeign> initTaskEventForeign() {
        return this.dao.query(SysDataTaskEventForeign.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataTaskReward> initTaskReward() {
        final List<SysDataTaskReward> list = this.dao.query(SysDataTaskReward.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTaskReward::getSerial, SysDataTaskReward -> SysDataTaskReward));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskRewardDynamic> initTaskRewardDynamic() {
        return this.dao.query(SysDataTaskRewardDynamic.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskForeign> initTaskRewardForeign() {
        return this.dao.query(SysDataTaskForeign.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataWorldBoos> initWorldBoos() {
        return this.dao.query(SysDataWorldBoos.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataPartyBandit> initPartyBanditList() {
        return this.dao.query(SysDataPartyBandit.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataStarOfficer> initStarOfficerList() {
        return this.dao.query(SysDataStarOfficer.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataSealDemon> initSealDemonList() {
        return this.dao.query(SysDataSealDemon.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataThreeScopeDemon> initThreeScopeDemonList() {
        return this.dao.query(SysDataThreeScopeDemon.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskSurround> initTaskSurround() {
        return this.dao.query(SysDataTaskSurround.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataLuckDraw> initLuckDraw() {
        return this.dao.query(SysDataLuckDraw.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataLottery> initLottery() {
        return this.dao.query(SysDataLottery.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataLotterySuper> initLotterySuper() {
        return this.dao.query(SysDataLotterySuper.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataLotteryRare> initLotteryRare() {
        return this.dao.query(SysDataLotteryRare.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataMagicBox> initMagicBox() {
        return this.dao.query(SysDataMagicBox.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTitle> initTitle() {
        final List<SysDataTitle> list = this.dao.query(SysDataTitle.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataTitle::getSerial, SysDataTitle -> SysDataTitle));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataGradeAward> initGradeAward() {
        final List<SysDataGradeAward> list = this.dao.query(SysDataGradeAward.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataGradeAward::getGrade, SysDataGradeAward -> SysDataGradeAward));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataChargeAward> initChargeAward() {
        return this.dao.query(SysDataChargeAward.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysCharge> initCharge() {
        final List<SysCharge> list = this.dao.query(SysCharge.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysCharge::getSerial, SysCharge -> SysCharge));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataExchange> initExchange() {
        final List<SysDataExchange> list = this.dao.query(SysDataExchange.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataExchange::getSerial, SysDataExchange -> SysDataExchange));
    }
    
    @TargetDataSource(name = "se")
    public Map<Byte, SysDataNewDistrictSign> initNewDistrict() {
        final List<SysDataNewDistrictSign> list = this.dao.query(SysDataNewDistrictSign.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataNewDistrictSign::getDay, SysDataNewDistrictSign -> SysDataNewDistrictSign));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataPrizeDraw> initPrizeDraw() {
        return this.dao.query(SysDataPrizeDraw.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataCard> initCard() {
        final List<SysDataCard> list = this.dao.query(SysDataCard.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataCard::getSerial, SysDataCard -> SysDataCard));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataCardFiveExercise> initCardFiveExercise() {
        final List<SysDataCardFiveExercise> list = this.dao.query(SysDataCardFiveExercise.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataCardFiveExercise::getGrade, SysDataCardFiveExercise -> SysDataCardFiveExercise));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPartner> initPartner() {
        final List<SysPartner> list = this.dao.query(SysPartner.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPartner::getSerial, SysPartner -> SysPartner));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, Map<Short, SysPartnerProperty>> initPartnerProperty() {
        final Map<Integer, Map<Short, SysPartnerProperty>> returnMap = new HashMap<Integer, Map<Short, SysPartnerProperty>>();
        final List<SysPartnerProperty> list = this.dao.query(SysPartnerProperty.class, Cnd.NEW());
        if (list.size() > 0) {
            final List<SysPartner> partners = this.dao.query(SysPartner.class, Cnd.NEW());
            final List<Integer> partnerSerials = partners.stream().map(SysPartner::getSerial).collect(Collectors.toList());
            for (final Integer partnerSerial : partnerSerials) {
                final Map<Short, SysPartnerProperty> map = list.stream().filter(property -> property.getPartnerSerial() == partnerSerial).collect(Collectors.toMap(SysPartnerProperty::getLevel, SysPartnerProperty -> SysPartnerProperty));
                returnMap.put(partnerSerial, map);
            }
        }
        return returnMap;
    }
    
    @TargetDataSource(name = "se")
    public Map<String, Map<Integer, SysLevelExp>> initLevelExp() {
        final Map<String, Map<Integer, SysLevelExp>> returnMap = new HashMap<String, Map<Integer, SysLevelExp>>();
        final List<SysLevelExp> list = this.dao.query(SysLevelExp.class, Cnd.NEW());
        final Map<Integer, SysLevelExp> heroMap = list.stream().filter(obj -> obj.getType() == LOGIC.LOGICTYPE_HERO.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("heroMap", heroMap);
        final Map<Integer, SysLevelExp> petMap = list.stream().filter(obj -> obj.getType() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("petMap", petMap);
        final Map<Integer, SysLevelExp> partnerMap = list.stream().filter(obj -> obj.getType() == LOGIC.LOGICTYPE_PARTNER.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("partnerMap", partnerMap);
        final Map<Integer, SysLevelExp> horseMap = list.stream().filter(obj -> obj.getType() == LOGIC.HORSE.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("horseMap", horseMap);
        return returnMap;
    }
    
    @TargetDataSource(name = "se")
    public Map<String, String> initCommonSet() {
        final List<CommonSet> list = this.dao.query(CommonSet.class, Cnd.NEW());
        final Map<String, String> map = list.stream().collect(Collectors.toMap(CommonSet::getKey, CommonSet -> CommonSet.getValue()));
        return map;
    }
    
    @TargetDataSource(name = "ac")
    public Map<String, String> initVersion() {
        Sql sql = Sqls.create("SELECT content FROM t_configuration where dict = 'hot_update'");
        sql = sql.setCallback(Sqls.callback.str());
        this.dao.execute(sql);
        if (sql.getString() != null) {
            final Map<String, String> map = JsonUtils.string2Map(sql.getString(), String.class, String.class);
            return map;
        }
        return null;
    }
    
    public List<PractitionersSource> initPractitionersSource(final List<Role> roles) {
        ArrayList<PractitionersSource> practitionersSources = new ArrayList<PractitionersSource>();
        if (roles == null) {
            return practitionersSources;
        }
        final long[] needExps = { 0L };
        practitionersSources = roles.stream().map(role -> {
            needExps[0] = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), role.getRelive(), role.getLevel() + 1);
            final PractitionersSource practitionersSource = new PractitionersSource(role, needExps[0]);
            return practitionersSource;
        }).sorted(Comparator.comparing(PractitionersSource::getReliveAndLevel, Comparator.reverseOrder()).thenComparing(PractitionersSource::getExp, Comparator.reverseOrder())).collect(Collectors.toCollection(ArrayList::new));
        return Collections.synchronizedList(practitionersSources);
    }
    
    public List<QlkRankingSource> initQlkRankingSource(final List<Role> roles) {
        final Map<Long, List<WearEquip>> wearEquipMaps = RankingList.wearEquipMaps;
        ArrayList<QlkRankingSource> qlkRankingSource = new ArrayList<QlkRankingSource>();
        if (roles == null || roles.size() <= 0 || wearEquipMaps == null || wearEquipMaps.size() <= 0) {
            return qlkRankingSource;
        }
        final int[][] values = new int[1][1];
        qlkRankingSource = roles.stream().map(role -> {
            values[0] = RankingUtils.getRoleQlkWuxing(role, wearEquipMaps.get(role.getId()), null);
            final QlkRankingSource qlkRankingSource2 = new QlkRankingSource(role, values[0]);
            return qlkRankingSource2;
        }).collect(Collectors.toCollection(ArrayList::new));
        return Collections.synchronizedList((List<QlkRankingSource>)qlkRankingSource.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkj, Comparator.reverseOrder())).collect(Collectors.toCollection(ArrayList::new)));
    }
    
    public Map<Long, WaterRankRecord> initWaterRank() {
        final List<WaterRankRecord> list = this.dao.query(WaterRankRecord.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(WaterRankRecord::getRId, WaterRankRecord -> WaterRankRecord));
    }
    
    public void initWaterMirror() {
        final List<Mirror> list = this.dao.query(Mirror.class, Cnd.NEW());
        for (final Mirror mirror : list) {
            final Pos pos = JsonUtils.string2Object(mirror.getPosInfo(), Pos.class);
            final Set<Long> set = DataCache.MIRROR_MAP_NPC.computeIfAbsent(pos.getMapId(), k -> new HashSet());
            set.add(mirror.getRId());
        }
        final Map<Long, Mirror> map = list.stream().collect(Collectors.toMap(Mirror::getRId, Mirror -> Mirror));
        DataCache.MIRROR_MAP.putAll(map);
    }
    
    public Map<Long, CaRankRecord> initCaRank() {
        final List<CaRankRecord> list = this.dao.query(CaRankRecord.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(CaRankRecord::getRId, CaRankRecord -> CaRankRecord));
    }
    
    @TargetDataSource(name = "se")
    public Object[] initTianFuSkill() {
        final List<SysTianFuSkill> tianFuSkills = this.dao.query(SysTianFuSkill.class, Cnd.NEW());
        final Map<Integer, SysTianFuSkill> map = tianFuSkills.stream().collect(Collectors.toMap(SysTianFuSkill::getCeSerial, SysTianFuSkill -> SysTianFuSkill));
        final Map<Integer, SysTianFuSkill> opportunitys = tianFuSkills.stream().filter(tianFuSkill -> tianFuSkill.getOpportunity() != null && !"".equals(tianFuSkill.getOpportunity())).collect(Collectors.toMap(SysTianFuSkill::getCeSerial, SysTianFuSkill -> SysTianFuSkill));
        final Map<Integer, SysTianFuSkill> noOpportunitys = tianFuSkills.stream().filter(tianFuSkill -> tianFuSkill.getOpportunity() == null || "".equals(tianFuSkill.getOpportunity())).collect(Collectors.toMap(SysTianFuSkill::getCeSerial, SysTianFuSkill -> SysTianFuSkill));
        final Object[] objs = { map, opportunitys, noOpportunitys };
        return objs;
    }
    
    @TargetDataSource(name = "se")
    public Map<Short, SysDayInCePractice> initDayInCePractice() {
        final List<SysDayInCePractice> sysDayInCePractices = this.dao.query(SysDayInCePractice.class, Cnd.NEW());
        final Map<Short, SysDayInCePractice> map = sysDayInCePractices.stream().collect(Collectors.toMap(SysDayInCePractice::getLevel, SysDayInCePractice -> SysDayInCePractice));
        return map;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataAcsrySkill> initAcsrySkill() {
        final List<SysDataAcsrySkill> list = this.dao.query(SysDataAcsrySkill.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataAcsrySkill::getSerial, SysDataAcsrySkill -> SysDataAcsrySkill));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataFurniture> initFurniture() {
        final List<SysDataFurniture> list = this.dao.query(SysDataFurniture.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysDataFurniture::getSerial, SysDataFurniture -> SysDataFurniture));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, Map<Byte, PetAccColor>> initPetAccColor() {
        final List<SysPetAcsryColor> list = this.dao.query(SysPetAcsryColor.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPetAcsryColor::getDict, SysPetAcsryColor -> SysPetAcsryColor.getValue()));
    }
    
    public CopyOnWriteArrayList initEmailSystem() {
        final List<EmailSystem> emailSystems = this.dao.query(EmailSystem.class, Cnd.where("recipient", "=", 0).and("restrictions", "=", null).and("expiration_time", ">", new Date().getTime()).and("is_delete", "=", 0).desc("expiration_time"));
        return new CopyOnWriteArrayList(emailSystems);
    }
    
    public List<Role> getRoles() {
        List<Role> roles = null;
        if (RankingList.roles.size() <= 0) {
            roles = this.dao.query(Role.class, Cnd.NEW());
        }
        else {
            roles = RankingList.roles;
        }
        return roles;
    }
    
    public List<Pet> getPets() {
        List<Pet> pets = null;
        if (RankingList.pets.size() <= 0) {
            pets = this.dao.query(Pet.class, Cnd.NEW());
            RankingList.pets.addAll(pets);
            RankingList.petMaps.putAll(pets.stream().collect(Collectors.groupingBy(Pet::getRoleId)));
        }
        else {
            pets = RankingList.pets;
        }
        return pets;
    }
    
    public List<WearEquip> getWearEquips() {
        List<WearEquip> wearEquips = null;
        if (RankingList.wearEquips.size() <= 0) {
            wearEquips = this.dao.query(WearEquip.class, Cnd.NEW());
            RankingList.wearEquips.addAll(wearEquips);
            RankingList.wearEquipMaps.putAll(wearEquips.stream().collect(Collectors.groupingBy(WearEquip::getRId)));
        }
        else {
            wearEquips = RankingList.wearEquips;
        }
        return wearEquips;
    }
}
