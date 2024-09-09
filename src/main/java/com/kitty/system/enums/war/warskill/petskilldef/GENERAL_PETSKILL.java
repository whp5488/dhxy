package com.kitty.system.enums.war.warskill.petskilldef;

import java.util.HashMap;
import com.google.common.collect.Lists;
import java.util.Map;
import java.util.List;

public enum GENERAL_PETSKILL
{
    PETSKILL_CHANGYINDONGDU("帐饮东都", 80001, 0), 
    PETSKILL_YUANQUANWANHU("源泉万斛", 80002, 0), 
    PETSKILL_SHENGONGGUILI("神工鬼力", 80003, 0), 
    PETSKILL_BEIDAOJIANXING("倍道兼行", 80004, 0), 
    PETSKILL_SHISIYAOJUE("视死要诀", 80005, 0), 
    PETSKILL_FENSHENYAOJUE("分身要诀", 80006, 0), 
    PETSKILL_HUNFEIYAOJUE("魂飞要诀", 80007, 0), 
    PETSKILL_WANYINGYAOJUE("万萤要诀", 80008, 0), 
    PETSKILL_FENLIEGONGJI("分裂攻击", 80009, 0), 
    PETSKILL_CIWUFANBU("慈乌反哺", 80010, 0), 
    PETSKILL_FANBUZHISI("反哺之私", 80011, 0), 
    PETSKILL_ZHAOYUNMUYU("朝云暮雨", 80012, 1), 
    PETSKILL_XIANFENGDAOGU("妙手仁心", 80013, 0), 
    PETSKILL_MIAOSHOURENXIN("妙手回春", 80014, 0), 
    PETSKILL_FENGYIN("封印", 80015, 0), 
    PETSKILL_HUNLUAN("混乱", 80016, 0), 
    PETSKILL_ZHONGCHENG("忠诚", 80017, 0), 
    PETSKILL_DAYI("大义", 80018, 0), 
    PETSKILL_ZIYI("自医", 80019, 0), 
    PETSKILL_YICHAN("遗产", 80020, 0), 
    PETSKILL_QINGMINGSHU("清明术", 80021, 0), 
    PETSKILL_TUOKUNSHU("脱困术", 80022, 0), 
    PETSKILL_JINGANGBUHUAI("金刚不坏", 80023, 0), 
    PETSKILL_ZHONGBUBIWEI("忠不避危", 80024, 0), 
    PETSKILL_YITUIWEIJIN("以退为进", 80025, 0), 
    PETSKILL_HUIGEHUIRI("挥戈回日", 80026, 1), 
    PETSKILL_PANSHAN("蹒跚", 80027, 0), 
    PETSKILL_NUXIAN("怒气冲天", 80028, 0), 
    PETSKILL_HENXIAN("恨仙", 80029, 0), 
    PETSKILL_DAOQIANGBURU("刀枪不入", 80030, 0), 
    PETSKILL_JISHIYU("使命召唤", 80031, 0), 
    PETSKILL_NINGSHENSHU("凝神术", 80032, 0), 
    PETSKILL_XUEZHAIXUECHANG("血债血偿", 80033, 1), 
    PETSKILL_SHASHENCHENGREN("杀身成仁", 80034, 1), 
    PETSKILL_LIDAITAOJAING("李代桃僵", 80035, 1), 
    PETSKILL_GEQUSUOXU("各取所需", 80036, 1), 
    PETSKILL_FENGKOULANGJIAN("风口浪尖", 80037, 0), 
    PETSKILL_WULEIHONGDIN("五雷轰顶", 80038, 0), 
    PETSKILL_XINGYUNLIUSHUI("行云流水", 80039, 0), 
    PETSKILL_GEANGUANHUO("隔岸观火", 80040, 0), 
    PETSKILL_NIUFACHONGGUAN("怒发冲冠", 80041, 0), 
    PETSKILL_JIEERLIANSAN("接二连三", 80042, 0), 
    PETSKILL_MUKONGYIQIE("目空一切", 80043, 0), 
    PETSKILL_YAZIBIBAO("睚眦必报", 80044, 0), 
    PETSKILL_SIWUJIDAN("肆无忌惮", 80045, 0), 
    PETSKILL_WURENZHIJING("无人之境", 80046, 0), 
    PETSKILL_YIXUEHAUNXUE("以血还血", 80047, 0), 
    PETSKILL_NINGBOWEIBU("凌波微步", 80048, 0), 
    PETSKILL_GESHANDANIU("隔山打牛", 80049, 0);
    
    private String note;
    private int value;
    private int type;
    
    public static List<Map<String, Integer>> toList() {
        List<Map<String, Integer>> list = Lists.newArrayList();
        for (GENERAL_PETSKILL n : values()) {
            Map<String, Integer> map = new HashMap();
            map.put("value", Integer.valueOf(n.getValue()));
            map.put("type", Integer.valueOf(n.getType()));
            list.add(map);
        }
        return list;
    }
    
    public String getNote() {
        return this.note;
    }
    
    public int getValue() {
        return this.value;
    }
    
    public int getType() {
        return this.type;
    }
    
    private GENERAL_PETSKILL(final String note, final int value, final int type) {
        this.note = note;
        this.value = value;
        this.type = type;
    }
}
