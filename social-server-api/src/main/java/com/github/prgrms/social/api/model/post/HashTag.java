package com.github.prgrms.social.api.model.post;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(force = true)
@Getter
@Entity
@ToString(exclude = {"posts"})
@EqualsAndHashCode(of = "id")
public class HashTag {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "태그이름", required = true)
    @Column(nullable = false)
    private final String name;

    @ApiModelProperty(value = "생성일시", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToMany
    @JoinTable(
            name = "post_hashtag",
            joinColumns = @JoinColumn(name = "hashtag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    @Setter
    @JsonManagedReference
    private List<Post> posts = new ArrayList<>();

    @Builder
    private HashTag(Long id, String name, LocalDateTime createAt) {
        checkArgument(isNotEmpty(name), "name must be provided. ");

        this.id = id;
        this.name = name;
        this.createAt = defaultIfNull(createAt, LocalDateTime.now());
    }
}
