package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.CategoryAdapter;
import vn.shoestore.domain.model.Category;
import vn.shoestore.infrastructure.repository.repository.CategoryRepository;
import vn.shoestore.shared.anotation.Adapter;
import vn.shoestore.shared.utils.ModelMapperUtils;

import java.util.List;

@Adapter
@RequiredArgsConstructor
public class CategoryAdapterImpl implements CategoryAdapter {
  private final CategoryRepository categoryRepository;

  @Override
  public List<Category> findAllCategory() {
    return ModelMapperUtils.mapList(categoryRepository.findAll(), Category.class);
  }
}
