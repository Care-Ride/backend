package backend.knowhow.domain.gifticon.service.s3;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Storage {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String upload(MultipartFile file, String prefix) {
        validateImage(file);

        String key = prefix + "/" + UUID.randomUUID() + "_" + sanitize(file.getOriginalFilename());

        try (InputStream is = file.getInputStream()) {
            PutObjectRequest req = PutObjectRequest.builder()
                    .bucket(bucket)
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(req, RequestBody.fromInputStream(is, file.getSize()));
            return key; // DB에 저장할 값
        } catch (IOException e) {
            throw new BaseException(ErrorType.IMAGE_UPLOAD_ERROR);
        }
    }

    public void delete(String key) {
        s3Client.deleteObject(DeleteObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build());
    }

    // 이미지 아닌 파일 업로드 차단
    private void validateImage(MultipartFile file) {
        if (file == null || file.isEmpty()){
            throw new BaseException(ErrorType.INVALID_IMAGE_TYPE);
        }
        String ct = file.getContentType();
        if (ct == null || !ct.startsWith("image/")) {
            throw new BaseException(ErrorType.INVALID_IMAGE_TYPE);
        }
    }

    // 이름 특수문자 제거
    private String sanitize(String name) {
        if (name == null) return "file";
        return name.replaceAll("[^a-zA-Z0-9._-]", "_");
    }
}
