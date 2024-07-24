package vn.shoestore.usecases.logic.import_product;

import vn.shoestore.application.request.ConfirmImportTicketRequest;
import vn.shoestore.application.request.ImportProductRequest;

public interface ImportProductUseCase {
  void saveOrUpdateImportTicket(ImportProductRequest request);

  void submitImportTicket(ConfirmImportTicketRequest request);

  void deleteImportTicket(Long importTicket);
}
