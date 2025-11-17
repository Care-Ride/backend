package backend.knowhow.global.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    // Common
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-400", "잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON-401", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN,"COMMON-403", "접근이 거부되었습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "COMMON-404", "리소스를 찾을 수 없습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR,"COMMON-500", "서버 내부 오류입니다."),

    // Member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER-404", "회원을 찾을 수 없습니다."),

    // JWT
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-402", "만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-403", "Refresh Token을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-404", "Refresh Token이 유효하지 않습니다."),

    // OAuth
    KAKAO_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "KAKAO-401", "카카오 AccessToken이 유효하지 않습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-500", "외부 API 호출 중 오류가 발생했습니다.");



    private final HttpStatus httpStatus;
    private final String code;
    private final String message;



}
