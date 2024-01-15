package artgallery.files.repository;

import artgallery.files.model.CompressionParams;
import artgallery.files.model.ImageCompressionRequest;

public interface PaintingCompressionFacade {
  <T extends CompressionParams> void sendCompressionRequest(ImageCompressionRequest<T> request);
}
