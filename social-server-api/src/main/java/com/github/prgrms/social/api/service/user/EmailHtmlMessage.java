package com.github.prgrms.social.api.service.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailHtmlMessage {

    private String link;

    private String name;

    private String linkName;

    private String message;

}
