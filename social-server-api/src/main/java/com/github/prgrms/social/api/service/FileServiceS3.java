package com.github.prgrms.social.api.service;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.github.prgrms.social.api.aws.S3Client;
import com.github.prgrms.social.api.model.commons.AttachedFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Profile("prod")
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceS3 implements FileService{

    private final S3Client s3Client;

    @Override
    public String uploadFile(String path, MultipartFile file) throws IOException {
        AttachedFile profileFile = AttachedFile.toAttachedFile(file);
        String profileImage = null;

        if(profileFile != null) {
            try {
                profileImage = s3Client.upload(profileFile.inputStream(), profileFile.length()
                        , profileFile.randomName(path,"png"), profileFile.getContentType(), null);
            } catch(AmazonS3Exception e) {
                log.warn("Amazon S3 error (key : {} ) {}", e.getMessage(), e);
            }
        }

        return profileImage;
    }

    @Override
    public void deleteFile(String url) {
        s3Client.delete(url);
    }
}
