package vn.shoestore.domain.adapter;

import vn.shoestore.domain.model.ImportTicket;
import vn.shoestore.domain.model.ImportTicketProduct;

import java.util.List;
import java.util.Optional;

public interface ImportTicketAdapter {
  ImportTicket saveImportTicket(ImportTicket importTicket);

  void saveAllImportTicketProduct(List<ImportTicketProduct> importTicketProducts);

  List<ImportTicketProduct> findAllByTicketIdIn(List<Long> ticketIds);

  void deleteImportTicketProductByIds(List<Long> ids);

  ImportTicket getTicketById(Long id);
}
