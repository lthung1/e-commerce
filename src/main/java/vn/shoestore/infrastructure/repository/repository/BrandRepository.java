package vn.shoestore.infrastructure.repository.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.shoestore.infrastructure.repository.entity.BrandEntity;

@Repository
public interface BrandRepository extends JpaRepository<BrandEntity , Long> {}
