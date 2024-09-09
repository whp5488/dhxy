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
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.init.domain.SysPetSkill;
import com.kitty.system.game.fightnew.utils.FightUtils;
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
    
    public PetFightObject(Pet pet, FightObject master) {
        this.acsryPetSkillMap = new HashMap();
        this.horseSkillMap = new HashMap();
        this.master = master;
        Map<String, Object> autoSet = pet.getAutoFightSet();
        if (autoSet == null) {
            autoSet = PetFightObject.publicAutoSet;
        }
        this.setId(pet.getPetId());
        this.setIcon((int)pet.getShape());
        this.setName(pet.getPetName());
        this.setRelive((int)pet.getRebirth());
        this.setLevel((int)pet.getPetLv());
        this.setClose(pet.getPetClose());
        this.setPos((int)SkillFunc.getPetPosByMasterPos(Integer.valueOf(master.getPos())));
        this.setLogic(LOGIC.LOGICTYPE_PET.getValue());
        this.setSerial((int)pet.getPetType());
        this.setFightMsg(pet.getMsgMap());
        this.setSpeed(CountUtils.countPetSpeed(pet, true));
        this.setPhyPower(CountUtils.countPetPhyPower(pet, SpringUtils.getPetService().getPetEffective((long)this.getRole().getId(), pet)));
        this.setMaxLife((int)pet.getMaxLife());
        this.setMaxMana((int)pet.getMaxMana());
        this.setCurrMana((int)pet.getCurrMana());
        this.setCurrLife(pet.getCurrLife());
        this.pet = pet;
        this.setASide(master.isASide());
        this.setHostId((int)master.getId());
        this.setTargetId((int)pet.getPetId());
        if (autoSet.size() > 0) {
            int type = (int)Integer.valueOf(String.valueOf(autoSet.get("type")));
            int autoSkillId = (int)Integer.valueOf(String.valueOf(autoSet.get("skillId")));
            this.setType(type);
            this.setSkillId(autoSkillId);
            this.setAutoFightType(type);
            this.setAutoSkillId(autoSkillId);
            this.setAction((int)Integer.valueOf(autoSet.getOrDefault("action", Integer.valueOf(0)).toString()));
            if (autoSet.get("target") != null && !"".equals(autoSet.get("target"))) {
                String target = String.valueOf(autoSet.get("target"));
                this.setTargetId((int)Integer.valueOf(target));
                this.setAutoTarget((int)Integer.valueOf(target));
            }
        }
        this.setSkillIds(SkillFunc.initPetSkill(pet.getSkills()));
        this.setHorseSkillMap(SkillFunc.initHorseSkill((long)master.getRole().getId(), pet.getHorse()));
        this.setPropertyMap(CountUtils.proFieldValues(pet));
        CountUtils.restProFieldValue(this.getRole(), pet, this.getPropertyMap(), this.getAcsryPetSkillMap());
        HorseSkillUtils.countHorseSkillGain(this.getPropertyMap(), this.getHorseSkillMap());
        TianFuUtils.changePetPropery(this.getRole(), pet, this.getPropertyMap());
        this.color();
    }
    
    public void color() {
        if (this.pet.getColorList() != null && this.pet.getColorList().size() > 0) {
            byte currentColor = 0;
            for (Map<String, Byte> map : this.pet.getColorList()) {
                if ((byte)map.get("is_use") == 1) {
                    currentColor = (byte)map.get("color");
                    break;
                }
                continue;
            }
            this.getColorMap().setv("currHut", Byte.valueOf(currentColor));
        }
        byte acc = SpringUtils.getPetService().isOrnament((long)this.getRole().getId(), (SysPet)PetDataPool.petMap.get(this.pet.getPetType()), this.pet);
        this.getColorMap().setv("acc", Byte.valueOf(acc));
        if (this.pet.getAccColor() != null && this.pet.getAccColor().size() > 0) {
            for (Byte key : this.pet.getAccColor().keySet()) {
                if ((byte)this.pet.getAccColor().get(key) == 1) {
                    this.getColorMap().setv("currAccHut", key);
                    break;
                }
                continue;
            }
        }
        if (this.pet.getAccColorTwo() != null && this.pet.getAccColorTwo().size() > 0) {
            for (Byte key : this.pet.getAccColorTwo().keySet()) {
                if ((byte)this.pet.getAccColorTwo().get(key) == 1) {
                    this.getColorMap().setv("currAccHut2", key);
                    break;
                }
                continue;
            }
        }
        if (this.pet.getBrandNew() != null && this.pet.getBrandNew().size() > 0) {
            for (Integer key2 : this.pet.getBrandNew().keySet()) {
                if ((byte)this.pet.getBrandNew().get(key2) == 1) {
                    this.getColorMap().setv("hf", key2);
                    break;
                }
                continue;
            }
        }
    }
    
    @Override
    public RoleSkill getSkillBy(Integer skillId) {
        if (String.valueOf(skillId).length() == 5) {
            Optional<PetSkill> anyPetSkill2 = this.pet.getSkills().stream().filter(skill -> skill.getSerial() == (int)skillId).findAny();
            if (anyPetSkill2.isPresent()) {
                SysPetSkill petSkill = (SysPetSkill)SkillDataPool.petSkillMap.get(skillId);
                if (petSkill == null) {
                    return null;
                }
                RoleSkill roleSkill = new RoleSkill();
                roleSkill.setSkillId(petSkill.getSerial());
                roleSkill.setType(petSkill.getAttr());
                return roleSkill;
            }
        }
        else if (String.valueOf(skillId).length() == 6) {
            String hostSkillId = String.valueOf(skillId).substring(0, 5);
            Optional<PetSkill> anyPetSkill = this.pet.getSkills().stream().filter(skill -> skill.getSerial() == Integer.parseInt(hostSkillId)).findAny();
            if (anyPetSkill.isPresent()) {
                return FightUtils.getPetRoleSkill((int)skillId);
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
        return (int)this.master.getId();
    }
    
    @Override
    public long getHostUid() {
        return (long)this.master.getRole().getId();
    }
    
    @Override
    public IoSession getSession() {
        return SessionUtils.getSessionBy(this.getHostUid());
    }
    
    @Override
    public FightMember asFightMember() {
        FightMember fightMember = new FightMember();
        fightMember.setId((int)this.getId());
        fightMember.setHost(this.getHostId());
        fightMember.setIcon(this.getIcon());
        fightMember.setSerial(this.getSerial());
        fightMember.setPosition(this.getPos());
        fightMember.setList(this.asFieldValues());
        List<Integer> statusList = super.getStatusList();
        if (statusList != null) {
            fightMember.setStatusList(statusList);
            return fightMember;
        }
        return fightMember;
    }
    
    @Override
    public List<FiedValue> asFieldValues() {
        ArrayList<FiedValue> fiedValues = new ArrayList<>();
        fiedValues.add(new FiedValue("hostId", (byte)3, Integer.valueOf(this.getHostId())));
        fiedValues.add(new FiedValue("name", (byte)4, this.getName()));
        fiedValues.add(new FiedValue("maxLife", (byte)3, Integer.valueOf(this.getMaxLife())));
        fiedValues.add(new FiedValue("maxMana", (byte)3, Integer.valueOf(this.getMaxMana())));
        fiedValues.add(new FiedValue("currLife", (byte)3, Integer.valueOf(this.getCurrLife())));
        fiedValues.add(new FiedValue("currMana", (byte)3, Integer.valueOf(this.getCurrMana())));
        fiedValues.add(new FiedValue("rei", (byte)3, this.pet.getRebirth()));
        fiedValues.add(new FiedValue("lv", (byte)3, this.pet.getPetLv()));
        int currHut = this.colorMap.getInt("currHut", -1);
        if (currHut > 0) {
            fiedValues.add(new FiedValue("currHut", (byte)2, Short.valueOf((short)currHut)));
        }
        int currAcc = this.colorMap.getInt("acc", 0);
        if (currAcc > 0) {
            fiedValues.add(new FiedValue("acc", (byte)1, Byte.valueOf((byte)currAcc)));
        }
        int currAccHut = this.colorMap.getInt("currAccHut", -1);
        if (currAccHut > 0) {
            fiedValues.add(new FiedValue("currAccHut", (byte)2, Short.valueOf((short)currAccHut)));
        }
        int currAccHut2 = this.colorMap.getInt("currAccHut2", -1);
        if (currAccHut2 > 0) {
            fiedValues.add(new FiedValue("currAccHut2", (byte)2, Short.valueOf((short)currAccHut2)));
        }
        int hf = this.colorMap.getInt("hf", -1);
        if (hf > 0) {
            fiedValues.add(new FiedValue("hf", (byte)2, Short.valueOf((short)hf)));
            return fiedValues;
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
        (publicAutoSet = NutMap.NEW()).setv("type", Integer.valueOf(1));
        PetFightObject.publicAutoSet.setv("action", Integer.valueOf(1));
        PetFightObject.publicAutoSet.setv("list", new ArrayList<>());
    }
}
