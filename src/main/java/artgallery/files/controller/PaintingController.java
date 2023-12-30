package artgallery.files.controller;

import artgallery.files.model.ImageModel;
import artgallery.files.service.PaintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/paintings")
@RequiredArgsConstructor
public class PaintingController {
  private final PaintingService paintingService;

  @PutMapping(value = "/{id}", consumes = {"image/png", "image/jpeg"})
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> putPaintingRaw(@PathVariable long id, @RequestBody byte[] bytes, @RequestHeader("Content-Type") String contentType) throws IOException {
    paintingService.putPaintingRaw(new ImageModel(id, bytes, contentType));
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  @GetMapping(value = "/{id}")
  public ResponseEntity<?> getPainting(@PathVariable long id, @RequestParam(defaultValue = "raw") String type) throws IOException {
    var imageModel = switch (type) {
      case "raw" -> paintingService.getPaintingRaw(id);
      case "compressed" -> paintingService.getPaintingCompressed(id);
      default -> throw new RuntimeException("unknown type");
    };
    return ResponseEntity.status(HttpStatus.OK)
        .contentType(MediaType.parseMediaType(imageModel.mimeType()))
        .body(imageModel.bytes());
  }

  @DeleteMapping(value = "/{id}")
  @PreAuthorize("hasRole('MODERATOR')")
  public ResponseEntity<?> deletePainting(@PathVariable long id, @RequestParam(defaultValue = "raw") String type) throws IOException {
    switch (type) {
      case "raw" -> paintingService.deletePaintingRaw(id);
      case "compressed" -> paintingService.deletePaintingCompressed(id);
      default -> throw new RuntimeException("unknown type");
    }
    return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
  }

}
