
package com.kitty.system.game.fightnew.bean;

import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.fightnew.messages.vo.FightMember;
import com.kitty.mina.cache.SessionUtils;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import java.util.Optional;
import com.kitty.system.game.fightnew.utils.FightUtils;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import java.util.Iterator;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import java.util.HashMap;
import com.kitty.system.game.fightnew.book.horse.vo.HorseSkillParam;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.fightnew.FightObject;

public class PetFightObject extends FightObject
{
    private static final NutMap publicAutoSet;
    private FightObject master;
    private Pet pet;
    private Map<Integer, AcsryPetSkillParam> acsryPetSkillMap;
    private Map<Integer, HorseSkillParam> horseSkillMap;
    
    public PetFightObject(final Pet pet, final FightObject master) {
        this.acsryPetSkillMap = new HashMap<Integer, AcsryPetSkillParam>();
        this.horseSkillMap = new HashMap<Integer, HorseSkillParam>();
        this.master = master;
        Map<String, Object> autoSet = pet.getAutoFightSet();
        if (autoSet == null) {
            autoSet = (Map<String, Object>)PetFightObject.publicAutoSet;
        }
        this.setId(pet.getPetId());
        this.setIcon(pet.getShape());
        this.setName(pet.getPetName());
        this.setRelive(pet.getRebirth());
        this.setLevel(pet.getPetLv());
        this.setClose(pet.getPetClose());
        this.setPos(SkillFunc.getPetPosByMasterPos(master.getPos()));
        this.setLogic(LOGIC.LOGICTYPE_PET.getValue());
        this.setSerial(pet.getPetType());
        this.setFightMsg(pet.getMsgMap());
        this.setSpeed(CountUtils.countPetSpeed(pet, true));
        this.setPhyPower(CountUtils.countPetPhyPower(pet, SpringUtils.getPetService().getPetEffective(this.getRole().getId(), pet)));
        this.setMaxLife(pet.getMaxLife());
        this.setMaxMana(pet.getMaxMana());
        this.setCurrMana(pet.getCurrMana());
        this.setCurrLife(pet.getCurrLife());
        this.pet = pet;
        this.setASide(master.isASide());
        this.setHostId(master.getId());
        this.setTargetId(pet.getPetId());
        if (autoSet.size() > 0) {
            final int type = Integer.valueOf(String.valueOf(autoSet.get("type")));
            final int autoSkillId = Integer.valueOf(String.valueOf(autoSet.get("skillId")));
            this.setType(type);
            this.setSkillId(autoSkillId);
            this.setAutoFightType(type);
            this.setAutoSkillId(autoSkillId);
            this.setAction(Integer.valueOf(autoSet.getOrDefault("action", 0).toString()));
            if (autoSet.get("target") != null && !"".equals(autoSet.get("target"))) {
                final String target = String.valueOf(autoSet.get("target"));
                this.setTargetId(Integer.valueOf(target));
                this.setAutoTarget(Integer.valueOf(target));
            }
        }
        this.setSkillIds(SkillFunc.initPetSkill(pet.getSkills()));
        this.setHorseSkillMap(SkillFunc.initHorseSkill(master.getRole().getId(), pet.getHorse()));
        this.setPropertyMap(CountUtils.proFieldValues(pet));
        CountUtils.restProFieldValue(this.getRole(), pet, this.getPropertyMap(), this.getAcsryPetSkillMap());
        HorseSkillUtils.countHorseSkillGain(this.getPropertyMap(), this.getHorseSkillMap());
        TianFuUtils.changePetPropery(this.getRole(), pet, this.getPropertyMap());
        this.color();
    }
    
    public void color() {
        if (this.pet.getColorList() != null && this.pet.getColorList().size() > 0) {
            byte currentColor = 0;
            for (final Map<String, Byte> map : this.pet.getColorList()) {
                if (map.get("is_use") == 1) {
                    currentColor = map.get("color");
                    break;
                }
            }
            this.getColorMap().setv("currHut", (Object)currentColor);
        }
        final byte acc = SpringUtils.getPetService().isOrnament(this.getRole().getId(), PetDataPool.petMap.get(this.pet.getPetType()), this.pet);
        this.getColorMap().setv("acc", (Object)acc);
        if (this.pet.getAccColor() != null && this.pet.getAccColor().size() > 0) {
            for (final Byte key : this.pet.getAccColor().keySet()) {
                if (this.pet.getAccColor().get(key) == 1) {
                    this.getColorMap().setv("currAccHut", (Object)key);
                    break;
                }
            }
        }
        if (this.pet.getAccColorTwo() != null && this.pet.getAccColorTwo().size() > 0) {
            for (final Byte key : this.pet.getAccColorTwo().keySet()) {
                if (this.pet.getAccColorTwo().get(key) == 1) {
                    this.getColorMap().setv("currAccHut2", (Object)key);
                    break;
                }
            }
        }
        if (this.pet.getBrandNew() != null && this.pet.getBrandNew().size() > 0) {
            for (final Integer key2 : this.pet.getBrandNew().keySet()) {
                if (this.pet.getBrandNew().get(key2) == 1) {
                    this.getColorMap().setv("hf", (Object)key2);
                    break;
                }
            }
        }
    }
    
