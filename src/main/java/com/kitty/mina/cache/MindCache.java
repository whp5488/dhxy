package com.kitty.mina.cache;

import com.kitty.system.game.mind.domain.Mind;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface MindCache
{
    public static final ConcurrentHashMap<Long, List<Mind>> MIND_MAP = new ConcurrentHashMap();
}
