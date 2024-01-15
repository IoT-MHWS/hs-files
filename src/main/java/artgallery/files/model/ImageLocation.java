package artgallery.files.model;


import lombok.Getter;

import java.nio.file.Path;

@Getter
public class ImageLocation {
  private final LocationType locationType;
  private final String location;

  private ImageLocation(LocationType type, String location) {
    this.locationType = type;
    this.location = location;
  }

  public enum LocationType {
    FILESYSTEM,
  }

  public static ImageLocation getFsLocation(Path path) {
    return new ImageLocation(ImageLocation.LocationType.FILESYSTEM, path.toString());
  }
}
