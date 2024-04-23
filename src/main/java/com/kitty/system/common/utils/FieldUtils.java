package com.kitty.system.common.utils;

import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.vo.Npc;
import java.util.ArrayList;
import java.util.List;

public class FieldUtils {
  public static List<FiedValue> getFriendChatList(Role role) {
    return getFriendChatList(role.getRoleId(), role.getLevel(), Integer.valueOf(role.getIcon()));
  }
  
  public static List<FiedValue> getFriendChatList(Integer roleId, Integer level, Integer icon) {
    List<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("id", (byte)3, roleId));
    list.add(new FiedValue("level", (byte)3, level));
    list.add(new FiedValue("icon", (byte)3, icon));
    list.add(new FiedValue("t", (byte)1, Byte.valueOf((byte)0)));
    return list;
  }
  
  public static ArrayList<FiedValue> getChatList(Role role) {
    ArrayList<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("id", (byte)3, role.getRoleId()));
    list.add(new FiedValue("level", (byte)3, role.getLevel()));
    list.add(new FiedValue("icon", (byte)3, Integer.valueOf(role.getIcon())));
    list.add(new FiedValue("t", (byte)1, Byte.valueOf((byte)0)));
    return list;
  }
  
  public static ArrayList<FiedValue> getChatList(Npc npc) {
    ArrayList<FiedValue> list = new ArrayList<>();
    list.add(new FiedValue("icon", (byte)3, Integer.valueOf(npc.getIcon())));
    list.add(new FiedValue("t", (byte)1, Byte.valueOf((byte)1)));
    return list;
  }
}
