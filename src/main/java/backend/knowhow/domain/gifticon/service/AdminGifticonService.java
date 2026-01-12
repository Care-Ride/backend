package backend.knowhow.domain.gifticon.service;

import backend.knowhow.domain.gifticon.domain.Gifticon;
import backend.knowhow.domain.gifticon.domain.GifticonProduct;
import backend.knowhow.domain.gifticon.dto.admin.request.GifticonBarcodeInsertRequest;
import backend.knowhow.domain.gifticon.dto.admin.request.GifticonProductCreateRequest;
import backend.knowhow.domain.gifticon.dto.summary.GifticonBarcodeSummary;
import backend.knowhow.domain.gifticon.dto.summary.GifticonProductSummary;
import backend.knowhow.domain.gifticon.repository.GifticonProductRepository;
import backend.knowhow.domain.gifticon.repository.GifticonRepository;
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
    private final GifticonRepository gifticonRepository;
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

        // DB 저장 실패 시 S3 파일 삭제
        try{
            GifticonProduct gifticonProduct = request.toEntity(imageKey);
            GifticonProduct saveProduct = gifticonProductRepository.save(gifticonProduct);
            return GifticonProductSummary.from(saveProduct);
        } catch (Exception e){
            s3Storage.delete(imageKey);
            throw new BaseException(ErrorType.IMAGE_UPLOAD_ERROR);
        }
    }

    @Transactional
    public GifticonBarcodeSummary addGifticonBarcodeImage(GifticonBarcodeInsertRequest request, MultipartFile image) {
        GifticonProduct product = gifticonProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new BaseException(ErrorType.GIFTICON_NOT_FOUND));

        // 이미지 저장
        String imageKey = s3Storage.upload(image, "gifticon");
        // DB 저장 실패 시 S3 파일 삭제
        try{
            Gifticon gifticon = request.toEntity(imageKey, product);
            Gifticon savedGifticon = gifticonRepository.save(gifticon);
            // product 재고 증가
            product.increaseStock(1);
            return GifticonBarcodeSummary.from(savedGifticon);
        } catch (Exception e){
            s3Storage.delete(imageKey);
            throw new BaseException(ErrorType.IMAGE_UPLOAD_ERROR);
        }
    }
}
