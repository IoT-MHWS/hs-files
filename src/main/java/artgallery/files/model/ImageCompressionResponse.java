package artgallery.files.model;

import lombok.Data;

@Data
public class ImageCompressionResponse {
  private ImageLocation source;
  private ImageLocation destination;
  private String mimeType;
  private boolean result;
}
