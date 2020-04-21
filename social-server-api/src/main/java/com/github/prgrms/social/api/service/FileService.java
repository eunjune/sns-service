package com.github.prgrms.social.api.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    String uploadFile(String path, MultipartFile file) throws IOException;

    void deleteFile(String url);
}
