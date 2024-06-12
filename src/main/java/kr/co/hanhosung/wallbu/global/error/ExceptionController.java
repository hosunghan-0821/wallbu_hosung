package kr.co.hanhosung.wallbu.global.error;


import kr.co.hanhosung.wallbu.global.error.dto.ErrorResponseDto;
import kr.co.hanhosung.wallbu.global.error.exception.BusinessLogicException;
import kr.co.hanhosung.wallbu.global.error.exception.InvalidArgumentException;
import kr.co.hanhosung.wallbu.global.error.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.NativeWebRequest;

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
}
