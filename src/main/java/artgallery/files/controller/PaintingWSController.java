package artgallery.files.controller;

import artgallery.files.event.PaintingStatusSessionRepository;
import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;
import artgallery.files.model.ImageCompressionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
@RequiredArgsConstructor
public class PaintingWSController {

  @MessageMapping("/pictures.compression")
  public void processPicturesDoneMessage(@Payload ImageCompressionResponse response) {
    log.info("received response on /pictures.compression: " + response);
  }

}
