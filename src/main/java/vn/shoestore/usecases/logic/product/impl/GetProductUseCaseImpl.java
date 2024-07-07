package vn.shoestore.usecases.logic.product.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.application.response.SearchProductResponse;
import vn.shoestore.domain.adapter.ProductAdapter;
import vn.shoestore.domain.model.Product;
import vn.shoestore.shared.anotation.UseCase;
import vn.shoestore.usecases.logic.product.IGetProductUseCase;

@UseCase
@RequiredArgsConstructor
public class GetProductUseCaseImpl implements IGetProductUseCase {
  private final ProductAdapter productAdapter;
  @Override
  public SearchProductResponse searchProduct(SearchProductRequest request) {
    Page<Product> productPage = productAdapter.getProductByCondition(request);
    if (productPage.isEmpty()) return SearchProductResponse.builder().build();
    return SearchProductResponse.builder()
        .data(productPage.getContent())
        .total(productPage.getTotalElements())
        .build();
  }
}
