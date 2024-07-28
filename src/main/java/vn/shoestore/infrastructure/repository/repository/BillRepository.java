package vn.shoestore.infrastructure.repository.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.shoestore.infrastructure.repository.entity.BillEntity;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {
  @Query(
      value =
          """
        select * from bills where
            (:userId is null or user_id = :userId)
            order by created_date desc
        """,
      nativeQuery = true)
  Page<BillEntity> findAllByConditions(Long userId, Pageable pageable);
}
