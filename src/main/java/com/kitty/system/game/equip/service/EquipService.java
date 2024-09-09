package com.kitty.system.game.equip.service;

import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.equip.message.ReqPetAcsryLock;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.equip.message.ReqRepairPetAcsry;
import com.kitty.system.game.equip.message.ReqUnPetEquip;
import com.kitty.system.init.domain.SysPetAcsry;
import com.kitty.common.utils.NumberUtil;
import com.kitty.system.game.equip.message.ReqPetEquip;
import com.kitty.listener.event.RoleAspectChangeEvent;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.equip.message.vo.EffectInfo;
import com.kitty.system.game.equip.message.RespEffect;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.equip.helper.UpgradeHandler;
import org.nutz.lang.Strings;
import com.kitty.system.game.equip.helper.UpgradeHelper;
import com.kitty.system.game.safe.util.SafeUtil;
import java.util.ConcurrentModificationException;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.stream.Collectors;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.game.equip.domain.Effect;
import java.util.ArrayList;
import java.util.Collections;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.bag.message.RespFlyPetEqInfo;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.equip.domain.PetEquipEasy;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.equip.domain.EquipEasy;
import com.kitty.system.game.bag.message.RespFlyInfo;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.LoginEvent;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import org.nutz.dao.Dao;
import org.springframework.beans.factory.annotation.Autowired;
import com.kitty.common.thread.IdWorker;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class EquipService
{
    Logger logger;
    @Autowired
    private IdWorker worker;
    @Autowired
    private Dao dao;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PetService petService;
    @Autowired
    private BagService bagService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private BroadcastService broadcastService;
    
    public EquipService() {
        this.logger = LoggerFactory.getLogger(EquipService.class);
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        this.loadRoleEquip(role);
    }
    
    private void loadRoleEquip(Role role) {
        this.refreshRoleEquip(role);
    }
    
    public void refreshRoleEquip(Role role) {
        List<WearEquip> wearEquips = this.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            WearEquip wearEquip = (WearEquip)wearEquips.get(i);
            RespFlyInfo info = new RespFlyInfo();
            info.setEquip(new EquipEasy(wearEquip, (int)role.getRoleId()));
            MessagePusher.pushMessage(role, info);
        }
    }
    
    public void refreshPetEquip(Role role) {
        Map<Long, PetEquip> petBagEquip = this.bagService.getPetBagEquip((long)role.getId());
        for (Long key : petBagEquip.keySet()) {
            this.refreshPetEquip(role, (PetEquip)petBagEquip.get(key));
        }
    }
    
    public void refreshPetEquip(Role role, PetEquip petEquip) {
        if (role != null && petEquip != null) {
            PetEquipEasy petEquipEasy = new PetEquipEasy(petEquip);
            Map<Integer, Pet> petMap = this.petService.selectPet(role);
            if (petEquip.getPId() > 0L) {
                Pet pet = (Pet)petMap.values().stream().filter(e -> e.getId().equals(Long.valueOf(petEquip.getPId()))).findFirst().orElse(null);
                if (pet != null) {
                    petEquipEasy.setPetId(String.valueOf(pet.getPetId()));
                }
            }
            MessagePusher.pushMessage((long)role.getId(), new RespFlyPetEqInfo(petEquipEasy));
        }
    }
    
    public List<WearEquip> selectWearEquip(Role role) {
        List<WearEquip> equips = (List)DataCache.EQUIP_MAP.get(role.getId());
        if (equips == null) {
            List<WearEquip> wearEquips = Collections.synchronizedList(this.dao.query(WearEquip.class, Cnd.where("r_id", "=", role.getId())));
            if (wearEquips == null) {
                wearEquips = Collections.synchronizedList(new ArrayList<>());
            }
            wearEquips.forEach(equip -> equip.doAfterInit());
            DataCache.EQUIP_MAP.put(role.getId(), wearEquips);
            return wearEquips;
        }
        else {
            return equips;
        }
    }
    
    public List<Effect> selectEffect(Role role) {
        List<Effect> effects = (List)DataCache.EFFECT_MAP.get(role.getId());
        if (effects == null) {
            effects = this.dao.query(Effect.class, Cnd.where("r_id", "=", role.getId()));
            if (effects == null) {
                effects = new ArrayList<>();
            }
            else {
                Map<Integer, SysEffect> effectMap = ItemDataPool.effectMap;
                for (Effect effect : effects) {
                    SysEffect sysEffect = (SysEffect)effectMap.get(Integer.valueOf(effect.getSerial()));
                    if (sysEffect != null) {
                        effect.setPart(sysEffect.getPart());
                    }
                    else {
                        continue;
                    }
                }
            }
            DataCache.EFFECT_MAP.put(role.getId(), effects);
            return effects;
        }
        else {
            return effects;
        }
    }
    
    public void handleOnEquip(Role role, BagEquip equip) {
        this.countEquipBaseAddition(role);
        if (equip.getId() == null) {
            return;
        }
        RespFlyInfo info = new RespFlyInfo();
        info.setEquip(new EquipEasy(equip, (int)role.getRoleId()));
        MessagePusher.pushMessage(role, info);
        if (equip.getPosition() == ADORN.WEAPON.getValue()) {
            Team team = this.teamService.getTeam((int)role.getRoleId());
            if (team != null) {
                this.teamService.updateTeamList(role);
            }
        }
        return;
    }
    
    public void countEquipBaseAddition(Role role) {
        int life = CountUtils.countLife(role);
        int mana = CountUtils.countMana(role);
        if (role.getCurrLife().equals(role.getMaxLife()) || (int)role.getCurrLife() >= life) {
            role.setCurrLife(Integer.valueOf(life));
        }
        if (role.getCurrMana().equals(role.getMaxMana()) || (int)role.getCurrMana() >= mana) {
            role.setCurrMana(Integer.valueOf(mana));
        }
        role.setMaxLife(Integer.valueOf(life));
        role.setMaxMana(Integer.valueOf(mana));
        this.roleService.refreshRoleInfo(role, false);
    }
    
    public void unEquip(Role role, long id, boolean is) {
        List<WearEquip> equips = this.selectWearEquip(role);
        WearEquip wearEquip = (WearEquip)equips.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (wearEquip != null) {
            int pos = this.bagService.getPos(role, false);
            if (pos <= 0) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "背包已满，无法卸下装备", false));
                return;
            }
            BagEquip equip = new BagEquip();
            BeanUtils.copyBeanProp(equip, wearEquip);
            equip.setPos(pos);
            this.bagService.addEquip(role, equip, false, "卸下装备");
            wearEquip.setDelete(true);
            wearEquip.save();
            List<WearEquip> wearEquips = this.selectWearEquip(role);
            int i = 0;
            while (i < wearEquips.size()) {
            	WearEquip e = (WearEquip)wearEquips.get(i);
                if (e.getId().equals(wearEquip.getId())) {
                    wearEquips.remove(i);
                    break;
                }
                ++i;
            }
            if (equip.getPosition() == 1) {
                role.setWeaponIcon(0);
            }
            this.changeEquip(role, equip, wearEquip, is);
            this.broadcastService.sendUpdateAppear(role);
            role.save();
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("脱下装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName()), false));
        }
        else {
            this.logger.error("未找到要卸下的装备,角色编号{}，装备编号{}", role.getRoleId(), Long.valueOf(id));
        }
    }
    
    public void wearEquip(Role role, Long id, boolean is) {
        Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        BagEquip equip = (BagEquip)bagEquipMap.get(id);
        if (equip != null && equip.getId() != null) {
            String tips = this.wearEquipCheck(role, equip, true);
            if (tips == null) {
                WearEquip wearEquip = this.getWearEquip(role, equip.getPosition());
                if (wearEquip != null && wearEquip.getId() != null) {
                    boolean flag = true;
                    if (wearEquip.getPosition() == ADORN.RING.getValue()) {
                        List<WearEquip> wearEquips = this.selectWearEquip(role);
                        List<WearEquip> list = (List)wearEquips.stream().filter(e -> e.getPosition() == ADORN.RING.getValue()).collect(Collectors.toList());
                        if (list.size() < 2) {
                            this.deleteWearEquip(equip, bagEquipMap);
                            flag = false;
                        }
                    }
                    if (flag) {
                        BagEquip eq = new BagEquip();
                        BeanUtils.copyBeanProp(eq, wearEquip);
                        this.switchoverEquipGem(role, equip, wearEquip);
                        eq.setPos(equip.getPos());
                        this.bagService.addEquip(role, eq, false, "替换装备");
                        equip.setDelete(true);
                        equip.save();
                        bagEquipMap.remove(equip.getId());
                        wearEquip.setDelete(true);
                        wearEquip.save();
                        List<WearEquip> wearEquips2 = this.selectWearEquip(role);
                        int i = 0;
                        while (i < wearEquips2.size()) {
                            WearEquip e = (WearEquip)wearEquips2.get(i);
                            if (e.getId().equals(wearEquip.getId())) {
                                wearEquips2.remove(i);
                                break;
                            }
                            ++i;
                        }
                    }
                }
                else {
                    this.deleteWearEquip(equip, bagEquipMap);
                }
                WearEquip wear = new WearEquip();
                equip.setPos(-1);
                BeanUtils.copyBeanProp(wear, equip);
                wear.setDelete(false);
                List<WearEquip> wearEquips = this.selectWearEquip(role);
                wearEquips.add(wear);
                wear.save();
                if (equip.getPosition() == 1) {
                    role.setWeaponIcon(equip.getType());
                }
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("穿上装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName()), false));
                this.changeEquip(role, equip, wear, is);
                role.save();
            }
        }
        else {
            this.logger.error("未找到要穿戴的装备,角色编号{}，装备编号{}", role.getRoleId(), id);
        }
    }
    
    public void switchoverEquipGem(Role role, BagEquip bagEquip, WearEquip wearEquip) {
        int gemNumBag = DataGlobalFunc.getGemNum(bagEquip);
        if (gemNumBag > 0) {
            return;
        }
        int gemNumWear = DataGlobalFunc.getGemNum(wearEquip);
        if (gemNumBag >= gemNumWear) {
            return;
        }
        EquipAlike equipData = DataGlobalFunc.getEquipData(Integer.valueOf(bagEquip.getType()));
        if (equipData == null) {
            return;
        }
        if (gemNumBag >= equipData.getMaxEmbedGemCnt()) {
            return;
        }
        int gemNum = gemNumWear;
        int maxEmbedGemCnt = equipData.getMaxEmbedGemCnt();
        int residue = maxEmbedGemCnt - gemNumWear;
        if (residue < 0) {
            Map<Integer, Integer> deductGem = DataGlobalFunc.getCalculateGem(equipData.getPos(), maxEmbedGemCnt);
            Map<Integer, Integer> calculateGem = DataGlobalFunc.getCalculateGem(equipData.getPos(), gemNumWear);
            for (Integer key : deductGem.keySet()) {
                Integer num = (Integer)calculateGem.get(key);
                if (num == null) {
                    continue;
                }
                num = Integer.valueOf((int)num - (int)deductGem.get(key));
                if ((int)num <= 0) {
                    calculateGem.remove(key);
                }
                else {
                    boolean checkPos = this.bagService.checkPos(role, (int)key, (int)num);
                    if (!checkPos) {
                        MessagePusher.pushMessage(role, new RespMsg("由于#<R>包裹空间不足#，#<R>不能#帮您自动转移宝石。请及时#<R>消理包裹#"));
                        return;
                    }
                    calculateGem.put(key, num);
                }
            }
            for (Integer key : calculateGem.keySet()) {
                this.bagService.addGoods(role, (int)key, (int)calculateGem.get(key), 0, true, false, "切换装备宝石");
            }
            gemNum = maxEmbedGemCnt;
            if (calculateGem.size() > 0) {
                MessagePusher.pushMessage(role, new RespMsg("已自动帮您转移#<R>部分宝石#，多余的宝石已经放入#<R>包裹#"));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("已自动帮您转移宝石"));
        }
        wearEquip.getField().forEach(e -> {
            if (e.getType().equals(Integer.valueOf(6670))) {
                e.setValue(Integer.valueOf(0));
                return;
            }
            return;
        });
        BagEquip eq = new BagEquip();
        BeanUtils.copyBeanProp(eq, wearEquip);
        BagEquip equip = EquipUtils.gemChange(DataGlobalFunc.getEquipData(Integer.valueOf(wearEquip.getType())), eq);
        BeanUtils.copyBeanProp(wearEquip, equip);
        int finalGemNum = gemNum;
        bagEquip.getField().forEach(e -> {
            if (e.getType().equals(Integer.valueOf(6670))) {
                e.setValue(Integer.valueOf(finalGemNum));
                return;
            }
            return;
        });
        EquipUtils.gemChange(equipData, bagEquip);
        return;
    }
    
    public void deleteWearEquip(BagEquip equip, Map<Long, BagEquip> bagEquipMap) {
        equip.setDelete(true);
        equip.save();
        bagEquipMap.remove(equip.getId());
    }
    
    public WearEquip getWearEquip(Role role, int pos) {
        List<WearEquip> wearEquips = this.selectWearEquip(role);
        int i = 0;
        while (i < wearEquips.size()) {
            WearEquip wearEquip = (WearEquip)wearEquips.get(i);
            if (wearEquip.getPosition() == pos) {
                return wearEquip;
            }
            ++i;
        }
        return null;
    }
    
    public String wearEquipCheck(Role role, BagEquip equip, boolean isHint) {
        if (equip == null) {
            return "穿戴条件不匹配";
        }
        Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(equip.getType()));
        if ((int)type == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
            SysNewBieEq bieEq4 = (SysNewBieEq)EquipDataPool.newBieEq.get(Integer.valueOf(equip.getType()));
            return this.wearEquipCheckLimit(role, equip, bieEq4.getOwnerRole(), Integer.valueOf(bieEq4.getSex()), Integer.valueOf(bieEq4.getNeedGg()), Integer.valueOf(bieEq4.getNeedLx()), Integer.valueOf(bieEq4.getNeedLl()), Integer.valueOf(bieEq4.getNeedMj()), Integer.valueOf(bieEq4.getNeedRei()), Integer.valueOf(bieEq4.getNeedGrade()), isHint);
        }
        if ((int)type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
            SysSeniorBieEq bieEq3 = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(equip.getType()));
            return this.wearEquipCheckLimit(role, equip, bieEq3.getOwnerRole(), Integer.valueOf(bieEq3.getSex()), Integer.valueOf(bieEq3.getNeedGg()), Integer.valueOf(bieEq3.getNeedLx()), Integer.valueOf(bieEq3.getNeedLl()), Integer.valueOf(bieEq3.getNeedMj()), Integer.valueOf(bieEq3.getNeedRei()), Integer.valueOf(bieEq3.getNeedGrade()), isHint);
        }
        if ((int)type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
            SysMagicalBieEq bieEq2 = (SysMagicalBieEq)EquipDataPool.magicalBieEq.get(Integer.valueOf(equip.getType()));
            return this.wearEquipCheckLimit(role, equip, bieEq2.getOwnerRole(), Integer.valueOf(bieEq2.getSex()), Integer.valueOf(bieEq2.getNeedGg()), Integer.valueOf(bieEq2.getNeedLx()), Integer.valueOf(bieEq2.getNeedLl()), Integer.valueOf(bieEq2.getNeedMj()), Integer.valueOf(bieEq2.getNeedRei()), Integer.valueOf(bieEq2.getNeedGrade()), isHint);
        }
        if ((int)type == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
            SysSinBieEq bieEq = (SysSinBieEq)EquipDataPool.sinBieEq.get(Integer.valueOf(equip.getType()));
            return this.wearEquipCheckLimit(role, equip, bieEq.getOwnerRole(), Integer.valueOf(bieEq.getSex()), Integer.valueOf(bieEq.getNeedGg()), Integer.valueOf(bieEq.getNeedLx()), Integer.valueOf(bieEq.getNeedLl()), Integer.valueOf(bieEq.getNeedMj()), Integer.valueOf(bieEq.getNeedRei()), Integer.valueOf(bieEq.getNeedGrade()), isHint);
        }
        if ((int)type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
            SysDataAcsry acsry = (SysDataAcsry)EquipDataPool.acsryMap.get(Integer.valueOf(equip.getType()));
            return this.wearEquipCheckLimit(role, equip, acsry.getOwnerRole(), Integer.valueOf(acsry.getSex()), Integer.valueOf(acsry.getNeedGg()), Integer.valueOf(acsry.getNeedLx()), Integer.valueOf(acsry.getNeedLl()), Integer.valueOf(acsry.getNeedMj()), Integer.valueOf(acsry.getNeedRei()), Integer.valueOf(acsry.getNeedGrade()), isHint);
        }
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "穿戴条件不匹配", false));
        return "穿戴条件不匹配";
    }
    
    public String wearEquipCheckLimit(Role role, BagEquip equip, List<Integer> ownerRole, Integer sex, Integer gg, Integer lx, Integer ll, Integer mj, Integer relive, Integer level, boolean isHint) {
        String msg = null;
        if (!ownerRole.contains(Integer.valueOf(0)) && !ownerRole.contains(role.getSerial()) && !ownerRole.contains(role.getRace())) {
            msg = "种族不匹配";
        }
        if ((int)sex != 0 && !sex.equals(role.getSex())) {
            msg = "性别不匹配";
        }
        if ((double)(int)role.getGg() + CountUtils.countExtraPro(role, Integer.valueOf(PRO_DICT_TYPE.GG.getDist()), equip.getId()) < (double)(int)gg) {
            msg = "你的#<R>根骨属性#不足#<R>" + gg + "#";
        }
        if ((double)(int)role.getLx() + CountUtils.countExtraPro(role, Integer.valueOf(PRO_DICT_TYPE.LX.getDist()), equip.getId()) < (double)(int)lx) {
            msg = "你的#<R>灵性属性#不足#<R>" + lx + "#";
        }
        if ((double)(int)role.getLl() + CountUtils.countExtraPro(role, Integer.valueOf(PRO_DICT_TYPE.LL.getDist()), equip.getId()) < (double)(int)ll) {
            msg = "你的#<R>力量属性#不足#<R>" + ll + "#";
        }
        if ((double)(int)role.getMj() + CountUtils.countExtraPro(role, Integer.valueOf(PRO_DICT_TYPE.MJ.getDist()), equip.getId()) < (double)(int)mj) {
            msg = "你的#<R>敏捷属性#不足#<R>" + mj + "#";
        }
        int need = (int)relive * 1000 + (int)level;
        int curve = (int)role.getRelive() * 1000 + (int)role.getLevel();
        if (curve < need) {
            msg = "你的#<R>角色等级#不足";
            if ((int)relive > 0) {
                msg = msg + "#<R>" + relive + "转" + level + "级#";
            }
            else {
                msg = msg + "#<R>" + level + "级#";
            }
        }
        if (isHint) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(msg));
            return msg;
        }
        return msg;
    }
    
    public BagEquip addEquip(Role role, BagEquip equip, int pos, int bd, String cause) {
        if (equip != null) {
            equip.setBd(bd);
            equip.setPos(pos);
            equip.setRId((long)role.getId());
            equip.setId(Long.valueOf(this.worker.nextId()));
            this.bagService.addEquip(role, equip, true, cause);
            return equip;
        }
        return null;
    }
    
    public List<WearEquip> getEquipValid(Role role, boolean is) {
        List<WearEquip> list = new ArrayList<>();
        try {
            List<WearEquip> wearEquips = this.selectWearEquip(role);
            for (WearEquip wearEquip : wearEquips) {
                BagEquip equip = new BagEquip();
                BeanUtils.copyBeanProp(equip, wearEquip);
                int valid = this.checkEquipValid(role, equip, is);
                if (valid <= 0) {
                    continue;
                }
                list.add(wearEquip);
            }
        }
        catch (ConcurrentModificationException ex) {}
        return list;
    }
    
    public int checkEquipValid(Role role, BagEquip equip, boolean is) {
        String tips = this.wearEquipCheck(role, equip, false);
        if (tips != null) {
            if (is) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format(tips + ", #<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#失效了", Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), Integer.valueOf(equip.getType()), equip.getName()), false));
            }
            return 0;
        }
        else {
            if (equip.getPosition() == ADORN.WEAPON.getValue()) {
                role.setWeaponIcon(equip.getType());
            }
            return 1;
        }
    }
    
    public void checkEquipValid(Role role) {
        List<WearEquip> wearEquips = this.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            WearEquip wearEquip = (WearEquip)wearEquips.get(i);
            BagEquip equip = new BagEquip();
            BeanUtils.copyBeanProp(equip, wearEquip);
            wearEquips.set(i, wearEquip);
            wearEquip.save();
            role.save();
            this.changeEquip(role, equip, wearEquip, false);
        }
    }
    
    public void upgradeEquip(Role role, int pos, int type, String param, int operationType, boolean flag, String cause) {
        if (type != 11 && !SafeUtil.isUnlock(role)) {
            return;
        }
        UpgradeHandler handler = UpgradeHelper.INSTANCE.getHandler(Integer.valueOf(type));
        if (handler != null) {
            String message = handler.acceptUpgrade(role, Integer.valueOf(pos), Integer.valueOf(operationType), param, cause);
            if (!Strings.isEmpty(message) && message != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), message, false));
            }
            else {
                role.save();
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
        return;
    }
    
    public void changeEquip(Role role, BagEquip equip, WearEquip wearEquip, boolean is) {
        if (equip != null) {
            this.handleOnEquip(role, equip);
            this.broadcastService.handleOnEquip(role, equip);
        }
        if (is) {
            EventDispatcher.getInstance().fireEvent(new ChangeEquipEvent(EventType.CHANGE_EQUIP_WEAR, role, equip, wearEquip));
        }
    }
    
    public void addEffect(Role role, int serial, int bd) {
        SysEffect sysEffect = (SysEffect)ItemDataPool.effectMap.get(Integer.valueOf(serial));
        if (sysEffect == null) {
            return;
        }
        List<Effect> effects = this.selectEffect(role);
        Effect effect = new Effect(Long.valueOf(this.worker.nextId()), (long)role.getId(), serial, (byte)0, bd, sysEffect.getPart());
        effects.add(effect);
        effect.save();
        return;
    }
    
    public void getEffect(Role role) {
        List<Effect> effects = this.selectEffect(role);
        int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        RespEffect respEffect = new RespEffect();
        for (Effect effect : effects) {
            if (effect.getTime() < time) {
                respEffect.getList().add(new EffectInfo(effect.getSerial(), effect.getState(), effect.getTime()));
            }
            else {
                continue;
            }
        }
        MessagePusher.pushMessage(role, respEffect);
    }
    
    public List<Integer> getWearEffect(Role role) {
        List<Integer> list = new ArrayList<>();
        List<Effect> effects = this.selectEffect(role);
        List<Effect> collect = (List)effects.stream().filter(e -> e.getState() == 1).collect(Collectors.toList());
        for (Effect effect : collect) {
            list.add(Integer.valueOf(effect.getSerial()));
        }
        return list;
    }
    
    public void wearEffect(Role role, int serial) {
        Map<Integer, SysEffect> effectMap = ItemDataPool.effectMap;
        SysEffect effect = (SysEffect)effectMap.get(Integer.valueOf(serial));
        if (effect == null) {
            return;
        }
        List<Effect> effects = this.selectEffect(role);
        Effect orElse = (Effect)effects.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (orElse == null) {
            return;
        }
        List<Effect> collect = (List)effects.stream().filter(e -> e.getState() == 1 && e.getPart() == effect.getPart()).collect(Collectors.toList());
        Iterator<Effect> iterator = collect.iterator();
        while (iterator.hasNext()) {
        	Effect e = (Effect)iterator.next();
            e.setState((byte)0);
            e.save();
        }
        orElse.setState((byte)1);
        orElse.save();
        MessagePusher.pushMessage(role, new RespHint(26, (short)1));
        this.broadcastService.sendUpdateAppear(role);
        EventDispatcher.getInstance().fireEvent(new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
        return;
    }
    
    public void unEffect(Role role, int serial) {
        Map<Integer, SysEffect> effectMap = ItemDataPool.effectMap;
        SysEffect effect = (SysEffect)effectMap.get(Integer.valueOf(serial));
        if (effect == null) {
            return;
        }
        List<Effect> effects = this.selectEffect(role);
        Effect orElse = (Effect)effects.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (orElse == null) {
            return;
        }
        orElse.setState((byte)0);
        orElse.save();
        MessagePusher.pushMessage(role, new RespHint(27, (short)1));
        this.broadcastService.sendUpdateAppear(role);
        EventDispatcher.getInstance().fireEvent(new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
        return;
    }
    
    public void wearEquip(Role role, ReqPetEquip reqPetEquip) {
        boolean clicker = this.roleService.mouserClicker(role, "eq", 250L);
        if (!clicker) {
            return;
        }
        if (!NumberUtil.isNumber(reqPetEquip.getId()) || !NumberUtil.isNumber(reqPetEquip.getEId())) {
            return;
        }
        int petId = (int)Integer.valueOf(reqPetEquip.getId());
        long eId = (long)Long.valueOf(reqPetEquip.getEId());
        Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip((long)role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return;
        }
        PetEquip equip = (PetEquip)petEquipMap.values().stream().filter(e -> e.getId().equals(Long.valueOf(eId))).findFirst().orElse(null);
        if (equip != null && equip.getId() != null && equip.getPId() <= 0L) {
            Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(petId));
            if (pet == null) {
                return;
            }
            SysPetAcsry sysPetAcsry = (SysPetAcsry)EquipDataPool.petAcsryMap.get(Integer.valueOf(equip.getSerial()));
            if ((int)pet.getRebirth() * 1000 + (int)pet.getPetLv() >= sysPetAcsry.getNeedRei() * 1000 + sysPetAcsry.getNeedGrade()) {
                PetEquip wearEquip = (PetEquip)petEquipMap.values().stream().filter(e -> e.getPosition() == sysPetAcsry.getPos() && pet.getId().equals(Long.valueOf(e.getPId()))).findFirst().orElse(null);
                if (wearEquip != null) {
                    wearEquip.setPId(0L);
                    int positionNum = wearEquip.getPositionNum() - 1;
                    wearEquip.setPositionNum((short)((positionNum < 0) ? 0 : positionNum));
                    wearEquip.save();
                    this.refreshPetEquip(role, wearEquip);
                }
                equip.setPId((long)pet.getId());
                equip.save();
                this.refreshPetEquip(role, equip);
                this.petService.calculateBase((long)role.getId(), pet);
                this.petService.getArenaScore((long)role.getId(), pet);
                this.petService.sendPetPanels(role, pet);
                pet.save();
            }
            else {
                String tips = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", Integer.valueOf(sysPetAcsry.getSerial()), Integer.valueOf(0), Integer.valueOf(0), Integer.valueOf(sysPetAcsry.getSerial()), Integer.valueOf(sysPetAcsry.getSerial()), sysPetAcsry.getName());
                String msg = String.format("%s需要召唤兽达到#<R>%s转%d级#之后才可装备", tips, Integer.valueOf(sysPetAcsry.getNeedRei()), Integer.valueOf(sysPetAcsry.getNeedGrade()));
                MessagePusher.pushMessage(role, new RespMsg(msg));
            }
        }
        return;
    }
    
    public void unEquip(Role role, ReqUnPetEquip reqUnPetEquip) {
        int petId = (int)Integer.valueOf(reqUnPetEquip.getId());
        Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip((long)role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return;
        }
        Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(petId));
        if (pet == null) {
            return;
        }
        PetEquip equip = (PetEquip)petEquipMap.values().stream().filter(e -> e.getPosition() == reqUnPetEquip.getPosition() && pet.getId().equals(Long.valueOf(e.getPId()))).findFirst().orElse(null);
        if (equip != null && equip.getId() != null) {
            int pos = this.bagService.getPos(role, true);
            if (pos <= 0) {
                MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                return;
            }
            equip.setPId(0L);
            int positionNum = equip.getPositionNum() - 1;
            equip.setPositionNum((short)((positionNum < 0) ? 0 : positionNum));
            equip.save();
            this.refreshPetEquip(role, equip);
            this.petService.calculateBase((long)role.getId(), pet);
            this.petService.getArenaScore((long)role.getId(), pet);
            this.petService.sendPetPanels(role, pet);
            pet.save();
        }
        return;
    }
    
    public void repairPetAcsry(Role role, ReqRepairPetAcsry repairPetAcsry) {
        Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip((long)role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0 || !NumberUtil.isNumber(repairPetAcsry.getId())) {
            return;
        }
        Long id = Long.valueOf(repairPetAcsry.getId());
        PetEquip equip = (PetEquip)petEquipMap.get(id);
        if (equip == null || equip.getId() == null || equip.getPositionNum() >= 2) {
            return;
        }
        int num = (repairPetAcsry.getNum() > 2) ? 2 : repairPetAcsry.getNum();
        Long price = (Long)Const.PET_ACSRY_REPAIR_COST.get(Byte.valueOf(equip.getProductOrder()));
        if (this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, (long)price * (long)num, "宠物佩饰修复")) {
            int positionNum = (equip.getPositionNum() <= 0) ? 0 : equip.getPositionNum();
            equip.setPositionNum((short)((positionNum + num > 2) ? 2 : (positionNum + num)));
            equip.save();
            this.refreshPetEquip(role, equip);
        }
        return;
    }
    
    public void petAcsryLock(Role role, ReqPetAcsryLock petAcsryLock) {
        if (petAcsryLock.getLock() < 0 || petAcsryLock.getLock() > 1) {
            return;
        }
        Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip((long)role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0 || !NumberUtil.isNumber(petAcsryLock.getId())) {
            return;
        }
        Long id = Long.valueOf(petAcsryLock.getId());
        PetEquip equip = (PetEquip)petEquipMap.get(id);
        if (equip == null || equip.getId() == null) {
            return;
        }
        if (equip.getLock() == petAcsryLock.getLock()) {
            return;
        }
        equip.setLock(petAcsryLock.getLock());
        equip.save();
        this.refreshPetEquip(role, equip);
        return;
    }
}
