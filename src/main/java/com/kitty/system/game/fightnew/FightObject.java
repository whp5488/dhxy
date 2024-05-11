
package com.kitty.system.game.fightnew;

import java.beans.ConstructorProperties;
import com.kitty.system.game.fightnew.bean.Fight;
import java.util.HashMap;
import com.kitty.common.utils.BigDecimalUtil;
import java.util.Collection;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.equip.domain.Effect;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.enums.war.warskill.skilldef.EFFECTTYPE;
import com.kitty.system.game.fightnew.bean.PetMirrorFightObject;
import com.kitty.system.game.fightnew.bean.MonsterFightObject;
import com.kitty.system.game.fightnew.bean.PartnerFightObject;
import com.kitty.system.game.fightnew.bean.PlayerFightObject;
import com.kitty.system.game.fightnew.bean.PetFightObject;
import com.kitty.system.game.fightnew.imbue.utils.ImbueUtil;
import java.util.Iterator;
import java.util.ArrayList;
import com.kitty.system.game.fightnew.messages.vo.SkillCool;
import java.util.Optional;
import com.kitty.system.game.fightnew.imbue.bean.Imbue;
import java.util.Map;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.List;
import org.nutz.lang.util.NutMap;

public class FightObject implements Comparable<FightObject>
{
    protected Integer id;
    protected int serial;
    protected String name;
    protected int level;
    protected int relive;
    protected int pos;
    protected int race;
    protected int icon;
    protected String color;
    protected NutMap colorMap;
    protected int currLife;
    protected int maxLife;
    protected int weaponId;
    protected int gem;
    protected int currMana;
    protected int maxMana;
    protected int speed;
    protected int phyPower;
    protected boolean doAction;
    protected int hostId;
    protected int fightPetId;
    protected int logic;
    protected List<RoleSkill> skillIds;
    protected int targetId;
    protected int type;
    protected int skillId;
    protected int AutoFight;
    protected int born;
    protected int morph;
    protected NutMap skillCoolMap;
    protected NutMap statusMap;
    protected NutMap propertyMap;
    protected NutMap statusSubMap;
    protected Map<Integer, Imbue> imbueMap;
    protected long close;
    protected int action;
    protected boolean isASide;
    protected int autoFightType;
    protected int autoSkillId;
    protected int autoTarget;
    protected int comeRound;
    protected int dieRound;
    protected int reviveSize;
    protected List<NutMap> fightMsg;
    protected List<Integer> yepFightPet;
    private NutMap fightActionRecord;
    private NutMap beAttackedMap;
    private NutMap otherMap;
    private Map<Integer, Boolean> isCompleted;
    protected Map<String, Object> tempCache;
    private NutMap roundController;
    private Map<Integer, List<Integer>> roundUseSkillMap;
    
    public void setType(final int type) {
        this.type = type;
    }
    
    public Object getTempCacheValue(final String key) {
        if (this.tempCache != null) {
            return this.tempCache.get(key);
        }
        return null;
    }
    
    public RoleSkill getSkillBy(final Integer skillId) {
        final Optional<RoleSkill> optional = this.getSkillIds().stream().filter(skill -> skill.getSkillId().equals(skillId)).findFirst();
        if (optional.isPresent()) {
            return optional.get();
        }
        return null;
    }
    
    public void setCurrLife(final int currLife) {
        if (currLife > this.maxLife) {
            this.currLife = this.maxLife;
        }
        else {
            this.currLife = Math.max(currLife, 0);
        }
    }
    
    public int setCurrMana(final int currMana) {
        if (currMana > this.maxMana) {
            this.currMana = this.maxMana;
        }
        else {
            this.currMana = Math.max(currMana, 0);
        }
        return this.currMana;
    }
    
    public int reduceMp(final int mp) {
        this.currMana -= mp;
        if (this.currMana < 0) {
            this.currMana = 0;
        }
        return this.currMana;
    }
    
    public int reduceHp(final int hp) {
        this.currLife -= hp;
        if (this.currLife < 0) {
            this.currLife = 0;
        }
        return this.currLife;
    }
    
    public boolean isFallDown() {
        return this.currLife <= 0;
    }
    
    public boolean isCompleted(final int round, final boolean bo) {
        final boolean boo = this.isCompleted.getOrDefault(round, false);
        if (!boo && bo) {
            this.isCompleted.put(round, true);
        }
        return boo;
    }
    
