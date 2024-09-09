package com.kitty.system.game.player.serializer.ipml;

import java.util.IdentityHashMap;
import com.alibaba.fastjson.parser.Feature;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import java.util.List;
import java.util.HashMap;
import com.alibaba.fastjson.TypeReference;
import java.util.Map;
import com.alibaba.fastjson.JSON;
import com.kitty.system.game.horse.domain.Horse;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import com.kitty.system.game.player.serializer.IHorsePropSerializer;

public class HorseSerializer implements IHorsePropSerializer
{
    Logger logger;
    
    public HorseSerializer() {
        this.logger = LoggerFactory.getLogger(this.getClass());
    }
    
    @Override
    public void serialize(Horse obj) {
        String dark_base = JSON.toJSONString(obj.getDarkBaseMap(), true);
        obj.setDarkBase(dark_base);
        String skill = JSON.toJSONString(obj.getSkill(), true);
        obj.setSkillJson(skill);
    }
    
    @Override
    public void deserialize(Horse obj) {
        String darkBase = obj.getDarkBase();
        String skillJson = obj.getSkillJson();
        Map<Byte, List<HorseBase>> map_base = (Map)JSON.parseObject(darkBase, new TypeReference<HashMap<Byte, List<HorseBase>>>() {}, new Feature[0]);
        obj.setDarkBaseMap(map_base);
        IdentityHashMap<Integer, Long> skill = (IdentityHashMap)JSON.parseObject(skillJson, new TypeReference<IdentityHashMap<Integer, Long>>() {}, new Feature[0]);
        obj.setSkill(skill);
    }
}
