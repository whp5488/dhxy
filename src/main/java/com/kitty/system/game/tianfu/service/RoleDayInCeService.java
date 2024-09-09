package com.kitty.system.game.tianfu.service;

import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.enums.war.wardef.DAY_IN_CE;
import com.kitty.common.thread.IdGenerator;
import org.nutz.dao.Cnd;
import java.util.Optional;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import java.util.Arrays;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import java.util.stream.Collectors;
import java.util.Iterator;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.common.message.RespMsg;
import com.kitty.listener.event.ChangeRuneWearEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.common.utils.reflect.ReflectUtils;
import java.util.Map;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.skill.pool.SkillDataPool;
import java.util.List;
import com.kitty.system.game.tianfu.message.RespReturnAchievement;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.tianfu.message.RespDayInCe;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.role.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class RoleDayInCeService
{
    @Autowired
    private Dao dao;
    
    public RoleDayInCeService() {
    }
    
    public void getDayInCeLevel(Role role) {
        RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
        if (roleDayInCe != null) {
            short level = roleDayInCe.getLevel();
            RespDayInCe respDayInCe = new RespDayInCe(level);
            MessagePusher.pushMessage(role, respDayInCe);
        }
    }
    
    public void getAchievement(Role role) {
        Long achievementScore = role.getExtendBox().getAchievementScore();
        FiedValue fiedValue = new FiedValue(String.valueOf(Const.RES_TYPE_ACHIEVEMENT), (byte)4, String.valueOf(achievementScore));
        List<FiedValue> list = new ArrayList<>();
        list.add(fiedValue);
        MessagePusher.pushMessage(role, new RespReturnAchievement(list));
    }
    
    public void addDayInCeLevel(Role role, Integer addLevel) {
        RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
        Map<Short, SysDayInCePractice> map = SkillDataPool.dayInCePracticeMap;
        SysDayInCePractice sysDayInCePractice = (SysDayInCePractice)map.get(Short.valueOf((short)(roleDayInCe.getLevel() + (int)addLevel)));
        if (sysDayInCePractice == null) {
            return;
        }
        int need = (int)role.getRelive() * 1000;
        if (need < sysDayInCePractice.getNeed()) {
            int relive = sysDayInCePractice.getNeed() / 1000;
            String msg = "你的#<R>角色转生#不足";
            if (relive > 0) {
                msg = msg + "#<R>" + relive + "转#";
            }
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        else {
            int needAchievementScore = 0;
            int needCoin = 0;
            for (int i = roleDayInCe.getLevel() + 1; i <= roleDayInCe.getLevel() + (int)addLevel; ++i) {
                sysDayInCePractice = (SysDayInCePractice)map.get(Short.valueOf((short)i));
                if (sysDayInCePractice != null) {
                    needAchievementScore += sysDayInCePractice.getAchievementScore();
                    needCoin += sysDayInCePractice.getCoin();
                }
            }
            RoleService roleService = SpringUtils.getRoleService();
            if (!roleService.checkCurrency(role, Const.RESTYPE_COIN, (long)needCoin)) {
                return;
            }
            if (!roleService.checkCurrency(role, Const.RES_TYPE_ACHIEVEMENT, (long)needAchievementScore)) {
                return;
            }
            roleService.deductCurrency(role, Const.RESTYPE_COIN, (long)needCoin, "天策符扣贡币");
            roleService.deductCurrency(role, Const.RES_TYPE_ACHIEVEMENT, (long)needAchievementScore, "成就积分");
            roleDayInCe.setLevel((short)(roleDayInCe.getLevel() + (int)addLevel));
            for (Map.Entry<Byte, String> entry : TianFuUtils.client2serverGridMap.entrySet()) {
                if ((byte)entry.getKey() <= roleDayInCe.getLevel()) {
                    String value = (String)entry.getValue();
                    if ((long)ReflectUtils.invokeGetter(roleDayInCe, value) == 0L) {
                        ReflectUtils.invokeSetter(roleDayInCe, value, Integer.valueOf(1));
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
            roleDayInCe.save();
            this.getAchievement(role);
            this.getDayInCeLevel(role);
            EventDispatcher.getInstance().fireEvent(new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
            return;
        }
    }
    
    public void tianFuWear(Role role, byte type, long tianFuId, int grid) {
        if (type == 1) {
            Optional<RoleTianFu> optional = (DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> (long)obj.getId() == tianFuId).findFirst();
            if (!optional.isPresent()) {
                return;
            }
            RoleTianFu roleTianFu = (RoleTianFu)optional.get();
            RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
            String dayInCeGrid = (String)TianFuUtils.client2serverGridMap.get(Byte.valueOf((byte)grid));
            if (dayInCeGrid == null) {
                return;
            }
            byte[] categoryLayer = this.getCategoryLayer(dayInCeGrid);
            byte categoryType = categoryLayer[0];
            byte layer = categoryLayer[1];
            if (roleTianFu.getCategory() != categoryType) {
                MessagePusher.pushMessage(role, new RespMsg("类型不匹配"));
                return;
            }
            if (grid > 3 && layer != -1 && layer != roleTianFu.getLayer()) {
                MessagePusher.pushMessage(role, new RespMsg("此天策符不属于此层级"));
                return;
            }
            long gridValue = (long)ReflectUtils.invokeGetter(roleDayInCe, dayInCeGrid);
            if (gridValue == 0L) {
                return;
            }
            List<RoleTianFuClient> list = new ArrayList<>();
            for (RoleTianFu rtf : DataCache.ROLE_TIAN_FU.get(role.getId())) {
                if (rtf.getId().equals(roleTianFu.getId())) {
                    continue;
                }
                if (rtf.getIsWear() == 1 && (rtf.getTianFuSerial() == roleTianFu.getTianFuSerial() || (long)rtf.getId() == gridValue)) {
                    rtf.setIsWear(0);
                    rtf.save();
                    ReflectUtils.invokeSetter(roleDayInCe, rtf.getDayInCeGrid(), Integer.valueOf(1));
                    rtf.setDayInCeGrid(null);
                    RoleTianFuClient roleTianFuClient2 = new RoleTianFuClient(rtf);
                    list.add(roleTianFuClient2);
                    TianFuUtils.excuteWearInstantlyTakeEffect(role, rtf);
                }
                else {
                    continue;
                }
            }
            roleTianFu.setDayInCeGrid(dayInCeGrid);
            roleTianFu.setIsWear(1);
            ReflectUtils.invokeSetter(roleDayInCe, dayInCeGrid, roleTianFu.getId());
            roleTianFu.save();
            roleDayInCe.save();
            RoleTianFuClient roleTianFuClient2 = new RoleTianFuClient(roleTianFu);
            list.add(roleTianFuClient2);
            TianFuUtils.excuteWearInstantlyTakeEffect(role, roleTianFu);
            MessagePusher.pushMessage(role, new RespTianFuList((byte)3, list));
            if (TianFuUtils.countFuPlayerBookGain(roleTianFu, true)) {
                EventDispatcher.getInstance().fireEvent(new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
            }
        }
        else if (type == 0) {
            List<RoleTianFu> noWears = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            if (noWears.size() >= 75) {
                MessagePusher.pushMessage(role, new RespMsg("天策符背包已无空闲格子"));
                return;
            }
            RoleTianFu oldRoleTianFu = (RoleTianFu)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> (long)obj.getId() == tianFuId).findFirst().orElse(null);
            if (oldRoleTianFu == null) {
                return;
            }
            RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(role.getId());
            String dayInCeGrid = (String)TianFuUtils.client2serverGridMap.get(Byte.valueOf((byte)grid));
            if (dayInCeGrid == null) {
                return;
            }
            oldRoleTianFu.setDayInCeGrid(null);
            oldRoleTianFu.setIsWear(0);
            ReflectUtils.invokeSetter(roleDayInCe, dayInCeGrid, Integer.valueOf(1));
            oldRoleTianFu.save();
            roleDayInCe.save();
            TianFuUtils.excuteWearInstantlyTakeEffect(role, oldRoleTianFu);
            RoleTianFuClient roleTianFuClient = new RoleTianFuClient(oldRoleTianFu);
            MessagePusher.pushMessage(role, new RespTianFuList((byte)3, Arrays.asList(roleTianFuClient)));
            if (TianFuUtils.countFuPlayerBookGain(oldRoleTianFu, false)) {
                EventDispatcher.getInstance().fireEvent(new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
            }
        }
    }
    
    public void chageRace(Role role) {
        List<RoleTianFu> inappropriate = this.getInappropriate((long)role.getId(), (int)role.getRace());
        if (inappropriate != null) {
            for (RoleTianFu roleTianFu : inappropriate) {
                this.tianFuWear(role, (byte)0, (long)roleTianFu.getId(), (int)(byte)TianFuUtils.server2clientGridMap.get(roleTianFu.getDayInCeGrid()));
            }
        }
    }
    
    public RoleDayInCe getDataForRoleId(Long roleId) {
        RoleDayInCe roleDayInCe = (RoleDayInCe)DataCache.ROLE_DAY_IN_CE.get(roleId);
        if (roleDayInCe == null) {
            List<RoleDayInCe> roleDayInCes = this.dao.query(RoleDayInCe.class, Cnd.where("role_id", "=", roleId));
            if (roleDayInCes.size() <= 0) {
                roleDayInCe = new RoleDayInCe();
                roleDayInCe.setId(Long.valueOf(IdGenerator.getNextId()));
                roleDayInCe.setRoleId((long)roleId);
                roleDayInCe.setLevel((short)0);
                roleDayInCe.save();
            }
            else {
                roleDayInCe = (RoleDayInCe)roleDayInCes.get(0);
            }
            DataCache.ROLE_DAY_IN_CE.put(roleId, roleDayInCe);
            return roleDayInCe;
        }
        else {
            return roleDayInCe;
        }
    }
    
    private byte[] getCategoryLayer(String dayInCeGrid) {
        String last = dayInCeGrid.substring(dayInCeGrid.length() - 2, dayInCeGrid.length() - 1);
        byte categoryType = -1;
        byte layer = -1;
        if (!last.matches("^[0-9]+$")) {
            int n2 = -1;
            switch (dayInCeGrid.hashCode()) {
                case -188693147: {
                    if (dayInCeGrid.equals("gattaca")) {
                        n2 = 0;
                        break;
                    }
                    break;
                }
                case 336625201: {
                    if (dayInCeGrid.equals("loadObj")) {
                        n2 = 1;
                        break;
                    }
                    break;
                }
                case -115818722: {
                    if (dayInCeGrid.equals("driveBeasts")) {
                        n2 = 2;
                        break;
                    }
                    break;
                }
            }
            switch (n2) {
                case 0: {
                    categoryType = DAY_IN_CE.GATTACA.getValue();
                    break;
                }
                case 1: {
                    categoryType = DAY_IN_CE.LOAD_OBJ.getValue();
                    break;
                }
                case 2: {
                    categoryType = DAY_IN_CE.DRIVER_BEASTS.getValue();
                    break;
                }
            }
        }
        else {
            String category = dayInCeGrid.substring(0, dayInCeGrid.length() - 2);
            int n = -1;
            switch (category.hashCode()) {
                case -188693147: {
                    if (category.equals("gattaca")) {
                        n = 0;
                        break;
                    }
                    break;
                }
                case 336625201: {
                    if (category.equals("loadObj")) {
                        n = 1;
                        break;
                    }
                    break;
                }
                case -115818722: {
                    if (category.equals("driveBeasts")) {
                        n = 2;
                        break;
                    }
                    break;
                }
            }
            switch (n) {
                case 0: {
                    categoryType = DAY_IN_CE.GATTACA.getValue();
                    break;
                }
                case 1: {
                    categoryType = DAY_IN_CE.LOAD_OBJ.getValue();
                    break;
                }
                case 2: {
                    categoryType = DAY_IN_CE.DRIVER_BEASTS.getValue();
                    break;
                }
            }
            layer = Byte.parseByte(last);
        }
        return new byte[] { categoryType, layer };
    }
    
    public int checkDemount(Role role, int race) {
        Integer raceOld = role.getRace();
        if (race == (int)raceOld) {
            return 0;
        }
        List<RoleTianFu> inappropriate = this.getInappropriate((long)role.getId(), race);
        if (inappropriate != null) {
            int inappropriateCount = inappropriate.size();
            List<RoleTianFu> noWears = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            int num = 75 - noWears.size() - inappropriateCount;
            if (num < 0) {
                return Math.abs(num);
            }
        }
        return 0;
    }
    
    public List<RoleTianFu> getInappropriate(long roleId, int race) {
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(Long.valueOf(roleId));
        if (roleTianFus == null) {
            return null;
        }
        Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
        List<RoleTianFu> returnList = new ArrayList<>();
        for (RoleTianFu roleTianFu : roleTianFus) {
            if (roleTianFu.getIsWear() != 1) {
                continue;
            }
            SysTianFuSkill sysTianFuSkill = (SysTianFuSkill)tianFuSkillMap.get(Integer.valueOf(roleTianFu.getTianFuSerial()));
            if (sysTianFuSkill != null && !sysTianFuSkill.getRace().contains(Byte.valueOf((byte)race))) {
                returnList.add(roleTianFu);
            }
            else {
                continue;
            }
        }
        if (returnList.size() > 0) {
            return returnList;
        }
        return null;
    }
}
