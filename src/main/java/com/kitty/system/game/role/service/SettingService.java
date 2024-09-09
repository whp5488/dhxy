package com.kitty.system.game.role.service;

import java.util.List;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.message.RespInstruct;
import java.util.Arrays;
import com.kitty.system.game.role.message.vo.Instruct;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespHint;
import java.util.HashMap;
import java.util.Map;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.dirtywords.WordDisposePool;
import com.kitty.system.enums.war.wardef.WarInstruct;
import com.kitty.system.game.role.message.ReqUpdateInstruct;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class SettingService
{
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    
    public SettingService() {
    }
    
    public Setting getSetting(Role role) {
        Setting setting = (Setting)DataCache.SETTING_MAP.get(role.getId());
        if (setting == null) {
            setting = (Setting)this.dao.fetch(Setting.class, Cnd.where("r_id", "=", role.getId()));
            if (setting == null) {
                setting = new Setting(this.worker.nextId(), (long)role.getId());
                setting.save();
            }
            DataCache.SETTING_MAP.put(role.getId(), setting);
            return setting;
        }
        else {
            return setting;
        }
    }
    
    public void updateInstruct(Role role, ReqUpdateInstruct updateInstruct) {
        byte length = WarInstruct.getLength(updateInstruct.getSerial(), updateInstruct.getIndex());
        if (length <= 0) {
            return;
        }
        if (updateInstruct.getType() >= 1) {
            Setting setting2 = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
            Map<Short, Map<Byte, String>> map2 = setting2.getSpecial();
            Map<Byte, String> byteStringMap2 = (Map)map2.get(Short.valueOf(updateInstruct.getSerial()));
            if (byteStringMap2 != null) {
                byteStringMap2.remove(Byte.valueOf(updateInstruct.getIndex()));
                setting2.save();
                MessagePusher.pushMessage(role, new RespHint(28, (short)1));
                return;
            }
            MessagePusher.pushMessage(role, new RespHint(28, (short)0));
        }
        else {
            String instruct = updateInstruct.getString().trim();
            if (instruct.length() > length) {
                MessagePusher.pushMessage(role, new RespMsg(String.format("该自定义指令最多编辑%d个字。#<E:17>#", Byte.valueOf(length))));
                MessagePusher.pushMessage(role, new RespHint(28, (short)0));
                return;
            }
            if (instruct.length() <= 0) {
                MessagePusher.pushMessage(role, new RespMsg("请输入自定义指令"));
                MessagePusher.pushMessage(role, new RespHint(28, (short)0));
                return;
            }
            List<String> set = WordDisposePool.checkDirtyWords(instruct);
            if (set.size() >= 1) {
                MessagePusher.pushMessage(role, new RespMsg("自定义指令存在非法字符。"));
                MessagePusher.pushMessage(role, new RespHint(28, (short)0));
                return;
            }
            Setting setting = ((SettingService)SpringUtils.getBean(SettingService.class)).getSetting(role);
            Map<Short, Map<Byte, String>> map = setting.getSpecial();
            Map<Byte, String> byteStringMap = (Map)map.get(Short.valueOf(updateInstruct.getSerial()));
            if (byteStringMap == null) {
                byteStringMap = new HashMap();
                map.put(Short.valueOf(updateInstruct.getSerial()), byteStringMap);
            }
            byteStringMap.put(Byte.valueOf(updateInstruct.getIndex()), instruct);
            setting.save();
            MessagePusher.pushMessage(role, new RespHint(28, (short)1));
            MessagePusher.pushMessage(role, new RespInstruct(Arrays.asList(new Instruct(updateInstruct.getSerial(), updateInstruct.getIndex(), instruct))));
        }
        return;
    }
}
