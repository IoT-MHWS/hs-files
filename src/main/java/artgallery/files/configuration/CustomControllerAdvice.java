package artgallery.files.configuration;

import artgallery.files.model.ApiError;
import feign.FeignException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.UnknownHostException;
import java.nio.file.NoSuchFileException;

@ControllerAdvice
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler({NoSuchFileException.class})
  public ResponseEntity<?> handleNoSuchFileException(NoSuchFileException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiError(HttpStatus.NOT_FOUND, "element not found"));
  }

  @ExceptionHandler({
    FeignException.class
  })
  public ResponseEntity<?> feignException(FeignException ex) {
    var status = HttpStatus.resolve(ex.status());
    if (status == null) {
      status = HttpStatus.SERVICE_UNAVAILABLE;
    }
    return ResponseEntity.status(status)
      .body(ex.contentUTF8());
  }

}
