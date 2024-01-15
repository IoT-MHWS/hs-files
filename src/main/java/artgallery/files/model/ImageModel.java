package artgallery.files.model;

import java.nio.file.Path;

public record ImageModel(long id, byte[] bytes, String mimeType, Path fullPath) {
}
