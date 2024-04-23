package com.kitty.common.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;

public class BlockingUniqueQueue<E> extends LinkedBlockingQueue<E> {
  private static final long serialVersionUID = 8351632564634804122L;
  
  private Set<E> datas = new ConcurrentHashSet<>(1200);
  
  public boolean contains(Object o) {
    return this.datas.contains(o);
  }
  
  public boolean containsAll(Collection<?> c) {
    return this.datas.containsAll(c);
  }
  
  public E take() {
    E head = null;
    try {
      head = super.take();
    } catch (Exception exception) {}
    this.datas.remove(head);
    return head;
  }
  
  public boolean add(E e) {
    if (contains(e))
      return false; 
    this.datas.add(e);
    return super.add(e);
  }
  
  public boolean addAll(Collection<? extends E> c) {
    boolean modified = false;
    for (E e : c) {
      if (add(e))
        modified = true; 
    } 
    return modified;
  }
  
  public boolean remove(Object o) {
    this.datas.remove(o);
    return super.remove(o);
  }
  
  public boolean removeAll(Collection<?> c) {
    for (Object e : c)
      this.datas.remove(e); 
    return super.removeAll(c);
  }
  
  public Iterator<E> iterator() {
    return new Itr(super.iterator());
  }
  
  private class Itr implements Iterator<E> {
    private Iterator<E> it;
    
    private E curr;
    
    Itr(Iterator<E> it) {
      this.it = it;
    }
    
    public boolean hasNext() {
      return this.it.hasNext();
    }
    
    public E next() {
      this.curr = this.it.next();
      return this.curr;
    }
    
    public void remove() {
      this.it.remove();
      BlockingUniqueQueue.this.datas.remove(this.curr);
    }
  }
}
