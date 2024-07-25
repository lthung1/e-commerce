package vn.shoestore.infrastructure.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shoestore.infrastructure.repository.entity.ProductPropertiesEntity;

import java.util.List;

@Repository
public interface ProductPropertiesRepository extends JpaRepository<ProductPropertiesEntity, Long> {
  List<ProductPropertiesEntity> findAllByProductIdInAndIsAble(
      List<Long> productIds, Boolean isAble);

  List<ProductPropertiesEntity> findAllByProductIdInAndSizeInAndIsAble(
      List<Long> productIds, List<Integer> sizes, Boolean isAble);

  List<ProductPropertiesEntity> findAllByIdIn(List<Long> ids);
}
