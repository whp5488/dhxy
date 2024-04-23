package com.kitty.system.init.service;

import com.kitty.common.model.Pos;
import com.kitty.common.mysql.TargetDataSource;
import com.kitty.common.start.ServerService;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.TimeUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.enums.war.itempro.def.PET_PASSIVE_SKILL_TRIGGER_DICT;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.activity.model.Event;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import com.kitty.system.game.dirtywords.domain.Word;
import com.kitty.system.game.email.domain.EmailSystem;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.fightnew.book.pet.utils.PetTriggerPassiveSkill;
import com.kitty.system.game.map.service.MapService;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.ranking.RankingList;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.util.RankingUtils;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.welfare.domain.GiftBag;
import com.kitty.system.init.domain.CommonSet;
import com.kitty.system.init.domain.HorseSkill;
import com.kitty.system.init.domain.SysAboveAttrLib;
import com.kitty.system.init.domain.SysBoundShop;
import com.kitty.system.init.domain.SysCanMove;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.system.init.domain.SysCommonAward;
import com.kitty.system.init.domain.SysCountryQuestion;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysDataAcsryAttr;
import com.kitty.system.init.domain.SysDataAcsryEgg;
import com.kitty.system.init.domain.SysDataAcsryIdentify;
import com.kitty.system.init.domain.SysDataAcsrySkill;
import com.kitty.system.init.domain.SysDataAcsrySuit;
import com.kitty.system.init.domain.SysDataAcsrySuitColor;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.init.domain.SysDataCardFiveExercise;
import com.kitty.system.init.domain.SysDataChargeAward;
import com.kitty.system.init.domain.SysDataExchange;
import com.kitty.system.init.domain.SysDataFurniture;
import com.kitty.system.init.domain.SysDataGradeAward;
import com.kitty.system.init.domain.SysDataLimitShop;
import com.kitty.system.init.domain.SysDataLottery;
import com.kitty.system.init.domain.SysDataLotteryRare;
import com.kitty.system.init.domain.SysDataLotterySuper;
import com.kitty.system.init.domain.SysDataLuckDraw;
import com.kitty.system.init.domain.SysDataMagicBox;
import com.kitty.system.init.domain.SysDataMonster;
import com.kitty.system.init.domain.SysDataMonsterPeripheral;
import com.kitty.system.init.domain.SysDataNewDistrictSign;
import com.kitty.system.init.domain.SysDataPartyBandit;
import com.kitty.system.init.domain.SysDataPrizeDraw;
import com.kitty.system.init.domain.SysDataRecommendShop;
import com.kitty.system.init.domain.SysDataSealDemon;
import com.kitty.system.init.domain.SysDataStall;
import com.kitty.system.init.domain.SysDataStallMenu;
import com.kitty.system.init.domain.SysDataStarOfficer;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.domain.SysDataTaskCarnival;
import com.kitty.system.init.domain.SysDataTaskCatchesGhosts;
import com.kitty.system.init.domain.SysDataTaskCondition;
import com.kitty.system.init.domain.SysDataTaskDragon;
import com.kitty.system.init.domain.SysDataTaskEvent;
import com.kitty.system.init.domain.SysDataTaskEventForeign;
import com.kitty.system.init.domain.SysDataTaskFight;
import com.kitty.system.init.domain.SysDataTaskFive;
import com.kitty.system.init.domain.SysDataTaskForeign;
import com.kitty.system.init.domain.SysDataTaskFormula;
import com.kitty.system.init.domain.SysDataTaskGodsCourt;
import com.kitty.system.init.domain.SysDataTaskKeju;
import com.kitty.system.init.domain.SysDataTaskLookFor;
import com.kitty.system.init.domain.SysDataTaskLoop;
import com.kitty.system.init.domain.SysDataTaskMain;
import com.kitty.system.init.domain.SysDataTaskMo;
import com.kitty.system.init.domain.SysDataTaskMoAward;
import com.kitty.system.init.domain.SysDataTaskNpc;
import com.kitty.system.init.domain.SysDataTaskNpcName;
import com.kitty.system.init.domain.SysDataTaskNpcNameType;
import com.kitty.system.init.domain.SysDataTaskParty;
import com.kitty.system.init.domain.SysDataTaskReward;
import com.kitty.system.init.domain.SysDataTaskRewardDynamic;
import com.kitty.system.init.domain.SysDataTaskSchool;
import com.kitty.system.init.domain.SysDataTaskSurround;
import com.kitty.system.init.domain.SysDataTaskTower;
import com.kitty.system.init.domain.SysDataTaskTreasureMap;
import com.kitty.system.init.domain.SysDataTaskXm;
import com.kitty.system.init.domain.SysDataThreeScopeDemon;
import com.kitty.system.init.domain.SysDataTitle;
import com.kitty.system.init.domain.SysDataWorldBoos;
import com.kitty.system.init.domain.SysDrug;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.domain.SysGiftItem;
import com.kitty.system.init.domain.SysGiftItemOpen;
import com.kitty.system.init.domain.SysGuajiMap;
import com.kitty.system.init.domain.SysGuajiWar;
import com.kitty.system.init.domain.SysHallQuestion;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysHorse;
import com.kitty.system.init.domain.SysLevelExp;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysMapInfo;
import com.kitty.system.init.domain.SysMapTeleporter;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysNpcTypeInfo;
import com.kitty.system.init.domain.SysOtherItem;
import com.kitty.system.init.domain.SysPartner;
import com.kitty.system.init.domain.SysPartnerProperty;
import com.kitty.system.init.domain.SysPartyFlag;
import com.kitty.system.init.domain.SysPartyPractice;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.init.domain.SysPetAcsryAttrLib;
import com.kitty.system.init.domain.SysPetAcsryColor;
import com.kitty.system.init.domain.SysPetExercise;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.init.domain.SysProvinceQuestion;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.init.domain.SysRefine;
import com.kitty.system.init.domain.SysReiEn;
import com.kitty.system.init.domain.SysRoleChangeColor;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysShopNpcArena;
import com.kitty.system.init.domain.SysShopNpcBrac;
import com.kitty.system.init.domain.SysShopNpcDi;
import com.kitty.system.init.domain.SysShopNpcDrug;
import com.kitty.system.init.domain.SysShopNpcEquip;
import com.kitty.system.init.domain.SysShopNpcFeats;
import com.kitty.system.init.domain.SysShopNpcGrocery;
import com.kitty.system.init.domain.SysShopNpcGuo;
import com.kitty.system.init.domain.SysShopNpcGuoRecycle;
import com.kitty.system.init.domain.SysShopNpcLong;
import com.kitty.system.init.domain.SysShopNpcNao;
import com.kitty.system.init.domain.SysShopNpcNice;
import com.kitty.system.init.domain.SysShopNpcPet;
import com.kitty.system.init.domain.SysShopOther;
import com.kitty.system.init.domain.SysShopStrengthenMaterial;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.init.domain.SysSkillUpGradeConsume;
import com.kitty.system.init.domain.SysTaskItem;
import com.kitty.system.init.domain.SysUpgrade;
import com.kitty.system.init.domain.SysWarRole;
import com.kitty.system.init.domain.SysWxOtherWarData;
import com.kitty.system.init.domain.Tgroups;
import com.kitty.system.init.domain.vo.PetAccColor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.dao.Sqls;
import org.nutz.dao.sql.Sql;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InitService {
  @Autowired
  Dao dao;
  
  @TargetDataSource(name = "ac")
  public List<Word> getWord() {
    return this.dao.query(Word.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "ac")
  public Map<String, Tgroups> initGroups() {
    List<Tgroups> list = this.dao.query(Tgroups.class, (Condition)Cnd.where("skid", "!=", "NULL"));
    return (Map<String, Tgroups>)list.stream().collect(Collectors.toMap(Tgroups::getSkid, tgroups -> tgroups));
  }
  
  public Map<String, GiftBag> initGiftBag() {
    List<GiftBag> list = this.dao.query(GiftBag.class, (Condition)Cnd.NEW());
    return (Map<String, GiftBag>)list.stream().collect(Collectors.toMap(GiftBag::getName, giftBag -> giftBag));
  }
  
  public Map<Integer, Event> initEvent() {
    List<Event> list = this.dao.query(Event.class, (Condition)Cnd.NEW());
    return (Map<Integer, Event>)list.stream().collect(Collectors.toMap(Event::getSerial, event -> event));
  }
  
  @TargetDataSource(name = "ac")
  public long initDate() {
    String[] split = ((ServerService)SpringUtils.getBean(ServerService.class)).getMarking().split(",");
    Sql sql = Sqls.create("SELECT opday FROM t_groups WHERE skid = '" + split[0] + "'");
    sql = sql.setCallback(Sqls.callback.str());
    this.dao.execute(sql);
    if (sql.getString() != null)
      return TimeUtil.timeInMillis(sql.getString()); 
    return System.currentTimeMillis();
  }
  
  @TargetDataSource(name = "se")
  public List<SysCanMove> initCanMove() {
    List<SysCanMove> list = this.dao.query(SysCanMove.class, (Condition)Cnd.NEW());
    return list;
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPet> initPet() {
    List<SysPet> list = this.dao.query(SysPet.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysPet>)list.stream().collect(Collectors.toMap(SysPet::getSerial, sysPet -> sysPet));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysHorse> initHorse() {
    List<SysHorse> list = this.dao.query(SysHorse.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysHorse>)list.stream().collect(Collectors.toMap(SysHorse::getSerial, sysHorse -> sysHorse));
  }
  
  @TargetDataSource(name = "se")
  public List<HorseSkill> initHorseSkill() {
    return this.dao.query(HorseSkill.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysReiEn> initReiEn() {
    List<SysReiEn> list = this.dao.query(SysReiEn.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysReiEn>)list.stream().collect(Collectors.toMap(SysReiEn::getSerial, sysReiEn -> sysReiEn));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysCountryQuestion> initCountryQuestionLib() {
    List<SysCountryQuestion> list = this.dao.query(SysCountryQuestion.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysCountryQuestion>)list.stream().collect(Collectors.toMap(SysCountryQuestion::getSerial, sysCountryQuestion -> sysCountryQuestion));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysProvinceQuestion> initProvinceQuestion() {
    List<SysProvinceQuestion> list = this.dao.query(SysProvinceQuestion.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysProvinceQuestion>)list.stream().collect(Collectors.toMap(SysProvinceQuestion::getSerial, sysProvinceQuestion -> sysProvinceQuestion));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysHallQuestion> initHallQuestion() {
    List<SysHallQuestion> list = this.dao.query(SysHallQuestion.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysHallQuestion>)list.stream().collect(Collectors.toMap(SysHallQuestion::getSerial, sysHallQuestion -> sysHallQuestion));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysRoleChangeColor> initRoleChangeColor() {
    List<SysRoleChangeColor> list = this.dao.query(SysRoleChangeColor.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysRoleChangeColor>)list.stream().collect(Collectors.toMap(SysRoleChangeColor::getSerial, sysRoleChangeColor -> sysRoleChangeColor));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysGiftItem> initGiftItem() {
    List<SysGiftItem> list = this.dao.query(SysGiftItem.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysGiftItem>)list.stream().collect(Collectors.toMap(SysGiftItem::getSerial, sysGiftItem -> sysGiftItem));
  }
  
  @TargetDataSource(name = "se")
  public List<SysGiftItemOpen> initGiftItemOpen() {
    List<SysGiftItemOpen> list = this.dao.query(SysGiftItemOpen.class, (Condition)Cnd.NEW());
    return list;
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysGuajiMap> initGuajiMap() {
    List<SysGuajiMap> list = this.dao.query(SysGuajiMap.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysGuajiMap>)list.stream().collect(Collectors.toMap(SysGuajiMap::getMapSerial, sysGuajiMap -> sysGuajiMap));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysGuajiWar> initGuajiWar() {
    List<SysGuajiWar> list = this.dao.query(SysGuajiWar.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysGuajiWar>)list.stream().collect(Collectors.toMap(SysGuajiWar::getSerial, sysGuajiWar -> sysGuajiWar));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysHero> initHero() {
    List<SysHero> list = this.dao.query(SysHero.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysHero>)list.stream().collect(Collectors.toMap(SysHero::getSerial, sysHero -> sysHero));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysCommonAward> initCommonAward() {
    List<SysCommonAward> list = this.dao.query(SysCommonAward.class, (Condition)Cnd.NEW());
    return (Map<String, SysCommonAward>)list.stream().collect(Collectors.toMap(SysCommonAward::getDict, sysCommonAward -> sysCommonAward));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysMapInfo> initMapinfo() {
    List<SysMapInfo> list = this.dao.query(SysMapInfo.class, (Condition)Cnd.NEW());
    ((MapService)SpringUtils.getBean(MapService.class)).initMapInfo(list);
    return (Map<Integer, SysMapInfo>)list.stream().collect(Collectors.toMap(SysMapInfo::getMapSerial, SysMapinfo -> SysMapinfo));
  }
  
  @TargetDataSource(name = "se")
  public List<SysMapTeleporter> initMapteleporter() {
    return this.dao.query(SysMapTeleporter.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysNpcInfo> initNpcInfo() {
    return this.dao.query(SysNpcInfo.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysNpcTypeInfo> initNpcTypeInfo() {
    List<SysNpcTypeInfo> list = this.dao.query(SysNpcTypeInfo.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysNpcTypeInfo>)list.stream().collect(Collectors.toMap(SysNpcTypeInfo::getId, sysNpcTypeInfo -> sysNpcTypeInfo));
  }
  
  @TargetDataSource(name = "se")
  public List<SysPartyFlag> initPartyFlag() {
    return this.dao.query(SysPartyFlag.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysOtherItem> initOtherItem() {
    List<SysOtherItem> list = this.dao.query(SysOtherItem.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysOtherItem>)list.stream().collect(Collectors.toMap(SysOtherItem::getSerial, sysOtherItem -> sysOtherItem));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataAcsryEgg> initAcsryEgg() {
    List<SysDataAcsryEgg> list = this.dao.query(SysDataAcsryEgg.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataAcsryEgg>)list.stream().collect(Collectors.toMap(SysDataAcsryEgg::getSerial, sysDataAcsryEgg -> sysDataAcsryEgg));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysEffect> initEffect() {
    List<SysEffect> list = this.dao.query(SysEffect.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysEffect>)list.stream().collect(Collectors.toMap(SysEffect::getSerial, sysEffect -> sysEffect));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPetExercise> initPetExercise() {
    List<SysPetExercise> list = this.dao.query(SysPetExercise.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysPetExercise>)list.stream().collect(Collectors.toMap(SysPetExercise::getSoul, sysPetExercise -> sysPetExercise));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysRbAttr> initRbattr() {
    List<SysRbAttr> list = this.dao.query(SysRbAttr.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysRbAttr>)list.stream().collect(Collectors.toMap(SysRbAttr::getId, SysRbattr -> SysRbattr));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcArena> initShopNpcArena() {
    List<SysShopNpcArena> list = this.dao.query(SysShopNpcArena.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcArena>)list.stream().collect(Collectors.toMap(SysShopNpcArena::getSerial, sysShopNpcArena -> sysShopNpcArena));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcBrac> initShopNpcBrac() {
    List<SysShopNpcBrac> list = this.dao.query(SysShopNpcBrac.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcBrac>)list.stream().collect(Collectors.toMap(SysShopNpcBrac::getSerial, sysShopNpcBrac -> sysShopNpcBrac));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcDi> initShopNpcDi() {
    List<SysShopNpcDi> list = this.dao.query(SysShopNpcDi.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcDi>)list.stream().collect(Collectors.toMap(SysShopNpcDi::getSerial, sysShopNpcDi -> sysShopNpcDi));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcFeats> initShopNpcFeats() {
    List<SysShopNpcFeats> list = this.dao.query(SysShopNpcFeats.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcFeats>)list.stream().collect(Collectors.toMap(SysShopNpcFeats::getSerial, sysShopNpcFeats -> sysShopNpcFeats));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcGuo> initShopNpcGuo() {
    List<SysShopNpcGuo> list = this.dao.query(SysShopNpcGuo.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcGuo>)list.stream().collect(Collectors.toMap(SysShopNpcGuo::getSerial, sysShopNpcGuo -> sysShopNpcGuo));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcGuoRecycle> initShopNpcGuoRecycle() {
    List<SysShopNpcGuoRecycle> list = this.dao.query(SysShopNpcGuoRecycle.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcGuoRecycle>)list.stream().collect(Collectors.toMap(SysShopNpcGuoRecycle::getSerial, sysShopNpcGuoRecycle -> sysShopNpcGuoRecycle));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcLong> initShopNpcLong() {
    List<SysShopNpcLong> list = this.dao.query(SysShopNpcLong.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcLong>)list.stream().collect(Collectors.toMap(SysShopNpcLong::getSerial, sysShopNpcLong -> sysShopNpcLong));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcNao> initShopNpcNao() {
    List<SysShopNpcNao> list = this.dao.query(SysShopNpcNao.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcNao>)list.stream().collect(Collectors.toMap(SysShopNpcNao::getSerial, sysShopNpcNao -> sysShopNpcNao));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcNice> initShopNpcNice() {
    List<SysShopNpcNice> list = this.dao.query(SysShopNpcNice.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcNice>)list.stream().collect(Collectors.toMap(SysShopNpcNice::getSerial, sysShopNpcNice -> sysShopNpcNice));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDrug> initDrug() {
    List<SysDrug> list = this.dao.query(SysDrug.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDrug>)list.stream().collect(Collectors.toMap(SysDrug::getSerial, sysDrug -> sysDrug));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcPet> initShopNpcPet() {
    List<SysShopNpcPet> list = this.dao.query(SysShopNpcPet.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcPet>)list.stream().collect(Collectors.toMap(SysShopNpcPet::getSerial, sysShopNpcPet -> sysShopNpcPet));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcDrug> initShopNpcDrug() {
    List<SysShopNpcDrug> list = this.dao.query(SysShopNpcDrug.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcDrug>)list.stream().collect(Collectors.toMap(SysShopNpcDrug::getSerial, sysShopNpcDrug -> sysShopNpcDrug));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcEquip> initShopNpcEquip() {
    List<SysShopNpcEquip> list = this.dao.query(SysShopNpcEquip.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcEquip>)list.stream().collect(Collectors.toMap(SysShopNpcEquip::getSerial, sysShopNpcEquip -> sysShopNpcEquip));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopNpcGrocery> initShopNpcGrocery() {
    List<SysShopNpcGrocery> list = this.dao.query(SysShopNpcGrocery.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopNpcGrocery>)list.stream().collect(Collectors.toMap(SysShopNpcGrocery::getSerial, sysShopNpcGrocery -> sysShopNpcGrocery));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopOther> initShopOther() {
    List<SysShopOther> list = this.dao.query(SysShopOther.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopOther>)list.stream().collect(Collectors.toMap(SysShopOther::getSerial, sysShopOther -> sysShopOther));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysShopStrengthenMaterial> initShopStrengthenMaterial() {
    List<SysShopStrengthenMaterial> list = this.dao.query(SysShopStrengthenMaterial.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysShopStrengthenMaterial>)list.stream().collect(Collectors.toMap(SysShopStrengthenMaterial::getSerial, sysShopStrengthenMaterial -> sysShopStrengthenMaterial));
  }
  
  @TargetDataSource(name = "se")
  public Object[] initSkill() {
    List<SysSkill> list = this.dao.query(SysSkill.class, (Condition)Cnd.NEW());
    Map<Integer, SysSkill> skillMap = (Map<Integer, SysSkill>)list.stream().collect(Collectors.toMap(SysSkill::getSerial, sysSkill -> sysSkill));
    Map<Integer, List<SysSkill>> raceGroupSkill = (Map<Integer, List<SysSkill>>)list.stream().collect(Collectors.groupingBy(SysSkill::getRace));
    Map<Integer, Map<Integer, Map<Integer, List<SysSkill>>>> groupSkillMap = new HashMap<>();
    Map<Integer, Map<Integer, List<SysSkill>>> sexGroupMap = null;
    for (Map.Entry<Integer, List<SysSkill>> entry : raceGroupSkill.entrySet()) {
      sexGroupMap = new HashMap<>();
      Map<Integer, List<SysSkill>> sexSkillMap = (Map<Integer, List<SysSkill>>)((List)entry.getValue()).stream().collect(Collectors.groupingBy(SysSkill::getSex));
      groupSkillMap.put(entry.getKey(), sexGroupMap);
      for (Map.Entry<Integer, List<SysSkill>> entry1 : sexSkillMap.entrySet()) {
        Map<Integer, List<SysSkill>> attrGroupMap = (Map<Integer, List<SysSkill>>)((List)entry1.getValue()).stream().collect(Collectors.groupingBy(SysSkill::getAttr));
        sexGroupMap.put(entry1.getKey(), attrGroupMap);
      } 
    } 
    return new Object[] { skillMap, groupSkillMap };
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysSkillUpGradeConsume> initSkillUpGradeConsume() {
    List<SysSkillUpGradeConsume> list = this.dao.query(SysSkillUpGradeConsume.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysSkillUpGradeConsume>)list.stream().collect(Collectors.toMap(SysSkillUpGradeConsume::getGrade, SysSkillExplain -> SysSkillExplain));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPetSkill> initPetSkill() {
    List<SysPetSkill> list = this.dao.query(SysPetSkill.class, (Condition)Cnd.NEW());
    initPassivePetSkill(list);
    return (Map<Integer, SysPetSkill>)list.stream().collect(Collectors.toMap(SysPetSkill::getSerial, sysPetSkill -> sysPetSkill));
  }
  
  private void initPassivePetSkill(List<SysPetSkill> list) {
    List<SysPetSkill> passiveSkills = new ArrayList<>();
    Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps = new LinkedHashMap<>();
    Map<Integer, SysPetSkill> initiativeTriggerPassiveMap = new LinkedHashMap<>();
    Map<Integer, SysPetSkill> initiativeSkillMap = new LinkedHashMap<>();
    for (SysPetSkill petSkill : list) {
      if (0 == petSkill.getInitiative().intValue()) {
        passiveSkills.add(petSkill);
        continue;
      } 
      if (1 == petSkill.getInitiative().intValue() && petSkill.getPassiveTriggerSkill() != null && !"".equals(petSkill.getPassiveTriggerSkill())) {
        initiativeTriggerPassiveMap.put(petSkill.getSerial(), petSkill);
        initiativeSkillMap.put(petSkill.getSerial(), petSkill);
        continue;
      } 
      if (2 == petSkill.getInitiative().intValue()) {
        passiveSkills.add(petSkill);
        initiativeSkillMap.put(petSkill.getSerial(), petSkill);
        continue;
      } 
      initiativeSkillMap.put(petSkill.getSerial(), petSkill);
    } 
    for (PET_PASSIVE_SKILL_TRIGGER_DICT dict : PET_PASSIVE_SKILL_TRIGGER_DICT.values()) {
      Map<Integer, SysPetSkill> petSkillMap = new HashMap<>();
      for (int j = passiveSkills.size() - 1; j >= 0; j--) {
        if (dict.getValue().equals(((SysPetSkill)passiveSkills.get(j)).getTriggerType())) {
          petSkillMap.put(((SysPetSkill)passiveSkills.get(j)).getSerial(), passiveSkills.get(j));
          passiveSkills.remove(j);
        } 
      } 
      petSkillMap = petSkillMapSort(petSkillMap);
      data_pet_skill_maps.put(dict.getValue(), petSkillMap);
    } 
    for (int i = passiveSkills.size() - 1; i >= 0; i--)
      otherTypeSkill(data_pet_skill_maps, passiveSkills.get(i)); 
    data_pet_skill_maps.put(PET_PASSIVE_SKILL_TRIGGER_DICT.PASSIVE_TRIGGER_FOR_INITIATIVE_SKILL.getValue(), initiativeTriggerPassiveMap);
    initiativeSkillMap = petSkillMapSort(initiativeSkillMap);
    data_pet_skill_maps.put(PET_PASSIVE_SKILL_TRIGGER_DICT.INITIATIVE_SKILL.getValue(), initiativeSkillMap);
    PetTriggerPassiveSkill.data_pet_skill_maps = data_pet_skill_maps;
  }
  
  private void otherTypeSkill(Map<String, Map<Integer, SysPetSkill>> data_pet_skill_maps, SysPetSkill sysPetSkill) {
    List<String> lists = (List<String>)PET_PASSIVE_SKILL_TRIGGER_DICT.getSkillTypeOther().get(sysPetSkill.getSerial());
    if (lists != null && lists.size() > 0)
      for (String type : lists) {
        Map<Integer, SysPetSkill> integerSysPetSkillMap = data_pet_skill_maps.get(type);
        if (integerSysPetSkillMap == null) {
          integerSysPetSkillMap = new HashMap<>();
          data_pet_skill_maps.put(type, integerSysPetSkillMap);
        } 
        integerSysPetSkillMap.put(sysPetSkill.getSerial(), sysPetSkill);
        petSkillMapSort(integerSysPetSkillMap);
      }  
  }
  
  private Map<Integer, SysPetSkill> petSkillMapSort(Map<Integer, SysPetSkill> sourceMap) {
    Map<Integer, SysPetSkill> result = new LinkedHashMap<>();
    result = (Map<Integer, SysPetSkill>)sourceMap.entrySet().stream().sorted(Comparator.comparing(e -> Integer.valueOf(((SysPetSkill)e.getValue()).getExecutionSequence()))).collect(
        Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldVal, newVal) -> oldVal, LinkedHashMap::new));
    return result;
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysUpgrade> initUpgrade() {
    List<SysUpgrade> list = this.dao.query(SysUpgrade.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysUpgrade>)list.stream().collect(Collectors.toMap(SysUpgrade::getId, sysUpgrade -> sysUpgrade));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPartyPractice> initPartyPractice() {
    List<SysPartyPractice> list = this.dao.query(SysPartyPractice.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysPartyPractice>)list.stream().collect(Collectors.toMap(SysPartyPractice::getLevel, sysPartyPractice -> sysPartyPractice));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysAboveAttrLib> initAboveAttrLib() {
    List<SysAboveAttrLib> list = this.dao.query(SysAboveAttrLib.class, (Condition)Cnd.NEW());
    return (Map<String, SysAboveAttrLib>)list.stream().collect(Collectors.toMap(SysAboveAttrLib::getDict, sysAboveAttrLib -> sysAboveAttrLib));
  }
  
  @TargetDataSource(name = "se")
  public List<SysPurifyAttr> initPurify() {
    return this.dao.query(SysPurifyAttr.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataAcsry> initAcsry() {
    List<SysDataAcsry> list = this.dao.query(SysDataAcsry.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataAcsry>)list.stream().collect(Collectors.toMap(SysDataAcsry::getSerial, sysDataAcsry -> sysDataAcsry));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysDataAcsryIdentify> initAcsryAIdentify() {
    List<SysDataAcsryIdentify> list = this.dao.query(SysDataAcsryIdentify.class, (Condition)Cnd.NEW());
    return (Map<String, SysDataAcsryIdentify>)list.stream().collect(Collectors.toMap(SysDataAcsryIdentify::getSuiteType, sysDataAcsryIdentify -> sysDataAcsryIdentify));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysDataAcsrySuit> initAcsrySuit() {
    List<SysDataAcsrySuit> list = this.dao.query(SysDataAcsrySuit.class, (Condition)Cnd.NEW());
    return (Map<String, SysDataAcsrySuit>)list.stream().collect(Collectors.toMap(SysDataAcsrySuit::getDist, sysDataAcsrySuit -> sysDataAcsrySuit));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysDataAcsryAttr> initAcsryAttr() {
    List<SysDataAcsryAttr> list = this.dao.query(SysDataAcsryAttr.class, (Condition)Cnd.NEW());
    return (Map<String, SysDataAcsryAttr>)list.stream().collect(Collectors.toMap(SysDataAcsryAttr::getDict, sysDataAcsryAttr -> sysDataAcsryAttr));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, Map<Integer, SysDataAcsrySuitColor>> initAcsrySuitColor() {
    List<SysDataAcsrySuitColor> sysDataAcsrySuitColors = this.dao.query(SysDataAcsrySuitColor.class, (Condition)Cnd.NEW());
    Map<String, Map<Integer, SysDataAcsrySuitColor>> map = new HashMap<>();
    for (SysDataAcsrySuitColor acsrySuitColor : sysDataAcsrySuitColors) {
      Map<Integer, SysDataAcsrySuitColor> mapC = map.get(acsrySuitColor.getSuitDist());
      if (mapC == null) {
        mapC = new HashMap<>();
        map.put(acsrySuitColor.getSuitDist(), mapC);
      } 
      mapC.put(Integer.valueOf(acsrySuitColor.getSkillSerial()), acsrySuitColor);
    } 
    return map;
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPetAcsry> initPetAcsry() {
    List<SysPetAcsry> list = this.dao.query(SysPetAcsry.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysPetAcsry>)list.stream().collect(Collectors.toMap(SysPetAcsry::getSerial, sysPetAcsry -> sysPetAcsry));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPetAcsryAdvAttrLib> initPetAcsryAdvAttrLib() {
    List<SysPetAcsryAdvAttrLib> list = this.dao.query(SysPetAcsryAdvAttrLib.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysPetAcsryAdvAttrLib>)list.stream().collect(Collectors.toMap(SysPetAcsryAdvAttrLib::getAttrSerial, sysPetAcsryAdvAttrLib -> sysPetAcsryAdvAttrLib));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysPetAcsryAttrLib> initPetAcsryAttrLib() {
    List<SysPetAcsryAttrLib> list = this.dao.query(SysPetAcsryAttrLib.class, (Condition)Cnd.NEW());
    return (Map<String, SysPetAcsryAttrLib>)list.stream().collect(Collectors.toMap(SysPetAcsryAttrLib::getDict, sysPetAcsryAttrLib -> sysPetAcsryAttrLib));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysWarRole> initWarRole() {
    List<SysWarRole> list = this.dao.query(SysWarRole.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysWarRole>)list.stream().collect(Collectors.toMap(SysWarRole::getSerial, sysWarRole -> sysWarRole));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysWxOtherWarData> initWxOtherWarData() {
    List<SysWxOtherWarData> list = this.dao.query(SysWxOtherWarData.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysWxOtherWarData>)list.stream().collect(Collectors.toMap(SysWxOtherWarData::getId, sysWxOtherWarData -> sysWxOtherWarData));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataLimitShop> initLimitShop() {
    List<SysDataLimitShop> list = this.dao.query(SysDataLimitShop.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataLimitShop>)list.stream().collect(Collectors.toMap(SysDataLimitShop::getSerial, sysDataLimitShop -> sysDataLimitShop));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataRecommendShop> initRecommendShop() {
    List<SysDataRecommendShop> list = this.dao.query(SysDataRecommendShop.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataRecommendShop>)list.stream().collect(Collectors.toMap(SysDataRecommendShop::getSerial, sysDataRecommendShop -> sysDataRecommendShop));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysBoundShop> initBoundShop() {
    List<SysBoundShop> list = this.dao.query(SysBoundShop.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysBoundShop>)list.stream().collect(Collectors.toMap(SysBoundShop::getSerial, sysBoundShop -> sysBoundShop));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataMonster> initDataMonster() {
    List<SysDataMonster> list = this.dao.query(SysDataMonster.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataMonster>)list.stream().collect(Collectors.toMap(SysDataMonster::getSerial, sysDataMonster -> sysDataMonster));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataMonsterPeripheral> initMonsterPeripheral() {
    List<SysDataMonsterPeripheral> list = this.dao.query(SysDataMonsterPeripheral.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataMonsterPeripheral>)list.stream().collect(Collectors.toMap(SysDataMonsterPeripheral::getSerial, sysDataMonsterPeripheral -> sysDataMonsterPeripheral));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataStallMenu> initDataStallMenu() {
    List<SysDataStallMenu> list = this.dao.query(SysDataStallMenu.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataStallMenu>)list.stream().collect(Collectors.toMap(SysDataStallMenu::getSerial, sysDataStallMenu -> sysDataStallMenu));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataStall> initDataStall() {
    List<SysDataStall> list = this.dao.query(SysDataStall.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataStall>)list.stream().collect(Collectors.toMap(SysDataStall::getSerial, sysDataStall -> sysDataStall));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataStuff> initDataStuff() {
    List<SysDataStuff> list = this.dao.query(SysDataStuff.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataStuff>)list.stream().collect(Collectors.toMap(SysDataStuff::getSerial, sysDataStuff -> sysDataStuff));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysTaskItem> initTaskItem() {
    List<SysTaskItem> list = this.dao.query(SysTaskItem.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysTaskItem>)list.stream().collect(Collectors.toMap(SysTaskItem::getItemId, sysTaskItem -> sysTaskItem));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysNewBieEq> initNewBieEq() {
    List<SysNewBieEq> list = this.dao.query(SysNewBieEq.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysNewBieEq>)list.stream().collect(Collectors.toMap(SysNewBieEq::getSerial, sysNewBieEq -> sysNewBieEq));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysSeniorBieEq> initSeniorBieEq() {
    List<SysSeniorBieEq> list = this.dao.query(SysSeniorBieEq.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysSeniorBieEq>)list.stream().collect(Collectors.toMap(SysSeniorBieEq::getSerial, sysSeniorBieEq -> sysSeniorBieEq));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysMagicalBieEq> initMagicalBieEq() {
    List<SysMagicalBieEq> list = this.dao.query(SysMagicalBieEq.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysMagicalBieEq>)list.stream().collect(Collectors.toMap(SysMagicalBieEq::getSerial, sysMagicalBieEq -> sysMagicalBieEq));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysSinBieEq> initSinBieEq() {
    List<SysSinBieEq> list = this.dao.query(SysSinBieEq.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysSinBieEq>)list.stream().collect(Collectors.toMap(SysSinBieEq::getSerial, sysSinBieEq -> sysSinBieEq));
  }
  
  @TargetDataSource(name = "se")
  public List<SysRefine> initRefine() {
    return this.dao.query(SysRefine.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskMain> initTaskMain() {
    List<SysDataTaskMain> list = this.dao.query(SysDataTaskMain.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskMain>)list.stream().collect(Collectors.toMap(SysDataTaskMain::getSerial, sysDataTaskMain -> sysDataTaskMain));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskSchool> initTaskSchool() {
    List<SysDataTaskSchool> list = this.dao.query(SysDataTaskSchool.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskSchool>)list.stream().collect(Collectors.toMap(SysDataTaskSchool::getSerial, sysDataTaskSchool -> sysDataTaskSchool));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskTreasureMap> initTaskTreasureMap() {
    return this.dao.query(SysDataTaskTreasureMap.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskParty> initTaskParty() {
    return this.dao.query(SysDataTaskParty.class, (Condition)Cnd.where("is_load", "=", Integer.valueOf(1)));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskLoop> initTaskLoop() {
    return this.dao.query(SysDataTaskLoop.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskTower> initTaskTower() {
    return this.dao.query(SysDataTaskTower.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskLookFor> initTaskLookFor() {
    return this.dao.query(SysDataTaskLookFor.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskXm> initTaskXm() {
    return this.dao.query(SysDataTaskXm.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskFive> initTaskFive() {
    return this.dao.query(SysDataTaskFive.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskCatchesGhosts> initTaskCatchesGhosts() {
    return this.dao.query(SysDataTaskCatchesGhosts.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskGodsCourt> initTaskGodsCourt() {
    return this.dao.query(SysDataTaskGodsCourt.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskCarnival> initTaskCarnival() {
    return this.dao.query(SysDataTaskCarnival.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskDragon> initTaskDragon() {
    return this.dao.query(SysDataTaskDragon.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskMo> initTaskMo() {
    return this.dao.query(SysDataTaskMo.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskMoAward> initTaskMoAward() {
    return this.dao.query(SysDataTaskMoAward.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskKeju> initTaskKj() {
    return this.dao.query(SysDataTaskKeju.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskNpc> initTaskNpc() {
    List<SysDataTaskNpc> list = this.dao.query(SysDataTaskNpc.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskNpc>)list.stream().collect(Collectors.toMap(SysDataTaskNpc::getSerial, sysDataTaskNpc -> sysDataTaskNpc));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskNpcName> initNpcName() {
    List<SysDataTaskNpcName> list = this.dao.query(SysDataTaskNpcName.class, (Condition)Cnd.NEW());
    return list;
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysDataTaskNpcNameType> initNpcNameType() {
    List<SysDataTaskNpcNameType> list = this.dao.query(SysDataTaskNpcNameType.class, (Condition)Cnd.NEW());
    return (Map<String, SysDataTaskNpcNameType>)list.stream().collect(Collectors.toMap(SysDataTaskNpcNameType::getKey, sysDataTaskNpcNameType -> sysDataTaskNpcNameType));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskFormula> initTaskFormula() {
    List<SysDataTaskFormula> list = this.dao.query(SysDataTaskFormula.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskFormula>)list.stream().collect(Collectors.toMap(SysDataTaskFormula::getSerial, sysDataTaskFormula -> sysDataTaskFormula));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskFight> initTaskFight() {
    List<SysDataTaskFight> list = this.dao.query(SysDataTaskFight.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskFight>)list.stream().collect(Collectors.toMap(SysDataTaskFight::getSerial, sysDataTaskFight -> sysDataTaskFight));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskCondition> initTaskCondition() {
    List<SysDataTaskCondition> list = this.dao.query(SysDataTaskCondition.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskCondition>)list.stream().collect(Collectors.toMap(SysDataTaskCondition::getSubmitCondition, sysDataTaskCondition -> sysDataTaskCondition));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTaskEvent> initTaskEvent() {
    List<SysDataTaskEvent> list = this.dao.query(SysDataTaskEvent.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTaskEvent>)list.stream().collect(Collectors.toMap(SysDataTaskEvent::getSerial, sysDataTaskEvent -> sysDataTaskEvent));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskEventForeign> initTaskEventForeign() {
    return this.dao.query(SysDataTaskEventForeign.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<String, SysDataTaskReward> initTaskReward() {
    List<SysDataTaskReward> list = this.dao.query(SysDataTaskReward.class, (Condition)Cnd.NEW());
    return (Map<String, SysDataTaskReward>)list.stream().collect(Collectors.toMap(SysDataTaskReward::getSerial, sysDataTaskReward -> sysDataTaskReward));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskRewardDynamic> initTaskRewardDynamic() {
    return this.dao.query(SysDataTaskRewardDynamic.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskForeign> initTaskRewardForeign() {
    return this.dao.query(SysDataTaskForeign.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataWorldBoos> initWorldBoos() {
    return this.dao.query(SysDataWorldBoos.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataPartyBandit> initPartyBanditList() {
    return this.dao.query(SysDataPartyBandit.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataStarOfficer> initStarOfficerList() {
    return this.dao.query(SysDataStarOfficer.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataSealDemon> initSealDemonList() {
    return this.dao.query(SysDataSealDemon.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataThreeScopeDemon> initThreeScopeDemonList() {
    return this.dao.query(SysDataThreeScopeDemon.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataTaskSurround> initTaskSurround() {
    return this.dao.query(SysDataTaskSurround.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataLuckDraw> initLuckDraw() {
    return this.dao.query(SysDataLuckDraw.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataLottery> initLottery() {
    return this.dao.query(SysDataLottery.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataLotterySuper> initLotterySuper() {
    return this.dao.query(SysDataLotterySuper.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataLotteryRare> initLotteryRare() {
    return this.dao.query(SysDataLotteryRare.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataMagicBox> initMagicBox() {
    return this.dao.query(SysDataMagicBox.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataTitle> initTitle() {
    List<SysDataTitle> list = this.dao.query(SysDataTitle.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataTitle>)list.stream().collect(Collectors.toMap(SysDataTitle::getSerial, sysDataTitle -> sysDataTitle));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataGradeAward> initGradeAward() {
    List<SysDataGradeAward> list = this.dao.query(SysDataGradeAward.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataGradeAward>)list.stream().collect(Collectors.toMap(SysDataGradeAward::getGrade, sysDataGradeAward -> sysDataGradeAward));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataChargeAward> initChargeAward() {
    return this.dao.query(SysDataChargeAward.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysCharge> initCharge() {
    List<SysCharge> list = this.dao.query(SysCharge.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysCharge>)list.stream().collect(Collectors.toMap(SysCharge::getSerial, sysCharge -> sysCharge));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataExchange> initExchange() {
    List<SysDataExchange> list = this.dao.query(SysDataExchange.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataExchange>)list.stream().collect(Collectors.toMap(SysDataExchange::getSerial, sysDataExchange -> sysDataExchange));
  }
  
  @TargetDataSource(name = "se")
  public Map<Byte, SysDataNewDistrictSign> initNewDistrict() {
    List<SysDataNewDistrictSign> list = this.dao.query(SysDataNewDistrictSign.class, (Condition)Cnd.NEW());
    return (Map<Byte, SysDataNewDistrictSign>)list.stream().collect(Collectors.toMap(SysDataNewDistrictSign::getDay, sysDataNewDistrictSign -> sysDataNewDistrictSign));
  }
  
  @TargetDataSource(name = "se")
  public List<SysDataPrizeDraw> initPrizeDraw() {
    return this.dao.query(SysDataPrizeDraw.class, (Condition)Cnd.NEW());
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataCard> initCard() {
    List<SysDataCard> list = this.dao.query(SysDataCard.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataCard>)list.stream().collect(Collectors.toMap(SysDataCard::getSerial, sysDataCard -> sysDataCard));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataCardFiveExercise> initCardFiveExercise() {
    List<SysDataCardFiveExercise> list = this.dao.query(SysDataCardFiveExercise.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataCardFiveExercise>)list.stream().collect(Collectors.toMap(SysDataCardFiveExercise::getGrade, sysDataCardFiveExercise -> sysDataCardFiveExercise));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysPartner> initPartner() {
    List<SysPartner> list = this.dao.query(SysPartner.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysPartner>)list.stream().collect(Collectors.toMap(SysPartner::getSerial, sysPartner -> sysPartner));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, Map<Short, SysPartnerProperty>> initPartnerProperty() {
    Map<Integer, Map<Short, SysPartnerProperty>> returnMap = new HashMap<>();
    List<SysPartnerProperty> list = this.dao.query(SysPartnerProperty.class, (Condition)Cnd.NEW());
    if (list.size() > 0) {
      List<SysPartner> partners = this.dao.query(SysPartner.class, (Condition)Cnd.NEW());
      List<Integer> partnerSerials = (List<Integer>)partners.stream().map(SysPartner::getSerial).collect(Collectors.toList());
      for (Integer partnerSerial : partnerSerials) {
        Map<Short, SysPartnerProperty> map = (Map<Short, SysPartnerProperty>)list.stream().filter(property -> (property.getPartnerSerial() == partnerSerial.intValue())).collect(Collectors.toMap(SysPartnerProperty::getLevel, sysPartnerProperty -> sysPartnerProperty));
        returnMap.put(partnerSerial, map);
      } 
    } 
    return returnMap;
  }
  
  @TargetDataSource(name = "se")
  public Map<String, Map<Integer, SysLevelExp>> initLevelExp() {
    Map<String, Map<Integer, SysLevelExp>> returnMap = new HashMap<>();
    List<SysLevelExp> list = this.dao.query(SysLevelExp.class, (Condition)Cnd.NEW());
    Map<Integer, SysLevelExp> heroMap = (Map<Integer, SysLevelExp>)list.stream().filter(obj -> (obj.getType() == LOGIC.LOGICTYPE_HERO.getValue())).collect(Collectors.toMap(SysLevelExp::getLevel, sysLevelExp -> sysLevelExp));
    returnMap.put("heroMap", heroMap);
    Map<Integer, SysLevelExp> petMap = (Map<Integer, SysLevelExp>)list.stream().filter(obj -> (obj.getType() == LOGIC.LOGICTYPE_PET.getValue())).collect(Collectors.toMap(SysLevelExp::getLevel, sysLevelExp -> sysLevelExp));
    returnMap.put("petMap", petMap);
    Map<Integer, SysLevelExp> partnerMap = (Map<Integer, SysLevelExp>)list.stream().filter(obj -> (obj.getType() == LOGIC.LOGICTYPE_PARTNER.getValue())).collect(Collectors.toMap(SysLevelExp::getLevel, sysLevelExp -> sysLevelExp));
    returnMap.put("partnerMap", partnerMap);
    Map<Integer, SysLevelExp> horseMap = (Map<Integer, SysLevelExp>)list.stream().filter(obj -> (obj.getType() == LOGIC.HORSE.getValue())).collect(Collectors.toMap(SysLevelExp::getLevel, sysLevelExp -> sysLevelExp));
    returnMap.put("horseMap", horseMap);
    return returnMap;
  }
  
  @TargetDataSource(name = "se")
  public Map<String, String> initCommonSet() {
    List<CommonSet> list = this.dao.query(CommonSet.class, (Condition)Cnd.NEW());
    Map<String, String> map = (Map<String, String>)list.stream().collect(Collectors.toMap(CommonSet::getKey, commonSet -> commonSet.getValue()));
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
    if (roles == null)
      return practitionersSources; 
    long[] needExps = { 0L };
    practitionersSources = (ArrayList<PractitionersSource>)roles.stream().map(role -> {
          needExps[0] = DataGlobalFunc.getExpForRebirthLevel(LOGIC.LOGICTYPE_HERO.getValue(), role.getRelive().intValue(), role.getLevel().intValue() + 1);
          return new PractitionersSource(role, needExps[0]);
        }).sorted(Comparator.comparing(PractitionersSource::getReliveAndLevel, Comparator.reverseOrder()).thenComparing(PractitionersSource::getExp, Comparator.reverseOrder())).collect(Collectors.toCollection(ArrayList::new));
    return Collections.synchronizedList(practitionersSources);
  }
  
  public List<QlkRankingSource> initQlkRankingSource(List<Role> roles) {
    Map<Long, List<WearEquip>> wearEquipMaps = RankingList.wearEquipMaps;
    ArrayList<QlkRankingSource> qlkRankingSource = new ArrayList<>();
    if (roles == null || roles.size() <= 0 || wearEquipMaps == null || wearEquipMaps.size() <= 0)
      return qlkRankingSource; 
    int[][] values = new int[1][1];
    qlkRankingSource = (ArrayList<QlkRankingSource>)roles.stream().map(role -> {
          values[0] = RankingUtils.getRoleQlkWuxing(role, (List)wearEquipMaps.get(role.getId()), null);
          return new QlkRankingSource(role, values[0]);
        }).collect(Collectors.toCollection(ArrayList::new));
    return Collections.synchronizedList((List<QlkRankingSource>)qlkRankingSource.stream().sorted(Comparator.comparing(QlkRankingSource::getQlkj, Comparator.reverseOrder())).collect(Collectors.toCollection(ArrayList::new)));
  }
  
  public Map<Long, WaterRankRecord> initWaterRank() {
    List<WaterRankRecord> list = this.dao.query(WaterRankRecord.class, (Condition)Cnd.NEW());
    return (Map<Long, WaterRankRecord>)list.stream().collect(Collectors.toMap(WaterRankRecord::getRId, waterRankRecord -> waterRankRecord));
  }
  
  public void initWaterMirror() {
    List<Mirror> list = this.dao.query(Mirror.class, (Condition)Cnd.NEW());
    for (Mirror mirror : list) {
      Pos pos = (Pos)JsonUtils.string2Object(mirror.getPosInfo(), Pos.class);
      Set<Long> set = DataCache.MIRROR_MAP_NPC.computeIfAbsent(pos.getMapId(), k -> new HashSet());
      set.add(mirror.getRId());
    } 
    Map<Long, Mirror> map = (Map<Long, Mirror>)list.stream().collect(Collectors.toMap(Mirror::getRId, mirror -> mirror));
    DataCache.MIRROR_MAP.putAll(map);
  }
  
  public Map<Long, CaRankRecord> initCaRank() {
    List<CaRankRecord> list = this.dao.query(CaRankRecord.class, (Condition)Cnd.NEW());
    return (Map<Long, CaRankRecord>)list.stream().collect(Collectors.toMap(CaRankRecord::getRId, caRankRecord -> caRankRecord));
  }
  
  @TargetDataSource(name = "se")
  public Object[] initTianFuSkill() {
    List<SysTianFuSkill> tianFuSkills = this.dao.query(SysTianFuSkill.class, (Condition)Cnd.NEW());
    Map<Integer, SysTianFuSkill> map = (Map<Integer, SysTianFuSkill>)tianFuSkills.stream().collect(Collectors.toMap(SysTianFuSkill::getCeSerial, sysTianFuSkill -> sysTianFuSkill));
    Map<Integer, SysTianFuSkill> opportunitys = (Map<Integer, SysTianFuSkill>)tianFuSkills.stream().filter(tianFuSkill -> (tianFuSkill.getOpportunity() != null && !"".equals(tianFuSkill.getOpportunity()))).collect(Collectors.toMap(SysTianFuSkill::getCeSerial, sysTianFuSkill -> sysTianFuSkill));
    Map<Integer, SysTianFuSkill> noOpportunitys = (Map<Integer, SysTianFuSkill>)tianFuSkills.stream().filter(tianFuSkill -> (tianFuSkill.getOpportunity() == null || "".equals(tianFuSkill.getOpportunity()))).collect(Collectors.toMap(SysTianFuSkill::getCeSerial, sysTianFuSkill -> sysTianFuSkill));
    Object[] objs = new Object[3];
    objs[0] = map;
    objs[1] = opportunitys;
    objs[2] = noOpportunitys;
    return objs;
  }
  
  @TargetDataSource(name = "se")
  public Map<Short, SysDayInCePractice> initDayInCePractice() {
    List<SysDayInCePractice> sysDayInCePractices = this.dao.query(SysDayInCePractice.class, (Condition)Cnd.NEW());
    Map<Short, SysDayInCePractice> map = (Map<Short, SysDayInCePractice>)sysDayInCePractices.stream().collect(Collectors.toMap(SysDayInCePractice::getLevel, sysDayInCePractice -> sysDayInCePractice));
    return map;
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataAcsrySkill> initAcsrySkill() {
    List<SysDataAcsrySkill> list = this.dao.query(SysDataAcsrySkill.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataAcsrySkill>)list.stream().collect(Collectors.toMap(SysDataAcsrySkill::getSerial, sysDataAcsrySkill -> sysDataAcsrySkill));
  }
  
  @TargetDataSource(name = "se")
  public Map<Integer, SysDataFurniture> initFurniture() {
    List<SysDataFurniture> list = this.dao.query(SysDataFurniture.class, (Condition)Cnd.NEW());
    return (Map<Integer, SysDataFurniture>)list.stream().collect(Collectors.toMap(SysDataFurniture::getSerial, sysDataFurniture -> sysDataFurniture));
  }
  
  @TargetDataSource(name = "se")
  public Map<String, Map<Byte, PetAccColor>> initPetAccColor() {
    List<SysPetAcsryColor> list = this.dao.query(SysPetAcsryColor.class, (Condition)Cnd.NEW());
    return (Map<String, Map<Byte, PetAccColor>>)list.stream().collect(Collectors.toMap(SysPetAcsryColor::getDict, sysPetAcsryColor -> sysPetAcsryColor.getValue()));
  }
  
  public CopyOnWriteArrayList initEmailSystem() {
    List<EmailSystem> emailSystems = this.dao.query(EmailSystem.class, (Condition)Cnd.where("recipient", "=", Integer.valueOf(0)).and("restrictions", "=", null)
        .and("expiration_time", ">", Long.valueOf((new Date()).getTime())).and("is_delete", "=", Integer.valueOf(0)).desc("expiration_time"));
    return new CopyOnWriteArrayList<>(emailSystems);
  }
  
  public List<Role> getRoles() {
    List<Role> roles = null;
    if (RankingList.roles.size() <= 0) {
      roles = this.dao.query(Role.class, (Condition)Cnd.NEW());
    } else {
      roles = RankingList.roles;
    } 
    return roles;
  }
  
  public List<Pet> getPets() {
    List<Pet> pets = null;
    if (RankingList.pets.size() <= 0) {
      pets = this.dao.query(Pet.class, (Condition)Cnd.NEW());
      RankingList.pets.addAll(pets);
      RankingList.petMaps.putAll((Map)pets.stream().collect(Collectors.groupingBy(Pet::getRoleId)));
    } else {
      pets = RankingList.pets;
    } 
    return pets;
  }
  
  public List<WearEquip> getWearEquips() {
    List<WearEquip> wearEquips = null;
    if (RankingList.wearEquips.size() <= 0) {
      wearEquips = this.dao.query(WearEquip.class, (Condition)Cnd.NEW());
      RankingList.wearEquips.addAll(wearEquips);
      RankingList.wearEquipMaps.putAll((Map)wearEquips.stream().collect(Collectors.groupingBy(WearEquip::getRId)));
    } else {
      wearEquips = RankingList.wearEquips;
    } 
    return wearEquips;
  }
}
