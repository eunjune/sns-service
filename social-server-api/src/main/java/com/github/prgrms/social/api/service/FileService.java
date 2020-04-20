package com.github.prgrms.social.api.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    String uploadFile(String realPath, MultipartFile file);

}
