package com.kitty.system.game.mind.helper;

import com.kitty.system.game.vo.utils.Const;
import java.util.Map;
import java.util.Iterator;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.mind.message.RespPreUpgradeMind;
import com.kitty.system.game.mind.helper.vo.MindNeo;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.mind.domain.Mind;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.mind.service.MindService;
import com.kitty.common.thread.IdWorker;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class UpgradeHandler
{
    protected Logger logger;
    protected IdWorker worker;
    protected MindService mindService;
    protected BagService bagService;
    protected RoleService roleService;
    static final int SL_LIMIT_GARDE = 1080;
    static final int LIMIT_GARDE = 58;
    static final int DF_LIMIT_GARDE = 2100;
    static final int ZJ_LIMIT_GARDE = 3138;
    static final String ERROR_TYPE = "操作的类型不正确";
    
    public UpgradeHandler() {
        this.logger = LoggerFunction.UPGRADE_EQUIP.getLogger();
        this.worker = SpringUtils.getBean(IdWorker.class);
        this.mindService = SpringUtils.getBean(MindService.class);
        this.bagService = SpringUtils.getBean(BagService.class);
        this.roleService = SpringUtils.getRoleService();
    }
    
    public abstract String acceptUpgrade(Role role, int serial, int type, String param, String cause);
    
    public Mind getMind(Role role, int serial) {
        List<Mind> list = this.mindService.getMind(role.getId());
        return list.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
    }
    
    public void refresh(Role role, Mind mind) {
        mind.save();
        this.mindService.refreshMind(role.getId(), mind);
    }
    
    public void mindPreRefine(Role role, Mind mind, byte type) {
        List<MindNeo> list = new ArrayList<>();
        if (type == 1) {
            if (mind.getRefineGeneralHistory() != null && mind.getRefineGeneralHistory().size() > 0) {
                list.add(new MindNeo(mind.getRefineGeneralHistory()));
            }
        }
        else if (type == 2) {
            List<List<FieldProp>> history = mind.getRefineHighHistory();
            if (history != null && history.size() > 0) {
                for (List<FieldProp> fields : history) {
                    list.add(new MindNeo(fields));
                }
            }
        }
        else if (type == 3) {
            List<List<FieldProp>> superHistory = mind.getRefineSuperHistory();
            if (superHistory != null && superHistory.size() > 0) {
                for (List<FieldProp> fields : superHistory) {
                    list.add(new MindNeo(fields));
                }
            }
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeMind(mind.getSerial(), list, type));
    }
    
    public boolean deduct(Role role, Map<Integer, Integer> consume) {
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, amount, "心法打造实现类");
                if (!b) {
                    return false;
                }
            }
            else if (key.equals(Const.RESTYPE_COIN)) {
                boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, amount, "心法打造实现类");
                if (!b) {
                    return false;
                }
            }
            else if (key.equals(Const.RES_TYPE_CHARGE_SCORE)) {
                boolean b = this.roleService.deductCurrency(role, Const.RES_TYPE_CHARGE_SCORE, (int)amount, "心法打造实现类");
                if (!b) {
                    return false;
                }
            }
            else {
                Integer integer = this.bagService.consumeBagGoods(role, key, amount, true, true, "心法打造");
                if (integer != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
