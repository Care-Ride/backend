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
    INVALID_DATE_FORMAT(HttpStatus.BAD_REQUEST, "COMMON-405", "날짜 형식이 올바르지 않습니다."),
    INVALID_PAGE_REQUEST(HttpStatus.BAD_REQUEST, "COMMON-406", "페이지 요청 값이 올바르지 않습니다."),

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
    GOOGLE_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "GOOGLE-401", "구글 ID Token이 유효하지 않습니다."),
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
    DRIVE_ACCESS_DENIED(HttpStatus.FORBIDDEN, "DRIVE-406", "해당 운전세션에 접근 권한이 없습니다."),

    // Mission
    MISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "MISSION-404", "미션을 찾을 수 없습니다."),
    MISSION_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "MISSION-400", "아직 미션을 달성하지 않았습니다."),
    MISSION_ALREADY_RECEIVED(HttpStatus.CONFLICT, "MISSION-409", "이미 포인트를 수령한 미션입니다."),

    // Point
    INSUFFICIENT_POINTS(HttpStatus.BAD_REQUEST, "POINT-400", "포인트가 부족합니다."),
    INVALID_POINT_AMOUNT(HttpStatus.BAD_REQUEST, "POINT-401", "포인트 금액이 올바르지 않습니다."),

    //Gifticon
    GIFTICON_ALREADY_EXIST(HttpStatus.CONFLICT, "GIFTICON-409", "이미 존재하는 기프티콘입니다. 추가할 수 없습니다."),

    // Image
    IMAGE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "IMAGE-500", "이미지 업로드 중 에러가 발생했습니다."),
    INVALID_IMAGE_TYPE(HttpStatus.BAD_REQUEST, "IMAGE-400", "이미지가 비어있거나 형식이 올바르지 않습니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;



}
