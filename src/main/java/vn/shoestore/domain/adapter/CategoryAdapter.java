package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.Category;

import java.util.List;

public interface CategoryAdapter {
  List<Category> findAllCategory();
}
