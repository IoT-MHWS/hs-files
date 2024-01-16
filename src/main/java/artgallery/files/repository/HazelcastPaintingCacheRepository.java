package artgallery.files.repository;

import artgallery.files.model.cache.ImageData;
import artgallery.files.model.cache.ImageFilesMetadata;
import artgallery.files.model.cache.PaintingMetadata;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.map.IMap;
import org.springframework.stereotype.Repository;

@Repository
public class HazelcastPaintingCacheRepository implements PaintingCacheRepository {
  private final IMap<Long, ImageData> paintingsFilesCompressedMap;
  private final IMap<Long, ImageFilesMetadata> paintingsFilesMetadataMap;
  private final IMap<Long, PaintingMetadata> paintingsMetadataMap;

  public HazelcastPaintingCacheRepository(HazelcastInstance hazelcastClient, HazelcastPaintingCacheConfiguration configuration) {
    this.paintingsFilesCompressedMap = hazelcastClient.getMap(configuration.paintingsFilesCompressedMap);
    this.paintingsFilesMetadataMap = hazelcastClient.getMap(configuration.paintingsFilesMetadataMap);
    this.paintingsMetadataMap = hazelcastClient.getMap(configuration.paintingsMetadataMap);
  }

  public ImageData getPaintingFileCompressed(Long id) {
    return paintingsFilesCompressedMap.get(id);
  }

  public void setPaintingFileCompressed(Long id, ImageData data) {
    paintingsFilesCompressedMap.set(id, data);
  }

  public ImageFilesMetadata getPaintingFileMetadata(Long id) {
    return paintingsFilesMetadataMap.get(id);
  }

  public void setPaintingFileMetadata(Long id, ImageFilesMetadata data) {
    paintingsFilesMetadataMap.set(id, data);
  }

  public PaintingMetadata getPaintingMetadata(Long id) {
    return paintingsMetadataMap.get(id);
  }

}
