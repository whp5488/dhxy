package com.kitty.common.utils;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingUniqueQueue<E> extends LinkedBlockingQueue<E>
{
    private static final long serialVersionUID = 8351632564634804122L;
    private Set<E> datas;
    
    public BlockingUniqueQueue() {
        this.datas = new ConcurrentHashSet(1200);
    }
    
    @Override
    public boolean contains(Object o) {
        return this.datas.contains(o);
    }
    
    @Override
    public boolean containsAll(Collection<?> c) {
        return this.datas.containsAll(c);
    }
    
    @Override
    public E take() {
        E head = null;
        try {
            head = super.take();
        }
        catch (Exception ex) {}
        this.datas.remove(head);
        return head;
    }
    
    @Override
    public boolean add(E e) {
        if (this.contains(e)) {
            return false;
        }
        this.datas.add(e);
        return super.add(e);
    }
    
    @Override
    public boolean addAll(Collection<? extends E> c) {
        boolean modified = false;
        for (E e : c) {
            if (this.add(e)) {
                modified = true;
            }
            else {
                continue;
            }
        }
        return modified;
    }
    
    @Override
    public boolean remove(Object o) {
        this.datas.remove(o);
        return super.remove(o);
    }
    
    @Override
    public boolean removeAll(Collection<?> c) {
        for (Object e : c) {
            this.datas.remove(e);
        }
        return super.removeAll(c);
    }
    
    @Override
    public Iterator<E> iterator() {
        return new BlockingUniqueQueue.Itr(super.iterator());
    }
    
    private class Itr implements Iterator<E>
    {
        private Iterator<E> it;
        private E curr;
        
        Itr(Iterator<E> it) {
            this.it = it;
        }
        
        @Override
        public boolean hasNext() {
            return this.it.hasNext();
        }
        
        @Override
        public E next() {
            return this.curr = this.it.next();
        }
        
        @Override
        public void remove() {
            this.it.remove();
            BlockingUniqueQueue.this.datas.remove(this.curr);
        }
    }
}
