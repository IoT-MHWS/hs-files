package artgallery.files.service;

import artgallery.files.configuration.ServerUserDetails;
import artgallery.files.model.ImageCompressionResponse;
import artgallery.files.model.cache.ImageData;
import artgallery.files.model.cache.ImageFilesMetadata;
import artgallery.files.repository.CMSFeignClient;
import artgallery.files.repository.PaintingCacheRepository;
import artgallery.files.repository.PaintingCompressionFacade;
import artgallery.files.repository.PaintingRepository;
import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;
import artgallery.files.model.ImageModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class GenericPaintingService implements PaintingService {

  private final PaintingRepository paintingRepository;
  private final PaintingCompressionFacade paintingCompressionFacade;
  private final PaintingCacheRepository paintingCacheRepository;
  private final CMSFeignClient cmsFeignClient;

  private final CompressionParams compressionParams = CompressionParams.builder()
    .width(256)
    .height(256)
    .keepAspectRatio(true)
    .build();

  public void putPaintingRaw(ImageModel image, ServerUserDetails userDetails) throws IOException {
    var cache = paintingCacheRepository.getPaintingMetadata(image.id());
    if (cache == null) {
      cmsFeignClient.getPaintingById(image.id(),
        userDetails.getIdAsString(), userDetails.getUsername(), userDetails.getAuthoritiesAsString());
    }
    paintingCacheRepository.deletePaintingFileMetadata(image.id());
    var model = paintingRepository.putPaintingRaw(image);
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
    paintingCacheRepository.deletePaintingFileMetadata(id);
    paintingRepository.deletePaintingRaw(id);
  }

  public ImageModel getPaintingCompressed(long id) throws IOException {
    var cache = paintingCacheRepository.getPaintingFileCompressed(id);
    if (cache == null) {
      var compressedPainting = paintingRepository.getPaintingCompressed(id);
      paintingCacheRepository.setPaintingFileCompressed(id, new ImageData(compressedPainting.bytes(), compressedPainting.mimeType()));
      return compressedPainting;
    }
    return new ImageModel(id, cache.data(), cache.mimeType(), null);
  }

  public void deletePaintingCompressed(long id) throws IOException {
    paintingCacheRepository.deletePaintingFileCompressed(id);
    paintingCacheRepository.deletePaintingFileMetadata(id);
    paintingRepository.deletePaintingCompressed(id);
  }

  // if not success -> delete inserted raw image (SAGA)
  public void processCompressionResponse(ImageCompressionResponse response) throws IOException {
    long id = response.getId();
    paintingCacheRepository.deletePaintingFileCompressed(id);
    paintingCacheRepository.deletePaintingFileMetadata(id);
    if (!response.isResult()) {
      paintingRepository.deletePaintingRaw(id);
    }
  }

  public ImageFilesMetadata getPaintingFileMetadata(long id) throws IOException {
    var cache = paintingCacheRepository.getPaintingFileMetadata(id);
    if (cache == null) {
      boolean hasRaw = paintingRepository.hasPaintingRaw(id);
      boolean hasCompressed = paintingRepository.hasPaintingCompressed(id);
      cache = new ImageFilesMetadata(hasRaw, hasCompressed);
      paintingCacheRepository.setPaintingFileMetadata(id, cache);
    }
    return cache;
  }

}
