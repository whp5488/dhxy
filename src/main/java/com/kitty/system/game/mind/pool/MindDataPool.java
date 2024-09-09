package com.kitty.system.game.mind.pool;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Collections;
import com.kitty.system.game.vo.FieldProp;
import java.util.ArrayList;
import java.util.stream.Collectors;
import com.kitty.system.game.mind.domain.Mind;
import java.util.List;

public class MindDataPool
{
    public static final List<Integer> CS;
    public static final List<Integer> SL;
    public static final List<Integer> DF;
    public static final List<Integer> ZJ;
    
    public MindDataPool() {
    }
    
    public static int mindDefine(int classify, List<Mind> list) {
        int min = 0;
        if (classify == 2) {
            List<Mind> collect = list.stream().filter(e -> e.getClassify() == classify && e.getSerial() != 2000).collect(Collectors.toList());
            min = mindDefine(collect);
        }
        else if (classify == 3) {
            List<Mind> collect = list.stream().filter(e -> e.getClassify() == classify && e.getSerial() != 3000).collect(Collectors.toList());
            min = mindDefine(collect);
        }
        else if (classify == 4) {
            List<Mind> collect = list.stream().filter(e -> e.getClassify() == classify && e.getSerial() != 4000).collect(Collectors.toList());
            min = mindDefine(collect);
        }
        return min;
    }
    
    public static int mindDefine(List<Mind> list) {
        List<Integer> objects = new ArrayList<>();
        for (Mind mind : list) {
            int sum = 0;
            for (FieldProp prop : mind.getRefine()) {
                sum += prop.getValue();
            }
            if (sum > 0) {
                objects.add(sum / mind.getRefine().size());
            }
        }
        return (int)Collections.min(objects);
    }
    
    public static String getClassifyName(int classify) {
        switch (classify) {
            case 2: {
                return "熟路篇";
            }
            case 3: {
                return "登峰篇";
            }
            case 4: {
                return "造极";
            }
            default: {
                return "初涉篇";
            }
        }
    }
    
    public static String getSerialName(int serial) {
        switch (serial) {
            case 1001: {
                return "贪";
            }
            case 1002: {
                return "嗔";
            }
            case 1003: {
                return "痴";
            }
            case 2001: {
                return "私";
            }
            case 2002: {
                return "疑";
            }
            case 2003: {
                return "执";
            }
            case 2004: {
                return "妄";
            }
            case 2005: {
                return "惧";
            }
            case 2006: {
                return "恨";
            }
            case 2007: {
                return "烦";
            }
            case 3001: {
                return "乐";
            }
            case 3002: {
                return "愁";
            }
            case 3003: {
                return "怨";
            }
            case 3004: {
                return "恼";
            }
            case 3005: {
                return "怠";
            }
            case 3006: {
                return "失";
            }
            case 3007: {
                return "悲";
            }
            case 4001: {
                return "怒";
            }
            case 4002: {
                return "爱";
            }
            case 4003: {
                return "喜";
            }
            case 4004: {
                return "恶";
            }
            case 4005: {
                return "惊";
            }
            case 4006: {
                return "恐";
            }
            case 4007: {
                return "思";
            }
            case 4008: {
                return "哀";
            }
            case 4009: {
                return "欲";
            }
            default: {
                return "心";
            }
        }
    }
    
    static {
        CS = Arrays.asList(100, 100);
        SL = Arrays.asList(69, 69);
        DF = Arrays.asList(30, 50);
        ZJ = Arrays.asList(10, 30);
    }
}
