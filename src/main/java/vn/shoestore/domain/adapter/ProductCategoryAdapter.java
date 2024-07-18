package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.ProductCategory;

import java.util.List;

public interface ProductCategoryAdapter {
  List<ProductCategory> findAllByProductIds(List<Long> productIds);

  void saveAll(List<ProductCategory> productAttachments);

  void deleteByIds(List<Long> ids);
}
