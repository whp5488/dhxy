
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
    public void serialize(final Horse obj) {
        final String dark_base = JSON.toJSONString((Object)obj.getDarkBaseMap(), true);
        obj.setDarkBase(dark_base);
        final String skill = JSON.toJSONString((Object)obj.getSkill(), true);
        obj.setSkillJson(skill);
    }
    
    @Override
    public void deserialize(final Horse obj) {
        final String darkBase = obj.getDarkBase();
        final String skillJson = obj.getSkillJson();
        final Map<Byte, List<HorseBase>> map_base = (Map<Byte, List<HorseBase>>)JSON.parseObject(darkBase, (TypeReference)new TypeReference<HashMap<Byte, List<HorseBase>>>() {}, new Feature[0]);
        obj.setDarkBaseMap(map_base);
        final IdentityHashMap<Integer, Long> skill = (IdentityHashMap<Integer, Long>)JSON.parseObject(skillJson, (TypeReference)new TypeReference<IdentityHashMap<Integer, Long>>() {}, new Feature[0]);
        obj.setSkill(skill);
    }
}
