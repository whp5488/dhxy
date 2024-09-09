package com.kitty.system.game.mind.helper;

import java.util.Iterator;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.mind.make.MindManage;
import com.kitty.system.game.mind.domain.Mind;
import com.kitty.common.utils.JsonUtils;
import com.kitty.record.RecordThread;
import java.util.ArrayList;
import java.util.List;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.domain.vo.MindDefine;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("MindRefineReplace")
public class MindRefineReplaceHandler extends UpgradeHandler
{
    public MindRefineReplaceHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, int serial, int type, String param, String cause) {
        int garde = role.getRelive() * 1000 + role.getLevel();
        if (garde < 58) {
            return "未达到58级~";
        }
        MindDefine mindDefine = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (mindDefine == null) {
            return "参悟错误";
        }
        Mind mind = this.getMind(role, serial);
        if (mind == null) {
            this.logger.error("洗练心法属性替换错误：玩家ID = {}，心法编号 = {}", role.getRoleId(), serial);
            return null;
        }
        int index = type / 1000;
        if (index > 0) {
            --index;
        }
        int genre = type % 1000;
        if (genre == 1) {
            if (mind.getRefineGeneralHistory().size() <= 0) {
                return null;
            }
            mind.setRefine(mind.getRefineGeneralHistory());
            mind.setRefineGeneralHistory(new ArrayList<>());
        }
        else if (genre == 2) {
            if (mind.getRefineHighHistory().size() <= 0) {
                return null;
            }
            mind.setRefine(mind.getRefineHighHistory().get(index));
            mind.setRefineHighHistory(new ArrayList<>());
        }
        else if (genre == 3) {
            if (mind.getRefineSuperHistory().size() <= 0) {
                return null;
            }
            mind.setRefine(mind.getRefineSuperHistory().get(index));
            mind.setRefineSuperHistory(new ArrayList<>());
        }
        this.refresh(role, mind);
        this.updateXin(role, mindDefine, this.mindService.getMind(role.getId()));
        this.roleService.calculateBase(role);
        this.roleService.refreshRoleInfo(role, false);
        RecordThread.getInstance().add2Queue(role.getId(), role.getRoleId(), (short)5, (byte)3, "心法洗练替换" + JsonUtils.object2String(mind), role.getMarking());
        return null;
    }
    
    public void updateXin(Role role, MindDefine mindDefine, List<Mind> list) {
        int min = MindManage.getMin(mindDefine, list);
        if (1 == mindDefine.getClassify() && 1000 != mindDefine.getSerial()) {
            Mind mind = list.stream().filter(e -> e.getSerial() == 1000).findFirst().orElse(null);
            if (mind != null) {
                this.excute(role, mind, min);
            }
        }
        else if (2 == mindDefine.getClassify() && 2000 != mindDefine.getSerial()) {
            Mind mind = list.stream().filter(e -> e.getSerial() == 2000).findFirst().orElse(null);
            if (mind != null) {
                this.excute(role, mind, min);
            }
        }
        else if (3 == mindDefine.getClassify() && 3000 != mindDefine.getSerial()) {
            Mind mind = list.stream().filter(e -> e.getSerial() == 3000).findFirst().orElse(null);
            if (mind != null) {
                this.excute(role, mind, min);
            }
        }
        else if (4 == mindDefine.getClassify() && 4000 != mindDefine.getSerial()) {
            Mind mind = list.stream().filter(e -> e.getSerial() == 4000).findFirst().orElse(null);
            if (mind != null) {
                this.excute(role, mind, min);
            }
        }
    }
    
    private void excute(Role role, Mind mind, int min) {
        if (mind.getRefine() != null && mind.getRefine().size() > 0) {
            for (FieldProp fp : mind.getRefine()) {
                fp.setValue(min);
            }
        }
        this.refresh(role, mind);
    }
}
