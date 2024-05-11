
package com.kitty.system.game.card.service;

import com.kitty.listener.event.HeartBeatEvent;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_TYPE;
import com.kitty.system.init.domain.vo.Param;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.card.message.RespFiveExercise;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.init.domain.SysDataCardFiveExercise;
import com.kitty.system.common.message.RespMsg;
import java.util.Comparator;
import java.util.ArrayList;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.card.message.RespCardFlyInfo;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.bag.message.RespIconCartoon;
import com.kitty.system.common.data.DataGlobalFunc;
import java.util.stream.Collector;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.game.card.pool.CardDataPool;
import com.kitty.system.init.domain.SysDataCard;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Iterator;
import com.kitty.listener.event.LoginEvent;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.kitty.system.game.card.domain.CardChange;
import java.util.Map;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.card.domain.CardExercise;
import com.kitty.system.game.role.domain.Role;
import org.slf4j.LoggerFactory;
import com.kitty.system.game.broadcast.BroadcastService;
import com.kitty.system.game.team.service.TeamService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import com.kitty.system.game.echo.handler.EchoHandler;

@Service
public class CardService extends EchoHandler
{
    Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TeamService teamService;
    @Autowired
    private BroadcastService broadcastService;
    
    public CardService() {
        this.logger = LoggerFactory.getLogger(CardService.class);
    }
    
    public CardExercise getCardExercise(final Role role) {
        final CardExercise cardExercise = DataCache.CARD_EXERCISE_MAP.get(role.getId());
        if (cardExercise == null) {
            CardExercise fetch = (CardExercise)this.dao.fetch(CardExercise.class, Cnd.where("r_id", "=", (Object)role.getId()));
            if (fetch == null) {
                fetch = new CardExercise(this.worker.nextId(), role.getId());
                fetch.save();
                this.logger.error("获取变身卡-五行修炼，数据为空角色编号：{}，角色SID：{}", role.getRoleId(), (Object)role.getSid());
            }
            DataCache.CARD_EXERCISE_MAP.put(role.getId(), fetch);
            return fetch;
        }
        return cardExercise;
    }
    
