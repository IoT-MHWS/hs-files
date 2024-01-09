package artgallery.files.configuration;

import artgallery.files.event.PaintingStatusEventListener;
import artgallery.files.event.PaintingStatusRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;
import org.springframework.messaging.simp.SimpMessagingTemplate;

@Configuration
public class PaintingConfiguration {
  @Bean
  @Description("track compression services (join/leave)")
  public PaintingStatusEventListener paintingStatusEventListener() {
    return new PaintingStatusEventListener(paintingStatusRepository());
  }

  @Bean
  @Description("keep connected compression services")
  public PaintingStatusRepository paintingStatusRepository() {
    return new PaintingStatusRepository();
  }

}
