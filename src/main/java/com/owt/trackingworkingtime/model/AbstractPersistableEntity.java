package com.owt.trackingworkingtime.model;

import jakarta.persistence.*;
import org.springframework.data.domain.Persistable;

import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractPersistableEntity<T extends Serializable>
        implements Persistable<T> {

    @Transient
    private boolean isNew = true;

    @Override
    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean isNew) {
        this.isNew = isNew;
    }

    @PostLoad
    @PostPersist
    void markNotNew() {
        this.isNew = false;
    }
}
