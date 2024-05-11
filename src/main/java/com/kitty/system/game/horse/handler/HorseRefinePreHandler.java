
package com.kitty.system.game.horse.handler;

import java.util.Iterator;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.horse.message.RespPreUpgradeHorse;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.horse.message.vo.MountBase;
import java.util.ArrayList;
import com.kitty.system.game.horse.domain.vo.HorseBase;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.horse.domain.Horse;
import org.springframework.stereotype.Component;

@Component("HorseRefinePre")
public class HorseRefinePreHandler extends FeedHorseHandler
{
    @Override
    public String acceptFeed(final Horse horse, final Role role, final short type, final byte operation, final Object... args) {
        if (operation > 3) {
            return null;
        }
        final List<HorseBase> bases = horse.getDarkBaseMap().get(operation);
        final List<MountBase> list = new ArrayList<MountBase>();
        if (bases != null) {
            for (final HorseBase basis : bases) {
                final List<FiedValue> objects = new ArrayList<FiedValue>();
                for (final Integer key : basis.getSkill().keySet()) {
                    objects.add(new FiedValue(String.valueOf(key), (byte)4, String.valueOf(basis.getSkill().get(key))));
                }
                list.add(new MountBase(basis.getGrow_up(), basis.getMove_sp(), basis.getScore(), objects));
            }
        }
        final RespPreUpgradeHorse upgradeHorse = new RespPreUpgradeHorse();
        upgradeHorse.setId(String.valueOf(horse.getId()));
        upgradeHorse.setAsk(operation);
        upgradeHorse.setList(list);
        MessagePusher.pushMessage(role, upgradeHorse);
        return null;
    }
}
