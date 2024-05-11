
package com.kitty.system.game.market.service;

import com.kitty.common.start.ServerService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.market.message.RespMarketGoodCard;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.market.message.RespMarketPetCard;
import com.kitty.system.game.market.message.ReqGoodInfo;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.game.market.model.MarketDealRecord;
import com.kitty.mina.cache.Deploy;
import com.kitty.system.game.market.message.ReqBuyFromStall;
import com.kitty.system.game.market.message.RespColumbia;
import com.kitty.system.game.market.message.vo.Columbia;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.market.model.StallItem;
import com.kitty.common.utils.PageUtil;
import com.kitty.system.game.market.message.RespStallList;
import java.util.Collection;
import com.kitty.system.init.domain.SysDataStallMenu;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import com.kitty.common.db.BaseEntity;
import com.kitty.common.db.Db4CommonService;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.system.game.enter.message.RespDestroySucceed;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.card.domain.CardChange;
import com.kitty.listener.BaseGameEvent;
import com.kitty.listener.event.ranking.PetChangeEvent;
import com.kitty.listener.EventType;
import com.kitty.listener.EventDispatcher;
import com.kitty.system.game.pet.domain.Pet;
import java.util.ConcurrentModificationException;
import com.kitty.logs.LoggerUtils;
import com.kitty.system.game.bag.domain.BagGoods;
import com.kitty.record.RecordThread;
import com.kitty.common.utils.JsonUtils;
import com.kitty.system.game.vo.UnifiedValue;
import com.kitty.system.game.team.message.RespConfirm;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.mina.cache.SessionUtils;
import com.kitty.system.game.market.message.ReqSetStallGoods;
import org.apache.mina.core.session.IoSession;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.common.message.RespHint;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.market.model.vo.CheckStall;
import com.kitty.system.game.market.model.Cargo;
import com.kitty.system.game.market.message.RespStallMine;
import java.util.Map;
import com.kitty.system.game.market.message.RespMarketSearchItem;
import java.util.Comparator;
import java.util.function.Function;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import com.kitty.system.init.domain.SysDataStall;
import com.kitty.system.game.market.pool.MarketDataPool;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import java.util.ArrayList;
import org.nutz.dao.Condition;
import org.nutz.dao.Cnd;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.market.model.Market;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import com.kitty.system.game.pet.service.PetService;
import com.kitty.system.game.card.service.CardService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.common.thread.IdWorker;
import org.springframework.beans.factory.annotation.Autowired;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

@Service
public class MarketService
{
    private Logger logger;
    @Autowired
    Dao dao;
    @Autowired
    IdWorker worker;
    @Autowired
    RoleService roleService;
    @Autowired
    BagService bagService;
    @Autowired
    CardService cardService;
    @Autowired
    PetService petService;
    private ConcurrentMap<Integer, List<Market>> marketMap;
    
    public MarketService() {
        this.logger = LoggerFunction.MARKET.getLogger();
        this.marketMap = new ConcurrentHashMap<Integer, List<Market>>();
    }
    
    public void init() {
        final List<Market> list = this.dao.query(Market.class, Cnd.NEW());
        for (final Market market : list) {
            List<Market> markets = this.marketMap.get(market.getStallMenuSerial());
            if (markets == null) {
                markets = new ArrayList<Market>();
            }
            markets.add(market);
            this.marketMap.put(market.getStallMenuSerial(), markets);
        }
    }
    
    private boolean commonVerify(final Role role) {
        if (role.getExtendBox().getBanTrade() == 1) {
            MessagePusher.pushMessage(role, new RespMsg("您没有摆摊权限"));
            return false;
        }
        return true;
    }
    
    public void reqPrice(final Role role, final int serial) {
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final SysDataStall stall = data_stall.get(serial);
        if (stall == null) {
            MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
            return;
        }
        String minimum;
        final String price = minimum = stall.getBasicsPrice().toString();
        final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
        final List<Market> markets = this.marketMap.get(stall.getStallMenuSerial());
        if (markets != null) {
            final List<Market> collect = markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getSerial() == serial && obj.getStallMenuSerial() == stall.getStallMenuSerial() && exact > obj.getStartTime() + obj.getPublicityTime() && obj.getSaleAmount() == 0 && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
            if (collect.size() > 0) {
                final Long min = collect.stream().map(Market::getPrice).collect(Collectors.minBy(Long::compare)).get();
                minimum = String.valueOf(min);
            }
        }
        role.pushTempCache("price", price);
        MessagePusher.pushMessage(role, new RespMarketSearchItem(serial, price, minimum));
    }
    
