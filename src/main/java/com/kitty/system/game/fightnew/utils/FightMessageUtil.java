
package com.kitty.system.game.fightnew.utils;

import java.util.Collections;
import java.util.Iterator;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.fightnew.FightObject;
import com.kitty.mina.message.Message;
import java.util.List;
import com.kitty.system.game.fightnew.bean.Fight;

public class FightMessageUtil
{
    public static void sendFightPackage(final Fight fight, final List<Message> messages) {
        for (final FightObject fightObject : fight.getListA()) {
            if (fightObject.isPlayer()) {
                for (final Message message : messages) {
                    MessagePusher.pushMessage(fightObject.getRole(), message);
                }
            }
        }
        if (fight.isPkFight()) {
            for (final FightObject fightObject : fight.getListB()) {
                if (fightObject.isPlayer()) {
                    for (final Message message : messages) {
                        MessagePusher.pushMessage(fightObject.getRole(), message);
                    }
                }
            }
        }
    }
    
    public static void sendFightPackage(final Fight fight, final Message message) {
        if (fight == null || message == null) {
            return;
        }
        sendFightPackage(fight, Collections.singletonList(message));
    }
    
    public static void sendFightPackage(final long roleId, final Message message) {
        MessagePusher.pushMessage(roleId, message);
    }
    
    public static void sendFightPackage(final Fight fight, final Message message, final boolean isA, final boolean isFightSpetator) {
        if (fight == null || message == null) {
            return;
        }
        sendFightPackage(fight, Collections.singletonList(message), isA, isFightSpetator);
    }
    
    public static void sendFightPackage(final Fight fight, final List<Message> messages, final boolean isA, final boolean isFightSpetator) {
        final List<FightObject> targets = isA ? fight.getListA() : fight.getListB();
        for (final Message message : messages) {
            for (final FightObject fightObject : targets) {
                if (fightObject.isPlayer()) {
                    MessagePusher.pushMessage(fightObject.getRole(), message);
                }
            }
        }
    }
}
