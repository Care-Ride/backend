package backend.knowhow.domain.gifticon.service;

import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.dto.user.response.GifticonProductListResponse;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.repository.GifticonProductRepository;
import backend.knowhow.domain.gifticon.service.s3.S3PresignedUrlProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class GifticonService {

    private final GifticonProductRepository gifticonProductRepository;
    private final S3PresignedUrlProvider s3PresignedUrlProvider;

    private static final Duration IMAGE_URL_EXPIRE = Duration.ofMinutes(3); // presigned image 파기 시간

    @Transactional(readOnly = true)
    public GifticonProductListResponse getAvailableProducts(int page, int size){
        // 재고가 0 이상인 상품 조회 (페이징)
        Pageable pageable = PageRequest.of(page, size);
        Page<GifticonProduct> products = gifticonProductRepository.findAllByStockGreaterThan(0, pageable);
        
        // 이미지 presigned url 생성
        Page<GifticonProductSummary> summaries = products.map(product -> {
            // 이미지 비어있으면 null값 입력
            String imageKey = product.getImageKey();
            if(imageKey== null || imageKey.isBlank()){
                return GifticonProductSummary.of(product, null);
            }
            
            String presignedUrl = s3PresignedUrlProvider.getPresignedGetUrl(
                    imageKey,
                    IMAGE_URL_EXPIRE
            );
            return GifticonProductSummary.of(product, presignedUrl);
        });

        return GifticonProductListResponse.from(summaries);
    }
}
