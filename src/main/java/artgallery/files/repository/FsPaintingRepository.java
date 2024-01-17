package artgallery.files.repository;

import artgallery.files.model.ImageLocation;
import artgallery.files.model.ImageModel;
import artgallery.files.util.FsUtil;
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

  public ImageModel getPaintingRaw(long id) throws IOException {
    return this.getPainting(id, configuration::getFullPaintingPathRaw);
  }

  public ImageModel putPaintingRaw(ImageModel imageModel) throws IOException {
    return this.putPainting(imageModel, configuration::getFullPaintingPathRaw);
  }

  public void deletePaintingRaw(long id) throws IOException {
    this.deletePainting(id, configuration::getFullPaintingPathRaw);
  }

  @Override
  public boolean hasPaintingRaw(long id) throws IOException {
    return this.hasPainting(id, configuration::getFullPaintingPathRaw);
  }

  public ImageModel getPaintingCompressed(long id) throws IOException {
    return this.getPainting(id, configuration::getFullPaintingPathCompressed);
  }

  public void deletePaintingCompressed(long id) throws IOException {
    this.deletePainting(id, configuration::getFullPaintingPathCompressed);
  }

  @Override
  public boolean hasPaintingCompressed(long id) throws IOException {
    return this.hasPainting(id, configuration::getFullPaintingPathCompressed);
  }

  /*
  /root-path/{raw|compressed}-path/suffix-path
   */

  public ImageLocation getImageLocationRaw(ImageModel raw) {
    return ImageLocation.getFsLocation(configuration.getRootPath().relativize(raw.fullPath()));
  }

  public ImageLocation getImageLocationCompressedFromRaw(ImageModel raw) throws IOException {
    var suffix = configuration.getFullPaintingPathRaw().relativize(raw.fullPath());
    return ImageLocation.getFsLocation(this.getPath(configuration.getPaintingPathCompressed(), suffix.toString()));
  }

  @FunctionalInterface
  private interface FuncPaintingPath {
    Path apply();
  }

  private ImageModel getPainting(long id, FuncPaintingPath funcNameToFullPath) throws IOException {
    IOException lastException = null;
    for (var extension : ImageMimeUtil.getCompatibleExtensions()) {
      try {
        String name = getName(id, extension);
        Path path = this.getPath(funcNameToFullPath.apply(), name);
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

  private ImageModel putPainting(ImageModel imageModel, FuncPaintingPath funcNameToFullPath) throws IOException {
    // delete before insertion
    this.deletePainting(imageModel.id(), funcNameToFullPath);

    String extension = ImageMimeUtil.mimeToExtension(imageModel.mimeType());
    String name = getName(imageModel.id(), extension);
    Path path = this.getPath(funcNameToFullPath.apply(), name);
    FsUtil.put(path, imageModel.bytes());
    return new ImageModel(imageModel.id(), imageModel.bytes(), imageModel.mimeType(), path);
  }

  private void deletePainting(long id, FuncPaintingPath funcNameToFullPath) throws IOException {
    try (var files = Files.find(funcNameToFullPath.apply(), 1, (p, attr) ->
      p.toFile().getName().matches(String.format("%d\\..*", id)))) {
      files.forEach(file -> {
        try {
          FsUtil.delete(file);
        } catch (IOException ignored) {
        }
      });
    }
  }

  private boolean hasPainting(long id, FuncPaintingPath funcNameToFullPath) throws IOException {
    try (var files = Files.find(funcNameToFullPath.apply(), 1, (p, attr) ->
      p.toFile().getName().matches(String.format("%d\\..*", id)))) {
      return files.findAny().isPresent();
    }
  }

  private Path getPath(Path path, String name) throws IOException {
    return Paths.get(path.toString(), name);
  }

  private String getName(long id, String extension) {
    return id + "." + extension;
  }

}
