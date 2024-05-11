
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
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ChangeEquipEvent;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.equip.helper.UpgradeHandler;
import org.nutz.lang.Strings;
import com.kitty.system.game.equip.helper.UpgradeHelper;
import com.kitty.system.game.safe.util.SafeUtil;
import java.util.ConcurrentModificationException;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.init.domain.SysDataAcsry;
import com.kitty.system.init.domain.SysSinBieEq;
import com.kitty.system.init.domain.SysMagicalBieEq;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysNewBieEq;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.game.equip.domain.vo.EquipAlike;
import com.kitty.system.game.equip.utils.EquipUtils;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.fightnew.service.CountUtils;
import com.kitty.system.game.team.domain.Team;
import com.kitty.system.enums.common.ADORN;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.init.domain.SysEffect;
import com.kitty.system.init.pool.ItemDataPool;
import com.kitty.system.game.equip.domain.Effect;
import java.util.ArrayList;
import java.util.Collections;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.bag.message.RespFlyPetEqInfo;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.equip.domain.PetEquipEasy;
import java.util.Iterator;
import java.util.Map;
import com.kitty.system.game.equip.domain.PetEquip;
import java.util.List;
import com.kitty.mina.message.Message;
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
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        this.loadRoleEquip(role);
    }
    
    private void loadRoleEquip(final Role role) {
        this.refreshRoleEquip(role);
    }
    
    public void refreshRoleEquip(final Role role) {
        final List<WearEquip> wearEquips = this.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            final WearEquip wearEquip = wearEquips.get(i);
            final RespFlyInfo info = new RespFlyInfo();
            info.setEquip(new EquipEasy(wearEquip, role.getRoleId()));
            MessagePusher.pushMessage(role, info);
        }
    }
    
    public void refreshPetEquip(final Role role) {
        final Map<Long, PetEquip> petBagEquip = this.bagService.getPetBagEquip(role.getId());
        for (final Long key : petBagEquip.keySet()) {
            this.refreshPetEquip(role, petBagEquip.get(key));
        }
    }
    
    public void refreshPetEquip(final Role role, final PetEquip petEquip) {
        if (role != null && petEquip != null) {
            final PetEquipEasy petEquipEasy = new PetEquipEasy(petEquip);
            final Map<Integer, Pet> petMap = this.petService.selectPet(role);
            if (petEquip.getPId() > 0L) {
                final Pet pet = petMap.values().stream().filter(e -> e.getId().equals(petEquip.getPId())).findFirst().orElse(null);
                if (pet != null) {
                    petEquipEasy.setPetId(String.valueOf(pet.getPetId()));
                }
            }
            MessagePusher.pushMessage(role.getId(), new RespFlyPetEqInfo(petEquipEasy));
        }
    }
    
    public List<WearEquip> selectWearEquip(final Role role) {
        final List<WearEquip> equips = DataCache.EQUIP_MAP.get(role.getId());
        if (equips == null) {
            List<WearEquip> wearEquips = Collections.synchronizedList(this.dao.query(WearEquip.class, Cnd.where("r_id", "=", role.getId())));
            if (wearEquips == null) {
                wearEquips = Collections.synchronizedList(new ArrayList<>());
            }
            wearEquips.forEach(equip -> equip.doAfterInit());
            DataCache.EQUIP_MAP.put(role.getId(), wearEquips);
            return wearEquips;
        }
        return equips;
    }
    
    public List<Effect> selectEffect(final Role role) {
        List<Effect> effects = DataCache.EFFECT_MAP.get(role.getId());
        if (effects == null) {
            effects = this.dao.query(Effect.class, Cnd.where("r_id", "=", role.getId()));
            if (effects == null) {
                effects = new ArrayList<Effect>();
            }
            else {
                final Map<Integer, SysEffect> effectMap = ItemDataPool.effectMap;
                for (final Effect effect : effects) {
                    final SysEffect sysEffect = effectMap.get(effect.getSerial());
                    if (sysEffect != null) {
                        effect.setPart(sysEffect.getPart());
                    }
                }
            }
            DataCache.EFFECT_MAP.put(role.getId(), effects);
            return effects;
        }
        return effects;
    }
    
    public void handleOnEquip(final Role role, final BagEquip equip) {
        this.countEquipBaseAddition(role);
        if (equip.getId() == null) {
            return;
        }
        final RespFlyInfo info = new RespFlyInfo();
        info.setEquip(new EquipEasy(equip, role.getRoleId()));
        MessagePusher.pushMessage(role, info);
        if (equip.getPosition() == ADORN.WEAPON.getValue()) {
            final Team team = this.teamService.getTeam(role.getRoleId());
            if (team != null) {
                this.teamService.updateTeamList(role);
            }
        }
    }
    
    public void countEquipBaseAddition(final Role role) {
        final int life = CountUtils.countLife(role);
        final int mana = CountUtils.countMana(role);
        if (role.getCurrLife().equals(role.getMaxLife()) || role.getCurrLife() >= life) {
            role.setCurrLife(life);
        }
        if (role.getCurrMana().equals(role.getMaxMana()) || role.getCurrMana() >= mana) {
            role.setCurrMana(mana);
        }
        role.setMaxLife(life);
        role.setMaxMana(mana);
        this.roleService.refreshRoleInfo(role, false);
    }
    
    public void unEquip(final Role role, final long id, final boolean is) {
        final List<WearEquip> equips = this.selectWearEquip(role);
        final WearEquip wearEquip = equips.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (wearEquip != null) {
            final int pos = this.bagService.getPos(role, false);
            if (pos <= 0) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "背包已满，无法卸下装备", false));
                return;
            }
            final BagEquip equip = new BagEquip();
            BeanUtils.copyBeanProp(equip, wearEquip);
            equip.setPos(pos);
            this.bagService.addEquip(role, equip, false, "卸下装备");
            wearEquip.setDelete(true);
            wearEquip.save();
            final List<WearEquip> wearEquips = this.selectWearEquip(role);
            for (int i = 0; i < wearEquips.size(); ++i) {
                WearEquip e = wearEquips.get(i);
                if (e.getId().equals(wearEquip.getId())) {
                    wearEquips.remove(i);
                    break;
                }
            }
            if (equip.getPosition() == 1) {
                role.setWeaponIcon(0);
            }
            this.changeEquip(role, equip, wearEquip, is);
            this.broadcastService.sendUpdateAppear(role);
            role.save();
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("脱下装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", equip.getType(), equip.getType(), equip.getType(), equip.getName()), false));
        }
        else {
            this.logger.error("未找到要卸下的装备,角色编号{}，装备编号{}", role.getRoleId(), id);
        }
    }
    
    public void wearEquip(final Role role, final Long id, final boolean is) {
        final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        final BagEquip equip = bagEquipMap.get(id);
        if (equip != null && equip.getId() != null) {
            final String tips = this.wearEquipCheck(role, equip, true);
            if (tips == null) {
                final WearEquip wearEquip = this.getWearEquip(role, equip.getPosition());
                if (wearEquip != null && wearEquip.getId() != null) {
                    boolean flag = true;
                    if (wearEquip.getPosition() == ADORN.RING.getValue()) {
                        final List<WearEquip> wearEquips = this.selectWearEquip(role);
                        final List<WearEquip> list = wearEquips.stream().filter(e -> e.getPosition() == ADORN.RING.getValue()).collect(Collectors.toList());
                        if (list.size() < 2) {
                            this.deleteWearEquip(equip, bagEquipMap);
                            flag = false;
                        }
                    }
                    if (flag) {
                        final BagEquip eq = new BagEquip();
                        BeanUtils.copyBeanProp(eq, wearEquip);
                        this.switchoverEquipGem(role, equip, wearEquip);
                        eq.setPos(equip.getPos());
                        this.bagService.addEquip(role, eq, false, "替换装备");
                        equip.setDelete(true);
                        equip.save();
                        bagEquipMap.remove(equip.getId());
                        wearEquip.setDelete(true);
                        wearEquip.save();
                        final List<WearEquip> wearEquips2 = this.selectWearEquip(role);
                        for (int i = 0; i < wearEquips2.size(); ++i) {
                            final WearEquip e = wearEquips2.get(i);
                            if (e.getId().equals(wearEquip.getId())) {
                                wearEquips2.remove(i);
                                break;
                            }
                        }
                    }
                }
                else {
                    this.deleteWearEquip(equip, bagEquipMap);
                }
                final WearEquip wear = new WearEquip();
                equip.setPos(-1);
                BeanUtils.copyBeanProp(wear, equip);
                wear.setDelete(false);
                final List<WearEquip> wearEquips = this.selectWearEquip(role);
                wearEquips.add(wear);
                wear.save();
                if (equip.getPosition() == 1) {
                    role.setWeaponIcon(equip.getType());
                }
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("穿上装备#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", equip.getType(), equip.getType(), equip.getType(), equip.getName()), false));
                this.changeEquip(role, equip, wear, is);
                role.save();
            }
        }
        else {
            this.logger.error("未找到要穿戴的装备,角色编号{}，装备编号{}", role.getRoleId(), id);
        }
    }
    
    public void switchoverEquipGem(final Role role, final BagEquip bagEquip, final WearEquip wearEquip) {
        final int gemNumBag = DataGlobalFunc.getGemNum(bagEquip);
        if (gemNumBag > 0) {
            return;
        }
        final int gemNumWear = DataGlobalFunc.getGemNum(wearEquip);
        if (gemNumBag >= gemNumWear) {
            return;
        }
        final EquipAlike equipData = DataGlobalFunc.getEquipData(bagEquip.getType());
        if (equipData == null) {
            return;
        }
        if (gemNumBag >= equipData.getMaxEmbedGemCnt()) {
            return;
        }
        int gemNum = gemNumWear;
        final int maxEmbedGemCnt = equipData.getMaxEmbedGemCnt();
        final int residue = maxEmbedGemCnt - gemNumWear;
        if (residue < 0) {
            final Map<Integer, Integer> deductGem = DataGlobalFunc.getCalculateGem(equipData.getPos(), maxEmbedGemCnt);
            final Map<Integer, Integer> calculateGem = DataGlobalFunc.getCalculateGem(equipData.getPos(), gemNumWear);
            for (final Integer key : deductGem.keySet()) {
                Integer num = calculateGem.get(key);
                if (num == null) {
                    continue;
                }
                num -= deductGem.get(key);
                if (num <= 0) {
                    calculateGem.remove(key);
                }
                else {
                    final boolean checkPos = this.bagService.checkPos(role, key, num);
                    if (!checkPos) {
                        MessagePusher.pushMessage(role, new RespMsg("由于#<R>包裹空间不足#，#<R>不能#帮您自动转移宝石。请及时#<R>消理包裹#"));
                        return;
                    }
                    calculateGem.put(key, num);
                }
            }
            for (final Integer key : calculateGem.keySet()) {
                this.bagService.addGoods(role, key, calculateGem.get(key), 0, true, false, "切换装备宝石");
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
            if (e.getType().equals(6670)) {
                e.setValue(0);
                return;
            }
            else {
                return;
            }
        });
        final BagEquip eq = new BagEquip();
        BeanUtils.copyBeanProp(eq, wearEquip);
        final BagEquip equip = EquipUtils.gemChange(DataGlobalFunc.getEquipData(wearEquip.getType()), eq);
        BeanUtils.copyBeanProp(wearEquip, equip);
        final int finalGemNum = gemNum;
        bagEquip.getField().forEach(e -> {
            if (e.getType().equals(6670)) {
                e.setValue(finalGemNum);
                return;
            }
            else {
                return;
            }
        });
        EquipUtils.gemChange(equipData, bagEquip);
    }
    
    public void deleteWearEquip(final BagEquip equip, final Map<Long, BagEquip> bagEquipMap) {
        equip.setDelete(true);
        equip.save();
        bagEquipMap.remove(equip.getId());
    }
    
    public WearEquip getWearEquip(final Role role, final int pos) {
        final List<WearEquip> wearEquips = this.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            final WearEquip wearEquip = wearEquips.get(i);
            if (wearEquip.getPosition() == pos) {
                return wearEquip;
            }
        }
        return null;
    }
    
    public String wearEquipCheck(final Role role, final BagEquip equip, final boolean isHint) {
        if (equip == null) {
            return "穿戴条件不匹配";
        }
        final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(equip.getType());
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_EQPT.getValue()) {
            final SysNewBieEq bieEq = EquipDataPool.newBieEq.get(equip.getType());
            return this.wearEquipCheckLimit(role, equip, bieEq.getOwnerRole(), bieEq.getSex(), bieEq.getNeedGg(), bieEq.getNeedLx(), bieEq.getNeedLl(), bieEq.getNeedMj(), bieEq.getNeedRei(), bieEq.getNeedGrade(), isHint);
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()) {
            final SysSeniorBieEq bieEq2 = EquipDataPool.seniorBieEq.get(equip.getType());
            return this.wearEquipCheckLimit(role, equip, bieEq2.getOwnerRole(), bieEq2.getSex(), bieEq2.getNeedGg(), bieEq2.getNeedLx(), bieEq2.getNeedLl(), bieEq2.getNeedMj(), bieEq2.getNeedRei(), bieEq2.getNeedGrade(), isHint);
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) {
            final SysMagicalBieEq bieEq3 = EquipDataPool.magicalBieEq.get(equip.getType());
            return this.wearEquipCheckLimit(role, equip, bieEq3.getOwnerRole(), bieEq3.getSex(), bieEq3.getNeedGg(), bieEq3.getNeedLx(), bieEq3.getNeedLl(), bieEq3.getNeedMj(), bieEq3.getNeedRei(), bieEq3.getNeedGrade(), isHint);
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) {
            final SysSinBieEq bieEq4 = EquipDataPool.sinBieEq.get(equip.getType());
            return this.wearEquipCheckLimit(role, equip, bieEq4.getOwnerRole(), bieEq4.getSex(), bieEq4.getNeedGg(), bieEq4.getNeedLx(), bieEq4.getNeedLl(), bieEq4.getNeedMj(), bieEq4.getNeedRei(), bieEq4.getNeedGrade(), isHint);
        }
        if (type == ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) {
            final SysDataAcsry acsry = EquipDataPool.acsryMap.get(equip.getType());
            return this.wearEquipCheckLimit(role, equip, acsry.getOwnerRole(), acsry.getSex(), acsry.getNeedGg(), acsry.getNeedLx(), acsry.getNeedLl(), acsry.getNeedMj(), acsry.getNeedRei(), acsry.getNeedGrade(), isHint);
        }
        MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), "穿戴条件不匹配", false));
        return "穿戴条件不匹配";
    }
    
    public String wearEquipCheckLimit(final Role role, final BagEquip equip, final List<Integer> ownerRole, final Integer sex, final Integer gg, final Integer lx, final Integer ll, final Integer mj, final Integer relive, final Integer level, final boolean isHint) {
        String msg = null;
        if (!ownerRole.contains(0) && !ownerRole.contains(role.getSerial()) && !ownerRole.contains(role.getRace())) {
            msg = "种族不匹配";
        }
        if (sex != 0 && !sex.equals(role.getSex())) {
            msg = "性别不匹配";
        }
        if (role.getGg() + CountUtils.countExtraPro(role, PRO_DICT_TYPE.GG.getDist(), equip.getId()) < gg) {
            msg = "你的#<R>根骨属性#不足#<R>" + gg + "#";
        }
        if (role.getLx() + CountUtils.countExtraPro(role, PRO_DICT_TYPE.LX.getDist(), equip.getId()) < lx) {
            msg = "你的#<R>灵性属性#不足#<R>" + lx + "#";
        }
        if (role.getLl() + CountUtils.countExtraPro(role, PRO_DICT_TYPE.LL.getDist(), equip.getId()) < ll) {
            msg = "你的#<R>力量属性#不足#<R>" + ll + "#";
        }
        if (role.getMj() + CountUtils.countExtraPro(role, PRO_DICT_TYPE.MJ.getDist(), equip.getId()) < mj) {
            msg = "你的#<R>敏捷属性#不足#<R>" + mj + "#";
        }
        final int need = relive * 1000 + level;
        final int curve = role.getRelive() * 1000 + role.getLevel();
        if (curve < need) {
            msg = "你的#<R>角色等级#不足";
            if (relive > 0) {
                msg = msg + "#<R>" + relive + "转" + level + "级#";
            }
            else {
                msg = msg + "#<R>" + level + "级#";
            }
        }
        if (isHint) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(msg));
        }
        return msg;
    }
    
    public BagEquip addEquip(final Role role, final BagEquip equip, final int pos, final int bd, final String cause) {
        if (equip != null) {
            equip.setBd(bd);
            equip.setPos(pos);
            equip.setRId(role.getId());
            equip.setId(this.worker.nextId());
            this.bagService.addEquip(role, equip, true, cause);
            return equip;
        }
        return null;
    }
    
    public List<WearEquip> getEquipValid(final Role role, final boolean is) {
        final List<WearEquip> list = new ArrayList<WearEquip>();
        try {
            final List<WearEquip> wearEquips = this.selectWearEquip(role);
            for (final WearEquip wearEquip : wearEquips) {
                final BagEquip equip = new BagEquip();
                BeanUtils.copyBeanProp(equip, wearEquip);
                final int valid = this.checkEquipValid(role, equip, is);
                if (valid <= 0) {
                    continue;
                }
                list.add(wearEquip);
            }
        }
        catch (final ConcurrentModificationException ex) {}
        return list;
    }
    
    public int checkEquipValid(final Role role, final BagEquip equip, final boolean is) {
        final String tips = this.wearEquipCheck(role, equip, false);
        if (tips != null) {
            if (is) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format(tips + ", #<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#失效了", equip.getType(), equip.getType(), equip.getType(), equip.getName()), false));
            }
            return 0;
        }
        if (equip.getPosition() == ADORN.WEAPON.getValue()) {
            role.setWeaponIcon(equip.getType());
        }
        return 1;
    }
    
    public void checkEquipValid(final Role role) {
        final List<WearEquip> wearEquips = this.selectWearEquip(role);
        for (int i = 0; i < wearEquips.size(); ++i) {
            final WearEquip wearEquip = wearEquips.get(i);
            final BagEquip equip = new BagEquip();
            BeanUtils.copyBeanProp(equip, wearEquip);
            wearEquips.set(i, wearEquip);
            wearEquip.save();
            role.save();
            this.changeEquip(role, equip, wearEquip, false);
        }
    }
    
    public void upgradeEquip(final Role role, final int pos, final int type, final String param, final int operationType, final boolean flag, final String cause) {
        if (type != 11 && !SafeUtil.isUnlock(role)) {
            return;
        }
        final UpgradeHandler handler = UpgradeHelper.INSTANCE.getHandler(type);
        if (handler != null) {
            final String message = handler.acceptUpgrade(role, pos, operationType, param, cause);
            if (!Strings.isEmpty((CharSequence)message) && message != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), message, false));
            }
            else {
                role.save();
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
    
    public void changeEquip(final Role role, final BagEquip equip, final WearEquip wearEquip, final boolean is) {
        if (equip != null) {
            this.handleOnEquip(role, equip);
            this.broadcastService.handleOnEquip(role, equip);
        }
        if (is) {
            EventDispatcher.getInstance().fireEvent(new ChangeEquipEvent(EventType.CHANGE_EQUIP_WEAR, role, equip, wearEquip));
        }
    }
    
    public void addEffect(final Role role, final int serial, final int bd) {
        final SysEffect sysEffect = ItemDataPool.effectMap.get(serial);
        if (sysEffect == null) {
            return;
        }
        final List<Effect> effects = this.selectEffect(role);
        final Effect effect = new Effect(this.worker.nextId(), role.getId(), serial, (byte)0, bd, sysEffect.getPart());
        effects.add(effect);
        effect.save();
    }
    
    public void getEffect(final Role role) {
        final List<Effect> effects = this.selectEffect(role);
        final int time = new Long(System.currentTimeMillis() / 1000L).intValue();
        final RespEffect respEffect = new RespEffect();
        for (final Effect effect : effects) {
            if (effect.getTime() < time) {
                respEffect.getList().add(new EffectInfo(effect.getSerial(), effect.getState(), effect.getTime()));
            }
        }
        MessagePusher.pushMessage(role, respEffect);
    }
    
    public List<Integer> getWearEffect(final Role role) {
        final List<Integer> list = new ArrayList<Integer>();
        final List<Effect> effects = this.selectEffect(role);
        final List<Effect> collect = effects.stream().filter(e -> e.getState() == 1).collect(Collectors.toList());
        for (final Effect effect : collect) {
            list.add(effect.getSerial());
        }
        return list;
    }
    
    public void wearEffect(final Role role, final int serial) {
        final Map<Integer, SysEffect> effectMap = ItemDataPool.effectMap;
        final SysEffect effect = effectMap.get(serial);
        if (effect == null) {
            return;
        }
        final List<Effect> effects = this.selectEffect(role);
        final Effect orElse = effects.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (orElse == null) {
            return;
        }
        final List<Effect> collect = effects.stream().filter(e -> e.getState() == 1 && e.getPart() == effect.getPart()).collect(Collectors.toList());
        for (Effect e : collect) {
            e.setState((byte)0);
            e.save();
        }
        orElse.setState((byte)1);
        orElse.save();
        MessagePusher.pushMessage(role, new RespHint(26, (short)1));
        this.broadcastService.sendUpdateAppear(role);
        EventDispatcher.getInstance().fireEvent(new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
    }
    
    public void unEffect(final Role role, final int serial) {
        final Map<Integer, SysEffect> effectMap = ItemDataPool.effectMap;
        final SysEffect effect = effectMap.get(serial);
        if (effect == null) {
            return;
        }
        final List<Effect> effects = this.selectEffect(role);
        final Effect orElse = effects.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (orElse == null) {
            return;
        }
        orElse.setState((byte)0);
        orElse.save();
        MessagePusher.pushMessage(role, new RespHint(27, (short)1));
        this.broadcastService.sendUpdateAppear(role);
        EventDispatcher.getInstance().fireEvent(new RoleAspectChangeEvent(EventType.ROLE_ASPECT_CHANGE, role));
    }
    
    public void wearEquip(final Role role, final ReqPetEquip reqPetEquip) {
        final boolean clicker = this.roleService.mouserClicker(role, "eq", 250L);
        if (!clicker) {
            return;
        }
        if (!NumberUtil.isNumber(reqPetEquip.getId()) || !NumberUtil.isNumber(reqPetEquip.getEId())) {
            return;
        }
        final int petId = Integer.valueOf(reqPetEquip.getId());
        final long eId = Long.valueOf(reqPetEquip.getEId());
        final Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return;
        }
        final PetEquip equip = petEquipMap.values().stream().filter(e -> e.getId().equals(eId)).findFirst().orElse(null);
        if (equip != null && equip.getId() != null && equip.getPId() <= 0L) {
            final Pet pet = this.petService.selectPet(role).get(petId);
            if (pet == null) {
                return;
            }
            final SysPetAcsry sysPetAcsry = EquipDataPool.petAcsryMap.get(equip.getSerial());
            if (pet.getRebirth() * 1000 + pet.getPetLv() >= sysPetAcsry.getNeedRei() * 1000 + sysPetAcsry.getNeedGrade()) {
                final PetEquip wearEquip = petEquipMap.values().stream().filter(e -> e.getPosition() == sysPetAcsry.getPos() && pet.getId().equals(e.getPId())).findFirst().orElse(null);
                if (wearEquip != null) {
                    wearEquip.setPId(0L);
                    final int positionNum = wearEquip.getPositionNum() - 1;
                    wearEquip.setPositionNum((short)((positionNum < 0) ? 0 : positionNum));
                    wearEquip.save();
                    this.refreshPetEquip(role, wearEquip);
                }
                equip.setPId(pet.getId());
                equip.save();
                this.refreshPetEquip(role, equip);
                this.petService.calculateBase(role.getId(), pet);
                this.petService.getArenaScore(role.getId(), pet);
                this.petService.sendPetPanels(role, pet);
                pet.save();
            }
            else {
                final String tips = String.format("#<II%d>##<MI:%d,M:1,MP:%d,MT:%d,CI:%d>[%s]#", sysPetAcsry.getSerial(), 0, 0, sysPetAcsry.getSerial(), sysPetAcsry.getSerial(), sysPetAcsry.getName());
                final String msg = String.format("%s需要召唤兽达到#<R>%s转%d级#之后才可装备", tips, sysPetAcsry.getNeedRei(), sysPetAcsry.getNeedGrade());
                MessagePusher.pushMessage(role, new RespMsg(msg));
            }
        }
    }
    
    public void unEquip(final Role role, final ReqUnPetEquip reqUnPetEquip) {
        final int petId = Integer.valueOf(reqUnPetEquip.getId());
        final Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0) {
            return;
        }
        final Pet pet = this.petService.selectPet(role).get(petId);
        if (pet == null) {
            return;
        }
        final PetEquip equip = petEquipMap.values().stream().filter(e -> e.getPosition() == reqUnPetEquip.getPosition() && pet.getId().equals(e.getPId())).findFirst().orElse(null);
        if (equip != null && equip.getId() != null) {
            final int pos = this.bagService.getPos(role, true);
            if (pos <= 0) {
                MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                return;
            }
            equip.setPId(0L);
            final int positionNum = equip.getPositionNum() - 1;
            equip.setPositionNum((short)((positionNum < 0) ? 0 : positionNum));
            equip.save();
            this.refreshPetEquip(role, equip);
            this.petService.calculateBase(role.getId(), pet);
            this.petService.getArenaScore(role.getId(), pet);
            this.petService.sendPetPanels(role, pet);
            pet.save();
        }
    }
    
    public void repairPetAcsry(final Role role, final ReqRepairPetAcsry repairPetAcsry) {
        final Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0 || !NumberUtil.isNumber(repairPetAcsry.getId())) {
            return;
        }
        final Long id = Long.valueOf(repairPetAcsry.getId());
        final PetEquip equip = petEquipMap.get(id);
        if (equip == null || equip.getId() == null || equip.getPositionNum() >= 2) {
            return;
        }
        final int num = (repairPetAcsry.getNum() > 2) ? 2 : repairPetAcsry.getNum();
        final Long price = Const.PET_ACSRY_REPAIR_COST.get(equip.getProductOrder());
        if (this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, price * num, "宠物佩饰修复")) {
            final int positionNum = (equip.getPositionNum() <= 0) ? 0 : equip.getPositionNum();
            equip.setPositionNum((short)((positionNum + num > 2) ? 2 : (positionNum + num)));
            equip.save();
            this.refreshPetEquip(role, equip);
        }
    }
    
    public void petAcsryLock(final Role role, final ReqPetAcsryLock petAcsryLock) {
        if (petAcsryLock.getLock() < 0 || petAcsryLock.getLock() > 1) {
            return;
        }
        final Map<Long, PetEquip> petEquipMap = this.bagService.getPetBagEquip(role.getId());
        if (petEquipMap == null || petEquipMap.size() <= 0 || !NumberUtil.isNumber(petAcsryLock.getId())) {
            return;
        }
        final Long id = Long.valueOf(petAcsryLock.getId());
        final PetEquip equip = petEquipMap.get(id);
        if (equip == null || equip.getId() == null) {
            return;
        }
        if (equip.getLock() == petAcsryLock.getLock()) {
            return;
        }
        equip.setLock(petAcsryLock.getLock());
        equip.save();
        this.refreshPetEquip(role, equip);
    }
}
