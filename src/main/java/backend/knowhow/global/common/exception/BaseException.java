package backend.knowhow.global.common.exception;

import backend.knowhow.global.common.response.ErrorType;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException{

    private final ErrorType errorType;

    public BaseException(ErrorType errorType){
        super(errorType.getMessage());
        this.errorType = errorType;
    }

    public HttpStatus getHttpStatus() {
        return this.errorType.getHttpStatus();
    }

}
