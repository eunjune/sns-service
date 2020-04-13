package com.github.prgrms.social.api.model.post;

import com.github.prgrms.social.api.model.BaseTimeEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id", callSuper = false)
@ToString(exclude = {"post"})
@Entity
public class Image extends BaseTimeEntity {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "이미지 URL", required = true)
    @Column(nullable = false)
    private final String path;

    @ManyToOne
    private Post post;

    @Builder
    private Image(Long id, String path) {
        checkArgument(isNotEmpty(path), "path must be provided.");

        this.id = id;
        this.path = path;
    }


}
