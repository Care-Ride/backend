package backend.knowhow.global.common.response;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"status", "code", "message", "data"})
public record ApiResponse<T>(
        int status,
        String code,
        String message,
        @JsonInclude(JsonInclude.Include.NON_NULL) T data
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                SuccessType.SUCCESS.getHttpStatus().value(),
                SuccessType.SUCCESS.getCode(),
                SuccessType.SUCCESS.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(
                SuccessType.SUCCESS.getHttpStatus().value(),
                SuccessType.SUCCESS.getCode(),
                SuccessType.SUCCESS.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(SuccessType success, T data) {
        return new ApiResponse<>(
                success.getHttpStatus().value(),
                success.getCode(),
                success.getMessage(),
                data);
    }

    public static ApiResponse<Void> success(SuccessType success) {
        return new ApiResponse<>(
                success.getHttpStatus().value(),
                success.getCode(),
                success.getMessage(),
                null);
    }



    public static ApiResponse<?> error(ErrorType error) {
        return new ApiResponse<>(
                error.getHttpStatus().value(),
                error.getCode(),
                error.getMessage(),
                null);
    }

}
