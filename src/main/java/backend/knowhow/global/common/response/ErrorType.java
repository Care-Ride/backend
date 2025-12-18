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
    INVALID_ROLE(HttpStatus.BAD_REQUEST, "MEMBER-405", "유효하지 않은 역할입니다."),

    // JWT
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-401", "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-402", "만료된 토큰입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "AUTH-403", "Refresh Token을 찾을 수 없습니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "AUTH-404", "Refresh Token이 유효하지 않습니다."),
    TOKEN_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH-405", "인증이 필요합니다. Authorization 헤더가 비어있습니다."),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "AUTH-406", "접근 권한이 없습니다."),

    // OAuth
    KAKAO_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "KAKAO-401", "카카오 AccessToken이 유효하지 않습니다."),
    EXTERNAL_API_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "EXTERNAL-500", "외부 API 호출 중 오류가 발생했습니다."),

    // Connection
    INVALID_CONNECTION_CODE(HttpStatus.BAD_REQUEST, "CONNECT-400", "연동코드가 올바르지 않거나 만료되었습니다."),
    ALREADY_LINKED(HttpStatus.CONFLICT, "CONNECT-401", "이미 연결된 관계입니다."),
    CONNECTION_CODE_NOT_FOUND(HttpStatus.NOT_FOUND, "CONNECT-402", "연동코드를 찾을 수 없습니다."),
    LINK_NOT_FOUND(HttpStatus.NOT_FOUND, "CONNECT-403", "연결 정보를 찾을 수 없습니다."),
    LINK_INFO_INVALID(HttpStatus.BAD_REQUEST, "LINK-400", "관계 설정 정보가 올바르지 않습니다."),

    // Setting
    INVALID_SETTING_LEVEL(HttpStatus.BAD_REQUEST, "SETTING-400", "기기설정 레벨이 올바르지 않습니다."),

    // driving
    KAKAO_MAP_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "KAKAO-500", "카카오 지도 API 호출 중 오류가 발생했습니다."),
    KMA_WEATHER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "KMA-500", "기상청 API 호출 중 오류가 발생했습니다."),
    DRIVE_ALREADY_ENDED(HttpStatus.BAD_REQUEST, "DRIVE-401", "이미 운전이 종료가 된 세션입니다."),
    DRIVE_SESSION_NOT_FOUND(HttpStatus.NOT_FOUND, "DRIVE-404", "운전세션을 찾을 수 없습니다."),
    DRIVE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "DRIVE-406", "해당 운전세션에 접근 권한이 없습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;



}
