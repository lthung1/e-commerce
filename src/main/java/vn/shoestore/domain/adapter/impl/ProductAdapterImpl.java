package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.domain.adapter.ProductAdapter;
import vn.shoestore.domain.model.Product;
import vn.shoestore.infrastructure.repository.entity.ProductEntity;
import vn.shoestore.infrastructure.repository.repository.ProductRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;
import vn.shoestore.shared.utils.ModelTransformUtils;

import java.util.Optional;

@Adapter
@RequiredArgsConstructor
public class ProductAdapterImpl implements ProductAdapter {
  private final ProductRepository productRepository;

  @Override
  public Page<Product> getProductByCondition(SearchProductRequest request) {
    return ModelMapperUtils.mapPage(
        productRepository.findAllByConditions(
            request, PageRequest.of(request.getPage() - 1, request.getSize())),
        Product.class);
  }

  @Override
  public Product save(Product product) {
    return ModelMapperUtils.mapper(
        productRepository.save(ModelMapperUtils.mapper(product, ProductEntity.class)),
        Product.class);
  }

  @Override
  public Product getProductById(Long id) {
    Optional<ProductEntity> optionalProductEntity = productRepository.findById(id);
    return optionalProductEntity
        .map(productEntity -> ModelMapperUtils.mapper(productEntity, Product.class))
        .orElse(null);
  }

  @Override
  public void delete(Long id) {
    productRepository.deleteById(id);
  }
}
