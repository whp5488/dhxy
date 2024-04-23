package com.kitty.system.game.player.serializer.ipml;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import com.kitty.system.game.player.serializer.IHorsePropSerializer;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HorseSerializer implements IHorsePropSerializer {
  Logger logger = LoggerFactory.getLogger(getClass());
  
  public void serialize(Horse obj) {
    String dark_base = JSON.toJSONString(obj.getDarkBaseMap(), true);
    obj.setDarkBase(dark_base);
    String skill = JSON.toJSONString(obj.getSkill(), true);
    obj.setSkillJson(skill);
  }
  
  public void deserialize(Horse obj) {
    String darkBase = obj.getDarkBase();
    String skillJson = obj.getSkillJson();
    Map<Byte, List<HorseBase>> map_base = (Map<Byte, List<HorseBase>>)JSON.parseObject(darkBase, new TypeReference<HashMap<Byte, List<HorseBase>>>() {
        
        },  new com.alibaba.fastjson.parser.Feature[0]);
    obj.setDarkBaseMap(map_base);
    IdentityHashMap<Integer, Long> skill = (IdentityHashMap<Integer, Long>)JSON.parseObject(skillJson, new TypeReference<IdentityHashMap<Integer, Long>>() {
        
        },  new com.alibaba.fastjson.parser.Feature[0]);
    obj.setSkill(skill);
  }
}
