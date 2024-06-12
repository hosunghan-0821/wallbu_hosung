package kr.co.hanhosung.wallbu.domain;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createDate;

    @LastModifiedBy
    private LocalDateTime updateDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    private boolean isDeleted;


    @PrePersist
    public void beforeCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.isDeleted = false;
        this.createDate = now;
        this.updateDate = now;
    }

    @PreUpdate
    public void beforeUpdate() {
        LocalDateTime now = LocalDateTime.now();
        this.updateDate = now;
    }

    public void softDelete() {
        this.isDeleted = true;
    }
}
