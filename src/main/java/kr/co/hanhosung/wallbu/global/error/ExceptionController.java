package kr.co.hanhosung.wallbu.global.error;


import kr.co.hanhosung.wallbu.global.error.dto.ErrorCode;
import kr.co.hanhosung.wallbu.global.error.dto.ErrorResponseDto;
import kr.co.hanhosung.wallbu.global.error.exception.AuthorizationException;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.global.error.exception.InvalidArgumentException;
import kr.co.hanhosung.wallbu.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.Objects;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class ExceptionController {


    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleRuntimeException(RuntimeException ex, NativeWebRequest request) {

        return new ResponseEntity<>(
                ErrorResponseDto.builder().message(ex.getMessage()).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleNotFoundException(NotFoundException notFoundException, NativeWebRequest request) {

        return new ResponseEntity<>(
                ErrorResponseDto.builder().errorCode(notFoundException.getErrorCode().getErrorCode()).message(notFoundException.getErrorCode().getDefaultMessage()).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleInvalidArgumentException(InvalidArgumentException invalidArgumentException, NativeWebRequest request) {

        return new ResponseEntity<>(
                ErrorResponseDto.builder().errorCode(invalidArgumentException.getErrorCode().getErrorCode()).message(invalidArgumentException.getErrorCode().getDefaultMessage()).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleBusinessLogicException(BusinessLogicException businessLogicException, NativeWebRequest request) {

        return new ResponseEntity<>(
                ErrorResponseDto.builder().errorCode(businessLogicException.getErrorCode().getErrorCode()).message(businessLogicException.getErrorCode().getDefaultMessage()).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleValidationException(MethodArgumentNotValidException methodArgumentNotValidException, NativeWebRequest request) {



        String errorMessages = makeFiledErrorMessages(methodArgumentNotValidException.getAllErrors());

        return new ResponseEntity<>(
                ErrorResponseDto.builder().errorCode(ErrorCode.INVALID_ARGUMENT_EXCEPTION.getErrorCode()).message(errorMessages).build(),
                HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponseDto> handleAuthorizationException(AuthorizationException authorizationException, NativeWebRequest request) {

        return new ResponseEntity<>(
                ErrorResponseDto.builder().errorCode(authorizationException.getErrorCode().getErrorCode()).message(authorizationException.getErrorCode().getDefaultMessage()).build(),
                HttpStatus.UNAUTHORIZED
        );
    }

    private String makeFiledErrorMessages(List<ObjectError> objectErrors) {

        StringBuilder sb = new StringBuilder();
        for (ObjectError objectError : objectErrors) {
            if(objectError instanceof FieldError) {
                FieldError fieldError = (FieldError) objectError;
                sb.append(fieldError.getField()).append(" : ").append(fieldError.getDefaultMessage()).append(" / ");
            }
        }
        return  sb.toString();
    }
}
