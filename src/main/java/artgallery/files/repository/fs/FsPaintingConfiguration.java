package artgallery.files.repository.fs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FsPaintingConfiguration {

  @Value("${app.paintings.repository.fs.mount}")
  private String paintingsMount;

  @Value("${app.paintings.repository.fs.locations.raw}")
  private String locationRaw;

  @Value("${app.paintings.repository.fs.locations.compressed}")
  private String locationCompressed;

  public Path getPaintingMount() {
    return Paths.get(paintingsMount);
  }

  public Path getPaintingPathRaw() throws IOException {
    Path path = Paths.get(paintingsMount, locationRaw);
    Files.createDirectories(path);
    return path;
  }

  public Path getPaintingPathCompressed() throws IOException {
    Path path = Paths.get(paintingsMount, locationCompressed);
    Files.createDirectories(path);
    return path;
  }

}
