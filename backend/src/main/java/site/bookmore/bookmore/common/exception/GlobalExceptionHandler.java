package site.bookmore.bookmore.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import site.bookmore.bookmore.common.dto.ErrorResponse;
import site.bookmore.bookmore.common.dto.ResultResponse;

import javax.persistence.PersistenceException;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;

import static site.bookmore.bookmore.common.exception.ErrorCode.DATABASE_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<ResultResponse<ErrorResponse>> persistenceException(PersistenceException e) {
        log.error("{} {}", DATABASE_ERROR.name(), DATABASE_ERROR.getMessage());
        e.printStackTrace();
        return ResponseEntity.status(DATABASE_ERROR.getHttpStatus())
                .body(ResultResponse.error(ErrorResponse.of(DATABASE_ERROR)));
    }

    @ExceptionHandler(AbstractAppException.class)
    public ResponseEntity<ResultResponse<ErrorResponse>> abstractBaseExceptionHandler(AbstractAppException e) {
        log.error("{} {}", e.getErrorCode().name(), e.getMessage());
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ResultResponse.error(e));
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ResultResponse<ErrorResponse>> handleBindExceptions(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        Map<String, String> result = new HashMap<>();

        String key;

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            key = fieldError.getField();
            if (key.lastIndexOf('.') != -1) {
                key = key.substring(key.lastIndexOf('.') + 1);
            }
            result.put(key, fieldError.getDefaultMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultResponse.error(ErrorResponse.of(result)));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ResultResponse<ErrorResponse>> handleValidationExceptions(ConstraintViolationException e) {
        Map<String, String> result = new HashMap<>();

        String key;

        for (ConstraintViolation<?> cv : e.getConstraintViolations()) {

            key = String.valueOf(cv.getPropertyPath());
            if (key.lastIndexOf('.') != -1) {
                key = key.substring(key.lastIndexOf('.') + 1);
            }

            result.put(key, cv.getMessage());
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(ResultResponse.error(ErrorResponse.of(result)));
    }
}