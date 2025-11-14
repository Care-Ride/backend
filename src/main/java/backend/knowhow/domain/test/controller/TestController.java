package backend.knowhow.domain.test.controller;

import backend.knowhow.global.common.exception.BaseException;
import backend.knowhow.global.common.response.ApiResponse;
import backend.knowhow.global.common.response.ErrorType;
import backend.knowhow.global.common.response.SuccessType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    @GetMapping("/success")
    public ApiResponse<?> testSuccess() {
        return ApiResponse.success(SuccessType.SUCCESS, "hello");
    }

    @GetMapping("/error")
    public ApiResponse<?> testError() {
        throw new BaseException(ErrorType.BAD_REQUEST);
    }

    @GetMapping("/member")
    public ApiResponse<?> testMemberNotFound() {
        throw new BaseException(ErrorType.MEMBER_NOT_FOUND);
    }
}
