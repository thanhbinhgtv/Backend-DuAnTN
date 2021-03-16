package duantn.backend.authentication;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@RestController
public class CustomHandleException extends ResponseEntityExceptionHandler {
    @ExceptionHandler(CustomException.class)    //bắt loại exception
    public ResponseEntity<Object> handleAllException(Exception ex, WebRequest request){

        Map<String, String> map=new HashMap<>();
        map.put("date", new Date().toString());
        map.put("mess", ex.getMessage());
        return new ResponseEntity<Object>(map, HttpStatus.BAD_REQUEST); //trả về json lỗi và loại lỗi
    }

}
