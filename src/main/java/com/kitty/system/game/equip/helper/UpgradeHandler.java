
package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.mina.message.Message;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.vo.FiedEquip;
import com.kitty.system.common.enter.FiedValue;
import java.util.ArrayList;
import com.kitty.system.game.equip.message.RespUpgradePreview;
import java.util.Map;
import java.util.Iterator;
import java.util.List;
import java.util.ConcurrentModificationException;
import com.kitty.logs.LoggerUtils;
import com.kitty.common.utils.bean.BeanUtils;
import com.kitty.system.game.equip.domain.WearEquip;
import com.kitty.system.game.bag.domain.BagEquip;
import com.kitty.system.game.equip.helper.vo.EquipInfo;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.chat.service.ChatService;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.equip.service.EquipService;
import com.kitty.common.thread.IdWorker;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class UpgradeHandler
{
    protected Logger logger;
    protected IdWorker worker;
    protected EquipService equipService;
    protected BagService bagService;
    protected RoleService roleService;
    protected ChatService chatService;
    static final String ROLE_VACANCY_NOT_ENOUGH = "很抱歉，你的包裹空位不足，请整理后再来吧。";
    static final String ERROR_TYPE = "操作的类型不正确";
    
    public UpgradeHandler() {
        this.logger = LoggerFunction.UPGRADE_EQUIP.getLogger();
        this.worker = SpringUtils.getBean(IdWorker.class);
        this.equipService = SpringUtils.getBean(EquipService.class);
        this.bagService = SpringUtils.getBean(BagService.class);
        this.roleService = SpringUtils.getRoleService();
        this.chatService = SpringUtils.getBean(ChatService.class);
    }
    
    public abstract String acceptUpgrade(final Role p0, final Integer p1, final Integer p2, final String p3, final String p4);
    
    int getVacancy(final Role role) {
        return this.bagService.getPos(role, false);
    }
    
    public EquipInfo getEquip(final Role role, final int pos, final Long id) {
        final EquipInfo equipInfo = new EquipInfo();
        BagEquip equip = new BagEquip();
        try {
            if (pos < 0) {
                boolean flag = true;
                final List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
                for (final WearEquip wearEquip : wearEquips) {
                    if (wearEquip != null && wearEquip.getId() != null && wearEquip.getId().equals(id)) {
                        flag = false;
                        BeanUtils.copyBeanProp(equip, wearEquip);
                    }
                }
                if (flag) {
                    equip = null;
                    equipInfo.setTips("身上没有穿戴该道具");
                }
            }
            else {
                final Map<Long, BagEquip> bagEquip = this.bagService.getBagEquip(role);
                equip = bagEquip.get(id);
                if (equip == null) {
                    equipInfo.setTips("背包没有该道具");
                }
            }
            if (equip != null && equip.getId() == null) {
                equip = null;
                equipInfo.setTips("获取装备信息错误");
                LoggerUtils.info("玩家[{}]获取装备信息错误：装备ID = {}，位置 = {}" + role.getRoleId(), id, pos);
            }
            equipInfo.setEquip(equip);
        }
        catch (final ConcurrentModificationException e) {
            e.getMessage();
        }
        return equipInfo;
    }
    
    public void refresh(final Role role, final BagEquip equip, final int pos, final Long id, final boolean flag) {
        try {
            if (pos < 0) {
                final List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
                final WearEquip wearEquip = new WearEquip();
                BeanUtils.copyBeanProp(wearEquip, equip);
                for (int i = 0; i < wearEquips.size(); ++i) {
                    final WearEquip wear = wearEquips.get(i);
                    if (wear.getId().equals(id)) {
                        wearEquips.set(i, wearEquip);
                        break;
                    }
                }
                wearEquip.save();
                if (flag) {
                    this.equipService.changeEquip(role, equip, wearEquip, true);
                }
            }
            else {
                final Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
                bagEquipMap.put(equip.getId(), equip);
                equip.save();
                if (flag) {
                    this.bagService.refreshRoleEquip(role, equip, false);
                }
            }
        }
        catch (final ConcurrentModificationException ex) {}
    }
    
    public void animation(final Role role, final BagEquip equip, final BagEquip newEq, final int level) {
        final RespUpgradePreview preview = new RespUpgradePreview();
        final List<FiedValue> listOne = new ArrayList<FiedValue>();
        for (final FiedEquip fiedEquip : equip.getField()) {
            listOne.add(new FiedValue(String.valueOf(fiedEquip.getType()), (byte)3, fiedEquip.getValue()));
        }
        final List<FiedValue> listTwo = new ArrayList<FiedValue>();
        for (final FiedEquip fiedEquip2 : newEq.getField()) {
            listTwo.add(new FiedValue(String.valueOf(fiedEquip2.getType()), (byte)3, fiedEquip2.getValue()));
        }
        preview.setListOne(listOne);
        preview.setListTwo(listTwo);
        preview.setLevel(level);
        preview.setName(newEq.getName());
        preview.setSerial(newEq.getType());
        MessagePusher.pushMessage(role, preview);
    }
    
    public int getPj(final int pj) {
        final String value = String.valueOf(pj);
        if (value.length() >= 2) {
            return Integer.valueOf(value.substring(1, 2));
        }
        return 0;
    }
    
    public int fieldSerial(final List<FiedEquip> field) {
        final int[] serial = { 0 };
        field.forEach(e -> {
            if (e.getType().equals(6680)) {
                serial[0] = e.getValue();
                return;
            }
            else {
                return;
            }
        });
        return serial[0];
    }
    
    public BagEquip clearHistory(final BagEquip equip) {
        equip.setRedoGeneralHistory(new ArrayList<FiedEquip>());
        equip.setRedoHighHistory(new ArrayList<List<FiedEquip>>());
        equip.setRedoSuperHistory(new ArrayList<List<FiedEquip>>());
        equip.setArtificeGeneralHistory(new ArrayList<FiedEquip>());
        equip.setArtificeHighHistory(new ArrayList<List<FiedEquip>>());
        equip.setArtificeSuperHistory(new ArrayList<List<FiedEquip>>());
        equip.setPurifyGeneralHistory(new ArrayList<FiedEquip>());
        equip.setPurifyHighHistory(new ArrayList<List<FiedEquip>>());
        equip.setPurifySuperHistory(new ArrayList<List<FiedEquip>>());
        return equip;
    }
    
    public FiedEquip getPurify(final List<FiedEquip> field, final int pj) {
        if (pj >= 5) {
            final List<SysPurifyAttr> list = EquipDataPool.purify;
            for (final FiedEquip fiedEquip : field) {
                final SysPurifyAttr purifyAttr = list.stream().filter(e -> fiedEquip.getType() - PRO_DICT_SERIAL.EQUIP_PRO.getSerial() == CONVERT_DICT.getDist(e.getDict())).findFirst().orElse(null);
                if (purifyAttr != null) {
                    return fiedEquip;
                }
            }
        }
        return null;
    }
    
    public int expend(final int type) {
        int count = 1;
        if (type == 2) {
            count = 5;
        }
        else if (type == 3) {
            count = 20;
        }
        return count;
    }
    
    public BagEquip remoldSave(final BagEquip equip, final List<List<FiedEquip>> redo, final int type) {
        if (redo.size() > 0) {
            if (type == 1) {
                equip.setRedoGeneralHistory(redo.get(0));
            }
            else if (type == 2) {
                equip.setRedoHighHistory(redo);
            }
            else if (type == 3) {
                equip.setRedoSuperHistory(redo);
            }
        }
        return equip;
    }
    
    public BagEquip remoldPurifySave(final BagEquip equip, final List<List<FiedEquip>> redo, final int type) {
        if (redo.size() > 0) {
            if (type == 1) {
                equip.setPurifyGeneralHistory(redo.get(0));
            }
            else if (type == 2) {
                equip.setPurifyHighHistory(redo);
            }
            else if (type == 3) {
                equip.setPurifySuperHistory(redo);
            }
        }
        return equip;
    }
    
    public BagEquip refineSave(final BagEquip equip, final List<List<FiedEquip>> refine, final int type) {
        if (refine.size() > 0) {
            if (type == 1) {
                equip.setArtificeGeneralHistory(refine.get(0));
            }
            else if (type == 2) {
                equip.setArtificeHighHistory(refine);
            }
            else if (type == 3) {
                equip.setArtificeSuperHistory(refine);
            }
        }
        return equip;
    }
    
    public boolean deduct(final Role role, final Map<Integer, Integer> consume) {
        for (final Integer key : consume.keySet()) {
            final Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                final boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, amount, "装备打造实现类");
                if (!b) {
                    return false;
                }
                continue;
            }
            else if (key.equals(Const.RESTYPE_COIN)) {
                final boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, amount, "装备打造实现类");
                if (!b) {
                    return false;
                }
                continue;
            }
            else {
                final Integer integer = this.bagService.consumeBagGoods(role, key, amount, true, true, "装备打造");
                if (integer != null) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
}
