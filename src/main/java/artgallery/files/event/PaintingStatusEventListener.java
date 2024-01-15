package artgallery.files.event;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@RequiredArgsConstructor
@Slf4j
public class PaintingStatusEventListener {
  private final PaintingStatusSessionRepository paintingStatusSessionRepository;

  @EventListener
  private void handleSessionConnected(SessionConnectedEvent event) {
    SimpMessageHeaderAccessor headers = SimpMessageHeaderAccessor.wrap(event.getMessage());
    log.info("connected: " + headers.getSessionId());
    paintingStatusSessionRepository.add(headers.getSessionId());
  }

  @EventListener
  private void handleSessionDisconnect(SessionDisconnectEvent event) {
    paintingStatusSessionRepository.remove(event.getSessionId());
  }
}
