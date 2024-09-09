package com.kitty.mina;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import com.kitty.mina.task.CmdRecord;
import java.util.Map;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.ConcurrentMap;

public class MessageStatistics
{
    private static MessageStatistics self;
    private ConcurrentMap<Integer, AtomicInteger> sentCounter;
    private ConcurrentMap<Integer, AtomicInteger> receivedCounter;
    
    public MessageStatistics() {
        this.sentCounter = new ConcurrentHashMap();
        this.receivedCounter = new ConcurrentHashMap();
    }
    
    public static MessageStatistics getInstance() {
        return MessageStatistics.self;
    }
    
    public void addReceived(int cmd) {
        this.receivedCounter.putIfAbsent(Integer.valueOf(cmd), new AtomicInteger());
        AtomicInteger counter = (AtomicInteger)this.receivedCounter.get(Integer.valueOf(cmd));
        counter.incrementAndGet();
    }
    
    public void addSent(int cmd) {
        this.sentCounter.putIfAbsent(Integer.valueOf(cmd), new AtomicInteger());
        AtomicInteger counter = (AtomicInteger)this.sentCounter.get(Integer.valueOf(cmd));
        counter.incrementAndGet();
    }
    
    @Override
    public String toString() {
        List<CmdRecord> received = new ArrayList<>();
        List<CmdRecord> sent = new ArrayList<>();
        for (Map.Entry<Integer, AtomicInteger> entry : this.receivedCounter.entrySet()) {
            received.add(new CmdRecord((int)entry.getKey(), ((AtomicInteger)entry.getValue()).get()));
        }
        for (Map.Entry<Integer, AtomicInteger> entry : this.sentCounter.entrySet()) {
            sent.add(new CmdRecord((int)entry.getKey(), ((AtomicInteger)entry.getValue()).get()));
        }
        Collections.sort(received);
        Collections.sort(sent);
        StringBuilder result = new StringBuilder();
        result.append("received = ").append(received.toString()).append("\n").append("sent = ").append(sent.toString());
        return result.toString();
    }
    
    static {
        MessageStatistics.self = new MessageStatistics();
    }
}
