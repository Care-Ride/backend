package backend.knowhow.domain.alert.domain;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ErrorType;

import java.util.Arrays;

public enum KoroadType {

    OLD_MAN("oldman"),
    CHILD("child"),
    SCHOOL("school");

    private final String param;

    KoroadType(String param) {
        this.param = param;
    }

    public String param() {
        return param;
    }

    public static KoroadType from(String value) {
        return Arrays.stream(values())
                .filter(type -> type.param.equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new BaseException(ErrorType.BAD_REQUEST));
    }
}
