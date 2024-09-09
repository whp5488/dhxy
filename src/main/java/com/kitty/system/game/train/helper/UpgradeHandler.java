package com.kitty.system.game.train.helper;

import com.kitty.system.game.vo.utils.Const;
import java.util.Iterator;
import com.kitty.system.game.train.domain.vo.FieldSeal;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.train.message.RespPreUpgradeSeal;
import com.kitty.system.game.train.helper.vo.SealNeo;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import com.kitty.system.game.train.domain.TrainSeal;
import com.kitty.system.game.role.domain.Role;
import com.kitty.common.utils.spring.SpringUtils;
import com.kitty.logs.LoggerFunction;
import com.kitty.system.game.role.service.RoleService;
import com.kitty.system.game.bag.service.BagService;
import com.kitty.system.game.train.TrainService;
import com.kitty.common.thread.IdWorker;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

@Component
public abstract class UpgradeHandler
{
    protected Logger logger;
    protected IdWorker worker;
    protected TrainService trainService;
    protected BagService bagService;
    protected RoleService roleService;
    static final String ERROR_TYPE = "操作的类型不正确";
    
    public UpgradeHandler() {
        this.logger = LoggerFunction.UPGRADE_EQUIP.getLogger();
        this.worker = SpringUtils.getBean(IdWorker.class);
        this.trainService = SpringUtils.getBean(TrainService.class);
        this.bagService = SpringUtils.getBean(BagService.class);
        this.roleService = SpringUtils.getRoleService();
    }
    
    public abstract String acceptUpgrade(Role role, Long id, Integer type, String param, String cause);
    
    public TrainSeal getSeal(Role role, long id) {
        Map<Long, TrainSeal> sealMap = this.trainService.getTrainSeal(role.getId());
        if (sealMap == null || !sealMap.containsKey(id)) {
            return null;
        }
        return sealMap.get(id);
    }
    
    public void refresh(Role role, TrainSeal seal, byte type) {
        seal.save();
        this.trainService.refreshTrainSeal(role.getId(), seal, type);
    }
    
    public void sealPreRefine(Role role, TrainSeal seal, byte type) {
        List<SealNeo> list = new ArrayList<>();
        if (type == 1) {
            list.add(new SealNeo(seal.getRefineGeneralHistory()));
        }
        else if (type == 2) {
            List<List<FieldSeal>> history = seal.getRefineHighHistory();
            for (List<FieldSeal> fields : history) {
                list.add(new SealNeo(fields));
            }
        }
        else if (type == 3) {
            List<List<FieldSeal>> superHistory = seal.getRefineSuperHistory();
            for (List<FieldSeal> fields : superHistory) {
                list.add(new SealNeo(fields));
            }
        }
        MessagePusher.pushMessage(role, new RespPreUpgradeSeal(seal.getId().toString(), list, type));
    }
    
    public boolean deduct(Role role, Map<Integer, Integer> consume) {
        for (Integer key : consume.keySet()) {
            Integer amount = consume.get(key);
            if (key.equals(Const.RESTYPE_SILVER)) {
                boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_SILVER, amount, "信物打造实现类");
                if (!b) {
                    return false;
                }
            }
            else if (key.equals(Const.RESTYPE_COIN)) {
                boolean b = this.roleService.deductCurrency(role, Const.RESTYPE_COIN, amount, "信物打造实现类");
                if (!b) {
                    return false;
                }
            }
            else {
                Integer integer = this.bagService.consumeBagGoods(role, key, amount, true, true, "信物打造");
                if (integer != null) {
                    return false;
                }
            }
        }
        return true;
    }
}
