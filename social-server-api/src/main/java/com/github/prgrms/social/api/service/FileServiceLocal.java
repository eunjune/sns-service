package com.github.prgrms.social.api.service;

import com.github.prgrms.social.api.model.commons.AttachedFile;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Profile("!prod")
@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceLocal {

    public String uploadFile(String realPath, MultipartFile file) throws IOException {
        AttachedFile attachedFile = AttachedFile.toAttachedFile(file);
        assert attachedFile != null;
        String extension = attachedFile.extension("png");
        String randomName = attachedFile.randomName(realPath,extension);
        file.transferTo(new File(randomName));
        return randomName.substring(realPath.length()+1);
    }
}
