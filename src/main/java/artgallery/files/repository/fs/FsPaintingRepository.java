package artgallery.files.repository.fs;

import artgallery.files.model.ImageModel;
import artgallery.files.util.ImageMimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@RequiredArgsConstructor
public class FsPaintingRepository {

  private final FsPaintingConfiguration configuration;

  public ImageModel getPaintingRaw(long id) throws IOException {
    return this.getPainting(id, configuration::getPaintingPathRaw);
  }

  public ImageModel putPaintingRaw(ImageModel imageModel) throws IOException {
    return this.putPainting(imageModel, configuration::getPaintingPathRaw);
  }

  public void deletePaintingRaw(long id) throws IOException {
    this.deletePainting(id, configuration::getPaintingPathRaw);
  }

  public ImageModel getPaintingCompressed(long id) throws IOException {
    return this.getPainting(id, configuration::getPaintingPathCompressed);
  }

  public void deletePaintingCompressed(long id) throws IOException {
    this.deletePainting(id, configuration::getPaintingPathCompressed);
  }

  public Path getPaintingCompressedPath(ImageModel raw) throws IOException {
    var suffix = raw.path().toString().substring(configuration.getPaintingPathRaw().toString().length());
    return getPaintingPath(configuration.getPaintingPathCompressed(), suffix);
  }

  public Path getPaintingMount() {
    return configuration.getPaintingMount();
  }

  @FunctionalInterface
  private interface FuncPaintingPath {
    Path apply() throws IOException;
  }

  private ImageModel getPainting(long id, FuncPaintingPath funcNameToPath) throws IOException {
    IOException lastException = null;
    for (var extension : ImageMimeUtil.getCompatibleExtensions()) {
      try {
        String name = getPaintingName(id, extension);
        Path path = this.getPaintingPath(funcNameToPath.apply(), name);
        return new ImageModel(id, FsUtil.get(path), ImageMimeUtil.extensionToMime(extension), path);
      } catch (IOException ex) {
        lastException = ex;
      }
    }
    if (lastException != null) {
      throw lastException;
    }
    return null;
  }

  private ImageModel putPainting(ImageModel imageModel, FuncPaintingPath funcNameToPath) throws IOException {
    // delete before insertion
    this.deletePainting(imageModel.id(), funcNameToPath);

    String extension = ImageMimeUtil.mimeToExtension(imageModel.mimeType());
    String name = getPaintingName(imageModel.id(), extension);
    Path path = this.getPaintingPath(funcNameToPath.apply(), name);
    FsUtil.put(path, imageModel.bytes());
    return new ImageModel(imageModel.id(), imageModel.bytes(), imageModel.mimeType(), path);
  }

  private void deletePainting(long id, FuncPaintingPath funcNameToPath) throws IOException {
    try (var files = Files.find(funcNameToPath.apply(), 1, (p, attr) ->
      p.toFile().getName().matches(String.format("%d\\..*", id)))) {
      files.forEach(file -> {
        try {
          FsUtil.delete(file);
        } catch (IOException ignored) {
        }
      });
    }
  }

  public Path getPaintingPath(Path path, String name) throws IOException {
    return Paths.get(path.toString(), name);
  }

  public String getPaintingName(long id, String extension) {
    return id + "." + extension;
  }

}
