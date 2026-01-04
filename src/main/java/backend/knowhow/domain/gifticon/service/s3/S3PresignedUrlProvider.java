package backend.knowhow.domain.gifticon.service.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class S3PresignedUrlProvider {

    private final S3Presigner presigner;

    @Value("${aws.s3.bucket}")
    private String bucket;

    public String getPresignedGetUrl(String key, Duration expiresIn) {
        // s3 객체 정보
        GetObjectRequest getReq = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        // presigned url 생성
        GetObjectPresignRequest presignReq = GetObjectPresignRequest.builder()
                .signatureDuration(expiresIn)
                .getObjectRequest(getReq)
                .build();

        return presigner.presignGetObject(presignReq).url().toString();
    }
}
