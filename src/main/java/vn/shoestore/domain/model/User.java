package vn.shoestore.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
  private Long id;

  private String username;

  private String password;

  private String email;

  private String firstName;

  private String lastName;

  private Boolean active;

  private LocalDateTime createdDate;

  private LocalDateTime updatedDate;
}
