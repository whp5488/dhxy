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
import org.nutz.dao.Cnd;
import com.kitty.system.game.tianfu.message.RespTianFuDelete;
import com.kitty.record.RecordThread;
import com.kitty.system.common.message.RespNotifyMiscEx;
import java.util.Optional;
import com.kitty.system.game.tianfu.message.RespTianFuDebris;
import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import java.util.ArrayList;
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
    
    public void roleTianFuList(Role role) {
        List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getStorePos() == 0).collect(Collectors.toList());
        List<RoleTianFuClient> list = new ArrayList<>();
        RespTianFuList respTianFuList = new RespTianFuList((byte)0, list);
        if (roleTianFus == null || roleTianFus.size() <= 0) {
            MessagePusher.pushMessage(role, respTianFuList);
            return;
        }
        synchronized (roleTianFus) {
            for (RoleTianFu roleTianFu : roleTianFus) {
                if (roleTianFu.getTianFuSerial() == 9997) {
                    continue;
                }
                RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
                list.add(roleTianFuClient);
            }
        }
        MessagePusher.pushMessage(role, respTianFuList);
        return;
    }
    
    public void refreshTianFuDebris(Role role) {
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null) {
            return;
        }
        Optional<RoleTianFu> optional = roleTianFus.stream().filter(tianFu -> tianFu.getTianFuSerial() == 9997 && tianFu.getStorePos() == 0).findFirst();
        if (optional.isPresent()) {
            RoleTianFu roleTianFu = (RoleTianFu)optional.get();
            RespTianFuDebris respTianFuDebris = new RespTianFuDebris(roleTianFu);
            MessagePusher.pushMessage(role, respTianFuDebris);
        }
        return;
    }
    
    public void roleTianFuSynthesis(Role role, Short num) {
        List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
        Optional<RoleTianFu> optional = roleTianFus.stream().filter(tianFu -> tianFu.getTianFuSerial() == 9997).findFirst();
        List<RoleTianFuClient> list = new ArrayList<>();
        if (optional.isPresent()) {
            RoleTianFu roleTianFuDebris = (RoleTianFu)optional.get();
            if (roleTianFuDebris.getCount() < (short)num * 10) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片数量不足"));
                return;
            }
            boolean flag = false;
            int maxCount = 75 - roleTianFus.size();
            int i = 0;
            while (i < (short)num) {
                if (i >= maxCount) {
                    flag = true;
                    break;
                }
                RoleTianFu roleTianFu = this.debrisToTianFu(role);
                if (roleTianFu != null) {
                    RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
                    list.add(roleTianFuClient);
                }
                ++i;
            }
            if (flag) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量已达到上限"));
            }
            RespTianFuDebris respTianFuDebris = new RespTianFuDebris(roleTianFuDebris);
            MessagePusher.pushMessage(role, respTianFuDebris);
        }
        RespTianFuList respTianFuList = new RespTianFuList((byte)1, list);
        MessagePusher.pushMessage(role, respTianFuList);
    }
    
    public void roleTianFuResolve(Role role, List<Long> ids) {
        List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getTianFuSerial() != 9997).collect(Collectors.toList());
        boolean flag = false;
        List<String> deleteIds = new ArrayList<>();
        for (Long id : ids) {
            if (flag) {
                break;
            }
            for (int i = roleTianFus.size() - 1; i >= 0; --i) {
                if (id.equals(((RoleTianFu)roleTianFus.get(i)).getId())) {
                    if (!this.revoleToTianFu((RoleTianFu)roleTianFus.get(i))) {
                        flag = true;
                        try {
                            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)4, (byte)2, "分解天策符，编号：" + ((RoleTianFu)roleTianFus.get(i)).getTianFuSerial() + ",等级：" + (int)((RoleTianFu)roleTianFus.get(i)).getLevel() + ",品阶：" + (int)((RoleTianFu)roleTianFus.get(i)).getProductOrder(), role.getMarking());
                        }
                        catch (Exception e) {
                            this.logger.error("分解天策符存入mongodb出错！");
                        }
                        break;
                    }
                    deleteIds.add(((RoleTianFu)roleTianFus.get(i)).getId().toString());
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
    
    public void roleTianFuIdentify(Role role, List<Long> ids) {
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        List<RoleTianFuClient> list = new ArrayList<>();
        for (Long id : ids) {
            for (int i = roleTianFus.size() - 1; i >= 0; --i) {
                if (id.equals(((RoleTianFu)roleTianFus.get(i)).getId())) {
                    boolean flag = this.identifyTianFu(role, (RoleTianFu)roleTianFus.get(i));
                    if (flag) {
                        RoleTianFuClient roleTianFuClient = new RoleTianFuClient((RoleTianFu)roleTianFus.get(i));
                        list.add(roleTianFuClient);
                    }
                }
            }
        }
        RespTianFuList respTianFuList = new RespTianFuList((byte)2, list);
        MessagePusher.pushMessage(role, respTianFuList);
    }
    
    public List<RoleTianFu> getDataByRoleId(Long roleId) {
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(roleId);
        if (roleTianFus == null) {
            roleTianFus = this.dao.query(RoleTianFu.class, Cnd.where("role_id", "=", roleId));
            roleTianFus = Collections.synchronizedList(roleTianFus);
            if (roleTianFus == null) {
                roleTianFus = Collections.synchronizedList(new ArrayList<>());
            }
            else {
                RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(roleId);
                if (roleDayInCe == null) {
                    roleDayInCe = this.roleDayInCeService.getDataForRoleId(roleId);
                }
                Map<Long, String> tianFuGridMap = TianFuUtils.getTianFuGridMap(roleDayInCe);
                if (tianFuGridMap.size() > 0) {
                    for (int i = 0; i < roleTianFus.size(); ++i) {
                        RoleTianFu roleTianFu = (RoleTianFu)roleTianFus.get(i);
                        SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)SkillDataPool.tianFuSkillMap.get(Integer.valueOf(roleTianFu.getTianFuSerial()));
                        if (sysTianFuSkill != null) {
                            roleTianFu.setTriggerType(sysTianFuSkill.getOpportunity());
                        }
                        for (Map.Entry<Long, String> entry : tianFuGridMap.entrySet()) {
                            if (((Long)entry.getKey()).equals(roleTianFu.getId())) {
                                roleTianFu.setDayInCeGrid((String)entry.getValue());
                                break;
                            }
                            continue;
                        }
                    }
                }
            }
            DataCache.ROLE_TIAN_FU.put(roleId, roleTianFus);
            return roleTianFus;
        }
        else {
            return roleTianFus;
        }
    }
    
    @Transactional
    public RoleTianFu debrisToTianFu(Role role) {
        byte num = 10;
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null || roleTianFus.size() <= 0) {
            return null;
        }
        Optional<RoleTianFu> optional = roleTianFus.stream().filter(obj -> obj.getTianFuSerial() == 9997).findFirst();
        if (!optional.isPresent()) {
            return null;
        }
        RoleTianFu roleTianFuDebris = (RoleTianFu)optional.get();
        if (roleTianFuDebris.getCount() < num) {
            return null;
        }
        Random random = new Random();
        List<SysTianFuSkill> values = (List)SkillDataPool.tianFuSkillMap.values().stream().filter(obj -> obj.getRace().contains(Byte.valueOf((byte)(int)role.getRace()))).collect(Collectors.toList());
        int index = random.nextInt(values.size());
        SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)values.get(index);
        if (sysTianFuSkill != null) {
            RoleTianFu roleTianFu = new RoleTianFu();
            roleTianFu.setId(Long.valueOf(IdGenerator.getNextId()));
            roleTianFu.setRoleId((long)role.getId());
            roleTianFu.setCategory(sysTianFuSkill.getCategory());
            roleTianFu.setLayer(sysTianFuSkill.getLayer());
            roleTianFu.setIsWear(0);
            roleTianFu.setTianFuSerial(sysTianFuSkill.getCeSerial());
            roleTianFu.setToIdentifyCeSerial(sysTianFuSkill.getToIdentifyCeSerial());
            roleTianFu.setTriggerType(sysTianFuSkill.getOpportunity());
            roleTianFu.setCreateTime(new Date());
            roleTianFu.save();
            (DataCache.ROLE_TIAN_FU.get(role.getId())).add(roleTianFu);
            try {
                RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)4, (byte)1, "合成天策符，编号：" + roleTianFu.getTianFuSerial() + ",等级：" + (int)roleTianFu.getLevel() + ",品阶：" + (int)roleTianFu.getProductOrder(), role.getMarking());
            }
            catch (Exception e) {
                this.logger.error("合成天策符存入mongodb出错！");
            }
            if (roleTianFuDebris.getCount() - num <= 0) {
                roleTianFuDebris.setCount((short)0);
                roleTianFuDebris.setDelete(true);
                (DataCache.ROLE_TIAN_FU.get(role.getId())).remove(roleTianFuDebris);
            }
            else {
                roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() - num));
            }
            roleTianFuDebris.save();
            return roleTianFu;
        }
        else {
            return null;
        }
    }
    
    private boolean revoleToTianFu(RoleTianFu roleTianFu) {
        byte num = 1;
        if (roleTianFu.getProductOrder() == 2) {
            num = 5;
        }
        else if (roleTianFu.getProductOrder() == 3) {
            num = 100;
        }
        Optional<RoleTianFu> optional = (DataCache.ROLE_TIAN_FU.get(Long.valueOf(roleTianFu.getRoleId()))).stream().filter(obj -> obj.getTianFuSerial() == 9997 && obj.getStorePos() == 0).findFirst();
        RoleTianFu roleTianFuDebris;
        if (!optional.isPresent()) {
            roleTianFuDebris = new RoleTianFu();
            roleTianFuDebris.setId(Long.valueOf(IdGenerator.getNextId()));
            roleTianFuDebris.setRoleId(roleTianFu.getRoleId());
            roleTianFuDebris.setCreateTime(new Date());
            roleTianFuDebris.setTianFuSerial(9997);
            roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() + num));
            (DataCache.ROLE_TIAN_FU.get(Long.valueOf(roleTianFu.getRoleId()))).add(roleTianFuDebris);
        }
        else {
            roleTianFuDebris = (RoleTianFu)optional.get();
            if (roleTianFuDebris.getCount() + num > 9999) {
                return false;
            }
            roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() + num));
        }
        roleTianFuDebris.save();
        (DataCache.ROLE_TIAN_FU.get(Long.valueOf(roleTianFu.getRoleId()))).remove(roleTianFu);
        roleTianFu.setDelete(true);
        roleTianFu.save();
        return true;
    }
    
    @Transactional
    public boolean identifyTianFu(Role role, RoleTianFu roleTianFu) {
        RoleService roleService = SpringUtils.getRoleService();
        if (!roleService.checkCurrency(role, Const.RESTYPE_COIN, 50000L)) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx("师贡数量不足"));
            return false;
        }
        roleService.deductCurrency(role, Const.RESTYPE_COIN, 50000L, "鉴定天策符");
        SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)SkillDataPool.tianFuSkillMap.get(Integer.valueOf(roleTianFu.getTianFuSerial()));
        if (sysTianFuSkill == null) {
            return false;
        }
        List<Integer> pointList = new ArrayList<>();
        for (short point : sysTianFuSkill.getPointWeight()) {
            pointList.add(Integer.valueOf((int)point));
        }
        int level = LotteryUtils.lotteryIntEasy(pointList, (int)sysTianFuSkill.getBaoDiWeight(), true) + 1;
        level = Math.min(10, level);
        roleTianFu.setLevel((byte)level);
        List<Byte> rarityJudges = new ArrayList<>();
        for (int i = 0; i < sysTianFuSkill.getRarityJudge().size(); ++i) {
            if (i != 0) {
                rarityJudges.add(sysTianFuSkill.getRarityJudge().get(i));
            }
        }
        int i = rarityJudges.size() - 1;
        while (i >= 0) {
            if ((byte)rarityJudges.get(i) <= level) {
                roleTianFu.setProductOrder((byte)(i + 1));
                break;
            }
            --i;
        }
        roleTianFu.setToIdentifyCeSerial(1);
        try {
            roleTianFu.save();
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    public void updateRole(RoleTianFu roleTianFu, Long targetRoleId) {
        long oldRoleId = roleTianFu.getRoleId();
        roleTianFu.setRoleId((long)targetRoleId);
        roleTianFu.setIsWear(0);
        roleTianFu.save();
        (DataCache.ROLE_TIAN_FU.get(Long.valueOf(oldRoleId))).remove(roleTianFu);
        (DataCache.ROLE_TIAN_FU.get(targetRoleId)).add(roleTianFu);
    }
    
    public boolean delRune(Role role, long id) {
        List<RoleTianFu> list = DataCache.ROLE_TIAN_FU.get(role.getId());
        RoleTianFu rune = (RoleTianFu)list.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (rune == null) {
            return false;
        }
        (DataCache.ROLE_TIAN_FU.get(role.getId())).remove(rune);
        rune.setDelete(true);
        rune.save();
        return true;
    }
    
    public boolean putTianFu(Role role, int serial, int num) {
        short numShort = (short)num;
        if (9997 == serial) {
            Optional<RoleTianFu> optional = (DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst();
            RoleTianFu roleTianFuDebris;
            if (optional.isPresent()) {
                roleTianFuDebris = (RoleTianFu)optional.get();
                if (roleTianFuDebris.getCount() + numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
                roleTianFuDebris.setCount((short)(roleTianFuDebris.getCount() + numShort));
            }
            else {
                List<RoleTianFu> tianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (tianFus.size() >= 75) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符数量超出上限"));
                    return false;
                }
                if (numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
                roleTianFuDebris = new RoleTianFu();
                roleTianFuDebris.setId(Long.valueOf(IdGenerator.getNextId()));
                roleTianFuDebris.setRoleId((long)role.getId());
                roleTianFuDebris.setTianFuSerial(9997);
                roleTianFuDebris.setCount(numShort);
                roleTianFuDebris.setCreateTime(new Date());
                (DataCache.ROLE_TIAN_FU.get(role.getId())).add(roleTianFuDebris);
            }
            roleTianFuDebris.save();
            this.refreshTianFuDebris(role);
        }
        else {
            int i = 0;
            while (i < num) {
                Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
                SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)tianFuSkillMap.get(Integer.valueOf(serial));
                if (sysTianFuSkill == null) {
                    return false;
                }
                List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (roleTianFus.size() >= 75) {
                    return false;
                }
                RoleTianFu roleTianFu = new RoleTianFu();
                roleTianFu.setId(Long.valueOf(IdGenerator.getNextId()));
                roleTianFu.setRoleId((long)role.getId());
                roleTianFu.setCategory(sysTianFuSkill.getCategory());
                roleTianFu.setLayer(sysTianFuSkill.getLayer());
                roleTianFu.setTianFuSerial(serial);
                roleTianFu.setToIdentifyCeSerial(sysTianFuSkill.getToIdentifyCeSerial());
                roleTianFu.setCreateTime(new Date());
                roleTianFu.setTriggerType(sysTianFuSkill.getOpportunity());
                roleTianFu.save();
                (DataCache.ROLE_TIAN_FU.get(role.getId())).add(roleTianFu);
                this.roleTianFuList(role);
                ++i;
            }
        }
        return true;
    }
    
    public boolean checkTianFu(Role role, int serial, int num) {
        short numShort = (short)num;
        if (9997 == serial) {
            Optional<RoleTianFu> optional = (DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getTianFuSerial() == serial && obj.getStorePos() == 0).findFirst();
            if (optional.isPresent()) {
                RoleTianFu roleTianFuDebris = (RoleTianFu)optional.get();
                if (roleTianFuDebris.getCount() + numShort > 9999) {
                    MessagePusher.pushMessage(role, new RespNotifyMiscEx("天策符碎片超出上限"));
                    return false;
                }
            }
            else {
                List<RoleTianFu> tianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
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
            int i = 0;
            while (i < num) {
                Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
                SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)tianFuSkillMap.get(Integer.valueOf(serial));
                if (sysTianFuSkill == null) {
                    return false;
                }
                ++i;
            }
            List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            if (roleTianFus.size() + num > 75) {
                return false;
            }
        }
        return true;
    }
}
