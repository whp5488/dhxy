package com.kitty.common.utils;

import java.util.Map;
import java.util.HashMap;
import com.kitty.common.bean.Lottery;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;

public class LotteryUtils
{
    public LotteryUtils() {
    }
    
    public static int lottery(List<Double> originalRates) {
        if (originalRates == null || originalRates.isEmpty()) {
            return -1;
        }
        int size = originalRates.size();
        double sumRate = 0.0;
        for (double rate : originalRates) {
            sumRate += rate;
        }
        List<Double> sortOrignalRates = new ArrayList(size);
        Double tempSumRate = Double.valueOf(0.0);
        for (double rate2 : originalRates) {
            tempSumRate = Double.valueOf((double)tempSumRate + rate2);
            sortOrignalRates.add(Double.valueOf((double)tempSumRate / sumRate));
        }
        double nextDouble = Math.random();
        sortOrignalRates.add(Double.valueOf(nextDouble));
        Collections.sort(sortOrignalRates);
        return sortOrignalRates.indexOf(Double.valueOf(nextDouble));
    }
    
    public static int lotteryIntEasy(List<Integer> originalRates, int baodi, boolean isOrder) {
        if (originalRates == null || originalRates.isEmpty()) {
            return -1;
        }
        int size = originalRates.size();
        double sumRate = 1000.0;
        List<Double> sortOrignalRates = new ArrayList(size);
        Double tempSumRate = Double.valueOf(0.0);
        Iterator<Integer> iterator = originalRates.iterator();
        while (iterator.hasNext()) {
            double rate = (double)(int)iterator.next();
            tempSumRate = Double.valueOf((double)tempSumRate + rate);
            sortOrignalRates.add(tempSumRate);
        }
        double nextDouble = Math.random() * sumRate + (double)baodi;
        sortOrignalRates.add(Double.valueOf(nextDouble));
        if (isOrder) {
            Collections.sort(sortOrignalRates);
        }
        else {
            Collections.sort(sortOrignalRates, Comparator.reverseOrder());
        }
        return sortOrignalRates.indexOf(Double.valueOf(nextDouble));
    }
    
    public static void main(String[] args) {
        List<Lottery> list = new ArrayList<>();
        list.add(new Lottery(Integer.valueOf(1), "电视", Double.valueOf(4.5)));
        list.add(new Lottery(Integer.valueOf(2), "手机", Double.valueOf(95.5)));
        list.add(new Lottery(Integer.valueOf(3), "飞机", Double.valueOf(0.339999)));
        list.add(new Lottery(Integer.valueOf(4), "冰箱", Double.valueOf(1.0E-4)));
        List<Double> orignalRates = new ArrayList(list.size());
        for (Lottery lottery : list) {
            orignalRates.add(lottery.getBili());
        }
        Map<String, Integer> map = new HashMap();
        for (int i = 0; i < 1000000; ++i) {
            int index = lottery(orignalRates);
            String name = ((Lottery)list.get(index)).getName();
            if (map.containsKey(name)) {
                Integer integer = (Integer)map.get(name);
                map.put(name, integer = Integer.valueOf((int)integer + 1));
            }
            else {
                map.put(name, Integer.valueOf(1));
            }
        }
        System.out.println("我抽奖了一百万次商品出现的概率" + map);
    }
}