    public NutMap getStatusMap() {
        if (this.statusMap == null) {
            this.setStatusMap(NutMap.NEW());
        }
        return this.statusMap;
    }
    
    public void setStatus(final int status, final int value) {
        this.getStatusMap().setv(String.valueOf(status), (Object)value);
    }
    
    public void removeStatus(final int status) {
        this.getStatusMap().unset(String.valueOf(status));
        this.getStatusMap().unset(status + "value");
        this.getStatusMap().unset(status + "record");
        this.getStatusMap().unset(status + "odds");
        this.getStatusMap().unset(status + "perpetrator");
        this.getStatusMap().unset(status + "statusConstraint");
        this.removeStatusSub(status + "target");
    }
    
    public void removeSkillCool(final String dist) {
        this.getSkillCoolMap().unset(dist);
        this.getSkillCoolMap().unset(dist + "value");
        this.getSkillCoolMap().unset(dist + "value_targets");
    }
    
    public void removeSkillCoolOther(final String dist) {
        this.getSkillCoolMap().unset(dist + "value");
        this.getSkillCoolMap().unset(dist + "value_targets");
    }
    
    public List<SkillCool> getSkillCoolList() {
        final List<SkillCool> list = new ArrayList<SkillCool>();
        for (final Map.Entry<String, Object> entry : this.getSkillCoolMap().entrySet()) {
            if (!entry.getKey().matches("^[0-9]+$")) {
                continue;
            }
            final SkillCool cool = new SkillCool();
            cool.setObjId(this.getId());
            cool.setSkillId(Integer.parseInt(entry.getKey()));
            cool.setRound((Integer)entry.getValue());
            list.add(cool);
        }
        return list;
    }
    
    public void reduceSkillCool() {
        final List<String> keys = new ArrayList<String>();
        final List<String> removeSkillCoolOther = new ArrayList<String>();
        for (final Map.Entry<String, Object> entry : this.getSkillCoolMap().entrySet()) {
            if (!(entry.getValue() instanceof Integer)) {
                continue;
            }
            int value = (Integer) entry.getValue();
            if (value <= 0) {
                keys.add(entry.getKey());
            }
            --value;
            this.skillCoolMap.setv((String)entry.getKey(), (Object)value);
            if (value != 0) {
                continue;
            }
            removeSkillCoolOther.add(entry.getKey());
        }
        for (final String key : keys) {
            this.removeSkillCool(key);
        }
        for (final String keyOther : removeSkillCoolOther) {
            this.removeSkillCoolOther(keyOther);
        }
    }
    
    public void reduceImbue() {
        ImbueUtil.reduceImbueRounds(this.imbueMap);
    }
    
    public void removeStatusSub(final String dict) {
        this.statusSubMap.unset(dict);
    }
    
    public int getStatusValue(final int status) {
        final String key = String.valueOf(status);
        return this.getStatusMap().getInt(key, 0);
    }
    
    public double getSelfAllTempPro(final int dist) {
        final String pro_dist = String.valueOf(dist);
        double sum = 0.0;
        sum += this.propertyMap.getDouble(pro_dist);
        sum += ImbueUtil.getProValue(this.imbueMap, dist);
        return sum;
    }
    
    public boolean isPet() {
        return this instanceof PetFightObject;
    }
    
    public boolean isPlayer() {
        return this instanceof PlayerFightObject;
    }
    
    public boolean isPartner() {
        return this instanceof PartnerFightObject;
    }
    
    public boolean isMonster() {
        return this instanceof MonsterFightObject;
    }
    
    public boolean isPetMirror() {
        return this instanceof PetMirrorFightObject;
    }
    
    public Boolean RoleIsDamgeImmunity() {
        return this.getStatusValue(EFFECTTYPE.EFFECTTYPE_TONGCHOUDIKAI.getValue()) >= 1 || this.getStatusValue(EFFECTTYPE.EFFECTTYPE_MONSTER_WUDI.getValue()) >= 1;
    }
    
    public boolean isFrozen() {
        return this.getStatusValue(1003) >= 1;
    }
    
    public boolean isPoison() {
        return this.getStatusValue(1005) >= 1;
    }
    
    public boolean isConfusion() {
        return this.getStatusValue(1002) >= 1;
    }
    
