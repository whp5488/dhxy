
package com.kitty.system.game.fightnew.bean;

import org.slf4j.LoggerFactory;
import java.util.Iterator;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import java.util.function.Function;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.mina.cache.SessionUtils;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.fightnew.book.acsry.utils.AcsrySkillUtil;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.pet.domain.vo.SuitParam;
import com.kitty.system.enums.war.wardef.RACE;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.skill.domain.RoleSkill;
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
    
    public PlayerFightObject(final Role role, final int index, final int customIndex, final boolean isA) {
        this.assistParams = new ArrayList<AssistParam>();
        this.dayInCeFight = new DayInCeFight();
        this.abnormalRoundMap = new HashMap<Integer, Integer>();
        this.acsrySkillList = new ArrayList<Integer>();
        this.acsryPropertyMap = NutMap.NEW();
        this.isAnimationFinish = false;
        this.index = index;
        this.customIndex = customIndex;
        this.role = role;
        Map<String, Object> autoSet = role.getExtendBox().getAutoFightSet();
        if (autoSet == null || autoSet.size() <= 0) {
            autoSet = (Map<String, Object>)PlayerFightObject.publicAutoSet;
        }
        this.setId(role.getRoleId());
        this.setHostId(role.getRoleId());
        this.setSerial(role.getSerial());
        this.setWeaponId(role.getWeaponIcon());
        this.setName(role.getName());
        this.setRelive(role.getRelive());
        this.setLevel(role.getLevel());
        this.setIcon(role.getIcon());
        this.setRace(role.getRace());
        this.setColor(role.getColor());
        this.setASide(isA);
        this.setLogic(LOGIC.LOGICTYPE_HERO.getValue());
        this.setPos(PositionUtils.positions.get(index));
        this.setMaxLife(role.getMaxLife());
        this.setMaxMana(role.getMaxMana());
        this.setCurrMana(role.getCurrMana());
        this.setCurrLife(role.getCurrLife());
        this.setPhyPower(CountUtils.countPhyPower(role));
        this.setSpeed(CountUtils.countSpeed(role));
        this.setFightMsg(role.getExtendBox().getMsgMap());
        this.setFightPetId(role.getExtendBox().getCombatPet());
        this.yepFightPet.add(role.getExtendBox().getCombatPet());
        this.role = role;
        this.setAutoFight(role.getExtendBox().getAutoFight());
        final List<RoleSkill> collect = role.getSkills().values().stream().collect(Collectors.toList());
        this.setSkillIds(collect);
        final int type = Integer.valueOf(String.valueOf(autoSet.get("type")));
        final int skillId = Integer.valueOf(String.valueOf(autoSet.get("skillId")));
        final int action = Integer.valueOf(String.valueOf(autoSet.get("action")));
        this.setType(type);
        this.setSkillId(skillId);
        this.setAutoFightType(type);
        this.setAutoSkillId(skillId);
        this.setAction(action);
        if (autoSet.get("target") != null && !"".equals(autoSet.get("target"))) {
            final String target = String.valueOf(autoSet.get("target"));
            this.setTargetId(Integer.valueOf(target));
            this.setAutoTarget(Integer.valueOf(target));
        }
        if (role.getRace() == RACE.RACE_LONG.getValue()) {
            this.getSkillCoolMap().setv(String.valueOf(30041), 4);
            final Object born = autoSet.get("born");
            if (born != null) {
                this.setBorn((Integer) autoSet.get("born"));
            }
            else {
                this.setBorn(1);
            }
        }
        this.setPropertyMap(CountUtils.countPro(role));
        final List<SuitParam> suits = role.getExtendBox().getSuits();
        if (suits.size() > 0) {
            final List<Integer> petIds = suits.get(0).getPetIds();
            if (petIds != null && petIds.size() > 0) {
                this.assistParams = petIds.stream().map(petId -> {
                    final AssistParam assist = new AssistParam();
                    assist.setPetId(petId);
                    return assist;
                }).collect(Collectors.toList());
            }
        }
        final List<WearEquip> wearEquips = SpringUtils.getEquipService().getEquipValid(role, false);
        if (wearEquips.size() > 0) {
            final int num = DataGlobalFunc.getWeaponGemNum(wearEquips);
            this.setGem(num);
        }
        final int currTime = new Long(System.currentTimeMillis() / 1000L).intValue();
        if (role.getMasquerade() > 0 && role.getCardExpireTime() > currTime) {
            final SysDataCard dataCard = CardDataPool.cardMap.get(role.getMasquerade());
            if (dataCard != null && dataCard.getMorph() > 0) {
                this.setIcon(dataCard.getMorph());
            }
        }
        this.dayInCeFight = this.getDayInCeFightValues();
        this.acsrySkillList = AcsrySkillUtil.getAcsrySkillList(role);
    }
    
    @Override
    public RoleSkill getSkillBy(final Integer skillId) {
        return this.getRole().getSkills().get(skillId);
    }
    
    @Override
    public IoSession getSession() {
        return SessionUtils.getSessionBy(this.getRole().getId());
    }
    
    private DayInCeFight getDayInCeFightValues() {
        final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(this.role.getId());
        final Map<Long, RoleTianFu> roleTianFus = (Map<Long, RoleTianFu>)DataCache.ROLE_TIAN_FU.get(this.role.getId()).stream().collect(Collectors.toMap(RoleTianFu::getId, RoleTianFu -> RoleTianFu));
        final List<Long> tianFuSerials = TianFuUtils.getAllGridValues(roleDayInCe);
        final List<TianFuObject> tianFuList = new ArrayList<TianFuObject>();
        for (final Long id : tianFuSerials) {
            final RoleTianFu roleTianFu = roleTianFus.get(id);
            if (roleTianFu != null) {
                if (roleTianFu.getIsWear() != 1) {
                    continue;
                }
                final TianFuObject tianFuObject = new TianFuObject();
                tianFuObject.setRoleTianFuId(roleTianFu.getId());
                tianFuObject.setCeSerial(roleTianFu.getTianFuSerial());
                tianFuObject.setLevel(roleTianFu.getLevel());
                tianFuObject.setProductOrder(roleTianFu.getProductOrder());
                if (roleTianFu.getTriggerType() == null) {
                    final SysTianFuSkill sysTianFuSkill = SkillDataPool.tianFuSkillMap.get(roleTianFu.getTianFuSerial());
                    if (sysTianFuSkill != null) {
                        tianFuObject.setTriggerType(sysTianFuSkill.getOpportunity());
                    }
                }
                else {
                    tianFuObject.setTriggerType(roleTianFu.getTriggerType());
                }
                tianFuList.add(tianFuObject);
            }
        }
        final DayInCeFight dayInCeFight = new DayInCeFight();
        dayInCeFight.setLevel(roleDayInCe.getLevel());
        dayInCeFight.setTianFuList(tianFuList);
        return dayInCeFight;
    }
    
    public void instalAbnormalRoundMap(final Fight fight) {
        if (this.getStatusValue(1002) > 0) {
            this.getAbnormalRoundMap().put(fight.getRound(), 1002);
        }
        else if (this.getStatusValue(1003) > 0) {
            this.getAbnormalRoundMap().put(fight.getRound(), 1003);
        }
        else if (this.getStatusValue(1004) > 0) {
            this.getAbnormalRoundMap().put(fight.getRound(), 1004);
        }
        else if (this.getStatusValue(1006) > 0) {
            this.getAbnormalRoundMap().put(fight.getRound(), 1006);
        }
    }
    
    public List<Integer> getAcsrySkillList() {
        return this.acsrySkillList;
    }
    
    public void setAcsrySkillList(final List<Integer> acsrySkillList) {
        this.acsrySkillList = acsrySkillList;
    }
    
    public NutMap getAcsryPropertyMap() {
        return this.acsryPropertyMap;
    }
    
    public void setAcsryPropertyMap(final NutMap acsryPropertyMap) {
        this.acsryPropertyMap = acsryPropertyMap;
    }
    
    @Override
    public double getSelfAllTempPro(final int dist) {
        double num = super.getSelfAllTempPro(dist);
        final String distStr = String.valueOf(dist);
        final int round = this.getAcsryPropertyMap().getInt(distStr, 0);
        if (round != 0) {
            final double value = this.getAcsryPropertyMap().getDouble(distStr + "value", 0.0);
            num += value;
            if (round == -1) {}
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
        (publicAutoSet = NutMap.NEW()).setv("type", 0);
        PlayerFightObject.publicAutoSet.setv("action", (Object)1);
        PlayerFightObject.publicAutoSet.setv("skillId", 0);
        PlayerFightObject.publicAutoSet.setv("born", 0);
    }
}