    public void sendMarketList(final Role role) {
        try {
            final RespStallMine respStallMine = new RespStallMine();
            respStallMine.setStallTotalNum((byte)role.getExtendBox().getStall());
            final List<Cargo> goodsList = new ArrayList<Cargo>();
            respStallMine.setList(goodsList);
            final List<Market> markets = this.queryMyMarket(role);
            final Map<Integer, SysDataStall> stall = MarketDataPool.t_data_stall;
            final int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
            for (final Market market : markets) {
                final SysDataStall sysDataStall = stall.get(market.getSerial());
                final Cargo cargo = new Cargo();
                if (market.getAuditTime() > 0) {
                    if (sysDataStall == null) {
                        continue;
                    }
                    cargo.setShowEndTime(market.getAuditTime());
                    cargo.setEndTime(market.getAuditTime() + sysDataStall.getAuditTime());
                    if (time > cargo.getEndTime()) {
                        cargo.setStatus((byte)5);
                    }
                    else {
                        cargo.setStatus((byte)4);
                    }
                }
                else {
                    final int publicityTime = market.getStartTime() + market.getPublicityTime();
                    if (time < publicityTime) {
                        cargo.setShowEndTime(market.getStartTime());
                        cargo.setEndTime(publicityTime);
                        cargo.setStatus((byte)2);
                    }
                    else {
                        cargo.setShowEndTime(publicityTime);
                        cargo.setEndTime(publicityTime + market.getExpireTime());
                        if (time > cargo.getEndTime()) {
                            cargo.setStatus((byte)3);
                        }
                        else {
                            cargo.setStatus((byte)1);
                        }
                    }
                }
                cargo.setId(String.valueOf(market.getId()));
                cargo.setSerial(market.getSerial());
                cargo.setPrice(String.valueOf(market.getPrice()));
                cargo.setAmount(market.getAmount());
                cargo.setSaleAmount(market.getSaleAmount());
                if (market.getStatus() == 1) {
                    cargo.setStatus((byte)6);
                }
                final String extra = this.specialExtra(market.getPara(), sysDataStall.getIsItem());
                if (extra != null) {
                    cargo.setExtra(extra);
                }
                goodsList.add(cargo);
            }
            MessagePusher.pushMessage(role, respStallMine);
        }
        catch (final Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkStall(final Role role, final Map<Integer, SysDataStall> data_stall, final CheckStall checkStall, int serial, final long price) {
        try {
            if (checkStall != null) {
                if (checkStall.getPet() != null) {
                    serial = checkStall.getPet().getPetType();
                }
                else if (checkStall.getEquip() != null) {
                    serial = checkStall.getEquip().getType();
                }
            }
            final SysDataStall stall = data_stall.get(serial);
            if (stall == null) {
                this.logger.error("检查物品是否可上架,物品不可上架编号为：{}", (Object)serial);
                MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
                return false;
            }
            if (stall.getIsItem() == 1 || stall.getIsItem() == 3) {
                if (stall.getIsCheck() > 0) {
                    final Object obj = role.getTempCache("price", null);
                    if (obj == null) {
                        return false;
                    }
                    final Long cache = Long.valueOf(String.valueOf(obj));
                    final List<Long> list = new ArrayList<Long>();
                    list.add(cache);
                    double nominate = 0.05;
                    for (int i = 0; i < 10; ++i) {
                        list.add(BigDecimalUtil.decimalScaleBig(0, cache + cache * nominate).longValue());
                        list.add(BigDecimalUtil.decimalScaleBig(0, cache + cache * -nominate).longValue());
                        nominate += 0.05;
                    }
                    if (!list.contains(price)) {
                        return false;
                    }
                }
            }
            else if (stall.getIsItem() == 0 && checkStall.getEquip() != null) {
                final Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(checkStall.getEquip().getType());
                if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue()) && checkStall.getEquip().getProductOrder() < ACC_GRADE.PRICELESS.getValue()) {
                    return false;
                }
                if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue())) {
                    final SysSeniorBieEq bieEq = EquipDataPool.seniorBieEq.get(checkStall.getEquip().getType());
                    final boolean flag = DataGlobalFunc.seniorIsGem(bieEq, checkStall.getEquip());
                    if (!flag) {
                        MessagePusher.pushMessage(role, new RespMsg("此装备属性不佳，不可上架交易~"));
                        return false;
                    }
                }
                if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_XIANQI.getValue()) || type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue())) {
                    final int level = role.getRelive() * 1000 + role.getLevel();
                    if (level < 1100) {
                        MessagePusher.pushMessage(role, new RespMsg("等级达到1转100级才可以交易此装备~"));
                        return false;
                    }
                    if (type.equals(ITEMLARGETYPE.ITEM_LARGE_TYPE_SHENBING.getValue()) && price > 500000L) {
                        MessagePusher.pushMessage(role, new RespMsg("价格过高~"));
                        return false;
                    }
                }
                final int num = DataGlobalFunc.getGemNum(checkStall.getEquip());
                if (num > 0) {
                    MessagePusher.pushMessage(role, new RespMsg("请先取下宝石再进行寄售"));
                    return false;
                }
                if (checkStall.getEquip().getBd() == 2) {
                    MessagePusher.pushMessage(role, new RespMsg("当前物品不可交易"));
                    return false;
                }
                if (checkStall.getEquip().getBd() > 0 && checkStall.getEquip().getBd() > TimeUtil.dayInMillis(TimeUtil.when())) {
                    MessagePusher.pushMessage(role, new RespHint(14, (short)0));
                    final String date = TimeUtil.stampToDate(checkStall.getEquip().getBd() * 1000L);
                    final String[] split = date.split("-");
                    MessagePusher.pushMessage(role, new RespMsg(String.format("#<R>%s年%s月%s日05点前不可交易#", split[0], split[1], split[2])));
                    return false;
                }
            }
            else if (stall.getIsItem() == 2 && checkStall.getPet() != null) {
                final Integer petSerial = checkStall.getPet().getPetType();
                if (petSerial != null && petSerial > 0) {
                    final SysPet sysPet = PetDataPool.petMap.get(petSerial);
                    if (sysPet == null) {
                        return false;
                    }
                    if (sysPet.getLevelType() < 4) {
                        MessagePusher.pushMessage(role, new RespMsg("只有神兽才可以交易~"));
                        return false;
                    }
                    final int level2 = role.getRelive() * 1000 + role.getLevel();
                    if (level2 < 1100) {
                        MessagePusher.pushMessage(role, new RespMsg("等级达到1转100级才可以交易此宠物~"));
                        return false;
                    }
                }
                if (role.getExtendBox().getCombatPet().equals(checkStall.getPet().getPetId())) {
                    MessagePusher.pushMessage(role, new RespMsg("参战中的宠物不可以上架"));
                    return false;
                }
                if (checkStall.getPet().getBindTime() == 2) {
                    MessagePusher.pushMessage(role, new RespMsg("当前物品不可交易"));
                    return false;
                }
                if (checkStall.getPet().getBindTime() > 0 && checkStall.getPet().getBindTime() > TimeUtil.dayInMillis(TimeUtil.when())) {
                    MessagePusher.pushMessage(role, new RespHint(14, (short)0));
                    final String date2 = TimeUtil.stampToDate(checkStall.getPet().getBindTime() * 1000L);
                    final String[] split2 = date2.split("-");
                    MessagePusher.pushMessage(role, new RespMsg(String.format("#<R>%s年%s月%s日05点前不可交易#", split2[0], split2[1], split2[2])));
                    return false;
                }
                if (checkStall.getPet().getPetBase().getGrade_base() < 3) {
                    return false;
                }
                final List<Integer> unset = new ArrayList<Integer>();
                final List<Integer> skills = new ArrayList<Integer>();
                for (int j = 0; j < checkStall.getPet().getSkills().size(); ++j) {
                    final PetSkill petSkill = checkStall.getPet().getSkills().get(j);
                    if (petSkill.getSerial() >= 0) {
                        unset.add(j);
                        if (petSkill.getType() != PETSKILLSUB.SUBTYPE_SPECIAL.getValue() && petSkill.getType() != PETSKILLSUB.SUBTYPE_BORN.getValue() && petSkill.getType() != PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                            final int current = SkillFunc.getPetSkillSubType(petSkill.getSerial());
                            if (current == PETSKILLSUB.SUBTYPE_SENIOR.getValue() || current == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                                skills.add(j);
                            }
                        }
                    }
                }
                if (unset.size() < 5 || skills.size() < 3) {
                    return false;
                }
                if (checkStall.getPet().getRebirth() == 0 && checkStall.getPet().getBone() < 1) {
                    return false;
                }
                if (checkStall.getPet().getRebirth() == 1 && checkStall.getPet().getBone() < 2) {
                    return false;
                }
                if (checkStall.getPet().getRebirth() >= 2 && checkStall.getPet().getBone() < 4) {
                    return false;
                }
                final List<PetEquip> petWearEquip = this.bagService.getPetWearEquip(role.getId(), checkStall.getPet().getId());
                if (petWearEquip != null && petWearEquip.size() > 0) {
                    MessagePusher.pushMessage(role.getId(), new RespMsg(" 请卸下召唤兽佩饰后再试"));
                    return false;
                }
            }
            else if (stall.getIsItem() == 4 && checkStall.getRune() != null) {
                if (role.getRelive() * 1000 + role.getLevel() < 1100) {
                    MessagePusher.pushMessage(role, new RespMsg("等级达到1转100级才可以交易天策符~"));
                    return false;
                }
                if (price > 1000000000L) {
                    MessagePusher.pushMessage(role, new RespMsg("价格过高~"));
                    return false;
                }
                final Object obj = role.getTempCache("price", null);
                if (obj == null) {
                    return false;
                }
                final RoleTianFu rune = checkStall.getRune();
                if (rune == null) {
                    return false;
                }
                final SysTianFuSkill runeInfo = SkillDataPool.tianFuSkillMap.get(rune.getTianFuSerial());
                if (runeInfo == null || runeInfo.getRarityJudge().size() < 4) {
                    MessagePusher.pushMessage(role, new RespMsg("此装备属性不佳，不可上架交易~"));
                    return false;
                }
                if (rune.getLevel() < runeInfo.getRarityJudge().get(3)) {
                    MessagePusher.pushMessage(role, new RespMsg("此装备属性不佳，不可上架交易~"));
                    return false;
                }
            }
            return true;
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("检查物品是否可上架错误，玩家编号 = {}，上架物品编号为：{}", (Object)role.getRoleId(), (Object)serial);
            return false;
        }
    }
    
    public void upShell(final IoSession session, final ReqSetStallGoods req) {
        if (req.getAmount() > 32000 || req.getAmount() <= 0) {
            return;
        }
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final boolean limit = this.marketLimit(role);
        if (!limit) {
            return;
        }
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final List<Market> markets = this.queryMyMarket(role);
        if (markets.size() >= role.getExtendBox().getStall()) {
            MessagePusher.pushMessage(role, new RespMsg("目前摊位已满，请下架部分物品后再次寄售"));
            return;
        }
        if (!this.commonVerify(role)) {
            return;
        }
        final Long price = Long.valueOf(req.getPrice());
        if (price <= 0L) {
            MessagePusher.pushMessage(role, new RespMsg("请输入价格后再上架~"));
            return;
        }
        if (req.getType() == 0) {
            this.equipShelves(role, req, price);
        }
        else if (req.getType() == 1) {
            this.goodsShelves(role, req, price);
        }
        else if (req.getType() == 2) {
            this.petShelves(role, req, price);
        }
        else if (req.getType() == 3) {
            this.cardShelves(role, req, price);
        }
        else if (req.getType() == 4) {
            this.runeShelves(role, req, price);
        }
    }
    
    public void equipShelves(final Role role, final ReqSetStallGoods req, final Long price) {
        final Long inventory = Long.valueOf(req.getInventory());
        final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        final BagEquip equip = bagEquipMap.values().stream().filter(e -> e.getId().equals(inventory)).findFirst().orElse(null);
        if (equip == null) {
            MessagePusher.pushMessage(role, new RespMsg("背包中没有该装备"));
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final CheckStall checkStall = new CheckStall(equip);
        if (this.checkStall(role, data_stall, checkStall, equip.getType(), price)) {
            final boolean b = this.putAway(role, data_stall, equip.getType(), req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                final SysDataStall stall = data_stall.get(equip.getType());
                final RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(0, stall.getRevenueRatio() * 100.0))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("摆摊装备：请求参数 = %s", req.toString()), role.getMarking());
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
    }
    
    public void goodsShelves(final Role role, final ReqSetStallGoods req, final Long price) {
        final int inventory = Integer.valueOf(req.getInventory());
        int serial = -1;
        int num = -1;
        BagEquip equip = null;
        BagGoods goods = null;
        try {
            final Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
            goods = bagGoodsMap.values().stream().filter(e -> e.getPos() == inventory).findFirst().orElse(null);
        }
        catch (final ConcurrentModificationException e) {
            LoggerUtils.error("物品摆摊处理下标异常", e.getMessage());
        }
        if (goods != null) {
            serial = goods.getSerial();
            num = DataGlobalFunc.unrestricted(goods.getRests());
        }
        else {
            final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
            equip = bagEquipMap.values().stream().filter(e -> e.getPos() == inventory).findFirst().orElse(null);
            if (equip != null) {
                num = 1;
                serial = equip.getType();
            }
        }
        if (serial <= 0 || num <= 0) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        if (req.getAmount() > num) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("可上架道具数量：" + num));
            return;
        }
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final CheckStall checkStall = new CheckStall(equip);
        if (this.checkStall(role, data_stall, checkStall, serial, price)) {
            final boolean b = this.putAway(role, data_stall, serial, req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                final SysDataStall stall = data_stall.get(serial);
                final RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(0, stall.getRevenueRatio() * 100.0))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("摆摊物品：请求参数 = %s", req.toString()), role.getMarking());
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
    }
    
    public void petShelves(final Role role, final ReqSetStallGoods req, final Long price) {
        final int inventory = Integer.valueOf(req.getInventory());
        final Map<Integer, Pet> map = this.petService.selectPet(role);
        final Pet pet = map.get(inventory);
        if (pet == null) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final CheckStall checkStall = new CheckStall(pet);
        if (this.checkStall(role, data_stall, checkStall, pet.getPetType(), price)) {
            final boolean b = this.putAway(role, data_stall, pet.getPetType(), req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                final SysDataStall stall = data_stall.get(pet.getPetType());
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                final RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(0, stall.getRevenueRatio() * 100.0))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("摆摊宠物：请求参数 = %s", req.toString()), role.getMarking());
                EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, new ArrayList<PetEquip>()));
                EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, new ArrayList<PetEquip>()));
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
    }
    
    public void cardShelves(final Role role, final ReqSetStallGoods req, final Long price) {
        final long inventory = Long.valueOf(req.getInventory());
        int num = -1;
        final Map<Long, CardChange> cardMap = this.cardService.getCard(role);
        final CardChange change = cardMap.get(inventory);
        if (change != null) {
            num = DataGlobalFunc.unrestricted(change.getRests());
        }
        if (req.getAmount() > num) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("可上架道具数量：" + num));
            return;
        }
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        if (this.checkStall(role, data_stall, null, change.getSerial(), price)) {
            final boolean b = this.putAway(role, data_stall, change.getSerial(), req.getPrice(), req.getAmount(), req.getInventory(), null);
            if (b) {
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                final SysDataStall stall = data_stall.get(change.getSerial());
                final RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(0, stall.getRevenueRatio() * 100.0))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("摆摊变身卡：请求参数 = %s", req.toString()), role.getMarking());
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
    }
    
    private void runeShelves(final Role role, final ReqSetStallGoods req, final Long price) {
        final long inventory = Long.valueOf(req.getInventory());
        final List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        final RoleTianFu roleTianFu = roleTianFus.stream().filter(e -> e.getId().equals(inventory)).findFirst().orElse(null);
        if (roleTianFu == null) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final CheckStall checkStall = new CheckStall(roleTianFu);
        if (this.checkStall(role, data_stall, checkStall, roleTianFu.getTianFuSerial(), price)) {
            final boolean b = this.putAway(role, data_stall, roleTianFu.getTianFuSerial(), req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                final SysDataStall stall = data_stall.get(roleTianFu.getTianFuSerial());
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                final RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(0, stall.getRevenueRatio() * 100.0))));
                MessagePusher.pushMessage(role, confirm);
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
    }
    
    public boolean putAway(final Role role, final Map<Integer, SysDataStall> data_stall, final int serial, final String price, final short amount, final String inventory, final CheckStall checkStall) {
        if (amount > 32000 || amount <= 0) {
            return false;
        }
        final Long value = Long.valueOf(price);
        if (value <= 0L) {
            return false;
        }
        long serviceCharge = BigDecimalUtil.decimalScaleDown(0, Long.valueOf(price) * 0.01).longValue();
        if (serviceCharge < 1000L) {
            serviceCharge = 1000L;
        }
        else if (serviceCharge > 100000L) {
            serviceCharge = 100000L;
        }
        final boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, serviceCharge, "上架道具");
        if (!b && !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, serviceCharge, "上架道具")) {
            MessagePusher.pushMessage(role, new RespMsg("您当前无力支付手续费"));
            return false;
        }
        final SysDataStall stall = data_stall.get(serial);
        if (stall == null) {
            this.logger.error("上架道具,物品不可上架编号为：{}", (Object)serial);
            MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
            return false;
        }
        final Market market = new Market();
        market.setId(this.worker.nextId());
        market.setSerial(serial);
        market.setRoleId(role.getRoleId());
        market.setStallMenuSerial(stall.getStallMenuSerial());
        market.setCategory(stall.getCategory());
        market.setPrice(Long.valueOf(price));
        market.setMarketPrice(stall.getBasicsPrice());
        market.setStartTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
        if (stall.getPublicityPeriod() > 0) {
            market.setIsPublicity(1);
            market.setPublicityTime(stall.getPublicityPeriod());
        }
        market.setExpireTime(stall.getExpireTime());
        market.setAmount(amount);
        if (stall.getIsItem() == 0 && checkStall.getEquip() != null) {
            final String string = JsonUtils.object2String(checkStall.getEquip());
            if (string == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return false;
            }
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("摆摊装备：%s", checkStall.getEquip().toString()), role.getMarking());
            this.bagService.deleteRoleEquip(role, checkStall.getEquip(), "集市摆摊");
            market.setPara(string);
            return this.market(role, stall, market, serviceCharge);
        }
        else if (stall.getIsItem() == 2 && checkStall.getPet() != null) {
            final Pet pet = checkStall.getPet();
            final String string2 = JsonUtils.object2String(pet);
            if (string2 == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return false;
            }
            RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)2, (byte)2, String.format("摆摊宠物：%s", pet.toString()), role.getMarking());
            market.setPara(string2);
            final Map<Integer, Pet> map = this.petService.selectPet(role);
            map.remove(pet.getPetId());
            MessagePusher.pushMessage(role, new RespDestroySucceed(Const.BUYBACK_TYPE_PET, String.valueOf(pet.getPetId())));
            pet.setDelete(true);
            RoleUtils.toUpdateAssistList(role, pet, false);
            pet.save();
            return this.market(role, stall, market, serviceCharge);
        }
        else if (stall.getIsItem() == 4 && checkStall.getRune() != null) {
            final RoleTianFu rune = checkStall.getRune();
            final String string2 = JsonUtils.object2String(rune);
            if (string2 == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return false;
            }
            final RoleTianFuService service = SpringUtils.getBean(RoleTianFuService.class);
            final boolean delRune = service.delRune(role, rune.getId());
            if (!delRune) {
                return false;
            }
            service.roleTianFuList(role);
            market.setPara(string2);
            return this.market(role, stall, market, serviceCharge);
        }
        else {
            if (stall.getIsItem() == 3) {
                final long id = Long.valueOf(inventory);
                final boolean consume = this.cardService.shelvesConsume(role, id, amount);
                return consume && this.market(role, stall, market, serviceCharge);
            }
            if (stall.getIsItem() == 1) {
                final int pos = Integer.valueOf(inventory);
                final boolean consume2 = this.bagService.shelvesConsume(role, pos, amount);
                return consume2 && this.market(role, stall, market, serviceCharge);
            }
            return false;
        }
    }
    
    public boolean market(final Role role, final SysDataStall stall, final Market market, final long serviceCharge) {
        List<Market> markets = this.marketMap.get(stall.getStallMenuSerial());
        if (markets == null) {
            markets = new ArrayList<Market>();
        }
        markets.add(market);
        this.marketMap.put(stall.getStallMenuSerial(), markets);
        MessagePusher.pushMessage(role, new RespMsg(String.format("摆摊成功，扣除#<IR%d>#%s，物品将在30分钟内上架", Const.RESTYPE_COIN, MoneyUtil.colours(serviceCharge))));
        Db4CommonService.getInstance().add2Queue(market);
        return true;
    }
    
    public void marketQuery(final ReqGeneralNotify req, final Role role) {
        final String[] split = req.getString().split("-");
        if (split.length < 6) {
            return;
        }
        final int menus = Integer.valueOf(split[0]);
        final int stallSerial = Integer.valueOf(split[1]);
        final int category = Integer.valueOf(split[2]);
        final int sort = Integer.valueOf(split[3]);
        final int page = Integer.valueOf(split[4]);
        final int type = Integer.valueOf(split[5]);
        final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
        final Map<Integer, SysDataStallMenu> t_data_stall_menu = MarketDataPool.t_data_stall_menu;
        final List<Integer> objects = new ArrayList<Integer>();
        final List<Market> markets = new ArrayList<Market>();
        final SysDataStallMenu menu = t_data_stall_menu.get(stallSerial);
        if (menu != null) {
            if (menu.getAncestors() < 0) {
                for (final SysDataStallMenu stallMenu : t_data_stall_menu.values()) {
                    if (stallMenu.getAncestors() > 0 && stallMenu.getAncestors().equals(stallSerial)) {
                        final List<Market> list = this.marketMap.get(stallMenu.getSerial());
                        if (list == null) {
                            continue;
                        }
                        markets.addAll(list);
                        objects.add(stallMenu.getSerial());
                    }
                }
            }
        }
        final List<Market> marketList = this.marketMap.get(stallSerial);
        if (marketList != null) {
            markets.addAll(marketList);
            objects.add(stallSerial);
        }
        List<Market> list2 = new ArrayList<Market>();
        if (markets.size() > 0) {
            final List<Market> collect = new ArrayList<Market>();
            if (type == 1) {
                if (category == 0) {
                    for (final Integer object : objects) {
                        final List<Market> markets_ = markets.stream().filter(obj -> object.equals(obj.getStallMenuSerial()) && obj.getPublicityTime() > 0 && obj.getStartTime() + obj.getPublicityTime() > exact && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                        if (markets_.size() > 0) {
                            collect.addAll(markets_);
                        }
                    }
                }
                else {
                    for (final Integer object : objects) {
                        final List<Market> markets_ = markets.stream().filter(obj -> object.equals(obj.getStallMenuSerial()) && obj.getCategory() == category && obj.getPublicityTime() > 0 && obj.getStartTime() + obj.getPublicityTime() > exact && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                        if (markets_.size() > 0) {
                            collect.addAll(markets_);
                        }
                    }
                }
            }
            else if (category == 0) {
                for (final Integer object : objects) {
                    final List<Market> markets_ = markets.stream().filter(obj -> obj.getAmount() > 0 && object.equals(obj.getStallMenuSerial()) && exact > obj.getStartTime() + obj.getPublicityTime() && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                    if (markets_.size() > 0) {
                        collect.addAll(markets_);
                    }
                }
            }
            else {
                for (final Integer object : objects) {
                    final List<Market> markets_ = markets.stream().filter(obj -> obj.getAmount() > 0 && object.equals(obj.getStallMenuSerial()) && obj.getCategory() == category && exact > obj.getStartTime() + obj.getPublicityTime() && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                    if (markets_.size() > 0) {
                        collect.addAll(markets_);
                    }
                }
            }
            final int min = Math.min(Math.max(2, BigDecimalUtil.decimalScaleRoundUpInt(0, collect.size() / 8.0)) / 2, 5);
            if (page > min) {
                MessagePusher.pushMessage(role, new RespMsg("摊位有限，其他商品在排队。"));
                return;
            }
            list2 = collect.stream().sorted(Comparator.comparing(Market::getStartTime)).collect(Collectors.toList());
            if (sort == 0) {
                list2 = collect.stream().sorted(Comparator.comparing(Market::getPrice)).collect(Collectors.toList());
            }
            else {
                list2 = collect.stream().sorted(Comparator.comparing(Market::getPrice, Comparator.reverseOrder())).collect(Collectors.toList());
            }
        }
        final RespStallList respStall = this.loadMarketQueryData(role, list2, page, exact, req.getString());
        MessagePusher.pushMessage(role, respStall);
    }
    
    public RespStallList loadMarketQueryData(final Role role, final List<Market> list, final int page, final int exact, final String string) {
        final Map<Integer, SysDataStall> stall = MarketDataPool.t_data_stall;
        final List<Market> subList = PageUtil.subList(list, page, 8);
        final ArrayList<StallItem> objects = new ArrayList<StallItem>();
        for (final Market market : subList) {
            final StallItem item = new StallItem();
            item.setId(String.valueOf(market.getId()));
            item.setSerial(market.getSerial());
            item.setPrice(String.valueOf(market.getPrice()));
            item.setAmount(market.getAmount());
            item.setNum(market.getFocus().size());
            if (market.getFocus().contains(role.getRoleId())) {
                item.setFocus(1);
            }
            final int publicityTime = market.getStartTime() + market.getPublicityTime();
            if (exact < publicityTime) {
                item.setStartTime(market.getStartTime());
                item.setEndTime(publicityTime);
                item.setStatus((byte)2);
            }
            else {
                item.setStartTime(publicityTime);
                item.setEndTime(publicityTime + market.getExpireTime());
                if (exact > item.getEndTime()) {
                    item.setStatus((byte)3);
                }
                else {
                    item.setStatus((byte)1);
                }
            }
            final SysDataStall sysDataStall = stall.get(market.getSerial());
            final String extra = this.specialExtra(market.getPara(), sysDataStall.getIsItem());
            if (extra != null) {
                item.setExtra(JsonUtils.object2String(extra));
            }
            objects.add(item);
        }
        final RespStallList respStall = new RespStallList();
        respStall.setTotalPage((short)BigDecimalUtil.decimalScaleRoundUpInt(0, list.size() / 8.0));
        respStall.setCurrPage((short)page);
        respStall.setList(objects);
        respStall.setClassify(string);
        return respStall;
    }
    
    public String specialExtra(final String string, final int type) {
        if (type == 4) {
            final NutMap nutMap = new NutMap();
            final RoleTianFu runt = JsonUtils.string2Object(string, RoleTianFu.class);
            nutMap.setv("runt", (Object)runt.getLevel());
            return JsonUtils.object2String(nutMap);
        }
        return null;
    }
    
    public void removeGoods(final Role role, final ReqGeneralNotify req, final boolean is) {
        if (is) {
            final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
            if (!clicker) {
                return;
            }
        }
        try {
            final long id = Long.valueOf(req.getStatus());
            final int serial = Integer.valueOf(req.getString());
            final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
            final SysDataStall stall = data_stall.get(serial);
            if (stall == null) {
                MessagePusher.pushMessage(role, new RespMsg("未找到该物品信息"));
                return;
            }
            int pos = 0;
            if (stall.getIsItem() == 0 || stall.getIsItem() == 1) {
                pos = this.bagService.getPos(role, false);
                if (pos <= 0) {
                    MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                    return;
                }
            }
            else if (stall.getIsItem() == 2) {
                final SysPet sysPet = PetDataPool.petMap.get(stall.getSerial());
                if (sysPet == null) {
                    return;
                }
                final boolean flag = SpringUtils.getPetService().canGetPet(role, sysPet, false);
                if (!flag) {
                    return;
                }
            }
            else if (stall.getIsItem() == 4) {
                final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (roleTianFus.size() >= 75) {
                    MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                    return;
                }
            }
            final List<Market> markets = this.marketMap.get(stall.getStallMenuSerial());
            if (markets != null) {
                final Market market = markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getSerial() == serial && obj.getId().equals(id) && obj.getRoleId().equals(role.getRoleId())).findFirst().orElse(null);
                if (market != null) {
                    if (is && market.getStatus() == 1) {
                        MessagePusher.pushMessage(role, new RespMsg("物品已被冻结，暂无法取回"));
                        return;
                    }
                    final int num = market.getAmount() - market.getSaleAmount();
                    if (num <= 0) {
                        this.logger.error("玩家：{}，撤摊取回剩余数量：{}", (Object)role.getName(), (Object)num);
                        return;
                    }
                    markets.remove(market);
                    this.marketMap.put(stall.getStallMenuSerial(), markets);
                    market.setDelete(true);
                    Db4CommonService.getInstance().add2Queue(market);
                    if (stall.getIsItem() == 0 && market.getPara() != null) {
                        final BagEquip equip = JsonUtils.string2Object(market.getPara(), BagEquip.class);
                        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("撤摊摊装备：%s", market.getPara()), role.getMarking());
                        equip.setPos(pos);
                        equip.setDelete(false);
                        this.bagService.addEquip(role, equip, false, "撤摊取回");
                    }
                    else if (stall.getIsItem() == 2 && market.getPara() != null) {
                        final Pet pet = JsonUtils.string2Object(market.getPara(), Pet.class);
                        if (pet == null) {
                            MessagePusher.pushMessage(role, new RespMsg("宠物异常，暂时无法处理"));
                            return;
                        }
                        pet.doAfterInit();
                        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)0, (byte)2, String.format("撤摊摊装备：%s", market.getPara()), role.getMarking());
                        pet.setDelete(false);
                        final Map<Integer, Pet> map = this.petService.selectPet(role);
                        map.put(pet.getPetId(), pet);
                        SpringUtils.getPetService().loadPet(role, pet);
                        RoleUtils.toUpdateAssistList(role, pet, true);
                        pet.save();
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, new ArrayList<PetEquip>()));
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, new ArrayList<PetEquip>()));
                    }
                    else if (stall.getIsItem() == 3) {
                        this.cardService.addCard(role, market.getSerial(), num, 0, true, false);
                    }
                    else if (stall.getIsItem() == 4) {
                        if (market.getPara() == null) {
                            return;
                        }
                        final RoleTianFu rune = JsonUtils.string2Object(market.getPara(), RoleTianFu.class);
                        rune.setDelete(false);
                        DataCache.ROLE_TIAN_FU.get(role.getId()).add(rune);
                        rune.save();
                        SpringUtils.getBean(RoleTianFuService.class).roleTianFuList(role);
                    }
                    else {
                        if (num > market.getAmount()) {
                            this.logger.error("撤摊取回物品计算错误，摊位ID：{}, 出售物品编号 = {}，出售总数量 = {}，已经出售数量 = {}", new Object[] { market.getId(), market.getSerial(), market.getAmount(), market.getSaleAmount() });
                            return;
                        }
                        this.bagService.addGoods(role, market.getSerial(), num, 0, false, false, "撤摊取回");
                    }
                    MessagePusher.pushMessage(role, new RespMsg("取回成功"));
                    this.sendMarketList(role);
                }
            }
        }
        catch (final Exception e) {
            this.logger.error(e.getMessage());
        }
    }
    
    public void restartStall(final Role role, final ReqGeneralNotify req) {
        if (!this.commonVerify(role)) {
            return;
        }
        final long id = Long.valueOf(req.getStatus());
        final String[] split = req.getString().split("-");
        if (split.length < 2) {
            return;
        }
        final long price = Long.valueOf(split[0]);
        final short amount = Short.valueOf(split[1]);
        if (amount > 32000 || amount <= 0 || price <= 0L) {
            return;
        }
        final List<Market> list = this.queryMyMarket(role);
        final Market market = list.stream().filter(obj -> obj.getId().equals(id) && obj.getRoleId().equals(role.getRoleId())).findFirst().orElse(null);
        if (market == null) {
            return;
        }
        if (market.getStatus() == 1) {
            MessagePusher.pushMessage(role, new RespMsg("物品已被冻结，暂无法操作"));
            return;
        }
        final int num = market.getAmount() - market.getSaleAmount();
        if (amount > num) {
            return;
        }
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final SysDataStall stall = data_stall.get(market.getSerial());
        if (stall == null) {
            this.logger.error("重新上架,物品不可上架编号为：{}", (Object)market.getSerial());
            MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
            return;
        }
        final CheckStall checkStall = new CheckStall();
        if (stall.getIsItem() == 2 && market.getPara() != null) {
            final Pet pet = JsonUtils.string2Object(market.getPara(), Pet.class);
            if (pet == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return;
            }
            checkStall.setPet(pet);
        }
        else if (stall.getIsItem() == 0 && market.getPara() != null) {
            final BagEquip equip = JsonUtils.string2Object(market.getPara(), BagEquip.class);
            if (equip == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return;
            }
            checkStall.setEquip(equip);
        }
        else if (stall.getIsItem() == 4 && market.getPara() != null) {
            final RoleTianFu rune = JsonUtils.string2Object(market.getPara(), RoleTianFu.class);
            if (rune == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return;
            }
            checkStall.setRune(rune);
        }
        if (this.checkStall(role, data_stall, checkStall, market.getSerial(), price)) {
            final List<Market> markets = this.marketMap.get(stall.getStallMenuSerial());
            if (markets == null) {
                return;
            }
            markets.remove(market);
            market.setPrice(price);
            market.setMarketPrice(stall.getBasicsPrice());
            market.setStartTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
            if (stall.getPublicityPeriod() > 0) {
                market.setIsPublicity(1);
                market.setPublicityTime(stall.getPublicityPeriod());
            }
            market.setExpireTime(stall.getExpireTime());
            market.setAmount(amount);
            markets.add(market);
            this.marketMap.put(stall.getStallMenuSerial(), markets);
            Db4CommonService.getInstance().add2Queue(market);
            this.sendMarketList(role);
        }
    }
    
    public void focus(final Role role, final ReqGeneralNotify req) {
        if (!this.commonVerify(role)) {
            return;
        }
        final long id = Long.parseLong(req.getStatus());
        final int serial = Integer.parseInt(req.getString());
        final Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        final SysDataStall stall = data_stall.get(serial);
        if (stall == null) {
            MessagePusher.pushMessage(role, new RespMsg("未找到该物品信息"));
            return;
        }
        final List<Market> markets = this.marketMap.get(stall.getStallMenuSerial());
        if (markets != null) {
            final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
            final Market market = markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getSerial() == serial && obj.getId().equals(id) && obj.getSaleAmount() == 0 && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).findFirst().orElse(null);
            if (market != null) {
                if (market.getFocus().contains(role.getRoleId())) {
                    market.getFocus().remove(role.getRoleId());
                    MessagePusher.pushMessage(role, new RespMsg("取消关注，目前关注人数：" + market.getFocus().size()));
                }
                else {
                    market.getFocus().add(role.getRoleId());
                    MessagePusher.pushMessage(role, new RespMsg("关注成功，目前关注人数：" + market.getFocus().size()));
                }
                markets.forEach(e -> {
                    if (e.getId().equals(id)) {
                        e.setFocus(market.getFocus());
                        return;
                    }
                    else {
                        return;
                    }
                });
                this.marketMap.put(stall.getStallMenuSerial(), markets);
            }
        }
    }
    
    private List<Market> queryMyMarket(final Role role) {
        final List<Market> result = new ArrayList<Market>();
        for (final Integer key : this.marketMap.keySet()) {
            final List<Market> markets = this.marketMap.get(key);
            if (markets != null) {
                for (int i = 0; i < markets.size(); ++i) {
                    final Market market = markets.get(i);
                    if (market != null && market.getRoleId().equals(role.getRoleId())) {
                        result.add(market);
                    }
                }
            }
        }
        return result;
    }
    
    public void columbia(final Role role, final ReqGeneralNotify req) {
        if (!this.commonVerify(role)) {
            return;
        }
        final String[] split = req.getString().split("-");
        if (split.length < 2) {
            return;
        }
        final int stallSerial = Integer.valueOf(split[0]);
        final int category = Integer.valueOf(split[1]);
        if (category <= 0) {
            return;
        }
        final List<Market> markets = this.marketMap.get(stallSerial);
        if (markets != null) {
            final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
            final List<Market> collect = markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getCategory() == category && exact > obj.getStartTime() + obj.getPublicityTime() && obj.getSaleAmount() == 0 && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
            final List<Market> list = collect.stream().sorted(Comparator.comparing(Market::getStartTime).thenComparing(Market::getPrice)).collect(Collectors.toList());
            final List<Market> subList = PageUtil.subList(list, 1, list.size());
            final long averaging = list.stream().collect(Collectors.averagingLong(Market::getPrice)).longValue();
            final List<Columbia> objects = new ArrayList<Columbia>();
            for (final Market market : subList) {
                final Columbia c = new Columbia(String.valueOf(market.getId()), market.getSerial(), String.valueOf(market.getPrice()));
                objects.add(c);
            }
            final RespColumbia columbia = new RespColumbia();
            columbia.setPara(req.getString());
            columbia.setPrice(String.valueOf(averaging));
            columbia.setList(objects);
            MessagePusher.pushMessage(role, columbia);
        }
    }
    
    public void takeCash(final Role role, final ReqGeneralNotify req) {
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final Long id = Long.valueOf(req.getStatus());
        final List<Market> list = this.queryMyMarket(role);
        final Market market = list.stream().filter(obj -> obj.getId().equals(id)).findFirst().orElse(null);
        if (market != null && market.getSaleAmount() > 0) {
            if (market.getStatus() == 1) {
                MessagePusher.pushMessage(role, new RespMsg("物品已被冻结，暂无法提现"));
                return;
            }
            final SysDataStall stall = MarketDataPool.t_data_stall.get(market.getSerial());
            if (stall == null) {
                return;
            }
            if (market.getAuditTime() > 0) {
                final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
                final int auditTime = market.getAuditTime() + stall.getAuditTime();
                if (exact < auditTime) {
                    MessagePusher.pushMessage(role, new RespMsg("审核中不可提现"));
                    return;
                }
            }
            final List<Market> markets = this.marketMap.get(market.getStallMenuSerial());
            final long price = market.getSaleAmount() * market.getPrice();
            markets.remove(market);
            if (market.getAmount() <= 0) {
                market.setDelete(true);
            }
            else {
                market.setSaleAmount((short)0);
                market.setAuditTime(0);
                markets.add(market);
            }
            this.roleService.addCurrency(role, stall.getType(), BigDecimalUtil.decimalScaleDown(0, price - price * stall.getRevenueRatio()).longValue(), "交易：物品编号：" + market.getSerial(), true, false);
            this.marketMap.put(market.getStallMenuSerial(), markets);
            this.sendMarketList(role);
            Db4CommonService.getInstance().add2Queue(market);
        }
    }
    
    public synchronized void buyGoods(final IoSession session, final ReqBuyFromStall req) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final boolean limit = this.marketLimit(role);
        if (!limit) {
            return;
        }
        final boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        final short amount = req.getAmount();
        if (req.getAmount() > 32000 || req.getAmount() <= 0) {
            return;
        }
        try {
            if (!this.commonVerify(role)) {
                return;
            }
            final String[] split = req.getPara().split("-");
            if (split.length < 2) {
                return;
            }
            final int stallSerial = Integer.valueOf(split[0]);
            final int category = Integer.valueOf(split[1]);
            final long id = Long.valueOf(req.getId());
            final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
            final List<Market> markets = this.marketMap.get(stallSerial);
            if (markets != null) {
                final List<Market> collect = markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getCategory() == category && obj.getStallMenuSerial() == stallSerial && exact > obj.getStartTime() + obj.getPublicityTime() && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                List<Market> list = collect.stream().sorted(Comparator.comparing(Market::getStartTime).thenComparing(Market::getPrice)).collect(Collectors.toList());
                list = collect.stream().sorted(Comparator.comparing(Market::getPrice)).collect(Collectors.toList());
                final Market market = list.stream().filter(obj -> obj.getId().equals(id)).findFirst().orElse(null);
                if (market == null) {
                    MessagePusher.pushMessage(session, new RespMsg("您购买的物品不存在或已下架"));
                    return;
                }
                final SysDataStall stall = MarketDataPool.t_data_stall.get(market.getSerial());
                if (stall == null) {
                    return;
                }
                if (market.getAmount() <= 0) {
                    MessagePusher.pushMessage(session, new RespMsg("您购买的物品不存在或已下架"));
                    return;
                }
                if (amount > 0) {
                    final boolean b = this.roleService.checkCurrency(role, stall.getType(), req.getAmount() * market.getPrice());
                    if (!b) {
                        return;
                    }
                    if (Deploy.market_limit) {
                        final long already = role.getTaskProgress().getEveryDayUpperLimit().getLong("market", 0L);
                        final long quota = getQuota(role.getTc());
                        if (already + req.getAmount() * market.getPrice() > quota) {
                            MessagePusher.pushMessage(role, new RespMsg("今日可交易金额剩余：" + MoneyUtil.colours(quota - already)));
                            return;
                        }
                        role.getTaskProgress().getEveryDayUpperLimit().setv("market", already + req.getAmount() * market.getPrice());
                    }
                    if (stall.getIsItem() == 1 || stall.getIsItem() == 3) {
                        long economize = 0L;
                        long price = 0L;
                        if (stall.getIsItem() == 1) {
                            final boolean checkPos = this.bagService.checkPos(role, market.getSerial(), amount);
                            if (!checkPos) {
                                return;
                            }
                        }
                        final List<MarketDealRecord> objects = new ArrayList<MarketDealRecord>();
                        int sum = 0;
                        int quantity = 0;
                        for (final Market m : list) {
                            if (m.getAmount() <= 0) {
                                continue;
                            }
                            final int num = (sum + m.getAmount() > amount) ? (amount - sum) : m.getAmount();
                            if (market.getPrice() > m.getPrice()) {
                                economize += (market.getPrice() - m.getPrice()) * num;
                                quantity += num;
                            }
                            final short mAmount = m.getAmount();
                            final int residue = Math.max(0, mAmount - num);
                            m.setAmount((short)residue);
                            final int buyNum = (residue == 0) ? mAmount : num;
                            if (buyNum < 0) {
                                this.logger.error("购买物品数量出错--------------------------------------------------------");
                                return;
                            }
                            sum += buyNum;
                            m.setSaleAmount((short)(m.getSaleAmount() + buyNum));
                            m.setAuditTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
                            Db4CommonService.getInstance().add2Queue(m);
                            objects.add(new MarketDealRecord(m, num, new IdWorker().nextId(), role.getRoleId()));
                            this.updateMarket(markets, stallSerial, market);
                            price += m.getPrice() * num;
                            if (sum >= amount) {
                                break;
                            }
                        }
                        if (sum <= 0) {
                            this.logger.error("购买物品数量异常：{}", (Object)sum);
                            return;
                        }
                        MessagePusher.pushMessage(session, new RespMsg("购买成功"));
                        this.roleService.deductCurrency(role, stall.getType(), price, "购买物品");
                        if (stall.getIsItem() == 1) {
                            this.bagService.addGoods(role, market.getSerial(), sum, TimeUtil.dayInMillis(TimeUtil.addition(30)), true, true, "集市购买物品");
                        }
                        else if (stall.getIsItem() == 3) {
                            this.cardService.addCard(role, market.getSerial(), sum, TimeUtil.dayInMillis(TimeUtil.addition(30)), true, true);
                        }
                        this.marketMap.put(stallSerial, markets);
                        if (economize > 0L) {
                            MessagePusher.pushMessage(session, new RespMsg(String.format("已自动购买#<LG1>%d#个同类的低价商品，节省#<IR2>#%s银两", quantity, MoneyUtil.colours(economize))));
                        }
                        for (final MarketDealRecord object : objects) {
                            Db4CommonService.getInstance().add2Queue(object);
                        }
                    }
                    else if (stall.getIsItem() == 0) {
                        final int pos = this.bagService.getPos(role, false);
                        if (pos < 0) {
                            MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                            return;
                        }
                        market.setAmount((short)0);
                        market.setSaleAmount((short)1);
                        market.setAuditTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
                        this.updateMarket(markets, stallSerial, market);
                        Db4CommonService.getInstance().add2Queue(market);
                        MessagePusher.pushMessage(session, new RespMsg("购买成功"));
                        this.roleService.deductCurrency(role, stall.getType(), market.getPrice(), "购买装备");
                        final BagEquip equip = JsonUtils.string2Object(market.getPara(), BagEquip.class);
                        equip.setPos(pos);
                        equip.setDelete(false);
                        equip.setRId(role.getId());
                        equip.setBd(TimeUtil.dayInMillis(TimeUtil.addition(30)));
                        equip.setArtificeGeneralHistory(new ArrayList<FiedEquip>());
                        equip.setArtificeHighHistory(new ArrayList<List<FiedEquip>>());
                        equip.setArtificeSuperHistory(new ArrayList<List<FiedEquip>>());
                        equip.setRedoGeneralHistory(new ArrayList<FiedEquip>());
                        equip.setRedoHighHistory(new ArrayList<List<FiedEquip>>());
                        equip.setRedoSuperHistory(new ArrayList<List<FiedEquip>>());
                        equip.setPurifyGeneralHistory(new ArrayList<FiedEquip>());
                        equip.setPurifyHighHistory(new ArrayList<List<FiedEquip>>());
                        equip.setPurifySuperHistory(new ArrayList<List<FiedEquip>>());
                        this.bagService.addEquip(role, equip, false, "集市购买");
                        Db4CommonService.getInstance().add2Queue(new MarketDealRecord(market, 1, new IdWorker().nextId(), role.getRoleId()));
                    }
                    else if (stall.getIsItem() == 2) {
                        final SysPet sysPet = PetDataPool.petMap.get(stall.getSerial());
                        if (sysPet == null) {
                            return;
                        }
                        final boolean flag = SpringUtils.getPetService().canGetPet(role, sysPet, false);
                        if (!flag) {
                            return;
                        }
                        final Pet pet = JsonUtils.string2Object(market.getPara(), Pet.class);
                        if (pet == null) {
                            this.logger.error("购买宠物转换数据为空，交易编号 = {}, PET = {}", (Object)req.getId(), (Object)market.getPara());
                            return;
                        }
                        pet.doAfterInit();
                        this.roleService.deductCurrency(role, stall.getType(), market.getPrice(), "购买宠物");
                        market.setAmount((short)0);
                        market.setSaleAmount((short)1);
                        market.setAuditTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
                        this.updateMarket(markets, stallSerial, market);
                        Db4CommonService.getInstance().add2Queue(market);
                        pet.setDelete(false);
                        pet.setRoleId(role.getRoleId());
                        pet.setBindTime(TimeUtil.dayInMillis(TimeUtil.addition(30)));
                        final Map<Integer, Pet> map = this.petService.selectPet(role);
                        map.put(pet.getPetId(), pet);
                        pet.save();
                        RoleUtils.toUpdateAssistList(role, pet, true);
                        role.save();
                        SpringUtils.getPetService().loadPet(role, pet);
                        MessagePusher.pushMessage(session, new RespMsg("购买成功"));
                        Db4CommonService.getInstance().add2Queue(new MarketDealRecord(market, 1, new IdWorker().nextId(), role.getRoleId()));
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, new ArrayList<PetEquip>()));
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, new ArrayList<PetEquip>()));
                    }
                    else if (stall.getIsItem() == 4) {
                        this.buyRune(role, markets, market, stall, stallSerial);
                    }
                }
            }
        }
        catch (final Exception e) {
            e.printStackTrace();
            this.logger.error("购买物品出错，-------------------------------------------------------");
        }
    }
    
    public void buyRune(final Role role, final List<Market> markets, final Market market, final SysDataStall stall, final int stallSerial) {
        final List<RoleTianFu> roleTianFus = (List<RoleTianFu>)DataCache.ROLE_TIAN_FU.get(role.getId()).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
        if (roleTianFus.size() >= 75) {
            MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
            return;
        }
        market.setAmount((short)0);
        market.setSaleAmount((short)1);
        market.setAuditTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
        this.updateMarket(markets, stallSerial, market);
        Db4CommonService.getInstance().add2Queue(market);
        MessagePusher.pushMessage(role, new RespMsg("购买成功"));
        this.roleService.deductCurrency(role, stall.getType(), market.getPrice(), "购买天策符");
        final RoleTianFu rune = JsonUtils.string2Object(market.getPara(), RoleTianFu.class);
        rune.setRoleId(role.getId());
        rune.setDelete(false);
        rune.setBd(TimeUtil.dayInMillis(TimeUtil.addition(60)));
        DataCache.ROLE_TIAN_FU.get(role.getId()).add(rune);
        rune.save();
        SpringUtils.getBean(RoleTianFuService.class).roleTianFuList(role);
        Db4CommonService.getInstance().add2Queue(new MarketDealRecord(market, 1, new IdWorker().nextId(), role.getRoleId()));
    }
    
    public void updateMarket(final List<Market> markets, final int stallSerial, final Market market) {
        markets.stream().map(p -> {
            if (p.getId().equals(market.getId())) {
                p.setAmount(market.getAmount());
                p.setSaleAmount(market.getSaleAmount());
                p.setAuditTime(market.getAuditTime());
            }
            return p;
        }).collect(Collectors.toList());
        this.marketMap.put(stallSerial, markets);
    }
    
    public void goodsInfo(final IoSession session, final ReqGoodInfo req) {
        final Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        final List<Market> markets = this.marketMap.get((int)req.getSerial());
        if (markets != null) {
            final Long id = Long.valueOf(req.getId());
            final Market market = markets.stream().filter(obj -> obj.getId().equals(id) && obj.getStatus() == 2).findFirst().orElse(null);
            if (market != null) {
                final SysDataStall stall = MarketDataPool.t_data_stall.get(market.getSerial());
                if (stall != null) {
                    final int[] variables = this.getVariables(market);
                    final int status = variables[0];
                    final int endTime = variables[1];
                    if (stall.getIsItem() == 0) {
                        final BagEquip equip = JsonUtils.string2Object(market.getPara(), BagEquip.class);
                        this.equipDetail(role, equip, market, status, endTime);
                    }
                    else if (stall.getIsItem() != 1) {
                        if (stall.getIsItem() == 2) {
                            final Pet pet = JsonUtils.string2Object(market.getPara(), Pet.class);
                            if (pet == null) {
                                this.logger.error("宠物交易详情转换数据为空，交易编号 = {}，分类编号 = {}, PET = {}", new Object[] { req.getId(), req.getSerial(), market.getPara() });
                                return;
                            }
                            pet.doAfterInit();
                            this.petDetail(role, pet, market, status, endTime);
                        }
                    }
                }
            }
        }
    }
    
    public void petDetail(final Role role, final Pet pet, final Market market, final int status, final int endTime) {
        final RespMarketPetCard card = new RespMarketPetCard();
        card.setId(String.valueOf(market.getId()));
        card.setRoleId(market.getRoleId());
        card.setSerial(market.getSerial());
        card.setSkills(pet.getSkills());
        card.setStatus((byte)status);
        card.setEndTime(endTime);
        final Role ownerRole = this.roleService.getPlayer(market.getRoleId());
        card.setFields(this.petService.cardPetFied(ownerRole, pet, new ArrayList<PetEquip>()));
        final List<FieldProp> fieldProps = this.petService.petFieldPro(ownerRole, pet);
        if (fieldProps != null) {
            card.setProList(fieldProps);
        }
        MessagePusher.pushMessage(role, card);
    }
    
    public void equipDetail(final Role role, final BagEquip equip, final Market market, final int status, final int endTime) {
        final RespMarketGoodCard card = new RespMarketGoodCard(String.valueOf(market.getId()), market.getRoleId(), market.getSerial(), equip.getField(), equip.getArtifice());
        card.setStatus((byte)status);
        card.setEndTime(endTime);
        MessagePusher.pushMessage(role, card);
    }
    
    private int[] getVariables(final Market market) {
        final int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
        int status = 1;
        int endTime = exact;
        final int publicityTime = market.getStartTime() + market.getPublicityTime();
        if (exact < publicityTime) {
            status = 2;
            endTime = publicityTime;
        }
        else {
            final int time = publicityTime + market.getExpireTime();
            if (exact > time) {
                status = 3;
            }
            else {
                status = 1;
            }
        }
        final int[] objs = { status, endTime };
        return objs;
    }
    
    public String updateMarketStatus(final int classify, final long id, final byte status) {
        final List<Market> markets = this.marketMap.get(classify);
        if (markets == null) {
            return "无此分类";
        }
        final Market market = markets.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (market == null) {
            return "未找到商品或已下架";
        }
        market.setStatus(status);
        Db4CommonService.getInstance().add2Queue(market);
        return null;
    }
    
    public String withdraw(final int classify, final long id) {
        final List<Market> markets = this.marketMap.get(classify);
        if (markets == null) {
            return "无此分类";
        }
        final Market market = markets.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (market == null) {
            return "未找到商品或已下架";
        }
        final Role role = this.roleService.getOnlinePlayer(market.getRoleId());
        if (role == null) {
            return "未找到玩家或玩家不在线";
        }
        final ReqGeneralNotify notify = new ReqGeneralNotify();
        notify.setStatus(String.valueOf(id));
        notify.setString(String.valueOf(market.getSerial()));
        this.removeGoods(role, notify, false);
        return null;
    }
    
    public String deleteMarket(final int classify, final long id) {
        final List<Market> markets = this.marketMap.get(classify);
        if (markets == null) {
            return "无此分类";
        }
        final Market market = markets.stream().filter(e -> e.getId().equals(id)).findFirst().orElse(null);
        if (market == null) {
            return "未找到商品或已下架";
        }
        markets.remove(market);
        market.setDelete(true);
        Db4CommonService.getInstance().add2Queue(market);
        return null;
    }
    
    public boolean marketLimit(final Role role) {
        if (!SafeUtil.isUnlock(role)) {
            return false;
        }
        if (SpringUtils.getBean(ServerService.class).getType() == 1) {
            final String msg = DataGlobalFunc.needGradeJudge(Deploy.market_need_relive, Deploy.market_need_level, role.getRelive(), role.getLevel(), "你的#<R>角色等级#不足");
            if (msg != null) {
                MessagePusher.pushMessage(role, new RespMsg(msg));
                return false;
            }
            final Message message = DataGlobalFunc.operationalConstraint(role, Deploy.market, "充值#<R>%d#才可以操作集市！");
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
    
    public static long getQuota(final int tc) {
        final int max = Math.max(0, tc - Deploy.market);
        return max / 100 * 800000000L + 4000000000L;
    }
}
