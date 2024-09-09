package com.kitty.system.admin.service;

import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.admin.domain.BriefMagical;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.system.game.equip.make.MakeAttribute;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.admin.domain.BriefSin;
import com.kitty.system.init.domain.SysPetAcsryAdvAttrLib;
import com.kitty.system.enums.war.itempro.def.PET_EQUIP_PRO_DICT_SCORE;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.system.admin.domain.BriePetfAcsry;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.enums.war.itempro.def.EQUIP_PRO_DICT_SCORE;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.admin.domain.BriefAcsry;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import java.util.IdentityHashMap;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.admin.domain.BriefHorse;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import java.util.Arrays;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import java.util.Date;
import com.kitty.common.thread.IdGenerator;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import java.util.stream.Collectors;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.service.CountUtils;
import java.util.ArrayList;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.admin.domain.BriefPet;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.RoleChangeEvent;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.service.RoleDayInCeService;
import com.kitty.system.init.domain.SysHero;
import com.kitty.system.init.domain.SysSkill;
import com.kitty.system.game.skill.domain.RoleSkill;
import com.kitty.system.game.skill.pool.SkillDataPool;
import java.util.HashMap;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.springframework.stereotype.Service;

@Service
public class GmService
{
    @Autowired
    IdWorker worker;
    @Autowired
    RoleService roleService;
    @Autowired
    PetService petService;
    
    public GmService() {
    }
    
    public boolean adjustRoleGrade(int id, int grade) {
        Role role = this.roleService.getPlayer(id);
        role.setRelive(grade / 1000);
        role.setLevel(grade % 1000);
        SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
        Map<Integer, Integer> skillProficient = new HashMap<>();
        Map<Integer, SysSkill> skillMap = SkillDataPool.skillMap;
        for (Integer key : role.getSkills().keySet()) {
            RoleSkill skill = role.getSkills().get(key);
            if (skill.getSkillId().equals(30041)) {
                continue;
            }
            SysSkill sysSkill = skillMap.get(skill.getSkillId());
            skillProficient.put(sysSkill.getPos(), sysRbAttr.getInitSkillExp());
        }
        this.roleService.resetRole(role, skillProficient, PartDataPool.heroMap.get(role.getSerial()), SkillDataPool.skillMap, SpringUtils.getBean(RoleDayInCeService.class), RoleChangeEvent.RACE);
        return true;
    }
    
    public boolean customPet(BriefPet briefPet) {
        Role role = this.roleService.getPlayer(briefPet.getId());
        if (role == null) {
            return false;
        }
        SysPet sysPet = PetDataPool.petMap.get(briefPet.getSerial());
        Pet pet = this.petService.initAsPet(sysPet);
        pet.setRoleId(role.getRoleId());
        pet.setRebirth(briefPet.getRei());
        pet.setPetLv(briefPet.getGrade());
        pet.setGg(pet.getPetLv());
        pet.setLx(pet.getPetLv());
        pet.setLl(pet.getPetLv());
        pet.setMj(pet.getPetLv());
        pet.setFpoint(FormulaHero.restorePorCount(pet.getRebirth(), pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue()));
        pet.setPetClose(briefPet.getClose());
        pet.setAwaken(briefPet.getAwaken());
        List<PetSkill> petSkills = this.petService.initSkills(sysPet);
        List<PetSkill> skills = customPetSkillParse(sysPet, briefPet.getSkills());
        for (int i = 0; i < petSkills.size(); ++i) {
            if (skills.size() > i) {
                petSkills.get(i).setSerial(skills.get(i).getSerial());
            }
        }
        pet.setSkills(petSkills);
        PetBase petBase = new PetBase();
        petBase.setGrow_up(briefPet.getGrowUp());
        petBase.setHp_base(briefPet.getHp());
        petBase.setMp_base(briefPet.getMp());
        petBase.setAp_base(briefPet.getAp());
        petBase.setSp_base(briefPet.getSp());
        int grade = this.petService.countPetGrade(sysPet, petBase);
        petBase.setGrade_base(grade);
        List<PetEquip> list = new ArrayList<>();
        pet.setPetBase(petBase);
        int life = CountUtils.countPetLife(pet, list);
        pet.setCurrLife(life);
        pet.setMaxLife(life);
        int mana = CountUtils.countPetMana(pet, list);
        pet.setCurrMana(mana);
        pet.setMaxMana(mana);
        this.petService.getArenaScore(role.getId(), pet);
        this.petService.selectPet(role).put(pet.getPetId(), pet);
        this.petService.loadPet(role, pet);
        pet.save();
        return true;
    }
    
