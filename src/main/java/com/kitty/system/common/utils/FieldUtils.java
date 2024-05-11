
package com.kitty.system.common.utils;

import com.kitty.system.game.vo.Npc;
import java.util.ArrayList;
import com.kitty.system.common.enter.FiedValue;
import java.util.List;
import com.kitty.system.game.role.domain.Role;

public class FieldUtils
{
    public static List<FiedValue> getFriendChatList(final Role role) {
        return getFriendChatList(role.getRoleId(), role.getLevel(), role.getIcon());
    }
    
    public static List<FiedValue> getFriendChatList(final Integer roleId, final Integer level, final Integer icon) {
        final List<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("id", (byte)3, roleId));
        list.add(new FiedValue("level", (byte)3, level));
        list.add(new FiedValue("icon", (byte)3, icon));
        list.add(new FiedValue("t", (byte)1, 0));
        return list;
    }
    
    public static ArrayList<FiedValue> getChatList(final Role role) {
        final ArrayList<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("id", (byte)3, role.getRoleId()));
        list.add(new FiedValue("level", (byte)3, role.getLevel()));
        list.add(new FiedValue("icon", (byte)3, role.getIcon()));
        list.add(new FiedValue("t", (byte)1, 0));
        return list;
    }
    
    public static ArrayList<FiedValue> getChatList(final Npc npc) {
        final ArrayList<FiedValue> list = new ArrayList<FiedValue>();
        list.add(new FiedValue("icon", (byte)3, npc.getIcon()));
        list.add(new FiedValue("t", (byte)1, 1));
        return list;
    }
}
