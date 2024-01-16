package artgallery.files.service;

import artgallery.files.model.ImageCompressionResponse;
import artgallery.files.model.cache.ImageData;
import artgallery.files.model.cache.ImageFilesMetadata;
import artgallery.files.repository.PaintingCacheRepository;
import artgallery.files.repository.PaintingCompressionFacade;
import artgallery.files.repository.PaintingRepository;
import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;
import artgallery.files.model.ImageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class GenericPaintingService implements PaintingService {

  private final PaintingRepository paintingRepository;
  private final PaintingCompressionFacade paintingCompressionFacade;
  private final PaintingCacheRepository paintingCacheRepository;

  private final CompressionParams compressionParams = CompressionParams.builder()
    .width(256)
    .height(256)
    .keepAspectRatio(true)
    .build();

  public void putPaintingRaw(ImageModel image) throws IOException {
    var model = paintingRepository.putPaintingRaw(image);
    paintingCacheRepository.setPaintingFileMetadata(image.id(), new ImageFilesMetadata(true, false));
    // send request to worker for compression
    var sourcePath = paintingRepository.getImageLocationRaw(model);
    var destinationPath = paintingRepository.getImageLocationCompressedFromRaw(model);

    var request = new ImageCompressionRequest<>(image.id(), sourcePath, destinationPath, model.mimeType(), compressionParams);
    paintingCompressionFacade.sendCompressionRequest(request);
  }

  public ImageModel getPaintingRaw(long id) throws IOException {
    return paintingRepository.getPaintingRaw(id);
  }

  public void deletePaintingRaw(long id) throws IOException {
    paintingRepository.deletePaintingRaw(id);
    var paintingFileMetadata = paintingCacheRepository.getPaintingFileMetadata(id);

    paintingCacheRepository.setPaintingFileMetadata(id, new ImageFilesMetadata(
      false,
      this.hasCompressed(id, paintingFileMetadata)
    ));
  }

  public ImageModel getPaintingCompressed(long id) throws IOException {
    return paintingRepository.getPaintingCompressed(id);
  }

  public void deletePaintingCompressed(long id) throws IOException {
    paintingRepository.deletePaintingCompressed(id);
    var paintingFileMetadata = paintingCacheRepository.getPaintingFileMetadata(id);

    paintingCacheRepository.setPaintingFileMetadata(id, new ImageFilesMetadata(
      this.hasRaw(id, paintingFileMetadata),
      false
    ));
  }

  // if not success -> delete inserted raw image (SAGA)
  public void processCompressionResponse(ImageCompressionResponse response) throws IOException {
    long id = response.getId();
    ImageFilesMetadata metadata;
    if (!response.isResult()) {
      paintingRepository.deletePaintingRaw(id);
      metadata = new ImageFilesMetadata(false, false);
    } else {
      var paintingFileMetadata = paintingCacheRepository.getPaintingFileMetadata(id);
      metadata = new ImageFilesMetadata(this.hasRaw(id, paintingFileMetadata), true);
    }
    paintingCacheRepository.setPaintingFileMetadata(id, metadata);
  }

  private boolean hasRaw(long id, ImageFilesMetadata metadata) throws IOException {
    if (metadata == null) {
      return paintingRepository.hasPaintingRaw(id);
    }
    return metadata.hasRaw();
  }

  private boolean hasCompressed(long id, ImageFilesMetadata metadata) throws IOException {
    if (metadata == null) {
      return paintingRepository.hasPaintingCompressed(id);
    }
    return metadata.hasCompressed();
  }
}
