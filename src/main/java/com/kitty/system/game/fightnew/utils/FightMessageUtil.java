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
    public FightMessageUtil() {
    }
    
    public static void sendFightPackage(Fight fight, List<Message> messages) {
        for (FightObject fightObject : fight.getListA()) {
            if (fightObject.isPlayer()) {
                for (Message message : messages) {
                    MessagePusher.pushMessage(fightObject.getRole(), message);
                }
            }
            else {
                continue;
            }
        }
        if (fight.isPkFight()) {
            for (FightObject fightObject : fight.getListB()) {
                if (fightObject.isPlayer()) {
                    for (Message message : messages) {
                        MessagePusher.pushMessage(fightObject.getRole(), message);
                    }
                }
                else {
                    continue;
                }
            }
        }
    }
    
    public static void sendFightPackage(Fight fight, Message message) {
        if (fight == null || message == null) {
            return;
        }
        sendFightPackage(fight, Collections.singletonList(message));
        return;
    }
    
    public static void sendFightPackage(long roleId, Message message) {
        MessagePusher.pushMessage(roleId, message);
    }
    
    public static void sendFightPackage(Fight fight, Message message, boolean isA, boolean isFightSpetator) {
        if (fight == null || message == null) {
            return;
        }
        sendFightPackage(fight, Collections.singletonList(message), isA, isFightSpetator);
        return;
    }
    
    public static void sendFightPackage(Fight fight, List<Message> messages, boolean isA, boolean isFightSpetator) {
        List<FightObject> targets = isA ? fight.getListA() : fight.getListB();
        for (Message message : messages) {
            for (FightObject fightObject : targets) {
                if (fightObject.isPlayer()) {
                    MessagePusher.pushMessage(fightObject.getRole(), message);
                }
                else {
                    continue;
                }
            }
        }
    }
}
