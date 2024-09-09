package com.kitty.mina.cache;

import com.kitty.system.game.train.domain.TrainSeal;
import java.util.Map;
import com.kitty.system.game.train.domain.Train;
import java.util.concurrent.ConcurrentHashMap;

public interface TrainCache
{
    public static final ConcurrentHashMap<Long, Train> TRAIN_MAP = new ConcurrentHashMap();
    public static final ConcurrentHashMap<Long, Map<Long, TrainSeal>> TRAIN_SEAL_MAP = new ConcurrentHashMap();
}
