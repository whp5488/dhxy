package com.kitty.system.game.horse.service;

import com.kitty.system.game.horse.handler.FeedHorseHandler;
import com.kitty.system.common.message.RespNotifyMiscEx;
import org.nutz.lang.Strings;
import com.kitty.system.game.horse.handler.FeedHelper;
import com.kitty.system.game.horse.message.ReqUpgradeHorse;
import com.kitty.system.game.fightnew.book.horse.utils.HorseSkillUtils;
import com.kitty.system.game.horse.message.ReqMountControlPet;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.CheckCharUtils;
import com.kitty.system.enums.war.wardef.LOGIC;
import com.kitty.system.common.message.RespMsg;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.horse.message.ReqSetMountName;
import org.apache.mina.core.session.IoSession;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.horse.message.RespMount;
import com.kitty.system.game.horse.message.vo.Mount;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
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
import java.util.stream.Collectors;
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
    
    public Map<Long, Horse> getHorse(Role role) {
        Map<Long, Horse> horseMap = (Map)DataCache.HORSE_MAP.get(role.getId());
        if (horseMap == null) {
            List<Horse> list = this.dao.query(Horse.class, Cnd.where("r_id", "=", role.getId()));
            for (Horse horse : list) {
                horse.doAfterInit();
            }
            Map<Long, Horse> map = (Map)list.stream().collect(Collectors.toMap(Horse::getId, Horse -> Horse));
            DataCache.HORSE_MAP.put(role.getId(), map);
            return map;
        }
        return horseMap;
    }
    
    public void creationHorse(Role role) {
        Map<Long, Horse> map = new HashMap(4);
        Integer serial = role.getSerial();
        Map<Integer, SysHorse> horseMap = HorseDataPool.horseMap;
        for (Integer key : horseMap.keySet()) {
            SysHorse sysHorse = (SysHorse)horseMap.get(key);
            if (sysHorse.getNeedSerial().contains(serial)) {
                Horse horse = this.creationHorse((long)role.getId(), sysHorse);
                horse.setScore(this.score(horse));
                horse.save();
                map.put(horse.getId(), horse);
                if (map.size() >= 4) {
                    break;
                }
                continue;
            }
            else {
                continue;
            }
        }
        DataCache.HORSE_MAP.put(role.getId(), map);
    }
    
    public Horse creationHorse(long id, SysHorse sysHorse) {
        List<Double> rates = new ArrayList(HorseDataPool.horseGrowList.size());
        for (Lottery lottery : HorseDataPool.horseGrowList) {
            rates.add(lottery.getBili());
        }
        int index = LotteryUtils.lottery(rates);
        Lottery lottery = (Lottery)HorseDataPool.horseGrowList.get(index);
        int[] horseMoveScope = HorseDataPool.horseMoveScope;
        Horse horse = new Horse(Long.valueOf(this.worker.nextId()), id, (short)0, sysHorse.getSerial(), sysHorse.getName(), 0L, (int)lottery.getSerial(), RandomUtil.round(Integer.valueOf(horseMoveScope[0]), Integer.valueOf(horseMoveScope[1])), sysHorse.getSeat());
        IdentityHashMap<Integer, Long> skill = this.creationHorseSkill(sysHorse.getSeat(), new IdentityHashMap(3));
        horse.setSkill(skill);
        return horse;
    }
    
    public IdentityHashMap<Integer, Long> creationHorseSkill(byte seat, IdentityHashMap<Integer, Long> map) {
        if (map.size() <= 0) {
            map.put(Integer.valueOf(1), Long.valueOf(0L));
            map.put(Integer.valueOf(2), Long.valueOf(0L));
            map.put(Integer.valueOf(3), Long.valueOf(0L));
        }
        IdentityHashMap<Integer, Long> skill = new IdentityHashMap(3);
        int[] horseSkillProb = HorseDataPool.horseSkillProb;
        int i = 1;
        for (Integer key : map.keySet()) {
            List<Lottery> list = null;
            int round = RandomUtil.round(Integer.valueOf(horseSkillProb[0]), Integer.valueOf(horseSkillProb[1]));
            if (round <= horseSkillProb[2]) {
                list = (List)((Map)HorseDataPool.superHorseSkillMap.get(Byte.valueOf(seat))).get(Integer.valueOf(i));
            }
            else {
                list = (List)((Map)HorseDataPool.commonHorseSkillMap.get(Byte.valueOf(seat))).get(Integer.valueOf(i));
            }
            List<Double> rates = new ArrayList(list.size());
            for (Lottery lottery : list) {
                rates.add(lottery.getBili());
            }
            int index = LotteryUtils.lottery(rates);
            Lottery lottery = (Lottery)list.get(index);
            Long value = (Long)map.get(key);
            if (value == null) {
                value = Long.valueOf(0L);
            }
            skill.put(lottery.getSerial(), value);
            if (++i > 3) {
                break;
            }
            continue;
        }
        return skill;
    }
    
    public int score(Horse horse) {
        int score = (int)((double)horse.getGrowUp() * 1.8 + (double)(horse.getGrowthDan() * 48) + (double)(horse.getMoveSp() * 5) + (double)(horse.getLevel() * 20) + this.skillScore(horse));
        return score;
    }
    
    public double skillScore(Horse horse) {
        int score = 0;
        for (Integer key : horse.getSkill().keySet()) {
            Long value = (Long)horse.getSkill().get(key);
            int base = 27;
            Byte grade = (Byte)HorseDataPool.horseSkillGradeMap.get(key);
            if (grade == null) {
                base = 27;
            }
            else if ((byte)grade == 2) {
                base = 75;
            }
            else if ((byte)grade == 3) {
                base = 184;
            }
            score += base + (int)((double)(long)value / 357.0);
        }
        return (double)score;
    }
    
    public void convertMount(Role role) {
        Map<Integer, SysHorse> map = HorseDataPool.horseMap;
        Map<Long, Horse> horseMap = this.getHorse(role);
        for (Long key : horseMap.keySet()) {
            Horse horse = (Horse)horseMap.get(key);
            SysHorse sysHorse = (SysHorse)map.values().stream().filter(e -> e.getNeedSerial().contains(role.getSerial()) && e.getSeat() == horse.getSeat()).findFirst().orElse(null);
            if (sysHorse == null) {
                this.logger.error("种族转换更换坐骑外观数据错误：角色外观编号= {}，位置：{}", role.getSerial(), Byte.valueOf(horse.getSeat()));
            }
            else {
                String name = sysHorse.getName();
                SysHorse mount = (SysHorse)map.get(Integer.valueOf(horse.getSerial()));
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
    
    public void vitDispose(Role role) {
        Map<Long, Horse> horseMap = this.getHorse(role);
        Horse horse = (Horse)horseMap.values().stream().filter(e -> e.getState() == 1).findFirst().orElse(null);
        if (horse == null) {
            return;
        }
        int vit = horse.getVit() - 3;
        horse.setVit((vit < 0) ? 0 : vit);
        horse.save();
        if (horse.getVit() <= 0) {
            this.calculateControlPet(role, (long)horse.getId());
        }
        return;
    }
    
    public void calculateControlPet(Role role, long id) {
        List<Pet> collect = (List)this.petService.selectPet(role).values().stream().filter(e -> e.getHorse() == id).collect(Collectors.toList());
        for (Pet pet : collect) {
            this.petService.calculateBase((long)role.getId(), pet);
            pet.save();
            this.petService.sendPetPanels(role, pet);
            EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, this.petService.getPetEffective((long)role.getId(), pet)));
        }
        role.save();
    }
    
    public void refresh(Role role, Map<Long, Horse> horseMap) {
        List<Mount> list = new ArrayList<>();
        for (Long key : horseMap.keySet()) {
            Horse horse = (Horse)horseMap.get(key);
            Mount mount = new Mount(horse);
            list.add(mount);
        }
        MessagePusher.pushMessage(role, new RespMount(list));
    }
    
    public void refresh(Role role, Horse horse) {
        if (role == null || horse == null) {
            return;
        }
        List<Mount> list = new ArrayList<>();
        list.add(new Mount(horse));
        MessagePusher.pushMessage(role, new RespMount(list));
        return;
    }
    
    public void changeName(IoSession session, ReqSetMountName reqSetMountName) {
        Role role = SessionUtils.getRoleBySession(session);
        Map<Long, Horse> horseMap = this.getHorse(role);
        Horse horse = (Horse)horseMap.get(Long.valueOf(reqSetMountName.getId()));
        if (horse != null) {
            if (!this.roleService.canCreateRole(session, reqSetMountName.getName(), LOGIC.HORSE.getValue())) {
                return;
            }
            String name = CheckCharUtils.replaceName(reqSetMountName.getName());
            horse.setName(name);
            horse.save();
            this.refresh(role, horse);
        }
        else {
            MessagePusher.pushMessage(session, new RespMsg("未找到该坐骑信息"));
        }
    }
    
    public void rideMount(Role role, String mountId, byte type) {
        Long id = Long.valueOf(mountId);
        Map<Long, Horse> horseMap = this.getHorse(role);
        Horse horse = (Horse)horseMap.get(id);
        if (horse == null) {
            return;
        }
        if (type == 0) {
            this.comeDown(role, horse);
            return;
        }
        SysHorse sysHorse = (SysHorse)HorseDataPool.horseMap.get(Integer.valueOf(horse.getSerial()));
        if (sysHorse == null) {
            return;
        }
        String msg = DataGlobalFunc.needGradeJudge(sysHorse.getNeedGrade() / 1000, sysHorse.getNeedGrade() % 1000, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
        if (msg != null) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
            return;
        }
        for (Long key : horseMap.keySet()) {
            Horse h = (Horse)horseMap.get(key);
            if (h != null && h.getState() == 1) {
                this.comeDown(role, h);
            }
            else {
                continue;
            }
        }
        horse.setState((byte)1);
        horse.save();
        this.refresh(role, horse);
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
        return;
    }
    
    public void comeDown(Role role, Horse horse) {
        horse.setState((byte)0);
        horse.save();
        this.refresh(role, horse);
        SpringUtils.getBroadcastService().sendUpdateAppear(role);
    }
    
    public Horse getRideMount(Role role) {
        Map<Long, Horse> horseMap = this.getHorse(role);
        for (Long key : horseMap.keySet()) {
            Horse horse = (Horse)horseMap.get(key);
            if (horse != null && horse.getState() == 1) {
                return horse;
            }
            continue;
        }
        return null;
    }
    
    public void controlPet(Role role, ReqMountControlPet controlPet) {
        Pet pet = (Pet)this.petService.selectPet(role).get(Integer.valueOf(controlPet.getPetId()));
        if (pet == null) {
            return;
        }
        Long id = Long.valueOf(0L);
        if (!controlPet.getMountId().isEmpty()) {
            id = Long.valueOf(controlPet.getMountId());
        }
        if ((long)id > 0L) {
            Horse horse = (Horse)this.getHorse(role).get(id);
            if (horse == null) {
                return;
            }
        }
        PetService petService = SpringUtils.getPetService();
        pet.setHorse((long)id);
        petService.calculateBase((long)role.getId(), pet);
        pet.save();
        role.save();
        HorseSkillUtils.studyFinish(role, pet.getHorse(), pet);
        petService.sendPetPanels(role, pet);
        return;
    }
    
    public void upgrade(Role role, ReqUpgradeHorse reqUpgradeHorse) {
        boolean clicker = this.roleService.mouserClicker(role, "mount", 250L);
        if (!clicker) {
            return;
        }
        Long id = Long.valueOf(reqUpgradeHorse.getParam());
        Map<Long, Horse> horseMap = this.getHorse(role);
        Horse horse = (Horse)horseMap.get(id);
        if (horse == null) {
            return;
        }
        short type = reqUpgradeHorse.getType();
        FeedHorseHandler handler = FeedHelper.INSTANCE.getHandler(reqUpgradeHorse.getType());
        if (handler != null) {
            String tips = handler.acceptFeed(horse, role, type, reqUpgradeHorse.getOperationType(), Integer.valueOf(reqUpgradeHorse.getRests()));
            if (!Strings.isEmpty(tips)) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), tips, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
        return;
    }
    
    public void addExp(Role role, Horse horse, long exp) {
        if (exp <= 0L) {
            return;
        }
        boolean flag = false;
        short level = horse.getLevel();
        if (horse.getLevel() >= HorseDataPool.horseLevelMax) {
            return;
        }
        short limit = HorseDataPool.getGrowthDanLimit((int)role.getRelive() * 1000 + (int)role.getLevel());
        long totalExp = horse.getExp() + exp;
        long needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.HORSE.getValue(), 0, (int)level);
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
                needExp = DataGlobalFunc.getExpForRebirthLevel(LOGIC.HORSE.getValue(), 0, (int)level);
            }
        }
        else {
            horse.setExp(totalExp);
        }
        if (flag) {
            horse.setScore(this.score(horse));
            this.calculateControlPet(role, (long)horse.getId());
        }
        horse.save();
        this.refresh(role, horse);
        return;
    }
    
    public void domesticateEarnings(Role role, long id, long exp, int serial, int tips) {
        Map<Long, Horse> horseMap = this.getHorse(role);
        Horse horse = (Horse)horseMap.get(Long.valueOf(id));
        if (horse == null) {
            return;
        }
        short limit = HorseDataPool.getGrowthDanLimit((int)role.getRelive() * 1000 + (int)role.getLevel());
        if (horse.getLevel() >= limit) {
            return;
        }
        this.addExp(role, horse, exp);
        if (serial == 703) {
            if (tips > 0) {
                exp = (long)tips;
            }
            MessagePusher.notify2Misc(role, true, 703, horse.getName(), Long.valueOf(exp));
        }
        return;
    }
}
