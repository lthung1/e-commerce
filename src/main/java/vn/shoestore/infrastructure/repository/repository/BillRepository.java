package vn.shoestore.infrastructure.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shoestore.infrastructure.repository.entity.BillEntity;

@Repository
public interface BillRepository extends JpaRepository<BillEntity, Long> {}
