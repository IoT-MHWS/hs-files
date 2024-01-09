package artgallery.files.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ImageCompressionRequest<T extends CompressionParams> {
  private String source;
  private String destination;
  private String mimeType;
  private T params;
}
