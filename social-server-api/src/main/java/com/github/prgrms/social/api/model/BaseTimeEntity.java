package com.github.prgrms.social.api.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseTimeEntity {

    @ApiModelProperty(value = "생성일시", required = true)
    @CreatedDate
    private LocalDateTime createdAt;

    @ApiModelProperty(value = "수정일시", required = true)
    @LastModifiedDate
    private LocalDateTime modifiedAt;
}
