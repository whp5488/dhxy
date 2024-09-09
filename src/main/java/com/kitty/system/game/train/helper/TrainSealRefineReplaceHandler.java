package com.kitty.system.game.train.helper;

import com.kitty.system.game.train.domain.TrainSeal;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("TrainSealRefineReplace")
public class TrainSealRefineReplaceHandler extends UpgradeHandler
{
    public TrainSealRefineReplaceHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, Long id, Integer type, String param, String cause) {
        TrainSeal seal = this.getSeal(role, id);
        if (seal == null) {
            this.logger.error("洗练信物属性替换错误：玩家ID = {}，信物ID = {}", role.getRoleId(), id);
            return null;
        }
        int index = type / 1000;
        int genre = type % 1000;
        if (genre == 1) {
            if (seal.getRefineGeneralHistory().size() <= 0) {
                return null;
            }
            seal.setRefine(seal.getRefineGeneralHistory());
            seal.setRefineGeneralHistory(new ArrayList<>());
        }
        else if (genre == 2) {
            if (seal.getRefineHighHistory().size() <= 0) {
                return null;
            }
            seal.setRefine(seal.getRefineHighHistory().get(index));
            seal.setRefineHighHistory(new ArrayList<>());
        }
        else if (genre == 3) {
            if (seal.getRefineSuperHistory().size() <= 0) {
                return null;
            }
            seal.setRefine(seal.getRefineSuperHistory().get(index));
            seal.setRefineSuperHistory(new ArrayList<>());
        }
        this.refresh(role, seal, (byte)1);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)5, (byte)3, "信物洗练替换" + JsonUtils.object2String(seal), role.getMarking());
        return null;
    }
}
