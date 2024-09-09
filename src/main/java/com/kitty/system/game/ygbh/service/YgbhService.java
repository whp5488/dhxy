
package com.kitty.system.game.ygbh.service;

import com.kitty.common.thread.IdWorker;
import com.kitty.mina.message.MessagePusher;
import com.kitty.system.game.role.domain.Role;
import com.kitty.system.game.ygbh.domain.ListA;
import com.kitty.system.game.ygbh.domain.ListB;
import com.kitty.system.game.ygbh.domain.YgbhItem;
import com.kitty.system.game.ygbh.message.RespS2cYgbhInfo;
import org.nutz.dao.Dao;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class YgbhService
{
    Logger logger;
    @Autowired
    private Dao dao;
    @Autowired
    private IdWorker worker;
    
    public void getYbghData(final Role role) {
        final List<YgbhItem> listAItems = new ArrayList<>();
        listAItems.add(new YgbhItem(1, 1, 21082, 1));
        listAItems.add(new YgbhItem(1, 1, 21079, 2));
        listAItems.add(new YgbhItem(1, 1, 21077, 3));
        listAItems.add(new YgbhItem(1, 1, 21078, 4));
        listAItems.add(new YgbhItem(3, 1, 92208, 5));
        listAItems.add(new YgbhItem(3, 1, 92203, 6));
        final ListA listA = new ListA(listAItems);
        final List<YgbhItem> listBItems = new ArrayList<>();
        listBItems.add(new YgbhItem(1, 2, 21080, 7));
        listBItems.add(new YgbhItem(1, 2, 21081, 8));
        listBItems.add(new YgbhItem(1, 2, 21076, 9));
        listBItems.add(new YgbhItem(99, 2, 30018, 10));
        listBItems.add(new YgbhItem(9999, 2, 75406, 11));
        listBItems.add(new YgbhItem(999, 2, 75116, 12));
        final ListB listB = new ListB(listBItems);
        MessagePusher.pushMessage(role, new RespS2cYgbhInfo(498, 648, listA, listB));
    }
}