    public boolean isSleep() {
        return this.getStatusValue(1004) >= 1;
    }
    
    public boolean isForgotten() {
        return this.getStatusValue(1006) >= 1;
    }
    
    public boolean inDeBuffStatus() {
        return this.isFrozen() || this.isPoison() || this.isConfusion() || this.isSleep() || this.isForgotten();
    }
    
    public Role getRole() {
        return null;
    }
    
    public Pet getPet() {
        return null;
    }
    
    public void clearAssistBuff() {
        this.removeStatus(1010);
        this.removeStatus(1008);
        this.removeStatus(17);
        this.removeStatus(1009);
        this.removeStatus(1022);
    }
    
    public boolean hasAssistBuff() {
        return this.getStatusValue(1010) > 0 || this.getStatusValue(1008) > 0 || this.getStatusValue(17) > 0 || this.getStatusValue(1009) > 0 || this.getStatusValue(1022) > 0;
    }
    
    public void clearObstacleBuff() {
        this.removeStatus(1005);
        this.removeStatus(1004);
        this.removeStatus(1006);
        this.removeStatus(1003);
        this.removeStatus(1002);
    }
    
    public IoSession getSession() {
        return null;
    }
    
    public void clearAnomalousStatus() {
        this.removeStatus(1002);
        this.removeStatus(1003);
        this.removeStatus(1004);
        this.removeStatus(1006);
        this.removeStatus(1019);
    }
    
    public void clearStatus(final List<Integer> statusList) {
        if (statusList != null && statusList.size() > 0) {
            for (final Integer status : statusList) {
                this.removeStatus(status);
            }
        }
    }
    
    public List<Integer> getStatusList(final List<Integer> sourceStatusList) {
        final List<Integer> returnList = new ArrayList<Integer>();
        if (sourceStatusList != null && sourceStatusList.size() > 0) {
            for (final Integer status : sourceStatusList) {
                if (this.getStatusValue(status) > 0) {
                    returnList.add(status);
                }
            }
        }
        if (returnList.size() > 0) {
            return returnList;
        }
        return null;
    }
    
    public long getHostUid() {
        return 0L;
    }
    
    @Override
    public int compareTo(final FightObject o) {
        final int oSelf = (int)o.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + o.getSpeed();
        final int self = (int)this.getSelfAllTempPro(PRO_DICT_TYPE.RISE_SPEED.getDist()) + this.getSpeed();
        return oSelf - self;
    }
    
    public FightMember asFightMember() {
        final FightMember fightMember = new FightMember();
        fightMember.setId(this.getId());
        fightMember.setHost(this.getHostId());
        fightMember.setSerial((this.getLogic() == LOGIC.LOGICTYPE_MONSTER.getValue()) ? 50000 : this.getSerial());
        fightMember.setPosition(this.getPos());
        fightMember.setIcon(this.getIcon());
        fightMember.setWeaponIcon(this.getWeaponId());
        fightMember.setList(this.asFieldValues());
        final List<Integer> statusList = this.getStatusList();
        if (statusList != null) {
            fightMember.setStatusList(statusList);
        }
        if (this.isPlayer()) {
            final List<Effect> effects = SpringUtils.getBean(EquipService.class).selectEffect(this.getRole());
            if (effects != null && effects.size() > 0) {
                final Effect wing = effects.stream().filter(e -> e.getState() == 1 && e.getPart() == ADORN.WING.getValue()).findFirst().orElse(null);
                if (wing != null) {
                    fightMember.getLight_effect().add(wing.getSerial());
                }
            }
        }
        return fightMember;
    }
    
    public List<FiedValue> asFieldValues() {
        final ArrayList<FiedValue> fiedValues = new ArrayList<FiedValue>();
        fiedValues.add(new FiedValue("name", (byte)4, this.getName()));
        if (this.getGem() > 0) {
            fiedValues.add(new FiedValue("gem", (byte)1, this.getGem()));
        }
        fiedValues.add(new FiedValue("lv", (byte)2, this.getLevel()));
        fiedValues.add(new FiedValue("rei", (byte)3, this.getRelive()));
        fiedValues.add(new FiedValue("maxLife", (byte)3, this.getMaxLife()));
        fiedValues.add(new FiedValue("maxMana", (byte)3, this.getMaxMana()));
        fiedValues.add(new FiedValue("currLife", (byte)3, this.getCurrLife()));
        fiedValues.add(new FiedValue("currMana", (byte)3, this.getCurrMana()));
        final List<FiedValue> deyList = SpringUtils.getRoleService().deyList(this.getColor());
        if (deyList.size() > 0) {
            fiedValues.addAll(deyList);
        }
        return fiedValues;
    }
    
