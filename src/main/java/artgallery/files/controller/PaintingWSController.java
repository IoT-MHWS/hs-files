package artgallery.files.controller;

import artgallery.files.model.ImageCompressionResponse;
import artgallery.files.service.PaintingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.io.IOException;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaintingWSController {
  private final PaintingService paintingService;

  @MessageMapping("/pictures.compression")
  public void processPicturesDoneMessage(@Payload ImageCompressionResponse response) throws IOException {
    log.info("received response on /pictures.compression: " + response);
    paintingService.processCompressionResponse(response);
  }

}
