package artgallery.files.repository;

import artgallery.files.model.cache.ImageData;
import artgallery.files.model.cache.ImageFilesMetadata;
import artgallery.files.model.cache.PaintingMetadata;

public interface PaintingCacheRepository {

  ImageData getPaintingFileCompressed(Long id);

  void setPaintingFileCompressed(Long id, ImageData data);

  void deletePaintingFileCompressed(Long id);

  ImageFilesMetadata getPaintingFileMetadata(Long id);

  void setPaintingFileMetadata(Long id, ImageFilesMetadata data);

  void deletePaintingFileMetadata(Long id);

  PaintingMetadata getPaintingMetadata(Long id);
}
