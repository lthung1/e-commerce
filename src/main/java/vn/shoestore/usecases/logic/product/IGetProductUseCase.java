package vn.shoestore.usecases.logic.product;

import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.application.response.SearchProductResponse;

public interface IGetProductUseCase {
  SearchProductResponse searchProduct(SearchProductRequest request);
}