    public Map<Long, CardChange> getCard(final Role role) {
        final Map<Long, CardChange> cardChangeMap = DataCache.CUTTING_FERRULE_MAP.get(role.getId());
        if (cardChangeMap == null) {
            final List<CardChange> list = this.dao.query(CardChange.class, Cnd.where("r_id", "=", (Object)role.getId()));
            final Map<Long, CardChange> map = list.stream().collect(Collectors.toMap(CardChange::getId, CardChange -> CardChange));
            DataCache.CUTTING_FERRULE_MAP.put(role.getId(), map);
            return map;
        }
        return cardChangeMap;
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(final LoginEvent loginEvent) {
        final Role role = loginEvent.getRole();
        final Map<Long, CardChange> cardMap = this.getCard(role);
        for (final Long key : cardMap.keySet()) {
            final CardChange cardChange = cardMap.get(key);
            this.refresh(role, cardChange);
        }
    }
    
    public int addCard(final Role role, final Integer serial, Integer num, final Integer bd, final boolean isCartoon, final boolean isTips) {
        final SysDataCard card = CardDataPool.cardMap.get(serial);
        if (card == null) {
            this.logger.error("角色{} 未找到变身卡：编号为 = {}, 数量 = {}", role.getName(), serial, num);
            return -1;
        }
        if (isTips) {
            MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), String.format("你获得了#<II%d>##<MP:0,M:1,MT:%d,CI:%d}>[%s]#", card.getSerial(), card.getSerial(), card.getSerial(), card.getName()), true));
        }
        final Map<Long, CardChange> map = this.getCard(role);
        final int canMerge = card.getCanMerge();
        final List<CardChange> collect = map.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
        for (final CardChange cardChange : collect) {
            final int residue = canMerge - DataGlobalFunc.getCardChangeNum(cardChange);
            if (residue <= 0) {
                continue;
            }
            final int count = 0;
            if (residue >= num) {
                this.saveCard(role, serial, num, cardChange, count, bd, isCartoon);
                num = 0;
                break;
            }
            num = Math.abs(residue - num);
            this.saveCard(role, serial, residue, cardChange, num, bd, isCartoon);
        }
        while (num > 0) {
            int add = 0;
            final int mod = num - canMerge;
            if (mod > 0) {
                add = canMerge;
            }
            else {
                add = num;
            }
            num = mod;
            final CardChange cardChange2 = this.newAddCard(role, serial, add, bd, isCartoon);
            map.put(cardChange2.getId(), cardChange2);
            cardChange2.save();
        }
        return num;
    }
    
    public CardChange newAddCard(final Role role, final Integer serial, final Integer num, final Integer bd, final boolean isCartoon) {
        final CardChange cardChange = new CardChange();
        cardChange.setId(this.worker.nextId());
        cardChange.setrId(role.getId());
        cardChange.setSerial(serial);
        cardChange.getRests().setv(String.valueOf(bd), (Object)num);
        if (isCartoon) {
            this.cartoon(role, serial);
        }
        this.refresh(role, cardChange);
        return cardChange;
    }
    
    public boolean saveCard(final Role role, final Integer serial, final Integer num, final CardChange cardChange, final int surplus, final int bd, final boolean isCartoon) {
        final String key = String.valueOf(bd);
        final int count = cardChange.getRests().getInt(key, 0);
        cardChange.getRests().setv(key, (Object)(num + count));
        this.refresh(role, cardChange);
        if (surplus >= 0 && isCartoon) {
            this.cartoon(role, serial);
        }
        cardChange.save();
        return true;
    }
    
    public void cartoon(final Role role, final Integer serial) {
        if (this.roleService.mouserCartoon(role, serial.toString(), 200L)) {
            final RespIconCartoon respIconCartoon = new RespIconCartoon();
            respIconCartoon.setSerial(serial);
            MessagePusher.pushMessage(role, respIconCartoon);
        }
    }
    
    public void refresh(final Role role, final CardChange cardChange) {
        final RespCardFlyInfo cardFlyInfo = new RespCardFlyInfo(String.valueOf(cardChange.getId()), cardChange.getSerial(), JsonUtils.map2String((Map<?, ?>)cardChange.getRests()));
        MessagePusher.pushMessage(role, cardFlyInfo);
    }
    
    public boolean cardDetach(final Role role, final int serial, int num) {
        boolean flag = false;
        final SysDataCard dataCard = CardDataPool.cardMap.get(serial);
        if (dataCard == null) {
            return flag;
        }
        final Map<Long, CardChange> cardMap = this.getCard(role);
        final List<CardChange> collect = cardMap.values().stream().filter(e -> e.getSerial() == serial).collect(Collectors.toList());
        for (final CardChange card : collect) {
            if (num <= 0) {
                flag = true;
                break;
            }
            final int cardChangeNum = DataGlobalFunc.getCardChangeNum(card);
            if (cardChangeNum >= num) {
                this.consume(role, cardMap, card, num);
                flag = true;
                break;
            }
            card.setRests(new NutMap());
            card.setDelete(true);
            cardMap.remove(card.getId());
            card.save();
            this.refresh(role, card);
            num -= cardChangeNum;
        }
        return flag;
    }
    
    public void useCar(final Role role, final int serial, final long id) {
        final SysDataCard card = CardDataPool.cardMap.get(serial);
        if (card == null) {
            this.logger.error("使用变身卡角色{} 未找到变身卡：编号为 = {}", (Object)role.getName(), (Object)serial);
            return;
        }
        final int grade = card.getNeedOwnerGrade();
        final String msg = DataGlobalFunc.needGradeJudge(grade / 1000, grade % 1000, role.getRelive(), role.getLevel(), "");
        if (msg != null) {
            MessagePusher.notify2Player(role, 900, msg, card.getName());
            return;
        }
        if (this.getCardExercise(role).getFiveGrade() < card.getNeedFiveGrade()) {
            MessagePusher.notify2Player(role, 901, card.getNeedFiveGrade(), card.getName());
            return;
        }
        final Map<Long, CardChange> map = this.getCard(role);
        final CardChange cardChange = map.values().stream().filter(e -> e.getSerial() == serial && e.getId().equals(id)).findFirst().orElse(null);
        if (cardChange != null) {
            final NutMap rests = cardChange.getRests();
            String key = String.valueOf(2);
            int num = rests.getInt(key);
            if (num > 0) {
                this.transfiguration(role, card.getSerial(), card.getExpire() * 60 + new Long(System.currentTimeMillis() / 1000L).intValue(), card.getName());
                this.useCarTrailing(role, map, cardChange, key, num);
                rests.setv(key, (Object)(num - 1));
                return;
            }
            final List<Integer> list = new ArrayList<Integer>();
            for (final String s : rests.keySet()) {
                list.add(Integer.valueOf(s));
            }
            final List<Integer> collect = list.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
            key = String.valueOf(collect.get(0));
            num = rests.getInt(key);
            this.transfiguration(role, card.getSerial(), card.getExpire() * 60 + new Long(System.currentTimeMillis() / 1000L).intValue(), card.getName());
            this.useCarTrailing(role, map, cardChange, key, num);
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("未获取到您要使用的变身卡"));
        }
    }
    
    public void useCarTrailing(final Role role, final Map<Long, CardChange> map, final CardChange card, final String key, int num) {
        --num;
        if (num <= 0) {
            card.getRests().remove((Object)key);
        }
        else {
            card.getRests().setv(key, (Object)num);
        }
        final int count = DataGlobalFunc.getCardChangeNum(card);
        if (count <= 0) {
            card.setDelete(true);
            map.remove(card.getId());
        }
        card.save();
        this.refresh(role, card);
    }
    
    public void transfiguration(final Role role, final int serial, final int time, final String name) {
        boolean flag = false;
        if (role.getMasquerade() > 0) {
            flag = true;
        }
        role.setMasquerade(serial);
        role.setCardExpireTime(time);
        this.roleService.calculateBase(role);
        this.roleService.refreshRole(role);
        this.roleService.stateRefresh(role);
        this.broadcastService.sendUpdateAppear(role);
        this.teamService.updateTeamList(role);
        String msg = "";
        if (serial > 0 && name != null) {
            msg = "你使用了#<R>" + name + "#，变的更加强大了#<E:89>#";
            if (flag) {
                msg = "你使用了#<R>" + name + "#，一阵烟雾过后获得了新的造型，变的更加强大了#<E:89>#";
            }
        }
        else if (name != null) {
            msg = "#<R>" + name + "#失效了";
        }
        if (!msg.isEmpty()) {
            MessagePusher.pushMessage(role, new RespMsg(msg));
        }
        role.save();
    }
    
    public void upgradeFiveGrade(final Role role) {
        final boolean clicker = this.roleService.mouserClicker(role, "up_car_five", 250L);
        if (!clicker) {
            return;
        }
        final CardExercise cardExercise = this.getCardExercise(role);
        if (cardExercise != null && this.upgradeFiveGradeCheck(role, cardExercise)) {
            final int fiveGrade = cardExercise.getFiveGrade();
            final SysDataCardFiveExercise exercise = CardDataPool.cardFiveExerciseMap.get(cardExercise.getFiveGrade());
            final long wx = exercise.getWx();
            final boolean flag = this.roleService.checkCurrency(role, Const.RESTYPE_COIN, wx);
            if (!flag) {
                return;
            }
            this.roleService.deductCurrency(role, Const.RESTYPE_COIN, wx, "upgradeFiveGrade");
            cardExercise.setFiveGrade(fiveGrade + 1);
            cardExercise.save();
            final List<FiedValue> objects = new ArrayList<FiedValue>();
            objects.add(new FiedValue("grade", (byte)2, cardExercise.getFiveGrade()));
            MessagePusher.pushMessage(role, new RespFiveExercise(objects));
            MessagePusher.pushMessage(role, new RespMsg("你消耗了#<IR" + Const.RESTYPE_COIN + ">#" + MoneyUtil.colours(wx) + "，五行等级提示到了" + cardExercise.getFiveGrade() + "级"));
            this.roleService.calculateBase(role);
        }
    }
    
    public boolean upgradeFiveGradeCheck(final Role role, final CardExercise exercise) {
        if (exercise == null) {
            return false;
        }
        boolean flag = true;
        final int nextFiveGrade = exercise.getFiveGrade() + 1;
        if (exercise.getGoldGrade() < nextFiveGrade) {
            flag = false;
            MessagePusher.pushMessage(role, new RespMsg("请先提升#<r:16,g:243,b:239>金#五行修炼等级"));
        }
        else if (exercise.getWoodGrade() < nextFiveGrade) {
            flag = false;
            MessagePusher.pushMessage(role, new RespMsg("请先提升#<r:16,g:243,b:239>木#五行修炼等级"));
        }
        else if (exercise.getWaterGrade() < nextFiveGrade) {
            flag = false;
            MessagePusher.pushMessage(role, new RespMsg("请先提升#<r:16,g:243,b:239>水#五行修炼等级"));
        }
        else if (exercise.getFireGrade() < nextFiveGrade) {
            flag = false;
            MessagePusher.pushMessage(role, new RespMsg("请先提升#<r:16,g:243,b:239>火#五行修炼等级"));
        }
        else if (exercise.getFireGrade() < nextFiveGrade) {
            flag = false;
            MessagePusher.pushMessage(role, new RespMsg("请先提升#<r:16,g:243,b:239>土#五行修炼等级"));
        }
        return flag;
    }
    
    public boolean addExp(final Role role, final int exp, final int type, final CardExercise cardExercise, final Param param, final String reason) {
        if (exp <= 0) {
            return false;
        }
        int level = param.getK();
        int totalExp = (param.getV() + exp > Const.INT_LIMIT) ? Const.INT_LIMIT : (param.getV() + exp);
        int needExp = this.getNeedExpWxInfo(CardDataPool.cardFiveExerciseMap.get(level), type);
        if (totalExp >= needExp) {
            while (totalExp >= needExp) {
                totalExp -= needExp;
                ++level;
                this.updateCardExercise(cardExercise, type, new Param(level, totalExp));
                this.informFiveExercise(role, cardExercise, type);
                if (level > CardDataPool.cardFiveExerciseMaxGrade) {
                    break;
                }
                final SysDataCardFiveExercise exercise = CardDataPool.cardFiveExerciseMap.get(level);
                if (exercise == null) {
                    this.logger.error("五行修炼升级重新获取升级经验错误，当前等级为：{}", (Object)level);
                    break;
                }
                needExp = this.getNeedExpWxInfo(exercise, type);
            }
        }
        else {
            this.updateCardExercise(cardExercise, type, new Param(level, totalExp));
            this.informFiveExercise(role, cardExercise, type);
        }
        return true;
    }
    
    public int getNeedExpWxInfo(final SysDataCardFiveExercise exercise, final int type) {
        if (type == PRO_DICT_TYPE.WX_J.getDist()) {
            return exercise.getGold();
        }
        if (type == PRO_DICT_TYPE.WX_M.getDist()) {
            return exercise.getWood();
        }
        if (type == PRO_DICT_TYPE.WX_S.getDist()) {
            return exercise.getWater();
        }
        if (type == PRO_DICT_TYPE.WX_H.getDist()) {
            return exercise.getFire();
        }
        if (type == PRO_DICT_TYPE.WX_T.getDist()) {
            return exercise.getSoil();
        }
        return -1;
    }
    
    public void informFiveExercise(final Role role, final CardExercise exercise, final int type) {
        final List<FiedValue> objects = new ArrayList<FiedValue>();
        if (type == PRO_DICT_TYPE.WX_J.getDist()) {
            objects.add(new FiedValue("goldGrade", (byte)2, exercise.getGoldGrade()));
            objects.add(new FiedValue("goldExp", (byte)3, exercise.getGoldExp()));
        }
        else if (type == PRO_DICT_TYPE.WX_M.getDist()) {
            objects.add(new FiedValue("woodGrade", (byte)2, exercise.getWoodGrade()));
            objects.add(new FiedValue("woodExp", (byte)3, exercise.getWoodExp()));
        }
        else if (type == PRO_DICT_TYPE.WX_S.getDist()) {
            objects.add(new FiedValue("waterGrade", (byte)2, exercise.getWaterGrade()));
            objects.add(new FiedValue("waterExp", (byte)3, exercise.getWaterExp()));
        }
        else if (type == PRO_DICT_TYPE.WX_H.getDist()) {
            objects.add(new FiedValue("fireGrade", (byte)2, exercise.getFireGrade()));
            objects.add(new FiedValue("fireExp", (byte)3, exercise.getFireExp()));
        }
        else if (type == PRO_DICT_TYPE.WX_T.getDist()) {
            objects.add(new FiedValue("soilGrade", (byte)2, exercise.getSoilGrade()));
            objects.add(new FiedValue("soilExp", (byte)3, exercise.getSoilExp()));
        }
        objects.add(new FiedValue("useCount", (byte)2, exercise.getUseCount()));
        MessagePusher.pushMessage(role, new RespFiveExercise(objects));
    }
    
    public Param getWxInfo(final CardExercise cardExercise, final int type) {
        if (type == PRO_DICT_TYPE.WX_J.getDist()) {
            return new Param(cardExercise.getGoldGrade(), cardExercise.getGoldExp());
        }
        if (type == PRO_DICT_TYPE.WX_M.getDist()) {
            return new Param(cardExercise.getWoodGrade(), cardExercise.getWoodExp());
        }
        if (type == PRO_DICT_TYPE.WX_S.getDist()) {
            return new Param(cardExercise.getWaterGrade(), cardExercise.getWaterExp());
        }
        if (type == PRO_DICT_TYPE.WX_H.getDist()) {
            return new Param(cardExercise.getFireGrade(), cardExercise.getFireExp());
        }
        if (type == PRO_DICT_TYPE.WX_T.getDist()) {
            return new Param(cardExercise.getSoilGrade(), cardExercise.getSoilExp());
        }
        return null;
    }
    
    public void updateCardExercise(final CardExercise cardExercise, final int type, final Param param) {
        int grade = param.getK();
        int exp = param.getV();
        if (grade > CardDataPool.cardFiveExerciseMaxGrade) {
            grade = CardDataPool.cardFiveExerciseMaxGrade;
            exp = -1;
        }
        if (type == PRO_DICT_TYPE.WX_J.getDist()) {
            cardExercise.setGoldGrade(grade);
            cardExercise.setGoldExp(exp);
        }
        else if (type == PRO_DICT_TYPE.WX_M.getDist()) {
            cardExercise.setWoodGrade(grade);
            cardExercise.setWoodExp(exp);
        }
        else if (type == PRO_DICT_TYPE.WX_S.getDist()) {
            cardExercise.setWaterGrade(grade);
            cardExercise.setWaterExp(exp);
        }
        else if (type == PRO_DICT_TYPE.WX_H.getDist()) {
            cardExercise.setFireGrade(grade);
            cardExercise.setFireExp(exp);
        }
        else if (type == PRO_DICT_TYPE.WX_T.getDist()) {
            cardExercise.setSoilGrade(grade);
            cardExercise.setSoilExp(exp);
        }
        cardExercise.save();
    }
    
    public void checkCardExpire(final Role role, final int currTime) {
        final int cardExpireTime = role.getCardExpireTime();
        if (cardExpireTime - 1 <= currTime) {
            this.cardExpire(role);
        }
    }
    
    public void cardExpire(final Role role) {
        String name = null;
        final SysDataCard card = CardDataPool.cardMap.get(role.getMasquerade());
        if (card != null) {
            name = card.getName();
        }
        this.transfiguration(role, 0, 0, name);
    }
    
    public void reqFiveExercise(final Role role, final boolean flag) {
        if (flag && DataGlobalFunc.needGradeJudge(1, 80, role.getRelive(), role.getLevel(), "") != null) {
            MessagePusher.pushMessage(role, new RespMsg("#<LG1>五行修炼#1转80级开启，请先提升等级"));
            return;
        }
        final CardExercise cardExercise = this.getCardExercise(role);
        MessagePusher.pushMessage(role, new RespFiveExercise(cardExercise));
    }
    
    public void consume(final Role role, final Map<Long, CardChange> carMap, final CardChange card, int consume) {
        final NutMap rests = card.getRests();
        final String key = String.valueOf(2);
        final int num = rests.getInt(key, 0);
        if (num < consume) {
            rests.remove((Object)key);
            consume -= num;
            final List<Integer> list = new ArrayList<Integer>();
            for (final String s : rests.keySet()) {
                list.add(Integer.valueOf(s));
            }
            final List<Integer> collect = list.stream().sorted(Comparator.comparing(Integer::intValue).reversed()).collect(Collectors.toList());
            for (final Integer key_ : collect) {
                final int anInt = rests.getInt(String.valueOf(key_), 0);
                if (anInt > 0) {
                    if (consume > anInt) {
                        rests.remove((Object)String.valueOf(key_));
                        consume -= anInt;
                    }
                    else {
                        final int residue = anInt - consume;
                        if (residue <= 0) {
                            rests.remove((Object)String.valueOf(key_));
                            break;
                        }
                        rests.setv(String.valueOf(key_), (Object)residue);
                        break;
                    }
                }
            }
        }
        else {
            final int residue2 = num - consume;
            if (residue2 <= 0) {
                rests.remove((Object)String.valueOf(key));
            }
        }
        card.setRests(rests);
        if (DataGlobalFunc.getCardChangeNum(card) <= 0) {
            card.setRests(new NutMap());
            card.setDelete(true);
            carMap.remove(card.getId());
        }
        this.refresh(role, card);
        card.save();
    }
    
    public boolean shelvesConsume(final Role role, final long id, int consume) {
        final int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
        final Map<Long, CardChange> cardMap = this.getCard(role);
        final CardChange card = cardMap.get(id);
        final NutMap rests = card.getRests();
        final int num = DataGlobalFunc.unrestricted(rests);
        if (consume > num) {
            return false;
        }
        final List<Integer> list = new ArrayList<Integer>();
        for (final String s : rests.keySet()) {
            final int key = Integer.valueOf(s);
            if (key != 2 && key < time) {
                list.add(key);
            }
        }
        list.add(0);
        for (final Integer key2 : list) {
            final int anInt = rests.getInt(String.valueOf(key2), 0);
            if (anInt > 0) {
                if (consume > anInt) {
                    rests.remove((Object)String.valueOf(key2));
                    consume -= anInt;
                }
                else {
                    final int residue = anInt - consume;
                    if (residue <= 0) {
                        rests.remove((Object)String.valueOf(key2));
                        break;
                    }
                    rests.setv(String.valueOf(key2), (Object)residue);
                    break;
                }
            }
        }
        card.setRests(rests);
        if (DataGlobalFunc.getCardChangeNum(card) <= 0) {
            card.setRests(new NutMap());
            card.setDelete(true);
            cardMap.remove(card.getId());
        }
        card.save();
        this.refresh(role, card);
        return true;
    }
    
    @Override
    public void handleEcho(final HeartBeatEvent heartBeatEvent) {
        final Role role = heartBeatEvent.getRole();
        if (role == null) {
            return;
        }
        final int currTime = new Long(heartBeatEvent.getCurveHeartTime() / 1000L).intValue();
        if (role.getMasquerade() > 0) {
            this.checkCardExpire(role, currTime);
        }
    }
}