    public double getSurplusLifePercent() {
        if (this.maxLife <= 0) {
            return 0.0;
        }
        final String surplusLifePercentStr = BigDecimalUtil.divide(String.valueOf(this.getCurrLife()), String.valueOf(this.getMaxLife()));
        final double surplusLifePercent = Double.parseDouble(surplusLifePercentStr);
        return surplusLifePercent;
    }
    
    public Object pushTempCache(final String key, final Object value) {
        if (this.tempCache == null) {
            this.tempCache = new HashMap<String, Object>(4);
        }
        return this.tempCache.put(key, value);
    }
    
    public <T> T getTempCache(final String key, final Object dft) {
        if (this.tempCache == null) {
            return (T)dft;
        }
        final T result = (T)this.tempCache.get(key);
        return (T)((result == null) ? dft : result);
    }
    
    public double getSurplusManaPercent() {
        if (this.maxMana <= 0) {
            return 0.0;
        }
        final String surplusManaPercentStr = BigDecimalUtil.divide(String.valueOf(this.getCurrMana()), String.valueOf(this.getMaxMana()));
        final double surplusManaPercent = Double.parseDouble(surplusManaPercentStr);
        return surplusManaPercent;
    }
    
    public void recordAction(final Fight fight) {
        this.getFightActionRecord().setv(String.valueOf(fight.getRound()), (Object)this.getType());
        this.getFightActionRecord().setv(fight.getRound() + "details", (Object)this.getSkillId());
    }
    
    public List<Integer> getStatusList() {
        if (this.getStatusMap().size() > 0) {
            final List<Integer> statusList = new ArrayList<Integer>();
            for (final Map.Entry<String, Object> entry : this.getStatusMap().entrySet()) {
                if (!entry.getKey().matches("^[0-9]+$")) {
                    continue;
                }
                if (this.getStatusMap().getInt((String)entry.getKey(), 0) <= 0) {
                    continue;
                }
                statusList.add(Integer.parseInt(entry.getKey()));
                statusList.add(this.getStatusMap().getInt((String)entry.getKey(), 0));
            }
            if (statusList.size() > 0) {
                return statusList;
            }
        }
        return null;
    }
    
    public void addFallDownList(final int targetId) {
        if (this.getTempCache() == null) {
            this.tempCache = new HashMap<String, Object>();
        }
        ArrayList<Integer> targetFallDownList = (ArrayList<Integer>)this.getTempCache().get("fallDownList");
        if (targetFallDownList == null) {
            targetFallDownList = new ArrayList<Integer>();
        }
        targetFallDownList.add(targetId);
    }
    
    public void createFallDownList() {
        if (this.tempCache == null) {
            this.tempCache = new HashMap<String, Object>();
        }
        this.getTempCache().put("fallDownList", new ArrayList());
    }
    
    public void addRoundUseSkill(final int round, final int skillId) {
        List<Integer> skillIds = this.roundUseSkillMap.get(round);
        if (skillIds == null) {
            skillIds = new ArrayList<Integer>();
            this.roundUseSkillMap.put(round, skillIds);
        }
        skillIds.add(skillId);
    }
    
    public void setId(final Integer id) {
        this.id = id;
    }
    
    public void setSerial(final int serial) {
        this.serial = serial;
    }
    
    public void setName(final String name) {
        this.name = name;
    }
    
    public void setLevel(final int level) {
        this.level = level;
    }
    
    public void setRelive(final int relive) {
        this.relive = relive;
    }
    
    public void setPos(final int pos) {
        this.pos = pos;
    }
    
    public void setRace(final int race) {
        this.race = race;
    }
    
    public void setIcon(final int icon) {
        this.icon = icon;
    }
    
    public void setColor(final String color) {
        this.color = color;
    }
    
    public void setColorMap(final NutMap colorMap) {
        this.colorMap = colorMap;
    }
    
    public void setMaxLife(final int maxLife) {
        this.maxLife = maxLife;
    }
    
