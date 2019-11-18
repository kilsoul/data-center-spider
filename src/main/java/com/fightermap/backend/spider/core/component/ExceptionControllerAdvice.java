package com.fightermap.backend.spider.core.component;

import com.fightermap.backend.spider.common.exception.ForbiddenException;
import com.fightermap.backend.spider.common.exception.NotFoundException;
import com.google.common.base.Throwables;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

/**
 * 异常处理类
 *
 * @author zengqk
 */
@Slf4j
@ControllerAdvice()
public class ExceptionControllerAdvice {
    private static final Map<Class<? extends Throwable>, ErrorDetail> ERROR_DETAIL_MAP = new HashMap<>();
    private static final ErrorDetail UNKNOWN = new ErrorDetail(HttpStatus.INTERNAL_SERVER_ERROR, "error.unknown", null);

    static {
        ERROR_DETAIL_MAP.put(NotFoundException.class, new ErrorDetail(HttpStatus.NOT_FOUND, "error.not.found", null));
        ERROR_DETAIL_MAP.put(IllegalArgumentException.class, new ErrorDetail(HttpStatus.BAD_REQUEST, "error.illegal.argument", null));
        ERROR_DETAIL_MAP.put(ForbiddenException.class, new ErrorDetail(HttpStatus.FORBIDDEN, "error.access.forbidden", null));
        ERROR_DETAIL_MAP.put(ConstraintViolationException.class, new ErrorDetail(HttpStatus.CONFLICT, "error.conflict", null));
        ERROR_DETAIL_MAP.put(ServletRequestBindingException.class, new ErrorDetail(HttpStatus.BAD_REQUEST, "error.illegal.request", null));
    }

    @ExceptionHandler({
            NotFoundException.class,
            IllegalArgumentException.class,
            ForbiddenException.class,
            ConstraintViolationException.class,
            ServletRequestBindingException.class
    })
    public ResponseEntity<ErrorDetail> notFoundException(Throwable exception) {
        ErrorDetail errorDetail = ERROR_DETAIL_MAP.getOrDefault(exception.getClass(), UNKNOWN);
        errorDetail.setMessage(exception.getMessage());
        errorDetail.setStackTrace(Throwables.getStackTraceAsString(exception));
        log.error("Received response status [{}];", errorDetail.getStatusCode(), exception);
        return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDetail> exception(Exception exception) {
        ErrorDetail errorDetail = UNKNOWN;
        errorDetail.setMessage(exception.getMessage());
        errorDetail.setStackTrace(Throwables.getStackTraceAsString(exception));
        log.error("Received response status [{}];", errorDetail.getStatusCode(), exception);
        return new ResponseEntity<>(errorDetail, errorDetail.getStatus());
    }

    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class ErrorDetail {

        private HttpStatus status;

        private Integer statusCode;

        private String code;

        private String message;

        private String stackTrace;

        public ErrorDetail(HttpStatus status, String code, String message) {
            this.status = status;
            this.statusCode = status.value();
            this.code = code;
            this.message = message;
        }
    }
}
