package artgallery.files.configuration;

import artgallery.files.model.ApiError;
import com.hazelcast.client.HazelcastClientOfflineException;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.net.SocketException;
import java.nio.file.NoSuchFileException;

@ControllerAdvice
@Slf4j
public class CustomControllerAdvice extends ResponseEntityExceptionHandler {

  @ExceptionHandler({NoSuchFileException.class})
  public ResponseEntity<ApiError> handleNoSuchFileException(NoSuchFileException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
      .contentType(MediaType.APPLICATION_JSON)
      .body(new ApiError(HttpStatus.NOT_FOUND, "element not found"));
  }

  @ExceptionHandler({
    FeignException.class
  })
  public ResponseEntity<String> feignException(FeignException ex) {
    log.error(ex.contentUTF8(), ex.getMessage());
    var status = HttpStatus.resolve(ex.status());
    if (status == null) {
      status = HttpStatus.SERVICE_UNAVAILABLE;
    }
    return ResponseEntity.status(status)
      .contentType(MediaType.APPLICATION_JSON)
      .body(ex.contentUTF8());
  }

  @ExceptionHandler({SocketException.class, HazelcastClientOfflineException.class})
  public ResponseEntity<ApiError> socketException(Exception ex) {
    log.error(ex.getMessage());
    return ResponseEntity
      .status(HttpStatus.SERVICE_UNAVAILABLE)
      .contentType(MediaType.APPLICATION_JSON)
      .build();
  }

}
