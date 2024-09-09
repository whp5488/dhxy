package com.kitty.system.game.mind.helper;

import com.kitty.system.game.mind.pool.MindDataPool;
import com.kitty.system.game.vo.utils.Const;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Iterator;
import com.kitty.system.init.domain.vo.MindDefineRange;
import java.util.ArrayList;
import com.kitty.system.game.vo.FieldProp;
import java.util.List;
import com.kitty.system.game.mind.domain.Mind;
import com.kitty.common.utils.RandomUtil;
import com.kitty.system.common.data.DataGlobalFunc;
import com.kitty.system.init.pool.ConvertDataPool;
import com.kitty.system.init.domain.vo.MindDefine;
import com.kitty.system.game.role.domain.Role;
import org.springframework.stereotype.Component;

@Component("MindRefine")
public class MindRefineHandler extends UpgradeHandler
{
    public MindRefineHandler() {
    }
    
    @Override
    public String acceptUpgrade(Role role, int serial, int type, String param, String cause) {
        int garde = (int)role.getRelive() * 1000 + role.getLevel();
        if (garde < 58) {
            return "未达到1转80级，无法参悟哦~";
        }
        MindDefine mindDefine = ConvertDataPool.mindDefineList.stream().filter(e -> e.getSerial() == serial).findFirst().orElse(null);
        if (mindDefine == null) {
            return "参悟错误";
        }
        Mind mind = this.getMind(role, serial);
        if (mind == null) {
            return "您还没有参悟心法哦~";
        }
        if (serial == 1000 || serial == 2000 || serial == 3000 || serial == 4000) {
            return null;
        }
        if (this.checkFull(mind)) {
            return "当前心法已洗练至满级，无法再洗练哦~";
        }
        if (!this.deduct(role, this.cost(mind.getClassify(), DataGlobalFunc.expend(type)))) {
            return null;
        }
        if (type == 1) {
            mind.getRefineGeneralHistory().clear();
            mind.setRefineNum(mind.getRefineNum() + 1);
            int[] count2 = count(mind.getRefineNum(), this.min(mind.getClassify()));
            mind.setRefineGeneralHistory(this.creation(mindDefine, RandomUtil.round(count2[0], count2[1])));
        }
        else if (type == 2) {
            mind.getRefineHighHistory().clear();
            for (int i = 0; i < 5; ++i) {
                mind.setRefineNum(mind.getRefineNum() + 1);
                int[] count = count(mind.getRefineNum(), this.min(mind.getClassify()));
                mind.getRefineHighHistory().add(this.creation(mindDefine, RandomUtil.round(count[0], count[1])));
            }
        }
        else {
            mind.getRefineSuperHistory().clear();
            for (int i = 0; i < 20; ++i) {
                mind.setRefineNum(mind.getRefineNum() + 1);
                int[] count = count(mind.getRefineNum(), this.min(mind.getClassify()));
                mind.getRefineSuperHistory().add(this.creation(mindDefine, RandomUtil.round(count[0], count[1])));
            }
        }
        mind.save();
        this.mindPreRefine(role, mind, (byte)type);
        return null;
    }
    
    public List<FieldProp> creation(MindDefine mindDefine, int value) {
        List<FieldProp> objects = new ArrayList<>();
        for (MindDefineRange defineRange : mindDefine.getList()) {
            objects.add(new FieldProp(defineRange.getS(), value));
        }
        return objects;
    }
    
    private boolean checkFull(Mind mind) {
        AtomicInteger sum = new AtomicInteger();
        mind.getRefine().forEach(e -> sum.addAndGet(e.getValue()));
        return sum.get() / mind.getRefine().size() == 100;
    }
    
    public Map<Integer, Integer> cost(int classfiy, int num) {
        int jf = 0;
        int wzj = 0;
        if (2 == classfiy) {
            wzj = (jf = 1 * num);
        }
        else if (3 == classfiy) {
            jf = 10 * num;
            wzj = 5 * num;
        }
        else if (4 == classfiy) {
            wzj = (jf = 10 * num);
        }
        Map<Integer, Integer> map = new HashMap<>();
        map.put(75408, wzj);
        map.put(Const.RES_TYPE_CHARGE_SCORE, jf);
        return map;
    }
    
    public static int[] count(int count, int num) {
        double param = 8.0;
        if (num == 69 || num == 30) {
            param = 20.0;
        }
        else if (num == 10) {
            param = 15.0;
        }
        double progress = (double)count / (Math.pow(count, 0.15) * param + (double)count) * 100.0 + (double)num;
        return new int[] { (int)Math.max(num, Math.min(90.0, progress - 10.0)), (int)Math.min(100.0, progress + 10.0) };
    }
    
    public int min(int classify) {
        int min = MindDataPool.SL.get(0);
        if (classify == 1) {
            min = MindDataPool.CS.get(0);
        }
        else if (classify == 2) {
            min = MindDataPool.SL.get(0);
        }
        else if (classify == 3) {
            min = MindDataPool.DF.get(0);
        }
        else if (classify == 4) {
            min = MindDataPool.ZJ.get(0);
        }
        return min;
    }
}
