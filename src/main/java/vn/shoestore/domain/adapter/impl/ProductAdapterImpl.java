package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.domain.adapter.ProductAdapter;
import vn.shoestore.domain.model.Product;
import vn.shoestore.infrastructure.repository.repository.ProductRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;

@Adapter
@RequiredArgsConstructor
public class ProductAdapterImpl implements ProductAdapter {
  private final ProductRepository productRepository;

  @Override
  public Page<Product> getProductByCondition(SearchProductRequest request) {
    return ModelMapperUtils.mapPage(
        productRepository.findAllByConditions(
            request, PageRequest.of(request.getPage() - 1 , request.getSize())),
        Product.class);
  }
}
