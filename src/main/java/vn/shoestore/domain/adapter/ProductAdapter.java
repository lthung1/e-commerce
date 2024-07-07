package vn.shoestore.domain.adapter;

import org.springframework.data.domain.Page;
import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.domain.model.Product;

public interface ProductAdapter {
  Page<Product> getProductByCondition(SearchProductRequest request);
}
