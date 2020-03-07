package com.github.prgrms.social.api.model.post;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;

@Entity
@NoArgsConstructor(force = true)
@Getter
@ToString(exclude = {"targetPost","post"})
public class Retweet {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "리트윗 시간", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;


    @OneToOne
    @Setter
    @JsonBackReference
    private Post post;


    @OneToOne
    @Setter
    @JsonManagedReference
    private Post targetPost;

    @Builder
    private Retweet(LocalDateTime createAt) {
        this.id = null;
        this.createAt = defaultIfNull(createAt, now());
    }
}
