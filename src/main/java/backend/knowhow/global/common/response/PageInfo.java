package backend.knowhow.global.common.response;

import org.springframework.data.domain.Page;

public record PageInfo(
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean hasNext
) {
    public static PageInfo from(Page<?> p) {
        return new PageInfo(
                p.getNumber(),
                p.getSize(),
                p.getTotalElements(),
                p.getTotalPages(),
                p.hasNext()
        );
    }
}
