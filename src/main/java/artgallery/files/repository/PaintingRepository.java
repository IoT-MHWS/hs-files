package artgallery.files.repository;

import artgallery.files.model.ImageModel;

import java.io.IOException;

public interface PaintingRepository {

  ImageModel getPaintingRaw(long id) throws IOException;

  void putPaintingRaw(ImageModel imageModel) throws IOException;

  void deletePaintingRaw(long id) throws IOException;

  ImageModel getPaintingCompressed(long id) throws IOException;

  void putPaintingCompressed(ImageModel imageModel) throws IOException;

  void deletePaintingCompressed(long id) throws IOException;

}
