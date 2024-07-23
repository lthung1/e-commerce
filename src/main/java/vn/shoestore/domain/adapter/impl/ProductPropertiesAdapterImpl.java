package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.ProductPropertiesAdapter;
import vn.shoestore.domain.model.ProductProperties;
import vn.shoestore.infrastructure.repository.entity.ProductPropertiesEntity;
import vn.shoestore.infrastructure.repository.repository.ProductPropertiesRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class ProductPropertiesAdapterImpl implements ProductPropertiesAdapter {
  private final ProductPropertiesRepository productPropertiesRepository;

  @Override
  public void saveAll(List<ProductProperties> productProperties) {
    productPropertiesRepository.saveAll(
        ModelMapperUtils.mapList(productProperties, ProductPropertiesEntity.class));
  }

  @Override
  public List<ProductProperties> getAllByProductIdInAndIsAble(
      List<Long> productIds, Boolean isAble) {
    return ModelMapperUtils.mapList(
        productPropertiesRepository.findAllByProductIdInAndIsAble(productIds, isAble),
        ProductProperties.class);
  }

  @Override
  public List<ProductProperties> findAllByProductIdInAndSizeInAndIsAble(
      List<Long> productIds, List<Integer> sizes, Boolean isAble) {
    return ModelMapperUtils.mapList(
        productPropertiesRepository.findAllByProductIdInAndSizeInAndIsAble(
            productIds, sizes, isAble),
        ProductProperties.class);
  }
}
