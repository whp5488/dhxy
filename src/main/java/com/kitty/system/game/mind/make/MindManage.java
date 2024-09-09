package com.kitty.system.game.mind.make;

import java.util.ArrayList;
import java.util.Iterator;
import com.kitty.system.game.vo.FieldProp;
import java.util.Random;
import com.kitty.system.game.mind.pool.MindDataPool;
import com.kitty.system.init.domain.vo.MindDefineRange;
import com.kitty.system.game.mind.domain.Mind;
import java.util.List;
import com.kitty.system.init.domain.vo.MindDefine;
import com.kitty.system.game.role.domain.Role;

public class MindManage
{
    public MindManage() {
    }
    
    public static Mind creation(Role role, long nextId, MindDefine mindDefine, List<Mind> list) {
        int min = 0;
        Mind mind = new Mind(nextId, role.getId(), mindDefine.getClassify(), mindDefine.getSerial());
        for (MindDefineRange defineRange : mindDefine.getList()) {
            if (mindDefine.getClassify() == 1) {
                mind.getRefine().add(new FieldProp(defineRange.getS(), 100));
                min = 100;
            }
            else if (mindDefine.getClassify() == 2) {
                int v = 69;
                if (mindDefine.getSerial() == 2000 && min <= 0) {
                    v = getMin(mindDefine, list);
                }
                min = v;
                mind.getRefine().add(new FieldProp(defineRange.getS(), v));
            }
            else if (mindDefine.getClassify() == 3) {
                int v = MindDataPool.DF.get(new Random().nextInt(MindDataPool.DF.size()));
                if (mindDefine.getSerial() == 3000 && min <= 0) {
                    v = getMin(mindDefine, list);
                }
                min = v;
                mind.getRefine().add(new FieldProp(defineRange.getS(), v));
            }
            else if (mindDefine.getClassify() == 4) {
                int v = MindDataPool.ZJ.get(new Random().nextInt(MindDataPool.ZJ.size()));
                if (mindDefine.getSerial() == 4000 && min <= 0) {
                    v = getMin(mindDefine, list);
                }
                min = v;
                mind.getRefine().add(new FieldProp(defineRange.getS(), v));
            }
        }
        if (mindDefine.getSerial() == 1000 || mindDefine.getSerial() == 2000 || mindDefine.getSerial() == 3000 || mindDefine.getSerial() == 4000) {
            List<MindDefineRange> ranges = mindDefine.getMap().get(role.getSerial());
            if (ranges != null) {
                if (min <= 0) {
                    min = getMin(mindDefine, list);
                }
                for (MindDefineRange defineRange2 : ranges) {
                    mind.getRefine().add(new FieldProp(defineRange2.getS(), min));
                }
            }
        }
        return mind;
    }
    
    public static int getMin(MindDefine mindDefine, List<Mind> list) {
        int min = 100;
        if (list != null && list.size() > 0) {
            for (Mind mind : list) {
                if (mindDefine.getClassify() == mind.getClassify() && 1000 != mind.getSerial() && 2000 != mind.getSerial() && 3000 != mind.getSerial() && 4000 != mind.getSerial() && mind.getRefine() != null && mind.getRefine().size() > 0) {
                    for (FieldProp fp : mind.getRefine()) {
                        if (min > fp.getValue()) {
                            min = fp.getValue();
                        }
                    }
                }
            }
        }
        return min;
    }
    
    public static List<FieldProp> creation(int serial, List<Mind> list, MindDefine mindDefine) {
        int min = 0;
        List<FieldProp> objects = new ArrayList<>();
        if (mindDefine.getClassify() == 1 && mindDefine.getSerial() == 1000) {
            min = 100;
        }
        else if (mindDefine.getClassify() == 2 && mindDefine.getSerial() == 2000) {
            int v = 69;
            if (min <= 0) {
                min = Math.max(v, MindDataPool.mindDefine(2, list));
            }
        }
        else if (mindDefine.getClassify() == 3 && mindDefine.getSerial() == 3000) {
            int v = MindDataPool.DF.get(new Random().nextInt(MindDataPool.DF.size()));
            if (min <= 0) {
                min = Math.max(v, MindDataPool.mindDefine(3, list));
            }
        }
        else if (mindDefine.getClassify() == 4 && mindDefine.getSerial() == 4000) {
            int v = MindDataPool.ZJ.get(new Random().nextInt(MindDataPool.ZJ.size()));
            if (min <= 0) {
                min = Math.max(v, MindDataPool.mindDefine(4, list));
            }
        }
        for (MindDefineRange defineRange : mindDefine.getList()) {
            if (mindDefine.getClassify() == 1 && mindDefine.getSerial() == 1000) {
                objects.add(new FieldProp(defineRange.getS(), min));
            }
            else if (mindDefine.getClassify() == 2 && mindDefine.getSerial() == 2000) {
                objects.add(new FieldProp(defineRange.getS(), min));
            }
            else if (mindDefine.getClassify() == 3 && mindDefine.getSerial() == 3000) {
                objects.add(new FieldProp(defineRange.getS(), min));
            }
            else if (mindDefine.getClassify() == 4 && mindDefine.getSerial() == 4000) {
                objects.add(new FieldProp(defineRange.getS(), min));
            }
        }
        if (mindDefine.getSerial() == 1000 || mindDefine.getSerial() == 2000 || mindDefine.getSerial() == 3000 || mindDefine.getSerial() == 4000) {
            List<MindDefineRange> ranges = mindDefine.getMap().get(serial);
            if (ranges != null && ranges.size() > 0) {
                for (MindDefineRange defineRange2 : ranges) {
                    objects.add(new FieldProp(defineRange2.getS(), min));
                }
            }
        }
        return objects;
    }
}
