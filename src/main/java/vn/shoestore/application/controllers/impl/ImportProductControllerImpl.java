package vn.shoestore.application.controllers.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import vn.shoestore.application.controllers.IImportProductController;
import vn.shoestore.application.request.ConfirmImportTicketRequest;
import vn.shoestore.application.request.ImportProductRequest;
import vn.shoestore.application.response.BaseResponse;
import vn.shoestore.shared.factory.ResponseFactory;
import vn.shoestore.usecases.logic.import_product.ImportProductUseCase;

@Component
@RequiredArgsConstructor
public class ImportProductControllerImpl implements IImportProductController {
  private final ImportProductUseCase importProductUseCase;

  @Override
  public ResponseEntity<BaseResponse> saveOrUpdateTicket(ImportProductRequest request) {
    importProductUseCase.saveOrUpdateImportTicket(request);
    return ResponseFactory.success();
  }

  @Override
  public ResponseEntity<BaseResponse> confirmImportTicket(ConfirmImportTicketRequest request) {
    importProductUseCase.submitImportTicket(request);
    return ResponseFactory.success();
  }

  @Override
  public ResponseEntity<BaseResponse> deleteTicket(Long id) {
    importProductUseCase.deleteImportTicket(id);
    return ResponseFactory.success();
  }
}
