package com.kitty.system.game.fightnew.bean;

import org.slf4j.LoggerFactory;
import java.util.Iterator;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.mina.cache.SessionUtils;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.enums.war.wardef.RACE;
import java.util.stream.Collectors;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.common.utils.PositionUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Map;
import com.kitty.system.game.pet.domain.vo.AssistParam;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import org.nutz.lang.util.NutMap;
import org.slf4j.Logger;
import com.kitty.system.game.fightnew.FightObject;

public class PlayerFightObject extends FightObject
{
    private static final Logger log;
    private static final NutMap publicAutoSet;
    private Role role;
    private int index;
    private int customIndex;
    private List<AssistParam> assistParams;
    private DayInCeFight dayInCeFight;
    protected Map<Integer, Integer> abnormalRoundMap;
    private List<Integer> acsrySkillList;
    private NutMap acsryPropertyMap;
    private boolean isAnimationFinish;
    
    public PlayerFightObject(Role role, int index, int customIndex, boolean isA) {
        this.assistParams = new ArrayList<>();
        this.dayInCeFight = new DayInCeFight();
        this.abnormalRoundMap = new HashMap();
        this.acsrySkillList = new ArrayList<>();
        this.acsryPropertyMap = NutMap.NEW();
        this.isAnimationFinish = false;
        this.index = index;
        this.customIndex = customIndex;
        this.role = role;
        Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
        if (autoSet == null || autoSet.size() <= 0) {
            autoSet = PlayerFightObject.publicAutoSet;
        }
        this.setId(role.getRoleId());
        this.setHostId((int)role.getRoleId());
        this.setSerial((int)role.getSerial());
        this.setWeaponId(role.getWeaponIcon());
        this.setName(role.getName());
        this.setRelive((int)role.getRelive());
        this.setLevel((int)role.getLevel());
        this.setIcon(role.getIcon());
        this.setRace((int)role.getRace());
        this.setColor(role.getColor());
        this.setASide(isA);
        this.setLogic(LOGIC.LOGICTYPE_HERO.getValue());
        this.setPos((int)(short)PositionUtils.positions.get(index));
        this.setMaxLife((int)role.getMaxLife());
        this.setMaxMana((int)role.getMaxMana());
        this.setCurrMana((int)role.getCurrMana());
        this.setCurrLife((int)role.getCurrLife());
        this.setPhyPower(CountUtils.countPhyPower(role));
        this.setSpeed(CountUtils.countSpeed(role));
        this.setFightMsg(role.getExtendBox().getMsgMap());
        this.setFightPetId((int)role.getExtendBox().getCombatPet());
        this.yepFightPet.add(role.getExtendBox().getCombatPet());
        this.role = role;
        this.setAutoFight((int)role.getExtendBox().getAutoFight());
        List<RoleSkill> collect = (List)role.getSkills().values().stream().collect(Collectors.toList());
        this.setSkillIds(collect);
        int type = (int)Integer.valueOf(String.valueOf(autoSet.get("type")));
        int skillId = (int)Integer.valueOf(String.valueOf(autoSet.get("skillId")));
        int action = (int)Integer.valueOf(String.valueOf(autoSet.get("action")));
        this.setType(type);
        this.setSkillId(skillId);
        this.setAutoFightType(type);
        this.setAutoSkillId(skillId);
        this.setAction(action);
        if (autoSet.get("target") != null && !"".equals(autoSet.get("target"))) {
            String target = String.valueOf(autoSet.get("target"));
            this.setTargetId((int)Integer.valueOf(target));
            this.setAutoTarget((int)Integer.valueOf(target));
        }
        if ((int)role.getRace() == RACE.RACE_LONG.getValue()) {
            this.getSkillCoolMap().setv(String.valueOf(30041), Integer.valueOf(4));
            Object born = autoSet.get("born");
            if (born != null) {
                this.setBorn((int)autoSet.get("born"));
            }
            else {
                this.setBorn(1);
            }
        }
        this.setPropertyMap(CountUtils.countPro(role));
        List<SuitParam> suits = role.getExtendBox().getSuits();
        if (suits.size() > 0) {
            List<Integer> petIds = ((SuitParam)suits.get(0)).getPetIds();
            if (petIds != null && petIds.size() > 0) {
                this.assistParams = (List)petIds.stream().map(petId -> {
                    AssistParam assist = new AssistParam();
                    assist.setPetId(petId);
                    return assist;
                }).collect(Collectors.toList());
            }
        }
        List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            this.setGem(num);
        }
        int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            SysDataCard dataCard = (SysDataCard)CardDataPool.cardMap.get(Integer.valueOf(role.getMasquerade()));
            if (dataCard != null && dataCard.getMorph() > 0) {
                this.setIcon(dataCard.getMorph());
            }
        }
        this.dayInCeFight = this.getDayInCeFightValues();
        this.acsrySkillList = AcsrySkillUtil.getAcsrySkillList(role);
    }
    
    @Override
    public RoleSkill getSkillBy(Integer skillId) {
        return (RoleSkill)this.getRole().getSkills().get(skillId);
    }
    
    @Override
    public IoSession getSession() {
        return SessionUtils.getSessionBy((long)this.getRole().getId());
    }
    
    private DayInCeFight getDayInCeFightValues() {
        RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(this.role.getId());
        Map<Long, RoleTianFu> roleTianFus = (Map)(DataCache.ROLE_TIAN_FU.get(this.role.getId())).stream().collect(Collectors.toMap(RoleTianFu::getId, RoleTianFu -> RoleTianFu));
        List<Long> tianFuSerials = TianFuUtils.getAllGridValues(roleDayInCe);
        List<TianFuObject> tianFuList = new ArrayList<>();
        for (Long id : tianFuSerials) {
            RoleTianFu roleTianFu = (RoleTianFu)roleTianFus.get(id);
            if (roleTianFu == null) {
                continue;
            }
            if (roleTianFu.getIsWear() != 1) {
                continue;
            }
            TianFuObject tianFuObject = new TianFuObject();
            tianFuObject.setRoleTianFuId((long)roleTianFu.getId());
            tianFuObject.setCeSerial(roleTianFu.getTianFuSerial());
            tianFuObject.setLevel(roleTianFu.getLevel());
            tianFuObject.setProductOrder(roleTianFu.getProductOrder());
            if (roleTianFu.getTriggerType() == null) {
                SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)SkillDataPool.tianFuSkillMap.get(Integer.valueOf(roleTianFu.getTianFuSerial()));
                if (sysTianFuSkill != null) {
                    tianFuObject.setTriggerType(sysTianFuSkill.getOpportunity());
                }
            }
            else {
                tianFuObject.setTriggerType(roleTianFu.getTriggerType());
            }
            tianFuList.add(tianFuObject);
        }
        DayInCeFight dayInCeFight = new DayInCeFight();
        dayInCeFight.setLevel(roleDayInCe.getLevel());
        dayInCeFight.setTianFuList(tianFuList);
        return dayInCeFight;
    }
    
    public void instalAbnormalRoundMap(Fight fight) {
        if (this.getStatusValue(1002) > 0) {
            this.getAbnormalRoundMap().put(Integer.valueOf(fight.getRound()), Integer.valueOf(1002));
        }
        else if (this.getStatusValue(1003) > 0) {
            this.getAbnormalRoundMap().put(Integer.valueOf(fight.getRound()), Integer.valueOf(1003));
        }
        else if (this.getStatusValue(1004) > 0) {
            this.getAbnormalRoundMap().put(Integer.valueOf(fight.getRound()), Integer.valueOf(1004));
        }
        else if (this.getStatusValue(1006) > 0) {
            this.getAbnormalRoundMap().put(Integer.valueOf(fight.getRound()), Integer.valueOf(1006));
        }
    }
    
    public List<Integer> getAcsrySkillList() {
        return this.acsrySkillList;
    }
    
    public void setAcsrySkillList(List<Integer> acsrySkillList) {
        this.acsrySkillList = acsrySkillList;
    }
    
    public NutMap getAcsryPropertyMap() {
        return this.acsryPropertyMap;
    }
    
    public void setAcsryPropertyMap(NutMap acsryPropertyMap) {
        this.acsryPropertyMap = acsryPropertyMap;
    }
    
    @Override
    public double getSelfAllTempPro(int dist) {
        double num = super.getSelfAllTempPro(dist);
        String distStr = String.valueOf(dist);
        int round = this.getAcsryPropertyMap().getInt(distStr, 0);
        if (round != 0) {
            double value = this.getAcsryPropertyMap().getDouble(distStr + "value", 0.0);
            num += value;
        }
        return num;
    }
    
    @Override
    public Role getRole() {
        return this.role;
    }
    
    public int getIndex() {
        return this.index;
    }
    
    public int getCustomIndex() {
        return this.customIndex;
    }
    
    public List<AssistParam> getAssistParams() {
        return this.assistParams;
    }
    
    public DayInCeFight getDayInCeFight() {
        return this.dayInCeFight;
    }
    
    public Map<Integer, Integer> getAbnormalRoundMap() {
        return this.abnormalRoundMap;
    }
    
    public void setAnimationFinish(final boolean isAnimationFinish) {
        this.isAnimationFinish = isAnimationFinish;
    }
    
    public boolean isAnimationFinish() {
        return this.isAnimationFinish;
    }
    
    static {
        log = LoggerFactory.getLogger(PlayerFightObject.class);
        (publicAutoSet = NutMap.NEW()).setv("type", Integer.valueOf(0));
        PlayerFightObject.publicAutoSet.setv("action", Integer.valueOf(1));
        PlayerFightObject.publicAutoSet.setv("skillId", Integer.valueOf(0));
        PlayerFightObject.publicAutoSet.setv("born", Integer.valueOf(0));
    }
}
