
package com.kitty.system.game.tianfu.service;

import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.enums.war.wardef.DAY_IN_CE;
import com.kitty.common.thread.IdGenerator;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import java.util.Optional;
import java.util.Arrays;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.game.tianfu.message.RespTianFuList;
import com.kitty.system.game.tianfu.pojo.RoleTianFuClient;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import java.util.Iterator;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ChangeRuneWearEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.common.utils.reflect.ReflectUtils;
import java.util.Map;
import com.kitty.system.game.fightnew.book.tianfu.utils.TianFuUtils;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.tianfu.domain.SysDayInCePractice;
import com.kitty.system.game.skill.pool.SkillDataPool;
import java.util.List;
import com.kitty.system.game.tianfu.message.RespReturnAchievement;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.message.Message;
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
    
    public void getDayInCeLevel(final Role role) {
        final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
        if (roleDayInCe != null) {
            final short level = roleDayInCe.getLevel();
            final RespDayInCe respDayInCe = new RespDayInCe(level);
            MessagePusher.pushMessage(role, respDayInCe);
        }
    }
    
    public void getAchievement(final Role role) {
        final Long achievementScore = role.getExtendBox().getAchievementScore();
        final FiedValue fiedValue = new FiedValue(String.valueOf(Const.RES_TYPE_ACHIEVEMENT), (byte)4, String.valueOf(achievementScore));
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(fiedValue);
        MessagePusher.pushMessage(role, new RespReturnAchievement(list));
    }
    
    public void addDayInCeLevel(final Role role, final Integer addLevel) {
        final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
        final Map<Short, SysDayInCePractice> map = SkillDataPool.dayInCePracticeMap;
        SysDayInCePractice sysDayInCePractice = map.get((short)(roleDayInCe.getLevel() + addLevel));
        if (sysDayInCePractice == null) {
            return;
        }
        final int need = role.getRelive() * 1000;
        if (need < sysDayInCePractice.getNeed()) {
            final int relive = sysDayInCePractice.getNeed() / 1000;
            String msg = "你的#<R>角色转生#不足";
            if (relive > 0) {
                msg = msg + "#<R>" + relive + "转#";
            }
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        int needAchievementScore = 0;
        int needCoin = 0;
        for (int i = roleDayInCe.getLevel() + 1; i <= roleDayInCe.getLevel() + addLevel; ++i) {
            sysDayInCePractice = map.get((short)i);
            if (sysDayInCePractice != null) {
                needAchievementScore += sysDayInCePractice.getAchievementScore();
                needCoin += sysDayInCePractice.getCoin();
            }
        }
        final RoleService roleService = SpringUtils.getRoleService();
        if (!roleService.checkCurrency(role, Const.RESTYPE_COIN, needCoin)) {
            return;
        }
        if (!roleService.checkCurrency(role, Const.RES_TYPE_ACHIEVEMENT, needAchievementScore)) {
            return;
        }
        roleService.deductCurrency(role, Const.RESTYPE_COIN, needCoin, "天策符扣贡币");
        roleService.deductCurrency(role, Const.RES_TYPE_ACHIEVEMENT, needAchievementScore, "成就积分");
        roleDayInCe.setLevel((short)(roleDayInCe.getLevel() + addLevel));
        for (final Map.Entry<Byte, String> entry : TianFuUtils.client2serverGridMap.entrySet()) {
            if (entry.getKey() <= roleDayInCe.getLevel()) {
                final String value = entry.getValue();
                if ((Long)ReflectUtils.invokeGetter(roleDayInCe, value) != 0L) {
                    continue;
                }
                ReflectUtils.invokeSetter(roleDayInCe, value, 1);
            }
        }
        roleDayInCe.save();
        this.getAchievement(role);
        this.getDayInCeLevel(role);
        EventDispatcher.getInstance().fireEvent(new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
    }
    
    public void tianFuWear(final Role role, final byte type, final long tianFuId, final int grid) {
        if (type == 1) {
            final Optional<RoleTianFu> optional = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getId() == tianFuId).findFirst();
            if (!optional.isPresent()) {
                return;
            }
            final RoleTianFu roleTianFu = optional.get();
            final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
            final String dayInCeGrid = TianFuUtils.client2serverGridMap.get((byte)grid);
            if (dayInCeGrid == null) {
                return;
            }
            final byte[] categoryLayer = this.getCategoryLayer(dayInCeGrid);
            final byte categoryType = categoryLayer[0];
            final byte layer = categoryLayer[1];
            if (roleTianFu.getCategory() != categoryType) {
                MessagePusher.pushMessage(role, new RespMsg("类型不匹配"));
                return;
            }
            if (grid > 3 && layer != -1 && layer != roleTianFu.getLayer()) {
                MessagePusher.pushMessage(role, new RespMsg("此天策符不属于此层级"));
                return;
            }
            final long gridValue = ReflectUtils.invokeGetter(roleDayInCe, dayInCeGrid);
            if (gridValue == 0L) {
                return;
            }
            final List<RoleTianFuClient> list = new ArrayList<RoleTianFuClient>();
            for (final RoleTianFu rtf : DataCache.ROLE_TIAN_FU.get(role.getId())) {
                if (rtf.getId().equals(roleTianFu.getId())) {
                    continue;
                }
                if (rtf.getIsWear() != 1 || (rtf.getTianFuSerial() != roleTianFu.getTianFuSerial() && rtf.getId() != gridValue)) {
                    continue;
                }
                rtf.setIsWear(0);
                rtf.save();
                ReflectUtils.invokeSetter(roleDayInCe, rtf.getDayInCeGrid(), 1);
                rtf.setDayInCeGrid(null);
                final RoleTianFuClient roleTianFuClient = new RoleTianFuClient(rtf);
                list.add(roleTianFuClient);
                TianFuUtils.excuteWearInstantlyTakeEffect(role, rtf);
            }
            roleTianFu.setDayInCeGrid(dayInCeGrid);
            roleTianFu.setIsWear(1);
            ReflectUtils.invokeSetter(roleDayInCe, dayInCeGrid, roleTianFu.getId());
            roleTianFu.save();
            roleDayInCe.save();
            final RoleTianFuClient roleTianFuClient = new RoleTianFuClient(roleTianFu);
            list.add(roleTianFuClient);
            TianFuUtils.excuteWearInstantlyTakeEffect(role, roleTianFu);
            MessagePusher.pushMessage(role, new RespTianFuList((byte)3, list));
            if (TianFuUtils.countFuPlayerBookGain(roleTianFu, true)) {
                EventDispatcher.getInstance().fireEvent(new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
            }
        }
        else if (type == 0) {
            final List<RoleTianFu> noWears = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            if (noWears.size() >= 75) {
                MessagePusher.pushMessage(role, new RespMsg("天策符背包已无空闲格子"));
                return;
            }
            final RoleTianFu oldRoleTianFu = DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getId() == tianFuId).findFirst().orElse(null);
            if (oldRoleTianFu == null) {
                return;
            }
            final RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(role.getId());
            final String dayInCeGrid = TianFuUtils.client2serverGridMap.get((byte)grid);
            if (dayInCeGrid == null) {
                return;
            }
            oldRoleTianFu.setDayInCeGrid(null);
            oldRoleTianFu.setIsWear(0);
            ReflectUtils.invokeSetter(roleDayInCe, dayInCeGrid, 1);
            oldRoleTianFu.save();
            roleDayInCe.save();
            TianFuUtils.excuteWearInstantlyTakeEffect(role, oldRoleTianFu);
            final RoleTianFuClient roleTianFuClient2 = new RoleTianFuClient(oldRoleTianFu);
            MessagePusher.pushMessage(role, new RespTianFuList((byte)3, Arrays.asList(roleTianFuClient2)));
            if (TianFuUtils.countFuPlayerBookGain(oldRoleTianFu, false)) {
                EventDispatcher.getInstance().fireEvent(new ChangeRuneWearEvent(EventType.CHANGE_RUNE_WEAR, role));
            }
        }
    }
    
    public void chageRace(final Role role) {
        final List<RoleTianFu> inappropriate = this.getInappropriate(role.getId(), role.getRace());
        if (inappropriate != null) {
            for (final RoleTianFu roleTianFu : inappropriate) {
                this.tianFuWear(role, (byte)0, roleTianFu.getId(), TianFuUtils.server2clientGridMap.get(roleTianFu.getDayInCeGrid()));
            }
        }
    }
    
    public RoleDayInCe getDataForRoleId(final Long roleId) {
        RoleDayInCe roleDayInCe = DataCache.ROLE_DAY_IN_CE.get(roleId);
        if (roleDayInCe == null) {
            final List<RoleDayInCe> roleDayInCes = this.dao.query(RoleDayInCe.class, Cnd.where("role_id", "=", roleId));
            if (roleDayInCes.size() <= 0) {
                roleDayInCe = new RoleDayInCe();
                roleDayInCe.setId(IdGenerator.getNextId());
                roleDayInCe.setRoleId(roleId);
                roleDayInCe.setLevel((short)0);
                roleDayInCe.save();
            }
            else {
                roleDayInCe = roleDayInCes.get(0);
            }
            DataCache.ROLE_DAY_IN_CE.put(roleId, roleDayInCe);
        }
        return roleDayInCe;
    }
    
    private byte[] getCategoryLayer(final String dayInCeGrid) {
        final String last = dayInCeGrid.substring(dayInCeGrid.length() - 2, dayInCeGrid.length() - 1);
        byte categoryType = -1;
        byte layer = -1;
        if (!last.matches("^[0-9]+$")) {
            switch (dayInCeGrid) {
                case "gattaca": {
                    categoryType = DAY_IN_CE.GATTACA.getValue();
                    break;
                }
                case "loadObj": {
                    categoryType = DAY_IN_CE.LOAD_OBJ.getValue();
                    break;
                }
                case "driveBeasts": {
                    categoryType = DAY_IN_CE.DRIVER_BEASTS.getValue();
                    break;
                }
            }
        }
        else {
            final String substring;
            final String category = substring = dayInCeGrid.substring(0, dayInCeGrid.length() - 2);
            switch (substring) {
                case "gattaca": {
                    categoryType = DAY_IN_CE.GATTACA.getValue();
                    break;
                }
                case "loadObj": {
                    categoryType = DAY_IN_CE.LOAD_OBJ.getValue();
                    break;
                }
                case "driveBeasts": {
                    categoryType = DAY_IN_CE.DRIVER_BEASTS.getValue();
                    break;
                }
            }
            layer = Byte.parseByte(last);
        }
        return new byte[] { categoryType, layer };
    }
    
    public int checkDemount(final Role role, final int race) {
        final Integer raceOld = role.getRace();
        if (race == raceOld) {
            return 0;
        }
        final List<RoleTianFu> inappropriate = this.getInappropriate(role.getId(), race);
        if (inappropriate != null) {
            final int inappropriateCount = inappropriate.size();
            final List<RoleTianFu> noWears = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
            final int num = 75 - noWears.size() - inappropriateCount;
            if (num < 0) {
                return Math.abs(num);
            }
        }
        return 0;
    }
    
    public List<RoleTianFu> getInappropriate(final long roleId, final int race) {
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(roleId);
        if (roleTianFus == null) {
            return null;
        }
        final Map<Integer, SysTianFuSkill> tianFuSkillMap = SkillDataPool.tianFuSkillMap;
        final List<RoleTianFu> returnList = new ArrayList<RoleTianFu>();
        for (final RoleTianFu roleTianFu : roleTianFus) {
            if (roleTianFu.getIsWear() != 1) {
                continue;
            }
            final SysTianFuSkill sysTianFuSkill = tianFuSkillMap.get(roleTianFu.getTianFuSerial());
            if (sysTianFuSkill == null || sysTianFuSkill.getRace().contains((byte)race)) {
                continue;
            }
            returnList.add(roleTianFu);
        }
        if (returnList.size() > 0) {
            return returnList;
        }
        return null;
    }
}
