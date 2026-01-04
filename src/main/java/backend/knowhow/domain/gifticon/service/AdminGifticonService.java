package backend.knowhow.domain.gifticon.service;

import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.dto.admin.request.GifticonProductCreateRequest;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.repository.GifticonProductRepository;
import backend.knowhow.domain.gifticon.service.s3.S3Storage;
import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminGifticonService {
    
    private final GifticonProductRepository gifticonProductRepository;
    private final S3Storage s3Storage;

    @Transactional(readOnly = true)
    public List<GifticonProductSummary> getAllProducts() {
        // 전체 상품 조회
        List<GifticonProduct> products = gifticonProductRepository.findAll();
        return products.stream()
                .map(GifticonProductSummary::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public GifticonProductSummary addGifticonProduct(GifticonProductCreateRequest request, MultipartFile image) {
        // 이미 존재하는 상품인지 확인
        Optional<GifticonProduct> existProduct = gifticonProductRepository.findByBrandNameAndProductName(request.getBrandName(), request.getProductName());
        if(existProduct.isPresent()) {
            throw new BaseException(ErrorType.GIFTICON_ALREADY_EXIST);
        }

        // 이미지 저장
        String imageKey = s3Storage.upload(image, "gifticon_product");

        GifticonProduct gifticonProduct = request.toEntity(imageKey);
        GifticonProduct saveProduct = gifticonProductRepository.save(gifticonProduct);

        return GifticonProductSummary.from(saveProduct);
    }
}
