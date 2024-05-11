
package com.kitty.common.utils;

import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import com.kitty.common.thread.ThreadSafe;
import java.io.Serializable;
import java.util.Set;
import java.util.AbstractSet;

@ThreadSafe
public class ConcurrentHashSet<E> extends AbstractSet<E> implements Set<E>, Serializable
{
    private static final long serialVersionUID = -8672117787651310382L;
    private static final Object PRESENT;
    private final ConcurrentHashMap<E, Object> map;
    
    public ConcurrentHashSet() {
        this.map = new ConcurrentHashMap<E, Object>();
    }
    
    public ConcurrentHashSet(final int initialCapacity) {
        this.map = new ConcurrentHashMap<E, Object>(initialCapacity);
    }
    
    @Override
    public Iterator<E> iterator() {
        return this.map.keySet().iterator();
    }
    
    @Override
    public int size() {
        return this.map.size();
    }
    
    @Override
    public boolean isEmpty() {
        return this.map.isEmpty();
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.map.containsKey(o);
    }
    
    @Override
    public boolean add(final E e) {
        return this.map.put(e, ConcurrentHashSet.PRESENT) == null;
    }
    
    @Override
    public boolean remove(final Object o) {
        return this.map.remove(o) == ConcurrentHashSet.PRESENT;
    }
    
    @Override
    public void clear() {
        this.map.clear();
    }
    
    static {
        PRESENT = new Object();
    }
}
