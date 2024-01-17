package artgallery.files.service;

import artgallery.files.configuration.ServerUserDetails;
import artgallery.files.model.ImageCompressionResponse;
import artgallery.files.model.ImageModel;
import artgallery.files.model.cache.ImageFilesMetadata;

import java.io.IOException;

public interface PaintingService {
  void putPaintingRaw(ImageModel image, ServerUserDetails userDetails) throws IOException;

  ImageModel getPaintingRaw(long id) throws IOException;

  void deletePaintingRaw(long id) throws IOException;

  ImageModel getPaintingCompressed(long id) throws IOException;

  void deletePaintingCompressed(long id) throws IOException;

  void processCompressionResponse(ImageCompressionResponse response) throws IOException;

  ImageFilesMetadata getPaintingFileMetadata(long id) throws IOException;
}
