package com.estsoft.ormi_p2.service;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidUserDataException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidUserDataException(InvalidUserDataException ex) {
        return ex.getMessage(); // 예: "이미 존재하는 이메일입니다."
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<String> handleMaxUploadSize(MaxUploadSizeExceededException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("파일 크기가 너무 큽니다. 최대 5MB까지 업로드할 수 있습니다.");
    }

}

