package artgallery.files.service;

import artgallery.files.model.ImageModel;

import java.io.IOException;

public interface PaintingService {
  void putPaintingRaw(ImageModel image) throws IOException;

  ImageModel getPaintingRaw(long id) throws IOException;

  void deletePaintingRaw(long id) throws IOException;

  ImageModel getPaintingCompressed(long id) throws IOException;

  void deletePaintingCompressed(long id) throws IOException;
}
