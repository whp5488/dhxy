package com.kitty.mina.cache;

import java.io.Serializable;
import java.util.Map;
import com.kitty.system.game.team.domain.Team;
import com.kitty.common.utils.ConcurrentHashSet;
import java.util.concurrent.ConcurrentHashMap;

public interface SessionCache
{
    public static final ConcurrentHashMap<Integer, ConcurrentHashSet<Integer>> MAPID_ROLEIDS = new ConcurrentHashMap();
    public static final ConcurrentHashMap<Integer, Team> TEAMS = new ConcurrentHashMap();
    public static final ConcurrentHashMap<Integer, Integer> ROLE_TEAM = new ConcurrentHashMap();
    public static final Map<Integer, Map<Serializable, ConcurrentHashSet<Integer>>> GROUP_MAPID_ROLEIDS = new ConcurrentHashMap();
}
