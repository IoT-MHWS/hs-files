package artgallery.files.service;

import artgallery.files.controller.PaintingWSController;
import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;
import artgallery.files.model.ImageModel;
import artgallery.files.repository.fs.FsPaintingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class PaintingService {

  private final FsPaintingRepository paintingRepository;

  @Autowired
  @Lazy
  private final PaintingWSController paintingWSController;

  private final CompressionParams compressionParams = CompressionParams.builder()
    .width(256)
    .height(256)
    .keepAspectRatio(true)
    .build();

  public void putPaintingRaw(ImageModel image) throws IOException {
    var model = paintingRepository.putPaintingRaw(image);

    // send request to worker for compression
    var compressedPath = paintingRepository.getPaintingCompressedPath(model);

    String sourcePathRel = this.getRelativePath(model.path()).toString();
    String destPathRel = this.getRelativePath(compressedPath).toString();

    var request = new ImageCompressionRequest<>(sourcePathRel, destPathRel, model.mimeType(), compressionParams);
    paintingWSController.sendCompressionRequest(request);
  }

  public ImageModel getPaintingRaw(long id) throws IOException {
    return paintingRepository.getPaintingRaw(id);
  }

  public void deletePaintingRaw(long id) throws IOException {
    paintingRepository.deletePaintingRaw(id);
  }

  public ImageModel getPaintingCompressed(long id) throws IOException {
    return paintingRepository.getPaintingCompressed(id);
  }

  public void deletePaintingCompressed(long id) throws IOException {
    paintingRepository.deletePaintingCompressed(id);
  }

  private Path getRelativePath(Path cur) {
    return paintingRepository.getPaintingMount().relativize(cur);
  }
}
