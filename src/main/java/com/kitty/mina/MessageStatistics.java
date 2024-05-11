
package com.kitty.mina;

import java.util.Iterator;
import java.util.List;
import java.util.Collections;
import java.util.Map;
import com.kitty.mina.task.CmdRecord;
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
        this.sentCounter = new ConcurrentHashMap<Integer, AtomicInteger>();
        this.receivedCounter = new ConcurrentHashMap<Integer, AtomicInteger>();
    }
    
    public static MessageStatistics getInstance() {
        return MessageStatistics.self;
    }
    
    public void addReceived(final int cmd) {
        this.receivedCounter.putIfAbsent(cmd, new AtomicInteger());
        final AtomicInteger counter = this.receivedCounter.get(cmd);
        counter.incrementAndGet();
    }
    
    public void addSent(final int cmd) {
        this.sentCounter.putIfAbsent(cmd, new AtomicInteger());
        final AtomicInteger counter = this.sentCounter.get(cmd);
        counter.incrementAndGet();
    }
    
    @Override
    public String toString() {
        final List<CmdRecord> received = new ArrayList<CmdRecord>();
        final List<CmdRecord> sent = new ArrayList<CmdRecord>();
        for (final Map.Entry<Integer, AtomicInteger> entry : this.receivedCounter.entrySet()) {
            received.add(new CmdRecord(entry.getKey(), entry.getValue().get()));
        }
        for (final Map.Entry<Integer, AtomicInteger> entry : this.sentCounter.entrySet()) {
            sent.add(new CmdRecord(entry.getKey(), entry.getValue().get()));
        }
        Collections.sort(received);
        Collections.sort(sent);
        final StringBuilder result = new StringBuilder();
        result.append("received = ").append(received.toString()).append("\n").append("sent = ").append(sent.toString());
        return result.toString();
    }
    
    static {
        MessageStatistics.self = new MessageStatistics();
    }
}
