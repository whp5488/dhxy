package com.kitty.system.game.player.bean;

import com.kitty.common.utils.TimeUtil;
import java.util.HashMap;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import java.util.List;
import java.util.ArrayList;
import com.kitty.system.game.vo.TitleInfo;
import java.util.concurrent.ConcurrentHashMap;
import org.nutz.lang.util.NutMap;
import org.codehaus.jackson.annotate.JsonIgnore;
import java.util.Map;
import java.io.Serializable;

public class PlayerExtendBox implements Serializable
{
    private int star;
    private int buyPetGrid;
    private Integer position;
    private Integer storePosition;
    private int petEquipPosition;
    private Integer combatPet;
    private Long guoScore;
    private Long bracScore;
    private Long naoScore;
    private Long diScore;
    private Long arenaScore;
    private Long longScore;
    private Long niceScore;
    private Long helpScore;
    private Long featsScore;
    private Long chargeScore;
    private Long boundGold;
    private Long achievementScore;
    private int banTrade;
    private int stall;
    private byte drill;
    private byte fkp;
    private byte sc;
    @JsonIgnore
    private Map<String, Integer> setMap;
    private byte autoFight;
    @JsonIgnore
    private NutMap newDistrict;
    @JsonIgnore
    private NutMap points;
    @JsonIgnore
    private Map<String, Integer> autoAttrib;
    @JsonIgnore
    private NutMap params;
    @JsonIgnore
    private NutMap state;
    @JsonIgnore
    private Map<String, Object> autoFightSet;
    @JsonIgnore
    private Map<Integer, Map<Integer, Integer>> quotaLimit;
    private int resetQuotaTime;
    @JsonIgnore
    private NutMap dayQuotaLimit;
    @JsonIgnore
    private ConcurrentHashMap<Integer, TitleInfo> titleMap;
    private String signDay;
    private String signbuSign;
    private int signSize;
    private String newcomer;
    @JsonIgnore
    private ArrayList<Integer> totalContent;
    @JsonIgnore
    private List<NutMap> msgMap;
    private List<SuitParam> suits;
    
    public PlayerExtendBox() {
        this.star = 0;
        this.buyPetGrid = 0;
        this.position = Integer.valueOf(0);
        this.storePosition = Integer.valueOf(0);
        this.petEquipPosition = 0;
        this.combatPet = Integer.valueOf(0);
        this.guoScore = Long.valueOf(0L);
        this.bracScore = Long.valueOf(0L);
        this.naoScore = Long.valueOf(0L);
        this.diScore = Long.valueOf(0L);
        this.arenaScore = Long.valueOf(0L);
        this.longScore = Long.valueOf(0L);
        this.niceScore = Long.valueOf(0L);
        this.helpScore = Long.valueOf(0L);
        this.featsScore = Long.valueOf(0L);
        this.chargeScore = Long.valueOf(0L);
        this.boundGold = Long.valueOf(0L);
        this.achievementScore = Long.valueOf(0L);
        this.banTrade = 0;
        this.stall = 8;
        this.drill = 0;
        this.fkp = 0;
        this.sc = 0;
        this.setMap = new HashMap();
        this.autoFight = 0;
        this.newDistrict = new NutMap();
        this.points = new NutMap();
        this.autoAttrib = new HashMap();
        this.params = new NutMap();
        this.state = new NutMap();
        this.autoFightSet = new HashMap();
        this.quotaLimit = new HashMap();
        this.resetQuotaTime = TimeUtil.dayInMillis(TimeUtil.getWeekday(2));
        this.dayQuotaLimit = new NutMap();
        this.titleMap = new ConcurrentHashMap();
        this.totalContent = new ArrayList<>();
        this.msgMap = new ArrayList<>();
        this.suits = new ArrayList<>();
    }
    
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
        this.petEquipPosition = (int)petEquipPosition;
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
