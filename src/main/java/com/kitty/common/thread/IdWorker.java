package com.kitty.common.thread;

import java.lang.management.ManagementFactory;
import java.net.InetAddress;
import java.net.NetworkInterface;

public class IdWorker {
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
  
  private static long lastTimestamp = -1L;
  
  private long sequence = 0L;
  
  private final long workerId;
  
  private final long datacenterId;
  
  public IdWorker() {
    this.datacenterId = getDatacenterId(31L);
    this.workerId = getMaxWorkerId(this.datacenterId, 31L);
  }
  
  public IdWorker(long workerId, long datacenterId) {
    if (workerId <= 31L && workerId >= 0L) {
      if (datacenterId <= 31L && datacenterId >= 0L) {
        this.workerId = workerId;
        this.datacenterId = datacenterId;
      } else {
        throw new IllegalArgumentException(String.format("datacenter Id can't be greater than %d or less than 0", new Object[] { Long.valueOf(31L) }));
      } 
    } else {
      throw new IllegalArgumentException(String.format("worker Id can't be greater than %d or less than 0", new Object[] { Long.valueOf(31L) }));
    } 
  }
  
  public synchronized long nextId() {
    long timestamp = timeGen();
    if (timestamp < lastTimestamp)
      throw new RuntimeException(String.format("Clock moved backwards.  Refusing to generate id for %d milliseconds", new Object[] { Long.valueOf(lastTimestamp - timestamp) })); 
    if (lastTimestamp == timestamp) {
      this.sequence = this.sequence + 1L & 0xFFFL;
      if (this.sequence == 0L)
        timestamp = tilNextMillis(lastTimestamp); 
    } else {
      this.sequence = 0L;
    } 
    lastTimestamp = timestamp;
    long nextId = timestamp - 1288834974657L << 22L | this.datacenterId << 17L | this.workerId << 12L | this.sequence;
    return nextId;
  }
  
  private long tilNextMillis(long lastTimestamp) {
    long timestamp;
    for (timestamp = timeGen(); timestamp <= lastTimestamp; timestamp = timeGen());
    return timestamp;
  }
  
  private long timeGen() {
    return System.currentTimeMillis();
  }
  
  protected static long getMaxWorkerId(long datacenterId, long maxWorkerId) {
    StringBuffer mpid = new StringBuffer();
    mpid.append(datacenterId);
    String name = ManagementFactory.getRuntimeMXBean().getName();
    if (!name.isEmpty())
      mpid.append(name.split("@")[0]); 
    return (mpid.toString().hashCode() & 0xFFFF) % (maxWorkerId + 1L);
  }
  
  protected static long getDatacenterId(long maxDatacenterId) {
    long id = 0L;
    try {
      InetAddress ip = InetAddress.getLocalHost();
      NetworkInterface network = NetworkInterface.getByInetAddress(ip);
      if (network == null) {
        id = 1L;
      } else {
        id = System.currentTimeMillis();
        id %= maxDatacenterId + 1L;
      } 
    } catch (Exception var7) {
      var7.printStackTrace();
      System.out.println(" getDatacenterId: " + var7.getMessage());
    } 
    return id;
  }
}
