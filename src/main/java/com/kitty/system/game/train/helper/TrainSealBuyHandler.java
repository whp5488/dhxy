package com.kitty.system.game.train.helper;

import java.util.Map;
import com.kitty.system.game.train.make.TrainSealManage;
import com.kitty.system.game.train.domain.TrainSeal;
import com.kitty.system.game.train.pool.TrainDataPool;
import com.kitty.system.init.domain.SysDataTrainSeal;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("TrainSealBuy")
public class TrainSealBuyHandler extends UpgradeHandler
{
    public TrainSealBuyHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Long id, Integer type, String param, String cause) {
        int serial = Integer.valueOf(param);
        SysDataTrainSeal seal = TrainDataPool.trainSealMap.get(serial);
        if (seal == null) {
            return "没有该信物信息";
        }
        Map<Long, TrainSeal> trainSealMap = this.trainService.getTrainSeal(role.getId());
        TrainSeal orElse = trainSealMap.values().stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (orElse != null) {
            return "你已经拥有该信物";
        }
        TrainSeal trainSeal = TrainSealManage.createTrainSeal(seal.getSerial());
        if (trainSeal != null) {
            trainSeal.setId(this.worker.nextId());
            trainSeal.setRId(role.getId());
            this.trainService.addTrainSeal(role, trainSeal, true, "购买信物");
            return null;
        }
        return "操作的类型不正确";
    }
}
