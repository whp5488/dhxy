package com.kitty.system.game.mind.service;

import com.kitty.system.game.mind.helper.UpgradeHandler;
import com.kitty.system.common.message.RespNotifyMiscEx;
import org.nutz.lang.Strings;
import com.kitty.system.common.message.RespMsg;
import com.kitty.system.game.mind.helper.UpgradeHelper;
import com.kitty.system.game.vo.FieldProp;
import com.kitty.system.game.mind.make.MindManage;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.domain.vo.MindDefine;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.mind.message.RespMind;
import com.kitty.listener.EventType;
import com.kitty.listener.annotation.EventHandler;
import java.util.Iterator;
import com.kitty.system.game.role.domain.Role;
import com.kitty.listener.event.LoginEvent;
import org.nutz.dao.Cnd;
import com.kitty.mina.cache.MindCache;
import com.kitty.system.game.mind.domain.Mind;
import java.util.List;
import com.kitty.system.game.role.service.RoleService;
import javax.annotation.Resource;
import org.nutz.dao.Dao;
import org.springframework.stereotype.Service;

@Service
public class MindService
{
    @Resource
    private Dao dao;
    @Resource
    public RoleService roleService;
    
    public MindService() {
    }
    
    public List<Mind> getMind(long id) {
        List<Mind> mindList = MindCache.MIND_MAP.get(id);
        if (mindList != null) {
            return mindList;
        }
        mindList = this.dao.query(Mind.class, Cnd.where("r_id", "=", id));
        MindCache.MIND_MAP.put(id, mindList);
        return mindList;
    }
    
    @EventHandler({ EventType.LOGIN })
    public void handleLoginEvent(LoginEvent loginEvent) {
        Role role = loginEvent.getRole();
        List<Mind> minds = this.getMind(role.getId());
        for (Mind mind : minds) {
            this.refreshMind(role.getId(), mind);
        }
    }
    
    public void refreshMind(long id, Mind mind) {
        MessagePusher.pushMessage(id, new RespMind(mind));
    }
    
    public void correction(Role role) {
        List<Mind> minds = this.getMind(role.getId());
        minds.forEach(mind -> {
            if (mind.getSerial() == 1000) {
                MindDefine mindDefine4 = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == 1000).findFirst().orElse(null);
                if (mindDefine4 == null) {
                    return;
                }
                List<FieldProp> refine4 = MindManage.creation(role.getSerial(), minds, mindDefine4);
                mind.setRefine(refine4);
                mind.save();
                this.refreshMind(role.getId(), mind);
            }
            else if (mind.getSerial() == 2000) {
                MindDefine mindDefine3 = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == 2000).findFirst().orElse(null);
                if (mindDefine3 == null) {
                    return;
                }
                List<FieldProp> refine3 = MindManage.creation(role.getSerial(), minds, mindDefine3);
                mind.setRefine(refine3);
                mind.save();
                this.refreshMind(role.getId(), mind);
            }
            else if (mind.getSerial() == 3000) {
                MindDefine mindDefine2 = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == 3000).findFirst().orElse(null);
                if (mindDefine2 == null) {
                    return;
                }
                List<FieldProp> refine2 = MindManage.creation(role.getSerial(), minds, mindDefine2);
                mind.setRefine(refine2);
                mind.save();
                this.refreshMind(role.getId(), mind);
            }
            else if (mind.getSerial() == 4000) {
                MindDefine mindDefine = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == 4000).findFirst().orElse(null);
                if (mindDefine == null) {
                    return;
                }
                List<FieldProp> refine = MindManage.creation(role.getSerial(), minds, mindDefine);
                mind.setRefine(refine);
                mind.save();
                this.refreshMind(role.getId(), mind);
            }
        });
    }
    
    private int getXinXld(Mind mind) {
        int min = 100;
        List<FieldProp> refine = mind.getRefine();
        if (refine != null && refine.size() > 0) {
            for (FieldProp fp : refine) {
                if (min > fp.getValue()) {
                    min = fp.getValue();
                }
            }
        }
        else {
            min = 0;
        }
        return min;
    }
    
    private void setXinXld(List<FieldProp> refine, int xld) {
        if (refine != null && refine.size() > 0) {
            for (FieldProp fp : refine) {
                fp.setValue(xld);
            }
        }
    }
    
    public void upgrade(Role role, int serial, int type, String param, int operationType, boolean flag, String cause) {
        UpgradeHandler handler = UpgradeHelper.INSTANCE.getHandler(type);
        if (handler != null) {
            String message = handler.acceptUpgrade(role, serial, operationType, param, cause);
            if (!Strings.isEmpty(message) && message != null) {
                MessagePusher.pushMessage(role, new RespNotifyMiscEx(role.getId(), message, false));
            }
        }
        else {
            MessagePusher.pushMessage(role, new RespMsg("很抱歉，该功能暂未开放。"));
        }
    }
}
