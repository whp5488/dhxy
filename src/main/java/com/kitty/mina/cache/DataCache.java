package com.kitty.mina.cache;

import com.kitty.system.game.activity.domain.WeekTreasure;
import com.kitty.system.game.activity.model.Dugeon;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.chat.domain.RoleOfflineMessage;
import com.kitty.system.game.email.domain.Email;
import com.kitty.system.game.equip.domain.Effect;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.gate.LoginAuth;
import com.kitty.system.game.horse.domain.Horse;
import com.kitty.system.game.house.model.House;
import com.kitty.system.game.mirror.model.FightMirror;
import com.kitty.system.game.npc.domain.Mirror;
import com.kitty.system.game.npc.domain.SysNpcInfo;
import com.kitty.system.game.partner.domain.Partner;
import com.kitty.system.game.role.domain.Domesticate;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.domain.Setting;
import com.kitty.system.game.suit.domain.Suit;
import com.kitty.system.game.tianfu.domain.RoleDayInCe;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.welfare.domain.FiveGuess;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;

public interface DataCache {
  public static final ConcurrentHashMap<Long, Role> ONLINE_ROLES = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Role> OFFLINE_ROLES = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Setting> SETTING_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, List<WearEquip>> EQUIP_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Map<Long, CardChange>> CUTTING_FERRULE_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, CardExercise> CARD_EXERCISE_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Map<Long, Horse>> HORSE_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Map<Integer, Partner>> PARTNER_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, List<Effect>> EFFECT_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, FiveGuess> FIVE_GUESS_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Map<Long, WeekTreasure>> WEEK_TREASURE = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, List<Domesticate>> DOMESTICATE_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Map<Long, Suit>> SUIT_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, List<House>> HOUSE_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<String, LoginAuth> LOGIN_AUTH_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, List<String>> GIFT_RECORD = new ConcurrentHashMap<>();
  
  public static final Map<Long, FightMirror> FIGHT_MIRRORS = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, SysNpcInfo> PLACE_NPC = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, SysNpcInfo> ID_NPC = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Set<Integer>> MAP_NPC_ID_S = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<String, Integer> NAME_NPC_ID = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Npc> SCENE_TASK_NPC = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Npc> BOOS_OFFICER_SET = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Set<Integer>> BOOS_OFFICER_MAP_NPC = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Npc> STAR_OFFICER_SET = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Set<Integer>> STAR_OFFICER_MAP_NPC = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Npc> SEAL_DEMON_SET = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Set<Integer>> SEAL_DEMON_MAP_NPC = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Npc> THREE_SCOPE_SET = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Set<Integer>> THREE_SCOPE_MAP_NPC = new ConcurrentHashMap<>();
  
  public static final Map<Integer, Npc> GROUP_NPC = new ConcurrentHashMap<>();
  
  public static final Map<Integer, Map<Serializable, Set<Integer>>> GROUP_NPC_ID_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Mirror> MIRROR_MAP = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Integer, Set<Long>> MIRROR_MAP_NPC = new ConcurrentHashMap<>();
  
  public static final Map<Long, Dugeon> DUNGEON_MAP = new ConcurrentHashMap<>();
  
  public static final Map<Long, Long> ROLE_DUNGEON = new ConcurrentHashMap<>();
  
  public static final Map<Long, RoleOfflineMessage> OFFLINE_MESSAGES = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, List<RoleTianFu>> ROLE_TIAN_FU = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, RoleDayInCe> ROLE_DAY_IN_CE = new ConcurrentHashMap<>();
  
  public static final ConcurrentHashMap<Long, Lock> UID_LOCK_MAP = new ConcurrentHashMap<>(2048);
  
  public static final ConcurrentHashMap<Long, List<Email>> ROLE_EMAIL = new ConcurrentHashMap<>();
}
