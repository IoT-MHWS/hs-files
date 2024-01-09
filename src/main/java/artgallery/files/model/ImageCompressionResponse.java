package artgallery.files.model;

import lombok.Data;

@Data
public class ImageCompressionResponse {
  private String source;
  private String destination;
  private String mimeType;
  private boolean result;
}
