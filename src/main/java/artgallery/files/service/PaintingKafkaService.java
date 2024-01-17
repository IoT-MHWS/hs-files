package artgallery.files.service;

import artgallery.files.model.PaintingDeleteDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class PaintingKafkaService {
  private final PaintingService paintingService;
  static private final ObjectMapper objectMapper = new ObjectMapper();

  @KafkaListener(topics = "delete-painting", groupId = "files")
  public void deletePainting(String msg) throws IOException {
    PaintingDeleteDTO painting = objectMapper.readValue(msg, PaintingDeleteDTO.class);
    paintingService.deletePaintingCompressed(painting.getId());
    paintingService.deletePaintingRaw(painting.getId());
  }
}
