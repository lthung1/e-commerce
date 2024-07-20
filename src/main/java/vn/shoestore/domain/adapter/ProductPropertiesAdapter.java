package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.ProductProperties;

import java.util.List;

public interface ProductPropertiesAdapter {
  void saveAll(List<ProductProperties> productProperties);

  List<ProductProperties> getAllByProductIdInAndIsAble(List<Long> productIds, Boolean isAble);
}
