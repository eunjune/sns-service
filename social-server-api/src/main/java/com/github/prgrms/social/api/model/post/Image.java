package com.github.prgrms.social.api.model.post;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.persistence.*;
import java.time.LocalDateTime;

import static com.google.common.base.Preconditions.checkArgument;
import static java.time.LocalDateTime.now;
import static org.apache.commons.lang3.ObjectUtils.defaultIfNull;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;

@Entity
@NoArgsConstructor(force = true)
@Getter
@Setter
@EqualsAndHashCode(of = "id")
@ToString(exclude = {"post"})
public class Image {

    @ApiModelProperty(value = "PK", required = true)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private final Long id;

    @ApiModelProperty(value = "이미지 URL", required = true)
    @Column(nullable = false)
    private final String path;

    @ApiModelProperty(value = "이미지 업로드 날짜", required = true)
    @Column(nullable = false, columnDefinition = "TIMESTAMP")
    private final LocalDateTime createAt;

    @ManyToOne
    private Post post;

    @Builder
    private Image(Long id, String path, LocalDateTime createAt) {
        checkArgument(isNotEmpty(path), "path must be provided.");

        this.id = id;
        this.path = path;
        this.createAt = defaultIfNull(createAt,now());
    }


}
