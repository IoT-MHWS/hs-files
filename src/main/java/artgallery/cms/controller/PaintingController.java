package artgallery.cms.controller;

import artgallery.cms.configuration.ServerUserDetails;
import artgallery.cms.service.PaintingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/paintings")
@RequiredArgsConstructor
public class PaintingController {
  private final PaintingService paintingService;

  @GetMapping("/")
  public ResponseEntity<?> getAllPaintings(@AuthenticationPrincipal ServerUserDetails userDetails) {
    return ResponseEntity.ok().body(userDetails.toString());
  }

}
