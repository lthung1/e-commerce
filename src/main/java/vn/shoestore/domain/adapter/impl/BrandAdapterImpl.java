package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.BrandAdapter;
import vn.shoestore.domain.model.Brand;
import vn.shoestore.infrastructure.repository.entity.BrandEntity;
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

  @Override
  public List<Brand> findAllByIds(List<Long> ids) {
    return ModelMapperUtils.mapList(brandRepository.findAllByIdIn(ids), Brand.class);
  }

  @Override
  public Brand createOrUpdateBrand(Brand brand) {
    return ModelMapperUtils.mapper(
        brandRepository.save(ModelMapperUtils.mapper(brand, BrandEntity.class)), Brand.class);
  }

  @Override
  public void deleteById(Long id) {
    brandRepository.deleteById(id);
  }
}
