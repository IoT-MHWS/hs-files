package artgallery.files.repository.fs;

import artgallery.files.model.ImageModel;
import artgallery.files.repository.PaintingRepository;
import artgallery.files.util.ImageMimeUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Repository
@RequiredArgsConstructor
public class FsPaintingRepository implements PaintingRepository {

  private final FsPaintingConfiguration configuration;

  @Override
  public ImageModel getPaintingRaw(long id) throws IOException {
    return this.getPainting(id, configuration::getPaintingPathRaw);
  }

  @Override
  public void putPaintingRaw(ImageModel imageModel) throws IOException {
    this.putPainting(imageModel, configuration::getPaintingPathRaw);
  }

  @Override
  public void deletePaintingRaw(long id) throws IOException {
    this.deletePainting(id, configuration::getPaintingPathRaw);
  }

  @Override
  public ImageModel getPaintingCompressed(long id) throws IOException {
    return this.getPainting(id, configuration::getPaintingPathCompressed);
  }

  @Override
  public void putPaintingCompressed(ImageModel imageModel) throws IOException {
    this.putPainting(imageModel, configuration::getPaintingPathCompressed);
  }

  @Override
  public void deletePaintingCompressed(long id) throws IOException {
    this.deletePainting(id, configuration::getPaintingPathCompressed);
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
        return new ImageModel(id, FsUtil.get(path), ImageMimeUtil.extensionToMime(extension));
      } catch (IOException ex) {
        lastException = ex;
      }
    }
    if (lastException != null) {
      throw lastException;
    }
    return null;
  }

  private void putPainting(ImageModel imageModel, FuncPaintingPath funcNameToPath) throws IOException {
    // delete before insertion
    this.deletePainting(imageModel.id(), funcNameToPath);

    String extension = ImageMimeUtil.mimeToExtension(imageModel.mimeType());
    String name = getPaintingName(imageModel.id(), extension);
    Path path = this.getPaintingPath(funcNameToPath.apply(), name);
    FsUtil.put(path, imageModel.bytes());
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
