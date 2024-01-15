package artgallery.files.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
@Slf4j
public class FsPaintingConfiguration {

  @Value("${app.paintings.repository.fs.mount}")
  private String paintingsMount;

  @Value("${app.paintings.repository.fs.locations.raw}")
  private String locationRaw;

  @Value("${app.paintings.repository.fs.locations.compressed}")
  private String locationCompressed;

  @EventListener
  public void initOnContextRefreshed(ContextRefreshedEvent event) {
    try {
      var raw = getFullPaintingPathRaw();
      Files.createDirectories(raw);

      var compressed = getFullPaintingPathCompressed();
      Files.createDirectories(compressed);

      log.info("created picture directories");
    } catch (IOException ex) {
      log.error("error during initialization: " + ex.getMessage());
    }
  }

  public Path getFullPaintingPathRaw() {
    return Paths.get(paintingsMount, locationRaw);
  }

  public Path getFullPaintingPathCompressed() {
    return Paths.get(paintingsMount, locationCompressed);
  }

  public Path getRootPath() {
    return Paths.get(paintingsMount);
  }

  public Path getPaintingPathRaw() {
    return Paths.get(locationRaw);
  }

  public Path getPaintingPathCompressed() {
    return Paths.get(locationCompressed);
  }

}
