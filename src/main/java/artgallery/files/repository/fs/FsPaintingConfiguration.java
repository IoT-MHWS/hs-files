package artgallery.files.repository.fs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class FsPaintingConfiguration {

  @Value("${app.fs.paintings.path}")
  private String paintingsPath;

  private static final String SUBDIR_RAW = "raw";
  private static final String SUBDIR_COMPRESSED = "compressed";

  public Path getPaintingPathRaw() throws IOException {
    Path path = Paths.get(paintingsPath, SUBDIR_RAW);
    Files.createDirectories(path);
    return path;
  }

  public Path getPaintingPathCompressed() throws IOException {
    Path path = Paths.get(paintingsPath, SUBDIR_COMPRESSED);
    Files.createDirectories(path);
    return path;
  }

}
