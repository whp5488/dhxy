package com.kitty.system.game.horse.handler;

import java.util.IdentityHashMap;
import java.util.Iterator;
import com.alibaba.fastjson.JSON;
import com.kitty.record.RecordThread;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import java.util.ArrayList;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import org.springframework.stereotype.Component;

@Component("HorseRefineReplace")
public class HorseRefineReplaceHandler extends FeedHorseHandler
{
    public HorseRefineReplaceHandler() {
    }
    
    @Override
    public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
        if (operation > 3) {
            return null;
        }
        Integer index = Integer.valueOf(String.valueOf(args[0]));
        List<HorseBase> list = (List)horse.getDarkBaseMap().get(Byte.valueOf(operation));
        HorseBase base = (HorseBase)list.get((int)index);
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
            for (Integer key2 : skill.keySet()) {
                skill.put(key2, objects.get(i));
                if (++i > 2) {
                    break;
                }
                continue;
            }
            horse.setSkill(skill);
        }
        horse.getDarkBaseMap().put(Byte.valueOf(operation), new ArrayList<>());
        horse.setScore(this.horseService.score(horse));
        horse.save();
        HorseSkillUtils.studyFinish(role, (long)horse.getId(), null);
        this.horseService.refresh(role, horse);
        RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)1000, (byte)4, String.format("替换坐骑:%d，技能：%s", horse.getId(), JSON.toJSONString(horse.getSkill(), true)), role.getMarking());
        return null;
    }
}
