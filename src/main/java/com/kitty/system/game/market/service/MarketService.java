package com.kitty.system.game.market.service;

import com.kitty.mina.message.Message;
import com.kitty.common.start.ServerService;
import com.kitty.system.game.safe.util.SafeUtil;
import com.kitty.system.game.market.message.RespMarketGoodCard;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.market.message.RespMarketPetCard;
import com.kitty.system.game.market.message.ReqGoodInfo;
import com.kitty.system.game.market.model.MarketDealRecord;
import com.kitty.mina.cache.Deploy;
import com.kitty.system.game.market.message.ReqBuyFromStall;
import com.kitty.system.game.market.message.RespColumbia;
import com.kitty.system.game.market.message.vo.Columbia;
import org.nutz.lang.util.NutMap;
import com.kitty.system.game.market.model.StallItem;
import com.kitty.common.utils.PageUtil;
import com.kitty.system.game.market.message.RespStallList;
import java.util.Comparator;
import com.kitty.system.init.domain.SysDataStallMenu;
import com.kitty.system.game.general.message.ReqGeneralNotify;
import com.kitty.common.db.Db4CommonService;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.role.utils.RoleUtils;
import com.kitty.system.game.enter.message.RespDestroySucceed;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.system.game.tianfu.service.RoleTianFuService;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.cache.DataCache;
import com.kitty.system.game.card.domain.CardChange;
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
import com.kitty.system.game.equip.domain.PetEquip;
import com.kitty.system.game.tianfu.domain.RoleTianFu;
import com.kitty.common.utils.BigDecimalUtil;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.system.init.domain.SysSeniorBieEq;
import com.kitty.system.enums.common.ACC_GRADE;
import com.kitty.system.enums.war.itempro.def.ITEMLARGETYPE;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.common.message.RespHint;
import com.kitty.system.game.fightnew.utils.SkillFunc;
import com.kitty.system.enums.war.wardef.PETSKILLSUB;
import com.kitty.system.game.pet.domain.vo.PetSkill;
import com.kitty.common.utils.TimeUtil;
import com.kitty.system.game.pet.pool.PetDataPool;
import com.kitty.system.init.domain.SysPet;
import com.kitty.system.game.skill.pool.SkillDataPool;
import com.kitty.system.game.tianfu.domain.SysTianFuSkill;
import com.kitty.system.game.market.model.vo.CheckStall;
import com.kitty.system.game.market.model.Cargo;
import com.kitty.system.game.market.message.RespStallMine;
import java.util.Map;
import com.kitty.system.game.market.message.RespMarketSearchItem;
import java.util.Optional;
import java.util.stream.Collectors;
import com.kitty.system.init.domain.SysDataStall;
import com.kitty.system.game.market.pool.MarketDataPool;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.role.domain.Role;
import java.util.Iterator;
import java.util.ArrayList;
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
        this.marketMap = new ConcurrentHashMap();
    }
    
    public void init() {
        List<Market> list = this.dao.query(Market.class, Cnd.NEW());
        for (Market market : list) {
            List<Market> markets = (List)this.marketMap.get(Integer.valueOf(market.getStallMenuSerial()));
            if (markets == null) {
                markets = new ArrayList<>();
            }
            markets.add(market);
            this.marketMap.put(Integer.valueOf(market.getStallMenuSerial()), markets);
        }
    }
    
    private boolean commonVerify(Role role) {
        if (role.getExtendBox().getBanTrade() == 1) {
            MessagePusher.pushMessage(role, new RespMsg("您没有摆摊权限"));
            return false;
        }
        return true;
    }
    
    public void reqPrice(Role role, int serial) {
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(serial));
        if (stall == null) {
            MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
            return;
        }
        String minimum;
        String price = minimum = stall.getBasicsPrice().toString();
        int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
        List<Market> markets = (List)this.marketMap.get(stall.getStallMenuSerial());
        if (markets != null) {
            List<Market> collect = (List)markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getSerial() == serial && obj.getStallMenuSerial() == (int)stall.getStallMenuSerial() && exact > obj.getStartTime() + obj.getPublicityTime() && obj.getSaleAmount() == 0 && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
            if (collect.size() > 0) {
                Long min = (Long)((Optional)collect.stream().map(Market::getPrice).collect(Collectors.minBy(Long::compare))).get();
                minimum = String.valueOf(min);
            }
        }
        role.pushTempCache("price", price);
        MessagePusher.pushMessage(role, new RespMarketSearchItem(serial, price, minimum));
        return;
    }
    
    public void sendMarketList(Role role) {
        try {
            RespStallMine respStallMine = new RespStallMine();
            respStallMine.setStallTotalNum((byte)role.getExtendBox().getStall());
            List<Cargo> goodsList = new ArrayList<>();
            respStallMine.setList(goodsList);
            List<Market> markets = this.queryMyMarket(role);
            Map<Integer, SysDataStall> stall = MarketDataPool.t_data_stall;
            int time = Math.toIntExact(System.currentTimeMillis() / 1000L);
            for (Market market : markets) {
                SysDataStall sysDataStall = (SysDataStall)stall.get(Integer.valueOf(market.getSerial()));
                Cargo cargo = new Cargo();
                if (market.getAuditTime() > 0) {
                    if (sysDataStall == null) {
                        continue;
                    }
                    cargo.setShowEndTime(market.getAuditTime());
                    cargo.setEndTime(market.getAuditTime() + (int)sysDataStall.getAuditTime());
                    if (time > cargo.getEndTime()) {
                        cargo.setStatus((byte)5);
                    }
                    else {
                        cargo.setStatus((byte)4);
                    }
                }
                else {
                    int publicityTime = market.getStartTime() + market.getPublicityTime();
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
                String extra = this.specialExtra(market.getPara(), (int)sysDataStall.getIsItem());
                if (extra != null) {
                    cargo.setExtra(extra);
                }
                goodsList.add(cargo);
            }
            MessagePusher.pushMessage(role, respStallMine);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public boolean checkStall(Role role, Map<Integer, SysDataStall> data_stall, CheckStall checkStall, int serial, long price) {
        try {
            if (checkStall != null) {
                if (checkStall.getPet() != null) {
                    serial = (int)checkStall.getPet().getPetType();
                }
                else if (checkStall.getEquip() != null) {
                    serial = checkStall.getEquip().getType();
                }
            }
            SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(serial));
            if (stall == null) {
                this.logger.error("检查物品是否可上架,物品不可上架编号为：{}", Integer.valueOf(serial));
                MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
                return false;
            }
            if (stall.getIsItem() == 1 || stall.getIsItem() == 3) {
                if (stall.getIsCheck() > 0) {
                    Object obj = role.getTempCache("price", null);
                    if (obj == null) {
                        return false;
                    }
                    Long cache = Long.valueOf(String.valueOf(obj));
                    List<Long> list = new ArrayList<>();
                    list.add(cache);
                    double nominate = 0.05;
                    for (int j = 0; j < 10; ++j) {
                        list.add(Long.valueOf(BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Double.valueOf((double)(long)cache + (double)(long)cache * nominate)).longValue()));
                        list.add(Long.valueOf(BigDecimalUtil.decimalScaleBig(Integer.valueOf(0), Double.valueOf((double)(long)cache + (double)(long)cache * -nominate)).longValue()));
                        nominate += 0.05;
                    }
                    if (!list.contains(Long.valueOf(price))) {
                        return false;
                    }
                }
            }
            else if (stall.getIsItem() == 0 && checkStall.getEquip() != null) {
                Integer type = DataGlobalFunc.GetItemTypeByItemTypeId(Integer.valueOf(checkStall.getEquip().getType()));
                if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_ACSRY.getValue())) && checkStall.getEquip().getProductOrder() < ACC_GRADE.PRICELESS.getValue()) {
                    return false;
                }
                if (type.equals(Integer.valueOf(ITEMLARGETYPE.ITEM_LARGE_TYPE_SENIOREQPT.getValue()))) {
                    SysSeniorBieEq bieEq = (SysSeniorBieEq)EquipDataPool.seniorBieEq.get(Integer.valueOf(checkStall.getEquip().getType()));
                    boolean flag = DataGlobalFunc.seniorIsGem(bieEq, checkStall.getEquip());
                    if (!flag) {
                        MessagePusher.pushMessage(role, new RespMsg("此装备属性不佳，不可上架交易~"));
                        return false;
                    }
                }
                int num = DataGlobalFunc.getGemNum(checkStall.getEquip());
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
                    String date2 = TimeUtil.stampToDate((long)checkStall.getEquip().getBd() * 1000L);
                    String[] split2 = date2.split("-");
                    MessagePusher.pushMessage(role, new RespMsg(String.format("#<R>%s年%s月%s日05点前不可交易#", split2[0], split2[1], split2[2])));
                    return false;
                }
            }
            else if (stall.getIsItem() == 2 && checkStall.getPet() != null) {
                Integer petSerial = checkStall.getPet().getPetType();
                if (petSerial != null && (int)petSerial > 0) {
                    SysPet sysPet = (SysPet)PetDataPool.petMap.get(petSerial);
                    if (sysPet == null) {
                        return false;
                    }
                    if ((int)sysPet.getLevelType() < 4) {
                        MessagePusher.pushMessage(role, new RespMsg("只有神兽才可以交易~"));
                        return false;
                    }
                    int level = (int)role.getRelive() * 1000 + (int)role.getLevel();
                    if (level < 1100) {
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
                    String date = TimeUtil.stampToDate((long)checkStall.getPet().getBindTime() * 1000L);
                    String[] split = date.split("-");
                    MessagePusher.pushMessage(role, new RespMsg(String.format("#<R>%s年%s月%s日05点前不可交易#", split[0], split[1], split[2])));
                    return false;
                }
                if ((int)checkStall.getPet().getPetBase().getGrade_base() < 3) {
                    return false;
                }
                List<Integer> unset = new ArrayList<>();
                List<Integer> skills = new ArrayList<>();
                for (int i = 0; i < checkStall.getPet().getSkills().size(); ++i) {
                    PetSkill petSkill = (PetSkill)checkStall.getPet().getSkills().get(i);
                    if (petSkill.getSerial() >= 0) {
                        unset.add(Integer.valueOf(i));
                        if (petSkill.getType() != PETSKILLSUB.SUBTYPE_SPECIAL.getValue() && petSkill.getType() != PETSKILLSUB.SUBTYPE_BORN.getValue() && petSkill.getType() != PETSKILLSUB.SUB_TYPE_SOUL.getValue()) {
                            int current = (int)SkillFunc.getPetSkillSubType(Integer.valueOf(petSkill.getSerial()));
                            if (current == PETSKILLSUB.SUBTYPE_SENIOR.getValue() || current == PETSKILLSUB.SUBTYPE_SUPREME.getValue()) {
                                skills.add(Integer.valueOf(i));
                            }
                        }
                    }
                }
                if (unset.size() < 5 || skills.size() < 3) {
                    return false;
                }
                if ((int)checkStall.getPet().getRebirth() == 0 && (int)checkStall.getPet().getBone() < 1) {
                    return false;
                }
                if ((int)checkStall.getPet().getRebirth() == 1 && (int)checkStall.getPet().getBone() < 2) {
                    return false;
                }
                if ((int)checkStall.getPet().getRebirth() >= 2 && (int)checkStall.getPet().getBone() < 4) {
                    return false;
                }
                List<PetEquip> petWearEquip = this.bagService.getPetWearEquip((long)role.getId(), (long)checkStall.getPet().getId());
                if (petWearEquip != null && petWearEquip.size() > 0) {
                    MessagePusher.pushMessage((long)role.getId(), new RespMsg(" 请卸下召唤兽佩饰后再试"));
                    return false;
                }
            }
            else if (stall.getIsItem() == 4 && checkStall.getRune() != null) {
                if ((int)role.getRelive() * 1000 + (int)role.getLevel() < 1100) {
                    MessagePusher.pushMessage(role, new RespMsg("等级达到1转100级才可以交易天策符~"));
                    return false;
                }
                if (price > 1000000000L) {
                    MessagePusher.pushMessage(role, new RespMsg("价格过高~"));
                    return false;
                }
                RoleTianFu rune = checkStall.getRune();
                if (rune == null) {
                    return false;
                }
                SysTianFuSkill runeInfo = (SysTianFuSkill)SkillDataPool.tianFuSkillMap.get(Integer.valueOf(rune.getTianFuSerial()));
                if (runeInfo == null || runeInfo.getRarityJudge().size() < 4) {
                    MessagePusher.pushMessage(role, new RespMsg("此装备属性不佳，不可上架交易~"));
                    return false;
                }
                if (rune.getLevel() < (byte)runeInfo.getRarityJudge().get(3)) {
                    MessagePusher.pushMessage(role, new RespMsg("此装备属性不佳，不可上架交易~"));
                    return false;
                }
            }
            return true;
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("检查物品是否可上架错误，玩家编号 = {}，上架物品编号为：{}", role.getRoleId(), Integer.valueOf(serial));
            return false;
        }
    }
    
    public void upShell(IoSession session, ReqSetStallGoods req) {
        if (req.getAmount() > 32000 || req.getAmount() <= 0) {
            return;
        }
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        boolean limit = this.marketLimit(role);
        if (!limit) {
            return;
        }
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        List<Market> markets = this.queryMyMarket(role);
        if (markets.size() >= role.getExtendBox().getStall()) {
            MessagePusher.pushMessage(role, new RespMsg("目前摊位已满，请下架部分物品后再次寄售"));
            return;
        }
        if (!this.commonVerify(role)) {
            return;
        }
        Long price = Long.valueOf(req.getPrice());
        if ((long)price < 10L) {
            return;
        }
        if ((long)price <= 0L) {
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
        return;
    }
    
    public void equipShelves(Role role, ReqSetStallGoods req, Long price) {
        Long inventory = Long.valueOf(req.getInventory());
        Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
        BagEquip equip = (BagEquip)bagEquipMap.values().stream().filter(e -> e.getId().equals(inventory)).findFirst().orElse(null);
        if (equip == null) {
            MessagePusher.pushMessage(role, new RespMsg("背包中没有该装备"));
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        CheckStall checkStall = new CheckStall(equip);
        if (this.checkStall(role, data_stall, checkStall, equip.getType(), (long)price)) {
            boolean b = this.putAway(role, data_stall, equip.getType(), req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(equip.getType()));
                RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(stall.getRevenueRatio() * 100.0)))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("摆摊装备：请求参数 = %s", req.toString()), role.getMarking());
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
        return;
    }
    
    public void goodsShelves(Role role, ReqSetStallGoods req, Long price) {
        int inventory = (int)Integer.valueOf(req.getInventory());
        int serial = -1;
        int num = -1;
        BagEquip equip = null;
        BagGoods goods = null;
        try {
            Map<Long, BagGoods> bagGoodsMap = this.bagService.getBagGoods(role);
            goods = (BagGoods)bagGoodsMap.values().stream().filter(e -> e.getPos() == inventory).findFirst().orElse(null);
        }
        catch (ConcurrentModificationException e) {
            LoggerUtils.error("物品摆摊处理下标异常", e.getMessage());
        }
        if (goods != null) {
            serial = goods.getSerial();
            num = DataGlobalFunc.unrestricted(goods.getRests());
        }
        else {
            Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
            equip = (BagEquip)bagEquipMap.values().stream().filter(e -> e.getPos() == inventory).findFirst().orElse(null);
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
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        CheckStall checkStall = new CheckStall(equip);
        if (this.checkStall(role, data_stall, checkStall, serial, (long)price)) {
            boolean b = this.putAway(role, data_stall, serial, req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(serial));
                RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(stall.getRevenueRatio() * 100.0)))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("摆摊物品：请求参数 = %s", req.toString()), role.getMarking());
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
        return;
    }
    
    public void petShelves(Role role, ReqSetStallGoods req, Long price) {
        int inventory = (int)Integer.valueOf(req.getInventory());
        Map<Integer, Pet> map = this.petService.selectPet(role);
        Pet pet = (Pet)map.get(Integer.valueOf(inventory));
        if (pet == null) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        CheckStall checkStall = new CheckStall(pet);
        if (this.checkStall(role, data_stall, checkStall, (int)pet.getPetType(), (long)price)) {
            boolean b = this.putAway(role, data_stall, (int)pet.getPetType(), req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                SysDataStall stall = (SysDataStall)data_stall.get(pet.getPetType());
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(stall.getRevenueRatio() * 100.0)))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("摆摊宠物：请求参数 = %s", req.toString()), role.getMarking());
                EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, new ArrayList<>()));
                EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, new ArrayList<>()));
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
        return;
    }
    
    public void cardShelves(Role role, ReqSetStallGoods req, Long price) {
        long inventory = (long)Long.valueOf(req.getInventory());
        int num = -1;
        Map<Long, CardChange> cardMap = this.cardService.getCard(role);
        CardChange change = (CardChange)cardMap.get(Long.valueOf(inventory));
        if (change != null) {
            num = DataGlobalFunc.unrestricted(change.getRests());
        }
        if (req.getAmount() > num) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            MessagePusher.pushMessage(role, new RespMsg("可上架道具数量：" + num));
            return;
        }
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        if (this.checkStall(role, data_stall, null, change.getSerial(), (long)price)) {
            boolean b = this.putAway(role, data_stall, change.getSerial(), req.getPrice(), req.getAmount(), req.getInventory(), null);
            if (b) {
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(change.getSerial()));
                RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(stall.getRevenueRatio() * 100.0)))));
                MessagePusher.pushMessage(role, confirm);
                RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("摆摊变身卡：请求参数 = %s", req.toString()), role.getMarking());
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
        return;
    }
    
    private void runeShelves(Role role, ReqSetStallGoods req, Long price) {
        long inventory = (long)Long.valueOf(req.getInventory());
        List<RoleTianFu> roleTianFus = DataCache.ROLE_TIAN_FU.get(role.getId());
        if (roleTianFus == null) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        RoleTianFu roleTianFu = (RoleTianFu)roleTianFus.stream().filter(e -> e.getId().equals(Long.valueOf(inventory))).findFirst().orElse(null);
        if (roleTianFu == null) {
            MessagePusher.pushMessage(role, new RespHint(14, (short)0));
            return;
        }
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        CheckStall checkStall = new CheckStall(roleTianFu);
        if (this.checkStall(role, data_stall, checkStall, roleTianFu.getTianFuSerial(), (long)price)) {
            boolean b = this.putAway(role, data_stall, roleTianFu.getTianFuSerial(), req.getPrice(), req.getAmount(), req.getInventory(), checkStall);
            if (b) {
                SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(roleTianFu.getTianFuSerial()));
                this.sendMarketList(role);
                MessagePusher.pushMessage(role, new RespHint(14, (short)1));
                RespConfirm confirm = new RespConfirm();
                confirm.setTitle("上架成功");
                confirm.setConfirmType("market_put_away");
                confirm.setOnlyConfirm(1);
                confirm.setNo_close_btn(1);
                confirm.setTips("出售期1天，未出售的商品自动下架");
                confirm.setPara_str(JsonUtils.object2String(new UnifiedValue("tax", BigDecimalUtil.decimalScaleIntegerDown(Integer.valueOf(0), Double.valueOf(stall.getRevenueRatio() * 100.0)))));
                MessagePusher.pushMessage(role, confirm);
                return;
            }
        }
        MessagePusher.pushMessage(role, new RespHint(14, (short)0));
        return;
    }
    
    public boolean putAway(Role role, Map<Integer, SysDataStall> data_stall, int serial, String price, short amount, String inventory, CheckStall checkStall) {
        if (amount > 32000 || amount <= 0) {
            return false;
        }
        Long value = Long.valueOf(price);
        if ((long)value <= 0L) {
            return false;
        }
        long serviceCharge = BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((double)(long)Long.valueOf(price) * 0.01)).longValue();
        if (serviceCharge < 1000L) {
            serviceCharge = 1000L;
        }
        else if (serviceCharge > 100000L) {
            serviceCharge = 100000L;
        }
        boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, serviceCharge, "上架道具");
        if (!b && !this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, serviceCharge, "上架道具")) {
            MessagePusher.pushMessage(role, new RespMsg("您当前无力支付手续费"));
            return false;
        }
        SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(serial));
        if (stall == null) {
            this.logger.error("上架道具,物品不可上架编号为：{}", Integer.valueOf(serial));
            MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
            return false;
        }
        Market market = new Market();
        market.setId(Long.valueOf(this.worker.nextId()));
        market.setSerial(serial);
        market.setRoleId(role.getRoleId());
        market.setStallMenuSerial((int)stall.getStallMenuSerial());
        market.setCategory(stall.getCategory());
        market.setPrice((long)Long.valueOf(price));
        market.setMarketPrice((long)Long.valueOf((long)stall.getBasicsPrice()));
        market.setStartTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
        if ((int)stall.getPublicityPeriod() > 0) {
            market.setIsPublicity(1);
            market.setPublicityTime((int)stall.getPublicityPeriod());
        }
        market.setExpireTime((int)stall.getExpireTime());
        market.setAmount(amount);
        if (stall.getIsItem() == 0 && checkStall.getEquip() != null) {
            String string2 = JsonUtils.object2String(checkStall.getEquip());
            if (string2 == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return false;
            }
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("摆摊装备：%s", checkStall.getEquip().toString()), role.getMarking());
            this.bagService.deleteRoleEquip(role, checkStall.getEquip(), "集市摆摊");
            market.setPara(string2);
            return this.market(role, stall, market, serviceCharge);
        }
        else if (stall.getIsItem() == 2 && checkStall.getPet() != null) {
            Pet pet = checkStall.getPet();
            String string = JsonUtils.object2String(pet);
            if (string == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return false;
            }
            RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)2, (byte)2, String.format("摆摊宠物：%s", pet.toString()), role.getMarking());
            market.setPara(string);
            Map<Integer, Pet> map = this.petService.selectPet(role);
            map.remove(pet.getPetId());
            MessagePusher.pushMessage(role, new RespDestroySucceed(Const.BUYBACK_TYPE_PET, String.valueOf(pet.getPetId())));
            pet.setDelete(true);
            RoleUtils.toUpdateAssistList(role, pet, false);
            pet.save();
            return this.market(role, stall, market, serviceCharge);
        }
        else if (stall.getIsItem() == 4 && checkStall.getRune() != null) {
            RoleTianFu rune = checkStall.getRune();
            String string = JsonUtils.object2String(rune);
            if (string == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return false;
            }
            RoleTianFuService service = (RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class);
            boolean delRune = service.delRune(role, (long)rune.getId());
            if (!delRune) {
                return false;
            }
            service.roleTianFuList(role);
            market.setPara(string);
            return this.market(role, stall, market, serviceCharge);
        }
        else {
            if (stall.getIsItem() == 3) {
                long id = (long)Long.valueOf(inventory);
                boolean consume2 = this.cardService.shelvesConsume(role, id, (int)amount);
                return consume2 && this.market(role, stall, market, serviceCharge);
            }
            if (stall.getIsItem() == 1) {
                int pos = (int)Integer.valueOf(inventory);
                boolean consume = this.bagService.shelvesConsume(role, pos, (int)amount);
                return consume && this.market(role, stall, market, serviceCharge);
            }
            return false;
        }
    }
    
    public boolean market(Role role, SysDataStall stall, Market market, long serviceCharge) {
        List<Market> markets = (List)this.marketMap.get(stall.getStallMenuSerial());
        if (markets == null) {
            markets = new ArrayList<>();
        }
        markets.add(market);
        this.marketMap.put(stall.getStallMenuSerial(), markets);
        MessagePusher.pushMessage(role, new RespMsg(String.format("摆摊成功，扣除#<IR%d>#%s，物品将在30分钟内上架", Const.RESTYPE_COIN, MoneyUtil.colours(Long.valueOf(serviceCharge)))));
        Db4CommonService.getInstance().add2Queue(market);
        return true;
    }
    
    public void marketQuery(ReqGeneralNotify req, Role role) {
        String[] split = req.getString().split("-");
        if (split.length < 6) {
            return;
        }
        int menus = (int)Integer.valueOf(split[0]);
        int stallSerial = (int)Integer.valueOf(split[1]);
        int category = (int)Integer.valueOf(split[2]);
        int sort = (int)Integer.valueOf(split[3]);
        int page = (int)Integer.valueOf(split[4]);
        int type = (int)Integer.valueOf(split[5]);
        int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
        Map<Integer, SysDataStallMenu> t_data_stall_menu = MarketDataPool.t_data_stall_menu;
        List<Integer> objects = new ArrayList<>();
        List<Market> markets = new ArrayList<>();
        SysDataStallMenu menu = (SysDataStallMenu)t_data_stall_menu.get(Integer.valueOf(stallSerial));
        if (menu != null && (int)menu.getAncestors() < 0) {
            for (SysDataStallMenu stallMenu : t_data_stall_menu.values()) {
                if ((int)stallMenu.getAncestors() > 0 && stallMenu.getAncestors().equals(Integer.valueOf(stallSerial))) {
                    List<Market> list = (List)this.marketMap.get(stallMenu.getSerial());
                    if (list != null) {
                        markets.addAll(list);
                        objects.add(stallMenu.getSerial());
                    }
                    else {
                        continue;
                    }
                }
                else {
                    continue;
                }
            }
        }
        List<Market> marketList = (List)this.marketMap.get(Integer.valueOf(stallSerial));
        if (marketList != null) {
            markets.addAll(marketList);
            objects.add(Integer.valueOf(stallSerial));
        }
        List<Market> list2 = new ArrayList<>();
        if (markets.size() > 0) {
            List<Market> collect = new ArrayList<>();
            if (type == 1) {
                if (category == 0) {
                    for (Integer object : objects) {
                        List<Market> markets_ = (List)markets.stream().filter(obj -> object.equals(Integer.valueOf(obj.getStallMenuSerial())) && obj.getPublicityTime() > 0 && obj.getStartTime() + obj.getPublicityTime() > exact && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                        if (markets_.size() > 0) {
                            collect.addAll(markets_);
                        }
                        else {
                            continue;
                        }
                    }
                }
                else {
                    for (Integer object : objects) {
                        List<Market> markets_ = (List)markets.stream().filter(obj -> object.equals(Integer.valueOf(obj.getStallMenuSerial())) && obj.getCategory() == category && obj.getPublicityTime() > 0 && obj.getStartTime() + obj.getPublicityTime() > exact && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                        if (markets_.size() > 0) {
                            collect.addAll(markets_);
                        }
                        else {
                            continue;
                        }
                    }
                }
            }
            else if (category == 0) {
                for (Integer object : objects) {
                    List<Market> markets_ = (List)markets.stream().filter(obj -> obj.getAmount() > 0 && object.equals(Integer.valueOf(obj.getStallMenuSerial())) && exact > obj.getStartTime() + obj.getPublicityTime() && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                    if (markets_.size() > 0) {
                        collect.addAll(markets_);
                    }
                    else {
                        continue;
                    }
                }
            }
            else {
                for (Integer object : objects) {
                    List<Market> markets_ = (List)markets.stream().filter(obj -> obj.getAmount() > 0 && object.equals(Integer.valueOf(obj.getStallMenuSerial())) && obj.getCategory() == category && exact > obj.getStartTime() + obj.getPublicityTime() && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                    if (markets_.size() > 0) {
                        collect.addAll(markets_);
                    }
                    else {
                        continue;
                    }
                }
            }
            int min = Math.min(Math.max(2, BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf((double)collect.size() / 8.0))) / 2, 5);
            if (page > min) {
                MessagePusher.pushMessage(role, new RespMsg("摊位有限，其他商品在排队。"));
                return;
            }
            list2 = (List)collect.stream().sorted(Comparator.comparing(Market::getStartTime)).collect(Collectors.toList());
            if (sort == 0) {
                list2 = (List)collect.stream().sorted(Comparator.comparing(Market::getPrice)).collect(Collectors.toList());
            }
            else {
                list2 = (List)collect.stream().sorted(Comparator.comparing(Market::getPrice, Comparator.reverseOrder())).collect(Collectors.toList());
            }
        }
        RespStallList respStall = this.loadMarketQueryData(role, list2, page, exact, req.getString());
        MessagePusher.pushMessage(role, respStall);
        return;
    }
    
    public RespStallList loadMarketQueryData(Role role, List<Market> list, int page, int exact, String string) {
        Map<Integer, SysDataStall> stall = MarketDataPool.t_data_stall;
        List<Market> subList = PageUtil.subList(list, Integer.valueOf(page), Integer.valueOf(8));
        ArrayList<StallItem> objects = new ArrayList<>();
        for (Market market : subList) {
            StallItem item = new StallItem();
            item.setId(String.valueOf(market.getId()));
            item.setSerial(market.getSerial());
            item.setPrice(String.valueOf(market.getPrice()));
            item.setAmount(market.getAmount());
            item.setNum(market.getFocus().size());
            if (market.getFocus().contains(role.getRoleId())) {
                item.setFocus(1);
            }
            int publicityTime = market.getStartTime() + market.getPublicityTime();
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
            SysDataStall sysDataStall = (SysDataStall)stall.get(Integer.valueOf(market.getSerial()));
            String extra = this.specialExtra(market.getPara(), (int)sysDataStall.getIsItem());
            if (extra != null) {
                item.setExtra(JsonUtils.object2String(extra));
            }
            objects.add(item);
        }
        RespStallList respStall = new RespStallList();
        respStall.setTotalPage((short)BigDecimalUtil.decimalScaleRoundUpInt(Integer.valueOf(0), Double.valueOf((double)list.size() / 8.0)));
        respStall.setCurrPage((short)page);
        respStall.setList(objects);
        respStall.setClassify(string);
        return respStall;
    }
    
    public String specialExtra(String string, int type) {
        if (type == 4) {
            NutMap nutMap = new NutMap();
            RoleTianFu runt = (RoleTianFu)JsonUtils.string2Object(string, RoleTianFu.class);
            nutMap.setv("runt", Byte.valueOf(runt.getLevel()));
            return JsonUtils.object2String(nutMap);
        }
        return null;
    }
    
    public void removeGoods(Role role, ReqGeneralNotify req, boolean is) {
        if (is) {
            boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
            if (!clicker) {
                return;
            }
        }
        try {
            long id = (long)Long.valueOf(req.getStatus());
            int serial = (int)Integer.valueOf(req.getString());
            Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
            SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(serial));
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
                SysPet sysPet = (SysPet)PetDataPool.petMap.get(stall.getSerial());
                if (sysPet == null) {
                    return;
                }
                boolean flag = SpringUtils.getPetService().canGetPet(role, sysPet, false);
                if (!flag) {
                    return;
                }
            }
            else if (stall.getIsItem() == 4) {
                List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
                if (roleTianFus.size() >= 75) {
                    MessagePusher.pushMessage(role, new RespMsg("物品栏空间不足"));
                    return;
                }
            }
            List<Market> markets = (List)this.marketMap.get(stall.getStallMenuSerial());
            if (markets != null) {
                Market market = (Market)markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getSerial() == serial && obj.getId().equals(Long.valueOf(id)) && obj.getRoleId().equals(role.getRoleId())).findFirst().orElse(null);
                if (market != null) {
                    if (is && market.getStatus() == 1) {
                        MessagePusher.pushMessage(role, new RespMsg("物品已被冻结，暂无法取回"));
                        return;
                    }
                    int num = market.getAmount() - market.getSaleAmount();
                    if (num <= 0) {
                        this.logger.error("玩家：{}，撤摊取回剩余数量：{}", role.getName(), Integer.valueOf(num));
                        return;
                    }
                    markets.remove(market);
                    this.marketMap.put(stall.getStallMenuSerial(), markets);
                    market.setDelete(true);
                    Db4CommonService.getInstance().add2Queue(market);
                    if (stall.getIsItem() == 0 && market.getPara() != null) {
                        BagEquip equip = (BagEquip)JsonUtils.string2Object(market.getPara(), BagEquip.class);
                        RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("撤摊摊装备：%s", market.getPara()), role.getMarking());
                        equip.setPos(pos);
                        equip.setDelete(false);
                        this.bagService.addEquip(role, equip, false, "撤摊取回");
                    }
                    else if (stall.getIsItem() == 2 && market.getPara() != null) {
                        Pet pet = (Pet)JsonUtils.string2Object(market.getPara(), Pet.class);
                        if (pet == null) {
                            MessagePusher.pushMessage(role, new RespMsg("宠物异常，暂时无法处理"));
                            return;
                        }
                        pet.doAfterInit();
                        RecordThread.getInstance().add2Queue((long)role.getId(), (int)role.getRoleId(), (short)0, (byte)2, String.format("撤摊摊装备：%s", market.getPara()), role.getMarking());
                        pet.setDelete(false);
                        Map<Integer, Pet> map = this.petService.selectPet(role);
                        map.put(pet.getPetId(), pet);
                        SpringUtils.getPetService().loadPet(role, pet);
                        RoleUtils.toUpdateAssistList(role, pet, true);
                        pet.save();
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, new ArrayList<>()));
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, new ArrayList<>()));
                    }
                    else if (stall.getIsItem() == 3) {
                        this.cardService.addCard(role, Integer.valueOf(market.getSerial()), Integer.valueOf(num), Integer.valueOf(0), true, false);
                    }
                    else if (stall.getIsItem() == 4) {
                        if (market.getPara() == null) {
                            return;
                        }
                        RoleTianFu rune = (RoleTianFu)JsonUtils.string2Object(market.getPara(), RoleTianFu.class);
                        rune.setDelete(false);
                        (DataCache.ROLE_TIAN_FU.get(role.getId())).add(rune);
                        rune.save();
                        ((RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class)).roleTianFuList(role);
                    }
                    else {
                        if (num > market.getAmount()) {
                            this.logger.error("撤摊取回物品计算错误，摊位ID：{}, 出售物品编号 = {}，出售总数量 = {}，已经出售数量 = {}", new Object[] { market.getId(), Integer.valueOf(market.getSerial()), Short.valueOf(market.getAmount()), Short.valueOf(market.getSaleAmount()) });
                            return;
                        }
                        this.bagService.addGoods(role, market.getSerial(), num, 0, false, false, "撤摊取回");
                    }
                    MessagePusher.pushMessage(role, new RespMsg("取回成功"));
                    this.sendMarketList(role);
                }
            }
        }
        catch (Exception e) {
            this.logger.error(e.getMessage());
        }
    }
    
    public void restartStall(Role role, ReqGeneralNotify req) {
        if (!this.commonVerify(role)) {
            return;
        }
        long id = (long)Long.valueOf(req.getStatus());
        String[] split = req.getString().split("-");
        if (split.length < 2) {
            return;
        }
        long price = (long)Long.valueOf(split[0]);
        short amount = (short)Short.valueOf(split[1]);
        if (amount > 32000 || amount <= 0 || price <= 0L) {
            return;
        }
        if (price < 10L) {
            return;
        }
        List<Market> list = this.queryMyMarket(role);
        Market market = (Market)list.stream().filter(obj -> obj.getId().equals(Long.valueOf(id)) && obj.getRoleId().equals(role.getRoleId())).findFirst().orElse(null);
        if (market == null) {
            return;
        }
        if (market.getStatus() == 1) {
            MessagePusher.pushMessage(role, new RespMsg("物品已被冻结，暂无法操作"));
            return;
        }
        int num = market.getAmount() - market.getSaleAmount();
        if (amount > num) {
            return;
        }
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(market.getSerial()));
        if (stall == null) {
            this.logger.error("重新上架,物品不可上架编号为：{}", Integer.valueOf(market.getSerial()));
            MessagePusher.pushMessage(role, new RespMsg("该物品不可上架交易"));
            return;
        }
        CheckStall checkStall = new CheckStall();
        if (stall.getIsItem() == 2 && market.getPara() != null) {
            Pet pet = (Pet)JsonUtils.string2Object(market.getPara(), Pet.class);
            if (pet == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return;
            }
            checkStall.setPet(pet);
        }
        else if (stall.getIsItem() == 0 && market.getPara() != null) {
            BagEquip equip = (BagEquip)JsonUtils.string2Object(market.getPara(), BagEquip.class);
            if (equip == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return;
            }
            checkStall.setEquip(equip);
        }
        else if (stall.getIsItem() == 4 && market.getPara() != null) {
            RoleTianFu rune = (RoleTianFu)JsonUtils.string2Object(market.getPara(), RoleTianFu.class);
            if (rune == null) {
                MessagePusher.pushMessage(role, new RespMsg("物品信息错误，请稍后再试"));
                return;
            }
            checkStall.setRune(rune);
        }
        if (this.checkStall(role, data_stall, checkStall, market.getSerial(), price)) {
            List<Market> markets = (List)this.marketMap.get(stall.getStallMenuSerial());
            if (markets == null) {
                return;
            }
            markets.remove(market);
            market.setPrice((long)Long.valueOf(price));
            market.setMarketPrice((long)Long.valueOf((long)stall.getBasicsPrice()));
            market.setStartTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
            if ((int)stall.getPublicityPeriod() > 0) {
                market.setIsPublicity(1);
                market.setPublicityTime((int)stall.getPublicityPeriod());
            }
            market.setExpireTime((int)stall.getExpireTime());
            market.setAmount(amount);
            markets.add(market);
            this.marketMap.put(stall.getStallMenuSerial(), markets);
            Db4CommonService.getInstance().add2Queue(market);
            this.sendMarketList(role);
        }
        return;
    }
    
    public void focus(Role role, ReqGeneralNotify req) {
        if (!this.commonVerify(role)) {
            return;
        }
        long id = Long.parseLong(req.getStatus());
        int serial = Integer.parseInt(req.getString());
        Map<Integer, SysDataStall> data_stall = MarketDataPool.t_data_stall;
        SysDataStall stall = (SysDataStall)data_stall.get(Integer.valueOf(serial));
        if (stall == null) {
            MessagePusher.pushMessage(role, new RespMsg("未找到该物品信息"));
            return;
        }
        List<Market> markets = (List)this.marketMap.get(stall.getStallMenuSerial());
        if (markets != null) {
            int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
            Market market = (Market)markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getSerial() == serial && obj.getId().equals(Long.valueOf(id)) && obj.getSaleAmount() == 0 && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).findFirst().orElse(null);
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
                    if (e.getId().equals(Long.valueOf(id))) {
                        e.setFocus(market.getFocus());
                        return;
                    }
                    return;
                });
                this.marketMap.put(stall.getStallMenuSerial(), markets);
            }
        }
        return;
    }
    
    private List<Market> queryMyMarket(Role role) {
        List<Market> result = new ArrayList<>();
        for (Integer key : this.marketMap.keySet()) {
            List<Market> markets = (List)this.marketMap.get(key);
            if (markets != null) {
                for (int i = 0; i < markets.size(); ++i) {
                    Market market = (Market)markets.get(i);
                    if (market != null && market.getRoleId().equals(role.getRoleId())) {
                        result.add(market);
                    }
                }
            }
            else {
                continue;
            }
        }
        return result;
    }
    
    public void columbia(Role role, ReqGeneralNotify req) {
        if (!this.commonVerify(role)) {
            return;
        }
        String[] split = req.getString().split("-");
        if (split.length < 2) {
            return;
        }
        int stallSerial = (int)Integer.valueOf(split[0]);
        int category = (int)Integer.valueOf(split[1]);
        if (category <= 0) {
            return;
        }
        List<Market> markets = (List)this.marketMap.get(Integer.valueOf(stallSerial));
        if (markets != null) {
            int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
            List<Market> collect = (List)markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getCategory() == category && exact > obj.getStartTime() + obj.getPublicityTime() && obj.getSaleAmount() == 0 && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
            List<Market> list = (List)collect.stream().sorted(Comparator.comparing(Market::getStartTime).thenComparing(Market::getPrice)).collect(Collectors.toList());
            List<Market> subList = PageUtil.subList(list, Integer.valueOf(1), Integer.valueOf(list.size()));
            long averaging = ((Double)list.stream().collect(Collectors.averagingLong(Market::getPrice))).longValue();
            List<Columbia> objects = new ArrayList<>();
            for (Market market : subList) {
                Columbia c = new Columbia(String.valueOf(market.getId()), market.getSerial(), String.valueOf(market.getPrice()));
                objects.add(c);
            }
            RespColumbia columbia = new RespColumbia();
            columbia.setPara(req.getString());
            columbia.setPrice(String.valueOf(averaging));
            columbia.setList(objects);
            MessagePusher.pushMessage(role, columbia);
        }
        return;
    }
    
    public void takeCash(Role role, ReqGeneralNotify req) {
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        Long id = Long.valueOf(req.getStatus());
        List<Market> list = this.queryMyMarket(role);
        Market market = (Market)list.stream().filter(obj -> obj.getId().equals(id)).findFirst().orElse(null);
        if (market != null && market.getSaleAmount() > 0) {
            if (market.getStatus() == 1) {
                MessagePusher.pushMessage(role, new RespMsg("物品已被冻结，暂无法提现"));
                return;
            }
            SysDataStall stall = (SysDataStall)MarketDataPool.t_data_stall.get(Integer.valueOf(market.getSerial()));
            if (stall == null) {
                return;
            }
            if (market.getAuditTime() > 0) {
                int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
                int auditTime = market.getAuditTime() + (int)stall.getAuditTime();
                if (exact < auditTime) {
                    MessagePusher.pushMessage(role, new RespMsg("审核中不可提现"));
                    return;
                }
            }
            List<Market> markets = (List)this.marketMap.get(Integer.valueOf(market.getStallMenuSerial()));
            long price = (long)market.getSaleAmount() * market.getPrice();
            markets.remove(market);
            if (market.getAmount() <= 0) {
                market.setDelete(true);
            }
            else {
                market.setSaleAmount((short)0);
                market.setAuditTime(0);
                markets.add(market);
            }
            this.roleService.addCurrency(role, stall.getType(), Long.valueOf(BigDecimalUtil.decimalScaleDown(Integer.valueOf(0), Double.valueOf((double)price - (double)price * stall.getRevenueRatio())).longValue()), "交易：物品编号：" + market.getSerial(), true, false);
            this.marketMap.put(Integer.valueOf(market.getStallMenuSerial()), markets);
            this.sendMarketList(role);
            Db4CommonService.getInstance().add2Queue(market);
        }
        return;
    }
    
    public synchronized void buyGoods(IoSession session, ReqBuyFromStall req) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        boolean limit = this.marketLimit(role);
        if (!limit) {
            return;
        }
        boolean clicker = this.roleService.mouserClicker(role, "buy", 250L);
        if (!clicker) {
            return;
        }
        short amount = req.getAmount();
        if (req.getAmount() > 32000 || req.getAmount() <= 0) {
            return;
        }
        try {
            if (!this.commonVerify(role)) {
                return;
            }
            String[] split = req.getPara().split("-");
            if (split.length < 2) {
                return;
            }
            int stallSerial = (int)Integer.valueOf(split[0]);
            int category = (int)Integer.valueOf(split[1]);
            long id = (long)Long.valueOf(req.getId());
            int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
            List<Market> markets = (List)this.marketMap.get(Integer.valueOf(stallSerial));
            if (markets != null) {
                List<Market> collect = (List)markets.stream().filter(obj -> obj.getAmount() > 0 && obj.getCategory() == category && obj.getStallMenuSerial() == stallSerial && exact > obj.getStartTime() + obj.getPublicityTime() && exact < obj.getStartTime() + obj.getPublicityTime() + obj.getExpireTime() && obj.getStatus() == 2).collect(Collectors.toList());
                List<Market> list = (List)collect.stream().sorted(Comparator.comparing(Market::getStartTime).thenComparing(Market::getPrice)).collect(Collectors.toList());
                list = (List)collect.stream().sorted(Comparator.comparing(Market::getPrice)).collect(Collectors.toList());
                Market market = (Market)list.stream().filter(obj -> obj.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
                if (market == null) {
                    MessagePusher.pushMessage(session, new RespMsg("您购买的物品不存在或已下架"));
                    return;
                }
                SysDataStall stall = (SysDataStall)MarketDataPool.t_data_stall.get(Integer.valueOf(market.getSerial()));
                if (stall == null) {
                    return;
                }
                if (market.getAmount() <= 0) {
                    MessagePusher.pushMessage(session, new RespMsg("您购买的物品不存在或已下架"));
                    return;
                }
                if (amount > 0) {
                    boolean b = this.roleService.checkCurrency(role, stall.getType(), (long)req.getAmount() * market.getPrice());
                    if (!b) {
                        return;
                    }
                    if (Deploy.market_limit) {
                        long already = role.getTaskProgress().getEveryDayUpperLimit().getLong("market", 0L);
                        long quota = getQuota(role.getTc());
                        if (already + (long)req.getAmount() * market.getPrice() > quota) {
                            MessagePusher.pushMessage(role, new RespMsg("今日可交易金额剩余：" + MoneyUtil.colours(Long.valueOf(quota - already))));
                            return;
                        }
                        role.getTaskProgress().getEveryDayUpperLimit().setv("market", Long.valueOf(already + (long)req.getAmount() * market.getPrice()));
                    }
                    if (stall.getIsItem() == 1 || stall.getIsItem() == 3) {
                        long economize = 0L;
                        long price = 0L;
                        if (stall.getIsItem() == 1) {
                            boolean checkPos = this.bagService.checkPos(role, market.getSerial(), (int)amount);
                            if (!checkPos) {
                                return;
                            }
                        }
                        List<MarketDealRecord> objects = new ArrayList<>();
                        int sum = 0;
                        int quantity = 0;
                        for (Market m : list) {
                            if (m.getAmount() <= 0) {
                                continue;
                            }
                            int num = (sum + m.getAmount() > amount) ? (amount - sum) : m.getAmount();
                            if (market.getPrice() > m.getPrice()) {
                                economize += (market.getPrice() - m.getPrice()) * (long)num;
                                quantity += num;
                            }
                            short mAmount = m.getAmount();
                            int residue = Math.max(0, mAmount - num);
                            m.setAmount((short)residue);
                            int buyNum = (residue == 0) ? mAmount : num;
                            if (buyNum < 0) {
                                this.logger.error("购买物品数量出错--------------------------------------------------------");
                                return;
                            }
                            sum += buyNum;
                            m.setSaleAmount((short)(m.getSaleAmount() + buyNum));
                            m.setAuditTime(Math.toIntExact(System.currentTimeMillis() / 1000L));
                            Db4CommonService.getInstance().add2Queue(m);
                            objects.add(new MarketDealRecord(m, num, new IdWorker().nextId(), (int)role.getRoleId()));
                            this.updateMarket(markets, stallSerial, market);
                            price += m.getPrice() * (long)num;
                            if (sum >= amount) {
                                break;
                            }
                            continue;
                        }
                        if (sum <= 0) {
                            this.logger.error("购买物品数量异常：{}", Integer.valueOf(sum));
                            return;
                        }
                        MessagePusher.pushMessage(session, new RespMsg("购买成功"));
                        this.roleService.deductCurrency(role, stall.getType(), price, "购买物品");
                        if (stall.getIsItem() == 1) {
                            this.bagService.addGoods(role, market.getSerial(), sum, TimeUtil.dayInMillis(TimeUtil.addition(Integer.valueOf(30))), true, true, "集市购买物品");
                        }
                        else if (stall.getIsItem() == 3) {
                            this.cardService.addCard(role, Integer.valueOf(market.getSerial()), Integer.valueOf(sum), Integer.valueOf(TimeUtil.dayInMillis(TimeUtil.addition(Integer.valueOf(30)))), true, true);
                        }
                        this.marketMap.put(Integer.valueOf(stallSerial), markets);
                        if (economize > 0L) {
                            MessagePusher.pushMessage(session, new RespMsg(String.format("已自动购买#<LG1>%d#个同类的低价商品，节省#<IR2>#%s银两", Integer.valueOf(quantity), MoneyUtil.colours(Long.valueOf(economize)))));
                        }
                        for (MarketDealRecord object : objects) {
                            Db4CommonService.getInstance().add2Queue(object);
                        }
                    }
                    else if (stall.getIsItem() == 0) {
                        int pos = this.bagService.getPos(role, false);
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
                        BagEquip equip = (BagEquip)JsonUtils.string2Object(market.getPara(), BagEquip.class);
                        equip.setPos(pos);
                        equip.setDelete(false);
                        equip.setRId((long)role.getId());
                        equip.setBd(TimeUtil.dayInMillis(TimeUtil.addition(Integer.valueOf(30))));
                        equip.setArtificeGeneralHistory(new ArrayList<>());
                        equip.setArtificeHighHistory(new ArrayList<>());
                        equip.setArtificeSuperHistory(new ArrayList<>());
                        equip.setRedoGeneralHistory(new ArrayList<>());
                        equip.setRedoHighHistory(new ArrayList<>());
                        equip.setRedoSuperHistory(new ArrayList<>());
                        equip.setPurifyGeneralHistory(new ArrayList<>());
                        equip.setPurifyHighHistory(new ArrayList<>());
                        equip.setPurifySuperHistory(new ArrayList<>());
                        this.bagService.addEquip(role, equip, false, "集市购买");
                        Db4CommonService.getInstance().add2Queue(new MarketDealRecord(market, 1, new IdWorker().nextId(), (int)role.getRoleId()));
                    }
                    else if (stall.getIsItem() == 2) {
                        SysPet sysPet = (SysPet)PetDataPool.petMap.get(stall.getSerial());
                        if (sysPet == null) {
                            return;
                        }
                        boolean flag = SpringUtils.getPetService().canGetPet(role, sysPet, false);
                        if (!flag) {
                            return;
                        }
                        Pet pet = (Pet)JsonUtils.string2Object(market.getPara(), Pet.class);
                        if (pet == null) {
                            this.logger.error("购买宠物转换数据为空，交易编号 = {}, PET = {}", req.getId(), market.getPara());
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
                        pet.setBindTime(TimeUtil.dayInMillis(TimeUtil.addition(Integer.valueOf(30))));
                        Map<Integer, Pet> map = this.petService.selectPet(role);
                        map.put(pet.getPetId(), pet);
                        pet.save();
                        RoleUtils.toUpdateAssistList(role, pet, true);
                        role.save();
                        SpringUtils.getPetService().loadPet(role, pet);
                        MessagePusher.pushMessage(session, new RespMsg("购买成功"));
                        Db4CommonService.getInstance().add2Queue(new MarketDealRecord(market, 1, new IdWorker().nextId(), (int)role.getRoleId()));
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_CHANGE, pet, new ArrayList<>()));
                        EventDispatcher.getInstance().fireEvent(new PetChangeEvent(EventType.PET_SCORE_CHANGE, pet, new ArrayList<>()));
                    }
                    else if (stall.getIsItem() == 4) {
                        this.buyRune(role, markets, market, stall, stallSerial);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            this.logger.error("购买物品出错，-------------------------------------------------------");
        }
        return;
    }
    
    public void buyRune(Role role, List<Market> markets, Market market, SysDataStall stall, int stallSerial) {
        List<RoleTianFu> roleTianFus = (List)(DataCache.ROLE_TIAN_FU.get(role.getId())).stream().filter(obj -> obj.getIsWear() == 0 && obj.getStorePos() == 0).collect(Collectors.toList());
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
        RoleTianFu rune = (RoleTianFu)JsonUtils.string2Object(market.getPara(), RoleTianFu.class);
        rune.setRoleId((long)role.getId());
        rune.setDelete(false);
        rune.setBd(TimeUtil.dayInMillis(TimeUtil.addition(Integer.valueOf(60))));
        (DataCache.ROLE_TIAN_FU.get(role.getId())).add(rune);
        rune.save();
        ((RoleTianFuService)SpringUtils.getBean(RoleTianFuService.class)).roleTianFuList(role);
        Db4CommonService.getInstance().add2Queue(new MarketDealRecord(market, 1, new IdWorker().nextId(), (int)role.getRoleId()));
        return;
    }
    
    public void updateMarket(List<Market> markets, int stallSerial, Market market) {
        markets.stream().map(p -> {
            if (p.getId().equals(market.getId())) {
                p.setAmount(market.getAmount());
                p.setSaleAmount(market.getSaleAmount());
                p.setAuditTime(market.getAuditTime());
                return p;
            }
            return p;
        }).collect(Collectors.toList());
        this.marketMap.put(Integer.valueOf(stallSerial), markets);
    }
    
    public void goodsInfo(IoSession session, ReqGoodInfo req) {
        Role role = SessionUtils.getRoleBySession(session);
        if (role == null) {
            return;
        }
        List<Market> markets = (List)this.marketMap.get(Integer.valueOf((int)req.getSerial()));
        if (markets != null) {
            Long id = Long.valueOf(req.getId());
            Market market = (Market)markets.stream().filter(obj -> obj.getId().equals(id) && obj.getStatus() == 2).findFirst().orElse(null);
            if (market != null) {
                SysDataStall stall = (SysDataStall)MarketDataPool.t_data_stall.get(Integer.valueOf(market.getSerial()));
                if (stall != null) {
                    int[] variables = this.getVariables(market);
                    int status = variables[0];
                    int endTime = variables[1];
                    if (stall.getIsItem() == 0) {
                        BagEquip equip = (BagEquip)JsonUtils.string2Object(market.getPara(), BagEquip.class);
                        this.equipDetail(role, equip, market, status, endTime);
                    }
                    else if (stall.getIsItem() != 1 && stall.getIsItem() == 2) {
                        Pet pet = (Pet)JsonUtils.string2Object(market.getPara(), Pet.class);
                        if (pet == null) {
                            this.logger.error("宠物交易详情转换数据为空，交易编号 = {}，分类编号 = {}, PET = {}", new Object[] { req.getId(), Short.valueOf(req.getSerial()), market.getPara() });
                            return;
                        }
                        pet.doAfterInit();
                        this.petDetail(role, pet, market, status, endTime);
                    }
                }
            }
        }
        return;
    }
    
    public void petDetail(Role role, Pet pet, Market market, int status, int endTime) {
        RespMarketPetCard card = new RespMarketPetCard();
        card.setId(String.valueOf(market.getId()));
        card.setRoleId((int)market.getRoleId());
        card.setSerial(market.getSerial());
        card.setSkills(pet.getSkills());
        card.setStatus((byte)status);
        card.setEndTime(endTime);
        Role ownerRole = this.roleService.getPlayer((int)market.getRoleId());
        card.setFields(this.petService.cardPetFied(ownerRole, pet, new ArrayList<>()));
        List<FieldProp> fieldProps = this.petService.petFieldPro(ownerRole, pet);
        if (fieldProps != null) {
            card.setProList(fieldProps);
        }
        MessagePusher.pushMessage(role, card);
    }
    
    public void equipDetail(Role role, BagEquip equip, Market market, int status, int endTime) {
        RespMarketGoodCard card = new RespMarketGoodCard(String.valueOf(market.getId()), (int)market.getRoleId(), market.getSerial(), equip.getField(), equip.getArtifice());
        card.setStatus((byte)status);
        card.setEndTime(endTime);
        MessagePusher.pushMessage(role, card);
    }
    
    private int[] getVariables(Market market) {
        int exact = Math.toIntExact(System.currentTimeMillis() / 1000L);
        int status = 1;
        int endTime = exact;
        int publicityTime = market.getStartTime() + market.getPublicityTime();
        if (exact < publicityTime) {
            status = 2;
            endTime = publicityTime;
        }
        else {
            int time = publicityTime + market.getExpireTime();
            if (exact > time) {
                status = 3;
            }
            else {
                status = 1;
            }
        }
        int[] objs = { status, endTime };
        return objs;
    }
    
    public String updateMarketStatus(int classify, long id, byte status) {
        List<Market> markets = (List)this.marketMap.get(Integer.valueOf(classify));
        if (markets == null) {
            return "无此分类";
        }
        Market market = (Market)markets.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (market == null) {
            return "未找到商品或已下架";
        }
        market.setStatus(status);
        Db4CommonService.getInstance().add2Queue(market);
        return null;
    }
    
    public String withdraw(int classify, long id) {
        List<Market> markets = (List)this.marketMap.get(Integer.valueOf(classify));
        if (markets == null) {
            return "无此分类";
        }
        Market market = (Market)markets.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (market == null) {
            return "未找到商品或已下架";
        }
        Role role = this.roleService.getOnlinePlayer((int)market.getRoleId());
        if (role == null) {
            return "未找到玩家或玩家不在线";
        }
        ReqGeneralNotify notify = new ReqGeneralNotify();
        notify.setStatus(String.valueOf(id));
        notify.setString(String.valueOf(market.getSerial()));
        this.removeGoods(role, notify, false);
        return null;
    }
    
    public String deleteMarket(int classify, long id) {
        List<Market> markets = (List)this.marketMap.get(Integer.valueOf(classify));
        if (markets == null) {
            return "无此分类";
        }
        Market market = (Market)markets.stream().filter(e -> e.getId().equals(Long.valueOf(id))).findFirst().orElse(null);
        if (market == null) {
            return "未找到商品或已下架";
        }
        markets.remove(market);
        market.setDelete(true);
        Db4CommonService.getInstance().add2Queue(market);
        return null;
    }
    
    public boolean marketLimit(Role role) {
        if (!SafeUtil.isUnlock(role)) {
            return false;
        }
        if (((ServerService)SpringUtils.getBean(ServerService.class)).getType() == 1) {
            String msg = DataGlobalFunc.needGradeJudge(Deploy.market_need_relive, Deploy.market_need_level, (int)role.getRelive(), (int)role.getLevel(), "你的#<R>角色等级#不足");
            if (msg != null) {
                MessagePusher.pushMessage(role, new RespMsg(msg));
                return false;
            }
            Message message = DataGlobalFunc.operationalConstraint(role, Deploy.market, "充值#<R>%d#才可以操作集市！");
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
    
    public static long getQuota(int tc) {
        int max = Math.max(0, tc - Deploy.market);
        return (long)(max / 100) * 800000000L + 4000000000L;
    }
}
