package vn.shoestore.infrastructure.repository.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.shoestore.application.request.SearchProductRequest;
import vn.shoestore.infrastructure.repository.entity.ProductEntity;


@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

  @Query(
      value =
          """
                    select p.* from products p
                    JOIN product_brands pb on p.id = pb.product_id
                    JOIN product_categories pc on p.id = pc.product_id
                    WHERE
                        (:#{#request.getName()} is null or p.name like concat('%' , :#{#request.getName()}, '%'))
                        AND (:#{#request.getCode()} is null  or p.code like concat('%' , :#{#request.getCode()} , '%'))
                        AND (:#{#request.getBrands().empty == true} or pb.brand_id in :#{#request.getBrands()})
                        AND (:#{#request.getCategories().empty == true} or pc.category_id in :#{#request.getCategories()})
                        AND (:#{#request.getMinCost()} is null or p.price >= :#{#request.getMinCost()})
                        AND (:#{#request.getMaxCost()} is null or p.price <= :#{#request.getMaxCost()})
                  """,
      nativeQuery = true,
      countProjection = "p.id")
  Page<ProductEntity> findAllByConditions(SearchProductRequest request, Pageable pageable);
}
