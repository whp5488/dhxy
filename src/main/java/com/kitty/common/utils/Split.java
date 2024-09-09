package com.kitty.common.utils;

import java.util.ArrayList;
import java.util.List;

public class Split
{
    public Split() {
    }
    
    public List<String> StringSplit(String str) {
        String[] resu = str.split("\\|");
        List<String> list = new ArrayList<>();
        for (String k : resu) {
            list.add(k);
        }
        return list;
    }
}
