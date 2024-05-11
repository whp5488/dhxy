
package com.kitty.system.game.horse.service;

import com.kitty.system.game.horse.handler.FeedHorseHandler;
import com.kitty.system.common.message.RespNotifyMiscEx;
import org.nutz.lang.Strings;
import com.kitty.system.game.horse.handler.FeedHelper;
import com.kitty.system.game.horse.message.ReqUpgradeHorse;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.horse.message.ReqMountControlPet;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespMsg;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.horse.message.ReqSetMountName;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.horse.message.RespMount;
import com.kitty.system.game.horse.message.vo.Mount;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import java.util.stream.Collector;
import com.kitty.system.game.pet.domain.Pet;
import com.kitty.common.utils.spring.SpringUtils;
import java.util.IdentityHashMap;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.bean.Lottery;
import java.util.ArrayList;
import com.kitty.system.init.domain.SysHorse;
import com.kitty.system.game.horse.pool.HorseDataPool;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.horse.domain.Horse;
import java.util.Map;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class HorseService
{
    Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PetService petService;
    
    public HorseService() {
        this.logger = LoggerFactory.getLogger(HorseService.class);
    }
    
    public Map<Long, Horse> getHorse(final Role role) {
        final Map<Long, Horse> horseMap = DataCache.HORSE_MAP.get(role.getId());
        if (horseMap == null) {
            final List<Horse> list = this.dao.query(Horse.class, Cnd.where("r_id", "=", role.getId()));
            for (final Horse horse : list) {
                horse.doAfterInit();
            }
            final Map<Long, Horse> map = list.stream().collect(Collectors.toMap(Horse::getId, Horse -> Horse));
            DataCache.HORSE_MAP.put(role.getId(), map);
            return map;
        }
        return horseMap;
    }
    
    public void creationHorse(final Role role) {
        final Map<Long, Horse> map = new HashMap<Long, Horse>(4);
        final Integer serial = role.getSerial();
        final Map<Integer, SysHorse> horseMap = HorseDataPool.horseMap;
        for (final Integer key : horseMap.keySet()) {
            final SysHorse sysHorse = horseMap.get(key);
            if (sysHorse.getNeedSerial().contains(serial)) {
                final Horse horse = this.creationHorse(role.getId(), sysHorse);
                horse.setScore(this.score(horse));
                horse.save();
                map.put(horse.getId(), horse);
                if (map.size() >= 4) {
                    break;
                }
                continue;
            }
        }
        DataCache.HORSE_MAP.put(role.getId(), map);
    }
    
    public Horse creationHorse(final long id, final SysHorse sysHorse) {
        final List<Double> rates = new ArrayList<Double>(HorseDataPool.horseGrowList.size());
        for (final Lottery lottery : HorseDataPool.horseGrowList) {
            rates.add(lottery.getBili());
        }
        final int index = LotteryUtils.lottery(rates);
        Lottery lottery = HorseDataPool.horseGrowList.get(index);
        final int[] horseMoveScope = HorseDataPool.horseMoveScope;
        final Horse horse = new Horse(this.worker.nextId(), id, (short)0, sysHorse.getSerial(), sysHorse.getName(), 0L, lottery.getSerial(), RandomUtil.round(horseMoveScope[0], horseMoveScope[1]), sysHorse.getSeat());
        final IdentityHashMap<Integer, Long> skill = this.creationHorseSkill(sysHorse.getSeat(), new IdentityHashMap<Integer, Long>(3));
        horse.setSkill(skill);
        return horse;
    }
    
    public IdentityHashMap<Integer, Long> creationHorseSkill(final byte seat, final IdentityHashMap<Integer, Long> map) {
        if (map.size() <= 0) {
            map.put(1, 0L);
            map.put(2, 0L);
            map.put(3, 0L);
        }
        final IdentityHashMap<Integer, Long> skill = new IdentityHashMap<Integer, Long>(3);
        final int[] horseSkillProb = HorseDataPool.horseSkillProb;
        int i = 1;
        for (final Integer key : map.keySet()) {
            List<Lottery> list = null;
            final int round = RandomUtil.round(horseSkillProb[0], horseSkillProb[1]);
            if (round <= horseSkillProb[2]) {
                list = (List)HorseDataPool.superHorseSkillMap.get(seat).get(i);
            }
            else {
                list = (List)HorseDataPool.commonHorseSkillMap.get(seat).get(i);
            }
            final List<Double> rates = new ArrayList<Double>(list.size());
            for (final Lottery lottery : list) {
                rates.add(lottery.getBili());
            }
            final int index = LotteryUtils.lottery(rates);
            Lottery lottery = list.get(index);
            Long value = map.get(key);
            if (value == null) {
                value = 0L;
            }
            skill.put(lottery.getSerial(), value);
            if (++i > 3) {
                break;
            }
        }
        return skill;
    }
    
    public int score(final Horse horse) {
        final int score = (int)(horse.getGrowUp() * 1.8 + horse.getGrowthDan() * 48 + horse.getMoveSp() * 5 + horse.getLevel() * 20 + this.skillScore(horse));
        return score;
    }
    
    public double skillScore(final Horse horse) {
        int score = 0;
        for (final Integer key : horse.getSkill().keySet()) {
            final Long value = horse.getSkill().get(key);
            int base = 27;
            final Byte grade = HorseDataPool.horseSkillGradeMap.get(key);
            if (grade == null) {
                base = 27;
            }
            else if (grade == 2) {
                base = 75;
            }
            else if (grade == 3) {
                base = 184;
            }
            score += base + (int)(value / 357.0);
        }
        return score;
    }
    
    public void convertMount(final Role role) {
        final Map<Integer, SysHorse> map = HorseDataPool.horseMap;
        final Map<Long, Horse> horseMap = this.getHorse(role);
        for (final Long key : horseMap.keySet()) {
            final Horse horse = horseMap.get(key);
            final SysHorse sysHorse = map.values().stream().filter(e -> e.getNeedSerial().contains(role.getSerial()) && e.getSeat() == horse.getSeat()).findFirst().orElse(null);
            if (sysHorse == null) {
                this.logger.error("种族转换更换坐骑外观数据错误：角色外观编号= {}，位置：{}", (Object)role.getSerial(), (Object)horse.getSeat());
            }
            else {
                String name = sysHorse.getName();
                final SysHorse mount = map.get(horse.getSerial());
                if (!mount.getName().equals(horse.getName())) {
                    name = horse.getName();
                }
                horse.setSerial(sysHorse.getSerial());
                horse.setName(name);
                horse.save();
            }
        }
        this.refresh(role, horseMap);
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
    }
    
    public void vitDispose(final Role role) {
        final Map<Long, Horse> horseMap = this.getHorse(role);
        final Horse horse = horseMap.values().stream().filter(e -> e.getState() == 1).findFirst().orElse(null);
        if (horse == null) {
            return;
        }
        final int vit = horse.getVit() - 3;
        horse.setVit((vit < 0) ? 0 : vit);
        horse.save();
        if (horse.getVit() <= 0) {
            this.calculateControlPet(role, horse.getId());
        }
    }
    
    public void calculateControlPet(final Role role, final long id) {
        final List<Pet> collect = this.petService.selectPet(role).values().stream().filter(e -> e.getHorse() == id).collect(Collectors.toList());
        for (final Pet pet : collect) {
            this.petService.calculateBase(role.getId(), pet);
            pet.save();
            this.petService.sendPetPanels(role, pet);
            EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.petService.getPetEffective(role.getId(), pet)));
        }
        role.save();
    }
    
    public void refresh(final Role role, final Map<Long, Horse> horseMap) {
        final List<Mount> list = new ArrayList<Mount>();
        for (final Long key : horseMap.keySet()) {
            final Horse horse = horseMap.get(key);
            final Mount mount = new Mount(horse);
            list.add(mount);
        }
        MessagePusher.pushMessage(role, new RespMount(list));
    }
    
    public void refresh(final Role role, final Horse horse) {
        if (role == null || horse == null) {
            return;
        }
        final List<Mount> list = new ArrayList<Mount>();
        list.add(new Mount(horse));
        MessagePusher.pushMessage(role, new RespMount(list));
    }
    
    public void changeName(final IoSession session, final ReqSetMountName reqSetMountName) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {}
        final Map<Long, Horse> horseMap = this.getHorse(role);
        final Horse horse = horseMap.get(Long.valueOf(reqSetMountName.getId()));
        if (horse != null) {
            if (!this.roleService.canCreateRole(session, reqSetMountName.getName(), LOGIC.HORSE.getValue())) {
                return;
            }
            final String name = CheckCharUtils.replaceName(reqSetMountName.getName());
            horse.setName(name);
            horse.save();
            this.refresh(role, horse);
        }
        else {
            MessagePusher.pushMessage(session, new RespMsg("未找到该坐骑信息"));
        }
    }
    
    public void rideMount(final Role role, final String mountId, final byte type) {
        final Long id = Long.valueOf(mountId);
        final Map<Long, Horse> horseMap = this.getHorse(role);
        final Horse horse = horseMap.get(id);
        if (horse == null) {
            return;
        }
        if (type == 0) {
            this.comeDown(role, horse);
            return;
        }
        final SysHorse sysHorse = HorseDataPool.horseMap.get(horse.getSerial());
        if (sysHorse == null) {
            return;
        }
        final String msg = DataGlobalFunc.needGradeJudge(sysHorse.getNeedGrade() / 1000, sysHorse.getNeedGrade() % 1000, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        for (final Long key : horseMap.keySet()) {
            final Horse h = horseMap.get(key);
            if (h != null && h.getState() == 1) {
                this.comeDown(role, h);
            }
        }
        horse.setState((byte)1);
        horse.save();
        this.refresh(role, horse);
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
    }
    
    public void comeDown(final Role role, final Horse horse) {
        horse.setState((byte)0);
        horse.save();
        this.refresh(role, horse);
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
    }
    
    public Horse getRideMount(final Role role) {
        final Map<Long, Horse> horseMap = this.getHorse(role);
        for (final Long key : horseMap.keySet()) {
            final Horse horse = horseMap.get(key);
            if (horse != null && horse.getState() == 1) {
                return horse;
            }
        }
        return null;
    }
    
    public void controlPet(final Role role, final ReqMountControlPet controlPet) {
        final Pet pet = this.petService.selectPet(role).get(controlPet.getPetId());
        if (pet == null) {
            return;
        }
        Long id = 0L;
        if (!controlPet.getMountId().isEmpty()) {
            id = Long.valueOf(controlPet.getMountId());
        }
        if (id > 0L) {
            final Horse horse = this.getHorse(role).get(id);
            if (horse == null) {
                return;
            }
        }
        final PetService petService = SpringUtils.getPetService();
        pet.setHorse(id);
        petService.calculateBase(role.getId(), pet);
        pet.save();
        role.save();
        HorseSkillUtils.studyFinish(role, pet.getHorse(), pet);
        petService.sendPetPanels(role, pet);
    }
    
    public void upgrade(final Role role, final ReqUpgradeHorse reqUpgradeHorse) {
        final boolean clicker = this.roleService.mouserClicker(role, "mount", 250L);
        if (!clicker) {
            return;
        }
        final Long id = Long.valueOf(reqUpgradeHorse.getParam());
        final Map<Long, Horse> horseMap = this.getHorse(role);
        final Horse horse = horseMap.get(id);
        if (horse == null) {
            return;
        }
        final short type = reqUpgradeHorse.getType();
        final FeedHorseHandler handler = FeedHelper.INSTANCE.getHandler(reqUpgradeHorse.getType());
        if (handler != null) {
            final String tips = handler.acceptFeed(horse, role, type, reqUpgradeHorse.getOperationType(), reqUpgradeHorse.getRests());
            if (!Strings.isEmpty((CharSequence)tips)) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), tips, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
    
    public void addExp(final Role role, final Horse horse, final long exp) {
        if (exp <= 0L) {
            return;
        }
        boolean flag = false;
        short level = horse.getLevel();
        if (horse.getLevel() >= HorseDataPool.horseLevelMax) {
            return;
        }
        final short limit = HorseDataPool.getGrowthDanLimit(role.getRelive() * 1000 + role.getLevel());
        long totalExp = horse.getExp() + exp;
        long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.HORSE.getValue(), 0, level);
        if (totalExp >= needExp) {
            flag = true;
            while (totalExp >= needExp) {
                totalExp -= needExp;
                ++level;
                horse.setLevel(level);
                horse.setExp(totalExp);
                if (horse.getLevel() >= limit) {
                    break;
                }
                needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.HORSE.getValue(), 0, level);
            }
        }
        else {
            horse.setExp(totalExp);
        }
        if (flag) {
            horse.setScore(this.score(horse));
            this.calculateControlPet(role, horse.getId());
        }
        horse.save();
        this.refresh(role, horse);
    }
    
    public void domesticateEarnings(final Role role, final long id, long exp, final int serial, final int tips) {
        final Map<Long, Horse> horseMap = this.getHorse(role);
        final Horse horse = horseMap.get(id);
        if (horse == null) {
            return;
        }
        final short limit = HorseDataPool.getGrowthDanLimit(role.getRelive() * 1000 + role.getLevel());
        if (horse.getLevel() >= limit) {
            return;
        }
        this.addExp(role, horse, exp);
        if (serial == 703) {
            if (tips > 0) {
                exp = tips;
            }
            MessagePusher.notify2Misc(role, true, 703, horse.getName(), exp);
        }
    }
}
