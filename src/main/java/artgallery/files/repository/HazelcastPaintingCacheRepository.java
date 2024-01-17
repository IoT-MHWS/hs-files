package artgallery.files.repository;

import artgallery.files.model.cache.ImageData;
import artgallery.files.model.cache.ImageFilesMetadata;
import artgallery.files.model.cache.PaintingMetadata;
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
  private final HazelcastInstance hazelcastClient;
  private IMap<Long, ImageData> paintingsFilesCompressedMap;
  private IMap<Long, ImageFilesMetadata> paintingsFilesMetadataMap;
  private IMap<Long, PaintingMetadata> paintingsMetadataMap;

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
    return paintingsFilesCompressedMap.get(id);
  }

  @Override
  public void setPaintingFileCompressed(Long id, ImageData data) {
    paintingsFilesCompressedMap.set(id, data);
  }

  @Override
  public void deletePaintingFileCompressed(Long id) {
    paintingsFilesCompressedMap.delete(id);
  }

  @Override
  public ImageFilesMetadata getPaintingFileMetadata(Long id) {
    return paintingsFilesMetadataMap.get(id);
  }

  @Override
  public void setPaintingFileMetadata(Long id, ImageFilesMetadata data) {
    paintingsFilesMetadataMap.set(id, data);
  }

  @Override
  public void deletePaintingFileMetadata(Long id) {
    paintingsFilesMetadataMap.delete(id);
  }

  @Override
  public PaintingMetadata getPaintingMetadata(Long id) {
    return paintingsMetadataMap.get(id);
  }

}
