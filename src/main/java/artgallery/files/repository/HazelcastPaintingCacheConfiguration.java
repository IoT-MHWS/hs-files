package artgallery.files.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HazelcastPaintingCacheConfiguration {
  @Value("${app.hazelcast.maps.paintings-files-metadata}")
  public String paintingsFilesMetadataMap;

  @Value("${app.hazelcast.maps.paintings-files-compressed}")
  public String paintingsFilesCompressedMap;

  @Value("${app.hazelcast.maps.paintings-metadata}")
  public String paintingsMetadataMap;
}
