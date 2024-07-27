package vn.shoestore.shared.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import vn.shoestore.application.response.ProductResponse;

import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class ProductCartDTO {
  private Long productId;
  private Long productPropertiesId;
  private Integer size;
  private Integer amount;
  private Double price;
  private Double promotionPrice;
  private Float percentDiscount;

  private ProductResponse product;

  public Double getPrice() {
    if (Objects.isNull(product) || Objects.isNull(product.getPrice())) return 0d;
    return (double) (this.amount * this.product.getPrice());
  }

  public Float getPercentDiscount() {
    if (Objects.isNull(product)) return 0f;
    return this.product.getPercentDiscount();
  }

  private Double getPromotionPrice() {
    if (Objects.isNull(product) || Objects.isNull(product.getPromotionPrice())) return null;
    return (double) (this.amount * this.product.getPromotionPrice());
  }
}
