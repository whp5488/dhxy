
package com.kitty.system.game.pet.service;

import com.kitty.system.init.domain.vo.PetAccColor;
import com.kitty.system.game.pet.message.ReqPetCutAccColor;
import com.kitty.system.init.domain.SysDataStuff;
import com.kitty.system.init.pool.ItemDataPool;
import java.util.Optional;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.PRO_TYPE;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.fightnew.book.acsrypet.pojo.AcsryPetSkillParam;
import com.kitty.system.game.vo.FieldProp;
import com.google.common.base.Joiner;
import com.kitty.system.game.market.message.vo.HorseSkillCard;
import com.kitty.system.game.market.message.vo.PetEquipCard;
import com.kitty.system.game.market.message.RespMarketPetCard;
import com.kitty.system.game.pet.message.ReqPetFlyStarter;
import com.kitty.system.game.pet.message.ReqPetFly;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.pet.domain.vo.PetCorrection;
import org.nutz.lang.util.NutMap;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.game.pet.handler.FeedPetHandler;
import com.kitty.system.common.message.RespMsg;
import org.nutz.lang.Strings;
import com.kitty.system.game.pet.handler.FeedHelper;
import com.kitty.system.game.safe.util.SafeUtil;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.PetCache;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.game.pet.message.ReqSetShapeTemp;
import com.kitty.system.enums.war.wardef.FIGHT_TYPE;
import com.kitty.system.game.pet.message.RespPetCorrection;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.common.formula.FormulaHero;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.enter.message.RespUpdate;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.pet.message.RespPetInfo;
import org.nutz.json.Json;
import com.kitty.system.game.fightnew.messages.old.RespFightMsgDlg;
import com.kitty.system.game.pet.message.RespPetEffctInfo;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.wardef.LOGIC_SKILL;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import java.util.Arrays;
import com.kitty.system.game.vo.utils.Const;
import java.util.Collection;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysPetAcsry;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import com.kitty.common.utils.RandomUtil;
import java.util.HashMap;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.List;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.ArrayList;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.vo.Condition;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.init.pool.PartDataPool;
import com.kitty.system.init.domain.SysRbAttr;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.mina.cache.Deploy;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.horse.service.HorseService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class PetService
{
    private Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private HorseService horseService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private BagService bagService;
    
    public PetService() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    public void loadPet(final Role role) {
        final Map<Integer, Pet> map = this.selectPet(role);
        for (final Integer key : map.keySet()) {
            final Pet pet = map.get(key);
            this.loadPet(role, pet);
        }
    }
    
    public void doWeeklyReset(final Role role) {
        final Map<Integer, Pet> map = this.selectPet(role);
        for (final Integer key : map.keySet()) {
            final Pet pet = map.get(key);
            if (pet.getDomesticate() > 0) {
                pet.setDomesticate((short)0);
                this.loadPet(role, pet);
                pet.save();
            }
        }
    }
    
    public void firstClaimPet(final Role role, final Integer type) {
        if (role.getExtendBox().getFkp() == 0 && Deploy.firstClaimPet.contains(type)) {
            final SysPet sysPet = PetDataPool.petMap.get(type);
            if (sysPet != null) {
                final boolean flag = this.canGetPet(role, sysPet, false);
                if (flag) {
                    final Pet pet = this.addPet(role, sysPet, 0, "上线领取");
                    this.loadPet(role, pet);
                    role.getExtendBox().setCombatPet(pet.getPetId());
                    this.petInfo(role, pet);
                    role.getExtendBox().setFkp((byte)1);
                    role.save();
                }
            }
        }
    }
    
    public boolean canGetPet(final Role role, final SysPet pet, final boolean is) {
        boolean flag = true;
        String msg = "";
        final Integer relive = role.getRelive();
        if (pet != null) {
            if (is && pet.getCanCompose() == 0) {
                flag = false;
                msg = "该宠物不可以合成";
            }
            if (relive <= 0 && role.getLevel() < pet.getOpenLv()) {
                msg = String.format("需要等级%d", pet.getOpenLv());
                flag = false;
            }
        }
        final SysRbAttr data = PartDataPool.rbAttrMap.get(role.getRelive() + 1);
        if (data != null) {
            final Integer limit = data.getPetNum() + role.getExtendBox().getBuyPetGrid();
            final Map<Integer, Pet> map = this.selectPet(role);
            if (map != null && this.selectPet(role).size() >= limit) {
                msg = String.format("召唤兽超过上限%d个,无法获得", limit);
                final Integer next_relive = relive + 1;
                final SysRbAttr next_data = PartDataPool.rbAttrMap.get(next_relive + 1);
                if (next_data != null) {
                    final Integer num = next_data.getPetNum() + role.getExtendBox().getBuyPetGrid();
                    msg = String.format("召唤兽超过当前上限%d个，%d转后上限增加至%d个", limit, next_relive, num);
                }
                flag = false;
            }
        }
        else {
            msg = "获取宠物信息失败！";
            flag = false;
        }
        if (!flag) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
        }
        return flag;
    }
    
    public void onC2SGetPet(final Integer serial, final Role role) {
        final SysPet sysPet = PetDataPool.petMap.get(serial);
        if (sysPet != null) {
            boolean flag = this.canGetPet(role, sysPet, true);
            if (!flag) {
                return;
            }
            flag = this.roleService.checkCondition(role, sysPet.getNeedItem(), null);
            if (!flag) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("合成材料不足"));
                return;
            }
            flag = this.roleService.costCondition(role, new Condition(null, sysPet.getNeedItem()), "合成兑换", true, false);
            if (flag) {
                final Pet pet = this.addPet(role, sysPet, 0, "合成");
                if (pet != null) {
                    this.loadPet(role, pet);
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("五行能量流转，你获得了新召唤兽" + String.format("#<O>%s#", sysPet.getName())));
                    if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
                        MessagePusher.notify2Lb(role, 701, role.getName(), String.format("#<MM:%d,M:2,MP:%d,MT:%d,CP:%d,%s>[%s]#", pet.getPetId(), role.getRoleId(), pet.getPetType(), pet.getRebirth(), "O", pet.getPetName()));
                    }
                    else {
                        MessagePusher.notify2Lb(role, 700, role.getName(), String.format("#<MM:%d,M:2,MP:%d,MT:%d,CP:%d,%s>[%s]#", pet.getPetId(), role.getRoleId(), pet.getPetType(), pet.getRebirth(), "O", pet.getPetName()));
                    }
                }
                else {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获取宠物信息失败！", false));
                }
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "获取宠物信息失败！", false));
        }
    }
    
    public Pet addPet(final Role role, final SysPet sysPet, final int bd, final String cause) {
        final Pet pet = this.initAsPet(sysPet);
        try {
            if (pet != null) {
                pet.setBindTime(bd);
                pet.setRoleId(role.getRoleId());
                final PetBase petBase = this.createPetBase(sysPet, 1);
                if (petBase == null) {
                    this.logger.error("初始化宠物基础属性为空");
                }
                final List<PetEquip> list = new ArrayList<>();
                pet.setPetBase(petBase);
                final int life = CountUtils.countPetLife(pet, list);
                pet.setCurrLife(life);
                pet.setMaxLife(life);
                final int mana = CountUtils.countPetMana(pet, list);
                pet.setCurrMana(mana);
                pet.setMaxMana(mana);
                this.getArenaScore(role.getId(), pet);
                if (pet.getPetBase() == null) {
                    this.logger.error("设置完宠物宠物评分后宠物基础属性为空");
                }
                this.selectPet(role).put(pet.getPetId(), pet);
                pet.save();
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)2, (byte)1, cause + "-增加宠物" + JsonUtils.object2String(pet), role.getMarking());
                return pet;
            }
            return null;
        }
        catch (final Exception e) {
            this.logger.error("增加宠物异常：玩家 = {}，添加宠物 = {}，宠物信息{}", role.getRoleId(), cause, pet);
            e.printStackTrace();
            return null;
        }
    }
    
    public int getPetId() {
        return SpringUtils.getIdentityService().getNextId(IdentityType.PET);
    }
    
    public Pet initAsPet(final SysPet sysPet) {
        if (sysPet != null) {
            final Pet pet = new Pet();
            pet.setId(this.worker.nextId());
            pet.setPetId(this.getPetId());
            pet.setCorrection(this.createCorrection());
            pet.setPetType(sysPet.getSerial());
            pet.setShape(sysPet.getShape());
            pet.setPetName(sysPet.getName());
            pet.setSkills(this.initSkills(sysPet));
            pet.setMsgMap(DataGlobalFunc.setMsg());
            pet.setCloseLimitMap(DataGlobalFunc.closeLimit());
            final Map<String, Byte> map = new HashMap<String, Byte>();
            map.put("color", (byte)0);
            map.put("is_use", (byte)1);
            final List<Map<String, Byte>> list = new ArrayList<Map<String, Byte>>();
            list.add(map);
            pet.setColorList(list);
            return pet;
        }
        this.logger.error("合成宠物信息为空");
        return null;
    }
    
    public PetBase createPetBase(final SysPet sysPet, final Integer type) {
        final PetBase petBase = new PetBase();
        final int growUp = this.getGrowUp(sysPet, type);
        int hp = 0;
        int mp = 0;
        int ap = 0;
        int sp = 0;
        if (sysPet.getStaticInitVarScore() >= 90) {
            hp = sysPet.getHpMax();
            mp = sysPet.getMpMax();
            ap = sysPet.getApMax();
            sp = sysPet.getSpMax();
        }
        else {
            hp = RandomUtil.round(sysPet.getHpMin(), sysPet.getHpMax());
            mp = RandomUtil.round(sysPet.getMpMin(), sysPet.getMpMax());
            ap = RandomUtil.round(sysPet.getApMin(), sysPet.getApMax());
            sp = RandomUtil.round(sysPet.getSpMin(), sysPet.getSpMax());
        }
        petBase.setHp_base(hp);
        petBase.setMp_base(mp);
        petBase.setAp_base(ap);
        petBase.setSp_base(sp);
        petBase.setGrow_up(growUp);
        final int grade = this.countPetGrade(sysPet, petBase);
        petBase.setGrade_base(grade);
        return petBase;
    }
    
    public Map<Integer, Integer> createCorrection() {
        final Map<Integer, Integer> map = new HashMap<Integer, Integer>();
        final List<Integer> list = PRACTICE_PET_PRO_DICT.toList();
        for (final Integer v : list) {
            map.put(v, 0);
        }
        return map;
    }
    
    public void calculateBase(final long id, final Pet pet) {
        final List<PetEquip> equips = this.getPetEffective(id, pet);
        final int life = CountUtils.countPetLife(pet, equips);
        pet.setCurrLife(life);
        pet.setMaxLife(life);
        final int mana = CountUtils.countPetMana(pet, equips);
        pet.setCurrMana(mana);
        pet.setMaxMana(mana);
    }
    
    public List<PetEquip> getPetEffective(final long id, final Pet pet) {
        final List<PetEquip> objects = new ArrayList<PetEquip>();
        final Map<Long, PetEquip> equipMap = this.bagService.getPetBagEquip(id);
        final List<PetEquip> collect = equipMap.values().stream().filter(e -> pet.getId().equals(e.getPId())).collect(Collectors.toList());
        for (final PetEquip equip : collect) {
            final SysPetAcsry sysPetAcsry = EquipDataPool.petAcsryMap.get(equip.getSerial());
            if (pet.getRebirth() * 1000 + pet.getPetLv() >= sysPetAcsry.getNeedRei() * 1000 + sysPetAcsry.getNeedGrade()) {
                objects.add(equip);
            }
        }
        return objects;
    }
    
    public int getGrowUp(final SysPet sysPet, final Integer flag) {
        final int[] growthRate = sysPet.getGrowthRate();
        int grow_up = growthRate[0];
        if (sysPet.getStaticInitVarScore() >= 90 && sysPet.getSerial().equals(21053)) {
            grow_up = growthRate[growthRate.length - 1];
        }
        else {
            final int index = (int)(Math.random() * growthRate.length);
            grow_up = growthRate[index];
        }
        return grow_up;
    }
    
    public List<PetSkill> initSkills(final SysPet pet) {
        List<Integer> list = new ArrayList<Integer>();
        if (pet.getSkills().size() > 0) {
            list.addAll(pet.getSkills());
        }
        if (pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
            list.addAll(Arrays.asList(Const.NIUBI_PET_INIT_SKILLS));
        }
        else if (pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
            list.addAll(Arrays.asList(Const.TOP_PET_INIT_SKILLS));
        }
        else if (pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
            list.addAll(Arrays.asList(Const.VERY_PET_INIT_SKILLS));
        }
        else if (pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue()) {
            list.addAll(Arrays.asList(Const.PROTECT_PET_INIT_SKILLS));
        }
        else {
            list.addAll(Arrays.asList(Const.NORMAL_PET_INIT_SKILLS));
        }
        list = list.subList(0, list.size() - pet.getSkills().size());
        if (pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue() || pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
            list.addAll(Arrays.asList(Const.NIUBI_PET_INIT_SPECIAL_SKILLS));
        }
        final List<PetSkill> petSkills = new ArrayList<PetSkill>();
        for (final Integer serial : list) {
            final PetSkill petSkill = new PetSkill();
            petSkill.setSerial(serial);
            if (serial > 0) {
                petSkill.setType(PETSKILLSUB.SUBTYPE_BORN.getValue());
            }
            if (serial.equals(Const.LOCK_SKILLPOS)) {
                petSkill.setSerial(-1);
                if (pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
                    petSkill.setType(PETSKILLSUB.SUB_TYPE_SOUL.getValue());
                }
                else {
                    petSkill.setType(PETSKILLSUB.SUBTYPE_SPECIAL.getValue());
                }
            }
            petSkills.add(petSkill);
        }
        return petSkills;
    }
    
    public int countPetGrade(final SysPet sysPet, final PetBase base) {
        if (sysPet.getStaticInitVarScore() >= 90) {
            return 3;
        }
        final int[] weight = sysPet.getWeight();
        final int hp_w = weight[0];
        final int mp_w = weight[1];
        final int ap_w = weight[2];
        final int sp_w = weight[3];
        final double scoreHp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(hp_w * (base.getHp_base() - sysPet.getHpMin() + hp_w)), String.valueOf(sysPet.getHpMax() - sysPet.getHpMin() + hp_w)));
        final double scoreMp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(mp_w * (base.getMp_base() - sysPet.getMpMin() + mp_w)), String.valueOf(sysPet.getMpMax() - sysPet.getMpMin() + mp_w)));
        final double scoreAp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(ap_w * (base.getAp_base() - sysPet.getApMin() + mp_w)), String.valueOf(sysPet.getApMax() - sysPet.getApMin() + ap_w)));
        final double scoreSp = Double.valueOf(BigDecimalUtil.divide(String.valueOf(sp_w * (base.getSp_base() - sysPet.getSpMin() + sp_w)), String.valueOf(sysPet.getSpMax() - sysPet.getSpMin() + sp_w)));
        double score = 100.0;
        if (sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1] != Const.grand_t_range[0]) {
            score = 100.0 * Double.valueOf(BigDecimalUtil.divide(String.valueOf(base.getGrow_up() - sysPet.getGrowthRate()[0]), String.valueOf(sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1] - sysPet.getGrowthRate()[0])));
        }
        score = (int)((scoreHp + scoreMp + scoreAp + scoreSp + score) / (hp_w + mp_w + ap_w + sp_w + 100) * 100.0);
        if (base.getGrow_up() != sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1]) {
            return this.countPetGrade(score);
        }
        if (score > 98.0) {
            return this.countPetGrade(score);
        }
        final double one = Math.abs(base.getHp_base()) + Math.abs(base.getMp_base()) + Math.abs(base.getAp_base()) + Math.abs(base.getSp_base());
        final double two = Math.abs(sysPet.getHpMax()) + Math.abs(sysPet.getMpMax()) + Math.abs(sysPet.getApMax()) + Math.abs(sysPet.getSpMax());
        if (one / two > 0.985) {
            return this.countPetGrade(99.0);
        }
        return this.countPetGrade(score);
    }
    
    public int countPetGrade(final double score) {
        if (score > 98.0) {
            return 4;
        }
        if (score > 86.0) {
            return 3;
        }
        if (score > 75.0) {
            return 2;
        }
        return 1;
    }
    
    public Integer getArenaScore(final long id, final Pet pet) {
        int score = 0;
        final SysPet dataPet = PetDataPool.petMap.get(pet.getPetType());
        if (dataPet != null) {
            score += this.getBaseScope(pet, dataPet);
            score += score * dataPet.getLevelType();
            final List<PetSkill> skillsList = pet.getSkills();
            for (final PetSkill petSkill : skillsList) {
                if (petSkill.getSerial() > 0) {
                    if (petSkill.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                        score += Const.soul;
                    }
                    else {
                        score += this.petCheckSills(petSkill.getSerial());
                    }
                }
            }
            if (dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Normal.getValue()) {
                score += 0;
            }
            else if (dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue()) {
                score += 500;
            }
            else if (dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
                score += 1000;
            }
            else if (dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
                score += 1500;
            }
            else if (dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
                score += 2000;
            }
        }
        final int petClose = (int)Math.min(pet.getPetClose() / 20000L, 1000L);
        score += petClose;
        score += (pet.getColorList().size() - 1) * 50;
        score += pet.getAccColor().size() * 50;
        score += pet.getAccColorTwo().size() * 50;
        score += pet.getBrandNew().size() * 200;
        final List<PetEquip> collect = this.getPetEffective(id, pet);
        for (final PetEquip equip : collect) {
            final FiedEquip fiedEquip = equip.getField().stream().filter(e -> e.getType().equals(6679)).findFirst().orElse(null);
            if (fiedEquip != null) {
                score += fiedEquip.getValue();
            }
        }
        pet.setScore(score);
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, collect));
        return score;
    }
    
    public Integer petCheckSills(final int skillId) {
        final int logicType = DataGlobalFunc.GetObjType(skillId);
        if (logicType == LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue()) {
            final Integer subType = SkillFunc.getPetSkillSubType(skillId);
            if (subType == PETSKILLSUB.SUBTYPE_NORMAL.getValue()) {
                return Const.normal;
            }
            if (subType == PETSKILLSUB.SUBTYPE_SENIOR.getValue()) {
                return Const.senior;
            }
            if (subType == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                return Const.supreme;
            }
            if (subType == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                return Const.special;
            }
            if (subType == PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                return Const.born;
            }
        }
        return 0;
    }
    
    public int getBaseScope(final Pet pet, final SysPet dataPet) {
        int score = 0;
        final double hp = pet.getPetBase().getHp_base() - (double)dataPet.getHpMin();
        final int hp_score = (dataPet.getHpMax() == (double)dataPet.getHpMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(0, calculateData(hp * 200.0, dataPet.getHpMax() - (double)dataPet.getHpMin()));
        score += Math.abs(hp_score);
        final double mp = pet.getPetBase().getMp_base() - (double)dataPet.getMpMin();
        final int mp_score = (dataPet.getMpMax() == (double)dataPet.getMpMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(0, calculateData(mp * 200.0, dataPet.getMpMax() - (double)dataPet.getMpMin()));
        score += Math.abs(mp_score);
        final double ap = pet.getPetBase().getAp_base() - (double)dataPet.getApMin();
        final int ap_score = (dataPet.getApMax() == (double)dataPet.getApMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(0, calculateData(ap * 200.0, dataPet.getApMax() - (double)dataPet.getApMin()));
        score += Math.abs(ap_score);
        final double sp = pet.getPetBase().getSp_base() - (double)dataPet.getSpMin();
        final int sp_score = (dataPet.getSpMax() == (double)dataPet.getSpMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(0, calculateData(sp * 200.0, dataPet.getSpMax() - (double)dataPet.getSpMin()));
        score += Math.abs(sp_score);
        if (pet.getGrowthDan() >= 1) {
            score += 500;
        }
        score += Arrays.binarySearch(dataPet.getGrowthRate(), pet.getPetBase().getGrow_up()) * 50;
        score += pet.getPetLv() * 4;
        score += pet.getRebirth() * 200;
        score += pet.getFly() * 70;
        score += pet.getBone() * 50;
        score += pet.getPetBase().getGrade_base() * 60;
        score += (pet.getCorrectionInfo().getLevel() - pet.getCorrectionInfo().getPoints()) * 10;
        return score;
    }
    
    public static Double calculateData(final Double thed, final Double t) {
        if (t <= 0.0) {
            return 0.0;
        }
        return BigDecimalUtil.decimalScaleDouble(0, thed / t);
    }
    
    public void loadPet(final Role role, final Pet pet) {
        this.petInfo(role, pet);
        final RespPetEffctInfo effctInfo = new RespPetEffctInfo(role.getRoleId(), pet.getPetId(), pet.getSkills());
        MessagePusher.pushMessage(role, effctInfo);
        final RespFightMsgDlg respFightMsgDlg = new RespFightMsgDlg();
        respFightMsgDlg.setId(pet.getPetId());
        respFightMsgDlg.setMsg(Json.toJson(pet.getMsgMap()));
        MessagePusher.pushMessage(role, respFightMsgDlg);
        this.sendPetCorrection(role, pet);
        this.sendPetPanels(role, pet);
    }
    
    public void petInfo(final Role role, final Pet pet) {
        final RespPetInfo petInfo = new RespPetInfo();
        BeanUtils.copyBeanProp(petInfo, pet);
        if (role.getExtendBox().getCombatPet().equals(pet.getPetId())) {
            petInfo.setCombat(1);
        }
        MessagePusher.pushMessage(role, petInfo);
    }
    
    public void sendPetPanels(final Role role, final Pet pet) {
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(pet.getPetId());
        final List<FiedValue> list = this.getPetFieds(role.getId(), pet, true);
        list.add(new FiedValue("exp", (byte)4, String.valueOf(pet.getExp())));
        final SysRbAttr sysRbattr = PartDataPool.rbAttrMap.get(pet.getRebirth() + 1);
        list.add(new FiedValue("nextExp", (byte)4, String.valueOf(FormulaHero.formula_petLvUpExp(sysRbattr.getLvLimit(), pet.getPetLv(), pet.getPetLv() + 1, pet.getRebirth()))));
        int mount = 0;
        if (pet.getHorse() > 0L) {
            final Horse horse = this.horseService.getHorse(role).get(pet.getHorse());
            if (horse != null) {
                mount = horse.getSerial();
            }
        }
        list.add(new FiedValue("mount", (byte)3, mount));
        final int domesticate = 30 - pet.getDomesticate();
        list.add(new FiedValue("domesticate", (byte)2, (domesticate <= 0) ? 0 : domesticate));
        list.add(new FiedValue("acc", (byte)1, this.isOrnament(role.getId(), PetDataPool.petMap.get(pet.getPetType()), pet)));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void sendPetCorrection(final Role role, final Pet pet) {
        final RespPetCorrection respPetCorrection = new RespPetCorrection();
        respPetCorrection.setPetId(pet.getPetId());
        respPetCorrection.setExp(String.valueOf(pet.getCorrectionInfo().getExp()));
        respPetCorrection.setLevel(pet.getCorrectionInfo().getLevel());
        respPetCorrection.setPoints(pet.getCorrectionInfo().getPoints());
        respPetCorrection.setNutMap(JsonUtils.map2String(pet.getCorrection()));
        MessagePusher.pushMessage(role, respPetCorrection);
    }
    
    public void refreshPetClose(final Role role, final Pet pet, final boolean once, final int type) {
        if (type == FIGHT_TYPE.MONSTER.getValue()) {
            boolean flag = false;
            long close = pet.getPetClose();
            if (once) {
                flag = true;
                close -= 25L;
                close = ((close < 0L) ? 0L : close);
            }
            else {
                flag = true;
                Integer num = pet.getCloseLimitMap().get("close_fight");
                if (num != null && num < 200000) {
                    num += 5;
                    if (num > 200000) {
                        close += num - 200000;
                        num = 200000;
                    }
                    else {
                        close += 5L;
                    }
                    pet.getCloseLimitMap().put("close_fight", num);
                }
            }
            if (flag) {
                pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
                pet.save();
                if (role != null) {
                    this.refreshPetInfo(role, pet);
                }
            }
        }
    }
    
    public void refreshPetLevelClose(final Pet pet) {
        final long close = pet.getPetClose() + pet.getPetLv() * 15;
        pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
    }
    
    public void addPetClose(final Role role, final long petId, long value, final int serial, final int tips) {
        final Map<Integer, Pet> petMap = this.selectPet(role);
        final Pet pet = petMap.values().stream().filter(e -> e.getId().equals(petId)).findFirst().orElse(null);
        if (pet != null) {
            final long close = pet.getPetClose() + value;
            pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
            this.getArenaScore(role.getId(), pet);
            this.loadPet(role, pet);
            pet.save();
            if (serial == 702) {
                if (tips > 0) {
                    value = tips;
                }
                MessagePusher.notify2Misc(role, true, 702, pet.getPetName(), value);
            }
        }
    }
    
    public void refreshPetInfo(final Role role, final Pet pet) {
        final RespUpdate respUpdate = new RespUpdate();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        respUpdate.setId(pet.getPetId());
        final List<PetEquip> equips = this.getPetEffective(role.getId(), pet);
        list.add(new FiedValue("life", (byte)3, CountUtils.countPetLife(pet, equips)));
        list.add(new FiedValue("mana", (byte)3, CountUtils.countPetMana(pet, equips)));
        list.add(new FiedValue("curr_life", (byte)3, pet.getCurrLife()));
        list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
        list.add(new FiedValue("pet_close", (byte)4, String.valueOf(pet.getPetClose())));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void refineReplace(final Role role, final ReqSetShapeTemp reqSetShapeTemp) {
        final Pet pet = this.selectPet(role).get(reqSetShapeTemp.getId());
        if (pet != null) {
            final List<PetBase> petBases = pet.getDarkBaseMap().get(reqSetShapeTemp.getType());
            if (reqSetShapeTemp.getIndex() >= petBases.size()) {
                return;
            }
            final PetBase petBase = petBases.get(reqSetShapeTemp.getIndex());
            pet.setPetBase(petBase);
            this.calculateBase(role.getId(), pet);
            this.getArenaScore(role.getId(), pet);
            final RespUpdate respUpdate = new RespUpdate();
            final List<FiedValue> list = new ArrayList<FiedValue>();
            respUpdate.setId(pet.getPetId());
            final PetBase base = pet.getPetBase();
            list.add(new FiedValue("hp_base", (byte)3, base.getHp_base()));
            list.add(new FiedValue("mp_base", (byte)3, base.getMp_base()));
            list.add(new FiedValue("ap_base", (byte)3, base.getAp_base()));
            list.add(new FiedValue("sp_base", (byte)3, base.getSp_base()));
            list.add(new FiedValue("grow_up", (byte)3, base.getGrow_up()));
            list.add(new FiedValue("grade_base", (byte)3, base.getGrade_base()));
            list.add(new FiedValue("score", (byte)3, pet.getScore()));
            list.add(new FiedValue("fly", (byte)1, pet.getFly().byteValue()));
            list.add(new FiedValue("starter", (byte)1, (byte)pet.getFlyStarter()));
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
            pet.getDarkBaseMap().put(reqSetShapeTemp.getType(), new ArrayList<PetBase>());
            pet.save();
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未获取到宠物信息", false));
        }
    }
    
    public boolean addExp(final Role role, final Pet pet, final long exp, final boolean reason) {
        if (exp <= 0L) {
            return false;
        }
        boolean flag = true;
        int level = pet.getPetLv();
        final SysRbAttr sysRbattr = PartDataPool.rbAttrMap.get(pet.getRebirth() + 1);
        if (sysRbattr == null) {
            return false;
        }
        final Integer lvLimit = sysRbattr.getLvLimit();
        final int roleGrade = role.getRelive() * 1000 + role.getLevel();
        final int petGrade = pet.getRebirth() * 1000 + pet.getPetLv();
        if (petGrade - roleGrade >= 10) {
            final String msg = String.format("%s超过你的%d级%d级", pet.getPetName(), role.getLevel(), 10);
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
            return false;
        }
        if (pet.getPetLv() >= lvLimit && pet.getExp() >= DataGlobalFunc.storageExp(pet.getRebirth() * 1000 + pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue())) {
            final String msg = String.format("%s召唤兽已到达%d转的上限%d级", pet.getPetName(), pet.getRebirth(), lvLimit);
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
            return false;
        }
        final String exp_tips = "#<G>" + exp + "#点经验";
        long storageExp = DataGlobalFunc.storageExp(pet.getRebirth() * 1000 + pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
        long totalExp = (pet.getExp() + exp > storageExp) ? storageExp : (pet.getExp() + exp);
        long needExp = BigDecimalUtil.decimalScaleIntegerDown(0, FormulaHero.formula_petLvUpExp(lvLimit, pet.getPetLv(), pet.getPetLv() + 1, pet.getRebirth()));
        if (totalExp >= needExp && pet.getPetLv() < lvLimit) {
            while (totalExp >= needExp) {
                totalExp -= needExp;
                ++level;
                pet.setExp(totalExp);
                storageExp = DataGlobalFunc.storageExp(pet.getRebirth() * 1000 + pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
                if (level > lvLimit || pet.getRebirth() * 1000 + level - roleGrade > 10) {
                    totalExp = ((pet.getExp() + exp > storageExp) ? storageExp : (pet.getExp() + exp));
                    pet.setExp(totalExp);
                    flag = false;
                }
                if (!flag) {
                    if (pet.getExp() >= storageExp) {
                        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你的召唤兽#<G>" + pet.getPetName() + "#等级已超过角色等级8级无法获得经验", true));
                    }
                    this.updateExp(role, pet);
                    break;
                }
                pet.setPetLv(level);
                this.petLevelChangeEvent(role, pet);
                needExp = BigDecimalUtil.decimalScaleIntegerDown(0, FormulaHero.formula_petLvUpExp(lvLimit, pet.getPetLv(), pet.getPetLv() + 1, pet.getRebirth()));
            }
        }
        else {
            pet.setExp(totalExp);
            this.updateExp(role, pet);
        }
        pet.save();
        role.save();
        if (reason) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "你的#<G>" + pet.getPetName() + "#获得了#<IR12>#" + exp_tips, true));
        }
        return true;
    }
    
    public void updateExp(final Role role, final Pet pet) {
        final RespUpdate respUpdate = new RespUpdate();
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("exp", (byte)4, String.valueOf(pet.getExp())));
        respUpdate.setId(pet.getPetId());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void petLevelChangeEvent(final Role role, final Pet pet) {
        pet.setGg(pet.getGg() + 1);
        pet.setLx(pet.getLx() + 1);
        pet.setLl(pet.getLl() + 1);
        pet.setMj(pet.getMj() + 1);
        pet.setFpoint(pet.getFpoint() + 4);
        this.refreshPetLevelClose(pet);
        this.calculateBase(role.getId(), pet);
        this.getArenaScore(role.getId(), pet);
        this.sendPetPanels(role, pet);
    }
    
    public Map<Integer, Pet> selectPet(final Role role) {
        Map<Integer, Pet> map = PetCache.PET_MAP.get(role.getId());
        if (map == null) {
            map = new HashMap<Integer, Pet>();
            final List<Pet> list = this.dao.query(Pet.class, Cnd.where("role_id", "=", role.getRoleId()));
            for (final Pet pet : list) {
                pet.doAfterInit();
                map.put(pet.getPetId(), pet);
            }
            PetCache.PET_MAP.put(role.getId(), map);
            return map;
        }
        return map;
    }
    
    public void feedPet(final Role role, final Pet pet, final int param, final String type, final int num, final Object... args) {
        final boolean clicker = this.roleService.mouserClicker(role, type, 250L);
        if (!clicker) {
            return;
        }
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        final FeedPetHandler handler = FeedHelper.INSTANCE.getHandler(type);
        if (handler != null) {
            final String tips = handler.acceptFeed(pet, role, param, type, num, args);
            if (!Strings.isEmpty(tips)) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), tips, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
    
    public void assignCorrection(final Role role, final Pet pet, final String nutMap) {
        final Map<Integer, Integer> defMaxMap = ConvertDataPool.defPetCorrectionMaxMap.get(pet.getRebirth() + 1);
        final Map<Integer, Integer> correctionMap = pet.getCorrection();
        final PetCorrection correction = pet.getCorrectionInfo();
        final NutMap map = Json.fromJson(NutMap.class, nutMap);
        final List<Integer> list = PRACTICE_PET_PRO_DICT.toList();
        int count = 0;
        for (final String key : map.keySet()) {
            final Integer pro = Integer.valueOf(key);
            final boolean contains = list.contains(pro);
            if (!contains) {
                MessagePusher.pushMessage(role, new RespMsg("修炼属性中没有该修炼类型"));
                return;
            }
            final int num = map.getInt(key);
            if (num < 0) {
                return;
            }
            if (num <= 0) {
                continue;
            }
            if (num > defMaxMap.get(pro)) {
                MessagePusher.pushMessage(role, new RespMsg("修炼超过限值"));
                return;
            }
            count += num - correctionMap.get(pro);
        }
        if (count > correction.getPoints()) {
            MessagePusher.pushMessage(role, new RespMsg("剩余修炼点不足"));
            return;
        }
        for (final String key : map.keySet()) {
            final int num2 = map.getInt(key);
            if (num2 > 0) {
                final Integer pro2 = Integer.valueOf(key);
                correctionMap.put(pro2, num2);
            }
        }
        final int points = correction.getPoints() - count;
        correction.setPoints((points < 0) ? 0 : points);
        this.sendPetCorrection(role, pet);
        pet.save();
        role.save();
    }
    
    public void restAssignCorrection(final Role role, final Pet pet, final byte type) {
        if (!this.checkRestExpend(role, pet, type)) {
            return;
        }
        pet.getCorrectionInfo().setPoints(pet.getCorrectionInfo().getLevel());
        pet.setCorrection(this.createCorrection());
        this.sendPetCorrection(role, pet);
        MessagePusher.pushMessage(role, new RespMsg("重置成功"));
        pet.save();
    }
    
    public void restAttribute(final Role role, final Pet pet, final byte type) {
        final Integer count = FormulaHero.restorePorCount(pet.getRebirth(), pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
        if (pet.getFpoint().equals(count)) {
            MessagePusher.pushMessage(role, new RespMsg("当前召唤兽还没加过点，无需重置"));
            return;
        }
        if (!this.checkRestExpend(role, pet, type)) {
            return;
        }
        pet.setGg(pet.getPetLv());
        pet.setLx(pet.getPetLv());
        pet.setLl(pet.getPetLv());
        pet.setMj(pet.getPetLv());
        pet.setFpoint(count);
        this.calculateBase(role.getId(), pet);
        this.sendPetPanels(role, pet);
        MessagePusher.pushMessage(role, new RespMsg("重置成功"));
        pet.save();
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.getPetEffective(role.getId(), pet)));
    }
    
    public boolean checkRestExpend(final Role role, final Pet pet, final byte type) {
        if (!SafeUtil.isUnlock(role)) {
            return false;
        }
        if (pet.getRebirth() > 0) {
            if (type == 1) {
                final boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 500000L, "重置宠物加点");
                if (!currency) {
                    return false;
                }
            }
            else {
                final Integer goods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, 90020, 1, false, true, "重置宠物加点");
                if (goods != null) {
                    MessagePusher.pushMessage(role, new RespMsg("背包没有万兽灵符"));
                    return false;
                }
            }
        }
        return true;
    }
    
    public void relive(final Role role, final Integer serial) {
        final Pet pet = this.selectPet(role).get(serial);
        if (pet != null) {
            if (role.getRelive() > pet.getRebirth()) {
                if (pet.getRebirth() < 4) {
                    final int needClose = Const.pet_relive[pet.getRebirth()];
                    if (pet.getPetClose() < needClose) {
                        MessagePusher.pushMessage(role, new RespMsg("亲密度不足"));
                        return;
                    }
                    pet.setPetClose(pet.getPetClose() - needClose);
                    final SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(pet.getRebirth() + 2);
                    final int count = FormulaHero.restorePorCount(pet.getRebirth() + 1, sysRbAttr.getInitLv(), LOGIC.LOGICTYPE_PET.getValue());
                    pet.setGg(sysRbAttr.getInitLv());
                    pet.setLx(sysRbAttr.getInitLv());
                    pet.setLl(sysRbAttr.getInitLv());
                    pet.setMj(sysRbAttr.getInitLv());
                    pet.setFpoint(count);
                    pet.setPetLv(sysRbAttr.getInitLv());
                    pet.setRebirth(pet.getRebirth() + 1);
                    this.calculateBase(role.getId(), pet);
                    pet.save();
                    MessagePusher.pushMessage(role, new RespHint(9, (short)1));
                    this.loadPet(role, pet);
                    MessagePusher.pushMessage(role, new RespMsg(String.format("#<ZWG>%s#转生成功", pet.getPetName())));
                    EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.getPetEffective(role.getId(), pet)));
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg("转生已上限"));
                }
            }
            else if (pet.getRebirth() == 3) {
                MessagePusher.pushMessage(role, new RespMsg("转生已上限"));
            }
            else {
                MessagePusher.pushMessage(role, new RespMsg("请先将人物转生"));
            }
        }
    }
    
    public void petFly(final Role role, final ReqPetFly reqPetFly) {
        final Pet pet = this.selectPet(role).get(reqPetFly.getPetId());
        if (pet == null) {
            return;
        }
        if (pet.getFly() >= 3 || !DataGlobalFunc.checkPetFly(pet)) {
            MessagePusher.pushMessage(role, new RespHint(23, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升条件！"));
            return;
        }
        final int count = DataGlobalFunc.getPetFlyCount(pet.getRebirth(), pet.getPetLv());
        if (count <= 0 || pet.getFly() + 1 > count) {
            MessagePusher.pushMessage(role, new RespHint(23, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升条件！"));
            return;
        }
        MessagePusher.pushMessage(role, new RespHint(23, (short)1));
        pet.setFly(count);
        this.calculateBase(role.getId(), pet);
        this.sendPetPanels(role, pet);
        pet.save();
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.getPetEffective(role.getId(), pet)));
    }
    
    public void petFlyStarter(final Role role, final ReqPetFlyStarter reqPetFlyStarter) {
        if (reqPetFlyStarter.getStarter() < 1 || reqPetFlyStarter.getStarter() > 4) {
            return;
        }
        final Pet pet = this.selectPet(role).get(reqPetFlyStarter.getPetId());
        if (pet == null) {
            return;
        }
        if (pet.getFly() < 2) {
            MessagePusher.pushMessage(role, new RespHint(24, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升调整！"));
            return;
        }
        final boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 5000000L);
        if (flag && !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 5000000L, "宠物飞升自定义初始值")) {
            MessagePusher.pushMessage(role, new RespHint(24, (short)0));
            return;
        }
        final int fly = (pet.getFly() + 1 > 3) ? 3 : (pet.getFly() + 1);
        MessagePusher.pushMessage(role, new RespHint(24, (short)1));
        pet.setFly(fly);
        pet.setFlyStarter(reqPetFlyStarter.getStarter());
        this.calculateBase(role.getId(), pet);
        this.sendPetPanels(role, pet);
        pet.save();
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.getPetEffective(role.getId(), pet)));
    }
    
    public void chatPetDetail(final Role ownerRole, final Role role, final int petId) {
        final Pet pet = this.selectPet(ownerRole).get(petId);
        if (pet == null) {
            MessagePusher.pushMessage(role, new RespMsg("链接已过期~"));
            return;
        }
        final RespMarketPetCard card = new RespMarketPetCard();
        final List<PetEquip> petEffective = this.getPetEffective(ownerRole.getId(), pet);
        card.setId(String.valueOf(pet.getPetId()));
        card.setRoleId(ownerRole.getRoleId());
        card.setSerial(pet.getPetType());
        card.setSkills(pet.getSkills());
        card.setStatus((byte)0);
        card.setEndTime(0);
        card.setFields(this.cardPetFied(ownerRole, pet, petEffective));
        final List<FieldProp> fieldProps = this.petFieldPro(ownerRole, pet);
        if (fieldProps != null) {
            card.setProList(fieldProps);
        }
        if (petEffective.size() > 0) {
            final List<PetEquipCard> equips = new ArrayList<PetEquipCard>();
            for (final PetEquip equip : petEffective) {
                equips.add(new PetEquipCard(equip.getId(), equip.getSerial(), equip.getProductOrder()));
            }
            card.setEquips(equips);
        }
        if (pet.getHorse() > 0L) {
            final Map<Long, Horse> horseMap = this.horseService.getHorse(ownerRole);
            final Horse horse = horseMap.get(pet.getHorse());
            if (horse != null) {
                final List<HorseSkillCard> horseSkillCard = new ArrayList<>();
                final String result = Joiner.on(",").withKeyValueSeparator(":").join(horse.getSkill());
                horseSkillCard.add(new HorseSkillCard(horse.getGrowUp(), horse.getLevel(), result));
                card.setHorseSkillCard(horseSkillCard);
            }
        }
        MessagePusher.pushMessage(role, card);
    }
    
    public List<FiedValue> cardPetFied(final Role role, final Pet pet, final List<PetEquip> petEffective) {
        final List<FiedValue> list = this.getPetFieds(role.getId(), pet, false);
        list.add(new FiedValue("name", (byte)4, pet.getPetName()));
        list.add(new FiedValue("correction", (byte)3, pet.getCorrectionInfo().getLevel()));
        list.add(new FiedValue("at", (byte)3, CountUtils.countPetPhyPower(pet, petEffective)));
        list.add(new FiedValue("sp", (byte)3, CountUtils.countPetSpeed(pet, false)));
        list.add(new FiedValue("curr_grow_up", (byte)3, (int)(CountUtils.getPetGrowUp(pet, false) * 1000.0)));
        list.add(new FiedValue("max_grow_up", (byte)3, (int)(CountUtils.getPetGrowUp(pet, true) * 1000.0)));
        if (petEffective != null && petEffective.size() > 0) {
            final byte acc = this.isOrnament(role.getId(), PetDataPool.petMap.get(pet.getPetType()), pet);
            if (acc > 0) {
                list.add(new FiedValue("acc", (byte)1, acc));
            }
        }
        return list;
    }
    
    public List<FieldProp> petFieldPro(final Role role, final Pet pet) {
        final NutMap nutMap = CountUtils.proFieldValues(pet);
        if (nutMap != null) {
            CountUtils.restProFieldValue(role, pet, nutMap, new HashMap<>());
            HorseSkillUtils.countHorseSkillGain(nutMap, SkillFunc.initHorseSkill(role.getId(), pet.getHorse()));
            TianFuUtils.changePetPropery(role, pet, nutMap);
            final Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
            final List<Integer> wuXinDist = PRO_DICT_TYPE.getWuXinDist();
            final List<FieldProp> proList = new ArrayList<>();
            for (final String key : nutMap.keySet()) {
                final Integer integer = Integer.valueOf(key);
                final Integer type = map.get(integer);
                if (type != null) {
                    if (type.equals(PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue())) {
                        proList.add(new FieldProp(integer, (int)(nutMap.getDouble(key) * 1000.0)));
                    }
                    else {
                        if (!type.equals(PRO_TYPE.Pro_Value_NUM_TYPE.getValue())) {
                            continue;
                        }
                        if (wuXinDist.contains(integer)) {
                            proList.add(new FieldProp(integer, (int)(nutMap.getDouble(key) * 100.0)));
                        }
                        else {
                            proList.add(new FieldProp(integer, nutMap.getInt(key)));
                        }
                    }
                }
            }
            return proList;
        }
        return null;
    }
    
    public List<FiedValue> getPetFieds(final Long id, final Pet pet, final boolean flag) {
        final List<FiedValue> list = new ArrayList<FiedValue>();
        List<PetEquip> equips = new ArrayList<PetEquip>();
        if (id != null) {
            equips = this.getPetEffective(id, pet);
        }
        list.add(new FiedValue("life", (byte)3, CountUtils.countPetLife(pet, equips)));
        list.add(new FiedValue("mana", (byte)3, CountUtils.countPetMana(pet, equips)));
        list.add(new FiedValue("pet_close", (byte)4, String.valueOf(pet.getPetClose())));
        final PetBase base = pet.getPetBase();
        list.add(new FiedValue("hp_base", (byte)3, base.getHp_base()));
        list.add(new FiedValue("mp_base", (byte)3, base.getMp_base()));
        list.add(new FiedValue("ap_base", (byte)3, base.getAp_base()));
        list.add(new FiedValue("sp_base", (byte)3, base.getSp_base()));
        list.add(new FiedValue("grow_up", (byte)3, base.getGrow_up()));
        list.add(new FiedValue("grade_base", (byte)3, base.getGrade_base()));
        list.add(new FiedValue("level", (byte)2, pet.getPetLv().shortValue()));
        list.add(new FiedValue("relive", (byte)1, pet.getRebirth().byteValue()));
        list.add(new FiedValue("score", (byte)3, pet.getScore()));
        list.add(new FiedValue("bone", (byte)1, pet.getBone().byteValue()));
        list.add(new FiedValue("growth_dan", (byte)1, pet.getGrowthDan().byteValue()));
        final short[] sendPetColor = this.getSendPetColor(pet.getColorList());
        if (sendPetColor != null) {
            list.add(new FiedValue("currentColor", (byte)2, sendPetColor[0]));
            list.add(new FiedValue("colorList", (byte)2, sendPetColor[1]));
        }
        final short[] accColor = getSendPetAccColor(pet.getAccColor());
        if (accColor != null) {
            list.add(new FiedValue("accColor", (byte)2, accColor[0]));
            list.add(new FiedValue("accColorList", (byte)2, accColor[1]));
        }
        final short[] accColorTwo = getSendPetAccColor(pet.getAccColorTwo());
        if (accColorTwo != null) {
            list.add(new FiedValue("accColor2", (byte)2, accColorTwo[0]));
            list.add(new FiedValue("accColorList2", (byte)2, accColorTwo[1]));
        }
        list.add(new FiedValue("fly", (byte)1, pet.getFly().byteValue()));
        list.add(new FiedValue("awaken", (byte)2, pet.getAwaken()));
        this.petHf(pet, list);
        if (flag) {
            list.add(new FiedValue("gg", (byte)2, pet.getGg().shortValue()));
            list.add(new FiedValue("lx", (byte)2, pet.getLx().shortValue()));
            list.add(new FiedValue("ll", (byte)2, pet.getLl().shortValue()));
            list.add(new FiedValue("mj", (byte)2, pet.getMj().shortValue()));
            list.add(new FiedValue("fpoint", (byte)2, pet.getFpoint().shortValue()));
            list.add(new FiedValue("curr_life", (byte)3, pet.getCurrLife()));
            list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
            list.add(new FiedValue("starter", (byte)1, (byte)pet.getFlyStarter()));
        }
        return list;
    }
    
    public void petHf(final Pet pet, final List<FiedValue> list) {
        if (pet.getBrandNew().size() > 0) {
            final String brandNew = Joiner.on(",").skipNulls().join(pet.getBrandNew().keySet());
            list.add(new FiedValue("list_hf", (byte)4, brandNew));
            short useHf = 0;
            for (final Integer key : pet.getBrandNew().keySet()) {
                if (pet.getBrandNew().get(key) == 1) {
                    useHf = key.shortValue();
                    break;
                }
            }
            list.add(new FiedValue("use_hf", (byte)2, useHf));
        }
    }
    
    public byte isOrnament(final long id, final SysPet sysPet, final Pet pet) {
        byte flag = 0;
        if (sysPet == null) {
            return flag;
        }
        final Map<Long, PetEquip> equipMap = this.bagService.getPetBagEquip(id);
        if (equipMap.size() <= 0) {
            return flag;
        }
        final List<PetEquip> collect = equipMap.values().stream().filter(e -> pet.getId().equals(e.getPId()) && e.getProductOrder() >= ACC_GRADE.PRICELESS.getValue()).collect(Collectors.toList());
        if (collect.size() <= 0) {
            return flag;
        }
        if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() && collect.size() >= 1) {
            flag = 1;
        }
        else if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue() && collect.size() >= 2) {
            flag = 1;
        }
        else if ((sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) && collect.size() >= 3) {
            flag = 1;
        }
        return flag;
    }
    
    public short[] getSendPetColor(final List<Map<String, Byte>> colorList) {
        final short[] colors = new short[2];
        if (colorList != null && colorList.size() > 0) {
            byte currentColor = 0;
            final StringBuffer sb = new StringBuffer("00000000");
            for (final Map<String, Byte> map : colorList) {
                if (map.get("is_use") == 1) {
                    currentColor = map.get("color");
                }
                if (map.get("color") == 0) {
                    continue;
                }
                sb.setCharAt(map.get("color") - 1, '1');
            }
            final String colorBanary = sb.reverse().toString();
            final short returnColorList = Short.parseShort(colorBanary, 2);
            colors[0] = currentColor;
            colors[1] = returnColorList;
            return colors;
        }
        return null;
    }
    
    public static short[] getSendPetAccColor(final Map<Byte, Byte> colorList) {
        final short[] colors = new short[2];
        if (colorList != null && colorList.size() > 0) {
            byte currentColor = 0;
            final StringBuffer sb = new StringBuffer("0000000000");
            for (final Byte key : colorList.keySet()) {
                if (colorList.get(key) == 1) {
                    currentColor = key;
                }
                sb.setCharAt(key - 1, '1');
            }
            final String colorBanary = sb.reverse().toString();
            final short returnColorList = Short.parseShort(colorBanary, 2);
            colors[0] = currentColor;
            colors[1] = returnColorList;
            return colors;
        }
        return null;
    }
    
    public void changeColor(final Role role, final int petId, final byte colorSerial) {
        final Pet pet = this.selectPet(role).get(petId);
        if (pet == null) {
            return;
        }
        final List<Map<String, Byte>> colorList = pet.getColorList();
        final Optional<Map<String, Byte>> colorOption = colorList.stream().filter(map -> map.get("color") == colorSerial).findAny();
        if (!colorOption.isPresent()) {
            return;
        }
        final int[] needYuanqizhuGB = this.getNeedYuanqizhuGB(pet, colorSerial);
        final int yuanqizhuNum = needYuanqizhuGB[0];
        final int gongNum = needYuanqizhuGB[1];
        if (gongNum > 0) {
            final boolean gongFlag = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, gongNum, "宠物变更颜色");
            if (!gongFlag) {
                return;
            }
        }
        final Integer yqzGoods = SpringUtils.getBean(BagService.class).consumeBagGoods(role, 75194, yuanqizhuNum, true, true, "宠物变更颜色");
        if (yqzGoods != null) {
            return;
        }
        colorList.forEach(map -> {
            map.put("is_use", (byte)0);
        });
        final Map<String, Byte> stringByteMap = colorOption.get();
        stringByteMap.put("is_use", (byte)1);
        pet.save();
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(petId);
        final short[] sendPetColor = this.getSendPetColor(colorList);
        final List<FiedValue> list = new ArrayList<>();
        if (sendPetColor != null) {
            list.add(new FiedValue("currentColor", (byte)2, sendPetColor[0]));
            list.add(new FiedValue("colorList", (byte)2, sendPetColor[1]));
        }
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void disassembleYuanqidan(final Role role, final Integer serial, Integer num) {
        if (num == null) {
            num = 1;
        }
        final BagService bean = SpringUtils.getBean(BagService.class);
        final Map<Integer, SysDataStuff> data_stuff = ItemDataPool.data_stuff;
        final SysDataStuff sysDataStuff = data_stuff.get(serial);
        if (sysDataStuff == null) {
            return;
        }
        if (sysDataStuff.getItemPJ() >= 5) {
            return;
        }
        final Integer goods = bean.consumeBagGoods(role, serial, num, true, true, "宠物变更颜色");
        if (goods != null) {
            return;
        }
        final Integer itemPJ = sysDataStuff.getItemPJ();
        Integer yqzNum = DICT_YUANQIDAN_PJ_ZHU.getNum(itemPJ);
        yqzNum *= num;
        final int i = bean.addGoods(role, 75194, yqzNum, 0, true, false, "宠物变更颜色");
    }
    
    public void cutAccColor(final Role role, final ReqPetCutAccColor reqPetCutAccColor) {
        final Pet pet = this.selectPet(role).get(reqPetCutAccColor.getId());
        if (pet == null) {
            return;
        }
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null || sysPet.getAccColor() == null || sysPet.getAccColor().isEmpty()) {
            return;
        }
        final Map<Byte, PetAccColor> petAccColorMap = PetDataPool.petAccColorMap.get(sysPet.getAccColor());
        if (petAccColorMap == null || petAccColorMap.get(reqPetCutAccColor.getSerial()) == null) {
            return;
        }
        if (reqPetCutAccColor.getPos() > 1 && sysPet.getLevelType() < PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
            return;
        }
        if (!this.checkAccColor(role.getId(), pet, sysPet)) {
            return;
        }
        if (reqPetCutAccColor.getPos() == 1) {
            if (this.accColorConsume(role, pet.getAccColor().get(reqPetCutAccColor.getSerial()), petAccColorMap, reqPetCutAccColor.getSerial())) {
                for (final Byte key : pet.getAccColor().keySet()) {
                    if (pet.getAccColor().get(key) == 1) {
                        pet.getAccColor().put(key, (byte)0);
                    }
                }
                if (reqPetCutAccColor.getSerial() > 0) {
                    pet.getAccColor().put(reqPetCutAccColor.getSerial(), (byte)1);
                }
                pet.save();
                this.cutAccColor(role.getId(), pet, reqPetCutAccColor.getPos());
            }
        }
        else if (reqPetCutAccColor.getPos() == 2 && this.accColorConsume(role, pet.getAccColorTwo().get(reqPetCutAccColor.getSerial()), petAccColorMap, reqPetCutAccColor.getSerial())) {
            for (final Byte key : pet.getAccColorTwo().keySet()) {
                if (pet.getAccColorTwo().get(key) == 1) {
                    pet.getAccColorTwo().put(key, (byte)0);
                }
            }
            if (reqPetCutAccColor.getSerial() > 0) {
                pet.getAccColorTwo().put(reqPetCutAccColor.getSerial(), (byte)1);
            }
            pet.save();
            this.cutAccColor(role.getId(), pet, reqPetCutAccColor.getPos());
        }
    }
    
    public void cutAccColor(final long id, final Pet pet, final byte pos) {
        final RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(pet.getPetId());
        final List<FiedValue> list = new ArrayList<FiedValue>();
        if (pos == 1) {
            final short[] accColor = getSendPetAccColor(pet.getAccColor());
            if (accColor != null) {
                list.add(new FiedValue("accColor", (byte)2, accColor[0]));
                list.add(new FiedValue("accColorList", (byte)2, accColor[1]));
            }
        }
        else if (pos == 2) {
            final short[] accColorTwo = getSendPetAccColor(pet.getAccColorTwo());
            if (accColorTwo != null) {
                list.add(new FiedValue("accColor2", (byte)2, accColorTwo[0]));
                list.add(new FiedValue("accColorList2", (byte)2, accColorTwo[1]));
            }
        }
        respUpdate.setList(list);
        MessagePusher.pushMessage(id, respUpdate);
    }
    
    public boolean accColorConsume(final Role role, final Byte value, final Map<Byte, PetAccColor> petAccColorMap, final byte serial) {
        final PetAccColor accColor = petAccColorMap.get(serial);
        Map<Integer, Integer> map = accColor.getMat();
        if (value != null) {
            map = petAccColorMap.get(serial).getSwitchMat();
        }
        for (final Integer key : map.keySet()) {
            final Integer amount = map.get(key);
            final Integer num = this.bagService.getBagGoods(role, key, amount);
            if (num != null) {
                MessagePusher.pushMessage(role, new RespMsg("您的染料不足"));
                return false;
            }
        }
        if (accColor.getSilver() > 0 && !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, accColor.getSilver(), "宠物饰品染色")) {
            return false;
        }
        for (final Integer key : map.keySet()) {
            final Integer amount = map.get(key);
            final Integer integer = this.bagService.consumeBagGoods(role, key, amount, true, true, "宠物饰品染色");
            if (integer != null) {
                MessagePusher.pushMessage(role, new RespMsg("道具扣除失败"));
                return false;
            }
        }
        return true;
    }
    
    public boolean checkAccColor(final long id, final Pet pet, final SysPet sysPet) {
        boolean flag = true;
        if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() && pet.getPetClose() < 1000000L) {
            flag = false;
        }
        else if (sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue() && pet.getPetClose() < 2000000L) {
            flag = false;
        }
        else if ((sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) && pet.getPetClose() < 10000000L) {
            flag = false;
        }
        if (flag) {
            if (this.isOrnament(id, sysPet, pet) == 0) {
                flag = false;
                MessagePusher.pushMessage(id, new RespMsg("装备的无价佩饰数量不足"));
            }
        }
        else {
            MessagePusher.pushMessage(id, new RespMsg("亲密度不足"));
        }
        return flag;
    }
    
    private int[] getNeedYuanqizhuGB(final Pet pet, final Byte colorSerial) {
        final int[] returnDatas = { 1, 0 };
        if (colorSerial == 0) {
            return returnDatas;
        }
        final SysPet sysPet = PetDataPool.petMap.get(pet.getPetType());
        Integer levelType = sysPet.getLevelType();
        if (levelType == null) {
            levelType = 1;
        }
        int yuanqizhuNum = 6;
        int gongNum = 100000;
        switch (levelType) {
            case 2: {
                yuanqizhuNum = 20;
                gongNum = 200000;
                break;
            }
            case 3: {
                yuanqizhuNum = 35;
                gongNum = 300000;
                break;
            }
            case 4: {
                yuanqizhuNum = 100;
                gongNum = 800000;
                break;
            }
            case 5: {
                yuanqizhuNum = 150;
                gongNum = 1000000;
                break;
            }
        }
        returnDatas[0] = yuanqizhuNum;
        returnDatas[1] = gongNum;
        return returnDatas;
    }
    
    enum DICT_YUANQIDAN_PJ_ZHU
    {
        PJ_ONE(Integer.valueOf(2), Integer.valueOf(1)), 
        PJ_TWO(Integer.valueOf(3), Integer.valueOf(3)), 
        PJ_THREE(Integer.valueOf(4), Integer.valueOf(25));
        
        private Integer pj;
        private Integer num;
        
        private DICT_YUANQIDAN_PJ_ZHU(final Integer pj, final Integer num) {
            this.pj = pj;
            this.num = num;
        }
        
        static Integer getNum(final Integer pj) {
            Integer num = DICT_YUANQIDAN_PJ_ZHU.PJ_ONE.num;
            for (final DICT_YUANQIDAN_PJ_ZHU concise : values()) {
                if (concise.getPj().equals(pj)) {
                    num = concise.getNum();
                    break;
                }
            }
            return num;
        }
        
        public Integer getPj() {
            return this.pj;
        }
        
        public Integer getNum() {
            return this.num;
        }
    }
}