    public void setWeaponId(final int weaponId) {
        this.weaponId = weaponId;
    }
    
    public void setGem(final int gem) {
        this.gem = gem;
    }
    
    public void setMaxMana(final int maxMana) {
        this.maxMana = maxMana;
    }
    
    public void setSpeed(final int speed) {
        this.speed = speed;
    }
    
    public void setPhyPower(final int phyPower) {
        this.phyPower = phyPower;
    }
    
    public void setDoAction(final boolean doAction) {
        this.doAction = doAction;
    }
    
    public void setHostId(final int hostId) {
        this.hostId = hostId;
    }
    
    public void setFightPetId(final int fightPetId) {
        this.fightPetId = fightPetId;
    }
    
    public void setLogic(final int logic) {
        this.logic = logic;
    }
    
    public void setSkillIds(final List<RoleSkill> skillIds) {
        this.skillIds = skillIds;
    }
    
    public void setTargetId(final int targetId) {
        this.targetId = targetId;
    }
    
    public void setSkillId(final int skillId) {
        this.skillId = skillId;
    }
    
    public void setAutoFight(final int AutoFight) {
        this.AutoFight = AutoFight;
    }
    
    public void setBorn(final int born) {
        this.born = born;
    }
    
    public void setMorph(final int morph) {
        this.morph = morph;
    }
    
    public void setSkillCoolMap(final NutMap skillCoolMap) {
        this.skillCoolMap = skillCoolMap;
    }
    
    public void setStatusMap(final NutMap statusMap) {
        this.statusMap = statusMap;
    }
    
    public void setPropertyMap(final NutMap propertyMap) {
        this.propertyMap = propertyMap;
    }
    
    public void setStatusSubMap(final NutMap statusSubMap) {
        this.statusSubMap = statusSubMap;
    }
    
    public void setImbueMap(final Map<Integer, Imbue> imbueMap) {
        this.imbueMap = imbueMap;
    }
    
    public void setClose(final long close) {
        this.close = close;
    }
    
    public void setAction(final int action) {
        this.action = action;
    }
    
    public void setASide(final boolean isASide) {
        this.isASide = isASide;
    }
    
    public void setAutoFightType(final int autoFightType) {
        this.autoFightType = autoFightType;
    }
    
    public void setAutoSkillId(final int autoSkillId) {
        this.autoSkillId = autoSkillId;
    }
    
    public void setAutoTarget(final int autoTarget) {
        this.autoTarget = autoTarget;
    }
    
    public void setComeRound(final int comeRound) {
        this.comeRound = comeRound;
    }
    
    public void setDieRound(final int dieRound) {
        this.dieRound = dieRound;
    }
    
    public void setReviveSize(final int reviveSize) {
        this.reviveSize = reviveSize;
    }
    
    public void setFightMsg(final List<NutMap> fightMsg) {
        this.fightMsg = fightMsg;
    }
    
    public void setYepFightPet(final List<Integer> yepFightPet) {
        this.yepFightPet = yepFightPet;
    }
    
    public void setFightActionRecord(final NutMap fightActionRecord) {
        this.fightActionRecord = fightActionRecord;
    }
    
    public void setBeAttackedMap(final NutMap beAttackedMap) {
        this.beAttackedMap = beAttackedMap;
    }
    
    public void setOtherMap(final NutMap otherMap) {
        this.otherMap = otherMap;
    }
    
    public void setIsCompleted(final Map<Integer, Boolean> isCompleted) {
        this.isCompleted = isCompleted;
    }
    
    public void setTempCache(final Map<String, Object> tempCache) {
        this.tempCache = tempCache;
    }
    
    public void setRoundController(final NutMap roundController) {
        this.roundController = roundController;
    }
    
    public void setRoundUseSkillMap(final Map<Integer, List<Integer>> roundUseSkillMap) {
        this.roundUseSkillMap = roundUseSkillMap;
    }
    
    public Integer getId() {
        return this.id;
    }
    
    public int getSerial() {
        return this.serial;
    }
    
