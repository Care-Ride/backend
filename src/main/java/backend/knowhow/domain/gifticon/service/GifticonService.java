package backend.knowhow.domain.gifticon.service;

import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.dto.response.GifticonProductListResponse;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.repository.GifticonProductRepository;
import backend.knowhow.global.common.response.PageInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class GifticonService {

    private final GifticonProductRepository gifticonProductRepository;

    @Transactional(readOnly = true)
    public GifticonProductListResponse getAvailableProducts(int page, int size){
        // 재고가 0 이상인 상품 조회 (페이징)
        Pageable pageable = PageRequest.of(page, size);
        Page<GifticonProduct> products = gifticonProductRepository.findAllByStockGreaterThan(0, pageable);

        return GifticonProductListResponse.from(products);
    }
}