    public boolean customRune(int rId, int serial, byte grade) {
        SysTianFuSkill fuSkill = SkillDataPool.tianFuSkillMap.get(Integer.valueOf(serial));
        if (fuSkill == null) {
            return false;
        }
        Role role = this.roleService.getPlayer(rId);
        List<RoleTianFu> fuList = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (((List)fuList.stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList())).size() >= 75) {
            return false;
        }
        RoleTianFu rune = new RoleTianFu();
        rune.setId(Long.valueOf(IdGenerator.getNextId()));
        rune.setRoleId((long)role.getId());
        rune.setCategory(fuSkill.getCategory());
        rune.setLayer(fuSkill.getLayer());
        rune.setIsWear(0);
        rune.setTianFuSerial(fuSkill.getCeSerial());
        List<Byte> rarityJudges = new ArrayList<>();
        for (int i = 0; i < fuSkill.getRarityJudge().size(); ++i) {
            if (i != 0) {
                rarityJudges.add(fuSkill.getRarityJudge().get(i));
            }
        }
        int i = rarityJudges.size() - 1;
        while (i >= 0) {
            if ((byte)rarityJudges.get(i) <= grade) {
                rune.setProductOrder((byte)(i + 1));
                break;
            }
            --i;
        }
        rune.setLevel(grade);
        rune.setToIdentifyCeSerial(1);
        rune.setTriggerType(fuSkill.getOpportunity());
        rune.setCreateTime(new Date());
        rune.save();
        fuList.add(rune);
        MessagePusher.pushMessage(role, new RespTianFuList((byte)3, Arrays.asList(new RoleTianFuClient(rune))));
        ((BagService)SpringUtils.getBean(BagService.class)).cartoon(role, Integer.valueOf(serial));
        return true;
    }
    
    public boolean customHorse(BriefHorse briefHorse) {
        Role role = this.roleService.getPlayer(briefHorse.getId());
        if (role == null) {
            return false;
        }
        HorseService horseService = SpringUtils.getHorseService();
        Map<Long, Horse> horseMap = horseService.getHorse(role);
        Horse horse = (Horse)horseMap.values().stream().filter(e -> e.getSeat() == briefHorse.getSeat()).findFirst().orElse(null);
        if (horse == null) {
            return false;
        }
        ArrayList<Long> objects = new ArrayList<>();
        for (Integer key : horse.getSkill().keySet()) {
            Long anInt = (Long)horse.getSkill().get(key);
            objects.add(anInt);
        }
        IdentityHashMap<Integer, Long> skill = new IdentityHashMap();
        int i = 0;
        String[] split = briefHorse.getSkills().split(",");
        int length = split.length;
        int j = 0;
        while (j < length) {
            String string = split[j];
            int key2 = (int)Integer.valueOf(string);
            skill.put(Integer.valueOf(key2), objects.get(i));
            if (++i > 2) {
                break;
            }
            ++j;
        }
        horse.setSkill(skill);
        horse.setGrowUp(briefHorse.getGrowUp());
        horse.setMoveSp(briefHorse.getMove());
        horse.setScore(horseService.score(horse));
        horse.save();
        HorseSkillUtils.studyFinish(role, (long)horse.getId(), null);
        horseService.refresh(role, horse);
        return true;
    }
    
    public boolean customAcsry(Role role, int pos, BriefAcsry briefAcsry) {
        SysDataAcsry acsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(briefAcsry.getSerial()));
        if (acsry == null) {
            return false;
        }
        double score = 0.0;
        Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
        List<FiedEquip> field = new ArrayList<>();
        String[] split;
        for (String str : split = briefAcsry.getBase().split(";")) {
            String[] strings = str.split(",");
            int key = (int)Integer.valueOf(strings[0]);
            int val = (int)Integer.valueOf(strings[1]);
            field.add(new FiedEquip(Integer.valueOf(key), Integer.valueOf(val)));
            int dist = (int)DataGlobalFunc.GetProDictType(key);
            Double odds = (Double)map.get(Integer.valueOf(dist));
            if (odds != null) {
                score += (double)val * (double)odds;
            }
        }
        field.add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(acsry.getMaxEndure())));
        field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score)) + acsry.getBaseScore())));
        BagEquip equip = new BagEquip();
        equip.setName(acsry.getName());
        equip.setPosition(acsry.getPos());
        equip.setType(acsry.getSerial());
        equip.setField(field);
        equip.setProductOrder(briefAcsry.getPj());
        SpringUtils.getEquipService().addEquip(role, equip, pos, 0, "定制配饰");
        return true;
    }
    
    public boolean customAcsryPet(Role role, int pos, BriePetfAcsry briePetfAcsry) {
        List<SysPetAcsry> collect = (List)EquipDataPool.petAcsryMap.values().stream().filter(e -> e.getPos() == briePetfAcsry.getPos() && e.getGrade() == briePetfAcsry.getGrade()).collect(Collectors.toList());
        if (collect.size() <= 0) {
            return false;
        }
        SysPetAcsry sysPetAcsry = (SysPetAcsry)collect.get(RandomUtil.round(Integer.valueOf(0), Integer.valueOf(collect.size() - 1)));
        int subjoinScore = 0;
        if (ACC_GRADE.PRICELESS.getValue() == briePetfAcsry.getPj()) {
            subjoinScore = 600;
        }
        else if (ACC_GRADE.RESERVE.getValue() == briePetfAcsry.getPj()) {
            subjoinScore = 200;
        }
        double score = 0.0;
        Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
        List<FiedEquip> field = new ArrayList<>();
        String[] split;
        for (String str : split = briePetfAcsry.getBase().split(";")) {
            String[] strings = str.split(",");
            int key = (int)Integer.valueOf(strings[0]);
            int val = (int)Integer.valueOf(strings[1]);
            field.add(new FiedEquip(Integer.valueOf(key), Integer.valueOf(val)));
            SysPetAcsryAdvAttrLib advAttrLib = (SysPetAcsryAdvAttrLib)EquipDataPool.petAcsryAdvAttrLibMap.get(Integer.valueOf(key));
            if (advAttrLib != null) {
                if (advAttrLib.getType() == 3) {
                    score += (double)(advAttrLib.getScore() * val / (int)advAttrLib.getValueList().get(1));
                }
                else if (advAttrLib.getType() == 2) {
                    int base = (int)advAttrLib.getValueList().get(0);
                    score += (double)(advAttrLib.getScore() * base / (int)advAttrLib.getValueList().get(1));
                }
                else {
                    score += (double)(advAttrLib.getScore() * (int)advAttrLib.getValueList().get(0) / (int)advAttrLib.getValueList().get(1));
                }
            }
            else {
                int dist = (int)DataGlobalFunc.GetProDictType(key);
                Double odds = (Double)map.get(Integer.valueOf(dist));
                if (odds != null) {
                    score += (double)val * (double)odds;
                }
            }
        }
        field.add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf(BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(score + (double)subjoinScore)))));
        PetEquip petEquip = new PetEquip(sysPetAcsry.getName(), 0, sysPetAcsry.getSerial(), sysPetAcsry.getPos(), briePetfAcsry.getPj(), field);
        petEquip.setId(Long.valueOf(this.worker.nextId()));
        petEquip.setRId((long)role.getId());
        ((BagService)SpringUtils.getBean(BagService.class)).addPetEquip(role, petEquip, true, "自定义宠物配饰");
        return true;
    }
    
    public static List<PetSkill> customPetSkillParse(SysPet sysPet, String str) {
        List<Integer> skills = sysPet.getSkills();
        List<Integer> list = new ArrayList<>();
        String[] split = str.split(",");
        if (skills.size() > 0) {
            int length = split.length;
            int i = 0;
            while (i < length) {
                String s = split[i];
                Integer serial = Integer.valueOf(s);
                if (skills.contains(serial)) {
                    list.add(serial);
                    break;
                }
                ++i;
            }
        }
        for (int length2 = split.length, j = 0; j < length2; ++j) {
            String s = split[j];
            Integer serial = Integer.valueOf(s);
            if (!list.contains(serial)) {
                list.add(serial);
            }
        }
        List<PetSkill> objects = new ArrayList<>();
        for (Integer serial2 : list) {
            int subType = 0;
            String value = String.valueOf(serial2);
            if (value.length() >= 2) {
                subType = (int)Integer.valueOf(value.substring(1, 2));
            }
            subType = ((subType < 3) ? 0 : subType);
            objects.add(new PetSkill((int)serial2, subType));
        }
        return objects;
    }
    
    public boolean customSin(Role role, int pos, BriefSin briefSin) {
        SysSinBieEq bieEq = (SysSinBieEq)EquipDataPool.sinBieEq.get(Integer.valueOf(briefSin.getSerial()));
        if (bieEq == null) {
            return false;
        }
        BagEquip equip = new BagEquip();
        equip.setPosition(bieEq.getPos());
        List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), briefSin.getGemCnt(), 0, 0.0);
        equip.setType(bieEq.getSerial());
        equip.setName(bieEq.getName());
        equip.setField(field);
        String[] split;
        for (String str : split = briefSin.getBase().split(";")) {
            String[] strings = str.split(",");
            int key = (int)Integer.valueOf(strings[0]);
            int val = (int)Integer.valueOf(strings[1]);
            equip.getArtifice().add(new FiedEquip(Integer.valueOf(key), Integer.valueOf(val)));
        }
        Integer score = MakeAttribute.calEquipScore(equip);
        equip.getField().add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf((int)score + bieEq.getBaseScore())));
        ((EquipService)SpringUtils.getBean(EquipService.class)).addEquip(role, equip, pos, 0, "定制仙器");
        return true;
    }
    
    public boolean customMagical(Role role, int pos, BriefMagical briefMagical) {
        SysMagicalBieEq bieEq = (SysMagicalBieEq)EquipDataPool.magicalBieEq.get(Integer.valueOf(briefMagical.getSerial()));
        if (bieEq == null) {
            return false;
        }
        BagEquip equip = new BagEquip();
        equip.setPosition(bieEq.getPos());
        equip.setType(bieEq.getSerial());
        equip.setName(bieEq.getName());
        String[] splitBase;
        for (String str : splitBase = briefMagical.getBase().split(";")) {
            String[] strings = str.split(",");
            equip.getField().add(new FiedEquip(Integer.valueOf(strings[0]), Integer.valueOf(strings[1])));
        }
        equip.getField().add(new FiedEquip(Integer.valueOf(6670), Integer.valueOf(briefMagical.getGemCnt())));
        equip.getField().add(new FiedEquip(Integer.valueOf(6672), Integer.valueOf(bieEq.getMaxEndure())));
        String[] split;
        for (String str2 : split = briefMagical.getRefining().split(";")) {
            String[] strings2 = str2.split(",");
            equip.getArtifice().add(new FiedEquip(Integer.valueOf(strings2[0]), Integer.valueOf(strings2[1])));
        }
        Integer score = MakeAttribute.calEquipScore(equip);
        equip.getField().add(new FiedEquip(Integer.valueOf(6679), Integer.valueOf((int)score + bieEq.getBaseScore())));
        ((EquipService)SpringUtils.getBean(EquipService.class)).addEquip(role, equip, pos, 0, "定制仙器");
        return true;
    }
}