    public String getName() {
        return this.name;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getRelive() {
        return this.relive;
    }
    
    public int getPos() {
        return this.pos;
    }
    
    public int getRace() {
        return this.race;
    }
    
    public int getIcon() {
        return this.icon;
    }
    
    public String getColor() {
        return this.color;
    }
    
    public NutMap getColorMap() {
        return this.colorMap;
    }
    
    public int getCurrLife() {
        return this.currLife;
    }
    
    public int getMaxLife() {
        return this.maxLife;
    }
    
    public int getWeaponId() {
        return this.weaponId;
    }
    
    public int getGem() {
        return this.gem;
    }
    
    public int getCurrMana() {
        return this.currMana;
    }
    
    public int getMaxMana() {
        return this.maxMana;
    }
    
    public int getSpeed() {
        return this.speed;
    }
    
    public int getPhyPower() {
        return this.phyPower;
    }
    
    public boolean isDoAction() {
        return this.doAction;
    }
    
    public int getHostId() {
        return this.hostId;
    }
    
    public int getFightPetId() {
        return this.fightPetId;
    }
    
    public int getLogic() {
        return this.logic;
    }
    
    public List<RoleSkill> getSkillIds() {
        return this.skillIds;
    }
    
    public int getTargetId() {
        return this.targetId;
    }
    
    public int getType() {
        return this.type;
    }
    
    public int getSkillId() {
        return this.skillId;
    }
    
    public int getAutoFight() {
        return this.AutoFight;
    }
    
    public int getBorn() {
        return this.born;
    }
    
    public int getMorph() {
        return this.morph;
    }
    
    public NutMap getSkillCoolMap() {
        return this.skillCoolMap;
    }
    
    public NutMap getPropertyMap() {
        return this.propertyMap;
    }
    
    public NutMap getStatusSubMap() {
        return this.statusSubMap;
    }
    
    public Map<Integer, Imbue> getImbueMap() {
        return this.imbueMap;
    }
    
    public long getClose() {
        return this.close;
    }
    
    public int getAction() {
        return this.action;
    }
    
    public boolean isASide() {
        return this.isASide;
    }
    
    public int getAutoFightType() {
        return this.autoFightType;
    }
    
    public int getAutoSkillId() {
        return this.autoSkillId;
    }
    
    public int getAutoTarget() {
        return this.autoTarget;
    }
    
    public int getComeRound() {
        return this.comeRound;
    }
    
    public int getDieRound() {
        return this.dieRound;
    }
    
    public int getReviveSize() {
        return this.reviveSize;
    }
    
    public List<NutMap> getFightMsg() {
        return this.fightMsg;
    }
    
    public List<Integer> getYepFightPet() {
        return this.yepFightPet;
    }
    
    public NutMap getFightActionRecord() {
        return this.fightActionRecord;
    }
    
    public NutMap getBeAttackedMap() {
        return this.beAttackedMap;
    }
    
    public NutMap getOtherMap() {
        return this.otherMap;
    }
    
    public Map<Integer, Boolean> getIsCompleted() {
        return this.isCompleted;
    }
    
    public Map<String, Object> getTempCache() {
        return this.tempCache;
    }
    
    public NutMap getRoundController() {
        return this.roundController;
    }
    
    public Map<Integer, List<Integer>> getRoundUseSkillMap() {
        return this.roundUseSkillMap;
    }
    
    public FightObject() {
        this.colorMap = new NutMap();
        this.weaponId = 0;
        this.gem = 0;
        this.doAction = false;
        this.targetId = 0;
        this.AutoFight = 0;
        this.born = 0;
        this.morph = 0;
        this.skillCoolMap = new NutMap();
        this.statusMap = new NutMap();
        this.propertyMap = new NutMap();
        this.statusSubMap = new NutMap();
        this.imbueMap = new HashMap<Integer, Imbue>();
        this.close = 0L;
        this.isASide = false;
        this.comeRound = 1;
        this.dieRound = 0;
        this.reviveSize = 0;
        this.yepFightPet = new ArrayList<Integer>();
        this.fightActionRecord = NutMap.NEW();
        this.beAttackedMap = NutMap.NEW();
        this.otherMap = NutMap.NEW();
        this.isCompleted = new HashMap<Integer, Boolean>();
        this.roundController = NutMap.NEW();
        this.roundUseSkillMap = new HashMap<Integer, List<Integer>>();
    }
    
    @ConstructorProperties({ "id", "serial", "name", "level", "relive", "pos", "race", "icon", "color", "colorMap", "currLife", "maxLife", "weaponId", "gem", "currMana", "maxMana", "speed", "phyPower", "doAction", "hostId", "fightPetId", "logic", "skillIds", "targetId", "type", "skillId", "AutoFight", "born", "morph", "skillCoolMap", "statusMap", "propertyMap", "statusSubMap", "imbueMap", "close", "action", "isASide", "autoFightType", "autoSkillId", "autoTarget", "comeRound", "dieRound", "reviveSize", "fightMsg", "yepFightPet", "fightActionRecord", "beAttackedMap", "otherMap", "isCompleted", "tempCache", "roundController", "roundUseSkillMap" })
    public FightObject(final Integer id, final int serial, final String name, final int level, final int relive, final int pos, final int race, final int icon, final String color, final NutMap colorMap, final int currLife, final int maxLife, final int weaponId, final int gem, final int currMana, final int maxMana, final int speed, final int phyPower, final boolean doAction, final int hostId, final int fightPetId, final int logic, final List<RoleSkill> skillIds, final int targetId, final int type, final int skillId, final int AutoFight, final int born, final int morph, final NutMap skillCoolMap, final NutMap statusMap, final NutMap propertyMap, final NutMap statusSubMap, final Map<Integer, Imbue> imbueMap, final long close, final int action, final boolean isASide, final int autoFightType, final int autoSkillId, final int autoTarget, final int comeRound, final int dieRound, final int reviveSize, final List<NutMap> fightMsg, final List<Integer> yepFightPet, final NutMap fightActionRecord, final NutMap beAttackedMap, final NutMap otherMap, final Map<Integer, Boolean> isCompleted, final Map<String, Object> tempCache, final NutMap roundController, final Map<Integer, List<Integer>> roundUseSkillMap) {
        this.colorMap = new NutMap();
        this.weaponId = 0;
        this.gem = 0;
        this.doAction = false;
        this.targetId = 0;
        this.AutoFight = 0;
        this.born = 0;
        this.morph = 0;
        this.skillCoolMap = new NutMap();
        this.statusMap = new NutMap();
        this.propertyMap = new NutMap();
        this.statusSubMap = new NutMap();
        this.imbueMap = new HashMap<Integer, Imbue>();
        this.close = 0L;
        this.isASide = false;
        this.comeRound = 1;
        this.dieRound = 0;
        this.reviveSize = 0;
        this.yepFightPet = new ArrayList<Integer>();
        this.fightActionRecord = NutMap.NEW();
        this.beAttackedMap = NutMap.NEW();
        this.otherMap = NutMap.NEW();
        this.isCompleted = new HashMap<Integer, Boolean>();
        this.roundController = NutMap.NEW();
        this.roundUseSkillMap = new HashMap<Integer, List<Integer>>();
        this.id = id;
        this.serial = serial;
        this.name = name;
        this.level = level;
        this.relive = relive;
        this.pos = pos;
        this.race = race;
        this.icon = icon;
        this.color = color;
        this.colorMap = colorMap;
        this.currLife = currLife;
        this.maxLife = maxLife;
        this.weaponId = weaponId;
        this.gem = gem;
        this.currMana = currMana;
        this.maxMana = maxMana;
        this.speed = speed;
        this.phyPower = phyPower;
        this.doAction = doAction;
        this.hostId = hostId;
        this.fightPetId = fightPetId;
        this.logic = logic;
        this.skillIds = skillIds;
        this.targetId = targetId;
        this.type = type;
        this.skillId = skillId;
        this.AutoFight = AutoFight;
        this.born = born;
        this.morph = morph;
        this.skillCoolMap = skillCoolMap;
        this.statusMap = statusMap;
        this.propertyMap = propertyMap;
        this.statusSubMap = statusSubMap;
        this.imbueMap = imbueMap;
        this.close = close;
        this.action = action;
        this.isASide = isASide;
        this.autoFightType = autoFightType;
        this.autoSkillId = autoSkillId;
        this.autoTarget = autoTarget;
        this.comeRound = comeRound;
        this.dieRound = dieRound;
        this.reviveSize = reviveSize;
        this.fightMsg = fightMsg;
        this.yepFightPet = yepFightPet;
        this.fightActionRecord = fightActionRecord;
        this.beAttackedMap = beAttackedMap;
        this.otherMap = otherMap;
        this.isCompleted = isCompleted;
        this.tempCache = tempCache;
        this.roundController = roundController;
        this.roundUseSkillMap = roundUseSkillMap;
    }
}
