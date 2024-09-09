package com.kitty.system.game.mind.helper;

import java.util.HashMap;
import com.kitty.system.enums.war.itempro.mind.MindClassify;
import java.util.stream.Collectors;
import java.util.Map;
import com.kitty.system.game.mind.domain.Mind;
import java.util.List;
import com.kitty.system.game.mind.pool.MindDataPool;
import com.kitty.common.utils.convert.MoneyUtil;
import com.kitty.system.game.vo.utils.Const;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.mind.message.RespMind;
import com.kitty.system.game.mind.make.MindManage;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.domain.vo.MindDefine;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("MindLearn")
public class MindLearnHandler extends UpgradeHandler
{
    private static final String TIPS = "#<LG1>%s·%s#参悟成功，消耗%s";
    
    public MindLearnHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, int serial, int type, String param, String cause) {
        MindDefine mindDefine = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (mindDefine == null) {
            return "参悟错误";
        }
        int garde = (int)role.getRelive() * 1000 + (int)role.getLevel();
        String checkMsg = this.checkClassify(mindDefine.getClassify(), garde);
        if (checkMsg != null) {
            return checkMsg;
        }
        List<Mind> list = this.mindService.getMind((long)role.getId());
        String msg = this.checkCanLearn(garde, list, mindDefine);
        if (msg != null) {
            return msg;
        }
        Map<Integer, Integer> cost = this.cost(mindDefine.getClassify());
        if (cost.size() <= 0) {
            return "操作的类型不正确";
        }
        if (!this.deduct(role, cost)) {
            return "扣除费用失败";
        }
        Mind mind = MindManage.creation(role, this.worker.nextId(), mindDefine, list);
        list.add(mind);
        mind.save();
        MessagePusher.pushMessage((long)role.getId(), new RespMind(mind));
        String colours = MoneyUtil.colours(Long.valueOf((long)(Integer)cost.get(Const.RESTYPE_COIN)));
        this.roleService.calculateBase(role);
        this.roleService.refreshRoleInfo(role, false);
        return String.format("#<LG1>%s·%s#参悟成功，消耗%s", MindDataPool.getClassifyName(mindDefine.getClassify()), MindDataPool.getSerialName(mindDefine.getSerial()), "#<IR3>#" + colours);
    }
    
    public String checkClassify(int classify, int garde) {
        if (classify == 1 && garde < 58) {
            return "未达到0转58级，无法参悟哦~";
        }
        if (classify == 2 && garde < 1080) {
            return "未达到1转80级，无法参悟哦~";
        }
        if (classify == 3 && garde < 2100) {
            return "未达到2转100级，无法参悟哦~";
        }
        if (classify == 4 && garde < 3138) {
            return "未达到3转138级，无法参悟哦~";
        }
        return null;
    }
    
    public String checkCanLearn(int grade, List<Mind> list, MindDefine mindDefine) {
        Mind mind = (Mind)list.stream().filter(e -> e.getSerial() == mindDefine.getSerial()).findFirst().orElse(null);
        if (mind != null) {
            return "已参悟该心法，不可以重复参悟哦~";
        }
        if (mindDefine.getClassify() == 1 && mindDefine.getSerial() == 1000) {
            List<Mind> collect = (List)list.stream().filter(e -> e.getClassify() == 1 && e.getSerial() != 1000).collect(Collectors.toList());
            if (collect.size() < MindClassify.CS_LIST.size()) {
                return String.format("参悟本篇心法#<R>(%d/%d)#后才能参悟心", Integer.valueOf(collect.size()), Integer.valueOf(MindClassify.CS_LIST.size()));
            }
        }
        else if (mindDefine.getClassify() == 2) {
            List<Mind> csList = (List)list.stream().filter(e -> e.getClassify() == 1).collect(Collectors.toList());
            if (csList.size() < MindClassify.CS_LIST.size() + 1) {
                return "请先参悟前一篇章所有节点";
            }
            if (mindDefine.getSerial() == 2000) {
                List<Mind> slList2 = (List)list.stream().filter(e -> e.getClassify() == 2 && e.getSerial() != 2000).collect(Collectors.toList());
                if (slList2.size() < MindClassify.SL_LIST.size()) {
                    return String.format("参悟本篇心法#<R>(%d/%d)#后才能参悟心", Integer.valueOf(slList2.size()), Integer.valueOf(MindClassify.SL_LIST.size()));
                }
            }
        }
        else if (mindDefine.getClassify() == 3) {
            if (grade < 2100) {
                return "2转100级可参悟";
            }
            List<Mind> slList = (List)list.stream().filter(e -> e.getClassify() == 2).collect(Collectors.toList());
            if (slList.size() < MindClassify.SL_LIST.size() + 1) {
                return "请先参悟前一篇章所有节点";
            }
            if (mindDefine.getSerial() == 3000) {
                List<Mind> dfList2 = (List)list.stream().filter(e -> e.getClassify() == 3 && e.getSerial() != 3000).collect(Collectors.toList());
                if (dfList2.size() < MindClassify.DF_LIST.size()) {
                    return String.format("参悟本篇心法#<R>(%d/%d)#后才能参悟心", Integer.valueOf(dfList2.size()), Integer.valueOf(MindClassify.DF_LIST.size()));
                }
            }
        }
        else if (mindDefine.getClassify() == 4) {
            if (grade < 3138) {
                return "3转138级可参悟";
            }
            List<Mind> dfList = (List)list.stream().filter(e -> e.getClassify() == 3).collect(Collectors.toList());
            if (dfList.size() < MindClassify.DF_LIST.size() + 1) {
                return "请先参悟前一篇章所有节点";
            }
            if (mindDefine.getSerial() == 4000) {
                List<Mind> zjList = (List)list.stream().filter(e -> e.getClassify() == 4 && e.getSerial() != 4000).collect(Collectors.toList());
                if (zjList.size() < MindClassify.ZJ_LIST.size()) {
                    return String.format("参悟本篇心法#<R>(%d/%d)#后才能参悟心", Integer.valueOf(zjList.size()), Integer.valueOf(MindClassify.ZJ_LIST.size()));
                }
            }
        }
        return null;
    }
    
    public Map<Integer, Integer> cost(int classify) {
        Map<Integer, Integer> map = new HashMap();
        if (classify == 1) {
            map.put(Const.RESTYPE_COIN, Integer.valueOf(10000));
        }
        else if (classify == 2) {
            map.put(Const.RESTYPE_COIN, Integer.valueOf(100000));
        }
        else if (classify == 3) {
            map.put(Const.RESTYPE_COIN, Integer.valueOf(800000));
        }
        else if (classify == 4) {
            map.put(Const.RESTYPE_COIN, Integer.valueOf(1800000));
        }
        return map;
    }
}
