package com.kitty.system.game.equip.helper;

import com.kitty.system.game.vo.utils.Const;
import com.kitty.system.enums.war.itempro.def.CONVERT_DICT;
import com.kitty.system.enums.war.itempro.def.PRO_DICT_SERIAL;
import com.kitty.system.init.domain.SysPurifyAttr;
import com.kitty.system.game.equip.pool.EquipDataPool;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.common.enter.FiedValue;
import com.kitty.system.game.vo.FiedEquip;
import java.util.ArrayList;
import com.kitty.system.game.equip.message.RespUpgradePreview;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
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
        this.worker = (IdWorker)SpringUtils.getBean(IdWorker.class);
        this.equipService = (EquipService)SpringUtils.getBean(EquipService.class);
        this.bagService = (BagService)SpringUtils.getBean(BagService.class);
        this.roleService = SpringUtils.getRoleService();
        this.chatService = (ChatService)SpringUtils.getBean(ChatService.class);
    }
    
    public abstract String acceptUpgrade(Role role, Integer upgradePos, Integer type, String param, String cause);
    
    int getVacancy(Role role) {
        return this.bagService.getPos(role, false);
    }
    
    public EquipInfo getEquip(Role role, int pos, Long id) {
        EquipInfo equipInfo = new EquipInfo();
        BagEquip equip = new BagEquip();
        try {
            if (pos < 0) {
                boolean flag = true;
                List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
                for (WearEquip wearEquip : wearEquips) {
                    if (wearEquip != null && wearEquip.getId() != null && wearEquip.getId().equals(id)) {
                        flag = false;
                        BeanUtils.copyBeanProp(equip, wearEquip);
                    }
                    else {
                        continue;
                    }
                }
                if (flag) {
                    equip = null;
                    equipInfo.setTips("身上没有穿戴该道具");
                }
            }
            else {
                Map<Long, BagEquip> bagEquip = this.bagService.getBagEquip(role);
                equip = (BagEquip)bagEquip.get(id);
                if (equip == null) {
                    equipInfo.setTips("背包没有该道具");
                }
            }
            if (equip != null && equip.getId() == null) {
                equip = null;
                equipInfo.setTips("获取装备信息错误");
                LoggerUtils.info("玩家[{}]获取装备信息错误：装备ID = {}，位置 = {}" + role.getRoleId(), id, Integer.valueOf(pos));
            }
            equipInfo.setEquip(equip);
        }
        catch (ConcurrentModificationException e) {
            e.getMessage();
        }
        return equipInfo;
    }
    
    public void refresh(Role role, BagEquip equip, int pos, Long id, boolean flag) {
        try {
            if (pos < 0) {
                List<WearEquip> wearEquips = this.equipService.selectWearEquip(role);
                WearEquip wearEquip = new WearEquip();
                BeanUtils.copyBeanProp(wearEquip, equip);
                int i = 0;
                while (i < wearEquips.size()) {
                    WearEquip wear = (WearEquip)wearEquips.get(i);
                    if (wear.getId().equals(id)) {
                        wearEquips.set(i, wearEquip);
                        break;
                    }
                    ++i;
                }
                wearEquip.save();
                if (flag) {
                    this.equipService.changeEquip(role, equip, wearEquip, true);
                }
            }
            else {
                Map<Long, BagEquip> bagEquipMap = this.bagService.getBagEquip(role);
                bagEquipMap.put(equip.getId(), equip);
                equip.save();
                if (flag) {
                    this.bagService.refreshRoleEquip(role, equip, false);
                }
            }
        }
        catch (ConcurrentModificationException ex) {}
    }
    
    public void animation(Role role, BagEquip equip, BagEquip newEq, int level) {
        RespUpgradePreview preview = new RespUpgradePreview();
        List<FiedValue> listOne = new ArrayList<>();
        for (FiedEquip fiedEquip : equip.getField()) {
            listOne.add(new FiedValue(String.valueOf(fiedEquip.getType()), (byte)3, fiedEquip.getValue()));
        }
        List<FiedValue> listTwo = new ArrayList<>();
        for (FiedEquip fiedEquip2 : newEq.getField()) {
            listTwo.add(new FiedValue(String.valueOf(fiedEquip2.getType()), (byte)3, fiedEquip2.getValue()));
        }
        preview.setListOne(listOne);
        preview.setListTwo(listTwo);
        preview.setLevel(level);
        preview.setName(newEq.getName());
        preview.setSerial(newEq.getType());
        MessagePusher.pushMessage(role, preview);
    }
    
    public int getPj(int pj) {
        String value = String.valueOf(pj);
        if (value.length() >= 2) {
            return (int)Integer.valueOf(value.substring(1, 2));
        }
        return 0;
    }
    
    public int fieldSerial(List<FiedEquip> field) {
        int[] serial = { 0 };
        field.forEach(e -> {
            if (e.getType().equals(Integer.valueOf(6680))) {
                serial[0] = (int)e.getValue();
                return;
            }
            return;
        });
        return serial[0];
    }
    
    public BagEquip clearHistory(BagEquip equip) {
        equip.setRedoGeneralHistory(new ArrayList<>());
        equip.setRedoHighHistory(new ArrayList<>());
        equip.setRedoSuperHistory(new ArrayList<>());
        equip.setArtificeGeneralHistory(new ArrayList<>());
        equip.setArtificeHighHistory(new ArrayList<>());
        equip.setArtificeSuperHistory(new ArrayList<>());
        equip.setPurifyGeneralHistory(new ArrayList<>());
        equip.setPurifyHighHistory(new ArrayList<>());
        equip.setPurifySuperHistory(new ArrayList<>());
        return equip;
    }
    
    public FiedEquip getPurify(List<FiedEquip> field, int pj) {
        if (pj >= 5) {
            List<SysPurifyAttr> list = EquipDataPool.purify;
            for (FiedEquip fiedEquip : field) {
                SysPurifyAttr purifyAttr = (SysPurifyAttr)list.stream().filter(e -> (int)fiedEquip.getType() - PRO_DICT_SERIAL.EQUIP_PRO.getSerial() == CONVERT_DICT.getDist(e.getDict())).findFirst().orElse(null);
                if (purifyAttr != null) {
                    return fiedEquip;
                }
                continue;
            }
        }
        return null;
    }
    
    public BagEquip remoldSave(BagEquip equip, List<List<FiedEquip>> redo, int type) {
        if (redo.size() > 0) {
            if (type == 1) {
                equip.setRedoGeneralHistory((List)redo.get(0));
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
    
    public BagEquip remoldPurifySave(BagEquip equip, List<List<FiedEquip>> redo, int type) {
        if (redo.size() > 0) {
            if (type == 1) {
                equip.setPurifyGeneralHistory((List)redo.get(0));
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
    
    public BagEquip refineSave(BagEquip equip, List<List<FiedEquip>> refine, int type) {
        if (refine.size() > 0) {
            if (type == 1) {
                equip.setArtificeGeneralHistory((List)refine.get(0));
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
    
    public boolean deduct(Role role, Map<Integer, Integer> consume) {
        for (Integer key : consume.keySet()) {
            Integer amount = (Integer)consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, (long)(int)amount, "装备打造实现类");
                if (!b) {
                    return false;
                }
                continue;
            }
            else if (key.equals(Const.RESTYPE_COIN)) {
                boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, (long)(int)amount, "装备打造实现类");
                if (!b) {
                    return false;
                }
                continue;
            }
            else {
                Integer integer = this.bagService.consumeBagGoods(role, (int)key, (int)amount, true, true, "装备打造");
                if (integer != null) {
                    return false;
                }
                continue;
            }
        }
        return true;
    }
}
