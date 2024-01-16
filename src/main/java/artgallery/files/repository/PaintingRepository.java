package artgallery.files.repository;

import artgallery.files.model.ImageLocation;
import artgallery.files.model.ImageModel;

import java.io.IOException;

public interface PaintingRepository {
  ImageModel getPaintingRaw(long id) throws IOException;

  ImageModel putPaintingRaw(ImageModel imageModel) throws IOException;

  void deletePaintingRaw(long id) throws IOException;

  boolean hasPaintingRaw(long id) throws IOException;

  ImageModel getPaintingCompressed(long id) throws IOException;

  void deletePaintingCompressed(long id) throws IOException;

  boolean hasPaintingCompressed(long id) throws IOException;

  ImageLocation getImageLocationRaw(ImageModel raw) throws IOException;

  ImageLocation getImageLocationCompressedFromRaw(ImageModel raw) throws IOException;

}
