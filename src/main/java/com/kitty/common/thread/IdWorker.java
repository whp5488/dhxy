package com.kitty.common.thread;

import java.net.NetworkInterface;
import java.net.InetAddress;
import java.lang.management.ManagementFactory;

public class IdWorker
{
    private static final long twepoch = 1288834974657L;
    private static final long workerIdBits = 5L;
    private static final long datacenterIdBits = 5L;
    private static final long maxWorkerId = 31L;
    private static final long maxDatacenterId = 31L;
    private static final long sequenceBits = 12L;
    private static final long workerIdShift = 12L;
    private static final long datacenterIdShift = 17L;
    private static final long timestampLeftShift = 22L;
    private static final long sequenceMask = 4095L;
    private static long lastTimestamp;
    private long sequence;
    private final long workerId;
    private final long datacenterId;
    
    public IdWorker() {
        this.sequence = 0L;
        this.datacenterId = getDatacenterId(31L);
        this.workerId = getMaxWorkerId(this.datacenterId, 31L);
    }
    
    public IdWorker(long workerId, long datacenterId) {
        this.sequence = 0L;
        if (workerId > 31L || workerId < 0L) {
            throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", Long.valueOf(31L)));
        }
        if (datacenterId > 31L || datacenterId < 0L) {
            throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", Long.valueOf(31L)));
        }
        this.workerId = workerId;
        this.datacenterId = datacenterId;
        return;
    }
    
    public synchronized long nextId() {
        long timestamp = this.timeGen();
        if (timestamp < IdWorker.lastTimestamp) {
            throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", Long.valueOf(IdWorker.lastTimestamp - timestamp)));
        }
        if (IdWorker.lastTimestamp == timestamp) {
            this.sequence = (this.sequence + 1L & 0xFFFL);
            if (this.sequence == 0L) {
                timestamp = this.tilNextMillis(IdWorker.lastTimestamp);
            }
        }
        else {
            this.sequence = 0L;
        }
        IdWorker.lastTimestamp = timestamp;
        long nextId = timestamp - 1288834974657L << 22 | this.datacenterId << 17 | this.workerId << 12 | this.sequence;
        return nextId;
    }
    
    private long tilNextMillis(final long lastTimestamp) {
        long timestamp;
        for (timestamp = this.timeGen(); timestamp <= lastTimestamp; timestamp = this.timeGen()) {}
        return timestamp;
    }
    
    private long timeGen() {
        return System.currentTimeMillis();
    }
    
    protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
        StringBuffer mpid = new StringBuffer();
        mpid.append(datacenterId);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        if (!name.isEmpty()) {
            mpid.append(name.split("@")[0]);
        }
        return (long)(mpid.toString().hashCode() & 0xFFFF) % (maxWorkerId + 1L);
    }
    
    protected static long getDatacenterId(long maxDatacenterId) {
        long id = 0L;
        try {
            InetAddress ip = InetAddress.getLocalHost();//本地网络有问题。
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
//            if (network == null) {
                id = 1L;
//            }
//            else {
//                byte[] mac = network.getHardwareAddress();//读不到mac
//                id = ((0xFFL & (long)mac[mac.length - 1]) | (0xFF00L & (long)mac[mac.length - 2] << 8)) >> 6;// 这行空指针，mac为空。
//                id %= maxDatacenterId + 1L;
//            }
        }
        catch (Exception e) {
            e.printStackTrace();
            System.out.println(" getDatacenterId: " + e.getMessage());
        }
        return id;
    }
    
    static {
        IdWorker.lastTimestamp = -1L;
    }
}
