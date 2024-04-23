package com.kitty.system.game.player.bean;

import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.game.vo.TitleInfo;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.nutz.lang.util.NutMap;

public class PlayerExtendBox implements Serializable {
  private int star = 0;
  
  private int buyPetGrid = 0;
  
  private Integer position = Integer.valueOf(0);
  
  private Integer storePosition = Integer.valueOf(0);
  
  private int petEquipPosition = 0;
  
  private Integer combatPet = Integer.valueOf(0);
  
  private Long guoScore = Long.valueOf(0L);
  
  private Long bracScore = Long.valueOf(0L);
  
  private Long naoScore = Long.valueOf(0L);
  
  private Long diScore = Long.valueOf(0L);
  
  private Long arenaScore = Long.valueOf(0L);
  
  private Long longScore = Long.valueOf(0L);
  
  private Long niceScore = Long.valueOf(0L);
  
  private Long helpScore = Long.valueOf(0L);
  
  private Long featsScore = Long.valueOf(0L);
  
  private Long chargeScore = Long.valueOf(0L);
  
  private Long boundGold = Long.valueOf(0L);
  
  private Long achievementScore = Long.valueOf(0L);
  
  private int banTrade = 0;
  
  private int stall = 8;
  
  private byte drill = 0;
  
  private byte fkp = 0;
  
  private byte sc = 0;
  
  @JsonIgnore
  private Map<String, Integer> setMap = new HashMap<>();
  
  private byte autoFight = 0;
  
  @JsonIgnore
  private NutMap newDistrict = new NutMap();
  
  @JsonIgnore
  private NutMap points = new NutMap();
  
  @JsonIgnore
  private Map<String, Integer> autoAttrib = new HashMap<>();
  
  @JsonIgnore
  private NutMap params = new NutMap();
  
  @JsonIgnore
  private NutMap state = new NutMap();
  
  @JsonIgnore
  private Map<String, Object> autoFightSet = new HashMap<>();
  
  @JsonIgnore
  private Map<Integer, Map<Integer, Integer>> quotaLimit = new HashMap<>();
  
  private int resetQuotaTime = TimeUtil.dayInMillis(TimeUtil.getWeekday(2));
  
  @JsonIgnore
  private NutMap dayQuotaLimit = new NutMap();
  
  @JsonIgnore
  private ConcurrentHashMap<Integer, TitleInfo> titleMap = new ConcurrentHashMap<>();
  
  private String signDay;
  
  private String signbuSign;
  
  private int signSize;
  
  private String newcomer;
  
  @JsonIgnore
  private ArrayList<Integer> totalContent = new ArrayList<>();
  
  @JsonIgnore
  private List<NutMap> msgMap = new ArrayList<>();
  
  private List<SuitParam> suits = new ArrayList<>();
  
  public int getStar() {
    return this.star;
  }
  
  public void setStar(int star) {
    this.star = star;
  }
  
  public int getBuyPetGrid() {
    return this.buyPetGrid;
  }
  
  public void setBuyPetGrid(int buyPetGrid) {
    this.buyPetGrid = buyPetGrid;
  }
  
  public Integer getPosition() {
    return this.position;
  }
  
  public void setPosition(Integer position) {
    this.position = position;
  }
  
  public Integer getStorePosition() {
    return this.storePosition;
  }
  
  public void setStorePosition(Integer storePosition) {
    this.storePosition = storePosition;
  }
  
  public Integer getPetEquipPosition() {
    return Integer.valueOf(this.petEquipPosition);
  }
  
  public void setPetEquipPosition(Integer petEquipPosition) {
    this.petEquipPosition = petEquipPosition.intValue();
  }
  
  public Integer getCombatPet() {
    return this.combatPet;
  }
  
  public void setCombatPet(Integer combatPet) {
    this.combatPet = combatPet;
  }
  
  public Long getGuoScore() {
    return this.guoScore;
  }
  
  public void setGuoScore(Long guoScore) {
    this.guoScore = guoScore;
  }
  
  public Long getBracScore() {
    return this.bracScore;
  }
  
  public void setBracScore(Long bracScore) {
    this.bracScore = bracScore;
  }
  
  public Long getNaoScore() {
    return this.naoScore;
  }
  
  public void setNaoScore(Long naoScore) {
    this.naoScore = naoScore;
  }
  
  public Long getDiScore() {
    return this.diScore;
  }
  
  public void setDiScore(Long diScore) {
    this.diScore = diScore;
  }
  
  public Long getArenaScore() {
    return this.arenaScore;
  }
  
  public void setArenaScore(Long arenaScore) {
    this.arenaScore = arenaScore;
  }
  
  public Long getLongScore() {
    return this.longScore;
  }
  
  public void setLongScore(Long longScore) {
    this.longScore = longScore;
  }
  
  public Long getNiceScore() {
    return this.niceScore;
  }
  
  public void setNiceScore(Long niceScore) {
    this.niceScore = niceScore;
  }
  
  public Long getHelpScore() {
    return this.helpScore;
  }
  
