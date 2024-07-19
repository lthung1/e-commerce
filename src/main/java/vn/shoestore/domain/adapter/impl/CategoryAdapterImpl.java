package vn.shoestore.domain.adapter.impl;

import lombok.RequiredArgsConstructor;
import vn.shoestore.domain.adapter.CategoryAdapter;
import vn.shoestore.domain.model.Category;
import vn.shoestore.infrastructure.repository.entity.CategoryEntity;
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

  @Override
  public List<Category> findAllByIdIn(List<Long> ids) {
    return ModelMapperUtils.mapList(categoryRepository.findAllByIdIn(ids), Category.class);
  }

  @Override
  public Category createOrUpdateCategory(Category category) {
    return ModelMapperUtils.mapper(
        categoryRepository.save(ModelMapperUtils.mapper(category, CategoryEntity.class)),
        Category.class);
  }

  @Override
  public void deleteById(Long id) {
    categoryRepository.deleteById(id);
  }
}
