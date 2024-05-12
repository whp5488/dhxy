
package com.kitty.system.game.tianfu.service;

import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.common.utils.spring.SpringUtils;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import com.kitty.common.thread.IdGenerator;
import java.util.Random;
import java.util.Map;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import java.util.Collections;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.system.game.tianfu.message.RespTianFuDelete;
import com.kitty.record.RecordThread;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.util.Optional;
import com.kitty.system.game.tianfu.message.RespTianFuDebris;
import java.util.Iterator;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import java.util.ArrayList;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.mina.cache.DataCache;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import com.kitty.logs.LoggerFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class RoleTianFuService
{
    private final Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private RoleDayInCeService roleDayInCeService;
    
    public RoleTianFuService() {
        this.logger = LoggerFunction.RUNE.getLogger();
    }
    
    public void roleTianFuList(final Role role) {
        final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getStorePos() == 0).collect(Collectors.toList());
        final List<RoleTianFuClient> list = new ArrayList<RoleTianFuClient>();
        final RespTianFuList respTianFuList = new RespTianFuList((byte)0, list);
        if (roleTianFus == null || roleTianFus.size() <= 0) {
            MessagePusher.pushMessage(role, respTianFuList);
            return;
        }
        synchronized (roleTianFus) {
            for (final RoleTianFu roleTianFu : roleTianFus) {
                if (roleTianFu.getTianFuSerial() == 9997) {
                    continue;
                }
                final RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
                list.add(roleTianFuClient);
            }
        }
        MessagePusher.pushMessage(role, respTianFuList);
    }
    
    public void refreshTianFuDebris(final Role role) {
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null) {
            return;
        }
        final Optional<RoleTianFu> optional = roleTianFus.stream().filter(tianFu -> tianFu.getTianFuSerial() == 9997 && tianFu.getStorePos() == 0).findFirst();
        if (optional.isPresent()) {
            final RoleTianFu roleTianFu = optional.get();
            final RespTianFuDebris respTianFuDebris = new RespTianFuDebris(roleTianFu);
            MessagePusher.pushMessage(role, respTianFuDebris);
        }
    }
    
    public void roleTianFuSynthesis(final Role role, final Short num) {
        final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
        final Optional<RoleTianFu> optional = roleTianFus.stream().filter(tianFu -> tianFu.getTianFuSerial() == 9997).findFirst();
        final List<RoleTianFuClient> list = new ArrayList<RoleTianFuClient>();
        if (optional.isPresent()) {
            final RoleTianFu roleTianFuDebris = optional.get();
            if (roleTianFuDebris.getCount() < num * 10) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片数量不足"));
                return;
            }
            boolean flag = false;
            final int maxCount = 75 - roleTianFus.size();
            for (int i = 0; i < num; ++i) {
                if (i >= maxCount) {
                    flag = true;
                    break;
                }
                final RoleTianFu roleTianFu = this.debrisToTianFu(role);
                if (roleTianFu != null) {
                    final RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
                    list.add(roleTianFuClient);
                }
            }
            if (flag) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达到上限"));
            }
            final RespTianFuDebris respTianFuDebris = new RespTianFuDebris(roleTianFuDebris);
            MessagePusher.pushMessage(role, respTianFuDebris);
        }
        final RespTianFuList respTianFuList = new RespTianFuList((byte)1, list);
        MessagePusher.pushMessage(role, respTianFuList);
    }
    
    public void roleTianFuResolve(final Role role, final List<Long> ids) {
        final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getTianFuSerial() != 9997).collect(Collectors.toList());
        boolean flag = false;
        final List<String> deleteIds = new ArrayList<String>();
        for (final Long id : ids) {
            if (flag) {
                break;
            }
            for (int i = roleTianFus.size() - 1; i >= 0; --i) {
                if (id.equals(roleTianFus.get(i).getId())) {
                    if (!this.revoleToTianFu(roleTianFus.get(i))) {
                        flag = true;
                        try {
                            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)4, (byte)2, "分解天策符，编号：" + roleTianFus.get(i).getTianFuSerial() + ",等级：" + roleTianFus.get(i).getLevel() + ",品阶：" + roleTianFus.get(i).getProductOrder(), role.getMarking());
                        }
                        catch (final Exception e) {
                            this.logger.error("分解天策符存入mongodb出错！");
                        }
                        break;
                    }
                    deleteIds.add(roleTianFus.get(i).getId().toString());
                }
            }
        }
        if (flag) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
        }
        if (deleteIds.size() > 0) {
            MessagePusher.pushMessage(role, new RespTianFuDelete(deleteIds));
        }
        this.refreshTianFuDebris(role);
    }
    
    public void roleTianFuIdentify(final Role role, final List<Long> ids) {
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        final List<RoleTianFuClient> list = new ArrayList<RoleTianFuClient>();
        for (final Long id : ids) {
            for (int i = roleTianFus.size() - 1; i >= 0; --i) {
                if (id.equals(roleTianFus.get(i).getId())) {
                    final boolean flag = this.identifyTianFu(role, roleTianFus.get(i));
                    if (flag) {
                        final RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFus.get(i));
                        list.add(roleTianFuClient);
                    }
                }
            }
        }
        final RespTianFuList respTianFuList = new RespTianFuList((byte)2, list);
        MessagePusher.pushMessage(role, respTianFuList);
    }
    
    public List<RoleTianFu> getDataByRoleId(final Long roleId) {
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(roleId);
        if (roleTianFus == null) {
            roleTianFus = this.dao.query(RoleTianFu.class, Cnd.where("role_id", "=", roleId));
            roleTianFus = Collections.synchronizedList(roleTianFus);
            if (roleTianFus == null) {
                roleTianFus = Collections.synchronizedList(new ArrayList<RoleTianFu>());
            }
            else {
                RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(roleId);
                if (roleDayInCe == null) {
                    roleDayInCe = this.roleDayInCeService.getDataForRoleId(roleId);
                }
                final Map<Long, String> tianFuGridMap = TianFuUtils.getTianFuGridMap(roleDayInCe);
                if (tianFuGridMap.size() > 0) {
                    for (int i = 0; i < roleTianFus.size(); ++i) {
                        final RoleTianFu roleTianFu = roleTianFus.get(i);
                        final SysTianFuSkill sysTianFuSkill = SkillDataPool.tianFuSkillMap.get(roleTianFu.getTianFuSerial());
                        if (sysTianFuSkill != null) {
                            roleTianFu.setTriggerType(sysTianFuSkill.getOpportunity());
                        }
                        for (final Map.Entry<Long, String> entry : tianFuGridMap.entrySet()) {
                            if (entry.getKey().equals(roleTianFu.getId())) {
                                roleTianFu.setDayInCeGrid(entry.getValue());
                                break;
                            }
                        }
                    }
                }
            }
            DataCache.ROLE_TIAN_FU.put(roleId, roleTianFus);
        }
        return roleTianFus;
    }
    
    @Transactional
    public RoleTianFu debrisToTianFu(final Role role) {
        final byte num = 10;
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null || roleTianFus.size() <= 0) {
            return null;
        }
        final Optional<RoleTianFu> optional = roleTianFus.stream().filter(obj -> obj.getTianFuSerial() == 9997).findFirst();
        if (!optional.isPresent()) {
            return null;
        }
        final RoleTianFu roleTianFuDebris = optional.get();
        if (roleTianFuDebris.getCount() < num) {
            return null;
        }
        final Random random = new Random();
        final List<SysTianFuSkill> values = SkillDataPool.tianFuSkillMap.values().stream().filter(obj -> obj.getRace().contains((byte)(int)role.getRace())).collect(Collectors.toList());
        final int index = random.nextInt(values.size());
        final SysTianFuSkill sysTianFuSkill = values.get(index);
        if (sysTianFuSkill != null) {
            final RoleTianFu roleTianFu = new RoleTianFu();
            roleTianFu.setId(IdGenerator.getNextId());
            roleTianFu.setRoleId(role.getId());
            roleTianFu.setCategory(sysTianFuSkill.getCategory());
            roleTianFu.setLayer(sysTianFuSkill.getLayer());
            roleTianFu.setIsWear(0);
            roleTianFu.setTianFuSerial(sysTianFuSkill.getCeSerial());
            roleTianFu.setToIdentifyCeSerial(sysTianFuSkill.getToIdentifyCeSerial());
            roleTianFu.setTriggerType(sysTianFuSkill.getOpportunity());
            roleTianFu.setCreateTime(new Date());
            roleTianFu.save();
            DataCache.ROLE_TIAN_FU.get(role.getId()).add(roleTianFu);
            try {
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)4, (byte)1, "合成天策符，编号：" + roleTianFu.getTianFuSerial() + ",等级：" + roleTianFu.getLevel() + ",品阶：" + roleTianFu.getProductOrder(), role.getMarking());
            }
            catch (final Exception e) {
                this.logger.error("合成天策符存入mongodb出错！");
            }
            if (roleTianFuDebris.getCount() - num <= 0) {
                roleTianFuDebris.setCount((short)0);
                roleTianFuDebris.setDelete(true);
                DataCache.ROLE_TIAN_FU.get(role.getId()).remove(roleTianFuDebris);
            }
            else {
                roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() - num));
            }
            roleTianFuDebris.save();
            return roleTianFu;
        }
        return null;
    }
    
    private boolean revoleToTianFu(final RoleTianFu roleTianFu) {
        byte num = 1;
        if (roleTianFu.getProductOrder() == 2) {
            num = 5;
        }
        else if (roleTianFu.getProductOrder() == 3) {
            num = 100;
        }
        final Optional<RoleTianFu> optional = DataCache.ROLE_TIAN_FU.get(roleTianFu.getRoleId()).stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() == 0).findFirst();
        RoleTianFu roleTianFuDebris;
        if (!optional.isPresent()) {
            roleTianFuDebris = new RoleTianFu();
            roleTianFuDebris.setId(IdGenerator.getNextId());
            roleTianFuDebris.setRoleId(roleTianFu.getRoleId());
            roleTianFuDebris.setCreateTime(new Date());
            roleTianFuDebris.setTianFuSerial(9997);
            roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() + num));
            DataCache.ROLE_TIAN_FU.get(roleTianFu.getRoleId()).add(roleTianFuDebris);
        }
        else {
            roleTianFuDebris = optional.get();
            if (roleTianFuDebris.getCount() + num > 9999) {
                return false;
            }
            roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() + num));
        }
        roleTianFuDebris.save();
        DataCache.ROLE_TIAN_FU.get(roleTianFu.getRoleId()).remove(roleTianFu);
        roleTianFu.setDelete(true);
        roleTianFu.save();
        return true;
    }
    
    @Transactional
    public boolean identifyTianFu(final Role role, final RoleTianFu roleTianFu) {
        final RoleService roleService = SpringUtils.getRoleService();
        if (!roleService.checkCurrency(role, Const.RESTYPE_COIN, 50000L)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("师贡数量不足"));
            return false;
        }
        roleService.deductCurrency(role, Const.RESTYPE_COIN, 50000L, "鉴定天策符");
        final SysTianFuSkill sysTianFuSkill = SkillDataPool.tianFuSkillMap.get(roleTianFu.getTianFuSerial());
        if (sysTianFuSkill == null) {
            return false;
        }
        final List<Integer> pointList = new ArrayList<Integer>();
        for (final short point : sysTianFuSkill.getPointWeight()) {
            pointList.add((int)point);
        }
        int level = LotteryUtils.lotteryIntEasy(pointList, sysTianFuSkill.getBaoDiWeight(), true) + 1;
        level = Math.min(10, level);
        roleTianFu.setLevel((byte)level);
        final List<Byte> rarityJudges = new ArrayList<Byte>();
        for (int i = 0; i < sysTianFuSkill.getRarityJudge().size(); ++i) {
            if (i != 0) {
                rarityJudges.add(sysTianFuSkill.getRarityJudge().get(i));
            }
        }
        for (int i = rarityJudges.size() - 1; i >= 0; --i) {
            if (rarityJudges.get(i) <= level) {
                roleTianFu.setProductOrder((byte)(i + 1));
                break;
            }
        }
        roleTianFu.setToIdentifyCeSerial(1);
        try {
            roleTianFu.save();
            return true;
        }
        catch (final Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void updateRole(final RoleTianFu roleTianFu, final Long targetRoleId) {
        final long oldRoleId = roleTianFu.getRoleId();
        roleTianFu.setRoleId(targetRoleId);
        roleTianFu.setIsWear(0);
        roleTianFu.save();
        DataCache.ROLE_TIAN_FU.get(oldRoleId).remove(roleTianFu);
        DataCache.ROLE_TIAN_FU.get(targetRoleId).add(roleTianFu);
    }
    
    public boolean delRune(final Role role, final long id) {
        final List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
        final RoleTianFu rune = list.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (rune == null) {
            return false;
        }
        DataCache.ROLE_TIAN_FU.get(role.getId()).remove(rune);
        rune.setDelete(true);
        rune.save();
        return true;
    }
    
    public boolean putTianFu(final Role role, final int serial, final int num) {
        final short numShort = (short)num;
        if (9997 == serial) {
            final Optional<RoleTianFu> optional = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst();
            RoleTianFu roleTianFuDebris;
            if (optional.isPresent()) {
                roleTianFuDebris = optional.get();
                if (roleTianFuDebris.getCount() + numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
                roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() + numShort));
            }
            else {
                final List<RoleTianFu> tianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (tianFus.size() >= 75) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量超出上限"));
                    return false;
                }
                if (numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
                roleTianFuDebris = new RoleTianFu();
                roleTianFuDebris.setId(IdGenerator.getNextId());
                roleTianFuDebris.setRoleId(role.getId());
                roleTianFuDebris.setTianFuSerial(9997);
                roleTianFuDebris.setCount(numShort);
                roleTianFuDebris.setCreateTime(new Date());
                DataCache.ROLE_TIAN_FU.get(role.getId()).add(roleTianFuDebris);
            }
            roleTianFuDebris.save();
            this.refreshTianFuDebris(role);
        }
        else {
            for (int i = 0; i < num; ++i) {
                final Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
                final SysTianFuSkill sysTianFuSkill = tianFuSkillMap.get(serial);
                if (sysTianFuSkill == null) {
                    return false;
                }
                final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (roleTianFus.size() >= 75) {
                    return false;
                }
                final RoleTianFu roleTianFu = new RoleTianFu();
                roleTianFu.setId(IdGenerator.getNextId());
                roleTianFu.setRoleId(role.getId());
                roleTianFu.setCategory(sysTianFuSkill.getCategory());
                roleTianFu.setLayer(sysTianFuSkill.getLayer());
                roleTianFu.setTianFuSerial(serial);
                roleTianFu.setToIdentifyCeSerial(sysTianFuSkill.getToIdentifyCeSerial());
                roleTianFu.setCreateTime(new Date());
                roleTianFu.setTriggerType(sysTianFuSkill.getOpportunity());
                roleTianFu.save();
                DataCache.ROLE_TIAN_FU.get(role.getId()).add(roleTianFu);
                this.roleTianFuList(role);
            }
        }
        return true;
    }
    
    public boolean checkTianFu(final Role role, final int serial, final int num) {
        final short numShort = (short)num;
        if (9997 == serial) {
            final Optional<RoleTianFu> optional = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst();
            if (optional.isPresent()) {
                final RoleTianFu roleTianFuDebris = optional.get();
                if (roleTianFuDebris.getCount() + numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
            }
            else {
                final List<RoleTianFu> tianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (tianFus.size() >= 75) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量超出上限"));
                    return false;
                }
                if (numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
            }
        }
        else {
            for (int i = 0; i < num; ++i) {
                final Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
                final SysTianFuSkill sysTianFuSkill = tianFuSkillMap.get(serial);
                if (sysTianFuSkill == null) {
                    return false;
                }
            }
            final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            if (roleTianFus.size() + num > 75) {
                return false;
            }
        }
        return true;
    }
}
