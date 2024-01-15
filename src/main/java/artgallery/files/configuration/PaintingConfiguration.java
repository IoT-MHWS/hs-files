package artgallery.files.configuration;

import artgallery.files.event.PaintingStatusEventListener;
import artgallery.files.event.PaintingStatusSessionRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Description;

@Configuration
public class PaintingConfiguration {
  @Bean
  @Description("track compression services (join/leave)")
  public PaintingStatusEventListener paintingStatusEventListener() {
    return new PaintingStatusEventListener(paintingStatusRepository());
  }

  @Bean
  @Description("keep connected compression services")
  public PaintingStatusSessionRepository paintingStatusRepository() {
    return new PaintingStatusSessionRepository();
  }

}
