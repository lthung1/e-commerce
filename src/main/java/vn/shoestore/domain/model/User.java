package vn.shoestore.domain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
  private Long id;

  private String username;

  @JsonIgnore private String password;

  private String email;

  private String firstName;

  private String lastName;

  @Builder.Default private Boolean active = true;

  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;

  @Builder.Default List<Role> roles = new ArrayList<>();
}
