package artgallery.files.service;

import artgallery.files.model.ImageModel;
import artgallery.files.repository.PaintingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class PaintingService {

  private final PaintingRepository paintingRepository;

  public void putPaintingRaw(ImageModel image) throws IOException {
    paintingRepository.putPaintingRaw(image);
  }

  public ImageModel getPaintingRaw(long id) throws IOException {
    return paintingRepository.getPaintingRaw(id);
  }

  public void deletePaintingRaw(long id) throws IOException {
    paintingRepository.deletePaintingRaw(id);
  }

  public void putPaintingCompressed(ImageModel image) throws IOException {
    paintingRepository.putPaintingCompressed(image);
  }

  public ImageModel getPaintingCompressed(long id) throws IOException {
    return paintingRepository.getPaintingCompressed(id);
  }

  public void deletePaintingCompressed(long id) throws IOException {
    paintingRepository.deletePaintingCompressed(id);
  }
}
