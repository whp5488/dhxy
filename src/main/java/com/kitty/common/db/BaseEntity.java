
package com.kitty.common.db;

import java.io.Serializable;

public abstract class BaseEntity<PK extends Comparable<PK> & Serializable> implements Serializable
{
    protected boolean delete;
    
    public BaseEntity() {
        this.delete = false;
    }
    
    public abstract PK getId();
    
    public void doAfterInit() {
    }
    
    public void doBeforeSave() {
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = 31 * result + this.getId().hashCode();
        return result;
    }
    
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (this.getClass() != obj.getClass()) {
            return false;
        }
        final BaseEntity other = (BaseEntity)obj;
        return this.getId().equals(other.getId());
    }
    
    public boolean isDelete() {
        return this.delete;
    }
    
    public void setDelete(final boolean delete) {
        this.delete = delete;
    }
}
