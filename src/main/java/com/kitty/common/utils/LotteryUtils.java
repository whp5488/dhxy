
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
    public static int lottery(final List<Double> originalRates) {
        if (originalRates == null || originalRates.isEmpty()) {
            return -1;
        }
        final int size = originalRates.size();
        double sumRate = 0.0;
        for (final double rate : originalRates) {
            sumRate += rate;
        }
        final List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0.0;
        for (final double rate2 : originalRates) {
            tempSumRate += rate2;
            sortOrignalRates.add(tempSumRate / sumRate);
        }
        final double nextDouble = Math.random();
        sortOrignalRates.add(nextDouble);
        Collections.sort(sortOrignalRates);
        return sortOrignalRates.indexOf(nextDouble);
    }
    
    public static int lotteryIntEasy(final List<Integer> originalRates, final int baodi, final boolean isOrder) {
        if (originalRates == null || originalRates.isEmpty()) {
            return -1;
        }
        final int size = originalRates.size();
        final double sumRate = 1000.0;
        final List<Double> sortOrignalRates = new ArrayList<Double>(size);
        Double tempSumRate = 0.0;
        for (final double rate : originalRates) {
            tempSumRate += rate;
            sortOrignalRates.add(tempSumRate);
        }
        final double nextDouble = Math.random() * sumRate + baodi;
        sortOrignalRates.add(nextDouble);
        if (isOrder) {
            Collections.sort(sortOrignalRates);
        }
        else {
            Collections.sort(sortOrignalRates, Comparator.reverseOrder());
        }
        return sortOrignalRates.indexOf(nextDouble);
    }
    
    public static void main(final String[] args) {
        final List<Lottery> list = new ArrayList<Lottery>();
        list.add(new Lottery(1, "电视", 4.5));
        list.add(new Lottery(2, "手机", 95.5));
        list.add(new Lottery(3, "飞机", 0.339999));
        list.add(new Lottery(4, "冰箱", 1.0E-4));
        final List<Double> orignalRates = new ArrayList<Double>(list.size());
        for (final Lottery lottery : list) {
            orignalRates.add(lottery.getBili());
        }
        final Map<String, Integer> map = new HashMap<String, Integer>();
        for (int i = 0; i < 1000000; ++i) {
            final int index = lottery(orignalRates);
            final String name = list.get(index).getName();
            if (map.containsKey(name)) {
                Integer integer = map.get(name);
                map.put(name, ++integer);
            }
            else {
                map.put(name, 1);
            }
        }
        System.out.println("我抽奖了一百万次商品出现的概率" + map);
    }
}
