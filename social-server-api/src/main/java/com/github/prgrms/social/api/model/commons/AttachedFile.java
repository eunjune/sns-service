package com.github.prgrms.social.api.model.commons;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.apache.commons.io.FilenameUtils.getExtension;
import static org.apache.commons.lang3.StringUtils.*;

@AllArgsConstructor
@Getter
public class AttachedFile {

    private String originalFileName;

    private String contentType;

    private byte[] bytes;

    // 파일 보안 검사. 올바른 파일인지
    private static boolean verify(MultipartFile multipartFile) throws IOException {
        if (multipartFile != null && multipartFile.getSize() > 0 && multipartFile.getOriginalFilename() != null) {
            String contentType = multipartFile.getContentType();
            // 이미지인 경우만 처리
            if (isNotEmpty(contentType) && contentType.toLowerCase().startsWith("image")) {
                /*String fileSignature = byteToJpgSignature(multipartFile.getBytes());
                if(fileSignature.equals("ffd8ffe04a464946")) {
                    return true;
                }*/
                return true;
            }
        }
        return false;
    }

    // 파일 시그네쳐 체크
    private static String byteToJpgSignature(byte[] bytes){
        String siganture = "";

        for(int i = 0; i<10; ++i) {
            if(i != 4 && i != 5) {
                siganture = siganture + String.format("%x",bytes[i] & 0xff);
            }
        }

        return siganture;
    }

    // MultipartFile 객체를 커스텀 File 객체로 변환.
    public static AttachedFile toAttachedFile(MultipartFile multipartFile) throws IOException {
        return verify(multipartFile) ?
                new AttachedFile(multipartFile.getOriginalFilename(), multipartFile.getContentType(), multipartFile.getBytes())
                : null;
    }

    // 확장자
    public String extension(String defaultExtension) {
        return defaultIfEmpty(getExtension(originalFileName), defaultExtension);
    }

    // 이미지 파일이름을 랜덤값으로 변환.
    public String randomName(String defaultExtension) {
        return randomName(null, defaultExtension);
    }

    public String randomName(String basePath, String defaultExtension) {
        String name = isEmpty(basePath) ? UUID.randomUUID().toString() : basePath + "/" + UUID.randomUUID().toString();
        return name + "." + extension(defaultExtension);
    }

    public InputStream inputStream() {
        return new ByteArrayInputStream(bytes);
    }

    // 이미지 바이트 길이
    public long length() {
        return bytes.length;
    }
}
