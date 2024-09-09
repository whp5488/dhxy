package com.kitty.system.game.mind.helper;

import com.kitty.system.game.mind.domain.Mind;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("MindPreRefine")
public class MindPreRefineHandler extends UpgradeHandler
{
    public MindPreRefineHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, int serial, int type, String param, String cause) {
        int garde = role.getRelive() * 1000 + role.getLevel();
        if (garde < 58) {
            return "未达到58级，无法习练心法悟哦~";
        }
        Mind mind = this.getMind(role, serial);
        if (mind == null) {
            this.logger.error("心法洗练预览错误：玩家ID = {}，心法编号 = {}", role.getRoleId(), serial);
            return null;
        }
        this.mindPreRefine(role, mind, (byte)type);
        return null;
    }
}
