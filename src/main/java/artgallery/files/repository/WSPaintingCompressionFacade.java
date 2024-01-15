package artgallery.files.repository;

import artgallery.files.event.PaintingStatusSessionRepository;
import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class WSPaintingCompressionFacade implements PaintingCompressionFacade {

  private final SimpMessagingTemplate simpMessagingTemplate;
  private final PaintingStatusSessionRepository paintingStatusSessionRepository;

  public <T extends CompressionParams> void sendCompressionRequest(ImageCompressionRequest<T> request) {
    String sessionId = paintingStatusSessionRepository.getNext();
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
