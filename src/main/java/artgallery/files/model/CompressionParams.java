package artgallery.files.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CompressionParams {
  private int width;
  private int height;
  private boolean keepAspectRatio;
}
