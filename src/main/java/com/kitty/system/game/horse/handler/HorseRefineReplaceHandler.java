
package com.kitty.system.game.horse.handler;

import java.util.IdentityHashMap;
import java.util.Iterator;
import com.alibaba.fastjson.JSON;
import com.kitty.record.RecordThread;
import com.kitty.system.game.pet.domain.Pet;
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
    @Override
    public String acceptFeed(final Horse horse, final Role role, final short type, final byte operation, final Object... args) {
        if (operation > 3) {
            return null;
        }
        final Integer index = Integer.valueOf(String.valueOf(args[0]));
        final List<HorseBase> list = horse.getDarkBaseMap().get(operation);
        final HorseBase base = list.get(index);
        if (base != null) {
            horse.setGrowUp(base.getGrow_up());
            horse.setMoveSp(base.getMove_sp());
            final ArrayList<Long> objects = new ArrayList<Long>();
            for (final Integer key : horse.getSkill().keySet()) {
                final Long anInt = horse.getSkill().get(key);
                objects.add(anInt);
            }
            final IdentityHashMap<Integer, Long> skill = base.getSkill();
            int i = 0;
            for (final Integer key2 : skill.keySet()) {
                skill.put(key2, objects.get(i));
                if (++i > 2) {
                    break;
                }
            }
            horse.setSkill(skill);
        }
        horse.getDarkBaseMap().put(operation, new ArrayList<HorseBase>());
        horse.setScore(this.horseService.score(horse));
        horse.save();
        HorseSkillUtils.studyFinish(role, horse.getId(), null);
        this.horseService.refresh(role, horse);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)4, String.format("替换坐骑:%d，技能：%s", horse.getId(), JSON.toJSONString((Object)horse.getSkill(), true)), role.getMarking());
        return null;
    }
}