    @Override
    public RoleSkill getSkillBy(final Integer skillId) {
        if (String.valueOf(skillId).length() == 5) {
            final Optional<PetSkill> anyPetSkill = this.pet.getSkills().stream().filter(skill -> skill.getSerial() == skillId).findAny();
            if (anyPetSkill.isPresent()) {
                final SysPetSkill petSkill = SkillDataPool.petSkillMap.get(skillId);
                if (petSkill == null) {
                    return null;
                }
                final RoleSkill roleSkill = new RoleSkill();
                roleSkill.setSkillId(petSkill.getSerial());
                roleSkill.setType(petSkill.getAttr());
                return roleSkill;
            }
        }
        else if (String.valueOf(skillId).length() == 6) {
            final String hostSkillId = String.valueOf(skillId).substring(0, 5);
            final Optional<PetSkill> anyPetSkill2 = this.pet.getSkills().stream().filter(skill -> skill.getSerial() == Integer.parseInt(hostSkillId)).findAny();
            if (anyPetSkill2.isPresent()) {
                return FightUtils.getPetRoleSkill(skillId);
            }
        }
        return null;
    }
    
    @Override
    public Role getRole() {
        return this.master.getRole();
    }
    
    @Override
    public int getHostId() {
        return this.master.getId();
    }
    
    @Override
    public long getHostUid() {
        return this.master.getRole().getId();
    }
    
    @Override
    public IoSession getSession() {
        return SessionUtils.getSessionBy(this.getHostUid());
    }
    
    @Override
    public FightMember asFightMember() {
        final FightMember fightMember = new FightMember();
        fightMember.setId(this.getId());
        fightMember.setHost(this.getHostId());
        fightMember.setIcon(this.getIcon());
        fightMember.setSerial(this.getSerial());
        fightMember.setPosition(this.getPos());
        fightMember.setList(this.asFieldValues());
        final List<Integer> statusList = super.getStatusList();
        if (statusList != null) {
            fightMember.setStatusList(statusList);
        }
        return fightMember;
    }
    
    @Override
    public List<FiedValue> asFieldValues() {
        final ArrayList<FiedValue> fiedValues = new ArrayList<FiedValue>();
        fiedValues.add(new FiedValue("hostId", (byte)3, this.getHostId()));
        fiedValues.add(new FiedValue("name", (byte)4, this.getName()));
        fiedValues.add(new FiedValue("maxLife", (byte)3, this.getMaxLife()));
        fiedValues.add(new FiedValue("maxMana", (byte)3, this.getMaxMana()));
        fiedValues.add(new FiedValue("currLife", (byte)3, this.getCurrLife()));
        fiedValues.add(new FiedValue("currMana", (byte)3, this.getCurrMana()));
        fiedValues.add(new FiedValue("rei", (byte)3, this.pet.getRebirth()));
        fiedValues.add(new FiedValue("lv", (byte)3, this.pet.getPetLv()));
        final int currHut = this.colorMap.getInt("currHut", -1);
        if (currHut > 0) {
            fiedValues.add(new FiedValue("currHut", (byte)2, (short)currHut));
        }
        final int currAcc = this.colorMap.getInt("acc", 0);
        if (currAcc > 0) {
            fiedValues.add(new FiedValue("acc", (byte)1, (byte)currAcc));
        }
        final int currAccHut = this.colorMap.getInt("currAccHut", -1);
        if (currAccHut > 0) {
            fiedValues.add(new FiedValue("currAccHut", (byte)2, (short)currAccHut));
        }
        final int currAccHut2 = this.colorMap.getInt("currAccHut2", -1);
        if (currAccHut2 > 0) {
            fiedValues.add(new FiedValue("currAccHut2", (byte)2, (short)currAccHut2));
        }
        final int hf = this.colorMap.getInt("hf", -1);
        if (hf > 0) {
            fiedValues.add(new FiedValue("hf", (byte)2, (short)hf));
        }
        return fiedValues;
    }
    
    @Override
    public String toString() {
        return "PetFightObject{id=" + this.id + ", name='" + this.name + '\'' + '}';
    }
    
    @Override
    public Pet getPet() {
        return this.pet;
    }
    
    public Map<Integer, AcsryPetSkillParam> getAcsryPetSkillMap() {
        return this.acsryPetSkillMap;
    }
    
    public Map<Integer, HorseSkillParam> getHorseSkillMap() {
        return this.horseSkillMap;
    }
    
    public void setHorseSkillMap(final Map<Integer, HorseSkillParam> horseSkillMap) {
        this.horseSkillMap = horseSkillMap;
    }
    
    static {
        (publicAutoSet = NutMap.NEW()).setv("type", (Object)1);
        PetFightObject.publicAutoSet.setv("action", (Object)1);
        PetFightObject.publicAutoSet.setv("list", (Object)new ArrayList());
    }
}
