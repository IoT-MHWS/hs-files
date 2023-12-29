package artgallery.files.model;

public record ImageModel(long id, byte[] bytes, String mimeType) {
}
