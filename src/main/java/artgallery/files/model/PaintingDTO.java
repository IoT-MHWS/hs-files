package artgallery.files.model;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PaintingDTO {
  private long id;
  private String name;
  private Integer yearOfCreation;
  private Long artistId;
}
