package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.BaseTimeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(force = true)
@Getter
@Setter
@ToString(exclude = {"posts"})
@EqualsAndHashCode(of = "id", callSuper = false)
@Entity
public class HashTag extends BaseTimeEntity {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "태그이름", required = true)
    @Column(nullable = false, unique = true)
    private final String name;

    @ManyToMany
    @JoinTable(
            name = "post_hashtag",
            joinColumns = @JoinColumn(name = "hashtag_id"),
            inverseJoinColumns = @JoinColumn(name = "post_id"))
    private Set<Post> posts = new HashSet<>();

    @Builder
    private HashTag(Long id, String name) {
        checkArgument(isNotEmpty(name), "name must be provided. ");

        this.id = id;
        this.name = name;
    }
}
