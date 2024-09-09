package com.kitty.system.game.ranking;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import com.kitty.system.game.activity.model.user.ca.CaRankRecord;
import com.kitty.system.game.activity.model.user.lu.WaterRankRecord;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.system.game.ranking.pojo.QlkRankingSource;
import com.kitty.system.game.ranking.pojo.WeaponsRankingSource;
import com.kitty.system.game.ranking.pojo.PractitionersSource;
import com.kitty.system.game.ranking.pojo.ComprehensiveStrengthSource;
import com.kitty.system.game.ranking.pojo.PetRankingSource;
import com.kitty.system.game.equip.domain.WearEquip;
import java.util.Map;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.system.game.role.domain.Role;
import java.util.List;

public interface RankingList
{
    public static final List<Role> roles = new ArrayList<>();
    public static final List<Pet> pets = new ArrayList<>();
    public static final Map<Integer, List<Pet>> petMaps = new HashMap();
    public static final List<WearEquip> wearEquips = new ArrayList<>();
    public static final Map<Long, List<WearEquip>> wearEquipMaps = new HashMap();
    public static final List<PetRankingSource> petRankingListSource = Collections.synchronizedList(new ArrayList<>());
    public static final List<ComprehensiveStrengthSource> comprehensiveStrengthSources = Collections.synchronizedList(new ArrayList<>());
    public static final List<PractitionersSource> practitionersSources = Collections.synchronizedList(new ArrayList<>());
    public static final List<WeaponsRankingSource> weaponsRankingSources = Collections.synchronizedList(new ArrayList<>());
    public static final List<QlkRankingSource> qlkRankingSources = Collections.synchronizedList(new ArrayList<>());
    public static final ConcurrentHashMap<Long, WaterRankRecord> WATER_RANK_RECORDS = new ConcurrentHashMap();
    public static final ConcurrentHashMap<Long, CaRankRecord> CA_RANK_RECORDS = new ConcurrentHashMap();
}
