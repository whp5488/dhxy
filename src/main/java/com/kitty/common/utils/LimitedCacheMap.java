
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
    
    public LimitedCacheMap(final int capacity, final long aliveTime) {
        this(capacity, aliveTime, Boolean.FALSE);
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
            protected boolean removeEldestEntry(final Map.Entry<K, Element<V>> eldest) {
                if (this.size() > capacity) {
                    return true;
                }
                final long eleAliveTime = System.currentTimeMillis() - eldest.getValue().bornTime;
                return eleAliveTime > aliveTime;
            }
        };
    }
    
    public V put(final K key, final V value) {
        V preValue = null;
        final Element<V> newValue = new Element<V>(value);
        this.readLock.lock();
        try {
            if (this.data.containsKey(key)) {
                preValue = (V)this.data.get(key).value;
            }
            this.data.put(key, newValue);
            return preValue;
        }
        finally {
            this.readLock.unlock();
        }
    }
    
    public V get(final K key) {
        final Lock lock = this.useLru ? this.writeLock : this.readLock;
        Element<V> target = null;
        lock.lock();
        try {
            target = this.data.get(key);
            if (target == null) {
                return null;
            }
        }
        finally {
            lock.unlock();
        }
        final long eleAlive = System.currentTimeMillis() - target.bornTime;
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
    
    public void remove(final K key) {
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
    
    public static void main(final String[] args) throws Exception {
        final LimitedCacheMap<Integer, Integer> map = new LimitedCacheMap<Integer, Integer>(3, 1500L);
        map.put(1, 1);
        System.out.println("-----");
        Thread.sleep(1200L);
        System.out.println(map.get(1));
    }
    
    @Override
    public String toString() {
        return "LinkedCacheMap [data=" + this.data + ", capacity=" + this.capacity + ", aliveTime=" + this.aliveTime + ", useLru=" + this.useLru + "]";
    }
    
    class Element<V>
    {
        V value;
        long bornTime;
        
        Element(final V v) {
            this.value = v;
            this.bornTime = System.currentTimeMillis();
        }
        
        @Override
        public String toString() {
            return "Element [value=" + this.value + "]";
        }
    }
}
