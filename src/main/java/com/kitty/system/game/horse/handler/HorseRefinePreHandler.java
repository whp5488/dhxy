package com.kitty.system.game.horse.handler;

import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.horse.message.RespPreUpgradeHorse;
import com.kitty.system.game.horse.message.vo.MountBase;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import org.springframework.stereotype.Component;

@Component("HorseRefinePre")
public class HorseRefinePreHandler extends FeedHorseHandler
{
    public HorseRefinePreHandler() {
    }
    
    @Override
    public String acceptFeed(Horse horse, Role role, short type, byte operation, Object... args) {
        if (operation > 3) {
            return null;
        }
        List<HorseBase> bases = (List)horse.getDarkBaseMap().get(Byte.valueOf(operation));
        List<MountBase> list = new ArrayList<>();
        if (bases != null) {
            for (HorseBase basis : bases) {
                List<FiedValue> objects = new ArrayList<>();
                for (Integer key : basis.getSkill().keySet()) {
                    objects.add(new FiedValue(String.valueOf(key), (byte)4, String.valueOf(basis.getSkill().get(key))));
                }
                list.add(new MountBase(basis.getGrow_up(), basis.getMove_sp(), basis.getScore(), objects));
            }
        }
        RespPreUpgradeHorse upgradeHorse = new RespPreUpgradeHorse();
        upgradeHorse.setId(String.valueOf(horse.getId()));
        upgradeHorse.setAsk(operation);
        upgradeHorse.setList(list);
        MessagePusher.pushMessage(role, upgradeHorse);
        return null;
    }
}
