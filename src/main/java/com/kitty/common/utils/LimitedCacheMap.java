package com.kitty.common.utils;

import java.util.concurrent.locks.Lock;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.LinkedHashMap;

public class LimitedCacheMap<K, V>
{
    private LinkedHashMap<K, Element<V>> data;
    private int capacity;
    private long aliveTime;
    private boolean useLru;
    private ReentrantReadWriteLock lock;
    private ReentrantReadWriteLock.ReadLock readLock;
    private ReentrantReadWriteLock.WriteLock writeLock;
    
    public LimitedCacheMap(int capacity, long aliveTime) {
        this(capacity, aliveTime, (boolean)Boolean.FALSE);
    }
    
    public LimitedCacheMap(final int capacity, final long aliveTime, final boolean useLru) {
        this.lock = new ReentrantReadWriteLock();
        this.readLock = this.lock.readLock();
        this.writeLock = this.lock.writeLock();
        this.capacity = capacity;
        this.aliveTime = aliveTime;
        this.useLru = useLru;
        this.data = new LinkedHashMap<K, Element<V>>(capacity, 1.0f, useLru) {
            private static final long serialVersionUID = 1L;
            
            @Override
            protected boolean removeEldestEntry(Map.Entry<K, Element<V>> eldest) {
                if (this.size() > capacity) {
                    return true;
                }
                long eleAliveTime = System.currentTimeMillis() - ((Element)eldest.getValue()).bornTime;
                return eleAliveTime > aliveTime;
            }
        };
    }
    
    public V put(K key, V value) {
        V preValue = null;
        Element<V> newValue = new Element(value);
        this.readLock.lock();
        try {
            if (this.data.containsKey(key)) {
                preValue = ((Element<V>)this.data.get(key)).value;
            }
            this.data.put(key, newValue);
            return preValue;
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    public V get(K key) {
        Lock lock = this.useLru ? this.writeLock : this.readLock;
        Element<V> target = null;
        lock.lock();
        try {
            target = (Element)this.data.get(key);
            if (target == null) {
                return null;
            }
        }
        finally {
            lock.unlock();
        }
        long eleAlive = System.currentTimeMillis() - target.bornTime;
        if (eleAlive < this.aliveTime) {
            return target.value;
        }
        this.remove(key);
        return null;
    }
    
    public int size() {
        this.readLock.lock();
        try {
            return this.data.size();
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    public void remove(K key) {
        this.writeLock.lock();
        try {
            this.data.remove(key);
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    public void clear() {
        this.writeLock.lock();
        try {
            this.data.clear();
        }
        finally {
            this.writeLock.unlock();
        }
    }
    
    public static void main(String[] args) throws Exception {
        LimitedCacheMap<Integer, Integer> map = new LimitedCacheMap(3, 1500L);
        map.put(Integer.valueOf(1), Integer.valueOf(1));
        System.out.println("-----");
        Thread.sleep(1200L);
        System.out.println(map.get(Integer.valueOf(1)));
    }
    
    @Override
    public String toString() {
        return "LinkedCacheMap [data=" + this.data + ", capacity=" + this.capacity + ", aliveTime=" + this.aliveTime + ", useLru=" + this.useLru + "]";
    }
    
    class Element<V>
    {
        V value;
        long bornTime;
        
        Element(V v) {
            this.value = v;
            this.bornTime = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            return "Element [value=" + this.value + "]";
        }
    }
}
