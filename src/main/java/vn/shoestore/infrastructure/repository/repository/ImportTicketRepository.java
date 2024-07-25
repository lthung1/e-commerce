package vn.shoestore.infrastructure.repository.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.shoestore.infrastructure.repository.entity.ImportTicketEntity;

@Repository
public interface ImportTicketRepository extends JpaRepository<ImportTicketEntity, Long> {
  @Query(value = """
    select * from import_tickets where id > 0
    """, nativeQuery = true)
  Page<ImportTicketEntity> getAllByConditions(Pageable pageable);
}
