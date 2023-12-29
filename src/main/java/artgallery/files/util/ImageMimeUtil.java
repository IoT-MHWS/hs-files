package artgallery.files.util;

import jodd.net.MimeTypes;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ImageMimeUtil {

  public static final String[] COMPATIBLE_MIME_TYPES = {"image/png", "image/jpeg"};

  public static String[] getCompatibleMimeTypes() {
    return COMPATIBLE_MIME_TYPES;
  }

  public static List<String> getCompatibleExtensions() {
    return Arrays.stream(COMPATIBLE_MIME_TYPES).map(ImageMimeUtil::mimeToExtension).collect(Collectors.toList());
  }

  public static String extensionToMime(String extension) {
    return MimeTypes.getMimeType(extension);
  }

  public static String mimeToExtension(String mimeType) {
    String[] extensions = MimeTypes.findExtensionsByMimeTypes(mimeType, false);
    return extensions.length > 0 ? extensions[0] : null;
  }

}
