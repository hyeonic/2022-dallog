package com.allog.dallog.global.error;

import com.allog.dallog.domain.auth.exception.EmptyAuthorizationHeaderException;
import com.allog.dallog.domain.auth.exception.InvalidTokenException;
import com.allog.dallog.domain.auth.exception.NoPermissionException;
import com.allog.dallog.domain.auth.exception.NoSuchOAuthTokenException;
import com.allog.dallog.domain.category.exception.DuplicatedExternalCategoryException;
import com.allog.dallog.domain.category.exception.InvalidCategoryException;
import com.allog.dallog.domain.category.exception.NoSuchCategoryException;
import com.allog.dallog.domain.member.exception.InvalidMemberException;
import com.allog.dallog.domain.member.exception.NoSuchMemberException;
import com.allog.dallog.domain.schedule.exception.InvalidScheduleException;
import com.allog.dallog.domain.schedule.exception.NoSuchScheduleException;
import com.allog.dallog.domain.subscription.exception.ExistSubscriptionException;
import com.allog.dallog.domain.subscription.exception.InvalidSubscriptionException;
import com.allog.dallog.domain.subscription.exception.NoSuchSubscriptionException;
import com.allog.dallog.global.error.dto.ErrorReportRequest;
import com.allog.dallog.global.error.dto.ErrorResponse;
import com.allog.dallog.infrastructure.oauth.exception.OAuthException;
import javax.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
public class ControllerAdvice {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdvice.class);
    private static final String INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT = "%s 필드는 %s (전달된 값: %s)";

    @ExceptionHandler({
            InvalidCategoryException.class,
            InvalidMemberException.class,
            InvalidScheduleException.class,
            InvalidSubscriptionException.class,
            ExistSubscriptionException.class,
            DuplicatedExternalCategoryException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidData(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler({
            EmptyAuthorizationHeaderException.class,
            InvalidTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleInvalidAuthorization(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
    }

    @ExceptionHandler(NoPermissionException.class)
    public ResponseEntity<ErrorResponse> handleNoPermission(final NoPermissionException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
    }

    @ExceptionHandler({
            NoSuchCategoryException.class,
            NoSuchMemberException.class,
            NoSuchSubscriptionException.class,
            NoSuchScheduleException.class,
            NoSuchOAuthTokenException.class
    })
    public ResponseEntity<ErrorResponse> handleNoSuchData(final RuntimeException e) {
        ErrorResponse errorResponse = new ErrorResponse(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
    }

    @ExceptionHandler(OAuthException.class)
    public ResponseEntity<ErrorResponse> handleOAuthException(final RuntimeException e) {
        log.error("OAuth 통신 과정에서 에러가 발생했습니다.", e);
        ErrorResponse errorResponse = new ErrorResponse("OAuth 통신 과정에서 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleInvalidRequestBody() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 형식의 Request Body 입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleInvalidDtoField(final MethodArgumentNotValidException e) {
        FieldError firstFieldError = e.getFieldErrors().get(0);
        String errorMessage = String.format(INVALID_DTO_FIELD_ERROR_MESSAGE_FORMAT, firstFieldError.getField(),
                firstFieldError.getDefaultMessage(), firstFieldError.getRejectedValue());

        ErrorResponse errorResponse = new ErrorResponse(errorMessage);
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponse> handleTypeMismatch() {
        ErrorResponse errorResponse = new ErrorResponse("잘못된 데이터 타입입니다.");
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnexpectedException(final Exception e,
                                                                   final HttpServletRequest request) {
        ErrorReportRequest errorReport = new ErrorReportRequest(request, e);
        log.error(errorReport.getLogMessage(), e);

        ErrorResponse errorResponse = new ErrorResponse("예상하지 못한 서버 에러가 발생했습니다.");
        return ResponseEntity.internalServerError().body(errorResponse);
    }
}
