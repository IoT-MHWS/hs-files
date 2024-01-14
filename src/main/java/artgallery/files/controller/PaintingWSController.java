package artgallery.files.controller;

import artgallery.files.event.PaintingStatusRepository;
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

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final PaintingStatusRepository paintingStatusRepository;

  @MessageMapping("/pictures.compression")
  public void processPicturesDoneMessage(@Payload ImageCompressionResponse response) {
    log.info("received response on /pictures.compression: " + response);
  }

  public <T extends CompressionParams> void sendCompressionRequest(ImageCompressionRequest<T> request) {
    String sessionId = paintingStatusRepository.getNext();
    if (sessionId == null) {
      log.warn("no delegated workers");
      return;
    }
    SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor
      .create(SimpMessageType.MESSAGE);
    headerAccessor.setSessionId(sessionId);
    headerAccessor.setLeaveMutable(true);

    simpMessagingTemplate.convertAndSendToUser(sessionId, "/queue/pictures.compression", request, headerAccessor.getMessageHeaders());
    log.info("sent request to /queue/pictures.compression with sessionId=" + sessionId);
  }
}
