
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
        this.datas = new ConcurrentHashSet<E>(1200);
    }
    
    @Override
    public boolean contains(final Object o) {
        return this.datas.contains(o);
    }
    
    @Override
    public boolean containsAll(final Collection<?> c) {
        return this.datas.containsAll(c);
    }
    
    @Override
    public E take() {
        E head = null;
        try {
            head = super.take();
        }
        catch (final Exception ex) {}
        this.datas.remove(head);
        return head;
    }
    
    @Override
    public boolean add(final E e) {
        if (this.contains(e)) {
            return false;
        }
        this.datas.add(e);
        return super.add(e);
    }
    
    @Override
    public boolean addAll(final Collection<? extends E> c) {
        boolean modified = false;
        for (final E e : c) {
            if (this.add(e)) {
                modified = true;
            }
        }
        return modified;
    }
    
    @Override
    public boolean remove(final Object o) {
        this.datas.remove(o);
        return super.remove(o);
    }
    
    @Override
    public boolean removeAll(final Collection<?> c) {
        for (final Object e : c) {
            this.datas.remove(e);
        }
        return super.removeAll(c);
    }
    
    @Override
    public Iterator<E> iterator() {
        return new Itr(super.iterator());
    }
    
    private class Itr implements Iterator<E>
    {
        private Iterator<E> it;
        private E curr;
        
        Itr(final Iterator<E> it) {
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
