package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.BrandAdapter;
import vn.shoestore.domain.model.Brand;
import vn.shoestore.infrastructure.repository.repository.BrandRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class BrandAdapterImpl implements BrandAdapter {

  private final BrandRepository brandRepository;

  @Override
  public List<Brand> findAll() {
    return ModelMapperUtils.mapList(brandRepository.findAll(), Brand.class);
  }
}
