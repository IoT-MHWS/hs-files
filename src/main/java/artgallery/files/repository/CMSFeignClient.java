package artgallery.files.repository;

import artgallery.files.model.PaintingDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;

@Component
@FeignClient(name = "cms", path = "/api/v1/paintings")
public interface CMSFeignClient {
  @GetMapping("/{id}")
  PaintingDTO getPaintingById(@PathVariable("id") long id,
                              @RequestHeader("X-User-Id") String userId,
                              @RequestHeader("X-User-Name") String userName,
                              @RequestHeader("X-User-Authorities") String userAuthorities);
}
