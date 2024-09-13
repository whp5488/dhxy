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
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.market.message.vo.HorseSkillCard;
import com.google.common.base.Joiner;
import com.kitty.system.game.market.message.vo.PetEquipCard;
import com.kitty.system.game.market.message.RespMarketPetCard;
import com.kitty.system.game.pet.message.ReqPetFlyStarter;
import com.kitty.system.game.pet.message.ReqPetFly;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.pet.domain.vo.PetCorrection;
import org.nutz.lang.util.NutMap;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.game.pet.handler.FeedPetHandler;
import org.nutz.lang.Strings;
import com.kitty.system.common.message.RespMsg;
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
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import java.util.Arrays;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysPetAcsry;
import java.util.stream.Collectors;
import com.kitty.system.enums.war.itempro.def.PRACTICE_PET_PRO_DICT;
import com.kitty.common.utils.RandomUtil;
import java.util.HashMap;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.identity.IdentityType;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.system.game.pet.domain.PetBase;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import com.kitty.system.game.fightnew.service.CountUtils;
import java.util.ArrayList;
import com.kitty.system.enums.war.wardef.PETLEVELTYPE;
import com.kitty.system.game.vo.Condition;
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
    
    public void loadPet(Role role) {
        Map<Integer, Pet> map = this.selectPet(role);
        for (Integer key : map.keySet()) {
            Pet pet = (Pet)map.get(key);
            this.loadPet(role, pet);
        }
    }
    
    public void doWeeklyReset(Role role) {
        Map<Integer, Pet> map = this.selectPet(role);
        for (Integer key : map.keySet()) {
            Pet pet = (Pet)map.get(key);
            if (pet.getDomesticate() > 0) {
                pet.setDomesticate((short)0);
                this.loadPet(role, pet);
                pet.save();
            }
            else {
                continue;
            }
        }
    }
    
    public void firstClaimPet(Role role, Integer type) {
        if (role.getExtendBox().getFkp() == 0 && Deploy.firstClaimPet.contains(type)) {
            SysPet sysPet = (SysPet)PetDataPool.petMap.get(type);
            if (sysPet != null) {
                boolean flag = this.canGetPet(role, sysPet, false);
                if (flag) {
                    Pet pet = this.addPet(role, sysPet, 0, "上线领取");
                    this.loadPet(role, pet);
                    role.getExtendBox().setCombatPet(pet.getPetId());
                    this.petInfo(role, pet);
                    role.getExtendBox().setFkp((byte)1);
                    role.save();
                }
            }
        }
    }
    
    public boolean canGetPet(Role role, SysPet pet, boolean is) {
        boolean flag = true;
        String msg = "";
        Integer relive = role.getRelive();
        if (pet != null) {
            if (is && (int)pet.getCanCompose() == 0) {
                flag = false;
                msg = "该宠物不可以合成";
            }
            if ((int)relive <= 0 && (int)role.getLevel() < (int)pet.getOpenLv()) {
                msg = String.format("需要等级%d", pet.getOpenLv());
                flag = false;
            }
        }
        SysRbAttr data = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)role.getRelive() + 1));
        if (data != null) {
            Integer limit = Integer.valueOf((int)data.getPetNum() + role.getExtendBox().getBuyPetGrid());
            Map<Integer, Pet> map = this.selectPet(role);
            if (map != null && this.selectPet(role).size() >= (int)limit) {
                msg = String.format("召唤兽超过上限%d个,无法获得", limit);
                Integer next_relive = Integer.valueOf((int)relive + 1);
                SysRbAttr next_data = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)next_relive + 1));
                if (next_data != null) {
                    Integer num = Integer.valueOf((int)next_data.getPetNum() + role.getExtendBox().getBuyPetGrid());
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
    
    public void onC2SGetPet(Integer serial, Role role) {
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(serial);
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
                Pet pet = this.addPet(role, sysPet, 0, "合成");
                if (pet != null) {
                    this.loadPet(role, pet);
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("五行能量流转，你获得了新召唤兽" + String.format("#<O>%s#", sysPet.getName())));
                    if ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || (int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
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
    
    public Pet addPet(Role role, SysPet sysPet, int bd, String cause) {
        Pet pet = this.initAsPet(sysPet);
        try {
            if (pet != null) {
                pet.setBindTime(bd);
                pet.setRoleId(role.getRoleId());
                PetBase petBase = this.createPetBase(sysPet, Integer.valueOf(1));
                if (petBase == null) {
                    this.logger.error("初始化宠物基础属性为空");
                }
                List<PetEquip> list = new ArrayList<>();
                pet.setPetBase(petBase);
                int life = CountUtils.countPetLife(pet, list);
                pet.setCurrLife(life);
                pet.setMaxLife(Integer.valueOf(life));
                int mana = CountUtils.countPetMana(pet, list);
                pet.setCurrMana(Integer.valueOf(mana));
                pet.setMaxMana(Integer.valueOf(mana));
                this.getArenaScore((long)role.getId(), pet);
                if (pet.getPetBase() == null) {
                    this.logger.error("设置完宠物宠物评分后宠物基础属性为空");
                }
                this.selectPet(role).put(pet.getPetId(), pet);
                pet.save();
                RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)2, (byte)1, cause + "-增加宠物" + JsonUtils.object2String(pet), role.getMarking());
                return pet;
            }
            else {
                return null;
            }
        }
        catch (Exception e) {
            this.logger.error("增加宠物异常：玩家 = {}，添加宠物 = {}，宠物信息{}", new Object[] { role.getRoleId(), cause, pet.toString() });
            e.printStackTrace();
            return null;
        }
    }
    
    public int getPetId() {
        return SpringUtils.getIdentityService().getNextId(IdentityType.PET);
    }
    
    public Pet initAsPet(SysPet sysPet) {
        if (sysPet != null) {
            Pet pet = new Pet();
            pet.setId(Long.valueOf(this.worker.nextId()));
            pet.setPetId(Integer.valueOf(this.getPetId()));
            pet.setCorrection(this.createCorrection());
            pet.setPetType(sysPet.getSerial());
            pet.setShape(sysPet.getShape());
            pet.setPetName(sysPet.getName());
            pet.setSkills(this.initSkills(sysPet));
            pet.setMsgMap(DataGlobalFunc.setMsg());
            pet.setCloseLimitMap(DataGlobalFunc.closeLimit());
            Map<String, Byte> map = new HashMap();
            map.put("color", Byte.valueOf((byte)0));
            map.put("is_use", Byte.valueOf((byte)1));
            List<Map<String, Byte>> list = new ArrayList<>();
            list.add(map);
            pet.setColorList(list);
            return pet;
        }
        this.logger.error("合成宠物信息为空");
        return null;
    }
    
    public PetBase createPetBase(SysPet sysPet, Integer type) {
        PetBase petBase = new PetBase();
        int growUp = this.getGrowUp(sysPet, type);
        int hp = 0;
        int mp = 0;
        int ap = 0;
        int sp = 0;
        if ((int)sysPet.getStaticInitVarScore() >= 90) {
            hp = (int)sysPet.getHpMax();
            mp = (int)sysPet.getMpMax();
            ap = (int)sysPet.getApMax();
            sp = (int)sysPet.getSpMax();
        }
        else {
            hp = RandomUtil.round(sysPet.getHpMin(), sysPet.getHpMax());
            mp = RandomUtil.round(sysPet.getMpMin(), sysPet.getMpMax());
            ap = RandomUtil.round(sysPet.getApMin(), sysPet.getApMax());
            sp = RandomUtil.round(sysPet.getSpMin(), sysPet.getSpMax());
        }
        petBase.setHp_base(Integer.valueOf(hp));
        petBase.setMp_base(Integer.valueOf(mp));
        petBase.setAp_base(Integer.valueOf(ap));
        petBase.setSp_base(Integer.valueOf(sp));
        petBase.setGrow_up(Integer.valueOf(growUp));
        int grade = this.countPetGrade(sysPet, petBase);
        petBase.setGrade_base(Integer.valueOf(grade));
        return petBase;
    }
    
    public Map<Integer, Integer> createCorrection() {
        Map<Integer, Integer> map = new HashMap();
        List<Integer> list = PRACTICE_PET_PRO_DICT.toList();
        for (Integer v : list) {
            map.put(v, Integer.valueOf(0));
        }
        return map;
    }
    
    public void calculateBase(long id, Pet pet) {
        List<PetEquip> equips = this.getPetEffective(id, pet);
        int life = CountUtils.countPetLife(pet, equips);
        pet.setCurrLife(life);
        pet.setMaxLife(Integer.valueOf(life));
        int mana = CountUtils.countPetMana(pet, equips);
        pet.setCurrMana(Integer.valueOf(mana));
        pet.setMaxMana(Integer.valueOf(mana));
    }
    
    public List<PetEquip> getPetEffective(long id, Pet pet) {
        List<PetEquip> objects = new ArrayList<>();
        Map<Long, PetEquip> equipMap = this.bagService.getPetBagEquip(id);
        List<PetEquip> collect = (List)equipMap.values().stream().filter(e -> pet.getId().equals(Long.valueOf(e.getPId()))).collect(Collectors.toList());
        for (PetEquip equip : collect) {
            SysPetAcsry sysPetAcsry = (SysPetAcsry)EquipDataPool.petAcsryMap.get(Integer.valueOf(equip.getSerial()));
            if ((int)pet.getRebirth() * 1000 + (int)pet.getPetLv() >= sysPetAcsry.getNeedRei() * 1000 + sysPetAcsry.getNeedGrade()) {
                objects.add(equip);
            }
            else {
                continue;
            }
        }
        return objects;
    }
    
    public int getGrowUp(SysPet sysPet, Integer flag) {
        int[] growthRate = sysPet.getGrowthRate();
        int grow_up = growthRate[0];
        if ((int)sysPet.getStaticInitVarScore() >= 90 && sysPet.getSerial().equals(Integer.valueOf(21053))) {
            grow_up = growthRate[growthRate.length - 1];
        }
        else {
            int index = (int)(Math.random() * (double)growthRate.length);
            grow_up = growthRate[index];
        }
        return grow_up;
    }
    
    public List<PetSkill> initSkills(SysPet pet) {
        List<Integer> list = new ArrayList<>();
        if (pet.getSkills().size() > 0) {
            list.addAll(pet.getSkills());
        }
        if ((int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
            list.addAll(Arrays.asList(Const.NIUBI_PET_INIT_SKILLS));
        }
        else if ((int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
            list.addAll(Arrays.asList(Const.TOP_PET_INIT_SKILLS));
        }
        else if ((int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
            list.addAll(Arrays.asList(Const.VERY_PET_INIT_SKILLS));
        }
        else if ((int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue()) {
            list.addAll(Arrays.asList(Const.PROTECT_PET_INIT_SKILLS));
        }
        else {
            list.addAll(Arrays.asList(Const.NORMAL_PET_INIT_SKILLS));
        }
        list = list.subList(0, list.size() - pet.getSkills().size());
        if ((int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || (int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue() || (int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || (int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
            list.addAll(Arrays.asList(Const.NIUBI_PET_INIT_SPECIAL_SKILLS));
        }
        List<PetSkill> petSkills = new ArrayList<>();
        for (Integer serial : list) {
            PetSkill petSkill = new PetSkill();
            petSkill.setSerial((int)serial);
            if ((int)serial > 0) {
                petSkill.setType(PETSKILLSUB.SUBTYPE_BORN.getValue());
            }
            if (serial.equals(Const.LOCK_SKILLPOS)) {
                petSkill.setSerial(-1);
                if ((int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() || (int)pet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
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
    
    public int countPetGrade(SysPet sysPet, PetBase base) {
        if ((int)sysPet.getStaticInitVarScore() >= 90) {
            return 3;
        }
        int[] weight = sysPet.getWeight();
        int hp_w = weight[0];
        int mp_w = weight[1];
        int ap_w = weight[2];
        int sp_w = weight[3];
        double scoreHp = (double)Double.valueOf(BigDecimalUtil.divide(String.valueOf(hp_w * ((int)base.getHp_base() - (int)sysPet.getHpMin() + hp_w)), String.valueOf((int)sysPet.getHpMax() - (int)sysPet.getHpMin() + hp_w)));
        double scoreMp = (double)Double.valueOf(BigDecimalUtil.divide(String.valueOf(mp_w * ((int)base.getMp_base() - (int)sysPet.getMpMin() + mp_w)), String.valueOf((int)sysPet.getMpMax() - (int)sysPet.getMpMin() + mp_w)));
        double scoreAp = (double)Double.valueOf(BigDecimalUtil.divide(String.valueOf(ap_w * ((int)base.getAp_base() - (int)sysPet.getApMin() + mp_w)), String.valueOf((int)sysPet.getApMax() - (int)sysPet.getApMin() + ap_w)));
        double scoreSp = (double)Double.valueOf(BigDecimalUtil.divide(String.valueOf(sp_w * ((int)base.getSp_base() - (int)sysPet.getSpMin() + sp_w)), String.valueOf((int)sysPet.getSpMax() - (int)sysPet.getSpMin() + sp_w)));
        double score = 100.0;
        if ((double)sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1] != (double)Const.grand_t_range[0]) {
            score = 100.0 * (double)Double.valueOf(BigDecimalUtil.divide(String.valueOf((int)base.getGrow_up() - sysPet.getGrowthRate()[0]), String.valueOf(sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1] - sysPet.getGrowthRate()[0])));
        }
        score = (double)(int)((scoreHp + scoreMp + scoreAp + scoreSp + score) / (double)(hp_w + mp_w + ap_w + sp_w + 100) * 100.0);
        if ((int)base.getGrow_up() != sysPet.getGrowthRate()[sysPet.getGrowthRate().length - 1]) {
            return this.countPetGrade(score);
        }
        if (score > 98.0) {
            return this.countPetGrade(score);
        }
        double one = (double)(Math.abs((int)base.getHp_base()) + Math.abs((int)base.getMp_base()) + Math.abs((int)base.getAp_base()) + Math.abs((int)base.getSp_base()));
        double two = (double)(Math.abs((int)sysPet.getHpMax()) + Math.abs((int)sysPet.getMpMax()) + Math.abs((int)sysPet.getApMax()) + Math.abs((int)sysPet.getSpMax()));
        if (one / two > 0.985) {
            return this.countPetGrade(99.0);
        }
        return this.countPetGrade(score);
    }
    
    public int countPetGrade(double score) {
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
    
    public Integer getArenaScore(long id, Pet pet) {
        int score = 0;
        SysPet dataPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (dataPet != null) {
            score += this.getBaseScope(pet, dataPet);
            score += score * (int)dataPet.getLevelType();
            List<PetSkill> skillsList = pet.getSkills();
            for (PetSkill petSkill : skillsList) {
                if (petSkill.getSerial() > 0) {
                    if (petSkill.getType() == PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                        score += (int)Const.soul;
                    }
                    else {
                        score += (int)this.petCheckSills(petSkill.getSerial());
                    }
                }
                else {
                    continue;
                }
            }
            if ((int)dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Normal.getValue()) {
                score += 0;
            }
            else if ((int)dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue()) {
                score += 500;
            }
            else if ((int)dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue()) {
                score += 1000;
            }
            else if ((int)dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
                score += 1500;
            }
            else if ((int)dataPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) {
                score += 2000;
            }
        }
        int petClose = (int)Math.min(pet.getPetClose() / 20000L, 1000L);
        score += petClose;
        score += (pet.getColorList().size() - 1) * 50;
        score += pet.getAccColor().size() * 50;
        score += pet.getAccColorTwo().size() * 50;
        score += pet.getBrandNew().size() * 200;
        List<PetEquip> collect = this.getPetEffective(id, pet);
        for (PetEquip equip : collect) {
            FiedEquip fiedEquip = (FiedEquip)equip.getField().stream().filter(e -> e.getType().equals(Integer.valueOf(6679))).findFirst().orElse(null);
            if (fiedEquip != null) {
                score += (int)fiedEquip.getValue();
            }
            else {
                continue;
            }
        }
        pet.setScore(Integer.valueOf(score));
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, collect));
        return Integer.valueOf(score);
    }
    
    public Integer petCheckSills(int skillId) {
        int logicType = (int)DataGlobalFunc.GetObjType(Integer.valueOf(skillId));
        if (logicType == LOGIC_SKILL.LOGIC_TYPE_PET_SKILL.getValue()) {
            Integer subType = SkillFunc.getPetSkillSubType(Integer.valueOf(skillId));
            if ((int)subType == PETSKILLSUB.SUBTYPE_NORMAL.getValue()) {
                return Const.normal;
            }
            if ((int)subType == PETSKILLSUB.SUBTYPE_SENIOR.getValue()) {
                return Const.senior;
            }
            if ((int)subType == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                return Const.supreme;
            }
            if ((int)subType == PETSKILLSUB.SUBTYPE_SPECIAL.getValue()) {
                return Const.special;
            }
            if ((int)subType == PETSKILLSUB.SUBTYPE_BORN.getValue()) {
                return Const.born;
            }
        }
        return Integer.valueOf(0);
    }
    
    public int getBaseScope(Pet pet, SysPet dataPet) {
        int score = 0;
        double hp = (double)(int)pet.getPetBase().getHp_base() - (double)dataPet.getHpMin();
        int hp_score = ((double)dataPet.getHpMax() == (double)(int)dataPet.getHpMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(hp * 200.0), Double.valueOf((double)dataPet.getHpMax() - (double)(int)dataPet.getHpMin())));
        score += Math.abs(hp_score);
        double mp = (double)(int)pet.getPetBase().getMp_base() - (double)dataPet.getMpMin();
        int mp_score = ((double)dataPet.getMpMax() == (double)(int)dataPet.getMpMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(mp * 200.0), Double.valueOf((double)dataPet.getMpMax() - (double)(int)dataPet.getMpMin())));
        score += Math.abs(mp_score);
        double ap = (double)(int)pet.getPetBase().getAp_base() - (double)dataPet.getApMin();
        int ap_score = ((double)dataPet.getApMax() == (double)(int)dataPet.getApMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(ap * 200.0), Double.valueOf((double)dataPet.getApMax() - (double)(int)dataPet.getApMin())));
        score += Math.abs(ap_score);
        double sp = (double)(int)pet.getPetBase().getSp_base() - (double)dataPet.getSpMin();
        int sp_score = ((double)dataPet.getSpMax() == (double)(int)dataPet.getSpMin()) ? 140 : BigDecimalUtil.decimalScaleInteger(Integer.valueOf(0), calculateData(Double.valueOf(sp * 200.0), Double.valueOf((double)dataPet.getSpMax() - (double)(int)dataPet.getSpMin())));
        score += Math.abs(sp_score);
        if ((int)pet.getGrowthDan() >= 1) {
            score += 500;
        }
        score += Arrays.binarySearch(dataPet.getGrowthRate(), (int)pet.getPetBase().getGrow_up()) * 50;
        score += (int)pet.getPetLv() * 4;
        score += (int)pet.getRebirth() * 200;
        score += (int)pet.getFly() * 70;
        score += (int)pet.getBone() * 50;
        score += (int)pet.getPetBase().getGrade_base() * 60;
        score += (pet.getCorrectionInfo().getLevel() - pet.getCorrectionInfo().getPoints()) * 10;
        return score;
    }
    
    public static Double calculateData(Double thed, Double t) {
        if ((double)t <= 0.0) {
            return Double.valueOf(0.0);
        }
        return Double.valueOf(BigDecimalUtil.decimalScaleDouble(Integer.valueOf(0), Double.valueOf((double)thed / (double)t)));
    }
    
    public void loadPet(Role role, Pet pet) {
        this.petInfo(role, pet);
        RespPetEffctInfo effctInfo = new RespPetEffctInfo(role.getRoleId(), pet.getPetId(), pet.getSkills());
        MessagePusher.pushMessage(role, effctInfo);
        RespFightMsgDlg respFightMsgDlg = new RespFightMsgDlg();
        respFightMsgDlg.setId((int)pet.getPetId());
        respFightMsgDlg.setMsg(Json.toJson(pet.getMsgMap()));
        MessagePusher.pushMessage(role, respFightMsgDlg);
        this.sendPetCorrection(role, pet);
        this.sendPetPanels(role, pet);
    }
    
    public void petInfo(Role role, Pet pet) {
        RespPetInfo petInfo = new RespPetInfo();
        BeanUtils.copyBeanProp(petInfo, pet);
        if (role.getExtendBox().getCombatPet().equals(pet.getPetId())) {
            petInfo.setCombat(Integer.valueOf(1));
        }
        MessagePusher.pushMessage(role, petInfo);
    }
    
    public void sendPetPanels(Role role, Pet pet) {
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)pet.getPetId());
        List<FiedValue> list = this.getPetFieds(role.getId(), pet, true);
        list.add(new FiedValue("exp", (byte)4, String.valueOf(pet.getExp())));
        SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)pet.getRebirth() + 1));
        list.add(new FiedValue("nextExp", (byte)4, String.valueOf(FormulaHero.formula_petLvUpExp(sysRbattr.getLvLimit(), pet.getPetLv(), Integer.valueOf((int)pet.getPetLv() + 1), pet.getRebirth()))));
        int mount = 0;
        if (pet.getHorse() > 0L) {
            Horse horse = (Horse)this.horseService.getHorse(role).get(Long.valueOf(pet.getHorse()));
            if (horse != null) {
                mount = horse.getSerial();
            }
        }
        list.add(new FiedValue("mount", (byte)3, Integer.valueOf(mount)));
        int domesticate = 30 - pet.getDomesticate();
        list.add(new FiedValue("domesticate", (byte)2, Integer.valueOf((domesticate <= 0) ? 0 : domesticate)));
        list.add(new FiedValue("acc", (byte)1, Byte.valueOf(this.isOrnament((long)role.getId(), (SysPet)PetDataPool.petMap.get(pet.getPetType()), pet))));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void sendPetCorrection(Role role, Pet pet) {
        RespPetCorrection respPetCorrection = new RespPetCorrection();
        respPetCorrection.setPetId(pet.getPetId());
        respPetCorrection.setExp(String.valueOf(pet.getCorrectionInfo().getExp()));
        respPetCorrection.setLevel(pet.getCorrectionInfo().getLevel());
        respPetCorrection.setPoints(pet.getCorrectionInfo().getPoints());
        respPetCorrection.setNutMap(JsonUtils.map2String(pet.getCorrection()));
        MessagePusher.pushMessage(role, respPetCorrection);
    }
    
    public void refreshPetClose(Role role, Pet pet, boolean once, int type) {
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
                Integer num = (Integer)pet.getCloseLimitMap().get("close_fight");
                if (num != null && (int)num < 200000) {
                    num = Integer.valueOf((int)num + 5);
                    if ((int)num > 200000) {
                        close += (long)((int)num - 200000);
                        num = Integer.valueOf(200000);
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
    
    public void refreshPetLevelClose(Pet pet) {
        long close = pet.getPetClose() + (long)((int)pet.getPetLv() * 15);
        pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
    }
    
    public void addPetClose(Role role, long petId, long value, int serial, int tips) {
        Map<Integer, Pet> petMap = this.selectPet(role);
        Pet pet = (Pet)petMap.values().stream().filter(e -> e.getId().equals(Long.valueOf(petId))).findFirst().orElse(null);
        if (pet != null) {
            long close = pet.getPetClose() + value;
            pet.setPetClose((close > 2000000000L) ? 2000000000L : close);
            this.getArenaScore((long)role.getId(), pet);
            this.loadPet(role, pet);
            pet.save();
            if (serial == 702) {
                if (tips > 0) {
                    value = (long)tips;
                }
                MessagePusher.notify2Misc(role, true, 702, pet.getPetName(), Long.valueOf(value));
            }
        }
    }
    
    public void refreshPetInfo(Role role, Pet pet) {
        RespUpdate respUpdate = new RespUpdate();
        List<FiedValue> list = new ArrayList<>();
        respUpdate.setId((int)pet.getPetId());
        List<PetEquip> equips = this.getPetEffective((long)role.getId(), pet);
        list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countPetLife(pet, equips))));
        list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countPetMana(pet, equips))));
        list.add(new FiedValue("curr_life", (byte)3, Integer.valueOf(pet.getCurrLife())));
        list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
        list.add(new FiedValue("pet_close", (byte)4, String.valueOf(pet.getPetClose())));
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void refineReplace(Role role, ReqSetShapeTemp reqSetShapeTemp) {
        Pet pet = (Pet)this.selectPet(role).get(Integer.valueOf(reqSetShapeTemp.getId()));
        if (pet != null) {
            List<PetBase> petBases = (List)pet.getDarkBaseMap().get(Integer.valueOf(reqSetShapeTemp.getType()));
            if (reqSetShapeTemp.getIndex() >= petBases.size()) {
                return;
            }
            PetBase petBase = (PetBase)petBases.get(reqSetShapeTemp.getIndex());
            pet.setPetBase(petBase);
            this.calculateBase((long)role.getId(), pet);
            this.getArenaScore((long)role.getId(), pet);
            RespUpdate respUpdate = new RespUpdate();
            List<FiedValue> list = new ArrayList<>();
            respUpdate.setId((int)pet.getPetId());
            PetBase base = pet.getPetBase();
            list.add(new FiedValue("hp_base", (byte)3, base.getHp_base()));
            list.add(new FiedValue("mp_base", (byte)3, base.getMp_base()));
            list.add(new FiedValue("ap_base", (byte)3, base.getAp_base()));
            list.add(new FiedValue("sp_base", (byte)3, base.getSp_base()));
            list.add(new FiedValue("grow_up", (byte)3, base.getGrow_up()));
            list.add(new FiedValue("grade_base", (byte)3, base.getGrade_base()));
            list.add(new FiedValue("score", (byte)3, pet.getScore()));
            list.add(new FiedValue("fly", (byte)1, Byte.valueOf(pet.getFly().byteValue())));
            list.add(new FiedValue("starter", (byte)1, Byte.valueOf((byte)pet.getFlyStarter())));
            respUpdate.setList(list);
            MessagePusher.pushMessage(role, respUpdate);
            pet.getDarkBaseMap().put(Integer.valueOf(reqSetShapeTemp.getType()), new ArrayList<>());
            pet.save();
        }
        else {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "未获取到宠物信息", false));
        }
    }
    
    public boolean addExp(Role role, Pet pet, long exp, boolean reason) {
        if (exp <= 0L) {
            return false;
        }
        boolean flag = true;
        int level = (int)pet.getPetLv();
        SysRbAttr sysRbattr = (SysRbAttr)PartDataPool.rbAttrMap.get(Integer.valueOf((int)pet.getRebirth() + 1));
        if (sysRbattr != null) {
            Integer lvLimit = sysRbattr.getLvLimit();
            int roleGrade = (int)role.getRelive() * 1000 + (int)role.getLevel();
            int petGrade = (int)pet.getRebirth() * 1000 + (int)pet.getPetLv();
            if (petGrade - roleGrade >= 10) {
                String msg = String.format("%s超过你的%d级%d级", pet.getPetName(), role.getLevel(), Integer.valueOf(10));
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
                return false;
            }
            if ((int)pet.getPetLv() >= (int)lvLimit && pet.getExp() >= DataGlobalFunc.storageExp((int)pet.getRebirth() * 1000 + (int)pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue())) {
                String msg = String.format("%s召唤兽已到达%d转的上限%d级", pet.getPetName(), pet.getRebirth(), lvLimit);
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), msg, false));
                return false;
            }
            String exp_tips = "#<G>" + exp + "#点经验";
            long storageExp = DataGlobalFunc.storageExp((int)pet.getRebirth() * 1000 + (int)pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
            long totalExp = (pet.getExp() + exp > storageExp) ? storageExp : (pet.getExp() + exp);
            long needExp = (long)Long.valueOf(FormulaHero.formula_petLvUpExp(lvLimit, pet.getPetLv(), Integer.valueOf((int)pet.getPetLv() + 1), pet.getRebirth()));
            if (totalExp >= needExp && (int)pet.getPetLv() < (int)lvLimit) {
                while (totalExp >= needExp) {
                    totalExp -= needExp;
                    ++level;
                    pet.setExp(totalExp);
                    storageExp = DataGlobalFunc.storageExp((int)pet.getRebirth() * 1000 + (int)pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
                    if (level > (int)lvLimit || (int)pet.getRebirth() * 1000 + level - roleGrade > 10) {
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
                    else {
                        pet.setPetLv(Integer.valueOf(level));
                        this.petLevelChangeEvent(role, pet);
                        needExp = (long)BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Long.valueOf(FormulaHero.formula_petLvUpExp(lvLimit, pet.getPetLv(), Integer.valueOf((int)pet.getPetLv() + 1), pet.getRebirth())));
                    }
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
        else {
            return false;
        }
    }
    
    public void updateExp(Role role, Pet pet) {
        RespUpdate respUpdate = new RespUpdate();
        List<FiedValue> list = new ArrayList<>();
        list.add(new FiedValue("exp", (byte)4, String.valueOf(pet.getExp())));
        respUpdate.setId((int)pet.getPetId());
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
    }
    
    public void petLevelChangeEvent(Role role, Pet pet) {
        pet.setGg(Integer.valueOf((int)pet.getGg() + 1));
        pet.setLx(Integer.valueOf((int)pet.getLx() + 1));
        pet.setLl(Integer.valueOf((int)pet.getLl() + 1));
        pet.setMj(Integer.valueOf((int)pet.getMj() + 1));
        pet.setFpoint(Integer.valueOf((int)pet.getFpoint() + 4));
        this.refreshPetLevelClose(pet);
        this.calculateBase((long)role.getId(), pet);
        this.getArenaScore((long)role.getId(), pet);
        this.sendPetPanels(role, pet);
    }
    
    public Map<Integer, Pet> selectPet(Role role) {
        Map<Integer, Pet> map = (Map)PetCache.PET_MAP.get(role.getId());
        if (map == null) {
            map = new HashMap();
            List<Pet> list = this.dao.query(Pet.class, Cnd.where("role_id", "=", role.getRoleId()));
            for (Pet pet : list) {
                pet.doAfterInit();
                map.put(pet.getPetId(), pet);
            }
            PetCache.PET_MAP.put(role.getId(), map);
            return map;
        }
        return map;
    }
    
    public void feedPet(Role role, Pet pet, int param, String type, int num, Object... args) {
        boolean clicker = this.roleService.mouserClicker(role, type, 250L);
        if (!clicker) {
            return;
        }
        if (!SafeUtil.isUnlock(role)) {
            return;
        }
        FeedPetHandler handler = FeedHelper.INSTANCE.getHandler(type);
        if (handler != null) {
            String tips = handler.acceptFeed(pet, role, Integer.valueOf(param), type, num, args);
            if (!Strings.isEmpty(tips)) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), tips, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
    
    public void assignCorrection(Role role, Pet pet, String nutMap) {
        Map<Integer, Integer> defMaxMap = ConvertDataPool.defPetCorrectionMaxMap.get(pet.getRebirth() + 1);
        Map<Integer, Integer> correctionMap = pet.getCorrection();
        PetCorrection correction = pet.getCorrectionInfo();
        NutMap map = Json.fromJson(NutMap.class, nutMap);
        List<Integer> list = PRACTICE_PET_PRO_DICT.toList();
        int count = 0;
        for (String key : map.keySet()) {
            Integer pro = Integer.valueOf(key);
            boolean contains = list.contains(pro);
            if (!contains) {
                MessagePusher.pushMessage(role, new RespMsg("修炼属性中没有该修炼类型"));
                return;
            }
            int num = map.getInt(key);
            if (num < 0) {
                return;
            }
            if (num > 0) {
                if (num > defMaxMap.get(pro)) {
                    MessagePusher.pushMessage(role, new RespMsg("修炼超过限值"));
                    return;
                }
                count += num - correctionMap.get(pro);
            }
        }
        if (count > correction.getPoints()) {
            MessagePusher.pushMessage(role, new RespMsg("剩余修炼点不足"));
            return;
        }
        for (String key : map.keySet()) {
            int num2 = map.getInt(key);
            if (num2 > 0) {
                Integer pro2 = Integer.valueOf(key);
                correctionMap.put(pro2, num2);
            }
        }
        int points = correction.getPoints() - count;
        correction.setPoints((points < 0) ? 0 : points);
        this.sendPetCorrection(role, pet);
        pet.save();
        role.save();
    }
    
    public void restAssignCorrection(Role role, Pet pet, byte type) {
        if (!this.checkRestExpend(role, pet, type)) {
            return;
        }
        pet.getCorrectionInfo().setPoints(pet.getCorrectionInfo().getLevel());
        pet.setCorrection(this.createCorrection());
        this.sendPetCorrection(role, pet);
        MessagePusher.pushMessage(role, new RespMsg("重置成功"));
        pet.save();
    }
    
    public void restAttribute(Role role, Pet pet, byte type) {
        Integer count = FormulaHero.restorePorCount(pet.getRebirth(), pet.getPetLv(), LOGIC.LOGICTYPE_PET.getValue());
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
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.getPetEffective((long)role.getId(), pet)));
    }
    
    public boolean checkRestExpend(Role role, Pet pet, byte type) {
        if (!SafeUtil.isUnlock(role)) {
            return false;
        }
        if ((int)pet.getRebirth() > 0) {
            if (type == 1) {
                boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 500000L, "重置宠物加点");
                if (!currency) {
                    return false;
                }
            }
            else {
                Integer goods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, 90020, 1, false, true, "重置宠物加点");
                if (goods != null) {
                    MessagePusher.pushMessage(role, new RespMsg("背包没有万兽灵符"));
                    return false;
                }
            }
        }
        return true;
    }
    
    public void relive(Role role, Integer serial) {
        Pet pet = this.selectPet(role).get(serial);
        if (pet != null) {
            if ((int)role.getRelive() > pet.getRebirth()) {
                if (pet.getRebirth() < 4) {
                    int needClose = Const.pet_relive[pet.getRebirth()];
                    if (pet.getPetClose() < (long)needClose) {
                        MessagePusher.pushMessage(role, new RespMsg("亲密度不足"));
                        return;
                    }
                    pet.setPetClose(pet.getPetClose() - (long)needClose);
                    SysRbAttr sysRbAttr = PartDataPool.rbAttrMap.get(pet.getRebirth() + 2);
                    int count = FormulaHero.restorePorCount((int)pet.getRebirth() + 1, sysRbAttr.getInitLv(), LOGIC.LOGICTYPE_PET.getValue());
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
    
    public void petFly(Role role, ReqPetFly reqPetFly) {
        Pet pet = this.selectPet(role).get(reqPetFly.getPetId());
        if (pet == null) {
            return;
        }
        if (pet.getFly() >= 3 || !DataGlobalFunc.checkPetFly(pet)) {
            MessagePusher.pushMessage(role, new RespHint(23, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升条件！"));
            return;
        }
        int count = DataGlobalFunc.getPetFlyCount(pet.getRebirth(), pet.getPetLv());
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
    
    public void petFlyStarter(Role role, ReqPetFlyStarter reqPetFlyStarter) {
        if (reqPetFlyStarter.getStarter() < 1 || reqPetFlyStarter.getStarter() > 4) {
            return;
        }
        Pet pet = this.selectPet(role).get(reqPetFlyStarter.getPetId());
        if (pet == null) {
            return;
        }
        if (pet.getFly() < 2) {
            MessagePusher.pushMessage(role, new RespHint(24, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("当前召唤兽不符合飞升调整！"));
            return;
        }
        boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_SILVER, 5000000L);
        if (flag && !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, 5000000L, "宠物飞升自定义初始值")) {
            MessagePusher.pushMessage(role, new RespHint(24, (short)0));
            return;
        }
        int fly = (pet.getFly() + 1 > 3) ? 3 : (pet.getFly() + 1);
        MessagePusher.pushMessage(role, new RespHint(24, (short)1));
        pet.setFly(fly);
        pet.setFlyStarter(reqPetFlyStarter.getStarter());
        this.calculateBase(role.getId(), pet);
        this.sendPetPanels(role, pet);
        pet.save();
        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.getPetEffective(role.getId(), pet)));
    }
    
    public void chatPetDetail(Role ownerRole, Role role, int petId) {
        Pet pet = (Pet)this.selectPet(ownerRole).get(Integer.valueOf(petId));
        if (pet == null) {
            MessagePusher.pushMessage(role, new RespMsg("链接已过期~"));
            return;
        }
        RespMarketPetCard card = new RespMarketPetCard();
        List<PetEquip> petEffective = this.getPetEffective((long)ownerRole.getId(), pet);
        card.setId(String.valueOf(pet.getPetId()));
        card.setRoleId((int)ownerRole.getRoleId());
        card.setSerial((int)pet.getPetType());
        card.setSkills(pet.getSkills());
        card.setStatus((byte)0);
        card.setEndTime(0);
        card.setFields(this.cardPetFied(ownerRole, pet, petEffective));
        List<FieldProp> fieldProps = this.petFieldPro(ownerRole, pet);
        if (fieldProps != null) {
            card.setProList(fieldProps);
        }
        if (petEffective.size() > 0) {
            List<PetEquipCard> equips = new ArrayList<>();
            for (PetEquip equip : petEffective) {
                equips.add(new PetEquipCard((long)equip.getId(), equip.getSerial(), equip.getProductOrder()));
            }
            card.setEquips(equips);
        }
        if (pet.getHorse() > 0L) {
            Map<Long, Horse> horseMap = this.horseService.getHorse(ownerRole);
            Horse horse = (Horse)horseMap.get(Long.valueOf(pet.getHorse()));
            if (horse != null) {
                List<HorseSkillCard> horseSkillCard = new ArrayList<>();
                String result = Joiner.on(",").withKeyValueSeparator(":").join(horse.getSkill());
                horseSkillCard.add(new HorseSkillCard(horse.getGrowUp(), horse.getLevel(), result));
                card.setHorseSkillCard(horseSkillCard);
            }
        }
        MessagePusher.pushMessage(role, card);
        return;
    }
    
    public List<FiedValue> cardPetFied(Role role, Pet pet, List<PetEquip> petEffective) {
        List<FiedValue> list = this.getPetFieds(role.getId(), pet, false);
        list.add(new FiedValue("name", (byte)4, pet.getPetName()));
        list.add(new FiedValue("correction", (byte)3, Integer.valueOf(pet.getCorrectionInfo().getLevel())));
        list.add(new FiedValue("at", (byte)3, Integer.valueOf(CountUtils.countPetPhyPower(pet, petEffective))));
        list.add(new FiedValue("sp", (byte)3, Integer.valueOf(CountUtils.countPetSpeed(pet, false))));
        list.add(new FiedValue("curr_grow_up", (byte)3, Integer.valueOf((int)(CountUtils.getPetGrowUp(pet, false) * 1000.0))));
        list.add(new FiedValue("max_grow_up", (byte)3, Integer.valueOf((int)(CountUtils.getPetGrowUp(pet, true) * 1000.0))));
        if (petEffective != null && petEffective.size() > 0) {
            byte acc = this.isOrnament((long)role.getId(), (SysPet)PetDataPool.petMap.get(pet.getPetType()), pet);
            if (acc > 0) {
                list.add(new FiedValue("acc", (byte)1, Byte.valueOf(acc)));
            }
        }
        return list;
    }
    
    public List<FieldProp> petFieldPro(Role role, Pet pet) {
        NutMap nutMap = CountUtils.proFieldValues(pet);
        if (nutMap != null) {
            CountUtils.restProFieldValue(role, pet, nutMap, new HashMap());
            HorseSkillUtils.countHorseSkillGain(nutMap, SkillFunc.initHorseSkill((long)role.getId(), pet.getHorse()));
            TianFuUtils.changePetPropery(role, pet, nutMap);
            Map<Integer, Integer> map = PRO_DICT_TYPE.toMap();
            List<Integer> wuXinDist = PRO_DICT_TYPE.getWuXinDist();
            List<FieldProp> proList = new ArrayList<>();
            for (String key : nutMap.keySet()) {
                Integer integer = Integer.valueOf(key);
                Integer type = (Integer)map.get(integer);
                if (type != null) {
                    if (type.equals(Integer.valueOf(PRO_TYPE.Pro_Value_PERCENT_TYPE.getValue()))) {
                        proList.add(new FieldProp(integer, Integer.valueOf((int)(nutMap.getDouble(key) * 1000.0))));
                    }
                    else if (type.equals(Integer.valueOf(PRO_TYPE.Pro_Value_NUM_TYPE.getValue()))) {
                        if (wuXinDist.contains(integer)) {
                            proList.add(new FieldProp(integer, Integer.valueOf((int)(nutMap.getDouble(key) * 100.0))));
                        }
                        else {
                            proList.add(new FieldProp(integer, Integer.valueOf(nutMap.getInt(key))));
                        }
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            return proList;
        }
        return null;
    }
    
    public List<FiedValue> getPetFieds(Long id, Pet pet, boolean flag) {
        List<FiedValue> list = new ArrayList<>();
        List<PetEquip> equips = new ArrayList<>();
        if (id != null) {
            equips = this.getPetEffective((long)id, pet);
        }
        list.add(new FiedValue("life", (byte)3, Integer.valueOf(CountUtils.countPetLife(pet, equips))));
        list.add(new FiedValue("mana", (byte)3, Integer.valueOf(CountUtils.countPetMana(pet, equips))));
        list.add(new FiedValue("pet_close", (byte)4, String.valueOf(pet.getPetClose())));
        PetBase base = pet.getPetBase();
        list.add(new FiedValue("hp_base", (byte)3, base.getHp_base()));
        list.add(new FiedValue("mp_base", (byte)3, base.getMp_base()));
        list.add(new FiedValue("ap_base", (byte)3, base.getAp_base()));
        list.add(new FiedValue("sp_base", (byte)3, base.getSp_base()));
        list.add(new FiedValue("grow_up", (byte)3, base.getGrow_up()));
        list.add(new FiedValue("grade_base", (byte)3, base.getGrade_base()));
        list.add(new FiedValue("level", (byte)2, Short.valueOf(pet.getPetLv().shortValue())));
        list.add(new FiedValue("relive", (byte)1, Byte.valueOf(pet.getRebirth().byteValue())));
        list.add(new FiedValue("score", (byte)3, pet.getScore()));
        list.add(new FiedValue("bone", (byte)1, Byte.valueOf(pet.getBone().byteValue())));
        list.add(new FiedValue("growth_dan", (byte)1, Byte.valueOf(pet.getGrowthDan().byteValue())));
        short[] sendPetColor = this.getSendPetColor(pet.getColorList());
        if (sendPetColor != null) {
            list.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
            list.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
        }
        short[] accColor = getSendPetAccColor(pet.getAccColor());
        if (accColor != null) {
            list.add(new FiedValue("accColor", (byte)2, Short.valueOf(accColor[0])));
            list.add(new FiedValue("accColorList", (byte)2, Short.valueOf(accColor[1])));
        }
        short[] accColorTwo = getSendPetAccColor(pet.getAccColorTwo());
        if (accColorTwo != null) {
            list.add(new FiedValue("accColor2", (byte)2, Short.valueOf(accColorTwo[0])));
            list.add(new FiedValue("accColorList2", (byte)2, Short.valueOf(accColorTwo[1])));
        }
        list.add(new FiedValue("fly", (byte)1, Byte.valueOf(pet.getFly().byteValue())));
        list.add(new FiedValue("awaken", (byte)2, Short.valueOf(pet.getAwaken())));
        this.petHf(pet, list);
        if (flag) {
            list.add(new FiedValue("gg", (byte)2, Short.valueOf(pet.getGg().shortValue())));
            list.add(new FiedValue("lx", (byte)2, Short.valueOf(pet.getLx().shortValue())));
            list.add(new FiedValue("ll", (byte)2, Short.valueOf(pet.getLl().shortValue())));
            list.add(new FiedValue("mj", (byte)2, Short.valueOf(pet.getMj().shortValue())));
            list.add(new FiedValue("fpoint", (byte)2, Short.valueOf(pet.getFpoint().shortValue())));
            list.add(new FiedValue("curr_life", (byte)3, Integer.valueOf(pet.getCurrLife())));
            list.add(new FiedValue("curr_mana", (byte)3, pet.getCurrMana()));
            list.add(new FiedValue("starter", (byte)1, Byte.valueOf((byte)pet.getFlyStarter())));
            return list;
        }
        return list;
    }
    
    public void petHf(Pet pet, List<FiedValue> list) {
        if (pet.getBrandNew().size() > 0) {
            String brandNew = Joiner.on(",").skipNulls().join(pet.getBrandNew().keySet());
            list.add(new FiedValue("list_hf", (byte)4, brandNew));
            short useHf = 0;
            for (Integer key : pet.getBrandNew().keySet()) {
                if ((byte)pet.getBrandNew().get(key) == 1) {
                    useHf = key.shortValue();
                    break;
                }
                continue;
            }
            list.add(new FiedValue("use_hf", (byte)2, Short.valueOf(useHf)));
        }
    }
    
    public byte isOrnament(long id, SysPet sysPet, Pet pet) {
        byte flag = 0;
        if (sysPet == null) {
            return flag;
        }
        Map<Long, PetEquip> equipMap = this.bagService.getPetBagEquip(id);
        if (equipMap.size() <= 0) {
            return flag;
        }
        List<PetEquip> collect = (List)equipMap.values().stream().filter(e -> pet.getId().equals(Long.valueOf(e.getPId())) && e.getProductOrder() >= ACC_GRADE.PRICELESS.getValue()).collect(Collectors.toList());
        if (collect.size() <= 0) {
            return flag;
        }
        if ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() && collect.size() >= 1) {
            flag = 1;
        }
        else if ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue() && collect.size() >= 2) {
            flag = 1;
        }
        else if (((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || (int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) && collect.size() >= 3) {
            flag = 1;
        }
        return flag;
    }
    
    public short[] getSendPetColor(List<Map<String, Byte>> colorList) {
        short[] colors = new short[2];
        if (colorList != null && colorList.size() > 0) {
            byte currentColor = 0;
            StringBuffer sb = new StringBuffer("00000000");
            for (Map<String, Byte> map : colorList) {
                if ((byte)map.get("is_use") == 1) {
                    currentColor = (byte)map.get("color");
                }
                if ((byte)map.get("color") == 0) {
                    continue;
                }
                sb.setCharAt((int)(Byte)map.get("color") - 1, '1');
            }
            String colorBanary = sb.reverse().toString();
            short returnColorList = Short.parseShort(colorBanary, 2);
            colors[0] = (short)currentColor;
            colors[1] = returnColorList;
            return colors;
        }
        return null;
    }
    
    public static short[] getSendPetAccColor(Map<Byte, Byte> colorList) {
        short[] colors = new short[2];
        if (colorList != null && colorList.size() > 0) {
            byte currentColor = 0;
            StringBuffer sb = new StringBuffer("0000000000");
            for (Byte key : colorList.keySet()) {
                if ((byte)colorList.get(key) == 1) {
                    currentColor = (byte)key;
                }
                sb.setCharAt((int)key - 1, '1');
            }
            String colorBanary = sb.reverse().toString();
            short returnColorList = Short.parseShort(colorBanary, 2);
            colors[0] = (short)currentColor;
            colors[1] = returnColorList;
            return colors;
        }
        return null;
    }
    
    public void changeColor(Role role, int petId, byte colorSerial) {
        Pet pet = (Pet)this.selectPet(role).get(Integer.valueOf(petId));
        if (pet == null) {
            return;
        }
        List<Map<String, Byte>> colorList = pet.getColorList();
        Optional<Map<String, Byte>> colorOption = colorList.stream().filter(map -> (byte)map.get("color") == colorSerial).findAny();
        if (!colorOption.isPresent()) {
            return;
        }
        int[] needYuanqizhuGB = this.getNeedYuanqizhuGB(pet, Byte.valueOf(colorSerial));
        int yuanqizhuNum = needYuanqizhuGB[0];
        int gongNum = needYuanqizhuGB[1];
        if (gongNum > 0) {
            boolean gongFlag = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, (long)gongNum, "宠物变更颜色");
            if (!gongFlag) {
                return;
            }
        }
        Integer yqzGoods = ((BagService)SpringUtils.getBean(BagService.class)).consumeBagGoods(role, 75194, yuanqizhuNum, true, true, "宠物变更颜色");
        if (yqzGoods != null) {
            return;
        }
        colorList.stream().forEach(map -> {
            Byte b = (Byte)map.put("is_use", Byte.valueOf((byte)0));
            return;
        });
        Map<String, Byte> stringByteMap = (Map)colorOption.get();
        stringByteMap.put("is_use", Byte.valueOf((byte)1));
        pet.save();
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId(petId);
        short[] sendPetColor = this.getSendPetColor(colorList);
        List<FiedValue> list = new ArrayList<>();
        if (sendPetColor != null) {
            list.add(new FiedValue("currentColor", (byte)2, Short.valueOf(sendPetColor[0])));
            list.add(new FiedValue("colorList", (byte)2, Short.valueOf(sendPetColor[1])));
        }
        respUpdate.setList(list);
        MessagePusher.pushMessage(role, respUpdate);
        return;
    }
    
    public void disassembleYuanqidan(Role role, Integer serial, Integer num) {
        if (num == null) {
            num = Integer.valueOf(1);
        }
        BagService bean = (BagService)SpringUtils.getBean(BagService.class);
        Map<Integer, SysDataStuff> data_stuff = ItemDataPool.data_stuff;
        SysDataStuff sysDataStuff = (SysDataStuff)data_stuff.get(serial);
        if (sysDataStuff == null) {
            return;
        }
        if ((int)sysDataStuff.getItemPJ() >= 5) {
            return;
        }
        Integer goods = bean.consumeBagGoods(role, (int)serial, (int)num, true, true, "宠物变更颜色");
        if (goods != null) {
            return;
        }
        Integer itemPJ = sysDataStuff.getItemPJ();
        Integer yqzNum = PetService.DICT_YUANQIDAN_PJ_ZHU.getNum(itemPJ);
        yqzNum = Integer.valueOf((int)yqzNum * (int)num);
        int i = bean.addGoods(role, 75194, (int)yqzNum, 0, true, false, "宠物变更颜色");
        return;
    }
    
    public void cutAccColor(Role role, ReqPetCutAccColor reqPetCutAccColor) {
        Pet pet = (Pet)this.selectPet(role).get(reqPetCutAccColor.getId());
        if (pet == null) {
            return;
        }
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        if (sysPet == null || sysPet.getAccColor() == null || sysPet.getAccColor().isEmpty()) {
            return;
        }
        Map<Byte, PetAccColor> petAccColorMap = (Map)PetDataPool.petAccColorMap.get(sysPet.getAccColor());
        if (petAccColorMap == null || petAccColorMap.get(Byte.valueOf(reqPetCutAccColor.getSerial())) == null) {
            return;
        }
        if (reqPetCutAccColor.getPos() > 1 && (int)sysPet.getLevelType() < PETLEVELTYPE.Pet_LevelType_GJSH.getValue()) {
            return;
        }
        if (!this.checkAccColor((long)role.getId(), pet, sysPet)) {
            return;
        }
        if (reqPetCutAccColor.getPos() == 1) {
            if (this.accColorConsume(role, (Byte)pet.getAccColor().get(Byte.valueOf(reqPetCutAccColor.getSerial())), petAccColorMap, reqPetCutAccColor.getSerial())) {
                for (Byte key : pet.getAccColor().keySet()) {
                    if ((byte)pet.getAccColor().get(key) == 1) {
                        pet.getAccColor().put(key, Byte.valueOf((byte)0));
                    }
                    else {
                        continue;
                    }
                }
                if (reqPetCutAccColor.getSerial() > 0) {
                    pet.getAccColor().put(Byte.valueOf(reqPetCutAccColor.getSerial()), Byte.valueOf((byte)1));
                }
                pet.save();
                this.cutAccColor((long)role.getId(), pet, reqPetCutAccColor.getPos());
            }
        }
        else if (reqPetCutAccColor.getPos() == 2 && this.accColorConsume(role, (Byte)pet.getAccColorTwo().get(Byte.valueOf(reqPetCutAccColor.getSerial())), petAccColorMap, reqPetCutAccColor.getSerial())) {
            for (Byte key : pet.getAccColorTwo().keySet()) {
                if ((byte)pet.getAccColorTwo().get(key) == 1) {
                    pet.getAccColorTwo().put(key, Byte.valueOf((byte)0));
                }
                else {
                    continue;
                }
            }
            if (reqPetCutAccColor.getSerial() > 0) {
                pet.getAccColorTwo().put(Byte.valueOf(reqPetCutAccColor.getSerial()), Byte.valueOf((byte)1));
            }
            pet.save();
            this.cutAccColor((long)role.getId(), pet, reqPetCutAccColor.getPos());
        }
        return;
    }
    
    public void cutAccColor(long id, Pet pet, byte pos) {
        RespUpdate respUpdate = new RespUpdate();
        respUpdate.setId((int)pet.getPetId());
        List<FiedValue> list = new ArrayList<>();
        if (pos == 1) {
            short[] accColor = getSendPetAccColor(pet.getAccColor());
            if (accColor != null) {
                list.add(new FiedValue("accColor", (byte)2, Short.valueOf(accColor[0])));
                list.add(new FiedValue("accColorList", (byte)2, Short.valueOf(accColor[1])));
            }
        }
        else if (pos == 2) {
            short[] accColorTwo = getSendPetAccColor(pet.getAccColorTwo());
            if (accColorTwo != null) {
                list.add(new FiedValue("accColor2", (byte)2, Short.valueOf(accColorTwo[0])));
                list.add(new FiedValue("accColorList2", (byte)2, Short.valueOf(accColorTwo[1])));
            }
        }
        respUpdate.setList(list);
        MessagePusher.pushMessage(id, respUpdate);
    }
    
    public boolean accColorConsume(Role role, Byte value, Map<Byte, PetAccColor> petAccColorMap, byte serial) {
        PetAccColor accColor = (PetAccColor)petAccColorMap.get(Byte.valueOf(serial));
        Map<Integer, Integer> map = accColor.getMat();
        if (value != null) {
            map = ((PetAccColor)petAccColorMap.get(Byte.valueOf(serial))).getSwitchMat();
        }
        for (Integer key : map.keySet()) {
            Integer amount = (Integer)map.get(key);
            Integer num = this.bagService.getBagGoods(role, (int)key, (int)amount);
            if (num != null) {
                MessagePusher.pushMessage(role, new RespMsg("您的染料不足"));
                return false;
            }
            continue;
        }
        if (accColor.getSilver() > 0 && !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, (long)accColor.getSilver(), "宠物饰品染色")) {
            return false;
        }
        for (Integer key : map.keySet()) {
            Integer amount = (Integer)map.get(key);
            Integer integer = this.bagService.consumeBagGoods(role, (int)key, (int)amount, true, true, "宠物饰品染色");
            if (integer != null) {
                MessagePusher.pushMessage(role, new RespMsg("道具扣除失败"));
                return false;
            }
            continue;
        }
        return true;
    }
    
    public boolean checkAccColor(long id, Pet pet, SysPet sysPet) {
        boolean flag = true;
        if ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_Senior.getValue() && pet.getPetClose() < 1000000L) {
            flag = false;
        }
        else if ((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_SS.getValue() && pet.getPetClose() < 2000000L) {
            flag = false;
        }
        else if (((int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_GJSH.getValue() || (int)sysPet.getLevelType() == PETLEVELTYPE.Pet_LevelType_TSSS.getValue()) && pet.getPetClose() < 10000000L) {
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
    
    private int[] getNeedYuanqizhuGB(Pet pet, Byte colorSerial) {
        int[] returnDatas = { 1, 0 };
        if ((byte)colorSerial == 0) {
            return returnDatas;
        }
        SysPet sysPet = (SysPet)PetDataPool.petMap.get(pet.getPetType());
        Integer levelType = sysPet.getLevelType();
        if (levelType == null) {
            levelType = Integer.valueOf(1);
        }
        int yuanqizhuNum = 6;
        int gongNum = 100000;
        switch ((int)levelType) {
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
        
        private DICT_YUANQIDAN_PJ_ZHU(Integer pj, Integer num) {
            this.pj = pj;
            this.num = num;
        }
        
        static Integer getNum(Integer pj) {
            Integer num = PetService.DICT_YUANQIDAN_PJ_ZHU.PJ_ONE.num;
            PetService.DICT_YUANQIDAN_PJ_ZHU[] values = values();
            int length = values.length;
            int i = 0;
            while (i < length) {
                PetService.DICT_YUANQIDAN_PJ_ZHU concise = values[i];
                if (concise.getPj().equals(pj)) {
                    num = concise.getNum();
                    break;
                }
                ++i;
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
