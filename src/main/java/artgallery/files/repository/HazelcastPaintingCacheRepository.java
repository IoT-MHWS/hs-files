package artgallery.files.repository;

import artgallery.files.model.cache.ImageData;
import artgallery.files.model.cache.ImageFilesMetadata;
import artgallery.files.model.cache.PaintingMetadata;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.client.util.ClientStateListener;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;

@Repository
@Slf4j
public class HazelcastPaintingCacheRepository implements PaintingCacheRepository {
  private final ObjectMapper objectMapper = new ObjectMapper();
  private final HazelcastInstance hazelcastClient;
  private IMap<Long, String> paintingsFilesCompressedMap;
  private IMap<Long, String> paintingsFilesMetadataMap;
  private IMap<Long, String> paintingsMetadataMap;

  public HazelcastPaintingCacheRepository(ClientConfig config, HazelcastPaintingCacheConfiguration configuration) {
    ClientStateListener clientStateListener = new ClientStateListener(config);
    this.hazelcastClient = HazelcastClient.newHazelcastClient(config);

    CompletableFuture.runAsync(() -> {
      try {
        if (clientStateListener.awaitConnected()) {
          paintingsFilesCompressedMap = hazelcastClient.getMap(configuration.paintingsFilesCompressedMap);
          paintingsFilesMetadataMap = hazelcastClient.getMap(configuration.paintingsFilesMetadataMap);
          paintingsMetadataMap = hazelcastClient.getMap(configuration.paintingsMetadataMap);
          log.info("hazelcast maps initialized");
        }
      } catch (InterruptedException ex) {
        log.error(ex.getMessage());
        Thread.currentThread().interrupt();
      }
    });
  }

  @Override
  public ImageData getPaintingFileCompressed(Long id) {
    try {
      var data = paintingsFilesCompressedMap.get(id);
      return data == null ? null : objectMapper.readValue(data, ImageData.class);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
      return null;
    }
  }

  @Override
  public void setPaintingFileCompressed(Long id, ImageData data) {
    try {
      paintingsFilesCompressedMap.set(id, objectMapper.writeValueAsString(data));
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
    }
  }

  @Override
  public void deletePaintingFileCompressed(Long id) {
    paintingsFilesCompressedMap.delete(id);
  }

  @Override
  public ImageFilesMetadata getPaintingFileMetadata(Long id) {
    try {
      var data = paintingsFilesMetadataMap.get(id);
      return data == null ? null : objectMapper.readValue(data, ImageFilesMetadata.class);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
      return null;
    }
  }

  @Override
  public void setPaintingFileMetadata(Long id, ImageFilesMetadata data) {
    try {
      paintingsFilesMetadataMap.set(id, objectMapper.writeValueAsString(data));
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
    }
  }

  @Override
  public void deletePaintingFileMetadata(Long id) {
    paintingsFilesMetadataMap.delete(id);
  }

  @Override
  public PaintingMetadata getPaintingMetadata(Long id) {
    try {
      var data = paintingsMetadataMap.get(id);
      return objectMapper.readValue(data, PaintingMetadata.class);
    } catch (JsonProcessingException ex) {
      log.error(ex.getMessage());
      return null;
    }
  }

}
