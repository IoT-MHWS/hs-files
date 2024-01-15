package artgallery.files.service;

import artgallery.files.repository.PaintingCompressionFacade;
import artgallery.files.repository.PaintingRepository;
import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;
import artgallery.files.model.ImageModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class GenericPaintingService implements PaintingService {

  private final PaintingRepository paintingRepository;

  private final PaintingCompressionFacade paintingCompressionFacade;

  private final CompressionParams compressionParams = CompressionParams.builder()
    .width(256)
    .height(256)
    .keepAspectRatio(true)
    .build();

  public void putPaintingRaw(ImageModel image) throws IOException {
    var model = paintingRepository.putPaintingRaw(image);

    // send request to worker for compression
    var sourcePath = paintingRepository.getImageLocationRaw(model);
    var destinationPath = paintingRepository.getImageLocationCompressedFromRaw(model);

    var request = new ImageCompressionRequest<>(sourcePath, destinationPath, model.mimeType(), compressionParams);
    paintingCompressionFacade.sendCompressionRequest(request);
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
}
