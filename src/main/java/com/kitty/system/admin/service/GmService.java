
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
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import java.util.Arrays;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import java.util.Date;
import com.kitty.common.thread.IdGenerator;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import java.util.List;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.equip.domain.PetEquip;
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
    
    public boolean adjustRoleGrade(final int id, final int grade) {
        final Role role = this.roleService.getPlayer(id);
        role.setRelive(grade / 1000);
        role.setLevel(grade % 1000);
        final SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
        final Map<Integer, Integer> skillProficient = new HashMap<Integer, Integer>();
        final Map<Integer, SysSkill> skillMap = SkillDataPool.skillMap;
        for (final Integer key : role.getSkills().keySet()) {
            final RoleSkill skill = role.getSkills().get(key);
            if (skill.getSkillId().equals(30041)) {
                continue;
            }
            final SysSkill sysSkill = skillMap.get(skill.getSkillId());
            skillProficient.put(sysSkill.getPos(), sysRbAttr.getInitSkillExp());
        }
        this.roleService.resetRole(role, skillProficient, PartDataPool.heroMap.get(role.getSerial()), SkillDataPool.skillMap, SpringUtils.getBean(RoleDayInCeService.class), RoleChangeEvent.RACE);
        return true;
    }
    
    public boolean customPet(final BriefPet briefPet) {
        final Role role = this.roleService.getPlayer(briefPet.getId());
        if (role == null) {
            return false;
        }
        final SysPet sysPet = PetDataPool.petMap.get(briefPet.getSerial());
        final Pet pet = this.petService.initAsPet(sysPet);
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
        final List<PetSkill> petSkills = this.petService.initSkills(sysPet);
        final List<PetSkill> skills = customPetSkillParse(sysPet, briefPet.getSkills());
        for (int i = 0; i < petSkills.size(); ++i) {
            if (skills.size() > i) {
                petSkills.get(i).setSerial(skills.get(i).getSerial());
            }
        }
        pet.setSkills(petSkills);
        final PetBase petBase = new PetBase();
        petBase.setGrow_up(briefPet.getGrowUp());
        petBase.setHp_base(briefPet.getHp());
        petBase.setMp_base(briefPet.getMp());
        petBase.setAp_base(briefPet.getAp());
        petBase.setSp_base(briefPet.getSp());
        final int grade = this.petService.countPetGrade(sysPet, petBase);
        petBase.setGrade_base(grade);
        final List<PetEquip> list = new ArrayList<PetEquip>();
        pet.setPetBase(petBase);
        final int life = CountUtils.countPetLife(pet, list);
        pet.setCurrLife(life);
        pet.setMaxLife(life);
        final int mana = CountUtils.countPetMana(pet, list);
        pet.setCurrMana(mana);
        pet.setMaxMana(mana);
        this.petService.getArenaScore(role.getId(), pet);
        this.petService.selectPet(role).put(pet.getPetId(), pet);
        this.petService.loadPet(role, pet);
        pet.save();
        return true;
    }
    
    public boolean customRune(final int rId, final int serial, final byte grade) {
        final SysTianFuSkill fuSkill = SkillDataPool.tianFuSkillMap.get(serial);
        if (fuSkill == null) {
            return false;
        }
        final Role role = this.roleService.getPlayer(rId);
        final List<RoleTianFu> fuList = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (fuList.stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList()).size() >= 75) {
            return false;
        }
        final RoleTianFu rune = new RoleTianFu();
        rune.setId(IdGenerator.getNextId());
        rune.setRoleId(role.getId());
        rune.setCategory(fuSkill.getCategory());
        rune.setLayer(fuSkill.getLayer());
        rune.setIsWear(0);
        rune.setTianFuSerial(fuSkill.getCeSerial());
        final List<Byte> rarityJudges = new ArrayList<Byte>();
        for (int i = 0; i < fuSkill.getRarityJudge().size(); ++i) {
            if (i != 0) {
                rarityJudges.add(fuSkill.getRarityJudge().get(i));
            }
        }
        for (int i = rarityJudges.size() - 1; i >= 0; --i) {
            if (rarityJudges.get(i) <= grade) {
                rune.setProductOrder((byte)(i + 1));
                break;
            }
        }
        rune.setLevel(grade);
        rune.setToIdentifyCeSerial(1);
        rune.setTriggerType(fuSkill.getOpportunity());
        rune.setCreateTime(new Date());
        rune.save();
        fuList.add(rune);
        MessagePusher.pushMessage(role, new RespTianFuList((byte)3, Arrays.asList(new RoleTianFuClient(rune))));
        SpringUtils.getBean(BagService.class).cartoon(role, serial);
        return true;
    }
    
    public boolean customHorse(final BriefHorse briefHorse) {
        final Role role = this.roleService.getPlayer(briefHorse.getId());
        if (role == null) {
            return false;
        }
        final HorseService horseService = SpringUtils.getHorseService();
        final Map<Long, Horse> horseMap = horseService.getHorse(role);
        final Horse horse = horseMap.values().stream().filter(e -> e.getSeat() == briefHorse.getSeat()).findFirst().orElse(null);
        if (horse == null) {
            return false;
        }
        final ArrayList<Long> objects = new ArrayList<Long>();
        for (final Integer key : horse.getSkill().keySet()) {
            final Long anInt = horse.getSkill().get(key);
            objects.add(anInt);
        }
        final IdentityHashMap<Integer, Long> skill = new IdentityHashMap<Integer, Long>();
        int i = 0;
        final String[] split2;
        final String[] split = split2 = briefHorse.getSkills().split(",");
        for (final String string : split2) {
            final int key2 = Integer.valueOf(string);
            skill.put(key2, objects.get(i));
            if (++i > 2) {
                break;
            }
        }
        horse.setSkill(skill);
        horse.setGrowUp(briefHorse.getGrowUp());
        horse.setMoveSp(briefHorse.getMove());
        horse.setScore(horseService.score(horse));
        horse.save();
        HorseSkillUtils.studyFinish(role, horse.getId(), null);
        horseService.refresh(role, horse);
        return true;
    }
    
    public boolean customAcsry(final Role role, final int pos, final BriefAcsry briefAcsry) {
        final SysDataAcsry acsry = EquipDataPool.acsryMap.get(briefAcsry.getSerial());
        if (acsry == null) {
            return false;
        }
        double score = 0.0;
        final Map<Integer, Double> map = EQUIP_PRO_DICT_SCORE.toMap();
        final List<FiedEquip> field = new ArrayList<FiedEquip>();
        final String[] split2;
        final String[] split = split2 = briefAcsry.getBase().split(";");
        for (final String str : split2) {
            final String[] strings = str.split(",");
            final int key = Integer.valueOf(strings[0]);
            final int val = Integer.valueOf(strings[1]);
            field.add(new FiedEquip(key, val));
            final int dist = DataGlobalFunc.GetProDictType(key);
            final Double odds = map.get(dist);
            if (odds != null) {
                score += val * odds;
            }
        }
        field.add(new FiedEquip(6672, acsry.getMaxEndure()));
        field.add(new FiedEquip(6679, BigDecimalUtil.decimalScaleIntegerDown(0, score) + acsry.getBaseScore()));
        final BagEquip equip = new BagEquip();
        equip.setName(acsry.getName());
        equip.setPosition(acsry.getPos());
        equip.setType(acsry.getSerial());
        equip.setField(field);
        equip.setProductOrder(briefAcsry.getPj());
        SpringUtils.getEquipService().addEquip(role, equip, pos, 0, "定制配饰");
        return true;
    }
    
    public boolean customAcsryPet(final Role role, final int pos, final BriePetfAcsry briePetfAcsry) {
        final List<SysPetAcsry> collect = EquipDataPool.petAcsryMap.values().stream().filter(e -> e.getPos() == briePetfAcsry.getPos() && e.getGrade() == briePetfAcsry.getGrade()).collect(Collectors.toList());
        if (collect.size() <= 0) {
            return false;
        }
        final SysPetAcsry sysPetAcsry = collect.get(RandomUtil.round(0, collect.size() - 1));
        int subjoinScore = 0;
        if (ACC_GRADE.PRICELESS.getValue() == briePetfAcsry.getPj()) {
            subjoinScore = 600;
        }
        else if (ACC_GRADE.RESERVE.getValue() == briePetfAcsry.getPj()) {
            subjoinScore = 200;
        }
        double score = 0.0;
        final Map<Integer, Double> map = PET_EQUIP_PRO_DICT_SCORE.toMap();
        final List<FiedEquip> field = new ArrayList<FiedEquip>();
        final String[] split2;
        final String[] split = split2 = briePetfAcsry.getBase().split(";");
        for (final String str : split2) {
            final String[] strings = str.split(",");
            final int key = Integer.valueOf(strings[0]);
            final int val = Integer.valueOf(strings[1]);
            field.add(new FiedEquip(key, val));
            final SysPetAcsryAdvAttrLib advAttrLib = EquipDataPool.petAcsryAdvAttrLibMap.get(key);
            if (advAttrLib != null) {
                if (advAttrLib.getType() == 3) {
                    score += advAttrLib.getScore() * val / advAttrLib.getValueList().get(1);
                }
                else if (advAttrLib.getType() == 2) {
                    final int base = advAttrLib.getValueList().get(0);
                    score += advAttrLib.getScore() * base / advAttrLib.getValueList().get(1);
                }
                else {
                    score += advAttrLib.getScore() * advAttrLib.getValueList().get(0) / advAttrLib.getValueList().get(1);
                }
            }
            else {
                final int dist = DataGlobalFunc.GetProDictType(key);
                final Double odds = map.get(dist);
                if (odds != null) {
                    score += val * odds;
                }
            }
        }
        field.add(new FiedEquip(6679, BigDecimalUtil.decimalScaleIntegerDown(0, score + subjoinScore)));
        final PetEquip petEquip = new PetEquip(sysPetAcsry.getName(), 0, sysPetAcsry.getSerial(), sysPetAcsry.getPos(), briePetfAcsry.getPj(), field);
        petEquip.setId(this.worker.nextId());
        petEquip.setRId(role.getId());
        SpringUtils.getBean(BagService.class).addPetEquip(role, petEquip, true, "自定义宠物配饰");
        return true;
    }
    
    public static List<PetSkill> customPetSkillParse(final SysPet sysPet, final String str) {
        final List<Integer> skills = sysPet.getSkills();
        final List<Integer> list = new ArrayList<Integer>();
        final String[] split = str.split(",");
        if (skills.size() > 0) {
            for (final String s : split) {
                final Integer serial = Integer.valueOf(s);
                if (skills.contains(serial)) {
                    list.add(serial);
                    break;
                }
            }
        }
        for (final String s : split) {
            final Integer serial = Integer.valueOf(s);
            if (!list.contains(serial)) {
                list.add(serial);
            }
        }
        final List<PetSkill> objects = new ArrayList<PetSkill>();
        for (final Integer serial2 : list) {
            int subType = 0;
            final String value = String.valueOf(serial2);
            if (value.length() >= 2) {
                subType = Integer.valueOf(value.substring(1, 2));
            }
            subType = ((subType < 3) ? 0 : subType);
            objects.add(new PetSkill(serial2, subType));
        }
        return objects;
    }
    
    public boolean customSin(final Role role, final int pos, final BriefSin briefSin) {
        final SysSinBieEq bieEq = EquipDataPool.sinBieEq.get(briefSin.getSerial());
        if (bieEq == null) {
            return false;
        }
        final BagEquip equip = new BagEquip();
        equip.setPosition(bieEq.getPos());
        final List<FiedEquip> field = MakeAttribute.createBaseBieEq(bieEq.getBaseAttr(), bieEq.getMaxEndure(), bieEq.getSerial(), briefSin.getGemCnt(), 0, 0.0);
        equip.setType(bieEq.getSerial());
        equip.setName(bieEq.getName());
        equip.setField(field);
        final String[] split2;
        final String[] split = split2 = briefSin.getBase().split(";");
        for (final String str : split2) {
            final String[] strings = str.split(",");
            final int key = Integer.valueOf(strings[0]);
            final int val = Integer.valueOf(strings[1]);
            equip.getArtifice().add(new FiedEquip(key, val));
        }
        final Integer score = MakeAttribute.calEquipScore(equip);
        equip.getField().add(new FiedEquip(6679, score + bieEq.getBaseScore()));
        SpringUtils.getBean(EquipService.class).addEquip(role, equip, pos, 0, "定制仙器");
        return true;
    }
    
    public boolean customMagical(final Role role, final int pos, final BriefMagical briefMagical) {
        final SysMagicalBieEq bieEq = EquipDataPool.magicalBieEq.get(briefMagical.getSerial());
        if (bieEq == null) {
            return false;
        }
        final BagEquip equip = new BagEquip();
        equip.setPosition(bieEq.getPos());
        equip.setType(bieEq.getSerial());
        equip.setName(bieEq.getName());
        final String[] split2;
        final String[] splitBase = split2 = briefMagical.getBase().split(";");
        for (final String str : split2) {
            final String[] strings = str.split(",");
            equip.getField().add(new FiedEquip(Integer.valueOf(strings[0]), Integer.valueOf(strings[1])));
        }
        equip.getField().add(new FiedEquip(6670, briefMagical.getGemCnt()));
        equip.getField().add(new FiedEquip(6672, bieEq.getMaxEndure()));
        final String[] split3;
        final String[] split = split3 = briefMagical.getRefining().split(";");
        for (final String str2 : split3) {
            final String[] strings2 = str2.split(",");
            equip.getArtifice().add(new FiedEquip(Integer.valueOf(strings2[0]), Integer.valueOf(strings2[1])));
        }
        final Integer score = MakeAttribute.calEquipScore(equip);
        equip.getField().add(new FiedEquip(6679, score + bieEq.getBaseScore()));
        SpringUtils.getBean(EquipService.class).addEquip(role, equip, pos, 0, "定制仙器");
        return true;
    }
}
