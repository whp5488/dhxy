package com.kitty.system.init.service;

import com.kitty.system.game.pet.domain.Pet;
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
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.ranking.util.RankingUtils;
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
import com.kitty.system.init.domain.SysDataTrainAttr;
import com.kitty.system.init.domain.SysDataTrainSeal;
import com.kitty.system.init.domain.SysDataTrainExp;
import com.kitty.system.init.domain.SysDataTrain;
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
import com.kitty.system.init.domain.SysDataChargeScoreShop;
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
import java.util.Comparator;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import java.util.LinkedHashMap;
import java.util.ArrayList;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import java.util.Iterator;
import java.util.HashMap;
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
import java.util.stream.Collectors;
import com.kitty.system.init.domain.Tgroups;
import java.util.Map;
import com.kitty.common.mysql.TargetDataSource;
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
    
    public InitService() {
    }
    
    @TargetDataSource(name = "ac")
    public List<Word> getWord() {
        return this.dao.query(Word.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "ac")
    public Map<String, Tgroups> initGroups() {
        List<Tgroups> list = this.dao.query(Tgroups.class, Cnd.where("skid", "!=", "NULL"));
        return (Map)list.stream().collect(Collectors.toMap(Tgroups::getSkid, Tgroups -> Tgroups));
    }
    
    public Map<String, GiftBag> initGiftBag() {
        List<GiftBag> list = this.dao.query(GiftBag.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(GiftBag::getName, GiftBag -> GiftBag));
    }
    
    public Map<Integer, Event> initEvent() {
        List<Event> list = this.dao.query(Event.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(Event::getSerial, Event -> Event));
    }
    
    @TargetDataSource(name = "ac")
    public long initDate() {
        String[] split = ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking().split(",");
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
        List<SysCanMove> list = this.dao.query(SysCanMove.class, Cnd.NEW());
        return list;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPet> initPet() {
        List<SysPet> list = this.dao.query(SysPet.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPet::getSerial, SysPet -> SysPet));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysHorse> initHorse() {
        List<SysHorse> list = this.dao.query(SysHorse.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysHorse::getSerial, SysHorse -> SysHorse));
    }
    
    @TargetDataSource(name = "se")
    public List<HorseSkill> initHorseSkill() {
        return this.dao.query(HorseSkill.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysReiEn> initReiEn() {
        List<SysReiEn> list = this.dao.query(SysReiEn.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysReiEn::getSerial, SysReiEn -> SysReiEn));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysCountryQuestion> initCountryQuestionLib() {
        List<SysCountryQuestion> list = this.dao.query(SysCountryQuestion.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysCountryQuestion::getSerial, SysCountryQuestion -> SysCountryQuestion));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysProvinceQuestion> initProvinceQuestion() {
        List<SysProvinceQuestion> list = this.dao.query(SysProvinceQuestion.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysProvinceQuestion::getSerial, SysProvinceQuestion -> SysProvinceQuestion));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysHallQuestion> initHallQuestion() {
        List<SysHallQuestion> list = this.dao.query(SysHallQuestion.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysHallQuestion::getSerial, SysHallQuestion -> SysHallQuestion));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysRoleChangeColor> initRoleChangeColor() {
        List<SysRoleChangeColor> list = this.dao.query(SysRoleChangeColor.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysRoleChangeColor::getSerial, SysRoleChangeColor -> SysRoleChangeColor));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysGiftItem> initGiftItem() {
        List<SysGiftItem> list = this.dao.query(SysGiftItem.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysGiftItem::getSerial, SysGiftItem -> SysGiftItem));
    }
    
    @TargetDataSource(name = "se")
    public List<SysGiftItemOpen> initGiftItemOpen() {
        List<SysGiftItemOpen> list = this.dao.query(SysGiftItemOpen.class, Cnd.NEW());
        return list;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysGuajiMap> initGuajiMap() {
        List<SysGuajiMap> list = this.dao.query(SysGuajiMap.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysGuajiMap::getMapSerial, SysGuajiMap -> SysGuajiMap));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysGuajiWar> initGuajiWar() {
        List<SysGuajiWar> list = this.dao.query(SysGuajiWar.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysGuajiWar::getSerial, SysGuajiWar -> SysGuajiWar));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysHero> initHero() {
        List<SysHero> list = this.dao.query(SysHero.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysHero::getSerial, SysHero -> SysHero));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysCommonAward> initCommonAward() {
        List<SysCommonAward> list = this.dao.query(SysCommonAward.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysCommonAward::getDict, SysCommonAward -> SysCommonAward));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysMapInfo> initMapinfo() {
        List<SysMapInfo> list = this.dao.query(SysMapInfo.class, Cnd.NEW());
        ((MapService)SpringUtils.getBean(MapService.class)).initMapInfo(list);
        return (Map)list.stream().collect(Collectors.toMap(SysMapInfo::getMapSerial, SysMapinfo -> SysMapinfo));
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
        List<SysNpcTypeInfo> list = this.dao.query(SysNpcTypeInfo.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysNpcTypeInfo::getId, SysNpcTypeInfo -> SysNpcTypeInfo));
    }
    
    @TargetDataSource(name = "se")
    public List<SysPartyFlag> initPartyFlag() {
        return this.dao.query(SysPartyFlag.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysOtherItem> initOtherItem() {
        List<SysOtherItem> list = this.dao.query(SysOtherItem.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysOtherItem::getSerial, SysOtherItem -> SysOtherItem));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataAcsryEgg> initAcsryEgg() {
        List<SysDataAcsryEgg> list = this.dao.query(SysDataAcsryEgg.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataAcsryEgg::getSerial, SysDataAcsryEgg -> SysDataAcsryEgg));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysEffect> initEffect() {
        List<SysEffect> list = this.dao.query(SysEffect.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysEffect::getSerial, SysEffect -> SysEffect));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetExercise> initPetExercise() {
        List<SysPetExercise> list = this.dao.query(SysPetExercise.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPetExercise::getSoul, SysPetExercise -> SysPetExercise));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysRbAttr> initRbattr() {
        List<SysRbAttr> list = this.dao.query(SysRbAttr.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysRbAttr::getId, SysRbattr -> SysRbattr));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcArena> initShopNpcArena() {
        List<SysShopNpcArena> list = this.dao.query(SysShopNpcArena.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcArena::getSerial, SysShopNpcArena -> SysShopNpcArena));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcBrac> initShopNpcBrac() {
        List<SysShopNpcBrac> list = this.dao.query(SysShopNpcBrac.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcBrac::getSerial, SysShopNpcBrac -> SysShopNpcBrac));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcDi> initShopNpcDi() {
        List<SysShopNpcDi> list = this.dao.query(SysShopNpcDi.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcDi::getSerial, SysShopNpcDi -> SysShopNpcDi));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcFeats> initShopNpcFeats() {
        List<SysShopNpcFeats> list = this.dao.query(SysShopNpcFeats.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcFeats::getSerial, SysShopNpcFeats -> SysShopNpcFeats));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcGuo> initShopNpcGuo() {
        List<SysShopNpcGuo> list = this.dao.query(SysShopNpcGuo.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcGuo::getSerial, SysShopNpcGuo -> SysShopNpcGuo));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcGuoRecycle> initShopNpcGuoRecycle() {
        List<SysShopNpcGuoRecycle> list = this.dao.query(SysShopNpcGuoRecycle.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcGuoRecycle::getSerial, SysShopNpcGuoRecycle -> SysShopNpcGuoRecycle));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcLong> initShopNpcLong() {
        List<SysShopNpcLong> list = this.dao.query(SysShopNpcLong.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcLong::getSerial, SysShopNpcLong -> SysShopNpcLong));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcNao> initShopNpcNao() {
        List<SysShopNpcNao> list = this.dao.query(SysShopNpcNao.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcNao::getSerial, SysShopNpcNao -> SysShopNpcNao));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcNice> initShopNpcNice() {
        List<SysShopNpcNice> list = this.dao.query(SysShopNpcNice.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcNice::getSerial, SysShopNpcNice -> SysShopNpcNice));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDrug> initDrug() {
        List<SysDrug> list = this.dao.query(SysDrug.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDrug::getSerial, SysDrug -> SysDrug));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcPet> initShopNpcPet() {
        List<SysShopNpcPet> list = this.dao.query(SysShopNpcPet.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcPet::getSerial, SysShopNpcPet -> SysShopNpcPet));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcDrug> initShopNpcDrug() {
        List<SysShopNpcDrug> list = this.dao.query(SysShopNpcDrug.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcDrug::getSerial, SysShopNpcDrug -> SysShopNpcDrug));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcEquip> initShopNpcEquip() {
        List<SysShopNpcEquip> list = this.dao.query(SysShopNpcEquip.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcEquip::getSerial, SysShopNpcEquip -> SysShopNpcEquip));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopNpcGrocery> initShopNpcGrocery() {
        List<SysShopNpcGrocery> list = this.dao.query(SysShopNpcGrocery.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopNpcGrocery::getSerial, SysShopNpcGrocery -> SysShopNpcGrocery));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopOther> initShopOther() {
        List<SysShopOther> list = this.dao.query(SysShopOther.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopOther::getSerial, SysShopOther -> SysShopOther));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysShopStrengthenMaterial> initShopStrengthenMaterial() {
        List<SysShopStrengthenMaterial> list = this.dao.query(SysShopStrengthenMaterial.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysShopStrengthenMaterial::getSerial, SysShopStrengthenMaterial -> SysShopStrengthenMaterial));
    }
    
    @TargetDataSource(name = "se")
    public Object[] initSkill() {
        List<SysSkill> list = this.dao.query(SysSkill.class, Cnd.NEW());
        Map<Integer, SysSkill> skillMap = (Map)list.stream().collect(Collectors.toMap(SysSkill::getSerial, SysSkill -> SysSkill));
        Map<Integer, List<SysSkill>> raceGroupSkill = (Map)list.stream().collect(Collectors.groupingBy(SysSkill::getRace));
        Map<Integer, Map<Integer, Map<Integer, List<SysSkill>>>> groupSkillMap = new HashMap();
        Map<Integer, Map<Integer, List<SysSkill>>> sexGroupMap = null;
        for (Map.Entry<Integer, List<SysSkill>> entry : raceGroupSkill.entrySet()) {
            sexGroupMap = new HashMap();
            Map<Integer, List<SysSkill>> sexSkillMap = (Map)((List)entry.getValue()).stream().collect(Collectors.groupingBy(SysSkill::getSex));
            groupSkillMap.put(entry.getKey(), sexGroupMap);
            for (Map.Entry<Integer, List<SysSkill>> entry2 : sexSkillMap.entrySet()) {
                Map<Integer, List<SysSkill>> attrGroupMap = (Map)((List)entry2.getValue()).stream().collect(Collectors.groupingBy(SysSkill::getAttr));
                sexGroupMap.put(entry2.getKey(), attrGroupMap);
            }
        }
        return new Object[] { skillMap, groupSkillMap };
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysSkillUpGradeConsume> initSkillUpGradeConsume() {
        List<SysSkillUpGradeConsume> list = this.dao.query(SysSkillUpGradeConsume.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysSkillUpGradeConsume::getGrade, SysSkillExplain -> SysSkillExplain));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetSkill> initPetSkill() {
        List<SysPetSkill> list = this.dao.query(SysPetSkill.class, Cnd.NEW());
        this.initPassivePetSkill(list);
        return (Map)list.stream().collect(Collectors.toMap(SysPetSkill::getSerial, SysPetSkill -> SysPetSkill));
    }
    
    private void initPassivePetSkill(List<SysPetSkill> list) {
        List<SysPetSkill> passiveSkills = new ArrayList<>();
        Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps = new LinkedHashMap();
        Map<Integer, SysPetSkill> initiativeTriggerPassiveMap = new LinkedHashMap();
        Map<Integer, SysPetSkill> initiativeSkillMap = new LinkedHashMap();
        for (SysPetSkill petSkill : list) {
            if (0 == (int)petSkill.getInitiative()) {
                passiveSkills.add(petSkill);
            }
            else if (1 == (int)petSkill.getInitiative() && petSkill.getPassiveTriggerSkill() != null && !"".equals(petSkill.getPassiveTriggerSkill())) {
                initiativeTriggerPassiveMap.put(petSkill.getSerial(), petSkill);
                initiativeSkillMap.put(petSkill.getSerial(), petSkill);
            }
            else if (2 == (int)petSkill.getInitiative()) {
                passiveSkills.add(petSkill);
                initiativeSkillMap.put(petSkill.getSerial(), petSkill);
            }
            else {
                initiativeSkillMap.put(petSkill.getSerial(), petSkill);
            }
        }
        for (PET_PASSIVE_SKILL_TRIGGER_DICT dict : PET_PASSIVE_SKILL_TRIGGER_DICT.values()) {
            Map<Integer, SysPetSkill> petSkillMap = new HashMap();
            for (int i = passiveSkills.size() - 1; i >= 0; --i) {
                if (dict.getValue().equals(((SysPetSkill)passiveSkills.get(i)).getTriggerType())) {
                    petSkillMap.put(((SysPetSkill)passiveSkills.get(i)).getSerial(), passiveSkills.get(i));
                    passiveSkills.remove(i);
                }
            }
            petSkillMap = this.petSkillMapSort(petSkillMap);
            data_pet_skill_maps.put(dict.getValue(), petSkillMap);
        }
        for (int j = passiveSkills.size() - 1; j >= 0; --j) {
            this.otherTypeSkill(data_pet_skill_maps, (SysPetSkill)passiveSkills.get(j));
        }
        data_pet_skill_maps.put(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue(), initiativeTriggerPassiveMap);
        initiativeSkillMap = this.petSkillMapSort(initiativeSkillMap);
        data_pet_skill_maps.put(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), initiativeSkillMap);
        PetTriggerPassiveSkill.data_pet_skill_maps = data_pet_skill_maps;
    }
    
    private void otherTypeSkill(Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps, SysPetSkill sysPetSkill) {
        List<String> lists = (List)PET_PASSIVE_SKILL_TRIGGER_DICT.getSkillTypeOther().get(sysPetSkill.getSerial());
        if (lists != null && lists.size() > 0) {
            for (String type : lists) {
                Map<Integer, SysPetSkill> integerSysPetSkillMap = (Map)data_pet_skill_maps.get(type);
                if (integerSysPetSkillMap == null) {
                    integerSysPetSkillMap = new HashMap();
                    data_pet_skill_maps.put(type, integerSysPetSkillMap);
                }
                integerSysPetSkillMap.put(sysPetSkill.getSerial(), sysPetSkill);
                this.petSkillMapSort(integerSysPetSkillMap);
            }
        }
    }
    
    private Map<Integer, SysPetSkill> petSkillMapSort(Map<Integer, SysPetSkill> sourceMap) {
        Map<Integer, SysPetSkill> result = new LinkedHashMap();
        result = (Map)sourceMap.entrySet().stream().sorted(Comparator.comparing(e -> Integer.valueOf(((SysPetSkill)e.getValue()).getExecutionSequence()))).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
        return result;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysUpgrade> initUpgrade() {
        List<SysUpgrade> list = this.dao.query(SysUpgrade.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysUpgrade::getId, SysUpgrade -> SysUpgrade));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPartyPractice> initPartyPractice() {
        List<SysPartyPractice> list = this.dao.query(SysPartyPractice.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPartyPractice::getLevel, SysPartyPractice -> SysPartyPractice));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysAboveAttrLib> initAboveAttrLib() {
        List<SysAboveAttrLib> list = this.dao.query(SysAboveAttrLib.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysAboveAttrLib::getDict, SysAboveAttrLib -> SysAboveAttrLib));
    }
    
    @TargetDataSource(name = "se")
    public List<SysPurifyAttr> initPurify() {
        return this.dao.query(SysPurifyAttr.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataAcsry> initAcsry() {
        List<SysDataAcsry> list = this.dao.query(SysDataAcsry.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataAcsry::getSerial, SysDataAcsry -> SysDataAcsry));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataAcsryIdentify> initAcsryAIdentify() {
        List<SysDataAcsryIdentify> list = this.dao.query(SysDataAcsryIdentify.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataAcsryIdentify::getSuiteType, SysDataAcsryIdentify -> SysDataAcsryIdentify));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataAcsrySuit> initAcsrySuit() {
        List<SysDataAcsrySuit> list = this.dao.query(SysDataAcsrySuit.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataAcsrySuit::getDist, SysDataAcsrySuit -> SysDataAcsrySuit));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataAcsryAttr> initAcsryAttr() {
        List<SysDataAcsryAttr> list = this.dao.query(SysDataAcsryAttr.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataAcsryAttr::getDict, SysDataAcsryAttr -> SysDataAcsryAttr));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, Map<Integer, SysDataAcsrySuitColor>> initAcsrySuitColor() {
        List<SysDataAcsrySuitColor> sysDataAcsrySuitColors = this.dao.query(SysDataAcsrySuitColor.class, Cnd.NEW());
        Map<String, Map<Integer, SysDataAcsrySuitColor>> map = new HashMap();
        for (SysDataAcsrySuitColor acsrySuitColor : sysDataAcsrySuitColors) {
            Map<Integer, SysDataAcsrySuitColor> mapC = (Map)map.get(acsrySuitColor.getSuitDist());
            if (mapC == null) {
                mapC = new HashMap();
                map.put(acsrySuitColor.getSuitDist(), mapC);
            }
            mapC.put(Integer.valueOf(acsrySuitColor.getSkillSerial()), acsrySuitColor);
        }
        return map;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetAcsry> initPetAcsry() {
        List<SysPetAcsry> list = this.dao.query(SysPetAcsry.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPetAcsry::getSerial, SysPetAcsry -> SysPetAcsry));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPetAcsryAdvAttrLib> initPetAcsryAdvAttrLib() {
        List<SysPetAcsryAdvAttrLib> list = this.dao.query(SysPetAcsryAdvAttrLib.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPetAcsryAdvAttrLib::getAttrSerial, SysPetAcsryAdvAttrLib -> SysPetAcsryAdvAttrLib));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysPetAcsryAttrLib> initPetAcsryAttrLib() {
        List<SysPetAcsryAttrLib> list = this.dao.query(SysPetAcsryAttrLib.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPetAcsryAttrLib::getDict, SysPetAcsryAttrLib -> SysPetAcsryAttrLib));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysWarRole> initWarRole() {
        List<SysWarRole> list = this.dao.query(SysWarRole.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysWarRole::getSerial, SysWarRole -> SysWarRole));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysWxOtherWarData> initWxOtherWarData() {
        List<SysWxOtherWarData> list = this.dao.query(SysWxOtherWarData.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysWxOtherWarData::getId, SysWxOtherWarData -> SysWxOtherWarData));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataLimitShop> initLimitShop() {
        List<SysDataLimitShop> list = this.dao.query(SysDataLimitShop.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataLimitShop::getSerial, SysDataLimitShop -> SysDataLimitShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataRecommendShop> initRecommendShop() {
        List<SysDataRecommendShop> list = this.dao.query(SysDataRecommendShop.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataRecommendShop::getSerial, SysDataRecommendShop -> SysDataRecommendShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysBoundShop> initBoundShop() {
        List<SysBoundShop> list = this.dao.query(SysBoundShop.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysBoundShop::getSerial, SysBoundShop -> SysBoundShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataChargeScoreShop> initChargeScoreShop() {
        List<SysDataChargeScoreShop> list = this.dao.query(SysDataChargeScoreShop.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataChargeScoreShop::getSerial, SysDataChargeScoreShop -> SysDataChargeScoreShop));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataMonster> initDataMonster() {
        List<SysDataMonster> list = this.dao.query(SysDataMonster.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataMonster::getSerial, SysDataMonster -> SysDataMonster));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataMonsterPeripheral> initMonsterPeripheral() {
        List<SysDataMonsterPeripheral> list = this.dao.query(SysDataMonsterPeripheral.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataMonsterPeripheral::getSerial, SysDataMonsterPeripheral -> SysDataMonsterPeripheral));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataStallMenu> initDataStallMenu() {
        List<SysDataStallMenu> list = this.dao.query(SysDataStallMenu.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataStallMenu::getSerial, SysDataStallMenu -> SysDataStallMenu));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataStall> initDataStall() {
        List<SysDataStall> list = this.dao.query(SysDataStall.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataStall::getSerial, SysDataStall -> SysDataStall));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataStuff> initDataStuff() {
        List<SysDataStuff> list = this.dao.query(SysDataStuff.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataStuff::getSerial, SysDataStuff -> SysDataStuff));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysTaskItem> initTaskItem() {
        List<SysTaskItem> list = this.dao.query(SysTaskItem.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysTaskItem::getItemId, SysTaskItem -> SysTaskItem));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysNewBieEq> initNewBieEq() {
        List<SysNewBieEq> list = this.dao.query(SysNewBieEq.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysNewBieEq::getSerial, SysNewBieEq -> SysNewBieEq));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysSeniorBieEq> initSeniorBieEq() {
        List<SysSeniorBieEq> list = this.dao.query(SysSeniorBieEq.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysSeniorBieEq::getSerial, SysSeniorBieEq -> SysSeniorBieEq));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysMagicalBieEq> initMagicalBieEq() {
        List<SysMagicalBieEq> list = this.dao.query(SysMagicalBieEq.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysMagicalBieEq::getSerial, SysMagicalBieEq -> SysMagicalBieEq));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysSinBieEq> initSinBieEq() {
        List<SysSinBieEq> list = this.dao.query(SysSinBieEq.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysSinBieEq::getSerial, SysSinBieEq -> SysSinBieEq));
    }
    
    @TargetDataSource(name = "se")
    public List<SysRefine> initRefine() {
        return this.dao.query(SysRefine.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskMain> initTaskMain() {
        List<SysDataTaskMain> list = this.dao.query(SysDataTaskMain.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskMain::getSerial, SysDataTaskMain -> SysDataTaskMain));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskSchool> initTaskSchool() {
        List<SysDataTaskSchool> list = this.dao.query(SysDataTaskSchool.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskSchool::getSerial, SysDataTaskSchool -> SysDataTaskSchool));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskTreasureMap> initTaskTreasureMap() {
        return this.dao.query(SysDataTaskTreasureMap.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskParty> initTaskParty() {
        return this.dao.query(SysDataTaskParty.class, Cnd.where("is_load", "=", Integer.valueOf(1)));
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
        List<SysDataTaskNpc> list = this.dao.query(SysDataTaskNpc.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskNpc::getSerial, SysDataTaskNpc -> SysDataTaskNpc));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskNpcName> initNpcName() {
        List<SysDataTaskNpcName> list = this.dao.query(SysDataTaskNpcName.class, Cnd.NEW());
        return list;
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataTaskNpcNameType> initNpcNameType() {
        List<SysDataTaskNpcNameType> list = this.dao.query(SysDataTaskNpcNameType.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskNpcNameType::getKey, SysDataTaskNpcNameType -> SysDataTaskNpcNameType));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskFormula> initTaskFormula() {
        List<SysDataTaskFormula> list = this.dao.query(SysDataTaskFormula.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskFormula::getSerial, SysDataTaskFormula -> SysDataTaskFormula));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskFight> initTaskFight() {
        List<SysDataTaskFight> list = this.dao.query(SysDataTaskFight.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskFight::getSerial, SysDataTaskFight -> SysDataTaskFight));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskCondition> initTaskCondition() {
        List<SysDataTaskCondition> list = this.dao.query(SysDataTaskCondition.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskCondition::getSubmitCondition, SysDataTaskCondition -> SysDataTaskCondition));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTaskEvent> initTaskEvent() {
        List<SysDataTaskEvent> list = this.dao.query(SysDataTaskEvent.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskEvent::getSerial, SysDataTaskEvent -> SysDataTaskEvent));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTaskEventForeign> initTaskEventForeign() {
        return this.dao.query(SysDataTaskEventForeign.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<String, SysDataTaskReward> initTaskReward() {
        List<SysDataTaskReward> list = this.dao.query(SysDataTaskReward.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTaskReward::getSerial, SysDataTaskReward -> SysDataTaskReward));
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
        List<SysDataTitle> list = this.dao.query(SysDataTitle.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTitle::getSerial, SysDataTitle -> SysDataTitle));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataGradeAward> initGradeAward() {
        List<SysDataGradeAward> list = this.dao.query(SysDataGradeAward.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataGradeAward::getGrade, SysDataGradeAward -> SysDataGradeAward));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataChargeAward> initChargeAward() {
        return this.dao.query(SysDataChargeAward.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysCharge> initCharge() {
        List<SysCharge> list = this.dao.query(SysCharge.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysCharge::getSerial, SysCharge -> SysCharge));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataExchange> initExchange() {
        List<SysDataExchange> list = this.dao.query(SysDataExchange.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataExchange::getSerial, SysDataExchange -> SysDataExchange));
    }
    
    @TargetDataSource(name = "se")
    public Map<Byte, SysDataNewDistrictSign> initNewDistrict() {
        List<SysDataNewDistrictSign> list = this.dao.query(SysDataNewDistrictSign.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataNewDistrictSign::getDay, SysDataNewDistrictSign -> SysDataNewDistrictSign));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataPrizeDraw> initPrizeDraw() {
        return this.dao.query(SysDataPrizeDraw.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataCard> initCard() {
        List<SysDataCard> list = this.dao.query(SysDataCard.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataCard::getSerial, SysDataCard -> SysDataCard));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataCardFiveExercise> initCardFiveExercise() {
        List<SysDataCardFiveExercise> list = this.dao.query(SysDataCardFiveExercise.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataCardFiveExercise::getGrade, SysDataCardFiveExercise -> SysDataCardFiveExercise));
    }
    
    @TargetDataSource(name = "se")
    public Object[] initTrain() {
        Object[] objs = new Object[2];
        List<SysDataTrain> list = this.dao.query(SysDataTrain.class, Cnd.NEW());
        Map<Integer, SysDataTrain> trainMap = (Map)list.stream().collect(Collectors.toMap(SysDataTrain::getSerial, SysDataTrain -> SysDataTrain));
        objs[0] = trainMap;
        Map<Integer, Integer> map = new HashMap();
        for (SysDataTrain sysDataTrain : list) {
            map.put(sysDataTrain.getBase_attr().get(0), Integer.valueOf(sysDataTrain.getSerial()));
        }
        objs[1] = map;
        return objs;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTrainExp> initTrainExp() {
        List<SysDataTrainExp> list = this.dao.query(SysDataTrainExp.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTrainExp::getGrade, SysDataTrainExp -> SysDataTrainExp));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataTrainSeal> initTrainSeal() {
        List<SysDataTrainSeal> list = this.dao.query(SysDataTrainSeal.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataTrainSeal::getSerial, SysDataTrainSeal -> SysDataTrainSeal));
    }
    
    @TargetDataSource(name = "se")
    public List<SysDataTrainAttr> initTrainAttr() {
        return this.dao.query(SysDataTrainAttr.class, Cnd.NEW());
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysPartner> initPartner() {
        List<SysPartner> list = this.dao.query(SysPartner.class, Cnd.NEW());
        return list.stream().collect(Collectors.toMap(SysPartner::getSerial, SysPartner -> SysPartner));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, Map<Short, SysPartnerProperty>> initPartnerProperty() {
        Map<Integer, Map<Short, SysPartnerProperty>> returnMap = new HashMap<>();
        List<SysPartnerProperty> list = this.dao.query(SysPartnerProperty.class, Cnd.NEW());
        if (list.size() > 0) {
            List<SysPartner> partners = this.dao.query(SysPartner.class, Cnd.NEW());
            List<Integer> partnerSerials = partners.stream().map(SysPartner::getSerial).collect(Collectors.toList());
            for (Integer partnerSerial : partnerSerials) {
                Map<Short, SysPartnerProperty> map = list.stream().filter(property -> property.getPartnerSerial() == (int)partnerSerial).collect(Collectors.toMap(SysPartnerProperty::getLevel, SysPartnerProperty -> SysPartnerProperty));
                returnMap.put(partnerSerial, map);
            }
            return returnMap;
        }
        return returnMap;
    }
    
    @TargetDataSource(name = "se")
    public Map<String, Map<Integer, SysLevelExp>> initLevelExp() {
        Map<String, Map<Integer, SysLevelExp>> returnMap = new HashMap<>();
        List<SysLevelExp> list = this.dao.query(SysLevelExp.class, Cnd.NEW());
        Map<Integer, SysLevelExp> heroMap = (Map)list.stream().filter(obj -> obj.getType() == LOGIC.LOGICTYPE_HERO.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("heroMap", heroMap);
        Map<Integer, SysLevelExp> petMap = (Map)list.stream().filter(obj -> obj.getType() == LOGIC.LOGICTYPE_PET.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("petMap", petMap);
        Map<Integer, SysLevelExp> partnerMap = (Map)list.stream().filter(obj -> obj.getType() == LOGIC.LOGICTYPE_PARTNER.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("partnerMap", partnerMap);
        Map<Integer, SysLevelExp> horseMap = (Map)list.stream().filter(obj -> obj.getType() == LOGIC.HORSE.getValue()).collect(Collectors.toMap(SysLevelExp::getLevel, SysLevelExp -> SysLevelExp));
        returnMap.put("horseMap", horseMap);
        return returnMap;
    }
    
    @TargetDataSource(name = "se")
    public Map<String, String> initCommonSet() {
        List<CommonSet> list = this.dao.query(CommonSet.class, Cnd.NEW());
        Map<String, String> map = (Map)list.stream().collect(Collectors.toMap(CommonSet::getKey, CommonSet -> CommonSet.getValue()));
        return map;
    }
    
    @TargetDataSource(name = "ac")
    public Map<String, String> initVersion() {
        Sql sql = Sqls.create("SELECT content FROM t_configuration where dict = 'hot_update'");
        sql = sql.setCallback(Sqls.callback.str());
        this.dao.execute(sql);
        if (sql.getString() != null) {
            Map<String, String> map = JsonUtils.string2Map(sql.getString(), String.class, String.class);
            return map;
        }
        return null;
    }
    
    public List<PractitionersSource> initPractitionersSource(List<Role> roles) {
        ArrayList<PractitionersSource> practitionersSources = new ArrayList<>();
        if (roles == null) {
            return practitionersSources;
        }
        long[] needExps = { 0L };
        practitionersSources = (ArrayList)roles.stream().map(role -> {
            needExps[0] = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), (int)role.getRelive(), (int)role.getLevel() + 1);
            PractitionersSource practitionersSource = new PractitionersSource(role, needExps[0]);
            return practitionersSource;
        }).sorted(Comparator.comparing(PractitionersSource::getReliveAndLevel, Comparator.reverseOrder()).thenComparing(PractitionersSource::getExp, Comparator.reverseOrder())).collect(Collectors.toCollection(ArrayList::new));
        return Collections.synchronizedList(practitionersSources);
    }
    
    public List<QlkRankingSource> initQlkRankingSource(List<Role> roles) {
        Map<Long, List<WearEquip>> wearEquipMaps = RankingList.wearEquipMaps;
        ArrayList<QlkRankingSource> qlkRankingSource = new ArrayList<>();
        if (roles == null || roles.size() <= 0 || wearEquipMaps == null || wearEquipMaps.size() <= 0) {
            return qlkRankingSource;
        }
        int[][] values = new int[1][1];
        qlkRankingSource = (ArrayList)roles.stream().map(role -> {
            values[0] = RankingUtils.getRoleQlkWuxing(role, (List)wearEquipMaps.get(role.getId()), null);
            QlkRankingSource qlkRankingSource2 = new QlkRankingSource(role, values[0]);
            return qlkRankingSource2;
        }).collect(Collectors.toCollection(ArrayList::new));
        return Collections.synchronizedList((List)qlkRankingSource.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkj, Comparator.reverseOrder())).collect(Collectors.toCollection(ArrayList::new)));
    }
    
    public Map<Long, WaterRankRecord> initWaterRank() {
        List<WaterRankRecord> list = this.dao.query(WaterRankRecord.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(WaterRankRecord::getRId, WaterRankRecord -> WaterRankRecord));
    }
    
    public void initWaterMirror() {
        List<Mirror> list = this.dao.query(Mirror.class, Cnd.NEW());
        for (Mirror mirror : list) {
            Pos pos = (Pos)JsonUtils.string2Object(mirror.getPosInfo(), Pos.class);
            Set<Long> set = (Set)DataCache.MIRROR_MAP_NPC.computeIfAbsent(pos.getMapId(), k -> new HashSet());
            set.add(mirror.getRId());
        }
        Map<Long, Mirror> map = (Map)list.stream().collect(Collectors.toMap(Mirror::getRId, Mirror -> Mirror));
        DataCache.MIRROR_MAP.putAll(map);
    }
    
    public Map<Long, CaRankRecord> initCaRank() {
        List<CaRankRecord> list = this.dao.query(CaRankRecord.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(CaRankRecord::getRId, CaRankRecord -> CaRankRecord));
    }
    
    @TargetDataSource(name = "se")
    public Object[] initTianFuSkill() {
        List<SysTianFuSkill> tianFuSkills = this.dao.query(SysTianFuSkill.class, Cnd.NEW());
        Map<Integer, SysTianFuSkill> map = (Map)tianFuSkills.stream().collect(Collectors.toMap(SysTianFuSkill::getCeSerial, SysTianFuSkill -> SysTianFuSkill));
        Map<Integer, SysTianFuSkill> opportunitys = (Map)tianFuSkills.stream().filter(tianFuSkill -> tianFuSkill.getOpportunity() != null && !"".equals(tianFuSkill.getOpportunity())).collect(Collectors.toMap(SysTianFuSkill::getCeSerial, SysTianFuSkill -> SysTianFuSkill));
        Map<Integer, SysTianFuSkill> noOpportunitys = (Map)tianFuSkills.stream().filter(tianFuSkill -> tianFuSkill.getOpportunity() == null || "".equals(tianFuSkill.getOpportunity())).collect(Collectors.toMap(SysTianFuSkill::getCeSerial, SysTianFuSkill -> SysTianFuSkill));
        Object[] objs = { map, opportunitys, noOpportunitys };
        return objs;
    }
    
    @TargetDataSource(name = "se")
    public Map<Short, SysDayInCePractice> initDayInCePractice() {
        List<SysDayInCePractice> sysDayInCePractices = this.dao.query(SysDayInCePractice.class, Cnd.NEW());
        Map<Short, SysDayInCePractice> map = (Map)sysDayInCePractices.stream().collect(Collectors.toMap(SysDayInCePractice::getLevel, SysDayInCePractice -> SysDayInCePractice));
        return map;
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataAcsrySkill> initAcsrySkill() {
        List<SysDataAcsrySkill> list = this.dao.query(SysDataAcsrySkill.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataAcsrySkill::getSerial, SysDataAcsrySkill -> SysDataAcsrySkill));
    }
    
    @TargetDataSource(name = "se")
    public Map<Integer, SysDataFurniture> initFurniture() {
        List<SysDataFurniture> list = this.dao.query(SysDataFurniture.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysDataFurniture::getSerial, SysDataFurniture -> SysDataFurniture));
    }
    
    @TargetDataSource(name = "se")
    public Map<String, Map<Byte, PetAccColor>> initPetAccColor() {
        List<SysPetAcsryColor> list = this.dao.query(SysPetAcsryColor.class, Cnd.NEW());
        return (Map)list.stream().collect(Collectors.toMap(SysPetAcsryColor::getDict, SysPetAcsryColor -> SysPetAcsryColor.getValue()));
    }
    
    public CopyOnWriteArrayList initEmailSystem() {
        List<EmailSystem> emailSystems = this.dao.query(EmailSystem.class, Cnd.where("recipient", "=", Integer.valueOf(0)).and("restrictions", "=", null).and("expiration_time", ">", Long.valueOf(new Date().getTime())).and("is_delete", "=", Integer.valueOf(0)).desc("expiration_time"));
        return new CopyOnWriteArrayList(emailSystems);
    }
    
    public List<Role> getRoles() {
        List<Role> roles = null;
        if (RankingList.roles.size() <= 0) {
            roles = this.dao.query(Role.class, Cnd.NEW());
            return roles;
        }
        roles = RankingList.roles;
        return roles;
    }
    
    public List<Pet> getPets() {
        List<Pet> pets = null;
        if (RankingList.pets.size() <= 0) {
            pets = this.dao.query(Pet.class, Cnd.NEW());
            RankingList.pets.addAll(pets);
            RankingList.petMaps.putAll((Map)pets.stream().collect(Collectors.groupingBy(Pet::getRoleId)));
            return pets;
        }
        pets = RankingList.pets;
        return pets;
    }
    
    public List<WearEquip> getWearEquips() {
        List<WearEquip> wearEquips = null;
        if (RankingList.wearEquips.size() <= 0) {
            wearEquips = this.dao.query(WearEquip.class, Cnd.NEW());
            RankingList.wearEquips.addAll(wearEquips);
            RankingList.wearEquipMaps.putAll((Map)wearEquips.stream().collect(Collectors.groupingBy(WearEquip::getRId)));
            return wearEquips;
        }
        wearEquips = RankingList.wearEquips;
        return wearEquips;
    }
}
