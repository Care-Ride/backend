package backend.knowhow.domain.gifticon.service;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.domain.GifticonUsage;
import backend.knowhow.domain.gifticon.dto.summary.GifticonBarcodeSummary;
import backend.knowhow.domain.gifticon.dto.user.request.GifticonPurchaseRequest;
import backend.knowhow.domain.gifticon.dto.user.response.GifticonProductListResponse;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.repository.GifticonProductRepository;
import backend.knowhow.domain.gifticon.repository.GifticonRepository;
import backend.knowhow.domain.gifticon.repository.GifticonUsageRepository;
import backend.knowhow.domain.gifticon.service.s3.S3PresignedUrlProvider;
import backend.knowhow.domain.member.domain.Member;
import backend.knowhow.domain.member.repository.MemberRepository;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
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

    private final MemberRepository memberRepository;
    private final GifticonRepository gifticonRepository;
    private final GifticonProductRepository gifticonProductRepository;
    private final GifticonUsageRepository gifticonUsageRepository;
    private final S3PresignedUrlProvider s3PresignedUrlProvider;

    private static final Duration IMAGE_URL_EXPIRE = Duration.ofMinutes(3); // presigned image 파기 시간

    @Transactional(readOnly = true)
    public GifticonProductListResponse getAvailableProducts(int page, int size, Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));

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

        return GifticonProductListResponse.of(summaries, member.getPointBalance());
    }

    // TODO: 기프티콘 구매로직 추후 개선 예정
    @Transactional
    public GifticonBarcodeSummary purchaseGifticon(GifticonPurchaseRequest request, Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorType.MEMBER_NOT_FOUND));
        GifticonProduct product = gifticonProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new BaseException(ErrorType.GIFTICON_NOT_FOUND));
        // 재고 부족 경우
        if(product.getStock() <= 0){
            throw new BaseException(ErrorType.PURCHASE_OUT_OF_STOCK);
        }
        // 잔액 부족 경우
        if(member.getPointBalance() < product.getRequiredPoint()){
            throw new BaseException(ErrorType.INSUFFICIENT_POINT_BALANCE);
        }

        // 실제 지급 가능한 Gifticon 1개 가져오기
        Gifticon gifticon = gifticonRepository
                .findUnassignedByProductId(product.getId(), PageRequest.of(0, 1))
                .stream()
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorType.PURCHASE_OUT_OF_STOCK));
        // 상품 재고 차감
        product.decreaseStock(1);
        // 포인트 차감
        member.usePoint(product.getRequiredPoint());
        // 사용 내역 생성 (요청됨)
        GifticonUsage usage = GifticonUsage.success(member, gifticon, product.getRequiredPoint());
        gifticonUsageRepository.save(usage);

        String imageUrl = s3PresignedUrlProvider.getPresignedGetUrl(gifticon.getImageKey(), Duration.ofMinutes(3));

        return GifticonBarcodeSummary.of(gifticon, imageUrl);
    }
}
