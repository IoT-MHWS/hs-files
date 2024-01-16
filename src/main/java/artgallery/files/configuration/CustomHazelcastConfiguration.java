package artgallery.files.configuration;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class CustomHazelcastConfiguration {

  @Value("${app.hazelcast.addresses}")
  String[] hazelcastAddresses;

  @Value("${app.hazelcast.cluster-name}")
  String hazelcastClusterName;

  @Bean
  HazelcastInstance getHazelcastClient() {
    ClientConfig clientConfig = new ClientConfig();
    clientConfig.getNetworkConfig().addAddress(hazelcastAddresses);
    clientConfig.setClusterName(hazelcastClusterName);
    return HazelcastClient.newHazelcastClient(clientConfig);
  }

  @EventListener(ContextClosedEvent.class)
  public void shutdownHazelcastClient(HazelcastInstance hazelcastClient) {
    hazelcastClient.shutdown();
  }
}