  public void setHelpScore(Long helpScore) {
    this.helpScore = helpScore;
  }
  
  public Long getFeatsScore() {
    return this.featsScore;
  }
  
  public void setFeatsScore(Long featsScore) {
    this.featsScore = featsScore;
  }
  
  public Long getChargeScore() {
    return this.chargeScore;
  }
  
  public void setChargeScore(Long chargeScore) {
    this.chargeScore = chargeScore;
  }
  
  public Long getBoundGold() {
    return this.boundGold;
  }
  
  public void setBoundGold(Long boundGold) {
    this.boundGold = boundGold;
  }
  
  public Long getAchievementScore() {
    return this.achievementScore;
  }
  
  public void setAchievementScore(Long achievementScore) {
    this.achievementScore = achievementScore;
  }
  
  public int getBanTrade() {
    return this.banTrade;
  }
  
  public void setBanTrade(int banTrade) {
    this.banTrade = banTrade;
  }
  
  public int getStall() {
    return this.stall;
  }
  
  public void setStall(int stall) {
    this.stall = stall;
  }
  
  public Map<String, Integer> getSetMap() {
    return this.setMap;
  }
  
  public void setSetMap(Map<String, Integer> setMap) {
    this.setMap = setMap;
  }
  
  public byte getAutoFight() {
    return this.autoFight;
  }
  
  public void setAutoFight(byte autoFight) {
    this.autoFight = autoFight;
  }
  
  public Map<String, Integer> getAutoAttrib() {
    return this.autoAttrib;
  }
  
  public void setAutoAttrib(Map<String, Integer> autoAttrib) {
    this.autoAttrib = autoAttrib;
  }
  
  public NutMap getParams() {
    return this.params;
  }
  
  public void setParams(NutMap params) {
    this.params = params;
  }
  
  public Map<String, Object> getAutoFightSet() {
    return this.autoFightSet;
  }
  
  public void setAutoFightSet(Map<String, Object> autoFightSet) {
    this.autoFightSet = autoFightSet;
  }
  
  public Map<Integer, Map<Integer, Integer>> getQuotaLimit() {
    return this.quotaLimit;
  }
  
  public void setQuotaLimit(Map<Integer, Map<Integer, Integer>> quotaLimit) {
    this.quotaLimit = quotaLimit;
  }
  
  public int getResetQuotaTime() {
    return this.resetQuotaTime;
  }
  
  public void setResetQuotaTime(int resetQuotaTime) {
    this.resetQuotaTime = resetQuotaTime;
  }
  
  public ConcurrentHashMap<Integer, TitleInfo> getTitleMap() {
    return this.titleMap;
  }
  
  public void setTitleMap(ConcurrentHashMap<Integer, TitleInfo> titleMap) {
    this.titleMap = titleMap;
  }
  
  public String getSignDay() {
    return this.signDay;
  }
  
  public void setSignDay(String signDay) {
    this.signDay = signDay;
  }
  
  public String getSignbuSign() {
    return this.signbuSign;
  }
  
  public void setSignbuSign(String signbuSign) {
    this.signbuSign = signbuSign;
  }
  
  public int getSignSize() {
    return this.signSize;
  }
  
  public void setSignSize(int signSize) {
    this.signSize = signSize;
  }
  
  public String getNewcomer() {
    return this.newcomer;
  }
  
  public void setNewcomer(String newcomer) {
    this.newcomer = newcomer;
  }
  
  public ArrayList<Integer> getTotalContent() {
    return this.totalContent;
  }
  
  public void setTotalContent(ArrayList<Integer> totalContent) {
    this.totalContent = totalContent;
  }
  
  public List<NutMap> getMsgMap() {
    return this.msgMap;
  }
  
  public void setMsgMap(List<NutMap> msgMap) {
    this.msgMap = msgMap;
  }
  
  public byte getDrill() {
    return this.drill;
  }
  
  public void setDrill(byte drill) {
    this.drill = drill;
  }
  
  public byte getFkp() {
    return this.fkp;
  }
  
  public void setFkp(byte fkp) {
    this.fkp = fkp;
  }
  
  public List<SuitParam> getSuits() {
    return this.suits;
  }
  
  public void setSuits(List<SuitParam> suits) {
    this.suits = suits;
  }
  
  public NutMap getDayQuotaLimit() {
    return this.dayQuotaLimit;
  }
  
  public void setDayQuotaLimit(NutMap dayQuotaLimit) {
    this.dayQuotaLimit = dayQuotaLimit;
  }
  
  public NutMap getPoints() {
    return this.points;
  }
  
  public void setPoints(NutMap points) {
    this.points = points;
  }
  
  public byte getSc() {
    return this.sc;
  }
  
  public void setSc(byte sc) {
    this.sc = sc;
  }
  
  public NutMap getNewDistrict() {
    return this.newDistrict;
  }
  
  public void setNewDistrict(NutMap newDistrict) {
    this.newDistrict = newDistrict;
  }
  
  public NutMap getState() {
    return this.state;
  }
  
  public void setState(NutMap state) {
    this.state = state;
  }
}
