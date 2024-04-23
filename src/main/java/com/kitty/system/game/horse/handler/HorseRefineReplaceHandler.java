package com.kitty.system.game.horse.handler;

import com.alibaba.fastjson.JSON;
import com.kitty.record.RecordThread;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import com.kitty.system.game.role.domain.Role;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import org.springframework.stereotype.Component;

@Component("HorseRefineReplace")
public class HorseRefineReplaceHandler extends FeedHorseHandler {
  public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
    if (operation > 3)
      return null; 
    Integer index = Integer.valueOf(String.valueOf(args[0]));
    List<HorseBase> list = (List<HorseBase>)horse.getDarkBaseMap().get(Byte.valueOf(operation));
    HorseBase base = list.get(index.intValue());
    if (base != null) {
      horse.setGrowUp(base.getGrow_up());
      horse.setMoveSp(base.getMove_sp());
      ArrayList<Long> objects = new ArrayList<>();
      for (Integer key : horse.getSkill().keySet()) {
        Long anInt = (Long)horse.getSkill().get(key);
        objects.add(anInt);
      } 
      IdentityHashMap<Integer, Long> skill = base.getSkill();
      int i = 0;
      for (Integer key : skill.keySet()) {
        skill.put(key, objects.get(i));
        i++;
        if (i > 2)
          break; 
      } 
      horse.setSkill(skill);
    } 
    horse.getDarkBaseMap().put(Byte.valueOf(operation), new ArrayList());
    horse.setScore(this.horseService.score(horse));
    horse.save();
    HorseSkillUtils.studyFinish(role, horse.getId().longValue(), null);
    this.horseService.refresh(role, horse);
    RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)4, String.format("替换坐骑:%d，技能：%s", new Object[] { horse.getId(), JSON.toJSONString(horse.getSkill(), true) }), role.getMarking());
    return null;
  }
}
