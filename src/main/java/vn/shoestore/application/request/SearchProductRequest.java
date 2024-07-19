package vn.shoestore.application.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class SearchProductRequest {
  private String name;
  private String code;

  @Builder.Default private List<Integer> brands = new ArrayList<>();

  @Builder.Default private List<Integer> categories = new ArrayList<>();

  @Builder.Default private Integer minCost = 0;

  @Builder.Default private Integer maxCost = 0;

  @Builder.Default private Integer page = 1;

  @Builder.Default private Integer size = 10;

  public List<Integer> getBrands() {
    if (brands == null) return Collections.emptyList();
    return brands;
  }

  public List<Integer> getCategories() {
    if (categories == null) return Collections.emptyList();
    return categories;
  }

}
